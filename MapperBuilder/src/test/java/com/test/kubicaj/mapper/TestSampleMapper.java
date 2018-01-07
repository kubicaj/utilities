package com.test.kubicaj.mapper;

import com.kubicaj.mapper.MapperBuilder;
import com.test.kubicaj.mapper.pojoObjects.SimplePojoTest1;
import com.test.kubicaj.mapper.pojoObjects.SimplePojoTest2;
import org.junit.Assert;
import org.junit.Test;

/**
 * test + samples how to use Mapper
 */
public class TestSampleMapper {

    // -----------------------------------------------------------------------------------------------------------------
    // INIT OBJECTS/ATTRIBUTES SECTION
    // -----------------------------------------------------------------------------------------------------------------

    private static SimplePojoTest1 testSimplePojoTest1 = new SimplePojoTest1();
    static {
        testSimplePojoTest1.setIntParam1(1);
        testSimplePojoTest1.setIntParam2(2);
        testSimplePojoTest1.setStrParam1("param1");
        testSimplePojoTest1.setStrParam2("param2");
    }

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
}
