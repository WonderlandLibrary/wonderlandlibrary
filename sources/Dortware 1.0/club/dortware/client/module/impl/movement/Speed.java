package club.dortware.client.module.impl.movement;

import club.dortware.client.Client;
import club.dortware.client.event.impl.PacketEvent;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.DoubleProperty;
import club.dortware.client.property.impl.StringProperty;
import club.dortware.client.event.impl.MovementEvent;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.util.impl.movement.MotionUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

@ModuleData(name = "Speed", description = "Makes your base speed faster", category = ModuleCategory.MOVEMENT, defaultKeyBind = Keyboard.KEY_F)
public class Speed extends Module {

    private boolean lastDistanceReset, reset;
    private float movementSpeed;
    private float lastDistance;

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new StringProperty<>("Mode", this, "Hypixel", "Mineplex", "YPort", "NCP", "Verus"));
        propertyManager.add(new DoubleProperty<>("Speed", this, 1.2F, 0.125F, 10F));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
        switch (mode) {
            case "YPort": {
//                double y = event.getPosY() + 0.00000000427273;
//                event.setPosY(y);
                if (lastDistanceReset) {
//                    event.setPosY(event.getPosY() + 0.42F);
                }
                break;
            }
            case "Verus":
                if (mc.thePlayer.ticksExisted % 16 == 0) {
                    event.setPosY(event.getPosY() + 0.06446);
                }
                break;
            case "Hypixel": {
                //position is only used on pre and reset to real AFTER post so no need to check.
                event.setOnGround(true);
                if (lastDistanceReset) {
                }
                break;
            }
        }

        if (event.isPre()) {
            double x = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double z = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDistance = (float) Math.hypot(x, z);
        }

    }

    @Subscribe
    public void onMove(MovementEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
        float speed = ((Double) propertyManager.getProperty(this, "Speed").getValue()).floatValue();
        switch (mode) {
            case "Mineplex": {
                mc.timer.timerSpeed = 1.0f;
                if (mc.thePlayer.isMovingOnGround()) {
                    mc.timer.timerSpeed = 2.49F;
                    if (reset)
                        movementSpeed = 0.8F;
                    else
                        movementSpeed += 0.6F;
                    lastDistanceReset = true;
                    reset = false;
                    event.setMotionY(mc.thePlayer.motionY = 0.42F);
                    MotionUtils.setMotion(event, 1.0E-5F);
                }
                else {
                    if (lastDistanceReset)
                        lastDistance = movementSpeed;
                    lastDistanceReset = false;
                    if (movementSpeed <= 0.8F)
                        movementSpeed = lastDistance - 0.01F;
                    else if (movementSpeed < 2.2F)
                        movementSpeed = lastDistance * 0.9825F;
                    else
                        movementSpeed = lastDistance * 0.97F;
                    movementSpeed = Math.min(speed + 0.053125F, movementSpeed);
                    movementSpeed = Math.max(movementSpeed, MotionUtils.getBaseSpeed() + 0.1627F);
                    MotionUtils.setMotion(event, movementSpeed);
                }
                break;
            }
            case "Verus":
                mc.timer.timerSpeed = 1.0F;
                if (mc.thePlayer.ticksExisted % 16 == 0) {
                    mc.timer.timerSpeed = 1.3F;
                    MotionUtils.setMotion(event, 0);
                    movementSpeed += 0.5F;
                    return;
                }
                movementSpeed *= 0.98F;
                movementSpeed = Math.min(speed + 0.053125F, movementSpeed);
                movementSpeed = Math.max(movementSpeed, MotionUtils.getBaseSpeed() + 0.1027F);
                MotionUtils.setMotion(event, movementSpeed);
                break;
            case "Hypixel":
                MotionUtils.setMotion(event, 0.5F);
                if (mc.thePlayer.isMovingOnGround()) {
                    event.setMotionY(mc.thePlayer.motionY = 0.5F);
                    lastDistanceReset = true;
                }
                else {
                    event.setMotionY(mc.thePlayer.motionY = 0.5F);
                    if (lastDistanceReset) {
                        event.setMotionY(0.5F);
//                        mc.thePlayer.motionY;
                        lastDistanceReset = false;
                    } else {
                        if (mc.thePlayer.ticksExisted % 4 != 0) {
                            movementSpeed *= 0.97;
                        }
                        else {
                            movementSpeed *= 0.99;
                        }
                    }
                }
//                MotionUtils.setMotion(event, Math.max(MotionUtils.getBaseSpeed() - 0.4F, movementSpeed));
                break;
            case "YPort":
                lastDistanceReset = mc.thePlayer.ticksExisted % 3 == 0 && mc.thePlayer.isCollidedVertically;
                if (lastDistanceReset) {
                    mc.timer.timerSpeed = 0.9F;
                    movementSpeed = 0.84F;
                }
                else {
                    movementSpeed = MotionUtils.getBaseSpeed();
                    mc.timer.timerSpeed = 1.16F;
                }
                movementSpeed = Math.max(movementSpeed, MotionUtils.getBaseSpeed());
                MotionUtils.setMotion(event, movementSpeed);
                break;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (event.getPacketDirection()) {
            case INBOUND:
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    toggle();
                }
                break;
            case OUTBOUND:
                break;
        }
    }

    @Override
    public void onEnable() {
        reset = true;
        movementSpeed = MotionUtils.getBaseSpeed();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
