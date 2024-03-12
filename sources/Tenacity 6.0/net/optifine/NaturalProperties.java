// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import java.util.IdentityHashMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.src.Config;
import java.util.Map;

public class NaturalProperties
{
    public int rotation;
    public boolean flip;
    private Map[] quadMaps;
    
    public NaturalProperties(final String type) {
        this.rotation = 1;
        this.flip = false;
        this.quadMaps = new Map[8];
        if (type.equals("4")) {
            this.rotation = 4;
        }
        else if (type.equals("2")) {
            this.rotation = 2;
        }
        else if (type.equals("F")) {
            this.flip = true;
        }
        else if (type.equals("4F")) {
            this.rotation = 4;
            this.flip = true;
        }
        else if (type.equals("2F")) {
            this.rotation = 2;
            this.flip = true;
        }
        else {
            Config.warn("NaturalTextures: Unknown type: " + type);
        }
    }
    
    public boolean isValid() {
        return this.rotation == 2 || this.rotation == 4 || this.flip;
    }
    
    public synchronized BakedQuad getQuad(final BakedQuad quadIn, final int rotate, final boolean flipU) {
        int i = rotate;
        if (flipU) {
            i = (rotate | 0x4);
        }
        if (i > 0 && i < this.quadMaps.length) {
            Map map = this.quadMaps[i];
            if (map == null) {
                map = new IdentityHashMap(1);
                this.quadMaps[i] = map;
            }
            BakedQuad bakedquad = map.get(quadIn);
            if (bakedquad == null) {
                bakedquad = this.makeQuad(quadIn, rotate, flipU);
                map.put(quadIn, bakedquad);
            }
            return bakedquad;
        }
        return quadIn;
    }
    
    private BakedQuad makeQuad(final BakedQuad quad, int rotate, final boolean flipU) {
        int[] aint = quad.getVertexData();
        final int i = quad.getTintIndex();
        final EnumFacing enumfacing = quad.getFace();
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        if (!this.isFullSprite(quad)) {
            rotate = 0;
        }
        aint = this.transformVertexData(aint, rotate, flipU);
        final BakedQuad bakedquad = new BakedQuad(aint, i, enumfacing, textureatlassprite);
        return bakedquad;
    }
    
    private int[] transformVertexData(final int[] vertexData, final int rotate, final boolean flipU) {
        final int[] aint = vertexData.clone();
        int i = 4 - rotate;
        if (flipU) {
            i += 3;
        }
        i %= 4;
        final int j = aint.length / 4;
        for (int k = 0; k < 4; ++k) {
            final int l = k * j;
            final int i2 = i * j;
            aint[i2 + 4] = vertexData[l + 4];
            aint[i2 + 4 + 1] = vertexData[l + 4 + 1];
            if (flipU) {
                if (--i < 0) {
                    i = 3;
                }
            }
            else if (++i > 3) {
                i = 0;
            }
        }
        return aint;
    }
    
    private boolean isFullSprite(final BakedQuad quad) {
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        final float f = textureatlassprite.getMinU();
        final float f2 = textureatlassprite.getMaxU();
        final float f3 = f2 - f;
        final float f4 = f3 / 256.0f;
        final float f5 = textureatlassprite.getMinV();
        final float f6 = textureatlassprite.getMaxV();
        final float f7 = f6 - f5;
        final float f8 = f7 / 256.0f;
        final int[] aint = quad.getVertexData();
        final int i = aint.length / 4;
        for (int j = 0; j < 4; ++j) {
            final int k = j * i;
            final float f9 = Float.intBitsToFloat(aint[k + 4]);
            final float f10 = Float.intBitsToFloat(aint[k + 4 + 1]);
            if (!this.equalsDelta(f9, f, f4) && !this.equalsDelta(f9, f2, f4)) {
                return false;
            }
            if (!this.equalsDelta(f10, f5, f8) && !this.equalsDelta(f10, f6, f8)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean equalsDelta(final float x1, final float x2, final float deltaMax) {
        final float f = MathHelper.abs(x1 - x2);
        return f < deltaMax;
    }
}
