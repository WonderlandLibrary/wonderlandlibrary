// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class Tracers extends Module
{
    public Tracers() {
        super("Tracers", 0, "Renders a line to each player", Category.RENDER);
    }
    
    @Override
    public void onEvent(final Event ev) {
        if (ev instanceof EventRender3D) {
            final EventRender3D event = (EventRender3D)ev;
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GL11.glLineWidth(1.0f);
            final float partialTicks = event.getPartialTicks();
            float x = (float)((float)(Tracers.mc.thePlayer.lastTickPosX + (Tracers.mc.thePlayer.posX - Tracers.mc.thePlayer.lastTickPosX) * partialTicks) - RenderManager.renderPosX);
            float y = (float)((float)(Tracers.mc.thePlayer.lastTickPosY + (Tracers.mc.thePlayer.posY - Tracers.mc.thePlayer.lastTickPosY) * partialTicks) - RenderManager.renderPosY);
            float z = (float)((float)(Tracers.mc.thePlayer.lastTickPosZ + (Tracers.mc.thePlayer.posZ - Tracers.mc.thePlayer.lastTickPosZ) * partialTicks) - RenderManager.renderPosZ);
            if (Tracers.mc.gameSettings.thirdPersonView == 0) {
                GL11.glLoadIdentity();
                Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
            }
            else {
                x = (float)(Tracers.mc.thePlayer.prevPosX + (Tracers.mc.thePlayer.posX - Tracers.mc.thePlayer.prevPosX) * partialTicks);
                y = (float)(Tracers.mc.thePlayer.prevPosY + (Tracers.mc.thePlayer.posY - Tracers.mc.thePlayer.prevPosY) * partialTicks);
                z = (float)(Tracers.mc.thePlayer.prevPosZ + (Tracers.mc.thePlayer.posZ - Tracers.mc.thePlayer.prevPosZ) * partialTicks);
                Tracers.mc.getRenderManager();
                final double x3 = -RenderManager.viewerPosX;
                Tracers.mc.getRenderManager();
                final double y3 = -RenderManager.viewerPosY;
                Tracers.mc.getRenderManager();
                GlStateManager.translate(x3, y3, -RenderManager.viewerPosZ);
            }
            final Vec3 playerPos = new Vec3(x, y + Tracers.mc.thePlayer.getEyeHeight(), z);
            for (final Entity e : Tracers.mc.theWorld.loadedEntityList) {
                if (e instanceof EntityPlayer && e != Tracers.mc.thePlayer) {
                    float x2 = (float)((float)(e.lastTickPosX + (e.posX - e.lastTickPosX) * partialTicks) - RenderManager.renderPosX);
                    float y2 = (float)((float)(e.lastTickPosY + (e.posY - e.lastTickPosY) * partialTicks) - RenderManager.renderPosY);
                    float z2 = (float)((float)(e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * partialTicks) - RenderManager.renderPosZ);
                    if (Tracers.mc.gameSettings.thirdPersonView != 0) {
                        x2 = (float)(e.prevPosX + (e.posX - e.prevPosX) * partialTicks);
                        y2 = (float)(e.prevPosY + (e.posY - e.prevPosY) * partialTicks);
                        z2 = (float)(e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks);
                    }
                    this.color(e);
                    GL11.glBegin(1);
                    GL11.glVertex3d(playerPos.getX(), playerPos.getY(), playerPos.getZ());
                    GL11.glVertex3d((double)x2, (double)y2, (double)z2);
                    GL11.glEnd();
                    GL11.glBegin(1);
                    GL11.glVertex3d((double)x2, (double)y2, (double)z2);
                    GL11.glVertex3d((double)x2, (double)(y2 + e.getEyeHeight()), (double)z2);
                    GL11.glEnd();
                }
            }
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
        }
    }
    
    private void color(final Entity e) {
        GL11.glColor3f(255.0f, 255.0f, 255.0f);
    }
}
