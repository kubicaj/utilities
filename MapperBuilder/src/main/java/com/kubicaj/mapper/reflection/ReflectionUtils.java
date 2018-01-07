package com.kubicaj.mapper.reflection;

import com.kubicaj.mapper.exception.MapperException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
     * invoke {@link Method} according parameters. In the method the definition of method is searching in cache at first.
     * If there is not particular record in the cache, then the method is find in class definition following by saving to cache
     *
     * @param classType - type of object {@code object}
     * @param methodName - name of method
     * @param object - object for who the method is invoking
     * @param args - method arguments
     * @param <T> - type of object
     * @return
     */
    public <T> Object invokeMethod(Class<T> classType, String methodName, T object, Object... args) {
        // at first find in cache. If not exists then add it there
        Map<String, Method> classMethodsStack = methodStaticCache.get(classType.getName());
        if (classMethodsStack == null) {
            classMethodsStack = new HashMap<>();
        }
        // try to find method in cache. If not exists then find in in class definition and add it to cache
        Method methodToInvoke = classMethodsStack.get(methodName);
        if (methodToInvoke == null) {
            try {
                methodToInvoke = classType.getMethod(methodName);
            } catch (NoSuchMethodException e) {
                throw new MapperException(String.format("There is not method with name %s for class %s", methodName, classType.getName()),e);
            }
        }
        try {
            return methodToInvoke.invoke(object,args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MapperException(String.format("The error occur when invoke method with name %s for class %s", methodName, classType.getName()),e);
        }
    }
}
