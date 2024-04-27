/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.stats.StatList
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.world;

import java.awt.Color;
import java.util.Objects;
import net.dev.important.Client;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.StrafeEvent;
import net.dev.important.event.TickEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.gui.font.Fonts;
import net.dev.important.injection.access.StaticStorage;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.movement.Speed;
import net.dev.important.utils.InventoryUtils;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.PlaceRotation;
import net.dev.important.utils.PlayerUtils;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.VecRotation;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.block.PlaceInfo;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.render.BlurUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.utils.timer.TickTimer;
import net.dev.important.utils.timer.TimeUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import oh.yalan.NativeClass;
import org.lwjgl.opengl.GL11;

@NativeClass
@Info(name="Scaffold", description="Automatically places blocks beneath your feet.", category=Category.WORLD, cnName="\u65b9\u5757\u98de\u884c")
public class Scaffold
extends Module {
    private final BoolValue towerEnabled = new BoolValue("EnableTower", false);
    private final ListValue towerModeValue = new ListValue("TowerMode", new String[]{"Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4", "Verus"}, "Motion", () -> (Boolean)this.towerEnabled.get());
    private final ListValue towerPlaceModeValue = new ListValue("Tower-PlaceTiming", new String[]{"Pre", "Post"}, "Post");
    private final BoolValue stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false, () -> (Boolean)this.towerEnabled.get());
    private final BoolValue onJumpValue = new BoolValue("OnJump", false, () -> (Boolean)this.towerEnabled.get());
    private final BoolValue noMoveOnlyValue = new BoolValue("NoMove", true, () -> (Boolean)this.towerEnabled.get());
    private final FloatValue towerTimerValue = new FloatValue("TowerTimer", 1.0f, 0.1f, 10.0f, () -> (Boolean)this.towerEnabled.get());
    private final FloatValue jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("Jump"));
    private final IntegerValue jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("Jump"));
    private final FloatValue constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("ConstantMotion"));
    private final FloatValue constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("ConstantMotion"));
    private final FloatValue teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("Teleport"));
    private final IntegerValue teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("Teleport"));
    private final BoolValue teleportGroundValue = new BoolValue("TeleportGround", true, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("Teleport"));
    private final BoolValue teleportNoMotionValue = new BoolValue("TeleportNoMotion", false, () -> (Boolean)this.towerEnabled.get() != false && ((String)this.towerModeValue.get()).equalsIgnoreCase("Teleport"));
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");
    private final BoolValue placeableDelay = new BoolValue("PlaceableDelay", false);
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000, "ms"){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)Scaffold.this.minDelayValue.get();
            if (i > newValue) {
                this.set(i);
            }
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000, "ms"){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)Scaffold.this.maxDelayValue.get();
            if (i < newValue) {
                this.set(i);
            }
        }
    };
    private final BoolValue smartDelay = new BoolValue("SmartDelay", true);
    private final ListValue autoBlockMode = new ListValue("AutoBlock", new String[]{"Spoof", "Switch", "Off"}, "Spoof");
    private final BoolValue stayAutoBlock = new BoolValue("StayAutoBlock", false, () -> !((String)this.autoBlockMode.get()).equalsIgnoreCase("off"));
    public final ListValue sprintModeValue = new ListValue("SprintMode", new String[]{"Same", "Ground", "Air", "Off"}, "Off");
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue downValue = new BoolValue("Down", false);
    private final BoolValue searchValue = new BoolValue("Search", true);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
    private final BoolValue eagleValue = new BoolValue("Eagle", false);
    private final BoolValue eagleSilentValue = new BoolValue("EagleSilent", false, () -> (Boolean)this.eagleValue.get());
    private final IntegerValue blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10, () -> (Boolean)this.eagleValue.get());
    private final FloatValue eagleEdgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2f, 0.0f, 0.5f, "m", () -> (Boolean)this.eagleValue.get());
    private final IntegerValue expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6, " blocks", () -> ((String)this.modeValue.get()).equalsIgnoreCase("expand"));
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue noHitCheckValue = new BoolValue("NoHitCheck", false, () -> (Boolean)this.rotationsValue.get());
    public final ListValue rotationModeValue = new ListValue("RotationMode", new String[]{"Normal", "AAC", "Static", "Static2", "Static3", "Custom"}, "Normal");
    private final FloatValue maxTurnSpeed = new FloatValue("MaxTurnSpeed", 180.0f, 0.0f, 180.0f, "\u00b0", () -> (Boolean)this.rotationsValue.get()){

        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float i = ((Float)Scaffold.this.minTurnSpeed.get()).floatValue();
            if (i > newValue.floatValue()) {
                this.set(Float.valueOf(i));
            }
        }
    };
    private final FloatValue minTurnSpeed = new FloatValue("MinTurnSpeed", 180.0f, 0.0f, 180.0f, "\u00b0", () -> (Boolean)this.rotationsValue.get()){

        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float i = ((Float)Scaffold.this.maxTurnSpeed.get()).floatValue();
            if (i < newValue.floatValue()) {
                this.set(Float.valueOf(i));
            }
        }
    };
    private final FloatValue staticPitchValue = new FloatValue("Static-Pitch", 86.0f, 80.0f, 90.0f, "\u00b0", () -> ((String)this.rotationModeValue.get()).toLowerCase().startsWith("static"));
    private final FloatValue customYawValue = new FloatValue("Custom-Yaw", 135.0f, -180.0f, 180.0f, "\u00b0", () -> ((String)this.rotationModeValue.get()).equalsIgnoreCase("custom"));
    private final FloatValue customPitchValue = new FloatValue("Custom-Pitch", 86.0f, -90.0f, 90.0f, "\u00b0", () -> ((String)this.rotationModeValue.get()).equalsIgnoreCase("custom"));
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false, () -> (Boolean)this.rotationsValue.get());
    private final IntegerValue keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20, () -> (Boolean)this.rotationsValue.get() != false && (Boolean)this.keepRotationValue.get() == false);
    private final ListValue placeConditionValue = new ListValue("Place-Condition", new String[]{"Air", "FallDown", "NegativeMotion", "Always"}, "Always");
    private final BoolValue rotationStrafeValue = new BoolValue("RotationStrafe", false);
    private final BoolValue zitterValue = new BoolValue("Zitter", false, () -> !this.isTowerOnly());
    private final ListValue zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Teleport", () -> !this.isTowerOnly() && (Boolean)this.zitterValue.get() != false);
    private final FloatValue zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f, () -> !this.isTowerOnly() && (Boolean)this.zitterValue.get() != false && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport"));
    private final FloatValue zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f, () -> !this.isTowerOnly() && (Boolean)this.zitterValue.get() != false && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport"));
    private final IntegerValue zitterDelay = new IntegerValue("ZitterDelay", 100, 0, 500, "ms", () -> !this.isTowerOnly() && (Boolean)this.zitterValue.get() != false && ((String)this.zitterModeValue.get()).equalsIgnoreCase("smooth"));
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f, () -> !this.isTowerOnly());
    public final FloatValue speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f, "x");
    public final FloatValue xzMultiplier = new FloatValue("XZ-Multiplier", 1.0f, 0.0f, 4.0f, "x");
    private final BoolValue customSpeedValue = new BoolValue("CustomSpeed", false);
    private final FloatValue customMoveSpeedValue = new FloatValue("CustomMoveSpeed", 0.3f, 0.0f, 5.0f, () -> (Boolean)this.customSpeedValue.get());
    private final BoolValue sameYValue = new BoolValue("SameY", false, () -> (Boolean)this.towerEnabled.get() == false);
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false, () -> !this.isTowerOnly());
    private final BoolValue smartSpeedValue = new BoolValue("SmartSpeed", false, () -> !this.isTowerOnly());
    private final BoolValue safeWalkValue = new BoolValue("SafeWalk", true);
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false, () -> (Boolean)this.safeWalkValue.get());
    private final BoolValue autoDisableSpeedValue = new BoolValue("AutoDisable-Speed", true);
    public final ListValue counterDisplayValue = new ListValue("Counter", new String[]{"Off", "Simple", "Advanced", "Sigma", "Novoline"}, "Simple");
    private final BoolValue markValue = new BoolValue("Mark", false);
    private final IntegerValue redValue = new IntegerValue("Red", 0, 0, 255, () -> (Boolean)this.markValue.get());
    private final IntegerValue greenValue = new IntegerValue("Green", 120, 0, 255, () -> (Boolean)this.markValue.get());
    private final IntegerValue blueValue = new IntegerValue("Blue", 255, 0, 255, () -> (Boolean)this.markValue.get());
    private final IntegerValue alphaValue = new IntegerValue("Alpha", 120, 0, 255, () -> (Boolean)this.markValue.get());
    private final BoolValue blurValue = new BoolValue("Blur-Advanced", false, () -> ((String)this.counterDisplayValue.get()).equalsIgnoreCase("advanced"));
    private final FloatValue blurStrength = new FloatValue("Blur-Strength", 1.0f, 0.0f, 30.0f, "x", () -> ((String)this.counterDisplayValue.get()).equalsIgnoreCase("advanced"));
    private PlaceInfo targetPlace;
    private PlaceInfo towerPlace;
    private int launchY;
    private boolean faceBlock;
    private Rotation lockRotation;
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
        return !((Boolean)this.towerEnabled.get() == false || (Boolean)this.onJumpValue.get() != false && !Scaffold.mc.field_71474_y.field_74314_A.func_151470_d() || (Boolean)this.noMoveOnlyValue.get() != false && MovementUtils.isMoving());
    }

    @Override
    public void onEnable() {
        if (PlayerUtils.checkBlock()) {
            this.setState(false);
            Client.hud.addNotification(new Notification("There are no blocks in the inventory", Notification.Type.WARNING));
        }
        if (Scaffold.mc.field_71439_g == null) {
            return;
        }
        this.progress = 0.0f;
        this.launchY = (int)Scaffold.mc.field_71439_g.field_70163_u;
        this.lastSlot = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
        this.slot = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
        if (((Boolean)this.autoDisableSpeedValue.get()).booleanValue() && Objects.requireNonNull(Client.moduleManager.getModule(Speed.class)).getState()) {
            Client.moduleManager.getModule(Speed.class).setState(false);
            Client.hud.addNotification(new Notification("Speed is disabled to prevent flags/errors.", Notification.Type.WARNING));
        }
        this.faceBlock = false;
        this.lastMS = System.currentTimeMillis();
    }

    private void fakeJump() {
        Scaffold.mc.field_71439_g.field_70160_al = true;
        Scaffold.mc.field_71439_g.func_71029_a(StatList.field_75953_u);
    }

    private void move(MotionEvent event) {
        switch (((String)this.towerModeValue.get()).toLowerCase()) {
            case "jump": {
                if (!Scaffold.mc.field_71439_g.field_70122_E || !this.timer.hasTimePassed((Integer)this.jumpDelayValue.get())) break;
                this.fakeJump();
                Scaffold.mc.field_71439_g.field_70181_x = ((Float)this.jumpMotionValue.get()).floatValue();
                this.timer.reset();
                break;
            }
            case "motion": {
                if (Scaffold.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.mc.field_71439_g.field_70181_x = 0.42;
                    break;
                }
                if (!(Scaffold.mc.field_71439_g.field_70181_x < 0.1)) break;
                Scaffold.mc.field_71439_g.field_70181_x = -0.3;
                break;
            }
            case "motiontp": {
                if (Scaffold.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.mc.field_71439_g.field_70181_x = 0.42;
                    break;
                }
                if (!(Scaffold.mc.field_71439_g.field_70181_x < 0.23)) break;
                Scaffold.mc.field_71439_g.func_70107_b(Scaffold.mc.field_71439_g.field_70165_t, (double)((int)Scaffold.mc.field_71439_g.field_70163_u), Scaffold.mc.field_71439_g.field_70161_v);
                break;
            }
            case "packet": {
                if (!Scaffold.mc.field_71439_g.field_70122_E || !this.timer.hasTimePassed(2)) break;
                this.fakeJump();
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u + 0.42, Scaffold.mc.field_71439_g.field_70161_v, false));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u + 0.76, Scaffold.mc.field_71439_g.field_70161_v, false));
                Scaffold.mc.field_71439_g.func_70107_b(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u + 1.08, Scaffold.mc.field_71439_g.field_70161_v);
                this.timer.reset();
                break;
            }
            case "teleport": {
                if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                    Scaffold.mc.field_71439_g.field_70181_x = 0.0;
                }
                if (!Scaffold.mc.field_71439_g.field_70122_E && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed((Integer)this.teleportDelayValue.get())) break;
                this.fakeJump();
                Scaffold.mc.field_71439_g.func_70634_a(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u + (double)((Float)this.teleportHeightValue.get()).floatValue(), Scaffold.mc.field_71439_g.field_70161_v);
                this.timer.reset();
                break;
            }
            case "constantmotion": {
                if (Scaffold.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    this.jumpGround = Scaffold.mc.field_71439_g.field_70163_u;
                    Scaffold.mc.field_71439_g.field_70181_x = ((Float)this.constantMotionValue.get()).floatValue();
                }
                if (!(Scaffold.mc.field_71439_g.field_70163_u > this.jumpGround + (double)((Float)this.constantMotionJumpGroundValue.get()).floatValue())) break;
                this.fakeJump();
                Scaffold.mc.field_71439_g.func_70107_b(Scaffold.mc.field_71439_g.field_70165_t, (double)((int)Scaffold.mc.field_71439_g.field_70163_u), Scaffold.mc.field_71439_g.field_70161_v);
                Scaffold.mc.field_71439_g.field_70181_x = ((Float)this.constantMotionValue.get()).floatValue();
                this.jumpGround = Scaffold.mc.field_71439_g.field_70163_u;
                break;
            }
            case "aac3.3.9": {
                if (Scaffold.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.mc.field_71439_g.field_70181_x = 0.4001;
                }
                Scaffold.mc.field_71428_T.field_74278_d = 1.0f;
                if (!(Scaffold.mc.field_71439_g.field_70181_x < 0.0)) break;
                Scaffold.mc.field_71439_g.field_70181_x -= 9.45E-6;
                Scaffold.mc.field_71428_T.field_74278_d = 1.6f;
                break;
            }
            case "aac3.6.4": {
                if (Scaffold.mc.field_71439_g.field_70173_aa % 4 == 1) {
                    Scaffold.mc.field_71439_g.field_70181_x = 0.4195464;
                    Scaffold.mc.field_71439_g.func_70107_b(Scaffold.mc.field_71439_g.field_70165_t - 0.035, Scaffold.mc.field_71439_g.field_70163_u, Scaffold.mc.field_71439_g.field_70161_v);
                    break;
                }
                if (Scaffold.mc.field_71439_g.field_70173_aa % 4 != 0) break;
                Scaffold.mc.field_71439_g.field_70181_x = -0.5;
                Scaffold.mc.field_71439_g.func_70107_b(Scaffold.mc.field_71439_g.field_70165_t + 0.035, Scaffold.mc.field_71439_g.field_70163_u, Scaffold.mc.field_71439_g.field_70161_v);
                break;
            }
            case "verus": {
                if (!Scaffold.mc.field_71441_e.func_72945_a((Entity)Scaffold.mc.field_71439_g, Scaffold.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -0.01, 0.0)).isEmpty() && Scaffold.mc.field_71439_g.field_70122_E && Scaffold.mc.field_71439_g.field_70124_G) {
                    this.verusState = 0;
                    this.verusJumped = true;
                }
                if (this.verusJumped) {
                    MovementUtils.strafe();
                    switch (this.verusState) {
                        case 0: {
                            this.fakeJump();
                            Scaffold.mc.field_71439_g.field_70181_x = 0.42f;
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
                            Scaffold.mc.field_71439_g.field_70181_x = 0.0;
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
            Scaffold.mc.field_71474_y.field_74311_E.field_74513_e = false;
            Scaffold.mc.field_71439_g.func_70031_b(false);
            return;
        }
        Scaffold.mc.field_71428_T.field_74278_d = ((Float)this.timerValue.get()).floatValue();
        boolean bl = this.shouldGoDown = (Boolean)this.downValue.get() != false && (Boolean)this.sameYValue.get() == false && GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74311_E) && this.getBlocksAmount() > 1;
        if (this.shouldGoDown) {
            Scaffold.mc.field_71474_y.field_74311_E.field_74513_e = false;
        }
        if (((Boolean)this.customSpeedValue.get()).booleanValue()) {
            MovementUtils.strafe(((Float)this.customMoveSpeedValue.get()).floatValue());
        }
        if (Scaffold.mc.field_71439_g.field_70122_E) {
            String mode = (String)this.modeValue.get();
            if (mode.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2f);
                Scaffold.mc.field_71439_g.field_70181_x = 0.0;
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("smooth")) {
                if (!GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74366_z)) {
                    Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = false;
                }
                if (!GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74370_x)) {
                    Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = false;
                }
                if (this.zitterTimer.hasTimePassed(((Integer)this.zitterDelay.get()).intValue())) {
                    this.zitterDirection = !this.zitterDirection;
                    this.zitterTimer.reset();
                }
                if (this.zitterDirection) {
                    Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = true;
                    Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = false;
                } else {
                    Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = false;
                    Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = true;
                }
            }
            if (((Boolean)this.eagleValue.get()).booleanValue() && !this.shouldGoDown) {
                double dif = 0.5;
                if (((Float)this.eagleEdgeDistanceValue.get()).floatValue() > 0.0f) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockPos = new BlockPos(Scaffold.mc.field_71439_g.field_70165_t + (double)(i == 0 ? -1 : (i == 1 ? 1 : 0)), Scaffold.mc.field_71439_g.field_70163_u - (Scaffold.mc.field_71439_g.field_70163_u == (double)((int)Scaffold.mc.field_71439_g.field_70163_u) + 0.5 ? 0.0 : 1.0), Scaffold.mc.field_71439_g.field_70161_v + (double)(i == 2 ? -1 : (i == 3 ? 1 : 0)));
                        PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                        if (!BlockUtils.isReplaceable(blockPos) || placeInfo == null) continue;
                        double calcDif = i > 1 ? Scaffold.mc.field_71439_g.field_70161_v - (double)blockPos.func_177952_p() : Scaffold.mc.field_71439_g.field_70165_t - (double)blockPos.func_177958_n();
                        if ((calcDif -= 0.5) < 0.0) {
                            calcDif *= -1.0;
                        }
                        if (!((calcDif -= 0.5) < dif)) continue;
                        dif = calcDif;
                    }
                }
                if (this.placedBlocksWithoutEagle >= (Integer)this.blocksToEagleValue.get()) {
                    boolean shouldEagle;
                    boolean bl2 = shouldEagle = Scaffold.mc.field_71441_e.func_180495_p(new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - 1.0, Scaffold.mc.field_71439_g.field_70161_v)).func_177230_c() == Blocks.field_150350_a || dif < (double)((Float)this.eagleEdgeDistanceValue.get()).floatValue();
                    if (((Boolean)this.eagleSilentValue.get()).booleanValue()) {
                        if (this.eagleSneaking != shouldEagle) {
                            mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Scaffold.mc.field_71439_g, shouldEagle ? C0BPacketEntityAction.Action.START_SNEAKING : C0BPacketEntityAction.Action.STOP_SNEAKING));
                        }
                        this.eagleSneaking = shouldEagle;
                    } else {
                        Scaffold.mc.field_71474_y.field_74311_E.field_74513_e = shouldEagle;
                    }
                    this.placedBlocksWithoutEagle = 0;
                } else {
                    ++this.placedBlocksWithoutEagle;
                }
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(((Float)this.zitterSpeed.get()).floatValue());
                double yaw = Math.toRadians((double)Scaffold.mc.field_71439_g.field_70177_z + (this.zitterDirection ? 90.0 : -90.0));
                Scaffold.mc.field_71439_g.field_70159_w -= Math.sin(yaw) * (double)((Float)this.zitterStrength.get()).floatValue();
                Scaffold.mc.field_71439_g.field_70179_y += Math.cos(yaw) * (double)((Float)this.zitterStrength.get()).floatValue();
                boolean bl3 = this.zitterDirection = !this.zitterDirection;
            }
        }
        if (((String)this.sprintModeValue.get()).equalsIgnoreCase("off") || ((String)this.sprintModeValue.get()).equalsIgnoreCase("ground") && !Scaffold.mc.field_71439_g.field_70122_E || ((String)this.sprintModeValue.get()).equalsIgnoreCase("air") && Scaffold.mc.field_71439_g.field_70122_E) {
            Scaffold.mc.field_71439_g.func_70031_b(false);
        }
        if (this.shouldGoDown) {
            this.launchY = (int)Scaffold.mc.field_71439_g.field_70163_u - 1;
        } else if (!((Boolean)this.sameYValue.get()).booleanValue()) {
            if (!((Boolean)this.autoJumpValue.get()).booleanValue() && (!((Boolean)this.smartSpeedValue.get()).booleanValue() || !Client.moduleManager.getModule(Speed.class).getState()) || GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74314_A) || Scaffold.mc.field_71439_g.field_70163_u < (double)this.launchY) {
                this.launchY = (int)Scaffold.mc.field_71439_g.field_70163_u;
            }
            if (((Boolean)this.autoJumpValue.get()).booleanValue() && !Client.moduleManager.getModule(Speed.class).getState() && MovementUtils.isMoving() && Scaffold.mc.field_71439_g.field_70122_E && Scaffold.mc.field_71439_g.field_70773_bE == 0) {
                Scaffold.mc.field_71439_g.func_70664_aZ();
                Scaffold.mc.field_71439_g.field_70773_bE = 10;
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (Scaffold.mc.field_71439_g == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.func_149614_c();
        }
    }

    @EventTarget
    public void onStrafe(StrafeEvent event) {
        if (this.lockRotation != null && ((Boolean)this.rotationStrafeValue.get()).booleanValue()) {
            float f;
            int dif = (int)((MathHelper.func_76142_g((float)(Scaffold.mc.field_71439_g.field_70177_z - this.lockRotation.getYaw() - 23.5f - 135.0f)) + 180.0f) / 45.0f);
            float yaw = this.lockRotation.getYaw();
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
                if ((f = MathHelper.func_76129_c((float)f)) < 1.0f) {
                    f = 1.0f;
                }
                f = friction / f;
                float yawSin = MathHelper.func_76126_a((float)((float)((double)yaw * Math.PI / 180.0)));
                float yawCos = MathHelper.func_76134_b((float)((float)((double)yaw * Math.PI / 180.0)));
                Scaffold.mc.field_71439_g.field_70159_w += (double)((calcStrafe *= f) * yawCos - (calcForward *= f) * yawSin);
                Scaffold.mc.field_71439_g.field_70179_y += (double)(calcForward * yawCos + calcStrafe * yawSin);
            }
            event.cancelEvent();
        }
    }

    private boolean shouldPlace() {
        boolean placeWhenAir = ((String)this.placeConditionValue.get()).equalsIgnoreCase("air");
        boolean placeWhenFall = ((String)this.placeConditionValue.get()).equalsIgnoreCase("falldown");
        boolean placeWhenNegativeMotion = ((String)this.placeConditionValue.get()).equalsIgnoreCase("negativemotion");
        boolean alwaysPlace = ((String)this.placeConditionValue.get()).equalsIgnoreCase("always");
        return this.towerActivation() || alwaysPlace || placeWhenAir && !Scaffold.mc.field_71439_g.field_70122_E || placeWhenFall && Scaffold.mc.field_71439_g.field_70143_R > 0.0f || placeWhenNegativeMotion && Scaffold.mc.field_71439_g.field_70181_x < 0.0;
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        Scaffold.mc.field_71439_g.field_70159_w *= (double)((Float)this.xzMultiplier.get()).floatValue();
        Scaffold.mc.field_71439_g.field_70179_y *= (double)((Float)this.xzMultiplier.get()).floatValue();
        if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
            RotationUtils.setTargetRotation(RotationUtils.limitAngleChange(RotationUtils.serverRotation, this.lockRotation, RandomUtils.nextFloat(((Float)this.minTurnSpeed.get()).floatValue(), ((Float)this.maxTurnSpeed.get()).floatValue())));
        }
        String mode = (String)this.modeValue.get();
        EventState eventState = event.getEventState();
        if ((!((Boolean)this.rotationsValue.get()).booleanValue() || ((Boolean)this.noHitCheckValue.get()).booleanValue() || this.faceBlock) && ((String)this.placeModeValue.get()).equalsIgnoreCase(eventState.getStateName()) && !this.towerActivation()) {
            this.place(false);
        }
        if (eventState == EventState.PRE && !this.towerActivation()) {
            if (!this.shouldPlace() || (!((String)this.autoBlockMode.get()).equalsIgnoreCase("Off") ? InventoryUtils.findAutoBlockBlock() == -1 : Scaffold.mc.field_71439_g.func_70694_bm() == null || !(Scaffold.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock))) {
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
        Scaffold.mc.field_71428_T.field_74278_d = ((Float)this.towerTimerValue.get()).floatValue();
        if (((String)this.towerPlaceModeValue.get()).equalsIgnoreCase(eventState.getStateName())) {
            this.place(true);
        }
        if (eventState == EventState.PRE) {
            boolean isHeldItemBlock;
            this.towerPlace = null;
            this.timer.update();
            boolean bl = isHeldItemBlock = Scaffold.mc.field_71439_g.func_70694_bm() != null && Scaffold.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock;
            if (InventoryUtils.findAutoBlockBlock() != -1 || isHeldItemBlock) {
                VecRotation vecRotation;
                BlockPos blockPos;
                this.launchY = (int)Scaffold.mc.field_71439_g.field_70163_u;
                if (((String)this.towerModeValue.get()).equalsIgnoreCase("verus") || !((Boolean)this.stopWhenBlockAbove.get()).booleanValue() || BlockUtils.getBlock(new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u + 2.0, Scaffold.mc.field_71439_g.field_70161_v)) instanceof BlockAir) {
                    this.move(event);
                }
                if (Scaffold.mc.field_71441_e.func_180495_p(blockPos = new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - 1.0, Scaffold.mc.field_71439_g.field_70161_v)).func_177230_c() instanceof BlockAir && this.search(blockPos, true, true) && ((Boolean)this.rotationsValue.get()).booleanValue() && (vecRotation = RotationUtils.faceBlock(blockPos)) != null) {
                    RotationUtils.setTargetRotation(RotationUtils.limitAngleChange(RotationUtils.serverRotation, vecRotation.getRotation(), RandomUtils.nextFloat(((Float)this.minTurnSpeed.get()).floatValue(), ((Float)this.maxTurnSpeed.get()).floatValue())));
                    this.towerPlace.setVec3(vecRotation.getVec());
                }
            }
        }
    }

    private void findBlock(boolean expand) {
        block5: {
            BlockPos blockPosition;
            block4: {
                BlockPos blockPos = this.shouldGoDown ? (Scaffold.mc.field_71439_g.field_70163_u == (double)((int)Scaffold.mc.field_71439_g.field_70163_u) + 0.5 ? new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - 0.6, Scaffold.mc.field_71439_g.field_70161_v) : new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - 0.6, Scaffold.mc.field_71439_g.field_70161_v).func_177977_b()) : (!this.towerActivation() && ((Boolean)this.sameYValue.get() != false || ((Boolean)this.autoJumpValue.get() != false || (Boolean)this.smartSpeedValue.get() != false && Client.moduleManager.getModule(Speed.class).getState()) && !GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74314_A)) && (double)this.launchY <= Scaffold.mc.field_71439_g.field_70163_u ? new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, (double)(this.launchY - 1), Scaffold.mc.field_71439_g.field_70161_v) : (blockPosition = Scaffold.mc.field_71439_g.field_70163_u == (double)((int)Scaffold.mc.field_71439_g.field_70163_u) + 0.5 ? new BlockPos((Entity)Scaffold.mc.field_71439_g) : new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u, Scaffold.mc.field_71439_g.field_70161_v).func_177977_b()));
                if (!(expand || BlockUtils.isReplaceable(blockPosition) && !this.search(blockPosition, !this.shouldGoDown, false))) {
                    return;
                }
                if (!expand) break block4;
                for (int i = 0; i < (Integer)this.expandLengthValue.get(); ++i) {
                    if (!this.search(blockPosition.func_177982_a(Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.WEST ? -i : (Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.EAST ? i : 0), 0, Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.NORTH ? -i : (Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.SOUTH ? i : 0)), false, false)) continue;
                    return;
                }
                break block5;
            }
            if (!((Boolean)this.searchValue.get()).booleanValue()) break block5;
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    if (!this.search(blockPosition.func_177982_a(x, 0, z), !this.shouldGoDown, false)) continue;
                    return;
                }
            }
        }
    }

    private void place(boolean towerActive) {
        Block block;
        if ((towerActive ? this.towerPlace : this.targetPlace) == null) {
            if (((Boolean)this.placeableDelay.get()).booleanValue()) {
                this.delayTimer.reset();
            }
            return;
        }
        if (!this.towerActivation() && (!this.delayTimer.hasTimePassed(this.delay) || ((Boolean)this.smartDelay.get()).booleanValue() && Scaffold.mc.field_71467_ac > 0 || (((Boolean)this.sameYValue.get()).booleanValue() || (((Boolean)this.autoJumpValue.get()).booleanValue() || ((Boolean)this.smartSpeedValue.get()).booleanValue() && Client.moduleManager.getModule(Speed.class).getState()) && !GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74314_A)) && this.launchY - 1 != (int)(towerActive ? this.towerPlace : this.targetPlace).getVec3().field_72448_b)) {
            return;
        }
        int blockSlot = -1;
        ItemStack itemStack = Scaffold.mc.field_71439_g.func_70694_bm();
        if (Scaffold.mc.field_71439_g.func_70694_bm() == null || !(Scaffold.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock)) {
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Off")) {
                return;
            }
            blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1) {
                return;
            }
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Spoof")) {
                mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                itemStack = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(blockSlot).func_75211_c();
            } else {
                Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = blockSlot - 36;
                Scaffold.mc.field_71442_b.func_78765_e();
            }
        }
        if (itemStack != null && itemStack.func_77973_b() != null && itemStack.func_77973_b() instanceof ItemBlock && (InventoryUtils.BLOCK_BLACKLIST.contains(block = ((ItemBlock)itemStack.func_77973_b()).func_179223_d()) || !block.func_149686_d())) {
            return;
        }
        if (Scaffold.mc.field_71442_b.func_178890_a(Scaffold.mc.field_71439_g, Scaffold.mc.field_71441_e, itemStack, (towerActive ? this.towerPlace : this.targetPlace).getBlockPos(), (towerActive ? this.towerPlace : this.targetPlace).getEnumFacing(), (towerActive ? this.towerPlace : this.targetPlace).getVec3())) {
            this.delayTimer.reset();
            long l = this.delay = (Boolean)this.placeableDelay.get() == false ? 0L : TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            if (Scaffold.mc.field_71439_g.field_70122_E) {
                float modifier = ((Float)this.speedModifierValue.get()).floatValue();
                Scaffold.mc.field_71439_g.field_70159_w *= (double)modifier;
                Scaffold.mc.field_71439_g.field_70179_y *= (double)modifier;
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                Scaffold.mc.field_71439_g.func_71038_i();
            } else {
                mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
            }
        }
        if (towerActive) {
            this.towerPlace = null;
        } else {
            this.targetPlace = null;
        }
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && blockSlot >= 0 && !((String)this.autoBlockMode.get()).equalsIgnoreCase("Switch")) {
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Scaffold.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }

    @Override
    public void onDisable() {
        if (Scaffold.mc.field_71439_g == null) {
            return;
        }
        if (!GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74311_E)) {
            Scaffold.mc.field_71474_y.field_74311_E.field_74513_e = false;
            if (this.eagleSneaking) {
                mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Scaffold.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        if (!GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74366_z)) {
            Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74370_x)) {
            Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = false;
        }
        this.lockRotation = null;
        Scaffold.mc.field_71428_T.field_74278_d = 1.0f;
        this.shouldGoDown = false;
        this.faceBlock = false;
        if (this.lastSlot != Scaffold.mc.field_71439_g.field_71071_by.field_70461_c && ((String)this.autoBlockMode.get()).equalsIgnoreCase("switch")) {
            Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = this.lastSlot;
            Scaffold.mc.field_71442_b.func_78765_e();
        }
        if (this.slot != Scaffold.mc.field_71439_g.field_71071_by.field_70461_c && ((String)this.autoBlockMode.get()).equalsIgnoreCase("spoof")) {
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Scaffold.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown) {
            return;
        }
        if (((Boolean)this.airSafeValue.get()).booleanValue() || Scaffold.mc.field_71439_g.field_70122_E) {
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
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        String info = this.getBlocksAmount() + " blocks";
        int infoWidth = Fonts.fontSFUI40.func_78256_a(info);
        int infoWidth2 = Fonts.minecraftFont.func_78256_a(this.getBlocksAmount() + "");
        if (counterMode.equalsIgnoreCase("simple")) {
            Fonts.minecraftFont.func_175065_a(this.getBlocksAmount() + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2 - 1), (float)(scaledResolution.func_78328_b() / 2 - 36), -16777216, false);
            Fonts.minecraftFont.func_175065_a(this.getBlocksAmount() + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2 + 1), (float)(scaledResolution.func_78328_b() / 2 - 36), -16777216, false);
            Fonts.minecraftFont.func_175065_a(this.getBlocksAmount() + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 35), -16777216, false);
            Fonts.minecraftFont.func_175065_a(this.getBlocksAmount() + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 37), -16777216, false);
            Fonts.minecraftFont.func_175065_a(this.getBlocksAmount() + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 36), -1, false);
        }
        if (counterMode.equalsIgnoreCase("advanced")) {
            boolean canRenderStack;
            boolean bl = canRenderStack = this.slot >= 0 && this.slot < 9 && Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot] != null && Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot].func_77973_b() != null && Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot].func_77973_b() instanceof ItemBlock;
            if (((Boolean)this.blurValue.get()).booleanValue()) {
                BlurUtils.blurArea(scaledResolution.func_78326_a() / 2 - infoWidth / 2 - 4, scaledResolution.func_78328_b() / 2 - 39, scaledResolution.func_78326_a() / 2 + infoWidth / 2 + 4, scaledResolution.func_78328_b() / 2 - (canRenderStack ? 5 : 26), ((Float)this.blurStrength.get()).floatValue());
            }
            RenderUtils.drawRect((float)(scaledResolution.func_78326_a() / 2 - infoWidth / 2 - 4), (float)(scaledResolution.func_78328_b() / 2 - 40), (float)(scaledResolution.func_78326_a() / 2 + infoWidth / 2 + 4), (float)(scaledResolution.func_78328_b() / 2 - 39), this.getBlocksAmount() > 1 ? -1 : -61424);
            RenderUtils.drawRect((float)(scaledResolution.func_78326_a() / 2 - infoWidth / 2 - 4), (float)(scaledResolution.func_78328_b() / 2 - 39), (float)(scaledResolution.func_78326_a() / 2 + infoWidth / 2 + 4), (float)(scaledResolution.func_78328_b() / 2 - 26), -1610612736);
            if (canRenderStack) {
                RenderUtils.drawRect((float)(scaledResolution.func_78326_a() / 2 - infoWidth / 2 - 4), (float)(scaledResolution.func_78328_b() / 2 - 26), (float)(scaledResolution.func_78326_a() / 2 + infoWidth / 2 + 4), (float)(scaledResolution.func_78328_b() / 2 - 5), -1610612736);
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)(scaledResolution.func_78326_a() / 2 - 8), (float)(scaledResolution.func_78328_b() / 2 - 25), (float)(scaledResolution.func_78326_a() / 2 - 8));
                this.renderItemStack(Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot], 0, 0);
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179117_G();
            Fonts.fontSFUI40.drawCenteredString(info, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 2 - 36, -1);
        }
        if (counterMode.equalsIgnoreCase("sigma")) {
            GlStateManager.func_179109_b((float)0.0f, (float)(-14.0f - this.progress * 4.0f), (float)0.0f);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glColor4f((float)0.15f, (float)0.15f, (float)0.15f, (float)this.progress);
            GL11.glBegin((int)6);
            GL11.glVertex2d((double)(scaledResolution.func_78326_a() / 2 - 3), (double)(scaledResolution.func_78328_b() - 60));
            GL11.glVertex2d((double)(scaledResolution.func_78326_a() / 2), (double)(scaledResolution.func_78328_b() - 57));
            GL11.glVertex2d((double)(scaledResolution.func_78326_a() / 2 + 3), (double)(scaledResolution.func_78328_b() - 60));
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            RenderUtils.drawRoundedRect(scaledResolution.func_78326_a() / 2 - infoWidth / 2 - 4, scaledResolution.func_78328_b() - 60, scaledResolution.func_78326_a() / 2 + infoWidth / 2 + 4, scaledResolution.func_78328_b() - 74, 2.0f, new Color(0.15f, 0.15f, 0.15f, this.progress).getRGB());
            GlStateManager.func_179117_G();
            Fonts.fontSFUI35.drawCenteredString(info, (float)(scaledResolution.func_78326_a() / 2) + 0.1f, scaledResolution.func_78328_b() - 70, new Color(1.0f, 1.0f, 1.0f, 0.8f * this.progress).getRGB(), false);
            GlStateManager.func_179109_b((float)0.0f, (float)(14.0f + this.progress * 4.0f), (float)0.0f);
        }
        if (counterMode.equalsIgnoreCase("novoline")) {
            if (this.slot >= 0 && this.slot < 9 && Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot] != null && Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot].func_77973_b() != null && Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot].func_77973_b() instanceof ItemBlock) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)(scaledResolution.func_78326_a() / 2 - 22), (float)(scaledResolution.func_78328_b() / 2 + 16), (float)(scaledResolution.func_78326_a() / 2 - 22));
                this.renderItemStack(Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[this.slot], 0, 0);
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179117_G();
            Fonts.minecraftFont.func_175065_a(this.getBlocksAmount() + " blocks", (float)(scaledResolution.func_78326_a() / 2), (float)(scaledResolution.func_78328_b() / 2 + 20), -1, true);
        }
    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderHelper.func_74520_c();
        mc.func_175599_af().func_180450_b(stack, x, y);
        mc.func_175599_af().func_175030_a(Scaffold.mc.field_71466_p, stack, x, y);
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (!((Boolean)this.markValue.get()).booleanValue()) {
            return;
        }
        for (int i = 0; i < (((String)this.modeValue.get()).equalsIgnoreCase("Expand") && !this.towerActivation() ? (Integer)this.expandLengthValue.get() + 1 : 2); ++i) {
            BlockPos blockPos = new BlockPos(Scaffold.mc.field_71439_g.field_70165_t + (double)(Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.WEST ? -i : (Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.EAST ? i : 0)), !this.towerActivation() && ((Boolean)this.sameYValue.get() != false || ((Boolean)this.autoJumpValue.get() != false || (Boolean)this.smartSpeedValue.get() != false && Client.moduleManager.getModule(Speed.class).getState()) && !GameSettings.func_100015_a((KeyBinding)Scaffold.mc.field_71474_y.field_74314_A)) && (double)this.launchY <= Scaffold.mc.field_71439_g.field_70163_u ? (double)(this.launchY - 1) : Scaffold.mc.field_71439_g.field_70163_u - (Scaffold.mc.field_71439_g.field_70163_u == (double)((int)Scaffold.mc.field_71439_g.field_70163_u) + 0.5 ? 0.0 : 1.0) - (this.shouldGoDown ? 1.0 : 0.0), Scaffold.mc.field_71439_g.field_70161_v + (double)(Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.NORTH ? -i : (Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.SOUTH ? i : 0)));
            PlaceInfo placeInfo = PlaceInfo.get(blockPos);
            if (!BlockUtils.isReplaceable(blockPos) || placeInfo == null) continue;
            RenderUtils.drawBlockBox(blockPos, new Color((Integer)this.redValue.get(), (Integer)this.greenValue.get(), (Integer)this.blueValue.get(), (Integer)this.alphaValue.get()), false);
            break;
        }
    }

    private boolean search(BlockPos blockPosition, boolean checks) {
        return this.search(blockPosition, checks, false);
    }

    private boolean search(BlockPos blockPosition, boolean checks, boolean towerActive) {
        this.faceBlock = false;
        if (!BlockUtils.isReplaceable(blockPosition)) {
            return false;
        }
        boolean staticYawMode = ((String)this.rotationModeValue.get()).equalsIgnoreCase("AAC") || ((String)this.rotationModeValue.get()).contains("Static") && !((String)this.rotationModeValue.get()).equalsIgnoreCase("static3");
        Vec3 eyesPos = new Vec3(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)Scaffold.mc.field_71439_g.func_70047_e(), Scaffold.mc.field_71439_g.field_70161_v);
        PlaceRotation placeRotation = null;
        for (EnumFacing side : StaticStorage.facings()) {
            BlockPos neighbor = blockPosition.func_177972_a(side);
            if (!BlockUtils.canBeClicked(neighbor)) continue;
            Vec3 dirVec = new Vec3(side.func_176730_m());
            for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
                for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                    for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                        Vec3 posVec = new Vec3((Vec3i)blockPosition).func_72441_c(xSearch, ySearch, zSearch);
                        double distanceSqPosVec = eyesPos.func_72436_e(posVec);
                        Vec3 hitVec = posVec.func_178787_e(new Vec3(dirVec.field_72450_a * 0.5, dirVec.field_72448_b * 0.5, dirVec.field_72449_c * 0.5));
                        if (checks && (eyesPos.func_72436_e(hitVec) > 18.0 || distanceSqPosVec > eyesPos.func_72436_e(posVec.func_178787_e(dirVec)) || Scaffold.mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) != null)) continue;
                        for (int i = 0; i < (staticYawMode ? 2 : 1); ++i) {
                            double diffX = staticYawMode && i == 0 ? 0.0 : hitVec.field_72450_a - eyesPos.field_72450_a;
                            double diffY = hitVec.field_72448_b - eyesPos.field_72448_b;
                            double diffZ = staticYawMode && i == 1 ? 0.0 : hitVec.field_72449_c - eyesPos.field_72449_c;
                            double diffXZ = MathHelper.func_76133_a((double)(diffX * diffX + diffZ * diffZ));
                            Rotation rotation = new Rotation(MathHelper.func_76142_g((float)((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f)), MathHelper.func_76142_g((float)((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))))));
                            if (((String)this.rotationModeValue.get()).equalsIgnoreCase("static") && !Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                rotation = new Rotation(MovementUtils.getScaffoldRotation(Scaffold.mc.field_71439_g.field_70177_z, Scaffold.mc.field_71439_g.field_70702_br), ((Float)this.staticPitchValue.get()).floatValue());
                            }
                            if ((((String)this.rotationModeValue.get()).equalsIgnoreCase("static2") || ((String)this.rotationModeValue.get()).equalsIgnoreCase("static3")) && !Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                rotation = new Rotation(rotation.getYaw(), ((Float)this.staticPitchValue.get()).floatValue());
                            }
                            if (((String)this.rotationModeValue.get()).equalsIgnoreCase("custom") && !Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                rotation = new Rotation(Scaffold.mc.field_71439_g.field_70177_z + ((Float)this.customYawValue.get()).floatValue(), ((Float)this.customPitchValue.get()).floatValue());
                            }
                            Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                            Vec3 vector = eyesPos.func_72441_c(rotationVector.field_72450_a * 4.0, rotationVector.field_72448_b * 4.0, rotationVector.field_72449_c * 4.0);
                            MovingObjectPosition obj = Scaffold.mc.field_71441_e.func_147447_a(eyesPos, vector, false, false, true);
                            if (obj.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK || !obj.func_178782_a().equals((Object)neighbor) || placeRotation != null && !(RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation()))) continue;
                            placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.func_176734_d(), hitVec), rotation);
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
                if ((int)(10.0f * MathHelper.func_76142_g((float)limitedRotation.getYaw())) == (int)(10.0f * MathHelper.func_76142_g((float)placeRotation.getRotation().getYaw())) && (int)(10.0f * MathHelper.func_76142_g((float)limitedRotation.getPitch())) == (int)(10.0f * MathHelper.func_76142_g((float)placeRotation.getRotation().getPitch()))) {
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
        }
        if (towerActive) {
            this.towerPlace = placeRotation.getPlaceInfo();
        } else {
            this.targetPlace = placeRotation.getPlaceInfo();
        }
        return true;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (Scaffold.mc.field_71439_g == null) {
            return;
        }
        for (int i = 0; i < 8; ++i) {
            if (Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[i] == null || Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[i].field_77994_a > 0) continue;
            Scaffold.mc.field_71439_g.field_71071_by.field_70462_a[i] = null;
        }
    }

    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            Block block;
            ItemStack itemStack = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack == null || !(itemStack.func_77973_b() instanceof ItemBlock) || InventoryUtils.BLOCK_BLACKLIST.contains(block = ((ItemBlock)itemStack.func_77973_b()).func_179223_d()) || !block.func_149686_d()) continue;
            amount += itemStack.field_77994_a;
        }
        return amount;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

