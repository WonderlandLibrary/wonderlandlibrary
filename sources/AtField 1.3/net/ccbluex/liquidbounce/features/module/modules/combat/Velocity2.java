/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.Objects;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.VelocityPlus;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Velocity2", description="Edit your velocity", category=ModuleCategory.COMBAT)
public final class Velocity2
extends Module {
    private boolean reverseHurt;
    private final FloatValue customY;
    private final BoolValue cobwebValue;
    private final BoolValue customC06FakeLag;
    private final BoolValue noFireValue;
    private boolean canCancelJump;
    private final FloatValue aac4XZReducerValue;
    private int velocityTick;
    private MSTimer velocityTimer;
    private final FloatValue hytpacketbset;
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
    private final FloatValue reverse2StrengthValue;
    private final BoolValue hytGround;
    private final ListValue modeValue;
    private final BoolValue customYStart;
    private final FloatValue newaac4XZReducerValue;
    private final BoolValue aacPushYReducerValue;
    private boolean jump;
    private final IntegerValue velocityTickValue;
    private final FloatValue reverseStrengthValue;
    private IBlock block;
    private final FloatValue customZ;
    private final FloatValue aacPushXZReducerValue;
    private boolean canCleanJump;
    private final FloatValue verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
    private final FloatValue customX;
    private boolean velocityInput;
    private final FloatValue hytpacketaset;

    public Velocity2() {
        this.modeValue = new ListValue("Mode", new String[]{"NewGrimAC", "Custom", "AAC4", "Simple", "AAC", "AACPush", "AACZero", "Reverse", "SmoothReverse", "Jump", "AAC5Reduce", "HytPacketA", "Glitch", "HytCancel", "HytTick", "Vanilla", "HytTest", "HytNewTest", "HytPacket", "NewAAC4", "FeiLe", "HytMotion", "NewHytMotion", "HytPacketB", "HytMotionB", "HytPacketFix", "S27", "LatestTestHyt", "Grim-Motion", "GrimReduce"}, "Vanilla");
        this.aac4XZReducerValue = new FloatValue("AAC4XZReducer", 1.36f, 1.0f, 3.0f);
        this.newaac4XZReducerValue = new FloatValue("NewAAC4XZReducer", 0.45f, 0.0f, 1.0f);
        this.velocityTickValue = new IntegerValue("VelocityTick", 1, 0, 10);
        this.reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f);
        this.reverse2StrengthValue = new FloatValue("SmoothReverseStrength", 0.05f, 0.02f, 0.1f);
        this.hytpacketaset = new FloatValue("HytPacketASet", 0.35f, 0.1f, 1.0f);
        this.hytpacketbset = new FloatValue("HytPacketBSet", 0.5f, 1.0f, 1.0f);
        this.aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f);
        this.aacPushYReducerValue = new BoolValue("AACPushYReducer", true);
        this.noFireValue = new BoolValue("noFire", false);
        this.cobwebValue = new BoolValue("NoCobweb", true);
        this.hytGround = new BoolValue("HytOnlyGround", true);
        this.customX = new FloatValue("CustomX", 0.0f, 0.0f, 1.0f);
        this.customYStart = new BoolValue("CanCustomY", false);
        this.customY = new FloatValue("CustomY", 1.0f, 1.0f, 2.0f);
        this.customZ = new FloatValue("CustomZ", 0.0f, 0.0f, 1.0f);
        this.customC06FakeLag = new BoolValue("CustomC06FakeLag", false);
        this.velocityTimer = new MSTimer();
    }

    @EventTarget
    public final void onBlockBB(BlockBBEvent blockBBEvent) {
        this.block = blockBBEvent.getBlock();
    }

    public final IBlock getBlock() {
        return this.block;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        String string;
        Object object;
        IEntityPlayerSP iEntityPlayerSP = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isInLava() || iEntityPlayerSP2.isInWeb()) {
            return;
        }
        if (((Boolean)this.noFireValue.get()).booleanValue()) {
            object = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
            if (object == null) {
                Intrinsics.throwNpe();
            }
            if (object.isBurning()) {
                return;
            }
        }
        object = (String)this.modeValue.get();
        boolean bl = false;
        Object object2 = object;
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string = ((String)object2).toLowerCase()) {
            case "newgrimac": {
                IEntityPlayerSP iEntityPlayerSP3;
                IEntityPlayerSP iEntityPlayerSP4;
                if (iEntityPlayerSP2.getHurtTime() > 0) {
                    iEntityPlayerSP4 = iEntityPlayerSP2;
                    iEntityPlayerSP4.setMotionX(iEntityPlayerSP4.getMotionX() + -1.0E-7);
                    iEntityPlayerSP3 = iEntityPlayerSP2;
                    iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() + -1.0E-7);
                }
                if (iEntityPlayerSP2.getHurtTime() <= 5) break;
                iEntityPlayerSP4 = iEntityPlayerSP2;
                iEntityPlayerSP4.setMotionX(iEntityPlayerSP4.getMotionX() + -1.1E-7);
                iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() + -1.2E-7);
                break;
            }
            case "grimreduce": {
                if (iEntityPlayerSP2.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
                iEntityPlayerSP6.setMotionY(iEntityPlayerSP6.getMotionY() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() + -1.0E-7);
                iEntityPlayerSP2.setAirBorne(true);
                break;
            }
            case "grim-motion": {
                if (iEntityPlayerSP2.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP2;
                iEntityPlayerSP9.setMotionY(iEntityPlayerSP9.getMotionY() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP10 = iEntityPlayerSP2;
                iEntityPlayerSP10.setMotionZ(iEntityPlayerSP10.getMotionZ() + -1.2E-7);
                iEntityPlayerSP2.setAirBorne(true);
                break;
            }
            case "jump": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || !iEntityPlayerSP2.getOnGround()) break;
                iEntityPlayerSP2.setMotionY(0.42);
                float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP11 = iEntityPlayerSP2;
                double d = iEntityPlayerSP11.getMotionX();
                IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP11;
                boolean bl2 = false;
                float f2 = (float)Math.sin(f);
                iEntityPlayerSP12.setMotionX(d - (double)f2 * 0.2);
                IEntityPlayerSP iEntityPlayerSP13 = iEntityPlayerSP2;
                d = iEntityPlayerSP13.getMotionZ();
                iEntityPlayerSP12 = iEntityPlayerSP13;
                bl2 = false;
                f2 = (float)Math.cos(f);
                iEntityPlayerSP12.setMotionZ(d + (double)f2 * 0.2);
                break;
            }
            case "latesttesthyt": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getHurtTime() > 5 || !iEntityPlayerSP2.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP14 = iEntityPlayerSP2;
                    iEntityPlayerSP14.setMotionX(iEntityPlayerSP14.getMotionX() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP15 = iEntityPlayerSP2;
                    iEntityPlayerSP15.setMotionZ(iEntityPlayerSP15.getMotionZ() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP16 = iEntityPlayerSP2;
                    iEntityPlayerSP16.setMotionY(iEntityPlayerSP16.getMotionY() * 0.001);
                    IEntityPlayerSP iEntityPlayerSP17 = iEntityPlayerSP2;
                    iEntityPlayerSP17.setMotionY(iEntityPlayerSP17.getMotionY() / (double)0.01f);
                    break;
                }
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP18 = iEntityPlayerSP2;
                iEntityPlayerSP18.setMotionX(iEntityPlayerSP18.getMotionX() * 0.35);
                IEntityPlayerSP iEntityPlayerSP19 = iEntityPlayerSP2;
                iEntityPlayerSP19.setMotionZ(iEntityPlayerSP19.getMotionZ() * 0.35);
                IEntityPlayerSP iEntityPlayerSP20 = iEntityPlayerSP2;
                iEntityPlayerSP20.setMotionY(iEntityPlayerSP20.getMotionY() * 0.001);
                IEntityPlayerSP iEntityPlayerSP21 = iEntityPlayerSP2;
                iEntityPlayerSP21.setMotionY(iEntityPlayerSP21.getMotionY() / (double)0.01f);
                break;
            }
            case "glitch": {
                iEntityPlayerSP2.setNoClip(this.velocityInput);
                if (iEntityPlayerSP2.getHurtTime() == 7) {
                    iEntityPlayerSP2.setMotionY(0.4);
                }
                this.velocityInput = false;
                break;
            }
            case "feile": {
                if (!iEntityPlayerSP2.getOnGround()) break;
                this.canCleanJump = true;
                iEntityPlayerSP2.setMotionY(1.5);
                iEntityPlayerSP2.setMotionZ(1.2);
                iEntityPlayerSP2.setMotionX(1.5);
                if (!iEntityPlayerSP2.getOnGround() || this.velocityTick <= 2) break;
                this.velocityInput = false;
                break;
            }
            case "aac5reduce": {
                if (iEntityPlayerSP2.getHurtTime() > 1 && this.velocityInput) {
                    IEntityPlayerSP iEntityPlayerSP22 = iEntityPlayerSP2;
                    iEntityPlayerSP22.setMotionX(iEntityPlayerSP22.getMotionX() * 0.81);
                    IEntityPlayerSP iEntityPlayerSP23 = iEntityPlayerSP2;
                    iEntityPlayerSP23.setMotionZ(iEntityPlayerSP23.getMotionZ() * 0.81);
                }
                if (!this.velocityInput || iEntityPlayerSP2.getHurtTime() >= 5 && !iEntityPlayerSP2.getOnGround() || !this.velocityTimer.hasTimePassed(120L)) break;
                this.velocityInput = false;
                break;
            }
            case "hyttick": {
                if (this.velocityTick > ((Number)this.velocityTickValue.get()).intValue()) {
                    if (iEntityPlayerSP2.getMotionY() > 0.0) {
                        iEntityPlayerSP2.setMotionY(0.0);
                    }
                    iEntityPlayerSP2.setMotionX(0.0);
                    iEntityPlayerSP2.setMotionZ(0.0);
                    iEntityPlayerSP2.setJumpMovementFactor(-1.0E-5f);
                    this.velocityInput = false;
                }
                if (!iEntityPlayerSP2.getOnGround() || this.velocityTick <= 1) break;
                this.velocityInput = false;
                break;
            }
            case "reverse": {
                if (!this.velocityInput) {
                    return;
                }
                if (!iEntityPlayerSP2.getOnGround()) {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * ((Number)this.reverseStrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                break;
            }
            case "aac4": {
                if (!iEntityPlayerSP2.getOnGround()) {
                    if (!this.velocityInput) break;
                    iEntityPlayerSP2.setSpeedInAir(0.02f);
                    IEntityPlayerSP iEntityPlayerSP24 = iEntityPlayerSP2;
                    iEntityPlayerSP24.setMotionX(iEntityPlayerSP24.getMotionX() * 0.6);
                    IEntityPlayerSP iEntityPlayerSP25 = iEntityPlayerSP2;
                    iEntityPlayerSP25.setMotionZ(iEntityPlayerSP25.getMotionZ() * 0.6);
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                iEntityPlayerSP2.setSpeedInAir(0.02f);
                break;
            }
            case "newaac4": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.getOnGround()) break;
                float f = ((Number)this.newaac4XZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP26 = iEntityPlayerSP2;
                iEntityPlayerSP26.setMotionX(iEntityPlayerSP26.getMotionX() * (double)f);
                IEntityPlayerSP iEntityPlayerSP27 = iEntityPlayerSP2;
                iEntityPlayerSP27.setMotionZ(iEntityPlayerSP27.getMotionZ() * (double)f);
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    iEntityPlayerSP2.setSpeedInAir(0.02f);
                    return;
                }
                if (iEntityPlayerSP2.getHurtTime() > 0) {
                    this.reverseHurt = true;
                }
                if (!iEntityPlayerSP2.getOnGround()) {
                    if (!this.reverseHurt) break;
                    iEntityPlayerSP2.setSpeedInAir(((Number)this.reverse2StrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                this.reverseHurt = false;
                break;
            }
            case "aac": {
                if (!this.velocityInput || !this.velocityTimer.hasTimePassed(80L)) break;
                IEntityPlayerSP iEntityPlayerSP28 = iEntityPlayerSP2;
                iEntityPlayerSP28.setMotionX(iEntityPlayerSP28.getMotionX() * ((Number)this.horizontalValue.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP29 = iEntityPlayerSP2;
                iEntityPlayerSP29.setMotionZ(iEntityPlayerSP29.getMotionZ() * ((Number)this.horizontalValue.get()).doubleValue());
                this.velocityInput = false;
                break;
            }
            case "hytpacket": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getHurtTime() > 5 || !iEntityPlayerSP2.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP30 = iEntityPlayerSP2;
                    iEntityPlayerSP30.setMotionX(iEntityPlayerSP30.getMotionX() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP31 = iEntityPlayerSP2;
                    iEntityPlayerSP31.setMotionZ(iEntityPlayerSP31.getMotionZ() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP32 = iEntityPlayerSP2;
                    iEntityPlayerSP32.setMotionY(iEntityPlayerSP32.getMotionY() / (double)1.781145f);
                    break;
                }
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP33 = iEntityPlayerSP2;
                iEntityPlayerSP33.setMotionX(iEntityPlayerSP33.getMotionX() * 0.5);
                IEntityPlayerSP iEntityPlayerSP34 = iEntityPlayerSP2;
                iEntityPlayerSP34.setMotionZ(iEntityPlayerSP34.getMotionZ() * 0.5);
                IEntityPlayerSP iEntityPlayerSP35 = iEntityPlayerSP2;
                iEntityPlayerSP35.setMotionY(iEntityPlayerSP35.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotion": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getHurtTime() > 5 || !iEntityPlayerSP2.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP36 = iEntityPlayerSP2;
                    iEntityPlayerSP36.setMotionX(iEntityPlayerSP36.getMotionX() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP37 = iEntityPlayerSP2;
                    iEntityPlayerSP37.setMotionZ(iEntityPlayerSP37.getMotionZ() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP38 = iEntityPlayerSP2;
                    iEntityPlayerSP38.setMotionY(iEntityPlayerSP38.getMotionY() * (double)0.381145f);
                    IEntityPlayerSP iEntityPlayerSP39 = iEntityPlayerSP2;
                    iEntityPlayerSP39.setMotionY(iEntityPlayerSP39.getMotionY() / (double)1.781145f);
                    break;
                }
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP40 = iEntityPlayerSP2;
                iEntityPlayerSP40.setMotionX(iEntityPlayerSP40.getMotionX() * 0.4);
                IEntityPlayerSP iEntityPlayerSP41 = iEntityPlayerSP2;
                iEntityPlayerSP41.setMotionZ(iEntityPlayerSP41.getMotionZ() * 0.4);
                IEntityPlayerSP iEntityPlayerSP42 = iEntityPlayerSP2;
                iEntityPlayerSP42.setMotionY(iEntityPlayerSP42.getMotionY() * (double)0.381145f);
                IEntityPlayerSP iEntityPlayerSP43 = iEntityPlayerSP2;
                iEntityPlayerSP43.setMotionY(iEntityPlayerSP43.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotionb": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP44 = iEntityPlayerSP2;
                iEntityPlayerSP44.setMotionX(iEntityPlayerSP44.getMotionX() * (double)0.451145f);
                IEntityPlayerSP iEntityPlayerSP45 = iEntityPlayerSP2;
                iEntityPlayerSP45.setMotionZ(iEntityPlayerSP45.getMotionZ() * (double)0.451145f);
                break;
            }
            case "newhytmotion": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getOnGround()) break;
                if (!iEntityPlayerSP2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                    IEntityPlayerSP iEntityPlayerSP46 = iEntityPlayerSP2;
                    iEntityPlayerSP46.setMotionX(iEntityPlayerSP46.getMotionX() * 0.47188);
                    IEntityPlayerSP iEntityPlayerSP47 = iEntityPlayerSP2;
                    iEntityPlayerSP47.setMotionZ(iEntityPlayerSP47.getMotionZ() * 0.47188);
                    if (iEntityPlayerSP2.getMotionY() != 0.42 && !(iEntityPlayerSP2.getMotionY() > 0.42)) break;
                    IEntityPlayerSP iEntityPlayerSP48 = iEntityPlayerSP2;
                    iEntityPlayerSP48.setMotionY(iEntityPlayerSP48.getMotionY() * 0.4);
                    break;
                }
                IEntityPlayerSP iEntityPlayerSP49 = iEntityPlayerSP2;
                iEntityPlayerSP49.setMotionX(iEntityPlayerSP49.getMotionX() * 0.65025);
                IEntityPlayerSP iEntityPlayerSP50 = iEntityPlayerSP2;
                iEntityPlayerSP50.setMotionZ(iEntityPlayerSP50.getMotionZ() * 0.65025);
                if (iEntityPlayerSP2.getMotionY() != 0.42 && !(iEntityPlayerSP2.getMotionY() > 0.42)) break;
                IEntityPlayerSP iEntityPlayerSP51 = iEntityPlayerSP2;
                iEntityPlayerSP51.setMotionY(iEntityPlayerSP51.getMotionY() * 0.4);
                break;
            }
            case "aacpush": {
                IEntityPlayerSP iEntityPlayerSP52;
                if (this.jump) {
                    if (iEntityPlayerSP2.getOnGround()) {
                        this.jump = false;
                    }
                } else {
                    if (iEntityPlayerSP2.getHurtTime() > 0 && iEntityPlayerSP2.getMotionX() != 0.0 && iEntityPlayerSP2.getMotionZ() != 0.0) {
                        iEntityPlayerSP2.setOnGround(true);
                    }
                    if (iEntityPlayerSP2.getHurtResistantTime() > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue()) {
                        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                        if (module == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module.getState()) {
                            iEntityPlayerSP52 = iEntityPlayerSP2;
                            iEntityPlayerSP52.setMotionY(iEntityPlayerSP52.getMotionY() - 0.014999993);
                        }
                    }
                }
                if (iEntityPlayerSP2.getHurtResistantTime() < 19) break;
                float f = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                iEntityPlayerSP52 = iEntityPlayerSP2;
                iEntityPlayerSP52.setMotionX(iEntityPlayerSP52.getMotionX() / (double)f);
                IEntityPlayerSP iEntityPlayerSP53 = iEntityPlayerSP2;
                iEntityPlayerSP53.setMotionZ(iEntityPlayerSP53.getMotionZ() / (double)f);
                break;
            }
            case "custom": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead()) break;
                IEntityPlayerSP iEntityPlayerSP54 = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
                if (iEntityPlayerSP54 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP54.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP55 = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
                if (iEntityPlayerSP55 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP55.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP56 = iEntityPlayerSP2;
                iEntityPlayerSP56.setMotionX(iEntityPlayerSP56.getMotionX() * ((Number)this.customX.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP57 = iEntityPlayerSP2;
                iEntityPlayerSP57.setMotionZ(iEntityPlayerSP57.getMotionZ() * ((Number)this.customZ.get()).doubleValue());
                if (((Boolean)this.customYStart.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP58 = iEntityPlayerSP2;
                    iEntityPlayerSP58.setMotionY(iEntityPlayerSP58.getMotionY() / ((Number)this.customY.get()).doubleValue());
                }
                if (!((Boolean)this.customC06FakeLag.get()).booleanValue()) break;
                VelocityPlus.access$getMc$p$s1046033730().getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ(), iEntityPlayerSP2.getRotationYaw(), iEntityPlayerSP2.getRotationPitch(), iEntityPlayerSP2.getOnGround()));
                break;
            }
            case "aaczero": {
                if (iEntityPlayerSP2.getHurtTime() > 0) {
                    if (!this.velocityInput || iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.getFallDistance() > 2.0f) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP59 = iEntityPlayerSP2;
                    iEntityPlayerSP59.setMotionY(iEntityPlayerSP59.getMotionY() - 1.0);
                    iEntityPlayerSP2.setAirBorne(true);
                    iEntityPlayerSP2.setOnGround(true);
                    break;
                }
                this.velocityInput = false;
            }
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntityPlayerSP iEntityPlayerSP = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntityVelocity(iPacket)) {
            String string;
            Object object;
            Object object2;
            ISPacketEntityVelocity iSPacketEntityVelocity = iPacket.asSPacketEntityVelocity();
            if (((Boolean)this.noFireValue.get()).booleanValue()) {
                object2 = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
                if (object2 == null) {
                    Intrinsics.throwNpe();
                }
                if (object2.isBurning()) {
                    return;
                }
            }
            if ((object = VelocityPlus.access$getMc$p$s1046033730().getTheWorld()) == null || (object = Objects.requireNonNull(MinecraftInstance.mc.getTheWorld()).getEntityByID(iSPacketEntityVelocity.getEntityID())) == null) {
                return;
            }
            if (object.equals(iEntityPlayerSP2) ^ true) {
                return;
            }
            this.velocityTimer.reset();
            object2 = (String)this.modeValue.get();
            boolean bl = false;
            Object object3 = object2;
            if (object3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string = ((String)object3).toLowerCase()) {
                case "vanilla": {
                    packetEvent.cancelEvent();
                    break;
                }
                case "s27": {
                    if (MinecraftInstance.classProvider.isSPacketExplosion(iPacket)) {
                        packetEvent.cancelEvent();
                    }
                    float f = ((Number)this.horizontalValue.get()).floatValue();
                    float f2 = ((Number)this.verticalValue.get()).floatValue();
                    iSPacketEntityVelocity.setMotionX((int)((float)iSPacketEntityVelocity.getMotionX() * f));
                    iSPacketEntityVelocity.setMotionZ((int)((float)iSPacketEntityVelocity.getMotionZ() * f));
                    break;
                }
                case "simple": {
                    float f = ((Number)this.horizontalValue.get()).floatValue();
                    float f3 = ((Number)this.verticalValue.get()).floatValue();
                    if (f == 0.0f && f3 == 0.0f) {
                        packetEvent.cancelEvent();
                    }
                    iSPacketEntityVelocity.setMotionX((int)((float)iSPacketEntityVelocity.getMotionX() * f));
                    iSPacketEntityVelocity.setMotionY((int)((float)iSPacketEntityVelocity.getMotionY() * f3));
                    iSPacketEntityVelocity.setMotionZ((int)((float)iSPacketEntityVelocity.getMotionZ() * f));
                    break;
                }
                case "hytpacketfix": {
                    if (iEntityPlayerSP2.getHurtTime() > 0 && !iEntityPlayerSP2.isDead()) {
                        IEntityPlayerSP iEntityPlayerSP3 = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
                        if (iEntityPlayerSP3 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!iEntityPlayerSP3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                            IEntityPlayerSP iEntityPlayerSP4 = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
                            if (iEntityPlayerSP4 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!iEntityPlayerSP4.isInWater()) {
                                IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                                iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
                                iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                                iEntityPlayerSP7.setMotionY(iEntityPlayerSP7.getMotionY() / (double)1.45f);
                            }
                        }
                    }
                    if (iEntityPlayerSP2.getHurtTime() < 1) {
                        iSPacketEntityVelocity.setMotionY(0);
                    }
                    if (iEntityPlayerSP2.getHurtTime() >= 5) break;
                    iSPacketEntityVelocity.setMotionX(0);
                    iSPacketEntityVelocity.setMotionZ(0);
                    break;
                }
                case "hyttest": {
                    if (!iEntityPlayerSP2.getOnGround()) break;
                    this.canCancelJump = false;
                    iSPacketEntityVelocity.setMotionX(0);
                    iSPacketEntityVelocity.setMotionY(0);
                    iSPacketEntityVelocity.setMotionZ(0);
                    IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                    iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() / 1.75);
                    IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP2;
                    iEntityPlayerSP9.setMotionZ(iEntityPlayerSP9.getMotionZ() / 1.75);
                    break;
                }
                case "hytnewtest": {
                    if (!iEntityPlayerSP2.getOnGround()) break;
                    this.velocityInput = true;
                    float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
                    iSPacketEntityVelocity.setMotionX((int)((double)iSPacketEntityVelocity.getMotionX() * 0.75));
                    iSPacketEntityVelocity.setMotionZ((int)((double)iSPacketEntityVelocity.getMotionZ() * 0.75));
                    IEntityPlayerSP iEntityPlayerSP10 = iEntityPlayerSP2;
                    double d = iEntityPlayerSP10.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP11 = iEntityPlayerSP10;
                    boolean bl2 = false;
                    float f4 = (float)Math.sin(f);
                    iEntityPlayerSP11.setMotionX(d - (double)f4 * 0.2);
                    IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP2;
                    d = iEntityPlayerSP12.getMotionZ();
                    iEntityPlayerSP11 = iEntityPlayerSP12;
                    bl2 = false;
                    f4 = (float)Math.cos(f);
                    iEntityPlayerSP11.setMotionZ(d + (double)f4 * 0.2);
                    break;
                }
                case "hytpacketa": {
                    iSPacketEntityVelocity.setMotionX((int)((double)((float)iSPacketEntityVelocity.getMotionX() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    iSPacketEntityVelocity.setMotionY(0);
                    iSPacketEntityVelocity.setMotionZ((int)((double)((float)iSPacketEntityVelocity.getMotionZ() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    packetEvent.cancelEvent();
                    break;
                }
                case "hytpacketb": {
                    iSPacketEntityVelocity.setMotionX((int)((double)((float)iSPacketEntityVelocity.getMotionX() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    iSPacketEntityVelocity.setMotionY((int)((double)((float)iSPacketEntityVelocity.getMotionY() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    iSPacketEntityVelocity.setMotionZ((int)((double)((float)iSPacketEntityVelocity.getMotionZ() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
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
                    float f = 0.0f;
                    float f5 = 0.0f;
                    packetEvent.cancelEvent();
                    break;
                }
                case "glitch": {
                    if (!iEntityPlayerSP2.getOnGround()) {
                        return;
                    }
                    this.velocityInput = true;
                    packetEvent.cancelEvent();
                    break;
                }
                case "hytcancel": {
                    packetEvent.cancelEvent();
                }
            }
        }
    }

    public static final IMinecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public final void setBlock(@Nullable IBlock iBlock) {
        this.block = iBlock;
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
        if (iEntityPlayerSP != null) {
            iEntityPlayerSP.setSpeedInAir(0.02f);
        }
    }

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        String string;
        IEntityPlayerSP iEntityPlayerSP = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
        if (iEntityPlayerSP == null || iEntityPlayerSP.isInWater() || iEntityPlayerSP.isInLava() || iEntityPlayerSP.isInWeb()) {
            return;
        }
        String string2 = (String)this.modeValue.get();
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string = string3.toLowerCase()) {
            case "aacpush": {
                this.jump = true;
                if (iEntityPlayerSP.isCollidedVertically()) break;
                jumpEvent.cancelEvent();
                break;
            }
            case "aac4": {
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                jumpEvent.cancelEvent();
                break;
            }
            case "aaczero": {
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                jumpEvent.cancelEvent();
            }
        }
    }
}

