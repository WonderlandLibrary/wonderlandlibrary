/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.scenario.effect.LockableResource;

public interface Filterable
extends LockableResource {
    public Object getData();

    public int getContentWidth();

    public int getContentHeight();

    public void setContentWidth(int var1);

    public void setContentHeight(int var1);

    public int getMaxContentWidth();

    public int getMaxContentHeight();

    public int getPhysicalWidth();

    public int getPhysicalHeight();

    public float getPixelScale();

    public void flush();
}

