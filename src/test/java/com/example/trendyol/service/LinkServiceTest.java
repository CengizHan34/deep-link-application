package com.example.trendyol.service;

import com.example.trendyol.dto.LinkRequestModel;
import com.example.trendyol.dto.LinkResponseModel;
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

    @Test(expected = RuntimeException.class)
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
            private String chooseDeeplinkType(String str) {
                return "ty://?Page=Product&amp;ContentId=1925865";
            }
        };
        String result = Deencapsulation.invoke(instance, "createDeepLink", link);
        assertNotNull(result);
        assertEquals(result, "ty://?Page=Product&amp;ContentId=1925865");
    }

    @Test
    public void chooseDeeplinkType_ifUrlsOfTypeProduct_createProductDeepLinkShouldWork() {
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
        String result = Deencapsulation.invoke(instance, "chooseDeeplinkType", link);
        assertNotNull(result);
        assertEquals(result, "ty://?Page=Product&amp;ContentId=1925865");
    }

    @Test
    public void chooseDeeplinkType_ifUrlsOfTypeProduct_createSearchDeepLinkShouldWork() {
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
        String result = Deencapsulation.invoke(instance, "chooseDeeplinkType", link);
        assertNotNull(result);
        assertEquals(result, "ty://?Page=Search&amp;Query=elbise");
    }

    @Test
    public void chooseDeeplinkType_ifUrlsOfTypeProduct_createPageDeepLinkShouldWork() {
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
        String result = Deencapsulation.invoke(instance, "chooseDeeplinkType", link);
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
}
