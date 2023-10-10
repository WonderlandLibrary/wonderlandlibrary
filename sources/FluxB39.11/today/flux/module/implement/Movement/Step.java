package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.event.*;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;

public class Step extends Module {
    public Step() {
        super("Step", Category.Movement, false);
    }
    public static boolean stepping = false;
    public static BooleanValue smooth = new BooleanValue("Step", "Smooth", false);

    @EventTarget
    public void onStep(StepEvent event) {
        if (event.isPre() && !mc.thePlayer.movementInput.jump && mc.thePlayer.isCollidedVertically) {
            event.setStepHeight(1.0D);
        } else if (!event.isPre() && event.getRealHeight() > 0.5D && event.getStepHeight() > 0.0D && !mc.thePlayer.movementInput.jump && mc.thePlayer.isCollidedVertically) {
            stepping = true;
            if (event.getRealHeight() >= 0.87D) {
                double realHeight = event.getRealHeight();
                double height1 = realHeight * 0.42D;
                double height2 = realHeight * 0.75D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height1, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height2, mc.thePlayer.posZ, mc.thePlayer.onGround));

            }

            if (smooth.getValueState()) {
                mc.timer.timerSpeed = 0.55F;
                (new Thread(() -> {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var1) {
                        ;
                    }
                    stepping = false;
                    mc.timer.timerSpeed = 1.0F;
                })).start();
            } else {
                stepping = false;
            }
        }
    }
}
