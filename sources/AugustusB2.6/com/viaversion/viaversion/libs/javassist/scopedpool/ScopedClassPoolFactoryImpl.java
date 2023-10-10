// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.scopedpool;

import com.viaversion.viaversion.libs.javassist.ClassPool;

public class ScopedClassPoolFactoryImpl implements ScopedClassPoolFactory
{
    @Override
    public ScopedClassPool create(final ClassLoader cl, final ClassPool src, final ScopedClassPoolRepository repository) {
        return new ScopedClassPool(cl, src, repository, false);
    }
    
    @Override
    public ScopedClassPool create(final ClassPool src, final ScopedClassPoolRepository repository) {
        return new ScopedClassPool(null, src, repository, true);
    }
}
