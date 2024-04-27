/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

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
import net.ccbluex.liquidbounce.features.module.modules.exploit.Phase;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Step", description="Allows you to step up blocks.", category=ModuleCategory.MOVEMENT)
public final class Step
extends Module {
    private final FloatValue jumpHeightValue;
    private double stepX;
    private boolean spartanSwitch;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Jump", "NCP", "MotionNCP", "OldNCP", "AAC", "LAAC", "AAC3.3.4", "Spartan", "Rewinside"}, "NCP");
    private boolean isAACStep;
    private final FloatValue heightValue = new FloatValue("Height", 1.0f, 0.6f, 10.0f);
    private int ncpNextStep;
    private double stepY;
    private final MSTimer timer;
    private double stepZ;
    private final IntegerValue delayValue;
    private boolean isStep;

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        block1: {
            block3: {
                block2: {
                    String string = (String)this.modeValue.get();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
                    if (!StringsKt.equals((String)string, (String)"motionncp", (boolean)true) || !iEntityPlayerSP2.isCollidedHorizontally() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break block1;
                    if (!iEntityPlayerSP2.getOnGround() || this != null) break block2;
                    this.fakeJump();
                    iEntityPlayerSP2.setMotionY(0.0);
                    moveEvent.setY(0.41999998688698);
                    this.ncpNextStep = 1;
                    break block1;
                }
                if (this.ncpNextStep != 1) break block3;
                moveEvent.setY(0.33319999363422);
                this.ncpNextStep = 2;
                break block1;
            }
            if (this.ncpNextStep != 2) break block1;
            double d = MovementUtils.getDirection();
            moveEvent.setY(0.24813599859094704);
            MoveEvent moveEvent2 = moveEvent;
            boolean bl = false;
            double d2 = Math.sin(d);
            moveEvent2.setX(-d2 * 0.7);
            moveEvent2 = moveEvent;
            bl = false;
            d2 = Math.cos(d);
            moveEvent2.setZ(d2 * 0.7);
            this.ncpNextStep = 0;
        }
    }

    @EventTarget
    public final void onStep(StepEvent stepEvent) {
        String string;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Phase.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) {
            stepEvent.setStepHeight(0.0f);
            return;
        }
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.movement.Fly");
        }
        Fly fly = (Fly)module2;
        if (fly.getState() && (StringsKt.equals((String)(string = (String)fly.getModeValue().get()), (String)"Hypixel", (boolean)true) || StringsKt.equals((String)string, (String)"OtherHypixel", (boolean)true) || StringsKt.equals((String)string, (String)"LatestHypixel", (boolean)true) || StringsKt.equals((String)string, (String)"Rewinside", (boolean)true) || StringsKt.equals((String)string, (String)"Mineplex", (boolean)true) && iEntityPlayerSP2.getInventory().getCurrentItemInHand() == null)) {
            stepEvent.setStepHeight(0.0f);
            return;
        }
        string = (String)this.modeValue.get();
        if (!iEntityPlayerSP2.getOnGround() || !this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue()) || StringsKt.equals((String)string, (String)"Jump", (boolean)true) || StringsKt.equals((String)string, (String)"MotionNCP", (boolean)true) || StringsKt.equals((String)string, (String)"LAAC", (boolean)true) || StringsKt.equals((String)string, (String)"AAC3.3.4", (boolean)true)) {
            iEntityPlayerSP2.setStepHeight(0.5f);
            stepEvent.setStepHeight(0.5f);
            return;
        }
        float f = ((Number)this.heightValue.get()).floatValue();
        iEntityPlayerSP2.setStepHeight(f);
        stepEvent.setStepHeight(f);
        if (stepEvent.getStepHeight() > 0.5f) {
            this.isStep = true;
            this.stepX = iEntityPlayerSP2.getPosX();
            this.stepY = iEntityPlayerSP2.getPosY();
            this.stepZ = iEntityPlayerSP2.getPosZ();
        }
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        block7: {
            block9: {
                IEntityPlayerSP iEntityPlayerSP;
                String string;
                block8: {
                    block6: {
                        string = (String)this.modeValue.get();
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            return;
                        }
                        iEntityPlayerSP = iEntityPlayerSP2;
                        if (!StringsKt.equals((String)string, (String)"jump", (boolean)true) || !iEntityPlayerSP.isCollidedHorizontally() || !iEntityPlayerSP.getOnGround() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break block6;
                        this.fakeJump();
                        iEntityPlayerSP.setMotionY(((Number)this.jumpHeightValue.get()).floatValue());
                        break block7;
                    }
                    if (!StringsKt.equals((String)string, (String)"laac", (boolean)true)) break block8;
                    if (!(!iEntityPlayerSP.isCollidedHorizontally() || iEntityPlayerSP.isOnLadder() || iEntityPlayerSP.isInWater() || iEntityPlayerSP.isInLava() || iEntityPlayerSP.isInWeb())) {
                        if (iEntityPlayerSP.getOnGround() && this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                            this.isStep = true;
                            this.fakeJump();
                            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP;
                            iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + 0.620000001490116);
                            float f = iEntityPlayerSP.getRotationYaw() * ((float)Math.PI / 180);
                            IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP;
                            double d = iEntityPlayerSP4.getMotionX();
                            IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP4;
                            boolean bl = false;
                            float f2 = (float)Math.sin(f);
                            iEntityPlayerSP5.setMotionX(d - (double)f2 * 0.2);
                            IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP;
                            d = iEntityPlayerSP6.getMotionZ();
                            iEntityPlayerSP5 = iEntityPlayerSP6;
                            bl = false;
                            f2 = (float)Math.cos(f);
                            iEntityPlayerSP5.setMotionZ(d + (double)f2 * 0.2);
                            this.timer.reset();
                        }
                        iEntityPlayerSP.setOnGround(true);
                    } else {
                        this.isStep = false;
                    }
                    break block7;
                }
                if (!StringsKt.equals((String)string, (String)"aac3.3.4", (boolean)true)) break block7;
                if (!iEntityPlayerSP.isCollidedHorizontally() || !MovementUtils.isMoving()) break block9;
                if (iEntityPlayerSP.getOnGround() && this == null) {
                    IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP;
                    iEntityPlayerSP7.setMotionX(iEntityPlayerSP7.getMotionX() * 1.26);
                    IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP;
                    iEntityPlayerSP8.setMotionZ(iEntityPlayerSP8.getMotionZ() * 1.26);
                    iEntityPlayerSP.jump();
                    this.isAACStep = true;
                }
                if (!this.isAACStep) break block7;
                IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP;
                iEntityPlayerSP9.setMotionY(iEntityPlayerSP9.getMotionY() - 0.015);
                if (!iEntityPlayerSP.isUsingItem() && iEntityPlayerSP.getMovementInput().getMoveStrafe() == 0.0f) {
                    iEntityPlayerSP.setJumpMovementFactor(0.3f);
                }
                break block7;
            }
            this.isAACStep = false;
        }
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        iEntityPlayerSP2.setStepHeight(0.5f);
    }

    @EventTarget(ignoreCondition=true)
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(iPacket) && this.isStep && StringsKt.equals((String)((String)this.modeValue.get()), (String)"OldNCP", (boolean)true)) {
            ICPacketPlayer iCPacketPlayer = iPacket.asCPacketPlayer();
            iCPacketPlayer.setY(iCPacketPlayer.getY() + 0.07);
            this.isStep = false;
        }
    }

    public Step() {
        this.jumpHeightValue = new FloatValue("JumpHeight", 0.42f, 0.37f, 0.42f);
        this.delayValue = new IntegerValue("Delay", 0, 0, 500);
        this.timer = new MSTimer();
    }

    private final void fakeJump() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        iEntityPlayerSP2.setAirBorne(true);
        iEntityPlayerSP2.triggerAchievement(MinecraftInstance.classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    @EventTarget(ignoreCondition=true)
    public final void onStepConfirm(StepConfirmEvent stepConfirmEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null || !this.isStep) {
            return;
        }
        if (iEntityPlayerSP.getEntityBoundingBox().getMinY() - this.stepY > 0.5) {
            String string = (String)this.modeValue.get();
            if (StringsKt.equals((String)string, (String)"NCP", (boolean)true) || StringsKt.equals((String)string, (String)"AAC", (boolean)true)) {
                this.fakeJump();
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                this.timer.reset();
            } else if (StringsKt.equals((String)string, (String)"Spartan", (boolean)true)) {
                this.fakeJump();
                if (this.spartanSwitch) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.6, this.stepZ, false));
                }
                this.spartanSwitch = !this.spartanSwitch;
                this.timer.reset();
            } else if (StringsKt.equals((String)string, (String)"Rewinside", (boolean)true)) {
                this.fakeJump();
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                this.timer.reset();
            }
        }
        this.isStep = false;
        this.stepX = 0.0;
        this.stepY = 0.0;
        this.stepZ = 0.0;
    }
}

