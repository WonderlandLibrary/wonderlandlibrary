/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.Pointer;
import java.lang.reflect.Method;

public class StdCallFunctionMapper
implements FunctionMapper {
    protected int getArgumentNativeStackSize(Class cls) {
        if (NativeMapped.class.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls.isArray()) {
            return Pointer.SIZE;
        }
        try {
            return Native.getNativeSize(cls);
        }
        catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("Unknown native stack allocation size for " + cls);
        }
    }

    public String getFunctionName(NativeLibrary library, Method method) {
        String name = method.getName();
        int pop = 0;
        Class<?>[] argTypes = method.getParameterTypes();
        for (int i2 = 0; i2 < argTypes.length; ++i2) {
            pop += this.getArgumentNativeStackSize(argTypes[i2]);
        }
        String decorated = name + "@" + pop;
        int conv = 1;
        try {
            name = library.getFunction(decorated, conv).getName();
        }
        catch (UnsatisfiedLinkError e2) {
            try {
                name = library.getFunction("_" + decorated, conv).getName();
            }
            catch (UnsatisfiedLinkError e22) {
                // empty catch block
            }
        }
        return name;
    }
}

