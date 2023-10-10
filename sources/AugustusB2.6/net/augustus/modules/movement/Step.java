// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.EdgeCosts;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;
import net.augustus.events.EventPostStep;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventPreStep;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class Step extends Module
{
    public final DoubleValue height;
    public final DoubleValue delay;
    public final DoubleValue timer;
    private final TimeHelper timeHelper;
    public StringValue mode;
    private boolean shouldStep;
    private boolean shouldTimerReset;
    
    public Step() {
        super("Step", new Color(129, 147, 164), Categorys.MOVEMENT);
        this.height = new DoubleValue(2, "Height", this, 1.0, 1.0, 5.0, 2);
        this.delay = new DoubleValue(3, "Delay", this, 400.0, 0.0, 1000.0, 0);
        this.timer = new DoubleValue(4, "Timer", this, 0.3, 0.05, 1.0, 2);
        this.timeHelper = new TimeHelper();
        this.mode = new StringValue(1, "Modes", this, "Vanilla", new String[] { "Vanilla", "NCPPacket" });
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Step.mc.thePlayer.stepHeight = 0.6f;
    }
    
    @EventTarget
    public void onEventPreStep(final EventPreStep eventPreStep) {
        if (this.shouldTimerReset) {
            Step.mc.getTimer().timerSpeed = 1.0f;
            this.shouldTimerReset = false;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vanilla":
            case "NCPPacket": {
                eventPreStep.setStepHeight((float)this.height.getValue());
                if (!this.timeHelper.reached((long)this.delay.getValue())) {
                    eventPreStep.setStepHeight(0.6f);
                    this.shouldStep = false;
                    break;
                }
                this.shouldStep = true;
                this.timeHelper.reset();
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventPostStep(final EventPostStep eventPostStep) {
        final Vec3 stepVec = new Vec3(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY, Step.mc.thePlayer.posZ);
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vanilla": {
                if (Step.mc.thePlayer.getEntityBoundingBox().minY - stepVec.yCoord > 0.6 && this.shouldStep && this.timer.getValue() != 1.0) {
                    Step.mc.getTimer().timerSpeed = (float)this.timer.getValue();
                    this.shouldTimerReset = true;
                    break;
                }
                break;
            }
            case "NCPPacket": {
                if (Step.mc.thePlayer.getEntityBoundingBox().minY - stepVec.yCoord <= 0.6 || !this.shouldStep) {
                    break;
                }
                final double height = Step.mc.thePlayer.getEntityBoundingBox().minY - stepVec.yCoord;
                int counter = 2;
                if (height == 1.0) {
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.42, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.75, stepVec.zCoord, false));
                    counter = 3;
                }
                else if (height < 0.65) {
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.35, stepVec.zCoord, false));
                }
                else if (height < 0.76) {
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.42, stepVec.zCoord, false));
                }
                else if (height < 0.878) {
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.42, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.73, stepVec.zCoord, false));
                    counter = 3;
                }
                else if (height < 1.26) {
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.425, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.779, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.06, stepVec.zCoord, false));
                    counter = 4;
                }
                else if (height < 1.6) {
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.425, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.779, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.0538, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.265, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.3, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.25, stepVec.zCoord, false));
                    counter = 7;
                }
                else if (height < 2.1) {
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.425, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.82, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.7, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 0.595, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.01, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.37, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.657, stepVec.zCoord, false));
                    Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(stepVec.xCoord, stepVec.yCoord + 1.87, stepVec.zCoord, false));
                    counter = 10;
                }
                if (this.timer.getValue() != 1.0) {
                    Step.mc.getTimer().timerSpeed = (float)Math.max((height == 1.0) ? this.timer.getValue() : (this.timer.getValue() * 3.43333 / counter), 0.1);
                    this.shouldTimerReset = true;
                    break;
                }
                break;
            }
        }
    }
    
    private boolean shouldStep() {
        if (Step.mc.thePlayer.isCollidedHorizontally) {
            BlockPos playerBlock = null;
            for (int i = 0; i < 4; ++i) {
                if (EdgeCosts.hasBlockCollision(new BlockPos(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY - i, Step.mc.thePlayer.posZ))) {
                    playerBlock = new BlockPos(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY - i + 1.0, Step.mc.thePlayer.posZ);
                    break;
                }
            }
            final float direction = (float)Math.toDegrees(MoveUtil.direction());
            final EnumFacing enumFacing = Step.mc.thePlayer.getHorizontalFacing(direction);
            BlockPos startBlock = playerBlock.offset(enumFacing);
            double height = 0.0;
            for (int j = 0; j <= this.height.getValue() + 3.0; ++j) {
                if (EdgeCosts.hasBlockCollision(startBlock)) {
                    final Block block = Step.mc.theWorld.getBlockState(startBlock).getBlock();
                    height += block.getBlockBoundsMaxY();
                    if (height > this.height.getValue()) {
                        return false;
                    }
                    if (j > 0 && !EdgeCosts.hasBlockCollision(startBlock.down())) {
                        return false;
                    }
                    startBlock = startBlock.up();
                }
                else {
                    if (!EdgeCosts.hasBlockCollision(startBlock.down()) && !EdgeCosts.hasBlockCollision(startBlock)) {
                        return Step.mc.thePlayer.posY < playerBlock.offset(enumFacing).getY() + this.height.getValue();
                    }
                    ++height;
                    startBlock = startBlock.up();
                }
            }
            return true;
        }
        return false;
    }
}
