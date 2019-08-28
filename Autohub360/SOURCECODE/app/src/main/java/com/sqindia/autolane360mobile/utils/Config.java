package com.sqindia.autolane360mobile.utils;

/**
 * Created by Salman on 22-03-2017.
 */
public class Config {



    public static String openPath="http://104.197.80.225/autolane360api/apis/";
    public static String localpath="http://192.168.1.13/Autolane360api/apis/";
    public static String currentpath="http://104.197.80.225/autolane360api/apis/";

    public static final String WEB_URL_LOGIN = currentpath+"auth_login";


    public static final String GET_CAR_LIST = currentpath+"get_car_list";
    public static final String GET_SOLD_CARS=currentpath+"get_sold_car_list";
    public static final String GET_IMAGES=currentpath+"get_car_img";

    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";



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
