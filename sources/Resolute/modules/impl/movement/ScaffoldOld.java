// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import java.util.Arrays;
import net.minecraft.init.Blocks;
import vip.Resolute.util.player.RotationUtils;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockLadder;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.Vec3;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockContainer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumFacing;
import vip.Resolute.util.movement.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import vip.Resolute.events.impl.EventMotion;
import net.minecraft.util.BlockPos;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.impl.EventSafeWalk;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import net.minecraft.block.Block;
import java.util.List;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class ScaffoldOld extends Module
{
    public static ModeSetting mode;
    public static ModeSetting towerMode;
    public NumberSetting timerBoost;
    public BooleanSetting safeWalk;
    public BooleanSetting swing;
    public BooleanSetting silent;
    public BooleanSetting sprint;
    public static TimerUtils timer;
    public static int heldItem;
    public float pitch;
    public float yaw;
    BlockData blockData;
    private TimerUtils towerStopwatch;
    private int slot;
    private static List<Block> invalidBlocks;
    
    public ScaffoldOld() {
        super("ScaffoldOld", 49, "Automatically places blocks under you", Category.MOVEMENT);
        this.timerBoost = new NumberSetting("Timer Boost", 1.0, 1.0, 2.0, 0.1);
        this.safeWalk = new BooleanSetting("SafeWalk", true);
        this.swing = new BooleanSetting("Swing", true);
        this.silent = new BooleanSetting("Silent", true);
        this.sprint = new BooleanSetting("Sprint", true);
        this.towerStopwatch = new TimerUtils();
        this.addSettings(ScaffoldOld.mode, ScaffoldOld.towerMode, this.safeWalk, this.silent, this.swing);
        this.towerStopwatch = new TimerUtils();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ScaffoldOld.mc.thePlayer != null) {
            ScaffoldOld.heldItem = ScaffoldOld.mc.thePlayer.inventory.currentItem;
        }
        this.towerStopwatch.reset();
        this.yaw = ScaffoldOld.mc.thePlayer.rotationYaw;
        this.slot = ScaffoldOld.mc.thePlayer.inventory.currentItem;
        this.blockData = null;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        ScaffoldOld.mc.gameSettings.keyBindSneak.pressed = false;
        ScaffoldOld.mc.thePlayer.movementInput.sneak = false;
        ScaffoldOld.mc.thePlayer.inventory.currentItem = ScaffoldOld.heldItem;
        ScaffoldOld.mc.thePlayer.inventory.currentItem = this.slot;
        ScaffoldOld.mc.timer.timerSpeed = 1.0f;
        this.blockData = null;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(ScaffoldOld.mode.getMode());
        if (e instanceof EventSafeWalk && this.safeWalk.isEnabled()) {
            e.setCancelled(true);
        }
        if (e instanceof EventUpdate) {
            final BlockPos pos = new BlockPos(ScaffoldOld.mc.thePlayer.posX, ScaffoldOld.mc.thePlayer.getEntityBoundingBox().minY - 1.0, ScaffoldOld.mc.thePlayer.posZ);
            this.getYaw(pos);
        }
        if (e instanceof EventMotion) {
            if (e.isPre()) {
                ScaffoldOld.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ScaffoldOld.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            final EventMotion event = (EventMotion)e;
            ScaffoldOld.mc.thePlayer.setSprinting(false);
            double x = ScaffoldOld.mc.thePlayer.posX;
            double z = ScaffoldOld.mc.thePlayer.posZ;
            if (ScaffoldOld.mc.theWorld.getBlockState(new BlockPos(ScaffoldOld.mc.thePlayer.posX, ScaffoldOld.mc.thePlayer.posY - 1.0, ScaffoldOld.mc.thePlayer.posZ)).getBlock().getMaterial().isReplaceable()) {
                x = ScaffoldOld.mc.thePlayer.posX;
                z = ScaffoldOld.mc.thePlayer.posZ;
            }
            final BlockPos underPos = new BlockPos(x, ScaffoldOld.mc.thePlayer.posY - 1.0, z);
            final BlockData data = this.getBlockData(underPos);
            if (this.getBlockSlot() == -1) {
                return;
            }
            if (e.isPre() && this.getBlockSlot() != -1 && ScaffoldOld.mc.gameSettings.keyBindJump.isPressed() && !MovementUtils.isMoving() && ScaffoldOld.towerMode.is("NCP")) {
                MovementUtils.setSpeed(0.0);
                if (ScaffoldOld.mc.thePlayer.onGround) {
                    if (MovementUtils.isOnGround(0.76) && !MovementUtils.isOnGround(0.75) && ScaffoldOld.mc.thePlayer.motionY > 0.23 && ScaffoldOld.mc.thePlayer.motionY < 0.25) {
                        ScaffoldOld.mc.thePlayer.motionY = Math.round(ScaffoldOld.mc.thePlayer.posY) - ScaffoldOld.mc.thePlayer.posY;
                    }
                    if (MovementUtils.isOnGround(1.0E-4)) {
                        ScaffoldOld.mc.thePlayer.motionY = 0.41999998688697815;
                        if (ScaffoldOld.timer.hasTimeElapsed(1500L, false)) {
                            ScaffoldOld.mc.thePlayer.motionY = -0.28;
                            ScaffoldOld.timer.reset();
                        }
                    }
                    else if (ScaffoldOld.mc.thePlayer.posY >= Math.round(ScaffoldOld.mc.thePlayer.posY) - 1.0E-4 && ScaffoldOld.mc.thePlayer.posY <= Math.round(ScaffoldOld.mc.thePlayer.posY) + 1.0E-4) {
                        ScaffoldOld.mc.thePlayer.motionY = 0.0;
                    }
                }
                else if (ScaffoldOld.mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
                    ScaffoldOld.mc.thePlayer.motionY = 0.41955;
                }
            }
            if (ScaffoldOld.mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
                if (e.isPre()) {
                    final BlockPos pos2 = new BlockPos(ScaffoldOld.mc.thePlayer.posX, ScaffoldOld.mc.thePlayer.posY - 1.0, ScaffoldOld.mc.thePlayer.posZ);
                    final BlockData blockData = this.getBlockData(pos2);
                    this.blockData = blockData;
                    if (ScaffoldOld.mode.is("AAC4.4.2") || ScaffoldOld.mode.is("AAC5")) {
                        event.setPitch(72.0f);
                        event.setYaw(this.yaw);
                    }
                    if (ScaffoldOld.mode.is("NCP")) {
                        event.setYaw(this.yaw);
                        event.setPitch(72.0f);
                    }
                    if (data.face == EnumFacing.UP) {
                        ScaffoldOld.mc.timer.timerSpeed = 1.0f;
                        event.setPitch(90.0f);
                    }
                    if (!this.silent.isEnabled()) {}
                    if (ScaffoldOld.mode.is("Snap")) {
                        final float[] facing = this.getRotationsAAC(blockData.position, blockData.face);
                        final float yaw = facing[0];
                        final float pitch = facing[1];
                        event.setYaw(yaw);
                        event.setPitch(pitch);
                    }
                }
                else if (this.getBlockSlot() != -1) {
                    ScaffoldOld.mc.thePlayer.inventory.currentItem = this.getBlockSlot();
                    if (ScaffoldOld.mode.is("Snap")) {
                        final float[] facing2 = this.getRotationsAAC(this.blockData.position, this.blockData.face);
                        final float yaw2 = facing2[0];
                        final float pitch2 = facing2[1];
                        event.setYaw(yaw2);
                        event.setPitch(pitch2);
                    }
                    if (ScaffoldOld.mode.is("AAC5")) {
                        if (ScaffoldOld.mc.theWorld.getCollidingBoundingBoxes(ScaffoldOld.mc.thePlayer, ScaffoldOld.mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.1, 0.0, -0.1)).isEmpty()) {
                            ScaffoldOld.mc.playerController.onPlayerRightClick(ScaffoldOld.mc.thePlayer, ScaffoldOld.mc.theWorld, ScaffoldOld.mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, this.getVec3(data.position, data.face));
                            if (this.swing.isEnabled()) {
                                ScaffoldOld.mc.thePlayer.swingItem();
                            }
                            else {
                                ScaffoldOld.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                            }
                        }
                    }
                    else {
                        ScaffoldOld.mc.playerController.onPlayerRightClick(ScaffoldOld.mc.thePlayer, ScaffoldOld.mc.theWorld, ScaffoldOld.mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, this.getVec3(data.position, data.face));
                        if (this.swing.isEnabled()) {
                            ScaffoldOld.mc.thePlayer.swingItem();
                        }
                        else {
                            ScaffoldOld.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        }
                    }
                }
            }
            else {
                if (ScaffoldOld.mode.is("AAC4.4.2") || ScaffoldOld.mode.is("AAC5")) {
                    event.setYaw(ScaffoldOld.mc.thePlayer.rotationYaw + 180.0f);
                    event.setPitch(80.0f);
                }
                if (ScaffoldOld.mode.is("NCP")) {
                    event.setYaw(this.yaw);
                    event.setPitch(72.0f);
                }
                if (ScaffoldOld.mode.is("Snap")) {}
                if (!this.silent.isEnabled()) {}
            }
        }
    }
    
    public static boolean isValidBlock(final Block block, final boolean toPlace) {
        if (block instanceof BlockContainer) {
            return false;
        }
        if (toPlace) {
            return !(block instanceof BlockFalling) && block.isFullBlock() && block.isFullCube();
        }
        final Material material = block.getMaterial();
        return !material.isReplaceable() && !material.isLiquid();
    }
    
    public static float[] getRotations(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - ScaffoldOld.mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - ScaffoldOld.mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        final double y = block.getY() + 0.5 + face.getFrontOffsetZ() / 2;
        final double d1 = ScaffoldOld.mc.thePlayer.posY + ScaffoldOld.mc.thePlayer.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    private int getBlockSlot() {
        int item = -1;
        int stacksize = 0;
        for (int i = 36; i < 45; ++i) {
            if (ScaffoldOld.mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && ScaffoldOld.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !ScaffoldOld.invalidBlocks.contains(((ItemBlock)ScaffoldOld.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock()) && ScaffoldOld.mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize >= stacksize) {
                item = i - 36;
                stacksize = ScaffoldOld.mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }
        return item;
    }
    
    public Vec3 getVec3(final BlockPos pos, final EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += new Random().nextDouble() / 2.0 - 0.25;
            z += new Random().nextDouble() / 2.0 - 0.25;
        }
        else {
            y += new Random().nextDouble() / 2.0 - 0.25;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += new Random().nextDouble() / 2.0 - 0.25;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += new Random().nextDouble() / 2.0 - 0.25;
        }
        return new Vec3(x, y, z);
    }
    
    private boolean isValidItem(final Item item) {
        if (item instanceof ItemBlock) {
            final ItemBlock iBlock = (ItemBlock)item;
            final Block block = iBlock.getBlock();
            return !ScaffoldOld.invalidBlocks.contains(block);
        }
        return false;
    }
    
    public static boolean isPosSolid(final BlockPos pos) {
        final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }
    
    public float[] getRotationsAAC(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - ScaffoldOld.mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - ScaffoldOld.mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        double y = block.getY() + 0.5 + face.getFrontOffsetZ() / 2;
        y += 0.5;
        final double d1 = ScaffoldOld.mc.thePlayer.posY + ScaffoldOld.mc.thePlayer.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    private BlockData getBlockData(final BlockPos pos) {
        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        final BlockPos pos2 = pos.add(0, -1, 0).add(1, 0, 0);
        final BlockPos pos3 = pos.add(0, -1, 0).add(0, 0, 1);
        final BlockPos pos4 = pos.add(0, -1, 0).add(-1, 0, 0);
        final BlockPos pos5 = pos.add(0, -1, 0).add(0, 0, -1);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        return null;
    }
    
    public void getYaw(final BlockPos pos) {
        final float[] rotations = RotationUtils.getFaceDirectionToBlockPos(pos, this.yaw, this.pitch);
        float yaw = 0.0f;
        if (ScaffoldOld.mc.gameSettings.keyBindForward.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw + 180.0f;
        }
        if (ScaffoldOld.mc.gameSettings.keyBindLeft.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw + 90.0f;
        }
        if (ScaffoldOld.mc.gameSettings.keyBindRight.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw - 90.0f;
        }
        if (ScaffoldOld.mc.gameSettings.keyBindBack.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw;
        }
        if (ScaffoldOld.mc.gameSettings.keyBindForward.isKeyDown() && ScaffoldOld.mc.gameSettings.keyBindLeft.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw + 90.0f + 45.0f;
        }
        if (ScaffoldOld.mc.gameSettings.keyBindForward.isKeyDown() && ScaffoldOld.mc.gameSettings.keyBindRight.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw - 90.0f - 45.0f;
        }
        if (ScaffoldOld.mc.gameSettings.keyBindBack.isKeyDown() && ScaffoldOld.mc.gameSettings.keyBindLeft.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw + 90.0f - 45.0f;
        }
        if (ScaffoldOld.mc.gameSettings.keyBindBack.isKeyDown() && ScaffoldOld.mc.gameSettings.keyBindRight.isKeyDown()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw - 90.0f + 45.0f;
        }
        if (!MovementUtils.isMoving()) {
            yaw = ScaffoldOld.mc.thePlayer.rotationYaw + 180.0f;
        }
        this.yaw = yaw;
    }
    
    public void getPitch(final BlockPos pos) {
        final float[] rotations = RotationUtils.getFaceDirectionToBlockPos(pos, this.yaw, this.pitch);
        final float pitch = rotations[1];
        this.pitch = pitch;
    }
    
    static {
        ScaffoldOld.mode = new ModeSetting("Mode", "NCP", new String[] { "NCP", "AAC4.4.2", "AAC5", "Snap" });
        ScaffoldOld.towerMode = new ModeSetting("Tower", "NCP", new String[] { "NCP", "None" });
        ScaffoldOld.timer = new TimerUtils();
        ScaffoldOld.heldItem = 0;
        ScaffoldOld.invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot);
    }
    
    private class BlockData
    {
        public BlockPos position;
        public EnumFacing face;
        
        private BlockData(final BlockPos position, final EnumFacing face, final BlockData blockData) {
            this.position = position;
            this.face = face;
        }
    }
}
