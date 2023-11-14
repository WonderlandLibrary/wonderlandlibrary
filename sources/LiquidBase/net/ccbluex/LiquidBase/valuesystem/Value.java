package net.ccbluex.LiquidBase.valuesystem;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class Value<T> {

    private String valueName;
    private T valueObject;

    public Value(String valueName, T valueObject) {
        this.valueName = valueName;
        this.valueObject = valueObject;
    }

    public String getValueName() {
        return valueName;
    }

    public T getObject() {
        return valueObject;
    }

    public void setObject(T valueObject) {
        this.valueObject = valueObject;
    }
}
