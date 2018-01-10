package com.kubicaj.utilities.pojomapper;

/**
 *
 * @param <R> - type of destination object
 */
public interface IMapperBuilder<R> {

    /**
     * Apply rules to build destination object.
     *
     * @return - destination object, result of building
     */
    public R apply();

    /**
     * destination type of object
     *
     * @return
     */
    public Class<R> getDestinationType();

}
