package org.dreamcore.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;

public class Tracers extends Feature {

    public static BooleanSetting clientColor;
    public static ColorSetting colorGlobal;
    public static BooleanSetting friend;
    public static BooleanSetting onlyPlayer = new BooleanSetting("Only Player", true, () -> true);
    public static NumberSetting width;
    public static BooleanSetting seeOnly = new BooleanSetting("See Only", false, () -> true);

    public Tracers() {
        super("Tracers", "Рисует линию к игрокам", Type.Visuals);
        clientColor = new BooleanSetting("Client Colored", false, () -> true);
        friend = new BooleanSetting("Friend Highlight", true, () -> true);
        colorGlobal = new ColorSetting("Tracers Color", new Color(0xFFFFFF).getRGB(), () -> !clientColor.getBoolValue());
        width = new NumberSetting("Tracers Width", 1.5F, 0.1F, 5F, 0.1F, () -> true);
        addSettings(colorGlobal, friend, seeOnly, onlyPlayer, width, clientColor);
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - mc.player.posX;
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(yaw, mc.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360;
        if (yaw > 180) {
            yaw = 360 - yaw;
        }
        return yaw;
    }

    @EventTarget
    public void onEvent3D(EventRender3D event) {
        Color color = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(colorGlobal.getColorValue());
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity != mc.player) {
                if (onlyPlayer.getBoolValue() && !(entity instanceof EntityPlayer))
                    continue;

                if (seeOnly.getBoolValue() && !canSeeEntityAtFov(entity, 150))
                    return;

                boolean old = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                mc.gameSettings.viewBobbing = old;

                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;

                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.enable(GL11.GL_BLEND);
                GlStateManager.enable(GL11.GL_LINE_SMOOTH);
                GlStateManager.glLineWidth(width.getNumberValue());
                GlStateManager.disable(GL11.GL_TEXTURE_2D);
                GlStateManager.disable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(false);
                if (dreamcore.instance.friendManager.isFriend(entity.getName()) && Tracers.friend.getBoolValue()) {
                    GlStateManager.color(0f, 255, 0, 255);
                } else {
                    GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                }
                GlStateManager.glBegin(GL11.GL_LINE_STRIP);
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                GlStateManager.glVertex3d(vec.xCoord, mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                GlStateManager.glVertex3d(x, y + 1.10, z);
                GlStateManager.glEnd();
                GlStateManager.enable(GL11.GL_TEXTURE_2D);
                GlStateManager.disable(GL11.GL_LINE_SMOOTH);
                GlStateManager.enable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(true);
                GlStateManager.disable(GL11.GL_BLEND);
                GlStateManager.resetColor();
            }
        }
    }

}
