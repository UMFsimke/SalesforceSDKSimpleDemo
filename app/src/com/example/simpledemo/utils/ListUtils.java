package com.example.simpledemo.utils;

import java.util.List;

public class ListUtils {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> int count(List<T> list) {
        return isEmpty(list) ? 0 : list.size();
    }
}
