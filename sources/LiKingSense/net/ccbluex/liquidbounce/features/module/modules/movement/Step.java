/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.StepConfirmEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Step", description="Allows you to step up blocks.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001a\u001a\u00020\bH\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001cH\u0016J\u0010\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0010\u0010!\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020\"H\u0007J\u0010\u0010#\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020$H\u0007J\u0010\u0010%\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020&H\u0007J\u0010\u0010'\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020(H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Step;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "heightValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "isAACStep", "", "isStep", "jumpHeightValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "ncpNextStep", "", "spartanSwitch", "stepX", "", "stepY", "stepZ", "tag", "", "getTag", "()Ljava/lang/String;", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "couldStep", "fakeJump", "", "onDisable", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onStep", "Lnet/ccbluex/liquidbounce/event/StepEvent;", "onStepConfirm", "Lnet/ccbluex/liquidbounce/event/StepConfirmEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class Step
extends Module {
    public final ListValue modeValue;
    public final FloatValue heightValue;
    public final FloatValue jumpHeightValue;
    public final IntegerValue delayValue;
    public boolean isStep;
    public double stepX;
    public double stepY;
    public double stepZ;
    public int ncpNextStep;
    public boolean spartanSwitch;
    public boolean isAACStep;
    public final MSTimer timer;

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        thePlayer.setStepHeight(0.5f);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block7: {
            block9: {
                IEntityPlayerSP thePlayer;
                String mode;
                block8: {
                    block6: {
                        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
                        mode = (String)this.modeValue.get();
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            return;
                        }
                        thePlayer = iEntityPlayerSP;
                        if (!StringsKt.equals((String)mode, (String)"jump", (boolean)true) || !thePlayer.isCollidedHorizontally() || !thePlayer.getOnGround() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break block6;
                        this.fakeJump();
                        thePlayer.setMotionY(((Number)this.jumpHeightValue.get()).floatValue());
                        break block7;
                    }
                    if (!StringsKt.equals((String)mode, (String)"laac", (boolean)true)) break block8;
                    if (!(!thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder() || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb())) {
                        if (thePlayer.getOnGround() && this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                            this.isStep = 1;
                            this.fakeJump();
                            IEntityPlayerSP iEntityPlayerSP = thePlayer;
                            iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() + 0.620000001490116);
                            float f = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                            double d = iEntityPlayerSP2.getMotionX();
                            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                            float f2 = (float)Math.sin(f);
                            iEntityPlayerSP3.setMotionX(d - (double)f2 * 0.2);
                            IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                            d = iEntityPlayerSP4.getMotionZ();
                            iEntityPlayerSP3 = iEntityPlayerSP4;
                            f2 = (float)Math.cos(f);
                            iEntityPlayerSP3.setMotionZ(d + (double)f2 * 0.2);
                            this.timer.reset();
                        }
                        thePlayer.setOnGround(true);
                    } else {
                        this.isStep = 0;
                    }
                    break block7;
                }
                if (!StringsKt.equals((String)mode, (String)"aac3.3.4", (boolean)true)) break block7;
                if (!thePlayer.isCollidedHorizontally() || !MovementUtils.isMoving()) break block9;
                if (thePlayer.getOnGround() && this.couldStep()) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() * 1.26);
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setMotionZ(iEntityPlayerSP5.getMotionZ() * 1.26);
                    thePlayer.jump();
                    this.isAACStep = 1;
                }
                if (!this.isAACStep) break block7;
                IEntityPlayerSP iEntityPlayerSP = thePlayer;
                iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - 0.015);
                if (!thePlayer.isUsingItem() && thePlayer.getMovementInput().getMoveStrafe() == 0.0f) {
                    thePlayer.setJumpMovementFactor(0.3f);
                }
                break block7;
            }
            this.isAACStep = 0;
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        block1: {
            block3: {
                block2: {
                    Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
                    String mode = (String)this.modeValue.get();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        return;
                    }
                    IEntityPlayerSP thePlayer = iEntityPlayerSP;
                    if (!StringsKt.equals((String)mode, (String)"motionncp", (boolean)true) || !thePlayer.isCollidedHorizontally() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break block1;
                    if (!thePlayer.getOnGround() || !this.couldStep()) break block2;
                    this.fakeJump();
                    thePlayer.setMotionY(0.0);
                    event.setY(0.41999998688698);
                    this.ncpNextStep = 1;
                    break block1;
                }
                if (this.ncpNextStep != 1) break block3;
                event.setY(0.33319999363422);
                this.ncpNextStep = 2;
                break block1;
            }
            if (this.ncpNextStep != 2) break block1;
            double yaw = MovementUtils.getDirection();
            event.setY(0.24813599859094704);
            MoveEvent moveEvent = event;
            double d = Math.sin(yaw);
            moveEvent.setX(-d * 0.7);
            moveEvent = event;
            d = Math.cos(yaw);
            moveEvent.setZ(d * 0.7);
            this.ncpNextStep = 0;
        }
    }

    @EventTarget
    public final void onStep(@NotNull StepEvent event) {
        String flyMode;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.movement.Fly");
        }
        Fly fly = (Fly)module;
        if (fly.getState() && (StringsKt.equals((String)(flyMode = (String)fly.getModeValue().get()), (String)"Hypixel", (boolean)true) || StringsKt.equals((String)flyMode, (String)"OtherHypixel", (boolean)true) || StringsKt.equals((String)flyMode, (String)"LatestHypixel", (boolean)true) || StringsKt.equals((String)flyMode, (String)"Rewinside", (boolean)true) || StringsKt.equals((String)flyMode, (String)"Mineplex", (boolean)true) && thePlayer.getInventory().getCurrentItemInHand() == null)) {
            event.setStepHeight(0.0f);
            return;
        }
        String mode = (String)this.modeValue.get();
        if (!thePlayer.getOnGround() || !this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue()) || StringsKt.equals((String)mode, (String)"Jump", (boolean)true) || StringsKt.equals((String)mode, (String)"MotionNCP", (boolean)true) || StringsKt.equals((String)mode, (String)"LAAC", (boolean)true) || StringsKt.equals((String)mode, (String)"AAC3.3.4", (boolean)true)) {
            thePlayer.setStepHeight(0.5f);
            event.setStepHeight(0.5f);
            return;
        }
        float height = ((Number)this.heightValue.get()).floatValue();
        thePlayer.setStepHeight(height);
        event.setStepHeight(height);
        if (event.getStepHeight() > 0.5f) {
            this.isStep = 1;
            this.stepX = thePlayer.getPosX();
            this.stepY = thePlayer.getPosY();
            this.stepZ = thePlayer.getPosZ();
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onStepConfirm(@NotNull StepConfirmEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || !this.isStep) {
            return;
        }
        if (thePlayer.getEntityBoundingBox().getMinY() - this.stepY > 0.5) {
            String mode = (String)this.modeValue.get();
            if (StringsKt.equals((String)mode, (String)"NCP", (boolean)true) || StringsKt.equals((String)mode, (String)"AAC", (boolean)true)) {
                this.fakeJump();
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                this.timer.reset();
            } else if (StringsKt.equals((String)mode, (String)"Spartan", (boolean)true)) {
                this.fakeJump();
                if (this.spartanSwitch) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.6, this.stepZ, false));
                }
                this.spartanSwitch = !this.spartanSwitch ? 1 : 0;
                this.timer.reset();
            } else if (StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true)) {
                this.fakeJump();
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                this.timer.reset();
            }
        }
        this.isStep = 0;
        this.stepX = 0.0;
        this.stepY = 0.0;
        this.stepZ = 0.0;
    }

    @EventTarget(ignoreCondition=true)
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.isStep && StringsKt.equals((String)((String)this.modeValue.get()), (String)"OldNCP", (boolean)true)) {
            ICPacketPlayer iCPacketPlayer = packet.asCPacketPlayer();
            iCPacketPlayer.setY(iCPacketPlayer.getY() + 0.07);
            this.isStep = 0;
        }
    }

    public final void fakeJump() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        thePlayer.setAirBorne(true);
        thePlayer.triggerAchievement(MinecraftInstance.classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    public final boolean couldStep() {
        double yaw = MovementUtils.getDirection();
        double x = -Math.sin(yaw) * 0.4;
        double z = Math.cos(yaw) * 0.4;
        return MinecraftInstance.mc.getTheWorld().getCollisionBoxes(MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().offset(x, 1.001335979112147, z)).isEmpty();
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    /*
     * Exception decompiling
     */
    public Step() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl76 : PUTFIELD - null : Stack underflow
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

