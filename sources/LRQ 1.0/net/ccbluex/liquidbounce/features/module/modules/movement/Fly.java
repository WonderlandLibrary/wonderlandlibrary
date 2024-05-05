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
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
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
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "SmoothVanilla", "NCP", "OldNCP", "AAC1.9.10", "AAC3.0.5", "AAC3.1.6-Gomme", "AAC3.3.12", "AAC3.3.12-Glide", "AAC3.3.13", "CubeCraft", "Hypixel", "BoostHypixel", "FreeHypixel", "Rewinside", "TeleportRewinside", "Mineplex", "NeruxVace", "Minesucht", "Spartan", "Spartan2", "BugSpartan", "MineSecure", "HawkEye", "HAC", "WatchCat", "Jetpack", "KeepAlive", "Flag"}, "Vanilla");
    private final FloatValue vanillaSpeedValue = new FloatValue("VanillaSpeed", 2.0f, 0.0f, 5.0f);
    private final BoolValue vanillaKickBypassValue = new BoolValue("VanillaKickBypass", false);
    private final FloatValue ncpMotionValue = new FloatValue("NCPMotion", 0.0f, 0.0f, 1.0f);
    private final FloatValue aacSpeedValue = new FloatValue("AAC1.9.10-Speed", 0.3f, 0.0f, 1.0f);
    private final BoolValue aacFast = new BoolValue("AAC3.0.5-Fast", true);
    private final FloatValue aacMotion = new FloatValue("AAC3.3.12-Motion", 10.0f, 0.1f, 10.0f);
    private final FloatValue aacMotion2 = new FloatValue("AAC3.3.13-Motion", 10.0f, 0.1f, 10.0f);
    private final BoolValue hypixelBoost = new BoolValue("Hypixel-Boost", true);
    private final IntegerValue hypixelBoostDelay = new IntegerValue("Hypixel-BoostDelay", 1200, 0, 2000);
    private final FloatValue hypixelBoostTimer = new FloatValue("Hypixel-BoostTimer", 1.0f, 0.0f, 5.0f);
    private final FloatValue mineplexSpeedValue = new FloatValue("MineplexSpeed", 1.0f, 0.5f, 10.0f);
    private final IntegerValue neruxVaceTicks = new IntegerValue("NeruxVace-Ticks", 6, 0, 20);
    private final BoolValue markValue = new BoolValue("Mark", true);
    private double startY;
    private final MSTimer flyTimer = new MSTimer();
    private final MSTimer groundTimer = new MSTimer();
    private boolean noPacketModify;
    private double aacJump;
    private int aac3delay;
    private int aac3glideDelay;
    private boolean noFlag;
    private final MSTimer mineSecureVClipTimer = new MSTimer();
    private final TickTimer spartanTimer = new TickTimer();
    private long minesuchtTP;
    private final MSTimer mineplexTimer = new MSTimer();
    private boolean wasDead;
    private final TickTimer hypixelTimer = new TickTimer();
    private int boostHypixelState = 1;
    private double moveSpeed;
    private double lastDistance;
    private boolean failedStart;
    private final TickTimer cubecraft2TickTimer = new TickTimer();
    private final TickTimer cubecraftTeleportTickTimer = new TickTimer();
    private final TickTimer freeHypixelTimer = new TickTimer();
    private float freeHypixelYaw;
    private float freeHypixelPitch;

    public final ListValue getModeValue() {
        return this.modeValue;
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

    @Override
    public void onDisable() {
        String mode;
        this.wasDead = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        this.noFlag = false;
        String string = mode = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (!(StringsKt.startsWith$default((String)string2.toUpperCase(), (String)"AAC", (boolean)false, (int)2, null) || StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"CubeCraft", (boolean)true))) {
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionY(0.0);
            thePlayer.setMotionZ(0.0);
        }
        thePlayer.getCapabilities().setFlying(false);
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        thePlayer.setSpeedInAir(0.02f);
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        float vanillaSpeed = ((Number)this.vanillaSpeedValue.get()).floatValue();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        Fly fly = this;
        boolean bl = false;
        boolean bl2 = false;
        Fly $this$run = fly;
        boolean bl3 = false;
        String string = (String)$this$run.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "vanilla": {
                thePlayer.getCapabilities().setFlying(false);
                thePlayer.setMotionY(0.0);
                thePlayer.setMotionX(0.0);
                thePlayer.setMotionZ(0.0);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() - (double)vanillaSpeed);
                }
                MovementUtils.strafe(vanillaSpeed);
                $this$run.handleVanillaKickBypass();
                break;
            }
            case "smoothvanilla": {
                thePlayer.getCapabilities().setFlying(true);
                $this$run.handleVanillaKickBypass();
                break;
            }
            case "cubecraft": {
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.6f);
                $this$run.cubecraftTeleportTickTimer.update();
                break;
            }
            case "ncp": {
                thePlayer.setMotionY(-((Number)$this$run.ncpMotionValue.get()).floatValue());
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    thePlayer.setMotionY(-0.5);
                }
                MovementUtils.strafe$default(0.0f, 1, null);
                break;
            }
            case "oldncp": {
                if ($this$run.startY > thePlayer.getPosY()) {
                    thePlayer.setMotionY(-1.0E-33);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    thePlayer.setMotionY(-0.2);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() && thePlayer.getPosY() < $this$run.startY - 0.1) {
                    thePlayer.setMotionY(0.2);
                }
                MovementUtils.strafe$default(0.0f, 1, null);
                break;
            }
            case "aac1.9.10": {
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    $this$run.aacJump += 0.2;
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    $this$run.aacJump -= 0.2;
                }
                if ($this$run.startY + $this$run.aacJump > thePlayer.getPosY()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                    thePlayer.setMotionY(0.8);
                    MovementUtils.strafe(((Number)$this$run.aacSpeedValue.get()).floatValue());
                }
                MovementUtils.strafe$default(0.0f, 1, null);
                break;
            }
            case "aac3.0.5": {
                if ($this$run.aac3delay == 2) {
                    thePlayer.setMotionY(0.1);
                } else if ($this$run.aac3delay > 2) {
                    $this$run.aac3delay = 0;
                }
                if (((Boolean)$this$run.aacFast.get()).booleanValue()) {
                    if (thePlayer.getMovementInput().getMoveStrafe() == 0.0f) {
                        thePlayer.setJumpMovementFactor(0.08f);
                    } else {
                        thePlayer.setJumpMovementFactor(0.0f);
                    }
                }
                n = $this$run.aac3delay;
                $this$run.aac3delay = n + 1;
                break;
            }
            case "aac3.1.6-gomme": {
                thePlayer.getCapabilities().setFlying(true);
                if ($this$run.aac3delay == 2) {
                    IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                    iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() + 0.05);
                } else if ($this$run.aac3delay > 2) {
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() - 0.05);
                    $this$run.aac3delay = 0;
                }
                n = $this$run.aac3delay;
                $this$run.aac3delay = n + 1;
                if (!$this$run.noFlag) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), thePlayer.getOnGround()));
                }
                if (!(thePlayer.getPosY() <= 0.0)) break;
                $this$run.noFlag = true;
                break;
            }
            case "flag": {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(thePlayer.getPosX() + thePlayer.getMotionX() * (double)999, thePlayer.getPosY() + (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() ? 1.5624 : 1.0E-8) - (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown() ? 0.0624 : 2.0E-8), thePlayer.getPosZ() + thePlayer.getMotionZ() * (double)999, thePlayer.getRotationYaw(), thePlayer.getRotationPitch(), true));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(thePlayer.getPosX() + thePlayer.getMotionX() * (double)999, thePlayer.getPosY() - (double)6969, thePlayer.getPosZ() + thePlayer.getMotionZ() * (double)999, thePlayer.getRotationYaw(), thePlayer.getRotationPitch(), true));
                thePlayer.setPosition(thePlayer.getPosX() + thePlayer.getMotionX() * (double)11, thePlayer.getPosY(), thePlayer.getPosZ() + thePlayer.getMotionZ() * (double)11);
                thePlayer.setMotionY(0.0);
                break;
            }
            case "keepalive": {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketKeepAlive());
                thePlayer.getCapabilities().setFlying(false);
                thePlayer.setMotionY(0.0);
                thePlayer.setMotionX(0.0);
                thePlayer.setMotionZ(0.0);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                    iEntityPlayerSP6.setMotionY(iEntityPlayerSP6.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                    iEntityPlayerSP7.setMotionY(iEntityPlayerSP7.getMotionY() - (double)vanillaSpeed);
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
            case "minesecure": {
                thePlayer.getCapabilities().setFlying(false);
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    thePlayer.setMotionY(-0.01);
                }
                thePlayer.setMotionX(0.0);
                thePlayer.setMotionZ(0.0);
                MovementUtils.strafe(vanillaSpeed);
                if (!$this$run.mineSecureVClipTimer.hasTimePassed(150L) || !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + (double)5, thePlayer.getPosZ(), false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(0.5, -1000.0, 0.5, false));
                double yaw = Math.toRadians(thePlayer.getRotationYaw());
                boolean bl4 = false;
                double x = -Math.sin(yaw) * 0.4;
                boolean bl5 = false;
                double z = Math.cos(yaw) * 0.4;
                thePlayer.setPosition(thePlayer.getPosX() + x, thePlayer.getPosY(), thePlayer.getPosZ() + z);
                $this$run.mineSecureVClipTimer.reset();
                break;
            }
            case "hac": {
                IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() * 0.8);
                IEntityPlayerSP iEntityPlayerSP9 = thePlayer;
                iEntityPlayerSP9.setMotionZ(iEntityPlayerSP9.getMotionZ() * 0.8);
                thePlayer.setMotionY(thePlayer.getMotionY() <= -0.42 ? 0.42 : -0.42);
                break;
            }
            case "hawkeye": {
                thePlayer.setMotionY(thePlayer.getMotionY() <= -0.42 ? 0.42 : -0.42);
                break;
            }
            case "teleportrewinside": {
                WVec3 vectorStart = new WVec3(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ());
                float yaw = -thePlayer.getRotationYaw();
                float pitch = -thePlayer.getRotationPitch();
                double length = 9.9;
                double d = Math.toRadians(yaw);
                boolean bl6 = false;
                double d2 = Math.sin(d);
                d = Math.toRadians(pitch);
                bl6 = false;
                double d3 = Math.cos(d);
                d = Math.toRadians(pitch);
                d2 = d2 * d3 * length + vectorStart.getXCoord();
                bl6 = false;
                d3 = Math.sin(d);
                d = Math.toRadians(yaw);
                d3 = d3 * length + vectorStart.getYCoord();
                bl6 = false;
                double d4 = Math.cos(d);
                d = Math.toRadians(pitch);
                bl6 = false;
                double d5 = Math.cos(d);
                double d6 = d4 * d5 * length + vectorStart.getZCoord();
                double d7 = d3;
                double d8 = d2;
                WVec3 vectorEnd = new WVec3(d8, d7, d6);
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(vectorEnd.getXCoord(), thePlayer.getPosY() + (double)2, vectorEnd.getZCoord(), true));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(vectorStart.getXCoord(), thePlayer.getPosY() + (double)2, vectorStart.getZCoord(), true));
                thePlayer.setMotionY(0.0);
                break;
            }
            case "minesucht": {
                double posX = thePlayer.getPosX();
                double posY = thePlayer.getPosY();
                double posZ = thePlayer.getPosZ();
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown()) break;
                if (System.currentTimeMillis() - $this$run.minesuchtTP > (long)99) {
                    void y$iv;
                    void x$iv;
                    void this_$iv;
                    WVec3 vec3 = thePlayer.getPositionEyes(0.0f);
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    WVec3 vec31 = iEntityPlayerSP10.getLook(0.0f);
                    WVec3 wVec3 = vec3;
                    double d = vec31.getXCoord() * (double)7;
                    double d9 = vec31.getYCoord() * (double)7;
                    double z$iv = vec31.getZCoord() * (double)7;
                    boolean $i$f$addVector = false;
                    WVec3 vec32 = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
                    if ((double)thePlayer.getFallDistance() > 0.8) {
                        thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(posX, posY + (double)50, posZ, false));
                        IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP11 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP11.fall(100.0f, 100.0f);
                        thePlayer.setFallDistance(0.0f);
                        thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(posX, posY + (double)20, posZ, true));
                    }
                    thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(vec32.getXCoord(), thePlayer.getPosY() + (double)50, vec32.getZCoord(), true));
                    thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(posX, posY, posZ, false));
                    thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(vec32.getXCoord(), posY, vec32.getZCoord(), true));
                    thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(posX, posY, posZ, false));
                    $this$run.minesuchtTP = System.currentTimeMillis();
                    break;
                }
                thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(posX, posY, posZ, true));
                break;
            }
            case "jetpack": {
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break;
                IEntityPlayerSP iEntityPlayerSP12 = thePlayer;
                iEntityPlayerSP12.setMotionY(iEntityPlayerSP12.getMotionY() + 0.15);
                IEntityPlayerSP iEntityPlayerSP13 = thePlayer;
                iEntityPlayerSP13.setMotionX(iEntityPlayerSP13.getMotionX() * 1.1);
                IEntityPlayerSP iEntityPlayerSP14 = thePlayer;
                iEntityPlayerSP14.setMotionZ(iEntityPlayerSP14.getMotionZ() * 1.1);
                break;
            }
            case "mineplex": {
                if (thePlayer.getInventory().getCurrentItemInHand() == null) {
                    void y$iv$iv;
                    void x$iv$iv;
                    void this_$iv$iv;
                    void y$iv;
                    void x$iv;
                    WVec3 this_$iv;
                    if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() && $this$run.mineplexTimer.hasTimePassed(100L)) {
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.6, thePlayer.getPosZ());
                        $this$run.mineplexTimer.reset();
                    }
                    IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP15 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP15.isSneaking() && $this$run.mineplexTimer.hasTimePassed(100L)) {
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() - 0.6, thePlayer.getPosZ());
                        $this$run.mineplexTimer.reset();
                    }
                    double d = thePlayer.getPosX();
                    IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP16 == null) {
                        Intrinsics.throwNpe();
                    }
                    WBlockPos blockPos = new WBlockPos(d, iEntityPlayerSP16.getEntityBoundingBox().getMinY() - 1.0, thePlayer.getPosZ());
                    WVec3 pitch = new WVec3(blockPos);
                    double length = 0.4;
                    double d10 = 0.4;
                    double z$iv = 0.4;
                    boolean $i$f$addVector = false;
                    this_$iv = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
                    WVec3 vec$iv = new WVec3(MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP).getDirectionVec());
                    boolean $i$f$add = false;
                    WVec3 vectorEnd = this_$iv;
                    double d11 = vec$iv.getXCoord();
                    double d12 = vec$iv.getYCoord();
                    double z$iv$iv = vec$iv.getZCoord();
                    boolean $i$f$addVector2 = false;
                    WVec3 vec = new WVec3(this_$iv$iv.getXCoord() + x$iv$iv, this_$iv$iv.getYCoord() + y$iv$iv, this_$iv$iv.getZCoord() + z$iv$iv);
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    IItemStack iItemStack = thePlayer.getInventory().getCurrentItemInHand();
                    if (iItemStack == null) {
                        Intrinsics.throwNpe();
                    }
                    iPlayerControllerMP.onPlayerRightClick(thePlayer, iWorldClient, iItemStack, blockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP), new WVec3(vec.getXCoord() * (double)0.4f, vec.getYCoord() * (double)0.4f, vec.getZCoord() * (double)0.4f));
                    MovementUtils.strafe(0.27f);
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f + ((Number)$this$run.mineplexSpeedValue.get()).floatValue());
                    break;
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                $this$run.setState(false);
                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lMineplex-\u00a7a\u00a7lFly\u00a78] \u00a7aSelect an empty slot to fly.");
                break;
            }
            case "aac3.3.12": {
                if (thePlayer.getPosY() < (double)-70) {
                    thePlayer.setMotionY(((Number)$this$run.aacMotion.get()).floatValue());
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                if (!Keyboard.isKeyDown((int)29)) break;
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.2f);
                MinecraftInstance.mc.setRightClickDelayTimer(0);
                break;
            }
            case "aac3.3.12-glide": {
                int blockPos;
                if (!thePlayer.getOnGround()) {
                    blockPos = $this$run.aac3glideDelay;
                    $this$run.aac3glideDelay = blockPos + 1;
                }
                if ($this$run.aac3glideDelay == 2) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                }
                if ($this$run.aac3glideDelay == 12) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.1f);
                }
                if ($this$run.aac3glideDelay < 12 || thePlayer.getOnGround()) break;
                $this$run.aac3glideDelay = 0;
                thePlayer.setMotionY(0.015);
                break;
            }
            case "aac3.3.13": {
                if (thePlayer.isDead()) {
                    $this$run.wasDead = true;
                }
                if ($this$run.wasDead || thePlayer.getOnGround()) {
                    $this$run.wasDead = false;
                    thePlayer.setMotionY(((Number)$this$run.aacMotion2.get()).floatValue());
                    thePlayer.setOnGround(false);
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                if (!Keyboard.isKeyDown((int)29)) break;
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.2f);
                MinecraftInstance.mc.setRightClickDelayTimer(0);
                break;
            }
            case "watchcat": {
                MovementUtils.strafe(0.15f);
                IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP17 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP17.setSprinting(true);
                if (thePlayer.getPosY() < $this$run.startY + (double)2) {
                    thePlayer.setMotionY(Math.random() * 0.5);
                    break;
                }
                if (!($this$run.startY > thePlayer.getPosY())) break;
                MovementUtils.strafe(0.0f);
                break;
            }
            case "spartan": {
                thePlayer.setMotionY(0.0);
                $this$run.spartanTimer.update();
                if (!$this$run.spartanTimer.hasTimePassed(12)) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + (double)8, thePlayer.getPosZ(), true));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() - (double)8, thePlayer.getPosZ(), true));
                $this$run.spartanTimer.reset();
                break;
            }
            case "spartan2": {
                MovementUtils.strafe(0.264f);
                if (thePlayer.getTicksExisted() % 8 != 0) break;
                thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + (double)10, thePlayer.getPosZ(), true));
                break;
            }
            case "neruxvace": {
                int blockPos;
                if (!thePlayer.getOnGround()) {
                    blockPos = $this$run.aac3glideDelay;
                    $this$run.aac3glideDelay = blockPos + 1;
                }
                if ($this$run.aac3glideDelay < ((Number)$this$run.neruxVaceTicks.get()).intValue() || thePlayer.getOnGround()) break;
                $this$run.aac3glideDelay = 0;
                thePlayer.setMotionY(0.015);
                break;
            }
            case "hypixel": {
                int boostDelay = ((Number)$this$run.hypixelBoostDelay.get()).intValue();
                if (((Boolean)$this$run.hypixelBoost.get()).booleanValue() && !$this$run.flyTimer.hasTimePassed(boostDelay)) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f + ((Number)$this$run.hypixelBoostTimer.get()).floatValue() * ((float)$this$run.flyTimer.hasTimeLeft(boostDelay) / (float)boostDelay));
                }
                $this$run.hypixelTimer.update();
                if (!$this$run.hypixelTimer.hasTimePassed(2)) break;
                thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 1.0E-5, thePlayer.getPosZ());
                $this$run.hypixelTimer.reset();
                break;
            }
            case "freehypixel": {
                if ($this$run.freeHypixelTimer.hasTimePassed(10)) {
                    thePlayer.getCapabilities().setFlying(true);
                    break;
                }
                thePlayer.setRotationYaw($this$run.freeHypixelYaw);
                thePlayer.setRotationPitch($this$run.freeHypixelPitch);
                thePlayer.setMotionY(0.0);
                thePlayer.setMotionZ(thePlayer.getMotionY());
                thePlayer.setMotionX(thePlayer.getMotionZ());
                if ($this$run.startY != new BigDecimal(thePlayer.getPosY()).setScale(3, RoundingMode.HALF_DOWN).doubleValue()) break;
                $this$run.freeHypixelTimer.update();
                break;
            }
            case "bugspartan": {
                thePlayer.getCapabilities().setFlying(false);
                thePlayer.setMotionY(0.0);
                thePlayer.setMotionX(0.0);
                thePlayer.setMotionZ(0.0);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP18 = thePlayer;
                    iEntityPlayerSP18.setMotionY(iEntityPlayerSP18.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP19 = thePlayer;
                    iEntityPlayerSP19.setMotionY(iEntityPlayerSP19.getMotionY() - (double)vanillaSpeed);
                }
                MovementUtils.strafe(vanillaSpeed);
            }
        }
    }

    @EventTarget
    public final void onMotion(MotionEvent event) {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"boosthypixel", (boolean)true)) {
            switch (Fly$WhenMappings.$EnumSwitchMapping$0[event.getEventState().ordinal()]) {
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
                    double xDist = d3 - iEntityPlayerSP5.getPrevPosX();
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d4 = iEntityPlayerSP6.getPosZ();
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    double zDist = d4 - iEntityPlayerSP7.getPrevPosZ();
                    double d5 = xDist * xDist + zDist * zDist;
                    Fly fly = this;
                    boolean bl = false;
                    fly.lastDistance = d = Math.sqrt(d5);
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        String mode = (String)this.modeValue.get();
        if (!((Boolean)this.markValue.get()).booleanValue() || StringsKt.equals((String)mode, (String)"Vanilla", (boolean)true) || StringsKt.equals((String)mode, (String)"SmoothVanilla", (boolean)true)) {
            return;
        }
        double y = this.startY + 2.0;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawPlatform(y, iEntityPlayerSP.getEntityBoundingBox().getMaxY() < y ? new Color(0, 255, 0, 90) : new Color(255, 0, 0, 90), 1.0);
        String string = mode;
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case 1492139162: {
                if (!string.equals("aac3.3.12")) return;
                break;
            }
            case 1435059604: {
                if (!string.equals("aac1.9.10")) return;
                RenderUtils.drawPlatform(this.startY + this.aacJump, new Color(0, 0, 255, 90), 1.0);
                return;
            }
        }
        RenderUtils.drawPlatform(-70.0, new Color(0, 0, 255, 90), 1.0);
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        String mode;
        block7: {
            String mode2;
            ICPacketPlayer packetPlayer;
            block9: {
                block8: {
                    if (this.noPacketModify) {
                        return;
                    }
                    if (!MinecraftInstance.classProvider.isCPacketPlayer(event.getPacket())) break block7;
                    packetPlayer = event.getPacket().asCPacketPlayer();
                    mode2 = (String)this.modeValue.get();
                    if (StringsKt.equals((String)mode2, (String)"NCP", (boolean)true) || StringsKt.equals((String)mode2, (String)"Rewinside", (boolean)true)) break block8;
                    if (!StringsKt.equals((String)mode2, (String)"Mineplex", (boolean)true)) break block9;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block9;
                }
                packetPlayer.setOnGround(true);
            }
            if (StringsKt.equals((String)mode2, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode2, (String)"BoostHypixel", (boolean)true)) {
                packetPlayer.setOnGround(false);
            }
        }
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(event.getPacket()) && StringsKt.equals((String)(mode = (String)this.modeValue.get()), (String)"BoostHypixel", (boolean)true)) {
            this.failedStart = true;
            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lBoostHypixel-\u00a7a\u00a7lFly\u00a78] \u00a7cSetback detected.");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMove(MoveEvent event) {
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
                    double yaw = Math.toRadians(iEntityPlayerSP.getRotationYaw());
                    if (this.cubecraftTeleportTickTimer.hasTimePassed(2)) {
                        MoveEvent moveEvent = event;
                        boolean bl2 = false;
                        double d3 = Math.sin(yaw);
                        moveEvent.setX(-d3 * 2.4);
                        moveEvent = event;
                        bl2 = false;
                        d3 = Math.cos(yaw);
                        moveEvent.setZ(d3 * 2.4);
                        this.cubecraftTeleportTickTimer.reset();
                        return;
                    }
                    MoveEvent moveEvent = event;
                    boolean bl3 = false;
                    double d4 = Math.sin(yaw);
                    moveEvent.setX(-d4 * 0.2);
                    moveEvent = event;
                    bl3 = false;
                    d4 = Math.cos(yaw);
                    moveEvent.setZ(d4 * 0.2);
                    return;
                }
            }
            if (!MovementUtils.isMoving()) {
                event.setX(0.0);
                event.setZ(0.0);
                return;
            }
            if (this.failedStart) {
                return;
            }
            double d5 = 1.0;
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
            double amplifier = d5 + d2;
            double baseSpeed = 0.29 * amplifier;
            switch (this.boostHypixelState) {
                case 1: {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.moveSpeed = (iEntityPlayerSP3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) ? 1.56 : 2.034) * baseSpeed;
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
                    this.moveSpeed = this.lastDistance - (iEntityPlayerSP4.getTicksExisted() % 2 == 0 ? 0.0103 : 0.0123) * (this.lastDistance - baseSpeed);
                    this.boostHypixelState = 4;
                    break;
                }
                default: {
                    this.moveSpeed = this.lastDistance - this.lastDistance / 159.8;
                }
            }
            double d6 = this.moveSpeed;
            double d7 = 0.3;
            Object object = this;
            boolean bl4 = false;
            ((Fly)object).moveSpeed = d = Math.max(d6, d7);
            double yaw = MovementUtils.getDirection();
            object = event;
            boolean bl5 = false;
            d = Math.sin(yaw);
            ((MoveEvent)object).setX(-d * this.moveSpeed);
            object = event;
            bl5 = false;
            d = Math.cos(yaw);
            ((MoveEvent)object).setZ(d * this.moveSpeed);
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP5.setMotionX(event.getX());
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP6.setMotionZ(event.getZ());
            return;
        }
        if (this.freeHypixelTimer.hasTimePassed(10)) return;
        event.zero();
    }

    @EventTarget
    public final void onBB(BlockBBEvent event) {
        block8: {
            block9: {
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    return;
                }
                String mode = (String)this.modeValue.get();
                if (!MinecraftInstance.classProvider.isBlockAir(event.getBlock())) break block8;
                if (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true)) break block9;
                if (!StringsKt.equals((String)mode, (String)"Mineplex", (boolean)true)) break block8;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block8;
            }
            double d = event.getY();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (d < iEntityPlayerSP.getPosY()) {
                double d2 = event.getX();
                double d3 = event.getY();
                double d4 = event.getZ();
                double d5 = (double)event.getX() + 1.0;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(d2, d3, d4, d5, iEntityPlayerSP2.getPosY(), (double)event.getZ() + 1.0));
            }
        }
    }

    @EventTarget
    public final void onJump(JumpEvent e) {
        block5: {
            block4: {
                String mode = (String)this.modeValue.get();
                if (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true)) break block4;
                if (!StringsKt.equals((String)mode, (String)"Mineplex", (boolean)true)) break block5;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block5;
            }
            e.cancelEvent();
        }
    }

    @EventTarget
    public final void onStep(StepEvent e) {
        block5: {
            block4: {
                String mode = (String)this.modeValue.get();
                if (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true)) break block4;
                if (!StringsKt.equals((String)mode, (String)"Mineplex", (boolean)true)) break block5;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block5;
            }
            e.setStepHeight(0.0f);
        }
    }

    private final void handleVanillaKickBypass() {
        if (!((Boolean)this.vanillaKickBypassValue.get()).booleanValue() || !this.groundTimer.hasTimePassed(1000L)) {
            return;
        }
        double ground = this.calculateGround();
        Fly fly = this;
        boolean bl = false;
        boolean bl2 = false;
        Fly $this$run = fly;
        boolean bl3 = false;
        if (MinecraftInstance.mc.getThePlayer() == null) {
            Intrinsics.throwNpe();
        }
        for (double posY = (v141321).getPosY(); posY > ground; posY -= 8.0) {
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP.getPosX();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, posY, iEntityPlayerSP2.getPosZ(), true));
            if (posY - 8.0 < ground) break;
        }
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getPosX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, ground, iEntityPlayerSP3.getPosZ(), true));
        double posY = ground;
        while (true) {
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if (!(posY < iEntityPlayerSP4.getPosY())) break;
            IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d2 = iEntityPlayerSP5.getPosX();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d2, posY, iEntityPlayerSP6.getPosZ(), true));
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            if (posY + 8.0 > iEntityPlayerSP7.getPosY()) break;
            posY += 8.0;
        }
        IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP8 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = iEntityPlayerSP8.getPosX();
        IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP9 == null) {
            Intrinsics.throwNpe();
        }
        double d4 = iEntityPlayerSP9.getPosY();
        IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP10 == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient3.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d3, d4, iEntityPlayerSP10.getPosZ(), true));
        this.groundTimer.reset();
    }

    private final double calculateGround() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IAxisAlignedBB playerBoundingBox = iEntityPlayerSP.getEntityBoundingBox();
        double blockHeight = 1.0;
        if (MinecraftInstance.mc.getThePlayer() == null) {
            Intrinsics.throwNpe();
        }
        for (double ground = (v141452).getPosY(); ground > 0.0; ground -= blockHeight) {
            IAxisAlignedBB customBox = MinecraftInstance.classProvider.createAxisAlignedBB(playerBoundingBox.getMaxX(), ground + blockHeight, playerBoundingBox.getMaxZ(), playerBoundingBox.getMinX(), ground, playerBoundingBox.getMinZ());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.checkBlockCollision(customBox)) continue;
            if (blockHeight <= 0.05) {
                return ground + blockHeight;
            }
            ground += blockHeight;
            blockHeight = 0.05;
        }
        return 0.0;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

