import importlib
import sys

modules = [
    ('apscheduler.schedulers.background', 'apscheduler'),
    ('requests', 'requests'),
    ('openpyxl', 'openpyxl'),
    ('shapefile', 'pyshp'),
    ('shapely', 'shapely'),
    ('rasterio', 'rasterio'),
    ('affine', 'affine'),
]

print('Checking optional external modules:')
for mod_name, pkg in modules:
    try:
        importlib.import_module(mod_name)
        print(f'  OK: {pkg} (import {mod_name})')
    except Exception as e:
        print(f'  MISSING: {pkg} -> {e.__class__.__name__}: {e}')

print('\nChecking local package imports:')
# Add src/main/python to sys.path if not already
import os
root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
src_py = os.path.join(root, 'src', 'main', 'python')
if src_py not in sys.path:
    sys.path.insert(0, src_py)

local_imports = [
    'cn.gov.forestry.executor.executor_application',
    'cn.gov.forestry.executor.job.schedule_job_executor',
    'cn.gov.forestry.executor.client.general_inner_query_client',
    'cn.gov.forestry.common.file.file_content',
]

for name in local_imports:
    try:
        importlib.import_module(name)
        print(f'  OK: {name}')
    except Exception as e:
        print(f'  FAIL: {name} -> {e.__class__.__name__}: {e}')
