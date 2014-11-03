package com.moonkin63.whois;

/**
 * Created with IntelliJ IDEA.
 * User: Moonkin63
 * Date: 01.11.13
 * Time: 20:12
 */

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.IDN;
import java.util.HashMap;
import java.util.Map;

public class DomainInfo {

    protected String domain;
    protected String dns1;
    protected String dns2;
    protected String status;
    protected String administrator;
    protected String registrator;
    protected String registrationDate;
    protected String expiredDate;
    protected String freeDate;

    public DomainInfo(String domain) throws Exception {
        try {
            domain = convertURL(domain);
            String domainParts[] = domain.split("\\.");
            String domainZone = null;
            if (domainParts.length >= 2) {
                domainZone = domainParts[domainParts.length - 1];
            }
            Map<String, String> params;

            if ("com".equalsIgnoreCase(domainZone)) {
                params = getComZoneParams(domain);
            } else {
                params = getAnotherZoneParams(domain);
            }
            setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();//todo logger
            throw new Exception("can't load domain info");
        }
    }

    private Map getComZoneParams(String domain) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        Document doc = Jsoup.connect("https://www.reg.ru/whois/?dname=" + domain).userAgent("Mozilla").get();
        Element div_block = doc.select("div.whois_block_content").first();
        Element pre = div_block.select("pre.pre_whois_raw").first();
        String content = pre.text();
        String lines[] = content.split("\\r?\\n");
        for (String line : lines) {
            int delemiterPos = line.indexOf(":");
            String key = line.substring(0, delemiterPos + 1);
            String value = line.substring(delemiterPos + 1, line.length());
            params.put(key, value);
        }
        return params;
    }

    private Map getAnotherZoneParams(String domain) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        Document doc = Jsoup.connect("https://www.reg.ru/whois/?dname=" + domain).userAgent("Mozilla").get();
        Element div_block = doc.select("div.whois_block_content").first();
        Element table = div_block.select("table").first();
        Elements tds = table.select("td");
        String paramName = "";
        for (Element td : tds) {
            if (paramName.isEmpty()) {
                paramName = td.text();
            } else {
                if (params.containsKey(paramName)) {
                    paramName += "2";
                }
                params.put(paramName, td.text());
                paramName = "";
            }
        }
        return params;
    }

    protected static String convertURL(String url) {
        return IDN.toASCII(url);
    }

    protected void setParameters(Map<String, String> params) {
        if (params.containsKey("Домен:")) {
            domain = params.get("Домен:");
        }
        if (params.containsKey("Domain Name:")) {
            domain = params.get("Domain Name:");
        }

        if (params.containsKey("Сервер DNS:")) {
            dns1 = params.get("Сервер DNS:");
        }
        if (params.containsKey("Name Server:")) {
            dns1 = params.get("Name Server:");
        }

        if (params.containsKey("Сервер DNS:2")) {
            dns2 = params.get("Сервер DNS:2");
        }

        if (params.containsKey("Статус:")) {
            status = params.get("Статус:");
        }
        if (params.containsKey("Domain Status:")) {
            status = params.get("Domain Status:");
        }

        if (params.containsKey("Администратор домена:")) {
            administrator = params.get("Администратор домена:");
        }
        if (params.containsKey("Admin Name:")) {
            administrator = params.get("Admin Name:");
        }

        if (params.containsKey("Регистратор:")) {
            registrator = params.get("Регистратор:");
        }
        if (params.containsKey("Registrant Name:")) {
            registrator = params.get("Registrant Name:");
        }

        if (params.containsKey("Дата регистрации:")) {
            registrationDate = params.get("Дата регистрации:");
        }
        if (params.containsKey("Creation Date:")) {
            String date = modifyComDate(params.get("Creation Date:"));
            registrationDate = date;
        }

        if (params.containsKey("Дата окончания регистрации:")) {
            expiredDate = params.get("Дата окончания регистрации:");
        }
        if (params.containsKey("Registrar Registration Expiration Date:")) {
            String date = modifyComDate(params.get("Registrar Registration Expiration Date:"));
            expiredDate = date;
        }

        if (params.containsKey("Дата освобождения:")) {
            freeDate = params.get("Дата освобождения:");
        }
    }

    public String getAllInfo() {
        String allInfo;
        allInfo = getDomain() + "\n"
                + getDns1() + "\n"
                + getDns2() + "\n"
                + getRegistrator() + "\n"
                + getAdministrator() + "\n"
                + getRegistrationDate() + "\n"
                + getExpiredDate() + "\n"
                + getFreeDate() + "\n"
                + getStatus() + "\n";
        return allInfo;
    }

    /**
     * @return all information about domain in JSON format
     */
    public String getJSONInfo() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getDomain() {
        return domain;
    }

    public String getDns1() {
        return dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public String getStatus() {
        return status;
    }

    public String getAdministrator() {
        return administrator;
    }

    public String getRegistrator() {
        return registrator;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public String getFreeDate() {
        return freeDate;
    }

    private String modifyComDate(String date) {
        String result = date.trim();
        result = result.replaceAll("-", ".");
        return result.substring(0, 10);
    }
}
