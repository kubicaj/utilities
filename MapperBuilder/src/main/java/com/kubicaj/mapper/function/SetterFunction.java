package com.kubicaj.mapper.function;

/**
 * function interface represented setter function
 *
 * @param <T> - type of invoking object
 * @param <U> - type of setter input argument
 */
@FunctionalInterface
public interface SetterFunction<T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u);
}
