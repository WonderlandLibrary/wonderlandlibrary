/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.opengl;

import java.nio.ByteBuffer;

public interface ImageData {
    public int getDepth();

    public int getWidth();

    public int getHeight();

    public int getTexWidth();

    public int getTexHeight();

    public ByteBuffer getImageBufferData();
}

