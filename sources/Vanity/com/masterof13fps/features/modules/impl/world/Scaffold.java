package com.masterof13fps.features.modules.impl.world;

import com.masterof13fps.Methods;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.entity.EntityUtils;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventMotion;
import com.masterof13fps.manager.eventmanager.impl.EventSafeWalk;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;

import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "Scaffold", description = "Its place blocks under you", category = Category.WORLD)
public class Scaffold extends Module {

    //BUILD
    public Setting delay = new Setting("Delay", this, 0, 0, 500, true);
    public Setting motion = new Setting("Motion", this, 1, 0, 5, false);
    public Setting diagonal = new Setting("Diagonal", this, false);
    public Setting silent = new Setting("Silent", this, true);
    public Setting noSwing = new Setting("NoSwing", this, false);
    //MOVEMENT
    public Setting safeWalk = new Setting("Safewalk", this, false);
    public Setting onlyGround = new Setting("OnlyGround", this, true);
    public Setting sprint = new Setting("Sprint", this, false);
    public Setting sneak = new Setting("Sneak", this, false);
    public Setting sneakAfter = new Setting("Sneak after...", this, 1, 1, 10, true);
    //RAYCAST
    public Setting rayCast = new Setting("Raycast", this, false);
    //ROTATION
    public Setting alwaysRotate = new Setting("Always Rotate", this, true);
    public Setting simpleRotations = new Setting("Simple Rotations", this, false);
    public Setting staticPitch = new Setting("Static Pitch", this, false);
    public Setting pitch = new Setting("Pitch", this, 82, 70, 90, false);

    float curYaw, curPitch;
    int sneakCount;
    int lastSlot;
    EnumFacing enumFacing;

    public Scaffold() {
        List<Block> blackList = Arrays.asList(Blocks.red_flower, Blocks.yellow_flower, Blocks.crafting_table, Blocks.chest, Blocks.enchanting_table, Blocks.anvil, Blocks.sand, Blocks.gravel, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.ice, Blocks.packed_ice, Blocks.cobblestone_wall, Blocks.water, Blocks.lava, Blocks.web, Blocks.sapling, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.tnt, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.tallgrass, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.ladder, Blocks.torch, Blocks.stone_button, Blocks.wooden_button, Blocks.redstone_torch, Blocks.redstone_wire, Blocks.furnace, Blocks.cactus, Blocks.oak_fence, Blocks.acacia_fence, Blocks.nether_brick_fence, Blocks.birch_fence, Blocks.dark_oak_fence, Blocks.jungle_fence, Blocks.oak_fence, Blocks.acacia_fence_gate, Blocks.snow_layer, Blocks.trapdoor, Blocks.ender_chest, Blocks.beacon, Blocks.hopper, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.carpet);
    }

    public float[] faceBlock(BlockPos pos, float yTranslation, float currentYaw, float currentPitch, float speed) {
        double x = (pos.getX() + 0.5F) - mc.thePlayer.posX - mc.thePlayer.motionX;
        double y = (pos.getY() - yTranslation) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = (pos.getZ() + 0.5F) - mc.thePlayer.posZ - mc.thePlayer.motionZ;

        double calculate = MathHelper.sqrt_double(x * x + z * z);
        float calcYaw = (float) (MathHelper.func_181159_b(z, x) * 180.0D / Math.PI) - 90.0F;
        float calcPitch = (float) -(MathHelper.func_181159_b(y, calculate) * 180.0D / Math.PI);

        //TODO: Besserer Mouse Sensi Fix da er auf Verus Kickt

        float yaw = updateRotation(currentYaw, calcYaw, speed);
        float pitch = updateRotation(currentPitch, calcPitch, speed);

        return new float[]{yaw, pitch >= 90 ? 90 : pitch <= -90 ? -90 : pitch};
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void onEvent(Event event) {
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

            if (silent.isToggled() && itemStack == null || (itemStack != null && !(itemStack.getItem() instanceof ItemBlock))) {
                for (int i = 0; i < 9; i++) {
                    ItemStack item = getPlayer().inventory.getStackInSlot(i);
                    if (item != null && item.getItem() instanceof ItemBlock) {
                        itemStack = item;
                        sendPacket(new C09PacketHeldItemChange(i));
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

                float[] rotation = faceBlock(blockPos, (float) (mc.theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMaxY() - mc.theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMinY()) + 0.5F, curYaw, curPitch, 180);
                if (!simpleRotations.isToggled()) {
                    curYaw = rotation[0];
                    curPitch = rotation[1];
                } else {
                    curYaw = getPlayer().rotationYaw + 180;
                    curPitch = getPlayer().onGround || staticPitch.isToggled() ? (float) pitch.getCurrentValue() : rotation[1];
                }
                MovingObjectPosition ray = EntityUtils.rayCastedBlock(curYaw, curPitch);
                if (timeHelper.hasReached((long) delay.getCurrentValue()) && (ray != null && ray.getBlockPos().equals(blockPos) || !rayCast.isToggled())) {

                    if (getPlayerController().onPlayerRightClick(getPlayer(), mc.theWorld, itemStack, blockPos, enumFacing, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))) {
                        getPlayer().motionX *= motion.getCurrentValue();
                        getPlayer().motionZ *= motion.getCurrentValue();
                        sneakCount++;
                        if (sneakCount > sneakAfter.getCurrentValue())
                            sneakCount = 0;

                        if (!noSwing.isToggled())
                            getPlayer().swingItem();


                        timeHelper.reset();
                    }
                } else {
                    if (sneak.isToggled())
                        getGameSettings().keyBindSneak.pressed = false;
                    timeHelper.reset();
                }
            }
        }
    }

    @Override
    public void onEnable() {
        sneakCount = 0;
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
        lastSlot = getPlayer().inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (silent.isToggled())
            sendPacket(new C09PacketHeldItemChange(lastSlot));
    }

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
