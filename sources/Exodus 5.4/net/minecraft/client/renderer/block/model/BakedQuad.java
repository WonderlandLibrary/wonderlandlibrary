/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;

public class BakedQuad {
    protected final EnumFacing face;
    protected final int[] vertexData;
    protected final int tintIndex;

    public int[] getVertexData() {
        return this.vertexData;
    }

    public BakedQuad(int[] nArray, int n, EnumFacing enumFacing) {
        this.vertexData = nArray;
        this.tintIndex = n;
        this.face = enumFacing;
    }

    public boolean hasTintIndex() {
        return this.tintIndex != -1;
    }

    public int getTintIndex() {
        return this.tintIndex;
    }

    public EnumFacing getFace() {
        return this.face;
    }
}

