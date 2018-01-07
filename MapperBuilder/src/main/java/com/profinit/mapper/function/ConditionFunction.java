package com.profinit.mapper.function;

@FunctionalInterface
public interface ConditionFunction extends ProcessingFunction {

    boolean test();

}
