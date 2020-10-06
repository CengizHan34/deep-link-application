package com.example.trendyol.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Getter
@ToString
@ApiModel("URL request class")
@NoArgsConstructor
@AllArgsConstructor
public class LinkRequestModel {
    @NotEmpty(message = "URL can not be empty!")
    @ApiModelProperty(notes = "Url cannot be empty")
    private String url;
}
