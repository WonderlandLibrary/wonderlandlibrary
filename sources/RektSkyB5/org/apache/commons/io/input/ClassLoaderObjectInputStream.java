/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Proxy;

public class ClassLoaderObjectInputStream
extends ObjectInputStream {
    private final ClassLoader classLoader;

    public ClassLoaderObjectInputStream(ClassLoader classLoader, InputStream inputStream) throws IOException, StreamCorruptedException {
        super(inputStream);
        this.classLoader = classLoader;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        try {
            return Class.forName(objectStreamClass.getName(), false, this.classLoader);
        }
        catch (ClassNotFoundException cnfe) {
            return super.resolveClass(objectStreamClass);
        }
    }

    @Override
    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        Class[] interfaceClasses = new Class[interfaces.length];
        for (int i2 = 0; i2 < interfaces.length; ++i2) {
            interfaceClasses[i2] = Class.forName(interfaces[i2], false, this.classLoader);
        }
        try {
            return Proxy.getProxyClass(this.classLoader, interfaceClasses);
        }
        catch (IllegalArgumentException e2) {
            return super.resolveProxyClass(interfaces);
        }
    }
}

