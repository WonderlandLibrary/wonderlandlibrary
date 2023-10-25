/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class Render2DUtil {
    public static void entityESPBox(Entity entity, int n) {
        if (!entity.isInvisible() && entity.getUniqueID() != null) {
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)3042);
            GL11.glLineWidth((float)2.0f);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            if (n == 0) {
                Minecraft.getMinecraft();
                double d = 1.0f - Minecraft.thePlayer.getDistanceToEntity(entity) / 40.0f;
                Minecraft.getMinecraft();
                GL11.glColor4d((double)d, (double)(Minecraft.thePlayer.getDistanceToEntity(entity) / 40.0f), (double)0.0, (double)0.5);
            } else if (n == 1) {
                GL11.glColor4d((double)0.0, (double)0.0, (double)1.0, (double)0.5);
            } else if (n == 2) {
                GL11.glColor4d((double)1.0, (double)1.0, (double)0.0, (double)0.5);
            } else if (n == 3) {
                GL11.glColor4d((double)1.0, (double)0.0, (double)0.0, (double)0.5);
            } else if (n == 4) {
                GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.5);
            }
            Minecraft.getMinecraft().getRenderManager();
            double d = entity.boundingBox.minX - 0.05 - entity.posX;
            double d2 = entity.posX;
            Minecraft.getMinecraft().getRenderManager();
            double d3 = d + (d2 - RenderManager.renderPosX);
            double d4 = entity.boundingBox.minY - entity.posY;
            double d5 = entity.posY;
            Minecraft.getMinecraft().getRenderManager();
            double d6 = d4 + (d5 - RenderManager.renderPosY);
            double d7 = entity.boundingBox.minZ - 0.05 - entity.posZ;
            double d8 = entity.posZ;
            Minecraft.getMinecraft().getRenderManager();
            double d9 = d7 + (d8 - RenderManager.renderPosZ);
            double d10 = entity.boundingBox.maxX + 0.05 - entity.posX;
            double d11 = entity.posX;
            Minecraft.getMinecraft().getRenderManager();
            double d12 = d10 + (d11 - RenderManager.renderPosX);
            double d13 = entity.boundingBox.maxY + 0.1 - entity.posY;
            double d14 = entity.posY;
            Minecraft.getMinecraft().getRenderManager();
            double d15 = d13 + (d14 - RenderManager.renderPosY);
            double d16 = entity.boundingBox.maxZ + 0.05 - entity.posZ;
            double d17 = entity.posZ;
            Minecraft.getMinecraft().getRenderManager();
            RenderGlobal.func_181561_a(new AxisAlignedBB(d3, d6, d9, d12, d15, d16 + (d17 - RenderManager.renderPosZ)));
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
        }
    }
}

