package com.test.kubicaj.utilities.pojomapper.pojoObjects;

public class SimplePojoWithInternalObject2 {

    private SimplePojoTest1 simplePojoTest1;
    private SimplePojoTest2 simplePojoTest2;
    private String strParam1;

    public SimplePojoTest1 getSimplePojoTest1() {
        return simplePojoTest1;
    }

    public void setSimplePojoTest1(SimplePojoTest1 simplePojoTest1) {
        this.simplePojoTest1 = simplePojoTest1;
    }

    public String getStrParam1() {
        return strParam1;
    }

    public void setStrParam1(String strParam1) {
        this.strParam1 = strParam1;
    }

    public SimplePojoTest2 getSimplePojoTest2() {
        return simplePojoTest2;
    }

    public void setSimplePojoTest2(SimplePojoTest2 simplePojoTest2) {
        this.simplePojoTest2 = simplePojoTest2;
    }
}
