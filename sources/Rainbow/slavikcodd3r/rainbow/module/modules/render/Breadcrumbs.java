package slavikcodd3r.rainbow.module.modules.render;

import java.awt.Color;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.InsideBlockRenderEvent;
import slavikcodd3r.rainbow.event.events.Render3DEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "Breadcrumbs")
public class Breadcrumbs extends Module
{   
    private final LinkedList<double[]> positions = new LinkedList<>();
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    public void onRender3D(Render3DEvent event) {

        synchronized(positions) {
            GL11.glPushMatrix();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            mc.entityRenderer.disableLightmap();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            final double renderPosX = mc.getRenderManager().viewerPosX;
            final double renderPosY = mc.getRenderManager().viewerPosY;
            final double renderPosZ = mc.getRenderManager().viewerPosZ;

            for(final double[] pos : positions)
                GL11.glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);

            GL11.glColor4d(1, 1, 1, 1);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }
    }

    public void enable() {
        if(mc.thePlayer == null)
            return;

        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }
        super.enable();
    }

    public void disable() {
        synchronized(positions) {
            positions.clear();
        }
        super.disable();
    }
}