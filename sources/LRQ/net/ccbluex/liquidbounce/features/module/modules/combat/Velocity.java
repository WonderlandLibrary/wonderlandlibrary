/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
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
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Velocity", description="Edit your velocity", category=ModuleCategory.COMBAT)
public final class Velocity
extends Module {
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
    private final FloatValue verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Custom", "AAC4", "Simple", "SimpleFix", "AAC", "AACPush", "AACZero", "Reverse", "SmoothReverse", "Jump", "AAC5Reduce", "HytPacketA", "Glitch", "GrimACHalf", "HytTick", "Vanilla", "HytTest", "HytNewTest", "HytPacket", "NewAAC4", "FeiLe", "HytMotion", "NewHytMotion", "HytPacketB", "HytMotionB", "HytPacketFix", "S27", "LatestTestHyt"}, "Vanilla");
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

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
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
            case "jump": {
                if (thePlayer.getHurtTime() <= 0 || !thePlayer.getOnGround()) break;
                thePlayer.setMotionY(0.42);
                float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                double d = iEntityPlayerSP3.getMotionX();
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP3;
                boolean bl2 = false;
                float f = (float)Math.sin(yaw);
                iEntityPlayerSP4.setMotionX(d - (double)f * 0.2);
                IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                d = iEntityPlayerSP5.getMotionZ();
                iEntityPlayerSP4 = iEntityPlayerSP5;
                bl2 = false;
                f = (float)Math.cos(yaw);
                iEntityPlayerSP4.setMotionZ(d + (double)f * 0.2);
                break;
            }
            case "GrimACHalf": {
                if (thePlayer.getHurtTime() <= 0 || !thePlayer.getOnGround()) break;
                thePlayer.setMotionY(0.42);
                float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                double d = iEntityPlayerSP6.getMotionX();
                IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP6;
                boolean bl3 = false;
                float f = (float)Math.sin(yaw);
                iEntityPlayerSP7.setMotionX(d - (double)f * 0.2);
                IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                d = iEntityPlayerSP8.getMotionZ();
                iEntityPlayerSP7 = iEntityPlayerSP8;
                bl3 = false;
                f = (float)Math.cos(yaw);
                iEntityPlayerSP7.setMotionZ(d + (double)f * 0.2);
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
                    IEntityPlayerSP iEntityPlayerSP9 = thePlayer;
                    iEntityPlayerSP9.setMotionX(iEntityPlayerSP9.getMotionX() * 0.81);
                    IEntityPlayerSP iEntityPlayerSP10 = thePlayer;
                    iEntityPlayerSP10.setMotionZ(iEntityPlayerSP10.getMotionZ() * 0.81);
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
            case "aac4": {
                if (!thePlayer.getOnGround()) {
                    if (!this.velocityInput) break;
                    thePlayer.setSpeedInAir(0.02f);
                    IEntityPlayerSP iEntityPlayerSP11 = thePlayer;
                    iEntityPlayerSP11.setMotionX(iEntityPlayerSP11.getMotionX() * 0.6);
                    IEntityPlayerSP iEntityPlayerSP12 = thePlayer;
                    iEntityPlayerSP12.setMotionZ(iEntityPlayerSP12.getMotionZ() * 0.6);
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                thePlayer.setSpeedInAir(0.02f);
                break;
            }
            case "newaac4": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.getOnGround()) break;
                float reduce = ((Number)this.newaac4XZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP13 = thePlayer;
                iEntityPlayerSP13.setMotionX(iEntityPlayerSP13.getMotionX() * (double)reduce);
                IEntityPlayerSP iEntityPlayerSP14 = thePlayer;
                iEntityPlayerSP14.setMotionZ(iEntityPlayerSP14.getMotionZ() * (double)reduce);
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
            case "aac": {
                if (!this.velocityInput || !this.velocityTimer.hasTimePassed(80L)) break;
                IEntityPlayerSP iEntityPlayerSP15 = thePlayer;
                iEntityPlayerSP15.setMotionX(iEntityPlayerSP15.getMotionX() * ((Number)this.horizontalValue.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP16 = thePlayer;
                iEntityPlayerSP16.setMotionZ(iEntityPlayerSP16.getMotionZ() * ((Number)this.horizontalValue.get()).doubleValue());
                this.velocityInput = false;
                break;
            }
            case "hytpacket": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP17 = thePlayer;
                    iEntityPlayerSP17.setMotionX(iEntityPlayerSP17.getMotionX() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP18 = thePlayer;
                    iEntityPlayerSP18.setMotionZ(iEntityPlayerSP18.getMotionZ() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP19 = thePlayer;
                    iEntityPlayerSP19.setMotionY(iEntityPlayerSP19.getMotionY() / (double)1.781145f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP20 = thePlayer;
                iEntityPlayerSP20.setMotionX(iEntityPlayerSP20.getMotionX() * 0.5);
                IEntityPlayerSP iEntityPlayerSP21 = thePlayer;
                iEntityPlayerSP21.setMotionZ(iEntityPlayerSP21.getMotionZ() * 0.5);
                IEntityPlayerSP iEntityPlayerSP22 = thePlayer;
                iEntityPlayerSP22.setMotionY(iEntityPlayerSP22.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotion": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP23 = thePlayer;
                    iEntityPlayerSP23.setMotionX(iEntityPlayerSP23.getMotionX() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP24 = thePlayer;
                    iEntityPlayerSP24.setMotionZ(iEntityPlayerSP24.getMotionZ() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP25 = thePlayer;
                    iEntityPlayerSP25.setMotionY(iEntityPlayerSP25.getMotionY() * (double)0.381145f);
                    IEntityPlayerSP iEntityPlayerSP26 = thePlayer;
                    iEntityPlayerSP26.setMotionY(iEntityPlayerSP26.getMotionY() / (double)1.781145f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP27 = thePlayer;
                iEntityPlayerSP27.setMotionX(iEntityPlayerSP27.getMotionX() * 0.4);
                IEntityPlayerSP iEntityPlayerSP28 = thePlayer;
                iEntityPlayerSP28.setMotionZ(iEntityPlayerSP28.getMotionZ() * 0.4);
                IEntityPlayerSP iEntityPlayerSP29 = thePlayer;
                iEntityPlayerSP29.setMotionY(iEntityPlayerSP29.getMotionY() * (double)0.381145f);
                IEntityPlayerSP iEntityPlayerSP30 = thePlayer;
                iEntityPlayerSP30.setMotionY(iEntityPlayerSP30.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotionb": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getOnGround() || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP31 = thePlayer;
                iEntityPlayerSP31.setMotionX(iEntityPlayerSP31.getMotionX() * (double)0.451145f);
                IEntityPlayerSP iEntityPlayerSP32 = thePlayer;
                iEntityPlayerSP32.setMotionZ(iEntityPlayerSP32.getMotionZ() * (double)0.451145f);
                break;
            }
            case "newhytmotion": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getOnGround()) break;
                if (!thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                    IEntityPlayerSP iEntityPlayerSP33 = thePlayer;
                    iEntityPlayerSP33.setMotionX(iEntityPlayerSP33.getMotionX() * 0.47188);
                    IEntityPlayerSP iEntityPlayerSP34 = thePlayer;
                    iEntityPlayerSP34.setMotionZ(iEntityPlayerSP34.getMotionZ() * 0.47188);
                    if (thePlayer.getMotionY() != 0.42 && !(thePlayer.getMotionY() > 0.42)) break;
                    IEntityPlayerSP iEntityPlayerSP35 = thePlayer;
                    iEntityPlayerSP35.setMotionY(iEntityPlayerSP35.getMotionY() * 0.4);
                    break;
                }
                IEntityPlayerSP iEntityPlayerSP36 = thePlayer;
                iEntityPlayerSP36.setMotionX(iEntityPlayerSP36.getMotionX() * 0.65025);
                IEntityPlayerSP iEntityPlayerSP37 = thePlayer;
                iEntityPlayerSP37.setMotionZ(iEntityPlayerSP37.getMotionZ() * 0.65025);
                if (thePlayer.getMotionY() != 0.42 && !(thePlayer.getMotionY() > 0.42)) break;
                IEntityPlayerSP iEntityPlayerSP38 = thePlayer;
                iEntityPlayerSP38.setMotionY(iEntityPlayerSP38.getMotionY() * 0.4);
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
                            IEntityPlayerSP iEntityPlayerSP39 = thePlayer;
                            iEntityPlayerSP39.setMotionY(iEntityPlayerSP39.getMotionY() - 0.014999993);
                        }
                    }
                }
                if (thePlayer.getHurtResistantTime() < 19) break;
                float reduce = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP40 = thePlayer;
                iEntityPlayerSP40.setMotionX(iEntityPlayerSP40.getMotionX() / (double)reduce);
                IEntityPlayerSP iEntityPlayerSP41 = thePlayer;
                iEntityPlayerSP41.setMotionZ(iEntityPlayerSP41.getMotionZ() / (double)reduce);
                break;
            }
            case "custom": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead()) break;
                IEntityPlayerSP iEntityPlayerSP42 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP42 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP42.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP43 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP43 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP43.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP44 = thePlayer;
                iEntityPlayerSP44.setMotionX(iEntityPlayerSP44.getMotionX() * ((Number)this.customX.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP45 = thePlayer;
                iEntityPlayerSP45.setMotionZ(iEntityPlayerSP45.getMotionZ() * ((Number)this.customZ.get()).doubleValue());
                if (((Boolean)this.customYStart.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP46 = thePlayer;
                    iEntityPlayerSP46.setMotionY(iEntityPlayerSP46.getMotionY() / ((Number)this.customY.get()).doubleValue());
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
                    IEntityPlayerSP iEntityPlayerSP47 = thePlayer;
                    iEntityPlayerSP47.setMotionY(iEntityPlayerSP47.getMotionY() - 1.0);
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
                case "vanilla": {
                    event.cancelEvent();
                    break;
                }
                case "s27": {
                    if (MinecraftInstance.classProvider.isSPacketExplosion(packet)) {
                        event.cancelEvent();
                    }
                    float horizontal = ((Number)this.horizontalValue.get()).floatValue();
                    float vertical = ((Number)this.verticalValue.get()).floatValue();
                    packetEntityVelocity.setMotionX((int)((float)packetEntityVelocity.getMotionX() * horizontal));
                    packetEntityVelocity.setMotionZ((int)((float)packetEntityVelocity.getMotionZ() * horizontal));
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
                        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP3 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!iEntityPlayerSP3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP4 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!iEntityPlayerSP4.isInWater()) {
                                IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                                iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                                iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                                iEntityPlayerSP7.setMotionY(iEntityPlayerSP7.getMotionY() / (double)1.45f);
                            }
                        }
                    }
                    if (thePlayer.getHurtTime() < 1) {
                        packetEntityVelocity.setMotionY(0);
                    }
                    if (thePlayer.getHurtTime() >= 5) break;
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
                    IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                    iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() / 1.75);
                    IEntityPlayerSP iEntityPlayerSP9 = thePlayer;
                    iEntityPlayerSP9.setMotionZ(iEntityPlayerSP9.getMotionZ() / 1.75);
                    break;
                }
                case "hytnewtest": {
                    if (!thePlayer.getOnGround()) break;
                    this.velocityInput = true;
                    float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                    packetEntityVelocity.setMotionX((int)((double)packetEntityVelocity.getMotionX() * 0.75));
                    packetEntityVelocity.setMotionZ((int)((double)packetEntityVelocity.getMotionZ() * 0.75));
                    IEntityPlayerSP iEntityPlayerSP10 = thePlayer;
                    double d = iEntityPlayerSP10.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP11 = iEntityPlayerSP10;
                    boolean vertical = false;
                    float f = (float)Math.sin(yaw);
                    iEntityPlayerSP11.setMotionX(d - (double)f * 0.2);
                    IEntityPlayerSP iEntityPlayerSP12 = thePlayer;
                    d = iEntityPlayerSP12.getMotionZ();
                    iEntityPlayerSP11 = iEntityPlayerSP12;
                    vertical = false;
                    f = (float)Math.cos(yaw);
                    iEntityPlayerSP11.setMotionZ(d + (double)f * 0.2);
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
                    break;
                }
                case "hytcancel": {
                    event.cancelEvent();
                }
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

