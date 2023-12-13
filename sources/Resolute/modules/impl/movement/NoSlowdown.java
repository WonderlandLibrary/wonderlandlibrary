// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemStack;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.modules.impl.combat.KillAura;
import net.minecraft.network.Packet;
import net.minecraft.item.ItemSword;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.impl.EventSlow;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtils;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class NoSlowdown extends Module
{
    public ModeSetting mode;
    private static final C07PacketPlayerDigging PLAYER_DIGGING;
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT;
    public static boolean enabled;
    TimerUtils timer;
    
    public NoSlowdown() {
        super("NoSlow", 0, "Removes item slowdown", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "NCP", new String[] { "NCP", "AAC5", "Vanilla" });
        this.timer = new TimerUtils();
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        NoSlowdown.mc.timer.timerSpeed = 1.0f;
        NoSlowdown.mc.thePlayer.speedInAir = 0.02f;
        NoSlowdown.enabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        NoSlowdown.mc.timer.timerSpeed = 1.0f;
        NoSlowdown.enabled = false;
        NoSlowdown.mc.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        if (e instanceof EventSlow) {
            e.setCancelled(true);
        }
        if (e instanceof EventMotion) {
            final EventMotion event = (EventMotion)e;
            if (this.mode.is("AAC5")) {
                final ItemStack heldItem = NoSlowdown.mc.thePlayer.getHeldItem();
                if (NoSlowdown.mc.thePlayer.isUsingItem() && heldItem != null && !(heldItem.getItem() instanceof ItemSword)) {
                    if (e.isPre()) {
                        NoSlowdown.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlowdown.mc.thePlayer.inventory.getCurrentItem()));
                    }
                }
                else {
                    if (!NoSlowdown.mc.thePlayer.isBlocking() && !KillAura.blocking) {
                        return;
                    }
                    this.sendPacket(event, true, true, true, 200L, false, false);
                }
            }
            if (this.mode.is("NCP") && MovementUtils.isMoving() && !KillAura.blocking && NoSlowdown.mc.thePlayer.isBlocking()) {
                if (e.isPre()) {
                    NoSlowdown.mc.getNetHandler().sendPacketNoEvent(NoSlowdown.PLAYER_DIGGING);
                }
                else {
                    NoSlowdown.mc.getNetHandler().sendPacketNoEvent(NoSlowdown.BLOCK_PLACEMENT);
                }
            }
        }
        if (!(e instanceof EventUpdate) || !this.mode.is("Vanilla") || NoSlowdown.mc.thePlayer.isBlocking() || NoSlowdown.mc.thePlayer.isEating()) {}
    }
    
    private void sendPacket(final EventMotion event, final boolean sendC07, final boolean sendC08, final boolean delay, final long delayValue, final boolean onGround, final boolean watchDog) {
        final C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN);
        final C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(NoSlowdown.mc.thePlayer.inventory.getCurrentItem());
        final C08PacketPlayerBlockPlacement blockMent = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, NoSlowdown.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f);
        if (onGround && !NoSlowdown.mc.thePlayer.onGround) {
            return;
        }
        if (sendC07 && event.isPre()) {
            if (delay && this.timer.hasTimeElapsed(delayValue, true)) {
                NoSlowdown.mc.getNetHandler().addToSendQueue(digging);
            }
            else if (!delay) {
                NoSlowdown.mc.getNetHandler().addToSendQueue(digging);
            }
        }
        if (sendC08 && !event.isPre()) {
            if (delay && this.timer.hasTimeElapsed(delayValue, true) && !watchDog) {
                NoSlowdown.mc.getNetHandler().addToSendQueue(blockPlace);
            }
            else if (!delay && !watchDog) {
                NoSlowdown.mc.getNetHandler().addToSendQueue(blockPlace);
            }
            else if (watchDog) {
                NoSlowdown.mc.getNetHandler().addToSendQueue(blockMent);
            }
        }
    }
    
    private boolean isBlocking() {
        return isHoldingSword() && NoSlowdown.mc.thePlayer.isBlocking();
    }
    
    public static boolean isHoldingSword() {
        return NoSlowdown.mc.thePlayer.getCurrentEquippedItem() != null && NoSlowdown.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }
    
    static {
        NoSlowdown.enabled = false;
        PLAYER_DIGGING = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
        BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
    }
}
