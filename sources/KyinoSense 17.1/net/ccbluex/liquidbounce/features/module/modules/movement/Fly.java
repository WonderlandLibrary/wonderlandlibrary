/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockSlime
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name="Flight", description="Allows you to fly in survival mode.", category=ModuleCategory.MOVEMENT)
public class Fly
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "BlocksmcTest", "Creative", "Damage", "Pearl", "NCP", "OldNCP", "AAC1.9.10", "AAC3.0.5", "AAC3.1.6-Gomme", "AAC3.3.12", "AAC3.3.12-Glide", "AAC3.3.13", "AAC5-Vanilla", "CubeCraft", "Rewinside", "TeleportRewinside", "FunCraft", "GTA5", "Mineplex", "NeruxVace", "Minesucht", "Verus", "VerusLowHop", "Spartan", "Spartan2", "BugSpartan", "Hypixel", "BoostHypixel", "FreeHypixel", "MineSecure", "HawkEye", "HAC", "WatchCat", "Watchdog", "Jetpack", "KeepAlive", "Flag", "Clip", "Jump", "Derp", "Collide"}, "Vanilla");
    private final FloatValue vanillaSpeedValue = new FloatValue("Speed", 2.0f, 0.0f, 5.0f);
    private final FloatValue vanillaVSpeedValue = new FloatValue("V-Speed", 2.0f, 0.0f, 5.0f);
    private final FloatValue vanillaMotionYValue = new FloatValue("Y-Motion", 0.0f, -1.0f, 1.0f);
    private final BoolValue vanillaKickBypassValue = new BoolValue("KickBypass", false);
    private final BoolValue groundSpoofValue = new BoolValue("GroundSpoof", false);
    private final FloatValue ncpMotionValue = new FloatValue("NCPMotion", 0.0f, 0.0f, 1.0f);
    private final ListValue verusDmgModeValue = new ListValue("VerusDamageMode", new String[]{"None", "Instant", "InstantC06", "Jump"}, "None");
    private final ListValue verusBoostModeValue = new ListValue("VerusBoostMode", new String[]{"Static", "Gradual"}, "Gradual");
    private final BoolValue verusReDamageValue = new BoolValue("VerusReDamage", true);
    private final IntegerValue verusReDmgTickValue = new IntegerValue("VerusReDamageTicks", 20, 0, 300);
    private final BoolValue verusVisualValue = new BoolValue("VerusVisualPos", false);
    private final FloatValue verusVisualHeightValue = new FloatValue("VerusVisualHeight", 0.42f, 0.0f, 1.0f);
    private final FloatValue verusSpeedValue = new FloatValue("VerusSpeed", 5.0f, 0.0f, 10.0f);
    private final FloatValue verusTimerValue = new FloatValue("VerusTimer", 1.0f, 0.1f, 10.0f);
    private final IntegerValue verusDmgTickValue = new IntegerValue("VerusTicks", 20, 0, 300);
    private final BoolValue verusSpoofGround = new BoolValue("VerusSpoofGround", false);
    private final BoolValue aac5NoClipValue = new BoolValue("AAC5NoClip", true);
    private final BoolValue aac5NofallValue = new BoolValue("AAC5NoFall", true);
    private final BoolValue aac5UseC04Packet = new BoolValue("AAC5UseC04", true);
    private final ListValue aac5Packet = new ListValue("AAC5Packet", new String[]{"Original", "Rise", "Other"}, "Original");
    private final IntegerValue aac5PursePacketsValue = new IntegerValue("AAC5-Purse", 7, 3, 20);
    private final IntegerValue clipDelay = new IntegerValue("ClipDelay-Tick", 25, 1, 50);
    private final FloatValue clipH = new FloatValue("ClipHorizontal", 7.9f, 0.0f, 10.0f);
    private final FloatValue clipV = new FloatValue("ClipVertical", 1.75f, -10.0f, 10.0f);
    private final FloatValue clipMotionY = new FloatValue("ClipMotion-Y", 0.0f, -2.0f, 2.0f);
    private final FloatValue clipTimer = new FloatValue("ClipTimer", 1.0f, 0.08f, 10.0f);
    private final BoolValue clipGroundSpoof = new BoolValue("ClipGroundSpoof", true);
    private final BoolValue clipCollisionCheck = new BoolValue("ClipCollisionCheck", true);
    private final BoolValue clipNoMove = new BoolValue("ClipNoMove", true);
    private final ListValue pearlActivateCheck = new ListValue("PearlActiveCheck", new String[]{"Teleport", "Damage"}, "Teleport");
    private final FloatValue aacSpeedValue = new FloatValue("AAC1.9.10Speed", 0.3f, 0.0f, 1.0f);
    private final BoolValue aacFast = new BoolValue("AAC3.0.5 Fast", true);
    private final FloatValue aacMotion = new FloatValue("AAC3.3.12 Motion", 10.0f, 0.1f, 10.0f);
    private final FloatValue aacMotion2 = new FloatValue("AAC3.3.13 Motion", 10.0f, 0.1f, 10.0f);
    private final ListValue hypixelBoostMode = new ListValue("BoostHypixelMode", new String[]{"Default", "MorePackets", "NCP"}, "Default");
    private final BoolValue hypixelVisualY = new BoolValue("BoostHypixelVisualY", true);
    private final BoolValue hypixelC04 = new BoolValue("BoostHypixelMoreC04s", false);
    private final BoolValue hypixelBoost = new BoolValue("HypixelBoost", true);
    private final IntegerValue hypixelBoostDelay = new IntegerValue("HypixelBoost-Delay", 1200, 0, 2000);
    private final FloatValue hypixelBoostTimer = new FloatValue("HypixelBoost Timer", 1.0f, 0.0f, 5.0f);
    private final FloatValue mineplexSpeedValue = new FloatValue("MineplexSpeed", 1.0f, 0.5f, 10.0f);
    private final IntegerValue neruxVaceTicks = new IntegerValue("NeruxVace-Ticks", 6, 0, 20);
    private final BoolValue resetMotionValue = new BoolValue("ResetMotion", true);
    private final BoolValue fakeDmgValue = new BoolValue("FakeDamage", true);
    private final BoolValue bobbingValue = new BoolValue("Bobbing", true);
    private final FloatValue bobbingAmountValue = new FloatValue("BobbingAmount", 0.2f, 0.0f, 1.0f);
    private final FloatValue timerSpeedValue = new FloatValue("TimerSpeed-BlocksmcTest", 0.8f, 0.1f, 1.0f);
    private final BoolValue markValue = new BoolValue("Mark", true);
    private final BoolValue speedDisplay = new BoolValue("SpeedDisplay", true);
    private BlockPos lastPosition;
    private double startY;
    private final MSTimer flyTimer = new MSTimer();
    private final MSTimer groundTimer = new MSTimer();
    private final MSTimer boostTimer = new MSTimer();
    private final MSTimer wdTimer = new MSTimer();
    private final MSTimer mineSecureVClipTimer = new MSTimer();
    private final MSTimer mineplexTimer = new MSTimer();
    private final TickTimer spartanTimer = new TickTimer();
    private final TickTimer verusTimer = new TickTimer();
    private final TickTimer hypixelTimer = new TickTimer();
    private final TickTimer cubecraftTeleportTickTimer = new TickTimer();
    private final TickTimer freeHypixelTimer = new TickTimer();
    private boolean shouldFakeJump;
    private boolean shouldActive = false;
    private boolean noPacketModify;
    private boolean isBoostActive = false;
    private boolean noFlag;
    private int pearlState = 0;
    private boolean wasDead;
    private int boostTicks;
    private int dmgCooldown = 0;
    private int verusJumpTimes = 0;
    public int wdState;
    public int wdTick = 0;
    private boolean canBoost;
    private boolean verusDmged;
    private boolean shouldActiveDmg = false;
    private float lastYaw;
    private float lastPitch;
    private double moveSpeed = 0.0;
    private int expectItemStack = -1;
    private double aacJump;
    private int aac3delay;
    private int aac3glideDelay;
    private long minesuchtTP;
    private int boostHypixelState = 1;
    private double lastDistance;
    private boolean failedStart = false;
    private float freeHypixelYaw;
    private float freeHypixelPitch;
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
        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(expectedX, expectedY, expectedZ, Fly.mc.field_71439_g.field_70122_E));
        Fly.mc.field_71439_g.func_70107_b(expectedX, expectedY, expectedZ);
    }

    private void hClip(double x, double y, double z) {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        double expectedX = Fly.mc.field_71439_g.field_70165_t + x;
        double expectedY = Fly.mc.field_71439_g.field_70163_u + y;
        double expectedZ = Fly.mc.field_71439_g.field_70161_v + z;
        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(expectedX, expectedY, expectedZ, Fly.mc.field_71439_g.field_70122_E));
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
        this.canBoost = true;
        this.noPacketModify = true;
        this.verusTimer.reset();
        this.flyTimer.reset();
        this.shouldFakeJump = false;
        this.shouldActive = true;
        this.isBoostActive = false;
        this.expectItemStack = -1;
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
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, false));
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, false));
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, true));
                        Fly.mc.field_71439_g.field_70179_y = 0.0;
                        Fly.mc.field_71439_g.field_70159_w = 0.0;
                        if (((Boolean)this.verusReDamageValue.get()).booleanValue()) {
                            this.dmgCooldown = (Integer)this.verusReDmgTickValue.get();
                        }
                    }
                } else if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("InstantC06")) {
                    if (Fly.mc.field_71439_g.field_70122_E && Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
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
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1, z, true));
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
            case "watchdog": {
                this.expectItemStack = this.getSlimeSlot();
                if (this.expectItemStack == -1 || !Fly.mc.field_71439_g.field_70122_E) break;
                Fly.mc.field_71439_g.func_70664_aZ();
                this.wdState = 1;
                break;
            }
            case "boosthypixel": {
                int i;
                if (!Fly.mc.field_71439_g.field_70122_E) break;
                if (((Boolean)this.hypixelC04.get()).booleanValue()) {
                    for (i = 0; i < 10; ++i) {
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, true));
                    }
                }
                if (((String)this.hypixelBoostMode.get()).equalsIgnoreCase("ncp")) {
                    for (i = 0; i < 65; ++i) {
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.049, Fly.mc.field_71439_g.field_70161_v, false));
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, false));
                    }
                } else {
                    double fallDistance;
                    double d = fallDistance = ((String)this.hypixelBoostMode.get()).equalsIgnoreCase("morepackets") ? 3.4025 : 3.0125;
                    while (fallDistance > 0.0) {
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.0624986421, Fly.mc.field_71439_g.field_70161_v, false));
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.0625, Fly.mc.field_71439_g.field_70161_v, false));
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.0624986421, Fly.mc.field_71439_g.field_70161_v, false));
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 1.3579E-6, Fly.mc.field_71439_g.field_70161_v, false));
                        fallDistance -= 0.0624986421;
                    }
                }
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, true));
                if (((Boolean)this.hypixelVisualY.get()).booleanValue()) {
                    Fly.mc.field_71439_g.func_70664_aZ();
                    Fly.mc.field_71439_g.field_70163_u += (double)0.42f;
                }
                this.boostHypixelState = 1;
                this.moveSpeed = 0.1;
                this.lastDistance = 0.0;
                this.failedStart = false;
            }
        }
        this.startY = Fly.mc.field_71439_g.field_70163_u;
        this.noPacketModify = false;
        this.aacJump = -3.8;
        if (mode.equalsIgnoreCase("freehypixel")) {
            this.freeHypixelTimer.reset();
            Fly.mc.field_71439_g.func_70634_a(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.42, Fly.mc.field_71439_g.field_70161_v);
            this.freeHypixelYaw = Fly.mc.field_71439_g.field_70177_z;
            this.freeHypixelPitch = Fly.mc.field_71439_g.field_70125_A;
        }
        if (!(mode.equalsIgnoreCase("watchdog") || mode.equalsIgnoreCase("bugspartan") || mode.equalsIgnoreCase("verus") || mode.equalsIgnoreCase("damage") || mode.toLowerCase().contains("hypixel") || !((Boolean)this.fakeDmgValue.get()).booleanValue())) {
            Fly.mc.field_71439_g.func_70103_a((byte)2);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.wasDead = false;
        MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        this.noFlag = false;
        String mode = (String)this.modeValue.get();
        if (((Boolean)this.resetMotionValue.get()).booleanValue() && !mode.toUpperCase().startsWith("AAC") && !mode.equalsIgnoreCase("Hypixel") && !mode.equalsIgnoreCase("CubeCraft") && !mode.equalsIgnoreCase("Collide") && !mode.equalsIgnoreCase("Verus") && !mode.equalsIgnoreCase("Jump") && !mode.equalsIgnoreCase("creative") || mode.equalsIgnoreCase("pearl") && this.pearlState != -1) {
            Fly.mc.field_71439_g.field_70159_w = 0.0;
            Fly.mc.field_71439_g.field_70181_x = 0.0;
            Fly.mc.field_71439_g.field_70179_y = 0.0;
        }
        if (((Boolean)this.resetMotionValue.get()).booleanValue() && this.boostTicks > 0 && mode.equalsIgnoreCase("Verus")) {
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
            case "vanilla": {
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
            case "cubecraft": {
                Fly.mc.field_71428_T.field_74278_d = 0.6f;
                this.cubecraftTeleportTickTimer.update();
                break;
            }
            case "blocksmctest": {
                Intrinsics.checkNotNullParameter(event, "event");
                EntityPlayerSP field_71439_g = MinecraftInstance.mc.field_71439_g;
                field_71439_g.field_70181_x += 0.025;
                if (MovementUtils.isMoving() && MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    MinecraftInstance.mc.field_71439_g.func_70664_aZ();
                }
                if (!MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    MinecraftInstance.mc.field_71428_T.field_74278_d = ((Number)this.timerSpeedValue.get()).floatValue();
                }
                if (MinecraftInstance.mc.field_71439_g.field_70122_E) break;
                MovementUtils.strafe(MovementUtils.getSpeed() * (this.canBoost ? 40.0f : 1.0f));
                if (!this.canBoost) break;
                this.canBoost = false;
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
                                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, false));
                                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, false));
                                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, true));
                                Fly.mc.field_71439_g.field_70179_y = 0.0;
                                Fly.mc.field_71439_g.field_70159_w = 0.0;
                            }
                        } else if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("InstantC06") && Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                            PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y + 4.0, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                            PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, false));
                            PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
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
            case "aac1.9.10": {
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    this.aacJump += 0.2;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    this.aacJump -= 0.2;
                }
                if (this.startY + this.aacJump > Fly.mc.field_71439_g.field_70163_u) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    Fly.mc.field_71439_g.field_70181_x = 0.8;
                    MovementUtils.strafe(((Float)this.aacSpeedValue.get()).floatValue());
                }
                MovementUtils.strafe();
                break;
            }
            case "aac3.0.5": {
                if (this.aac3delay == 2) {
                    Fly.mc.field_71439_g.field_70181_x = 0.1;
                } else if (this.aac3delay > 2) {
                    this.aac3delay = 0;
                }
                if (((Boolean)this.aacFast.get()).booleanValue()) {
                    Fly.mc.field_71439_g.field_70747_aH = (double)Fly.mc.field_71439_g.field_71158_b.field_78902_a == 0.0 ? 0.08f : 0.0f;
                }
                ++this.aac3delay;
                break;
            }
            case "aac3.1.6-gomme": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                if (this.aac3delay == 2) {
                    Fly.mc.field_71439_g.field_70181_x += 0.05;
                } else if (this.aac3delay > 2) {
                    Fly.mc.field_71439_g.field_70181_x -= 0.05;
                    this.aac3delay = 0;
                }
                ++this.aac3delay;
                if (!this.noFlag) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70122_E));
                }
                if (!(Fly.mc.field_71439_g.field_70163_u <= 0.0)) break;
                this.noFlag = true;
                break;
            }
            case "flag": {
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t + Fly.mc.field_71439_g.field_70159_w * 999.0, Fly.mc.field_71439_g.field_70163_u + (Fly.mc.field_71474_y.field_74314_A.func_151470_d() ? 1.5624 : 1.0E-8) - (Fly.mc.field_71474_y.field_74311_E.func_151470_d() ? 0.0624 : 2.0E-8), Fly.mc.field_71439_g.field_70161_v + Fly.mc.field_71439_g.field_70179_y * 999.0, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t + Fly.mc.field_71439_g.field_70159_w * 999.0, Fly.mc.field_71439_g.field_70163_u - 6969.0, Fly.mc.field_71439_g.field_70161_v + Fly.mc.field_71439_g.field_70179_y * 999.0, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
                Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t + Fly.mc.field_71439_g.field_70159_w * 11.0, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v + Fly.mc.field_71439_g.field_70179_y * 11.0);
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "keepalive": {
                mc.func_147114_u().func_147297_a((Packet)new C00PacketKeepAlive());
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
            case "minesecure": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                if (!Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.01f;
                }
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                MovementUtils.strafe(vanillaSpeed);
                if (!this.mineSecureVClipTimer.hasTimePassed(150L) || !Fly.mc.field_71474_y.field_74314_A.func_151470_d()) break;
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 5.0, Fly.mc.field_71439_g.field_70161_v, false));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, -1000.0, 0.5, false));
                double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
                double x = -Math.sin(yaw) * 0.4;
                double z = Math.cos(yaw) * 0.4;
                Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t + x, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v + z);
                this.mineSecureVClipTimer.reset();
                break;
            }
            case "hac": {
                Fly.mc.field_71439_g.field_70159_w *= 0.8;
                Fly.mc.field_71439_g.field_70179_y *= 0.8;
            }
            case "hawkeye": {
                Fly.mc.field_71439_g.field_70181_x = Fly.mc.field_71439_g.field_70181_x <= -0.42 ? 0.42 : -0.42;
                break;
            }
            case "teleportrewinside": {
                Vec3 vectorStart = new Vec3(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v);
                float yaw = -Fly.mc.field_71439_g.field_70177_z;
                float pitch = -Fly.mc.field_71439_g.field_70125_A;
                double length = 9.9;
                Vec3 vectorEnd = new Vec3(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 9.9 + vectorStart.field_72450_a, Math.sin(Math.toRadians(pitch)) * 9.9 + vectorStart.field_72448_b, Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 9.9 + vectorStart.field_72449_c);
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd.field_72450_a, Fly.mc.field_71439_g.field_70163_u + 2.0, vectorEnd.field_72449_c, true));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorStart.field_72450_a, Fly.mc.field_71439_g.field_70163_u + 2.0, vectorStart.field_72449_c, true));
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "minesucht": {
                double posX = Fly.mc.field_71439_g.field_70165_t;
                double posY = Fly.mc.field_71439_g.field_70163_u;
                double posZ = Fly.mc.field_71439_g.field_70161_v;
                if (!Fly.mc.field_71474_y.field_74351_w.func_151470_d()) break;
                if (System.currentTimeMillis() - this.minesuchtTP > 99L) {
                    Vec3 vec3 = Fly.mc.field_71439_g.func_174824_e(0.0f);
                    Vec3 vec31 = Fly.mc.field_71439_g.func_70676_i(0.0f);
                    Vec3 vec32 = vec3.func_72441_c(vec31.field_72450_a * 7.0, vec31.field_72448_b * 7.0, vec31.field_72449_c * 7.0);
                    if ((double)Fly.mc.field_71439_g.field_70143_R > 0.8) {
                        Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 50.0, posZ, false));
                        Fly.mc.field_71439_g.func_180430_e(100.0f, 100.0f);
                        Fly.mc.field_71439_g.field_70143_R = 0.0f;
                        Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 20.0, posZ, true));
                    }
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vec32.field_72450_a, Fly.mc.field_71439_g.field_70163_u + 50.0, vec32.field_72449_c, true));
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vec32.field_72450_a, posY, vec32.field_72449_c, true));
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    this.minesuchtTP = System.currentTimeMillis();
                    break;
                }
                Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, false));
                Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, true));
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
            case "mineplex": {
                if (Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null) {
                    if (Fly.mc.field_71474_y.field_74314_A.func_151470_d() && this.mineplexTimer.hasTimePassed(100L)) {
                        Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.6, Fly.mc.field_71439_g.field_70161_v);
                        this.mineplexTimer.reset();
                    }
                    if (Fly.mc.field_71439_g.func_70093_af() && this.mineplexTimer.hasTimePassed(100L)) {
                        Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u - 0.6, Fly.mc.field_71439_g.field_70161_v);
                        this.mineplexTimer.reset();
                    }
                    BlockPos blockPos = new BlockPos(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.func_174813_aQ().field_72338_b - 1.0, Fly.mc.field_71439_g.field_70161_v);
                    Vec3 vec = new Vec3((Vec3i)blockPos).func_72441_c((double)0.4f, (double)0.4f, (double)0.4f).func_178787_e(new Vec3(EnumFacing.UP.func_176730_m()));
                    Fly.mc.field_71442_b.func_178890_a(Fly.mc.field_71439_g, Fly.mc.field_71441_e, Fly.mc.field_71439_g.field_71071_by.func_70448_g(), blockPos, EnumFacing.UP, new Vec3(vec.field_72450_a * (double)0.4f, vec.field_72448_b * (double)0.4f, vec.field_72449_c * (double)0.4f));
                    MovementUtils.strafe(0.27f);
                    Fly.mc.field_71428_T.field_74278_d = 1.0f + ((Float)this.mineplexSpeedValue.get()).floatValue();
                    break;
                }
                Fly.mc.field_71428_T.field_74278_d = 1.0f;
                this.setState(false);
                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lMineplex-\u00a7a\u00a7lFly\u00a78] \u00a7aSelect an empty slot to fly.");
                break;
            }
            case "aac3.3.12": {
                if (Fly.mc.field_71439_g.field_70163_u < -70.0) {
                    Fly.mc.field_71439_g.field_70181_x = ((Float)this.aacMotion.get()).floatValue();
                }
                Fly.mc.field_71428_T.field_74278_d = 1.0f;
                if (!Keyboard.isKeyDown((int)29)) break;
                Fly.mc.field_71428_T.field_74278_d = 0.2f;
                Fly.mc.field_71467_ac = 0;
                break;
            }
            case "aac3.3.12-glide": {
                if (!Fly.mc.field_71439_g.field_70122_E) {
                    ++this.aac3glideDelay;
                }
                if (this.aac3glideDelay == 2) {
                    Fly.mc.field_71428_T.field_74278_d = 1.0f;
                }
                if (this.aac3glideDelay == 12) {
                    Fly.mc.field_71428_T.field_74278_d = 0.1f;
                }
                if (this.aac3glideDelay < 12 || Fly.mc.field_71439_g.field_70122_E) break;
                this.aac3glideDelay = 0;
                Fly.mc.field_71439_g.field_70181_x = 0.015;
                break;
            }
            case "aac3.3.13": {
                if (Fly.mc.field_71439_g.field_70128_L) {
                    this.wasDead = true;
                }
                if (this.wasDead || Fly.mc.field_71439_g.field_70122_E) {
                    this.wasDead = false;
                    Fly.mc.field_71439_g.field_70181_x = ((Float)this.aacMotion2.get()).floatValue();
                    Fly.mc.field_71439_g.field_70122_E = false;
                }
                Fly.mc.field_71428_T.field_74278_d = 1.0f;
                if (!Keyboard.isKeyDown((int)29)) break;
                Fly.mc.field_71428_T.field_74278_d = 0.2f;
                Fly.mc.field_71467_ac = 0;
                break;
            }
            case "watchcat": {
                MovementUtils.strafe(0.15f);
                Fly.mc.field_71439_g.func_70031_b(true);
                if (Fly.mc.field_71439_g.field_70163_u < this.startY + 2.0) {
                    Fly.mc.field_71439_g.field_70181_x = Math.random() * 0.5;
                    break;
                }
                if (!(this.startY > Fly.mc.field_71439_g.field_70163_u)) break;
                MovementUtils.strafe(0.0f);
                break;
            }
            case "spartan": {
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                this.spartanTimer.update();
                if (!this.spartanTimer.hasTimePassed(12)) break;
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 8.0, Fly.mc.field_71439_g.field_70161_v, true));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u - 8.0, Fly.mc.field_71439_g.field_70161_v, true));
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
                if (((String)this.pearlActivateCheck.get()).equalsIgnoreCase("damage") && this.pearlState == 1 && Fly.mc.field_71439_g.field_70737_aN > 0) {
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
            case "neruxvace": {
                if (!Fly.mc.field_71439_g.field_70122_E) {
                    ++this.aac3glideDelay;
                }
                if (this.aac3glideDelay < (Integer)this.neruxVaceTicks.get() || Fly.mc.field_71439_g.field_70122_E) break;
                this.aac3glideDelay = 0;
                Fly.mc.field_71439_g.field_70181_x = 0.015;
                break;
            }
            case "hypixel": {
                int boostDelay = (Integer)this.hypixelBoostDelay.get();
                if (((Boolean)this.hypixelBoost.get()).booleanValue() && !this.flyTimer.hasTimePassed(boostDelay)) {
                    Fly.mc.field_71428_T.field_74278_d = 1.0f + ((Float)this.hypixelBoostTimer.get()).floatValue() * ((float)this.flyTimer.hasTimeLeft(boostDelay) / (float)boostDelay);
                }
                this.hypixelTimer.update();
                if (!this.hypixelTimer.hasTimePassed(2)) break;
                Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 1.0E-5, Fly.mc.field_71439_g.field_70161_v);
                this.hypixelTimer.reset();
                break;
            }
            case "freehypixel": {
                if (this.freeHypixelTimer.hasTimePassed(10)) {
                    Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                    break;
                }
                Fly.mc.field_71439_g.field_70177_z = this.freeHypixelYaw;
                Fly.mc.field_71439_g.field_70125_A = this.freeHypixelPitch;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                if (this.startY != new BigDecimal(Fly.mc.field_71439_g.field_70163_u).setScale(3, RoundingMode.HALF_DOWN).doubleValue()) break;
                this.freeHypixelTimer.update();
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
        if (((String)this.modeValue.get()).equalsIgnoreCase("boosthypixel")) {
            switch (event.getEventState()) {
                case PRE: {
                    this.hypixelTimer.update();
                    if (this.hypixelTimer.hasTimePassed(2)) {
                        Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 1.0E-5, Fly.mc.field_71439_g.field_70161_v);
                        this.hypixelTimer.reset();
                    }
                    if (this.failedStart) break;
                    Fly.mc.field_71439_g.field_70181_x = 0.0;
                    break;
                }
                case POST: {
                    double xDist = Fly.mc.field_71439_g.field_70165_t - Fly.mc.field_71439_g.field_70169_q;
                    double zDist = Fly.mc.field_71439_g.field_70161_v - Fly.mc.field_71439_g.field_70166_s;
                    this.lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
                }
            }
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "funcraft": {
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
                int current = Fly.mc.field_71439_g.field_71071_by.field_70461_c;
                if (event.getEventState() == EventState.PRE) {
                    if (this.wdState == 1 && Fly.mc.field_71441_e.func_72945_a((Entity)Fly.mc.field_71439_g, Fly.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -1.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(this.expectItemStack));
                        this.wdState = 2;
                    }
                    Fly.mc.field_71428_T.field_74278_d = 1.0f;
                    if (this.wdState == 3 && this.expectItemStack != -1) {
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(current));
                        this.expectItemStack = -1;
                    }
                    if (this.wdState == 4) {
                        if (MovementUtils.isMoving()) {
                            MovementUtils.strafe(0.0f);
                        }
                        Fly.mc.field_71439_g.field_70181_x = -0.0015f;
                        break;
                    }
                    if (this.wdState >= 3) break;
                    float[] fArray = RotationUtils.getRotationFromPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70161_v, (int)Fly.mc.field_71439_g.field_70163_u - 1);
                    break;
                }
                if (this.wdState != 2) break;
                this.wdState = 3;
            }
        }
    }

    public float coerceAtMost(double value, double max) {
        return (float)Math.min(value, max);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        double y;
        String mode = (String)this.modeValue.get();
        if (!((Boolean)this.markValue.get()).booleanValue() || mode.equalsIgnoreCase("Vanilla") || mode.equalsIgnoreCase("Creative") || mode.equalsIgnoreCase("Damage") || mode.equalsIgnoreCase("AAC5-Vanilla") || mode.equalsIgnoreCase("Derp") || mode.equalsIgnoreCase("KeepAlive")) {
            return;
        }
        RenderUtils.drawPlatform(y, Fly.mc.field_71439_g.func_174813_aQ().field_72337_e < (y = this.startY + 2.0) ? new Color(0, 255, 0, 90) : new Color(255, 0, 0, 90), 1.0);
        switch (mode.toLowerCase()) {
            case "aac1.9.10": {
                RenderUtils.drawPlatform(this.startY + this.aacJump, new Color(0, 0, 255, 90), 1.0);
                break;
            }
            case "aac3.3.12": {
                RenderUtils.drawPlatform(-70.0, new Color(0, 0, 255, 90), 1.0);
            }
        }
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
        if (((Boolean)this.speedDisplay.get()).booleanValue()) {
            ScaledResolution sr = new ScaledResolution(mc);
            double xDiff = (Fly.mc.field_71439_g.field_70165_t - Fly.mc.field_71439_g.field_70142_S) * 2.0;
            double zDiff = (Fly.mc.field_71439_g.field_70161_v - Fly.mc.field_71439_g.field_70136_U) * 2.0;
            BigDecimal bg = new BigDecimal((double)MathHelper.func_76133_a((double)(xDiff * xDiff + zDiff * zDiff)) * 10.0);
            int speed = (int)((float)bg.intValue() * Fly.mc.field_71428_T.field_74278_d);
            String str = speed + "block/sec";
            Fonts.font35.func_78276_b(str, (sr.func_78326_a() - Fonts.font35.func_78256_a(str)) / 2, sr.func_78328_b() / 2 - 20, Colors.WHITE.c);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        String mode = (String)this.modeValue.get();
        if (this.noPacketModify) {
            return;
        }
        if (packet instanceof C09PacketHeldItemChange && mode.equalsIgnoreCase("watchdog") && this.wdState < 4) {
            event.cancelEvent();
        }
        if (packet instanceof S08PacketPlayerPosLook) {
            if (mode.equalsIgnoreCase("watchdog") && this.wdState == 3) {
                this.wdState = 4;
                if (((Boolean)this.fakeDmgValue.get()).booleanValue() && Fly.mc.field_71439_g != null) {
                    Fly.mc.field_71439_g.func_70103_a((byte)2);
                }
            }
            if (mode.equalsIgnoreCase("pearl") && ((String)this.pearlActivateCheck.get()).equalsIgnoreCase("teleport") && this.pearlState == 1) {
                this.pearlState = 2;
            }
            if (mode.equalsIgnoreCase("BoostHypixel")) {
                this.failedStart = true;
                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lBoostHypixel-\u00a7a\u00a7lFly\u00a78] \u00a7cSetback detected.");
            }
        }
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            boolean lastOnGround = packetPlayer.field_149474_g;
            if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null || mode.equalsIgnoreCase("Verus") && ((Boolean)this.verusSpoofGround.get()).booleanValue() && this.verusDmged) {
                packetPlayer.field_149474_g = true;
            }
            if (mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("BoostHypixel")) {
                packetPlayer.field_149474_g = false;
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
            if ((mode.equalsIgnoreCase("vanilla") || mode.equalsIgnoreCase("creative")) && ((Boolean)this.groundSpoofValue.get()).booleanValue()) {
                packetPlayer.field_149474_g = true;
            }
            if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump") && this.verusJumpTimes < 5 && mode.equalsIgnoreCase("Verus")) {
                packetPlayer.field_149474_g = false;
            }
        }
    }

    private void sendAAC5Packets() {
        float yaw = Fly.mc.field_71439_g.field_70177_z;
        float pitch = Fly.mc.field_71439_g.field_70125_A;
        for (C03PacketPlayer packet : this.aac5C03List) {
            PacketUtils.sendPacketNoEvent(packet);
            if (!packet.func_149466_j()) continue;
            if (packet.func_149463_k()) {
                yaw = packet.field_149476_e;
                pitch = packet.field_149473_f;
            }
            switch ((String)this.aac5Packet.get()) {
                case "Original": {
                    if (((Boolean)this.aac5UseC04Packet.get()).booleanValue()) {
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, 1.0E159, packet.field_149478_c, true));
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, true));
                        break;
                    }
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, 1.0E159, packet.field_149478_c, yaw, pitch, true));
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, yaw, pitch, true));
                    break;
                }
                case "Rise": {
                    if (((Boolean)this.aac5UseC04Packet.get()).booleanValue()) {
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, -1.0E159, packet.field_149478_c + 10.0, true));
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, true));
                        break;
                    }
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, -1.0E159, packet.field_149478_c + 10.0, yaw, pitch, true));
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, yaw, pitch, true));
                    break;
                }
                case "Other": {
                    if (((Boolean)this.aac5UseC04Packet.get()).booleanValue()) {
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, Double.MAX_VALUE, packet.field_149478_c, true));
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, true));
                        break;
                    }
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, Double.MAX_VALUE, packet.field_149478_c, yaw, pitch, true));
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.field_149479_a, packet.field_149477_b, packet.field_149478_c, yaw, pitch, true));
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
            case "gta5": {
                if (!(Fly.mc.field_71439_g.field_70143_R >= 1.0f)) break;
                Fly.mc.field_71439_g.field_70143_R = 0.0f;
                Fly.mc.field_71439_g.func_70664_aZ();
                event.setY(0.42);
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
            case "cubecraft": {
                double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
                if (this.cubecraftTeleportTickTimer.hasTimePassed(2)) {
                    event.setX(-Math.sin(yaw) * 2.4);
                    event.setZ(Math.cos(yaw) * 2.4);
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                event.setX(-Math.sin(yaw) * 0.2);
                event.setZ(Math.cos(yaw) * 0.2);
                break;
            }
            case "boosthypixel": {
                if (!MovementUtils.isMoving()) {
                    event.setX(0.0);
                    event.setZ(0.0);
                    break;
                }
                if (this.failedStart) break;
                double amplifier = 1.0 + (Fly.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? 0.2 * (double)(Fly.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1) : 0.0);
                double baseSpeed = 0.29 * amplifier;
                switch (this.boostHypixelState) {
                    case 1: {
                        this.moveSpeed = (Fly.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? 1.56 : 2.034) * baseSpeed;
                        this.boostHypixelState = 2;
                        break;
                    }
                    case 2: {
                        this.moveSpeed *= 2.16;
                        this.boostHypixelState = 3;
                        break;
                    }
                    case 3: {
                        this.moveSpeed = this.lastDistance - (Fly.mc.field_71439_g.field_70173_aa % 2 == 0 ? 0.0103 : 0.0123) * (this.lastDistance - baseSpeed);
                        this.boostHypixelState = 4;
                        break;
                    }
                    default: {
                        this.moveSpeed = this.lastDistance - this.lastDistance / 159.8;
                    }
                }
                this.moveSpeed = Math.max(this.moveSpeed, 0.3);
                double yaw = MovementUtils.getDirection();
                event.setX(-Math.sin(yaw) * this.moveSpeed);
                event.setZ(Math.cos(yaw) * this.moveSpeed);
                Fly.mc.field_71439_g.field_70159_w = event.getX();
                Fly.mc.field_71439_g.field_70179_y = event.getZ();
                break;
            }
            case "freehypixel": {
                if (this.freeHypixelTimer.hasTimePassed(10)) break;
                event.zero();
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
        if (event.getBlock() instanceof BlockAir && (mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("BoostHypixel") || mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null || mode.equalsIgnoreCase("Verus") && (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("none") || this.verusDmged)) && (double)event.getY() < Fly.mc.field_71439_g.field_70163_u) {
            event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), (double)Fly.mc.field_71439_g.field_70163_u, (double)(event.getZ() + 1)));
        }
    }

    @EventTarget
    public void onJump(JumpEvent e) {
        String mode = (String)this.modeValue.get();
        this.canBoost = true;
        if (mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("BoostHypixel") || mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null || mode.equalsIgnoreCase("FunCraft") && this.moveSpeed > 0.0 || mode.equalsIgnoreCase("watchdog") && this.wdState >= 1) {
            e.cancelEvent();
        }
    }

    @EventTarget
    public void onStep(StepEvent e) {
        String mode = (String)this.modeValue.get();
        if (mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("BoostHypixel") || mode.equalsIgnoreCase("Rewinside") || mode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null || mode.equalsIgnoreCase("FunCraft") || mode.equalsIgnoreCase("watchdog")) {
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
            mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, posY, Fly.mc.field_71439_g.field_70161_v, true));
            if (posY - 8.0 < ground) break;
        }
        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, ground, Fly.mc.field_71439_g.field_70161_v, true));
        for (posY = ground; posY < Fly.mc.field_71439_g.field_70163_u; posY += 8.0) {
            mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, posY, Fly.mc.field_71439_g.field_70161_v, true));
            if (posY + 8.0 > Fly.mc.field_71439_g.field_70163_u) break;
        }
        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, true));
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

    private int getSlimeSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemBlock itemBlock;
            ItemStack stack = Fly.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null || stack.func_77973_b() == null || !(stack.func_77973_b() instanceof ItemBlock) || !((itemBlock = (ItemBlock)stack.func_77973_b()).func_179223_d() instanceof BlockSlime)) continue;
            return i - 36;
        }
        return -1;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

