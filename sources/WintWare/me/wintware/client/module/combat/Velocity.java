/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity
extends Module {
    public Velocity() {
        super("Velocity", Category.Combat);
        ArrayList<String> mode = new ArrayList<String>();
        mode.add("Packet");
        mode.add("Motion");
        mode.add("AirStrafe");
        mode.add("MatrixVertical");
        Main.instance.setmgr.rSetting(new Setting("Velocity mode", this, "Packet", mode));
        Main.instance.setmgr.rSetting(new Setting("Vertical Percentage", this, 0.0, 0.0, 100.0, false));
        Main.instance.setmgr.rSetting(new Setting("Horizontal Percentage", this, 0.0, 0.0, 100.0, false));
    }

    @EventTarget
    public void onMotion(EventReceivePacket event) {
        String mode = Main.instance.setmgr.getSettingByName("Velocity Mode").getValString();
        double hori = Main.instance.setmgr.getSettingByName("Horizontal Percentage").getValDouble();
        double vert = Main.instance.setmgr.getSettingByName("Vertical Percentage").getValDouble();
        if (mode.equalsIgnoreCase("Motion")) {
            Entity entity;
            this.setSuffix("H: " + hori + "% V: " + vert + "%");
            if (event.getPacket() instanceof SPacketEntityVelocity && (entity = Velocity.mc.getConnection().clientWorldController.getEntityByID(((SPacketEntityVelocity)event.getPacket()).getEntityID())) instanceof EntityPlayerSP) {
                SPacketEntityVelocity vel = (SPacketEntityVelocity)event.getPacket();
                if (hori == 0.0 && vert == 0.0) {
                    event.setCancelled(true);
                    return;
                }
                if (hori != 100.0) {
                    SPacketEntityVelocity.motionX = (int)((double)(SPacketEntityVelocity.motionX / 100) * hori);
                    SPacketEntityVelocity.motionZ = (int)((double)(SPacketEntityVelocity.motionZ / 100) * hori);
                }
                if (vert != 100.0) {
                    SPacketEntityVelocity.motionY = (int)((double)(SPacketEntityVelocity.motionY / 100) * vert);
                }
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                SPacketExplosion vel = (SPacketExplosion)event.getPacket();
                if (hori == 0.0 && vert == 0.0) {
                    event.setCancelled(true);
                    return;
                }
                if (hori != 100.0) {
                    SPacketExplosion.motionX = (int)((double)(SPacketExplosion.motionX / 100.0f) * hori);
                    SPacketExplosion.motionZ = (int)((double)(SPacketExplosion.motionZ / 100.0f) * hori);
                }
                if (vert != 100.0) {
                    SPacketExplosion.motionY = (int)((double)(SPacketExplosion.motionY / 100.0f) * vert);
                }
            }
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        String mode = Main.instance.setmgr.getSettingByName("Velocity Mode").getValString();
        if (!mode.equalsIgnoreCase("Motion")) {
            this.setSuffix(mode);
        }
        if (mode.equalsIgnoreCase("Packet")) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.setCancelled(true);
            }
        }
        if (mode.equalsIgnoreCase("AirStrafe")) {
            if (Minecraft.player.hurtTime > 0) {
                MovementUtil.strafe();
            }
        }
    }
}

