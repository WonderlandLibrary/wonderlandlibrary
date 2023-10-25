/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.PlatformImage;

public interface ImageLoader {
    public Exception getException();

    public int getFrameCount();

    public PlatformImage getFrame(int var1);

    public int getFrameDelay(int var1);

    public int getLoopCount();

    public double getWidth();

    public double getHeight();
}

