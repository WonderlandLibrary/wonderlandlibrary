package me.crimp.claudius.mod.modules.pvp;

import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hub on 10 December 2019
 * Updated by hub on 08 July 2020
 */


//TODO REDO EVERYTHING
public class Dispenser32k extends Module {

    public static final List<Block> SHULKERS = Arrays.asList( // TODO: Move this to a central place
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.SILVER_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX
    );

    private static final DecimalFormat df = new DecimalFormat("#.#");

    private static final Minecraft mc = Minecraft.getMinecraft();

    private int stage;

    private BlockPos placeTarget;

    private int obiSlot;
    private int dispenserSlot;
    private int shulkerSlot;
    private int redstoneSlot;
    private int hopperSlot;

    private boolean isSneaking;

    public Dispenser32k() {
        super("Dispenser32k", "Auto32k With Dispenser",Category.Pvp, false, false);
    }

    @Override
    public void onEnable() {

        if (mc.player == null) { // TODO: check for conflicting active modules? freecam etc.
            return;
        }

        df.setRoundingMode(RoundingMode.CEILING);

        stage = 0;

        placeTarget = null;

        obiSlot = -1;
        dispenserSlot = -1;
        shulkerSlot = -1;
        redstoneSlot = -1;
        hopperSlot = -1;

        isSneaking = false;

        for (int i = 0; i < 9; i++) {

            if (obiSlot != -1 && dispenserSlot != -1 && shulkerSlot != -1 && redstoneSlot != -1 && hopperSlot != -1) {
                break;
            }

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block == Blocks.HOPPER) {
                hopperSlot = i;
            } else if (SHULKERS.contains(block)) {
                shulkerSlot = i;
            } else if (block == Blocks.OBSIDIAN) {
                obiSlot = i;
            } else if (block == Blocks.DISPENSER) {
                dispenserSlot = i;
            } else if (block == Blocks.REDSTONE_BLOCK) {
                redstoneSlot = i;
            }

        }

        if (obiSlot == -1 || dispenserSlot == -1 || shulkerSlot == -1 || redstoneSlot == -1 || hopperSlot == -1) {
            Command.sendMessage("[Auto32k] Items missing, disabling.");
            this.toggle();
            return;
        }

        // IntelliJ dumb, iirc this can cause npe when looking in water, at crystal etc.
        if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null || mc.objectMouseOver.getBlockPos().up() == null || Dispenser32k.mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
            Command.sendMessage("[Auto32k] Not a valid place target, disabling.");
            this.toggle();
            return;
        }

        placeTarget = mc.objectMouseOver.getBlockPos().up();

/*        if (autoEnableBypass.getValue()) { // TODO: Add IllegalItemBypass (Secret Close) Module & Option
            ModuleManager.getModuleByName("IllegalItemBypass").enable();
        }*/

        //Command.sendMessage("[Auto32k] Place Target: " + placeTarget.getX() + " " + placeTarget.getY() + " " + placeTarget.getZ() + " Distance: " + df.format(mc.player.getPositionVector().distanceTo(new Vec3d(placeTarget))));

    }

    @Override
    public void onUpdate() {

        if (mc.player == null) { // TODO: check for conflicting active modules? freecam etc.
            return;
        }

        // stage 0: place obi and dispenser
        if (stage == 0) {

            mc.player.inventory.currentItem = obiSlot;
            placeBlock(new BlockPos(placeTarget), EnumFacing.DOWN);

            mc.player.inventory.currentItem = dispenserSlot;
            //float[] angle = RotationUtil.getLegitRotations(RotationUtil.getEyesPos());
            //RotationUtil.faceYawAndPitch(angle[0], angle[1]);
            placeBlock(new BlockPos(placeTarget.add(0, 1, 0)), EnumFacing.DOWN);

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.add(0, 1, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

            stage = 1;
            return;

        }

        // stage 1: put shulker, place redstone
        if (stage == 1) {

            if (!(mc.currentScreen instanceof GuiContainer)) {
                return;
            }

            mc.playerController.windowClick(mc.player.openContainer.windowId, 1, shulkerSlot, ClickType.SWAP, mc.player);
            mc.player.closeScreen();

            mc.player.inventory.currentItem = redstoneSlot;
            placeBlock(new BlockPos(placeTarget.add(0, 2, 0)), EnumFacing.DOWN);

            stage = 2;
            return;

        }

        // stage 2: place hopper
        if (stage == 2) {

            // TODO: fix instahopper, why boken? ;(
            Block block = mc.world.getBlockState(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()).up()).getBlock();
            if ((block instanceof BlockAir) || (block instanceof BlockLiquid)) {
                return;
            }

            mc.player.inventory.currentItem = hopperSlot;
            placeBlock(new BlockPos(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite())), mc.player.getHorizontalFacing());

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

            mc.player.inventory.currentItem = shulkerSlot;

            stage = 3;
            return;

        }

        // stage 3: hopper gui
        if (stage == 3) {

            if (!(mc.currentScreen instanceof GuiContainer)) {
                return;
            }

            if (((GuiContainer) mc.currentScreen).inventorySlots.getSlot(0).getStack().isEmpty) {
                return;
            }

            mc.playerController.windowClick(mc.player.openContainer.windowId, 0, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);

/*            if (autoEnableHitAura.getValue()) { // TODO: Add Auto Kill Aura Option
                ModuleManager.getModuleByName("Aura").enable();
            }*/

            this.toggle();

        }

    };

    private void placeBlock(BlockPos pos, EnumFacing side) {

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        if (!isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
/*
        if (rotate.getValue()) { // TODO: Add Rotation Option
            faceVectorPacketInstant(hitVec);
        }*/

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);

    }

}
