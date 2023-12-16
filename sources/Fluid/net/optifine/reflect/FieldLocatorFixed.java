// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.reflect;

import java.lang.reflect.Field;

public class FieldLocatorFixed implements IFieldLocator
{
    private Field field;
    
    public FieldLocatorFixed(final Field field) {
        this.field = field;
    }
    
    @Override
    public Field getField() {
        return this.field;
    }
}
