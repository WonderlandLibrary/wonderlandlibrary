/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class MineplexGround
extends SpeedMode {
    private boolean spoofSlot;
    private float speed;

    /*
     * WARNING - void declaration
     */
    @Override
    public void onMotion() {
        block10: {
            block9: {
                if (!MovementUtils.isMoving()) break block9;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getOnGround()) break block9;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP2.getInventory().getCurrentItemInHand() == null) break block9;
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP3.isUsingItem()) break block10;
            }
            return;
        }
        this.spoofSlot = false;
        int n = 36;
        int n2 = 44;
        while (n <= n2) {
            void i;
            IItemStack itemStack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if ((itemStack = iEntityPlayerSP.getInventory().getStackInSlot((int)i)) == null) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange((int)(i - 36)));
                this.spoofSlot = true;
                break;
            }
            ++i;
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onUpdate() {
        float targetSpeed;
        void y$iv$iv;
        void x$iv$iv;
        void this_$iv$iv;
        void y$iv22;
        void x$iv;
        WVec3 this_$iv;
        block19: {
            block18: {
                if (!MovementUtils.isMoving()) break block18;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getOnGround()) break block18;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP2.isUsingItem()) break block19;
            }
            this.speed = 0.0f;
            return;
        }
        if (!this.spoofSlot) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getInventory().getCurrentItemInHand() != null) {
                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lMineplex\u00a7aSpeed\u00a78] \u00a7cYou need one empty slot.");
                return;
            }
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getPosX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = iEntityPlayerSP3.getEntityBoundingBox().getMinY() - 1.0;
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        WBlockPos blockPos = new WBlockPos(d, d2, iEntityPlayerSP4.getPosZ());
        WVec3 wVec3 = new WVec3(blockPos);
        double d3 = 0.4;
        double d4 = 0.4;
        double z$iv = 0.4;
        boolean $i$f$addVector = false;
        this_$iv = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv22, this_$iv.getZCoord() + z$iv);
        WVec3 vec$iv = new WVec3(MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP).getDirectionVec());
        boolean $i$f$add = false;
        WVec3 y$iv22 = this_$iv;
        double d5 = vec$iv.getXCoord();
        double d6 = vec$iv.getYCoord();
        double z$iv$iv = vec$iv.getZCoord();
        boolean $i$f$addVector2 = false;
        WVec3 vec = new WVec3(this_$iv$iv.getXCoord() + x$iv$iv, this_$iv$iv.getYCoord() + y$iv$iv, this_$iv$iv.getZCoord() + z$iv$iv);
        IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
        IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP5 == null) {
            Intrinsics.throwNpe();
        }
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        iPlayerControllerMP.onPlayerRightClick(iEntityPlayerSP5, iWorldClient, null, blockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP), new WVec3(vec.getXCoord() * (double)0.4f, vec.getYCoord() * (double)0.4f, vec.getZCoord() * (double)0.4f));
        Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (speed == null) {
            Intrinsics.throwNpe();
        }
        if ((targetSpeed = ((Number)speed.getMineplexGroundSpeedValue().get()).floatValue()) > this.speed) {
            this.speed += targetSpeed / (float)8;
        }
        if (this.speed >= targetSpeed) {
            this.speed = targetSpeed;
        }
        MovementUtils.strafe(this.speed);
        if (!this.spoofSlot) {
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP6.getInventory().getCurrentItem()));
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onDisable() {
        this.speed = 0.0f;
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP.getInventory().getCurrentItem()));
    }

    public MineplexGround() {
        super("MineplexGround");
    }
}

