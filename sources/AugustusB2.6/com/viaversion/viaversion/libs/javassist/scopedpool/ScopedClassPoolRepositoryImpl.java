// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.scopedpool;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.javassist.ClassPath;
import com.viaversion.viaversion.libs.javassist.LoaderClassPath;
import java.util.Collections;
import java.util.WeakHashMap;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import java.util.Map;

public class ScopedClassPoolRepositoryImpl implements ScopedClassPoolRepository
{
    private static final ScopedClassPoolRepositoryImpl instance;
    private boolean prune;
    boolean pruneWhenCached;
    protected Map<ClassLoader, ScopedClassPool> registeredCLs;
    protected ClassPool classpool;
    protected ScopedClassPoolFactory factory;
    
    public static ScopedClassPoolRepository getInstance() {
        return ScopedClassPoolRepositoryImpl.instance;
    }
    
    private ScopedClassPoolRepositoryImpl() {
        this.prune = true;
        this.registeredCLs = Collections.synchronizedMap(new WeakHashMap<ClassLoader, ScopedClassPool>());
        this.factory = new ScopedClassPoolFactoryImpl();
        this.classpool = ClassPool.getDefault();
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        this.classpool.insertClassPath(new LoaderClassPath(cl));
    }
    
    @Override
    public boolean isPrune() {
        return this.prune;
    }
    
    @Override
    public void setPrune(final boolean prune) {
        this.prune = prune;
    }
    
    @Override
    public ScopedClassPool createScopedClassPool(final ClassLoader cl, final ClassPool src) {
        return this.factory.create(cl, src, this);
    }
    
    @Override
    public ClassPool findClassPool(final ClassLoader cl) {
        if (cl == null) {
            return this.registerClassLoader(ClassLoader.getSystemClassLoader());
        }
        return this.registerClassLoader(cl);
    }
    
    @Override
    public ClassPool registerClassLoader(final ClassLoader ucl) {
        synchronized (this.registeredCLs) {
            if (this.registeredCLs.containsKey(ucl)) {
                return this.registeredCLs.get(ucl);
            }
            final ScopedClassPool pool = this.createScopedClassPool(ucl, this.classpool);
            this.registeredCLs.put(ucl, pool);
            return pool;
        }
    }
    
    @Override
    public Map<ClassLoader, ScopedClassPool> getRegisteredCLs() {
        this.clearUnregisteredClassLoaders();
        return this.registeredCLs;
    }
    
    @Override
    public void clearUnregisteredClassLoaders() {
        List<ClassLoader> toUnregister = null;
        synchronized (this.registeredCLs) {
            for (final Map.Entry<ClassLoader, ScopedClassPool> reg : this.registeredCLs.entrySet()) {
                if (reg.getValue().isUnloadedClassLoader()) {
                    final ClassLoader cl = reg.getValue().getClassLoader();
                    if (cl != null) {
                        if (toUnregister == null) {
                            toUnregister = new ArrayList<ClassLoader>();
                        }
                        toUnregister.add(cl);
                    }
                    this.registeredCLs.remove(reg.getKey());
                }
            }
            if (toUnregister != null) {
                for (final ClassLoader cl2 : toUnregister) {
                    this.unregisterClassLoader(cl2);
                }
            }
        }
    }
    
    @Override
    public void unregisterClassLoader(final ClassLoader cl) {
        synchronized (this.registeredCLs) {
            final ScopedClassPool pool = this.registeredCLs.remove(cl);
            if (pool != null) {
                pool.close();
            }
        }
    }
    
    public void insertDelegate(final ScopedClassPoolRepository delegate) {
    }
    
    @Override
    public void setClassPoolFactory(final ScopedClassPoolFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public ScopedClassPoolFactory getClassPoolFactory() {
        return this.factory;
    }
    
    static {
        instance = new ScopedClassPoolRepositoryImpl();
    }
}
