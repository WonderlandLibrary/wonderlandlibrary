/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AACHop350
extends SpeedMode
implements Listenable {
    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @EventTarget
    public final void onMotion(MotionEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (event.getEventState() == EventState.POST && MovementUtils.isMoving() && !thePlayer.isInWater() && !thePlayer.isInLava()) {
            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
            iEntityPlayerSP2.setJumpMovementFactor(iEntityPlayerSP2.getJumpMovementFactor() + 0.00208f);
            if (thePlayer.getFallDistance() <= 1.0f) {
                if (thePlayer.getOnGround()) {
                    thePlayer.jump();
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * (double)1.0118f);
                    IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                    iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * (double)1.0118f);
                } else {
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() - (double)0.0147f);
                    IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                    iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() * (double)1.00138f);
                    IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                    iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() * (double)1.00138f);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.getOnGround()) {
            thePlayer.setMotionZ(0.0);
            thePlayer.setMotionX(thePlayer.getMotionZ());
        }
    }

    @Override
    public void onDisable() {
        block0: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setJumpMovementFactor(0.02f);
        }
    }

    @Override
    public boolean handleEvents() {
        return this.isActive();
    }

    public AACHop350() {
        super("AACHop3.5.0");
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
    }
}

