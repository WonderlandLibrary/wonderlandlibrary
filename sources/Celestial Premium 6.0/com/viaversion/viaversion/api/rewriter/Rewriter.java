/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.rewriter;

import com.viaversion.viaversion.api.protocol.Protocol;

public interface Rewriter<T extends Protocol> {
    public void register();

    public T protocol();
}

