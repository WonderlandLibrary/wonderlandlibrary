// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.scopedpool;

import java.util.Map;
import com.viaversion.viaversion.libs.javassist.ClassPool;

public interface ScopedClassPoolRepository
{
    void setClassPoolFactory(final ScopedClassPoolFactory p0);
    
    ScopedClassPoolFactory getClassPoolFactory();
    
    boolean isPrune();
    
    void setPrune(final boolean p0);
    
    ScopedClassPool createScopedClassPool(final ClassLoader p0, final ClassPool p1);
    
    ClassPool findClassPool(final ClassLoader p0);
    
    ClassPool registerClassLoader(final ClassLoader p0);
    
    Map<ClassLoader, ScopedClassPool> getRegisteredCLs();
    
    void clearUnregisteredClassLoaders();
    
    void unregisterClassLoader(final ClassLoader p0);
}
