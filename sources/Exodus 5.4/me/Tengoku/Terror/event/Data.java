/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event;

import java.lang.reflect.Method;

public class Data {
    public final Object source;
    public final byte priority;
    public final Method target;

    Data(Object object, Method method, byte by) {
        this.source = object;
        this.target = method;
        this.priority = by;
    }
}

