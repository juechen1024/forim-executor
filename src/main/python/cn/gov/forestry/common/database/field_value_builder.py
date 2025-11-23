from __future__ import annotations

from datetime import datetime
from typing import Any, Dict, Iterable
from uuid import uuid1

from cn.gov.forestry.common.database.field_value import FieldValue
from cn.gov.forestry.common.domain.bo.system_field_data_type_enum import SystemFieldDataTypeEnum


class FieldValueBuilder:
    @staticmethod
    def string(value: Any) -> FieldValue:
        return FieldValue(dataTypeCode='string', value=str(value) if value is not None else None)

    @staticmethod
    def integer(value: Any) -> FieldValue:
        try:
            converted = None if value is None else int(value)
        except (TypeError, ValueError):
            converted = None
        return FieldValue(dataTypeCode='integer', value=converted)

    @staticmethod
    def double(value: Any) -> FieldValue:
        try:
            converted = None if value is None else float(value)
        except (TypeError, ValueError):
            converted = None
        return FieldValue(dataTypeCode='double', value=converted)

    @staticmethod
    def boolean(value: Any) -> FieldValue:
        if value is None:
            converted = None
        elif isinstance(value, bool):
            converted = value
        elif isinstance(value, str):
            converted = value.strip().lower() in {'true', '1', 'yes', 'y'}
        elif isinstance(value, (int, float)):
            converted = bool(value)
        else:
            converted = None
        return FieldValue(dataTypeCode='boolean', value=converted)

    @staticmethod
    def date(value: Any) -> FieldValue:
        if value is None:
            return FieldValue(dataTypeCode='date', value=None)
        if isinstance(value, datetime):
            return FieldValue(dataTypeCode='date', value=value.isoformat())
        if isinstance(value, str):
            for fmt in ("%Y-%m-%dT%H:%M:%S", "%Y-%m-%d %H:%M:%S", "%Y-%m-%d", "%Y/%m/%d"):
                try:
                    dt = datetime.strptime(value, fmt)
                    return FieldValue(dataTypeCode='date', value=dt.isoformat())
                except ValueError:
                    continue
            try:
                dt = datetime.fromisoformat(value)
                return FieldValue(dataTypeCode='date', value=dt.isoformat())
            except ValueError:
                return FieldValue(dataTypeCode='date', value=value)
        return FieldValue(dataTypeCode='date', value=str(value))

    @staticmethod
    def object(value: Any) -> FieldValue:
        return FieldValue(dataTypeCode='object', value=value)

    @staticmethod
    def array(value: Any) -> FieldValue:
        if isinstance(value, Iterable) and not isinstance(value, (str, bytes)):
            return FieldValue(dataTypeCode='array', value=list(value))
        return FieldValue(dataTypeCode='array', value=value)

    @staticmethod
    def createFieldValue(data_type: Any, value: Any) -> FieldValue:
        if isinstance(data_type, SystemFieldDataTypeEnum):
            enum_value = data_type
        elif isinstance(data_type, str):
            enum_value = SystemFieldDataTypeEnum.getByCodeOrDefault(data_type)
        else:
            enum_value = SystemFieldDataTypeEnum.STRING

        if enum_value is SystemFieldDataTypeEnum.INTEGER:
            return FieldValueBuilder.integer(value)
        if enum_value is SystemFieldDataTypeEnum.DOUBLE:
            return FieldValueBuilder.double(value)
        if enum_value is SystemFieldDataTypeEnum.BOOLEAN:
            return FieldValueBuilder.boolean(value)
        if enum_value is SystemFieldDataTypeEnum.DATE:
            return FieldValueBuilder.date(value)
        if enum_value is SystemFieldDataTypeEnum.OBJECT:
            return FieldValueBuilder.object(value)
        if enum_value is SystemFieldDataTypeEnum.ARRAY:
            return FieldValueBuilder.array(value)
        return FieldValueBuilder.string(value)

    @staticmethod
    def convertObjectMap(values: Dict[str, Any]) -> Dict[str, FieldValue]:
        return {key: FieldValueBuilder.object(value) for key, value in (values or {}).items()}

    @staticmethod
    def generateUuidField() -> FieldValue:
        return FieldValueBuilder.string(str(uuid1()))


__all__ = ["FieldValueBuilder"]
