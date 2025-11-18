from flask import request
from flask_restx import Api, Resource, fields
import threading
import logging
import inspect
from typing import Any, List, get_origin, get_args

from cn.gov.forestry.executor.executor_application import create_executor_and_scheduler, build_clients
from cn.gov.forestry.common.domain.dto.schedule.schedule_job_dto import ScheduleJobDTO

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


# Create Flask-RESTX Api when imported or run
def create_app():
    from flask import Flask
    app = Flask(__name__)
    api = Api(app, version='1.0', title='Forim Executor API', description='控制调度器与执行器的 HTTP 接口', doc='/docs')

    # Build clients, executor and scheduler at startup
    clients = build_clients()
    executor, scheduler = create_executor_and_scheduler(clients=clients)

    ns = api.namespace('api', description='Executor API')

    # Models
    schedule_job_model = api.model('ScheduleJobDTO', {
        'id': fields.String(required=False),
        'systemId': fields.String(required=False),
        'jobType': fields.String(required=False),
        'jobParams': fields.Raw(required=False)
    })

    rpc_model = api.model('RPC', {
        'args': fields.List(fields.Raw, required=False),
        'kwargs': fields.Raw(required=False)
    })

    # Track background threads / tasks
    _bg_threads = {}

    def _list_client_methods(client_obj: Any):
        return [m for m in dir(client_obj) if callable(getattr(client_obj, m)) and not m.startswith('_')]

    @ns.route('/health')
    class Health(Resource):
        def get(self):
            return {'status': 'ok'}, 200

    @ns.route('/clients')
    class ClientsList(Resource):
        def get(self):
            return {'clients': list(clients.keys())}, 200

    @ns.route('/clients/<string:client_name>')
    class ClientInfo(Resource):
        def get(self, client_name):
            c = clients.get(client_name)
            if not c:
                api.abort(404, 'unknown client')
            return {'client': client_name, 'methods': _list_client_methods(c)}, 200

    @ns.route('/clients/<string:client_name>/<string:method>')
    class ClientCall(Resource):
        @api.expect(rpc_model, validate=False)
        def post(self, client_name, method):
            c = clients.get(client_name)
            if not c:
                api.abort(404, 'unknown client')
            if not hasattr(c, method):
                api.abort(404, 'unknown method')
            payload = request.get_json()
            args = []
            kwargs = {}
            if isinstance(payload, dict) and ('args' in payload or 'kwargs' in payload):
                args = payload.get('args', []) or []
                kwargs = payload.get('kwargs', {}) or {}
            else:
                if payload is not None:
                    args = [payload]
            func = getattr(c, method)
            # try to coerce dict payloads into DTOs based on function annotations
            def _convert_value(val, expected_type):
                if expected_type is inspect._empty:
                    return val
                origin = get_origin(expected_type)
                try:
                    # handle Optional / Union by taking first arg that is not NoneType
                    if origin is None:
                        # simple case: expected_type is a class with from_dict
                        if isinstance(val, dict) and hasattr(expected_type, 'from_dict'):
                            return expected_type.from_dict(val)
                        # list/dict mismatch, return as-is
                        return val
                    if origin in (list, List):
                        inner = get_args(expected_type)[0] if get_args(expected_type) else None
                        if inner and hasattr(inner, 'from_dict') and isinstance(val, list):
                            return [inner.from_dict(v) if isinstance(v, dict) else v for v in val]
                        return val
                    return val
                except Exception:
                    return val

            try:
                sig = inspect.signature(func)
                bound = sig.bind_partial(*args, **kwargs)
                for name, value in list(bound.arguments.items()):
                    param = sig.parameters.get(name)
                    if param:
                        converted = _convert_value(value, param.annotation)
                        bound.arguments[name] = converted
                # call with bound arguments to preserve correct mapping
                res = func(*bound.args, **bound.kwargs)
                def _serialize(obj):
                    # DTO/dataclass -> dict, FieldValue -> to_dict, lists/dicts recurse
                    try:
                        if obj is None:
                            return None
                        # if has to_dict method
                        if hasattr(obj, 'to_dict') and callable(getattr(obj, 'to_dict')):
                            return obj.to_dict()
                        # dataclass-like: try asdict
                        from dataclasses import is_dataclass, asdict
                        if is_dataclass(obj):
                            return asdict(obj)
                    except Exception:
                        pass
                    # lists
                    if isinstance(obj, list):
                        return [ _serialize(o) for o in obj ]
                    if isinstance(obj, dict):
                        return { k: _serialize(v) for k, v in obj.items() }
                    # fallback to primitive
                    return obj

                return {'result': _serialize(res)}, 200
            except TypeError:
                # fallback: call as before
                try:
                    res = func(*args, **kwargs)
                    return {'result': res}, 200
                except Exception as e:
                    logging.exception('client call failed')
                    api.abort(500, str(e))
            except Exception as e:
                logging.exception('client call failed')
                api.abort(500, str(e))

    @ns.route('/clients/<string:client_name>/<string:method>/inspect')
    class ClientInspect(Resource):
        def get(self, client_name, method):
            c = clients.get(client_name)
            if not c or not hasattr(c, method):
                api.abort(404, 'unknown client or method')
            sig = str(inspect.signature(getattr(c, method)))
            return {'signature': sig}, 200

    @ns.route('/scheduler/check')
    class SchedulerCheck(Resource):
        def post(self):
            t = threading.Thread(target=scheduler.check_job_status, daemon=True)
            t.start()
            _bg_threads['last_scheduler_check'] = t
            return {'status': 'scheduled'}, 202
        def get(self):
            return self.post()

    @ns.route('/scheduler/run')
    class SchedulerRun(Resource):
        @api.expect(api.model('RunParams', {'interval': fields.Integer(required=False)}), validate=False)
        def post(self):
            body = request.get_json() or {}
            interval = int(body.get('interval', 60))
            def _run():
                from cn.gov.forestry.executor.executor_application import schedule_periodic_check
                schedule_periodic_check(scheduler, interval)
            t = threading.Thread(target=_run, daemon=True)
            t.start()
            _bg_threads['simple_scheduler'] = t
            return {'status': 'started', 'interval': interval}, 202

    @ns.route('/scheduler/run_apscheduler')
    class SchedulerRunAP(Resource):
        @api.expect(api.model('RunParamsAps', {'interval': fields.Integer(required=False)}), validate=False)
        def post(self):
            body = request.get_json() or {}
            interval = int(body.get('interval', 60))
            def _run_aps():
                try:
                    from cn.gov.forestry.executor.executor_application import run_with_apscheduler
                    run_with_apscheduler(interval)
                except Exception:
                    logging.exception('apscheduler failed')
            t = threading.Thread(target=_run_aps, daemon=True)
            t.start()
            _bg_threads['apscheduler'] = t
            return {'status': 'apscheduler_started', 'interval': interval}, 202

    @ns.route('/executor/execute')
    class ExecutorExecute(Resource):
        @api.expect(schedule_job_model, validate=False)
        def post(self):
            data = request.get_json() or {}
            try:
                dto = ScheduleJobDTO.from_dict(data) if hasattr(ScheduleJobDTO, 'from_dict') else ScheduleJobDTO()
            except Exception:
                dto = ScheduleJobDTO()
                for k, v in (data or {}).items():
                    setattr(dto, k, v)
            t = threading.Thread(target=lambda: executor.execute(dto), daemon=True)
            t.start()
            _bg_threads['last_execute'] = t
            return {'status': 'accepted'}, 202

    @ns.route('/executor/execute_sync')
    class ExecutorExecuteSync(Resource):
        @api.expect(schedule_job_model, validate=False)
        def post(self):
            data = request.get_json() or {}
            try:
                dto = ScheduleJobDTO.from_dict(data) if hasattr(ScheduleJobDTO, 'from_dict') else ScheduleJobDTO()
            except Exception:
                dto = ScheduleJobDTO()
                for k, v in (data or {}).items():
                    setattr(dto, k, v)
            try:
                executor.execute(dto)
                return {'status': 'done'}, 200
            except Exception as e:
                logging.exception('executor execute_sync failed')
                api.abort(500, str(e))

    @ns.route('/status/bg_threads')
    class StatusBg(Resource):
        def get(self):
            info = {k: (t.is_alive() if hasattr(t, 'is_alive') else False) for k, t in _bg_threads.items()}
            return info, 200

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(host='0.0.0.0', port=8080)
