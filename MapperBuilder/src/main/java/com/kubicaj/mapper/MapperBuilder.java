package com.kubicaj.mapper;

import com.kubicaj.mapper.function.ConditionFunction;
import com.kubicaj.mapper.function.GetterFunction;
import com.kubicaj.mapper.function.ProcessingFunction;
import com.kubicaj.mapper.function.SetterFunction;
import com.kubicaj.mapper.options.MapperOptions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Builder of object of type {@code S}
 *
 * @param <S> - type of object which is creating in builder
 */
public class MapperBuilder <S> {

    // -----------------------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------------------------------------------------------

    private MapperOptions mapperOptions;
    private List<FunctionWrapper> processingFunctions = new ArrayList<>();
    private final S objectToSet;
    private final Class<S> objectToSetType;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    protected MapperBuilder(MapperOptions mapperOptions, Class<S> objectToSetType) {
        try {
            this.mapperOptions = mapperOptions;
            this.objectToSetType = objectToSetType;
            this.objectToSet = objectToSetType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Not able to create Builder",e);
        }
    }

    protected MapperBuilder(Class<S> objectToSetType){
        try{
            this.mapperOptions = new MapperOptions();
            this.objectToSetType = objectToSetType;
            this.objectToSet = objectToSetType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Not able to create Builder",e);
        }
    }

    protected MapperBuilder(S objectToSet, Class<S> objectToSetType){
        this.mapperOptions = new MapperOptions();
        this.objectToSetType = objectToSetType;
        this.objectToSet = objectToSet;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // INSTANCES CREATION METHODS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Create new instance of {@link MapperBuilder}
     *
     * @param builderType - type of builder
     *
     * @return
     */
    public static <S> MapperBuilder<S> createBuilder(Class<S> builderType){
        return new MapperBuilder<S>(builderType);
    }

    /**
     * Create new instance of {@link MapperBuilder}
     *
     * @param builderType - type of builder
     * @param mapperOptions - options of builder
     *
     * @return
     */
    public static <S> MapperBuilder<S> createBuilder(Class<S> builderType,MapperOptions mapperOptions){
        return new MapperBuilder<S>(mapperOptions,builderType);
    }

    /**
     * Create new instance of {@link MapperBuilder}
     *
     * @param objectToSet - object use for applying of setter functions
     *
     * @return
     */
    public static <S> MapperBuilder<S> createBuilder(Class<S> builderType, S objectToSet){
        return new MapperBuilder<S>(objectToSet,builderType);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OTHERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * add processing of {@code setterFunction} method of object {@code objectToSet} with input parameter value
     * as return from {@code getterFunction} function of object {@code objectToGet}
     *
     * @param objectToSet - object for which we are setting a value
     * @param setterFunction - function which is call for setting of exact value of object {@code objectToSet}
     * @param objectToGet - object from which we are getting a value
     * @param getterFunction - function which is call on {@code objectToGet} to gain a value to {@code setterFunction}
     * @param <R> - type of value which is set into {@code objectToSet}
     * @param <G> - type of value {@code objectToGet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <R,G> MapperBuilder<S> withSetter(S objectToSet, SetterFunction<S,R> setterFunction, G objectToGet, GetterFunction<G,R> getterFunction){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(objectToSet,setterFunction,getterFunction.apply(objectToGet))));
        return this;
    }

    /**
     * add processing of {@code setterFunction} method of object {@code objectToSet} with input parameter as {@code valueToSet}
     *
     * @param objectToSet - object for which we are setting a value
     * @param setterFunction - function which is call for setting of exact value of object {@code objectToSet}
     * @param valueToSet - input parameter into method {@code setterFunction}
     * @param <R> - type of value which is set into {@code objectToSet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <R> MapperBuilder<S> withSetter(S objectToSet, SetterFunction<S,R> setterFunction, R valueToSet){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(objectToSet,setterFunction,valueToSet)));
        return this;
    }

    /**
     * add processing of {@code setterFunction} method of object {@link this#objectToSet} with input parameter value
     * as return from {@code getterFunction} function of object {@code objectToGet}
     *
     * @param setterFunction - function which is call for setting of exact value of object {@code objectToSet}
     * @param objectToGet - object from which we are getting a value
     * @param getterFunction - function which is call on {@code objectToGet} to gain a value to {@code setterFunction}
     * @param <R> - type of value which is set into {@code objectToSet}
     * @param <G> - type of value {@code objectToGet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <R,G> MapperBuilder<S> withSetter(SetterFunction<S,R> setterFunction, G objectToGet, GetterFunction<G,R> getterFunction){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(this.objectToSet,setterFunction,getterFunction.apply(objectToGet))));
        return this;
    }

    /**
     * add processing of {@code setterFunction} method of object {@link this#objectToSet} with input parameter as {@code valueToSet}
     *
     * @param setterFunction - function which is call for setting of exact value of object {@code objectToSet}
     * @param valueToSet - input parameter into method {@code setterFunction}
     * @param <R> - type of value which is set into {@code objectToSet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <R> MapperBuilder<S> withSetter(SetterFunction<S,R> setterFunction, R valueToSet){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(this.objectToSet,setterFunction,valueToSet)));
        return this;
    }

    /**
     * start processing with condition. Condition is evaluated according {@code conditionFunction}
     *
     * @param conditionFunction
     *
     * @return this instance of {@link MapperBuilder}
     */
    public MapperBuilder<S> whitStartCondition(ConditionFunction conditionFunction){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(conditionFunction));
        return this;
    }

    /**
     * end condition processing
     *
     * @return this instance of {@link MapperBuilder}
     */
    public MapperBuilder<S> whitEndCondition(){
        processingFunctions.add(FunctionWrapper.ofEndCondition());
        return this;
    }

    /**
     * Method process all functions added into builder. The functions are processing in order its were add into builder.
     */
    public S apply(){
        applyWhitCondition(true,processingFunctions.iterator());
        return objectToSet;
    }

    /**
     * Method process all functions added into builder. The functions are processing in order its were add into builder.
     * It is call in {@link MapperBuilder#apply()} as recursive processing of builder
     *
     * @param conditionEvaluationValue - evaluated condition - if false then processing of several function may be skip
     * @param functionIterator - the stack of function to process
     */
    private void applyWhitCondition(boolean conditionEvaluationValue,Iterator<FunctionWrapper> functionIterator){
        // if there are no more function then end
        if(!functionIterator.hasNext()){
            return;
        }
        // get next function
        FunctionWrapper nextFunctionWrapper = functionIterator.next();
        if(!conditionEvaluationValue){
            // check if end condition is in order to process
            if(nextFunctionWrapper.isEndCondition){
                applyWhitCondition(true,functionIterator);
            } else {
                applyWhitCondition(conditionEvaluationValue, functionIterator);
            }
        } else {
            ProcessingFunction nextFunction = nextFunctionWrapper.getProcessingFunction();
            if (nextFunction instanceof MapperBuilder.SetterRepresentation) {
                // in case of SetterRepresentation process setter function and continue processing
                ((SetterRepresentation) nextFunction).process();
                applyWhitCondition(conditionEvaluationValue, functionIterator);
            } else if (nextFunction instanceof ConditionFunction) {
                // in case of ConditionFunction evaluate condition and continue processing
                applyWhitCondition(((ConditionFunction) nextFunction).test(), functionIterator);
            } else {
                applyWhitCondition(conditionEvaluationValue, functionIterator);
            }
        }
    }

    /**
     * Create error according {@link MapperBuilder#mapperOptions}
     *
     * @param errorMessage - text for error message
     *
     * @return {@link RuntimeException} error instance
     */
    protected RuntimeException createNewNullError(String errorMessage) {
        if(errorMessage==null || errorMessage.isEmpty()){
            errorMessage = mapperOptions.getErrorMessage();
        }
        try {
            return mapperOptions.getNullErrorExceptionType().getConstructor(String.class).newInstance(errorMessage);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // INTERNAL CLASSES
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Setter representation of current value
     *
     * @param <R> the type of value which is set into object {@link SetterRepresentation#objectToSet} within function
     * {@link SetterRepresentation#setterFunction}
     */
    private class SetterRepresentation<R> implements ProcessingFunction{
        private S objectToSet;
        private SetterFunction<S,R> setterFunction;
        private R valueToSet;

        public SetterRepresentation(S objectToSet, SetterFunction<S, R> setterFunction, R valueToSet) {
            this.objectToSet = objectToSet;
            this.setterFunction = setterFunction;
            this.valueToSet = valueToSet;
        }

        /**
         * invoke setter function and return object to set
         *
         * @return - instance of object of type {@code S}
         */
        public S process(){
            if(objectToSet==null){
                throw createNewNullError("The object is null");
            }
            setterFunction.accept(objectToSet,valueToSet);
            return objectToSet;
        }
    }

    /**
     * Class wrap {@link ProcessingFunction} objects and consists several metadata information
     */
    private static class FunctionWrapper {

        private ProcessingFunction processingFunction;
        private boolean isEndCondition;
        private String functionName;

        protected FunctionWrapper(ProcessingFunction processingFunction) {
            this.processingFunction = processingFunction;
        }

        protected FunctionWrapper(String functionName, ProcessingFunction processingFunction) {
            this.processingFunction = processingFunction;
            this.functionName = functionName;
        }

        protected FunctionWrapper(boolean isEndCondition) {
            this.isEndCondition = isEndCondition;
        }

        public boolean isEndCondition() {
            return isEndCondition;
        }

        public boolean isGetFunction(){
            return processingFunction!=null && processingFunction instanceof GetterFunction;
        }

        public boolean isSetFunction(){
            return processingFunction!=null && processingFunction instanceof SetterFunction;
        }

        public ProcessingFunction getProcessingFunction() {
            return processingFunction;
        }

        public static FunctionWrapper ofFunctionWrap(ProcessingFunction processingFunction){
            return new FunctionWrapper(processingFunction);
        }

        public static FunctionWrapper ofFunctionWrap(String functionName, ProcessingFunction processingFunction){
            return new FunctionWrapper(processingFunction);
        }

        public static FunctionWrapper ofEndCondition(){
            return new FunctionWrapper(true);
        }
    }
}
