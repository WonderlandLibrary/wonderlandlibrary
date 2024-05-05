package tech.atani.client.feature.module.impl.movement;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.rotation.RotationEvent;
import tech.atani.client.listener.event.minecraft.player.movement.MovePlayerEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.event.minecraft.world.CollisionBoxesEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.utility.player.movement.MoveUtil;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

@Native
@ModuleData(name = "Flight", description = "Makes you fly", category = Category.MOVEMENT)
public class Flight extends Module {
    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[]{"Vanilla", "Old NCP", "Collision", "Vulcan", "Grim", "Verus", "BWPractice"}),
            vulcanMode = new StringBoxValue("Vulcan Mode", "Which mode will the vulcan mode use?", this, new String[]{"Normal", "Clip & Glide", "Glide", "Vanilla"}, new Supplier[]{() -> mode.is("Vulcan")}),
            grimMode = new StringBoxValue("Grim Mode", "Which mode will the grim mode use?", this, new String[]{"Explosion", "Boat"}, new Supplier[]{() -> mode.is("Grim")}),
            verusMode = new StringBoxValue("Verus Mode", "Which mode will the verus mode use?", this, new String[]{"Damage", "Jump", "Collision"}, new Supplier[]{() -> mode.is("Verus")});
    private final SliderValue<Integer> time = new SliderValue<Integer>("Time", "How long will the flight fly?", this, 10, 3, 15, 0, new Supplier[]{() -> mode.is("Vulcan") && vulcanMode.is("Normal")});
    private final SliderValue<Float> timer = new SliderValue<Float>("Timer", "How high will be the timer when flying?", this, 0.2f, 0.1f, 0.5f, 1, new Supplier[]{() -> mode.is("Vulcan") && vulcanMode.is("Normal")}),
            speed = new SliderValue<Float>("Speed", "How fast will the fly be?", this, 1.4f, 0f, 10f, 1, new Supplier[]{() -> ((mode.is("Vulcan") && vulcanMode.is("Normal")) || mode.is("Vanilla"))});

    // Old NCP
    private double moveSpeed;
    private boolean jumped;

    // Vulcan
    private double startY;
    private int stage;
    public int jumps;
    private final TimeHelper glideTime = new TimeHelper();

    // Grim
    boolean velo = false;
    private boolean launch;

    // Verus
    private final TimeHelper verusTimer = new TimeHelper();
    private boolean verusUp;

    @Override
    public String getSuffix() {
    	return mode.getValue();
    }

    @Listen
    public void onCollisionBoxes(CollisionBoxesEvent boxesEvent) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        switch(mode.getValue()) {
            case "Verus":
                switch (verusMode.getValue()) {
                    case "Collision":
                        boxesEvent.setBoundingBox(new AxisAlignedBB(-5, -1, -5, 5, 1, 5).offset(boxesEvent.getBlockPos().getX(), boxesEvent.getBlockPos().getY(), boxesEvent.getBlockPos().getZ()));
                        break;
                }
                break;
            case "Collision":
                if(!mc.gameSettings.keyBindSneak.pressed)
                    boxesEvent.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(boxesEvent.getBlockPos().getX(), boxesEvent.getBlockPos().getY(), boxesEvent.getBlockPos().getZ()));
                break;
        }
    }

    @Listen
    public void onRotation(RotationEvent rotationEvent) {
        switch (this.mode.getValue()) {
            case "Grim":
                switch (this.grimMode.getValue()) {
                    case "Boat":
                        if(mc.thePlayer.isRiding() && mc.thePlayer.ridingEntity != null) {
                            if(mc.thePlayer.ridingEntity instanceof EntityBoat) {
                                EntityBoat boat = (EntityBoat) mc.thePlayer.ridingEntity;
                                float yaw = boat.rotationYaw;
                                float pitch = 90;
                                rotationEvent.setYaw(yaw);
                                rotationEvent.setPitch(pitch);
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Listen
    public void onUpdateMotion(UpdateMotionEvent motionEvent) {
        switch (mode.getValue()) {
            case "BWPractice":
                mc.thePlayer.motionY = 0.0D;
                MoveUtil.setMoveSpeed(0.2f);
                break;
            case "Verus":
                if (motionEvent.getType() == UpdateMotionEvent.Type.MID) {
                    switch (verusMode.getValue()) {
                        case "Collision":
                            if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (mc.thePlayer.onGround) {
                                    mc.thePlayer.motionY = 0.42f;
                                    verusUp = true;
                                } else if (verusUp) {
                                    if (!mc.thePlayer.isCollidedHorizontally) {
                                        mc.thePlayer.motionY = -0.0784000015258789;
                                    }
                                    verusUp = false;
                                }
                            } else if (mc.thePlayer.ticksExisted % 3 == 0) {
                                mc.thePlayer.motionY = 0.42f;
                            }
                            MoveUtil.setMoveSpeed(mc.gameSettings.keyBindJump.isKeyDown() ? 0 : 0.33);
                            break;
                        case "Damage":
                            if (mc.thePlayer.hurtTime != 0) {
                                MoveUtil.strafe(5);
                            }

                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.jump();
                                MoveUtil.strafe(0.4F);
                            }
                            break;

                        case "Jump":
                            if (verusTimer.hasReached(545, true)) {
                                mc.thePlayer.jump();
                                mc.thePlayer.onGround = true;
                            }
                            break;
                    }
                }
                break;
            case "Grim":
                if (motionEvent.getType() == UpdateMotionEvent.Type.MID) {
                    switch (grimMode.getValue()) {
                        case "Explosion":
                            if (velo) {
                                if (mc.thePlayer.hurtTime != 0) {
                                    mc.thePlayer.posY -= 100;
                                    mc.thePlayer.posX += 15;
                                    mc.thePlayer.posZ += 15;
                                    mc.thePlayer.jump();
                                    mc.thePlayer.motionY *= 1.022;
                                    mc.thePlayer.onGround = true;
                                }
                                velo = false;
                            }
                            break;
                        case "Boat":
                            if(mc.thePlayer.isRiding() && mc.thePlayer.ridingEntity instanceof EntityBoat) {
                                launch = true;
                            }
                            if(launch && !mc.thePlayer.isRiding()) {
                                mc.thePlayer.motionY = 1.5;
                                MoveUtil.strafe(1.5);
                                launch = false;
                            }
                            break;
                    }
                }
                break;
            case "Vanilla":
                moveSpeed = speed.getValue();

                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY = moveSpeed / 2;

                else if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY = -moveSpeed / 2;
                else
                    mc.thePlayer.motionY = 0;

                MoveUtil.strafe(moveSpeed);
                break;
            case "Old NCP":
                if (mc.thePlayer.onGround && !jumped) {
                    mc.thePlayer.jump();
                    jumped = true;
                }
                break;
            case "Vulcan":
                if (motionEvent.getType() == UpdateMotionEvent.Type.PRE) {
                    switch (vulcanMode.getValue()) {
                        case "Clip & Glide":
                            if(mc.thePlayer.onGround) {
                                mc.thePlayer.jump();
                            } else if(mc.thePlayer.fallDistance > 0.25) {
                                if(jumps < 3) {
                                    this.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (jumps == 0 ? 10 : 10), mc.thePlayer.posZ);
                                    jumps++;
                                } else {
                                    jumped = true;

                                    if(glideTime.hasReached(146, true)) {
                                        mc.thePlayer.motionY = -0.1476D;
                                    } else {
                                        mc.thePlayer.motionY = -0.0975D;
                                    }
                                }
                            }
                            break;
                        case "Normal":
                            stage++;

                            switch (stage) {
                                case 1:
                                    if (!mc.thePlayer.onGround) {
                                        sendMessage("You need to be on ground to do this!");
                                        toggle();
                                        return;
                                    }
                                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
                                    mc.timer.timerSpeed = timer.getValue();
                                    break;
                                default:
                                    if (stage == 2 && mc.thePlayer.posY != startY) {
                                        mc.thePlayer.setPosition(mc.thePlayer.posX, startY, mc.thePlayer.posZ);
                                    }
                                    if (stage < time.getValue()) {
                                        mc.thePlayer.motionY = 0;
                                        MoveUtil.strafe(speed.getValue());
                                    }

                                    if (stage >= time.getValue()) {
                                        MoveUtil.strafe(0);
                                        setEnabled(false);
                                    }
                                    break;
                            }
                            break;
                        case "Glide":
                            if (!mc.thePlayer.isInWater()) {
                                if (!mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 2 == 0) {
                                    mc.thePlayer.motionY = -0.1476D;
                                } else {
                                    mc.thePlayer.motionY = -0.0975D;
                                }
                            }
                            break;
                        case "Vanilla":
                            mc.thePlayer.onGround = true;
                            mc.thePlayer.motionY = 0.0;
                            break;
                    }
                }
                break;
        }
    }

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        switch (mode.getValue()) {
            case "BWPractice":
                if(packetEvent.getPacket() instanceof C0APacketAnimation) {
                    packetEvent.setCancelled(true);
                }

                if(packetEvent.getPacket() instanceof C19PacketResourcePackStatus) {
                    packetEvent.setCancelled(true);
                }

                if(packetEvent.getPacket() instanceof C14PacketTabComplete) {
                    packetEvent.setCancelled(true);
                }
                break;
            case "Grim":
                switch(grimMode.getValue()){
                    case "Explosion":
                        if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                                velo = true;
                            }
                        }
                        break;
                }
                break;
            case "Vulcan":
                switch(vulcanMode.getValue()){
                    case "Clip & Glide":
                        if(jumped && packetEvent.getPacket() instanceof C03PacketPlayer) {
                            C03PacketPlayer packet = (C03PacketPlayer) packetEvent.getPacket();

                            if(mc.thePlayer.ticksExisted % 11 == 0) {
                                packet.setOnGround(true);
                            }
                        }
                        break;
                    case "Vanilla":
                        if(packetEvent.getPacket() instanceof C03PacketPlayer) {
                            packetEvent.setCancelled(true);
                        }
                        break;
                }
                break;
        }
    }

    @Listen
    public void onMove(MovePlayerEvent movePlayerEvent) {
        switch (mode.getValue()) {
            case "Old NCP":
                if (!mc.thePlayer.onGround) {
                    movePlayerEvent.setY(mc.thePlayer.ticksExisted % 2 == 0 ? -1.0E-9 : 1.0E-9);
                    mc.thePlayer.motionY = 0;

                    if (moveSpeed >= MoveUtil.getBaseMoveSpeed()) {
                        moveSpeed -= moveSpeed / 102;
                    }

                    if (mc.thePlayer.isCollidedHorizontally || !this.isMoving()) {
                        moveSpeed = MoveUtil.getBaseMoveSpeed();
                    }

                    MoveUtil.strafe(moveSpeed);
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        stage = 0;
        jumps = 0;
        launch = false;
        jumped = false;
        startY = mc.thePlayer.posY;
        moveSpeed = speed.getValue();
    }

    @Override
    public void onDisable() {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }

        stage = 0;
        jumps = 0;
        moveSpeed = 0;
        jumped = false;
        launch = false;
        verusUp = false;
        Methods.mc.timer.timerSpeed = 1f;
        Methods.mc.thePlayer.speedInAir = 0.02f;
        MoveUtil.strafe(0);

        if(mode.is("Vulcan") && vulcanMode.is("Vanilla")) {
            this.setPosition(mc.thePlayer.posX + 0.01, mc.thePlayer.posY, mc.thePlayer.posZ + 0.01);
        }
    }
}
