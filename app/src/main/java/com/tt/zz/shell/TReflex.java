package com.tt.zz.shell;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TReflex {

    /**
     * newInstance(...)实例化
     */


    public static Object newInstance(String className) {
        return newInstance(className, null, null);
    }

    public static Object newInstance(Class clazz) {
        return newInstance(clazz, null, null);
    }

    public static Object newInstance(String className, Class[] paramTypes, Object[] paramValues) {
        try {
            Class clazz = Class.forName(className);
            return newInstance(clazz, paramTypes, paramValues);
        } catch (ClassNotFoundException e) {
            LogUtils.e(e);
        }
        return null;
    }

    public static Object newInstance(Class clazz, Class[] paramTypes, Object[] paramValues) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(paramTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(paramValues);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }


    //获取字段
    public static Object getField(Object object, String fieldName) {
        try {
            Field field = getField(object.getClass(), fieldName);
            if (field != null)
                return field.get(object);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }

    //设置字段的值
    public static boolean setFieldValue(Object object, String fieldName, Object value) {
        try {
            Field field = getField(object.getClass(), fieldName);
            if (field != null)
                field.set(object, value);
            return true;
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return false;
    }


    //获取静态字段值
    public static Object getFieldStatic(Object object, String fieldName) {
        return getFieldStatic(object.getClass(), fieldName);
    }

    //获取静态字段值
    public static Object getFieldStatic(String clazzName, String fieldName) {
        try {
            Class clazz = Class.forName(clazzName);
            return getFieldStatic(clazz, fieldName);
        } catch (ClassNotFoundException e) {
            LogUtils.e(e);
        }
        return null;
    }

    //获取静态字段值
    public static Object getFieldStatic(Class clazz, String fieldName) {
        try {
            Field field = getField(clazz, fieldName);
            if (field != null)
                return field.get(null);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }

    //设置静态字段值
    public static boolean setFieldStatic(Object object, String fieldName, Object value) {
        return setFieldStatic(object.getClass(), fieldName, value);
    }

    //设置静态字段值
    public static boolean setFieldStatic(String clazzName, String fieldName, Object value) {
        try {
            Class clazz = Class.forName(clazzName);
            return setFieldStatic(clazz, fieldName, value);
        } catch (ClassNotFoundException e) {
            LogUtils.e(e);
        }
        return false;
    }

    //设置静态字段值
    public static boolean setFieldStatic(Class clazz, String fieldName, Object value) {
        try {
            Field field = getField(clazz, fieldName);
            if (field != null)
                field.set(null, value);
            return true;
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return false;
    }

    //获取字段(含父类)
    public static Field getField(Class clazz, String fieldName) {
        if (clazz == null) return null;
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            //未找到，则继续往父类找
        }
        return getField(clazz.getSuperclass(), fieldName);
    }

    //获取方法执行
    public static Object invokeMethod(Object object, String methodName) {
        return invokeMethod(object, methodName, null, null);
    }

    //获取方法执行
    public static Object invokeMethod(Object object, String methodName, Class[] paramTypes, Object[] paramValues) {
        try {
            Method method = getMethod(object.getClass(), methodName, paramTypes);
            if (method != null)
                return method.invoke(object, paramValues);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }

    //获取静态方法并且执行
    public static Object invokeMethodStatic(Class clazz, String methodName) {
        return invokeMethodStatic(clazz, methodName, null, null);
    }

    //获取静态方法并且执行
    public static Object invokeMethodStatic(Object object, String methodName) {
        return invokeMethodStatic(object, methodName, null, null);
    }

    //获取静态方法并且执行
    public static Object invokeMethodStatic(Object object, String methodName, Class[] paramTypes, Object[] paramValues) {
        return invokeMethodStatic(object.getClass(), methodName, paramTypes, paramValues);
    }

    //获取静态方法并且执行
    public static Object invokeMethodStatic(Class clazz, String methodName, Class[] paramTypes, Object[] paramValues) {
        try {
            Method method = getMethod(clazz, methodName, paramTypes);
            if (method != null)
                return method.invoke(null, paramValues);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }

    //获取方法
    public static Method getMethod(Class clazz, String methodName, Class[] paramTypes) {
        if (clazz == null) return null;
        Method declaredMethod = null;
        try {
            declaredMethod = clazz.getDeclaredMethod(methodName, paramTypes);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (NoSuchMethodException e) {
            //未找到， 则继续从父类找
        }
        return getMethod(clazz.getSuperclass(), methodName, paramTypes);
    }

    //动态代理方法
    public static Object invokeMethodProxy(Class clazz, InvocationHandler handler) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
    }

}