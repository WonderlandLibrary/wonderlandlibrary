/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.EnumSkyBlock;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRenderEntity;
import org.celestial.client.event.events.impl.render.EventRenderWorldLight;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;

public class NoRender
extends Feature {
    public static BooleanSetting rain;
    public static BooleanSetting hurt;
    public static BooleanSetting pumpkin;
    public static BooleanSetting armor;
    public static BooleanSetting totem;
    public static BooleanSetting blindness;
    public static BooleanSetting cameraSmooth;
    public static BooleanSetting cameraBounds;
    public static BooleanSetting noScoreBoard;
    public static BooleanSetting fire;
    public static BooleanSetting light;
    public static BooleanSetting fog;
    public static BooleanSetting bossBar;
    public static BooleanSetting tnt;
    public static BooleanSetting crystal;
    public static BooleanSetting fireworks;
    public static BooleanSetting swing;
    public static BooleanSetting sign;
    public static BooleanSetting frame;
    public static BooleanSetting banner;
    public static BooleanSetting glintEffect;
    public static BooleanSetting badEffects;
    public static BooleanSetting noWitherHearts;

    public NoRender() {
        super("NoRender", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u044b\u0435 \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u044b \u0440\u0435\u043d\u0434\u0435\u0440\u0430 \u0432 \u0438\u0433\u0440\u0435", Type.Visuals);
        rain = new BooleanSetting("Rain", true, () -> true);
        hurt = new BooleanSetting("HurtCamera", true, () -> true);
        pumpkin = new BooleanSetting("Pumpkin", true, () -> true);
        armor = new BooleanSetting("Armor", false, () -> true);
        totem = new BooleanSetting("Totem", true, () -> true);
        blindness = new BooleanSetting("Blindness", true, () -> true);
        cameraSmooth = new BooleanSetting("Camera Smooth", true, () -> true);
        cameraBounds = new BooleanSetting("Camera Bounds", false, () -> true);
        noScoreBoard = new BooleanSetting("No ScoreBoard", false, () -> true);
        fire = new BooleanSetting("Fire", true, () -> true);
        light = new BooleanSetting("Light", false, () -> true);
        fog = new BooleanSetting("Fog", false, () -> true);
        bossBar = new BooleanSetting("Boss Bar", true, () -> true);
        tnt = new BooleanSetting("Tnt", false, () -> true);
        crystal = new BooleanSetting("Crystal", false, () -> true);
        fireworks = new BooleanSetting("FireWorks", false, () -> true);
        swing = new BooleanSetting("Swing", false, () -> true);
        sign = new BooleanSetting("Sign", false, () -> true);
        frame = new BooleanSetting("Frame", false, () -> true);
        banner = new BooleanSetting("Banner", false, () -> true);
        glintEffect = new BooleanSetting("Glint Effect", false, () -> true);
        badEffects = new BooleanSetting("Bad Effects", false, () -> true);
        noWitherHearts = new BooleanSetting("No Wither Hearts", false, () -> true);
        this.addSettings(rain, hurt, pumpkin, armor, totem, blindness, noWitherHearts, cameraSmooth, cameraBounds, noScoreBoard, fire, light, fog, bossBar, tnt, crystal, fireworks, swing, sign, frame, banner, badEffects, glintEffect);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (!rain.getCurrentValue()) {
            return;
        }
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onEntityRenderer(EventRenderEntity event) {
        if (!this.getState()) {
            return;
        }
        if (event.getEntity() != null) {
            if (fireworks.getCurrentValue() && event.getEntity() instanceof EntityFireworkRocket) {
                event.setCancelled(true);
            } else if (crystal.getCurrentValue() && event.getEntity() instanceof EntityEnderCrystal) {
                event.setCancelled(true);
            } else if (tnt.getCurrentValue() && event.getEntity() instanceof EntityTNTPrimed) {
                event.setCancelled(true);
            } else if (frame.getCurrentValue() && event.getEntity() instanceof EntityItemFrame) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (banner.getCurrentValue()) {
            for (TileEntity e : NoRender.mc.world.loadedTileEntityList) {
                if (!(e instanceof TileEntityBanner)) continue;
                NoRender.mc.world.removeTileEntity(e.getPos());
            }
        }
        if (badEffects.getCurrentValue()) {
            if (NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
                NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
            }
            if (NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
                NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
            }
        }
        if (cameraSmooth.getCurrentValue()) {
            NoRender.mc.gameSettings.smoothCamera = false;
        }
        if (rain.getCurrentValue()) {
            NoRender.mc.world.setRainStrength(0.0f);
            NoRender.mc.world.setThunderStrength(0.0f);
        }
        if (blindness.getCurrentValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS) || NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
    }

    @EventTarget
    public void onWorldLight(EventRenderWorldLight event) {
        if (!this.getState()) {
            return;
        }
        if (light.getCurrentValue() && event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
            event.setCancelled(true);
        }
    }
}

