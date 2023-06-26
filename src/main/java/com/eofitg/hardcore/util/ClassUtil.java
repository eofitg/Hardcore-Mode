package com.eofitg.hardcore.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

    public static Class getClass(String packName, String className) {
        Class cls = null;
        try {
            cls = Class.forName(packName + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }
    public static List<Class> getClassList(String packName) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(packName.replace('.', '/'));
        String[] files = new File(resource.getFile()).list();
        List<Class> clazzs = new ArrayList<>();
        for (String file : files) {
            clazzs.add(getClass(packName, file.replace(".class", "")));
        }
        return clazzs;
    }

}
