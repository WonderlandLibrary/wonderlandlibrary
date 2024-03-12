// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class TextureClock extends TextureAtlasSprite
{
    private double currentAngle;
    private double angleDelta;
    
    public TextureClock(final String iconName) {
        super(iconName);
    }
    
    @Override
    public void updateAnimation() {
        if (!this.framesTextureData.isEmpty()) {
            final Minecraft minecraft = Minecraft.getMinecraft();
            double d0 = 0.0;
            if (minecraft.theWorld != null && minecraft.thePlayer != null) {
                d0 = minecraft.theWorld.getCelestialAngle(1.0f);
                if (!minecraft.theWorld.provider.isSurfaceWorld()) {
                    d0 = Math.random();
                }
            }
            double d2;
            for (d2 = d0 - this.currentAngle; d2 < -0.5; ++d2) {}
            while (d2 >= 0.5) {
                --d2;
            }
            d2 = MathHelper.clamp_double(d2, -1.0, 1.0);
            this.angleDelta += d2 * 0.1;
            this.angleDelta *= 0.8;
            this.currentAngle += this.angleDelta;
            int i;
            for (i = (int)((this.currentAngle + 1.0) * this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size()) {}
            if (i != this.frameCounter) {
                this.frameCounter = i;
                TextureUtil.uploadTextureMipmap(this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}
