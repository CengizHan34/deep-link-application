package com.example.trendyol.service;

import com.example.trendyol.dto.LinkRequestModel;
import com.example.trendyol.dto.LinkResponseModel;
import com.example.trendyol.enums.Parametters;
import com.example.trendyol.exception.CheckException;
import com.example.trendyol.repository.LinkRepository;
import com.example.trendyol.service.impl.LinkServiceImpl;
import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author created by cengizhan on 4.10.2020
 */
@RunWith(JMockit.class)
public class LinkServiceTest {
    private static String HOST = "www.trendyol.com";
    private static String PREFIX_DEEPLINK = "ty://?Page=";
    private LinkService instance;

    @org.mockito.Mock
    private LinkRepository linkRepository;

    @Before
    public void setUp() {
        this.instance = new LinkServiceImpl(linkRepository);
    }

    @Test(expected = CheckException.class)
    public void convertToDeepLink_ifHostnameDoesntComeTrue_shouldThrowRuntimeException() {
        LinkRequestModel linkRequestModel = new LinkRequestModel("www.cengiz.com");
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 0)
            private void checkHost(String str) {
                if (!HOST.equals(str)) {
                    throw new RuntimeException();
                }
            }
        };
        LinkResponseModel linkResponseModel = Deencapsulation.invoke(instance, "convertToDeeplink", linkRequestModel);

    }

    @Test
    public void convertToLink_ifHostnameDoesComeTrue_ShouldBeReturnLinkRequestModel() {
        LinkRequestModel linkRequestModel = new LinkRequestModel("https://www.trendyol.com/casio/saat-p-1925865?boutiqueId=439892&amp;merchantId=105064");
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 1)
            private void checkHost(String str) {

            }

            @Mock(invocations = 1)
            private String createDeepLink(String str) {
                return "ty://?Page=Product&amp;ContentId=1925865&amp;CampaignId=439892&amp;MerchantId=105064";
            }
        };
        MockUp<LinkRepository> linkRepositoryMockUp = new MockUp<LinkRepository>() {
            <Link> void save(com.example.trendyol.model.Link link) {
                System.out.println("success");
            }
        };
        Deencapsulation.setField(instance, "linkRepository", linkRepositoryMockUp.getMockInstance());
        LinkResponseModel linkResponseModel = Deencapsulation.invoke(instance, "convertToDeeplink", linkRequestModel);
        assertNotNull(linkResponseModel);
        assertNotNull(linkResponseModel.getUrl());
        assertEquals("ty://?Page=Product&amp;ContentId=1925865&amp;CampaignId=439892&amp;MerchantId=105064", linkResponseModel.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void checkHost_ifHostnameDoesntComeTrue_shouldThrowRuntimeException() {
        String host = "Test";
        Deencapsulation.invoke(instance, "checkHost", host);
    }

    @Test
    public void checkHost_ifHostnameDoesntComeTrue_shouldNotDoAnything() {
        Deencapsulation.invoke(instance, "checkHost", HOST);
    }

    @Test
    public void createDeepLink_ifStringTypeIsSent_shouldDeReturnString() {
        String link = "https://www.trendyol.com/casio/erkek-kol-saati-p-1925865";
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 1)
            private String createDeepLinkByLinkType(String str) {
                return "ty://?Page=Product&amp;ContentId=1925865";
            }
        };
        String result = Deencapsulation.invoke(instance, "createDeepLink", link);
        assertNotNull(result);
        assertEquals(result, "ty://?Page=Product&amp;ContentId=1925865");
    }

    @Test
    public void createDeepLinkByLinkType_ifUrlsOfTypeProduct_createProductDeepLinkShouldWork() {
        String link = "https://www.trendyol.com/casio/erkek-kol-saati-p-1925865";
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 1)
            private String createProductDeepLink(String str) {
                return "ty://?Page=Product&amp;ContentId=1925865";
            }

            @Mock(invocations = 0)
            private String createSearchDeepLink(String str) {
                return "";
            }

            @Mock(invocations = 0)
            private String createPageDeepLink() {
                return "";
            }
        };
        String result = Deencapsulation.invoke(instance, "createDeepLinkByLinkType", link);
        assertNotNull(result);
        assertEquals(result, "ty://?Page=Product&amp;ContentId=1925865");
    }

    @Test
    public void createDeepLinkByLinkType_ifUrlsOfTypeProduct_createSearchDeepLinkShouldWork() {
        String link = "/tum--urunler?q=elbise";
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 1)
            private String createSearchDeepLink(String link) {
                return "ty://?Page=Search&amp;Query=elbise";
            }

            ;

            @Mock(invocations = 0)
            private String createProductDeepLink(String link) {
                return "";
            }

            ;

            @Mock(invocations = 0)
            private String createPageDeepLink() {
                return "";
            }

            ;
        };
        String result = Deencapsulation.invoke(instance, "createDeepLinkByLinkType", link);
        assertNotNull(result);
        assertEquals(result, "ty://?Page=Search&amp;Query=elbise");
    }

    @Test
    public void createDeepLinkByLinkType_ifUrlsOfTypeProduct_createPageDeepLinkShouldWork() {
        String link = "/Hesabim/Favoriler";
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 0)
            private String createProductDeepLink(String str) {
                return "";
            }

            @Mock(invocations = 0)
            private String createSearchDeepLink(String str) {
                return "";
            }

            @Mock(invocations = 1)
            private String createPageDeepLink() {
                return "ty://?Page=Home";
            }
        };
        String result = Deencapsulation.invoke(instance, "createDeepLinkByLinkType", link);
        assertNotNull(result);
        assertEquals(result, "ty://?Page=Home");
    }

    @Test
    public void createPageDeepLink_ifThisMethodIsRun_stringValueMustReturn() {
        String result = Deencapsulation.invoke(instance, "createPageDeepLink");
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX_DEEPLINK).append("Home");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createSearchDeepLink_ifThisMethodIsRun_searchDeepLinkMustReturn() {
        String link = "/tum--urunler?q=elbise";
        String result = Deencapsulation.invoke(instance, "createSearchDeepLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX_DEEPLINK).append("Search&amp;Query=elbise");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createProductDeepLink_ifAllParametersAreSent_productDeepLinkMustReturn() {
        String link = "/casio/saat-p-1925865?boutiqueId=439892&amp;merchantId=105064";
        String result = Deencapsulation.invoke(instance, "createProductDeepLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX_DEEPLINK).append("Product&amp;ContentId=1925865&amp;CampaignId=439892&amp;MerchantId=105064");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createProductDeepLink_ifBoutiqueIdIsSent_productDeepLinkMustReturn() {
        String link = "/casio/erkek-kol-saati-p-1925865?boutiqueId=439892";
        String result = Deencapsulation.invoke(instance, "createProductDeepLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX_DEEPLINK).append("Product&amp;ContentId=1925865&amp;CampaignId=439892");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createProductDeepLink_ifMerchantIdIsSent_productDeepLinkMustReturn() {
        String link = "/casio/erkek-kol-saati-p-1925865?merchantId=105064";
        String result = Deencapsulation.invoke(instance, "createProductDeepLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX_DEEPLINK).append("Product&amp;ContentId=1925865&amp;MerchantId=105064");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createProductDeepLink_ifContentIdIsSent_productDeepLinkMustReturn() {
        String link = "/casio/erkek-kol-saati-p-1925865";
        String result = Deencapsulation.invoke(instance, "createProductDeepLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX_DEEPLINK).append("Product&amp;ContentId=1925865");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void fillLinkBuilder__shouldNotDoAnything() {
        StringBuilder stringBuilder = new StringBuilder();
        Deencapsulation.invoke(instance, "fillLinkBuilder", "Cengiz", "&&", stringBuilder);
        assertEquals(stringBuilder.toString(), "&&Cengiz");
    }

    @Test(expected = CheckException.class)
    public void convertToWebLink_ifHostnameDoesntComeTrue_shouldThrowRuntimeException() {
        LinkRequestModel linkRequestModel = new LinkRequestModel("tyyyy://////");
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 0)
            private void checkHost(String str) {
                if (!HOST.equals(str)) {
                    throw new RuntimeException();
                }
            }
        };
        LinkResponseModel linkResponseModel = Deencapsulation.invoke(instance, "convertToWebLink", linkRequestModel);

    }

    @Test
    public void convertToWebLink_ifHostnameDoesComeTrue_ShouldBeReturnLinkRequestModel() {
        LinkRequestModel linkRequestModel = new LinkRequestModel("ty://?Page=Product&amp;ContentId=1925865&amp;CampaignId=439892&amp;MerchantId=105064");
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 1)
            private void checkDeepLink(String str) {

            }

            @Mock(invocations = 1)
            private String createWebLinkByLinkType(String str) {
                return "https://www.trendyol.com/brand/name-p-1925865?boutiqueId=439892&amp;merchantId=105064";
            }
        };
        MockUp<LinkRepository> linkRepositoryMockUp = new MockUp<LinkRepository>() {
            <Link> void save(com.example.trendyol.model.Link link) {
                System.out.println("success");
            }
        };
        Deencapsulation.setField(instance, "linkRepository", linkRepositoryMockUp.getMockInstance());
        LinkResponseModel linkResponseModel = Deencapsulation.invoke(instance, "convertToWebLink", linkRequestModel);
        assertNotNull(linkResponseModel);
        assertNotNull(linkResponseModel.getUrl());
        assertEquals("https://www.trendyol.com/brand/name-p-1925865?boutiqueId=439892&amp;merchantId=105064", linkResponseModel.getUrl());
    }

    @Test
    public void createWebLinkByLinkType_ifUrlsOfTypeProduct_createProductWebLinkShouldWork() {
        String link = "ty://?Page=Product&amp;ContentId=1925865&amp;CampaignId=439892";
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 1)
            private String createProdcutWebLink(String str) {
                return "/brand/name-p-1925865?boutiqueId=439892";
            }

            @Mock(invocations = 0)
            private String createSearchWebLink(String str) {
                return "";
            }
        };
        String result = Deencapsulation.invoke(instance, "createWebLinkByLinkType", link);
        assertNotNull(result);
        assertEquals(result, "https://www.trendyol.com/brand/name-p-1925865?boutiqueId=439892");
    }

    @Test
    public void createWebLinkByLinkType_ifUrlsOfTypeSearch_createSearchDeepLinkShouldWork() {
        String link = "ty://?Page=Search&amp;Query=elbise";
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 0)
            private String createProdcutWebLink(String str) {
                return "/brand/name-p-1925865?boutiqueId=439892";
            }

            @Mock(invocations = 1)
            private String createSearchWebLink(String str) {
                return "/tum--urunler?q=elbise";
            }
        };
        String result = Deencapsulation.invoke(instance, "createWebLinkByLinkType", link);
        assertNotNull(result);
        assertEquals(result, "https://www.trendyol.com/tum--urunler?q=elbise");
    }

    @Test
    public void createWebLinkByLinkType_ifThereIsNoUrlType_stringTypeMustReturn() {
        String link = "ty://?Page=Favorites";
        new MockUp<LinkServiceImpl>() {
            @Mock(invocations = 0)
            private String createProdcutWebLink(String str) {
                return "";
            }

            @Mock(invocations = 0)
            private String createSearchWebLink(String str) {
                return "";
            }
        };
        String result = Deencapsulation.invoke(instance, "createWebLinkByLinkType", link);
        assertNotNull(result);
        assertEquals(result, "https://www.trendyol.com");
    }

    @Test(expected = CheckException.class)
    public void checkDeepLink_ifDeepLinkDoesntComeTrue_shouldThrowRuntimeException() {
        String host = "tk://asdasd";
        Deencapsulation.invoke(instance, "checkDeepLink", host);
    }

    @Test
    public void checkDeepLink_ifDeepLinkDoesntComeTrue_shouldNotDoAnything() {
        Deencapsulation.invoke(instance, "checkDeepLink", PREFIX_DEEPLINK);
    }

    @Test
    public void createSearchWebLink_ifThisMethodIsRun_searchWebLinkMustReturn(){
        String link = "ty://?Page=Search&amp;Query=elbise";
        String result = Deencapsulation.invoke(instance, "createSearchWebLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/tum--urunler?q=elbise");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createProdcutWebLink_ifAllParametersAreSent_productWebLinkMustReturn() {
        String link = "ty://?Page=Product&amp;ContentId=1925865&amp;CampaignId=439892&amp;MerchantId=105064";
        String result = Deencapsulation.invoke(instance, "createProdcutWebLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/brand/name-p-1925865?boutiqueId=439892&amp;merchantId=105064");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createProdcutWebLink_ifBoutiqueIdIsSent_productWebLinkMustReturn() {
        String link = "ty://?Page=Product&amp;ContentId=1925865&amp;CampaignId=439892";
        String result = Deencapsulation.invoke(instance, "createProdcutWebLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/brand/name-p-1925865?boutiqueId=439892");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void createProdcutWebLink_ifMerchantIdIsSent_productDeepLinkMustReturn() {
        String link = "ty://?Page=Product&amp;ContentId=1925865&amp;MerchantId=105064";
        String result = Deencapsulation.invoke(instance, "createProdcutWebLink", link);
        assertNotNull(result);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/brand/name-p-1925865?merchantId=105064");
        assertEquals(stringBuilder.toString(), result);
    }

    @Test
    public void replaceAllParametterNames_ifContentIdIsSent_pShouldReturn(){
        String result = Deencapsulation.invoke(instance, "replaceAllParametterNames",
                Parametters.ContentId.name()+"=");
        assertNotNull(result);
        assertEquals(result,"-p-");
    }

    @Test
    public void replaceAllParametterNames_ifCampaignIdIsSent_boutiqueIdShouldReturn(){
        String result = Deencapsulation.invoke(instance, "replaceAllParametterNames",
                Parametters.CampaignId.name());
        assertNotNull(result);
        assertEquals(result,"boutiqueId");
    }

    @Test
    public void replaceAllParametterNames_ifMerchantIdIsSent_pShouldReturn(){
        String result = Deencapsulation.invoke(instance, "replaceAllParametterNames", Parametters.MerchantId.name());
        assertNotNull(result);
        assertEquals(result,"merchantId");
    }




}
