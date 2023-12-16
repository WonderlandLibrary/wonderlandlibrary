// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import java.util.IdentityHashMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
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
        int index = rotate;
        if (flipU) {
            index = (rotate | 0x4);
        }
        if (index > 0 && index < this.quadMaps.length) {
            Object map = this.quadMaps[index];
            if (map == null) {
                map = new IdentityHashMap(1);
                this.quadMaps[index] = (Map)map;
            }
            BakedQuad quad = ((Map)map).get(quadIn);
            if (quad == null) {
                quad = this.makeQuad(quadIn, rotate, flipU);
                ((Map)map).put(quadIn, quad);
            }
            return quad;
        }
        return quadIn;
    }
    
    private BakedQuad makeQuad(final BakedQuad quad, final int rotate, final boolean flipU) {
        int[] vertexData = quad.func_178209_a();
        final int tintIndex = quad.func_178211_c();
        final EnumFacing face = quad.getFace();
        final TextureAtlasSprite sprite = quad.getSprite();
        if (!this.isFullSprite(quad)) {
            return quad;
        }
        vertexData = this.transformVertexData(vertexData, rotate, flipU);
        final BakedQuad bq = new BakedQuad(vertexData, tintIndex, face, sprite);
        return bq;
    }
    
    private int[] transformVertexData(final int[] vertexData, final int rotate, final boolean flipU) {
        final int[] vertexData2 = vertexData.clone();
        int v2 = 4 - rotate;
        if (flipU) {
            v2 += 3;
        }
        v2 %= 4;
        final int step = vertexData2.length / 4;
        for (int v3 = 0; v3 < 4; ++v3) {
            final int pos = v3 * step;
            final int pos2 = v2 * step;
            vertexData2[pos2 + 4] = vertexData[pos + 4];
            vertexData2[pos2 + 4 + 1] = vertexData[pos + 4 + 1];
            if (flipU) {
                if (--v2 < 0) {
                    v2 = 3;
                }
            }
            else if (++v2 > 3) {
                v2 = 0;
            }
        }
        return vertexData2;
    }
    
    private boolean isFullSprite(final BakedQuad quad) {
        final TextureAtlasSprite sprite = quad.getSprite();
        final float uMin = sprite.getMinU();
        final float uMax = sprite.getMaxU();
        final float uSize = uMax - uMin;
        final float uDelta = uSize / 256.0f;
        final float vMin = sprite.getMinV();
        final float vMax = sprite.getMaxV();
        final float vSize = vMax - vMin;
        final float vDelta = vSize / 256.0f;
        final int[] vertexData = quad.func_178209_a();
        final int step = vertexData.length / 4;
        for (int i = 0; i < 4; ++i) {
            final int pos = i * step;
            final float u = Float.intBitsToFloat(vertexData[pos + 4]);
            final float v = Float.intBitsToFloat(vertexData[pos + 4 + 1]);
            if (!this.equalsDelta(u, uMin, uDelta) && !this.equalsDelta(u, uMax, uDelta)) {
                return false;
            }
            if (!this.equalsDelta(v, vMin, vDelta) && !this.equalsDelta(v, vMax, vDelta)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean equalsDelta(final float x1, final float x2, final float deltaMax) {
        final float deltaAbs = MathHelper.abs(x1 - x2);
        return deltaAbs < deltaMax;
    }
}
