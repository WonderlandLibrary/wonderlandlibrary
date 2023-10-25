package net.optifine.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.optifine.Log;
import net.optifine.reflect.IFieldLocator;

public class FieldLocatorTypes
implements IFieldLocator {
    private Field field = null;

    public FieldLocatorTypes(Class cls, Class[] preTypes, Class type, Class[] postTypes, String errorName) {
        Field[] afield = cls.getDeclaredFields();
        ArrayList list = new ArrayList();
        for (int i = 0; i < afield.length; ++i) {
            Field field = afield[i];
            list.add((Object)field.getType());
        }
        ArrayList list1 = new ArrayList();
        list1.addAll((Collection)Arrays.asList((Object[])preTypes));
        list1.add((Object)type);
        list1.addAll((Collection)Arrays.asList((Object[])postTypes));
        int l = Collections.indexOfSubList((List)list, (List)list1);
        if (l < 0) {
            Log.log((String)("(Reflector) Field not found: " + errorName));
        } else {
            int j = Collections.indexOfSubList((List)list.subList(l + 1, list.size()), (List)list1);
            if (j >= 0) {
                Log.log((String)("(Reflector) More than one match found for field: " + errorName));
            } else {
                int k = l + preTypes.length;
                this.field = afield[k];
            }
        }
    }

    public Field getField() {
        return this.field;
    }
}
