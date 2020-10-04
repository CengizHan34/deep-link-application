package com.example.trendyol.service.impl;

import com.example.trendyol.dto.LinkRequestModel;
import com.example.trendyol.dto.LinkResponseModel;
import com.example.trendyol.enums.ProcessType;
import com.example.trendyol.exception.CheckException;
import com.example.trendyol.model.Link;
import com.example.trendyol.repository.LinkRepository;
import com.example.trendyol.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Service
public class LinkServiceImpl implements LinkService {
    private static Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);
    private static String HOST = "www.trendyol.com";
    private static String PREFIX_DEEPLINK = "ty://?Page=";
    private static String PRODUCT = "-p-";
    private static String PRODUCT_PATTERN = "-p-(\\d*).?(\\w*\\W\\d*)?\\W?\\w*\\W?(\\w*\\W\\d*)?";
    private static String SEARCH = "q=";
    private static String SEARCH_PATTERN = "q=(.+)";


    private LinkRepository linkRepository;

    public LinkServiceImpl(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public LinkResponseModel convertToDeeplink(LinkRequestModel linkRequestModel) {
        String deepLink = "";
        logger.info("LinkServiceImpl :: convertToDeeplink :: URL > {}", linkRequestModel.getUrl());
        String link = "";
        try {
            URL url = new URL(linkRequestModel.getUrl());
            checkHost(url.getHost());
            link = url.getFile();
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            throw new CheckException(e.getMessage());
        }

        deepLink = createDeepLink(link);
        LinkResponseModel linkResponseModel = LinkResponseModel.builder().url(deepLink).build();
        logger.info("LinkServiceImpl :: convertToDeeplink :: DEEPLINK > {}", linkResponseModel.getUrl());

        Link linkModel = Link.builder().id(UUID.randomUUID().toString()).webLink(linkRequestModel.getUrl())
                .deepLink(deepLink).processType(ProcessType.CONVERT_TO_DEEPLINK.name())
                .build();

        linkRepository.save(linkModel);
        logger.info("link model saved successfully. ID : {}", linkModel.getId());
        return linkResponseModel;
    }

    private void checkHost(String host) {
        if (!HOST.equalsIgnoreCase(host)) {
            throw new CheckException("bad hostname!");
        }
    }

    private String createDeepLink(String link) {
        return chooseDeeplinkType(link);
    }

    private String chooseDeeplinkType(String link) {
        String deepLink = "";
        if (link.contains(PRODUCT)) {
            deepLink = createProductDeepLink(link);
        } else if (link.contains(SEARCH)) {
            deepLink = createSearchDeepLink(link);
        } else {
            deepLink = createPageDeepLink();
        }
        return deepLink;
    }

    private String createPageDeepLink() {
        StringBuilder linkBuilder = new StringBuilder(PREFIX_DEEPLINK).append("Home");
        logger.info("createPageDeepLink :: created page deeplink : {}", linkBuilder.toString());
        return linkBuilder.toString();
    }

    private String createSearchDeepLink(String link) {
        StringBuilder linkBuilder = new StringBuilder(PREFIX_DEEPLINK).append("Search").append("&amp;");
        Pattern pattern = Pattern.compile(SEARCH_PATTERN);
        Matcher matcher = pattern.matcher(link);
        while (matcher.find()) {
            linkBuilder.append("Query=").append(matcher.group(1));
        }
        logger.info("createSearchDeepLink :: created search deeplink : {}", linkBuilder.toString());
        return linkBuilder.toString();
    }

    private String createProductDeepLink(String link) {
        StringBuilder linkBuilder = new StringBuilder(PREFIX_DEEPLINK).append("Product").append("&amp;");
        Pattern pattern = Pattern.compile(PRODUCT_PATTERN);
        link = link.replaceAll("boutiqueId", "CampaignId");
        link = link.replaceAll("merchantId", "MerchantId");

        Matcher matcher = pattern.matcher(link);
        while (matcher.find()) {
            fillLinkBuilder(matcher.group(1), "ContentId=", linkBuilder);
            fillLinkBuilder(matcher.group(2), "&amp;", linkBuilder);
            fillLinkBuilder(matcher.group(3), "&amp;", linkBuilder);
        }
        logger.info("createProductDeepLink :: created product deeplink : {}", linkBuilder.toString());
        return linkBuilder.toString();
    }

    private void fillLinkBuilder(String parameterToAdd, String characterToAdd, StringBuilder stringBuilder) {
        if (!Objects.isNull(parameterToAdd)) {
            stringBuilder.append(characterToAdd).append(parameterToAdd);
        }
        logger.info("created product link : {}", stringBuilder.toString());
    }

}
