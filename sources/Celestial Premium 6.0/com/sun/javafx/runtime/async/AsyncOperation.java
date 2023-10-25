/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.runtime.async;

public interface AsyncOperation {
    public void start();

    public void cancel();

    public boolean isCancelled();

    public boolean isDone();
}

