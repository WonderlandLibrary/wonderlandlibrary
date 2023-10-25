/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.runtime.async;

public interface AsyncOperationListener<V> {
    public void onProgress(int var1, int var2);

    public void onCompletion(V var1);

    public void onCancel();

    public void onException(Exception var1);
}

