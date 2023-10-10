package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.potion.Potion;
import today.flux.module.implement.Movement.Speed;
import today.flux.event.MoveEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.SubModule;

import java.util.List;

/**
 * Created by Admin on 2017/01/30.
 */

public class Latest extends SubModule {
    private double moveSpeed;
    private double lastDist;
    private double stage;

    public Latest() {
        super("Latest", "Speed");
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (this.mc.thePlayer != null) {
            this.moveSpeed = this.getBaseMoveSpeed();
        }
        this.lastDist = 0.0;
        this.stage = 2.0;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.timer.timerSpeed = 1.00f;
        this.moveSpeed = this.getBaseMoveSpeed();
        mc.thePlayer.motionX = 0.0D;
        mc.thePlayer.motionZ = 0.0D;
        this.mc.thePlayer.stepHeight = 0.6f;
    }

    @EventTarget
    public void onMove(final MoveEvent event) {
        if ((this.mc.thePlayer.onGround || this.stage == 3.0)) {
            if ((!this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.moveForward != 0.0f) || this.mc.thePlayer.moveStrafing != 0.0f) {
                if (this.stage == 2.0) {
                    this.moveSpeed *= 1.48f;
                    this.stage = 3.0;

                    mc.timer.timerSpeed = 1.08f;
                } else if (this.stage == 3.0) {
                    this.stage = 2.0;

                    final double difference = 0.88 * (this.lastDist - this.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                    mc.timer.timerSpeed = 1.08f;
                } else {
                    final List collidingList = mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0));
                    if (collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically) {
                        this.stage = 1.0;
                    }
                }
            } else {
                mc.timer.timerSpeed = 1.00f;
            }

            Speed.setMoveSpeed(event, this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed()));
        }
    }

    @EventTarget
    public void onUpdate(final PreUpdateEvent event) {
        if (this.stage == 3.0) {
            event.y += 0.3994;
        }

        final double xDist = mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
        final double zDist = mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2872D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
        }

        return baseSpeed;
    }
}
