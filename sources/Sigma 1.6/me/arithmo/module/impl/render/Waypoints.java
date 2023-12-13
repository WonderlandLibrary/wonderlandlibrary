/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package me.arithmo.module.impl.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.management.waypoints.Waypoint;
import me.arithmo.management.waypoints.WaypointManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.RotationUtils;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Waypoints
extends Module {
    private double gradualFOVModifier;
    public static Map<Waypoint, double[]> waypointMap = new HashMap<Waypoint, double[]>();

    public Waypoints(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventRenderGui.class, EventRender3D.class})
    public void onEvent(Event event) {
        if (event instanceof EventRender3D) {
            this.updatePositions();
        } else {
            EventRenderGui er = (EventRenderGui)event;
            GlStateManager.pushMatrix();
            ScaledResolution scaledRes = new ScaledResolution(mc, Waypoints.mc.displayWidth, Waypoints.mc.displayHeight);
            double twoDscale = (double)scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
            GlStateManager.scale(twoDscale, twoDscale, twoDscale);
            for (Waypoint waypoint : waypointMap.keySet()) {
                if (waypoint.getAddress() != Waypoints.mc.getCurrentServerData().serverIP) continue;
                GlStateManager.pushMatrix();
                String str = waypoint.getName() + " " + (int)Waypoints.mc.thePlayer.getDistance(waypoint.getVec3().xCoord, waypoint.getVec3().yCoord, waypoint.getVec3().zCoord) + "m";
                double[] renderPositions = waypointMap.get(waypoint);
                if (renderPositions[3] < 0.0 || renderPositions[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.translate(renderPositions[0], renderPositions[1], 0.0);
                this.scale();
                GlStateManager.translate(0.0, -2.5, 0.0);
                int strWidth = Waypoints.mc.fontRendererObj.getStringWidth(str);
                RenderingUtil.rectangleBordered((- strWidth) / 2 - 3, -12.0, strWidth / 2 + 3, 1.0, 1.0, Colors.getColor(40, 40, 40, 190), waypoint.getColor());
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                Waypoints.mc.fontRendererObj.drawStringWithShadow(str, (- strWidth) / 2, -9.0f, waypoint.getColor());
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }

    private void scale() {
        float scale = 1.35f;
        float target = scale * (Waypoints.mc.gameSettings.fovSetting / (Waypoints.mc.gameSettings.fovSetting * Waypoints.mc.thePlayer.func_175156_o()));
        if (this.gradualFOVModifier == 0.0 || Double.isNaN(this.gradualFOVModifier)) {
            this.gradualFOVModifier = target;
        }
        this.gradualFOVModifier += ((double)target - this.gradualFOVModifier) / ((double)Minecraft.debugFPS * 0.7);
        scale = (float)((double)scale * this.gradualFOVModifier);
        GlStateManager.scale(scale, scale, scale *= (float)(Waypoints.mc.currentScreen == null && GameSettings.isKeyDown(Waypoints.mc.gameSettings.ofKeyBindZoom) ? 3 : 1));
    }

    private void updatePositions() {
        waypointMap.clear();
        float pTicks = Waypoints.mc.timer.renderPartialTicks;
        for (Waypoint waypoint : Client.wm.getWaypoints()) {
            double x = waypoint.getVec3().xCoord - Waypoints.mc.getRenderManager().viewerPosX;
            double y = waypoint.getVec3().yCoord - Waypoints.mc.getRenderManager().viewerPosY;
            double z = waypoint.getVec3().zCoord - Waypoints.mc.getRenderManager().viewerPosZ;
            if (this.convertTo2D(x, y += 0.2, z)[2] < 0.0 || this.convertTo2D(x, y, z)[2] >= 1.0) continue;
            waypointMap.put(waypoint, new double[]{this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], Math.abs(this.convertTo2D(x, y + 1.0, z, waypoint)[1] - this.convertTo2D(x, y, z, waypoint)[1]), this.convertTo2D(x, y, z)[2]});
        }
    }

    private double[] convertTo2D(double x, double y, double z, Waypoint waypoint) {
        float pTicks = Waypoints.mc.timer.renderPartialTicks;
        float prevYaw = Waypoints.mc.thePlayer.rotationYaw;
        float prevPrevYaw = Waypoints.mc.thePlayer.prevRotationYaw;
        float[] rotations = RotationUtils.getRotationFromPosition(waypoint.getVec3().xCoord, waypoint.getVec3().zCoord, waypoint.getVec3().yCoord - 1.6);
        Waypoints.mc.getRenderViewEntity().rotationYaw = Waypoints.mc.getRenderViewEntity().prevRotationYaw = rotations[0];
        Waypoints.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = this.convertTo2D(x, y, z);
        Waypoints.mc.getRenderViewEntity().rotationYaw = prevYaw;
        Waypoints.mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        Waypoints.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), (float)Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }
}

