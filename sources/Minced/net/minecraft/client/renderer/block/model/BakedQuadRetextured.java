// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BakedQuadRetextured extends BakedQuad
{
    private final TextureAtlasSprite texture;
    private final TextureAtlasSprite spriteOld;
    
    public BakedQuadRetextured(final BakedQuad quad, final TextureAtlasSprite textureIn) {
        super(Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length), quad.tintIndex, FaceBakery.getFacingFromVertexData(quad.getVertexData()), textureIn, quad.applyDiffuseLighting, quad.format);
        this.texture = textureIn;
        this.format = quad.format;
        this.applyDiffuseLighting = quad.applyDiffuseLighting;
        this.spriteOld = quad.getSprite();
        this.remapQuad();
        this.fixVertexData();
    }
    
    private void remapQuad() {
        for (int i = 0; i < 4; ++i) {
            final int j = this.format.getIntegerSize() * i;
            final int k = this.format.getUvOffsetById(0) / 4;
            this.vertexData[j + k] = Float.floatToRawIntBits(this.texture.getInterpolatedU(this.spriteOld.getUnInterpolatedU(Float.intBitsToFloat(this.vertexData[j + k]))));
            this.vertexData[j + k + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(this.spriteOld.getUnInterpolatedV(Float.intBitsToFloat(this.vertexData[j + k + 1]))));
        }
    }
    
    @Override
    public TextureAtlasSprite getSprite() {
        return this.texture;
    }
}
