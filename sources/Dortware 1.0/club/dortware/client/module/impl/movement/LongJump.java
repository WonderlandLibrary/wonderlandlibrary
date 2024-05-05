package club.dortware.client.module.impl.movement;

import club.dortware.client.Client;
import club.dortware.client.event.impl.MovementEvent;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.StringProperty;
import club.dortware.client.util.impl.networking.PacketUtil;
import club.dortware.client.util.impl.movement.MotionUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;

@ModuleData(name = "Long Jump", category = ModuleCategory.MOVEMENT)
public class LongJump extends Module {

    private float hSpeed;
    private float ySpeed;

    private boolean done;

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new StringProperty<>("Mode", this, "Mineplex", "NCP", "Hypixel"));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();

        switch (mode) {
            case "NCP": {
                break;
            }
            case "Hypixel": {
                break;
            }
            case "Mineplex": {
                if (!done && mc.thePlayer.ticksExisted % 2 == 0 && event.isPre()) {

                }
                break;
            }
        }

    }

    @Subscribe
    public void onMove(MovementEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();

        switch (mode) {
            case "NCP":
            case "Hypixel": {
                break;
            }
            case "Mineplex": {
                double flySpeed = 1.3F;
                if (airSlot() == -10) {
                    return;
                }
                if (!this.done && mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(airSlot()));
                    BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1.0D, mc.thePlayer.posZ);
                    Vec3 vec = new Vec3(blockPos).addVector(0.4000000059604645D, 0.4000000059604645D, 0.4000000059604645D);
                    mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4000000059604645D, vec.yCoord * 0.4000000059604645D, vec.zCoord * 0.4000000059604645D));
                    this.hSpeed += 0.135D;
                    MotionUtils.setMotion(event, mc.thePlayer.ticksExisted % 2 == 0 ? -this.hSpeed : this.hSpeed);
                    if (this.hSpeed >= flySpeed) {
                        MotionUtils.setMotion(event, 0.0F);
                        event.setMotionY(mc.thePlayer.motionY = 0.42F);
                        this.done = true;
                        return;
                    }
                } else {
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    this.hSpeed *= 0.98D;
                    MovementInput movementInput = mc.thePlayer.movementInput;
                    mc.thePlayer.motionY = movementInput.jump ? 0.42F : movementInput.sneak ? -0.42F : 0;
//                    if (mc.thePlayer.fallDistance == 0) {
                        event.setMotionY(mc.thePlayer.motionY);
//                    }
//                    else if (mc.thePlayer.fallDistance < 1.4D) {
//                        event.setMotionY(mc.thePlayer.motionY += 0.03D);
//                    }
                    if (hSpeed < 0.5F) {
                        hSpeed = 0.4F;
                    }
                    MotionUtils.setMotion(event, this.hSpeed);
                }
                break;

            }
        }
    }

    @Override
    public void onEnable() {
        hSpeed = 0.26F;
        done = false;
    }

    @Override
    public void onDisable() {

    }

    private int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (mc.thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        return -10;
    }
}
