/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import optfine.Config;

public class NaturalProperties {
    public int rotation = 1;
    public boolean flip = false;
    private Map[] quadMaps = new Map[8];

    public NaturalProperties(String p_i44_1_) {
        if (p_i44_1_.equals("4")) {
            this.rotation = 4;
        } else if (p_i44_1_.equals("2")) {
            this.rotation = 2;
        } else if (p_i44_1_.equals("F")) {
            this.flip = true;
        } else if (p_i44_1_.equals("4F")) {
            this.rotation = 4;
            this.flip = true;
        } else if (p_i44_1_.equals("2F")) {
            this.rotation = 2;
            this.flip = true;
        } else {
            Config.warn("NaturalTextures: Unknown type: " + p_i44_1_);
        }
    }

    public boolean isValid() {
        return this.rotation != 2 && this.rotation != 4 ? this.flip : true;
    }

    public synchronized BakedQuad getQuad(BakedQuad p_getQuad_1_, int p_getQuad_2_, boolean p_getQuad_3_) {
        int i2 = p_getQuad_2_;
        if (p_getQuad_3_) {
            i2 = p_getQuad_2_ | 4;
        }
        if (i2 > 0 && i2 < this.quadMaps.length) {
            BakedQuad bakedquad;
            IdentityHashMap<BakedQuad, BakedQuad> map = this.quadMaps[i2];
            if (map == null) {
                this.quadMaps[i2] = map = new IdentityHashMap<BakedQuad, BakedQuad>(1);
            }
            if ((bakedquad = (BakedQuad)map.get(p_getQuad_1_)) == null) {
                bakedquad = this.makeQuad(p_getQuad_1_, p_getQuad_2_, p_getQuad_3_);
                map.put(p_getQuad_1_, bakedquad);
            }
            return bakedquad;
        }
        return p_getQuad_1_;
    }

    private BakedQuad makeQuad(BakedQuad p_makeQuad_1_, int p_makeQuad_2_, boolean p_makeQuad_3_) {
        int[] aint = p_makeQuad_1_.getVertexData();
        int i2 = p_makeQuad_1_.getTintIndex();
        EnumFacing enumfacing = p_makeQuad_1_.getFace();
        TextureAtlasSprite textureatlassprite = p_makeQuad_1_.getSprite();
        aint = this.fixVertexData(aint, p_makeQuad_2_, p_makeQuad_3_);
        BakedQuad bakedquad = new BakedQuad(aint, i2, enumfacing, textureatlassprite);
        return bakedquad;
    }

    private int[] fixVertexData(int[] p_fixVertexData_1_, int p_fixVertexData_2_, boolean p_fixVertexData_3_) {
        int[] aint = new int[p_fixVertexData_1_.length];
        for (int i2 = 0; i2 < p_fixVertexData_1_.length; ++i2) {
            aint[i2] = p_fixVertexData_1_[i2];
        }
        int i1 = 4 - p_fixVertexData_2_;
        if (p_fixVertexData_3_) {
            i1 += 3;
        }
        i1 %= 4;
        for (int j2 = 0; j2 < 4; ++j2) {
            int k2 = j2 * 7;
            int l2 = i1 * 7;
            aint[l2 + 4] = p_fixVertexData_1_[k2 + 4];
            aint[l2 + 4 + 1] = p_fixVertexData_1_[k2 + 4 + 1];
            if (p_fixVertexData_3_) {
                if (--i1 >= 0) continue;
                i1 = 3;
                continue;
            }
            if (++i1 <= 3) continue;
            i1 = 0;
        }
        return aint;
    }
}

