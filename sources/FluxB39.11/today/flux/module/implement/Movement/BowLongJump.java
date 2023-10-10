package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import today.flux.event.MoveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.utility.ChatUtils;
import today.flux.utility.MathUtils;
import today.flux.utility.PlayerUtils;

public class BowLongJump extends Module {
    boolean hasHurt = false;
    private int stage;
    private double moveSpeed, lastDist, boost = 4.0;
    private boolean jumped;

    int i, slotId, ticks;
    public BowLongJump() {
        super("BowJump", Category.Movement, false);
    }

    @Override
    public void onEnable() {
        stage = 1;
        moveSpeed = 0.1873;
        
        hasHurt = false;

        if (!mc.thePlayer.inventory.hasItem(Items.arrow)) {
            PlayerUtils.tellPlayer("Did not find arrows in your hotbar.");
            setEnabled(false);
            return;
        }

        ItemStack itemStack = null;

        for (i = 0; i < 9; i++) {
            itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemBow)
                break;
        }

        if (i == 9) {
            PlayerUtils.tellPlayer("Did not find a bow in your hotbar.");
            setEnabled(false);
            return;
        } else {
            slotId = mc.thePlayer.inventory.currentItem;
            if (i != slotId) {
                ChatUtils.debug("Switching slot from " + slotId + " to " + i);
                mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(i));
            }
            ticks = mc.thePlayer.ticksExisted;
            mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(itemStack));
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        lastDist = 0;
        stage = 4;
        jumped = false;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(PostUpdateEvent event) {
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if (mc.thePlayer.hurtTime == 9) {
            hasHurt = true;
        }
        
        if (!hasHurt) {
            if (mc.thePlayer.ticksExisted - ticks == 3) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, -89.5f, true));
                mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

                if (i != slotId) {
                    ChatUtils.debug("Switching back from " + i + " to " + slotId);
                    mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slotId));
                }
            }

            e.setX(0);
            if (e.getY() > 0)
                e.setY(0);
            e.setZ(0);
        } else {
            if (MathUtils.roundToPlace(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.41, 3)) {
                mc.thePlayer.motionY = 0;
            }
            if (mc.thePlayer.moveStrafing < 0 && mc.thePlayer.moveForward < 0) {
                stage = 1;
            }
            if (MathUtils.round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == MathUtils.round(0.943, 3)) {
                mc.thePlayer.motionY = 0;
            }

            if (stage == 1  && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) && mc.thePlayer.isCollidedVertically) {
                //start jump
                stage = 2;
                moveSpeed = (boost) * getBaseMoveSpeed() - 0.01;
            } else if (stage == 2) {
                stage = 3;
                mc.thePlayer.motionY = 0.64;
                e.y = 0.64;
                moveSpeed *= (2.149802);

                jumped = true;
            } else if (stage == 3) {
                stage = 4;
                final double difference = 0.66 * (lastDist - getBaseMoveSpeed());
                moveSpeed = lastDist - difference;
            } else if(stage == 4){
                // jumping...
                moveSpeed = lastDist - lastDist / 25.0;

                // if onground
                if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                    // end jump
                    stage = 1;

                    if (jumped) {
                        this.toggle();
                    }
                }
            }

            // Smart Moving
            moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
            float forward = mc.thePlayer.movementInput.moveForward;
            float strafe = mc.thePlayer.movementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;

            if (forward == 0.0f && strafe == 0.0f) {
                e.x = 0.0;
                e.z = 0.0;
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
            double mx = Math.cos(Math.toRadians(yaw + 90.0f));
            double mz = Math.sin(Math.toRadians(yaw + 90.0f));
            e.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
            e.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
            if (forward == 0.0f && strafe == 0.0f) {
                e.x = 0;
                e.z = 0;
            }
        }
    }

    private double getBaseMoveSpeed() {
        return 0.1873;
    }

}
