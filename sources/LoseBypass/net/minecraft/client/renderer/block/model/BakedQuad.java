/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;

public class BakedQuad {
    protected final int[] vertexData;
    protected final int tintIndex;
    protected final EnumFacing face;

    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn) {
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
    }

    public int[] getVertexData() {
        return this.vertexData;
    }

    public boolean hasTintIndex() {
        if (this.tintIndex == -1) return false;
        return true;
    }

    public int getTintIndex() {
        return this.tintIndex;
    }

    public EnumFacing getFace() {
        return this.face;
    }
}

