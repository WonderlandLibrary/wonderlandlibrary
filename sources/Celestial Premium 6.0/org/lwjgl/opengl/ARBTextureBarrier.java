/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import org.lwjgl.opengl.GL45;

public final class ARBTextureBarrier {
    private ARBTextureBarrier() {
    }

    public static void glTextureBarrier() {
        GL45.glTextureBarrier();
    }
}

