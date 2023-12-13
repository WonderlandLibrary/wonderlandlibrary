package today.flux.module.implement.Movement.longjump;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.block.BlockGlass;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import today.flux.event.PacketSendEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.Movement.LongJump;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.utility.*;

/**
 * Created by John on 2016/12/16.
 */
public class Hypixel extends SubModule {
    private int stage;
    private double moveSpeed, lastDist;
    private double boost = 4.0;
    boolean hurt;

    public Hypixel() {
        super("Innominate", "LongJump");
        this.stage = 1;
        this.moveSpeed = 0.0006;
    }

    private double getBaseMoveSpeed() {
        return 0.1873;
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1;
        this.moveSpeed = this.getBaseMoveSpeed();
        this.lastDist = 0;
        this.stage = 4;
        this.jumped = false;
        super.onDisable();
    }

    private boolean jumped;

    @EventTarget
    public void onUpdate(PreUpdateEvent e) {
        mc.thePlayer.cameraYaw = 0;
        mc.thePlayer.cameraPitch = 0;
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (MathUtils.roundToPlace(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.41, 3)) {
            mc.thePlayer.motionY = 0;
        }
        if (this.mc.thePlayer.moveStrafing < 0 && this.mc.thePlayer.moveForward < 0) {
            this.stage = 1;
        }
        if (MathUtils.round(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3) == MathUtils.round(0.943, 3)) {
            mc.thePlayer.motionY = 0;
        }

        if (this.stage == 1  && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && this.mc.thePlayer.isCollidedVertically) {
            //start jump
            this.stage = 2;
            this.moveSpeed = (this.boost) * this.getBaseMoveSpeed() - 0.01;
        } else if (this.stage == 2) {
            this.stage = 3;
            this.mc.thePlayer.motionY = 0.399;
            event.y = 0.399;
            this.moveSpeed *= (1.7);

            this.jumped = true;
        } else if (this.stage == 3) {
            this.stage = 4;
            final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        } else if(this.stage == 4){
            //jumping...
            this.moveSpeed = this.lastDist - this.lastDist / 200;

            //GLIDE
            if(event.y <= 0.1) {
                event.y *= 0.1;
            }

            //if onground
            if (mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
                //end jump
                this.stage = 1;

                if(LongJump.autoToggle.getValue() && this.jumped) {
                    ModuleManager.longJumpMod.toggle();
                }
            }
        }

        //Smart Moving
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        float forward = this.mc.thePlayer.movementInput.moveForward;
        float strafe = this.mc.thePlayer.movementInput.moveStrafe;
        float yaw = this.mc.thePlayer.rotationYaw;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        } else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
        event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0;
            event.z = 0;
        }
    }

    @EventTarget
    public void onUpdate(PostUpdateEvent event) {
        double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
        double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

}
