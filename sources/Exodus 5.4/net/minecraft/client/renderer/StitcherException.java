/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.Stitcher;

public class StitcherException
extends RuntimeException {
    private final Stitcher.Holder holder;

    public StitcherException(Stitcher.Holder holder, String string) {
        super(string);
        this.holder = holder;
    }
}

