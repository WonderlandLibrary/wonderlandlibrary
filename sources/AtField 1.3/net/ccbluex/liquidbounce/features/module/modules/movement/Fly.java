/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly$WhenMappings;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name="Fly", description="Allows you to fly in survival mode.", category=ModuleCategory.MOVEMENT, keyBind=33)
public final class Fly
extends Module {
    private final FloatValue mineplexSpeedValue;
    private final MSTimer flyTimer;
    private final IntegerValue hypixelBoostDelay;
    private final MSTimer mineSecureVClipTimer;
    private long minesuchtTP;
    private final TickTimer spartanTimer;
    private final FloatValue aacSpeedValue;
    private final BoolValue vanillaKickBypassValue;
    private double lastDistance;
    private final MSTimer groundTimer;
    private int aac3glideDelay;
    private boolean failedStart;
    private final FloatValue aacMotion2;
    private int boostHypixelState = 1;
    private double aacJump;
    private float freeHypixelPitch;
    private final TickTimer cubecraftTeleportTickTimer;
    private final IntegerValue neruxVaceTicks;
    private final FloatValue vanillaSpeedValue;
    private double startY;
    private int aac3delay;
    private float freeHypixelYaw;
    private final FloatValue aacMotion;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "SmoothVanilla", "NCP", "OldNCP", "AAC1.9.10", "AAC3.0.5", "AAC3.1.6-Gomme", "AAC3.3.12", "AAC3.3.12-Glide", "AAC3.3.13", "CubeCraft", "Hypixel", "BoostHypixel", "FreeHypixel", "Rewinside", "TeleportRewinside", "Mineplex", "NeruxVace", "Minesucht", "Spartan", "Spartan2", "BugSpartan", "MineSecure", "HawkEye", "HAC", "WatchCat", "Jetpack", "KeepAlive", "Flag"}, "Vanilla");
    private final FloatValue ncpMotionValue;
    private final TickTimer hypixelTimer;
    private final TickTimer freeHypixelTimer;
    private double moveSpeed;
    private final BoolValue hypixelBoost;
    private final MSTimer mineplexTimer;
    private final BoolValue markValue;
    private boolean noFlag;
    private final FloatValue hypixelBoostTimer;
    private final BoolValue aacFast;
    private boolean wasDead;
    private boolean noPacketModify;
    private final TickTimer cubecraft2TickTimer;

    public Fly() {
        this.vanillaSpeedValue = new FloatValue("VanillaSpeed", 2.0f, 0.0f, 5.0f);
        this.vanillaKickBypassValue = new BoolValue("VanillaKickBypass", false);
        this.ncpMotionValue = new FloatValue("NCPMotion", 0.0f, 0.0f, 1.0f);
        this.aacSpeedValue = new FloatValue("AAC1.9.10-Speed", 0.3f, 0.0f, 1.0f);
        this.aacFast = new BoolValue("AAC3.0.5-Fast", true);
        this.aacMotion = new FloatValue("AAC3.3.12-Motion", 10.0f, 0.1f, 10.0f);
        this.aacMotion2 = new FloatValue("AAC3.3.13-Motion", 10.0f, 0.1f, 10.0f);
        this.hypixelBoost = new BoolValue("Hypixel-Boost", true);
        this.hypixelBoostDelay = new IntegerValue("Hypixel-BoostDelay", 1200, 0, 2000);
        this.hypixelBoostTimer = new FloatValue("Hypixel-BoostTimer", 1.0f, 0.0f, 5.0f);
        this.mineplexSpeedValue = new FloatValue("MineplexSpeed", 1.0f, 0.5f, 10.0f);
        this.neruxVaceTicks = new IntegerValue("NeruxVace-Ticks", 6, 0, 20);
        this.markValue = new BoolValue("Mark", true);
        this.flyTimer = new MSTimer();
        this.groundTimer = new MSTimer();
        this.mineSecureVClipTimer = new MSTimer();
        this.spartanTimer = new TickTimer();
        this.mineplexTimer = new MSTimer();
        this.hypixelTimer = new TickTimer();
        this.cubecraft2TickTimer = new TickTimer();
        this.cubecraftTeleportTickTimer = new TickTimer();
        this.freeHypixelTimer = new TickTimer();
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        float f = ((Number)this.vanillaSpeedValue.get()).floatValue();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        Fly fly = this;
        boolean bl = false;
        boolean bl2 = false;
        Fly fly2 = fly;
        boolean bl3 = false;
        String string = (String)fly2.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "vanilla": {
                iEntityPlayerSP2.getCapabilities().setFlying(false);
                iEntityPlayerSP2.setMotionY(0.0);
                iEntityPlayerSP2.setMotionX(0.0);
                iEntityPlayerSP2.setMotionZ(0.0);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                    iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + (double)f);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                    iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() - (double)f);
                }
                MovementUtils.strafe(f);
                fly2.handleVanillaKickBypass();
                break;
            }
            case "smoothvanilla": {
                iEntityPlayerSP2.getCapabilities().setFlying(true);
                fly2.handleVanillaKickBypass();
                break;
            }
            case "cubecraft": {
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.6f);
                fly2.cubecraftTeleportTickTimer.update();
                break;
            }
            case "ncp": {
                iEntityPlayerSP2.setMotionY(-((Number)fly2.ncpMotionValue.get()).floatValue());
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    iEntityPlayerSP2.setMotionY(-0.5);
                }
                MovementUtils.strafe$default(0.0f, 1, null);
                break;
            }
            case "oldncp": {
                if (fly2.startY > iEntityPlayerSP2.getPosY()) {
                    iEntityPlayerSP2.setMotionY(-1.0E-33);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    iEntityPlayerSP2.setMotionY(-0.2);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() && iEntityPlayerSP2.getPosY() < fly2.startY - 0.1) {
                    iEntityPlayerSP2.setMotionY(0.2);
                }
                MovementUtils.strafe$default(0.0f, 1, null);
                break;
            }
            case "aac1.9.10": {
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    fly2.aacJump += 0.2;
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    fly2.aacJump -= 0.2;
                }
                if (fly2.startY + fly2.aacJump > iEntityPlayerSP2.getPosY()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                    iEntityPlayerSP2.setMotionY(0.8);
                    MovementUtils.strafe(((Number)fly2.aacSpeedValue.get()).floatValue());
                }
                MovementUtils.strafe$default(0.0f, 1, null);
                break;
            }
            case "aac3.0.5": {
                if (fly2.aac3delay == 2) {
                    iEntityPlayerSP2.setMotionY(0.1);
                } else if (fly2.aac3delay > 2) {
                    fly2.aac3delay = 0;
                }
                if (((Boolean)fly2.aacFast.get()).booleanValue()) {
                    if (iEntityPlayerSP2.getMovementInput().getMoveStrafe() == 0.0f) {
                        iEntityPlayerSP2.setJumpMovementFactor(0.08f);
                    } else {
                        iEntityPlayerSP2.setJumpMovementFactor(0.0f);
                    }
                }
                n = fly2.aac3delay;
                fly2.aac3delay = n + 1;
                break;
            }
            case "aac3.1.6-gomme": {
                iEntityPlayerSP2.getCapabilities().setFlying(true);
                if (fly2.aac3delay == 2) {
                    IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                    iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() + 0.05);
                } else if (fly2.aac3delay > 2) {
                    IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
                    iEntityPlayerSP6.setMotionY(iEntityPlayerSP6.getMotionY() - 0.05);
                    fly2.aac3delay = 0;
                }
                n = fly2.aac3delay;
                fly2.aac3delay = n + 1;
                if (!fly2.noFlag) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ(), iEntityPlayerSP2.getOnGround()));
                }
                if (!(iEntityPlayerSP2.getPosY() <= 0.0)) break;
                fly2.noFlag = true;
                break;
            }
            case "flag": {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(iEntityPlayerSP2.getPosX() + iEntityPlayerSP2.getMotionX() * (double)999, iEntityPlayerSP2.getPosY() + (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() ? 1.5624 : 1.0E-8) - (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown() ? 0.0624 : 2.0E-8), iEntityPlayerSP2.getPosZ() + iEntityPlayerSP2.getMotionZ() * (double)999, iEntityPlayerSP2.getRotationYaw(), iEntityPlayerSP2.getRotationPitch(), true));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(iEntityPlayerSP2.getPosX() + iEntityPlayerSP2.getMotionX() * (double)999, iEntityPlayerSP2.getPosY() - (double)6969, iEntityPlayerSP2.getPosZ() + iEntityPlayerSP2.getMotionZ() * (double)999, iEntityPlayerSP2.getRotationYaw(), iEntityPlayerSP2.getRotationPitch(), true));
                iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX() + iEntityPlayerSP2.getMotionX() * (double)11, iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ() + iEntityPlayerSP2.getMotionZ() * (double)11);
                iEntityPlayerSP2.setMotionY(0.0);
                break;
            }
            case "keepalive": {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketKeepAlive());
                iEntityPlayerSP2.getCapabilities().setFlying(false);
                iEntityPlayerSP2.setMotionY(0.0);
                iEntityPlayerSP2.setMotionX(0.0);
                iEntityPlayerSP2.setMotionZ(0.0);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                    iEntityPlayerSP7.setMotionY(iEntityPlayerSP7.getMotionY() + (double)f);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                    iEntityPlayerSP8.setMotionY(iEntityPlayerSP8.getMotionY() - (double)f);
                }
                MovementUtils.strafe(f);
                break;
            }
            case "minesecure": {
                iEntityPlayerSP2.getCapabilities().setFlying(false);
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    iEntityPlayerSP2.setMotionY(-0.01);
                }
                iEntityPlayerSP2.setMotionX(0.0);
                iEntityPlayerSP2.setMotionZ(0.0);
                MovementUtils.strafe(f);
                if (!fly2.mineSecureVClipTimer.hasTimePassed(150L) || !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + (double)5, iEntityPlayerSP2.getPosZ(), false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(0.5, -1000.0, 0.5, false));
                double d = Math.toRadians(iEntityPlayerSP2.getRotationYaw());
                boolean bl4 = false;
                double d2 = -Math.sin(d) * 0.4;
                boolean bl5 = false;
                double d3 = Math.cos(d) * 0.4;
                iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX() + d2, iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ() + d3);
                fly2.mineSecureVClipTimer.reset();
                break;
            }
            case "hac": {
                IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP2;
                iEntityPlayerSP9.setMotionX(iEntityPlayerSP9.getMotionX() * 0.8);
                IEntityPlayerSP iEntityPlayerSP10 = iEntityPlayerSP2;
                iEntityPlayerSP10.setMotionZ(iEntityPlayerSP10.getMotionZ() * 0.8);
                iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() <= -0.42 ? 0.42 : -0.42);
                break;
            }
            case "hawkeye": {
                iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() <= -0.42 ? 0.42 : -0.42);
                break;
            }
            case "teleportrewinside": {
                WVec3 wVec3 = new WVec3(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ());
                float f2 = -iEntityPlayerSP2.getRotationYaw();
                float f3 = -iEntityPlayerSP2.getRotationPitch();
                double d = 9.9;
                double d4 = Math.toRadians(f2);
                boolean bl6 = false;
                double d5 = Math.sin(d4);
                d4 = Math.toRadians(f3);
                bl6 = false;
                double d6 = Math.cos(d4);
                d4 = Math.toRadians(f3);
                d5 = d5 * d6 * d + wVec3.getXCoord();
                bl6 = false;
                d6 = Math.sin(d4);
                d4 = Math.toRadians(f2);
                d6 = d6 * d + wVec3.getYCoord();
                bl6 = false;
                double d7 = Math.cos(d4);
                d4 = Math.toRadians(f3);
                bl6 = false;
                double d8 = Math.cos(d4);
                double d9 = d7 * d8 * d + wVec3.getZCoord();
                double d10 = d6;
                double d11 = d5;
                WVec3 wVec32 = new WVec3(d11, d10, d9);
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(wVec32.getXCoord(), iEntityPlayerSP2.getPosY() + (double)2, wVec32.getZCoord(), true));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(wVec3.getXCoord(), iEntityPlayerSP2.getPosY() + (double)2, wVec3.getZCoord(), true));
                iEntityPlayerSP2.setMotionY(0.0);
                break;
            }
            case "minesucht": {
                double d = iEntityPlayerSP2.getPosX();
                double d12 = iEntityPlayerSP2.getPosY();
                double d13 = iEntityPlayerSP2.getPosZ();
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown()) break;
                if (System.currentTimeMillis() - fly2.minesuchtTP > (long)99) {
                    WVec3 wVec3 = iEntityPlayerSP2.getPositionEyes(0.0f);
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    WVec3 wVec33 = iEntityPlayerSP11.getLook(0.0f);
                    WVec3 wVec34 = wVec3;
                    double d14 = wVec33.getXCoord() * (double)7;
                    double d15 = wVec33.getYCoord() * (double)7;
                    double d16 = wVec33.getZCoord() * (double)7;
                    boolean bl7 = false;
                    WVec3 wVec35 = new WVec3(wVec34.getXCoord() + d14, wVec34.getYCoord() + d15, wVec34.getZCoord() + d16);
                    if ((double)iEntityPlayerSP2.getFallDistance() > 0.8) {
                        iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d12 + (double)50, d13, false));
                        IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP12 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP12.fall(100.0f, 100.0f);
                        iEntityPlayerSP2.setFallDistance(0.0f);
                        iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d12 + (double)20, d13, true));
                    }
                    iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(wVec35.getXCoord(), iEntityPlayerSP2.getPosY() + (double)50, wVec35.getZCoord(), true));
                    iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d12, d13, false));
                    iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(wVec35.getXCoord(), d12, wVec35.getZCoord(), true));
                    iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d12, d13, false));
                    fly2.minesuchtTP = System.currentTimeMillis();
                    break;
                }
                iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ(), false));
                iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d12, d13, true));
                break;
            }
            case "jetpack": {
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break;
                IEntityPlayerSP iEntityPlayerSP13 = iEntityPlayerSP2;
                iEntityPlayerSP13.setMotionY(iEntityPlayerSP13.getMotionY() + 0.15);
                IEntityPlayerSP iEntityPlayerSP14 = iEntityPlayerSP2;
                iEntityPlayerSP14.setMotionX(iEntityPlayerSP14.getMotionX() * 1.1);
                IEntityPlayerSP iEntityPlayerSP15 = iEntityPlayerSP2;
                iEntityPlayerSP15.setMotionZ(iEntityPlayerSP15.getMotionZ() * 1.1);
                break;
            }
            case "mineplex": {
                if (iEntityPlayerSP2.getInventory().getCurrentItemInHand() == null) {
                    if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() && fly2.mineplexTimer.hasTimePassed(100L)) {
                        iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + 0.6, iEntityPlayerSP2.getPosZ());
                        fly2.mineplexTimer.reset();
                    }
                    IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP16 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP16.isSneaking() && fly2.mineplexTimer.hasTimePassed(100L)) {
                        iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() - 0.6, iEntityPlayerSP2.getPosZ());
                        fly2.mineplexTimer.reset();
                    }
                    double d = iEntityPlayerSP2.getPosX();
                    IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP17 == null) {
                        Intrinsics.throwNpe();
                    }
                    WBlockPos wBlockPos = new WBlockPos(d, iEntityPlayerSP17.getEntityBoundingBox().getMinY() - 1.0, iEntityPlayerSP2.getPosZ());
                    WVec3 wVec3 = new WVec3(wBlockPos);
                    double d17 = 0.4;
                    double d18 = 0.4;
                    double d19 = 0.4;
                    boolean bl8 = false;
                    wVec3 = new WVec3(wVec3.getXCoord() + d17, wVec3.getYCoord() + d18, wVec3.getZCoord() + d19);
                    WVec3 wVec36 = new WVec3(MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP).getDirectionVec());
                    boolean bl9 = false;
                    WVec3 wVec37 = wVec3;
                    double d20 = wVec36.getXCoord();
                    double d21 = wVec36.getYCoord();
                    double d22 = wVec36.getZCoord();
                    boolean bl10 = false;
                    WVec3 wVec38 = new WVec3(wVec37.getXCoord() + d20, wVec37.getYCoord() + d21, wVec37.getZCoord() + d22);
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    IItemStack iItemStack = iEntityPlayerSP2.getInventory().getCurrentItemInHand();
                    if (iItemStack == null) {
                        Intrinsics.throwNpe();
                    }
                    iPlayerControllerMP.onPlayerRightClick(iEntityPlayerSP2, iWorldClient, iItemStack, wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP), new WVec3(wVec38.getXCoord() * (double)0.4f, wVec38.getYCoord() * (double)0.4f, wVec38.getZCoord() * (double)0.4f));
                    MovementUtils.strafe(0.27f);
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f + ((Number)fly2.mineplexSpeedValue.get()).floatValue());
                    break;
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                fly2.setState(false);
                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lMineplex-\u00a7a\u00a7lFly\u00a78] \u00a7aSelect an empty slot to fly.");
                break;
            }
            case "aac3.3.12": {
                if (iEntityPlayerSP2.getPosY() < (double)-70) {
                    iEntityPlayerSP2.setMotionY(((Number)fly2.aacMotion.get()).floatValue());
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                if (!Keyboard.isKeyDown((int)29)) break;
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.2f);
                MinecraftInstance.mc.setRightClickDelayTimer(0);
                break;
            }
            case "aac3.3.12-glide": {
                if (!iEntityPlayerSP2.getOnGround()) {
                    n = fly2.aac3glideDelay;
                    fly2.aac3glideDelay = n + 1;
                }
                if (fly2.aac3glideDelay == 2) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                }
                if (fly2.aac3glideDelay == 12) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.1f);
                }
                if (fly2.aac3glideDelay < 12 || iEntityPlayerSP2.getOnGround()) break;
                fly2.aac3glideDelay = 0;
                iEntityPlayerSP2.setMotionY(0.015);
                break;
            }
            case "aac3.3.13": {
                if (iEntityPlayerSP2.isDead()) {
                    fly2.wasDead = true;
                }
                if (fly2.wasDead || iEntityPlayerSP2.getOnGround()) {
                    fly2.wasDead = false;
                    iEntityPlayerSP2.setMotionY(((Number)fly2.aacMotion2.get()).floatValue());
                    iEntityPlayerSP2.setOnGround(false);
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                if (!Keyboard.isKeyDown((int)29)) break;
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.2f);
                MinecraftInstance.mc.setRightClickDelayTimer(0);
                break;
            }
            case "watchcat": {
                MovementUtils.strafe(0.15f);
                IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP18 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP18.setSprinting(true);
                if (iEntityPlayerSP2.getPosY() < fly2.startY + (double)2) {
                    iEntityPlayerSP2.setMotionY(Math.random() * 0.48954512);
                    break;
                }
                if (!(fly2.startY > iEntityPlayerSP2.getPosY())) break;
                MovementUtils.strafe(0.0f);
                break;
            }
            case "spartan": {
                iEntityPlayerSP2.setMotionY(0.0);
                fly2.spartanTimer.update();
                if (!fly2.spartanTimer.hasTimePassed(12)) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + (double)8, iEntityPlayerSP2.getPosZ(), true));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() - (double)8, iEntityPlayerSP2.getPosZ(), true));
                fly2.spartanTimer.reset();
                break;
            }
            case "spartan2": {
                MovementUtils.strafe(0.264f);
                if (iEntityPlayerSP2.getTicksExisted() % 8 != 0) break;
                iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + (double)10, iEntityPlayerSP2.getPosZ(), true));
                break;
            }
            case "neruxvace": {
                if (!iEntityPlayerSP2.getOnGround()) {
                    n = fly2.aac3glideDelay;
                    fly2.aac3glideDelay = n + 1;
                }
                if (fly2.aac3glideDelay < ((Number)fly2.neruxVaceTicks.get()).intValue() || iEntityPlayerSP2.getOnGround()) break;
                fly2.aac3glideDelay = 0;
                iEntityPlayerSP2.setMotionY(0.015);
                break;
            }
            case "hypixel": {
                n = ((Number)fly2.hypixelBoostDelay.get()).intValue();
                if (((Boolean)fly2.hypixelBoost.get()).booleanValue() && !fly2.flyTimer.hasTimePassed(n)) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f + ((Number)fly2.hypixelBoostTimer.get()).floatValue() * ((float)fly2.flyTimer.hasTimeLeft(n) / (float)n));
                }
                fly2.hypixelTimer.update();
                if (!fly2.hypixelTimer.hasTimePassed(2)) break;
                iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + 1.0E-5, iEntityPlayerSP2.getPosZ());
                fly2.hypixelTimer.reset();
                break;
            }
            case "freehypixel": {
                if (fly2.freeHypixelTimer.hasTimePassed(10)) {
                    iEntityPlayerSP2.getCapabilities().setFlying(true);
                    break;
                }
                iEntityPlayerSP2.setRotationYaw(fly2.freeHypixelYaw);
                iEntityPlayerSP2.setRotationPitch(fly2.freeHypixelPitch);
                iEntityPlayerSP2.setMotionY(0.0);
                iEntityPlayerSP2.setMotionZ(iEntityPlayerSP2.getMotionY());
                iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionZ());
                if (fly2.startY != new BigDecimal(iEntityPlayerSP2.getPosY()).setScale(3, RoundingMode.HALF_DOWN).doubleValue()) break;
                fly2.freeHypixelTimer.update();
                break;
            }
            case "bugspartan": {
                iEntityPlayerSP2.getCapabilities().setFlying(false);
                iEntityPlayerSP2.setMotionY(0.0);
                iEntityPlayerSP2.setMotionX(0.0);
                iEntityPlayerSP2.setMotionZ(0.0);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP19 = iEntityPlayerSP2;
                    iEntityPlayerSP19.setMotionY(iEntityPlayerSP19.getMotionY() + (double)f);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP20 = iEntityPlayerSP2;
                    iEntityPlayerSP20.setMotionY(iEntityPlayerSP20.getMotionY() - (double)f);
                }
                MovementUtils.strafe(f);
            }
        }
    }

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        block5: {
            block4: {
                String string = (String)this.modeValue.get();
                if (StringsKt.equals((String)string, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)string, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)string, (String)"Rewinside", (boolean)true)) break block4;
                if (!StringsKt.equals((String)string, (String)"Mineplex", (boolean)true)) break block5;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block5;
            }
            jumpEvent.cancelEvent();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"boosthypixel", (boolean)true)) {
            switch (Fly$WhenMappings.$EnumSwitchMapping$0[motionEvent.getEventState().ordinal()]) {
                case 1: {
                    this.hypixelTimer.update();
                    if (this.hypixelTimer.hasTimePassed(2)) {
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            Intrinsics.throwNpe();
                        }
                        double d = iEntityPlayerSP2.getPosX();
                        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP3 == null) {
                            Intrinsics.throwNpe();
                        }
                        double d2 = iEntityPlayerSP3.getPosY() + 1.0E-5;
                        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP4 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.setPosition(d, d2, iEntityPlayerSP4.getPosZ());
                        this.hypixelTimer.reset();
                    }
                    if (this.failedStart) break;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.setMotionY(0.0);
                    break;
                }
                case 2: {
                    double d;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = iEntityPlayerSP.getPosX();
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d4 = d3 - iEntityPlayerSP5.getPrevPosX();
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d5 = iEntityPlayerSP6.getPosZ();
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d6 = d5 - iEntityPlayerSP7.getPrevPosZ();
                    double d7 = d4 * d4 + d6 * d6;
                    Fly fly = this;
                    boolean bl = false;
                    fly.lastDistance = d = Math.sqrt(d7);
                    break;
                }
            }
        }
    }

    @EventTarget
    public final void onBB(BlockBBEvent blockBBEvent) {
        block8: {
            block9: {
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    return;
                }
                String string = (String)this.modeValue.get();
                if (!MinecraftInstance.classProvider.isBlockAir(blockBBEvent.getBlock())) break block8;
                if (StringsKt.equals((String)string, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)string, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)string, (String)"Rewinside", (boolean)true)) break block9;
                if (!StringsKt.equals((String)string, (String)"Mineplex", (boolean)true)) break block8;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block8;
            }
            double d = blockBBEvent.getY();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (d < iEntityPlayerSP.getPosY()) {
                double d2 = blockBBEvent.getX();
                double d3 = blockBBEvent.getY();
                double d4 = blockBBEvent.getZ();
                double d5 = (double)blockBBEvent.getX() + 1.0;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                blockBBEvent.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(d2, d3, d4, d5, iEntityPlayerSP2.getPosY(), (double)blockBBEvent.getZ() + 1.0));
            }
        }
    }

    @Override
    public void onDisable() {
        String string;
        this.wasDead = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        this.noFlag = false;
        String string2 = string = (String)this.modeValue.get();
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (!(StringsKt.startsWith$default((String)string3.toUpperCase(), (String)"AAC", (boolean)false, (int)2, null) || StringsKt.equals((String)string, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)string, (String)"CubeCraft", (boolean)true))) {
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionY(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
        }
        iEntityPlayerSP2.getCapabilities().setFlying(false);
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        iEntityPlayerSP2.setSpeedInAir(0.02f);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        block24: {
            double d;
            double d2;
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string = string2.toLowerCase();
            switch (string.hashCode()) {
                case -385327063: {
                    if (!string.equals("freehypixel")) return;
                    break block24;
                }
                case 1814517522: {
                    if (!string.equals("boosthypixel")) return;
                    break;
                }
                case -1031473397: {
                    if (!string.equals("cubecraft")) return;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = Math.toRadians(iEntityPlayerSP.getRotationYaw());
                    if (this.cubecraftTeleportTickTimer.hasTimePassed(2)) {
                        MoveEvent moveEvent2 = moveEvent;
                        boolean bl2 = false;
                        double d4 = Math.sin(d3);
                        moveEvent2.setX(-d4 * 2.4);
                        moveEvent2 = moveEvent;
                        bl2 = false;
                        d4 = Math.cos(d3);
                        moveEvent2.setZ(d4 * 2.4);
                        this.cubecraftTeleportTickTimer.reset();
                        return;
                    }
                    MoveEvent moveEvent3 = moveEvent;
                    boolean bl3 = false;
                    double d5 = Math.sin(d3);
                    moveEvent3.setX(-d5 * 0.2);
                    moveEvent3 = moveEvent;
                    bl3 = false;
                    d5 = Math.cos(d3);
                    moveEvent3.setZ(d5 * 0.2);
                    return;
                }
            }
            if (!MovementUtils.isMoving()) {
                moveEvent.setX(0.0);
                moveEvent.setZ(0.0);
                return;
            }
            if (this.failedStart) {
                return;
            }
            double d6 = 1.0;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                IPotionEffect iPotionEffect = iEntityPlayerSP2.getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED));
                if (iPotionEffect == null) {
                    Intrinsics.throwNpe();
                }
                d2 = 0.2 * ((double)iPotionEffect.getAmplifier() + 1.0);
            } else {
                d2 = 0.0;
            }
            double d7 = d6 + d2;
            double d8 = 0.29 * d7;
            switch (this.boostHypixelState) {
                case 1: {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.moveSpeed = (iEntityPlayerSP3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) ? 1.56 : 2.034) * d8;
                    this.boostHypixelState = 2;
                    break;
                }
                case 2: {
                    this.moveSpeed *= 2.16;
                    this.boostHypixelState = 3;
                    break;
                }
                case 3: {
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.moveSpeed = this.lastDistance - (iEntityPlayerSP4.getTicksExisted() % 2 == 0 ? 0.0103 : 0.0123) * (this.lastDistance - d8);
                    this.boostHypixelState = 4;
                    break;
                }
                default: {
                    this.moveSpeed = this.lastDistance - this.lastDistance / 159.8;
                }
            }
            double d9 = this.moveSpeed;
            double d10 = 0.3;
            Object object = this;
            boolean bl4 = false;
            ((Fly)object).moveSpeed = d = Math.max(d9, d10);
            d9 = MovementUtils.getDirection();
            object = moveEvent;
            boolean bl5 = false;
            d = Math.sin(d9);
            ((MoveEvent)object).setX(-d * this.moveSpeed);
            object = moveEvent;
            bl5 = false;
            d = Math.cos(d9);
            ((MoveEvent)object).setZ(d * this.moveSpeed);
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP5.setMotionX(moveEvent.getX());
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP6.setMotionZ(moveEvent.getZ());
            return;
        }
        if (this.freeHypixelTimer.hasTimePassed(10)) return;
        moveEvent.zero();
        return;
    }

    @EventTarget
    public final void onStep(StepEvent stepEvent) {
        block5: {
            block4: {
                String string = (String)this.modeValue.get();
                if (StringsKt.equals((String)string, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)string, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)string, (String)"Rewinside", (boolean)true)) break block4;
                if (!StringsKt.equals((String)string, (String)"Mineplex", (boolean)true)) break block5;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block5;
            }
            stepEvent.setStepHeight(0.0f);
        }
    }

    private final void handleVanillaKickBypass() {
        if (!((Boolean)this.vanillaKickBypassValue.get()).booleanValue() || !this.groundTimer.hasTimePassed(1000L)) {
            return;
        }
        double d = this.calculateGround();
        Fly fly = this;
        boolean bl = false;
        boolean bl2 = false;
        Fly fly2 = fly;
        boolean bl3 = false;
        if (MinecraftInstance.mc.getThePlayer() == null) {
            Intrinsics.throwNpe();
        }
        for (double d2 = (v188352).getPosY(); d2 > d; d2 -= 8.0) {
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d3 = iEntityPlayerSP.getPosX();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d3, d2, iEntityPlayerSP2.getPosZ(), true));
            if (d2 - 8.0 < d) break;
        }
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d4 = iEntityPlayerSP.getPosX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d4, d, iEntityPlayerSP3.getPosZ(), true));
        double d5 = d;
        while (true) {
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if (!(d5 < iEntityPlayerSP4.getPosY())) break;
            IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d6 = iEntityPlayerSP5.getPosX();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d6, d5, iEntityPlayerSP6.getPosZ(), true));
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            if (d5 + 8.0 > iEntityPlayerSP7.getPosY()) break;
            d5 += 8.0;
        }
        IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP8 == null) {
            Intrinsics.throwNpe();
        }
        double d7 = iEntityPlayerSP8.getPosX();
        IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP9 == null) {
            Intrinsics.throwNpe();
        }
        double d8 = iEntityPlayerSP9.getPosY();
        IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP10 == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient3.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d7, d8, iEntityPlayerSP10.getPosZ(), true));
        this.groundTimer.reset();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent render3DEvent) {
        String string = (String)this.modeValue.get();
        if (!((Boolean)this.markValue.get()).booleanValue() || StringsKt.equals((String)string, (String)"Vanilla", (boolean)true) || StringsKt.equals((String)string, (String)"SmoothVanilla", (boolean)true)) {
            return;
        }
        double d = this.startY + 2.0;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawPlatform(d, iEntityPlayerSP.getEntityBoundingBox().getMaxY() < d ? new Color(0, 255, 0, 90) : new Color(255, 0, 0, 90), 1.0);
        String string2 = string;
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string2 = string3.toLowerCase();
        switch (string2.hashCode()) {
            case 1492139162: {
                if (!string2.equals("aac3.3.12")) return;
                break;
            }
            case 1435059604: {
                if (!string2.equals("aac1.9.10")) return;
                RenderUtils.drawPlatform(this.startY + this.aacJump, new Color(0, 0, 255, 90), 1.0);
                return;
            }
        }
        RenderUtils.drawPlatform(-70.0, new Color(0, 0, 255, 90), 1.0);
        return;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        Object object;
        block7: {
            String string;
            block9: {
                block8: {
                    if (this.noPacketModify) {
                        return;
                    }
                    if (!MinecraftInstance.classProvider.isCPacketPlayer(packetEvent.getPacket())) break block7;
                    object = packetEvent.getPacket().asCPacketPlayer();
                    string = (String)this.modeValue.get();
                    if (StringsKt.equals((String)string, (String)"NCP", (boolean)true) || StringsKt.equals((String)string, (String)"Rewinside", (boolean)true)) break block8;
                    if (!StringsKt.equals((String)string, (String)"Mineplex", (boolean)true)) break block9;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block9;
                }
                object.setOnGround(true);
            }
            if (StringsKt.equals((String)string, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)string, (String)"BoostHypixel", (boolean)true)) {
                object.setOnGround(false);
            }
        }
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(packetEvent.getPacket()) && StringsKt.equals((String)(object = (String)this.modeValue.get()), (String)"BoostHypixel", (boolean)true)) {
            this.failedStart = true;
            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lBoostHypixel-\u00a7a\u00a7lFly\u00a78] \u00a7cSetback detected.");
        }
    }

    private final double calculateGround() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IAxisAlignedBB iAxisAlignedBB = iEntityPlayerSP.getEntityBoundingBox();
        double d = 1.0;
        if (MinecraftInstance.mc.getThePlayer() == null) {
            Intrinsics.throwNpe();
        }
        for (double d2 = (v188813).getPosY(); d2 > 0.0; d2 -= d) {
            IAxisAlignedBB iAxisAlignedBB2 = MinecraftInstance.classProvider.createAxisAlignedBB(iAxisAlignedBB.getMaxX(), d2 + d, iAxisAlignedBB.getMaxZ(), iAxisAlignedBB.getMinX(), d2, iAxisAlignedBB.getMinZ());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.checkBlockCollision(iAxisAlignedBB2)) continue;
            if (d <= 0.05) {
                return d2 + d;
            }
            d2 += d;
            d = 0.05;
        }
        return 0.0;
    }

    /*
     * Exception decompiling
     */
    @Override
    public void onEnable() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$FailedRewriteException: Block member is not a case, it's a class org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredComment
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteSwitch(SwitchStringRewriter.java:236)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }
}

