package com.example.trendyol.controller;


import com.example.trendyol.dto.LinkRequestModel;
import com.example.trendyol.dto.LinkResponseModel;
import com.example.trendyol.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author created by cengizhan on 3.10.2020
 */

@RestController
@RequestMapping("/api/link")
@Api(value = "Link Api documentation")
public class LinkController {
    private static Logger logger = LoggerFactory.getLogger(LinkController.class);

    private LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/deeplink")
    @ApiOperation("This method converts web links to deep links")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = LinkResponseModel.class)})
    public ResponseEntity convertToDeeplink(@Valid @RequestBody LinkRequestModel linkRequestModel) {
        logger.info("LinkController :: convertToDeeplink :: URL > {}", linkRequestModel.getUrl());
        return ResponseEntity.status(HttpStatus.OK).body(linkService.convertToDeeplink(linkRequestModel));
    }

    @PostMapping("/weblink")
    @ApiOperation("This method converts deep links to web links")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = LinkResponseModel.class)})
    public ResponseEntity convertToWebLink(@Valid @RequestBody LinkRequestModel linkRequestModel) {
        logger.info("LinkController :: convertToWebLink :: URL > {}", linkRequestModel.getUrl());
        return ResponseEntity.status(HttpStatus.OK).body(linkService.convertToWebLink(linkRequestModel));
    }


}
