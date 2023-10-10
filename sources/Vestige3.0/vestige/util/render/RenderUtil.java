package vestige.util.render;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtil {

    public static void prepareBoxRender(float lineWidth, double red, double green, double blue, double alpha) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDepthMask(false);

        GL11.glColor4d(red, green, blue, alpha);
    }

    public static void renderEntityBox(RenderManager rm, float partialTicks, Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();

        double posX = interpolate(entity.posX, entity.lastTickPosX, partialTicks);
        double posY = interpolate(entity.posY, entity.lastTickPosY, partialTicks);
        double posZ = interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);

        RenderGlobal.func_181561_a(
                new AxisAlignedBB(
                        bb.minX - 0.05 - entity.posX + (posX - rm.renderPosX),
                        bb.minY - 0.05 - entity.posY + (posY - rm.renderPosY),
                        bb.minZ - 0.05 - entity.posZ + (posZ - rm.renderPosZ),
                        bb.maxX + 0.05 - entity.posX + (posX - rm.renderPosX),
                        bb.maxY + 0.1 - entity.posY + (posY - rm.renderPosY),
                        bb.maxZ + 0.05 - entity.posZ + (posZ - rm.renderPosZ)
                )
        );
    }

    public static void renderCustomPlayerBox(RenderManager rm, float partialTicks, double x, double y, double z) {
        renderCustomPlayerBox(rm, partialTicks, x, y, z, x, y, z);
    }

    public static void renderCustomPlayerBox(RenderManager rm, float partialTicks, double x, double y, double z, double lastX, double lastY, double lastZ) {
        AxisAlignedBB bb = new AxisAlignedBB(x - 0.3, y, z - 0.3, x + 0.3, y + 1.8, z + 0.3);

        double posX = interpolate(x, lastX, partialTicks);
        double posY = interpolate(y, lastY, partialTicks);
        double posZ = interpolate(z, lastZ, partialTicks);

        RenderGlobal.func_181561_a(
                new AxisAlignedBB(
                        bb.minX - 0.05 - x + (posX - rm.renderPosX),
                        bb.minY - 0.05 - y + (posY - rm.renderPosY),
                        bb.minZ - 0.05 - z + (posZ - rm.renderPosZ),
                        bb.maxX + 0.05 - x + (posX - rm.renderPosX),
                        bb.maxY + 0.1 - y + (posY - rm.renderPosY),
                        bb.maxZ + 0.05 - z + (posZ - rm.renderPosZ)
                )
        );
    }

    public static void stopBoxRender() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4d(1, 1, 1, 1);
    }

    public static double interpolate(double current, double old, double scale) {
        return (old + (current - old) * scale);
    }

}