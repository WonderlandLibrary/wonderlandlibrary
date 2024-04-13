package dev.tenacity.module.impl.render;

import dev.tenacity.Tenacity;
import dev.tenacity.event.IEventListener;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import dev.tenacity.util.render.Render3DEvent;


public class ESP2D extends Module implements IEventListener<Render3DEvent> {
    public ESP2D() {
        super("ESP2D", "Renders a box around players", ModuleCategory.RENDER);
        if (mc.thePlayer != null) { ChatUtil.enable("ESP Enabled"); }
        if (Tenacity.getInstance() != null) {
            if (mc.thePlayer != null) { ChatUtil.enable("Eventbus listener registered"); }
            Tenacity.INSTANCE.getEventBus().register(this);
        }
    }

    @Override
    public void invoke(Render3DEvent event) {
        if (mc.thePlayer != null) { ChatUtil.enable("Eventbus listener registered"); }
        for (Object o : Minecraft.getMinecraft().theWorld.playerEntities) {
            ChatUtil.enable("Detecting entities...");
            if (o instanceof EntityPlayer) {
                ChatUtil.enable("Found Player");
                EntityPlayer player = (EntityPlayer) o;
                if (player != Minecraft.getMinecraft().thePlayer) {
                    if (mc.thePlayer != null) { ChatUtil.notify("Drawing box"); }
                    drawBox(player, event);
                }
            }
        }
    }

    private void drawBox(EntityPlayer player, Render3DEvent event) {
        if (mc.thePlayer != null) { ChatUtil.enable("Calculating..."); }
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks() - renderManager.viewerPosX;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks() - renderManager.viewerPosY;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks() - renderManager.viewerPosZ;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glColor3f(255.0F, 0.0F, 0.0F);
        GL11.glVertex3d(-0.5, 0, -0.5);
        GL11.glVertex3d(-0.5, 2, -0.5);
        GL11.glVertex3d(0.5, 2, -0.5);
        GL11.glVertex3d(0.5, 0, -0.5);
        GL11.glVertex3d(-0.5, 0, -0.5);
        GL11.glVertex3d(-0.5, 0, 0.5);
        GL11.glVertex3d(-0.5, 2, 0.5);
        GL11.glVertex3d(0.5, 2, 0.5);
        GL11.glVertex3d(0.5, 0, 0.5);
        GL11.glVertex3d(-0.5, 0, 0.5);
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}
