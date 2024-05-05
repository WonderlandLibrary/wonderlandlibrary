package club.dortware.client.module.impl.movement;

import club.dortware.client.Client;
import club.dortware.client.event.impl.BlockCollisionEvent;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.BooleanProperty;
import club.dortware.client.property.impl.DoubleProperty;
import club.dortware.client.property.impl.StringProperty;
import club.dortware.client.event.impl.MovementEvent;
import club.dortware.client.event.impl.PacketEvent;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.util.impl.networking.PacketUtil;
import club.dortware.client.util.impl.movement.MotionUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

@ModuleData(name = "Fly", description = "Allows you to fly like a bird", category = ModuleCategory.MOVEMENT, defaultKeyBind = Keyboard.KEY_G)
public class Flight extends Module {

    private float movementSpeed, lastSpeed;

    private boolean nigger, canFly;
    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new StringProperty<>("Mode", this, "Mineplex", "Vanilla", "Old AGC", "Hypixel", "Hypixel Damage", "Collide"));
        propertyManager.add(new DoubleProperty<>("Speed", this, 1.0, 0.2, 5));
        propertyManager.add(new BooleanProperty<>("Anti Kick", this, true));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
        float speed = ((Number) propertyManager.getProperty(this, "Speed").getValue()).floatValue();
        MovementInput movementInput = mc.thePlayer.movementInput;
        if (event.isPre()) {
            switch (mode) {
                case "Collide":
                    if (mc.thePlayer.isCollidedVertically && mc.thePlayer.ticksExisted % 2 == 0) {
                        movementSpeed = 2.12F;
                    }
                    break;
                case "Mineplex":
                    break;
                case "Old AGC":
                    if (mc.thePlayer.isMoving())
                        MotionUtils.setMotion(speed);
                    speed *= 0.5;
                    mc.thePlayer.motionY = movementInput.jump ? speed : movementInput.sneak ? -speed : -0.05;
                    break;
                case "Vanilla":
                    speed = 0.42F;
                    mc.thePlayer.motionY = movementInput.jump ? speed : movementInput.sneak ? -speed : 0.0F;
                    break;
            }
            double prevX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double prevZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastSpeed = (float) Math.hypot(prevX, prevZ);
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
        switch (mode) {
            case "Hypixel": {
                switch (event.getPacketDirection()) {
                    case INBOUND:
                        break;
                    case OUTBOUND:
                        break;
                }
            }
        }
    }

    @Subscribe
    public void onMove(MovementEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
        Number speed = (Number) propertyManager.getProperty(this, "Speed").getValue();
        switch (mode) {
            case "Hypixel":
                mc.timer.timerSpeed = 1F;
                if (mc.thePlayer.isMovingOnGround()) {
                    event.setMotionY(MotionUtils.getMotion(0.42F));
                    movementSpeed = MotionUtils.getBaseSpeed() * 2.149999388F;
                    MotionUtils.damage();
                    nigger = true;
                }
                else {
                    if (mc.thePlayer.ticksExisted % 2 != 0)
                        event.setMotionY(mc.thePlayer.motionY = 0.00118212);
                    else
                        event.setMotionY(mc.thePlayer.motionY = -0.00118212);
                    if (nigger)
                        movementSpeed = 1.4F;
                    else
                        movementSpeed -= movementSpeed / 159.1F;
                    if (mc.thePlayer.movementInput.jump)
                        event.setMotionY(0.5);
                    nigger = false;
                }

                MotionUtils.setMotion(event, movementSpeed);

                break;
            case "Collide":
                if (mc.thePlayer.isCollidedVertically && nigger) {
                    MotionUtils.setMotion(event, movementSpeed -= movementSpeed / 129);
                }
                if (!nigger) nigger = true;
                break;
            case "Mineplex":
                break;
            case "Vanilla":
//                PacketUtil.sendPacketSilent(new C0DPacketCloseWindow());
//                for (int i = 0; i < 60; ++i) {
//                    PacketUtil.sendPacketSilent(new C0CPacketInput(21, 0, false, false));
//                }
                MotionUtils.setMotion(event, speed.floatValue());
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onCollision(BlockCollisionEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();

        switch (mode) {
            case "Collide":
                if (event.getBlock() instanceof BlockAir) {
                    if (mc.thePlayer.isSneaking())
                        return;
                    double x = event.getX();
                    double y = event.getY();
                    double z = event.getZ();
                    if (y < mc.thePlayer.posY) {
                        event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-5, -1, -5, 5, 1.0F, 5).offset(x, y, z));
                    }
                }
                break;
        }

    }

    @Override
    public void onEnable() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
        //TODO add to switch block when modes require this
        switch (mode) {
            case "Hypixel":
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.22534D, mc.thePlayer.posZ, false));
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.04534D, mc.thePlayer.posZ, false));
                canFly = false;
                break;
            case "Collide":
                lastSpeed = (float) mc.thePlayer.posY;
                mc.thePlayer.motionY = 0.5F;
                MotionUtils.setMotion(1.2F);
                nigger = false;
                break;
            case "Vanilla":
                lastSpeed = 0.42F;
                mc.thePlayer.motionY = 0.42F;
                break;
        }
        super.onEnable();
    }
}