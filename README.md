# utilities
Various utilities for Java programming

## Mapper utility

The main purpose of this utility is mapping of attributes from class A to class B or mapping of particular values to class B

### Samples

```
    private static SimplePojoTest1 testSimplePojoTest1 = new SimplePojoTest1();
    static {
        testSimplePojoTest1.setIntParam1(1);
        testSimplePojoTest1.setStrParam1("param1");
        testSimplePojoTest1.setBoolParam1(true);
    }

    private static SimplePojoTestWitPrefix testSimplePojoTestWitPrefix1 = new SimplePojoTestWitPrefix();
    static {
        testSimplePojoTestWitPrefix1.setMyPrefixIntParam1(1);
        testSimplePojoTestWitPrefix1.setMyPrefixStrParam1("param1");
        testSimplePojoTestWitPrefix1.setMyPrefixBoolParam1(true);
        testSimplePojoTestWitPrefix1.setCustomParameter1("custom1");
    }

    // custom mapper
    private Function<SimplePojoTest1,SimplePojoTest2> MAPPER_SIMPLE_POJO_2 = (SimplePojoTest1 getterObject) -> {
        return MapperBuilder.createBuilder(SimplePojoTest2.class)
                .withSetter(SimplePojoTest2::setIntParam1, testSimplePojoTest1, SimplePojoTest1::getIntParam1)
                .withSetter(SimplePojoTest2::setStrParam1, testSimplePojoTest1, SimplePojoTest1::getStrParam1)
                .apply();
    };
```
The example show mapping from direct values into SomplePojoTest2
```
  // the object which you want to set
  SimplePojoTest2 simplePojoTest2 = new SimplePojoTest2();
  // create mapper builder
  MapperBuilder.createBuilder(SimplePojoTest2.class,simplePojoTest2)
        .withSetter(SimplePojoTest2::setIntParam1,1)
        .withSetter(SimplePojoTest2::setIntParam2,null)
        .withSetter(SimplePojoTest2::setStrParam1,"strParam1")
        .withSetter(SimplePojoTest2::setStrParam2,testSimplePojoTest1.getStrParam2())
        .apply();
```

The example show mapping from testSimplePojoTest1 into SomplePojoTest2
```
  // the object which you want to set
  SimplePojoTest2 simplePojoTest2 = new SimplePojoTest2();
  // create mapper builder
  MapperBuilder.createBuilder(SimplePojoTest2.class,simplePojoTest2)
        .withSetter(SimplePojoTest2::setIntParam1, testSimplePojoTest1, SimplePojoTest1::getIntParam1)
        .withSetter(SimplePojoTest2::setStrParam1, testSimplePojoTest1, SimplePojoTest1::getStrParam1)
        .apply();
```

The example show creating of Mapper as Functional interface and apply mapping of particular objects. The sample show using of condition mapping as well
```
  // custom mapper whit condition
  private BiFunction<SimplePojoTest1, ConditionFunction, SimplePojoTest2> MAPPER_SIMPLE_POJO_2_WITH_CONDITION = (SimplePojoTest1      getterObject, ConditionFunction setStrParam1) -> {
      return MapperBuilder.createBuilder(SimplePojoTest2.class)
        .withSetter(SimplePojoTest2::setIntParam1, testSimplePojoTest1, SimplePojoTest1::getIntParam1)
        .withStartCondition(setStrParam1)
          .withSetter(SimplePojoTest2::setStrParam1, testSimplePojoTest1, SimplePojoTest1::getStrParam1)
        .withEndCondition()
        .withSetter(SimplePojoTest2::setStrParam2, testSimplePojoTest1, SimplePojoTest1::getStrParam2)
        .apply();
    };
    
  // the condition is false, therefore the strParam1 has to be null
  SimplePojoTest2 simplePojoTest2 = MAPPER_SIMPLE_POJO_2_WITH_CONDITION.apply(testSimplePojoTest1,() -> "a".equals("b"));
```

This example show us simple reflection mapping. There is mapping from pojo SimplePojoTest into SimplePojoTest2
```
  SimplePojoTest2 simplePojoTest2 = ReflectionMapperBuilder
    .createReflectionBuilder(testSimplePojoTest1,SimplePojoTest1.class,SimplePojoTest2.class)
    .apply();
```

This example show us simple reflection mapping whit reference object passing. There is mapping from pojo SimplePojoTest into SimplePojoTest2
```
  SimplePojoTest2 simplePojoTest2 = new SimplePojoTest2();
  simplePojoTest2 = ReflectionMapperBuilder
    .createReflectionBuilder(testSimplePojoTest1,SimplePojoTest1.class,simplePojoTest2,SimplePojoTest2.class)
    .apply();
```

This example show us reflection mapping with setting of prefixies of destination and source class. There you see sample of additional mapping with reflection
```
  SimplePojoTestWithPrefixAndSuffix result = ReflectionMapperBuilder
    .createReflectionBuilder(
      testSimplePojoTestWitPrefix1,
      SimplePojoTestWitPrefix.class,
      SimplePojoTestWithPrefixAndSuffix.class,new MapperOptions()
        .setDestinationObjectFieldPrefix("myPrefix")
        .setDestinationObjectFieldSuffixe("MySuffix")
        .setSourceObjectFieldPrefix("myPrefix"))
    .withSetter(SimplePojoTestWithPrefixAndSuffix::setCustomParameter2,testSimplePojoTestWitPrefix1.getCustomParameter1())
    .apply();
```

This example show simple reflection mapping with excluding of few fields.
The field intParam1 will exclude because of configuration
The field iniParam2 will exclude because condition in configuration has positive result
The field strParam1 will include because condition in configuration has negative result
```
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
```
