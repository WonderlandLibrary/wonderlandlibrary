/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.systems.module.modules.render;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.Render2DEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.AntiBot;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import com.wallhacks.losebypass.utils.ColorUtil;
import java.awt.Color;
import java.util.Arrays;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

@Module.Registration(name="Radar", description="Shows nearby enemies", category=Module.Category.RENDER)
public class Radar
extends Module {
    private final IntSetting tilt = this.intSetting("Tilt", 45, 20, 90);
    private final IntSetting radius = this.intSetting("Radius", 45, 10, 200);
    private final IntSetting size = this.intSetting("Size", 10, 5, 25);
    private final ColorSetting friendColor = this.colorSetting("FriendColor", new Color(3064000));
    private final ModeSetting color = this.modeSetting("ColorMode", "Custom", Arrays.asList("Custom", "Distance"));
    private final ColorSetting customColor = this.colorSetting("Color", new Color(9927214)).visibility(v -> this.color.is("Custom"));
    private final ColorSetting closeColor = this.colorSetting("CloseColor", new Color(0xFF0000)).visibility(v -> this.color.is("Distance"));
    private final ColorSetting farColor = this.colorSetting("FarColor", new Color(65280)).visibility(v -> this.color.is("Distance"));

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        if (Radar.mc.getRenderViewEntity().rotationPitch < 0.0f && (Integer)this.tilt.getValue() >= 90) {
            return;
        }
        float tiltDegree = Math.max(0.0f, Math.min(90.0f - Radar.mc.getRenderViewEntity().rotationPitch, (float)((Integer)this.tilt.getValue()).intValue()));
        int x = Display.getWidth() / 2 / (Radar.mc.gameSettings.guiScale == 0 ? 1 : Radar.mc.gameSettings.guiScale);
        int y = Display.getHeight() / 2 / (Radar.mc.gameSettings.guiScale == 0 ? 1 : Radar.mc.gameSettings.guiScale);
        GL11.glPushMatrix();
        GL11.glPushAttrib((int)1048575);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GL11.glBlendFunc((int)770, (int)771);
        Radar.mc.theWorld.playerEntities.stream().filter(entityPlayer -> {
            if (!AntiBot.allowRender(entityPlayer)) return false;
            if (entityPlayer.isInvisible()) return false;
            return true;
        }).forEach(entityPlayer -> this.drawTracer(x, y, tiltDegree, (EntityPlayer)entityPlayer));
        GL11.glEnable((int)3553);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable((int)2848);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private void drawTracer(int x, int y, float tiltDegree, EntityPlayer entityPlayer) {
        float yaw = this.rotations(entityPlayer) - Radar.mc.getRenderViewEntity().rotationYaw;
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        GL11.glRotatef((float)tiltDegree, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.02f);
        this.drawTracerPointer(x, y - (Integer)this.radius.getValue(), ((Integer)this.size.getValue()).intValue(), this.color(entityPlayer));
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        GL11.glRotatef((float)(-yaw), (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glRotatef((float)(-tiltDegree), (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
    }

    private Color color(EntityPlayer entity) {
        Color color;
        if (LoseBypass.socialManager.isFriend(entity)) {
            color = this.friendColor.getColor();
            return color;
        }
        if (this.color.is("Custom")) {
            color = this.customColor.getColor();
            return color;
        }
        color = this.distanceColor(entity);
        return color;
    }

    private Color distanceColor(EntityPlayer entity) {
        float l = MathHelper.clamp_float((Radar.mc.thePlayer.getDistanceToEntity(entity) - 5.0f) / 40.0f, 0.0f, 1.0f);
        return ColorUtil.lerpColor(this.closeColor.getColor(), this.farColor.getColor(), l);
    }

    private float rotations(EntityLivingBase ent) {
        float partialTicks = Radar.mc.timer.renderPartialTicks;
        double x = ent.posX + (ent.posX - ent.lastTickPosX) * (double)partialTicks - (Radar.mc.getRenderViewEntity().posX + (Radar.mc.getRenderViewEntity().posX - Radar.mc.getRenderViewEntity().lastTickPosX) * (double)partialTicks);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ) * (double)partialTicks - (Radar.mc.getRenderViewEntity().posZ + (Radar.mc.getRenderViewEntity().posZ - Radar.mc.getRenderViewEntity().lastTickPosZ) * (double)partialTicks);
        return (float)(-(Math.atan2(x, z) * (double)57.29578f));
    }

    private void drawTracerPointer(float x, float y, float size, Color color) {
        GL11.glPushMatrix();
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glBegin((int)6);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x - size / 2.0f), (double)(y + size));
        GL11.glVertex2d((double)x, (double)((double)y + (double)size / 1.5));
        GL11.glVertex2d((double)(x + size / 2.0f), (double)(y + size));
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}

