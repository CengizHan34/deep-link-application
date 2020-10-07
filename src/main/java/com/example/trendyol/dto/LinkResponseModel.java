package com.example.trendyol.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Data
@Builder
@ApiModel("URL response class")
public class LinkResponseModel {
    @ApiModelProperty(notes = "the converted url is reverted")
    private String url;
}
