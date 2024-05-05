package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.MathUtils;
import rip.helium.utils.MovementUtils;
import rip.helium.utils.SpeedUtils;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

public class Speed extends Cheat {

    Stopwatch timer = new Stopwatch();
    int i = 0;
    DoubleProperty speed = new DoubleProperty("Speed", "Changes the speed of vanilla speed mode", null, 1.1, 0.1, 5.0, 0.1, null);
    private double moveSpeed;
    private final StringsProperty prop_mode;
    private int stage;
    private boolean doSlow;

    public Speed() {
        super("Speed", "Makes you move faster.", CheatCategory.MOVEMENT);
        this.prop_mode = new StringsProperty("Mode", "change the mode.", null, false, true,
                new String[]{"Vanilla", "VanillaBhop", "SunPvP", "Hypixel", "FaithfulHop", "YPort"},
                new Boolean[]{true, false, false, false, false, false});
        this.registerProperties(this.prop_mode, speed);
    }

    public static int airSlot() {
        final Minecraft mc = Minecraft.getMinecraft();
        for (int j = 0; j < 8; ++j) {
            if (mc.thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        return -10;
    }

    public static void setSpeed(final double speed) {
        mc.thePlayer.motionX = -MathHelper.sin(getDirection1()) * speed;
        mc.thePlayer.motionZ = MathHelper.cos(getDirection1()) * speed;
    }

    public static float getDirection1() {
        float yaw = mc.thePlayer.rotationYawHead;
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Timer.timerSpeed = 1.0f;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        doSlow = false;
        moveSpeed = 0;
    }


    @Collect
    public void onPlayerMove(PlayerMoveEvent event) {
        setMode(prop_mode.getSelectedStrings().get(0));
        switch (prop_mode.getSelectedStrings().get(0)) {
            case "Vanilla":
                if (mc.thePlayer.isMoving()) {
                    MovementUtils.setSpeed(event, speed.getValue());
                    if (!TargetStrafe.doStrafeAtSpeed(event, speed.getValue())) {
                        MovementUtils.setSpeed(event, speed.getValue());
                    }
                }
                break;
            case "Hypixel":
                if (MathUtils.round(mc.thePlayer.motionY, 3) == MathUtils.round(-.245D, 3)) {
                    event.setY(mc.thePlayer.motionY -= 0.125F);
                }

                if (mc.thePlayer.isMoving() && !mc.thePlayer.isInWater()) {
                    double baseSpeed = MovementUtils.getBaseMoveSpeed();
                    if (mc.thePlayer.onGround) {
                        event.setY(mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.3999998F)); // 3999998
                        moveSpeed = baseSpeed * 2.15 - 1.0E-4;
                        doSlow = true;
                    } else if (doSlow || mc.thePlayer.isCollidedHorizontally) {
                        moveSpeed -= 0.6336 * (moveSpeed - baseSpeed);
                        doSlow = false;
                    } else {
                        moveSpeed -= moveSpeed / 159;
                    }

                    if (mc.thePlayer.isCollidedHorizontally || !mc.thePlayer.isMoving())
                        moveSpeed = MovementUtils.getBaseMoveSpeed();

                    MovementUtils.setSpeed(event, Math.max(moveSpeed, baseSpeed));
                    if (!TargetStrafe.doStrafeAtSpeed(event, Math.max(moveSpeed, baseSpeed))) {
                        MovementUtils.setSpeed(event, Math.max(moveSpeed, baseSpeed));
                    }
                }
                break;
            case "VanillaBhop":
                double baseSpeed = MovementUtils.getBaseMoveSpeed();
                if (mc.thePlayer.isMoving()) {
                    MovementUtils.setSpeed(event, speed.getValue());
                    if (!TargetStrafe.doStrafeAtSpeed(event, speed.getValue())) {
                        MovementUtils.setSpeed(event, speed.getValue());
                        if (mc.thePlayer.onGround) {
                            event.setY(mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.41999998F)); // 3999998
                            moveSpeed = baseSpeed * 2.15 - 1.0E-4;
                            doSlow = true;
                        }
                    }
                    break;
                }
            case "FaithfulHop":
                double faithfulSpeed = MovementUtils.getBaseMoveSpeed();
                if (mc.thePlayer.isMoving()) {
                    MovementUtils.setSpeed(event, speed.getValue());
                    if (!TargetStrafe.doStrafeAtSpeed(event, speed.getValue())) {
                        MovementUtils.setSpeed(event, speed.getValue());
                        if (mc.thePlayer.onGround) {
                            event.setY(mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.21999998F)); // 3999998
                            moveSpeed = faithfulSpeed * 2.15 - 1.0E-4;
                            doSlow = true;
                        }
                    }
                    break;
                }
            case "SunPvP":
                double sunspeed = MovementUtils.getBaseMoveSpeed();
                if (mc.thePlayer.isMoving()) {
                    MovementUtils.setSpeed(event, speed.getValue());
                    if (!TargetStrafe.doStrafeAtSpeed(event, speed.getValue())) {
                        MovementUtils.setSpeed(event, speed.getValue());
                        if (mc.thePlayer.onGround) {
                            event.setY(mc.thePlayer.motionY = 0.42); // 3999998
                            //  moveSpeed = sunspeed * 2.15 - 1.0E-4;
                            doSlow = true;
                        }
                    }
                    break;
                }
        }
    }

    @Collect
    public void onUpdatePacket(PlayerUpdateEvent event) {
        switch (prop_mode.getSelectedStrings().get(0)) {
            //  case "SunPvP": {
            //event.setOnGround(true);
            case "YPort": {
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.fallDistance < 0.2) {
                        mc.thePlayer.motionY = -5f;
                    }
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }
                    if (!TargetStrafe.doStrafeAtUpdate(event, speed.getValue())) {
                        SpeedUtils.setPlayerSpeed(speed.getValue());
                    }
                }
                break;
            }
        }
    }
//}

    @Collect
    public void onPacketProcess(ProcessPacketEvent event) {
        switch (prop_mode.getSelectedStrings().get(0)) {
            case "Viper": {
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) event.getPacket();

                    playerPosLook.y += 1.0E-4F;
                }
                break;
            }
        }
    }

    @Collect
    public void onPacketSend(SendPacketEvent event) {
        switch (prop_mode.getSelectedStrings().get(0)) {
            case "Faithful": {
                if (mc.thePlayer.isEating() && !Helium.instance.cheatManager.isCheatEnabled("Speed") && !Helium.instance.cheatManager.isCheatEnabled("Flight") && mc.thePlayer.isSneaking()) {
                    return;

                }
                if (getMc().thePlayer != null && event.getPacket() instanceof C03PacketPlayer) {
                    event.setCancelled(true);
                    if (i > 2) {
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(getMc().thePlayer.prevPosX + ((getMc().thePlayer.posX - getMc().thePlayer.prevPosX) / 3), getMc().thePlayer.prevPosY + ((getMc().thePlayer.posY - getMc().thePlayer.prevPosY) / 3), getMc().thePlayer.prevPosZ + ((getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ) / 3),
                                getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch, true));
                        i = 0;
                    } else {
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvent(new C00PacketKeepAlive(-Integer.MAX_VALUE));
                    }
                    i++;
                }
                break;
            }
            case "Viper": {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
                    if (mc.thePlayer.ticksExisted < 50) {

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition());
                    } else {

                        packetPlayer.y += 0.42F;
                    }
                }
                break;
            }
        }
    }
}