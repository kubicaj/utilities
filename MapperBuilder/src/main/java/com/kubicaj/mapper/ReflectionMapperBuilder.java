package com.kubicaj.mapper;

/**
 *
 * @param <S> - type of destination object
 * @param <R> - type of source object
 */
public class ReflectionMapperBuilder <S,R> implements IMapperBuilder{

    // -----------------------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------------------------------------
    // ABSTRACT METHODS
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Object apply() {
        return null;
    }

    @Override
    public Class getDestinationType() {
        return null;
    }
}
