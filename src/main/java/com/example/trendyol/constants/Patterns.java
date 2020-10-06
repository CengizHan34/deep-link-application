package com.example.trendyol.constants;

/**
 * @author created by cengizhan on 4.10.2020
 */

public class Patterns {
    public static class ByProduct {
        public final static String PRODUCT = "-p-";
        public final static String PRODUCT_DEEPLINK_PATTERN = "-p-(\\d*).?(\\w*\\W\\d*)?\\W?\\w*\\W?(\\w*\\W\\d*)?";
        public final static String PRODUCT_WEBLINK_PATTERN =
                "Product&amp;(\\w*\\W\\d*)\\W?[a-z]*\\W?(\\w*\\W\\d*)?\\W?[a-z]*\\W?(\\w*\\W\\d*)?";
    }

    public static class BySearch {
        public final static String SEARCH = "q=";
        public final static String SEARCH_DEEPLINK_PATTERN = "q=(.+)";
        public final static String SEARCH_WEBLINK_PATTERN = "Search&amp;(\\w*\\W.*)";
    }
}
