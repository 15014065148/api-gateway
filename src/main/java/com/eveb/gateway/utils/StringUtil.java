package com.eveb.gateway.utils;

import java.util.regex.Pattern;

public class StringUtil {

    private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

    public  static boolean isNumeric(String str) {
        return NUMBER_PATTERN.matcher(str).matches();
    }
}
