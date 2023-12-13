// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.entity.Entity;
import vip.Resolute.util.movement.MovementUtils;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.item.ItemFireball;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.impl.EventMove;
import vip.Resolute.events.Event;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class HighJump extends Module
{
    public ModeSetting mode;
    public NumberSetting multi;
    int slot;
    int ticks;
    private int matrixStatus;
    private boolean matrixWasTimer;
    private TimerUtil timer;
    private boolean damaged;
    
    public HighJump() {
        super("HighJump", 0, "Makes the player jump high", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "Redesky", new String[] { "Redesky", "Matrix", "Watchdog" });
        this.multi = new NumberSetting("Multiplier", 1.0, () -> this.mode.is("Redesky"), 0.8, 1.5, 0.1);
        this.slot = 0;
        this.matrixStatus = 0;
        this.matrixWasTimer = false;
        this.timer = new TimerUtil();
        this.damaged = false;
        this.addSettings(this.mode, this.multi);
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        if (this.mode.is("Redesky") && HighJump.mc.thePlayer.onGround) {
            HighJump.mc.thePlayer.motionY = this.multi.getValue();
            final EntityPlayerSP thePlayer = HighJump.mc.thePlayer;
            thePlayer.motionX *= 1.5;
            final EntityPlayerSP thePlayer2 = HighJump.mc.thePlayer;
            thePlayer2.motionZ *= 1.5;
        }
        this.damaged = false;
        this.matrixStatus = 0;
        this.matrixWasTimer = false;
        this.timer.reset();
    }
    
    @Override
    public void onDisable() {
        HighJump.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (!(e instanceof EventMove) || !this.mode.is("Watchdog") || !this.damaged) {}
        if (e instanceof EventMotion) {
            if (this.mode.is("Watchdog")) {
                final int oldPitch = (int)HighJump.mc.thePlayer.rotationPitch;
                if (HighJump.mc.thePlayer.hurtTime > 0) {
                    this.damaged = true;
                }
                if (HighJump.mc.thePlayer.onGround && !this.damaged) {
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack itemStack = HighJump.mc.thePlayer.inventory.getStackInSlot(i);
                        if (itemStack != null && HighJump.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemFireball) {
                            HighJump.mc.getNetHandler().addToSendQueueSilent(new C09PacketHeldItemChange(i));
                            HighJump.mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C05PacketPlayerLook(HighJump.mc.thePlayer.rotationYaw, 90.0f, HighJump.mc.thePlayer.onGround));
                            HighJump.mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(HighJump.mc.thePlayer.inventory.getCurrentItem()));
                            HighJump.mc.getNetHandler().addToSendQueueSilent(new C09PacketHeldItemChange(HighJump.mc.thePlayer.inventory.currentItem));
                            HighJump.mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ, HighJump.mc.thePlayer.rotationYaw, (float)oldPitch, HighJump.mc.thePlayer.onGround));
                        }
                    }
                }
                if (this.damaged && HighJump.mc.thePlayer.onGround) {
                    HighJump.mc.thePlayer.motionY = 2.5;
                    MovementUtils.setMotion(MovementUtils.getBaseSpeedHypixelApplied() * 6.65);
                    this.toggle();
                }
                else {
                    this.damaged = false;
                }
            }
            if (this.mode.is("Matrix")) {
                if (this.matrixWasTimer) {
                    HighJump.mc.timer.timerSpeed = 1.0f;
                    this.matrixWasTimer = false;
                }
                if ((HighJump.mc.theWorld.getCollidingBoundingBoxes(HighJump.mc.thePlayer, HighJump.mc.thePlayer.getEntityBoundingBox().offset(0.0, HighJump.mc.thePlayer.motionY, 0.0).expand(0.0, 0.0, 0.0)).isEmpty() || HighJump.mc.theWorld.getCollidingBoundingBoxes(HighJump.mc.thePlayer, HighJump.mc.thePlayer.getEntityBoundingBox().offset(0.0, -4.0, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) && HighJump.mc.thePlayer.fallDistance > 10.0f && !HighJump.mc.thePlayer.onGround) {
                    HighJump.mc.timer.timerSpeed = 0.1f;
                    this.matrixWasTimer = true;
                }
                if (this.timer.hasElapsed(1000L) && this.matrixStatus == 1) {
                    HighJump.mc.timer.timerSpeed = 1.0f;
                    HighJump.mc.thePlayer.motionX = 0.0;
                    HighJump.mc.thePlayer.motionZ = 0.0;
                    this.matrixStatus = 0;
                    return;
                }
                if (this.matrixStatus == 1 && HighJump.mc.thePlayer.hurtTime > 0) {
                    HighJump.mc.timer.timerSpeed = 1.0f;
                    HighJump.mc.thePlayer.motionY = 3.0;
                    HighJump.mc.thePlayer.motionX = 0.0;
                    HighJump.mc.thePlayer.motionZ = 0.0;
                    HighJump.mc.thePlayer.jumpMovementFactor = 0.0f;
                    this.matrixStatus = 0;
                    return;
                }
                if (this.matrixStatus == 2) {
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ, false));
                    for (int j = 0; j <= 8; ++j) {
                        HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY + 0.399, HighJump.mc.thePlayer.posZ, false));
                        HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ, false));
                    }
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ, true));
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ, true));
                    HighJump.mc.timer.timerSpeed = 0.6f;
                    this.matrixStatus = 1;
                    this.timer.reset();
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY - 1.0, HighJump.mc.thePlayer.posZ), EnumFacing.UP));
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    return;
                }
                if (HighJump.mc.thePlayer.isCollidedHorizontally && this.matrixStatus == 0 && HighJump.mc.thePlayer.onGround) {
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY - 1.0, HighJump.mc.thePlayer.posZ), EnumFacing.UP));
                    HighJump.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    this.matrixStatus = 2;
                    HighJump.mc.timer.timerSpeed = 0.05f;
                }
                if (HighJump.mc.thePlayer.isCollidedHorizontally && HighJump.mc.thePlayer.onGround) {
                    HighJump.mc.thePlayer.motionX = 0.0;
                    HighJump.mc.thePlayer.motionZ = 0.0;
                    HighJump.mc.thePlayer.onGround = false;
                }
            }
            if (e.isPre() && this.mode.is("Redesky") && HighJump.mc.thePlayer.onGround) {
                this.toggle();
            }
        }
    }
}
