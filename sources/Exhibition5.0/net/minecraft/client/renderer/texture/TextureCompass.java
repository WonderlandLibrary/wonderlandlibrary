// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;

public class TextureCompass extends TextureAtlasSprite
{
    public double currentAngle;
    public double angleDelta;
    public static String field_176608_l;
    private static final String __OBFID = "CL_00001071";
    
    public TextureCompass(final String p_i1286_1_) {
        super(p_i1286_1_);
        TextureCompass.field_176608_l = p_i1286_1_;
    }
    
    @Override
    public void updateAnimation() {
        final Minecraft var1 = Minecraft.getMinecraft();
        if (var1.theWorld != null && var1.thePlayer != null) {
            this.updateCompass(var1.theWorld, var1.thePlayer.posX, var1.thePlayer.posZ, var1.thePlayer.rotationYaw, false, false);
        }
        else {
            this.updateCompass(null, 0.0, 0.0, 0.0, true, false);
        }
    }
    
    public void updateCompass(final World worldIn, final double p_94241_2_, final double p_94241_4_, double p_94241_6_, final boolean p_94241_8_, final boolean p_94241_9_) {
        if (!this.framesTextureData.isEmpty()) {
            double var10 = 0.0;
            if (worldIn != null && !p_94241_8_) {
                final BlockPos var11 = worldIn.getSpawnPoint();
                final double var12 = var11.getX() - p_94241_2_;
                final double var13 = var11.getZ() - p_94241_4_;
                p_94241_6_ %= 360.0;
                var10 = -((p_94241_6_ - 90.0) * 3.141592653589793 / 180.0 - Math.atan2(var13, var12));
                if (!worldIn.provider.isSurfaceWorld()) {
                    var10 = Math.random() * 3.141592653589793 * 2.0;
                }
            }
            if (p_94241_9_) {
                this.currentAngle = var10;
            }
            else {
                double var14;
                for (var14 = var10 - this.currentAngle; var14 < -3.141592653589793; var14 += 6.283185307179586) {}
                while (var14 >= 3.141592653589793) {
                    var14 -= 6.283185307179586;
                }
                var14 = MathHelper.clamp_double(var14, -1.0, 1.0);
                this.angleDelta += var14 * 0.1;
                this.angleDelta *= 0.8;
                this.currentAngle += this.angleDelta;
            }
            int var15;
            for (var15 = (int)((this.currentAngle / 6.283185307179586 + 1.0) * this.framesTextureData.size()) % this.framesTextureData.size(); var15 < 0; var15 = (var15 + this.framesTextureData.size()) % this.framesTextureData.size()) {}
            if (var15 != this.frameCounter) {
                this.frameCounter = var15;
                TextureUtil.uploadTextureMipmap(this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}
