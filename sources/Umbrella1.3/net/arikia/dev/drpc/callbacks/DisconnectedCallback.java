/*
 * Decompiled with CFR 0.150.
 */
package net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;

public interface DisconnectedCallback
extends Callback {
    public void apply(int var1, String var2);
}

