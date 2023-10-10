/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import java.io.IOException;

@Deprecated
public interface InputSupplier<T> {
    public T getInput() throws IOException;
}

