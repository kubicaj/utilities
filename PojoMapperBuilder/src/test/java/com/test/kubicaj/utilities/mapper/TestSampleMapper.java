package com.test.kubicaj.utilities.mapper;

import com.kubicaj.utilities.pojomapper.MapperBuilder;
import com.kubicaj.utilities.pojomapper.ReflectionMapperBuilder;
import com.kubicaj.utilities.pojomapper.function.ConditionFunction;
import com.kubicaj.utilities.pojomapper.options.MapperOptions;
import com.test.kubicaj.utilities.mapper.pojoObjects.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * test + samples how to use Mapper
 */
public class TestSampleMapper {

    // -----------------------------------------------------------------------------------------------------------------
    // INIT OBJECTS/ATTRIBUTES SECTION
    // -----------------------------------------------------------------------------------------------------------------

    // test object
    private static SimplePojoTest1 testSimplePojoTest1 = new SimplePojoTest1();
    static {
        testSimplePojoTest1.setIntParam1(1);
        testSimplePojoTest1.setIntParam2(2);
        testSimplePojoTest1.setStrParam1("param1");
        testSimplePojoTest1.setStrParam2("param2");
        testSimplePojoTest1.setBoolParam1(true);
    }

    private static SimplePojoWithInternalObject1 simplePojoWithInternalObject1 = new SimplePojoWithInternalObject1();
    static {
        simplePojoWithInternalObject1.setStrParam1("strParam1");
        simplePojoWithInternalObject1.setSimplePojoTest1(testSimplePojoTest1);
    }


    private static SimplePojoTestWitPrefix testSimplePojoTestWithPrefix1 = new SimplePojoTestWitPrefix();
    static {
        testSimplePojoTestWithPrefix1.setMyPrefixIntParam1(1);
        testSimplePojoTestWithPrefix1.setMyPrefixIntParam2(2);
        testSimplePojoTestWithPrefix1.setMyPrefixStrParam1("param1");
        testSimplePojoTestWithPrefix1.setMyPrefixStrParam2("param2");
        testSimplePojoTestWithPrefix1.setMyPrefixBoolParam1(true);
        testSimplePojoTestWithPrefix1.setCustomParameter1("custom1");
    }

    // custom mapper
    private Function<SimplePojoTest1,SimplePojoTest2> MAPPER_SIMPLE_POJO_2 = (SimplePojoTest1 getterObject) -> {
        return MapperBuilder.createBuilder(SimplePojoTest2.class)
                .withSetter(SimplePojoTest2::setIntParam1, testSimplePojoTest1, SimplePojoTest1::getIntParam1)
                .withSetter(SimplePojoTest2::setIntParam2, testSimplePojoTest1, SimplePojoTest1::getIntParam2)
                .withSetter(SimplePojoTest2::setStrParam1, testSimplePojoTest1, SimplePojoTest1::getStrParam1)
                .withSetter(SimplePojoTest2::setStrParam2, testSimplePojoTest1, SimplePojoTest1::getStrParam2)
                .apply();
    };

    // custom mapper whit condition
    private BiFunction<SimplePojoTest1, ConditionFunction, SimplePojoTest2> MAPPER_SIMPLE_POJO_2_WITH_CONDITION = (SimplePojoTest1 getterObject, ConditionFunction setStrParam1) -> {
        return MapperBuilder.createBuilder(SimplePojoTest2.class)
                .withSetter(SimplePojoTest2::setIntParam1, testSimplePojoTest1, SimplePojoTest1::getIntParam1)
                .withSetter(SimplePojoTest2::setIntParam2, testSimplePojoTest1, SimplePojoTest1::getIntParam2)
                .withStartCondition(setStrParam1)
                    .withSetter(SimplePojoTest2::setStrParam1, testSimplePojoTest1, SimplePojoTest1::getStrParam1)
                .withEndCondition()
                .withSetter(SimplePojoTest2::setStrParam2, testSimplePojoTest1, SimplePojoTest1::getStrParam2)
                .apply();
    };

    // -----------------------------------------------------------------------------------------------------------------
    // TEST METHOD SECTION
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * test/sample of {@link MapperBuilder} whit settings of direct mapping
     */
    @Test
    public void sampleTestMapperWithDirectValues(){
        // the object which you want to set
        SimplePojoTest2 simplePojoTest2 = new SimplePojoTest2();
        // create mapper builder
        MapperBuilder.createBuilder(SimplePojoTest2.class,simplePojoTest2)
                .withSetter(SimplePojoTest2::setIntParam1,1)
                .withSetter(SimplePojoTest2::setIntParam2,null)
                .withSetter(SimplePojoTest2::setStrParam1,"strParam1")
                .withSetter(SimplePojoTest2::setStrParam2,testSimplePojoTest1.getStrParam2())
                .apply();
        // check result (check external builder object)
        Assert.assertEquals(simplePojoTest2.getIntParam1(),1);
        Assert.assertEquals(simplePojoTest2.getIntParam2(),null);
        Assert.assertEquals(simplePojoTest2.getStrParam1(),"strParam1");
        Assert.assertEquals(simplePojoTest2.getStrParam2(), testSimplePojoTest1.getStrParam2());

        // ***** Test case - builder return new object

        // create mapper builder
        simplePojoTest2 = MapperBuilder.createBuilder(SimplePojoTest2.class)
                .withSetter(SimplePojoTest2::setIntParam1,2)
                .withSetter(SimplePojoTest2::setIntParam2,null)
                .withSetter(SimplePojoTest2::setStrParam1,"strParam3")
                .withSetter(SimplePojoTest2::setStrParam2,testSimplePojoTest1.getStrParam2())
                .apply();
        // check result (check external builder object)
        Assert.assertEquals(simplePojoTest2.getIntParam1(),2);
        Assert.assertEquals(simplePojoTest2.getIntParam2(),null);
        Assert.assertEquals(simplePojoTest2.getStrParam1(),"strParam3");
        Assert.assertEquals(simplePojoTest2.getStrParam2(), testSimplePojoTest1.getStrParam2());
    }

    /**
     * test/sample of {@link MapperBuilder} whit settings of other object
     */
    @Test
    public void sampleTestMapperWithOtherObjectMethodReference(){
        // the object which you want to set
        SimplePojoTest2 simplePojoTest2 = new SimplePojoTest2();
        // create mapper builder
        MapperBuilder.createBuilder(SimplePojoTest2.class,simplePojoTest2)
                .withSetter(SimplePojoTest2::setIntParam1, testSimplePojoTest1, SimplePojoTest1::getIntParam1)
                .withSetter(SimplePojoTest2::setIntParam2, testSimplePojoTest1, SimplePojoTest1::getIntParam2)
                .withSetter(SimplePojoTest2::setStrParam1, testSimplePojoTest1, SimplePojoTest1::getStrParam1)
                .withSetter(SimplePojoTest2::setStrParam2, testSimplePojoTest1, SimplePojoTest1::getStrParam2)
                .apply();
        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTest1
        Assert.assertEquals(simplePojoTest2.getIntParam1(), testSimplePojoTest1.getIntParam1());
        Assert.assertEquals(simplePojoTest2.getIntParam2(), testSimplePojoTest1.getIntParam2());
        Assert.assertEquals(simplePojoTest2.getStrParam1(), testSimplePojoTest1.getStrParam1());
        Assert.assertEquals(simplePojoTest2.getStrParam2(), testSimplePojoTest1.getStrParam2());
    }

    /**
     * test/sample of {@link MapperBuilder} as functional interface
     */
    @Test
    public void sampleTestMapperAsFunctionalInterface(){
        SimplePojoTest2 simplePojoTest2 = MAPPER_SIMPLE_POJO_2.apply(testSimplePojoTest1);
        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTest1
        Assert.assertEquals(simplePojoTest2.getIntParam1(), testSimplePojoTest1.getIntParam1());
        Assert.assertEquals(simplePojoTest2.getIntParam2(), testSimplePojoTest1.getIntParam2());
        Assert.assertEquals(simplePojoTest2.getStrParam1(), testSimplePojoTest1.getStrParam1());
        Assert.assertEquals(simplePojoTest2.getStrParam2(), testSimplePojoTest1.getStrParam2());
    }

    /**
     * test/sample of {@link MapperBuilder} as functional interface
     */
    @Test
    public void sampleTestMapperAsFunctionalInterfaceWithCondition(){
        // the condition is false, therefore the strParam1 has to be null
        SimplePojoTest2 simplePojoTest2 = MAPPER_SIMPLE_POJO_2_WITH_CONDITION.apply(testSimplePojoTest1,() -> "a".equals("b"));
        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTest1
        Assert.assertEquals(simplePojoTest2.getStrParam1(), null);

        // the condition is true, therefore the strParam1 has to be the same as testSimplePojoTest1#getStrParam1
        SimplePojoTest2 simplePojoTest3 = MAPPER_SIMPLE_POJO_2_WITH_CONDITION.apply(testSimplePojoTest1,() -> "b".equals("b"));
        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTest1
        Assert.assertEquals(simplePojoTest3.getStrParam1(), testSimplePojoTest1.getStrParam1());
    }

    /**
     * test simple reflection mapping
     */
    @Test
    public void testReflectionSimpleMapping(){
        SimplePojoTest2 simplePojoTest2 = ReflectionMapperBuilder
                .createReflectionBuilder(
                        testSimplePojoTest1,
                        SimplePojoTest1.class,
                        SimplePojoTest2.class)
                .apply();
        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTest1
        Assert.assertEquals(simplePojoTest2.getIntParam1(), testSimplePojoTest1.getIntParam1());
        Assert.assertEquals(simplePojoTest2.getIntParam2(), testSimplePojoTest1.getIntParam2());
        Assert.assertEquals(simplePojoTest2.getStrParam1(), testSimplePojoTest1.getStrParam1());
        Assert.assertEquals(simplePojoTest2.getStrParam2(), testSimplePojoTest1.getStrParam2());
    }

    /**
     * test simple reflection mapping
     */
    @Test
    public void testReflectionSimpleMappingWithDestinationReferencePassing(){
        SimplePojoTest2 simplePojoTest2 = new SimplePojoTest2();
        ReflectionMapperBuilder
                .createReflectionBuilder(
                        testSimplePojoTest1,
                        SimplePojoTest1.class,
                        simplePojoTest2,
                        SimplePojoTest2.class)
                .apply();
        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTest1
        Assert.assertEquals(simplePojoTest2.getIntParam1(), testSimplePojoTest1.getIntParam1());
        Assert.assertEquals(simplePojoTest2.getIntParam2(), testSimplePojoTest1.getIntParam2());
        Assert.assertEquals(simplePojoTest2.getStrParam1(), testSimplePojoTest1.getStrParam1());
        Assert.assertEquals(simplePojoTest2.getStrParam2(), testSimplePojoTest1.getStrParam2());
    }

    /**
     * test with excluding fields
     */
    @Test
    public void testReflectionWithExcludingFields(){
        SimplePojoTest2 simplePojoTest2 = new SimplePojoTest2();
        simplePojoTest2.setIntParam1(-10);
        simplePojoTest2.setIntParam2(-11);
        simplePojoTest2.setStrParam1("testB");
        ReflectionMapperBuilder
                .createReflectionBuilder(
                        testSimplePojoTest1,
                        SimplePojoTest1.class,
                        simplePojoTest2,
                        SimplePojoTest2.class,
                        new MapperOptions()
                                .addExcludingField("intParam1")
                                .addConditionalExcludingField("intParam2",() -> "a".equals("a"))
                                .addConditionalExcludingField("strParam1",() -> "a".equals("c")))
                .apply();
        // check result
        Assert.assertEquals(simplePojoTest2.getIntParam1(), -10);
        Assert.assertEquals(simplePojoTest2.getIntParam2(), Integer.valueOf(-11));
        Assert.assertEquals(simplePojoTest2.getStrParam1(), testSimplePojoTest1.getStrParam1());
        Assert.assertEquals(simplePojoTest2.getStrParam2(), testSimplePojoTest1.getStrParam2());
    }

    /**
     * test reflection mapping with prefixes and suffixes
     */
    @Test
    public void testReflectionSimpleMappingWithPrefixesAndSuffixes(){
        SimplePojoTestWithPrefixAndSuffix result = ReflectionMapperBuilder
                .createReflectionBuilder(
                        testSimplePojoTestWithPrefix1,
                        SimplePojoTestWitPrefix.class,
                        SimplePojoTestWithPrefixAndSuffix.class,
                        new MapperOptions()
                                .setDestinationObjectFieldPrefix("myPrefix")
                                .setDestinationObjectFieldSuffixe("MySuffix")
                                .setSourceObjectFieldPrefix("myPrefix"))
                .apply();

        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTestWithPrefix1
        Assert.assertEquals(result.getMyPrefixIntParam1MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixIntParam1());
        Assert.assertEquals(result.getMyPrefixIntParam2MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixIntParam2());
        Assert.assertEquals(result.getMyPrefixStrParam1MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixStrParam1());
        Assert.assertEquals(result.getMyPrefixStrParam2MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixStrParam2());
        Assert.assertEquals(result.getMyPrefixBoolParam2MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixBoolParam2());
        Assert.assertEquals(result.isMyPrefixBoolParam1MySuffix(), testSimplePojoTestWithPrefix1.isMyPrefixBoolParam1());
    }

    @Test
    public void testReflectionSimpleMappingWithPrefixesAndSuffixesWithAdditionlCustomMapping(){
        SimplePojoTestWithPrefixAndSuffix result = ReflectionMapperBuilder
                .createReflectionBuilder(
                        testSimplePojoTestWithPrefix1,
                        SimplePojoTestWitPrefix.class,
                        SimplePojoTestWithPrefixAndSuffix.class,
                        new MapperOptions()
                                .setDestinationObjectFieldPrefix("myPrefix")
                                .setDestinationObjectFieldSuffixe("MySuffix")
                                .setSourceObjectFieldPrefix("myPrefix"))
                .withSetter(SimplePojoTestWithPrefixAndSuffix::setCustomParameter2, testSimplePojoTestWithPrefix1.getCustomParameter1())
                .apply();

        // check result - attrs values of simplePojoTest2 has to be equal with attrs values of testSimplePojoTestWithPrefix1
        Assert.assertEquals(result.getMyPrefixIntParam1MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixIntParam1());
        Assert.assertEquals(result.getMyPrefixIntParam2MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixIntParam2());
        Assert.assertEquals(result.getMyPrefixStrParam1MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixStrParam1());
        Assert.assertEquals(result.getMyPrefixStrParam2MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixStrParam2());
        Assert.assertEquals(result.getMyPrefixBoolParam2MySuffix(), testSimplePojoTestWithPrefix1.getMyPrefixBoolParam2());
        Assert.assertEquals(result.isMyPrefixBoolParam1MySuffix(), testSimplePojoTestWithPrefix1.isMyPrefixBoolParam1());
        // check custom parameter
        Assert.assertEquals(result.getCustomParameter2(), testSimplePojoTestWithPrefix1.getCustomParameter1());
    }

    @Test
    public void testMappingOfInternalObject(){
        SimplePojoWithInternalObject2 simplePojoWithInternalObject2 = new SimplePojoWithInternalObject2();
        ReflectionMapperBuilder.createReflectionBuilder(
                simplePojoWithInternalObject1, SimplePojoWithInternalObject1.class,
                simplePojoWithInternalObject2, SimplePojoWithInternalObject2.class)
                .withInternalClassMapper(SimplePojoTest1.class,ReflectionMapperBuilder.createReflectionBuilder(SimplePojoTest1.class,SimplePojoTest1.class))
                .withInternalFieldMapper("simplePojoTest2",ReflectionMapperBuilder.createReflectionBuilder(simplePojoWithInternalObject1.getSimplePojoTest1(),SimplePojoTest1.class,SimplePojoTest2.class))
        .apply();

        // assert first level attribute
        Assert.assertEquals(simplePojoWithInternalObject2.getStrParam1(), simplePojoWithInternalObject1.getStrParam1());

        // check result of simple pojo test1
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest1().getIntParam1(), simplePojoWithInternalObject1.getSimplePojoTest1().getIntParam1());
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest1().getIntParam2(), simplePojoWithInternalObject1.getSimplePojoTest1().getIntParam2());
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest1().getStrParam1(), simplePojoWithInternalObject1.getSimplePojoTest1().getStrParam1());
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest1().getStrParam2(), simplePojoWithInternalObject1.getSimplePojoTest1().getStrParam2());

        // check result of simple pojo test2
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest2().getIntParam1(), simplePojoWithInternalObject1.getSimplePojoTest1().getIntParam1());
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest2().getIntParam2(), simplePojoWithInternalObject1.getSimplePojoTest1().getIntParam2());
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest2().getStrParam1(), simplePojoWithInternalObject1.getSimplePojoTest1().getStrParam1());
        Assert.assertEquals(simplePojoWithInternalObject2.getSimplePojoTest2().getStrParam2(), simplePojoWithInternalObject1.getSimplePojoTest1().getStrParam2());

    }
}
