package com.javarush.task.task36.task3602;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/* 
Найти класс по описанию
*/
public class Solution {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() {
        Class[] classes = Collections.class.getDeclaredClasses();
        for (Class clazz : classes) {
            if (
                    List.class.isAssignableFrom(clazz) &&
                            Modifier.isPrivate(clazz.getModifiers()) &&
                            Modifier.isStatic(clazz.getModifiers())
            ) {
                Method method = null;
                try {
                    method = clazz.getDeclaredMethod("get", int.class);
                } catch (NoSuchMethodException e) {
                }
                Constructor[] constructors = clazz.getDeclaredConstructors();
                for (Constructor constructor: constructors) {
                    constructor.setAccessible(true);
                    Class[] parameterTypes = constructor.getParameterTypes();
                    try {
                        method.setAccessible(true);
                        method.invoke(constructor.newInstance(parameterTypes), 0);
                    } catch (InvocationTargetException e) {
                        if (e.getCause().toString().contains("IndexOutOfBoundsException")) {
                            return clazz;
                        }
                    } catch (IllegalArgumentException e) {
                    } catch (NullPointerException e) {
                    } catch (IllegalAccessException e) {
                    } catch (InstantiationException e) {
                    }
                }
            }
        }
        return null;
    }
}
