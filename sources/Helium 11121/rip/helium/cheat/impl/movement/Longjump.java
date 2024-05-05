package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.MovementUtils;
import rip.helium.utils.SpeedUtils;
import rip.helium.utils.Vec3d;
import rip.helium.utils.property.impl.StringsProperty;

public class Longjump extends Cheat {
    private final StringsProperty mode;
    int i = 0;
    private double mineplexSpeed;
    private boolean done;
    private boolean back;
    private int setback;

    public Longjump() {
        super("LongJump", "Longjump class retard", CheatCategory.MOVEMENT);
        this.mode = new StringsProperty("Mode", "How this cheat will function.", null, false, false, new String[]{"Vanilla", "Mineplex", "Fierce", "SunPvP"}, new Boolean[]{true, false, false, false});
        this.registerProperties(mode);
    }

    public static void placeHeldItemUnderPlayer() {
        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1,
                mc.thePlayer.posZ);
        final Vec3d vec = new Vec3d(blockPos).addVector(0.4F, 0.4F, 0.4F);
        mc.playerController.onPlayerRightClic(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP,
                vec.scale(0.4));
    }

    public void onEnable() {

        this.back = false;
        this.done = false;
        this.mineplexSpeed = 0.2;
        //ChatUtil.chat("Credit to Dort!");
    }

    public void onDisable() {
        Timer.timerSpeed = 1f;
        SpeedUtils.setPlayerSpeed(0);
    }

    @Collect
    public void onPlayerMove(PlayerMoveEvent event) {
        switch (mode.getSelectedStrings().get(0)) {
            case "Mineplex": {
                if (Speed.airSlot() == -10) {
                    SpeedUtils.setMoveSpeed(event, 0);
                    return;
                }
                if (!done) {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(Speed.airSlot()));
                    placeHeldItemUnderPlayer();
                    SpeedUtils.setMoveSpeed(event, back ? -mineplexSpeed : mineplexSpeed);
                    back = !back;
                    if (mc.thePlayer.isMovingOnGround() && mc.thePlayer.ticksExisted % 2 == 0) {
                        mineplexSpeed += 0.135;
                    }
                    if (mineplexSpeed >= 2.5 && mc.thePlayer.isMovingOnGround()) {
                        event.setY(mc.thePlayer.motionY = 0.42f);
                        SpeedUtils.setMoveSpeed(event, 0);
                        done = true;
                    }
                } else {
                    mc.thePlayer.motionY += mc.thePlayer.fallDistance == 0 ? 0.038 : mc.thePlayer.fallDistance > 1.4 ? 0.032 : 0;
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        Timer.timerSpeed = 0.1f;
                        SpeedUtils.setPlayerSpeed(2500.5);
                    } else {
                        Timer.timerSpeed = 0.7f;
                        SpeedUtils.setPlayerSpeed(5.1);
                    }
                    SpeedUtils.setMoveSpeed(event, mineplexSpeed *= (mineplexSpeed < 1 ? 1.19 : mineplexSpeed < 2 ? 0.985 : mineplexSpeed < 2.5 ? 0.972 : 0.97));
                    if (mc.thePlayer.isMovingOnGround()) {
                        done = false;
                    }
                    if (mc.thePlayer.onGround) {
                        SpeedUtils.setPlayerSpeed(0);
                    }
                }
                break;
            }
            case "Vanilla": {
                if (mc.thePlayer.onGround) {
                    event.setY(mc.thePlayer.motionY = 0.4);
                }
                SpeedUtils.setPlayerSpeed(event, 9.2);
                break;
            }
            case "Fierce": {
                double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                double x = -Math.sin(yaw) * 5.9;
                double z = Math.cos(yaw) * 5.9;
                //mc.thePlayer.motionY = 0;
                mc.thePlayer.lastReportedPosY = 0;
                //mc.thePlayer.onGround = false;

                if (mc.thePlayer.onGround) {
                    event.setY(mc.thePlayer.motionY = 0.369432141234234);
                }

                if (mc.thePlayer.ticksExisted % 5 == 0 && mc.gameSettings.keyBindForward.pressed) {
                    Timer.timerSpeed = 1f;
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                } else if (mc.thePlayer.ticksExisted % 2 == 0 && !mc.gameSettings.keyBindForward.pressed && !mc.thePlayer.onGround) {
                    Timer.timerSpeed = 1f;
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                    SpeedUtils.setPlayerSpeed(0f);
                } else {
                    SpeedUtils.setPlayerSpeed(0f);
                }
                break;
            }
            case "SunPvP":
                double sunspeed = MovementUtils.getBaseMoveSpeed();
                if (mc.thePlayer.isMoving()) {
                    MovementUtils.setSpeed(event, 9.3);
                    if (!TargetStrafe.doStrafeAtSpeed(event, 9.3)) {
                        MovementUtils.setSpeed(event, 9.3);
                        if (mc.thePlayer.onGround) {
                            event.setY(mc.thePlayer.motionY = 0.42); // 3999998
                            //  moveSpeed = sunspeed * 2.15 - 1.0E-4;
                        }
                    }
                    break;
                }
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        switch (mode.getSelectedStrings().get(0)) {
            case "Faithful": {
                if (mc.thePlayer.onGround) {
                    event.setPosY(mc.thePlayer.motionY = 0.7);
                }
                SpeedUtils.setPlayerSpeed(2.5);
                break;
            }
        }
    }

    @Collect
    public void onPacketSend(SendPacketEvent event) {
        switch (mode.getSelectedStrings().get(0)) {
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
        }
    }

    private void setSpeed(final float speed) {
        final EntityPlayerSP player = mc.thePlayer;
        double yaw = player.rotationYaw;
        final boolean isMoving = player.moveForward != 0.0f || player.moveStrafing != 0.0f;
        final boolean isMovingForward = player.moveForward > 0.0f;
        final boolean isMovingBackward = player.moveForward < 0.0f;
        final boolean isMovingRight = player.moveStrafing > 0.0f;
        final boolean isMovingLeft = player.moveStrafing < 0.0f;
        final boolean isMovingSideways = isMovingLeft || isMovingRight;
        final boolean isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0;
            } else if (isMovingBackward && !isMovingSideways) {
                yaw += 180.0;
            } else if (isMovingForward && isMovingLeft) {
                yaw += 45.0;
            } else if (isMovingForward) {
                yaw -= 45.0;
            } else if (!isMovingStraight && isMovingLeft) {
                yaw += 90.0;
            } else if (!isMovingStraight && isMovingRight) {
                yaw -= 90.0;
            } else if (isMovingBackward && isMovingLeft) {
                yaw += 135.0;
            } else if (isMovingBackward) {
                yaw -= 135.0;
            }
            yaw = Math.toRadians(yaw);
            player.motionX = -Math.sin(yaw) * speed;
            player.motionZ = Math.cos(yaw) * speed;
        }
    }

    @Collect
    public void onUpdate(PlayerUpdateEvent event) {
        setMode(mode.getSelectedStrings().get(0));


    }

}
