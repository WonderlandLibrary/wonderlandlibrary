/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.util.Arrays;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class BoundCallable {
    private final Object callable;
    private final Object boundThis;
    private final Object[] boundArgs;

    BoundCallable(Object callable, Object boundThis, Object[] boundArgs) {
        this.callable = callable;
        this.boundThis = boundThis;
        this.boundArgs = BoundCallable.isEmptyArray(boundArgs) ? ScriptRuntime.EMPTY_ARRAY : (Object[])boundArgs.clone();
    }

    private BoundCallable(BoundCallable original, Object[] extraBoundArgs) {
        this.callable = original.callable;
        this.boundThis = original.boundThis;
        this.boundArgs = original.concatenateBoundArgs(extraBoundArgs);
    }

    Object getCallable() {
        return this.callable;
    }

    Object getBoundThis() {
        return this.boundThis;
    }

    Object[] getBoundArgs() {
        return this.boundArgs;
    }

    BoundCallable bind(Object[] extraBoundArgs) {
        if (BoundCallable.isEmptyArray(extraBoundArgs)) {
            return this;
        }
        return new BoundCallable(this, extraBoundArgs);
    }

    private Object[] concatenateBoundArgs(Object[] extraBoundArgs) {
        if (this.boundArgs.length == 0) {
            return (Object[])extraBoundArgs.clone();
        }
        int origBoundArgsLen = this.boundArgs.length;
        int extraBoundArgsLen = extraBoundArgs.length;
        Object[] newBoundArgs = new Object[origBoundArgsLen + extraBoundArgsLen];
        System.arraycopy(this.boundArgs, 0, newBoundArgs, 0, origBoundArgsLen);
        System.arraycopy(extraBoundArgs, 0, newBoundArgs, origBoundArgsLen, extraBoundArgsLen);
        return newBoundArgs;
    }

    private static boolean isEmptyArray(Object[] a) {
        return a == null || a.length == 0;
    }

    public String toString() {
        StringBuilder b = new StringBuilder(this.callable.toString()).append(" on ").append(this.boundThis);
        if (this.boundArgs.length != 0) {
            b.append(" with ").append(Arrays.toString(this.boundArgs));
        }
        return b.toString();
    }
}

