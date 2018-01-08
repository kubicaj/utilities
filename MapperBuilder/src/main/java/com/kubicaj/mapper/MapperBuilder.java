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

/**
 * Builder of object of type {@code S}
 *
 * @param <R> - type of object which is creating in builder
 */
public class MapperBuilder <R> implements IMapperBuilder{

    // -----------------------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // -----------------------------------------------------------------------------------------------------------------

    protected MapperOptions mapperOptions;
    private List<FunctionWrapper> processingFunctions = new ArrayList<>();
    protected final R destinationObject;
    protected final Class<R> destinationObjectType;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    protected MapperBuilder(MapperOptions mapperOptions, Class<R> destinationObjectType) {
        try {
            this.mapperOptions = mapperOptions;
            this.destinationObjectType = destinationObjectType;
            this.destinationObject = destinationObjectType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Not able to create Builder",e);
        }
    }

    protected MapperBuilder(Class<R> destinationObjectType){
        try{
            this.mapperOptions = new MapperOptions();
            this.destinationObjectType = destinationObjectType;
            this.destinationObject = destinationObjectType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Not able to create Builder",e);
        }
    }

    protected MapperBuilder(R destinationObject, Class<R> destinationObjectType){
        this.mapperOptions = new MapperOptions();
        this.destinationObjectType = destinationObjectType;
        this.destinationObject = destinationObject;
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
    public static <R> MapperBuilder<R> createBuilder(Class<R> builderType){
        return new MapperBuilder<R>(builderType);
    }

    /**
     * Create new instance of {@link MapperBuilder}
     *
     * @param objectToSet - object use for applying of setter functions
     *
     * @return
     */
    public static <R> MapperBuilder<R> createBuilder(Class<R> builderType, R objectToSet){
        return new MapperBuilder<R>(objectToSet,builderType);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ABSTRACT METHODS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see IMapperBuilder#apply()
     */
    @Override
    public R apply(){
        applyWhitCondition(true,processingFunctions.iterator());
        return destinationObject;
    }

    /**
     * @see IMapperBuilder#getDestinationType()
     */
    @Override
    public Class getDestinationType() {
        return destinationObjectType;
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
     * @param <S> - type of value which is set into {@code objectToSet}
     * @param <G> - type of value {@code objectToGet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <S,G> MapperBuilder<R> withSetter(R objectToSet, SetterFunction<R,S> setterFunction, G objectToGet, GetterFunction<G,S> getterFunction){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(objectToSet,setterFunction,getterFunction.apply(objectToGet))));
        return this;
    }

    /**
     * add processing of {@code setterFunction} method of object {@code objectToSet} with input parameter as {@code valueToSet}
     *
     * @param objectToSet - object for which we are setting a value
     * @param setterFunction - function which is call for setting of exact value of object {@code objectToSet}
     * @param valueToSet - input parameter into method {@code setterFunction}
     * @param <S> - type of value which is set into {@code objectToSet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <S> MapperBuilder<R> withSetter(R objectToSet, SetterFunction<R,S> setterFunction, S valueToSet){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(objectToSet,setterFunction,valueToSet)));
        return this;
    }

    /**
     * add processing of {@code setterFunction} method of object {@link this#destinationObject} with input parameter value
     * as return from {@code getterFunction} function of object {@code objectToGet}
     *
     * @param setterFunction - function which is call for setting of exact value of object {@code objectToSet}
     * @param objectToGet - object from which we are getting a value
     * @param getterFunction - function which is call on {@code objectToGet} to gain a value to {@code setterFunction}
     * @param <S> - type of value which is set into {@code objectToSet}
     * @param <G> - type of value {@code objectToGet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <S,G> MapperBuilder<R> withSetter(SetterFunction<R,S> setterFunction, G objectToGet, GetterFunction<G,S> getterFunction){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(this.destinationObject,setterFunction,getterFunction.apply(objectToGet))));
        return this;
    }

    /**
     * add processing of {@code setterFunction} method of object {@link this#destinationObject} with input parameter as {@code valueToSet}
     *
     * @param setterFunction - function which is call for setting of exact value of object {@code objectToSet}
     * @param valueToSet - input parameter into method {@code setterFunction}
     * @param <S> - type of value which is set into {@code objectToSet}
     *
     * @return this instance of {@link MapperBuilder}
     */
    public <S> MapperBuilder<R> withSetter(SetterFunction<R,S> setterFunction, S valueToSet){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(new SetterRepresentation(this.destinationObject,setterFunction,valueToSet)));
        return this;
    }

    /**
     * start processing with condition. Condition is evaluated according {@code conditionFunction}
     *
     * @param conditionFunction
     *
     * @return this instance of {@link MapperBuilder}
     */
    public MapperBuilder<R> whitStartCondition(ConditionFunction conditionFunction){
        processingFunctions.add(FunctionWrapper.ofFunctionWrap(conditionFunction));
        return this;
    }

    /**
     * end condition processing
     *
     * @return this instance of {@link MapperBuilder}
     */
    public MapperBuilder<R> whitEndCondition(){
        processingFunctions.add(FunctionWrapper.ofEndCondition());
        return this;
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
            if(nextFunctionWrapper.isEndCondition()){
                applyWhitCondition(true,functionIterator);
            } else {
                applyWhitCondition(conditionEvaluationValue, functionIterator);
            }
        } else {
            if (nextFunctionWrapper.isSetterRepresentation()) {
                // in case of SetterRepresentation process setter function and continue processing
                ((SetterRepresentation) nextFunctionWrapper.getProcessingFunction()).process();
                applyWhitCondition(conditionEvaluationValue, functionIterator);
            } else if (nextFunctionWrapper.isConditionFunction()) {
                // in case of ConditionFunction evaluate condition and continue processing
                applyWhitCondition(((ConditionFunction) nextFunctionWrapper.getProcessingFunction()).test(), functionIterator);
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
     * @param <S> the type of value which is set into object {@link SetterRepresentation#objectToSet} within function
     * {@link SetterRepresentation#setterFunction}
     */
    private class SetterRepresentation<S> implements ProcessingFunction{
        private R objectToSet;
        private SetterFunction<R,S> setterFunction;
        private S valueToSet;

        public SetterRepresentation(R objectToSet, SetterFunction<R, S> setterFunction, S valueToSet) {
            this.objectToSet = objectToSet;
            this.setterFunction = setterFunction;
            this.valueToSet = valueToSet;
        }

        /**
         * invoke setter function and return object to set
         *
         * @return - instance of object of type {@code S}
         */
        public R process(){
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

        public boolean isConditionFunction(){
            return processingFunction!=null && processingFunction instanceof ConditionFunction;
        }

        public boolean isSetterRepresentation(){
            return processingFunction!=null && processingFunction instanceof MapperBuilder.SetterRepresentation;
        }

        public ProcessingFunction getProcessingFunction() {
            return processingFunction;
        }

        /**
         * create new FunctionWrapper object
         *
         * @param processingFunction
         * @return
         */
        public static FunctionWrapper ofFunctionWrap(ProcessingFunction processingFunction){
            return new FunctionWrapper(processingFunction);
        }

        /**
         * create new function wrapper represented the ending of condition
         * @return
         */
        public static FunctionWrapper ofEndCondition(){
            return new FunctionWrapper(true);
        }
    }
}
