/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AntiVoid", category=ModuleCategory.PLAYER, description="null")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010$\u001a\u00020\u0006H\u0002J\b\u0010%\u001a\u00020&H\u0016J\u0010\u0010'\u001a\u00020&2\u0006\u0010(\u001a\u00020)H\u0007J\u0010\u0010*\u001a\u00020&2\u0006\u0010(\u001a\u00020+H\u0007J\u0010\u0010,\u001a\u00020&2\u0006\u0010(\u001a\u00020-H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\u00170\u0016j\b\u0012\u0004\u0012\u00020\u0017`\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AntiVoid;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoScaffoldValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "blink", "", "canBlink", "canCancel", "canSpoof", "flagged", "lastRecY", "", "maxFallDistValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "motionX", "motionY", "motionZ", "motionflagValue", "packetCache", "Ljava/util/ArrayList;", "Lnet/minecraft/network/play/client/CPacketPlayer;", "Lkotlin/collections/ArrayList;", "posX", "posY", "posZ", "resetMotionValue", "startFallDistValue", "tag", "", "getTag", "()Ljava/lang/String;", "tried", "voidOnlyValue", "checkVoid", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "LiKingSense"})
public final class AntiVoid
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Blink", "TPBack", "MotionFlag", "PacketFlag", "GroundSpoof", "OldHypixel", "Jartex", "OldCubecraft", "Packet"}, "Blink");
    public final FloatValue maxFallDistValue = new FloatValue("MaxFallDistance", 10.0f, 5.0f, 20.0f);
    public final BoolValue resetMotionValue = new BoolValue("ResetMotion", false);
    public final FloatValue startFallDistValue = new FloatValue("BlinkStartFallDistance", 2.0f, 0.0f, 5.0f);
    public final BoolValue autoScaffoldValue = new BoolValue("BlinkAutoScaffold", true);
    public final FloatValue motionflagValue = new FloatValue("MotionFlag-MotionY", 1.0f, 0.0f, 5.0f);
    public final BoolValue voidOnlyValue = new BoolValue("OnlyVoid", true);
    public final ArrayList<CPacketPlayer> packetCache = new ArrayList();
    public boolean blink;
    public boolean canBlink;
    public boolean canCancel;
    public boolean canSpoof;
    public boolean tried;
    public boolean flagged;
    public double posX;
    public double posY;
    public double posZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public double lastRecY;

    @Override
    public void onEnable() {
        this.canCancel = 0;
        this.blink = 0;
        this.canBlink = 0;
        this.canSpoof = 0;
        this.lastRecY = MinecraftInstance.mc.getThePlayer() != null ? MinecraftInstance.mc.getThePlayer().getPosY() : 0.0;
        this.tried = 0;
        this.flagged = 0;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (this.lastRecY == 0.0) {
            this.lastRecY = MinecraftInstance.mc.getThePlayer().getPosY();
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.mc.getThePlayer().getOnGround()) {
            this.tried = 0;
            this.flagged = 0;
        }
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "groundspoof": {
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid()) break;
                this.canSpoof = MinecraftInstance.mc.getThePlayer().getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue() ? 1 : 0;
                break;
            }
            case "motionflag": {
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid() || !(MinecraftInstance.mc.getThePlayer().getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) || this.tried) break;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() + ((Number)this.motionflagValue.get()).doubleValue());
                MinecraftInstance.mc.getThePlayer().setFallDistance(0.0f);
                this.tried = 1;
                break;
            }
            case "packetflag": {
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid() || !(MinecraftInstance.mc.getThePlayer().getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) || this.tried) break;
                Minecraft minecraft = MinecraftInstance.mc2;
                Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc2");
                minecraft.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Position(MinecraftInstance.mc.getThePlayer().getPosX() + (double)1, MinecraftInstance.mc.getThePlayer().getPosY() + (double)1, MinecraftInstance.mc.getThePlayer().getPosZ() + (double)1, false));
                this.tried = 1;
                break;
            }
            case "tpback": {
                if (MinecraftInstance.mc.getThePlayer().getOnGround() && !(BlockUtils.getBlock(new WBlockPos(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY() - 1.0, MinecraftInstance.mc.getThePlayer().getPosZ())) instanceof BlockAir)) {
                    this.posX = MinecraftInstance.mc.getThePlayer().getPrevPosX();
                    this.posY = MinecraftInstance.mc.getThePlayer().getPrevPosY();
                    this.posZ = MinecraftInstance.mc.getThePlayer().getPrevPosZ();
                }
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && !this.checkVoid() || !(MinecraftInstance.mc.getThePlayer().getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) || this.tried) break;
                MinecraftInstance.mc.getThePlayer().setPositionAndUpdate(this.posX, this.posY, this.posZ);
                MinecraftInstance.mc.getThePlayer().setFallDistance(0.0f);
                MinecraftInstance.mc.getThePlayer().setMotionX(0.0);
                MinecraftInstance.mc.getThePlayer().setMotionY(0.0);
                MinecraftInstance.mc.getThePlayer().setMotionZ(0.0);
                this.tried = 1;
                break;
            }
            case "jartex": {
                this.canSpoof = 0;
                if ((!((Boolean)this.voidOnlyValue.get()).booleanValue() || this.checkVoid()) && MinecraftInstance.mc.getThePlayer().getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue() && MinecraftInstance.mc.getThePlayer().getPosY() < this.lastRecY + 0.01 && MinecraftInstance.mc.getThePlayer().getMotionY() <= (double)0 && !MinecraftInstance.mc.getThePlayer().getOnGround() && !this.flagged) {
                    MinecraftInstance.mc.getThePlayer().setMotionY(0.0);
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    iEntityPlayerSP.setMotionZ(iEntityPlayerSP.getMotionZ() * 0.838);
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * 0.838);
                    this.canSpoof = 1;
                }
                this.lastRecY = MinecraftInstance.mc.getThePlayer().getPosY();
                break;
            }
            case "oldcubecraft": {
                this.canSpoof = 0;
                if ((!((Boolean)this.voidOnlyValue.get()).booleanValue() || this.checkVoid()) && MinecraftInstance.mc.getThePlayer().getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue() && MinecraftInstance.mc.getThePlayer().getPosY() < this.lastRecY + 0.01 && MinecraftInstance.mc.getThePlayer().getMotionY() <= (double)0 && !MinecraftInstance.mc.getThePlayer().getOnGround() && !this.flagged) {
                    MinecraftInstance.mc.getThePlayer().setMotionY(0.0);
                    MinecraftInstance.mc.getThePlayer().setMotionZ(0.0);
                    MinecraftInstance.mc.getThePlayer().setMotionX(0.0);
                    MinecraftInstance.mc.getThePlayer().setJumpMovementFactor(0.0f);
                    this.canSpoof = 1;
                    if (!this.tried) {
                        this.tried = 1;
                        Minecraft minecraft = MinecraftInstance.mc2;
                        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc2");
                        minecraft.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Position(MinecraftInstance.mc.getThePlayer().getPosX(), 32000.0, MinecraftInstance.mc.getThePlayer().getPosZ(), false));
                    }
                }
                this.lastRecY = MinecraftInstance.mc.getThePlayer().getPosY();
                break;
            }
            case "packet": {
                if (this.checkVoid()) {
                    this.canCancel = 1;
                }
                if (!this.canCancel) break;
                if (MinecraftInstance.mc.getThePlayer().getOnGround()) {
                    for (CPacketPlayer packet : this.packetCache) {
                        Minecraft minecraft = MinecraftInstance.mc2;
                        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc2");
                        minecraft.func_147114_u().func_147297_a((Packet)packet);
                    }
                    this.packetCache.clear();
                }
                this.canCancel = 0;
                break;
            }
            case "blink": {
                if (!this.blink) {
                    FallingPlayer.CollisionResult collide = new FallingPlayer(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY(), MinecraftInstance.mc.getThePlayer().getPosZ(), 0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f).findCollision(60);
                    if (this.canBlink && (collide == null || MinecraftInstance.mc.getThePlayer().getPosY() - (double)collide.getPos().getY() > ((Number)this.startFallDistValue.get()).doubleValue())) {
                        this.posX = MinecraftInstance.mc.getThePlayer().getPosX();
                        this.posY = MinecraftInstance.mc.getThePlayer().getPosY();
                        this.posZ = MinecraftInstance.mc.getThePlayer().getPosZ();
                        this.motionX = MinecraftInstance.mc.getThePlayer().getMotionX();
                        this.motionY = MinecraftInstance.mc.getThePlayer().getMotionY();
                        this.motionZ = MinecraftInstance.mc.getThePlayer().getMotionZ();
                        this.packetCache.clear();
                        this.blink = 1;
                    }
                    if (!MinecraftInstance.mc.getThePlayer().getOnGround()) break;
                    this.canBlink = 1;
                    break;
                }
                if (MinecraftInstance.mc.getThePlayer().getFallDistance() > ((Number)this.maxFallDistValue.get()).floatValue()) {
                    MinecraftInstance.mc.getThePlayer().setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    if (((Boolean)this.resetMotionValue.get()).booleanValue()) {
                        MinecraftInstance.mc.getThePlayer().setMotionX(0.0);
                        MinecraftInstance.mc.getThePlayer().setMotionY(0.0);
                        MinecraftInstance.mc.getThePlayer().setMotionZ(0.0);
                        MinecraftInstance.mc.getThePlayer().setJumpMovementFactor(0.0f);
                    } else {
                        MinecraftInstance.mc.getThePlayer().setMotionX(this.motionX);
                        MinecraftInstance.mc.getThePlayer().setMotionY(this.motionY);
                        MinecraftInstance.mc.getThePlayer().setMotionZ(this.motionZ);
                        MinecraftInstance.mc.getThePlayer().setJumpMovementFactor(0.0f);
                    }
                    if (((Boolean)this.autoScaffoldValue.get()).booleanValue()) {
                        LiquidBounce.INSTANCE.getModuleManager().get(Scaffold.class).setState(true);
                    }
                    this.packetCache.clear();
                    this.blink = 0;
                    this.canBlink = 0;
                    break;
                }
                if (!MinecraftInstance.mc.getThePlayer().getOnGround()) break;
                this.blink = 0;
                for (CPacketPlayer packet : this.packetCache) {
                    Minecraft minecraft = MinecraftInstance.mc2;
                    Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc2");
                    minecraft.func_147114_u().func_147297_a((Packet)packet);
                }
                break;
            }
        }
    }

    public final boolean checkVoid() {
        boolean dangerous;
        for (int i = (int)(-(MinecraftInstance.mc.getThePlayer().getPosY() - 1.4857625)); i <= 0; ++i) {
            dangerous = MinecraftInstance.mc.getTheWorld().getCollisionBoxes(MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().offset(MinecraftInstance.mc.getThePlayer().getMotionX() * 0.5, i, MinecraftInstance.mc.getThePlayer().getMotionZ() * 0.5)).isEmpty();
            if (dangerous) continue;
            break;
        }
        return dangerous;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IPacket packet = event.getPacket();
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "blink": {
                if (!this.blink || !(packet instanceof CPacketPlayer)) break;
                this.packetCache.add((CPacketPlayer)packet);
                event.cancelEvent();
                break;
            }
            case "packet": {
                if (this.canCancel && packet instanceof CPacketPlayer) {
                    this.packetCache.add((CPacketPlayer)packet);
                    event.cancelEvent();
                }
                if (!(packet instanceof SPacketPlayerPosLook)) break;
                this.packetCache.clear();
                this.canCancel = 0;
                break;
            }
            case "groundspoof": {
                if (!this.canSpoof || !(packet instanceof CPacketPlayer)) break;
                ((CPacketPlayer)packet).field_149474_g = 1;
                break;
            }
            case "jartex": {
                if (this.canSpoof && packet instanceof CPacketPlayer) {
                    ((CPacketPlayer)packet).field_149474_g = 1;
                }
                if (!this.canSpoof || !(packet instanceof SPacketPlayerPosLook)) break;
                this.flagged = 1;
                break;
            }
            case "oldcubecraft": {
                if (this.canSpoof && packet instanceof CPacketPlayer && ((CPacketPlayer)packet).field_149477_b < 1145.14191981) {
                    event.cancelEvent();
                }
                if (!this.canSpoof || !(packet instanceof SPacketPlayerPosLook)) break;
                this.flagged = 1;
                break;
            }
            case "oldhypixel": {
                if (packet instanceof SPacketPlayerPosLook && (double)MinecraftInstance.mc.getThePlayer().getFallDistance() > 3.125) {
                    MinecraftInstance.mc.getThePlayer().setFallDistance(3.125f);
                }
                if (!(packet instanceof CPacketPlayer)) break;
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() && MinecraftInstance.mc.getThePlayer().getFallDistance() >= ((Number)this.maxFallDistValue.get()).floatValue() && MinecraftInstance.mc.getThePlayer().getMotionY() <= (double)0 && this.checkVoid()) {
                    ((CPacketPlayer)packet).field_149477_b += 11.0;
                }
                if (((Boolean)this.voidOnlyValue.get()).booleanValue() || !(MinecraftInstance.mc.getThePlayer().getFallDistance() >= ((Number)this.maxFallDistValue.get()).floatValue())) break;
                ((CPacketPlayer)packet).field_149477_b += 11.0;
                break;
            }
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

