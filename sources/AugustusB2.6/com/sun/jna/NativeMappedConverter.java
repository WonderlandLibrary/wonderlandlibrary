// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.util.WeakHashMap;
import java.lang.ref.SoftReference;
import java.lang.ref.Reference;
import java.util.Map;

public class NativeMappedConverter implements TypeConverter
{
    private static final Map<Class<?>, Reference<NativeMappedConverter>> converters;
    private final Class<?> type;
    private final Class<?> nativeType;
    private final NativeMapped instance;
    
    public static NativeMappedConverter getInstance(final Class<?> cls) {
        synchronized (NativeMappedConverter.converters) {
            final Reference<NativeMappedConverter> r = NativeMappedConverter.converters.get(cls);
            NativeMappedConverter nmc = (r != null) ? r.get() : null;
            if (nmc == null) {
                nmc = new NativeMappedConverter(cls);
                NativeMappedConverter.converters.put(cls, new SoftReference<NativeMappedConverter>(nmc));
            }
            return nmc;
        }
    }
    
    public NativeMappedConverter(final Class<?> type) {
        if (!NativeMapped.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Type must derive from " + NativeMapped.class);
        }
        this.type = type;
        this.instance = this.defaultValue();
        this.nativeType = this.instance.nativeType();
    }
    
    public NativeMapped defaultValue() {
        if (this.type.isEnum()) {
            return (NativeMapped)this.type.getEnumConstants()[0];
        }
        return Klass.newInstance(this.type);
    }
    
    @Override
    public Object fromNative(final Object nativeValue, final FromNativeContext context) {
        return this.instance.fromNative(nativeValue, context);
    }
    
    @Override
    public Class<?> nativeType() {
        return this.nativeType;
    }
    
    @Override
    public Object toNative(Object value, final ToNativeContext context) {
        if (value == null) {
            if (Pointer.class.isAssignableFrom(this.nativeType)) {
                return null;
            }
            value = this.defaultValue();
        }
        return ((NativeMapped)value).toNative();
    }
    
    static {
        converters = new WeakHashMap<Class<?>, Reference<NativeMappedConverter>>();
    }
}
