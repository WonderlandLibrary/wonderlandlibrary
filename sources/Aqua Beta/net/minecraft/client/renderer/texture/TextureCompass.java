package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TextureCompass
extends TextureAtlasSprite {
    public double currentAngle;
    public double angleDelta;
    public static String locationSprite;

    public TextureCompass(String iconName) {
        super(iconName);
        locationSprite = iconName;
    }

    public void updateAnimation() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.theWorld != null && minecraft.thePlayer != null) {
            this.updateCompass((World)minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ, minecraft.thePlayer.rotationYaw, false, false);
        } else {
            this.updateCompass(null, 0.0, 0.0, 0.0, true, false);
        }
    }

    public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_) {
        if (!this.framesTextureData.isEmpty()) {
            double d0 = 0.0;
            if (worldIn != null && !p_94241_8_) {
                BlockPos blockpos = worldIn.getSpawnPoint();
                double d1 = (double)blockpos.getX() - p_94241_2_;
                double d2 = (double)blockpos.getZ() - p_94241_4_;
                d0 = -(((p_94241_6_ %= 360.0) - 90.0) * Math.PI / 180.0 - Math.atan2((double)d2, (double)d1));
                if (!worldIn.provider.isSurfaceWorld()) {
                    d0 = Math.random() * Math.PI * 2.0;
                }
            }
            if (p_94241_9_) {
                this.currentAngle = d0;
            } else {
                double d3;
                for (d3 = d0 - this.currentAngle; d3 < -Math.PI; d3 += Math.PI * 2) {
                }
                while (d3 >= Math.PI) {
                    d3 -= Math.PI * 2;
                }
                d3 = MathHelper.clamp_double((double)d3, (double)-1.0, (double)1.0);
                this.angleDelta += d3 * 0.1;
                this.angleDelta *= 0.8;
                this.currentAngle += this.angleDelta;
            }
            int i = (int)((this.currentAngle / (Math.PI * 2) + 1.0) * (double)this.framesTextureData.size()) % this.framesTextureData.size();
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
