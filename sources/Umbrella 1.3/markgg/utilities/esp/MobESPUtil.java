/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package markgg.utilities.esp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class MobESPUtil {
    public static void entityESPBox(Entity entity, int mode) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        if (mode == 0) {
            GL11.glColor4d((double)(1.0 - Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(entity) / 40.0), (double)(Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(entity) / 40.0), (double)0.0, (double)0.5);
        } else if (mode == 1) {
            GL11.glColor4d((double)0.0, (double)0.0, (double)1.0, (double)0.5);
        } else if (mode == 2) {
            GL11.glColor4d((double)1.0, (double)1.0, (double)0.0, (double)0.5);
        } else if (mode == 3) {
            GL11.glColor4d((double)1.0, (double)0.0, (double)0.0, (double)0.5);
        } else if (mode == 4) {
            GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.5);
        }
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(entity.boundingBox.minX - 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.minY - 0.05 - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.minZ - 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ), entity.boundingBox.maxX + 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.maxY + 0.1 - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.maxZ + 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ)), 0);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }
}

