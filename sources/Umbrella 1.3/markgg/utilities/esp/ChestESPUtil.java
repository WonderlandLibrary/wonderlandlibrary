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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class ChestESPUtil {
    public static void blockESPBox(BlockPos blockPos) {
        double d = blockPos.getX();
        Minecraft.getMinecraft().getRenderManager();
        double x = d - RenderManager.renderPosX;
        double d2 = blockPos.getY();
        Minecraft.getMinecraft().getRenderManager();
        double y = d2 - RenderManager.renderPosY;
        double d3 = blockPos.getZ();
        Minecraft.getMinecraft().getRenderManager();
        double z = d3 - RenderManager.renderPosZ;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glColor4d((double)0.0, (double)0.0, (double)1.0, (double)0.15f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)0.0, (double)0.0, (double)1.0, (double)0.5);
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }
}

