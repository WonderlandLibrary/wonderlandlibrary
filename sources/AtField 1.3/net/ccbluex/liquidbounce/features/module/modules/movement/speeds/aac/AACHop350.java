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
    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (motionEvent.getEventState() == EventState.POST && MovementUtils.isMoving() && !iEntityPlayerSP2.isInWater() && !iEntityPlayerSP2.isInLava()) {
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
            iEntityPlayerSP3.setJumpMovementFactor(iEntityPlayerSP3.getJumpMovementFactor() + 0.00208f);
            if (iEntityPlayerSP2.getFallDistance() <= 1.0f) {
                if (iEntityPlayerSP2.getOnGround()) {
                    iEntityPlayerSP2.jump();
                    IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                    iEntityPlayerSP4.setMotionX(iEntityPlayerSP4.getMotionX() * (double)1.0118f);
                    IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                    iEntityPlayerSP5.setMotionZ(iEntityPlayerSP5.getMotionZ() * (double)1.0118f);
                } else {
                    IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
                    iEntityPlayerSP6.setMotionY(iEntityPlayerSP6.getMotionY() - (double)0.0147f);
                    IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                    iEntityPlayerSP7.setMotionX(iEntityPlayerSP7.getMotionX() * (double)1.00138f);
                    IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                    iEntityPlayerSP8.setMotionZ(iEntityPlayerSP8.getMotionZ() * (double)1.00138f);
                }
            }
        }
    }

    @Override
    public void onMotion() {
    }

    @Override
    public boolean handleEvents() {
        return this.isActive();
    }

    public AACHop350() {
        super("AACHop3.5.0");
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
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
    public void onUpdate() {
    }

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (iEntityPlayerSP2.getOnGround()) {
            iEntityPlayerSP2.setMotionZ(0.0);
            iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionZ());
        }
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }
}

