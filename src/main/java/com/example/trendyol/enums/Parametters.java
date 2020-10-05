package com.example.trendyol.enums;

/**
 * @author created by cengizhan on 5.10.2020
 */
public enum Parametters {
    CampaignId("campaignId"),
    MerchantId("merchantId"),
    ContentId("contentId"),
    BoutiqueId("boutiqueId");

    private String value;

    Parametters(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
