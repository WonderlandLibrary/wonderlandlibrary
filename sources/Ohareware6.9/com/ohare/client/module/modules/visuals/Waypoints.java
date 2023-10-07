package com.ohare.client.module.modules.visuals;

import com.ohare.client.Client;
import com.ohare.client.event.events.render.Render2DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.RenderUtil;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * made by oHare for oHareWare
 *
 * @since 7/16/2019
 **/
public class Waypoints extends Module {
    public Waypoints() {
        super("Waypoints", Category.VISUALS, new Color(0x85DEE1).getRGB());
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        final ScaledResolution scaledRes = new ScaledResolution(mc);
        if (!mc.isSingleplayer() && mc.getCurrentServerData() != null) {
            Client.INSTANCE.getWaypointManager().getWaypoints().forEach(waypoint -> {
                if (mc.getCurrentServerData().serverIP.equals(waypoint.getServer()) && mc.thePlayer.dimension == waypoint.getDimension()) {
                    double posX = waypoint.getX();
                    double posY = waypoint.getY();
                    double posZ = waypoint.getZ();
                    AxisAlignedBB bb = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f));
                    mc.entityRenderer.setupCameraTransform(event.getPT(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    mc.entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        GlStateManager.scale(.5f, .5f, .5f);
                        float x = (float) position.x * 2;
                        float x2 = (float) position.z * 2;
                        float y = (float) position.y * 2;
                        final String nametext = "(" + Math.round(mc.thePlayer.getDistance(waypoint.getX(), waypoint.getY(), waypoint.getZ())) + "m) " + waypoint.getLabel();
                        RenderUtil.drawRect2((x + (x2 - x) / 2) - (mc.fontRendererObj.getStringWidth(nametext) >> 1) - 2, y - mc.fontRendererObj.FONT_HEIGHT - 4, (x + (x2 - x) / 2) + (mc.fontRendererObj.getStringWidth(nametext) >> 1) + 2, y - 2, new Color(0, 0, 0, 120).getRGB());
                        RenderUtil.drawCustomString(nametext, (x + ((x2 - x) / 2)) - (mc.fontRendererObj.getStringWidth(nametext) / 2), y - mc.fontRendererObj.FONT_HEIGHT - 2, -1);
                        GL11.glPopMatrix();
                    }
                }
            });
        }
    }
}

