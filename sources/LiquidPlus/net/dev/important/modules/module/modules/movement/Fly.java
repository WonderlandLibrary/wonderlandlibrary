/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0CPacketInput
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumParticleTypes
 */
package net.dev.important.modules.module.modules.movement;

import java.awt.Color;
import java.util.ArrayList;
import net.dev.important.Client;
import net.dev.important.event.ActionEvent;
import net.dev.important.event.BlockBBEvent;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.StepEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.utils.timer.TickTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

@Info(name="Fly", description="Allows you to fly in survival mode.", category=Category.MOVEMENT, cnName="\u98de\u884c")
public class Fly
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Motion", "Creative", "Damage", "Pearl", "NCP", "OldNCP", "Watchdog", "WatchdogTest", "FunCraft", "Rewinside", "Verus", "VerusLowHop", "Spartan", "Spartan2", "BugSpartan", "AAC5-Vanilla", "Jetpack", "KeepAlive", "Clip", "Jump", "Derp", "Collide"}, "Motion");
    private final FloatValue vanillaSpeedValue = new FloatValue("Speed", 2.0f, 0.0f, 5.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("motion") || ((String)this.modeValue.get()).equalsIgnoreCase("damage") || ((String)this.modeValue.get()).equalsIgnoreCase("pearl") || ((String)this.modeValue.get()).equalsIgnoreCase("aac5-vanilla") || ((String)this.modeValue.get()).equalsIgnoreCase("bugspartan") || ((String)this.modeValue.get()).equalsIgnoreCase("keepalive") || ((String)this.modeValue.get()).equalsIgnoreCase("derp"));
    private final FloatValue vanillaVSpeedValue = new FloatValue("V-Speed", 2.0f, 0.0f, 5.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("motion"));
    private final FloatValue vanillaMotionYValue = new FloatValue("Y-Motion", 0.0f, -1.0f, 1.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("motion"));
    private final BoolValue vanillaKickBypassValue = new BoolValue("KickBypass", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("motion") || ((String)this.modeValue.get()).equalsIgnoreCase("creative"));
    private final BoolValue groundSpoofValue = new BoolValue("GroundSpoof", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("motion") || ((String)this.modeValue.get()).equalsIgnoreCase("creative"));
    private final FloatValue ncpMotionValue = new FloatValue("NCPMotion", 0.0f, 0.0f, 1.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("ncp"));
    private final ListValue verusDmgModeValue = new ListValue("Verus-DamageMode", new String[]{"None", "Instant", "InstantC06", "Jump"}, "None", () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus"));
    private final ListValue verusBoostModeValue = new ListValue("Verus-BoostMode", new String[]{"Static", "Gradual"}, "Gradual", () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none"));
    private final BoolValue verusReDamageValue = new BoolValue("Verus-ReDamage", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("jump"));
    private final IntegerValue verusReDmgTickValue = new IntegerValue("Verus-ReDamage-Ticks", 20, 0, 300, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("jump") && (Boolean)this.verusReDamageValue.get() != false);
    private final BoolValue verusVisualValue = new BoolValue("Verus-VisualPos", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus"));
    private final FloatValue verusVisualHeightValue = new FloatValue("Verus-VisualHeight", 0.42f, 0.0f, 1.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus") && (Boolean)this.verusVisualValue.get() != false);
    private final FloatValue verusSpeedValue = new FloatValue("Verus-Speed", 5.0f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none"));
    private final FloatValue verusTimerValue = new FloatValue("Verus-Timer", 1.0f, 0.1f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none"));
    private final IntegerValue verusDmgTickValue = new IntegerValue("Verus-Ticks", 20, 0, 300, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus") && !((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none"));
    private final BoolValue verusSpoofGround = new BoolValue("Verus-SpoofGround", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus"));
    private final BoolValue aac5NoClipValue = new BoolValue("AAC5-NoClip", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("aac5-vanilla"));
    private final BoolValue aac5NofallValue = new BoolValue("AAC5-NoFall", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("aac5-vanilla"));
    private final BoolValue aac5UseC04Packet = new BoolValue("AAC5-UseC04", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("aac5-vanilla"));
    private final ListValue aac5Packet = new ListValue("AAC5-Packet", new String[]{"Original", "Rise", "Other"}, "Original", () -> ((String)this.modeValue.get()).equalsIgnoreCase("aac5-vanilla"));
    private final IntegerValue aac5PursePacketsValue = new IntegerValue("AAC5-Purse", 7, 3, 20, () -> ((String)this.modeValue.get()).equalsIgnoreCase("aac5-vanilla"));
    private final IntegerValue clipDelay = new IntegerValue("Clip-DelayTick", 25, 1, 50, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final FloatValue clipH = new FloatValue("Clip-Horizontal", 7.9f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final FloatValue clipV = new FloatValue("Clip-Vertical", 1.75f, -10.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final FloatValue clipMotionY = new FloatValue("Clip-MotionY", 0.0f, -2.0f, 2.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final FloatValue clipTimer = new FloatValue("Clip-Timer", 1.0f, -0.08f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final BoolValue clipGroundSpoof = new BoolValue("Clip-GroundSpoof", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final BoolValue clipCollisionCheck = new BoolValue("Clip-CollisionCheck", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final BoolValue clipNoMove = new BoolValue("Clip-NoMove", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final BoolValue fakeSprintingValue = new BoolValue("FakeSprinting", true, () -> ((String)this.modeValue.get()).toLowerCase().contains("watchdog"));
    private final BoolValue fakeNoMoveValue = new BoolValue("FakeNoMove", true, () -> ((String)this.modeValue.get()).toLowerCase().contains("watchdog"));
    private final BoolValue pulsiveTroll = new BoolValue("PulsiveTroll", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("watchdogtest"));
    private final BoolValue fakeDmgValue = new BoolValue("FakeDamage", true);
    private final BoolValue bobbingValue = new BoolValue("Bobbing", true);
    private final FloatValue bobbingAmountValue = new FloatValue("BobbingAmount", 0.2f, 0.0f, 1.0f, () -> (Boolean)this.bobbingValue.get());
    private final BoolValue markValue = new BoolValue("Mark", true);
    private BlockPos lastPosition;
    private double startY;
    private final MSTimer groundTimer = new MSTimer();
    private final MSTimer boostTimer = new MSTimer();
    private final MSTimer wdTimer = new MSTimer();
    private final TickTimer spartanTimer = new TickTimer();
    private final TickTimer verusTimer = new TickTimer();
    private boolean shouldFakeJump;
    private boolean shouldActive = false;
    private boolean noPacketModify;
    private boolean noFlag;
    private int pearlState = 0;
    private boolean wasDead;
    private int boostTicks;
    private int dmgCooldown = 0;
    private int verusJumpTimes = 0;
    private int wdState;
    private int wdTick = 0;
    private boolean verusDmged;
    private boolean shouldActiveDmg = false;
    private float lastYaw;
    private float lastPitch;
    private double moveSpeed = 0.0;
    private final ArrayList<C03PacketPlayer> aac5C03List = new ArrayList();

    private void doMove(double h, double v) {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        double x = Fly.mc.field_71439_g.field_70165_t;
        double y = Fly.mc.field_71439_g.field_70163_u;
        double z = Fly.mc.field_71439_g.field_70161_v;
        double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
        double expectedX = x + -Math.sin(yaw) * h;
        double expectedY = y + v;
        double expectedZ = z + Math.cos(yaw) * h;
        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(expectedX, expectedY, expectedZ, Fly.mc.field_71439_g.field_70122_E));
        Fly.mc.field_71439_g.func_70107_b(expectedX, expectedY, expectedZ);
    }

    private void hClip(double x, double y, double z) {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        double expectedX = Fly.mc.field_71439_g.field_70165_t + x;
        double expectedY = Fly.mc.field_71439_g.field_70163_u + y;
        double expectedZ = Fly.mc.field_71439_g.field_70161_v + z;
        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(expectedX, expectedY, expectedZ, Fly.mc.field_71439_g.field_70122_E));
        Fly.mc.field_71439_g.func_70107_b(expectedX, expectedY, expectedZ);
    }

    private double[] getMoves(double h, double v) {
        if (Fly.mc.field_71439_g == null) {
            return new double[]{0.0, 0.0, 0.0};
        }
        double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
        double expectedX = -Math.sin(yaw) * h;
        double expectedY = v;
        double expectedZ = Math.cos(yaw) * h;
        return new double[]{expectedX, expectedY, expectedZ};
    }

    @Override
    public void onEnable() {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        this.noPacketModify = true;
        this.verusTimer.reset();
        this.shouldFakeJump = false;
        this.shouldActive = true;
        double x = Fly.mc.field_71439_g.field_70165_t;
        double y = Fly.mc.field_71439_g.field_70163_u;
        double z = Fly.mc.field_71439_g.field_70161_v;
        this.lastYaw = Fly.mc.field_71439_g.field_70177_z;
        this.lastPitch = Fly.mc.field_71439_g.field_70125_A;
        String mode = (String)this.modeValue.get();
        this.boostTicks = 0;
        this.dmgCooldown = 0;
        this.pearlState = 0;
        this.verusJumpTimes = 0;
        this.verusDmged = false;
        this.moveSpeed = 0.0;
        this.wdState = 0;
        this.wdTick = 0;
        switch (mode.toLowerCase()) {
            case "ncp": {
                Fly.mc.field_71439_g.field_70181_x = -((Float)this.ncpMotionValue.get()).floatValue();
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.5;
                }
                MovementUtils.strafe();
                break;
            }
            case "oldncp": {
                if (this.startY > Fly.mc.field_71439_g.field_70163_u) {
                    Fly.mc.field_71439_g.field_70181_x = -1.0E-33;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.2;
                }
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d() && Fly.mc.field_71439_g.field_70163_u < this.startY - 0.1) {
                    Fly.mc.field_71439_g.field_70181_x = 0.2;
                }
                MovementUtils.strafe();
                break;
            }
            case "verus": {
                if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Instant")) {
                    if (Fly.mc.field_71439_g.field_70122_E && Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, false));
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, false));
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, true));
                        Fly.mc.field_71439_g.field_70179_y = 0.0;
                        Fly.mc.field_71439_g.field_70159_w = 0.0;
                        if (((Boolean)this.verusReDamageValue.get()).booleanValue()) {
                            this.dmgCooldown = (Integer)this.verusReDmgTickValue.get();
                        }
                    }
                } else if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("InstantC06")) {
                    if (Fly.mc.field_71439_g.field_70122_E && Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
                        Fly.mc.field_71439_g.field_70179_y = 0.0;
                        Fly.mc.field_71439_g.field_70159_w = 0.0;
                        if (((Boolean)this.verusReDamageValue.get()).booleanValue()) {
                            this.dmgCooldown = (Integer)this.verusReDmgTickValue.get();
                        }
                    }
                } else if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump")) {
                    if (Fly.mc.field_71439_g.field_70122_E) {
                        Fly.mc.field_71439_g.func_70664_aZ();
                        this.verusJumpTimes = 1;
                    }
                } else {
                    this.verusDmged = true;
                }
                if (((Boolean)this.verusVisualValue.get()).booleanValue()) {
                    Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, y + (double)((Float)this.verusVisualHeightValue.get()).floatValue(), Fly.mc.field_71439_g.field_70161_v);
                }
                this.shouldActiveDmg = this.dmgCooldown > 0;
                break;
            }
            case "bugspartan": {
                for (int i = 0; i < 65; ++i) {
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1, z, true));
                Fly.mc.field_71439_g.field_70159_w *= 0.1;
                Fly.mc.field_71439_g.field_70179_y *= 0.1;
                Fly.mc.field_71439_g.func_71038_i();
                break;
            }
            case "funcraft": {
                if (Fly.mc.field_71439_g.field_70122_E) {
                    Fly.mc.field_71439_g.func_70664_aZ();
                }
                this.moveSpeed = 1.0;
                break;
            }
            case "watchdogtest": {
                if (!Fly.mc.field_71439_g.field_70122_E) break;
                Fly.mc.field_71439_g.func_70107_b(x, y + 0.1, z);
            }
        }
        this.startY = Fly.mc.field_71439_g.field_70163_u;
        this.noPacketModify = false;
        if (!(mode.equalsIgnoreCase("watchdog") || mode.equalsIgnoreCase("watchdogtest") || mode.equalsIgnoreCase("bugspartan") || mode.equalsIgnoreCase("verus") || mode.equalsIgnoreCase("damage") || !((Boolean)this.fakeDmgValue.get()).booleanValue())) {
            Fly.mc.field_71439_g.func_70103_a((byte)2);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.wasDead = false;
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        this.noFlag = false;
        String mode = (String)this.modeValue.get();
        if (!mode.equalsIgnoreCase("Collide") && !mode.equalsIgnoreCase("Verus") && !mode.equalsIgnoreCase("Jump") && !mode.equalsIgnoreCase("creative") || mode.equalsIgnoreCase("pearl") && this.pearlState != -1) {
            Fly.mc.field_71439_g.field_70159_w = 0.0;
            Fly.mc.field_71439_g.field_70181_x = 0.0;
            Fly.mc.field_71439_g.field_70179_y = 0.0;
        }
        if (this.boostTicks > 0 && mode.equalsIgnoreCase("Verus")) {
            Fly.mc.field_71439_g.field_70159_w = 0.0;
            Fly.mc.field_71439_g.field_70179_y = 0.0;
        }
        if (mode.equalsIgnoreCase("AAC5-Vanilla") && !mc.func_71387_A()) {
            this.sendAAC5Packets();
        }
        Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
        Fly.mc.field_71428_T.field_74278_d = 1.0f;
        Fly.mc.field_71439_g.field_71102_ce = 0.02f;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        float vanillaSpeed = ((Float)this.vanillaSpeedValue.get()).floatValue();
        float vanillaVSpeed = ((Float)this.vanillaVSpeedValue.get()).floatValue();
        Fly.mc.field_71439_g.field_70145_X = false;
        if (((String)this.modeValue.get()).equalsIgnoreCase("aac5-vanilla") && ((Boolean)this.aac5NoClipValue.get()).booleanValue()) {
            Fly.mc.field_71439_g.field_70145_X = true;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "motion": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70181_x = ((Float)this.vanillaMotionYValue.get()).floatValue();
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x += (double)vanillaVSpeed;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x -= (double)vanillaVSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                this.handleVanillaKickBypass();
                break;
            }
            case "ncp": {
                Fly.mc.field_71439_g.field_70181_x = -((Float)this.ncpMotionValue.get()).floatValue();
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.5;
                }
                MovementUtils.strafe();
                break;
            }
            case "oldncp": {
                if (this.startY > Fly.mc.field_71439_g.field_70163_u) {
                    Fly.mc.field_71439_g.field_70181_x = -1.0E-33;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.2;
                }
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d() && Fly.mc.field_71439_g.field_70163_u < this.startY - 0.1) {
                    Fly.mc.field_71439_g.field_70181_x = 0.2;
                }
                MovementUtils.strafe();
                break;
            }
            case "clip": {
                Fly.mc.field_71439_g.field_70181_x = ((Float)this.clipMotionY.get()).floatValue();
                Fly.mc.field_71428_T.field_74278_d = ((Float)this.clipTimer.get()).floatValue();
                if (Fly.mc.field_71439_g.field_70173_aa % (Integer)this.clipDelay.get() != 0) break;
                double[] expectMoves = this.getMoves(((Float)this.clipH.get()).floatValue(), ((Float)this.clipV.get()).floatValue());
                if (((Boolean)this.clipCollisionCheck.get()).booleanValue() && !Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(expectMoves[0], expectMoves[1], expectMoves[2]).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) break;
                this.hClip(expectMoves[0], expectMoves[1], expectMoves[2]);
                break;
            }
            case "damage": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                if (Fly.mc.field_71439_g.field_70737_aN <= 0) break;
            }
            case "derp": 
            case "aac5-vanilla": 
            case "bugspartan": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x += (double)vanillaSpeed;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x -= (double)vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
            case "verus": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                if (!((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump") || this.shouldActiveDmg || this.verusDmged) {
                    Fly.mc.field_71439_g.field_70181_x = 0.0;
                }
                if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump") && this.verusJumpTimes < 5) {
                    if (Fly.mc.field_71439_g.field_70122_E) {
                        Fly.mc.field_71439_g.func_70664_aZ();
                        ++this.verusJumpTimes;
                    }
                    return;
                }
                if (this.shouldActiveDmg) {
                    if (this.dmgCooldown > 0) {
                        --this.dmgCooldown;
                    } else if (this.verusDmged) {
                        this.verusDmged = false;
                        double y = Fly.mc.field_71439_g.field_70163_u;
                        if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Instant")) {
                            if (Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, false));
                                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, false));
                                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, true));
                                Fly.mc.field_71439_g.field_70179_y = 0.0;
                                Fly.mc.field_71439_g.field_70159_w = 0.0;
                            }
                        } else if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("InstantC06") && Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                            PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                            PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                            PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
                            Fly.mc.field_71439_g.field_70179_y = 0.0;
                            Fly.mc.field_71439_g.field_70159_w = 0.0;
                        }
                        this.dmgCooldown = (Integer)this.verusReDmgTickValue.get();
                    }
                }
                if (!this.verusDmged && Fly.mc.field_71439_g.field_70737_aN > 0) {
                    this.verusDmged = true;
                    this.boostTicks = (Integer)this.verusDmgTickValue.get();
                }
                if (this.boostTicks > 0) {
                    Fly.mc.field_71428_T.field_74278_d = ((Float)this.verusTimerValue.get()).floatValue();
                    float motion = 0.0f;
                    motion = ((String)this.verusBoostModeValue.get()).equalsIgnoreCase("static") ? ((Float)this.verusSpeedValue.get()).floatValue() : (float)this.boostTicks / (float)((Integer)this.verusDmgTickValue.get()).intValue() * ((Float)this.verusSpeedValue.get()).floatValue();
                    --this.boostTicks;
                    MovementUtils.strafe(motion);
                    break;
                }
                if (this.verusDmged) {
                    Fly.mc.field_71428_T.field_74278_d = 1.0f;
                    MovementUtils.strafe((float)MovementUtils.getBaseMoveSpeed() * 0.6f);
                    break;
                }
                Fly.mc.field_71439_g.field_71158_b.field_78900_b = 0.0f;
                Fly.mc.field_71439_g.field_71158_b.field_78902_a = 0.0f;
                break;
            }
            case "creative": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                this.handleVanillaKickBypass();
                break;
            }
            case "keepalive": {
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C00PacketKeepAlive());
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x += (double)vanillaSpeed;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x -= (double)vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
            case "jetpack": {
                if (!Fly.mc.field_71474_y.field_74314_A.func_151470_d()) break;
                Fly.mc.field_71452_i.func_178927_a(EnumParticleTypes.FLAME.func_179348_c(), Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.2, Fly.mc.field_71439_g.field_70161_v, -Fly.mc.field_71439_g.field_70159_w, -0.5, -Fly.mc.field_71439_g.field_70179_y, new int[0]);
                Fly.mc.field_71439_g.field_70181_x += 0.15;
                Fly.mc.field_71439_g.field_70159_w *= 1.1;
                Fly.mc.field_71439_g.field_70179_y *= 1.1;
                break;
            }
            case "spartan": {
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                this.spartanTimer.update();
                if (!this.spartanTimer.hasTimePassed(12)) break;
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 8.0, Fly.mc.field_71439_g.field_70161_v, true));
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u - 8.0, Fly.mc.field_71439_g.field_70161_v, true));
                this.spartanTimer.reset();
                break;
            }
            case "spartan2": {
                MovementUtils.strafe(0.264f);
                if (Fly.mc.field_71439_g.field_70173_aa % 8 != 0) break;
                Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 10.0, Fly.mc.field_71439_g.field_70161_v, true));
                break;
            }
            case "pearl": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                int enderPearlSlot = this.getPearlSlot();
                if (this.pearlState == 0) {
                    if (enderPearlSlot == -1) {
                        Client.hud.addNotification(new Notification("You don't have any ender pearl!", Notification.Type.ERROR));
                        this.pearlState = -1;
                        this.setState(false);
                        return;
                    }
                    if (Fly.mc.field_71439_g.field_71071_by.field_70461_c != enderPearlSlot) {
                        Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C09PacketHeldItemChange(enderPearlSlot));
                    }
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(Fly.mc.field_71439_g.field_70177_z, 90.0f, Fly.mc.field_71439_g.field_70122_E));
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Fly.mc.field_71439_g.field_71069_bz.func_75139_a(enderPearlSlot + 36).func_75211_c(), 0.0f, 0.0f, 0.0f));
                    if (enderPearlSlot != Fly.mc.field_71439_g.field_71071_by.field_70461_c) {
                        Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C09PacketHeldItemChange(Fly.mc.field_71439_g.field_71071_by.field_70461_c));
                    }
                    this.pearlState = 1;
                }
                if (this.pearlState == 1 && Fly.mc.field_71439_g.field_70737_aN > 0) {
                    this.pearlState = 2;
                }
                if (this.pearlState != 2) break;
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x += (double)vanillaSpeed;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x -= (double)vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
            case "jump": {
                if (!Fly.mc.field_71439_g.field_70122_E) break;
                Fly.mc.field_71439_g.func_70664_aZ();
                break;
            }
            case "watchdog": {
                if (this.wdState == 0) {
                    Fly.mc.field_71439_g.field_70181_x = 0.1;
                    ++this.wdState;
                }
                if (this.wdState == 1 && this.wdTick == 3) {
                    ++this.wdState;
                }
                if (this.wdState != 4) break;
                Fly.mc.field_71428_T.field_74278_d = !this.boostTimer.hasTimePassed(500L) ? 1.6f : (!this.boostTimer.hasTimePassed(800L) ? 1.4f : (!this.boostTimer.hasTimePassed(1000L) ? 1.2f : 1.0f));
                Fly.mc.field_71439_g.field_70181_x = 1.0E-4;
                MovementUtils.strafe((float)(MovementUtils.getBaseMoveSpeed() * (Fly.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? 0.81 : 0.77)));
                break;
            }
            case "watchdogtest": {
                if (this.wdState == 3) {
                    Fly.mc.field_71428_T.field_74278_d = 1.0f;
                    Fly.mc.field_71439_g.field_70181_x = 1.0E-4;
                    MovementUtils.strafe((float)(MovementUtils.getBaseMoveSpeed() * (Fly.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? 0.8 : 0.75)));
                    break;
                }
                if (this.wdState != 2) break;
                Fly.mc.field_71428_T.field_74278_d = 1.5f;
                Fly.mc.field_71439_g.field_70181_x = 1.0E-4;
                MovementUtils.strafe((float)(MovementUtils.getBaseMoveSpeed() * (Fly.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? 0.81 : 0.77)));
            }
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        if (((Boolean)this.bobbingValue.get()).booleanValue()) {
            Fly.mc.field_71439_g.field_71109_bG = ((Float)this.bobbingAmountValue.get()).floatValue();
            Fly.mc.field_71439_g.field_71107_bF = ((Float)this.bobbingAmountValue.get()).floatValue();
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "funcraft": {
                event.setOnGround(true);
                if (!MovementUtils.isMoving()) {
                    this.moveSpeed = 0.25;
                }
                if (this.moveSpeed > 0.25) {
                    this.moveSpeed -= this.moveSpeed / 159.0;
                }
                if (event.getEventState() != EventState.PRE) break;
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                MovementUtils.strafe((float)this.moveSpeed);
                Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u - 8.0E-6, Fly.mc.field_71439_g.field_70161_v);
                break;
            }
            case "watchdog": {
                if (event.getEventState() != EventState.PRE) break;
                ++this.wdTick;
                break;
            }
            case "watchdogtest": {
                if (event.getEventState() != EventState.POST || !((Boolean)this.pulsiveTroll.get()).booleanValue() || this.wdState < 2 || Fly.mc.field_71439_g.field_70173_aa % 2 != 0) break;
                mc.func_147114_u().func_147297_a((Packet)new C0CPacketInput(this.coerceAtMost(Fly.mc.field_71439_g.field_70702_br, 0.98f), this.coerceAtMost(Fly.mc.field_71439_g.field_70701_bs, 0.98f), Fly.mc.field_71439_g.field_71158_b.field_78901_c, Fly.mc.field_71439_g.field_71158_b.field_78899_d));
            }
        }
    }

    public float coerceAtMost(double value, double max) {
        return (float)Math.min(value, max);
    }

    @EventTarget
    public void onAction(ActionEvent event) {
        if (((String)this.modeValue.get()).toLowerCase().contains("watchdog") && ((Boolean)this.fakeSprintingValue.get()).booleanValue()) {
            event.setSprinting(false);
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        double y;
        String mode = (String)this.modeValue.get();
        if (!((Boolean)this.markValue.get()).booleanValue() || mode.equalsIgnoreCase("Motion") || mode.equalsIgnoreCase("Creative") || mode.equalsIgnoreCase("Damage") || mode.equalsIgnoreCase("AAC5-Vanilla") || mode.equalsIgnoreCase("Derp") || mode.equalsIgnoreCase("KeepAlive")) {
            return;
        }
        RenderUtils.drawPlatform(y, Fly.mc.field_71439_g.func_174813_aQ().field_72337_e < (y = this.startY + 2.0) ? new Color(0, 255, 0, 90) : new Color(255, 0, 0, 90), 1.0);
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        float width;
        String mode = (String)this.modeValue.get();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        if (mode.equalsIgnoreCase("Verus") && this.boostTicks > 0) {
            width = (float)((Integer)this.verusDmgTickValue.get() - this.boostTicks) / (float)((Integer)this.verusDmgTickValue.get()).intValue() * 60.0f;
            RenderUtils.drawRect((float)scaledRes.func_78326_a() / 2.0f - 31.0f, (float)scaledRes.func_78328_b() / 2.0f + 14.0f, (float)scaledRes.func_78326_a() / 2.0f + 31.0f, (float)scaledRes.func_78328_b() / 2.0f + 18.0f, -1610612736);
            RenderUtils.drawRect((float)scaledRes.func_78326_a() / 2.0f - 30.0f, (float)scaledRes.func_78328_b() / 2.0f + 15.0f, (float)scaledRes.func_78326_a() / 2.0f - 30.0f + width, (float)scaledRes.func_78328_b() / 2.0f + 17.0f, -1);
        }
        if (mode.equalsIgnoreCase("Verus") && this.shouldActiveDmg) {
            width = (float)((Integer)this.verusReDmgTickValue.get() - this.dmgCooldown) / (float)((Integer)this.verusReDmgTickValue.get()).intValue() * 60.0f;
            RenderUtils.drawRect((float)scaledRes.func_78326_a() / 2.0f - 31.0f, (float)scaledRes.func_78328_b() / 2.0f + 14.0f + 10.0f, (float)scaledRes.func_78326_a() / 2.0f + 31.0f, (float)scaledRes.func_78328_b() / 2.0f + 18.0f + 10.0f, -1610612736);
            RenderUtils.drawRect((float)scaledRes.func_78326_a() / 2.0f - 30.0f, (float)scaledRes.func_78328_b() / 2.0f + 15.0f + 10.0f, (float)scaledRes.func_78326_a() / 2.0f - 30.0f + width, (float)scaledRes.func_78328_b() / 2.0f + 17.0f + 10.0f, -57569);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        String mode = (String)this.modeValue.get();
        if (this.noPacketModify) {
            return;
        }
        if (packet instanceof S08PacketPlayerPosLook && mode.equalsIgnoreCase("watchdog") && this.wdState == 3) {
            this.wdState = 4;
            if (this.boostTimer.hasTimePassed(8000L)) {
                Client.hud.addNotification(new Notification("Enabled boost.", Notification.Type.SUCCESS));
                this.boostTimer.reset();
            } else {
                Client.hud.addNotification(new Notification("Disabled boost to prevent flagging.", Notification.Type.WARNING));
            }
            if (((Boolean)this.fakeDmgValue.get()).booleanValue() && Fly.mc.field_71439_g != null) {
                Fly.mc.field_71439_g.func_70103_a((byte)2);
            }
        }
        if (mode.equalsIgnoreCase("WatchdogTest") && packet instanceof S08PacketPlayerPosLook) {
            if (this.wdState == 1) {
                this.wdState = 2;
                Client.hud.addNotification(new Notification("Activated.", Notification.Type.SUCCESS));
                if (((Boolean)this.fakeDmgValue.get()).booleanValue() && Fly.mc.field_71439_g != null) {
                    Fly.mc.field_71439_g.func_70103_a((byte)2);
                }
            } else if (this.wdState == 2) {
                this.wdState = 3;
                Client.hud.addNotification(new Notification("Flagged.", Notification.Type.INFO));
            }
        }
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            boolean lastOnGround = packetPlayer.field_149474_g;
            if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("Verus") && ((Boolean)this.verusSpoofGround.get()).booleanValue() && this.verusDmged) {
                packetPlayer.field_149474_g = true;
            }
            if (mode.equalsIgnoreCase("Derp")) {
                packetPlayer.field_149476_e = RandomUtils.nextFloat(0.0f, 360.0f);
                packetPlayer.field_149473_f = RandomUtils.nextFloat(-90.0f, 90.0f);
            }
            if (mode.equalsIgnoreCase("AAC5-Vanilla") && !mc.func_71387_A()) {
                if (((Boolean)this.aac5NofallValue.get()).booleanValue()) {
                    packetPlayer.field_149474_g = true;
                }
                this.aac5C03List.add(packetPlayer);
                event.cancelEvent();
                if (this.aac5C03List.size() > (Integer)this.aac5PursePacketsValue.get()) {
                    this.sendAAC5Packets();
                }
            }
            if (mode.equalsIgnoreCase("clip") && ((Boolean)this.clipGroundSpoof.get()).booleanValue()) {
                packetPlayer.field_149474_g = true;
            }
            if ((mode.equalsIgnoreCase("motion") || mode.equalsIgnoreCase("creative")) && ((Boolean)this.groundSpoofValue.get()).booleanValue()) {
                packetPlayer.field_149474_g = true;
            }
            if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump") && this.verusJumpTimes < 5 && mode.equalsIgnoreCase("Verus")) {
                packetPlayer.field_149474_g = false;
            }
            if (mode.equalsIgnoreCase("watchdog")) {
                if (this.wdState == 2) {
                    packetPlayer.field_149477_b -= 0.187;
                    ++this.wdState;
                }
                if (this.wdState > 3 && ((Boolean)this.fakeNoMoveValue.get()).booleanValue()) {
                    packetPlayer.func_149469_a(false);
                }
            }
            if (mode.equalsIgnoreCase("watchdogtest")) {
                if (this.wdState == 0 && packetPlayer.field_149474_g) {
                    packetPlayer.field_149477_b -= 0.187;
                    this.wdState = 1;
                } else {
                    packetPlayer.field_149477_b = this.startY;
                    if (((Boolean)this.fakeNoMoveValue.get()).booleanValue()) {
                        packetPlayer.func_149469_a(false);
                    }
                }
            }
        }
    }

    private void sendAAC5Packets() {
        float yaw = Fly.mc.field_71439_g.field_70177_z;
        float pitch = Fly.mc.field_71439_g.field_70125_A;
        for (C03PacketPlayer packet : this.aac5C03List) {
            PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)packet);
            if (!packet.func_149466_j()) continue;
            if (packet.func_149463_k()) {
                yaw = packet.field_149476_e;
                pitch = packet.field_149473_f;
            }
            switch ((String)this.aac5Packet.get()) {
                case "Original": {
                    if (((Boolean)this.aac5UseC04Packet.get()).booleanValue()) {
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, 1.0E159, packet.field_149478_c, true));
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, true));
                        break;
                    }
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, 1.0E159, packet.field_149478_c, yaw, pitch, true));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, yaw, pitch, true));
                    break;
                }
                case "Rise": {
                    if (((Boolean)this.aac5UseC04Packet.get()).booleanValue()) {
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, -1.0E159, packet.field_149478_c + 10.0, true));
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, true));
                        break;
                    }
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, -1.0E159, packet.field_149478_c + 10.0, yaw, pitch, true));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, yaw, pitch, true));
                    break;
                }
                case "Other": {
                    if (((Boolean)this.aac5UseC04Packet.get()).booleanValue()) {
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, Double.MAX_VALUE, packet.field_149478_c, true));
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, true));
                        break;
                    }
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, Double.MAX_VALUE, packet.field_149478_c, yaw, pitch, true));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, yaw, pitch, true));
                }
            }
        }
        this.aac5C03List.clear();
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "pearl": {
                if (this.pearlState == 2 || this.pearlState == -1) break;
                event.cancelEvent();
                break;
            }
            case "verus": {
                if (this.verusDmged) break;
                if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump")) {
                    event.zeroXZ();
                    break;
                }
                event.cancelEvent();
                break;
            }
            case "clip": {
                if (!((Boolean)this.clipNoMove.get()).booleanValue()) break;
                event.zeroXZ();
                break;
            }
            case "veruslowhop": {
                if (Fly.mc.field_71439_g.field_70134_J || Fly.mc.field_71439_g.func_180799_ab() || Fly.mc.field_71439_g.func_70090_H() || Fly.mc.field_71439_g.func_70617_f_() || Fly.mc.field_71474_y.field_74314_A.func_151470_d() || Fly.mc.field_71439_g.field_70154_o != null || !MovementUtils.isMoving()) break;
                Fly.mc.field_71474_y.field_74314_A.field_74513_e = false;
                if (Fly.mc.field_71439_g.field_70122_E) {
                    Fly.mc.field_71439_g.func_70664_aZ();
                    Fly.mc.field_71439_g.field_70181_x = 0.0;
                    MovementUtils.strafe(0.61f);
                    event.setY(0.41999998688698);
                }
                MovementUtils.strafe();
                break;
            }
            case "watchdog": {
                if (this.wdState >= 4) break;
                event.zeroXZ();
                break;
            }
            case "watchdogtest": {
                if (this.wdState >= 2) break;
                event.zeroXZ();
            }
        }
    }

    @EventTarget
    public void onBB(BlockBBEvent event) {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        String mode = (String)this.modeValue.get();
        if (event.getBlock() instanceof BlockAir && mode.equalsIgnoreCase("Jump") && (double)event.getY() < this.startY) {
            event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), (double)this.startY, (double)(event.getZ() + 1)));
        }
        if (event.getBlock() instanceof BlockAir && (mode.equalsIgnoreCase("collide") && !Fly.mc.field_71439_g.func_70093_af() || mode.equalsIgnoreCase("veruslowhop"))) {
            event.setBoundingBox(new AxisAlignedBB(-2.0, -1.0, -2.0, 2.0, 1.0, 2.0).func_72317_d((double)event.getX(), (double)event.getY(), (double)event.getZ()));
        }
        if (event.getBlock() instanceof BlockAir && (mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("Verus") && (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none") || this.verusDmged)) && (double)event.getY() < Fly.mc.field_71439_g.field_70163_u) {
            event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), (double)Fly.mc.field_71439_g.field_70163_u, (double)(event.getZ() + 1)));
        }
    }

    @EventTarget
    public void onJump(JumpEvent e) {
        String mode = (String)this.modeValue.get();
        if (mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("FunCraft") && this.moveSpeed > 0.0 || mode.equalsIgnoreCase("watchdog") && this.wdState >= 1 || mode.equalsIgnoreCase("watchdogtest")) {
            e.cancelEvent();
        }
    }

    @EventTarget
    public void onStep(StepEvent e) {
        String mode = (String)this.modeValue.get();
        if (mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("FunCraft") || mode.equalsIgnoreCase("watchdog") && this.wdState > 2 || mode.equalsIgnoreCase("watchdogtest")) {
            e.setStepHeight(0.0f);
        }
    }

    private void handleVanillaKickBypass() {
        double posY;
        if (!((Boolean)this.vanillaKickBypassValue.get()).booleanValue() || !this.groundTimer.hasTimePassed(1000L)) {
            return;
        }
        double ground = this.calculateGround();
        for (posY = Fly.mc.field_71439_g.field_70163_u; posY > ground; posY -= 8.0) {
            PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, posY, Fly.mc.field_71439_g.field_70161_v, true));
            if (posY - 8.0 < ground) break;
        }
        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, ground, Fly.mc.field_71439_g.field_70161_v, true));
        for (posY = ground; posY < Fly.mc.field_71439_g.field_70163_u; posY += 8.0) {
            PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, posY, Fly.mc.field_71439_g.field_70161_v, true));
            if (posY + 8.0 > Fly.mc.field_71439_g.field_70163_u) break;
        }
        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, true));
        this.groundTimer.reset();
    }

    private double calculateGround() {
        AxisAlignedBB playerBoundingBox = Fly.mc.field_71439_g.func_174813_aQ();
        double blockHeight = 1.0;
        for (double ground = Fly.mc.field_71439_g.field_70163_u; ground > 0.0; ground -= blockHeight) {
            AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.field_72336_d, ground + blockHeight, playerBoundingBox.field_72334_f, playerBoundingBox.field_72340_a, ground, playerBoundingBox.field_72339_c);
            if (!Fly.mc.field_71441_e.func_72829_c(customBox)) continue;
            if (blockHeight <= 0.05) {
                return ground + blockHeight;
            }
            ground += blockHeight;
            blockHeight = 0.05;
        }
        return 0.0;
    }

    private int getPearlSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = Fly.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null || !(stack.func_77973_b() instanceof ItemEnderPearl)) continue;
            return i - 36;
        }
        return -1;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

