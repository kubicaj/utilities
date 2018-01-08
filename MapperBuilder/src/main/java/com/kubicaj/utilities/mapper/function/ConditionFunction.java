package com.kubicaj.utilities.mapper.function;

/**
 * Class represented predictor without parameters
 */
@FunctionalInterface
public interface ConditionFunction extends ProcessingFunction {

    /**
     * Perform test method
     *
     * @return
     */
    boolean test();

}