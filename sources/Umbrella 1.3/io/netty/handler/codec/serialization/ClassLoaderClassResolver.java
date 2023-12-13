/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.serialization;

import io.netty.handler.codec.serialization.ClassResolver;

class ClassLoaderClassResolver
implements ClassResolver {
    private final ClassLoader classLoader;

    ClassLoaderClassResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Class<?> resolve(String className) throws ClassNotFoundException {
        try {
            return this.classLoader.loadClass(className);
        }
        catch (ClassNotFoundException e) {
            return Class.forName(className, false, this.classLoader);
        }
    }
}

