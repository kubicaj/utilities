package com.kubicaj.mapper;

/**
 *
 * @param <S> - type of destination object
 */
public interface IMapperBuilder<S> {

    /**
     * Apply rules to build destination object.
     *
     * @return - destination object, result of building
     */
    public S apply();

    /**
     * destination type of object
     *
     * @return
     */
    public Class<S> getDestinationType();
}
