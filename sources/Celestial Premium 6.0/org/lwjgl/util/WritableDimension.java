/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util;

import org.lwjgl.util.ReadableDimension;

public interface WritableDimension {
    public void setSize(int var1, int var2);

    public void setSize(ReadableDimension var1);

    public void setHeight(int var1);

    public void setWidth(int var1);
}

