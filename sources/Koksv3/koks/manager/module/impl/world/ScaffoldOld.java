package koks.manager.module.impl.world;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.*;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

/**
 * @author avox | lmao | kroko
 * @created on 04.09.2020 : 10:23
 */

@ModuleInfo(name = "ScaffoldOld", description = "Its place blocks under you", category = Module.Category.WORLD)
public class ScaffoldOld extends Module {

    private final List blackList;

    public BlockPos finalPos;
    public boolean shouldBuildDown;

    public Setting delay = new Setting("Delay", 0, 0, 100, true, this);
    public Setting motion = new Setting("Motion", 1, 0, 5, false, this);

    public Setting pitchVal = new Setting("Pitch", 82, 70, 90, true, this);

    public Setting sneak = new Setting("Sneak", false, this);
    public Setting sneakAfterBlocks = new Setting("Sneak After...", 10, 0, 20, true, this);

    public Setting swingItem = new Setting("Swing Item", true, this);
    public Setting safeWalk = new Setting("SafeWalk", true, this);
    public Setting onGround = new Setting("OnGround", true, this);

    public Setting silent = new Setting("Silent", true, this);

    public Setting downScaffold = new Setting("DownScaffold", false, this);

    public Setting randomHit = new Setting("Random Hit", false, this);
    public Setting sprint = new Setting("Sprint", false, this);

    public Setting rayCast = new Setting("RayCast", true, this);

    public Setting simpleRotations = new Setting("Simple Rotations", true, this);

    public Setting staticPitch = new Setting("StaticPitch", false, this);

    public Setting intave = new Setting("Intave", false, this);

    public Setting alwaysLook = new Setting("AlwaysLook", true, this);

    public float pitch, yaw;
    public int sneakCount;

    public ScaffoldOld() {
        this.blackList = Arrays.asList(Blocks.red_flower, Blocks.yellow_flower, Blocks.crafting_table, Blocks.chest, Blocks.enchanting_table, Blocks.anvil, Blocks.sand, Blocks.gravel, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.ice, Blocks.packed_ice, Blocks.cobblestone_wall, Blocks.water, Blocks.lava, Blocks.web, Blocks.sapling, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.tnt, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.tallgrass, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.ladder, Blocks.torch, Blocks.stone_button, Blocks.wooden_button, Blocks.redstone_torch, Blocks.redstone_wire, Blocks.furnace, Blocks.cactus, Blocks.oak_fence, Blocks.acacia_fence, Blocks.nether_brick_fence, Blocks.birch_fence, Blocks.dark_oak_fence, Blocks.jungle_fence, Blocks.oak_fence, Blocks.acacia_fence_gate, Blocks.snow_layer, Blocks.trapdoor, Blocks.ender_chest, Blocks.beacon, Blocks.hopper, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.carpet);
    }

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventMotion) {
            if (((EventMotion) event).getType() == EventMotion.Type.PRE) {
                ((EventMotion) event).setYaw(yaw);
                ((EventMotion) event).setPitch(pitch);
            }
        }

        if (event instanceof EventJump) {
            ((EventJump) event).setYaw(yaw);
        }

        if (event instanceof EventUpdate) {
            if (downScaffold.isToggled()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    shouldBuildDown = true;
                    mc.gameSettings.keyBindSneak.pressed = false;
                } else {
                    shouldBuildDown = false;
                }
            }
            BlockPos pos;

            pos = new BlockPos(mc.thePlayer.posX, (mc.thePlayer.getEntityBoundingBox()).minY - 1.0D - (shouldBuildDown ? 1 : 0), mc.thePlayer.posZ);
            getPlayer().setSprinting(sprint.isToggled());
            getGameSettings().keyBindSprint.pressed = sprint.isToggled();

            getBlockPosToPlaceOn(pos);

            if (simpleRotations.isToggled()) {
                setYawSimple();
            } else {
                setYaw();
            }

            pitch = getPitch(360);

        }
        if (event instanceof EventSafeWalk) {
            if (downScaffold.isToggled()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    shouldBuildDown = true;
                    mc.gameSettings.keyBindSneak.pressed = false;
                } else {
                    shouldBuildDown = false;
                }
            }
            if (safeWalk.isToggled()) {
                if (getPlayer().onGround || !onGround.isToggled())
                    ((EventSafeWalk) event).setSafe(true);
            }
        }
    }


    public void setYaw() {
        if (finalPos != null) {
            float[] rotations = rotationUtil.faceBlock(finalPos, yaw, pitch, 360);
            yaw = rotations[0];
        }
    }


    public void setYawSimple() {
        boolean forward = mc.gameSettings.keyBindForward.isKeyDown();
        boolean left = mc.gameSettings.keyBindLeft.isKeyDown();
        boolean right = mc.gameSettings.keyBindRight.isKeyDown();
        boolean back = mc.gameSettings.keyBindBack.isKeyDown();

        float yaw = 0;

        // Only one Key directions
        if (forward && !left && !right && !back)
            yaw = 180;
        if (!forward && left && !right && !back)
            yaw = 90;
        if (!forward && !left && right && !back)
            yaw = -90;
        if (!forward && !left && !right && back)
            yaw = 0;

        // Multi Key directions
        if (forward && left && !right && !back)
            yaw = 135;
        if (forward && !left && right && !back)
            yaw = -135;

        if (!forward && left && !right && back)
            yaw = 45;
        if (!forward && !left && right && back)
            yaw = -45;

        this.yaw = mc.thePlayer.rotationYaw + yaw;
    }

    public float getPitch(int speed) {
        if (mc.thePlayer.onGround || staticPitch.isToggled()) {
            return pitchVal.getCurrentValue();
        } else if (finalPos != null)
            return rotationUtil.faceBlock(finalPos, yaw, pitch, speed)[1];
        return pitchVal.getCurrentValue();
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void placeBlock(BlockPos pos, EnumFacing face) {
        finalPos = pos;
        ItemStack silentItemStack = null;
        int silentSlot = mc.thePlayer.inventory.currentItem;
        if (silent.isToggled() && (mc.thePlayer.getCurrentEquippedItem() == null || (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)))) {
            for (int i = 0; i < 9; i++) {
                if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem();
                    if (this.blackList.contains(itemBlock.getBlock()))
                        continue;

                    silentSlot = i;
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(silentSlot));
                    silentItemStack = mc.thePlayer.inventory.getStackInSlot(i);
                    break;
                }
            }
        } else {
            if (getPlayer().getCurrentEquippedItem() != null) {
                silentItemStack = (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) ? mc.thePlayer.getCurrentEquippedItem() : null;
                BlockPos position = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D - (shouldBuildDown ? 1 : 0), mc.thePlayer.posZ);
                if (mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir) {
                    if (silentItemStack != null && blackList.contains(((ItemBlock) silentItemStack.getItem()).getBlock()))
                        return;
                    mc.thePlayer.swingItem();
                }
            }
        }

        if (sneakCount >= sneakAfterBlocks.getCurrentValue() && sneak.isToggled())
            mc.gameSettings.keyBindSneak.pressed = true;

        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D - (shouldBuildDown ? 1 : 0), mc.thePlayer.posZ)).getBlock() instanceof BlockAir) {
            if (!simpleRotations.isToggled())
                setYaw();
            if (silentItemStack != null) {
                boolean rayCasted = !rayCast.isToggled() || rayCastUtil.isRayCastBlock(pos, rayCastUtil.rayCastedBlock(yaw, pitch));
                if (rayCast.isToggled()) {
                    if (rayCasted) {
                        if (timeHelper.hasReached(mc.thePlayer.onGround ? (randomUtil.getRandomLong((long) delay.getCurrentValue(), (long) delay.getCurrentValue() + 1)) : 20L)) {
                            if (blackList.contains(((ItemBlock) silentItemStack.getItem()).getBlock()))
                                return;
                            if (silentItemStack != null) {
                                MovingObjectPosition ray = rayCastUtil.rayCastedBlock(yaw, pitch);
                                if (intave.isToggled()) {
                                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, silentItemStack, ray.getBlockPos(), ray.sideHit, ray.hitVec))
                                        if (swingItem.isToggled())
                                            mc.thePlayer.swingItem();
                                        else
                                            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                                } else if (!intave.isToggled())
                                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, silentItemStack, pos, face, new Vec3(pos.getX() + (this.randomHit.isToggled() ? randomUtil.getRandomDouble(0, 0.7) : 0), pos.getY() + (this.randomHit.isToggled() ? randomUtil.getRandomDouble(0, 0.7) : 0), pos.getZ() + (this.randomHit.isToggled() ? randomUtil.getRandomDouble(0, 0.7) : 0))))
                                        if (swingItem.isToggled())
                                            mc.thePlayer.swingItem();
                                        else
                                            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());

                                sneakCount++;

                                mc.thePlayer.motionX *= motion.getCurrentValue();
                                mc.thePlayer.motionZ *= motion.getCurrentValue();

                                if (sneakCount > sneakAfterBlocks.getCurrentValue())
                                    sneakCount = 0;

                                timeHelper.reset();
                            }
                        }
                    } else {

                        timeHelper.reset();
                    }
                } else if (intave.isToggled() && !rayCast.isToggled()) {
                    if (timeHelper.hasReached(mc.thePlayer.onGround ? (randomUtil.getRandomLong((long) delay.getCurrentValue(), (long) delay.getCurrentValue() + 1)) : 20L)) {
                        if (blackList.contains(((ItemBlock) silentItemStack.getItem()).getBlock()))
                            return;
                        if (silentItemStack != null) {

                            mc.rightClickMouse();

                            getPlayer().swingItem();

                            sneakCount++;

                            mc.thePlayer.motionX *= motion.getCurrentValue();
                            mc.thePlayer.motionZ *= motion.getCurrentValue();

                            if (sneakCount > sneakAfterBlocks.getCurrentValue())
                                sneakCount = 0;

                            timeHelper.reset();
                        }
                    }
                } else {
                    if (timeHelper.hasReached(mc.thePlayer.onGround ? (randomUtil.getRandomLong((long) delay.getCurrentValue(), (long) delay.getCurrentValue() + 1)) : 20L)) {
                        if (blackList.contains(((ItemBlock) silentItemStack.getItem()).getBlock()))
                            return;
                        if (silentItemStack != null) {
                            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, silentItemStack, pos, face, new Vec3(pos.getX() + (this.randomHit.isToggled() ? randomUtil.getRandomDouble(0, 0.7) : 0), pos.getY() + (this.randomHit.isToggled() ? randomUtil.getRandomDouble(0, 0.7) : 0), pos.getZ() + (this.randomHit.isToggled() ? randomUtil.getRandomDouble(0, 0.7) : 0))))
                                if (swingItem.isToggled())
                                    mc.thePlayer.swingItem();
                                else
                                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                            sneakCount++;

                            mc.thePlayer.motionX *= motion.getCurrentValue();
                            mc.thePlayer.motionZ *= motion.getCurrentValue();

                            if (sneakCount > sneakAfterBlocks.getCurrentValue())
                                sneakCount = 0;

                            timeHelper.reset();
                        }
                    }

                    timeHelper.reset();
                }
            } else {
                mc.gameSettings.keyBindSneak.pressed = false;
                timeHelper.reset();
                if (!simpleRotations.isToggled())
                    setYaw();
            }
            shouldBuildDown = false;
        }else{
            mc.gameSettings.keyBindSneak.pressed = false;
        }
    }

    @Override
    public void onEnable() {
        yaw = getPlayer().rotationYaw;
        pitch = getPlayer().rotationPitch;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false;
        sneakCount = 0;
        yaw = 0;
        pitch = 0;
        mc.timer.timerSpeed = 1F;
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void getBlockPosToPlaceOn(BlockPos pos) {
        BlockPos blockPos1 = pos.add(-1, 0, 0);
        BlockPos blockPos2 = pos.add(1, 0, 0);
        BlockPos blockPos3 = pos.add(0, 0, -1);
        BlockPos blockPos4 = pos.add(0, 0, 1);
        float down = (shouldBuildDown ? 1 : 0);
        if (mc.theWorld.getBlockState(pos.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(pos.add(0, -1, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(pos.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(pos.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(pos.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(pos.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(pos.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(pos.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos1.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos1.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos2.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos2.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos3.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos3.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos4.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos4.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(0, 0 - down, 1), EnumFacing.NORTH);
        }
    }

}
