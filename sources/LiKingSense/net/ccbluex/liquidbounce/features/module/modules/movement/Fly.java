/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
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
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u009c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u00109\u001a\u00020\tH\u0002J\b\u0010:\u001a\u00020;H\u0002J\u0010\u0010<\u001a\u00020;2\u0006\u0010=\u001a\u00020>H\u0007J\b\u0010?\u001a\u00020;H\u0016J\b\u0010@\u001a\u00020;H\u0016J\u0010\u0010A\u001a\u00020;2\u0006\u0010B\u001a\u00020CH\u0007J\u0010\u0010D\u001a\u00020;2\u0006\u0010=\u001a\u00020EH\u0007J\u0010\u0010F\u001a\u00020;2\u0006\u0010=\u001a\u00020GH\u0007J\u0010\u0010H\u001a\u00020;2\u0006\u0010=\u001a\u00020IH\u0007J\u0012\u0010J\u001a\u00020;2\b\u0010=\u001a\u0004\u0018\u00010KH\u0007J\u0010\u0010L\u001a\u00020;2\u0006\u0010B\u001a\u00020MH\u0007J\u0012\u0010N\u001a\u00020;2\b\u0010=\u001a\u0004\u0018\u00010OH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010'\u001a\u00020(\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u000e\u0010+\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u00102\u001a\u0002038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b4\u00105R\u000e\u00106\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006P"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Fly;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aac3delay", "", "aac3glideDelay", "aacFast", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "aacJump", "", "aacMotion", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "aacMotion2", "aacSpeedValue", "boostHypixelState", "cubecraft2TickTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "cubecraftTeleportTickTimer", "failedStart", "", "flyTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "freeHypixelPitch", "", "freeHypixelTimer", "freeHypixelYaw", "groundTimer", "hypixelBoost", "hypixelBoostDelay", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hypixelBoostTimer", "hypixelTimer", "lastDistance", "markValue", "mineSecureVClipTimer", "mineplexSpeedValue", "mineplexTimer", "minesuchtTP", "", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "moveSpeed", "ncpMotionValue", "neruxVaceTicks", "noFlag", "noPacketModify", "spartanTimer", "startY", "tag", "", "getTag", "()Ljava/lang/String;", "vanillaKickBypassValue", "vanillaSpeedValue", "wasDead", "calculateGround", "handleVanillaKickBypass", "", "onBB", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onDisable", "onEnable", "onJump", "e", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onStep", "Lnet/ccbluex/liquidbounce/event/StepEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class Fly
extends Module {
    @NotNull
    public final ListValue modeValue;
    public final FloatValue vanillaSpeedValue;
    public final BoolValue vanillaKickBypassValue;
    public final FloatValue ncpMotionValue;
    public final FloatValue aacSpeedValue;
    public final BoolValue aacFast;
    public final FloatValue aacMotion;
    public final FloatValue aacMotion2;
    public final BoolValue hypixelBoost;
    public final IntegerValue hypixelBoostDelay;
    public final FloatValue hypixelBoostTimer;
    public final FloatValue mineplexSpeedValue;
    public final IntegerValue neruxVaceTicks;
    public final BoolValue markValue;
    public double startY;
    public final MSTimer flyTimer;
    public final MSTimer groundTimer;
    public boolean noPacketModify;
    public double aacJump;
    public int aac3delay;
    public int aac3glideDelay;
    public boolean noFlag;
    public final MSTimer mineSecureVClipTimer;
    public final TickTimer spartanTimer;
    public long minesuchtTP;
    public final MSTimer mineplexTimer;
    public boolean wasDead;
    public final TickTimer hypixelTimer;
    public int boostHypixelState;
    public double moveSpeed;
    public double lastDistance;
    public boolean failedStart;
    public final TickTimer cubecraft2TickTimer;
    public final TickTimer cubecraftTeleportTickTimer;
    public final TickTimer freeHypixelTimer;
    public float freeHypixelYaw;
    public float freeHypixelPitch;

    @NotNull
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
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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

    /*
     * Exception decompiling
     */
    @Override
    public void onDisable() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl36 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
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

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        String string;
        Fly fly;
        float vanillaSpeed = ((Number)this.vanillaSpeedValue.get()).floatValue();
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        Fly $this$run = fly = this;
        String string2 = string = (String)$this$run.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "vanilla": {
                thePlayer.getCapabilities().setFlying(false);
                MinecraftInstance.mc.getThePlayer().setOnGround(true);
                thePlayer.setMotionY(0.0);
                thePlayer.setMotionX(0.0);
                thePlayer.setMotionZ(0.0);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - (double)vanillaSpeed);
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
                int n = $this$run.aac3delay;
                $this$run.aac3delay = n + 1;
                break;
            }
            case "aac3.1.6-gomme": {
                thePlayer.getCapabilities().setFlying(true);
                if ($this$run.aac3delay == 2) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() + 0.05);
                } else if ($this$run.aac3delay > 2) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - 0.05);
                    $this$run.aac3delay = 0;
                }
                int n = $this$run.aac3delay;
                $this$run.aac3delay = n + 1;
                if (!$this$run.noFlag) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), thePlayer.getOnGround()));
                }
                if (!(thePlayer.getPosY() <= 0.0)) break;
                $this$run.noFlag = 1;
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
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - (double)vanillaSpeed);
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
                if (!$this$run.mineSecureVClipTimer.hasTimePassed(240L - 438L + 203L + 145L) || !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + (double)5, thePlayer.getPosZ(), false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(0.5, -1000.0, 0.5, false));
                double yaw = Math.toRadians(thePlayer.getRotationYaw());
                double x = -Math.sin(yaw) * 0.4;
                double z = Math.cos(yaw) * 0.4;
                thePlayer.setPosition(thePlayer.getPosX() + x, thePlayer.getPosY(), thePlayer.getPosZ() + z);
                $this$run.mineSecureVClipTimer.reset();
                break;
            }
            case "hac": {
                IEntityPlayerSP iEntityPlayerSP = thePlayer;
                iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() * 0.8);
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionZ(iEntityPlayerSP2.getMotionZ() * 0.8);
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
                double d2 = Math.sin(d);
                d = Math.toRadians(pitch);
                double d3 = Math.cos(d);
                d = Math.toRadians(pitch);
                d2 = d2 * d3 * length + vectorStart.getXCoord();
                d3 = Math.sin(d);
                d = Math.toRadians(yaw);
                d3 = d3 * length + vectorStart.getYCoord();
                double d4 = Math.cos(d);
                d = Math.toRadians(pitch);
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
                    WVec3 vec31 = MinecraftInstance.mc.getThePlayer().getLook(0.0f);
                    WVec3 wVec3 = vec3;
                    double d = vec31.getXCoord() * (double)7;
                    double d9 = vec31.getYCoord() * (double)7;
                    double z$iv = vec31.getZCoord() * (double)7;
                    WVec3 vec32 = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
                    if ((double)thePlayer.getFallDistance() > 0.8) {
                        thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(posX, posY + (double)50, posZ, false));
                        MinecraftInstance.mc.getThePlayer().fall(100.0f, 100.0f);
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
                IEntityPlayerSP iEntityPlayerSP = thePlayer;
                iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() + 0.15);
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * 1.1);
                IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 1.1);
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
                    if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() && $this$run.mineplexTimer.hasTimePassed(227L - 328L + 80L + 121L)) {
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.6, thePlayer.getPosZ());
                        $this$run.mineplexTimer.reset();
                    }
                    if (MinecraftInstance.mc.getThePlayer().isSneaking() && $this$run.mineplexTimer.hasTimePassed(21L - 41L + 12L - 8L + 116L)) {
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() - 0.6, thePlayer.getPosZ());
                        $this$run.mineplexTimer.reset();
                    }
                    WBlockPos blockPos = new WBlockPos(thePlayer.getPosX(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMinY() - (double)1, thePlayer.getPosZ());
                    WVec3 pitch = new WVec3(blockPos);
                    double length = 0.4;
                    double d = 0.4;
                    double z$iv = 0.4;
                    this_$iv = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
                    WVec3 vec$iv = new WVec3(MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP).getDirectionVec());
                    WVec3 vectorEnd = this_$iv;
                    double d10 = vec$iv.getXCoord();
                    double d11 = vec$iv.getYCoord();
                    double z$iv$iv = vec$iv.getZCoord();
                    WVec3 vec = new WVec3(this_$iv$iv.getXCoord() + x$iv$iv, this_$iv$iv.getYCoord() + y$iv$iv, this_$iv$iv.getZCoord() + z$iv$iv);
                    MinecraftInstance.mc.getPlayerController().onPlayerRightClick(thePlayer, MinecraftInstance.mc.getTheWorld(), thePlayer.getInventory().getCurrentItemInHand(), blockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP), new WVec3(vec.getXCoord() * (double)0.4f, vec.getYCoord() * (double)0.4f, vec.getZCoord() * (double)0.4f));
                    MovementUtils.strafe(0.27f);
                    MinecraftInstance.mc.getTimer().setTimerSpeed((float)1 + ((Number)$this$run.mineplexSpeedValue.get()).floatValue());
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
                if (!thePlayer.getOnGround()) {
                    int blockPos = $this$run.aac3glideDelay;
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
                    $this$run.wasDead = 1;
                }
                if ($this$run.wasDead || thePlayer.getOnGround()) {
                    $this$run.wasDead = 0;
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
                MinecraftInstance.mc.getThePlayer().setSprinting(true);
                if (thePlayer.getPosY() < $this$run.startY + (double)2) {
                    thePlayer.setMotionY(Math.random() * 0.48954512);
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
                if (!thePlayer.getOnGround()) {
                    int blockPos = $this$run.aac3glideDelay;
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
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() + (double)vanillaSpeed);
                }
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - (double)vanillaSpeed);
                }
                MovementUtils.strafe(vanillaSpeed);
            }
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"boosthypixel", (boolean)true)) {
            switch (Fly$WhenMappings.$EnumSwitchMapping$0[event.getEventState().ordinal()]) {
                case 1: {
                    this.hypixelTimer.update();
                    if (this.hypixelTimer.hasTimePassed(2)) {
                        MinecraftInstance.mc.getThePlayer().setPosition(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY() + 1.0E-5, MinecraftInstance.mc.getThePlayer().getPosZ());
                        this.hypixelTimer.reset();
                    }
                    if (this.failedStart) break;
                    MinecraftInstance.mc.getThePlayer().setMotionY(0.0);
                    break;
                }
                case 2: {
                    double d;
                    double xDist = MinecraftInstance.mc.getThePlayer().getPosX() - MinecraftInstance.mc.getThePlayer().getPrevPosX();
                    double zDist = MinecraftInstance.mc.getThePlayer().getPosZ() - MinecraftInstance.mc.getThePlayer().getPrevPosZ();
                    double d2 = xDist * xDist + zDist * zDist;
                    Fly fly = this;
                    fly.lastDistance = d = Math.sqrt(d2);
                    break;
                }
            }
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl92 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
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

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        String mode;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (this.noPacketModify) {
            return;
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(event.getPacket())) {
            ICPacketPlayer packetPlayer = event.getPacket().asCPacketPlayer();
            String mode2 = (String)this.modeValue.get();
            if (StringsKt.equals((String)mode2, (String)"NCP", (boolean)true) || StringsKt.equals((String)mode2, (String)"Rewinside", (boolean)true) || StringsKt.equals((String)mode2, (String)"Mineplex", (boolean)true) && MinecraftInstance.mc.getThePlayer().getInventory().getCurrentItemInHand() == null) {
                packetPlayer.setOnGround(true);
            }
            if (StringsKt.equals((String)mode2, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode2, (String)"BoostHypixel", (boolean)true)) {
                packetPlayer.setOnGround(false);
            }
        }
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(event.getPacket()) && StringsKt.equals((String)(mode = (String)this.modeValue.get()), (String)"BoostHypixel", (boolean)true)) {
            this.failedStart = 1;
            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lBoostHypixel-\u00a7a\u00a7lFly\u00a78] \u00a7cSetback detected.");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        block14: {
            double d;
            String string;
            Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
            String string2 = string = (String)this.modeValue.get();
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
            string = string3;
            switch (string.hashCode()) {
                case -385327063: {
                    if (!string.equals("freehypixel")) return;
                    break block14;
                }
                case 1814517522: {
                    if (!string.equals("boosthypixel")) return;
                    break;
                }
                case -1031473397: {
                    if (!string.equals("cubecraft")) return;
                    double yaw = Math.toRadians(MinecraftInstance.mc.getThePlayer().getRotationYaw());
                    if (this.cubecraftTeleportTickTimer.hasTimePassed(2)) {
                        MoveEvent moveEvent = event;
                        double d2 = Math.sin(yaw);
                        moveEvent.setX(-d2 * 2.4);
                        moveEvent = event;
                        d2 = Math.cos(yaw);
                        moveEvent.setZ(d2 * 2.4);
                        this.cubecraftTeleportTickTimer.reset();
                        return;
                    }
                    MoveEvent moveEvent = event;
                    double d3 = Math.sin(yaw);
                    moveEvent.setX(-d3 * 0.2);
                    moveEvent = event;
                    d3 = Math.cos(yaw);
                    moveEvent.setZ(d3 * 0.2);
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
            double amplifier = (double)1 + (MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) ? 0.2 * ((double)MinecraftInstance.mc.getThePlayer().getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)).getAmplifier() + 1.0) : 0.0);
            double baseSpeed = 0.29 * amplifier;
            switch (this.boostHypixelState) {
                case 1: {
                    this.moveSpeed = (MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) ? 1.56 : 2.034) * baseSpeed;
                    this.boostHypixelState = 2;
                    break;
                }
                case 2: {
                    this.moveSpeed *= 2.16;
                    this.boostHypixelState = 3;
                    break;
                }
                case 3: {
                    this.moveSpeed = this.lastDistance - (MinecraftInstance.mc.getThePlayer().getTicksExisted() % 2 == 0 ? 0.0103 : 0.0123) * (this.lastDistance - baseSpeed);
                    this.boostHypixelState = 4;
                    break;
                }
                default: {
                    this.moveSpeed = this.lastDistance - this.lastDistance / 159.8;
                }
            }
            double d4 = this.moveSpeed;
            double d5 = 0.3;
            Object object = this;
            ((Fly)object).moveSpeed = d = Math.max(d4, d5);
            double yaw = MovementUtils.getDirection();
            object = event;
            d = Math.sin(yaw);
            ((MoveEvent)object).setX(-d * this.moveSpeed);
            object = event;
            d = Math.cos(yaw);
            ((MoveEvent)object).setZ(d * this.moveSpeed);
            MinecraftInstance.mc.getThePlayer().setMotionX(event.getX());
            MinecraftInstance.mc.getThePlayer().setMotionZ(event.getZ());
            return;
        }
        if (this.freeHypixelTimer.hasTimePassed(10)) return;
        event.zero();
        return;
    }

    @EventTarget
    public final void onBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        String mode = (String)this.modeValue.get();
        if (MinecraftInstance.classProvider.isBlockAir(event.getBlock()) && (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true) || StringsKt.equals((String)mode, (String)"Mineplex", (boolean)true) && MinecraftInstance.mc.getThePlayer().getInventory().getCurrentItemInHand() == null) && (double)event.getY() < MinecraftInstance.mc.getThePlayer().getPosY()) {
            event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + 1.0, MinecraftInstance.mc.getThePlayer().getPosY(), (double)event.getZ() + 1.0));
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent e) {
        Intrinsics.checkParameterIsNotNull((Object)e, (String)"e");
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true) || StringsKt.equals((String)mode, (String)"Mineplex", (boolean)true) && MinecraftInstance.mc.getThePlayer().getInventory().getCurrentItemInHand() == null) {
            e.cancelEvent();
        }
    }

    @EventTarget
    public final void onStep(@NotNull StepEvent e) {
        Intrinsics.checkParameterIsNotNull((Object)e, (String)"e");
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"BoostHypixel", (boolean)true) || StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true) || StringsKt.equals((String)mode, (String)"Mineplex", (boolean)true) && MinecraftInstance.mc.getThePlayer().getInventory().getCurrentItemInHand() == null) {
            e.setStepHeight(0.0f);
        }
    }

    public final void handleVanillaKickBypass() {
        Fly fly;
        if (!((Boolean)this.vanillaKickBypassValue.get()).booleanValue() || !this.groundTimer.hasTimePassed(55L - 98L + 52L + 991L)) {
            return;
        }
        double ground = this.calculateGround();
        Fly $this$run = fly = this;
        for (double posY = MinecraftInstance.mc.getThePlayer().getPosY(); posY > ground; posY -= 8.0) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), posY, MinecraftInstance.mc.getThePlayer().getPosZ(), true));
            if (posY - 8.0 < ground) break;
        }
        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), ground, MinecraftInstance.mc.getThePlayer().getPosZ(), true));
        for (double posY = ground; posY < MinecraftInstance.mc.getThePlayer().getPosY(); posY += 8.0) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), posY, MinecraftInstance.mc.getThePlayer().getPosZ(), true));
            if (posY + 8.0 > MinecraftInstance.mc.getThePlayer().getPosY()) break;
        }
        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY(), MinecraftInstance.mc.getThePlayer().getPosZ(), true));
        this.groundTimer.reset();
    }

    public final double calculateGround() {
        IAxisAlignedBB playerBoundingBox = MinecraftInstance.mc.getThePlayer().getEntityBoundingBox();
        double blockHeight = 1.0;
        for (double ground = MinecraftInstance.mc.getThePlayer().getPosY(); ground > 0.0; ground -= blockHeight) {
            IAxisAlignedBB customBox = MinecraftInstance.classProvider.createAxisAlignedBB(playerBoundingBox.getMaxX(), ground + blockHeight, playerBoundingBox.getMaxZ(), playerBoundingBox.getMinX(), ground, playerBoundingBox.getMinZ());
            if (!MinecraftInstance.mc.getTheWorld().checkBlockCollision(customBox)) continue;
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

    /*
     * Exception decompiling
     */
    public Fly() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl202 : PUTFIELD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
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
}

