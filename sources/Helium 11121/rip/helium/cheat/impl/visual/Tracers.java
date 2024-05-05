package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.EntityRenderEvent;
import rip.helium.utils.MathUtils;
import rip.helium.utils.RenderUtil;

public class Tracers extends Cheat {

    public Tracers() {
        super("Tracers", "Draws tracer line to players", 0, CheatCategory.VISUAL);
    }


    @Collect
    public void onRender3D(EntityRenderEvent event) {
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) o;
            if (entity instanceof net.minecraft.entity.player.EntityPlayer && entity != mc.thePlayer && isInTablist((EntityLivingBase) entity)) {
                double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - RenderManager.renderPosX;
                double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - RenderManager.renderPosY;
                double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - RenderManager.renderPosZ;
                boolean old = mc.gameSettings.viewBobbing;
                RenderUtil.startDrawing();
                mc.gameSettings.viewBobbing = false;
                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
                mc.gameSettings.viewBobbing = old;
                float color = (float) Math.round(255.0D - mc.thePlayer.getDistanceSqToEntity(entity) * 255.0D /
                        MathUtils.square(mc.gameSettings.renderDistanceChunks * 2.5D)) / 255.0F;
                (new double[3])[0] = 0.0D;
                (new double[3])[1] = 1.0D;
                (new double[3])[2] = 1.0D;
                (new double[3])[0] = color;
                (new double[3])[1] = (1.0F - color);
                (new double[3])[2] = 0.0D;
                drawLine(entity, new double[3], posX, posY, posZ);
                RenderUtil.stopDrawing();
            }
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawLine(Entity entity, double[] color, double x, double y, double z) {
        GL11.glEnable(2848);
        if (color.length >= 4) {
            if (color[3] <= 0.1D)
                return;
            GL11.glColor4d(color[0], color[1], color[2], color[3]);
        } else {
            GL11.glColor3d(color[0], color[1], color[2]);
        }
        GL11.glLineWidth(1.0F);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(2848);
    }

    private boolean isInTablist(EntityLivingBase player) {
        if (mc.isSingleplayer()) {
            return true;
        }
        for (Object o : mc.getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
            if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }


}
