// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.win32;

import com.sun.jna.ToNativeConverter;
import com.sun.jna.FromNativeContext;
import com.sun.jna.WString;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.DefaultTypeMapper;

public class W32APITypeMapper extends DefaultTypeMapper
{
    public static final TypeMapper UNICODE;
    public static final TypeMapper ASCII;
    public static final TypeMapper DEFAULT;
    
    protected W32APITypeMapper(final boolean unicode) {
        if (unicode) {
            final TypeConverter stringConverter = new TypeConverter() {
                @Override
                public Object toNative(final Object value, final ToNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    if (value instanceof String[]) {
                        return new StringArray((String[])value, true);
                    }
                    return new WString(value.toString());
                }
                
                @Override
                public Object fromNative(final Object value, final FromNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    return value.toString();
                }
                
                @Override
                public Class<?> nativeType() {
                    return WString.class;
                }
            };
            this.addTypeConverter(String.class, stringConverter);
            this.addToNativeConverter(String[].class, stringConverter);
        }
        final TypeConverter booleanConverter = new TypeConverter() {
            @Override
            public Object toNative(final Object value, final ToNativeContext context) {
                return Boolean.TRUE.equals(value) ? 1 : 0;
            }
            
            @Override
            public Object fromNative(final Object value, final FromNativeContext context) {
                return ((int)value != 0) ? Boolean.TRUE : Boolean.FALSE;
            }
            
            @Override
            public Class<?> nativeType() {
                return Integer.class;
            }
        };
        this.addTypeConverter(Boolean.class, booleanConverter);
    }
    
    static {
        UNICODE = new W32APITypeMapper(true);
        ASCII = new W32APITypeMapper(false);
        DEFAULT = (Boolean.getBoolean("w32.ascii") ? W32APITypeMapper.ASCII : W32APITypeMapper.UNICODE);
    }
}
