package org.dreamcore.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.ArrayList;

public class Trails extends Feature {

    public ListSetting colorMode;
    public ColorSetting customColor;
    public NumberSetting width;
    public NumberSetting height;
    public NumberSetting sizeToRemove;

    public ArrayList<Point> points = new ArrayList<>();
    public float pointCount = 0;

    public Trails() {
        super("Trails", "Оставляет линию ходьбы", Type.Visuals);
        colorMode = new ListSetting("Color", "Rainbow", () -> true, "Rainbow", "Astolfo", "Client", "Custom");
        customColor = new ColorSetting("Custom Color", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        width = new NumberSetting("Width", 2F, 1F, 6F, 1F, () -> true);
        height = new NumberSetting("Height", 0, 0, 6F, 0.01F, () -> true);
        sizeToRemove = new NumberSetting("Time", 150, 10, 300, 10F, () -> true);
        addSettings(colorMode, customColor, width, height, sizeToRemove);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        String colorModeValue = colorMode.getOptions();

        if (!getState())
            return;
        
        double x = mc.getRenderManager().renderPosX;
        double y = mc.getRenderManager().renderPosY;
        double z = mc.getRenderManager().renderPosZ;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(width.getNumberValue());
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL11.GL_LINE_STRIP);

        float size = 200 * 10;
        long fade = System.currentTimeMillis() - (long) size;
        for (Point point : points) {
            float alpha;
            float offset = (point.getTime() - fade) / size;
            if (offset < 0 || offset > 1) {
                pointCount = points.indexOf(point);
                continue;
            }
            alpha = offset;

            Color customColorValue = Color.white;
            if (colorModeValue.equalsIgnoreCase("Rainbow")) {
                customColorValue = PaletteHelper.rainbow((int) x * 300, 1, 1);
            } else if (colorModeValue.equalsIgnoreCase("Client")) {
                customColorValue = ClientHelper.getClientColor();
            } else if (colorModeValue.equalsIgnoreCase("Custom")) {
                customColorValue = new Color(customColor.getColorValue());
            } else if (colorModeValue.equalsIgnoreCase("Astolfo")) {
                customColorValue = PaletteHelper.astolfo(false, (int) alpha);
            }

            RenderHelper.setColor(customColorValue, alpha);
            GL11.glVertex3d(point.getX() - x, point.getY() - y + 1, point.getZ() - z);
            GL11.glVertex3d(point.getX() - x, point.getY() - y + 0.01, point.getZ() - z);
        }

        GL11.glEnd();

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }

    @EventTarget
    public void sendPacket(EventSendPacket event) {
        if (!getState())
            return;
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            points.add(new Point(packet.getX(mc.player.posX), packet.getY(mc.player.posY) < 0 ? mc.player.posY : packet.getY(mc.player.posY), packet.getZ(mc.player.posZ), System.currentTimeMillis()));
        }
    }

    @Override
    public void onEnable() {
        points.clear();
        pointCount = 0;
    }

    @Override
    public void onDisable() {
        points.clear();
        pointCount = 0;
    }

    public static class Point {

        public double x;
        public double y;
        public double z;
        public long time;

        public Point(double x, double y, double z, long time) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.time = time;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public long getTime() {
            return time;
        }
    }
}