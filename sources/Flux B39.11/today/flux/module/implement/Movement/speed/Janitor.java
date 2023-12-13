package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.potion.Potion;
import today.flux.module.implement.Movement.Speed;
import today.flux.event.MoveEvent;
import today.flux.module.SubModule;

public class Janitor extends SubModule {
    public Janitor() {
        super("Janitor", "Speed");
    }

    @EventTarget
    public void onMove(final MoveEvent event) {

        float speed = 0.05F;

        if(this.mc.thePlayer.onGround && (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F)){
            event.setY(mc.thePlayer.motionY = 0.4);
            speed = 0.704F;
        }

        if(!this.mc.thePlayer.onGround) {
            if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                speed = 0.704F;
            }
        }

        Speed.setMoveSpeed(event, speed);
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2872D;
        if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
        }

        return baseSpeed;
    }
}