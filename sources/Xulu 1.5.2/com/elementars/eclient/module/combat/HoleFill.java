// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.world.GameType;
import com.elementars.eclient.Xulu;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import com.elementars.eclient.util.BlockInteractionHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import java.util.function.Consumer;
import com.elementars.eclient.enemy.Enemies;
import com.elementars.eclient.friend.Friends;
import java.util.function.Function;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.Objects;
import com.elementars.eclient.util.TargetPlayers;
import com.elementars.eclient.util.BoolSwitch;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.Material;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import com.elementars.eclient.module.Category;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class HoleFill extends Module
{
    private final Value<Integer> range;
    private final Value<Integer> yRange;
    private final Value<Boolean> rotate;
    private final Value<Boolean> triggerable;
    private final Value<Integer> waitTick;
    private final Value<Boolean> useEC;
    public final Value<Boolean> noGlitchBlocks;
    private final Value<Boolean> pre;
    private final Value<Boolean> chat;
    private ArrayList<BlockPos> holes;
    private List<Block> whiteList;
    BlockPos pos;
    private int waitCounter;
    int delay;
    
    public HoleFill() {
        super("HoleFill", "Fills holes", 0, Category.COMBAT, true);
        this.range = this.register(new Value<Integer>("Range", this, 5, 1, 10));
        this.yRange = this.register(new Value<Integer>("YRange", this, 2, 1, 10));
        this.rotate = this.register(new Value<Boolean>("Rotate", this, true));
        this.triggerable = this.register(new Value<Boolean>("Triggerable", this, true));
        this.waitTick = this.register(new Value<Integer>("TickDelay", this, 1, 0, 10));
        this.useEC = this.register(new Value<Boolean>("UseEnderchests", this, false));
        this.noGlitchBlocks = this.register(new Value<Boolean>("NoGlitchBlocks", this, true));
        this.pre = this.register(new Value<Boolean>("Prioritize Enemies", this, false));
        this.chat = this.register(new Value<Boolean>("Chat", this, false));
        this.holes = new ArrayList<BlockPos>();
        this.whiteList = Arrays.asList(Blocks.OBSIDIAN);
    }
    
    @Override
    public void onUpdate() {
        if (this.triggerable.getValue()) {
            if (this.delay > 0) {
                --this.delay;
            }
            else {
                this.toggle();
            }
        }
        this.holes = new ArrayList<BlockPos>();
        if (this.useEC.getValue()) {
            if (!this.whiteList.contains(Blocks.ENDER_CHEST)) {
                this.whiteList.add(Blocks.ENDER_CHEST);
            }
        }
        else {
            this.whiteList.remove(Blocks.ENDER_CHEST);
        }
        final Iterable<BlockPos> blocks = (Iterable<BlockPos>)BlockPos.getAllInBox(HoleFill.mc.player.getPosition().add(-this.range.getValue(), -this.yRange.getValue(), -this.range.getValue()), HoleFill.mc.player.getPosition().add((double)this.range.getValue(), (double)this.yRange.getValue(), (double)this.range.getValue()));
        for (final BlockPos pos : blocks) {
            if (!HoleFill.mc.world.getBlockState(pos).getMaterial().blocksMovement() && !HoleFill.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) {
                final boolean solidNeighbours = (HoleFill.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (HoleFill.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) && (HoleFill.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (HoleFill.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) && HoleFill.mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && HoleFill.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && HoleFill.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
                if (!solidNeighbours) {
                    continue;
                }
                this.holes.add(pos);
            }
        }
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = HoleFill.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (this.whiteList.contains(block)) {
                    newSlot = i;
                    break;
                }
            }
        }
        if (newSlot == -1) {
            return;
        }
        final int oldSlot = HoleFill.mc.player.inventory.currentItem;
        final BoolSwitch test = new BoolSwitch(false);
        TargetPlayers.targettedplayers.keySet().stream().map(s -> HoleFill.mc.world.getPlayerEntityByName(s)).filter(Objects::nonNull).filter(player -> HoleFill.mc.player.getDistance(player) <= this.range.getValue()).min(Comparator.comparing(player -> HoleFill.mc.player.getDistance(player))).ifPresent(closest -> {
            test.setValue(true);
            this.holes.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)closest::getDistanceSq));
            return;
        });
        if (!test.isValue()) {
            HoleFill.mc.world.playerEntities.stream().filter(player -> !Friends.isFriend(player.getName())).filter(player -> HoleFill.mc.player.getDistance(player) <= this.range.getValue()).min(Comparator.comparing(player -> HoleFill.mc.player.getDistance(player))).ifPresent(closest -> this.holes.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)closest::getDistanceSq)));
            if (this.pre.getValue()) {
                HoleFill.mc.world.playerEntities.stream().filter(player -> !Friends.isFriend(player.getName()) && Enemies.isEnemy(player.getName())).filter(player -> HoleFill.mc.player.getDistance(player) <= this.range.getValue()).min(Comparator.comparing(player -> HoleFill.mc.player.getDistance(player))).ifPresent(closest -> this.holes.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)closest::getDistanceSq)));
            }
        }
        if (this.waitTick.getValue() > 0.0) {
            if (this.waitCounter < this.waitTick.getValue()) {
                HoleFill.mc.player.inventory.currentItem = newSlot;
                this.holes.forEach(this::place);
                HoleFill.mc.player.inventory.currentItem = oldSlot;
                return;
            }
            this.waitCounter = 0;
        }
    }
    
    @Override
    public void onEnable() {
        this.delay = 20;
        if (HoleFill.mc.player != null && this.chat.getValue()) {
            this.sendDebugMessage(ChatFormatting.GREEN + "Enabled!");
        }
    }
    
    @Override
    public void onDisable() {
        if (HoleFill.mc.player != null && this.chat.getValue()) {
            this.sendDebugMessage(ChatFormatting.RED + "Disabled!");
        }
    }
    
    private void place(final BlockPos blockPos) {
        for (final Entity entity : HoleFill.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos))) {
            if (entity instanceof EntityLivingBase) {
                return;
            }
        }
        placeBlockScaffold(blockPos, this.rotate.getValue());
        ++this.waitCounter;
    }
    
    public static boolean placeBlockScaffold(final BlockPos pos, final boolean rotate) {
        final Vec3d eyesPos = new Vec3d(HoleFill.mc.player.posX, HoleFill.mc.player.posY + HoleFill.mc.player.getEyeHeight(), HoleFill.mc.player.posZ);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (BlockInteractionHelper.canBeClicked(neighbor)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (rotate) {
                    BlockInteractionHelper.faceVectorPacketInstant(hitVec);
                }
                HoleFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)HoleFill.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                processRightClickBlock(neighbor, side2, hitVec);
                HoleFill.mc.player.swingArm(EnumHand.MAIN_HAND);
                HoleFill.mc.rightClickDelayTimer = 0;
                HoleFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)HoleFill.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                if (Xulu.MODULE_MANAGER.getModuleT(HoleFill.class).noGlitchBlocks.getValue() && !Surround.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE)) {
                    Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbor, side2));
                }
                return true;
            }
        }
        return false;
    }
    
    private static PlayerControllerMP getPlayerController() {
        return HoleFill.mc.playerController;
    }
    
    public static void processRightClickBlock(final BlockPos pos, final EnumFacing side, final Vec3d hitVec) {
        getPlayerController().processRightClickBlock(HoleFill.mc.player, HoleFill.mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
    }
}
