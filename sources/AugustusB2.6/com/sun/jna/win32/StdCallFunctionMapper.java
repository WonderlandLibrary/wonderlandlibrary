// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.win32;

import com.sun.jna.Function;
import java.lang.reflect.Method;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Native;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeMapped;
import com.sun.jna.FunctionMapper;

public class StdCallFunctionMapper implements FunctionMapper
{
    protected int getArgumentNativeStackSize(Class<?> cls) {
        if (NativeMapped.class.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls.isArray()) {
            return Native.POINTER_SIZE;
        }
        try {
            return Native.getNativeSize(cls);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown native stack allocation size for " + cls);
        }
    }
    
    @Override
    public String getFunctionName(final NativeLibrary library, final Method method) {
        String name = method.getName();
        int pop = 0;
        final Class<?>[] parameterTypes;
        final Class<?>[] argTypes = parameterTypes = method.getParameterTypes();
        for (final Class<?> cls : parameterTypes) {
            pop += this.getArgumentNativeStackSize(cls);
        }
        final String decorated = name + "@" + pop;
        final int conv = 63;
        try {
            final Function func = library.getFunction(decorated, conv);
            name = func.getName();
        }
        catch (UnsatisfiedLinkError e) {
            try {
                final Function func2 = library.getFunction("_" + decorated, conv);
                name = func2.getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        return name;
    }
}
