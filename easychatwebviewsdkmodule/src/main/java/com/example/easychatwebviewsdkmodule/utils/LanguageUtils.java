package com.example.easychatwebviewsdkmodule.utils;

import java.util.HashMap;

public class LanguageUtils {
    public HashMap<String, String> map = new HashMap<>();

    public LanguageUtils() {
        map.put("no", "nb");
        map.put("zh", "cmn-CN");
        map.put("zh-TW", "cmn-TW");
    }

    public String getMappedLanguage(String inputLang) {
        if (map.containsKey(inputLang)) {
            return map.get((inputLang));
        } else {
            return inputLang;
        }
    }

}
