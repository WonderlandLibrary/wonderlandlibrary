/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.MathHelper;

public class TextureClock
extends TextureAtlasSprite {
    private double field_94240_i;
    private double field_94239_h;

    @Override
    public void updateAnimation() {
        if (!this.framesTextureData.isEmpty()) {
            Minecraft minecraft = Minecraft.getMinecraft();
            double d = 0.0;
            if (Minecraft.theWorld != null && Minecraft.thePlayer != null) {
                d = Minecraft.theWorld.getCelestialAngle(1.0f);
                if (!Minecraft.theWorld.provider.isSurfaceWorld()) {
                    d = Math.random();
                }
            }
            double d2 = d - this.field_94239_h;
            while (d2 < -0.5) {
                d2 += 1.0;
            }
            while (d2 >= 0.5) {
                d2 -= 1.0;
            }
            d2 = MathHelper.clamp_double(d2, -1.0, 1.0);
            this.field_94240_i += d2 * 0.1;
            this.field_94240_i *= 0.8;
            this.field_94239_h += this.field_94240_i;
            int n = (int)((this.field_94239_h + 1.0) * (double)this.framesTextureData.size()) % this.framesTextureData.size();
            while (n < 0) {
                n = (n + this.framesTextureData.size()) % this.framesTextureData.size();
            }
            if (n != this.frameCounter) {
                this.frameCounter = n;
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }

    public TextureClock(String string) {
        super(string);
    }
}

