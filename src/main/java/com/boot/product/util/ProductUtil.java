package com.boot.product.util;

public class ProductUtil {
    public static boolean isSlug(String name) {
        return name.matches("^[a-z\\-]*$");
    }

    public static String getSlug(String name) {
        return name.toLowerCase().replace(" ", "-");
    }

    public static String getSlugIfName(String name) {
        if (!isSlug(name)) {
            return getSlug(name);
        }
        return name;
    }
}
