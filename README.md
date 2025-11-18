# forim-executor — Python 移植说明

这个仓库包含从 Java 服务（`cn.gov.forestry`）移植为 Python 版的执行器代码（仅为轻量移植，保留了主要 DTO、客户端与调度/执行逻辑）。

**目的**
- 将主要 Java 源（调度/执行、内网客户端、DTO）移植为可运行的 Python 模块，方便在 Python 环境中集成或做二次开发。

**目录结构（重要部分）**
- `src/main/python/`：Python 源代码，模仿 Java 包结构（以 `cn.gov.forestry.*` 导入）。
- `requirements.txt`：建议依赖（包含可选 GIS / Excel 依赖）。
- `tools/check_imports.py`：检测本地包导入与可选依赖的脚本。

快速开始（Windows PowerShell）

1. 安装 Python 依赖（建议使用虚拟环境或 Conda）：

```powershell
python -m pip install -r requirements.txt
```

2. 设置 `PYTHONPATH` 以便直接从源码运行（项目根目录执行）：

```powershell
$env:PYTHONPATH = 'src/main/python'
```

3. 运行导入/依赖自检脚本（不会启动调度器）：

```powershell
python tools/check_imports.py
```

4. 直接导入入口模块进行干运行验证（不会启动定时任务）:

```powershell
python -c "import cn.gov.forestry.executor.executor_application as m; print('imported', m.__name__)"
```

5. 使用简单定时器运行（用于本地测试，会在后台以短定时调用 JobScheduler）:

```powershell
python -c "from cn.gov.forestry.executor.executor_application import run; run(60)"
```

6. 使用 APScheduler（生产建议）：
- 安装：`pip install apscheduler`
- 运行（会阻塞主线程）：

```

## API 文档（Swagger / 自动生成）

项目已通过 `flask_restx` 暴露自动生成的 Swagger UI 文档，启动服务后可在浏览器中交互式调用接口。

1. 启动 Flask-RESTX 服务：

```powershell
$env:PYTHONPATH = 'src/main/python'
python -m cn.gov.forestry.executor.server
```

2. 打开 Swagger UI：

在浏览器中访问 `http://localhost:8080/docs`，Swagger UI 将列出所有 API（命名空间 `/api` 下），可以直接在页面上填写参数并发送请求。

3. OpenAPI JSON：

可获取原始 OpenAPI 文档（JSON）：`http://localhost:8080/openapi.json`，便于生成客户端或与其他工具集成。

4. 示例：

- 在 Swagger UI 中，找到 `api` 命名空间下的 `POST /api/executor/execute`，填入 `ScheduleJobDTO` 的 JSON，点击 `Execute` 来触发异步执行。
- 也可以通过命令行：

```powershell
curl -X POST http://localhost:8080/api/executor/execute -H "Content-Type: application/json" -d '{ "id":"job-123", "systemId":"sys1", "jobType":"excel", "jobParams": {"systemId":"sys1","tableId":"t1"} }'
```

注意：默认不含认证，建议在生产环境前启用 API Key 或放在受控网络后。
```powershell
python -c "from cn.gov.forestry.executor.executor_application import run_with_apscheduler; run_with_apscheduler(60)"
```

可选/第三方依赖说明
- Excel 支持: `openpyxl`
- Shapefile 支持: `pyshp`, `shapely`
- GeoTIFF 支持: `rasterio`, `affine`
- Scheduler: `apscheduler`

这些依赖在 `requirements.txt` 中列为可选（按需安装）。代码对这些库做了懒加载并在缺失时抛出明确错误。

如何在开发时运行并调试
- 把 `src/main/python` 加入 `PYTHONPATH`（见上文），然后在 IDE 中以普通 Python 模块方式运行/调试 `cn.gov.forestry.executor.executor_application`。
- 若你修改了包结构，请确保对应的 `__init__.py` 存在（仓库已经为当前文件结构添加了这些文件）。

测试与未来工作（建议）
- 为 DTO 的 `to_dict`/`from_dict` 添加单元测试（`pytest`）。
- 为客户端（`requests`）添加基于 `requests-mock` 或 `responses` 的模拟测试。
- 添加 GitHub Actions 工作流在 PR 上跑导入检查与 `pytest`。

常见命令汇总（PowerShell）

```powershell
# 安装依赖
python -m pip install -r requirements.txt

# 设置 PYTHONPATH（临时，当前 shell 会话有效）
$env:PYTHONPATH = 'src/main/python'

# 运行 import/依赖检查
python tools/check_imports.py

# 干导入验证
python -c "import cn.gov.forestry.executor.executor_application as m; print('imported', m.__name__)"

# 本地快速运行（简单定时器）
python -c "from cn.gov.forestry.executor.executor_application import run; run(60)"

# 使用 APScheduler
python -c "from cn.gov.forestry.executor.executor_application import run_with_apscheduler; run_with_apscheduler(60)"
```

