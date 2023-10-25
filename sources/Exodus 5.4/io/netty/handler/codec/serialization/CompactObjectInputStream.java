/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.serialization;

import io.netty.handler.codec.serialization.ClassResolver;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

class CompactObjectInputStream
extends ObjectInputStream {
    private final ClassResolver classResolver;

    @Override
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        Class<?> clazz;
        try {
            clazz = this.classResolver.resolve(objectStreamClass.getName());
        }
        catch (ClassNotFoundException classNotFoundException) {
            clazz = super.resolveClass(objectStreamClass);
        }
        return clazz;
    }

    @Override
    protected void readStreamHeader() throws IOException {
        int n = this.readByte() & 0xFF;
        if (n != 5) {
            throw new StreamCorruptedException("Unsupported version: " + n);
        }
    }

    CompactObjectInputStream(InputStream inputStream, ClassResolver classResolver) throws IOException {
        super(inputStream);
        this.classResolver = classResolver;
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws ClassNotFoundException, IOException {
        int n = this.read();
        if (n < 0) {
            throw new EOFException();
        }
        switch (n) {
            case 0: {
                return super.readClassDescriptor();
            }
            case 1: {
                String string = this.readUTF();
                Class<?> clazz = this.classResolver.resolve(string);
                return ObjectStreamClass.lookupAny(clazz);
            }
        }
        throw new StreamCorruptedException("Unexpected class descriptor type: " + n);
    }
}

