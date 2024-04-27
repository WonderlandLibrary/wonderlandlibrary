/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  org.jetbrains.annotations.Nullable
 */
package cc.paimon.modules.combat;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
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
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NewVelocity", description="\u4f7f\u4f60\u6539\u53d8\u81ea\u5df1\u53d7\u5230\u7684\u51fb\u9000", category=ModuleCategory.COMBAT)
public final class OldVelocity
extends Module {
    private final FloatValue hytpacketaset;
    private IBlock block;
    private boolean jump;
    private final BoolValue customYStart;
    private final ListValue modeValue;
    private final FloatValue reverseStrengthValue;
    private final BoolValue aacPushYReducerValue;
    private final BoolValue customC06FakeLag;
    private int velocityTick;
    private final BoolValue onlyGroundValue;
    private final BoolValue hytGround;
    private final IntegerValue velocityTickValue;
    private final FloatValue customX;
    private final BoolValue onlyCombatValue;
    private boolean huayutingjumpflag;
    private boolean canCleanJump;
    private final BoolValue noFireValue;
    private final FloatValue hytpacketbset;
    private final FloatValue reverse2StrengthValue;
    private final FloatValue newaac4XZReducerValue;
    private MSTimer velocityTimer;
    private boolean canCancelJump;
    private boolean reverseHurt;
    private final FloatValue aacPushXZReducerValue;
    private final FloatValue verticalValue;
    private final FloatValue aac4XZReducerValue;
    private boolean jumpingflag;
    private int hytCount = 24;
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
    private final FloatValue customZ;
    private final FloatValue customY;
    private final BoolValue cobwebValue;
    private boolean velocityInput;

    public final int getHytCount() {
        return this.hytCount;
    }

    public final void setBlock(@Nullable IBlock iBlock) {
        this.block = iBlock;
    }

    @Override
    public void onDisable() {
        block0: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setSpeedInAir(0.02f);
        }
    }

    public OldVelocity() {
        this.verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
        this.modeValue = new ListValue("Mode", new String[]{"GrimReduce", "Grim-Motion", "HytFwNmslCnm", "test", "Hytjump1", "vanilla", "test1", "test2", "Jumping", "TestAAC5", "HytTestAAC4", "HytBest", "HuaYuTingJump", "Custom", "AAC4", "Simple", "SimpleFix", "AAC", "AACPush", "AACZero", "Reverse", "SmoothReverse", "Jump", "AAC5Reduce", "HytPacketA", "Glitch", "HytCancel", "HytTick", "Vanilla", "HytTest", "HytNewTest", "HytPacket", "NewAAC4", "Hyt", "FeiLe", "HytMotion", "NewHytMotion", "HytPacketB", "HytMotionB", "HytPacketFix", "NoXZ"}, "Vanilla");
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
        this.onlyCombatValue = new BoolValue("OnlyCombat", false);
        this.onlyGroundValue = new BoolValue("OnlyGround", false);
        this.hytGround = new BoolValue("HytGround", true);
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

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null || iEntityPlayerSP.isInWater() || iEntityPlayerSP.isInLava() || iEntityPlayerSP.isInWeb()) {
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
                break;
            }
        }
    }

    public final IBlock getBlock() {
        return this.block;
    }

    public final void setHytCount(int n) {
        this.hytCount = n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        Object object;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IPacket iPacket = packetEvent.getPacket();
        if (!MinecraftInstance.classProvider.isSPacketEntityVelocity(iPacket)) return;
        ISPacketEntityVelocity iSPacketEntityVelocity = iPacket.asSPacketEntityVelocity();
        if (((Boolean)this.noFireValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.isBurning()) {
                return;
            }
        }
        if ((object = MinecraftInstance.mc.getTheWorld()) == null || (object = object.getEntityByID(iSPacketEntityVelocity.getEntityID())) == null) {
            return;
        }
        if (object.equals(iEntityPlayerSP2) ^ true) {
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
            case "noxz": {
                if (iSPacketEntityVelocity.getMotionX() == 0 && iSPacketEntityVelocity.getMotionZ() == 0) {
                    return;
                }
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
                if (module == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
                }
                KillAura killAura = (KillAura)module;
                IEntityLivingBase iEntityLivingBase = LiquidBounce.INSTANCE.getCombatManager().getNearByEntity(((Number)killAura.getRangeValue().get()).floatValue() + 1.0f);
                if (iEntityLivingBase == null) {
                    return;
                }
                IEntityLivingBase iEntityLivingBase2 = iEntityLivingBase;
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP4.setMotionX(0.0);
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP5.setMotionZ(0.0);
                iSPacketEntityVelocity.setMotionX(0);
                iSPacketEntityVelocity.setMotionZ(0);
                int n = 0;
                int n2 = this.hytCount;
                if (n <= n2) {
                    while (true) {
                        IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP6 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP6.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity((IEntity)iEntityLivingBase2, ICPacketUseEntity.WAction.ATTACK));
                        IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP7 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP7.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
                        if (n == n2) break;
                        ++n;
                    }
                }
                if (this.hytCount <= 12) return;
                this.hytCount -= 5;
                return;
            }
            case "jumping": {
                IPacket iPacket2 = iPacket;
                boolean bl2 = false;
                if (!(((PacketImpl)iPacket2).getWrapped() instanceof SPacketEntityVelocity)) return;
                this.jumpingflag = true;
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP8.getHurtTime() == 0) return;
                packetEvent.cancelEvent();
                iPacket.asSPacketEntityVelocity().setMotionX(0);
                iPacket.asSPacketEntityVelocity().setMotionY(0);
                iPacket.asSPacketEntityVelocity().setMotionZ(0);
                return;
            }
            case "vanilla": {
                packetEvent.cancelEvent();
                return;
            }
            case "huayutingjump": {
                this.huayutingjumpflag = true;
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP9.getHurtTime() == 0) return;
                packetEvent.cancelEvent();
                iSPacketEntityVelocity.setMotionX(0);
                iSPacketEntityVelocity.setMotionY(0);
                iSPacketEntityVelocity.setMotionZ(0);
                return;
            }
            case "simple": {
                float f = ((Number)this.horizontalValue.get()).floatValue();
                float f2 = ((Number)this.verticalValue.get()).floatValue();
                if (f == 0.0f && f2 == 0.0f) {
                    packetEvent.cancelEvent();
                }
                iSPacketEntityVelocity.setMotionX((int)((float)iSPacketEntityVelocity.getMotionX() * f));
                iSPacketEntityVelocity.setMotionY((int)((float)iSPacketEntityVelocity.getMotionY() * f2));
                iSPacketEntityVelocity.setMotionZ((int)((float)iSPacketEntityVelocity.getMotionZ() * f));
                return;
            }
            case "hytpacketfix": {
                if (iEntityPlayerSP2.getHurtTime() > 0 && !iEntityPlayerSP2.isDead()) {
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP10.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                        IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP11 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!iEntityPlayerSP11.isInWater()) {
                            IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP2;
                            iEntityPlayerSP12.setMotionX(iEntityPlayerSP12.getMotionX() * 0.4);
                            IEntityPlayerSP iEntityPlayerSP13 = iEntityPlayerSP2;
                            iEntityPlayerSP13.setMotionZ(iEntityPlayerSP13.getMotionZ() * 0.4);
                            IEntityPlayerSP iEntityPlayerSP14 = iEntityPlayerSP2;
                            iEntityPlayerSP14.setMotionY(iEntityPlayerSP14.getMotionY() / (double)1.45f);
                        }
                    }
                }
                if (iEntityPlayerSP2.getHurtTime() < 1) {
                    iSPacketEntityVelocity.setMotionY(0);
                }
                if (iEntityPlayerSP2.getHurtTime() >= 5) return;
                iSPacketEntityVelocity.setMotionX(0);
                iSPacketEntityVelocity.setMotionZ(0);
                return;
            }
            case "hyttest": {
                if (!iEntityPlayerSP2.getOnGround()) return;
                this.canCancelJump = false;
                iSPacketEntityVelocity.setMotionX((int)0.985114);
                iSPacketEntityVelocity.setMotionY((int)0.885113);
                iSPacketEntityVelocity.setMotionZ((int)0.785112);
                IEntityPlayerSP iEntityPlayerSP15 = iEntityPlayerSP2;
                iEntityPlayerSP15.setMotionX(iEntityPlayerSP15.getMotionX() / 1.75);
                IEntityPlayerSP iEntityPlayerSP16 = iEntityPlayerSP2;
                iEntityPlayerSP16.setMotionZ(iEntityPlayerSP16.getMotionZ() / 1.75);
                return;
            }
            case "hytnewtest": {
                if (!iEntityPlayerSP2.getOnGround()) return;
                this.velocityInput = true;
                float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
                iSPacketEntityVelocity.setMotionX((int)((double)iSPacketEntityVelocity.getMotionX() * 0.75));
                iSPacketEntityVelocity.setMotionZ((int)((double)iSPacketEntityVelocity.getMotionZ() * 0.75));
                IEntityPlayerSP iEntityPlayerSP17 = iEntityPlayerSP2;
                double d = iEntityPlayerSP17.getMotionX();
                IEntityPlayerSP iEntityPlayerSP18 = iEntityPlayerSP17;
                boolean bl3 = false;
                float f3 = (float)Math.sin(f);
                iEntityPlayerSP18.setMotionX(d - (double)f3 * 0.2);
                IEntityPlayerSP iEntityPlayerSP19 = iEntityPlayerSP2;
                d = iEntityPlayerSP19.getMotionZ();
                iEntityPlayerSP18 = iEntityPlayerSP19;
                bl3 = false;
                f3 = (float)Math.cos(f);
                iEntityPlayerSP18.setMotionZ(d + (double)f3 * 0.2);
                return;
            }
            case "hytpacketa": {
                iSPacketEntityVelocity.setMotionX((int)((double)((float)iSPacketEntityVelocity.getMotionX() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                iSPacketEntityVelocity.setMotionY((int)0.7);
                iSPacketEntityVelocity.setMotionZ((int)((double)((float)iSPacketEntityVelocity.getMotionZ() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                packetEvent.cancelEvent();
                return;
            }
            case "hytpacketb": {
                iSPacketEntityVelocity.setMotionX((int)((double)((float)iSPacketEntityVelocity.getMotionX() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                iSPacketEntityVelocity.setMotionY((int)((double)((float)iSPacketEntityVelocity.getMotionY() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                iSPacketEntityVelocity.setMotionZ((int)((double)((float)iSPacketEntityVelocity.getMotionZ() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                return;
            }
            case "hyttestaac4": {
                if (MinecraftInstance.mc.getThePlayer() == null) return;
                Object object2 = MinecraftInstance.mc.getTheWorld();
                if (object2 == null || (object2 = object2.getEntityByID(iSPacketEntityVelocity.getEntityID())) == null) {
                    return;
                }
                if (object2.equals(MinecraftInstance.mc.getThePlayer()) ^ true) {
                    return;
                }
                this.velocityTimer.reset();
                this.velocityInput = true;
                return;
            }
            case "aac": 
            case "aaczero": 
            case "reverse": 
            case "aac4": 
            case "smoothreverse": 
            case "aac5reduce": {
                this.velocityInput = true;
                return;
            }
            case "hyttick": {
                this.velocityInput = true;
                float f = 0.0f;
                float f4 = 0.0f;
                packetEvent.cancelEvent();
                return;
            }
            case "glitch": {
                if (!iEntityPlayerSP2.getOnGround()) {
                    return;
                }
                this.velocityInput = true;
                packetEvent.cancelEvent();
                return;
            }
            case "hytcancel": {
                packetEvent.cancelEvent();
                return;
            }
        }
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP;
        block184: {
            block183: {
                block182: {
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        return;
                    }
                    iEntityPlayerSP = iEntityPlayerSP2;
                    if (iEntityPlayerSP.isInWater() || iEntityPlayerSP.isInLava() || iEntityPlayerSP.isInWeb()) {
                        return;
                    }
                    if (!((Boolean)this.onlyGroundValue.get()).booleanValue()) break block182;
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP3.getOnGround()) break block183;
                }
                if (!((Boolean)this.onlyCombatValue.get()).booleanValue() || LiquidBounce.INSTANCE.getCombatManager().getInCombat()) break block184;
            }
            return;
        }
        if (((Boolean)this.noFireValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP4.isBurning()) {
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
            case "grimreduce": {
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP;
                iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP;
                iEntityPlayerSP6.setMotionY(iEntityPlayerSP6.getMotionY() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP;
                iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() + -1.0E-7);
                iEntityPlayerSP.setAirBorne(true);
                break;
            }
            case "grim-motion": {
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP;
                iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP;
                iEntityPlayerSP9.setMotionY(iEntityPlayerSP9.getMotionY() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP10 = iEntityPlayerSP;
                iEntityPlayerSP10.setMotionZ(iEntityPlayerSP10.getMotionZ() + -1.2E-7);
                iEntityPlayerSP.setAirBorne(true);
                break;
            }
            case "test": {
                IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP11 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP11.getHurtTime() <= 0 || !this.jumpingflag) break;
                IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP12 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP12.getOnGround()) {
                    IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl2 = iEntityPlayerSP13.getHurtTime() <= 2;
                    IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP14 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP14.setMotionX(iEntityPlayerSP14.getMotionX() * 0.1);
                    IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP15 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP15.setMotionZ(iEntityPlayerSP15.getMotionZ() * 0.1);
                    IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP16 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl3 = iEntityPlayerSP16.getHurtTime() <= 4;
                    IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP17 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP17.setMotionX(iEntityPlayerSP17.getMotionX() * 0.2);
                    IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP18 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP18.setMotionZ(iEntityPlayerSP18.getMotionZ() * 0.2);
                } else {
                    IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP19 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP19.getHurtTime() <= 2) {
                        IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP20 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP20.setMotionX(iEntityPlayerSP20.getMotionX() * 0.1);
                        IEntityPlayerSP iEntityPlayerSP21 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP21 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP21.setMotionZ(iEntityPlayerSP21.getMotionZ() * 0.1);
                    }
                }
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP22 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP22 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP22, ICPacketEntityAction.WAction.START_SNEAKING));
                this.jumpingflag = false;
                break;
            }
            case "test1": {
                IEntityPlayerSP iEntityPlayerSP23 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP23 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP23.getHurtTime() > 0 && this.jumpingflag) {
                    IEntityPlayerSP iEntityPlayerSP24 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP24 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP24 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl4 = iEntityPlayerSP24.getHurtTime() <= 2;
                    IEntityPlayerSP iEntityPlayerSP25 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP25 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP25.setMotionX(iEntityPlayerSP25.getMotionX() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP26 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP26 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP26.setMotionZ(iEntityPlayerSP26.getMotionZ() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP27 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP27 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl5 = iEntityPlayerSP27.getHurtTime() <= 4;
                    IEntityPlayerSP iEntityPlayerSP28 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP28 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP28.setMotionX(iEntityPlayerSP28.getMotionX() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP29 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP29 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP29.setMotionZ(iEntityPlayerSP29.getMotionZ() * 0.5);
                } else {
                    IEntityPlayerSP iEntityPlayerSP30 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP30 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP30.getHurtTime() <= 9) {
                        IEntityPlayerSP iEntityPlayerSP31 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP31 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP31.setMotionX(iEntityPlayerSP31.getMotionX() * 0.6);
                        IEntityPlayerSP iEntityPlayerSP32 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP32 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP32.setMotionZ(iEntityPlayerSP32.getMotionZ() * 0.6);
                    }
                }
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP33 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP33 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP33, ICPacketEntityAction.WAction.START_SNEAKING));
                this.jumpingflag = false;
                break;
            }
            case "test2": {
                IEntityPlayerSP iEntityPlayerSP34 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP34 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP34.getHurtTime() > 0 && this.jumpingflag) {
                    IEntityPlayerSP iEntityPlayerSP35 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP35 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP35 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl6 = iEntityPlayerSP35.getHurtTime() <= 6;
                    IEntityPlayerSP iEntityPlayerSP36 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP36 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP36.setMotionX(iEntityPlayerSP36.getMotionX() * 0.3);
                    IEntityPlayerSP iEntityPlayerSP37 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP37 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP37.setMotionZ(iEntityPlayerSP37.getMotionZ() * 0.3);
                    IEntityPlayerSP iEntityPlayerSP38 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP38 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl7 = iEntityPlayerSP38.getHurtTime() <= 4;
                    IEntityPlayerSP iEntityPlayerSP39 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP39 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP39.setMotionX(iEntityPlayerSP39.getMotionX() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP40 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP40 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP40.setMotionZ(iEntityPlayerSP40.getMotionZ() * 0.5);
                } else {
                    IEntityPlayerSP iEntityPlayerSP41 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP41 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP41.setMotionX(iEntityPlayerSP41.getMotionX() * 0.3);
                    IEntityPlayerSP iEntityPlayerSP42 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP42 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP42.setMotionZ(iEntityPlayerSP42.getMotionZ() * 0.3);
                }
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP43 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP43 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP43, ICPacketEntityAction.WAction.START_SNEAKING));
                this.jumpingflag = false;
                break;
            }
            case "Hytjump1": {
                iEntityPlayerSP.setNoClip(this.velocityInput);
                if (iEntityPlayerSP.getHurtTime() == 7) {
                    IEntityPlayerSP iEntityPlayerSP44 = iEntityPlayerSP;
                    iEntityPlayerSP44.setMotionX(iEntityPlayerSP44.getMotionX() * 0.0);
                }
                IEntityPlayerSP iEntityPlayerSP45 = iEntityPlayerSP;
                iEntityPlayerSP45.setMotionZ(iEntityPlayerSP45.getMotionZ() * 0.0);
                iEntityPlayerSP.setMotionY(0.42);
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP46 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP46 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP46.getPosX();
                IEntityPlayerSP iEntityPlayerSP47 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP47 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityPlayerSP47.getPosY();
                IEntityPlayerSP iEntityPlayerSP48 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP48 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2, iEntityPlayerSP48.getPosZ(), true));
                break;
            }
            case "hytfwnmslcnm": {
                IEntityPlayerSP iEntityPlayerSP49 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP49 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP49.getHurtTime() <= 0 || !this.jumpingflag) break;
                IEntityPlayerSP iEntityPlayerSP50 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP50 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP50.getOnGround()) {
                    IEntityPlayerSP iEntityPlayerSP51 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP51 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP51 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl8 = iEntityPlayerSP51.getHurtTime() <= 6;
                    IEntityPlayerSP iEntityPlayerSP52 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP52 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP52.setMotionX(iEntityPlayerSP52.getMotionX() * 0.326934583);
                    IEntityPlayerSP iEntityPlayerSP53 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP53 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP53.setMotionZ(iEntityPlayerSP53.getMotionZ() * 0.326934583);
                    IEntityPlayerSP iEntityPlayerSP54 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP54 == null) {
                        Intrinsics.throwNpe();
                    }
                    boolean bl9 = iEntityPlayerSP54.getHurtTime() <= 4;
                    IEntityPlayerSP iEntityPlayerSP55 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP55 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP55.setMotionX(iEntityPlayerSP55.getMotionX() * 0.428534723);
                    IEntityPlayerSP iEntityPlayerSP56 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP56 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP56.setMotionZ(iEntityPlayerSP56.getMotionZ() * 0.428534723);
                } else {
                    IEntityPlayerSP iEntityPlayerSP57 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP57 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP57.getHurtTime() <= 9) {
                        IEntityPlayerSP iEntityPlayerSP58 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP58 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP58.setMotionX(iEntityPlayerSP58.getMotionX() * 0.326934583);
                        IEntityPlayerSP iEntityPlayerSP59 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP59 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP59.setMotionZ(iEntityPlayerSP59.getMotionZ() * 0.326934583);
                    }
                }
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP60 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP60 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP60, ICPacketEntityAction.WAction.START_SNEAKING));
                this.jumpingflag = false;
                break;
            }
            case "testaac5": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getHurtTime() > 5 || !iEntityPlayerSP.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP61 = iEntityPlayerSP;
                    iEntityPlayerSP61.setMotionX(iEntityPlayerSP61.getMotionX() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP62 = iEntityPlayerSP;
                    iEntityPlayerSP62.setMotionZ(iEntityPlayerSP62.getMotionZ() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP63 = iEntityPlayerSP;
                    iEntityPlayerSP63.setMotionY(iEntityPlayerSP63.getMotionY() * 0.001);
                    IEntityPlayerSP iEntityPlayerSP64 = iEntityPlayerSP;
                    iEntityPlayerSP64.setMotionY(iEntityPlayerSP64.getMotionY() / (double)0.01f);
                    break;
                }
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP65 = iEntityPlayerSP;
                iEntityPlayerSP65.setMotionX(iEntityPlayerSP65.getMotionX() * 0.35);
                IEntityPlayerSP iEntityPlayerSP66 = iEntityPlayerSP;
                iEntityPlayerSP66.setMotionZ(iEntityPlayerSP66.getMotionZ() * 0.35);
                IEntityPlayerSP iEntityPlayerSP67 = iEntityPlayerSP;
                iEntityPlayerSP67.setMotionY(iEntityPlayerSP67.getMotionY() * 0.001);
                IEntityPlayerSP iEntityPlayerSP68 = iEntityPlayerSP;
                iEntityPlayerSP68.setMotionY(iEntityPlayerSP68.getMotionY() / (double)0.01f);
                break;
            }
            case "jump": {
                if (iEntityPlayerSP.getHurtTime() <= 0 || !iEntityPlayerSP.getOnGround()) break;
                iEntityPlayerSP.setMotionY(0.42);
                float f = iEntityPlayerSP.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP69 = iEntityPlayerSP;
                double d = iEntityPlayerSP69.getMotionX();
                IEntityPlayerSP iEntityPlayerSP70 = iEntityPlayerSP69;
                boolean bl10 = false;
                float f2 = (float)Math.sin(f);
                iEntityPlayerSP70.setMotionX(d - (double)f2 * 0.2);
                IEntityPlayerSP iEntityPlayerSP71 = iEntityPlayerSP;
                d = iEntityPlayerSP71.getMotionZ();
                iEntityPlayerSP70 = iEntityPlayerSP71;
                bl10 = false;
                f2 = (float)Math.cos(f);
                iEntityPlayerSP70.setMotionZ(d + (double)f2 * 0.2);
                break;
            }
            case "glitch": {
                iEntityPlayerSP.setNoClip(this.velocityInput);
                if (iEntityPlayerSP.getHurtTime() == 7) {
                    iEntityPlayerSP.setMotionY(0.4);
                }
                this.velocityInput = false;
                break;
            }
            case "feile": {
                if (!iEntityPlayerSP.getOnGround()) break;
                this.canCleanJump = true;
                iEntityPlayerSP.setMotionY(1.5);
                iEntityPlayerSP.setMotionZ(1.2);
                iEntityPlayerSP.setMotionX(1.5);
                if (!iEntityPlayerSP.getOnGround() || this.velocityTick <= 2) break;
                this.velocityInput = false;
                break;
            }
            case "aac5reduce": {
                if (iEntityPlayerSP.getHurtTime() > 1 && this.velocityInput) {
                    IEntityPlayerSP iEntityPlayerSP72 = iEntityPlayerSP;
                    iEntityPlayerSP72.setMotionX(iEntityPlayerSP72.getMotionX() * 0.81);
                    IEntityPlayerSP iEntityPlayerSP73 = iEntityPlayerSP;
                    iEntityPlayerSP73.setMotionZ(iEntityPlayerSP73.getMotionZ() * 0.81);
                }
                if (!this.velocityInput || iEntityPlayerSP.getHurtTime() >= 5 && !iEntityPlayerSP.getOnGround() || !this.velocityTimer.hasTimePassed(120L)) break;
                this.velocityInput = false;
                break;
            }
            case "huayutingjump": {
                IEntityPlayerSP iEntityPlayerSP74 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP74 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP74.getHurtTime() <= 0 || !this.huayutingjumpflag) break;
                IEntityPlayerSP iEntityPlayerSP75 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP75 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP75.getOnGround()) {
                    IEntityPlayerSP iEntityPlayerSP76 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP76 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP76.getHurtTime() <= 6) {
                        IEntityPlayerSP iEntityPlayerSP77 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP77 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP77.setMotionX(iEntityPlayerSP77.getMotionX() * 0.600151164);
                        IEntityPlayerSP iEntityPlayerSP78 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP78 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP78.setMotionZ(iEntityPlayerSP78.getMotionZ() * 0.600151164);
                    }
                    IEntityPlayerSP iEntityPlayerSP79 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP79 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP79.getHurtTime() <= 4) {
                        IEntityPlayerSP iEntityPlayerSP80 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP80 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP80.setMotionX(iEntityPlayerSP80.getMotionX() * 0.700151164);
                        IEntityPlayerSP iEntityPlayerSP81 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP81 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP81.setMotionZ(iEntityPlayerSP81.getMotionZ() * 0.700151164);
                    }
                } else {
                    IEntityPlayerSP iEntityPlayerSP82 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP82 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP82.getHurtTime() <= 9) {
                        IEntityPlayerSP iEntityPlayerSP83 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP83 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP83.setMotionX(iEntityPlayerSP83.getMotionX() * 0.6001421204);
                        IEntityPlayerSP iEntityPlayerSP84 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP84 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP84.setMotionZ(iEntityPlayerSP84.getMotionZ() * 0.6001421204);
                    }
                }
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP85 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP85 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP85, ICPacketEntityAction.WAction.START_SNEAKING));
                this.huayutingjumpflag = false;
                break;
            }
            case "hyttick": {
                if (this.velocityTick > ((Number)this.velocityTickValue.get()).intValue()) {
                    if (iEntityPlayerSP.getMotionY() > 0.0) {
                        iEntityPlayerSP.setMotionY(0.0);
                    }
                    iEntityPlayerSP.setMotionX(0.0);
                    iEntityPlayerSP.setMotionZ(0.0);
                    iEntityPlayerSP.setJumpMovementFactor(-1.0E-5f);
                    this.velocityInput = false;
                }
                if (!iEntityPlayerSP.getOnGround() || this.velocityTick <= 1) break;
                this.velocityInput = false;
                break;
            }
            case "reverse": {
                if (!this.velocityInput) {
                    return;
                }
                if (!iEntityPlayerSP.getOnGround()) {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * ((Number)this.reverseStrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                break;
            }
            case "aac4": {
                if (!iEntityPlayerSP.getOnGround()) {
                    if (!this.velocityInput) break;
                    iEntityPlayerSP.setSpeedInAir(0.02f);
                    IEntityPlayerSP iEntityPlayerSP86 = iEntityPlayerSP;
                    iEntityPlayerSP86.setMotionX(iEntityPlayerSP86.getMotionX() * 0.6);
                    IEntityPlayerSP iEntityPlayerSP87 = iEntityPlayerSP;
                    iEntityPlayerSP87.setMotionZ(iEntityPlayerSP87.getMotionZ() * 0.6);
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                iEntityPlayerSP.setSpeedInAir(0.02f);
                break;
            }
            case "newaac4": {
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.getOnGround()) break;
                float f = ((Number)this.newaac4XZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP88 = iEntityPlayerSP;
                iEntityPlayerSP88.setMotionX(iEntityPlayerSP88.getMotionX() * (double)f);
                IEntityPlayerSP iEntityPlayerSP89 = iEntityPlayerSP;
                iEntityPlayerSP89.setMotionZ(iEntityPlayerSP89.getMotionZ() * (double)f);
                break;
            }
            case "hyttestaac4": {
                if (iEntityPlayerSP.isInWater() || iEntityPlayerSP.isInLava() || iEntityPlayerSP.isInWeb()) {
                    return;
                }
                if (!iEntityPlayerSP.getOnGround()) {
                    if (!this.velocityInput) break;
                    iEntityPlayerSP.setSpeedInAir(0.02f);
                    IEntityPlayerSP iEntityPlayerSP90 = iEntityPlayerSP;
                    iEntityPlayerSP90.setMotionX(iEntityPlayerSP90.getMotionX() * 0.6);
                    IEntityPlayerSP iEntityPlayerSP91 = iEntityPlayerSP;
                    iEntityPlayerSP91.setMotionZ(iEntityPlayerSP91.getMotionZ() * 0.6);
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                iEntityPlayerSP.setSpeedInAir(0.02f);
                break;
            }
            case "hytbest": {
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP92 = iEntityPlayerSP;
                iEntityPlayerSP92.setMotionX(iEntityPlayerSP92.getMotionX() / 1.0);
                IEntityPlayerSP iEntityPlayerSP93 = iEntityPlayerSP;
                iEntityPlayerSP93.setMotionZ(iEntityPlayerSP93.getMotionZ() / 1.0);
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    iEntityPlayerSP.setSpeedInAir(0.02f);
                    return;
                }
                if (iEntityPlayerSP.getHurtTime() > 0) {
                    this.reverseHurt = true;
                }
                if (!iEntityPlayerSP.getOnGround()) {
                    if (!this.reverseHurt) break;
                    iEntityPlayerSP.setSpeedInAir(((Number)this.reverse2StrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                this.reverseHurt = false;
                break;
            }
            case "aac": {
                if (!this.velocityInput || !this.velocityTimer.hasTimePassed(80L)) break;
                IEntityPlayerSP iEntityPlayerSP94 = iEntityPlayerSP;
                iEntityPlayerSP94.setMotionX(iEntityPlayerSP94.getMotionX() * ((Number)this.horizontalValue.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP95 = iEntityPlayerSP;
                iEntityPlayerSP95.setMotionZ(iEntityPlayerSP95.getMotionZ() * ((Number)this.horizontalValue.get()).doubleValue());
                this.velocityInput = false;
                break;
            }
            case "hyt": {
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP96 = iEntityPlayerSP;
                iEntityPlayerSP96.setMotionX(iEntityPlayerSP96.getMotionX() * (double)0.45f);
                IEntityPlayerSP iEntityPlayerSP97 = iEntityPlayerSP;
                iEntityPlayerSP97.setMotionZ(iEntityPlayerSP97.getMotionZ() * (double)0.65f);
                break;
            }
            case "hytpacket": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getHurtTime() > 5 || !iEntityPlayerSP.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP98 = iEntityPlayerSP;
                    iEntityPlayerSP98.setMotionX(iEntityPlayerSP98.getMotionX() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP99 = iEntityPlayerSP;
                    iEntityPlayerSP99.setMotionZ(iEntityPlayerSP99.getMotionZ() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP100 = iEntityPlayerSP;
                    iEntityPlayerSP100.setMotionY(iEntityPlayerSP100.getMotionY() / (double)1.781145f);
                    break;
                }
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP101 = iEntityPlayerSP;
                iEntityPlayerSP101.setMotionX(iEntityPlayerSP101.getMotionX() * 0.5);
                IEntityPlayerSP iEntityPlayerSP102 = iEntityPlayerSP;
                iEntityPlayerSP102.setMotionZ(iEntityPlayerSP102.getMotionZ() * 0.5);
                IEntityPlayerSP iEntityPlayerSP103 = iEntityPlayerSP;
                iEntityPlayerSP103.setMotionY(iEntityPlayerSP103.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotion": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getHurtTime() > 5 || !iEntityPlayerSP.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP104 = iEntityPlayerSP;
                    iEntityPlayerSP104.setMotionX(iEntityPlayerSP104.getMotionX() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP105 = iEntityPlayerSP;
                    iEntityPlayerSP105.setMotionZ(iEntityPlayerSP105.getMotionZ() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP106 = iEntityPlayerSP;
                    iEntityPlayerSP106.setMotionY(iEntityPlayerSP106.getMotionY() * (double)0.381145f);
                    IEntityPlayerSP iEntityPlayerSP107 = iEntityPlayerSP;
                    iEntityPlayerSP107.setMotionY(iEntityPlayerSP107.getMotionY() / (double)1.781145f);
                    break;
                }
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP108 = iEntityPlayerSP;
                iEntityPlayerSP108.setMotionX(iEntityPlayerSP108.getMotionX() * 0.4);
                IEntityPlayerSP iEntityPlayerSP109 = iEntityPlayerSP;
                iEntityPlayerSP109.setMotionZ(iEntityPlayerSP109.getMotionZ() * 0.4);
                IEntityPlayerSP iEntityPlayerSP110 = iEntityPlayerSP;
                iEntityPlayerSP110.setMotionY(iEntityPlayerSP110.getMotionY() * (double)0.381145f);
                IEntityPlayerSP iEntityPlayerSP111 = iEntityPlayerSP;
                iEntityPlayerSP111.setMotionY(iEntityPlayerSP111.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotionb": {
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getOnGround() || iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP112 = iEntityPlayerSP;
                iEntityPlayerSP112.setMotionX(iEntityPlayerSP112.getMotionX() * (double)0.451145f);
                IEntityPlayerSP iEntityPlayerSP113 = iEntityPlayerSP;
                iEntityPlayerSP113.setMotionZ(iEntityPlayerSP113.getMotionZ() * (double)0.451145f);
                break;
            }
            case "newhytmotion": {
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead() || iEntityPlayerSP.getOnGround()) break;
                if (!iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                    IEntityPlayerSP iEntityPlayerSP114 = iEntityPlayerSP;
                    iEntityPlayerSP114.setMotionX(iEntityPlayerSP114.getMotionX() * 0.47188);
                    IEntityPlayerSP iEntityPlayerSP115 = iEntityPlayerSP;
                    iEntityPlayerSP115.setMotionZ(iEntityPlayerSP115.getMotionZ() * 0.47188);
                    if (iEntityPlayerSP.getMotionY() != 0.42 && !(iEntityPlayerSP.getMotionY() > 0.42)) break;
                    IEntityPlayerSP iEntityPlayerSP116 = iEntityPlayerSP;
                    iEntityPlayerSP116.setMotionY(iEntityPlayerSP116.getMotionY() * 0.4);
                    break;
                }
                IEntityPlayerSP iEntityPlayerSP117 = iEntityPlayerSP;
                iEntityPlayerSP117.setMotionX(iEntityPlayerSP117.getMotionX() * 0.65025);
                IEntityPlayerSP iEntityPlayerSP118 = iEntityPlayerSP;
                iEntityPlayerSP118.setMotionZ(iEntityPlayerSP118.getMotionZ() * 0.65025);
                if (iEntityPlayerSP.getMotionY() != 0.42 && !(iEntityPlayerSP.getMotionY() > 0.42)) break;
                IEntityPlayerSP iEntityPlayerSP119 = iEntityPlayerSP;
                iEntityPlayerSP119.setMotionY(iEntityPlayerSP119.getMotionY() * 0.4);
                break;
            }
            case "aacpush": {
                if (this.jump) {
                    if (iEntityPlayerSP.getOnGround()) {
                        this.jump = false;
                    }
                } else {
                    if (iEntityPlayerSP.getHurtTime() > 0 && iEntityPlayerSP.getMotionX() != 0.0 && iEntityPlayerSP.getMotionZ() != 0.0) {
                        iEntityPlayerSP.setOnGround(true);
                    }
                    if (iEntityPlayerSP.getHurtResistantTime() > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue()) {
                        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                        if (module == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module.getState()) {
                            IEntityPlayerSP iEntityPlayerSP120 = iEntityPlayerSP;
                            iEntityPlayerSP120.setMotionY(iEntityPlayerSP120.getMotionY() - 0.014999993);
                        }
                    }
                }
                if (iEntityPlayerSP.getHurtResistantTime() < 19) break;
                float f = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP121 = iEntityPlayerSP;
                iEntityPlayerSP121.setMotionX(iEntityPlayerSP121.getMotionX() / (double)f);
                IEntityPlayerSP iEntityPlayerSP122 = iEntityPlayerSP;
                iEntityPlayerSP122.setMotionZ(iEntityPlayerSP122.getMotionZ() / (double)f);
                break;
            }
            case "custom": {
                if (iEntityPlayerSP.getHurtTime() <= 0 || iEntityPlayerSP.isDead()) break;
                IEntityPlayerSP iEntityPlayerSP123 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP123 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP123.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP124 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP124 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP124.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP125 = iEntityPlayerSP;
                iEntityPlayerSP125.setMotionX(iEntityPlayerSP125.getMotionX() * ((Number)this.customX.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP126 = iEntityPlayerSP;
                iEntityPlayerSP126.setMotionZ(iEntityPlayerSP126.getMotionZ() * ((Number)this.customZ.get()).doubleValue());
                if (((Boolean)this.customYStart.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP127 = iEntityPlayerSP;
                    iEntityPlayerSP127.setMotionY(iEntityPlayerSP127.getMotionY() / ((Number)this.customY.get()).doubleValue());
                }
                if (!((Boolean)this.customC06FakeLag.get()).booleanValue()) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getPosY(), iEntityPlayerSP.getPosZ(), iEntityPlayerSP.getRotationYaw(), iEntityPlayerSP.getRotationPitch(), iEntityPlayerSP.getOnGround()));
                break;
            }
            case "aaczero": {
                if (iEntityPlayerSP.getHurtTime() > 0) {
                    if (!this.velocityInput || iEntityPlayerSP.getOnGround() || iEntityPlayerSP.getFallDistance() > 2.0f) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP128 = iEntityPlayerSP;
                    iEntityPlayerSP128.setMotionY(iEntityPlayerSP128.getMotionY() - 1.0);
                    iEntityPlayerSP.setAirBorne(true);
                    iEntityPlayerSP.setOnGround(true);
                    break;
                }
                this.velocityInput = false;
                break;
            }
        }
    }

    @Override
    public String getTag() {
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return string2.toLowerCase().equals("testaac5") ? "Simple" : (String)this.modeValue.get();
    }
}

