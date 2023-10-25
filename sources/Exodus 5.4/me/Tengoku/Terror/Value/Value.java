/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.Value;

import me.Tengoku.Terror.Value.ValueManager;

public class Value {
    private float minFloatValue;
    private String stringValue;
    private String selectedOption;
    private int intValue;
    private float floatValue;
    private Object type;
    private String valType;
    private String[] options;
    private float increment;
    private int maxInt;
    private int minIntValue;
    private float maxFloat;
    private boolean booleanValue;
    private String name;

    public void setMaxInt(int n) {
        this.maxInt = n;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public float getFloatValue() {
        return this.floatValue;
    }

    public void setSelectedOption(String string) {
        this.selectedOption = string;
    }

    public float getIncrement() {
        return this.increment;
    }

    public void setMinIntValue(int n) {
        this.minIntValue = n;
    }

    public void setBooleanValue(boolean bl) {
        this.booleanValue = bl;
    }

    public String[] getOptions() {
        return this.options;
    }

    public void setIntValue(int n) {
        this.intValue = n;
    }

    public float getMinFloatValue() {
        return this.minFloatValue;
    }

    public int getMaxInt() {
        return this.maxInt;
    }

    public String getSelectedOption() {
        return this.selectedOption;
    }

    public String getName() {
        return this.name;
    }

    public void setIncrement(float f) {
        this.increment = f;
    }

    public Value(String string, Object object, String string2, String string3, String[] stringArray) {
        this.setName(string2);
        this.setSelectedOption(string3);
        this.setOptions(stringArray);
        this.setType(object);
        this.setValType(string);
        ValueManager.values.add(this);
    }

    public void setType(Object object) {
        this.type = object;
    }

    public Value(Object object, String string, String string2) {
        this.setName(string);
        this.setStringValue(string2);
        this.setType(object);
        this.setValType(this.valType);
        ValueManager.values.add(this);
    }

    public void setMinFloatValue(float f) {
        this.minFloatValue = f;
    }

    public int getMinIntValue() {
        return this.minIntValue;
    }

    public void setMaxFloat(float f) {
        this.maxFloat = f;
    }

    public Value(String string, Object object, String string2, float f, float f2, float f3, float f4) {
        this.setName(string2);
        this.setType(object);
        this.setValType(string);
        this.setFloatValue(f);
        this.setMinFloatValue(f2);
        this.setMaxFloat(f3);
        this.setIncrement(f4);
        ValueManager.values.add(this);
    }

    public void setValType(String string) {
        this.valType = string;
    }

    public void setStringValue(String string) {
        this.stringValue = string;
    }

    public boolean getBooleanValue() {
        return this.booleanValue;
    }

    public String getValType() {
        return this.valType;
    }

    public void setOptions(String[] stringArray) {
        this.options = stringArray;
    }

    public Value(String string, Object object, String string2, boolean bl) {
        this.setName(string2);
        this.setBooleanValue(bl);
        this.setType(object);
        this.setValType(string);
        ValueManager.values.add(this);
    }

    public void setName(String string) {
        this.name = string;
    }

    public Value(String string, Object object, String string2, int n, int n2, int n3, float f) {
        this.setName(string2);
        this.setIntValue(n);
        this.setMinIntValue(n2);
        this.setMaxInt(n3);
        this.setType(object);
        this.setValType(string);
        this.setIncrement(f);
        ValueManager.values.add(this);
    }

    public void setFloatValue(float f) {
        this.floatValue = f;
    }

    public Object getType() {
        return this.type;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public float getMaxFloat() {
        return this.maxFloat;
    }
}

