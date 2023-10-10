// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;

public class WrappedParameter
{
    private Parameter parameter;
    private DynamicParameter dynamicParameter;
    
    public WrappedParameter(final Parameter parameter) {
        this.parameter = parameter;
    }
    
    public WrappedParameter(final DynamicParameter dynamicParameter) {
        this.dynamicParameter = dynamicParameter;
    }
    
    public Parameter getParameter() {
        return this.parameter;
    }
    
    public DynamicParameter getDynamicParameter() {
        return this.dynamicParameter;
    }
    
    public int arity() {
        if (this.parameter != null) {
            return this.parameter.arity();
        }
        return 1;
    }
    
    public boolean hidden() {
        if (this.parameter != null) {
            return this.parameter.hidden();
        }
        return this.dynamicParameter.hidden();
    }
    
    public boolean required() {
        if (this.parameter != null) {
            return this.parameter.required();
        }
        return this.dynamicParameter.required();
    }
    
    public boolean password() {
        return this.parameter != null && this.parameter.password();
    }
    
    public String[] names() {
        if (this.parameter != null) {
            return this.parameter.names();
        }
        return this.dynamicParameter.names();
    }
    
    public boolean variableArity() {
        return this.parameter != null && this.parameter.variableArity();
    }
    
    public Class<? extends IParameterValidator>[] validateWith() {
        if (this.parameter != null) {
            return this.parameter.validateWith();
        }
        return this.dynamicParameter.validateWith();
    }
    
    public Class<? extends IValueValidator>[] validateValueWith() {
        if (this.parameter != null) {
            return this.parameter.validateValueWith();
        }
        return this.dynamicParameter.validateValueWith();
    }
    
    public boolean echoInput() {
        return this.parameter != null && this.parameter.echoInput();
    }
    
    public void addValue(final Parameterized parameterized, final Object obj, final Object obj2) {
        try {
            this.addValue(parameterized, obj, obj2, null);
        }
        catch (IllegalAccessException ex) {
            throw new ParameterException("Couldn't set " + obj + " to " + obj2, ex);
        }
    }
    
    public void addValue(final Parameterized parameterized, final Object obj, final Object value, final Field field) throws IllegalAccessException {
        if (this.parameter != null) {
            if (field != null) {
                field.set(obj, value);
                return;
            }
            parameterized.set(obj, value);
        }
        else {
            final String assignment = this.dynamicParameter.assignment();
            final String string;
            final int index;
            if ((index = (string = value.toString()).indexOf(assignment)) == -1) {
                throw new ParameterException("Dynamic parameter expected a value of the form a" + assignment + "b but got:" + string);
            }
            this.callPut(obj, parameterized, string.substring(0, index), string.substring(index + 1));
        }
    }
    
    private void callPut(final Object o, final Parameterized parameterized, final String s, final String s2) {
        try {
            this.findPut(parameterized.getType()).invoke(parameterized.get(o), s, s2);
        }
        catch (SecurityException ex2) {
            final InvocationTargetException ex;
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex3) {}
        catch (NoSuchMethodException ex4) {}
        catch (InvocationTargetException ex) {}
    }
    
    private Method findPut(final Class<?> clazz) throws SecurityException, NoSuchMethodException {
        return clazz.getMethod("put", Object.class, Object.class);
    }
    
    public String getAssignment() {
        if (this.dynamicParameter != null) {
            return this.dynamicParameter.assignment();
        }
        return "";
    }
    
    public boolean isHelp() {
        return this.parameter != null && this.parameter.help();
    }
    
    public boolean isNonOverwritableForced() {
        return this.parameter != null && this.parameter.forceNonOverwritable();
    }
}
