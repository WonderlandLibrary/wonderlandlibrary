package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PlaceRotation;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Scaffold", description="Automatically places blocks beneath your feet.", category=ModuleCategory.WORLD, keyBind=23)
public class Scaffold
extends Module {
    private final BoolValue towerEnabled = new BoolValue("EnableTower", false);
    private final ListValue towerModeValue = new ListValue("TowerMode", new String[]{"Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4", "Verus"}, "Motion");
    private final ListValue towerPlaceModeValue = new ListValue("Tower-PlaceTiming", new String[]{"Pre", "Post"}, "Post");
    private final BoolValue stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
    private final BoolValue onJumpValue = new BoolValue("OnJump", false);
    private final BoolValue noMoveOnlyValue = new BoolValue("NoMove", true);
    private final FloatValue towerTimerValue = new FloatValue("TowerTimer", 1.0f, 0.1f, 10.0f);
    private final FloatValue jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f);
    private final IntegerValue jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);
    private final FloatValue constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f);
    private final FloatValue constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f);
    private final FloatValue teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f);
    private final IntegerValue teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
    private final BoolValue teleportGroundValue = new BoolValue("TeleportGround", true);
    private final BoolValue teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");
    private final BoolValue placeableDelay = new BoolValue("PlaceableDelay", false);
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)Scaffold.this.minDelayValue.get();
            if (i > newValue) {
                this.set(i);
            }
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)Scaffold.this.maxDelayValue.get();
            if (i < newValue) {
                this.set(i);
            }
        }
    };
    private final BoolValue smartDelay = new BoolValue("SmartDelay", true);
    private final ListValue autoBlockMode = new ListValue("AutoBlock", new String[]{"Spoof", "Switch", "Matrix", "Off"}, "Spoof");
    private final BoolValue stayAutoBlock = new BoolValue("StayAutoBlock", false);
    public final ListValue sprintModeValue = new ListValue("SprintMode", new String[]{"Same", "Ground", "Air", "PlaceOff"}, "Air");
    public final BoolValue sprintValue = new BoolValue("Sprint", true);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue downValue = new BoolValue("Down", false);
    private final BoolValue searchValue = new BoolValue("Search", true);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
    private final BoolValue eagleValue = new BoolValue("Eagle", false);
    private final BoolValue eagleSilentValue = new BoolValue("EagleSilent", false);
    private final IntegerValue blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
    private final FloatValue eagleEdgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2f, 0.0f, 0.5f);
    private final BoolValue omniDirectionalExpand = new BoolValue("OmniDirectionalExpand", true);
    private final IntegerValue expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);
    private final IntegerValue searchAccuracyValue = new IntegerValue("SearchAccuracy", 8, 1, 24){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            if (this.getMaximum() < newValue) {
                this.set(this.getMaximum());
            } else if (this.getMinimum() > newValue) {
                this.set(this.getMinimum());
            }
        }
    };
    private final FloatValue xzRangeValue = new FloatValue("xzRange", 0.8f, 0.1f, 1.0f);
    private final FloatValue yRangeValue = new FloatValue("yRange", 0.8f, 0.1f, 1.0f);
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue noHitCheckValue = new BoolValue("NoHitCheck", false);
    public final ListValue rotationModeValue = new ListValue("RotationMode", new String[]{"Hypixel", "Normal", "AAC", "Static", "Static2", "Static3", "Custom"}, "Normal");
    public final ListValue rotationLookupValue = new ListValue("RotationLookup", new String[]{"Normal", "AAC", "Same"}, "Normal");
    private final FloatValue maxTurnSpeed = new FloatValue("MaxTurnSpeed", 180.0f, 0.0f, 180.0f){

        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float i = ((Float)Scaffold.this.minTurnSpeed.get()).floatValue();
            if (i > newValue.floatValue()) {
                this.set(Float.valueOf(i));
            }
        }
    };
    private final FloatValue minTurnSpeed = new FloatValue("MinTurnSpeed", 180.0f, 0.0f, 180.0f){

        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float i = ((Float)Scaffold.this.maxTurnSpeed.get()).floatValue();
            if (i < newValue.floatValue()) {
                this.set(Float.valueOf(i));
            }
        }
    };
    private final IntegerValue HypixelYawValue = new IntegerValue("HypixelYaw", 180, -360, 360);
    private final IntegerValue HypixelPitchValue = new IntegerValue("HypixelPitch", 79, 60, 100);
    private final FloatValue staticPitchValue = new FloatValue("Static-Pitch", 86.0f, 80.0f, 90.0f);
    private final FloatValue customYawValue = new FloatValue("Custom-Yaw", 135.0f, -180.0f, 180.0f);
    private final FloatValue customPitchValue = new FloatValue("Custom-Pitch", 86.0f, -90.0f, 90.0f);
    private final BoolValue keepRotOnJumpValue = new BoolValue("KeepRotOnJump", true);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false);
    private final IntegerValue keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);
    private final ListValue placeConditionValue = new ListValue("Place-Condition", new String[]{"Air", "FallDown", "NegativeMotion", "Always"}, "Always");
    private final BoolValue rotationStrafeValue = new BoolValue("RotationStrafe", false);
    private final BoolValue zitterValue = new BoolValue("Zitter", false);
    private final ListValue zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Teleport");
    private final FloatValue zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f);
    private final FloatValue zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f);
    private final IntegerValue zitterDelay = new IntegerValue("ZitterDelay", 100, 0, 500);
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
    public final FloatValue speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f);
    private final BoolValue customSpeedValue = new BoolValue("CustomSpeed", false);
    private final FloatValue customMoveSpeedValue = new FloatValue("CustomMoveSpeed", 0.3f, 0.0f, 5.0f);
    private final BoolValue sameYValue = new BoolValue("SameY", false);
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final BoolValue smartSpeedValue = new BoolValue("SmartSpeed", false);
    private final BoolValue safeWalkValue = new BoolValue("SafeWalk", true);
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);
    private final BoolValue autoDisableSpeedValue = new BoolValue("AutoDisable-Speed", true);
    public final ListValue counterDisplayValue = new ListValue("Counter", new String[]{"Off", "Simple", "Advanced", "Sigma", "Novoline"}, "Simple");
    private final BoolValue markValue = new BoolValue("Mark", false);
    private final IntegerValue redValue = new IntegerValue("Red", 0, 0, 255);
    private final IntegerValue greenValue = new IntegerValue("Green", 120, 0, 255);
    private final IntegerValue blueValue = new IntegerValue("Blue", 255, 0, 255);
    private final IntegerValue alphaValue = new IntegerValue("Alpha", 120, 0, 255);
    private PlaceInfo targetPlace;
    private PlaceInfo towerPlace;
    private int launchY;
    private boolean faceBlock;
    private Rotation lockRotation;
    private Rotation lookupRotation;
    private int slot;
    private int lastSlot;
    private boolean zitterDirection;
    private final MSTimer delayTimer = new MSTimer();
    private final MSTimer zitterTimer = new MSTimer();
    private long delay;
    private int placedBlocksWithoutEagle = 0;
    private boolean eagleSneaking;
    private boolean shouldGoDown = false;
    private float progress = 0.0f;
    private long lastMS = 0L;
    private final TickTimer timer = new TickTimer();
    private double jumpGround = 0.0;
    private int verusState = 0;
    private boolean verusJumped = false;

    public boolean isTowerOnly() {
        return (Boolean)this.towerEnabled.get() != false && (Boolean)this.onJumpValue.get() == false;
    }

    public boolean towerActivation() {
        return !((Boolean)this.towerEnabled.get() == false || (Boolean)this.onJumpValue.get() != false && !mc.getGameSettings().getKeyBindJump().isKeyDown() || (Boolean)this.noMoveOnlyValue.get() != false && MovementUtils.isMoving());
    }

    @Override
    public void onEnable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        this.progress = 0.0f;
        this.launchY = (int)mc.getThePlayer().getPosY();
        this.lastSlot = mc.getThePlayer().getInventory().getCurrentItem();
        this.slot = mc.getThePlayer().getInventory().getCurrentItem();
        if (((Boolean)this.autoDisableSpeedValue.get()).booleanValue() && LiquidBounce.moduleManager.getModule(Speed.class).getState()) {
            LiquidBounce.moduleManager.getModule(Speed.class).setState(false);
            LiquidBounce.hud.addNotification(new Notification("Scaffold", "Speed is disabled to prevent flags/errors.", NotifyType.INFO, 500, 1000));
        }
        this.faceBlock = false;
        this.lastMS = System.currentTimeMillis();
    }

    private void fakeJump() {
        mc.getThePlayer().setAirBorne(true);
        mc.getThePlayer().triggerAchievement(classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    private void move(MotionEvent event) {
        switch (((String)this.towerModeValue.get()).toLowerCase()) {
            case "jump": {
                if (!mc.getThePlayer().getOnGround() || !this.timer.hasTimePassed((Integer)this.jumpDelayValue.get())) break;
                this.fakeJump();
                mc.getThePlayer().setMotionY(((Float)this.jumpMotionValue.get()).floatValue());
                this.timer.reset();
                break;
            }
            case "motion": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    mc.getThePlayer().setMotionY(0.42);
                    break;
                }
                if (!(mc.getThePlayer().getMotionY() < 0.1)) break;
                mc.getThePlayer().setMotionY(0.3);
                break;
            }
            case "motiontp": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    mc.getThePlayer().setMotionY(0.42);
                    break;
                }
                if (!(mc.getThePlayer().getMotionY() < 0.23)) break;
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX(), (int)mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                break;
            }
            case "packet": {
                if (!mc.getThePlayer().getOnGround() || !this.timer.hasTimePassed(2)) break;
                this.fakeJump();
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + 0.42, mc.getThePlayer().getPosZ(), false));
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + 0.76, mc.getThePlayer().getPosZ(), false));
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + 1.08, mc.getThePlayer().getPosZ());
                this.timer.reset();
                break;
            }
            case "teleport": {
                if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                    mc.getThePlayer().setMotionY(0.0);
                }
                if (!mc.getThePlayer().getOnGround() && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed((Integer)this.teleportDelayValue.get())) break;
                this.fakeJump();
                mc.getThePlayer().setPositionAndUpdate(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + (double)((Float)this.teleportHeightValue.get()).floatValue(), mc.getThePlayer().getPosZ());
                this.timer.reset();
                break;
            }
            case "constantmotion": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    this.jumpGround = mc.getThePlayer().getPosY();
                    mc.getThePlayer().setMotionY(((Float)this.constantMotionValue.get()).floatValue());
                }
                if (!(mc.getThePlayer().getPosY() > this.jumpGround + (double)((Float)this.constantMotionJumpGroundValue.get()).floatValue())) break;
                this.fakeJump();
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX(), (int)mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                mc.getThePlayer().setMotionY(((Float)this.constantMotionValue.get()).floatValue());
                this.jumpGround = mc.getThePlayer().getPosY();
                break;
            }
            case "aac3.3.9": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    mc.getThePlayer().setMotionY(0.4001);
                }
                mc.getTimer().setTimerSpeed(1.0f);
                if (!(mc.getThePlayer().getMotionY() < 0.0)) break;
                mc.getThePlayer().setMotionY(-9.45E-6);
                mc.getTimer().setTimerSpeed(1.6f);
                break;
            }
            case "aac3.6.4": {
                if (mc.getThePlayer().getTicksExisted() % 4 == 1) {
                    mc.getThePlayer().setMotionY(0.4195464);
                    mc.getThePlayer().setPosition(mc.getThePlayer().getPosX() - 0.035, mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                    break;
                }
                if (mc.getThePlayer().getTicksExisted() % 4 != 0) break;
                mc.getThePlayer().setMotionY(-0.5);
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX() + 0.035, mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                break;
            }
            case "verus": {
                if (!mc.getTheWorld().getCollidingBoundingBoxes(mc.getThePlayer(), mc.getThePlayer().getEntityBoundingBox().offset(0.0, -0.01, 0.0)).isEmpty() && mc.getThePlayer().getOnGround() && mc.getThePlayer().isCollidedVertically()) {
                    this.verusState = 0;
                    this.verusJumped = true;
                }
                if (this.verusJumped) {
                    MovementUtils.strafe();
                    switch (this.verusState) {
                        case 0: {
                            this.fakeJump();
                            mc.getThePlayer().setMotionY(0.42f);
                            ++this.verusState;
                            break;
                        }
                        case 1: {
                            ++this.verusState;
                            break;
                        }
                        case 2: {
                            ++this.verusState;
                            break;
                        }
                        case 3: {
                            event.setOnGround(true);
                            mc.getThePlayer().setMotionY(0.0);
                            ++this.verusState;
                            break;
                        }
                        case 4: {
                            ++this.verusState;
                        }
                    }
                    this.verusJumped = false;
                }
                this.verusJumped = true;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.towerActivation()) {
            this.shouldGoDown = false;
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
            mc.getThePlayer().setSprinting(false);
            return;
        }
        if (((String)this.sprintModeValue.get()).equalsIgnoreCase("PlaceOff")) {
            if (mc.getThePlayer().getOnGround()) {
                // empty if block
            }
            mc.getThePlayer().setSprinting(true);
            mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * 1.0);
            mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * 1.0);
        }
        mc.getTimer().setTimerSpeed(((Float)this.timerValue.get()).floatValue());
        boolean bl = this.shouldGoDown = (Boolean)this.downValue.get() != false && (Boolean)this.sameYValue.get() == false && mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSneak()) && this.getBlocksAmount() > 1;
        if (this.shouldGoDown) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
        }
        if (((Boolean)this.customSpeedValue.get()).booleanValue()) {
            MovementUtils.strafe(((Float)this.customMoveSpeedValue.get()).floatValue());
        }
        if (mc.getThePlayer().getOnGround()) {
            String mode = (String)this.modeValue.get();
            if (mode.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2f);
                mc.getThePlayer().setMotionY(0.0);
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("smooth")) {
                if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                }
                if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                }
                if (this.zitterTimer.hasTimePassed(((Integer)this.zitterDelay.get()).intValue())) {
                    this.zitterDirection = !this.zitterDirection;
                    this.zitterTimer.reset();
                }
                if (this.zitterDirection) {
                    mc.getGameSettings().getKeyBindRight().setPressed(true);
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                } else {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                    mc.getGameSettings().getKeyBindLeft().setPressed(true);
                }
            }
            if (((Boolean)this.eagleValue.get()).booleanValue() && !this.shouldGoDown) {
                double dif = 0.5;
                if (((Float)this.eagleEdgeDistanceValue.get()).floatValue() > 0.0f) {
                    for (int i = 0; i < 4; ++i) {
                        WBlockPos WBlockPos2 = new WBlockPos(mc.getThePlayer().getPosX() + (double)(i == 0 ? -1 : (i == 1 ? 1 : 0)), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ() + (double)(i == 2 ? -1 : (i == 3 ? 1 : 0)));
                        PlaceInfo placeInfo = PlaceInfo.get(WBlockPos2);
                        if (!BlockUtils.isReplaceable(WBlockPos2) || placeInfo == null) continue;
                        double calcDif = i > 1 ? mc.getThePlayer().getPosZ() - (double)WBlockPos2.getZ() : mc.getThePlayer().getPosX() - (double)WBlockPos2.getX();
                        if ((calcDif -= 0.5) < 0.0) {
                            calcDif *= -1.0;
                        }
                        if (!((calcDif -= 0.5) < dif)) continue;
                        dif = calcDif;
                    }
                }
                if (this.placedBlocksWithoutEagle >= (Integer)this.blocksToEagleValue.get()) {
                    boolean shouldEagle;
                    boolean bl2 = shouldEagle = mc.getTheWorld().getBlockState(new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 1.0, mc.getThePlayer().getPosZ())).getBlock().equals(classProvider.getBlockEnum(BlockType.AIR)) || dif < (double)((Float)this.eagleEdgeDistanceValue.get()).floatValue();
                    if (((Boolean)this.eagleSilentValue.get()).booleanValue()) {
                        if (this.eagleSneaking != shouldEagle) {
                            mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), shouldEagle ? ICPacketEntityAction.WAction.START_SNEAKING : ICPacketEntityAction.WAction.STOP_SNEAKING));
                        }
                        this.eagleSneaking = shouldEagle;
                    } else {
                        mc.getGameSettings().getKeyBindSneak().setPressed(shouldEagle);
                    }
                    this.placedBlocksWithoutEagle = 0;
                } else {
                    ++this.placedBlocksWithoutEagle;
                }
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(((Float)this.zitterSpeed.get()).floatValue());
                double yaw = Math.toRadians((double)mc.getThePlayer().getRotationYaw() + (this.zitterDirection ? 90.0 : -90.0));
                mc.getThePlayer().setMotionX(-Math.sin(yaw) * (double)((Float)this.zitterStrength.get()).floatValue());
                mc.getThePlayer().setMotionZ(Math.cos(yaw) * (double)((Float)this.zitterStrength.get()).floatValue());
                boolean bl3 = this.zitterDirection = !this.zitterDirection;
            }
        }
        if (((String)this.sprintModeValue.get()).equalsIgnoreCase("off") || ((String)this.sprintModeValue.get()).equalsIgnoreCase("ground") && !mc.getThePlayer().getOnGround() || ((String)this.sprintModeValue.get()).equalsIgnoreCase("air") && mc.getThePlayer().getOnGround()) {
            mc.getThePlayer().setSprinting(true);
        }
        if (this.shouldGoDown) {
            this.launchY = (int)mc.getThePlayer().getPosY() - 1;
        } else if (!((Boolean)this.sameYValue.get()).booleanValue()) {
            if (!((Boolean)this.autoJumpValue.get()).booleanValue() && (!((Boolean)this.smartSpeedValue.get()).booleanValue() || !LiquidBounce.moduleManager.getModule(Speed.class).getState()) || mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindJump()) || mc.getThePlayer().getPosY() < (double)this.launchY) {
                this.launchY = (int)mc.getThePlayer().getPosY();
            }
            if (((Boolean)this.autoJumpValue.get()).booleanValue() && !LiquidBounce.moduleManager.getModule(Speed.class).getState() && MovementUtils.isMoving() && mc.getThePlayer().getOnGround()) {
                mc.getThePlayer().jump();
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (mc.getThePlayer() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (classProvider.isCPacketHeldItemChange(packet)) {
            ICPacketHeldItemChange packetHeldItemChange = packet.asCPacketHeldItemChange();
            this.slot = packetHeldItemChange.getSlotId();
        }
    }

    @EventTarget
    public void onStrafe(StrafeEvent event) {
        if (this.lookupRotation != null && ((Boolean)this.rotationStrafeValue.get()).booleanValue()) {
            float f;
            int dif = (int)((MathHelper.wrapDegrees((float)(mc.getThePlayer().getRotationYaw() - this.lookupRotation.getYaw() - 23.5f - 135.0f)) + 180.0f) / 45.0f);
            float yaw = this.lookupRotation.getYaw();
            float strafe = event.getStrafe();
            float forward = event.getForward();
            float friction = event.getFriction();
            float calcForward = 0.0f;
            float calcStrafe = 0.0f;
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
                }
            }
            if (calcForward > 1.0f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1.0f || calcForward > -0.9f && calcForward < -0.3f) {
                calcForward *= 0.5f;
            }
            if (calcStrafe > 1.0f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1.0f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
                calcStrafe *= 0.5f;
            }
            if ((f = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4f) {
                if ((f = MathHelper.sqrt((float)f)) < 1.0f) {
                    f = 1.0f;
                }
                f = friction / f;
                float yawSin = MathHelper.sin((float)((float)((double)yaw * Math.PI / 180.0)));
                float yawCos = MathHelper.cos((float)((float)((double)yaw * Math.PI / 180.0)));
                mc.getThePlayer().setMotionX((calcStrafe *= f) * yawCos - (calcForward *= f) * yawSin);
                mc.getThePlayer().setMotionZ(calcForward * yawCos + calcStrafe * yawSin);
            }
            event.cancelEvent();
        }
    }

    private boolean shouldPlace() {
        boolean placeWhenAir = ((String)this.placeConditionValue.get()).equalsIgnoreCase("air");
        boolean placeWhenFall = ((String)this.placeConditionValue.get()).equalsIgnoreCase("falldown");
        boolean placeWhenNegativeMotion = ((String)this.placeConditionValue.get()).equalsIgnoreCase("negativemotion");
        boolean alwaysPlace = ((String)this.placeConditionValue.get()).equalsIgnoreCase("always");
        return this.towerActivation() || alwaysPlace || placeWhenAir && !mc.getThePlayer().getOnGround() || placeWhenFall && mc.getThePlayer().getFallDistance() > 0.0f || placeWhenNegativeMotion && mc.getThePlayer().getMotionY() < 0.0;
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
            RotationUtils.setTargetRotation(RotationUtils.limitAngleChange(RotationUtils.serverRotation, this.lockRotation, RandomUtils.nextFloat(((Float)this.minTurnSpeed.get()).floatValue(), ((Float)this.maxTurnSpeed.get()).floatValue())));
        }
        String mode = (String)this.modeValue.get();
        EventState eventState = event.getEventState();
        if ((!((Boolean)this.rotationsValue.get()).booleanValue() || ((Boolean)this.noHitCheckValue.get()).booleanValue() || this.faceBlock) && ((String)this.placeModeValue.get()).equalsIgnoreCase(eventState.getStateName()) && !this.towerActivation()) {
            this.place(false);
        }
        if (eventState == EventState.PRE && !this.towerActivation()) {
            if (!this.shouldPlace() || (!((String)this.autoBlockMode.get()).equalsIgnoreCase("Off") ? InventoryUtils.findAutoBlockBlock() == -1 : mc.getThePlayer().getHeldItem() == null || !(mc.getThePlayer().getHeldItem().getItem() instanceof ItemBlock))) {
                return;
            }
            this.findBlock(mode.equalsIgnoreCase("expand") && !this.towerActivation());
        }
        if (this.targetPlace == null && ((Boolean)this.placeableDelay.get()).booleanValue()) {
            this.delayTimer.reset();
        }
        if (!this.towerActivation()) {
            this.verusState = 0;
            this.towerPlace = null;
            return;
        }
        mc.getTimer().setTimerSpeed(((Float)this.towerTimerValue.get()).floatValue());
        if (((String)this.towerPlaceModeValue.get()).equalsIgnoreCase(eventState.getStateName())) {
            this.place(true);
        }
        if (eventState == EventState.PRE) {
            boolean isHeldItemBlock;
            this.towerPlace = null;
            this.timer.update();
            boolean bl = isHeldItemBlock = mc.getThePlayer().getHeldItem() != null && mc.getThePlayer().getHeldItem().getItem() instanceof ItemBlock;
            if (InventoryUtils.findAutoBlockBlock() != -1 || isHeldItemBlock) {
                VecRotation vecRotation;
                this.launchY = (int)mc.getThePlayer().getPosY();
                if (((String)this.towerModeValue.get()).equalsIgnoreCase("verus") || !((Boolean)this.stopWhenBlockAbove.get()).booleanValue() || BlockUtils.getBlock(new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + 2.0, mc.getThePlayer().getPosZ())) instanceof BlockAir) {
                    this.move(event);
                }
                WBlockPos WBlockPos2 = new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 1.0, mc.getThePlayer().getPosZ());
                if (mc.getTheWorld().getBlockState(WBlockPos2).getBlock() instanceof BlockAir && this.search(WBlockPos2, true, true) && ((Boolean)this.rotationsValue.get()).booleanValue() && (vecRotation = RotationUtils.faceBlock(WBlockPos2)) != null) {
                    RotationUtils.setTargetRotation(RotationUtils.limitAngleChange(RotationUtils.serverRotation, vecRotation.getRotation(), RandomUtils.nextFloat(((Float)this.minTurnSpeed.get()).floatValue(), ((Float)this.maxTurnSpeed.get()).floatValue())));
                    this.towerPlace.setVec3(vecRotation.getVec());
                }
            }
        }
    }

    private void findBlock(boolean expand) {
        block5: {
            WBlockPos WBlockPosition;
            block4: {
                WBlockPos wBlockPos = this.shouldGoDown ? (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()) : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()).down()) : (!this.towerActivation() && ((Boolean)this.sameYValue.get() != false || ((Boolean)this.autoJumpValue.get() != false || (Boolean)this.smartSpeedValue.get() != false && LiquidBounce.moduleManager.getModule(Speed.class).getState()) && mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindJump())) && (double)this.launchY <= mc.getThePlayer().getPosY() ? new WBlockPos(mc.getThePlayer().getPosX(), (double)(this.launchY - 1), mc.getThePlayer().getPosZ()) : (WBlockPosition = mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? new WBlockPos(mc.getThePlayer()) : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ()).down()));
                if (!(expand || BlockUtils.isReplaceable(WBlockPosition) && !this.search(WBlockPosition, !this.shouldGoDown, false))) {
                    return;
                }
                if (!expand) break block4;
                double yaw = Math.toRadians(mc.getThePlayer().getRotationYaw());
                int x = (Boolean)this.omniDirectionalExpand.get() != false ? (int)Math.round(-Math.sin(yaw)) : mc.getThePlayer().getHorizontalFacing().getDirectionVec().getX();
                int z = (Boolean)this.omniDirectionalExpand.get() != false ? (int)Math.round(Math.cos(yaw)) : mc.getThePlayer().getHorizontalFacing().getDirectionVec().getZ();
                for (int i = 0; i < (Integer)this.expandLengthValue.get(); ++i) {
                    if (!this.search(WBlockPosition.add(x * i, 0, z * i), false, false)) continue;
                    return;
                }
                break block5;
            }
            if (!((Boolean)this.searchValue.get()).booleanValue()) break block5;
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    if (!this.search(WBlockPosition.add(x, 0, z), !this.shouldGoDown, false)) continue;
                    return;
                }
            }
        }
    }

    private void place(boolean towerActive) {
        IItemBlock itemBlock;
        IBlock block;
        if (((String)this.sprintModeValue.get()).equalsIgnoreCase("PlaceOff")) {
            mc.getThePlayer().setSprinting(false);
            mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * 1.0);
            mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * 1.0);
        }
        if ((towerActive ? this.towerPlace : this.targetPlace) == null) {
            if (((Boolean)this.placeableDelay.get()).booleanValue()) {
                this.delayTimer.reset();
            }
            return;
        }
        if (!this.towerActivation() && (!this.delayTimer.hasTimePassed(this.delay) || ((Boolean)this.smartDelay.get()).booleanValue() && mc.getRightClickDelayTimer() > 0 || (((Boolean)this.sameYValue.get()).booleanValue() || (((Boolean)this.autoJumpValue.get()).booleanValue() || ((Boolean)this.smartSpeedValue.get()).booleanValue() && LiquidBounce.moduleManager.getModule(Speed.class).getState()) && mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindJump())) && this.launchY - 1 != (int)(towerActive ? this.towerPlace : this.targetPlace).getVec3().getYCoord())) {
            return;
        }
        int blockSlot = -1;
        IItemStack itemStack = mc.getThePlayer().getHeldItem();
        if (mc.getThePlayer().getHeldItem() == null || !(mc.getThePlayer().getHeldItem().getItem() instanceof ItemBlock)) {
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Off")) {
                return;
            }
            blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1) {
                return;
            }
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Matrix") && blockSlot - 36 != this.slot) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(blockSlot - 36));
            }
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Spoof")) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(blockSlot - 36));
                itemStack = mc.getThePlayer().getInventoryContainer().getSlot(blockSlot).getStack();
            } else {
                mc.getThePlayer().getInventory().setCurrentItem(blockSlot - 36);
                mc.getPlayerController().updateController();
            }
        }
        if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemBlock && (InventoryUtils.BLOCK_BLACKLIST.contains(block = (itemBlock = itemStack.getItem().asItemBlock()).getBlock()) || !block.isFullCube(block.getDefaultState()) || itemStack.getStackSize() <= 0)) {
            return;
        }
        if (mc.getPlayerController().onPlayerRightClick(mc.getThePlayer(), mc.getTheWorld(), itemStack, (towerActive ? this.towerPlace : this.targetPlace).getBlockPos(), (towerActive ? this.towerPlace : this.targetPlace).getEnumFacing(), (towerActive ? this.towerPlace : this.targetPlace).getVec3())) {
            this.delayTimer.reset();
            long l = this.delay = (Boolean)this.placeableDelay.get() == false ? 0L : TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            if (mc.getThePlayer().getOnGround()) {
                float modifier = ((Float)this.speedModifierValue.get()).floatValue();
                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * (double)modifier);
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * (double)modifier);
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                mc.getThePlayer().swingItem();
            } else {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketAnimation());
            }
        }
        if (towerActive) {
            this.towerPlace = null;
        } else {
            this.targetPlace = null;
        }
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && blockSlot >= 0 && !((String)this.autoBlockMode.get()).equalsIgnoreCase("Switch")) {
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
        }
    }

    @Override
    public void onDisable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSneak())) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
            if (this.eagleSneaking) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), ICPacketEntityAction.WAction.STOP_SNEAKING));
            }
        }
        if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
            mc.getGameSettings().getKeyBindRight().setPressed(false);
        }
        if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
            mc.getGameSettings().getKeyBindLeft().setPressed(false);
        }
        this.lockRotation = null;
        this.lookupRotation = null;
        mc.getTimer().setTimerSpeed(1.0f);
        this.shouldGoDown = false;
        this.faceBlock = false;
        if (this.lastSlot != mc.getThePlayer().getInventory().getCurrentItem() && ((String)this.autoBlockMode.get()).equalsIgnoreCase("switch")) {
            mc.getThePlayer().getInventory().setCurrentItem(this.lastSlot);
            mc.getPlayerController().updateController();
        }
        if (this.slot != mc.getThePlayer().getInventory().getCurrentItem() && ((String)this.autoBlockMode.get()).equalsIgnoreCase("spoof")) {
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown) {
            return;
        }
        if (((Boolean)this.airSafeValue.get()).booleanValue() || mc.getThePlayer().getOnGround()) {
            event.setSafeWalk(true);
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (this.towerActivation()) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        this.progress = (float)(System.currentTimeMillis() - this.lastMS) / 100.0f;
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        String counterMode = (String)this.counterDisplayValue.get();
        IScaledResolution scaledResolution = classProvider.createScaledResolution(mc);
        String info = this.getBlocksAmount() + " blocks";
        int infoWidth = Fonts.font25.getStringWidth(info);
        int infoWidth2 = Fonts.minecraftFont.getStringWidth(this.getBlocksAmount() + "");
        if (counterMode.equalsIgnoreCase("simple")) {
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", scaledResolution.getScaledWidth() / 2 - infoWidth2 / 2 - 1, scaledResolution.getScaledHeight() / 2 - 36, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", scaledResolution.getScaledWidth() / 2 - infoWidth2 / 2 + 1, scaledResolution.getScaledHeight() / 2 - 36, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", scaledResolution.getScaledWidth() / 2 - infoWidth2 / 2, scaledResolution.getScaledHeight() / 2 - 35, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", scaledResolution.getScaledWidth() / 2 - infoWidth2 / 2, scaledResolution.getScaledHeight() / 2 - 37, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", scaledResolution.getScaledWidth() / 2 - infoWidth2 / 2, scaledResolution.getScaledHeight() / 2 - 36, -1, false);
        }
        if (counterMode.equalsIgnoreCase("advanced")) {
            boolean canRenderStack = this.slot >= 0 && this.slot < 9 && mc.getThePlayer().getInventory().getMainInventory().get(this.slot) != null && mc.getThePlayer().getInventory().getMainInventory().get(this.slot).getItem() != null && mc.getThePlayer().getInventory().getMainInventory().get(this.slot).getItem() instanceof ItemBlock;
            RenderUtils.drawRect(scaledResolution.getScaledWidth() / 2 - infoWidth / 2 - 4, scaledResolution.getScaledHeight() / 2 - 40, scaledResolution.getScaledWidth() / 2 + infoWidth / 2 + 4, scaledResolution.getScaledHeight() / 2 - 39, this.getBlocksAmount() > 1 ? -1 : -61424);
            RenderUtils.drawRect(scaledResolution.getScaledWidth() / 2 - infoWidth / 2 - 4, scaledResolution.getScaledHeight() / 2 - 39, scaledResolution.getScaledWidth() / 2 + infoWidth / 2 + 4, scaledResolution.getScaledHeight() / 2 - 26, -1610612736);
            if (canRenderStack) {
                RenderUtils.drawRect(scaledResolution.getScaledWidth() / 2 - infoWidth / 2 - 4, scaledResolution.getScaledHeight() / 2 - 26, scaledResolution.getScaledWidth() / 2 + infoWidth / 2 + 4, scaledResolution.getScaledHeight() / 2 - 5, -1610612736);
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(scaledResolution.getScaledWidth() / 2 - 8), (float)(scaledResolution.getScaledHeight() / 2 - 25), (float)(scaledResolution.getScaledWidth() / 2 - 8));
                this.renderItemStack(mc.getThePlayer().getInventory().getMainInventory().get(this.slot), 0, 0);
                GlStateManager.popMatrix();
            }
            GlStateManager.resetColor();
            Fonts.font25.drawCenteredString(info, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 - 36, -1);
        }
        if (counterMode.equalsIgnoreCase("sigma")) {
            GlStateManager.translate((float)0.0f, (float)(-14.0f - this.progress * 4.0f), (float)0.0f);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glColor4f((float)0.15f, (float)0.15f, (float)0.15f, (float)this.progress);
            GL11.glBegin((int)6);
            GL11.glVertex2d((double)(scaledResolution.getScaledWidth() / 2 - 3), (double)(scaledResolution.getScaledHeight() - 60));
            GL11.glVertex2d((double)(scaledResolution.getScaledWidth() / 2), (double)(scaledResolution.getScaledHeight() - 57));
            GL11.glVertex2d((double)(scaledResolution.getScaledWidth() / 2 + 3), (double)(scaledResolution.getScaledHeight() - 60));
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            RenderUtils.drawRoundedRect((float)scaledResolution.getScaledWidth() / 2.0f - (float)(infoWidth / 2) - 4.0f, (float)scaledResolution.getScaledHeight() - 60.0f, (float)scaledResolution.getScaledWidth() / 2.0f + (float)(infoWidth / 2) + 4.0f, (float)scaledResolution.getScaledHeight() - 74.0f, 2, new Color(0.15f, 0.15f, 0.15f, this.progress).getRGB());
            GlStateManager.resetColor();
            Fonts.font25.drawCenteredString(info, (float)(scaledResolution.getScaledWidth() / 2) + 0.1f, scaledResolution.getScaledHeight() - 70, new Color(1.0f, 1.0f, 1.0f, 0.8f * this.progress).getRGB(), false);
            GlStateManager.translate((float)0.0f, (float)(14.0f + this.progress * 4.0f), (float)0.0f);
        }
        if (counterMode.equalsIgnoreCase("novoline")) {
            if (this.slot >= 0 && this.slot < 9 && mc.getThePlayer().getInventory().getMainInventory().get(this.slot) != null && mc.getThePlayer().getInventory().getMainInventory().get(this.slot).getItem() != null && mc.getThePlayer().getInventory().getMainInventory().get(this.slot).getItem() instanceof ItemBlock) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(scaledResolution.getScaledWidth() / 2 - 22), (float)(scaledResolution.getScaledHeight() / 2 + 16), (float)(scaledResolution.getScaledWidth() / 2 - 22));
                this.renderItemStack(mc.getThePlayer().getInventory().getMainInventory().get(this.slot), 0, 0);
                GlStateManager.popMatrix();
            }
            GlStateManager.resetColor();
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + " blocks", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 + 20, -1, true);
        }
    }

    private void renderItemStack(IItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.getFontRendererObj(), stack, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (!((Boolean)this.markValue.get()).booleanValue()) {
            return;
        }
        double yaw = Math.toRadians(mc.getThePlayer().getRotationYaw());
        int x = (Boolean)this.omniDirectionalExpand.get() != false ? (int)Math.round(-Math.sin(yaw)) : mc.getThePlayer().getHorizontalFacing().getDirectionVec().getX();
        int z = (Boolean)this.omniDirectionalExpand.get() != false ? (int)Math.round(Math.cos(yaw)) : mc.getThePlayer().getHorizontalFacing().getDirectionVec().getZ();
        for (int i = 0; i < (((String)this.modeValue.get()).equalsIgnoreCase("Expand") && !this.towerActivation() ? (Integer)this.expandLengthValue.get() + 1 : 2); ++i) {
            WBlockPos WBlockPos2 = new WBlockPos(mc.getThePlayer().getPosX() + (double)(x * i), !this.towerActivation() && ((Boolean)this.sameYValue.get() != false || ((Boolean)this.autoJumpValue.get() != false || (Boolean)this.smartSpeedValue.get() != false && LiquidBounce.moduleManager.getModule(Speed.class).getState()) && mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindJump())) && (double)this.launchY <= mc.getThePlayer().getPosY() ? (double)(this.launchY - 1) : mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0) - (this.shouldGoDown ? 1.0 : 0.0), mc.getThePlayer().getPosZ() + (double)(z * i));
            PlaceInfo placeInfo = PlaceInfo.get(WBlockPos2);
            if (!BlockUtils.isReplaceable(WBlockPos2) || placeInfo == null) continue;
            RenderUtils.drawBlockBox(WBlockPos2, new Color((Integer)this.redValue.get(), (Integer)this.greenValue.get(), (Integer)this.blueValue.get(), (Integer)this.alphaValue.get()), false);
            break;
        }
    }

    private double calcStepSize(double range) {
        double accuracy = ((Integer)this.searchAccuracyValue.get()).intValue();
        if (range / (accuracy += accuracy % 2.0) < 0.01) {
            return 0.01;
        }
        return range / accuracy;
    }

    private boolean search(WBlockPos WBlockPosition, boolean checks) {
        return this.search(WBlockPosition, checks, false);
    }

    private boolean search(WBlockPos blockPosition, boolean checks, boolean towerActive) {
        this.faceBlock = false;
        double xzRV = ((Float)this.xzRangeValue.get()).floatValue();
        double xzSSV = this.calcStepSize(xzRV);
        double yRV = ((Float)this.yRangeValue.get()).floatValue();
        double ySSV = this.calcStepSize(yRV);
        double xSearchFace = 0.0;
        double ySearchFace = 0.0;
        double zSearchFace = 0.0;
        if (!BlockUtils.isReplaceable(blockPosition)) {
            return false;
        }
        boolean staticYawMode = ((String)this.rotationLookupValue.get()).equalsIgnoreCase("AAC") || ((String)this.rotationLookupValue.get()).equalsIgnoreCase("same") && (((String)this.rotationModeValue.get()).equalsIgnoreCase("AAC") || ((String)this.rotationModeValue.get()).contains("Static") && !((String)this.rotationModeValue.get()).equalsIgnoreCase("static3"));
        WVec3 eyesPos = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
        PlaceRotation placeRotation = null;
        for (EnumFacingType facingType : EnumFacingType.values()) {
            IEnumFacing side = classProvider.getEnumFacing(facingType);
            WBlockPos neighbor = blockPosition.offset(side);
            if (!BlockUtils.canBeClicked(neighbor)) continue;
            WVec3 dirVec = new WVec3(side.getDirectionVec());
            for (double xSearch = 0.5 - xzRV / 2.0; xSearch <= 0.5 + xzRV / 2.0; xSearch += xzSSV) {
                for (double ySearch = 0.5 - yRV / 2.0; ySearch <= 0.5 + yRV / 2.0; ySearch += ySSV) {
                    for (double zSearch = 0.5 - xzRV / 2.0; zSearch <= 0.5 + xzRV / 2.0; zSearch += xzSSV) {
                        WVec3 posVec = new WVec3(blockPosition).addVector(xSearch, ySearch, zSearch);
                        double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        WVec3 hitVec = posVec.add(new WVec3(dirVec.getXCoord() * 0.5, dirVec.getYCoord() * 0.5, dirVec.getZCoord() * 0.5));
                        if (checks && (eyesPos.squareDistanceTo(hitVec) > 18.0 || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.getTheWorld().rayTraceBlocks(eyesPos, hitVec, false, true, false) != null)) continue;
                        for (int i = 0; i < (staticYawMode ? 2 : 1); ++i) {
                            Rotation rotation;
                            double diffX = staticYawMode && i == 0 ? 0.0 : hitVec.getXCoord() - eyesPos.getXCoord();
                            double diffY = hitVec.getYCoord() - eyesPos.getYCoord();
                            double diffZ = staticYawMode && i == 1 ? 0.0 : hitVec.getZCoord() - eyesPos.getZCoord();
                            double diffXZ = MathHelper.sqrt((double)(diffX * diffX + diffZ * diffZ));
                            this.lookupRotation = rotation = new Rotation(MathHelper.wrapDegrees((float)((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f)), MathHelper.wrapDegrees((float)((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))))));
                            if (((String)this.rotationModeValue.get()).equalsIgnoreCase("hypixel") && (((Boolean)this.keepRotOnJumpValue.get()).booleanValue() || !mc.getGameSettings().getKeyBindJump().isKeyDown())) {
                                rotation = new Rotation(mc.getThePlayer().getRotationYaw() + (float)(mc.getThePlayer().getMovementInput().getMoveForward() > 0.0f ? 180 : 0) + (float)((Integer)this.HypixelYawValue.get()).intValue(), ((Integer)this.HypixelPitchValue.get()).intValue());
                            }
                            if (((String)this.rotationModeValue.get()).equalsIgnoreCase("static") && (((Boolean)this.keepRotOnJumpValue.get()).booleanValue() || !mc.getGameSettings().getKeyBindJump().isKeyDown())) {
                                rotation = new Rotation(MovementUtils.getScaffoldRotation(mc.getThePlayer().getRotationYaw(), mc.getThePlayer().getMoveForward()), ((Float)this.staticPitchValue.get()).floatValue());
                            }
                            if ((((String)this.rotationModeValue.get()).equalsIgnoreCase("static2") || ((String)this.rotationModeValue.get()).equalsIgnoreCase("static3")) && (((Boolean)this.keepRotOnJumpValue.get()).booleanValue() || !mc.getGameSettings().getKeyBindJump().isKeyDown())) {
                                rotation = new Rotation(rotation.getYaw(), ((Float)this.staticPitchValue.get()).floatValue());
                            }
                            if (((String)this.rotationModeValue.get()).equalsIgnoreCase("custom") && (((Boolean)this.keepRotOnJumpValue.get()).booleanValue() || !mc.getGameSettings().getKeyBindJump().isKeyDown())) {
                                rotation = new Rotation(mc.getThePlayer().getRotationYaw() + ((Float)this.customYawValue.get()).floatValue(), ((Float)this.customPitchValue.get()).floatValue());
                            }
                            WVec3 rotationVector = RotationUtils.getVectorForRotation(((String)this.rotationLookupValue.get()).equalsIgnoreCase("same") ? rotation : this.lookupRotation);
                            WVec3 vector = eyesPos.addVector(rotationVector.getXCoord() * 4.0, rotationVector.getYCoord() * 4.0, rotationVector.getZCoord() * 4.0);
                            IMovingObjectPosition obj = mc.getTheWorld().rayTraceBlocks(eyesPos, vector, false, false, true);
                            if (obj.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK || !obj.getBlockPos().equals(neighbor) || placeRotation != null && !(RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation()))) continue;
                            placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                        }
                    }
                }
            }
        }
        if (placeRotation == null) {
            return false;
        }
        if (((Boolean)this.rotationsValue.get()).booleanValue()) {
            if (((Float)this.minTurnSpeed.get()).floatValue() < 180.0f) {
                Rotation limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, placeRotation.getRotation(), RandomUtils.nextFloat(((Float)this.minTurnSpeed.get()).floatValue(), ((Float)this.maxTurnSpeed.get()).floatValue()));
                if ((int)(10.0f * MathHelper.wrapDegrees((float)limitedRotation.getYaw())) == (int)(10.0f * MathHelper.wrapDegrees((float)placeRotation.getRotation().getYaw())) && (int)(10.0f * MathHelper.wrapDegrees((float)limitedRotation.getPitch())) == (int)(10.0f * MathHelper.wrapDegrees((float)placeRotation.getRotation().getPitch()))) {
                    RotationUtils.setTargetRotation(placeRotation.getRotation(), (Integer)this.keepLengthValue.get());
                    this.lockRotation = placeRotation.getRotation();
                    this.faceBlock = true;
                } else {
                    RotationUtils.setTargetRotation(limitedRotation, (Integer)this.keepLengthValue.get());
                    this.lockRotation = limitedRotation;
                    this.faceBlock = false;
                }
            } else {
                RotationUtils.setTargetRotation(placeRotation.getRotation(), (Integer)this.keepLengthValue.get());
                this.lockRotation = placeRotation.getRotation();
                this.faceBlock = true;
            }
            if (((String)this.rotationLookupValue.get()).equalsIgnoreCase("same")) {
                this.lookupRotation = this.lockRotation;
            }
        }
        if (towerActive) {
            this.towerPlace = placeRotation.getPlaceInfo();
        } else {
            this.targetPlace = placeRotation.getPlaceInfo();
        }
        return true;
    }

    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            IItemStack itemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (itemStack == null || !classProvider.isItemBlock(itemStack.getItem())) continue;
            IBlock block = itemStack.getItem().asItemBlock().getBlock();
            IItemStack heldItem = mc.getThePlayer().getHeldItem();
            if ((heldItem == null || !heldItem.equals(itemStack)) && (InventoryUtils.BLOCK_BLACKLIST.contains(block) || classProvider.isBlockBush(block))) continue;
            amount += itemStack.getStackSize();
        }
        return amount;
    }

    @Override
    public String getTag() {
        return this.towerActivation() ? "Tower, " + (String)this.towerPlaceModeValue.get() : (String)this.placeModeValue.get();
    }
}
