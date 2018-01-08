package com.kubicaj.utilities.mapper.reflection;

import com.kubicaj.utilities.mapper.exception.MapperException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Utils for working with reflection. The advantage of using of this util is cache of method definitions.
 */
public class ReflectionUtils {

    /**
     * the cache of method definitions
     * key = class name
     * value = map of {@link Method}. The key of map is the method name
     */
    private static Map<String, Map<String, Method>> methodStaticCache = new HashMap<>();


    /**
     * the cache of field definitions
     * key = class name
     * value = map of {@link Field}. The key of map is the field name
     */
    private static Map<String, Map<String, Field>> classFieldsStaticCache = new HashMap<>();

    /**
     * invoke method
     *
     * @param method
     * @param object
     * @param args
     * @return - result of method
     */
    public static Object invokeMethod(Method method, Object object, Object... args) {
        if (method == null) {
            throw new MapperException("There is not method definition which you want to invoke");
        }
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MapperException(String.format("The error occur when invoke method with name %s", method.getName()), e);
        }
    }

    /**
     * get {@link List} of field definitions of class of type {@code classType}
     * If there is not particular record in the cache, then the fields are find in class definition following by saving to cache
     *
     * @param classType - {@link Class} represented type of class
     * @param <T>       - type of class
     * @return {@link List} of {@link Field} objects
     */
    public static <T> Collection<Field> getDeclarativeFields(Class<T> classType) {
        Map<String, Field> publicFieldStack = classFieldsStaticCache.get(classType.getName());
        if (publicFieldStack == null) {
            final Map<String, Field> newPublicFieldStack = new HashMap<>();
            Field[] fields = classType.getDeclaredFields();
            Arrays.asList(fields).forEach(field -> {
                newPublicFieldStack.put(field.getName(), field);
            });
            classFieldsStaticCache.put(classType.getName(), newPublicFieldStack);
            return newPublicFieldStack.values();
        } else {
            return publicFieldStack.values();
        }
    }

    /**
     * Search method definition in class of type {@code classType}
     *
     * @param classType  - type of object {@code object}
     * @param methodName - name of method
     * @param <T>        - type of object
     * @return - definition of method
     */
    public static <T> Method findMethodByName(Class<T> classType, String methodName, Class<?>... parameterTypes) {
        // at first find in cache. If not exists then add it there
        Map<String, Method> classMethodsStack = methodStaticCache.get(classType.getName());
        if (classMethodsStack == null) {
            classMethodsStack = new HashMap<>();
            methodStaticCache.put(classType.getName(), classMethodsStack);
        }
        // try to find method in cache. If not exists then find in in class definition and add it to cache
        Method methodToInvoke = classMethodsStack.get(methodName);
        if (methodToInvoke == null) {
            try {
                methodToInvoke = classType.getMethod(methodName, parameterTypes);
                classMethodsStack.put(methodName, methodToInvoke);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return methodToInvoke;
    }
}
