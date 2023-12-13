package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.module.implement.Movement.Speed;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;
import today.flux.utility.BlockUtils;
import today.flux.utility.DelayTimer;

/**
 * Created by John on 2017/01/10.
 */
public class SlowHop extends SubModule {
    private double lastDist;
    private int stage;
    private double moveSpeed;
    private final double boostSpeed = 1.48D;

    public SlowHop() {
        super("SlowHop", "Speed");
    }

    public void onEnable() {
        super.onEnable();

        this.mc.thePlayer.stepHeight = 0;
        mc.timer.timerSpeed = 1;
        this.lastDist = 0;
        this.stage = 0;
        this.moveSpeed = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.timer.timerSpeed = 1;
        this.moveSpeed = Speed.getBaseMoveSpeed();
        this.mc.thePlayer.stepHeight = 0.6f;
    }

    @EventTarget
    public void onUpdate(PostUpdateEvent event) {
        if (this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava() || BlockUtils.stairCollision()) {
            mc.timer.timerSpeed = 1.0F;
            this.lastDist = 0.0D;
            return;
        }

        double movementInput = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double strafe = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(movementInput * movementInput + strafe * strafe);
    }

    private DelayTimer stepFix = new DelayTimer();

    @EventTarget
    public void onMove(MoveEvent event) {
        if (this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava() || this.mc.thePlayer.capabilities.isFlying) {
            mc.timer.timerSpeed = 1;
            return;
        }

        if (this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
            this.stage = 2;
            mc.timer.timerSpeed = 1;
        }

        if (this.stage != 2 || this.mc.thePlayer.moveForward == 0.0F && this.mc.thePlayer.moveStrafing == 0.0F) {
            if (this.stage == 3) {
                double forward = 0.66D * (this.lastDist - Speed.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - forward;

                if(this.moveSpeed < 0.3)
                    this.moveSpeed = 0.3;
            } else {
                if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0D, this.mc.thePlayer.motionY, 0.0D)).size() > 0 || this.mc.thePlayer.onGround) {
                    this.stage = 1;
                }

                this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }
        } else if(this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically){
            event.setY(this.mc.thePlayer.motionY = 0.42D);
            this.moveSpeed *= boostSpeed;
            mc.timer.timerSpeed = 1.06f;
        }

        this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed());
        Speed.setMoveSpeed(event, this.moveSpeed);

        ++this.stage;
    }
}

