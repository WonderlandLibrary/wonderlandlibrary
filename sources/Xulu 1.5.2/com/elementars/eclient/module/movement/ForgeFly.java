// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.player.PlayerCapabilities;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ForgeFly extends Module
{
    private final Value<Boolean> fenable;
    private final Value<Double> speed;
    
    public ForgeFly() {
        super("ForgeFly", "ForgeHax ElytraFlight", 0, Category.MOVEMENT, true);
        this.fenable = this.register(new Value<Boolean>("Fly on Enable", this, false));
        this.speed = this.register(new Value<Double>("Speed", this, 0.05, 0.0, 10.0));
    }
    
    private void enableFly() {
        if (ForgeFly.mc.player == null || ForgeFly.mc.player.capabilities == null) {
            return;
        }
        ForgeFly.mc.player.capabilities.allowFlying = true;
        ForgeFly.mc.player.capabilities.isFlying = true;
    }
    
    private void disableFly() {
        if (ForgeFly.mc.player == null || ForgeFly.mc.player.capabilities == null) {
            return;
        }
        final PlayerCapabilities gmCaps = new PlayerCapabilities();
        ForgeFly.mc.playerController.getCurrentGameType().configurePlayerCapabilities(gmCaps);
        final PlayerCapabilities capabilities = ForgeFly.mc.player.capabilities;
        capabilities.allowFlying = gmCaps.allowFlying;
        capabilities.isFlying = (gmCaps.allowFlying && capabilities.isFlying);
        capabilities.setFlySpeed(gmCaps.getFlySpeed());
    }
    
    @Override
    public void onEnable() {
        if (this.fenable.getValue()) {
            ForgeFly.mc.addScheduledTask(() -> {
                if (ForgeFly.mc.player != null && !ForgeFly.mc.player.isElytraFlying()) {
                    ForgeFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ForgeFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                }
            });
        }
    }
    
    @Override
    public void onDisable() {
        this.disableFly();
        if (ForgeFly.mc.player != null) {
            ForgeFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ForgeFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }
    
    @Override
    public void onUpdate() {
        if (ForgeFly.mc.player.isElytraFlying()) {
            this.enableFly();
        }
        ForgeFly.mc.player.capabilities.setFlySpeed(this.speed.getValue().floatValue());
    }
}
