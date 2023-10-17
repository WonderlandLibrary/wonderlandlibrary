package de.tired.base.module.implementation.world;

import de.tired.base.annotations.ModuleAnnotation;

import de.tired.base.event.events.*;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import de.tired.util.combat.RayCastUtil;
import de.tired.util.hook.PlayerHook;
import de.tired.util.math.MathUtil;
import de.tired.util.math.TimerUtil;
import de.tired.util.player.StrafeUtil;
import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.util.hook.Rotations;
import de.tired.base.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ModuleAnnotation(name = "ScaffoldWalk", category = ModuleCategory.PLAYER, clickG = "Place blocks under you")
public class ScaffoldWalk extends Module {
    private final List invalidBlocks;


    private RayCastUtil rayCastUtil = new RayCastUtil();

    public int fakeSlot, slot, currentSlot;
    private final TimerUtil placeDelay = new TimerUtil();
    private final TimerUtil placeDelayFakePlace = new TimerUtil();

    private float[] randomFactor = new float[2];

    private float[] rotations = new float[2], savedRotations = new float[2];


    private final BooleanSetting swing = new BooleanSetting("Swing", this, true);

    private final BooleanSetting randomRotations = new BooleanSetting("RandomRotations", this, true);

    public BooleanSetting stopOnEdge = new BooleanSetting("stopOnEdge", this, true);

    public BooleanSetting fakePreSwing = new BooleanSetting("fakePreSwing", this, true);

    private final ModeSetting swingMode = new ModeSetting("swingMode", this, new String[]{"Vanilla", "Packet"}, swing::getValue);

    private final BooleanSetting rotationsValue = new BooleanSetting("Rotations", this, true);

    private final BooleanSetting gcdFix = new BooleanSetting("GcdFix", this, true);

    private final BooleanSetting tower = new BooleanSetting("Tower", this, true);
    private final BooleanSetting gcdFixA3 = new BooleanSetting("GcdFixA3", this, true, gcdFix::getValue);

    public final BooleanSetting saveWalk = new BooleanSetting("SaveWalk", this, true);

    public final BooleanSetting placeESP = new BooleanSetting("Place Esp", this, true);

    public final BooleanSetting sneak = new BooleanSetting("sneak", this, true);

    public final BooleanSetting packetSneak = new BooleanSetting("packet Sneak", this, true, () -> sneak.getValue());

    public final BooleanSetting resetRightClickDelayTimer = new BooleanSetting("Reset RightClickDelayTimer", this, true);

    private final BooleanSetting noSprint = new BooleanSetting("No Sprint", this, true);

    private final BooleanSetting rayTrace = new BooleanSetting("RayTrace", this, true, rotationsValue::getValue);

    private final ModeSetting rotationMode = new ModeSetting("RotationMode", this, new String[]{"Always", "180", "AllowRotation"}, rotationsValue::getValue);

    private final ModeSetting towerMode = new ModeSetting("TowerMode", this, new String[]{"Motion", "Verus", "BlocksMC"}, tower::getValue);

    public BooleanSetting prediction = new BooleanSetting("prediction", this, true, () -> !rotationMode.getValue().equalsIgnoreCase("180"));


    private final BooleanSetting movementCorrection = new BooleanSetting("MovementCorrection", this, true, () -> rotationsValue.getValue() && !rotationMode.getValue().equalsIgnoreCase("180"));


    public BooleanSetting randomCps = new BooleanSetting("randomCps", this, true);

    public ColorPickerSetting blockESPColor = new ColorPickerSetting("BlockESP Color", this, new Color(244, 0, 0, 255));

    private final NumberSetting delay = new NumberSetting("Delay", this, 12, 1, 500, 1);

    public List<Block> badBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
            Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane,
            Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore,
            Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest,
            Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore,
            Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate,
            Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
            Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook,
            Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine,
            Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser,
            Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall,
            Blocks.oak_fence, Blocks.stone_slab, Blocks.wooden_slab, Blocks.slime_block);

    private Object[] placementData;

    public boolean isAllowPlacement;

    public ScaffoldWalk() {
        this.invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder, Blocks.web);
    }

    @EventTarget
    public void onMotion(MoveEvent event) {

    }

    @EventTarget
    public void onRender3D(Render3DEvent2 event) {
        if (placeESP.getValue()) {
            if (placementData != null) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                MC.entityRenderer.setupCameraTransform(MC.timer.renderPartialTicks, 0);

                RenderUtil.instance.color(new Color(blockESPColor.ColorPickerC.getRed(), blockESPColor.ColorPickerC.getGreen(), blockESPColor.ColorPickerC.getBlue(), 122).getRGB());

                final BlockPos placeDir = (BlockPos) placementData[0];
                final EnumFacing face = (EnumFacing) placementData[1];

                double x1 = placeDir.getX() - RenderManager.renderPosX;
                double x2 = placeDir.getX() - RenderManager.renderPosX + 1;
                double y1 = placeDir.getY() - RenderManager.renderPosY;
                double y2 = placeDir.getY() - RenderManager.renderPosY + 1;
                double z1 = placeDir.getZ() - RenderManager.renderPosZ;
                double z2 = placeDir.getZ() - RenderManager.renderPosZ + 1;

                RenderUtil.glColor(new Color(blockESPColor.getColorPickerC().getRed(), blockESPColor.ColorPickerC.getGreen(), blockESPColor.getColorPickerC().getBlue(), 85).getRGB());
                RenderUtil.instance.drawBoundingBox(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();

                GlStateManager.resetColor();
            }
        }
    }

    @EventTarget
    public void onTick(EventTick eventTick) {
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        MC.thePlayer.setSprinting(!noSprint.getValue());
        if (stopOnEdge.getValue()) {
            if (allowPlacing())
                MC.thePlayer.motionX = MC.thePlayer.motionZ = 0;
        }
        if (event.getState() == UpdateState.PRE) {


            placementData = getPlacingPosition(MC.thePlayer.getPositionVector().getBlockPos().add(0, -1, 0), 1);
            isAllowPlacement = false;

            switch (rotationMode.getValue()) {
                case "Always": {
                    Vec3 blockSidePosition = getBlockSide((BlockPos) placementData[0], (EnumFacing) placementData[1]);
                    final float[] blockRotation = faceBlock(blockSidePosition, prediction.getValue(), 1);
                    rotations = new float[]{blockRotation[0], MathHelper.clamp_float(blockRotation[1], -90, 90)};
                    if (gcdFix.getValue())
                        rotations = applyMouseSensitivity(rotations[0], rotations[1], gcdFixA3.getValue());

                }
                break;

                case "180": {
                    Vec3 blockSidePosition = getBlockSide((BlockPos) placementData[0], (EnumFacing) placementData[1]);
                    final float[] blockRotation = faceBlock(blockSidePosition, prediction.getValue(), 1);

                    rotations[0] = MC.thePlayer.rotationYaw + 180;
                    rotations[1] = blockRotation[1];
                    if (gcdFix.getValue())
                        rotations = applyMouseSensitivity(rotations[0], rotations[1], gcdFixA3.getValue());

                    break;
                }

                case "AllowRotation": {
                    Vec3 blockSidePosition = getBlockSide((BlockPos) placementData[0], (EnumFacing) placementData[1]);
                    final float[] blockRotation = faceBlock(blockSidePosition, prediction.getValue(), 1);
                    if (allowPlacing()) {
                        rotations = blockRotation;
                        savedRotations = rotations;
                    } else {
                        rotations = savedRotations;
                    }
                    if (gcdFix.getValue())
                        rotations = applyMouseSensitivity(rotations[0], rotations[1], gcdFixA3.getValue());

                    break;
                }

            }

            if (!this.isAllowPlacement)
                randomFactor = new float[]{0, 0};
            else {
                randomFactor[0] = MathUtil.getRandom(-4, 7);
                randomFactor[1] = MathUtil.getRandom(0, 3);
            }
            if (!randomRotations.getValue())
                randomFactor = new float[]{0, 0};

        }
    }

    @EventTarget
    public void onRotation(RotationEvent e) {
        if (!rotationsValue.getValue()) return;

        final float fixDerpPitch = MathHelper.clamp_float(rotations[1], -90, 90);

        e.setYaw(rotations[0] + randomFactor[0]);
        e.setPitch(fixDerpPitch + randomFactor[1]);
        MC.thePlayer.renderYawOffset = rotations[0];

    }

    /**
     * Der tower movement flaggt für blocksmc, macht ihn aber nicht langsamer, wir machen eif nen no rotate
     *
     * @param event
     */

    @EventTarget
    public void onPacket(PacketEvent event) {
        final boolean towering = MC.gameSettings.keyBindSneak.pressed && !PlayerHook.isMoving() && tower.getValue();

        if (towering && towerMode.getValue().equalsIgnoreCase("BlocksMC")) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) event.getPacket();

                MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(playerPosLook.yaw, playerPosLook.pitch, MC.thePlayer.onGround));

                playerPosLook.yaw = MC.thePlayer.rotationYaw;
                playerPosLook.pitch = MC.thePlayer.rotationPitch;

            }
        }

    }

    @EventTarget
    public void onStrafe(EventStrafe e) {
        if (placementData == null) return;
        if (movementCorrection.getValue())
            StrafeUtil.customSilentMoveFlying(e, rotations[0]);
    }


    @EventTarget
    public void jump(JumpEvent event) {
        if (placementData == null) return;

        if (movementCorrection.getValue()) {
            event.setYaw(rotations[0]);
        }
    }


    @EventTarget
    public void onPlace(AttackingEvent e) {
        if (!hasBlockOnHotbar()) return;

        final boolean towering = MC.gameSettings.keyBindSneak.pressed && !PlayerHook.isMoving() && tower.getValue();

        if (towering) {
            this.isAllowPlacement = true; //Fix raycast setting issues

            switch (towerMode.getValue()) {
                case "Motion": {
                    PlayerHook.stop();
                    MC.thePlayer.motionY = .4;
                    break;
                }

                case "Verus": {
                    PlayerHook.stop();
                    MC.thePlayer.motionY = .5;
                    sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                    sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                    break;
                }

                case "BlocksMC": {
                    MC.timer.timerSpeed = 1F;
                    if (MC.thePlayer.onGround) {
                        MC.thePlayer.jump();
                        MC.timer.timerSpeed = 1.1F;
                    } else if (PlayerHook.getBlockRelativeToPlayer(0, -.8, 0) instanceof BlockAir) {
                        MC.thePlayer.motionY -= .4F;
                        MC.timer.timerSpeed = .9F;

                    }
                    PlayerHook.stop();
                    break;
                }

            }

        }

        currentSlot = MC.thePlayer.inventory.currentItem;

        this.slot = getBlockSlot();

        if (slot == -1) {
            fakeSlot = MC.thePlayer.inventory.currentItem;
        }
        if (slot == -1) {
            return;
        }
        fakeSlot = slot;

        MC.thePlayer.inventory.currentItem = slot;


        this.doPlacement();

        if (swing.getValue() && !rayTrace.getValue()) {
            switch (swingMode.getValue()) {
                case "Vanilla":
                    MC.thePlayer.swingItem();
                    break;
                case "Packet":
                    sendPacketUnlogged(new C0APacketAnimation());
                    break;
            }
        }


    }

    private boolean hasNeighbour(BlockPos pos) {
        return (pos.add(-1, 0, 0).getBlock() != Blocks.air || pos.add(1, 0, 0).getBlock() != Blocks.air || pos.add(0, 0, 1).getBlock() != Blocks.air || pos.add(0, 0, -1).getBlock() != Blocks.air || pos.add(0, -1, 0).getBlock() != Blocks.air);
    }

    @EventTarget
    public void onLook(EventLook e) {
        if (rayTrace.getValue()) {
            e.setRotations(rotations);
        }
    }

    private void doPlacement() {
        if (placementData == null) {
            placeDelay.doReset();
            return;
        }

        if (!hasBlockOnHotbar()) return;
        boolean allowClick;

        if (fakePreSwing.getValue()) {
            if ((placeDelayFakePlace.reachedTime(randomCps.getValue() ? MathUtil.getRandom(1, 40) + delay.getValueLong() : delay.getValueLong(), true))) {
                if (rayTrace.getValue())
                    MC.rightClickMouse();
                else
                    MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, MC.thePlayer.getHeldItem(), (BlockPos) placementData[0], (EnumFacing) placementData[1], blockDataToVec3((BlockPos) placementData[0], (EnumFacing) placementData[1]));

            }
        }

        double random = MathUtil.getRandomSin(1.0, delay.getValueFloat(), 500.0);
        if (randomCps.getValue())
            allowClick = placeDelay.reachedTime((long) (random));
        else {
            allowClick = placeDelay.reachedTime((long) (this.delay.getValueInt()));
        }


        if (rotationsValue.getValue() && rayTrace.getValue()) {
            isAllowPlacement = MC.objectMouseOver != null && MC.objectMouseOver.getBlockPos() != null && MC.objectMouseOver.sideHit == placementData[1] && MC.objectMouseOver.getBlockPos().equals(placementData[0]) && MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
        } else {
            isAllowPlacement = true;
        }


        if (sneak.getValue()) {
            if (!packetSneak.getValue())
                MC.gameSettings.keyBindSprint.pressed = false;
            else sendPacket(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }

        if (rayTrace.getValue()) {
            this.isAllowPlacement = false;
        }

        if (!MC.thePlayer.isUsingItem()) {
            if (!MC.playerController.getIsHittingBlock()) {
                MC.rightClickDelayTimer = 4;
                if (resetRightClickDelayTimer.getValue())
                    MC.rightClickDelayTimer = 0;
                MC.leftClickCounter = 0;
                isAllowPlacement = true;
            }
        }
        if (MC.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || MC.playerController.getIsHittingBlock() || !hasNeighbour(MC.thePlayer.getPositionVector().getBlockPos().add(0, -1, 0)) || MC.thePlayer.getPositionVector().getBlockPos().getBlock() != Blocks.air)
            isAllowPlacement = false;
        if (rayTrace.getValue()) {
            final BlockPos pos = (BlockPos) placementData[0];
            if (MC.objectMouseOver.hitVec.yCoord < pos.getBlock().getBlockBoundsMinY())
                isAllowPlacement = false;
        } else if (!rayTrace.getValue())
            isAllowPlacement = allowPlacing();

        if (MC.rightClickDelayTimer == 0 || isAllowPlacement) {
            if (!hasNeighbour(MC.thePlayer.getPositionVector().getBlockPos().add(0, -1, 0)) || MC.thePlayer.getPositionVector().getBlockPos().getBlock() != Blocks.air)
                return;
            if (allowClick && isAllowPlacement) {
                if (!rayTrace.getValue()) {
                    if (MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, MC.thePlayer.getHeldItem(), (BlockPos) placementData[0], (EnumFacing) placementData[1], blockDataToVec3((BlockPos) placementData[0], (EnumFacing) placementData[1]))) {
                        if (resetRightClickDelayTimer.getValue())
                            MC.rightClickDelayTimer = 0;
                        if (sneak.getValue()) {
                            if (!packetSneak.getValue())
                                MC.gameSettings.keyBindSprint.pressed = true;
                            else
                                sendPacket(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        }
                    }
                } else {

                    final MovingObjectPosition movingObjectPosition = rayCastUtil.rayCast(rotations[0], rotations[1], 4);
                    if (movingObjectPosition != null && movingObjectPosition.getBlockPos() != null) {
                        if (!MC.theWorld.isAirBlock(movingObjectPosition.getBlockPos())) {
                            MC.rightClickMouse(movingObjectPosition);
                            if (sneak.getValue()) {
                                if (!packetSneak.getValue())
                                    MC.gameSettings.keyBindSprint.pressed = true;
                                else
                                    sendPacket(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                            }

                        }
                    }
                }
            }
            placeDelay.doReset();
        }

        MC.thePlayer.inventory.currentItem = currentSlot;

    }

    private int getBlockSlot() {
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = MC.thePlayer.inventory.mainInventory[k];
            if (itemStack != null && isValid(itemStack) && itemStack.stackSize >= 1) {
                return k;
            }
        }
        return -1;
    }

    private boolean isValid(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock) {
            ItemBlock block = (ItemBlock) itemStack.getItem();
            return !badBlocks.contains(block.getBlock());
        }
        return false;
    }


    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_EVEN);
            return bd.doubleValue();
        }
    }


    public float[] applyMouseSensitivity(float yaw, float pitch, boolean a3) {
        float sensitivity = MC.gameSettings.mouseSensitivity;
        sensitivity = Math.max(0.001F, sensitivity);
        int deltaYaw = (int) ((yaw - Rotations.instance.yaw) / ((sensitivity * (sensitivity >= 0.5 ? sensitivity : 1) / 2)));
        int deltaPitch = (int) ((pitch - Rotations.instance.pitch) / ((sensitivity * (sensitivity >= 0.5 ? sensitivity : 1) / 2))) * -1;

        if (a3) {
            deltaYaw -= deltaYaw % 0.5 + 0.25;
            deltaPitch -= deltaPitch % 0.5 + 0.25;
        }

        float f = sensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8F;
        float f2 = (float) deltaYaw * f1;
        float f3 = (float) deltaPitch * f1;

        float endYaw = (float) ((double) Rotations.instance.yaw + (double) f2 * 0.15);
        float endPitch = (float) ((double) Rotations.instance.pitch - (double) f3 * 0.15);

        return new float[]{endYaw, endPitch};
    }

    private Vec3 blockDataToVec3(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
        double x = paramBlockPos.getX() + 0.5D;
        double y = paramBlockPos.getY() + 0.5D;
        double z = paramBlockPos.getZ() + 0.5D;
        x += paramEnumFacing.getFrontOffsetX() / 2.0D;
        y += paramEnumFacing.getFrontOffsetZ() / 2.0D;
        z += paramEnumFacing.getFrontOffsetY() / 2.0D;
        if (paramEnumFacing == EnumFacing.UP || paramEnumFacing == EnumFacing.DOWN) {
            x += this.randomNumber(-0.3, 0.3);
            z += this.randomNumber(-0.3, 0.3);
        } else {
            y += this.randomNumber(0.49, 0.5);
        }
        if (paramEnumFacing == EnumFacing.WEST || paramEnumFacing == EnumFacing.EAST) {
            z += this.randomNumber(-0.3, 0.3);
        }
        if (paramEnumFacing == EnumFacing.SOUTH || paramEnumFacing == EnumFacing.NORTH) {
            x += this.randomNumber(-0.3, 0.3);
        }
        return new Vec3(x, y, z);
    }

    private double randomNumber(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }


    public static ScaffoldWalk getInstance() {
        return ModuleManager.getInstance(ScaffoldWalk.class);
    }

    @Override
    public void onState() {
        if (MC.thePlayer == null) return;
        MC.timer.timerSpeed = 1F;
        placementData = null;
        MC.thePlayer.inventory.currentItem = currentSlot;
    }

    @Override
    public void onUndo() {
        if (MC.thePlayer == null) return;
        MC.timer.timerSpeed = 1F;
        MC.gameSettings.keyBindSprint.pressed = false;
        isAllowPlacement = false;
        MC.thePlayer.inventory.currentItem = currentSlot;

    }


    private boolean allowPlacing() {
        BlockPos pos1 = new BlockPos(MC.thePlayer.posX - 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ - 0.024D);
        BlockPos pos2 = new BlockPos(MC.thePlayer.posX - 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ + 0.024D);
        BlockPos pos3 = new BlockPos(MC.thePlayer.posX + 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ + 0.024D);
        BlockPos pos4 = new BlockPos(MC.thePlayer.posX + 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ - 0.024D);
        return (MC.thePlayer.worldObj.getBlockState(pos1).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(pos2).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(pos3).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(pos4).getBlock() == Blocks.air);
    }

    public boolean hasBlockOnHotbar() {
        for (int a = 0; a < 9; ++a) {
            if (MC.thePlayer.inventory.getStackInSlot(a) != null && MC.thePlayer.inventory.getStackInSlot(a).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }


    private Object[] getPlacingPosition(BlockPos pos, int expansion) {
        BlockPos blockPos = pos;
        EnumFacing facing = EnumFacing.UP;

        if (pos.add(0, 0, expansion).getBlock() != Blocks.air) {
            blockPos = pos.add(0, 0, expansion);
            facing = EnumFacing.NORTH;
        }
        if (pos.add(0, 0, -expansion).getBlock() != Blocks.air) {
            blockPos = pos.add(0, 0, -expansion);
            facing = EnumFacing.SOUTH;
        }
        if (pos.add(expansion, 0, 0).getBlock() != Blocks.air) {
            blockPos = pos.add(expansion, 0, 0);
            facing = EnumFacing.WEST;
        }
        if (pos.add(-expansion, 0, 0).getBlock() != Blocks.air) {
            blockPos = pos.add(-expansion, 0, 0);
            facing = EnumFacing.EAST;
        }
        if (pos.add(0, -expansion, 0).getBlock() != Blocks.air) {
            blockPos = pos.add(0, -expansion, 0);
            facing = EnumFacing.UP;
        }
        return new Object[]{blockPos, facing, new Vec3(0, 0, 0)};
    }

    private Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.NORTH) {
            return new Vec3(pos.getX(), pos.getY(), pos.getZ() - .5);
        }
        if (face == EnumFacing.EAST) {
            return new Vec3(pos.getX() + .5, pos.getY(), pos.getZ());
        }
        if (face == EnumFacing.SOUTH) {
            return new Vec3(pos.getX(), pos.getY(), pos.getZ() + .5);
        }
        if (face == EnumFacing.WEST) {
            return new Vec3(pos.getX() - .5, pos.getY(), pos.getZ());
        }
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    public float[] faceBlock(Vec3 vec3, boolean prediction, float predictionFactor) {
        final Vec3 playerPos = new Vec3(MC.thePlayer.posX + (prediction ? MC.thePlayer.motionX * predictionFactor : 0), MC.thePlayer.posY + (prediction ? MC.thePlayer.motionY * predictionFactor : 0), MC.thePlayer.posZ + (prediction ? MC.thePlayer.motionZ * predictionFactor : 0));

        final double diffX = vec3.xCoord + 0.5 - playerPos.xCoord;
        final double diffY = vec3.yCoord + 0.5 - (playerPos.yCoord + MC.thePlayer.getEyeHeight());
        final double diffZ = vec3.zCoord + 0.5 - playerPos.zCoord;

        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        double yaw = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        double pitch = -Math.toDegrees(Math.atan2(diffY, dist));
        yaw = MC.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_double(yaw - MC.thePlayer.rotationYaw);
        pitch = MC.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_double(pitch - MC.thePlayer.rotationPitch);
        return new float[]{(float) yaw, (float) pitch};
    }


}