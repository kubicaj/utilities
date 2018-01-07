package com.kubicaj.mapper.function;

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
