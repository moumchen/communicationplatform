package com.jixiata.auth;

public class DBSecurity {

    public static String generateSecurityString(String str){
        return str+"4789";
    }

    public static String getOriginalString(String str){
        return str.split("4789")[0];
    }

}
