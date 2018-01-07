package com.kubicaj.mapper.function;

/**
 * function interface represented getter function
 *
 * @param <T> - type of invoking object
 * @param <R> - type of return value
 */
@FunctionalInterface
public interface GetterFunction<T, R>extends ProcessingFunction {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);

}
