package me.iamguus.ovo.utils;

import me.iamguus.ovo.classes.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 3-1-2016.
 */
public class ListUtil {

    private static ListUtil instance;

    public String serialize(List<Map> list) {
        StringBuilder sb = new StringBuilder();
        for (Map object : list) {
            sb.append(object.getName() + ",");
        }

        String out = sb.toString();
        if (out.length() > 1) {
            out = out.substring(0, out.length() - 1);
        }

        return out;
    }

    public List<String> deserialize(String s) {
        List<String> list = new ArrayList<String>();

        for (String split : s.split(",")) {
            list.add(split);
        }

        return list;
    }

    public static ListUtil get() {
        if (instance == null) {
            instance = new ListUtil();
        }

        return instance;
    }
}
