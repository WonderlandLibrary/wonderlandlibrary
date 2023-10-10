package today.flux.module.implement.Movement.longjump;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import today.flux.event.PreUpdateEvent;
import today.flux.module.implement.Movement.LongJump;
import today.flux.module.implement.Movement.Speed;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.utility.*;

/**
 * Created by John on 2016/12/16.
 */
public class Dev extends SubModule {
    public Dev() {
        super("Dev", "LongJump");
    }


    private final TimeHelper boostTimer = new TimeHelper();
    private int stage;
    private double moveSpeed, lastDist;
    private boolean damaged;

    @Override
    public void onEnable() {
        this.stage = 0;
        this.lastDist = 0;
        this.moveSpeed = 0;
        this.damaged = false;
        this.boostTimer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.motionX = 0.0D;
        mc.thePlayer.motionZ = 0.0D;
        super.onDisable();
    }

    @EventTarget
    public void onMove(MoveEvent moveEvent) {
        final double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.71D : 1.83D;
        if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
            if (!damaged) {
                moveEvent.setY(mc.thePlayer.motionY = 0.42);
                EntityUtils.damagePlayer(1);
                damaged = true;
            }
        }

        if (PlayerUtils.isMoving()) {
            switch (stage) {
                case 0:
                    this.moveSpeed = boost * MoveUtils.defaultSpeed();
                    break;
                case 1:
                    this.moveSpeed *= 2.0D;
                    break;
                default:
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                    break;
            }

            this.moveSpeed = Math.max(this.moveSpeed, MoveUtils.defaultSpeed());

            PlayerUtils.setSpeed(this.moveSpeed);
            stage++;
        }
    }

    @EventTarget
    public final void onUpdate(PreUpdateEvent event) {
        if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
            event.setY(event.getY() + 0.0007435);
        }

        if (stage > 1) {
            mc.thePlayer.motionY = 0.0D;
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                event.setY(event.getY() + 0.0000023);
            } else {
                event.setY(event.getY() - 0.0000023);
            }
        }

        if (boostTimer.isDelayComplete(100) && !boostTimer.isDelayComplete(500)) {
            mc.timer.timerSpeed = 2.2F;
            boostTimer.reset();
        } else {
            mc.timer.timerSpeed = 1.0F;
        }

        double x = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
        double z = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;

        this.lastDist = Math.sqrt(x * x + z * z);
    }
}
