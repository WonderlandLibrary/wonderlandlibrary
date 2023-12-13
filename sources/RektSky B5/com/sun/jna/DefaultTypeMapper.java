/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.FromNativeConverter;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultTypeMapper
implements TypeMapper {
    private List toNativeConverters = new ArrayList();
    private List fromNativeConverters = new ArrayList();

    private Class getAltClass(Class cls) {
        if (cls == Boolean.class) {
            return Boolean.TYPE;
        }
        if (cls == Boolean.TYPE) {
            return Boolean.class;
        }
        if (cls == Byte.class) {
            return Byte.TYPE;
        }
        if (cls == Byte.TYPE) {
            return Byte.class;
        }
        if (cls == Character.class) {
            return Character.TYPE;
        }
        if (cls == Character.TYPE) {
            return Character.class;
        }
        if (cls == Short.class) {
            return Short.TYPE;
        }
        if (cls == Short.TYPE) {
            return Short.class;
        }
        if (cls == Integer.class) {
            return Integer.TYPE;
        }
        if (cls == Integer.TYPE) {
            return Integer.class;
        }
        if (cls == Long.class) {
            return Long.TYPE;
        }
        if (cls == Long.TYPE) {
            return Long.class;
        }
        if (cls == Float.class) {
            return Float.TYPE;
        }
        if (cls == Float.TYPE) {
            return Float.class;
        }
        if (cls == Double.class) {
            return Double.TYPE;
        }
        if (cls == Double.TYPE) {
            return Double.class;
        }
        return null;
    }

    public void addToNativeConverter(Class cls, ToNativeConverter converter) {
        this.toNativeConverters.add(new Entry(cls, converter));
        Class alt = this.getAltClass(cls);
        if (alt != null) {
            this.toNativeConverters.add(new Entry(alt, converter));
        }
    }

    public void addFromNativeConverter(Class cls, FromNativeConverter converter) {
        this.fromNativeConverters.add(new Entry(cls, converter));
        Class alt = this.getAltClass(cls);
        if (alt != null) {
            this.fromNativeConverters.add(new Entry(alt, converter));
        }
    }

    protected void addTypeConverter(Class cls, TypeConverter converter) {
        this.addFromNativeConverter(cls, converter);
        this.addToNativeConverter(cls, converter);
    }

    private Object lookupConverter(Class javaClass, List converters) {
        Iterator i2 = converters.iterator();
        while (i2.hasNext()) {
            Entry entry = (Entry)i2.next();
            if (!entry.type.isAssignableFrom(javaClass)) continue;
            return entry.converter;
        }
        return null;
    }

    public FromNativeConverter getFromNativeConverter(Class javaType) {
        return (FromNativeConverter)this.lookupConverter(javaType, this.fromNativeConverters);
    }

    public ToNativeConverter getToNativeConverter(Class javaType) {
        return (ToNativeConverter)this.lookupConverter(javaType, this.toNativeConverters);
    }

    private static class Entry {
        public Class type;
        public Object converter;

        public Entry(Class type, Object converter) {
            this.type = type;
            this.converter = converter;
        }
    }
}

