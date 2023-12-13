package koks.module.world;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.font.Fonts;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.*;
import koks.event.*;
import koks.event.DirectionSprintCheckEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Scaffold", description = "Its place blocks under you", category = Module.Category.WORLD)
public class Scaffold extends Module implements Module.Unsafe, Module.AlwaysEvent {

    //BUILD
    @Value(name = "Delay", minimum = 0, maximum = 500)
    int delay = 0;

    @Value(name = "MaxDelay", displayName = "Max Delay", minimum = 0, maximum = 500)
    int maxDelay = 10;

    @Value(name = "RandomizeDelay", displayName = "Randomize")
    boolean randomizeDelay = false;

    @Value(name = "CalculateDelayAfterBuild", displayName = "Calculate delay after build")
    boolean calculateDelayAfterBuild = false;

    @Value(name = "Smooth Delay")
    boolean smoothDelay = false;

    @Value(name = "Smooth Delay-Delay-Speed", displayName = "Delay Speed", minimum = 0.1, maximum = 2)
    double smoothDelaySpeed = 0.5;

    @Value(name = "Smooth Delay-RandomStrength", displayName = "Random Strength", minimum = 0.1, maximum = 0.9)
    double smoothDelayRandomStrength = 0.85;

    @Value(name = "Smooth Delay-Randomizing", displayName = "Randomizing")
    boolean smoothDelayRandomizing = true;

    @Value(name = "Boost Mode", modes = {"None", "When Place", "When Air"})
    String boostMode = "None";

    @Value(name = "Motion-Speed", displayName = "Motion", minimum = 0, maximum = 5)
    double motionSpeed = 1;

    @Value(name = "Blocks", minimum = 0, maximum = 10)
    int blocks = 0;

    @Value(name = "Diagonal")
    boolean diagonal = false;

    @Value(name = "UpScaffold")
    boolean upScaffold = true;

    @Value(name = "ResetRightClickDelayTime")
    boolean resetRightClickDelayTimer = false;

    @Value(name = "CanUp-Check")
    boolean canUpCheck = false;

    @Value(name = "DownScaffold")
    boolean downScaffold = false;

    @Value(name = "WorldHeightCheck")
    boolean worldHeightCheck = true;

    /*@Value(name = "SafeMode")
    boolean safeMode = false;*/

    @Value(name = "Tower")
    boolean tower = false;

    @Value(name = "Tower-Mode", displayName = "Mode", modes = {"Motion", "CubeCraft"})
    String towerMode = "Motion";

    @Value(name = "Tower-Motion", displayName = "Motion", minimum = 0.01, maximum = 1)
    double towerMotion = 0.42;

    @Value(name = "Tower-VerusDisabler", displayName = "Verus Disabler")
    boolean towerVerusDisabler = false;

    @Value(name = "Tower-WhileMoving", displayName = "WhileMoving")
    boolean towerWhileMoving = false;

    @Value(name = "No Speed Effect")
    boolean noSpeedEffect = false;

    @Value(name = "Expand")
    boolean expand = false;

    @Value(name = "Expand Length", minimum = 1, maximum = 5)
    int expandLength = 1;

    @Value(name = "All Direction Expand", displayName = "All Direction")
    boolean allDirectionExpand = true;

    @Value(name = "Silent")
    boolean silent = true;

    @Value(name = "Delayed back switch")
    boolean delayedBackSwitch = true;

    @Value(name = "Switch Blocks")
    boolean switchBlocks = false;

    @Value(name = "Switch Delay", displayName = "Delay", minimum = 0, maximum = 1000)
    int switchDelay = 0;

    @Value(name = "SameY")
    boolean sameY = false;

    @Value(name = "AutoJump")
    boolean autoJump = false;

    @Value(name = "Jump-Motion", minimum = 0, maximum = 1)
    double jumpMotion = 0.42;

    @Value(name = "Speed Boost", minimum = 0.2, maximum = 0.6)
    double speedBoost = 0.2;

    @Value(name = "NoSwing")
    boolean noSwing = false;

    @Value(name = "NoSwing-Mode", displayName = "Mode", modes = {"Vanilla", "Packet"})
    String noSwingMode = "Vanilla";

    @Value(name = "NecessaryPlacement")
    boolean necessaryPlacement = false;

    @Value(name = "Necessary Chance", minimum = 0, maximum = 100)
    int necessaryChance = 100;

    @Value(name = "BlockCheck")
    boolean blockCheck = false;

    @Value(name = "AllowAir")
    boolean allowAir = false;

    @Value(name = "AutomaticVector")
    boolean automaticVector = false;

    @Value(name = "FacingCheck")
    boolean facingCheck = false;

    //MOVEMENT
    @Value(name = "MoveFix")
    boolean moveFix = false;

    @Value(name = "Jump Fix")
    boolean jumpFix = true;

    @Value(name = "All Direction Jump")
    boolean allDirectionJump = false;

    @Value(name = "ShouldYaw")
    boolean shouldYaw = true;

    @Value(name = "SafeWalk")
    boolean safeWalk = false;

    @Value(name = "OnlyGround")
    boolean onlyGround = true;

    @Value(name = "Sprint")
    boolean sprint = false;

    @Value(name = "Sprint-Mode", displayName = "Mode", modes = {"Normal", "OnlyPost", "NoPacket"})
    String sprintMode = "Normal";

    @Value(name = "All Direction Sprint")
    boolean allDirectionSprint = false;

    @Value(name = "Sneak")
    boolean sneak = false;

    @Value(name = "SpoofSneak")
    boolean spoofSneak = false;

    @Value(name = "Sneak after...", minimum = 1, maximum = 10)
    int sneakAfter = 1;

    //RAYCAST
    @Value(name = "RayCast")
    boolean rayCast = false;

    //ROTATION
    @Value(name = "Rotations")
    boolean rotations = true;

    @Value(name = "Clamp Yaw")
    boolean clampYaw = false;

    @Value(name = "RandomAim")
    boolean randomAim = false;

    @Value(name = "ResetRotation")
    boolean resetRotation = false;

    @Value(name = "Reset-Mode", displayName = "Mode", modes = {"Silent", "Visible"})
    String resetMode = "Silent";

    @Value(name = "A3Fix")
    boolean a3Fix = false;

    @Value(name = "Prediction")
    boolean prediction = true;

    @Value(name = "AlwaysRotate")
    boolean alwaysRotate = true;

    /*@Value(name = "BestVector")
    boolean bestVector = false;*/

    @Value(name = "Necessary Rotations")
    boolean necessaryRotations = false;

    @Value(name = "Rotate in Air")
    boolean rotateInAir = false;

    @Value(name = "MouseFix")
    boolean mouseFix = true;

    @Value(name = "Simple Rotations")
    boolean simpleRotations = false;

    @Value(name = "DynamicYaw")
    boolean dynamicYaw = true;

    @Value(name = "Static Pitch")
    boolean staticPitch = false;

    @Value(name = "Ignore Yaw")
    boolean ignoreYaw = false;

    @Value(name = "Pitch", minimum = 70, maximum = 90)
    double pitch = 82;

    @Value(name = "RandomizePitch")
    boolean randomizePitch = false;

    int sneakCount, silentSlot = -1, startY;
    long currentDelay;

    boolean canBuild, hasSilent;

    EnumFacing enumFacing;
    ItemStack itemStack;

    boolean fakeSneak;

    int currentBlocks;
    float curYaw, curPitch;

    private final List<Block> blackList;
    private final List<Integer> switchedSlots = new ArrayList<>();

    private final TimeHelper timeHelper = new TimeHelper();
    private final TimeHelper switchTimer = new TimeHelper();
    private final TimeHelper itemSwitchTimer = new TimeHelper();

    public Scaffold() {
        this.blackList = Arrays.asList(Blocks.waterlily, Blocks.heavy_weighted_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.red_flower, Blocks.yellow_flower, Blocks.crafting_table, Blocks.chest, Blocks.enchanting_table, Blocks.anvil, Blocks.sand, Blocks.gravel, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.ice, Blocks.packed_ice, Blocks.cobblestone_wall, Blocks.water, Blocks.lava, Blocks.web, Blocks.sapling, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.tnt, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.tallgrass, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.ladder, Blocks.torch, Blocks.stone_button, Blocks.wooden_button, Blocks.redstone_torch, Blocks.redstone_wire, Blocks.furnace, Blocks.cactus, Blocks.oak_fence, Blocks.acacia_fence, Blocks.nether_brick_fence, Blocks.birch_fence, Blocks.dark_oak_fence, Blocks.jungle_fence, Blocks.oak_fence, Blocks.acacia_fence_gate, Blocks.snow_layer, Blocks.trapdoor, Blocks.ender_chest, Blocks.trapped_chest, Blocks.beacon, Blocks.hopper, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.carpet, Blocks.noteblock);
    }

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        if (name.contains("-") && name.split("-")[0].equalsIgnoreCase("Smooth Delay")) {
            return smoothDelay;
        }
        return switch (name) {
            case "Motion-Speed" -> boostMode.equalsIgnoreCase("When Place") || boostMode.equalsIgnoreCase("When Air");
            case "Necessary Chance" -> !necessaryPlacement;
            case "Sprint-Mode", "All Direction Sprint" -> sprint;
            case "MaxDelay", "CalculateDelayAfterBuild", "Smooth Delay" -> randomizeDelay;
            case "CanUp-Check" -> upScaffold;
            case "Rotate in Air" -> necessaryRotations;
            case "Reset-Mode" -> resetRotation;
            case "Tower-VerusDisabler", "Tower-Mode", "Tower-WhileMoving" -> tower;
            case "Tower-Motion" -> tower && towerMode.equalsIgnoreCase("Motion");
            case "Expand Length", "All Direction Expand" -> expand;
            case "ShouldYaw", "Jump Fix" -> moveFix;
            case "Switch Delay" -> switchBlocks;
            case "Silent Switch", "Switch Blocks", "Delayed back switch" -> silent;
            case "Jump-Motion" -> autoJump;
            case "Speed Boost", "AutoJump" -> sameY;
            case "NoSwing-Mode" -> noSwing;
            case "AutomaticVector", "FacingCheck" -> rotations && !rayCast;
            case "OnlyGround" -> safeWalk;
            case "SpoofSneak", "Sneak after..." -> sneak;
            case "Pitch" -> staticPitch;
            case "AllowAir" -> blockCheck;
            case "DynamicYaw" -> simpleRotations;
            case "All Direction Jump" -> !jumpFix;
            /*case "BestVector":*/
            case "BlockCheck", "MoveFix", "RayCast", "AlwaysRotate", "Necessary Rotations", "MouseFix", "Simple Rotations", "Static Pitch", "Ignore Yaw", "RandomAim" -> rotations;
            default -> super.isVisible(value, name);
        };
    }

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void alwaysEvent(Event event) {
        if (!this.isToggled())
            if (silent && hasSilent && delayedBackSwitch) {
                if (!switchTimer.hasReached(250)) {
                    if (event instanceof ItemSyncEvent) {
                        event.setCanceled(true);
                        getPlayerController().currentPlayerItem = getPlayer().inventory.currentItem;
                    }
                    if (event instanceof InventorySwitchSyncEvent) {
                        event.setCanceled(true);
                    }
                } else {
                    if (getPlayer().inventory.currentItem != getPlayer().inventory.fakeItem)
                        sendPacketUnlogged(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                    hasSilent = false;
                }
            }
    }

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        final RotationUtil rotationUtil = RotationUtil.getInstance();

        if (event instanceof final DirectionSprintCheckEvent sprintCheckEvent) {
            if (sprint && allDirectionSprint && isMoving())
                sprintCheckEvent.setSprintCheck(false);
        }

        if (silent && hasSilent) {
            if (event instanceof ItemSyncEvent) {
                event.setCanceled(true);
                getPlayerController().currentPlayerItem = getPlayer().inventory.currentItem;
            }

            if (event instanceof InventorySwitchSyncEvent) {
                event.setCanceled(true);
            }
        }

        if (event instanceof final SafeWalkEvent safeWalkEvent) {
            if (safeWalk && getWorld() != null && getPlayer() != null)
                safeWalkEvent.setSafe(getPlayer().onGround || !onlyGround);
        }

        if (event instanceof final MoveEvent moveEvent) {
            if (moveFix && rotations) {
                moveEvent.setYaw(getYaw());
            }
        }

        if (event instanceof KeyPressEvent) {
            if (sameY && autoJump && isMoving()) {
                getGameSettings().keyBindJump.pressed = false;
            }
        }

        if (event instanceof final JumpEvent jumpEvent) {
            if (sameY && autoJump && isMoving()) {
                jumpEvent.setHeight((float) jumpMotion);
            }
            if (rotations) {
                if (jumpFix) {
                    jumpEvent.setYaw(getYaw());
                } else if (allDirectionJump) {
                    jumpEvent.setYaw(MovementUtil.getInstance().getDirection(getPlayer().rotationYaw));
                }
            }
        }

        if (event instanceof final UpdatePlayerMovementState updatePlayerMovementState) {
            if (moveFix && rotations) {
                updatePlayerMovementState.setSilentMoveFix(true);
                updatePlayerMovementState.setYaw(curYaw);
                if (shouldYaw) {
                    updatePlayerMovementState.setShouldYaw(rotationUtil.getYaw(calcShouldYaw()) + 180);
                    updatePlayerMovementState.setFixYaw(true);
                }
            }
        }

        if (event instanceof final UpdateMotionEvent updateMotionEvent) {
            if (sprint) {
                if (sprintMode.equalsIgnoreCase("OnlyPost")) {
                    switch (updateMotionEvent.getType()) {
                        case PRE -> getPlayer().setSprinting(false);
                        case POST -> getPlayer().setSprinting(true);
                    }
                }
            }
        }

        if (event instanceof final RotationEvent rotationEvent) {

            if (rotations) {
                if (alwaysRotate || getBlockUnderPlayer(0.01F) == Blocks.air) {
                    if (!ignoreYaw)
                        rotationEvent.setYaw(curYaw);
                    rotationEvent.setPitch(curPitch + (float) (randomizePitch ? randomInRange(-0.1, 0.1) : 0));
                }
            }
        }

        if (event instanceof final PacketEvent packetEvent) {
            final Packet<? extends INetHandler> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.SEND) {
                if (silent) {
                    if (packet instanceof final C09PacketHeldItemChange heldItemChange) {
                        event.setCanceled(true);
                    }
                }
                if (sprint) {
                    switch (sprintMode) {
                        case "NoPacket":
                            if (packet instanceof final C0BPacketEntityAction action) {
                                if (action.getAction() == C0BPacketEntityAction.Action.START_SPRINTING || action.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING)
                                    event.setCanceled(true);
                            }
                            break;
                    }
                }
            }
        }

        if (event instanceof UpdateEvent) {
            if (boostMode.equalsIgnoreCase("When Air"))
                if (getBlockUnderPlayer(1F) == Blocks.air) {
                    getPlayer().motionX *= motionSpeed;
                    getPlayer().motionZ *= motionSpeed;
                }

            if (sprint) {
                switch (sprintMode) {
                    case "Normal", "NoPacket" -> getPlayer().setSprinting(true);
                }
            }
            getGameSettings().keyBindSprint.pressed = false;

            if (sameY)
                if (!getPlayer().onGround)
                    getPlayer().jumpMovementFactor = (float) speedBoost / 10;
                else {
                    if (autoJump && isMoving())
                        getPlayer().jump();
                }

            if (sneak && sneakCount >= sneakAfter) {
                if (getBlockUnderPlayer(0.1F) == Blocks.air) {
                    if (spoofSneak) {
                        if (!fakeSneak) {
                            sendPacketUnlogged(new C0CPacketInput(getPlayer().moveStrafing * 0.3F, getPlayer().moveForward * 0.3F, getPlayer().movementInput.jump, true));
                            sendPacketUnlogged(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.START_SNEAKING));
                            fakeSneak = true;
                        }
                    } else
                        getGameSettings().keyBindSneak.pressed = true;
                } else {
                    if (spoofSneak) {
                        if (fakeSneak) {
                            sendPacketUnlogged(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.STOP_SNEAKING));
                            sendPacketUnlogged(new C0CPacketInput(getPlayer().moveStrafing, getPlayer().moveForward, getPlayer().movementInput.jump, false));
                            fakeSneak = false;
                        }
                    } else
                        getGameSettings().keyBindSneak.pressed = false;
                }
            } else {
                if (sneak)
                    if (spoofSneak) {
                        if (fakeSneak) {
                            sendPacketUnlogged(new C0CPacketInput(getPlayer().moveStrafing, getPlayer().moveForward, getPlayer().movementInput.jump, false));
                            sendPacketUnlogged(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.STOP_SNEAKING));
                            fakeSneak = false;
                        }
                    } else
                        getGameSettings().keyBindSneak.pressed = false;
            }
        }

        if (event instanceof final MoveEntityWithHeadingEvent moveEntityWithHeadingEvent) {
            if (noSpeedEffect)
                moveEntityWithHeadingEvent.setAiSpeed(0.1F);
        }

        if (event instanceof AttackEvent && (mc.rightClickDelayTimer == 0 || canBuild)) {
            final MovementUtil movementUtil = MovementUtil.getInstance();
            final RandomUtil randomUtil = RandomUtil.getInstance();

            final double y = sameY && isMoving() ? startY : getY();
            boolean flag = true;

            if (!sprint) {
                getGameSettings().keyBindSprint.pressed = false;
                getPlayer().setSprinting(false);
            }

            if ((!isMoving() && getGameSettings().keyBindJump.pressed) || downScaffold && isKeyDown(getGameSettings().keyBindSneak.getKeyCode()) || getPlayer().onGround) {
                startY = (int) getY();
            }

            Vec3 position = expand(getPlayer().getPositionVector().addVector(0, (y - getY()), 0), movementUtil);

            BlockPos curPos = getBlockPosToPlaceOn(new BlockPos(position.xCoord, position.yCoord - 1, position.zCoord));

            itemStack = getPlayer().getCurrentEquippedItem();

            if (curPos != null) {
                if (!necessaryRotations || (!getPlayer().onGround && rotateInAir) || (mc.objectMouseOver == null || (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || (!mc.objectMouseOver.getBlockPos().equals(curPos)) && mc.objectMouseOver.sideHit == enumFacing || (mc.objectMouseOver.sideHit != enumFacing && mc.objectMouseOver.getBlockPos().equals(curPos))))) {
                    float[] rotation = rotationUtil.faceBlock(curPos, getWorld().getBlockState(curPos).getBlock().getBlockBoundsMaxY() - getWorld().getBlockState(curPos).getBlock().getBlockBoundsMinY() + 0.5D, a3Fix, mouseFix, prediction, randomAim, randomizePitch, clampYaw, 180);

                    /*if (bestVector) {
                        rotation = bestVector(curPos, rotation[1], 6);
                    }*/

                    if (rotation != null)
                        if (simpleRotations) {
                            curYaw = (dynamicYaw ? movementUtil.getDirection(getPlayer().rotationYaw) : getPlayer().rotationYaw) + 180;
                            curPitch = getPlayer().onGround || staticPitch ? (float) pitch : rotation[1];
                        } else {
                            curYaw = rotation[0];
                            if (!staticPitch)
                                curPitch = rotation[1];
                            else
                                curPitch = (float) pitch;
                        }
                }
                if (!getPlayer().isUsingItem())
                    if (!getPlayerController().func_181040_m() && (!worldHeightCheck || position.yCoord <= getWorld().getHeight() + 1)) {
                        mc.rightClickDelayTimer = 4;
                        if (resetRightClickDelayTimer)
                            mc.rightClickDelayTimer = 0;
                        mc.leftClickCounter = 0;
                        canBuild = true;

                        if (blockCheck && allowAir || !rayCast || (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && getWorld().getBlockState(mc.objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air))
                            if (!rayCast || (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null)) {
                                if (!calculateDelayAfterBuild)
                                    calculateDelay();
                                if (timeHelper.hasReached(currentDelay)) {
                                    final BlockPos blockpos = rayCast ? mc.objectMouseOver.getBlockPos() : curPos;
                                    if (blockpos != null && getWorld().getBlockState(curPos) != null && getWorld().getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                                        if (this.silentSlot != -1) {
                                            final ItemStack item = getPlayer().inventory.getFakeItem();
                                            if (item == null || !(item.getItem() instanceof ItemBlock)) {
                                                this.silentSlot = -1;
                                            }
                                        }

                                        if (silent && (itemStack == null || !(itemStack.getItem() instanceof ItemBlock)) || switchBlocks) {
                                            for (int i = 0; i < 9; i++) {
                                                final ItemStack item = getPlayer().inventory.getStackInSlot(i);
                                                if (item != null && item.getItem() instanceof ItemBlock) {
                                                    if (!blackList.contains(Block.getBlockFromItem(item.getItem()))) {
                                                        if ((!switchBlocks || !switchedSlots.contains(i)) && (this.silentSlot == -1 || switchBlocks && !switchedSlots.contains(i))) {
                                                            if (getPlayer().inventory.fakeItem != i) {
                                                                itemStack = item;
                                                                if (silentSlot != i) {
                                                                    sendPacketUnlogged(new C09PacketHeldItemChange(i));
                                                                }
                                                                this.silentSlot = i;
                                                                hasSilent = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            this.silentSlot = getPlayer().inventory.fakeItem;
                                            hasSilent = true;
                                        }

                                        if (this.silentSlot != -1) {
                                            itemStack = getPlayer().inventory.getStackInSlot(this.silentSlot);
                                        }

                                        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                                            Vec3 vec3 = new Vec3(curPos.getX() + 0.5, curPos.getY() + 0.5, curPos.getZ() + 0.5);
                                            if (automaticVector && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                                                vec3 = mc.objectMouseOver.hitVec;
                                            if (!blockCheck || mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || mc.objectMouseOver != null && allowAir && mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)
                                                if ((!rayCast || !facingCheck) || mc.objectMouseOver != null && mc.objectMouseOver.sideHit == enumFacing || (downScaffold && getGameSettings().keyBindSneak.pressed))
                                                    if (!sameY || !rayCast || (((blockpos.getY() == startY - 1 || !isMoving() && getGameSettings().keyBindJump.pressed) && (!mc.objectMouseOver.sideHit.equals(EnumFacing.UP))) || !isMoving()))
                                                        if (!canUpCheck || enumFacing != EnumFacing.UP || (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && mc.objectMouseOver.getBlockPos().equals(blockpos))) {
                                                            if (rayCast ? (getPlayerController().onPlayerRightClick(getPlayer(), getWorld(), itemStack, blockpos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec)) : getPlayerController().onPlayerRightClick(mc.thePlayer, getWorld(), itemStack, curPos, enumFacing, vec3)) {
                                                                if (calculateDelayAfterBuild)
                                                                    calculateDelay();
                                                                flag = false;

                                                                if (!noSwing)
                                                                    getPlayer().swingItem();
                                                                else
                                                                    switch (noSwingMode) {
                                                                        case "Packet":
                                                                            sendPacketUnlogged(new C0APacketAnimation());
                                                                            break;
                                                                    }

                                                                if (tower && (towerWhileMoving || !isMoving()) && getGameSettings().keyBindJump.pressed) {
                                                                    if (towerVerusDisabler) {
                                                                        sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                                                                        sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                                                                    }
                                                                    switch (towerMode) {
                                                                        case "Motion":
                                                                            if (blocks == 0 || currentBlocks < blocks)
                                                                                getPlayer().motionY = towerMotion;
                                                                            break;
                                                                        case "CubeCraft":
                                                                            getPlayer().jump();
                                                                            if (timeHelper.hasReached(51)) {
                                                                                getPlayer().motionY = 0F;
                                                                            }
                                                                            if (getPlayer().onGround) {
                                                                                getTimer().timerSpeed = 1.2F;
                                                                            }
                                                                            break;
                                                                    }
                                                                }

                                                                if (boostMode.equalsIgnoreCase("When Place"))
                                                                    if ((getPlayer().onGround || motionSpeed <= 1) && currentBlocks >= blocks) {
                                                                        getPlayer().motionX *= motionSpeed;
                                                                        getPlayer().motionZ *= motionSpeed;
                                                                    }
                                                                currentBlocks++;
                                                                if (currentBlocks > blocks)
                                                                    currentBlocks = 0;
                                                                sneakCount++;
                                                                if (sneakCount > sneakAfter)
                                                                    sneakCount = 0;

                                                                int blocks = 0;

                                                                if (switchBlocks) {
                                                                    for (int i = 0; i < 9; i++) {
                                                                        final ItemStack item = getPlayer().inventory.getStackInSlot(i);
                                                                        if (item != null && item.getItem() instanceof ItemBlock) {
                                                                            if (!blackList.contains(Block.getBlockFromItem(item.getItem()))) {
                                                                                blocks++;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (itemSwitchTimer.hasReached(switchDelay) || !switchBlocks) {
                                                                    if (silentSlot != -1 && silent && switchBlocks && blocks > 1)
                                                                        switchedSlots.add(silentSlot);
                                                                    if (blocks <= switchedSlots.size())
                                                                        switchedSlots.clear();
                                                                    itemSwitchTimer.reset();
                                                                }
                                                            }
                                                        }
                                        } else {
                                            return;
                                        }
                                        timeHelper.reset();
                                    }
                                    if (itemStack != null && itemStack.stackSize == 0 && this.silentSlot != -1) {
                                        getPlayer().inventory.mainInventory[this.silentSlot] = null;
                                    }
                                    if (flag && !necessaryPlacement && randomUtil.randomInRange(0, 100) <= necessaryChance && itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                                        if (getPlayerController().sendUseItem(getPlayer(), getWorld(), itemStack)) {
                                            if (calculateDelayAfterBuild)
                                                calculateDelay();
                                            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                                        }
                                    }
                                } else {
                                    if (sneak)
                                        getGameSettings().keyBindSneak.pressed = false;
                                }
                            }
                    }
            }
        }
        if (event instanceof Render2DEvent) {
            if (silentSlot != -1 || getPlayer().inventory.getCurrentItem() != null && getPlayer().inventory.getCurrentItem().getItem() instanceof ItemBlock) {
                ItemStack stack;
                if (silentSlot != -1) {
                    stack = getPlayer().inventory.getStackInSlot(silentSlot);
                } else {
                    stack = getPlayer().inventory.getCurrentItem();
                }
                if (stack != null && stack.getItem() instanceof ItemBlock) {
                    final Resolution sr = Resolution.getResolution();
                    final RenderItem renderItem = mc.getRenderItem();
                    final RenderUtil renderUtil = RenderUtil.getInstance();
                    final InventoryUtil inventoryUtil = InventoryUtil.getInstance();
                    renderUtil.drawRoundedRect(sr.getWidth() / 2f - 20, sr.getHeight() / 2F + 16, 40, 15, 8, new Color(35, 35, 50));
                    GlStateManager.enableRescaleNormal();
                    RenderHelper.enableGUIStandardItemLighting();
                    renderItem.renderItemIntoGUI(stack, (int) (sr.getWidth() / 2f - 20), sr.getHeight() / 2 + 16);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableRescaleNormal();
                    final String blocks = String.valueOf(inventoryUtil.blockSize(blackList));
                    Fonts.arial25.drawString(blocks, sr.getWidth() / 2f + 10 - Fonts.arial25.getStringWidth(blocks) / 2F, sr.getHeight() / 2F + 16, Color.white, false);
                }
            }
        }
    }

    private float[] bestVector(BlockPos position, float pitch, int reach) {
        final RotationUtil rotationUtil = RotationUtil.getInstance();
        for (float yaw = (int) getYaw(); yaw < getYaw() + 360; yaw += 0.1) {
            final MovingObjectPosition movingObjectPosition = rotationUtil.rayTrace(yaw, pitch, reach);
            if (movingObjectPosition.sideHit == enumFacing && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                sendMessage(movingObjectPosition.hitVec.xCoord + " " + movingObjectPosition.hitVec.yCoord + " " + movingObjectPosition.hitVec.zCoord);
                final BlockPos blockPos = movingObjectPosition.getBlockPos();
                return new float[]{yaw, pitch};
            }
        }
        return null;
    }

    public void calculateDelay() {
        final RandomUtil randomUtil = RandomUtil.getInstance();
        if (maxDelay < delay) return;
        currentDelay = randomizeDelay ? randomUtil.getRandomInteger(delay, maxDelay) : delay;
        if (smoothDelay) {
            currentDelay = (long) randomUtil.smooth(maxDelay, delay, smoothDelaySpeed / 10, smoothDelayRandomizing, smoothDelayRandomStrength);
        }
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public Vec3 calcShouldYaw() {
        BlockPos pos = new BlockPos(getX(), getY(), getZ());
        Vec3 vec2 = Entity.getVectorForRotation(0, getPlayer().rotationYaw);
        Vec3 v = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        Vec3 vec = v.addVector(0.5, 0, 0.5);
        return vec.addVector(Math.round(vec2.xCoord), 0, Math.round(vec2.zCoord));
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public Vec3 expand(Vec3 position, MovementUtil movementUtil) {
        if (expand) {
            final double direction = allDirectionExpand ? movementUtil.getDirection(getPlayer().rotationYaw) : getPlayer().rotationYaw;
            final Vec3 expandVector = new Vec3(-Math.sin(direction / 180 * Math.PI), 0, Math.cos(direction / 180 * Math.PI));
            int bestExpand = 0;
            for (int i = 0; i < expandLength; i++) {
                if (getGameSettings().keyBindJump.pressed && !isMoving())
                    break;
                if (getBlockPosToPlaceOn(new BlockPos(position.addVector(0, -1, 0).add(expandVector.multiply(i)))) != null && enumFacing != EnumFacing.UP) {
                    bestExpand = i;
                }
            }
            position = position.add(expandVector.multiply(bestExpand));
        }
        return position;
    }

    @Override
    public void onEnable() {
        getGameSettings().keyBindSprint.pressed = false;
        getPlayer().setSprinting(false);
        calculateDelay();
        hasSilent = false;
        canBuild = false;
        currentBlocks = 0;
        switchedSlots.clear();
        silentSlot = -1;
        startY = (int) getPlayer().posY;
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
    }

    @Override
    public void onDisable() {
        switchTimer.reset();
        getTimer().timerSpeed = 1F;
        if (resetRotation)
            resetRotations(getYaw(), getPitch(), resetMode.equalsIgnoreCase("Silent"));
        if (!delayedBackSwitch && hasSilent) {
            if (getPlayer().inventory.currentItem != getPlayer().inventory.fakeItem)
                sendPacketUnlogged(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
            hasSilent = false;
        }
        if (sneak)
            if (spoofSneak) {
                if (fakeSneak) {
                    sendPacketUnlogged(new C0CPacketInput(getPlayer().moveStrafing, getPlayer().moveForward, getPlayer().movementInput.jump, false));
                    sendPacketUnlogged(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.STOP_SNEAKING));
                }
            } else {
                if (!isKeyDown(getGameSettings().keyBindSneak.getKeyCode()))
                    getGameSettings().keyBindSneak.pressed = false;
            }
        fakeSneak = false;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public BlockPos searchPos(BlockPos pos) {
        for (int x = -1; x < 1; x++)
            for (int z = -1; z < 1; z++) {
                final BlockPos find = pos.add(x, 0, z);
                if (getWorld().getBlockState(find).getBlock() == Blocks.air) {
                    return find;
                }
            }
        return null;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    private BlockPos getBlockPosToPlaceOn(BlockPos pos) {
        final BlockPos blockPos1 = pos.add(-1, 0, 0);
        final BlockPos blockPos2 = pos.add(1, 0, 0);
        final BlockPos blockPos3 = pos.add(0, 0, -1);
        final BlockPos blockPos4 = pos.add(0, 0, 1);
        final boolean isDown = downScaffold && isKeyDown(getGameSettings().keyBindSneak.getKeyCode());
        if (isDown)
            getGameSettings().keyBindSneak.pressed = false;
        final float down = isDown ? 1 : 0;
        if (upScaffold && getWorld().getBlockState(pos.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.UP;
            return (pos.add(0, -1 - down, 0));
        } else if (isDown && getWorld().getBlockState(pos).getBlock() != Blocks.air) {
            //BLOCK DOWN
            enumFacing = EnumFacing.DOWN;
            return (pos);
        } else if (getWorld().getBlockState(pos.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.EAST;
            return (pos.add(-1, 0 - down, 0));
        } else if (getWorld().getBlockState(pos.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.WEST;
            return (pos.add(1, 0 - down, 0));
        } else if (getWorld().getBlockState(pos.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.SOUTH;
            return (pos.add(0, 0 - down, -1));
        } else if (getWorld().getBlockState(pos.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.NORTH;
            return (pos.add(0, 0 - down, 1));
        } else if (getWorld().getBlockState(blockPos1.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.DOWN;
            return (blockPos1.add(0, -1 - down, 0));
        } else if (getWorld().getBlockState(blockPos1.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.EAST;
            return (blockPos1.add(-1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos1.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.WEST;
            return (blockPos1.add(1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos1.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos1.add(0, 0 - down, -1));
        } else if (getWorld().getBlockState(blockPos1.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos1.add(0, 0 - down, 1));
        } else if (getWorld().getBlockState(blockPos2.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.UP;
            return (blockPos2.add(0, -1 - down, 0));
        } else if (getWorld().getBlockState(blockPos2.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.EAST;
            return (blockPos2.add(-1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos2.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.WEST;
            return (blockPos2.add(1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos2.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos2.add(0, 0 - down, -1));
        } else if (getWorld().getBlockState(blockPos2.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos2.add(0, 0 - down, 1));
        } else if (getWorld().getBlockState(blockPos3.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.UP;
            return (blockPos3.add(0, -1 - down, 0));
        } else if (getWorld().getBlockState(blockPos3.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.EAST;
            return (blockPos3.add(-1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos3.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.WEST;
            return (blockPos3.add(1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos3.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos3.add(0, 0 - down, -1));
        } else if (getWorld().getBlockState(blockPos3.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos3.add(0, 0 - down, 1));
        } else if (getWorld().getBlockState(blockPos4.add(0, -1 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.UP;
            return (blockPos4.add(0, -1 - down, 0));
        } else if (getWorld().getBlockState(blockPos4.add(-1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.EAST;
            return (blockPos4.add(-1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos4.add(1, 0 - down, 0)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.WEST;
            return (blockPos4.add(1, 0 - down, 0));
        } else if (getWorld().getBlockState(blockPos4.add(0, 0 - down, -1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos4.add(0, 0 - down, -1));
        } else if (getWorld().getBlockState(blockPos4.add(0, 0 - down, 1)).getBlock() != Blocks.air && diagonal) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos4.add(0, 0 - down, 1));
        }
        return null;
    }
}
