/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.AntiBot;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.lwjgl.opengl.GL11;

public class DamageMarkers
extends Feature {
    private final NumberSetting markerSize;
    private final NumberSetting ticksToRemove;
    private final BooleanSetting checkYourself;
    public ListSetting markerMode = new ListSetting("Marker Mode", "Text", () -> true, "Text", "Ban");
    public ListSetting markerColorMode = new ListSetting("Marker Color Mode", "Damage", () -> this.markerMode.currentMode.equals("Text"), "Damage", "Client", "Custom");
    public ColorSetting markerColor = new ColorSetting("Marker Color", Color.RED.getRGB(), () -> this.markerColorMode.currentMode.equals("Custom") && this.markerMode.currentMode.equals("Text"));
    private final HashMap<Integer, Float> healthMap = new HashMap();
    private final ArrayList<Marker> particles = new ArrayList();

    public DamageMarkers() {
        super("DamageMarkers", "\u041e\u0442\u043e\u0431\u0440\u043e\u0436\u0430\u0435\u0442 \u0440\u0435\u0433\u0435\u043d\u0435\u0440\u0430\u0446\u0438\u044e/\u0434\u0430\u043c\u0430\u0433 \u0432\u0441\u0435\u0445 \u044d\u043d\u0442\u0438\u0442\u0438 \u0432\u043e\u043a\u0440\u0443\u0433", Type.Visuals);
        this.markerSize = new NumberSetting("Marker size", 0.5f, 0.1f, 3.0f, 0.1f, () -> true);
        this.ticksToRemove = new NumberSetting("Ticks to remove", 35.0f, 5.0f, 50.0f, 5.0f, () -> true);
        this.checkYourself = new BooleanSetting("Check Yourself", true, () -> true);
        this.addSettings(this.markerMode, this.markerColor, this.markerSize, this.ticksToRemove, this.checkYourself);
    }

    @Override
    public void onDisable() {
        this.particles.clear();
        this.healthMap.clear();
        super.onDisable();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onUpdate(EventUpdate event) {
        ArrayList<Marker> arrayList = this.particles;
        synchronized (arrayList) {
            for (Entity entity : DamageMarkers.mc.world.loadedEntityList) {
                if (entity == null || DamageMarkers.mc.player.getDistanceToEntity(entity) > 10.0f || entity.isDead || entity == DamageMarkers.mc.player && this.checkYourself.getCurrentValue() || !(entity instanceof EntityLivingBase)) continue;
                float lastHealth = this.healthMap.getOrDefault(entity.getEntityId(), Float.valueOf(((EntityLivingBase)entity).getMaxHealth())).floatValue();
                this.healthMap.put(entity.getEntityId(), Float.valueOf(((EntityLivingBase)entity).getHealth()));
                if (lastHealth == ((EntityLivingBase)entity).getHealth()) continue;
                this.particles.add(new Marker(entity, "" + BigDecimal.valueOf(Math.abs(lastHealth - ((EntityLivingBase)entity).getHealth())).setScale(1, 4), entity.posX - 0.5 + (double)new Random(System.currentTimeMillis()).nextInt(5) * 0.1, entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0, entity.posZ - 0.5 + (double)new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.1));
            }
            ArrayList<Marker> needRemove = new ArrayList<Marker>();
            for (Marker marker : this.particles) {
                marker.ticks++;
                if (!((float)marker.ticks >= this.ticksToRemove.getCurrentValue()) && !marker.getEntity().isDead) continue;
                needRemove.add(marker);
            }
            for (Marker marker : needRemove) {
                this.particles.remove(marker);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(EventRender3D event) {
        ArrayList<Marker> arrayList = this.particles;
        synchronized (arrayList) {
            for (Marker marker : this.particles) {
                if (Celestial.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(marker.getEntity()) && (AntiBot.mode.currentMode.equals("Matrix New") || AntiBot.mode.currentMode.equals("Matrix"))) {
                    return;
                }
                RenderManager renderManager = mc.getRenderManager();
                double size = this.markerMode.currentMode.equals("Text") ? (double)(this.markerSize.getCurrentValue() / (float)marker.ticks * 2.0f) * 0.1 : (double)this.markerSize.getCurrentValue() * 0.1;
                size = MathHelper.clamp(size, 0.03, (double)this.markerSize.getCurrentValue());
                double x = marker.posX - renderManager.renderPosX;
                double y = marker.posY - renderManager.renderPosY;
                double z = marker.posZ - renderManager.renderPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                double textY = DamageMarkers.mc.gameSettings.thirdPersonView == 2 ? -1.0 : 1.0;
                GlStateManager.rotate(renderManager.playerViewX, (float)textY, 0.0f, 0.0f);
                GlStateManager.scale(-size, -size, size);
                GL11.glDepthMask(false);
                int color = -1;
                int oneColor = this.markerColor.getColor();
                switch (this.markerColorMode.currentMode) {
                    case "Client": {
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    }
                    case "Custom": {
                        color = oneColor;
                        break;
                    }
                    case "Damage": {
                        boolean startWithGreen;
                        boolean startWithRed = marker.str.startsWith("5") || marker.str.startsWith("4") || marker.str.startsWith("6") || marker.str.startsWith("7") || marker.str.startsWith("8") || marker.str.startsWith("9") || marker.str.startsWith("10") || marker.str.startsWith("11") || marker.str.startsWith("12") || marker.str.startsWith("13") || marker.str.startsWith("14") || marker.str.startsWith("15") || marker.str.startsWith("16") || marker.str.startsWith("17") || marker.str.startsWith("18") || marker.str.startsWith("19") || marker.str.startsWith("20") || marker.str.startsWith("21") || marker.str.startsWith("22") || marker.str.startsWith("23") || marker.str.startsWith("24") || marker.str.startsWith("25") || marker.str.startsWith("26") || marker.str.startsWith("27") || marker.str.startsWith("28") || marker.str.startsWith("29") || marker.str.startsWith("30");
                        boolean startWithYellow = marker.str.startsWith("3");
                        boolean bl = startWithGreen = marker.str.startsWith("1") || marker.str.startsWith("2");
                        int n = startWithRed ? Color.RED.getRGB() : (startWithYellow ? Color.YELLOW.getRGB() : (color = startWithGreen ? Color.GREEN.getRGB() : Color.WHITE.getRGB()));
                    }
                }
                if (this.markerMode.currentMode.equals("Text")) {
                    DamageMarkers.mc.fontRendererObj.drawStringWithShadow(marker.str, -((float)DamageMarkers.mc.fontRendererObj.getStringWidth(marker.str) / 2.0f), -(DamageMarkers.mc.fontRendererObj.FONT_HEIGHT - 1), color);
                } else {
                    RenderHelper.drawImage(new ResourceLocation("celestial/ban.png"), -((float)DamageMarkers.mc.fontRendererObj.getStringWidth(marker.str) / 2.0f), -(DamageMarkers.mc.fontRendererObj.FONT_HEIGHT - 1), this.markerSize.getCurrentValue() * 10.0f, this.markerSize.getCurrentValue() * 10.0f, Color.WHITE);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
                GlStateManager.disablePolygonOffset();
                GlStateManager.popMatrix();
            }
        }
    }

    private class Marker {
        private Entity entity;
        private String str;
        private double posX;
        private double posY;
        private double posZ;
        private int ticks = 0;

        public Marker(Entity entity, String str, double posX, double posY, double posZ) {
            this.entity = entity;
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }

        public Entity getEntity() {
            return this.entity;
        }

        public String getStr() {
            return this.str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public double getPosX() {
            return this.posX;
        }

        public void setPosX(double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return this.posY;
        }

        public void setPosY(double posY) {
            this.posY = posY;
        }

        public double getPosZ() {
            return this.posZ;
        }

        public void setPosZ(double posZ) {
            this.posZ = posZ;
        }

        public int getTicks() {
            return this.ticks;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }
    }
}

