package cn.gov.forestry.common.mapbox.layer;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayerSpecification {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("source")
    private String source;

    @JsonProperty("source-layer")
    private String sourceLayer;

    @JsonProperty("slot")
    private String slot;

    @JsonProperty("minzoom")
    private Integer minZoom;

    @JsonProperty("maxzoom")
    private Integer maxZoom;

    @JsonProperty("filter")
    private Map<String, Object> filter;

    @JsonProperty("layout")
    private Map<String, Object> layout;

    @JsonProperty("paint")
    private Map<String, Object> paint;

    @JsonProperty("additional-properties")
    private Map<String, Object> additionalProperties;
}