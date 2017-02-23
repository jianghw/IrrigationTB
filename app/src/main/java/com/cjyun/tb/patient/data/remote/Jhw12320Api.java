//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cjyun.tb.patient.data.remote;

import cn.collectcloud.password.scribe.builder.api.DefaultApi20;
import cn.collectcloud.password.scribe.extractors.JsonRefreshTokenExtractor;
import cn.collectcloud.password.scribe.extractors.JsonTokenExtractor;
import cn.collectcloud.password.scribe.extractors.TokenExtractor;
import cn.collectcloud.password.scribe.model.OAuthConfig;
import cn.collectcloud.password.scribe.utils.OAuthEncoder;

public class Jhw12320Api extends DefaultApi20 {
    private static final String URL_BASE_TB = "https://tb.ccd12320.com";
    private static final String JAR_VERSION = "2.2";
    private static final String AUTHORIZE_HOST = URL_BASE_TB;
    private static final String AUTHORIZE_URL = URL_BASE_TB+"/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = URL_BASE_TB+"/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s";

    public Jhw12320Api() {
    }

    public TokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    public TokenExtractor getRefreshTokenExtractor() {
        return new JsonRefreshTokenExtractor();
    }

    public String getAccessTokenEndpoint() {
        return URL_BASE_TB+"/oauth/token";
    }

    public String getAuthorizationUrl(OAuthConfig config) {
        return config.hasScope()?String.format(URL_BASE_TB+"/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s", new Object[]{config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope())}):String.format(URL_BASE_TB+"/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", new Object[]{config.getApiKey(), OAuthEncoder.encode(config.getCallback())});
    }
}
