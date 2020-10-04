package com.example.trendyol.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LinkRequestModel {
    @NotNull(message = "URL can not be null!")
    private String url;
}
