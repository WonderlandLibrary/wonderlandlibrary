// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.world.GameType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.util.BlockInteractionHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.util.Timer;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class AutoFeetBlock extends Module
{
    private final Value<Long> delay;
    private final Value<Boolean> rotate;
    private final Value<Boolean> noGlitchBlocks;
    Timer timer;
    int lastHotbarSlot;
    int playerHotbarSlot;
    boolean isSneaking;
    
    public AutoFeetBlock() {
        super("AutoFeetBlock", "Automatically phases yourself into a block", 0, Category.COMBAT, true);
        this.delay = this.register(new Value<Long>("MS Delay", this, 160L, 0L, 1000L));
        this.rotate = this.register(new Value<Boolean>("Rotate", this, true));
        this.noGlitchBlocks = this.register(new Value<Boolean>("NoGlitchBlocks", this, true));
        this.timer = new Timer();
    }
    
    @Override
    public void onEnable() {
        if (AutoFeetBlock.mc.player == null) {
            this.disable();
            return;
        }
        this.playerHotbarSlot = AutoFeetBlock.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
        AutoFeetBlock.mc.player.jump();
        this.timer.reset();
    }
    
    @Override
    public void onDisable() {
        if (AutoFeetBlock.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoFeetBlock.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoFeetBlock.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoFeetBlock.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        if (this.timer.hasReached(this.delay.getValue())) {
            final BlockPos offsetPos = new BlockPos(0, -1, 0);
            final BlockPos targetPos = new BlockPos(AutoFeetBlock.mc.player.getPositionVector()).add(offsetPos.x, offsetPos.y, offsetPos.z);
            if (this.placeBlock(targetPos)) {
                if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                    AutoFeetBlock.mc.player.inventory.currentItem = this.playerHotbarSlot;
                    this.lastHotbarSlot = this.playerHotbarSlot;
                }
                if (this.isSneaking) {
                    AutoFeetBlock.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoFeetBlock.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    this.isSneaking = false;
                }
                AutoFeetBlock.mc.player.onGround = false;
                AutoFeetBlock.mc.player.motionY = 20.0;
            }
            this.disable();
        }
    }
    
    private boolean placeBlock(final BlockPos pos) {
        final Block block = AutoFeetBlock.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : AutoFeetBlock.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoFeetBlock.mc.world.getBlockState(neighbour).getBlock();
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            AutoFeetBlock.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            AutoFeetBlock.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoFeetBlock.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        AutoFeetBlock.mc.playerController.processRightClickBlock(AutoFeetBlock.mc.player, AutoFeetBlock.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoFeetBlock.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoFeetBlock.mc.rightClickDelayTimer = 4;
        if (this.noGlitchBlocks.getValue() && !AutoFeetBlock.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE)) {
            AutoFeetBlock.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        }
        return true;
    }
    
    public int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoFeetBlock.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockObsidian) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
}
