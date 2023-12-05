package koks.manager.module.impl.world;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventMotion;
import koks.manager.event.impl.EventSafeWalk;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 * @author kroko
 * @created on 26.11.2020 : 17:33
 */

@ModuleInfo(name = "Scaffold", description = "Its place blocks under you", category = Module.Category.WORLD)
public class Scaffold extends Module {

    //BUILD
    public Setting delay = new Setting("Delay", 0, 0, 500, true, this);
    public Setting motion = new Setting("Motion", 1, 0, 5, false, this);
    public Setting diagonal = new Setting("Diagonal", false, this);
    public Setting silent = new Setting("Silent", true, this);
    public Setting noSwing = new Setting("NoSwing", false, this);
    public Setting smartPlacement = new Setting("Smart Placement", true, this);

    //MOVEMENT
    public Setting safeWalk = new Setting("Safewalk", false, this);
    public Setting onlyGround = new Setting("OnlyGround", true, this);
    public Setting sprint = new Setting("Sprint", false, this);
    public Setting sneak = new Setting("Sneak", false, this);
    public Setting sneakAfter = new Setting("Sneak after...", 1, 1, 10, true, this);

    //RAYCAST
    public Setting rayCast = new Setting("Raycast", false, this);

    //ROTATION
    public Setting alwaysRotate = new Setting("Always Rotate", true, this);
    public Setting simpleRotations = new Setting("Simple Rotations", false, this);
    public Setting staticPitch = new Setting("Static Pitch", false, this);
    public Setting pitch = new Setting("Pitch", 82, 70, 90, false, this);

    float curYaw, curPitch;
    int sneakCount;
    int lastSlot;
    EnumFacing enumFacing;
    private final List<Block> blackList;

    public Scaffold() {
        this.blackList = Arrays.asList(Blocks.red_flower, Blocks.yellow_flower, Blocks.crafting_table, Blocks.chest, Blocks.enchanting_table, Blocks.anvil, Blocks.sand, Blocks.gravel, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.ice, Blocks.packed_ice, Blocks.cobblestone_wall, Blocks.water, Blocks.lava, Blocks.web, Blocks.sapling, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.tnt, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.tallgrass, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.ladder, Blocks.torch, Blocks.stone_button, Blocks.wooden_button, Blocks.redstone_torch, Blocks.redstone_wire, Blocks.furnace, Blocks.cactus, Blocks.oak_fence, Blocks.acacia_fence, Blocks.nether_brick_fence, Blocks.birch_fence, Blocks.dark_oak_fence, Blocks.jungle_fence, Blocks.oak_fence, Blocks.acacia_fence_gate, Blocks.snow_layer, Blocks.trapdoor, Blocks.ender_chest, Blocks.beacon, Blocks.hopper, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.carpet);
    }

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventSafeWalk) {
            if (safeWalk.isToggled())
                ((EventSafeWalk) event).setSafe(mc.thePlayer.onGround || !onlyGround.isToggled());
        }

        if (event instanceof EventMotion) {
            if (((EventMotion) event).getType().equals(EventMotion.Type.PRE)) {
                if (alwaysRotate.isToggled()) {
                    ((EventMotion) event).setYaw(curYaw);
                    ((EventMotion) event).setPitch(curPitch);
                }
            }
        }

        if (event instanceof EventUpdate) {
            BlockPos blockPos = getBlockPosToPlaceOn(new BlockPos(getX(), getY() - 1, getZ()));
            ItemStack itemStack = getPlayer().getCurrentEquippedItem();

            if (silent.isToggled() && (itemStack == null || (itemStack != null && !(itemStack.getItem() instanceof ItemBlock)))) {
                for (int i = 0; i < 9; i++) {
                    ItemStack item = getPlayer().inventory.getStackInSlot(i);
                    if (item != null && item.getItem() instanceof ItemBlock) {
                        if (!blackList.contains(Block.getBlockFromItem(item.getItem()))) {
                            itemStack = item;
                            lastSlot = getPlayer().inventory.currentItem;
                            sendPacket(new C09PacketHeldItemChange(i));
                        }
                    }
                }
            }

            if (blockPos != null && itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                getPlayer().setSprinting(sprint.isToggled());
                getGameSettings().keyBindSprint.pressed = sprint.isToggled();

                if (sneak.isToggled() && sneakCount >= sneakAfter.getCurrentValue())
                    getGameSettings().keyBindSneak.pressed = true;
                else if (sneakCount < sneakAfter.getCurrentValue())
                    getGameSettings().keyBindSneak.pressed = false;

                float[] rotation = rotationUtil.faceBlock(blockPos, (float) (mc.theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMaxY() - mc.theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMinY()) + 0.5F, curYaw, curPitch, 180);
                if (!simpleRotations.isToggled()) {
                    curYaw = rotation[0];
                    curPitch = rotation[1];
                } else {
                    curYaw = getPlayer().rotationYaw + 180;
                    curPitch = getPlayer().onGround || staticPitch.isToggled() ? pitch.getCurrentValue() : rotation[1];
                }
                MovingObjectPosition ray = rayCastUtil.rayCastedBlock(curYaw, curPitch);
                if (timeHelper.hasReached((long) delay.getCurrentValue()) && ((ray != null && ray.getBlockPos().equals(blockPos)) || !rayCast.isToggled())) {
                    BlockPos blockpos = mc.objectMouseOver.getBlockPos();
                    if (blockpos != null && getWorld().getBlockState(blockPos) != null && getWorld().getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                        if (smartPlacement.isToggled() ? (getPlayerController().onPlayerRightClick(getPlayer(), getWorld(), itemStack, blockpos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec)) : mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, blockPos, enumFacing, new Vec3(blockPos.getX() + randomUtil.getRandomDouble(0, 0.7), blockPos.getY() + randomUtil.getRandomDouble(0, 0.7), blockPos.getZ() + randomUtil.getRandomDouble(0, 0.7)))) {
                            getPlayer().motionX *= motion.getCurrentValue();
                            getPlayer().motionZ *= motion.getCurrentValue();
                            sneakCount++;
                            if (sneakCount > sneakAfter.getCurrentValue())
                                sneakCount = 0;

                            if (!noSwing.isToggled())
                                getPlayer().swingItem();
                        }
                    }

                    timeHelper.reset();
                }
            } else {
                if (sneak.isToggled())
                    getGameSettings().keyBindSneak.pressed = false;
                timeHelper.reset();
            }
        }
    }

    @Override
    public void onEnable() {
        sneakCount = 0;
        lastSlot = -1;
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
        lastSlot = getPlayer().inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (silent.isToggled())
            sendPacket(new C09PacketHeldItemChange(lastSlot));
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private BlockPos getBlockPosToPlaceOn(BlockPos pos) {
        BlockPos blockPos1 = pos.add(-1, 0, 0);
        BlockPos blockPos2 = pos.add(1, 0, 0);
        BlockPos blockPos3 = pos.add(0, 0, -1);
        BlockPos blockPos4 = pos.add(0, 0, 1);
        float down = 0;
        if (mc.theWorld.getBlockState(pos.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.UP;
            return (pos.add(0, -1, 0));
        } else if (mc.theWorld.getBlockState(pos.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.EAST;
            return (pos.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(pos.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.WEST;
            return (pos.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.SOUTH;
            return (pos.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.NORTH;
            return (pos.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.UP;
            return (blockPos1.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos1.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.EAST;
            return (blockPos1.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos1.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.WEST;
            return (blockPos1.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos1.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos1.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.UP;
            return (blockPos2.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos2.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.EAST;
            return (blockPos2.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos2.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.WEST;
            return (blockPos2.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos2.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos2.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.UP;
            return (blockPos3.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos3.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.EAST;
            return (blockPos3.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos3.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.WEST;
            return (blockPos3.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos3.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos3.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.UP;
            return (blockPos4.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos4.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.EAST;
            return (blockPos4.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos4.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.WEST;
            return (blockPos4.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos4.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal.isToggled()) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos4.add(0, 0 - down, 1));
        }
        return null;
    }
}
