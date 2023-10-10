// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.reflect;

import com.viaversion.viaversion.libs.gson.internal.JavaVersion;
import java.lang.reflect.AccessibleObject;

public abstract class ReflectionAccessor
{
    private static final ReflectionAccessor instance;
    
    public abstract void makeAccessible(final AccessibleObject p0);
    
    public static ReflectionAccessor getInstance() {
        return ReflectionAccessor.instance;
    }
    
    static {
        instance = ((JavaVersion.getMajorJavaVersion() < 9) ? new PreJava9ReflectionAccessor() : new UnsafeReflectionAccessor());
    }
}
