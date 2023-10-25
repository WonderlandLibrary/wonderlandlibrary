package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.MathHelper;

public class TextureClock
extends TextureAtlasSprite {
    private double currentAngle;
    private double angleDelta;

    public TextureClock(String iconName) {
        super(iconName);
    }

    public void updateAnimation() {
        if (!this.framesTextureData.isEmpty()) {
            double d1;
            Minecraft minecraft = Minecraft.getMinecraft();
            double d0 = 0.0;
            if (minecraft.theWorld != null && minecraft.thePlayer != null) {
                d0 = minecraft.theWorld.getCelestialAngle(1.0f);
                if (!minecraft.theWorld.provider.isSurfaceWorld()) {
                    d0 = Math.random();
                }
            }
            for (d1 = d0 - this.currentAngle; d1 < -0.5; d1 += 1.0) {
            }
            while (d1 >= 0.5) {
                d1 -= 1.0;
            }
            d1 = MathHelper.clamp_double((double)d1, (double)-1.0, (double)1.0);
            this.angleDelta += d1 * 0.1;
            this.angleDelta *= 0.8;
            this.currentAngle += this.angleDelta;
            int i = (int)((this.currentAngle + 1.0) * (double)this.framesTextureData.size()) % this.framesTextureData.size();
            while (i < 0) {
                i = (i + this.framesTextureData.size()) % this.framesTextureData.size();
            }
            if (i != this.frameCounter) {
                this.frameCounter = i;
                TextureUtil.uploadTextureMipmap((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), (int)this.width, (int)this.height, (int)this.originX, (int)this.originY, (boolean)false, (boolean)false);
            }
        }
    }
}
