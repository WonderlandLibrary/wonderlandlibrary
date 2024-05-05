package tech.dort.dortware.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.impl.events.PlayerMovementUpdateEvent;
import tech.dort.dortware.impl.utils.movement.MotionUtils;

/**
 * @author Auth
 */

public class Strafe extends Module {

    public Strafe(ModuleData moduleData) {
        super(moduleData);
    }

    @Subscribe
    public void onUpdate(PlayerMovementUpdateEvent event) {
        if (!mc.thePlayer.onGround && mc.thePlayer.hurtTime == 0) {
            MotionUtils.setMotion(MotionUtils.getSpeed());
        }
//        if (event.getState() != PlayerMovementUpdateEvent.State.JUMP) {
//            float strafe = event.getStrafe();
//            float forward = event.getForward();
//            float modifier = 1.3f;
//            float friction = event.getFriction() * modifier;
//            float var4 = strafe * strafe + forward * forward;
//
//            if (var4 >= 1.0E-4F) {
//                var4 = MathHelper.sqrt_float(var4);
//
//                if (var4 < 1.0F) {
//                    var4 = 1.0F;
//                }
//
//                var4 = friction / var4;
//                strafe *= var4;
//                forward *= var4;
//                float var5 = MathHelper.sin(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
//                float var6 = MathHelper.cos(mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
//                mc.thePlayer.motionX += (strafe * var6 - forward * var5);
//                mc.thePlayer.motionZ += (forward * var6 + strafe * var5);
//            }
//            MotionUtils.setMotion(Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ));
//            if (!mc.thePlayer.isMovingOnGround()) {
//                return;
//            }
//            mc.thePlayer.jump();
//        }
//        event.setCancelled(true);
    }
}
