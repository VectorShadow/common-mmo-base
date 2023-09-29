package org.vsdl.common.mmo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String convertObjectToJSONString(Object o) {
        return gson.toJson(o);
    }

    public static String convertJSONStringToString(String s) {
        return convertJSONStringToObject(s, String.class);
    }

    public static <T> T convertJSONStringToObject(String s, Class c) {
        return (T) gson.fromJson(s, c);
    }
}
