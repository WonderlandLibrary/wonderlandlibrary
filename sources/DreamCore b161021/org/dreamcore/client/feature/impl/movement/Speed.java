/* 3eLeHyy#0089 */

package org.dreamcore.client.feature.impl.movement;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.motion.EventMove;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Speed extends Feature {

    public static int stage;
    private final BooleanSetting strafing;
    private final BooleanSetting boost;
    private final List<Packet<?>> packets;
    private final List<double[]> positions;
    private final TimerHelper pulseTimer = new TimerHelper();
    private final BooleanSetting potionCheck;
    public TimerHelper timerHelper = new TimerHelper();
    public double moveSpeed;
    public ListSetting speedMode;
    public BooleanSetting autoHitDisable;
    public NumberSetting jumpMoveFactor = new NumberSetting("Custom Speed", 0.0265F, 0.01F, 0.1F, 0.001F, () -> speedMode.currentMode.equals("Custom"));
    public BooleanSetting onGround = new BooleanSetting("Ground Only", false, () -> speedMode.currentMode.equals("Custom"));
    public NumberSetting onGroundSpeed = new NumberSetting("Custom Ground Speed", 0.5F, 0.001F, 10, 0.01F, () -> speedMode.currentMode.equals("Custom") && onGround.getBoolValue());
    public NumberSetting motionY = new NumberSetting("Custom Y-Motion", 0.42F, 0.01F, 1, 0.01F, () -> speedMode.currentMode.equals("Custom"));
    public BooleanSetting blink = new BooleanSetting("Blink", false, () -> speedMode.currentMode.equals("Custom"));
    public BooleanSetting timerExploit = new BooleanSetting("Timer Exploit", false, () -> !speedMode.currentMode.equals("Matrix Old"));
    public NumberSetting timer = new NumberSetting("Custom Timer", 1, 0.1F, 10, 0.1F, () -> speedMode.currentMode.equals("Custom") && !timerExploit.getBoolValue());
    public NumberSetting speed = new NumberSetting("Speed", 1, 0.1F, 10, 0.1F, () -> speedMode.currentMode.equals("Motion") || speedMode.currentMode.equals("MatrixGround 6.2.2"));
    private int boostTick;
    private boolean disableLogger;

    public Speed() {
        super("Speed", "Увеличивает вашу скорость", Type.Movement);

        speedMode = new ListSetting("Speed Mode", "Matrix 6.2.2", () -> true, "Matrix Timer", "Matrix 6.2.2", "Matrix 6.3.0", "MatrixGround 6.2.2", "Motion", "Old Sunrise", "MatrixHop", "MatrixOnGround", "NCP LowHop", "Custom");

        this.strafing = new BooleanSetting("Strafing", false, () -> true);
        this.boost = new BooleanSetting("GroundSpoof", true, () -> speedMode.currentMode.equals("Matrix Old") || speedMode.currentMode.equals("Custom"));
        this.potionCheck = new BooleanSetting("Speed Potion Check", false, () -> speedMode.currentMode.equals("Custom"));
        this.autoHitDisable = new BooleanSetting("Auto Hit Disable", false, () -> true);
        this.packets = new ArrayList<>();
        this.positions = new LinkedList<>();
        addSettings(speedMode, strafing, boost, speed, jumpMoveFactor, motionY, onGroundSpeed, onGround, blink, timerExploit, timer, potionCheck, autoHitDisable);
    }

    @EventTarget
    public void onMove(EventMove eventMove) {
        if (speedMode.currentMode.equals("NCP LowHop")) {
            if (MovementHelper.isMoving() && !mc.player.isInLiquid()) {
                if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && mc.player.jumpTicks == 0) {
                    mc.timer.timerSpeed = 1.09F;
                    eventMove.setY(mc.player.motionY = 0.1);
                    mc.player.jumpTicks = 10;
                } else if (eventMove.getY() < 0) {
                    mc.timer.timerSpeed = 1.04f;
                }

                float speed = mc.player.isSprinting() ? 0.97F : 1;
                double moveSpeed = Math.max(MovementHelper.getBaseMoveSpeed(), MovementHelper.getSpeed()) * speed;
                MovementHelper.setEventSpeed(eventMove, moveSpeed);
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (this.getState()) {
            String mode = speedMode.getOptions();
            this.setSuffix(mode);

            if (mode.equals("Matrix 6.3.0")) {
                if (MovementHelper.isMoving()) {
                    if (mc.player.onGround) {
                        this.boostTick = 11;
                        mc.player.jump();
                    } else if (this.boostTick < 11) {
                        this.boostTick++;
                    } else {
                        if (timerHelper.hasReached(460F)) {
                            mc.player.motionX *= 1 + 0.7;
                            mc.player.motionZ *= 1 + 0.7;
                            timerHelper.reset();
                        }
                        this.boostTick = 0;
                    }
                }
            }

            if (mode.equalsIgnoreCase("Motion")) {
                if (mc.player.onGround && mc.gameSettings.keyBindForward.pressed) {
                    mc.player.jump();
                }
                mc.player.jumpMovementFactor = speed.getNumberValue();
            }

            if (mode.equalsIgnoreCase("MatrixGround 6.2.2")) {
                if (mc.player.onGround && mc.gameSettings.keyBindForward.pressed) {
                    mc.player.jump();
                    mc.player.onGround = false;
                }

                if (mc.player.motionY > 0 && !mc.player.isInWater()) {
                    mc.player.motionY -= speed.getNumberValue();
                }
            } else if (mode.equalsIgnoreCase("Custom")) {
                if (!mc.player.isPotionActive(MobEffects.SPEED) && potionCheck.getBoolValue())
                    return;
                if (mc.player.onGround && !onGround.getBoolValue() && MovementHelper.isMoving()) {
                    if (boost.getBoolValue()) {
                        mc.player.onGround = false;
                    }
                    mc.player.motionY = motionY.getNumberValue();
                }
                if (strafing.getBoolValue()) {
                    MovementHelper.strafePlayer(MovementHelper.getSpeed());
                }
                mc.player.jumpMovementFactor = jumpMoveFactor.getNumberValue();
                if (onGround.getBoolValue()) {
                    MovementHelper.setSpeed(onGroundSpeed.getNumberValue());
                }
                if (!timerExploit.getBoolValue()) {
                    mc.timer.timerSpeed = timer.getNumberValue();
                }
                if (timerExploit.getBoolValue()) {
                    mc.timer.timerSpeed = mc.player.ticksExisted % 60 > 39 ? 1000 : 1;
                }
            } else if (mode.equalsIgnoreCase("Matrix 6.2.2")) {
                if (strafing.getBoolValue()) {
                    MovementHelper.strafePlayer(MovementHelper.getSpeed());
                }
                double x = mc.player.posX;
                double y = mc.player.posY;
                double z = mc.player.posZ;
                double yaw = mc.player.rotationYaw * 0.017453292;
                if (MovementHelper.isMoving() && mc.player.fallDistance < 0.1) {
                    ItemStack stack = mc.player.getHeldItemOffhand();
                    if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isCollidedHorizontally) {
                        return;
                    }
                    boolean isSpeed = mc.player.isPotionActive(MobEffects.SPEED);
                    boolean isRune = stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("небесных врат");
                    boolean isRuneAndSpeed = stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("небесных врат") && mc.player.isPotionActive(MobEffects.SPEED);
                    if (mc.player.onGround) {
                        this.boostTick = 8;
                        mc.player.jump();
                    } else if (this.boostTick < 8) {
                        mc.player.jumpMovementFactor *= 1.04F;
                        if (this.boostTick == 0) {
                            double motion = isRuneAndSpeed ? 1.67 : isSpeed ? 1.7 : isRune ? 1.7 : 1.7605;
                            mc.player.motionX *= motion;
                            mc.player.motionZ *= motion;
                            mc.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
                        } else {
                            mc.player.motionY -= 0.0098;
                        }
                        this.boostTick++;
                    } else {
                        this.boostTick = 0;
                    }
                } else if (mode.equalsIgnoreCase("Matrix 6.0.4")) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                    } else {
                        if (mc.player.ticksExisted % 5 == 0) {
                            mc.player.jumpMovementFactor = 0.0F;
                            mc.timer.timerSpeed = 0.6F;
                        }

                        if (mc.player.ticksExisted % 5 == 0) {
                            mc.player.jumpMovementFactor = 0.28F;
                            mc.timer.timerSpeed = 1.0F;
                        }

                        if (mc.player.ticksExisted % 10 == 0) {
                            mc.player.jumpMovementFactor = 0.38F;
                        }

                        if (mc.player.ticksExisted % 20 == 0) {
                            mc.player.jumpMovementFactor = 0.35F;
                            mc.timer.timerSpeed = 1.1F;
                        }
                    }
                }
            } else if (mode.equalsIgnoreCase("Old Sunrise")) {
                if (mc.player.onGround) {
                    mc.player.jump();
                } else {
                    if (mc.player.ticksExisted % 5 == 0) {
                        mc.player.jumpMovementFactor = 0.0F;
                        mc.timer.timerSpeed = 0.6F;
                    }

                    if (mc.player.ticksExisted % 5 == 0) {
                        mc.player.jumpMovementFactor = 0.28F;
                        mc.timer.timerSpeed = 1.0F;
                    }

                    if (mc.player.ticksExisted % 10 == 0) {
                        mc.player.jumpMovementFactor = 0.38F;
                    }

                    if (mc.player.ticksExisted % 20 == 0) {
                        mc.player.jumpMovementFactor = 0.35F;
                        mc.timer.timerSpeed = 1.3F;
                    }
                }
            } else if (mode.equalsIgnoreCase("Matrix Timer")) {
                mc.gameSettings.keyBindJump.pressed = false;
                if (strafing.getBoolValue()) {
                    MovementHelper.strafePlayer(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving()) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                    }
                    if (!mc.player.onGround && mc.player.fallDistance <= 0.1F) {
                        mc.timer.timerSpeed = 1.21F;
                    }
                    if (mc.player.fallDistance > 0.1F && mc.player.fallDistance < 1.3F) {
                        mc.timer.timerSpeed = 1F;
                    }
                }
            } else if (mode.equalsIgnoreCase("MatrixOnGround")) {
                //3eLeHyy / MyLifeIsShit
                if (mc.player.onGround) {
                    mc.timer.timerSpeed = 1.05F;
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1.00000000000E-14F, mc.player.posZ);
                    mc.player.motionX *= 1.55F;
                    mc.player.motionZ *= 1.55F;
                    mc.player.motionY = 0f;
                    mc.player.onGround = true;
                } else if (mc.player.fallDistance > 0) {
                    mc.player.motionY = -10000;
                }
            } else if (mode.equalsIgnoreCase("MatrixHop")) {
                if (mc.gameSettings.keyBindForward.isKeyDown()) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                        mc.timer.timerSpeed = 1.05F;
                        mc.player.motionX *= 1.0071;
                        mc.player.motionZ *= 1.0071;
                        mc.player.moveStrafing *= 2;
                    } else {
                        if (!mc.player.onGround && mc.player.fallDistance <= 0.1F) {
                            mc.player.motionY -= 0.0098;
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            if (boost.getBoolValue()) {
                mc.player.onGround = false;
            }
            mc.gameSettings.keyBindJump.pressed = false;
            mc.timer.timerSpeed = 6.78F;
            mc.player.jumpMovementFactor *= 1.04F;
            if (mc.player.motionY > 0 && !mc.player.onGround) {
                mc.player.motionY -= 0.00994;
            } else {
                mc.player.motionY -= 0.00995;
            }
        }
    }

    @EventTarget
    public void onUpdateTwo(EventUpdate event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer") || (mode.equalsIgnoreCase("Custom") && blink.getBoolValue())) {
            synchronized (this.positions) {
                this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
            }
            if (this.pulseTimer.hasReached(600)) {
                this.blink();
                this.pulseTimer.reset();
            }
        }
    }

    @Override
    public void onEnable() {
        boostTick = 0;
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            synchronized (this.positions) {
                this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight() / 2.0f, mc.player.posZ});
                this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
            }
        }
        super.onEnable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            if (mc.player == null || !(event.getPacket() instanceof CPacketPlayer) || this.disableLogger) {
                return;
            }
            event.setCancelled(true);
            if (!(event.getPacket() instanceof CPacketPlayer.Position) && !(event.getPacket() instanceof CPacketPlayer.PositionRotation)) {
                return;
            }
            this.packets.add(event.getPacket());
        }
    }

    private void blink() {
        try {
            this.disableLogger = true;
            Iterator<Packet<?>> packetIterator = this.packets.iterator();
            while (packetIterator.hasNext()) {
                mc.player.connection.sendPacket(packetIterator.next());
                packetIterator.remove();
            }
            this.disableLogger = false;
        } catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
        synchronized (this.positions) {
            this.positions.clear();
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.player.speedInAir = 0.02F;
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("YPort")) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            this.blink();
        }
        super.onDisable();
    }
}
