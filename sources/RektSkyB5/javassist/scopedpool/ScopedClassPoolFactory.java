/*
 * Decompiled with CFR 0.152.
 */
package javassist.scopedpool;

import javassist.ClassPool;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolRepository;

public interface ScopedClassPoolFactory {
    public ScopedClassPool create(ClassLoader var1, ClassPool var2, ScopedClassPoolRepository var3);

    public ScopedClassPool create(ClassPool var1, ScopedClassPoolRepository var2);
}

