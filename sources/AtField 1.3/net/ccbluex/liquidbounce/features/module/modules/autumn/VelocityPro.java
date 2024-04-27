/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.autumn;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="VelocityPro", description="Skid.", category=ModuleCategory.AUTUMN)
public final class VelocityPro
extends Module {
    private final BoolValue hytGround;
    private MSTimer velocityTimer;
    private final FloatValue reverseStrengthValue;
    private final FloatValue verticalValue;
    private final FloatValue aacPushXZReducerValue;
    private final BoolValue customYStart;
    private final FloatValue customZ;
    private final FloatValue hytpacketbset;
    private final BoolValue aacPushYReducerValue;
    private final FloatValue customY;
    private boolean canCancelJump;
    private final BoolValue customC06FakeLag;
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
    private boolean reverseHurt;
    private boolean velocityInput;
    private boolean jump;
    private final FloatValue reverse2StrengthValue;
    private final FloatValue customX;
    private final FloatValue newaac4XZReducerValue;
    private final ListValue modeValue;
    private final IntegerValue velocityTickValue;
    private boolean huayutingjumpflag;
    private int velocityTick;
    private boolean canCleanJump;
    private final BoolValue noFireValue;
    private final FloatValue hytpacketaset;
    private IBlock block;

    public VelocityPro() {
        this.verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
        this.modeValue = new ListValue("Mode", new String[]{"Jump", "GrimVelocity", "Autumn"}, "GrimVelocity");
        this.newaac4XZReducerValue = new FloatValue("NewAAC4XZReducer", 0.45f, 0.0f, 1.0f);
        this.velocityTickValue = new IntegerValue("VelocityTick", 1, 0, 10);
        this.reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f);
        this.reverse2StrengthValue = new FloatValue("SmoothReverseStrength", 0.05f, 0.02f, 0.1f);
        this.hytpacketaset = new FloatValue("HytPacketASet", 0.35f, 0.1f, 1.0f);
        this.hytpacketbset = new FloatValue("HytPacketBSet", 0.5f, 1.0f, 1.0f);
        this.aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f);
        this.aacPushYReducerValue = new BoolValue("AACPushYReducer", true);
        this.noFireValue = new BoolValue("noFire", false);
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
        String string3 = string2.toLowerCase();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isInLava() || iEntityPlayerSP2.isInWeb()) {
            return;
        }
        if (((Boolean)this.noFireValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.isBurning()) {
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
            case "autumn": {
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP4.getOnGround()) break;
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP5.getHurtTime() > 0) {
                    if (MinecraftInstance.mc.getThePlayer() == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP6.getMotionX() != 0.0) {
                        if (MinecraftInstance.mc.getThePlayer() == null) {
                            Intrinsics.throwNpe();
                        }
                        IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP7 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iEntityPlayerSP7.getMotionZ() != 0.0) {
                            if (MinecraftInstance.mc.getThePlayer() == null) {
                                Intrinsics.throwNpe();
                            }
                            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP8 == null) {
                                Intrinsics.throwNpe();
                            }
                            iEntityPlayerSP8.setOnGround(true);
                        }
                    }
                }
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP9.getHurtResistantTime() > 0) {
                    if (MinecraftInstance.mc.getThePlayer() == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP10.setMotionY(iEntityPlayerSP11.getMotionY() - 0.014999993);
                }
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP12 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP12.getHurtResistantTime() < 19) break;
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP13 == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP14 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP13.setMotionX(iEntityPlayerSP14.getMotionX() / (double)1.5f);
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP15 == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP16 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP15.setMotionZ(iEntityPlayerSP16.getMotionZ() / (double)1.5f);
                break;
            }
            case "GrimVelocity": {
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                if (!MinecraftInstance.mc2.field_71439_g.field_70122_E) break;
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.mc2.field_71439_g.field_70737_aN <= 0) break;
                IEntityPlayerSP iEntityPlayerSP17 = iEntityPlayerSP2;
                iEntityPlayerSP17.setMotionX(iEntityPlayerSP17.getMotionX() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP18 = iEntityPlayerSP2;
                iEntityPlayerSP18.setMotionY(iEntityPlayerSP18.getMotionY() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP19 = iEntityPlayerSP2;
                iEntityPlayerSP19.setMotionZ(iEntityPlayerSP19.getMotionZ() + -1.0E-7);
                iEntityPlayerSP2.setAirBorne(true);
                break;
            }
            case "jump": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || !iEntityPlayerSP2.getOnGround()) break;
                iEntityPlayerSP2.setMotionY(0.42);
                float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP20 = iEntityPlayerSP2;
                double d = iEntityPlayerSP20.getMotionX();
                IEntityPlayerSP iEntityPlayerSP21 = iEntityPlayerSP20;
                boolean bl2 = false;
                float f2 = (float)Math.sin(f);
                iEntityPlayerSP21.setMotionX(d - (double)f2 * 0.2);
                IEntityPlayerSP iEntityPlayerSP22 = iEntityPlayerSP2;
                d = iEntityPlayerSP22.getMotionZ();
                iEntityPlayerSP21 = iEntityPlayerSP22;
                bl2 = false;
                f2 = (float)Math.cos(f);
                iEntityPlayerSP21.setMotionZ(d + (double)f2 * 0.2);
                break;
            }
        }
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

    public final IBlock getBlock() {
        return this.block;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntityVelocity(iPacket)) {
            Object object;
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
            String string3 = string2.toLowerCase();
        }
    }
}

