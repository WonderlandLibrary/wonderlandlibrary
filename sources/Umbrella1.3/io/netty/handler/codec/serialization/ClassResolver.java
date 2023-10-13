/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.serialization;

public interface ClassResolver {
    public Class<?> resolve(String var1) throws ClassNotFoundException;
}

