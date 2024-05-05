/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
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
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Velocity2", category=ModuleCategory.COMBAT, description="faq")
public final class Velocity2
extends Module {
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
    private final FloatValue verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"JumpFix", "NewHyt", "Jump-MotionY", "GrimAC-FDP", "GrimAC-C03", "NewGrimAC", "Custom", "Simple", "AACPush", "AACZero", "Reverse", "SmoothReverse", "Jump", "AAC5Reduce", "HytPacketA", "Glitch", "HytTick", "Vanilla", "HytTest", "HytNewTest", "HytPacket", "NewAAC4", "FeiLe", "HytMotion", "NewHytMotion", "HytPacketB", "HytMotionB", "HytPacketFix", "LatestTestHyt", "Grim-Motion", "GrimReduce"}, "Vanilla");
    private final FloatValue aac4XZReducerValue = new FloatValue("AAC4XZReducer", 1.36f, 1.0f, 3.0f);
    private final FloatValue newaac4XZReducerValue = new FloatValue("NewAAC4XZReducer", 0.45f, 0.0f, 1.0f);
    private final IntegerValue velocityTickValue = new IntegerValue("VelocityTick", 1, 0, 10);
    private final FloatValue reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f);
    private final FloatValue reverse2StrengthValue = new FloatValue("SmoothReverseStrength", 0.05f, 0.02f, 0.1f);
    private final FloatValue hytpacketaset = new FloatValue("HytPacketASet", 0.35f, 0.1f, 1.0f);
    private final FloatValue hytpacketbset = new FloatValue("HytPacketBSet", 0.5f, 1.0f, 1.0f);
    private final FloatValue aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f);
    private final BoolValue aacPushYReducerValue = new BoolValue("AACPushYReducer", true);
    private IBlock block;
    private final BoolValue noFireValue = new BoolValue("noFire", false);
    private final BoolValue cobwebValue = new BoolValue("NoCobweb", true);
    private final BoolValue hytGround = new BoolValue("HytOnlyGround", true);
    private final FloatValue customX = new FloatValue("CustomX", 0.0f, 0.0f, 1.0f);
    private final BoolValue customYStart = new BoolValue("CanCustomY", false);
    private final FloatValue customY = new FloatValue("CustomY", 1.0f, 1.0f, 2.0f);
    private final FloatValue customZ = new FloatValue("CustomZ", 0.0f, 0.0f, 1.0f);
    private final BoolValue customC06FakeLag = new BoolValue("CustomC06FakeLag", false);
    private int cancelPacket = 6;
    private int resetPersec = 8;
    private int grimTCancel;
    private int updates;
    private MSTimer velocityTimer = new MSTimer();
    private boolean velocityInput;
    private boolean canCleanJump;
    private int velocityTick;
    private boolean reverseHurt;
    private boolean jump;
    private boolean canCancelJump;

    public final IBlock getBlock() {
        return this.block;
    }

    public final void setBlock(@Nullable IBlock iBlock) {
        this.block = iBlock;
    }

    public final int getCancelPacket() {
        return this.cancelPacket;
    }

    public final void setCancelPacket(int n) {
        this.cancelPacket = n;
    }

    public final int getResetPersec() {
        return this.resetPersec;
    }

    public final void setResetPersec(int n) {
        this.resetPersec = n;
    }

    public final int getGrimTCancel() {
        return this.grimTCancel;
    }

    public final void setGrimTCancel(int n) {
        this.grimTCancel = n;
    }

    public final int getUpdates() {
        return this.updates;
    }

    public final void setUpdates(int n) {
        this.updates = n;
    }

    @Override
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

    @Override
    public void onEnable() {
        this.updates = 0;
        super.onEnable();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        int n = this.updates;
        this.updates = n + 1;
        if (this.resetPersec > 0 && (this.updates >= 0 || this.updates >= this.resetPersec)) {
            this.updates = 0;
            if (this.grimTCancel > 0) {
                n = this.grimTCancel;
                this.grimTCancel = n + -1;
            }
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb()) {
            return;
        }
        if (((Boolean)this.noFireValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP2.isBurning()) {
                return;
            }
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "jumpfix": {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP3.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP4.getHurtTime() <= 0) break;
                MinecraftInstance.mc.getGameSettings().getKeyBindJump().onTick(MinecraftInstance.mc.getGameSettings().getKeyBindJump().getKeyCode());
                break;
            }
            case "jump-motiony": {
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP5.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP6.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP7.setMotionY(0.42);
                break;
            }
            case "newhyt": {
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP8.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP9.getHurtTime() > 0) {
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP10.getMotionX() != 0.0) {
                        IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP11 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iEntityPlayerSP11.getMotionZ() != 0.0) {
                            IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP12 == null) {
                                Intrinsics.throwNpe();
                            }
                            iEntityPlayerSP12.setOnGround(true);
                        }
                    }
                }
                IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP13 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP13.getHurtResistantTime() > 0) {
                    IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP14 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP15 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP14.setMotionY(iEntityPlayerSP15.getMotionY() * 0.98);
                }
                IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP16 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP16.getHurtResistantTime() < 19) break;
                IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP17 == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP18 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP17.setMotionX(iEntityPlayerSP18.getMotionX() / (double)1.5f);
                IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP19 == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP20 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP19.setMotionZ(iEntityPlayerSP20.getMotionZ() / (double)1.6f);
                break;
            }
            case "newgrimac": {
                if (thePlayer.getHurtTime() > 0) {
                    IEntityPlayerSP iEntityPlayerSP21 = thePlayer;
                    iEntityPlayerSP21.setMotionX(iEntityPlayerSP21.getMotionX() + -1.0E-7);
                    IEntityPlayerSP iEntityPlayerSP22 = thePlayer;
                    iEntityPlayerSP22.setMotionZ(iEntityPlayerSP22.getMotionZ() + -1.0E-7);
                }
                if (thePlayer.getHurtTime() <= 5) break;
                IEntityPlayerSP iEntityPlayerSP23 = thePlayer;
                iEntityPlayerSP23.setMotionX(iEntityPlayerSP23.getMotionX() + -1.5E-7);
                IEntityPlayerSP iEntityPlayerSP24 = thePlayer;
                iEntityPlayerSP24.setMotionZ(iEntityPlayerSP24.getMotionZ() + -1.5E-7);
                break;
            }
            case "grimreduce": {
                if (thePlayer.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP25 = thePlayer;
                iEntityPlayerSP25.setMotionX(iEntityPlayerSP25.getMotionX() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP26 = thePlayer;
                iEntityPlayerSP26.setMotionZ(iEntityPlayerSP26.getMotionZ() + -1.0E-7);
                thePlayer.setAirBorne(true);
                break;
            }
            case "grim-motion": {
                if (thePlayer.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP27 = thePlayer;
                iEntityPlayerSP27.setMotionX(iEntityPlayerSP27.getMotionX() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP28 = thePlayer;
                iEntityPlayerSP28.setMotionZ(iEntityPlayerSP28.getMotionZ() + -1.2E-7);
                thePlayer.setAirBorne(true);
                break;
            }
            case "jump": {
                if (thePlayer.getHurtTime() <= 0 || !thePlayer.getOnGround()) break;
                thePlayer.setMotionY(0.42);
                float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP29 = thePlayer;
                double d = iEntityPlayerSP29.getMotionX();
                IEntityPlayerSP iEntityPlayerSP30 = iEntityPlayerSP29;
                boolean bl2 = false;
                float f = (float)Math.sin(yaw);
                iEntityPlayerSP30.setMotionX(d - (double)f * 0.2);
                IEntityPlayerSP iEntityPlayerSP31 = thePlayer;
                d = iEntityPlayerSP31.getMotionZ();
                iEntityPlayerSP30 = iEntityPlayerSP31;
                bl2 = false;
                f = (float)Math.cos(yaw);
                iEntityPlayerSP30.setMotionZ(d + (double)f * 0.2);
                break;
            }
            case "latesttesthyt": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP32 = thePlayer;
                    iEntityPlayerSP32.setMotionX(iEntityPlayerSP32.getMotionX() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP33 = thePlayer;
                    iEntityPlayerSP33.setMotionZ(iEntityPlayerSP33.getMotionZ() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP34 = thePlayer;
                    iEntityPlayerSP34.setMotionY(iEntityPlayerSP34.getMotionY() * 0.001);
                    IEntityPlayerSP iEntityPlayerSP35 = thePlayer;
                    iEntityPlayerSP35.setMotionY(iEntityPlayerSP35.getMotionY() / (double)0.01f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP36 = thePlayer;
                iEntityPlayerSP36.setMotionX(iEntityPlayerSP36.getMotionX() * 0.35);
                IEntityPlayerSP iEntityPlayerSP37 = thePlayer;
                iEntityPlayerSP37.setMotionZ(iEntityPlayerSP37.getMotionZ() * 0.35);
                IEntityPlayerSP iEntityPlayerSP38 = thePlayer;
                iEntityPlayerSP38.setMotionY(iEntityPlayerSP38.getMotionY() * 0.001);
                IEntityPlayerSP iEntityPlayerSP39 = thePlayer;
                iEntityPlayerSP39.setMotionY(iEntityPlayerSP39.getMotionY() / (double)0.01f);
                break;
            }
            case "glitch": {
                thePlayer.setNoClip(this.velocityInput);
                if (thePlayer.getHurtTime() == 7) {
                    thePlayer.setMotionY(0.4);
                }
                this.velocityInput = false;
                break;
            }
            case "feile": {
                if (!thePlayer.getOnGround()) break;
                this.canCleanJump = true;
                thePlayer.setMotionY(1.5);
                thePlayer.setMotionZ(1.2);
                thePlayer.setMotionX(1.5);
                if (!thePlayer.getOnGround() || this.velocityTick <= 2) break;
                this.velocityInput = false;
                break;
            }
            case "aac5reduce": {
                if (thePlayer.getHurtTime() > 1 && this.velocityInput) {
                    IEntityPlayerSP iEntityPlayerSP40 = thePlayer;
                    iEntityPlayerSP40.setMotionX(iEntityPlayerSP40.getMotionX() * 0.81);
                    IEntityPlayerSP iEntityPlayerSP41 = thePlayer;
                    iEntityPlayerSP41.setMotionZ(iEntityPlayerSP41.getMotionZ() * 0.81);
                }
                if (!this.velocityInput || thePlayer.getHurtTime() >= 5 && !thePlayer.getOnGround() || !this.velocityTimer.hasTimePassed(120L)) break;
                this.velocityInput = false;
                break;
            }
            case "hyttick": {
                if (this.velocityTick > ((Number)this.velocityTickValue.get()).intValue()) {
                    if (thePlayer.getMotionY() > 0.0) {
                        thePlayer.setMotionY(0.0);
                    }
                    thePlayer.setMotionX(0.0);
                    thePlayer.setMotionZ(0.0);
                    thePlayer.setJumpMovementFactor(-1.0E-5f);
                    this.velocityInput = false;
                }
                if (!thePlayer.getOnGround() || this.velocityTick <= 1) break;
                this.velocityInput = false;
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
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                break;
            }
            case "newaac4": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.getOnGround()) break;
                float reduce = ((Number)this.newaac4XZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP42 = thePlayer;
                iEntityPlayerSP42.setMotionX(iEntityPlayerSP42.getMotionX() * (double)reduce);
                IEntityPlayerSP iEntityPlayerSP43 = thePlayer;
                iEntityPlayerSP43.setMotionZ(iEntityPlayerSP43.getMotionZ() * (double)reduce);
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    thePlayer.setSpeedInAir(0.02f);
                    return;
                }
                if (thePlayer.getHurtTime() > 0) {
                    this.reverseHurt = true;
                }
                if (!thePlayer.getOnGround()) {
                    if (!this.reverseHurt) break;
                    thePlayer.setSpeedInAir(((Number)this.reverse2StrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                this.reverseHurt = false;
                break;
            }
            case "hytpacket": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP44 = thePlayer;
                    iEntityPlayerSP44.setMotionX(iEntityPlayerSP44.getMotionX() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP45 = thePlayer;
                    iEntityPlayerSP45.setMotionZ(iEntityPlayerSP45.getMotionZ() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP46 = thePlayer;
                    iEntityPlayerSP46.setMotionY(iEntityPlayerSP46.getMotionY() / (double)1.781145f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP47 = thePlayer;
                iEntityPlayerSP47.setMotionX(iEntityPlayerSP47.getMotionX() * 0.5);
                IEntityPlayerSP iEntityPlayerSP48 = thePlayer;
                iEntityPlayerSP48.setMotionZ(iEntityPlayerSP48.getMotionZ() * 0.5);
                IEntityPlayerSP iEntityPlayerSP49 = thePlayer;
                iEntityPlayerSP49.setMotionY(iEntityPlayerSP49.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotion": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP50 = thePlayer;
                    iEntityPlayerSP50.setMotionX(iEntityPlayerSP50.getMotionX() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP51 = thePlayer;
                    iEntityPlayerSP51.setMotionZ(iEntityPlayerSP51.getMotionZ() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP52 = thePlayer;
                    iEntityPlayerSP52.setMotionY(iEntityPlayerSP52.getMotionY() * (double)0.381145f);
                    IEntityPlayerSP iEntityPlayerSP53 = thePlayer;
                    iEntityPlayerSP53.setMotionY(iEntityPlayerSP53.getMotionY() / (double)1.781145f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP54 = thePlayer;
                iEntityPlayerSP54.setMotionX(iEntityPlayerSP54.getMotionX() * 0.4);
                IEntityPlayerSP iEntityPlayerSP55 = thePlayer;
                iEntityPlayerSP55.setMotionZ(iEntityPlayerSP55.getMotionZ() * 0.4);
                IEntityPlayerSP iEntityPlayerSP56 = thePlayer;
                iEntityPlayerSP56.setMotionY(iEntityPlayerSP56.getMotionY() * (double)0.381145f);
                IEntityPlayerSP iEntityPlayerSP57 = thePlayer;
                iEntityPlayerSP57.setMotionY(iEntityPlayerSP57.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotionb": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getOnGround() || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP58 = thePlayer;
                iEntityPlayerSP58.setMotionX(iEntityPlayerSP58.getMotionX() * (double)0.451145f);
                IEntityPlayerSP iEntityPlayerSP59 = thePlayer;
                iEntityPlayerSP59.setMotionZ(iEntityPlayerSP59.getMotionZ() * (double)0.451145f);
                break;
            }
            case "newhytmotion": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getOnGround()) break;
                if (!thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                    IEntityPlayerSP iEntityPlayerSP60 = thePlayer;
                    iEntityPlayerSP60.setMotionX(iEntityPlayerSP60.getMotionX() * 0.47188);
                    IEntityPlayerSP iEntityPlayerSP61 = thePlayer;
                    iEntityPlayerSP61.setMotionZ(iEntityPlayerSP61.getMotionZ() * 0.47188);
                    if (thePlayer.getMotionY() != 0.42 && !(thePlayer.getMotionY() > 0.42)) break;
                    IEntityPlayerSP iEntityPlayerSP62 = thePlayer;
                    iEntityPlayerSP62.setMotionY(iEntityPlayerSP62.getMotionY() * 0.4);
                    break;
                }
                IEntityPlayerSP iEntityPlayerSP63 = thePlayer;
                iEntityPlayerSP63.setMotionX(iEntityPlayerSP63.getMotionX() * 0.65025);
                IEntityPlayerSP iEntityPlayerSP64 = thePlayer;
                iEntityPlayerSP64.setMotionZ(iEntityPlayerSP64.getMotionZ() * 0.65025);
                if (thePlayer.getMotionY() != 0.42 && !(thePlayer.getMotionY() > 0.42)) break;
                IEntityPlayerSP iEntityPlayerSP65 = thePlayer;
                iEntityPlayerSP65.setMotionY(iEntityPlayerSP65.getMotionY() * 0.4);
                break;
            }
            case "aacpush": {
                if (this.jump) {
                    if (thePlayer.getOnGround()) {
                        this.jump = false;
                    }
                } else {
                    if (thePlayer.getHurtTime() > 0 && thePlayer.getMotionX() != 0.0 && thePlayer.getMotionZ() != 0.0) {
                        thePlayer.setOnGround(true);
                    }
                    if (thePlayer.getHurtResistantTime() > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue()) {
                        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                        if (module == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module.getState()) {
                            IEntityPlayerSP iEntityPlayerSP66 = thePlayer;
                            iEntityPlayerSP66.setMotionY(iEntityPlayerSP66.getMotionY() - 0.014999993);
                        }
                    }
                }
                if (thePlayer.getHurtResistantTime() < 19) break;
                float reduce = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP67 = thePlayer;
                iEntityPlayerSP67.setMotionX(iEntityPlayerSP67.getMotionX() / (double)reduce);
                IEntityPlayerSP iEntityPlayerSP68 = thePlayer;
                iEntityPlayerSP68.setMotionZ(iEntityPlayerSP68.getMotionZ() / (double)reduce);
                break;
            }
            case "custom": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead()) break;
                IEntityPlayerSP iEntityPlayerSP69 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP69 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP69.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP70 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP70 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP70.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP71 = thePlayer;
                iEntityPlayerSP71.setMotionX(iEntityPlayerSP71.getMotionX() * ((Number)this.customX.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP72 = thePlayer;
                iEntityPlayerSP72.setMotionZ(iEntityPlayerSP72.getMotionZ() * ((Number)this.customZ.get()).doubleValue());
                if (((Boolean)this.customYStart.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP73 = thePlayer;
                    iEntityPlayerSP73.setMotionY(iEntityPlayerSP73.getMotionY() / ((Number)this.customY.get()).doubleValue());
                }
                if (!((Boolean)this.customC06FakeLag.get()).booleanValue()) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), thePlayer.getRotationYaw(), thePlayer.getRotationPitch(), thePlayer.getOnGround()));
                break;
            }
            case "aaczero": {
                if (thePlayer.getHurtTime() > 0) {
                    if (!this.velocityInput || thePlayer.getOnGround() || thePlayer.getFallDistance() > 2.0f) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP74 = thePlayer;
                    iEntityPlayerSP74.setMotionY(iEntityPlayerSP74.getMotionY() - 1.0);
                    thePlayer.setAirBorne(true);
                    thePlayer.setOnGround(true);
                    break;
                }
                this.velocityInput = false;
            }
        }
    }

    @EventTarget
    public final void onBlockBB(BlockBBEvent event) {
        this.block = event.getBlock();
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntityVelocity(packet)) {
            Object object;
            ISPacketEntityVelocity packetEntityVelocity = packet.asSPacketEntityVelocity();
            if (((Boolean)this.noFireValue.get()).booleanValue()) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP2.isBurning()) {
                    return;
                }
            }
            if ((object = MinecraftInstance.mc.getTheWorld()) == null || (object = object.getEntityByID(packetEntityVelocity.getEntityID())) == null) {
                return;
            }
            if (object.equals(thePlayer) ^ true) {
                return;
            }
            this.velocityTimer.reset();
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string2.toLowerCase()) {
                case "grimac-c03": {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead()) break;
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP4.isInWater()) break;
                    if (MinecraftInstance.classProvider.isCPacketPlayer(packet)) {
                        event.cancelEvent();
                    }
                    ISPacketEntityVelocity iSPacketEntityVelocity = packetEntityVelocity;
                    iSPacketEntityVelocity.setMotionX(iSPacketEntityVelocity.getMotionX() * 0);
                    ISPacketEntityVelocity iSPacketEntityVelocity2 = packetEntityVelocity;
                    iSPacketEntityVelocity2.setMotionY(iSPacketEntityVelocity2.getMotionY() * 0);
                    ISPacketEntityVelocity iSPacketEntityVelocity3 = packetEntityVelocity;
                    iSPacketEntityVelocity3.setMotionZ(iSPacketEntityVelocity3.getMotionZ() * 0);
                    break;
                }
                case "grimac-fdp": {
                    ISPacketEntityVelocity packetentityvelocity = packet.asSPacketEntityVelocity();
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead()) break;
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP5.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP6.isInWater()) break;
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP7.getOnGround()) break;
                    if (packet instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                        event.cancelEvent();
                        this.grimTCancel = this.cancelPacket;
                    }
                    int n = packetentityvelocity.getEntityID();
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (n != iEntityPlayerSP8.getEntityId()) break;
                    event.cancelEvent();
                    int n2 = this.grimTCancel;
                    this.grimTCancel = n2 + -1;
                    break;
                }
                case "vanilla": {
                    event.cancelEvent();
                    break;
                }
                case "simple": {
                    float horizontal = ((Number)this.horizontalValue.get()).floatValue();
                    float vertical = ((Number)this.verticalValue.get()).floatValue();
                    if (horizontal == 0.0f && vertical == 0.0f) {
                        event.cancelEvent();
                    }
                    packetEntityVelocity.setMotionX((int)((float)packetEntityVelocity.getMotionX() * horizontal));
                    packetEntityVelocity.setMotionY((int)((float)packetEntityVelocity.getMotionY() * vertical));
                    packetEntityVelocity.setMotionZ((int)((float)packetEntityVelocity.getMotionZ() * horizontal));
                    break;
                }
                case "hytpacketfix": {
                    if (thePlayer.getHurtTime() > 0 && !thePlayer.isDead()) {
                        IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP9 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!iEntityPlayerSP9.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                            IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP10 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!iEntityPlayerSP10.isInWater()) {
                                IEntityPlayerSP iEntityPlayerSP11 = thePlayer;
                                iEntityPlayerSP11.setMotionX(iEntityPlayerSP11.getMotionX() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP12 = thePlayer;
                                iEntityPlayerSP12.setMotionZ(iEntityPlayerSP12.getMotionZ() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP13 = thePlayer;
                                iEntityPlayerSP13.setMotionY(iEntityPlayerSP13.getMotionY() / (double)1.45f);
                            }
                        }
                    }
                    if (thePlayer.getHurtTime() < 2) {
                        packetEntityVelocity.setMotionY(0);
                    }
                    if (thePlayer.getHurtTime() >= 6) break;
                    packetEntityVelocity.setMotionX(0);
                    packetEntityVelocity.setMotionZ(0);
                    break;
                }
                case "hyttest": {
                    if (!thePlayer.getOnGround()) break;
                    this.canCancelJump = false;
                    packetEntityVelocity.setMotionX((int)0.985114);
                    packetEntityVelocity.setMotionY((int)0.885113);
                    packetEntityVelocity.setMotionZ((int)0.785112);
                    IEntityPlayerSP iEntityPlayerSP14 = thePlayer;
                    iEntityPlayerSP14.setMotionX(iEntityPlayerSP14.getMotionX() / 1.75);
                    IEntityPlayerSP iEntityPlayerSP15 = thePlayer;
                    iEntityPlayerSP15.setMotionZ(iEntityPlayerSP15.getMotionZ() / 1.75);
                    break;
                }
                case "hytnewtest": {
                    if (!thePlayer.getOnGround()) break;
                    this.velocityInput = true;
                    float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                    packetEntityVelocity.setMotionX((int)((double)packetEntityVelocity.getMotionX() * 0.75));
                    packetEntityVelocity.setMotionZ((int)((double)packetEntityVelocity.getMotionZ() * 0.75));
                    IEntityPlayerSP iEntityPlayerSP16 = thePlayer;
                    double d = iEntityPlayerSP16.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP17 = iEntityPlayerSP16;
                    boolean vertical = false;
                    float f = (float)Math.sin(yaw);
                    iEntityPlayerSP17.setMotionX(d - (double)f * 0.2);
                    IEntityPlayerSP iEntityPlayerSP18 = thePlayer;
                    d = iEntityPlayerSP18.getMotionZ();
                    iEntityPlayerSP17 = iEntityPlayerSP18;
                    vertical = false;
                    f = (float)Math.cos(yaw);
                    iEntityPlayerSP17.setMotionZ(d + (double)f * 0.2);
                    break;
                }
                case "hytpacketa": {
                    packetEntityVelocity.setMotionX((int)((double)((float)packetEntityVelocity.getMotionX() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    packetEntityVelocity.setMotionY((int)0.7);
                    packetEntityVelocity.setMotionZ((int)((double)((float)packetEntityVelocity.getMotionZ() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    event.cancelEvent();
                    break;
                }
                case "hytpacketb": {
                    packetEntityVelocity.setMotionX((int)((double)((float)packetEntityVelocity.getMotionX() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    packetEntityVelocity.setMotionY((int)((double)((float)packetEntityVelocity.getMotionY() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    packetEntityVelocity.setMotionZ((int)((double)((float)packetEntityVelocity.getMotionZ() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    break;
                }
                case "aac": 
                case "aaczero": 
                case "reverse": 
                case "aac4": 
                case "smoothreverse": 
                case "aac5reduce": {
                    this.velocityInput = true;
                    break;
                }
                case "hyttick": {
                    this.velocityInput = true;
                    float horizontal = 0.0f;
                    float vertical = 0.0f;
                    event.cancelEvent();
                    break;
                }
                case "glitch": {
                    if (!thePlayer.getOnGround()) {
                        return;
                    }
                    this.velocityInput = true;
                    event.cancelEvent();
                }
            }
        }
    }

    @EventTarget
    public final void onMove(MoveEvent event) {
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case 477609814: {
                if (!string.equals("grimac-fdp")) break;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP2.isDead()) break;
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP4.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP5.getOnGround()) break;
                event.zero();
            }
        }
    }

    @EventTarget
    public final void onJump(JumpEvent event) {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb()) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "aacpush": {
                this.jump = true;
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
            }
        }
    }
}

