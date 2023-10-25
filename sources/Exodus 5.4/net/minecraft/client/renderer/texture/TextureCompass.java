/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TextureCompass
extends TextureAtlasSprite {
    public double angleDelta;
    public static String field_176608_l;
    public double currentAngle;

    @Override
    public void updateAnimation() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (Minecraft.theWorld != null && Minecraft.thePlayer != null) {
            this.updateCompass(Minecraft.theWorld, Minecraft.thePlayer.posX, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, false, false);
        } else {
            this.updateCompass(null, 0.0, 0.0, 0.0, true, false);
        }
    }

    public TextureCompass(String string) {
        super(string);
        field_176608_l = string;
    }

    public void updateCompass(World world, double d, double d2, double d3, boolean bl, boolean bl2) {
        if (!this.framesTextureData.isEmpty()) {
            double d4 = 0.0;
            if (world != null && !bl) {
                BlockPos blockPos = world.getSpawnPoint();
                double d5 = (double)blockPos.getX() - d;
                double d6 = (double)blockPos.getZ() - d2;
                d4 = -(((d3 %= 360.0) - 90.0) * Math.PI / 180.0 - Math.atan2(d6, d5));
                if (!world.provider.isSurfaceWorld()) {
                    d4 = Math.random() * Math.PI * 2.0;
                }
            }
            if (bl2) {
                this.currentAngle = d4;
            } else {
                double d7 = d4 - this.currentAngle;
                while (d7 < -Math.PI) {
                    d7 += Math.PI * 2;
                }
                while (d7 >= Math.PI) {
                    d7 -= Math.PI * 2;
                }
                d7 = MathHelper.clamp_double(d7, -1.0, 1.0);
                this.angleDelta += d7 * 0.1;
                this.angleDelta *= 0.8;
                this.currentAngle += this.angleDelta;
            }
            int n = (int)((this.currentAngle / (Math.PI * 2) + 1.0) * (double)this.framesTextureData.size()) % this.framesTextureData.size();
            while (n < 0) {
                n = (n + this.framesTextureData.size()) % this.framesTextureData.size();
            }
            if (n != this.frameCounter) {
                this.frameCounter = n;
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}

