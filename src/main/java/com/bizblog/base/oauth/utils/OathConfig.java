package com.bizblog.base.oauth.utils;

import com.bizblog.base.oauth.APIConfig;


public class OathConfig {
    public static String getValue(String key) {
        return APIConfig.getInstance().getValue(key);
    }
}
