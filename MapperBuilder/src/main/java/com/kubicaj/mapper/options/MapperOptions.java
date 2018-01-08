package com.kubicaj.mapper.options;

import com.kubicaj.mapper.exception.MapperException;

/**
 * Class represented options for mapping object
 *
 * It include options for behaving in case of error, reflection options,...
 *
 */
public class MapperOptions {

    // -----------------------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Class of type {@link Throwable} which will throw in case of NPE excetpion
     * Default = Class of type {@link MapperException} is set in {@link MapperOptions#getNullErrorExceptionType()}
     */
    private Class<? extends RuntimeException> nullErrorExceptionType = null;

    /**
     * The error message in case of NPE exception.
     * The default value is = NPE occur
     */
    private String errorMessage = "Null value occur";

    /**
     * The option say us if we throw custom error in case the NPE occur.
     * value = true - the custom error of type {@link MapperOptions#nullErrorExceptionType} is throw
     * value = false - the default java {@link NullPointerException} is throw
     * The default value = true
     */
    private boolean autoThrowCustomErrorIfNull = true;

    /**
     * prefix of field which will be used for finding of getter method of source object
     * The prefix character is case sensitive
     *
     * Sample:
     * base field name = firstName
     * If prefix is set to <i>El</i> then the getter method <i>getElFirstName</i> will be finding in source object
     *
     * Default:
     * Empty value
     */
    private String sourceObjectFieldPrefix = "";

    /**
     * suffix of field which will be used for finding of getter method of source object
     * The suffix character is case sensitive
     *
     * Sample:
     * base field name = firstName
     * If suffix is set to <i>El</i> then the getter method <i>getFirstNameEl</i> will be finding in source object
     * Default:
     * Empty value
     */
    private String sourceObjectFieldSuffix = "";

    /**
     * prefix of field which will be used for parsing of base filedName.
     * The prefix character is case sensitive
     *
     * Sample:
     * field name = elFirstName
     * If prefix is set to <i>el</i> then the base field name will parse as <i>firstName</i>.
     * Then the getter with name <i>getFirstName</i> will be finding in source object.
     *
     * Default:
     * Empty value
     */
    private String destinationObjectFieldPrefix = "";

    /**
     * suffix of field which will be used for parsing of base filedName.
     * The suffix character is case sensitive
     *
     * Sample:
     * field name = firstNameEl
     * If prefix is set to <i>El</i> then the base field name will parse as <i>firstName</i>.
     * Then the getter with name <i>getFirstName</i> will be finding in source object.
     *
     * Default:
     * Empty value
     */
    private String destinationObjectFieldSuffixe = "";

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS AND SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public Class<? extends RuntimeException> getNullErrorExceptionType() {
        if(nullErrorExceptionType==null){
            try {
                nullErrorExceptionType = (Class<MapperException>) Class.forName(MapperException.class.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return nullErrorExceptionType;
    }

    /**
     * set option {@link MapperOptions#nullErrorExceptionType}
     *
     * @param nullErrorExceptionType
     * @return this instance
     */
    public MapperOptions setNullErrorExceptionType(Class<? extends RuntimeException> nullErrorExceptionType) {
        this.nullErrorExceptionType = nullErrorExceptionType;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * set option {@link MapperOptions#errorMessage}
     *
     * @param errorMessage
     * @return this instance
     */
    public MapperOptions setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public boolean isAutoThrowCustomErrorIfNull() {
        return autoThrowCustomErrorIfNull;
    }

    /**
     * set option {@link MapperOptions#autoThrowCustomErrorIfNull}
     *
     * @param autoThrowCustomErrorIfNull
     * @return this instance
     */
    public MapperOptions setAutoThrowCustomErrorIfNull(boolean autoThrowCustomErrorIfNull) {
        this.autoThrowCustomErrorIfNull = autoThrowCustomErrorIfNull;
        return this;
    }

    public String getSourceObjectFieldPrefix() {
        return sourceObjectFieldPrefix;
    }

    /**
     * set option {@link MapperOptions#sourceObjectFieldPrefix}
     *
     * @param sourceObjectFieldPrefix
     * @return this instance
     */
    public MapperOptions setSourceObjectFieldPrefix(String sourceObjectFieldPrefix) {
        this.sourceObjectFieldPrefix = sourceObjectFieldPrefix;
        return this;
    }

    public String getSourceObjectFieldSuffix() {
        return sourceObjectFieldSuffix;
    }

    /**
     * set option {@link MapperOptions#sourceObjectFieldPrefix}
     *
     * @param sourceObjectFieldSuffix
     * @return this instance
     */
    public MapperOptions setSourceObjectFieldSuffix(String sourceObjectFieldSuffix) {
        this.sourceObjectFieldSuffix = sourceObjectFieldSuffix;
        return this;
    }

    public String getDestinationObjectFieldPrefix() {
        return destinationObjectFieldPrefix;
    }

    /**
     * set option {@link MapperOptions#destinationObjectFieldPrefix}
     *
     * @param destinationObjectFieldPrefix
     * @return this instance
     */
    public MapperOptions setDestinationObjectFieldPrefix(String destinationObjectFieldPrefix) {
        this.destinationObjectFieldPrefix = destinationObjectFieldPrefix;
        return this;
    }

    public String getDestinationObjectFieldSuffixe() {
        return destinationObjectFieldSuffixe;
    }


    /**
     * set option {@link MapperOptions#destinationObjectFieldSuffixe}
     *
     * @param destinationObjectFieldSuffixe
     * @return this instance
     */
    public MapperOptions setDestinationObjectFieldSuffixe(String destinationObjectFieldSuffixe) {
        this.destinationObjectFieldSuffixe = destinationObjectFieldSuffixe;
        return this;
    }
}
