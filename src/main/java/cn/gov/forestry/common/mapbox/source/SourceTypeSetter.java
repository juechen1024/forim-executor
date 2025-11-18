package cn.gov.forestry.common.mapbox.source;

public interface SourceTypeSetter {
    SourceAfterTypeSetter type(String type);
    SourceAfterTypeSetter geojsonSource();
    SourceAfterTypeSetter imageSource();
    SourceAfterTypeSetter modelSource();
    SourceAfterTypeSetter batchedModelSource();
    SourceAfterTypeSetter rasterArraySource();
    SourceAfterTypeSetter rasterDEMSource();
    SourceAfterTypeSetter rasterSource();
    SourceAfterTypeSetter vectorSource();
    SourceAfterTypeSetter videoSource();
    SourceAfterTypeSetter unknownSource();
}
