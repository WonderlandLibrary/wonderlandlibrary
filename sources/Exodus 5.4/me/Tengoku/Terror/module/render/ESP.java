/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.Color
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import java.util.Iterator;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.Event3D;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.GLUtils;
import me.Tengoku.Terror.util.MathUtils;
import me.Tengoku.Terror.util.Render2DUtil;
import me.Tengoku.Terror.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class ESP
extends Module {
    private static double kaY = 0.0;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("ESP Mode").getValString();
        this.setDisplayName("ESP \ufffdf" + string);
    }

    public ESP() {
        super("ESP", 0, Category.RENDER, "It's just esp, nothing special.");
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Box");
        arrayList.add("2D");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("ESP Mode", (Module)this, "2D", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Draw Circle", this, true));
    }

    public static void draw2D(Entity entity, double d, double d2, double d3, int n) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(d, d2, d3);
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        RenderUtils.drawBorderedRect(-6, -20, 6, 3, 0.25, new java.awt.Color(0, 0, 0, 0), new java.awt.Color(255, 255, 255));
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)2896);
        GlStateManager.popMatrix();
    }

    public void renderNameTag(String string, double d, double d2, double d3, int n) {
        FontRenderer fontRenderer = Minecraft.fontRendererObj;
        float f = (float)(Minecraft.thePlayer.getDistance(d, d2, d3) / 4.0);
        if (f < 1.6f) {
            f = 1.6f;
        }
        RenderManager renderManager = mc.getRenderManager();
        float f2 = f;
        f2 /= 50.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d), (float)((float)d2 + 0.3f), (float)((float)d3));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-RenderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int n2 = Minecraft.fontRendererObj.getStringWidth(string) / 2;
        int n3 = 17;
        boolean bl = false;
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glScalef((float)0.0165f, (float)0.0165f, (float)0.0165f);
        GL11.glEnable((int)2848);
        GL11.glScalef((float)-1.0f, (float)-1.0f, (float)-1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    @EventTarget
    public void onRender3D(Event3D event3D) {
        String string2;
        if (Exodus.INSTANCE.settingsManager.getSettingByClass("Draw Circle", ESP.class).getValBoolean()) {
            for (String string2 : Minecraft.theWorld.playerEntities) {
                if (!(string2 instanceof EntityPlayer) || ((EntityPlayer)((Object)string2)).isInvisible()) continue;
                Iterator iterator = (EntityPlayer)((Object)string2);
                ESP.drawCircle(iterator, event3D.getPartialTicks(), 0.5);
            }
        }
        if ((string2 = Exodus.INSTANCE.settingsManager.getSettingByName("ESP Mode").getValString()).equalsIgnoreCase("Chams")) {
            for (Object object : Minecraft.theWorld.playerEntities) {
                if (!(object instanceof EntityPlayer) || object == Minecraft.thePlayer) continue;
                EntityPlayer entityPlayer = (EntityPlayer)object;
                GL11.glPushMatrix();
                GlStateManager.translate(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
                double d = entityPlayer.posX;
                double d2 = entityPlayer.posY + (double)entityPlayer.getEyeHeight() + 0.3;
                double d3 = entityPlayer.posZ;
                String string3 = "";
                if (entityPlayer.getHealth() >= 15.0f && entityPlayer.getHealth() <= 20.0f) {
                    string3 = " \u00a7\ufffda";
                } else if (entityPlayer.getHealth() >= 10.0f && entityPlayer.getHealth() < 15.0f) {
                    string3 = " \u00a7\ufffd6";
                } else if (entityPlayer.getHealth() >= 5.0f && entityPlayer.getHealth() < 10.0f) {
                    string3 = " \u00a7\ufffdc";
                } else if (entityPlayer.getHealth() >= 0.0f && entityPlayer.getHealth() < 5.0f) {
                    string3 = " \u00a7\ufffd4";
                }
                String string4 = "";
                String string5 = "";
                if (!entityPlayer.isSneaking()) {
                    string5 = "";
                } else if (entityPlayer.isSneaking()) {
                    string5 = "\u00a7\ufffd6[S] ";
                }
                int n = 17;
                int n2 = 0;
                if (!entityPlayer.isOnSameTeam(Minecraft.thePlayer)) {
                    n2 = new Color(255, 102, 0, 255).getRed();
                } else if (entityPlayer.isOnSameTeam(Minecraft.thePlayer)) {
                    n2 = new Color(0, 166, 22, 255).getRed();
                }
                this.renderNameTag(String.valueOf(String.valueOf(string3)) + MathUtils.round((double)entityPlayer.getHealth(), 2), d, d2, d3, n2);
                GlStateManager.translate(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);
                GL11.glPopMatrix();
            }
        }
        if (string2.equalsIgnoreCase("Box") && this.isToggled()) {
            for (Object object : Minecraft.theWorld.loadedEntityList) {
                if (!(object instanceof EntityPlayer) || object instanceof EntityPlayerSP) continue;
                Render2DUtil.entityESPBox((Entity)object, 0);
            }
        }
    }

    @EventTarget
    public void onEntityRender(Event3D event3D) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("ESP Mode", this).getValString();
        if (string.equalsIgnoreCase("2D") && this.isToggled()) {
            for (Entity entity : Minecraft.theWorld.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity instanceof EntityPlayerSP || entity.getUniqueID() == null || entity.isInvisible()) continue;
                EntityPlayer entityPlayer = (EntityPlayer)entity;
                double d = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)event3D.getPartialTicks();
                double d2 = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)event3D.getPartialTicks();
                double d3 = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)event3D.getPartialTicks();
                ESP.draw2D(entityPlayer, d - RenderManager.renderPosX, d2 - RenderManager.renderPosY, d3 - RenderManager.renderPosZ, new java.awt.Color(255, 0, 0, 255).getRGB());
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
        }
    }

    public static void drawCircle(Entity entity, float f, double d) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GLUtils.startSmooth();
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)3);
        double d2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        mc.getRenderManager();
        double d3 = d2 - RenderManager.viewerPosX;
        double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        mc.getRenderManager();
        double d5 = d4 - RenderManager.viewerPosY;
        double d6 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
        mc.getRenderManager();
        double d7 = d6 - RenderManager.viewerPosZ;
        float f2 = CustomIngameGui.color;
        double d8 = Math.PI * 2;
        int n = 0;
        while (n <= 90) {
            GL11.glVertex3d((double)(d3 + d * Math.cos((double)n * (Math.PI * 2) / 45.0)), (double)d5, (double)(d7 + d * Math.sin((double)n * (Math.PI * 2) / 45.0)));
            ++n;
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GLUtils.endSmooth();
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }
}

