package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.IClassProvider;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name="Fly", description="Allows you to fly in survival mode.", category=ModuleCategory.MOVEMENT, keyBind=33)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\b\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\t\n\t\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\t\b\u000020B¢J\b;0\tHJ\b<0=HJ>0=2?0@HJ\bA0=HJ\bB0=HJC0=2D0EHJF0=2?0GHJH0=2?0IHJJ0=2?0KHJL0=2\b?0MHJN0=2D0OHJP0=2\b?0QHJR0=2S0\tHJT0=2S0\tHJU0=2V0HJW0=2X0HJY0=2X0\tHR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0X¢\n\u0000R\f0X¢\n\u0000R\r0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R 0\tX¢\n\u0000R!0\tX¢\n\u0000R\"0X¢\n\u0000R#0X¢\n\u0000R$0X¢\n\u0000R%0X¢\n\u0000R&0'X¢\n\u0000R(0)¢\b\n\u0000\b*+R,0\tX¢\n\u0000R-0X¢\n\u0000R.0X¢\n\u0000R/0X¢\n\u0000R00X¢\n\u0000R10X¢\n\u0000R20X¢\n\u0000R30\tX¢\n\u0000R4058VX¢\b67R80X¢\n\u0000R90X¢\n\u0000R:0X¢\n\u0000¨Z"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Fly;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aac3delay", "", "aac3glideDelay", "aacFast", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "aacJump", "", "aacMotion", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "aacMotion2", "aacSpeedValue", "boostHypixelState", "cubecraft2TickTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "cubecraftTeleportTickTimer", "failedStart", "", "flyTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "freeHypixelPitch", "", "freeHypixelTimer", "freeHypixelYaw", "groundTimer", "hypixelBoost", "hypixelBoostDelay", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hypixelBoostTimer", "hypixelTimer", "hytStartY", "lastDistance", "markValue", "mineSecureVClipTimer", "mineplexSpeedValue", "mineplexTimer", "minesuchtTP", "", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "moveSpeed", "ncpMotionValue", "neruxVaceTicks", "noFlag", "noPacketModify", "redeskyHeight", "spartanTimer", "startY", "tag", "", "getTag", "()Ljava/lang/String;", "vanillaKickBypassValue", "vanillaSpeedValue", "wasDead", "calculateGround", "handleVanillaKickBypass", "", "onBB", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onDisable", "onEnable", "onJump", "e", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onStep", "Lnet/ccbluex/liquidbounce/event/StepEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "redeskyHClip1", "horizontal", "redeskyHClip2", "redeskySpeed", "speed", "redeskyVClip1", "vertical", "redeskyVClip2", "Pride"})
public final class Fly
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "SmoothVanilla", "NCP", "OldNCP", "AAC1.9.10", "AAC3.0.5", "AAC3.1.6-Gomme", "AAC3.3.12", "AAC3.3.12-Glide", "AAC3.3.13", "CubeCraft", "Hypixel", "BoostHypixel", "FreeHypixel", "Rewinside", "TeleportRewinside", "Mineplex", "NeruxVace", "Minesucht", "Redesky", "Spartan", "Spartan2", "BugSpartan", "HYT4v4Test", "MineSecure", "HawkEye", "HAC", "WatchCat", "Jetpack", "KeepAlive", "Flag"}, "Vanilla");
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
    private final FloatValue redeskyHeight = new FloatValue("Redesky-Height", 4.0f, 1.0f, 7.0f);
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
    private double hytStartY;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        this.flyTimer.reset();
        this.noPacketModify = true;
        double x = thePlayer.getPosX();
        double y = thePlayer.getPosY();
        double z = thePlayer.getPosZ();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        this.hytStartY = iEntityPlayerSP2.getPosY();
        String mode = (String)this.modeValue.get();
        Fly fly = this;
        boolean bl = false;
        boolean bl2 = false;
        Fly $this$run = fly;
        boolean bl3 = false;
        String string = mode;
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "ncp": {
                int i;
                if (!thePlayer.getOnGround()) break;
                n = 0;
                int n2 = 64;
                while (n <= n2) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.049, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, false));
                    ++i;
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.1, z, true));
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * 0.1);
                IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 0.1);
                thePlayer.swingItem();
                break;
            }
            case "oldncp": {
                int i;
                if (!thePlayer.getOnGround()) break;
                int n3 = 3;
                for (i = 0; i <= n3; ++i) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 1.01, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, false));
                }
                thePlayer.jump();
                thePlayer.swingItem();
                break;
            }
            case "bugspartan": {
                int i;
                int n4 = 64;
                for (i = 0; i <= n4; ++i) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.049, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, false));
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.1, z, true));
                IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() * 0.1);
                IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() * 0.1);
                thePlayer.swingItem();
                break;
            }
            case "infinitycubecraft": {
                ClientUtils.displayChatMessage("§8[§c§lCubeCraft-§a§lFly§8] §aPlace a block before landing.");
                break;
            }
            case "infinityvcubecraft": {
                ClientUtils.displayChatMessage("§8[§c§lCubeCraft-§a§lFly§8] §aPlace a block before landing.");
                thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + (double)2, thePlayer.getPosZ());
                break;
            }
            case "boosthypixel": {
                int i;
                if (!thePlayer.getOnGround()) break;
                int n5 = 9;
                for (i = 0; i <= n5; ++i) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), true));
                }
                for (double fallDistance = 3.0125; fallDistance > 0.0; fallDistance -= 0.0624986421) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.0624986421, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.0625, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.0624986421, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 1.3579E-6, thePlayer.getPosZ(), false));
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), true));
                thePlayer.jump();
                IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                iEntityPlayerSP7.setPosY(iEntityPlayerSP7.getPosY() + (double)0.42f);
                $this$run.boostHypixelState = 1;
                $this$run.moveSpeed = 0.1;
                $this$run.lastDistance = 0.0;
                $this$run.failedStart = false;
                break;
            }
            case "redesky": {
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP8.getOnGround()) break;
                $this$run.redeskyVClip1(((Number)$this$run.redeskyHeight.get()).floatValue());
            }
        }
        this.startY = thePlayer.getPosY();
        this.aacJump = -3.8;
        this.noPacketModify = false;
        if (StringsKt.equals(mode, "freehypixel", true)) {
            this.freeHypixelTimer.reset();
            thePlayer.setPositionAndUpdate(thePlayer.getPosX(), thePlayer.getPosY() + 0.42, thePlayer.getPosZ());
            this.freeHypixelYaw = thePlayer.getRotationYaw();
            this.freeHypixelPitch = thePlayer.getRotationPitch();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        String mode;
        this.wasDead = false;
        this.redeskySpeed(0);
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
        String string3 = string2.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toUpperCase()");
        if (!(StringsKt.startsWith$default(string3, "AAC", false, 2, null) || StringsKt.equals(mode, "Hypixel", true) || StringsKt.equals(mode, "CubeCraft", true))) {
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionY(0.0);
            thePlayer.setMotionZ(0.0);
        }
        if (StringsKt.equals(mode, "Redesky", true)) {
            this.redeskyHClip2(0.0);
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
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
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
            case "hyt4v4test": {
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP4.getPosY() <= $this$run.hytStartY)) break;
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP5.setOnGround(true);
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP6.jump();
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
                    IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                    iEntityPlayerSP7.setMotionY(iEntityPlayerSP7.getMotionY() + 0.05);
                } else if ($this$run.aac3delay > 2) {
                    IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                    iEntityPlayerSP8.setMotionY(iEntityPlayerSP8.getMotionY() - 0.05);
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
                    IEntityPlayerSP iEntityPlayerSP9 = thePlayer;
                    iEntityPlayerSP9.setMotionY(iEntityPlayerSP9.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP10 = thePlayer;
                    iEntityPlayerSP10.setMotionY(iEntityPlayerSP10.getMotionY() - (double)vanillaSpeed);
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
                IEntityPlayerSP iEntityPlayerSP11 = thePlayer;
                iEntityPlayerSP11.setMotionX(iEntityPlayerSP11.getMotionX() * 0.8);
                IEntityPlayerSP iEntityPlayerSP12 = thePlayer;
                iEntityPlayerSP12.setMotionZ(iEntityPlayerSP12.getMotionZ() * 0.8);
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
                    IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    WVec3 vec31 = iEntityPlayerSP13.getLook(0.0f);
                    WVec3 wVec3 = vec3;
                    double d = vec31.getXCoord() * (double)7;
                    double d9 = vec31.getYCoord() * (double)7;
                    double z$iv = vec31.getZCoord() * (double)7;
                    boolean $i$f$addVector = false;
                    WVec3 vec32 = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
                    if ((double)thePlayer.getFallDistance() > 0.8) {
                        thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(posX, posY + (double)50, posZ, false));
                        IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP14 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP14.fall(100.0f, 100.0f);
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
                IEntityPlayerSP iEntityPlayerSP15 = thePlayer;
                iEntityPlayerSP15.setMotionY(iEntityPlayerSP15.getMotionY() + 0.15);
                IEntityPlayerSP iEntityPlayerSP16 = thePlayer;
                iEntityPlayerSP16.setMotionX(iEntityPlayerSP16.getMotionX() * 1.1);
                IEntityPlayerSP iEntityPlayerSP17 = thePlayer;
                iEntityPlayerSP17.setMotionZ(iEntityPlayerSP17.getMotionZ() * 1.1);
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
                    IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP18 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP18.isSneaking() && $this$run.mineplexTimer.hasTimePassed(100L)) {
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() - 0.6, thePlayer.getPosZ());
                        $this$run.mineplexTimer.reset();
                    }
                    double d = thePlayer.getPosX();
                    IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP19 == null) {
                        Intrinsics.throwNpe();
                    }
                    WBlockPos blockPos = new WBlockPos(d, iEntityPlayerSP19.getEntityBoundingBox().getMinY() - 1.0, thePlayer.getPosZ());
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
                ClientUtils.displayChatMessage("§8[§c§lMineplex-§a§lFly§8] §aSelect an empty slot to fly.");
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
                IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP20 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP20.setSprinting(true);
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
                    IEntityPlayerSP iEntityPlayerSP21 = thePlayer;
                    iEntityPlayerSP21.setMotionY(iEntityPlayerSP21.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP22 = thePlayer;
                    iEntityPlayerSP22.setMotionY(iEntityPlayerSP22.getMotionY() - (double)vanillaSpeed);
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
            case "redesky": {
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.3f);
                $this$run.redeskyHClip2(7.0);
                $this$run.redeskyVClip2(10.0);
                $this$run.redeskyVClip1(-0.5f);
                $this$run.redeskyHClip1(2.0);
                $this$run.redeskySpeed(1);
                IEntityPlayerSP iEntityPlayerSP23 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP23 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP23.setMotionY(-0.01);
            }
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (StringsKt.equals((String)this.modeValue.get(), "boosthypixel", true)) {
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
                    break;
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
        if (!((Boolean)this.markValue.get()).booleanValue() || StringsKt.equals(mode, "Vanilla", true) || StringsKt.equals(mode, "SmoothVanilla", true)) {
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
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
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
        return;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        String mode;
        block7: {
            String mode2;
            ICPacketPlayer packetPlayer;
            block9: {
                block8: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    if (this.noPacketModify) {
                        return;
                    }
                    if (!MinecraftInstance.classProvider.isCPacketPlayer(event.getPacket())) break block7;
                    packetPlayer = event.getPacket().asCPacketPlayer();
                    mode2 = (String)this.modeValue.get();
                    if (StringsKt.equals(mode2, "NCP", true) || StringsKt.equals(mode2, "Rewinside", true)) break block8;
                    if (!StringsKt.equals(mode2, "Mineplex", true)) break block9;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) break block9;
                }
                packetPlayer.setOnGround(true);
            }
            if (StringsKt.equals(mode2, "Hypixel", true) || StringsKt.equals(mode2, "BoostHypixel", true)) {
                packetPlayer.setOnGround(false);
            }
        }
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(event.getPacket()) && StringsKt.equals(mode = (String)this.modeValue.get(), "BoostHypixel", true)) {
            this.failedStart = true;
            ClientUtils.displayChatMessage("§8[§c§lBoostHypixel-§a§lFly§8] §cSetback detected.");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        block24: {
            double d;
            double d2;
            Intrinsics.checkParameterIsNotNull(event, "event");
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            string = string3;
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
        return;
    }

    @EventTarget
    public final void onBB(@NotNull BlockBBEvent event) {
        block8: {
            block9: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    return;
                }
                String mode = (String)this.modeValue.get();
                if (!MinecraftInstance.classProvider.isBlockAir(event.getBlock())) break block8;
                if (StringsKt.equals(mode, "Hypixel", true) || StringsKt.equals(mode, "BoostHypixel", true) || StringsKt.equals(mode, "Rewinside", true)) break block9;
                if (!StringsKt.equals(mode, "Mineplex", true)) break block8;
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
    public final void onJump(@NotNull JumpEvent e) {
        block5: {
            block4: {
                Intrinsics.checkParameterIsNotNull(e, "e");
                String mode = (String)this.modeValue.get();
                if (StringsKt.equals(mode, "Hypixel", true) || StringsKt.equals(mode, "BoostHypixel", true) || StringsKt.equals(mode, "Rewinside", true)) break block4;
                if (!StringsKt.equals(mode, "Mineplex", true)) break block5;
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
    public final void onStep(@NotNull StepEvent e) {
        block5: {
            block4: {
                Intrinsics.checkParameterIsNotNull(e, "e");
                String mode = (String)this.modeValue.get();
                if (StringsKt.equals(mode, "Hypixel", true) || StringsKt.equals(mode, "BoostHypixel", true) || StringsKt.equals(mode, "Rewinside", true)) break block4;
                if (!StringsKt.equals(mode, "Mineplex", true)) break block5;
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
        for (double posY = (v178175).getPosY(); posY > ground; posY -= 8.0) {
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

    private final void redeskyVClip1(float vertical) {
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
        double d2 = iEntityPlayerSP3.getPosY() + (double)vertical;
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setPosition(d, d2, iEntityPlayerSP4.getPosZ());
    }

    private final void redeskyHClip1(double horizontal) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double playerYaw = Math.toRadians(iEntityPlayerSP.getRotationYaw());
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d = horizontal;
        double d2 = iEntityPlayerSP3.getPosX();
        IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
        boolean bl = false;
        double d3 = Math.sin(playerYaw);
        double d4 = d2 + d * -d3;
        IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP5 == null) {
            Intrinsics.throwNpe();
        }
        double d5 = iEntityPlayerSP5.getPosY();
        IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP6 == null) {
            Intrinsics.throwNpe();
        }
        double d6 = horizontal;
        d3 = iEntityPlayerSP6.getPosZ();
        d = d5;
        d2 = d4;
        bl = false;
        double d7 = Math.cos(playerYaw);
        iEntityPlayerSP4.setPosition(d2, d, d3 + d6 * d7);
    }

    private final void redeskyHClip2(double horizontal) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double playerYaw = Math.toRadians(iEntityPlayerSP.getRotationYaw());
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d = horizontal;
        double d2 = iEntityPlayerSP2.getPosX();
        IClassProvider iClassProvider = MinecraftInstance.classProvider;
        IINetHandlerPlayClient iINetHandlerPlayClient2 = iINetHandlerPlayClient;
        boolean bl = false;
        double d3 = Math.sin(playerYaw);
        double d4 = d2 + d * -d3;
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d5 = iEntityPlayerSP3.getPosY();
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        double d6 = horizontal;
        d3 = iEntityPlayerSP4.getPosZ();
        d = d5;
        d2 = d4;
        bl = false;
        double d7 = Math.cos(playerYaw);
        iINetHandlerPlayClient2.addToSendQueue(iClassProvider.createCPacketPlayerPosition(d2, d, d3 + d6 * d7, false));
    }

    private final void redeskyVClip2(double vertical) {
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
        double d2 = iEntityPlayerSP2.getPosY() + vertical;
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2, iEntityPlayerSP3.getPosZ(), false));
    }

    private final void redeskySpeed(int speed) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double playerYaw = Math.toRadians(iEntityPlayerSP.getRotationYaw());
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d = speed;
        IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
        boolean bl = false;
        double d2 = Math.sin(playerYaw);
        iEntityPlayerSP3.setMotionX(d * -d2);
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        d = speed;
        iEntityPlayerSP3 = iEntityPlayerSP4;
        bl = false;
        d2 = Math.cos(playerYaw);
        iEntityPlayerSP3.setMotionZ(d * d2);
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
        for (double ground = (v178827).getPosY(); ground > 0.0; ground -= blockHeight) {
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
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
