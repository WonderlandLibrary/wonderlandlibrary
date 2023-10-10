/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BakedQuad {
    protected final int[] vertexData;
    protected final int tintIndex;
    protected final EnumFacing face;
    private static final String __OBFID = "CL_00002512";
    private TextureAtlasSprite sprite = null;
    private int[] vertexDataSingle = null;

    public BakedQuad(int[] p_i8_1_, int p_i8_2_, EnumFacing p_i8_3_, TextureAtlasSprite p_i8_4_) {
        this.vertexData = p_i8_1_;
        this.tintIndex = p_i8_2_;
        this.face = p_i8_3_;
        this.sprite = p_i8_4_;
    }

    public TextureAtlasSprite getSprite() {
        return this.sprite;
    }

    public String toString() {
        return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }

    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn) {
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
    }

    public int[] getVertexData() {
        return this.vertexData;
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

    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = BakedQuad.makeVertexDataSingle(this.vertexData, this.sprite);
        }
        return this.vertexDataSingle;
    }

    private static int[] makeVertexDataSingle(int[] p_makeVertexDataSingle_0_, TextureAtlasSprite p_makeVertexDataSingle_1_) {
        int[] aint = new int[p_makeVertexDataSingle_0_.length];
        for (int i2 = 0; i2 < aint.length; ++i2) {
            aint[i2] = p_makeVertexDataSingle_0_[i2];
        }
        int i1 = p_makeVertexDataSingle_1_.sheetWidth / p_makeVertexDataSingle_1_.getIconWidth();
        int j2 = p_makeVertexDataSingle_1_.sheetHeight / p_makeVertexDataSingle_1_.getIconHeight();
        for (int k2 = 0; k2 < 4; ++k2) {
            int l2 = k2 * 7;
            float f2 = Float.intBitsToFloat(aint[l2 + 4]);
            float f1 = Float.intBitsToFloat(aint[l2 + 4 + 1]);
            float f22 = p_makeVertexDataSingle_1_.toSingleU(f2);
            float f3 = p_makeVertexDataSingle_1_.toSingleV(f1);
            aint[l2 + 4] = Float.floatToRawIntBits(f22);
            aint[l2 + 4 + 1] = Float.floatToRawIntBits(f3);
        }
        return aint;
    }
}

