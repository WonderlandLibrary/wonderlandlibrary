/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util;

import org.lwjgl.util.ReadableDimension;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.ReadableRectangle;
import org.lwjgl.util.WritableDimension;
import org.lwjgl.util.WritablePoint;

public interface WritableRectangle
extends WritablePoint,
WritableDimension {
    public void setBounds(int var1, int var2, int var3, int var4);

    public void setBounds(ReadablePoint var1, ReadableDimension var2);

    public void setBounds(ReadableRectangle var1);
}

