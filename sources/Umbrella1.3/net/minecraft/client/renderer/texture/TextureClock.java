/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.MathHelper;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class TextureClock
extends TextureAtlasSprite {
    private double field_94239_h;
    private double field_94240_i;
    private static final String __OBFID = "CL_00001070";

    public TextureClock(String p_i1285_1_) {
        super(p_i1285_1_);
    }

    @Override
    public void updateAnimation() {
        if (!this.framesTextureData.isEmpty()) {
            double var71;
            Minecraft var1 = Minecraft.getMinecraft();
            double var2 = 0.0;
            if (var1.theWorld != null && var1.thePlayer != null) {
                float var7 = var1.theWorld.getCelestialAngle(1.0f);
                var2 = var7;
                if (!var1.theWorld.provider.isSurfaceWorld()) {
                    var2 = Math.random();
                }
            }
            for (var71 = var2 - this.field_94239_h; var71 < -0.5; var71 += 1.0) {
            }
            while (var71 >= 0.5) {
                var71 -= 1.0;
            }
            var71 = MathHelper.clamp_double(var71, -1.0, 1.0);
            this.field_94240_i += var71 * 0.1;
            this.field_94240_i *= 0.8;
            this.field_94239_h += this.field_94240_i;
            int var6 = (int)((this.field_94239_h + 1.0) * (double)this.framesTextureData.size()) % this.framesTextureData.size();
            while (var6 < 0) {
                var6 = (var6 + this.framesTextureData.size()) % this.framesTextureData.size();
            }
            if (var6 != this.frameCounter) {
                this.frameCounter = var6;
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSub((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
                } else {
                    TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
                }
            }
        }
    }
}

