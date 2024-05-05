/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.movement;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.PacketReceiveEvent;
import com.wallhacks.losebypass.event.events.SettingChangeEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import java.util.Random;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@Module.Registration(name="Velocity", description="Take less knockback to win pvp", alwaysListening=true, category=Module.Category.MOVEMENT)
public class Velocity
extends Module {
    private final IntSetting chance = this.intSetting("Chance", 60, 1, 100).description("The chance for velocity to reduce your knockback");
    private final IntSetting minVertical = this.intSetting("MinVertical", 60, 1, 100);
    private final IntSetting maxVertical = this.intSetting("MaxVertical", 90, 1, 100);
    private final IntSetting minHorizontal = this.intSetting("MinHorizontal", 60, 1, 100);
    private final IntSetting maxHorizontal = this.intSetting("MaxHorizontal", 90, 1, 100);
    private final BooleanSetting sprintOnly = this.booleanSetting("SprintOnly", true).description("Only reduces knockback when you are sprinting, this helps to bypass anticheat and look more legit");
    private final Random random = new Random();

    @SubscribeEvent
    public void onSettingChange(SettingChangeEvent event) {
        if (event.getSetting() == this.minVertical) {
            if ((Integer)this.minVertical.getValue() <= (Integer)this.maxVertical.getValue()) return;
            this.minVertical.setValueSilent(this.maxVertical.getValue());
            return;
        }
        if (event.getSetting() == this.maxVertical) {
            if ((Integer)this.minVertical.getValue() <= (Integer)this.maxVertical.getValue()) return;
            this.maxVertical.setValueSilent(this.minVertical.getValue());
            return;
        }
        if (event.getSetting() == this.minHorizontal) {
            if ((Integer)this.minHorizontal.getValue() <= (Integer)this.maxHorizontal.getValue()) return;
            this.minHorizontal.setValueSilent(this.maxHorizontal.getValue());
            return;
        }
        if (event.getSetting() != this.maxHorizontal) return;
        if ((Integer)this.minHorizontal.getValue() <= (Integer)this.maxHorizontal.getValue()) return;
        this.maxHorizontal.setValueSilent(this.minHorizontal.getValue());
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
        if (!Velocity.mc.thePlayer.isSprinting()) {
            if ((Boolean)this.sprintOnly.getValue() != false) return;
        }
        if (!this.isEnabled()) return;
        if (((S12PacketEntityVelocity)event.getPacket()).getEntityID() != Velocity.mc.thePlayer.getEntityId()) return;
        if (this.random.nextInt(100) >= (Integer)this.chance.getValue()) return;
        S12PacketEntityVelocity velocity = (S12PacketEntityVelocity)event.getPacket();
        double horizontal = (double)((Integer)this.minHorizontal.getValue()).intValue() + (double)((Integer)this.maxHorizontal.getValue() - (Integer)this.minHorizontal.getValue()) * this.random.nextDouble();
        double vertical = (double)((Integer)this.minVertical.getValue()).intValue() + (double)((Integer)this.maxVertical.getValue() - (Integer)this.minVertical.getValue()) * this.random.nextDouble();
        velocity.motionX = (int)((double)velocity.motionX * (horizontal /= 100.0));
        velocity.motionZ = (int)((double)velocity.motionZ * horizontal);
        velocity.motionY = (int)((double)velocity.motionY * (vertical /= 100.0));
    }
}

