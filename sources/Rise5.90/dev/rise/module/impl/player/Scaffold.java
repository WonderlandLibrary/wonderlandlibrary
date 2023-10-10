/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.player;

import dev.rise.event.impl.motion.PostMotionEvent;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.CanPlaceBlockEvent;
import dev.rise.event.impl.other.MoveButtonEvent;
import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.event.impl.render.BlurEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.font.CustomFont;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.movement.Speed;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.player.RotationUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.world.BlockUtil;
import dev.rise.util.world.EnumFacingOffset;
import dev.rise.viamcp.ViaMCP;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This module automatically puts a block under you to easily bridge effortlessly.
 */
@ModuleInfo(name = "Scaffold", description = "Places blocks under you", category = Category.PLAYER)
public final class Scaffold extends Module {

    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);
    private final ModeSetting rotations = new ModeSetting("Rotations", this, "Normal", "None", "Normal", "Simple", "Down", "Snap", "Bruteforce", "Pitch Abuse");
    private final ModeSetting tower = new ModeSetting("Tower", this, "None", "None", "Vanilla", "Slow", "Verus", "Intave", "Hypixel");
    private final ModeSetting movementFix = new ModeSetting("Movement Fix", this, "None", "None", "Yaw", "Hidden");
    private final ModeSetting sprint = new ModeSetting("Sprint", this, "Normal", "Normal", "Disabled", "Bypass", "Legit");
    private final ModeSetting blockCounter = new ModeSetting("Block Counter", this, "Normal", "Normal", "MC");

    private final NoteSetting generalSettings = new NoteSetting("General Settings", this);
    private final NumberSetting timer = new NumberSetting("Timer", this, 1, 0.1, 10, 0.1);
    private final NumberSetting towerTimer = new NumberSetting("Tower Timer", this, 1, 0.1, 10, 0.1);
    private final BooleanSetting downwards = new BooleanSetting("Downwards", this, true);
    private final BooleanSetting safewalk = new BooleanSetting("Safe Walk", this, true);
    private final BooleanSetting strafe = new BooleanSetting("Strafe", this, false);
    private final BooleanSetting sameY = new BooleanSetting("Same Y", this, false);
    private final BooleanSetting swing = new BooleanSetting("Swing", this, true);

    private final NoteSetting bypassSettings = new NoteSetting("Bypass Settings", this);
    private final NumberSetting range = new NumberSetting("Range", this, 3, 1, 6, 0.5);
    private final BooleanSetting randomiseRotationSpeedOnEnable = new BooleanSetting("Randomise Rotation Speed On Enable", this, false);
    private final NumberSetting rotationSpeed = new NumberSetting("Rotation Speed", this, 50, 5, 360, 5);
    private final NumberSetting randomisation = new NumberSetting("Randomisation", this, 1, 0, 6, 0.1);
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", this, 0, 0, 5, 0.1);
    private final BooleanSetting randomisePlaceDelay = new BooleanSetting("Randomise Place Delay", this, false);
    private final NumberSetting speedMultiplier = new NumberSetting("Speed Multiplier", this, 1, 0, 2, 0.05);
    private final NumberSetting eagle = new NumberSetting("Eagle", this, 4, 0, 15, 1, "15-Never", "0-Packet");
    private final BooleanSetting ignoreSpeed = new BooleanSetting("Ignore Speed", this, false);
    private final BooleanSetting towerMove = new BooleanSetting("Tower Move", this, true);
    private final ModeSetting rayCast = new ModeSetting("Ray Cast", this, "Normal", "Normal", "Strict", "Off");
    private final ModeSetting placeOn = new ModeSetting("Place on", this, "Legit", "Legit", "Post");
    private final BooleanSetting dragClick = new BooleanSetting("Drag Click", this, false);
    private final BooleanSetting jitter = new BooleanSetting("Jitter", this, false);
    private final BooleanSetting telly = new BooleanSetting("Telly", this, false);
    private final BooleanSetting hideJumps = new BooleanSetting("Hide Jumps", this, false);

    private Vec3 targetBlock;
    private List<Vec3> placePossibilities = new ArrayList<>();
    private EnumFacingOffset enumFacing;
    private BlockPos blockFace;

    private float targetYaw, targetPitch, yaw, lastYaw, lastPitch;
    public static float pitch;

    private boolean lastGround;
    private int blockCount;
    private int ticksOnAir;
    private double startY;
    private int slot;
    private int offGroundTicks;
    private int blocksPlaced;
    private boolean sneaking;
    private boolean shiftPressed;
    TimeUtil timer3 = new TimeUtil();

    @Override
    public void onUpdateAlwaysInGui() {
        towerMove.hidden = tower.is("None") || tower.is("Slow") || tower.is("Hypixel");

        towerTimer.hidden = tower.is("None");
    }

    @Override
    protected void onEnable() {
        slot = mc.thePlayer.inventory.currentItem;

        yaw = lastYaw = mc.thePlayer.rotationYaw;
        pitch = lastPitch = mc.thePlayer.rotationPitch;

        if (rotations.is("Pitch Abuse")) {
            targetYaw = mc.thePlayer.rotationYaw;
            targetPitch = 94;
        } else {
            targetYaw = mc.thePlayer.rotationYaw - 180;
            targetPitch = 90;
        }

        startY = mc.thePlayer.posY;
        targetBlock = null;
        placePossibilities.clear();

        if (randomiseRotationSpeedOnEnable.isEnabled()) {
            rotationSpeed.setValue(50 + (85 - 50) * Math.random());
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof S2FPacketSetSlot)
            event.setCancelled(true);
    }

    @Override
    protected void onDisable() {
        if (slot != mc.thePlayer.inventory.currentItem)
            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

        EntityPlayer.movementYaw = null;
        mc.timer.timerSpeed = 1;
        mc.gameSettings.keyBindSneak.setKeyPressed(false);
        EntityPlayer.enableCameraYOffset = false;

        if (sneaking) {
            sneaking = false;
            PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        if (movementFix.is("Hidden")) {
            event.setCancelled(true);
            silentRotationStrafe(event, yaw);
        }
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (!rotations.is("None")) {
            if (enumFacing == null || blockFace == null)
                return;

            this.calculateRotations();
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir)
            ticksOnAir++;
        else
            ticksOnAir = 0;

        if (mc.thePlayer.onGround) {
            offGroundTicks = 0;
        } else
            offGroundTicks++;

        EntityPlayer.enableCameraYOffset = false;

        if (mc.thePlayer.posY > startY && hideJumps.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            EntityPlayer.enableCameraYOffset = true;
            EntityPlayer.cameraYPosition = startY;
        }

        int blocks = 0;

        if (strafe.isEnabled()) MoveUtil.strafe();

        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final Block block = ((ItemBlock) itemStack.getItem()).getBlock();
                if (block.isFullCube() && !BlockUtil.BLOCK_BLACKLIST.contains(block))
                    blocks += itemStack.getStackSize();
            }
        }

        blockCount = blocks;

        if (mc.thePlayer.onGround || (mc.gameSettings.keyBindJump.isKeyDown() && !sameY.isEnabled()))
            startY = mc.thePlayer.posY;

        final int blockSlot = BlockUtil.findBlock() - 36;

        if (blockSlot < 0 || blockSlot > 9)
            return;

        switch (sprint.getMode()) {
            case "Disabled": {
                mc.gameSettings.keyBindSprint.setKeyPressed(false);
                mc.thePlayer.setSprinting(false);
                break;
            }

            case "Bypass": {
                mc.thePlayer.setSprinting(false);
                break;
            }

            case "Legit": {
                if (Math.abs(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) - MathHelper.wrapAngleTo180_float(yaw)) > 90) {
                    mc.gameSettings.keyBindSprint.setKeyPressed(false);
                    mc.thePlayer.setSprinting(false);
                }
                break;
            }
        }

        placePossibilities = getPlacePossibilities();

        if (placePossibilities.isEmpty())
            return;

        placePossibilities.sort(Comparator.comparingDouble(vec3 -> mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord + 1, vec3.zCoord)));

        targetBlock = placePossibilities.get(0);

        enumFacing = getEnumFacing(targetBlock);

        if (downwards.isEnabled() && mc.gameSettings.keyBindSneak.isKeyDown() && mc.thePlayer.onGround && enumFacing != null && enumFacing.getEnumFacing() != null)
            enumFacing.enumFacing = EnumFacing.DOWN;

        if (sameY.isEnabled() && mc.thePlayer.posY < startY) startY = mc.thePlayer.posY;

        if (enumFacing == null)
            return;

        final BlockPos position = new BlockPos(targetBlock.xCoord, targetBlock.yCoord, targetBlock.zCoord);

        blockFace = position.add(enumFacing.getOffset().xCoord, enumFacing.getOffset().yCoord, enumFacing.getOffset().zCoord);

        if (blockFace == null)
            return;

        shiftPressed = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround && blocksPlaced == eagle.getValue() && eagle.getValue() != 15 && eagle.getValue() != 0;

        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && eagle.getValue() == 0) {
            if (!sneaking) {
                sneaking = true;
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
        } else {
            if (sneaking) {
                sneaking = false;
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }

        if (blocksPlaced > eagle.getValue()) blocksPlaced = 1;

        if (!rotations.is("None")) {
            event.setYaw(yaw);
            event.setPitch(pitch);

            mc.thePlayer.renderYawOffset = yaw;
            mc.thePlayer.rotationYawHead = yaw;

//            mc.thePlayer.rotationYaw = yaw;
//            mc.thePlayer.rotationPitch = pitch;

            lastYaw = yaw;
            lastPitch = pitch;
        } else {
            yaw = mc.thePlayer.rotationYaw;
            pitch = mc.thePlayer.rotationPitch;
        }

        if (placePossibilities.isEmpty() || targetBlock == null || enumFacing == null || blockFace == null || slot < 0 || slot > 9)
            return;

        mc.timer.timerSpeed = (float) timer.getValue();

        if (mc.gameSettings.keyBindJump.isKeyDown() && (towerMove.isEnabled() && !(tower.is("Slow") || tower.is("Hypixel")) || !MoveUtil.isMoving()) && (!(PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir) || tower.is("Intave") || tower.is("Slow")) && !this.getModule(Speed.class).isEnabled()) {
            mc.timer.timerSpeed = (float) towerTimer.getValue();

            switch (tower.getMode()) {
                case "Vanilla": {
                    mc.thePlayer.motionY = 0.42F;
                    break;
                }

                case "Hypixel":
                    if (mc.thePlayer.onGround)
                        mc.thePlayer.motionY = 0.4F;

                    if (offGroundTicks == 3) mc.thePlayer.motionY -= 0.02;
                    break;

                case "Slow": {
                    if (mc.thePlayer.onGround)
                        mc.thePlayer.motionY = 0.4F;
                    else if (PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir)
                        mc.thePlayer.motionY -= 0.4F;

                    MoveUtil.stop();
                    break;
                }

                case "Verus": {
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.motionY = 0.42F;
                    }
                    break;
                }

                case "Intave": {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.40444491418477924;
                    }

                    if (offGroundTicks == 5) {
                        mc.thePlayer.motionY = MoveUtil.getPredictedMotionY(mc.thePlayer.motionY);
                    }
                    break;
                }
            }
        }

        final double baseSpeed = getBaseSpeed();
        final double speedMultiplier = this.speedMultiplier.getValue();
        if (Math.abs(speedMultiplier - 1.0) > 1E-4 && mc.thePlayer.onGround && !(mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() > 2 - 1) && baseSpeed != 0) {
            MoveUtil.strafe(baseSpeed * speedMultiplier);
        }

        lastGround = mc.thePlayer.onGround;
    }

    public double getBaseSpeed() {
        if (mc.gameSettings.keyBindSprint.isKeyDown()) {
            if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && !ignoreSpeed.isEnabled()) {
                if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                    return 0.18386012061481244;
                } else {
                    return 0.21450346015841276;
                }
            } else {
                return 0.15321676228437875;
            }
        } else {
            if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && !ignoreSpeed.isEnabled()) {
                if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                    return 0.14143085686761;
                } else {
                    return 0.16500264553372018;
                }
            } else {
                return 0.11785905094607611;
            }
        }
    }

    public float[] calculateRotations() {
        if (((ticksOnAir > placeDelay.getValue()) || rotations.is("Down") || rotations.is("Snap")) || movementFix.is("Yaw")) {
            final float[] rotations = BlockUtil.getDirectionToBlock(blockFace.getX(), blockFace.getY(), blockFace.getZ(), enumFacing.getEnumFacing());

            if (movementFix.is("Yaw")) {
                targetYaw = (float) (MoveUtil.getDirectionWrappedTo90() * (180 / Math.PI) - 180);
                targetPitch = rotations[1];

                switch (this.rotations.getMode()) {
                    case "Snap": {
                        if (ticksOnAir > placeDelay.getValue()) {
                            targetYaw = (float) (MoveUtil.getDirectionWrappedTo90() * (180 / Math.PI) - 180);
                            targetPitch = rotations[1];
                        } else {
                            targetYaw = (float) (mc.thePlayer.rotationYaw + Math.random());
                            targetPitch = mc.thePlayer.rotationPitch;
                        }
                        break;
                    }
                }
            } else {
                switch (this.rotations.getMode()) {
                    case "Normal": {
                        targetYaw = rotations[0];
                        targetPitch = rotations[1];
                        break;
                    }

                    case "Simple": {
                        float yaw = 0;

                        switch (enumFacing.getEnumFacing()) {
                            case SOUTH: {
                                yaw = 180;
                                break;
                            }

                            case EAST: {
                                yaw = 90;
                                break;
                            }

                            case WEST: {
                                yaw = -90;
                                break;
                            }
                        }

                        targetYaw = yaw;
                        targetPitch = 90;
                        break;
                    }

                    case "Down": {

                        float rotationYaw = mc.thePlayer.rotationYaw;

                        if (mc.thePlayer.moveForward < 0 && mc.thePlayer.moveStrafing == 0) {
                            rotationYaw += 180;
                        }

                        if (mc.thePlayer.moveStrafing > 0) {
                            rotationYaw -= 90;
                        }

                        if (mc.thePlayer.moveStrafing < 0) {
                            rotationYaw += 90;
                        }

                        this.yaw = (float) (Math.toRadians(rotationYaw) * (180 / Math.PI) - 180 + Math.random());
                        this.pitch = (float) (87 + Math.random());

                        break;
                    }

                    case "Snap": {
                        targetYaw = rotations[0];
                        targetPitch = rotations[1];

                        if (ticksOnAir <= placeDelay.getValue()) {
                            targetYaw = (float) (mc.thePlayer.rotationYaw + Math.random());
                            targetPitch = mc.thePlayer.rotationPitch;
                        }
                        break;
                    }

                    case "Bruteforce": {
                        boolean found = false;
                        for (float yaw = mc.thePlayer.rotationYaw - 180; yaw <= mc.thePlayer.rotationYaw + 360 - 180 && !found; yaw += 45) {
                            for (float pitch = 90; pitch > 30 && !found; pitch -= 1) {
                                if (BlockUtil.lookingAtBlock(blockFace, yaw, pitch, enumFacing.getEnumFacing(), rayCast.is("Strict"))) {
                                    targetYaw = yaw;
                                    targetPitch = pitch;
                                    found = true;
                                }
                            }
                        }

                        if (!found) {
                            targetYaw = (float) (rotations[0] + (Math.random() - 0.5) * 4);
                            targetPitch = (float) (rotations[1] + (Math.random() - 0.5) * 4);
                        }
                        break;
                    }

                    case "Pitch Abuse":
                        boolean found = false;

                        for (float yaw = mc.thePlayer.rotationYaw; yaw <= mc.thePlayer.rotationYaw + 360 && !found; yaw += 45) {
                            for (float pitch = 90; pitch < 180 && !found; pitch += 1) {
                                if (BlockUtil.lookingAtBlock(blockFace, yaw, pitch, enumFacing.getEnumFacing(), rayCast.is("Strict"))) {
                                    targetYaw = yaw;
                                    targetPitch = pitch;
                                    found = true;
                                }
                            }
                        }

                        for (yaw = mc.thePlayer.rotationYaw - 180; yaw <= mc.thePlayer.rotationYaw + 360 - 180 && !found; yaw += 45) {
                            for (float pitch = 90; pitch > 30 && !found; pitch -= 1) {
                                if (BlockUtil.lookingAtBlock(blockFace, yaw, pitch, enumFacing.getEnumFacing(), rayCast.is("Strict"))) {
                                    targetYaw = yaw;
                                    targetPitch = pitch;
                                    found = true;
                                }
                            }
                        }

                        if (!found) {
                            targetYaw = mc.thePlayer.rotationYaw;
                            targetPitch = 94;
                        }
                        break;
                }
            }
        }

        final int fps = (int) (Minecraft.getDebugFPS() / 20.0F);

        final float rotationSpeed = (float) (this.rotationSpeed.getValue() + Math.random() * 20) * 6 / fps;

        final float deltaYaw = (((targetYaw - lastYaw) + 540) % 360) - 180;
        final float deltaPitch = targetPitch - lastPitch;

        final float distanceYaw = MathHelper.clamp_float(deltaYaw, -rotationSpeed, rotationSpeed);
        final float distancePitch = MathHelper.clamp_float(deltaPitch, -rotationSpeed, rotationSpeed);

        yaw = lastYaw + distanceYaw;
        pitch = lastPitch + distancePitch;

        targetPitch += (float) (this.randomisation.getValue() * (Math.random() - 0.5) * 3);
        targetYaw += (float) (this.randomisation.getValue() * (Math.random() - 0.5) * 3);

        if (rotationSpeed >= 355) {
            yaw = targetYaw;
            pitch = targetPitch;
        }

        final float[] currentRotations = new float[]{yaw, pitch};
        final float[] lastRotations = new float[]{lastYaw, lastPitch};

        final float[] fixedRotations = RotationUtil.getFixedRotation(currentRotations, lastRotations);

        yaw = fixedRotations[0];
        pitch = fixedRotations[1];

        if (!rotations.is("Pitch Abuse")) pitch = MathHelper.clamp_float(pitch, -90, 90);

        return new float[]{yaw, pitch};
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (blockCount == 0)
            return;

        final ScaledResolution sr = event.getScaledResolution();
        final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
        Color color;

        if (blockCount <= 63) {
            color = Color.RED;
        } else color = Color.GREEN;


        if (blockCounter.is("MC")) {
            final int height = sr.getScaledHeight() / 2;

            mc.fontRendererObj.drawStringWithShadow(String.valueOf(blockCount), sr.getScaledWidth() / 2F + 1F, height + 9, color.getRGB());

            if (itemStack != null) {
                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, sr.getScaledWidth() / 2F - 17F, height + 4);
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            } else
                CustomFont.drawCenteredString("?", sr.getScaledWidth() / 2F + 0.5F, height + 6, -1);
        }


        if (blockCounter.is("Normal")) {
            final int height = sr.getScaledHeight() - 90;
            RenderUtil.roundedRect((sr.getScaledWidth() / 2F) - 15, height, 30, 30, 6, new Color(0, 0, 0, 80));

            CustomFont.drawCenteredString(String.valueOf(blockCount), sr.getScaledWidth() / 2F, height + 19, -1);

            if (itemStack != null) {
                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, sr.getScaledWidth() / 2F - 8F, height + 2);
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            } else
                CustomFont.drawCenteredString("?", sr.getScaledWidth() / 2F + 0.5F, height + 6, -1);
        }
    }

    @Override
    public void onBlur(final BlurEvent event) {
        if (blockCounter.is("Normal")) {
            final ScaledResolution sr = new ScaledResolution(mc);
            final int height = sr.getScaledHeight() - 90;

            RenderUtil.roundedRect((sr.getScaledWidth() / 2F) - 15, height, 30, 30, 6, new Color(0, 0, 0, 80));
        }
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {
        if (jitter.isEnabled()) {
            if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) {
                if (mc.thePlayer.ticksExisted % 2 == 0)
                    event.setLeft(true);
                else
                    event.setRight(true);
            }

            if (mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                if (mc.thePlayer.ticksExisted % 2 == 0)
                    event.setForward(true);
                else
                    event.setBackward(true);
            }
        }

        if (telly.isEnabled() && MoveUtil.isMoving() && mc.thePlayer.onGround) {
            event.setJump(true);
        }

        event.setSneak(shiftPressed);
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof C09PacketHeldItemChange)
            event.setCancelled(true);

        if (p instanceof C08PacketPlayerBlockPlacement && !MoveUtil.isMoving() && mc.gameSettings.keyBindJump.isKeyDown()) {
            switch (tower.getMode()) {
                case "Hypixel":

                    mc.thePlayer.motionY = MoveUtil.getPredictedMotionY(mc.thePlayer.motionY);

                    break;
            }
        }
    }

    @Override
    public void onCanPlaceBlockEvent(final CanPlaceBlockEvent event) {
        if (placeOn.is("Legit")) {
            this.placeBlock();
        }
    }

    @Override
    public void onPostMotion(final PostMotionEvent event) {
        if (placeOn.is("Post")) {
            this.placeBlock();
        }
    }

    public void placeBlock() {
        final int blockSlot = BlockUtil.findBlock() - 36;

        if (blockSlot < 0 || blockSlot > 9)
            return;

        boolean switchedSlot = false;
        if (slot != blockSlot) {
            slot = blockSlot;
            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(slot));
            switchedSlot = true;
        }

        if (placePossibilities.isEmpty() || targetBlock == null || enumFacing == null || blockFace == null || slot < 0 || slot > 9)
            return;

        final MovingObjectPosition movingObjectPosition = mc.thePlayer.rayTraceCustom(mc.playerController.getBlockReachDistance(), mc.timer.renderPartialTicks, yaw, pitch);

        if (movingObjectPosition == null)
            return;

        final boolean sameY = (this.sameY.isEnabled() || (this.getModule(Speed.class).isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown())) && MoveUtil.isMoving();
        if (((int) startY - 1 != (int) targetBlock.yCoord && sameY))
            return;

        final Vec3 hitVec = movingObjectPosition.hitVec;
        final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(slot + 36).getStack();

        if (!switchedSlot && (offGroundTicks == 0 || (mc.thePlayer.fallDistance > 0 && offGroundTicks <= 3) || offGroundTicks > 5) && ticksOnAir > placeDelay.getValue() + (randomisePlaceDelay.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown() ? Math.random() * 3 : 0) && ((BlockUtil.lookingAtBlock(blockFace, yaw, pitch, enumFacing.getEnumFacing(), rayCast.is("Strict"))) || this.rayCast.is("Off"))) {
            if (!BlockUtil.lookingAtBlock(blockFace, yaw, pitch, enumFacing.getEnumFacing(), rayCast.is("Strict"))) {
                hitVec.yCoord = Math.random() + blockFace.getY();
                hitVec.zCoord = Math.random() + blockFace.getZ();
                hitVec.xCoord = Math.random() + blockFace.getX();
            }

            if (ViaMCP.getInstance().getVersion() > 47) {
                if (swing.isEnabled())
                    mc.thePlayer.swingItem();
                else
                    PacketUtil.sendPacket(new C0APacketAnimation());
            }

            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, item, blockFace, enumFacing.getEnumFacing(), hitVec);

            if (ViaMCP.getInstance().getVersion() <= 47) {
                if (swing.isEnabled())
                    mc.thePlayer.swingItem();
                else
                    PacketUtil.sendPacket(new C0APacketAnimation());
            }

            blocksPlaced++;
        } else if (dragClick.isEnabled() && Math.random() > 0.5)
            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(item));
    }

    public static void silentRotationStrafe(final StrafeEvent event, final float yaw) {
        final int dif = (int) ((MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw - yaw - 23.5F - 135.0F) + 180.0F) / 45.0F);
        final float strafe = event.getStrafe();
        final float forward = event.getForward();
        final float friction = event.getFriction();
        float calcForward = 0.0F;
        float calcStrafe = 0.0F;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }

            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }

            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }

            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }

            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }

            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }

            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }

            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }

        if (calcForward > 1.0F || (calcForward < 0.9F && calcForward > 0.3F) || calcForward < -1.0F || (calcForward > -0.9F && calcForward < -0.3F))
            calcForward *= 0.5F;

        if (calcStrafe > 1.0F || (calcStrafe < 0.9F && calcStrafe > 0.3F) || calcStrafe < -1.0F || (calcStrafe > -0.9F && calcStrafe < -0.3F))
            calcStrafe *= 0.5F;

        float d;
        if ((d = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4F) {
            if ((d = MathHelper.sqrt_float(d)) < 1.0F) {
                d = 1.0F;
            }
            d = friction / d;
            final float yawSin = MathHelper.sin((float) (yaw * Math.PI / 180.0));
            final float yawCos = MathHelper.cos((float) (yaw * Math.PI / 180.0));
            mc.thePlayer.motionX += (calcStrafe *= d) * yawCos - (calcForward *= d) * yawSin;
            mc.thePlayer.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }

    private EnumFacingOffset getEnumFacing(final Vec3 position) {
        for (int x2 = -1; x2 <= 1; x2 += 2) {
            if (!(PlayerUtil.getBlock(position.xCoord + x2, position.yCoord, position.zCoord) instanceof BlockAir)) {
                if (x2 > 0) {
                    return new EnumFacingOffset(EnumFacing.WEST, new Vec3(x2, 0, 0));
                } else {
                    return new EnumFacingOffset(EnumFacing.EAST, new Vec3(x2, 0, 0));
                }
            }
        }

        for (int y2 = -1; y2 <= 1; y2 += 2) {
            if (!(PlayerUtil.getBlock(position.xCoord, position.yCoord + y2, position.zCoord) instanceof BlockAir)) {
                if (y2 < 0) {
                    return new EnumFacingOffset(EnumFacing.UP, new Vec3(0, y2, 0));
                }
            }
        }

        for (int z2 = -1; z2 <= 1; z2 += 2) {
            if (!(PlayerUtil.getBlock(position.xCoord, position.yCoord, position.zCoord + z2) instanceof BlockAir)) {
                if (z2 < 0) {
                    return new EnumFacingOffset(EnumFacing.SOUTH, new Vec3(0, 0, z2));
                } else {
                    return new EnumFacingOffset(EnumFacing.NORTH, new Vec3(0, 0, z2));
                }
            }
        }

        return null;
    }


    //This methods purpose is to get block placement possibilities, blocks are 1 unit thick so please don't change it to 0.5 it causes bugs
    private List<Vec3> getPlacePossibilities() {
        final List<Vec3> possibilities = new ArrayList<>();
        final int range = (int) Math.ceil(this.range.getValue());

        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = PlayerUtil.getBlockRelativeToPlayer(x, y, z);

                    if (!(block instanceof BlockAir)) {
                        for (int x2 = -1; x2 <= 1; x2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x + x2, mc.thePlayer.posY + y, mc.thePlayer.posZ + z));

                        for (int y2 = -1; y2 <= 1; y2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y + y2, mc.thePlayer.posZ + z));

                        for (int z2 = -1; z2 <= 1; z2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z + z2));
                    }
                }
            }
        }

        possibilities.removeIf(vec3 -> !(PlayerUtil.getBlock(vec3.xCoord, vec3.yCoord, vec3.zCoord) instanceof BlockAir) || (mc.thePlayer.posX == vec3.xCoord && mc.thePlayer.posY + 1 == vec3.yCoord && mc.thePlayer.posZ == vec3.zCoord));

        return possibilities;
    }
}