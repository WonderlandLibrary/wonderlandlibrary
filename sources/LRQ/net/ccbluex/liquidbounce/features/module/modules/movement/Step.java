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
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
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
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Jump", "NCP", "MotionNCP", "OldNCP", "AAC", "LAAC", "AAC3.3.4", "Spartan", "Rewinside"}, "NCP");
    private final FloatValue heightValue = new FloatValue("Height", 1.0f, 0.6f, 10.0f);
    private final FloatValue jumpHeightValue = new FloatValue("JumpHeight", 0.42f, 0.37f, 0.42f);
    private final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 500);
    private boolean isStep;
    private double stepX;
    private double stepY;
    private double stepZ;
    private int ncpNextStep;
    private boolean spartanSwitch;
    private boolean isAACStep;
    private final MSTimer timer = new MSTimer();

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
    public final void onUpdate(UpdateEvent event) {
        String mode = (String)this.modeValue.get();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (StringsKt.equals((String)mode, (String)"jump", (boolean)true) && thePlayer.isCollidedHorizontally() && thePlayer.getOnGround() && !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            this.fakeJump();
            thePlayer.setMotionY(((Number)this.jumpHeightValue.get()).floatValue());
        } else if (StringsKt.equals((String)mode, (String)"laac", (boolean)true)) {
            if (!(!thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder() || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb())) {
                if (thePlayer.getOnGround() && this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                    this.isStep = true;
                    this.fakeJump();
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + 0.620000001490116);
                    float f = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    double d = iEntityPlayerSP3.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP3;
                    boolean bl = false;
                    float f2 = (float)Math.sin(f);
                    iEntityPlayerSP4.setMotionX(d - (double)f2 * 0.2);
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    d = iEntityPlayerSP5.getMotionZ();
                    iEntityPlayerSP4 = iEntityPlayerSP5;
                    bl = false;
                    f2 = (float)Math.cos(f);
                    iEntityPlayerSP4.setMotionZ(d + (double)f2 * 0.2);
                    this.timer.reset();
                }
                thePlayer.setOnGround(true);
            } else {
                this.isStep = false;
            }
        } else if (StringsKt.equals((String)mode, (String)"aac3.3.4", (boolean)true)) {
            if (thePlayer.isCollidedHorizontally() && MovementUtils.isMoving()) {
                if (thePlayer.getOnGround() && this.couldStep()) {
                    IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                    iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() * 1.26);
                    IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                    iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() * 1.26);
                    thePlayer.jump();
                    this.isAACStep = true;
                }
                if (this.isAACStep) {
                    IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                    iEntityPlayerSP8.setMotionY(iEntityPlayerSP8.getMotionY() - 0.015);
                    if (!thePlayer.isUsingItem() && thePlayer.getMovementInput().getMoveStrafe() == 0.0f) {
                        thePlayer.setJumpMovementFactor(0.3f);
                    }
                }
            } else {
                this.isAACStep = false;
            }
        }
    }

    @EventTarget
    public final void onMove(MoveEvent event) {
        String mode = (String)this.modeValue.get();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (StringsKt.equals((String)mode, (String)"motionncp", (boolean)true) && thePlayer.isCollidedHorizontally() && !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            if (thePlayer.getOnGround() && this.couldStep()) {
                this.fakeJump();
                thePlayer.setMotionY(0.0);
                event.setY(0.41999998688698);
                this.ncpNextStep = 1;
            } else if (this.ncpNextStep == 1) {
                event.setY(0.33319999363422);
                this.ncpNextStep = 2;
            } else if (this.ncpNextStep == 2) {
                double yaw = MovementUtils.getDirection();
                event.setY(0.24813599859094704);
                MoveEvent moveEvent = event;
                boolean bl = false;
                double d = Math.sin(yaw);
                moveEvent.setX(-d * 0.7);
                moveEvent = event;
                bl = false;
                d = Math.cos(yaw);
                moveEvent.setZ(d * 0.7);
                this.ncpNextStep = 0;
            }
        }
    }

    @EventTarget
    public final void onStep(StepEvent event) {
        String flyMode;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Phase.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) {
            event.setStepHeight(0.0f);
            return;
        }
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.movement.Fly");
        }
        Fly fly = (Fly)module2;
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
            this.isStep = true;
            this.stepX = thePlayer.getPosX();
            this.stepY = thePlayer.getPosY();
            this.stepZ = thePlayer.getPosZ();
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onStepConfirm(StepConfirmEvent event) {
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
                this.spartanSwitch = !this.spartanSwitch;
                this.timer.reset();
            } else if (StringsKt.equals((String)mode, (String)"Rewinside", (boolean)true)) {
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

    @EventTarget(ignoreCondition=true)
    public final void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.isStep && StringsKt.equals((String)((String)this.modeValue.get()), (String)"OldNCP", (boolean)true)) {
            ICPacketPlayer iCPacketPlayer = packet.asCPacketPlayer();
            iCPacketPlayer.setY(iCPacketPlayer.getY() + 0.07);
            this.isStep = false;
        }
    }

    private final void fakeJump() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        thePlayer.setAirBorne(true);
        thePlayer.triggerAchievement(MinecraftInstance.classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    private final boolean couldStep() {
        double yaw = MovementUtils.getDirection();
        boolean bl = false;
        double x = -Math.sin(yaw) * 0.4;
        boolean bl2 = false;
        double z = Math.cos(yaw) * 0.4;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        return iWorldClient.getCollisionBoxes(iEntityPlayerSP.getEntityBoundingBox().offset(x, 1.001335979112147, z)).isEmpty();
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

