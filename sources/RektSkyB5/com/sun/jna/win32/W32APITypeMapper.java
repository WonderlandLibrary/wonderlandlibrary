/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  [Ljava.lang.String;
 */
package com.sun.jna.win32;

import [Ljava.lang.String;;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.FromNativeContext;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;

public class W32APITypeMapper
extends DefaultTypeMapper {
    public static final TypeMapper UNICODE = new W32APITypeMapper(true);
    public static final TypeMapper ASCII = new W32APITypeMapper(false);
    static /* synthetic */ Class class$com$sun$jna$WString;
    static /* synthetic */ Class class$java$lang$Integer;

    protected W32APITypeMapper(boolean unicode) {
        if (unicode) {
            TypeConverter stringConverter = new TypeConverter(){

                public Object toNative(Object value, ToNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    if (value instanceof String[]) {
                        return new StringArray((String[])value, true);
                    }
                    return new WString(value.toString());
                }

                public Object fromNative(Object value, FromNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    return value.toString();
                }

                public Class nativeType() {
                    return class$com$sun$jna$WString == null ? (class$com$sun$jna$WString = W32APITypeMapper.class$("com.sun.jna.WString")) : class$com$sun$jna$WString;
                }
            };
            this.addTypeConverter(String.class, stringConverter);
            this.addToNativeConverter(String;.class, stringConverter);
        }
        TypeConverter booleanConverter = new TypeConverter(){

            public Object toNative(Object value, ToNativeContext context) {
                return new Integer(Boolean.TRUE.equals(value) ? 1 : 0);
            }

            public Object fromNative(Object value, FromNativeContext context) {
                return (Integer)value != 0 ? Boolean.TRUE : Boolean.FALSE;
            }

            public Class nativeType() {
                return class$java$lang$Integer == null ? (class$java$lang$Integer = W32APITypeMapper.class$("java.lang.Integer")) : class$java$lang$Integer;
            }
        };
        this.addTypeConverter(Boolean.class, booleanConverter);
    }
}

