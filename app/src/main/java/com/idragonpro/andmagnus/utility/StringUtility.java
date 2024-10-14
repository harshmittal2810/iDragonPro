package com.idragonpro.andmagnus.utility;

import java.util.Arrays;
import java.util.List;

public class StringUtility {

    public static List<String> getListOfStringFromString(String str) {
        return Arrays.asList(str.split("\\s*,\\s*"));
    }
}
