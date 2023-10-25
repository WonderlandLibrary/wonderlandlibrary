/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontResource;

public interface CompositeFontResource
extends FontResource {
    public FontResource getSlotResource(int var1);

    public int getNumSlots();

    public int getSlotForFont(String var1);
}

