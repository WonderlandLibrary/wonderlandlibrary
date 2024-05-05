/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.AveReborn.Value;
import me.AveReborn.events.EventPacket;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Mod {
    public Value<String> mode = new Value("Velocity", "Mode", 0);
    public Value<Double> verti = new Value<Double>("Velocity_Vertical", 1.0, 0.0, 1.0, 0.01);
    public Value<Double> hori = new Value<Double>("Velocity_Horizontal", 1.0, 0.0, 1.0, 0.01);

    public Velocity() {
        super("Velocity", Category.COMBAT);
        this.mode.mode.add("Normal");
        this.mode.mode.add("AACNormal");
        this.mode.mode.add("AACReverse");
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.motionX *= 1.0;
        Minecraft.thePlayer.motionZ *= 1.0;
        Minecraft.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }

    @EventTarget
    public void onReceivePacket(EventPacket event) {
        if (this.mode.isCurrentMode("Normal")) {
            this.setDisplayName("Normal");
            if (event.packet instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.packet;
                if (this.verti.getValueState().intValue() == 0 && this.hori.getValueState().intValue() == 0) {
                    event.setCancelled(true);
                } else {
                    packet.setMotionX(packet.getMotionX() * this.hori.getValueState().intValue());
                    packet.setMotionY(packet.getMotionY() * this.verti.getValueState().intValue());
                    packet.setMotionZ(packet.getMotionZ() * this.hori.getValueState().intValue());
                }
            }
            if (event.packet instanceof S27PacketExplosion) {
                S27PacketExplosion exp = (S27PacketExplosion)event.packet;
                if (this.verti.getValueState().intValue() == 0 && this.hori.getValueState().intValue() == 0) {
                    event.setCancelled(true);
                } else {
                    exp.setX(exp.getPosX() * (float)this.hori.getValueState().intValue());
                    exp.setY(exp.getPosY() * (float)this.verti.getValueState().intValue());
                    exp.setZ(exp.getPosZ() * (float)this.hori.getValueState().intValue());
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.mode.isCurrentMode("AACNormal")) {
            this.setDisplayName("AAC Normal");
            if (Minecraft.thePlayer.hurtTime > 0) {
                Minecraft.thePlayer.motionX *= 0.6;
                Minecraft.thePlayer.motionZ *= 0.6;
            } else {
                Minecraft.thePlayer.motionX *= 1.0;
                Minecraft.thePlayer.motionZ *= 1.0;
            }
        }
        if (this.mode.isCurrentMode("AACReverse")) {
            this.setDisplayName("AAC Reverse");
            if (Minecraft.thePlayer.hurtTime > 0) {
                Minecraft.thePlayer.motionX *= 0.9;
                Minecraft.thePlayer.motionZ *= 0.9;
                Minecraft.thePlayer.speedInAir = 0.05f;
            } else {
                Minecraft.thePlayer.motionX *= 1.0;
                Minecraft.thePlayer.motionZ *= 1.0;
                Minecraft.thePlayer.motionY *= 1.0;
                Minecraft.thePlayer.speedInAir = 0.02f;
            }
        }
    }
}

