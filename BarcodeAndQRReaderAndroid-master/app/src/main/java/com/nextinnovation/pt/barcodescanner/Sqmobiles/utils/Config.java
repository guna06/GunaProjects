package com.nextinnovation.pt.barcodescanner.Sqmobiles.utils;

/**
 * Created by Salman on 22-03-2017.
 */
public class Config {

    public static String serverpath="http://mobiles.sqindia.net/api/mobile/auth/";
    public static String localpath="http://192.168.1.99/sqmobiles/public/api/mobile/auth/";
    public static String currentpath="http://mobiles.sqindia.net/api/mobile/auth/";

    //user Module:
    public static final String WEB_URL_REGISTER = currentpath+"auth/user_registration";
    public static final String WEB_URL_LOGIN = currentpath+"/api/mobile/auth/login";


    //Profile Module
    public static final String WEB_URL_PROFILE = currentpath+"profile/create_profile";


    public static boolean isStringNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i <value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
