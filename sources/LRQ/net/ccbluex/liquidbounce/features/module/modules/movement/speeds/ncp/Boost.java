/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class Boost
extends SpeedMode {
    private int motionDelay;
    private float ground;

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        double speed = 3.1981;
        double offset = 4.69;
        boolean shouldOffset = true;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        Collection<IAxisAlignedBB> collection = iWorldClient.getCollidingBoundingBoxes(thePlayer, thePlayer.getEntityBoundingBox().offset(thePlayer.getMotionX() / offset, 0.0, thePlayer.getMotionZ() / offset));
        boolean bl = false;
        if (!collection.isEmpty()) {
            shouldOffset = false;
        }
        if (thePlayer.getOnGround() && this.ground < 1.0f) {
            this.ground += 0.2f;
        }
        if (!thePlayer.getOnGround()) {
            this.ground = 0.0f;
        }
        if (this.ground == 1.0f && this.shouldSpeedUp()) {
            if (!thePlayer.getSprinting()) {
                offset += 0.8;
            }
            if (thePlayer.getMoveStrafing() != 0.0f) {
                speed -= 0.1;
                offset += 0.5;
            }
            if (thePlayer.isInWater()) {
                speed -= 0.1;
            }
            ++this.motionDelay;
            switch (this.motionDelay) {
                case 1: {
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * speed);
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * speed);
                    break;
                }
                case 2: {
                    IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                    iEntityPlayerSP4.setMotionX(iEntityPlayerSP4.getMotionX() / 1.458);
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setMotionZ(iEntityPlayerSP5.getMotionZ() / 1.458);
                    break;
                }
                case 4: {
                    if (shouldOffset) {
                        thePlayer.setPosition(thePlayer.getPosX() + thePlayer.getMotionX() / offset, thePlayer.getPosY(), thePlayer.getPosZ() + thePlayer.getMotionZ() / offset);
                    }
                    this.motionDelay = 0;
                }
            }
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean shouldSpeedUp() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isInLava()) return false;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP2.isOnLadder()) return false;
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP3.isSneaking()) return false;
        if (!MovementUtils.isMoving()) return false;
        return true;
    }

    public Boost() {
        super("Boost");
    }
}

