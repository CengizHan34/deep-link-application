package com.example.trendyol.controller;


import com.example.trendyol.dto.LinkRequestModel;
import com.example.trendyol.service.LinkService;
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
public class LinkController {
    private static Logger logger = LoggerFactory.getLogger(LinkController.class);
    private LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity convertToDeeplink(@Valid @RequestBody LinkRequestModel linkRequestModel) {
        logger.info("LinkController :: convertToDeeplink :: URL > {}", linkRequestModel.getUrl());
        return ResponseEntity.status(HttpStatus.OK).body(linkService.convertToDeeplink(linkRequestModel));
    }
}
