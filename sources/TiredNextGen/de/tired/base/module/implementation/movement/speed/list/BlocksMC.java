package de.tired.base.module.implementation.movement.speed.list;

import de.tired.util.hook.PlayerHook;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.implementation.combat.KillAura;
import de.tired.base.module.implementation.movement.speed.SpeedExtension;
import de.tired.base.module.implementation.movement.speed.SpeedModeAnnotation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@SpeedModeAnnotation(name = "BlocksMC")
public class BlocksMC extends SpeedExtension {

    private boolean needsFlagFix = false;

    private int flagTicks = 0;

    @Override
    public void onPre(EventPreMotion eventPreMotion) {
        if (needsFlagFix) {
            MC.timer.timerSpeed = 1F;
            flagTicks = 20;
        }
        if (flagTicks > 0)
            flagTicks--;

        if (!PlayerHook.isMoving()) return;
            if (MC.thePlayer.motionY >= .2) {
                if (MC.gameSettings.keyBindRight.pressed) {
                    PlayerHook.increaseSpeedWithStrafe(PlayerHook.getSpeed(), MC.thePlayer.rotationYaw);
                }
            }
            if (MC.thePlayer.motionY <= .2)
                PlayerHook.setupStrafe();
            if (MC.thePlayer.onGround) {
                MC.thePlayer.motionX *= .92F;
                MC.thePlayer.motionZ *= .92F;
                MC.thePlayer.jump();
                MC.timer.timerSpeed = 1.1F;
            } else {
                if (MC.thePlayer.motionY >= .37) {
                    MC.thePlayer.motionY = PlayerHook.getPredictedMotionY(MC.thePlayer.motionY);
                    eventPreMotion.y = PlayerHook.getPredictedMotionY(MC.thePlayer.motionY);
                    final float yaw = KillAura.getInstance().isState() ? KillAura.getInstance().serverRotations[0] : MC.thePlayer.rotationYaw;
                    MC.thePlayer.motionX = -Math.sin(Math.toRadians(yaw)) * .17;
                    MC.thePlayer.motionZ = Math.cos(Math.toRadians(yaw)) * .17;
                }
        }
    }


    @Override
    public void onPacket(PacketEvent eventPacket) {
        if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook) {
            needsFlagFix = true;
          //  eventPacket.setCancelled(true);
      //      sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, MC.thePlayer.onGround));
        }
    }

    @Override
    public void onUpdate(UpdateEvent eventUpdate) {
    }


    @Override
    public void onEnable() {
        needsFlagFix = false;

    }

    @Override
    public void onDisable() {
        MC.thePlayer.ticksExisted = 0;
        MC.timer.timerSpeed = 1;
        flagTicks = 0;
        needsFlagFix = false;
    }

}
