package com.kubicaj.utilities.mapper;

import com.kubicaj.utilities.mapper.options.MapperOptions;
import com.kubicaj.utilities.mapper.reflection.ReflectionUtils;
import org.apache.commons.lang.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @param <R> - type of destination object
 * @param <S> - type of source object
 */
public class ReflectionMapperBuilder<R, S> extends MapperBuilder<R> {

    // -----------------------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------------------------------------------------------

    protected final S sourceObject;
    protected final Class<S> sourceObjectType;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * new instance of {@link ReflectionMapperBuilder}
     *
     * @param mapperOptions
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObjectType
     */
    protected ReflectionMapperBuilder(MapperOptions mapperOptions, S sourceObject, Class<S> sourceObjectType, Class<R> destinationObjectType) {
        super(mapperOptions, destinationObjectType);
        this.sourceObjectType = sourceObjectType;
        this.sourceObject = sourceObject;
    }

    /**
     * new instance of {@link ReflectionMapperBuilder}
     *
     * @param mapperOptions
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObject
     * @param destinationObjectType
     */
    protected ReflectionMapperBuilder(MapperOptions mapperOptions, S sourceObject, Class<S> sourceObjectType, R destinationObject, Class<R> destinationObjectType) {
        super(mapperOptions, destinationObject, destinationObjectType);
        this.sourceObjectType = sourceObjectType;
        this.sourceObject = sourceObject;
    }

    /**
     * new instance of {@link ReflectionMapperBuilder}
     *
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObjectType
     */
    protected ReflectionMapperBuilder(S sourceObject, Class<S> sourceObjectType, Class<R> destinationObjectType) {
        super(destinationObjectType);
        this.sourceObjectType = sourceObjectType;
        this.sourceObject = sourceObject;
    }

    /**
     * new instance of {@link ReflectionMapperBuilder}
     *
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObject - object of type <R> where the values will be set
     * @param destinationObjectType
     */
    protected ReflectionMapperBuilder(S sourceObject, Class<S> sourceObjectType, R destinationObject, Class<R> destinationObjectType) {
        super(destinationObject,destinationObjectType);
        this.sourceObjectType = sourceObjectType;
        this.sourceObject = sourceObject;
    }

    /**
     * Create new instance of {@link ReflectionMapperBuilder}
     *
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObjectType
     * @return
     */
    public static <R, S> ReflectionMapperBuilder<R, S> createReflectionBuilder(S sourceObject, Class<S> sourceObjectType, Class<R> destinationObjectType) {
        return new ReflectionMapperBuilder<R, S>(sourceObject, sourceObjectType, destinationObjectType);
    }

    /**
     * Create new instance of {@link ReflectionMapperBuilder}
     *
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObjectType
     * @return
     */
    public static <R, S> ReflectionMapperBuilder<R, S> createReflectionBuilder(S sourceObject, Class<S> sourceObjectType, Class<R> destinationObjectType, MapperOptions mapperOptions) {
        return new ReflectionMapperBuilder<R, S>(mapperOptions, sourceObject, sourceObjectType, destinationObjectType);
    }

    /**
     * Create new instance of {@link ReflectionMapperBuilder}
     *
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObject
     * @param destinationObjectType
     * @return
     */
    public static <R, S> ReflectionMapperBuilder<R, S> createReflectionBuilder(S sourceObject, Class<S> sourceObjectType, R destinationObject, Class<R> destinationObjectType) {
        return new ReflectionMapperBuilder<R, S>(sourceObject, sourceObjectType, destinationObject, destinationObjectType);
    }

    /**
     * Create new instance of {@link ReflectionMapperBuilder}
     *
     * @param sourceObject
     * @param sourceObjectType
     * @param destinationObject
     * @param destinationObjectType
     * @return
     */
    public static <R, S> ReflectionMapperBuilder<R, S> createReflectionBuilder(S sourceObject, Class<S> sourceObjectType, R destinationObject, Class<R> destinationObjectType, MapperOptions mapperOptions) {
        return new ReflectionMapperBuilder<R, S>(mapperOptions, sourceObject, sourceObjectType, destinationObject, destinationObjectType);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ABSTRACT METHODS
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public R apply() {
        Collection<Field> destinationObjectFields = ReflectionUtils.getDeclarativeFields(destinationObjectType);
        destinationObjectFields.stream().forEach(field -> {
            String fieldBaseName = getDestinationBaseFieldName(field);
            String setterFunctionName = "set" + WordUtils.capitalize(field.getName());
            String getterFunctionName = getSourceMethodGetterName(fieldBaseName, field.getType().equals(boolean.class));
            // search getter function
            Method getterFunction = ReflectionUtils.findMethodByName(sourceObjectType, getterFunctionName);
            if (getterFunction != null) {
                // search setter method
                Method setterMethod = ReflectionUtils.findMethodByName(destinationObjectType, setterFunctionName, field.getType());
                // get result of getter function
                Object getterResult = ReflectionUtils.invokeMethod(getterFunction, sourceObject);
                // invoke setter
                ReflectionUtils.invokeMethod(setterMethod, destinationObject, getterResult);
            }
        });
        // call processing of others mapping rules
        return super.apply();
    }

    /**
     * find the getter name of source object
     *
     * @param baseFieldName      - base name of method
     * @param isPrimitiveBoolean - flag indicate if the field is type of primitive boolean
     * @return
     */
    private String getSourceMethodGetterName(String baseFieldName, boolean isPrimitiveBoolean) {
        String prefix = "get";
        if (isPrimitiveBoolean) {
            prefix = "is";
        }
        return prefix +
                WordUtils.capitalize(mapperOptions.getSourceObjectFieldPrefix()) +
                WordUtils.capitalize(baseFieldName) +
                WordUtils.capitalize(mapperOptions.getSourceObjectFieldSuffix());
    }

    /**
     * find field name which is reduce by suffix and prefix according
     * {@link MapperOptions#destinationObjectFieldPrefix} and {@link MapperOptions#destinationObjectFieldSuffixe}
     *
     * @param field
     * @return - base name of field
     */
    private String getDestinationBaseFieldName(Field field) {
        String baseFieldName = field.getName();
        if (!mapperOptions.getDestinationObjectFieldPrefix().isEmpty()) {
            baseFieldName = baseFieldName.replaceAll("^" + mapperOptions.getDestinationObjectFieldPrefix(), "");
        }
        if (!mapperOptions.getDestinationObjectFieldSuffixe().isEmpty()) {
            int suffixIndex = baseFieldName.lastIndexOf(mapperOptions.getDestinationObjectFieldSuffixe());
            if (suffixIndex > -1) {
                baseFieldName = baseFieldName.substring(0, suffixIndex);
            }
        }
        return WordUtils.uncapitalize(baseFieldName);
    }
}
