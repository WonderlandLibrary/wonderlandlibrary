// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import com.elementars.eclient.event.events.EventSendPacket;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemBlock;
import com.elementars.eclient.util.BlockInteractionHelper;
import net.minecraft.init.Blocks;
import com.elementars.eclient.event.Event;
import com.elementars.eclient.event.events.PlayerMoveEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.module.Module;

public class Scaffold extends Module
{
    private BlockPos block;
    private EnumFacing side;
    private boolean rotated;
    private boolean isSpoofingAngles;
    private double yaw;
    private double pitch;
    private final Value<Integer> delay;
    int delayT;
    
    public Scaffold() {
        super("Scaffold", "Automatically places blocks below you", 0, Category.MOVEMENT, true);
        this.delay = this.register(new Value<Integer>("Delay", this, 0, 0, 20));
    }
    
    @Override
    public void onUpdate() {
        if (this.delayT > 0) {
            --this.delayT;
        }
    }
    
    @EventTarget
    public void onMove(final PlayerMoveEvent event) {
        if (event.getEventState() == Event.State.PRE) {
            this.rotated = false;
            this.block = null;
            this.side = null;
            final BlockPos pos = new BlockPos(Scaffold.mc.player.getPositionVector()).add(0, -1, 0);
            if (Scaffold.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                this.setBlockAndFacing(pos);
                if (this.block != null) {
                    final float[] facing = BlockInteractionHelper.getDirectionToBlock(this.block.getX(), this.block.getY(), this.block.getZ(), this.side);
                    final float yaw = facing[0];
                    final float pitch = Math.min(90.0f, facing[1] + 9.0f);
                    this.rotated = true;
                    this.yaw = yaw;
                    this.pitch = pitch;
                    this.isSpoofingAngles = true;
                }
            }
        }
        if (event.getEventState() == Event.State.POST && this.block != null && this.delayT == 0 && Scaffold.mc.player.getHeldItemMainhand() != null && Scaffold.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && Scaffold.mc.playerController.processRightClickBlock(Scaffold.mc.player, Scaffold.mc.world, this.block, this.side, new Vec3d((double)this.block.getX(), (double)this.block.getY(), (double)this.block.getZ()), EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {
            this.delayT = this.delay.getValue();
            Scaffold.mc.player.swingArm(EnumHand.MAIN_HAND);
            Scaffold.mc.player.motionX = 0.0;
            Scaffold.mc.player.motionZ = 0.0;
        }
    }
    
    @EventTarget
    public void onSend(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayer && this.isSpoofingAngles) {
            ((CPacketPlayer)event.getPacket()).yaw = (float)this.yaw;
            ((CPacketPlayer)event.getPacket()).pitch = (float)this.pitch;
        }
    }
    
    private void setBlockAndFacing(final BlockPos pos) {
        if (Scaffold.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.AIR) {
            this.block = pos.add(0, -1, 0);
            this.side = EnumFacing.UP;
        }
        else if (Scaffold.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.AIR) {
            this.block = pos.add(-1, 0, 0);
            this.side = EnumFacing.EAST;
        }
        else if (Scaffold.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.AIR) {
            this.block = pos.add(1, 0, 0);
            this.side = EnumFacing.WEST;
        }
        else if (Scaffold.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.AIR) {
            this.block = pos.add(0, 0, -1);
            this.side = EnumFacing.SOUTH;
        }
        else if (Scaffold.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.AIR) {
            this.block = pos.add(0, 0, 1);
            this.side = EnumFacing.NORTH;
        }
        else {
            this.block = null;
            this.side = null;
        }
    }
}
