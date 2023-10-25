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

    public BreakingFour(BakedQuad bakedQuad, TextureAtlasSprite textureAtlasSprite) {
        super(Arrays.copyOf(bakedQuad.getVertexData(), bakedQuad.getVertexData().length), bakedQuad.tintIndex, FaceBakery.getFacingFromVertexData(bakedQuad.getVertexData()));
        this.texture = textureAtlasSprite;
        this.func_178217_e();
    }

    private void func_178216_a(int n) {
        int n2 = 7 * n;
        float f = Float.intBitsToFloat(this.vertexData[n2]);
        float f2 = Float.intBitsToFloat(this.vertexData[n2 + 1]);
        float f3 = Float.intBitsToFloat(this.vertexData[n2 + 2]);
        float f4 = 0.0f;
        float f5 = 0.0f;
        switch (this.face) {
            case DOWN: {
                f4 = f * 16.0f;
                f5 = (1.0f - f3) * 16.0f;
                break;
            }
            case UP: {
                f4 = f * 16.0f;
                f5 = f3 * 16.0f;
                break;
            }
            case NORTH: {
                f4 = (1.0f - f) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case SOUTH: {
                f4 = f * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case WEST: {
                f4 = f3 * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case EAST: {
                f4 = (1.0f - f3) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
            }
        }
        this.vertexData[n2 + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(f4));
        this.vertexData[n2 + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(f5));
    }

    private void func_178217_e() {
        int n = 0;
        while (n < 4) {
            this.func_178216_a(n);
            ++n;
        }
    }
}

