package com.dong.statistics.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author <dr_dong>
 *         Time : 2017/11/20 10:02
 */
public class ResourceUtils {
    private static final String TAG = ResourceUtils.class.getSimpleName();
    private static Object idResource = null;

    private static Object getIdResource(Class<?> resource) {
        if (idResource == null) {
            try {
                Class<?>[] classes = resource.getClasses();
                for (Class<?> c : classes) {
                    int i = c.getModifiers();
                    String className = c.getName();
                    String s = Modifier.toString(i);
                    if (s.contains("static") && className.endsWith("$id")) {
                        return c.getConstructor().newInstance();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return idResource;
    }

    /**
     * 根据Id获取name
     *
     * @param rClass
     * @param id
     * @return
     */
    public static String getResourceNameById(Class<?> rClass, int id) {
        try {
            Class<?> aClass = getIdResource(rClass).getClass();
            Field[] fields = aClass.getFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if (id == fields[i].getInt(aClass)) {
                    IdResource idResource = new IdResource();
                    idResource.setId(id);
                    idResource.setName(name);
                    return name;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}