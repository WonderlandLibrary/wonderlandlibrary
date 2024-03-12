// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.tenacity.event.impl.player.SlowDownEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class NoSlow extends Module
{
    private final ModeSetting mode;
    final NumberSetting forward;
    final NumberSetting strafe;
    private boolean synced;
    
    public NoSlow() {
        super("NoSlow", "No Slow", Category.MOVEMENT, "Allows you to move at normal speed while using items.");
        this.mode = new ModeSetting("Mode", "Watchdog", new String[] { "Vanilla", "NCP", "Watchdog" });
        this.forward = new NumberSetting("Forward Multiplier", 1.0, 1.0, 0.20000000298023224, 0.10000000149011612);
        this.strafe = new NumberSetting("Strafe Multiplier", 1.0, 1.0, 0.20000000298023224, 0.10000000149011612);
        this.addSettings(this.mode, this.forward, this.strafe);
    }
    
    @Override
    public void onSlowDownEvent(final SlowDownEvent event) {
        final EntityPlayerSP thePlayer = NoSlow.mc.thePlayer;
        thePlayer.moveForward *= this.forward.getValue().floatValue();
        final EntityPlayerSP thePlayer2 = NoSlow.mc.thePlayer;
        thePlayer2.moveStrafing *= this.strafe.getValue().floatValue();
        event.cancel();
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        this.setSuffix(this.mode.getMode());
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Watchdog": {
                if (NoSlow.mc.thePlayer.onGround && NoSlow.mc.thePlayer.isUsingItem() && MovementUtils.isMoving()) {
                    if (e.isPre()) {
                        NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(NoSlow.mc.thePlayer.inventory.currentItem));
                        this.synced = true;
                    }
                    else {
                        NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange((NoSlow.mc.thePlayer.inventory.currentItem < 8) ? (NoSlow.mc.thePlayer.inventory.currentItem + 1) : (NoSlow.mc.thePlayer.inventory.currentItem - 1)));
                        this.synced = false;
                    }
                }
                if (!this.synced) {
                    NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(NoSlow.mc.thePlayer.inventory.currentItem));
                    this.synced = true;
                    break;
                }
                break;
            }
            case "NCP": {
                if (!MovementUtils.isMoving() || !NoSlow.mc.thePlayer.isUsingItem() || !NoSlow.mc.thePlayer.isOnGround() || NoSlow.mc.thePlayer.isEating()) {
                    break;
                }
                if (e.isPre()) {
                    PacketUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    break;
                }
                PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.getCurrentEquippedItem()));
                break;
            }
        }
    }
}
