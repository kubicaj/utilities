package com.kubicaj.mapper.function;

@FunctionalInterface
public interface ConditionFunction extends ProcessingFunction {

    boolean test();

}
