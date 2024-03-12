// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import net.optifine.Log;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class FieldLocatorTypes implements IFieldLocator
{
    private Field field;
    
    public FieldLocatorTypes(final Class cls, final Class[] preTypes, final Class type, final Class[] postTypes, final String errorName) {
        this.field = null;
        final Field[] afield = cls.getDeclaredFields();
        final List<Class> list = new ArrayList<Class>();
        for (int i = 0; i < afield.length; ++i) {
            final Field field = afield[i];
            list.add(field.getType());
        }
        final List<Class> list2 = new ArrayList<Class>();
        list2.addAll(Arrays.asList((Class[])preTypes));
        list2.add(type);
        list2.addAll(Arrays.asList((Class[])postTypes));
        final int l = Collections.indexOfSubList(list, list2);
        if (l < 0) {
            Log.log("(Reflector) Field not found: " + errorName);
        }
        else {
            final int j = Collections.indexOfSubList(list.subList(l + 1, list.size()), list2);
            if (j >= 0) {
                Log.log("(Reflector) More than one match found for field: " + errorName);
            }
            else {
                final int k = l + preTypes.length;
                this.field = afield[k];
            }
        }
    }
    
    @Override
    public Field getField() {
        return this.field;
    }
}
