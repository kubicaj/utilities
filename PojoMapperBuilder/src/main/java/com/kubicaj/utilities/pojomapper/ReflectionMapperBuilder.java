package com.kubicaj.utilities.pojomapper;

import com.kubicaj.utilities.pojomapper.options.MapperOptions;
import com.kubicaj.utilities.pojomapper.reflection.ReflectionUtils;
import org.apache.commons.lang.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <R> - type of destination object
 * @param <S> - type of source object
 */
public class ReflectionMapperBuilder<R, S> extends MapperBuilder<R> {

    // -----------------------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------------------------------------------------------

    protected S sourceObject;
    protected final Class<S> sourceObjectType;

    /**
     * set of internal mapper of object of complex types
     * Key = className or attributeName with full path
     * Value = Internal mapper invoking for set values of internal complex type
     * Sample:
     * key = com.kubicaj.utils.mapper.SimplePojo, value=IMapper for setting whole field which has type of java.kubicaj.utils.mapper.SimplePojo
     * key = destinationClassName#param1, value=IMapper for setting field whit name destinationClassName#param1
     */
    private Map<String, IMapperBuilder> internalMapperBuilders = new HashMap<>();

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
     * @param destinationObject     - object of type <R> where the values will be set
     * @param destinationObjectType
     */
    protected ReflectionMapperBuilder(S sourceObject, Class<S> sourceObjectType, R destinationObject, Class<R> destinationObjectType) {
        super(destinationObject, destinationObjectType);
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
     * @param sourceObjectType
     * @param destinationObjectType
     * @return
     */
    public static <R, S> ReflectionMapperBuilder<R, S> createReflectionBuilder(Class<S> sourceObjectType, Class<R> destinationObjectType) {
        return new ReflectionMapperBuilder<R, S>( null, sourceObjectType, destinationObjectType);
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
            // check if field is excluding from mapping. If not then continue
            if (!mapperOptions.isFieldExcluding(field.getName())) {
                // create setter name
                String setterFunctionName = "set" + WordUtils.capitalize(field.getName());
                // search getter function
                String fieldBaseName = getDestinationBaseFieldName(field);
                String getterFunctionName = getSourceMethodGetterName(fieldBaseName, field.getType().equals(boolean.class));
                Method getterFunction = ReflectionUtils.findMethodByName(sourceObjectType, getterFunctionName);
                // find custom mapper
                // field mapper has higher priority as class mapper
                IMapperBuilder customMapper = internalMapperBuilders.get(destinationObjectType.getName()+"#"+field.getName());
                if(customMapper == null){
                    customMapper = internalMapperBuilders.get(field.getType().getName());
                }
                if(customMapper == null) {
                    // if custom mapper not exists then continue
                    if (getterFunction != null) {
                        // search setter method
                        Method setterMethod = ReflectionUtils.findMethodByName(destinationObjectType, setterFunctionName, field.getType());
                        // get result of getter function
                        Object getterResult = ReflectionUtils.invokeMethod(getterFunction, sourceObject);
                        // invoke setter
                        ReflectionUtils.invokeMethod(setterMethod, destinationObject, getterResult);
                    }
                } else {
                    // search setter method
                    Method setterMethod = ReflectionUtils.findMethodByName(destinationObjectType, setterFunctionName, field.getType());
                    // invoke setter with result from custom mapper
                    if(customMapper instanceof ReflectionMapperBuilder){
                        if (((ReflectionMapperBuilder)customMapper).getSourceObject() == null)
                            ((ReflectionMapperBuilder)customMapper).setSourceObject(ReflectionUtils.invokeMethod(getterFunction, sourceObject));
                    }
                    ReflectionUtils.invokeMethod(setterMethod, destinationObject, customMapper.apply());
                }
            }
        });
        // call processing of others mapping rules
        return super.apply();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OTHER METHODS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * add mapping of field which type is {@code destinationInternalType}
     *
     * @param destinationInternalType
     * @param internalMapper           - mapper which will apply on whole field which have type {@code destinationInternalType}
     * @param <D>                     - type of filed whit specific object type
     * @return - this instance
     */
    public <D> ReflectionMapperBuilder<R, S> withInternalClassMapper(Class<D> destinationInternalType, IMapperBuilder internalMapper) {
        internalMapperBuilders.put(destinationInternalType.getName(), internalMapper);
        return this;
    }

    /**
     * add mapping of field with name {@code fieldName}
     *
     * @param fieldName
     * @param internalMapper - mapper which will apply on whole field which have type {@code destinationInternalType}
     * @return - this instance
     */
    public ReflectionMapperBuilder<R, S> withInternalFieldMapper(String fieldName, IMapperBuilder internalMapper) {
        internalMapperBuilders.put(destinationObjectType.getName() + "#" + fieldName, internalMapper);
        return this;
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

    /**
     * set source object of mapper
     *
     * @param sourceObject
     */
    protected void setSourceObject(S sourceObject){
        this.sourceObject = sourceObject;
    }

    protected S getSourceObject(){
        return this.sourceObject;
    }
}
