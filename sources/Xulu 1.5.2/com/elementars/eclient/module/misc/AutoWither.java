// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockAir;
import java.util.Iterator;
import java.util.List;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.item.ItemBlock;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.elementars.eclient.util.BlockInteractionHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.util.EnumUtil;
import com.elementars.eclient.module.Category;
import net.minecraft.util.math.BlockPos;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class AutoWither extends Module
{
    private static boolean isSneaking;
    private final Value<String> useMode;
    private final Value<Float> placeRange;
    private final Value<Integer> delay;
    private final Value<Boolean> rotate;
    private final Value<Boolean> debug;
    private BlockPos placeTarget;
    private boolean rotationPlaceableX;
    private boolean rotationPlaceableZ;
    private int bodySlot;
    private int headSlot;
    private int buildStage;
    private int delayStep;
    
    public AutoWither() {
        super("AutoWither", "Automatically places withers", 0, Category.MISC, true);
        this.useMode = this.register(new Value<String>("UseMode", this, "Spam", EnumUtil.enumConverter(UseMode.class)));
        this.placeRange = this.register(new Value<Float>("PlaceRange", this, 3.5f, 2.0f, 10.0f));
        this.delay = this.register(new Value<Integer>("Delay", this, 20, 12, 100));
        this.rotate = this.register(new Value<Boolean>("Rotate", this, true));
        this.debug = this.register(new Value<Boolean>("Debug", this, false));
    }
    
    private static void placeBlock(final BlockPos pos, final boolean rotate) {
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoWither.mc.world.getBlockState(neighbour).getBlock();
        if (!AutoWither.isSneaking && (BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock))) {
            AutoWither.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoWither.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            AutoWither.isSneaking = true;
        }
        if (rotate) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        AutoWither.mc.playerController.processRightClickBlock(AutoWither.mc.player, AutoWither.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoWither.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoWither.mc.rightClickDelayTimer = 4;
    }
    
    private static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            final IBlockState blockState;
            if (AutoWither.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(AutoWither.mc.world.getBlockState(neighbour), false) && !(blockState = AutoWither.mc.world.getBlockState(neighbour)).getMaterial().isReplaceable() && !(blockState.getBlock() instanceof BlockTallGrass) && !(blockState.getBlock() instanceof BlockDeadBush)) {
                return side;
            }
        }
        return null;
    }
    
    @Override
    public void onEnable() {
        if (AutoWither.mc.player == null) {
            this.disable();
            return;
        }
        this.buildStage = 1;
        this.delayStep = 1;
    }
    
    private boolean checkBlocksInHotbar() {
        this.headSlot = -1;
        this.bodySlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoWither.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() == Items.SKULL && stack.getItemDamage() == 1) {
                    if (AutoWither.mc.player.inventory.getStackInSlot(i).stackSize >= 3) {
                        this.headSlot = i;
                    }
                }
                else if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockSoulSand && AutoWither.mc.player.inventory.getStackInSlot(i).stackSize >= 4) {
                        this.bodySlot = i;
                    }
                    if (block == Blocks.SNOW) {
                        if (AutoWither.mc.player.inventory.getStackInSlot(i).stackSize >= 2) {
                            this.bodySlot = i;
                        }
                    }
                }
            }
        }
        return this.bodySlot != -1 && this.headSlot != -1;
    }
    
    private boolean testStructure() {
        return this.testWitherStructure();
    }
    
    private boolean testWitherStructure() {
        boolean noRotationPlaceable = true;
        this.rotationPlaceableX = true;
        this.rotationPlaceableZ = true;
        boolean isShitGrass = false;
        if (AutoWither.mc.world.getBlockState(this.placeTarget) == null) {
            return false;
        }
        final Block block = AutoWither.mc.world.getBlockState(this.placeTarget).getBlock();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (getPlaceableSide(this.placeTarget.up()) == null) {
            return false;
        }
        for (final BlockPos pos : BodyParts.bodyBase) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                noRotationPlaceable = false;
            }
        }
        for (final BlockPos pos : BodyParts.ArmsX) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) || this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) {
                this.rotationPlaceableX = false;
            }
        }
        for (final BlockPos pos : BodyParts.ArmsZ) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) || this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) {
                this.rotationPlaceableZ = false;
            }
        }
        for (final BlockPos pos : BodyParts.headsX) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                this.rotationPlaceableX = false;
            }
        }
        for (final BlockPos pos : BodyParts.headsZ) {
            if (this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) {
                this.rotationPlaceableZ = false;
            }
        }
        return !isShitGrass && noRotationPlaceable && (this.rotationPlaceableX || this.rotationPlaceableZ);
    }
    
    @Override
    public void onUpdate() {
        if (AutoWither.mc.player == null) {
            return;
        }
        if (this.buildStage == 1) {
            AutoWither.isSneaking = false;
            this.rotationPlaceableX = false;
            this.rotationPlaceableZ = false;
            if (!this.checkBlocksInHotbar()) {
                this.sendDebugMessage("no blocks in hotbar");
                return;
            }
            final List<BlockPos> blockPosList = BlockInteractionHelper.getSphere(AutoWither.mc.player.getPosition().down(), this.placeRange.getValue(), this.placeRange.getValue().intValue(), false, true, 0);
            boolean noPositionInArea = true;
            for (final BlockPos pos : blockPosList) {
                this.placeTarget = pos.down();
                if (!this.testStructure()) {
                    continue;
                }
                noPositionInArea = false;
                break;
            }
            if (noPositionInArea) {
                if (this.useMode.getValue().equalsIgnoreCase("Single")) {
                    if (this.debug.getValue()) {
                        this.sendDebugMessage(ChatFormatting.RED.toString() + "Position not valid, disabling.");
                    }
                    this.disable();
                }
                return;
            }
            AutoWither.mc.player.inventory.currentItem = this.bodySlot;
            for (final BlockPos pos2 : BodyParts.bodyBase) {
                placeBlock(this.placeTarget.add((Vec3i)pos2), this.rotate.getValue());
            }
            if (this.rotationPlaceableX) {
                for (final BlockPos pos2 : BodyParts.ArmsX) {
                    placeBlock(this.placeTarget.add((Vec3i)pos2), this.rotate.getValue());
                }
            }
            else if (this.rotationPlaceableZ) {
                for (final BlockPos pos2 : BodyParts.ArmsZ) {
                    placeBlock(this.placeTarget.add((Vec3i)pos2), this.rotate.getValue());
                }
            }
            this.buildStage = 2;
        }
        else if (this.buildStage == 2) {
            AutoWither.mc.player.inventory.currentItem = this.headSlot;
            if (this.rotationPlaceableX) {
                for (final BlockPos pos : BodyParts.headsX) {
                    placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.getValue());
                }
            }
            else if (this.rotationPlaceableZ) {
                for (final BlockPos pos : BodyParts.headsZ) {
                    placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.getValue());
                }
            }
            if (AutoWither.isSneaking) {
                AutoWither.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoWither.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                AutoWither.isSneaking = false;
            }
            if (this.useMode.getValue().equalsIgnoreCase("Single")) {
                this.disable();
            }
            this.buildStage = 3;
        }
        else if (this.buildStage == 3) {
            if (this.delayStep < this.delay.getValue()) {
                ++this.delayStep;
            }
            else {
                this.delayStep = 1;
                this.buildStage = 1;
            }
        }
    }
    
    private boolean placingIsBlocked(final BlockPos pos) {
        final Block block = AutoWither.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir)) {
            return true;
        }
        for (final Entity entity : AutoWither.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem)) {
                if (entity instanceof EntityXPOrb) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    private static class BodyParts
    {
        private static final BlockPos[] bodyBase;
        private static final BlockPos[] ArmsX;
        private static final BlockPos[] ArmsZ;
        private static final BlockPos[] headsX;
        private static final BlockPos[] headsZ;
        private static final BlockPos[] head;
        
        static {
            bodyBase = new BlockPos[] { new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
            ArmsX = new BlockPos[] { new BlockPos(-1, 2, 0), new BlockPos(1, 2, 0) };
            ArmsZ = new BlockPos[] { new BlockPos(0, 2, -1), new BlockPos(0, 2, 1) };
            headsX = new BlockPos[] { new BlockPos(0, 3, 0), new BlockPos(-1, 3, 0), new BlockPos(1, 3, 0) };
            headsZ = new BlockPos[] { new BlockPos(0, 3, 0), new BlockPos(0, 3, -1), new BlockPos(0, 3, 1) };
            head = new BlockPos[] { new BlockPos(0, 3, 0) };
        }
    }
    
    private enum UseMode
    {
        SINGLE, 
        SPAM;
    }
}
