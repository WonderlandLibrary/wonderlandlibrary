// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class TextureClock extends TextureAtlasSprite
{
    private double field_94239_h;
    private double field_94240_i;
    private static final String __OBFID = "CL_00001070";
    
    public TextureClock(final String p_i1285_1_) {
        super(p_i1285_1_);
    }
    
    @Override
    public void updateAnimation() {
        if (!this.framesTextureData.isEmpty()) {
            final Minecraft var1 = Minecraft.getMinecraft();
            double var2 = 0.0;
            if (var1.theWorld != null && var1.thePlayer != null) {
                final float var3 = var1.theWorld.getCelestialAngle(1.0f);
                var2 = var3;
                if (!var1.theWorld.provider.isSurfaceWorld()) {
                    var2 = Math.random();
                }
            }
            double var4;
            for (var4 = var2 - this.field_94239_h; var4 < -0.5; ++var4) {}
            while (var4 >= 0.5) {
                --var4;
            }
            var4 = MathHelper.clamp_double(var4, -1.0, 1.0);
            this.field_94240_i += var4 * 0.1;
            this.field_94240_i *= 0.8;
            this.field_94239_h += this.field_94240_i;
            int var5;
            for (var5 = (int)((this.field_94239_h + 1.0) * this.framesTextureData.size()) % this.framesTextureData.size(); var5 < 0; var5 = (var5 + this.framesTextureData.size()) % this.framesTextureData.size()) {}
            if (var5 != this.frameCounter) {
                this.frameCounter = var5;
                TextureUtil.uploadTextureMipmap(this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}
