package com.moonkin63.whois;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Moonkin63 on 04.11.2014.
 */
public class DomainTest {
    @Test
    public void testRuDomain() throws Exception {
        DomainInfo domainInfo = new DomainInfo("google.ru");
        System.out.println(domainInfo.getJSONInfo());
        String jsonInfo = domainInfo.getJSONInfo();
        Assert.assertFalse(StringUtils.isEmpty(jsonInfo) || "{}".equalsIgnoreCase(jsonInfo));
    }

    @Test
    public void testRfDomain() throws Exception {
        DomainInfo domainInfo = new DomainInfo("яндекс.рф");
        System.out.println(domainInfo.getJSONInfo());
        String jsonInfo = domainInfo.getJSONInfo();
        Assert.assertFalse(StringUtils.isEmpty(jsonInfo) || "{}".equalsIgnoreCase(jsonInfo));
    }

    @Test
    public void testComDomain() throws Exception {
        DomainInfo domainInfo = new DomainInfo("google.com");
        System.out.println(domainInfo.getJSONInfo());
        String jsonInfo = domainInfo.getJSONInfo();
        Assert.assertFalse(StringUtils.isEmpty(jsonInfo) || "{}".equalsIgnoreCase(jsonInfo));
    }
}
