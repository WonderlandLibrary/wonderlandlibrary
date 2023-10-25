/*
 * Decompiled with CFR 0.152.
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
    public Class<?> resolve(String string) throws ClassNotFoundException {
        try {
            return this.classLoader.loadClass(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            return Class.forName(string, false, this.classLoader);
        }
    }
}

