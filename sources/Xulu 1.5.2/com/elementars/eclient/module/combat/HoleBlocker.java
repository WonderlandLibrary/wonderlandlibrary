// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import net.minecraft.init.Blocks;
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
import com.elementars.eclient.util.BlockInteractionHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.module.ModuleManager;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class HoleBlocker extends Module
{
    private ArrayList<String> options;
    private final Value<Boolean> triggerable;
    private final Value<Integer> timeoutTicks;
    private final Value<Integer> blocksPerTick;
    private final Value<Integer> tickDelay;
    private final Value<Boolean> rotate;
    private final Value<Boolean> noGlitchBlocks;
    private final Value<Boolean> announceUsage;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int offsetStep;
    private int delayStep;
    private int totalTicksRunning;
    private boolean firstRun;
    private boolean isSneaking;
    double oldY;
    
    public HoleBlocker() {
        super("HoleBlocker", "Blocks others from entering your hole", 0, Category.COMBAT, true);
        this.triggerable = this.register(new Value<Boolean>("Triggerable", this, true));
        this.timeoutTicks = this.register(new Value<Integer>("TimeoutTicks", this, 40, 1, 100));
        this.blocksPerTick = this.register(new Value<Integer>("BlocksPerTick", this, 4, 1, 9));
        this.tickDelay = this.register(new Value<Integer>("TickDelay", this, 0, 0, 10));
        this.rotate = this.register(new Value<Boolean>("Rotate", this, true));
        this.noGlitchBlocks = this.register(new Value<Boolean>("NoGlitchBlocks", this, true));
        this.announceUsage = this.register(new Value<Boolean>("AnnounceUsage", this, true));
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.offsetStep = 0;
        this.delayStep = 0;
        this.totalTicksRunning = 0;
        this.isSneaking = false;
    }
    
    @Override
    public void onEnable() {
        if (HoleBlocker.mc.player == null) {
            this.disable();
            return;
        }
        this.oldY = HoleBlocker.mc.player.posY;
        this.firstRun = true;
        this.playerHotbarSlot = HoleBlocker.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            this.sendDebugMessage(ChatFormatting.GREEN.toString() + "Enabled!");
        }
    }
    
    @Override
    public void onDisable() {
        if (HoleBlocker.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            HoleBlocker.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            HoleBlocker.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)HoleBlocker.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            this.sendDebugMessage(ChatFormatting.RED.toString() + "Disabled!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (HoleBlocker.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (this.triggerable.getValue() && this.totalTicksRunning >= this.timeoutTicks.getValue()) {
            this.totalTicksRunning = 0;
            this.disable();
            return;
        }
        if (HoleBlocker.mc.player.posY != this.oldY) {
            this.disable();
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        if (this.firstRun) {
            this.firstRun = false;
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            Vec3d[] offsetPattern = new Vec3d[0];
            int maxSteps = 0;
            final String facing = HoleBlocker.mc.player.getHorizontalFacing().getName().toUpperCase();
            if (facing.equalsIgnoreCase("NORTH")) {
                offsetPattern = Offsets.NORTH;
                maxSteps = Offsets.NORTH.length;
            }
            else if (facing.equalsIgnoreCase("EAST")) {
                offsetPattern = Offsets.EAST;
                maxSteps = Offsets.EAST.length;
            }
            else if (facing.equalsIgnoreCase("SOUTH")) {
                offsetPattern = Offsets.SOUTH;
                maxSteps = Offsets.SOUTH.length;
            }
            else if (facing.equalsIgnoreCase("WEST")) {
                offsetPattern = Offsets.WEST;
                maxSteps = Offsets.WEST.length;
            }
            else {
                offsetPattern = Offsets.EAST;
                maxSteps = Offsets.EAST.length;
            }
            if (this.offsetStep >= maxSteps) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetStep]);
            final BlockPos targetPos = new BlockPos(HoleBlocker.mc.player.getPositionVector()).add(offsetPos.x, offsetPos.y, offsetPos.z);
            if (this.placeBlock(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                HoleBlocker.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                HoleBlocker.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)HoleBlocker.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
        ++this.totalTicksRunning;
    }
    
    private boolean placeBlock(final BlockPos pos) {
        final Block block = HoleBlocker.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : HoleBlocker.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
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
        final Block neighbourBlock = HoleBlocker.mc.world.getBlockState(neighbour).getBlock();
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            HoleBlocker.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            HoleBlocker.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)HoleBlocker.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        HoleBlocker.mc.playerController.processRightClickBlock(HoleBlocker.mc.player, HoleBlocker.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        HoleBlocker.mc.player.swingArm(EnumHand.MAIN_HAND);
        HoleBlocker.mc.rightClickDelayTimer = 4;
        if (this.noGlitchBlocks.getValue() && !HoleBlocker.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE)) {
            HoleBlocker.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        }
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = HoleBlocker.mc.player.inventory.getStackInSlot(i);
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
    
    public static boolean isExposed() {
        if (HoleBlocker.mc.world == null) {
            return false;
        }
        final BlockPos playerPos = new BlockPos(HoleBlocker.mc.player.getPositionVector());
        switch (HoleBlocker.mc.player.getHorizontalFacing()) {
            case NORTH: {
                for (final Vec3d vec3d : Offsets.NORTH) {
                    if (HoleBlocker.mc.world.getBlockState(playerPos.add(vec3d.x, vec3d.y, vec3d.z)).getBlock() == Blocks.AIR) {
                        return true;
                    }
                }
                break;
            }
            case SOUTH: {
                for (final Vec3d vec3d : Offsets.SOUTH) {
                    if (HoleBlocker.mc.world.getBlockState(playerPos.add(vec3d.x, vec3d.y, vec3d.z)).getBlock() == Blocks.AIR) {
                        return true;
                    }
                }
                break;
            }
            case EAST: {
                for (final Vec3d vec3d : Offsets.EAST) {
                    if (HoleBlocker.mc.world.getBlockState(playerPos.add(vec3d.x, vec3d.y, vec3d.z)).getBlock() == Blocks.AIR) {
                        return true;
                    }
                }
                break;
            }
            case WEST: {
                for (final Vec3d vec3d : Offsets.WEST) {
                    if (HoleBlocker.mc.world.getBlockState(playerPos.add(vec3d.x, vec3d.y, vec3d.z)).getBlock() == Blocks.AIR) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] NORTH;
        private static final Vec3d[] EAST;
        private static final Vec3d[] SOUTH;
        private static final Vec3d[] WEST;
        
        static {
            NORTH = new Vec3d[] { new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, 0.0) };
            EAST = new Vec3d[] { new Vec3d(-1.0, 1.0, 0.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 0.0) };
            SOUTH = new Vec3d[] { new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(0.0, 2.0, 0.0) };
            WEST = new Vec3d[] { new Vec3d(1.0, 1.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 0.0) };
        }
    }
}
