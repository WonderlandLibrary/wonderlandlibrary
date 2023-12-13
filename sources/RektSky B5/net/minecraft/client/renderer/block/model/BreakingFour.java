/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BreakingFour
extends BakedQuad {
    private final TextureAtlasSprite texture;

    public BreakingFour(BakedQuad p_i46217_1_, TextureAtlasSprite textureIn) {
        super(Arrays.copyOf(p_i46217_1_.getVertexData(), p_i46217_1_.getVertexData().length), p_i46217_1_.tintIndex, FaceBakery.getFacingFromVertexData(p_i46217_1_.getVertexData()));
        this.texture = textureIn;
        this.func_178217_e();
    }

    private void func_178217_e() {
        for (int i2 = 0; i2 < 4; ++i2) {
            this.func_178216_a(i2);
        }
    }

    private void func_178216_a(int p_178216_1_) {
        int i2 = 7 * p_178216_1_;
        float f2 = Float.intBitsToFloat(this.vertexData[i2]);
        float f1 = Float.intBitsToFloat(this.vertexData[i2 + 1]);
        float f22 = Float.intBitsToFloat(this.vertexData[i2 + 2]);
        float f3 = 0.0f;
        float f4 = 0.0f;
        switch (this.face) {
            case DOWN: {
                f3 = f2 * 16.0f;
                f4 = (1.0f - f22) * 16.0f;
                break;
            }
            case UP: {
                f3 = f2 * 16.0f;
                f4 = f22 * 16.0f;
                break;
            }
            case NORTH: {
                f3 = (1.0f - f2) * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case SOUTH: {
                f3 = f2 * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case WEST: {
                f3 = f22 * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case EAST: {
                f3 = (1.0f - f22) * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
            }
        }
        this.vertexData[i2 + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(f3));
        this.vertexData[i2 + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(f4));
    }
}

