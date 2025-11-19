package com.joaoMendes.vendas_api.utils;

public class StringUtils {

    public static String normalizeString(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }

    public static String cleanStringForSave(String s) {
        return s.trim();
    }

}