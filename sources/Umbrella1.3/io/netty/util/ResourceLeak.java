/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util;

public interface ResourceLeak {
    public void record();

    public boolean close();
}

