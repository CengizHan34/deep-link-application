package com.example.trendyol.service.impl;

import com.example.trendyol.dto.LinkRequestModel;
import com.example.trendyol.dto.LinkResponseModel;
import com.example.trendyol.enums.Parametters;
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

import static com.example.trendyol.constants.Patterns.*;
import static com.example.trendyol.constants.Url.*;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Service
public class LinkServiceImpl implements LinkService {
    private static Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);
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
        return createDeepLinkByLinkType(link);
    }

    private String createDeepLinkByLinkType(String link) {
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
        Pattern pattern = Pattern.compile(SEARCH_DEEPLINK_PATTERN);
        Matcher matcher = pattern.matcher(link);
        while (matcher.find()) {
            linkBuilder.append(QUERY).append("=").append(matcher.group(1));
        }
        logger.info("createSearchDeepLink :: created search deeplink : {}", linkBuilder.toString());
        return linkBuilder.toString();
    }

    private String makeTheFirstLetterLowercase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String createProductDeepLink(String link) {
        StringBuilder linkBuilder = new StringBuilder(PREFIX_DEEPLINK).append(PREFIX_PRODUCT);
        Pattern pattern = Pattern.compile(PRODUCT_DEEPLINK_PATTERN);
        link = link
                .replace(Parametters.BoutiqueId.getValue(), Parametters.CampaignId.name())
                .replace(Parametters.MerchantId.getValue(), Parametters.MerchantId.name());

        Matcher matcher = pattern.matcher(link);
        while (matcher.find()) {
            fillLinkBuilder(matcher.group(1), Parametters.ContentId.name() + "=", linkBuilder);
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

    @Override
    public LinkResponseModel convertToWebLink(LinkRequestModel linkRequestModel) {
        String webLink = "";
        logger.info("LinkServiceImpl :: convertToWebLink :: URL > {}", linkRequestModel.getUrl());
        checkDeepLink(linkRequestModel.getUrl());
        webLink = createWebLinkByLinkType(linkRequestModel.getUrl());
        LinkResponseModel linkResponseModel = LinkResponseModel.builder().url(webLink).build();
        Link linkModel = Link.builder().id(UUID.randomUUID().toString()).webLink(linkRequestModel.getUrl())
                .deepLink(webLink).processType(ProcessType.CONVERT_TO_WEBLINK.name())
                .build();
        linkRepository.save(linkModel);
        logger.info("link model saved successfully. ID : {}", linkModel.getId());
        return linkResponseModel;
    }

    private String createWebLinkByLinkType(String url) {
        StringBuilder webLinkBuilder = new StringBuilder(createTrendyolLink());
        if (url.contains(PREFIX_PRODUCT)) {
            webLinkBuilder.append(createProdcutWebLink(url));
        } else if (url.contains(PREFIX_SEARCH)) {
            webLinkBuilder.append(createSearchWebLink(url));
        }
        return webLinkBuilder.toString();
    }

    private void checkDeepLink(String url) {
        if (!url.startsWith(PREFIX_DEEPLINK)) {
            throw new CheckException("");
        }
    }

    private String createSearchWebLink(String url) {
        String searchLink = "";
        StringBuilder seachLinkBuilder = new StringBuilder();
        Pattern pattern = Pattern.compile(SEARCH_WEBLINK_PATTERN);
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            fillLinkBuilder(matcher.group(1), "", seachLinkBuilder);
        }
        searchLink = seachLinkBuilder.toString().replace(QUERY, "q");
        searchLink = SEARCH_URL + searchLink;
        logger.info("createProdcutWebLink :: created product weblink : {}", searchLink);
        return searchLink;
    }

    private String createTrendyolLink() {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(HTTPS).append(HOST);
        return linkBuilder.toString();
    }

    private String createProdcutWebLink(String url) {
        String productWebLink = "";
        StringBuilder linkBuilder = new StringBuilder();
        Pattern pattern = Pattern.compile(PRODUCT_WEBLINK_PATTERN);
        Matcher matcher = pattern.matcher(url);

        while (matcher.find()) {
            fillLinkBuilder(matcher.group(1), "", linkBuilder);
            fillLinkBuilder(matcher.group(2), "?", linkBuilder);
            fillLinkBuilder(matcher.group(3), "&amp;", linkBuilder);
        }

        productWebLink = PRODUCT_URL + replaceAllParametterNames(linkBuilder.toString());
        ;
        logger.info("createProdcutWebLink :: created product weblink : {}", productWebLink);
        return productWebLink;
    }

    private String replaceAllParametterNames(String url) {
        String contentId = Parametters.ContentId.name() + "=";
        return url.replace(contentId, "-p-")
                .replace(Parametters.CampaignId.name(), "boutiqueId")
                .replace(Parametters.MerchantId.name(), "merchantId");
    }
}
