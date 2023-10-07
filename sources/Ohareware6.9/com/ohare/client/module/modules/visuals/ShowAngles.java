package com.ohare.client.module.modules.visuals;

import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.value.impl.NumberValue;
import dorkbox.messageBus.annotations.Subscribe;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * made by oHare for oHareWare
 *
 * @since 7/23/2019
 **/
public class ShowAngles extends Module {
    private NumberValue<Float> width = new NumberValue<>("Width", 1f, 0.1f, 10f, 1.0F);

    public ShowAngles() {
        super("ShowAngles", Category.VISUALS, new Color(0x96FFC4).getRGB());
        setRenderlabel("Show Angles");
        setDescription("Show update event angles.");
        addValues(width);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.gameSettings.thirdPersonView == 0) return;
        drawAngle(event.getPartialTicks(),-mc.thePlayer.lastReportedYaw,1,0,0);
    }
    private void drawAngle(float partialTicks, float yaw, double r, double g, double b) {
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(partialTicks);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width.getValue());
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glColor3d(r, g, b);
            GL11.glVertex3d(-0.025, mc.thePlayer.height, 0.03);
            GL11.glVertex3d(-0.05, mc.thePlayer.height, 1.0);
            GL11.glVertex3d(-0.20, mc.thePlayer.height, 1.0);
            GL11.glVertex3d(0.0, mc.thePlayer.height, 1.25);
            GL11.glVertex3d(0.20, mc.thePlayer.height, 1.0);
            GL11.glVertex3d(0.05, mc.thePlayer.height, 1.0);
            GL11.glVertex3d(0.025, mc.thePlayer.height, 0.03);
            GL11.glVertex3d(-0.025, mc.thePlayer.height, 0.03);
            GL11.glEnd();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }
}
