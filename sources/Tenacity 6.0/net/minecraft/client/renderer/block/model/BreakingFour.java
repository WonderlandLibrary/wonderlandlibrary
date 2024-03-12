// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BreakingFour extends BakedQuad
{
    private final TextureAtlasSprite texture;
    
    public BreakingFour(final BakedQuad quad, final TextureAtlasSprite textureIn) {
        super(Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length), quad.tintIndex, FaceBakery.getFacingFromVertexData(quad.getVertexData()));
        this.texture = textureIn;
        this.remapQuad();
        this.fixVertexData();
    }
    
    private void remapQuad() {
        for (int i = 0; i < 4; ++i) {
            this.remapVert(i);
        }
    }
    
    private void remapVert(final int vertex) {
        final int i = this.vertexData.length / 4;
        final int j = i * vertex;
        final float f = Float.intBitsToFloat(this.vertexData[j]);
        final float f2 = Float.intBitsToFloat(this.vertexData[j + 1]);
        final float f3 = Float.intBitsToFloat(this.vertexData[j + 2]);
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
                break;
            }
        }
        this.vertexData[j + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(f4));
        this.vertexData[j + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(f5));
    }
}
