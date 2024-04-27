/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AntiAFK", description="Prevents you from getting kicked for being AFK.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\n\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u001cH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AntiAFK;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delayTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "jumpValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "moveValue", "randomTimerDelay", "", "rotateValue", "rotationAngleValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rotationDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "shouldMove", "", "swingDelayTimer", "swingDelayValue", "swingValue", "getRandomMoveKeyBind", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class AntiAFK
extends Module {
    public final MSTimer swingDelayTimer;
    public final MSTimer delayTimer;
    public final ListValue modeValue;
    public final IntegerValue swingDelayValue;
    public final IntegerValue rotationDelayValue;
    public final FloatValue rotationAngleValue;
    public final BoolValue jumpValue;
    public final BoolValue moveValue;
    public final BoolValue rotateValue;
    public final BoolValue swingValue;
    public boolean shouldMove;
    public long randomTimerDelay;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        block5 : switch (string3) {
            case "old": {
                MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(true);
                if (!this.delayTimer.hasTimePassed(178L - 189L + 129L + 382L)) break;
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setRotationYaw(iEntityPlayerSP2.getRotationYaw() + 180.0f);
                this.delayTimer.reset();
                break;
            }
            case "random": {
                this.getRandomMoveKeyBind().setPressed(this.shouldMove);
                if (!this.delayTimer.hasTimePassed(this.randomTimerDelay)) {
                    return;
                }
                this.shouldMove = 0;
                this.randomTimerDelay = 265L - 505L + 128L + 612L;
                switch (0) {
                    case 0: {
                        if (thePlayer.getOnGround()) {
                            thePlayer.jump();
                        }
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 1: {
                        if (!thePlayer.isSwingInProgress()) {
                            thePlayer.swingItem();
                        }
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 2: {
                        this.randomTimerDelay = 0;
                        this.shouldMove = 1;
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 3: {
                        thePlayer.getInventory().setCurrentItem(0);
                        MinecraftInstance.mc.getPlayerController().updateController();
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 4: {
                        IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                        iEntityPlayerSP3.setRotationYaw(iEntityPlayerSP3.getRotationYaw() + RandomUtils.INSTANCE.nextFloat(-180.0f, 180.0f));
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 5: {
                        if (thePlayer.getRotationPitch() <= (float)-90 || thePlayer.getRotationPitch() >= (float)90) {
                            thePlayer.setRotationPitch(0.0f);
                        }
                        IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                        iEntityPlayerSP4.setRotationPitch(iEntityPlayerSP4.getRotationPitch() + RandomUtils.INSTANCE.nextFloat(-10.0f, 10.0f));
                        this.delayTimer.reset();
                        break block5;
                    }
                }
                break;
            }
            case "custom": {
                if (((Boolean)this.moveValue.get()).booleanValue()) {
                    MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(true);
                }
                if (((Boolean)this.jumpValue.get()).booleanValue() && thePlayer.getOnGround()) {
                    thePlayer.jump();
                }
                if (((Boolean)this.rotateValue.get()).booleanValue() && this.delayTimer.hasTimePassed(((Number)this.rotationDelayValue.get()).intValue())) {
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setRotationYaw(iEntityPlayerSP5.getRotationYaw() + ((Number)this.rotationAngleValue.get()).floatValue());
                    if (thePlayer.getRotationPitch() <= (float)-90 || thePlayer.getRotationPitch() >= (float)90) {
                        thePlayer.setRotationPitch(0.0f);
                    }
                    IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                    iEntityPlayerSP6.setRotationPitch(iEntityPlayerSP6.getRotationPitch() + (RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) * (float)2 - (float)1));
                    this.delayTimer.reset();
                }
                if (!((Boolean)this.swingValue.get()).booleanValue() || thePlayer.isSwingInProgress() || !this.swingDelayTimer.hasTimePassed(((Number)this.swingDelayValue.get()).intValue())) break;
                thePlayer.swingItem();
                this.swingDelayTimer.reset();
                break;
            }
        }
    }

    public final IKeyBinding getRandomMoveKeyBind() {
        switch (0) {
            case 0: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindRight();
            }
            case 1: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindLeft();
            }
            case 2: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindBack();
            }
            case 3: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindForward();
            }
        }
        return null;
    }

    @Override
    public void onDisable() {
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindForward())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(false);
        }
    }

    /*
     * Exception decompiling
     */
    public AntiAFK() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl40 : PUTFIELD - null : Stack underflow
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

