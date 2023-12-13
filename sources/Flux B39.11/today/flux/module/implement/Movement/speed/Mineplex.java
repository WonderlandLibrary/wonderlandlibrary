package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import today.flux.module.implement.Movement.Speed;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;
import today.flux.utility.BlockUtils;
import today.flux.utility.DelayTimer;
import today.flux.utility.MoveUtils;

/*
 * Created by coinful on 2021/12/04.
 */
public class Mineplex extends SubModule {
    private double lastDist;
    private int stage;
    private double moveSpeed;
    private final double boostSpeed = 1.48D;

    public Mineplex() {
        super("Mineplex", "Speed");
    }

    @Override
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
    public void onMove(MoveEvent event) {
        mc.timer.timerSpeed = 0.9f;
        if (MoveUtils.isMoving() && mc.thePlayer.onGround) {
            if (moveSpeed < 0.5) moveSpeed = 0.8;
            else moveSpeed += 0.5;
            event.setY(mc.thePlayer.motionY = 0.42F);
            //mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42F, mc.thePlayer.posZ, true));
        } else if (!MoveUtils.isMoving()) {
            moveSpeed = 0.45;
        }
        moveSpeed = Math.max(moveSpeed, 0.4);
        moveSpeed -= moveSpeed / 44;
        moveSpeed = Math.min(1.6, moveSpeed);
        if (mc.thePlayer.isCollidedHorizontally || !MoveUtils.isMoving())
            moveSpeed = 0.32;
        Speed.setMotion(event, moveSpeed);
    }
}
