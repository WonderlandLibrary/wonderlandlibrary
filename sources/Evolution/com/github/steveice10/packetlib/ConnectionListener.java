/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib;

public interface ConnectionListener {
    public String getHost();

    public int getPort();

    public boolean isListening();

    public void bind();

    public void bind(boolean var1);

    public void bind(boolean var1, Runnable var2);

    public void close();

    public void close(boolean var1);

    public void close(boolean var1, Runnable var2);
}

