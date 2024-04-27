/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Velocity2", description="Allows you to modify the amount of knockback you take.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010 \u001a\u00020!H\u0016J\u0010\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020$H\u0007J\u0010\u0010%\u001a\u00020!2\u0006\u0010#\u001a\u00020&H\u0007J\u0010\u0010'\u001a\u00020!2\u0006\u0010#\u001a\u00020(H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\u00198VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Velocity2;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacPushXZReducerValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "aacPushYReducerValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "canCancelJump", "", "horizontalValue", "hytCount", "", "getHytCount", "()I", "setHytCount", "(I)V", "jump", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onlyCombatValue", "onlyGroundValue", "reverse2StrengthValue", "reverseHurt", "reverseStrengthValue", "tag", "", "getTag", "()Ljava/lang/String;", "velocityInput", "velocityTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "verticalValue", "onDisable", "", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class Velocity2
extends Module {
    public final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
    public final FloatValue verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Simple", "Noxz", "Hyt", "GrimFull", "JumpPlus", "GrimReduce"}, "GrimReduce");
    public final FloatValue reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f);
    public final FloatValue reverse2StrengthValue = new FloatValue("SmoothReverseStrength", 0.05f, 0.02f, 0.1f);
    public final FloatValue aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f);
    public final BoolValue aacPushYReducerValue = new BoolValue("AACPushYReducer", true);
    public final BoolValue onlyCombatValue = new BoolValue("OnlyCombat", false);
    public final BoolValue onlyGroundValue = new BoolValue("OnlyGround", false);
    public MSTimer velocityTimer = new MSTimer();
    public boolean velocityInput;
    public boolean reverseHurt;
    public boolean jump;
    public boolean canCancelJump;
    public int hytCount = 24;

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @Override
    public void onDisable() {
        block0: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setSpeedInAir(0.02f);
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb()) {
            return;
        }
        if (((Boolean)this.onlyGroundValue.get()).booleanValue() && !MinecraftInstance.mc.getThePlayer().getOnGround() || ((Boolean)this.onlyCombatValue.get()).booleanValue() && !LiquidBounce.INSTANCE.getCombatManager().getInCombat()) {
            return;
        }
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "grimreduce": {
                if (thePlayer.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() + -1.0E-7);
                thePlayer.setAirBorne(true);
                break;
            }
            case "aac4": {
                if (!thePlayer.getOnGround()) {
                    if (!this.velocityInput) break;
                    thePlayer.setSpeedInAir(0.02f);
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() * 0.6);
                    IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                    iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() * 0.6);
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(104L - 122L + 63L - 25L + 60L)) break;
                this.velocityInput = 0;
                thePlayer.setSpeedInAir(0.02f);
                break;
            }
            case "anticheat": {
                if (thePlayer.getHurtTime() > 0) {
                    IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                    iEntityPlayerSP7.setMotionX(iEntityPlayerSP7.getMotionX() + -1.1E-10);
                    IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                    iEntityPlayerSP8.setMotionY(iEntityPlayerSP8.getMotionY() + -1.1E-10);
                    IEntityPlayerSP iEntityPlayerSP9 = thePlayer;
                    iEntityPlayerSP9.setMotionZ(iEntityPlayerSP9.getMotionZ() + -1.1E-10);
                    thePlayer.setAirBorne(true);
                }
                if (thePlayer.getHurtTime() < 3) break;
                IEntityPlayerSP iEntityPlayerSP10 = thePlayer;
                iEntityPlayerSP10.setMotionX(iEntityPlayerSP10.getMotionX() + -0.1);
                IEntityPlayerSP iEntityPlayerSP11 = thePlayer;
                iEntityPlayerSP11.setMotionY(iEntityPlayerSP11.getMotionY() + -0.1);
                IEntityPlayerSP iEntityPlayerSP12 = thePlayer;
                iEntityPlayerSP12.setMotionZ(iEntityPlayerSP12.getMotionZ() + -0.1);
                thePlayer.setAirBorne(true);
                thePlayer.setSpeedInAir(0.01f);
                break;
            }
            case "grimac": {
                if (thePlayer.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP13 = thePlayer;
                iEntityPlayerSP13.setMotionX(iEntityPlayerSP13.getMotionX() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP14 = thePlayer;
                iEntityPlayerSP14.setMotionY(iEntityPlayerSP14.getMotionY() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP15 = thePlayer;
                iEntityPlayerSP15.setMotionZ(iEntityPlayerSP15.getMotionZ() + -1.2E-7);
                thePlayer.setAirBorne(true);
                break;
            }
            case "jump": {
                if (thePlayer.getHurtTime() <= 0 || !thePlayer.getOnGround()) break;
                thePlayer.setMotionY(0.42);
                float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP16 = thePlayer;
                double d = iEntityPlayerSP16.getMotionX();
                IEntityPlayerSP iEntityPlayerSP17 = iEntityPlayerSP16;
                float f = (float)Math.sin(yaw);
                iEntityPlayerSP17.setMotionX(d - (double)f * 0.2);
                IEntityPlayerSP iEntityPlayerSP18 = thePlayer;
                d = iEntityPlayerSP18.getMotionZ();
                iEntityPlayerSP17 = iEntityPlayerSP18;
                f = (float)Math.cos(yaw);
                iEntityPlayerSP17.setMotionZ(d + (double)f * 0.2);
                break;
            }
            case "glitch": {
                thePlayer.setNoClip(this.velocityInput);
                if (thePlayer.getHurtTime() == 7) {
                    thePlayer.setMotionY(0.4);
                }
                this.velocityInput = 0;
                break;
            }
            case "reverse": {
                if (!this.velocityInput) {
                    return;
                }
                if (!thePlayer.getOnGround()) {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * ((Number)this.reverseStrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(122L - 177L + 116L + 19L)) break;
                this.velocityInput = 0;
                break;
            }
            case "aac4": {
                if (!thePlayer.getOnGround()) {
                    if (!this.velocityInput) break;
                    thePlayer.setSpeedInAir(0.02f);
                    IEntityPlayerSP iEntityPlayerSP19 = thePlayer;
                    iEntityPlayerSP19.setMotionX(iEntityPlayerSP19.getMotionX() * 0.6);
                    IEntityPlayerSP iEntityPlayerSP20 = thePlayer;
                    iEntityPlayerSP20.setMotionZ(iEntityPlayerSP20.getMotionZ() * 0.6);
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(201L - 314L + 241L - 175L + 127L)) break;
                this.velocityInput = 0;
                thePlayer.setSpeedInAir(0.02f);
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    thePlayer.setSpeedInAir(0.02f);
                    return;
                }
                if (thePlayer.getHurtTime() > 0) {
                    this.reverseHurt = 1;
                }
                if (!thePlayer.getOnGround()) {
                    if (!this.reverseHurt) break;
                    thePlayer.setSpeedInAir(((Number)this.reverse2StrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(40L - 79L + 10L + 109L)) break;
                this.velocityInput = 0;
                this.reverseHurt = 0;
                break;
            }
            case "aac": {
                if (!this.velocityInput || !this.velocityTimer.hasTimePassed(262L - 270L + 134L + -46L)) break;
                IEntityPlayerSP iEntityPlayerSP21 = thePlayer;
                iEntityPlayerSP21.setMotionX(iEntityPlayerSP21.getMotionX() * ((Number)this.horizontalValue.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP22 = thePlayer;
                iEntityPlayerSP22.setMotionZ(iEntityPlayerSP22.getMotionZ() * ((Number)this.horizontalValue.get()).doubleValue());
                this.velocityInput = 0;
                break;
            }
            case "aacpush": {
                if (this.jump) {
                    if (thePlayer.getOnGround()) {
                        this.jump = 0;
                    }
                } else {
                    if (thePlayer.getHurtTime() > 0 && thePlayer.getMotionX() != 0.0 && thePlayer.getMotionZ() != 0.0) {
                        thePlayer.setOnGround(true);
                    }
                    if (thePlayer.getHurtResistantTime() > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue() && !LiquidBounce.INSTANCE.getModuleManager().get(Speed.class).getState()) {
                        IEntityPlayerSP iEntityPlayerSP23 = thePlayer;
                        iEntityPlayerSP23.setMotionY(iEntityPlayerSP23.getMotionY() - 0.014999993);
                    }
                }
                if (thePlayer.getHurtResistantTime() < 19) break;
                float reduce = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP24 = thePlayer;
                iEntityPlayerSP24.setMotionX(iEntityPlayerSP24.getMotionX() / (double)reduce);
                IEntityPlayerSP iEntityPlayerSP25 = thePlayer;
                iEntityPlayerSP25.setMotionZ(iEntityPlayerSP25.getMotionZ() / (double)reduce);
                break;
            }
            case "aaczero": {
                if (thePlayer.getHurtTime() > 0) {
                    if (!this.velocityInput || thePlayer.getOnGround() || thePlayer.getFallDistance() > 2.0f) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP26 = thePlayer;
                    iEntityPlayerSP26.setMotionY(iEntityPlayerSP26.getMotionY() - 1.0);
                    thePlayer.setAirBorne(true);
                    thePlayer.setOnGround(true);
                    break;
                }
                this.velocityInput = 0;
                break;
            }
        }
    }

    public final int getHytCount() {
        return this.hytCount;
    }

    public final void setHytCount(int n) {
        this.hytCount = n;
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
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

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb()) {
            return;
        }
        if (((Boolean)this.onlyGroundValue.get()).booleanValue() && !MinecraftInstance.mc.getThePlayer().getOnGround() || ((Boolean)this.onlyCombatValue.get()).booleanValue() && !LiquidBounce.INSTANCE.getCombatManager().getInCombat()) {
            return;
        }
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "aac4": {
                if (thePlayer.getHurtTime() <= 0) break;
                event.cancelEvent();
                this.velocityInput = 0;
                break;
            }
            case "aacpush": {
                this.jump = 1;
                if (thePlayer.isCollidedVertically()) break;
                event.cancelEvent();
                break;
            }
            case "aac4": {
                if (thePlayer.getHurtTime() <= 0) break;
                event.cancelEvent();
                break;
            }
            case "aaczero": {
                if (thePlayer.getHurtTime() <= 0) break;
                event.cancelEvent();
                break;
            }
        }
    }
}

