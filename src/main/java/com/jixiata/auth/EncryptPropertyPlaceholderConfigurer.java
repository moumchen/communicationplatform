package com.jixiata.auth;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    // 需要加密解密的属性名
    private String[] encryptProperties = {"jdbc.username","jdbc.password"};

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if(isEncryptProperty(propertyName)){
            return DBSecurity.getOriginalString(propertyValue);
        }
        return super.convertProperty(propertyName, propertyValue);
    }

    private boolean isEncryptProperty(String propertyName){
        for(String item : encryptProperties){
            if(item.equals(propertyName)){
                return true;
            }
        }
        return false;
    }
}
