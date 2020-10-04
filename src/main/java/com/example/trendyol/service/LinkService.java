package com.example.trendyol.service;

import com.example.trendyol.dto.LinkRequestModel;
import com.example.trendyol.dto.LinkResponseModel;

/**
 * @author created by cengizhan on 3.10.2020
 */
public interface LinkService {
    /**
     * This method converts web link to deep link.
     * @param linkRequestModel  contains web link url
     * @return deeplink url
     */
    LinkResponseModel convertToDeeplink(LinkRequestModel linkRequestModel);
}
