/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import org.lwjgl.util.vector.Vector;

public interface ReadableVector {
    public float length();

    public float lengthSquared();

    public Vector store(FloatBuffer var1);
}

