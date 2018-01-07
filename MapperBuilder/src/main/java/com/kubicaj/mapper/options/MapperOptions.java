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
}
