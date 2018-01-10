package com.kubicaj.utilities.pojomapper.function;

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
