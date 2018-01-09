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
The Example show mapping from direct values into SomplePojoTest2
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
