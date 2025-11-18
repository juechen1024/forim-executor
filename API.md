# API 文档（cn.gov.forestry.executor.server）

## 概述
- 服务说明：本服务对外暴露对调度器与执行器的控制与调用接口，以及对内部 `client` 的通用 RPC 风格调用。
- 默认监听：`0.0.0.0:8080`（通过 `python -m cn.gov.forestry.executor.server` 启动，需设置 `PYTHONPATH="$PWD/src/main/python"`）。
- 配置来源：使用 `.env` 或环境变量（例如 `FORIM_INNER_SCHEDULE_URL` 等）配置后端服务地址，详情见 `executor_application.py`。
- 认证：当前无认证（非生产安全）。建议在外网暴露前加入 API key 或部署到受限网络 / 反向代理 后。

## 通用约定
- 请求与响应使用 JSON。
- 常见返回字段：
  - 成功：HTTP 200 / 202 + JSON，例如 `{"status":"ok"}` 或 `{"result": ...}`。
  - 错误：HTTP 4xx/5xx + `{"error": "error message"}`。
- 异步行为：带后台执行的接口会立即返回 202 Accepted，并在后台线程中运行任务；需通过 `/status/bg_threads` 或后端 schedule 服务查询具体任务状态。

---

## 接口列表

### 1) 健康检查
- 路径：`GET /health`
- 描述：健康检测
- 请求：无
- 响应示例：
  - `200 OK`，Body: `{"status":"ok"}`

### 2) 列出可用 client
- 路径：`GET /clients`
- 描述：返回当前构建的内置 clients 列表（key 名称）
- 响应示例：
  - `200 OK`，Body: `{"clients": ["general","assets","db","metadata_q","metadata_o","schedule"]}`

### 3) 查询 client 的方法列表
- 路径：`GET /clients/{client_name}`
- 描述：返回指定 client 的可调用方法名数组
- 参数：`client_name`（路径参数）
- 响应示例：
  - `200 OK`，Body: `{"client":"schedule","methods":["create_asynchronous_job","update_asynchronous_job","get_schedule_jobs","create_schedule_job_log"]}`

### 4) Inspect client 方法签名
- 路径：`GET /clients/{client_name}/{method}/inspect`
- 描述：返回 Python 函数签名（便于构造调用参数）
- 响应示例：
  - `200 OK`，Body: `{"signature": "(dto)"}`

### 5) 调用 client 方法（RPC 风格）
- 路径：`POST /clients/{client_name}/{method}`
- 描述：通用 RPC 接口，调用指定 client 的方法
- 请求体格式（任选其一）：
  - 单参数（最常见）：直接传入 JSON，本服务会把它作为第一个位置参数传给方法
  - 显式参数：`{"args": [...], "kwargs": {...}}`
- 成功响应：`200 OK`，Body: `{"result": <方法返回值（JSON 可序列化）>}`
- 错误响应：`404`（未知 client/method），`500`（方法调用异常）
- 注意：许多 client 方法接受 DTO 对象或 dict；直接传 dict 通常被客户端方法接受。

示例：
```
curl -X POST http://localhost:8080/clients/schedule/get_schedule_jobs -H "Content-Type: application/json" -d '{ "systemId": "sys1" }'
```

### 6) 触发调度检查（一次性）
- 路径：`POST /scheduler/check` 或 `GET /scheduler/check`
- 描述：后台执行一次 `JobScheduler.check_job_status()`，立即返回
- 返回：`202 Accepted`，Body: `{"status":"scheduled"}`

### 7) 启动简单周期调度（后台线程）
- 路径：`POST /scheduler/run`
- 描述：使用内置简易定时器在后台循环执行检查
- 请求 Body（可选）：`{"interval": 60}`（秒，默认60）
- 返回：`202 Accepted`，Body: `{"status":"started","interval":60}`

### 8) 启动 APScheduler（后台）
- 路径：`POST /scheduler/run_apscheduler`
- 描述：在后台启动 APScheduler（需安装 `apscheduler`）
- 请求 Body（可选）：`{"interval": 60}`
- 返回：`202 Accepted`，Body: `{"status":"apscheduler_started","interval":60}`

### 9) 异步触发执行任务（非阻塞）
- 路径：`POST /executor/execute`
- 描述：接受 `ScheduleJobDTO` JSON，异步在后台执行 `executor.execute(dto)`
- 请求 Body：`ScheduleJobDTO` 对应的 JSON，例如：
  ```json
  { "id":"job-123", "systemId":"sys1", "jobType":"excel", "jobParams": {"systemId":"sys1","tableId":"t1","resourceFile": {"id":"file1"}} }
  ```
- 返回：`202 Accepted`，Body: `{"status":"accepted"}`

### 10) 同步执行任务（阻塞）
- 路径：`POST /executor/execute_sync`
- 描述：同步调用 `executor.execute`，阻塞直到完成或出错
- 返回示例：
  - 成功：`200 OK`，Body: `{"status":"done"}`
  - 失败：`500`，Body: `{"error":"..."}`

### 11) 后台线程状态
- 路径：`GET /status/bg_threads`
- 描述：返回服务内记录的后台线程是否存活（布尔映射）
- 返回示例：`{"last_scheduler_check": true, "simple_scheduler": false, "last_execute": false}`

---

## 错误处理与返回约定
- `400/404`：参数错误或找不到 client/method
- `500`：服务侧异常（会在 server 日志中记录堆栈）
- 异步接口（返回 `202`）不携带业务执行结果；业务结果请依赖 schedule 后端或扩展本地状态存储并实现查询接口。

## 安全建议（强烈建议）
- 在生产环境至少添加：
  - API Key（如 `X-API-KEY`），由 `.env` 或环境变量提供
  - 反向代理 + 认证/访问控制
  - IP 白名单或内网访问控制
- RPC 接口风险：允许任意 client 方法调用存在安全风险，建议为可调方法做白名单或添加权限控制。

## 可选扩展（可实现）
- 添加 `/executor/status/{jobId}`：在 `ScheduleJobExecutor` 中记录执行状态到内存或 Redis，并提供查询接口。
- 添加停止调度的 API（stop APScheduler / stop simple scheduler）。
- 将 HTTP 层迁移到 `FastAPI`，并生成 OpenAPI/Swagger 文档（`/docs`）。

## 运行示例（PowerShell）
- 启动服务：
```powershell
$env:PYTHONPATH = "$PWD\src\main\python"
python -m cn.gov.forestry.executor.server
```
- 健康检查：
```powershell
curl http://localhost:8080/health
```
- 异步执行任务：
```powershell
curl -X POST http://localhost:8080/executor/execute -H "Content-Type: application/json" -d '{ "id":"job-123", "systemId":"sys1", "jobType":"excel", "jobParams": {"systemId":"sys1","tableId":"t1", "resourceFile": {"id":"file1"}} }'
```
