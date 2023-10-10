package vestige.module.impl.movement;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.*;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.player.MovementUtil;

public class Longjump extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Hycraft", "Self damage");

    private final DoubleSetting motionY = new DoubleSetting("Motion Y", () -> mode.is("Vanilla"), 0.4, 0.1, 9, 0.1);
    private final DoubleSetting speed = new DoubleSetting("Speed", () -> mode.is("Vanilla"), 1, 0.1, 9, 0.1);

    private final BooleanSetting stopMovement = new BooleanSetting("Stop movement", () -> mode.is("Self damage"), false);
    private final IntegerSetting waitingTicks = new IntegerSetting("Waiting ticks", () -> mode.is("Self damage"), 10, 4, 20, 1);
    private final ModeSetting horizontalMove = new ModeSetting("Horizontal move", () -> mode.is("Self damage"), "Ignore", "Legit", "Ignore", "Boost", "Verus");

    private final DoubleSetting horizontalBoostAmount = new DoubleSetting("Horizontal boost amount", () -> mode.is("Self damage") && horizontalMove.is("Boost"), 0.2, 0.02, 1, 0.02);

    private final DoubleSetting afterVelocityYBoost = new DoubleSetting("After velocity Y boost", () -> mode.is("Self damage"), 0, 0, 0.08, 0.002);

    private boolean started;
    private int counter, ticks;

    private double velocityX, velocityY, velocityZ;

    public Longjump() {
        super("Longjump", Category.MOVEMENT);
        this.addSettings(mode, motionY, speed, stopMovement, waitingTicks, horizontalMove, horizontalBoostAmount, afterVelocityYBoost);
    }

    @Override
    public void onEnable() {
        counter = ticks = 0;
        started = false;

        velocityY = -1;

        switch (mode.getMode()) {
            case "Self damage":
                if(!mc.thePlayer.onGround) {
                    counter = 3;
                }
                break;
        }
    }

    @Override
    public void onDisable() {
        Vestige.instance.getPacketBlinkHandler().stopAll();
        mc.timer.timerSpeed = 1F;

        switch (mode.getMode()) {
            case "Vanilla":
                MovementUtil.strafe(0.1);
                break;
        }
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        switch (mode.getMode()) {
            case "Self damage":
                if(started) {
                    ++ticks;

                    if(ticks == waitingTicks.getValue() && velocityY != -1) {
                        switch (horizontalMove.getMode()) {
                            case "Legit":
                                mc.thePlayer.motionX = velocityX;
                                mc.thePlayer.motionZ = velocityZ;
                                break;
                            case "Ignore":
                                break;
                            case "Boost":
                                mc.thePlayer.motionX -= (double)(MathHelper.sin((float) Math.toRadians(mc.thePlayer.rotationYaw)) * horizontalBoostAmount.getValue());
                                mc.thePlayer.motionZ += (double)(MathHelper.cos((float) Math.toRadians(mc.thePlayer.rotationYaw)) * horizontalBoostAmount.getValue());
                                break;
                        }

                        mc.thePlayer.motionY = velocityY;
                    }

                    if(ticks > waitingTicks.getValue() && velocityY != -1) {
                        mc.thePlayer.motionY += afterVelocityYBoost.getValue();
                    }

                    if(ticks > 2 && ticks < waitingTicks.getValue()) {
                        Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                    } else {
                        Vestige.instance.getPacketBlinkHandler().stopAll();
                    }

                    if(mc.thePlayer.onGround) {
                        this.setEnabled(false);
                    }
                } else {
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        counter++;

                        if(counter > 3) {
                            started = true;
                            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
                            return;
                        }
                    }

                    if(mc.thePlayer.motionY > 0.3) {
                        Vestige.instance.getPacketBlinkHandler().stopAll();
                    } else {
                        Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                    }
                }
                break;
            case "Hycraft":
                if(mc.thePlayer.onGround) {
                    if(started) {
                        this.setEnabled(false);
                        return;
                    }

                    if(!mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.jump();
                        started = true;
                    }
                } else {
                    if(ticks >= 2 && ticks <= 8) {
                        mc.thePlayer.motionY += 0.07;
                    }

                    ticks++;
                }
                break;
        }
    }

    @Listener
    public void onEntityAction(EntityActionEvent event) {
        if(mode.is("Self damage") && stopMovement.isEnabled()) {
            if(counter < 4) {
                event.setSprinting(false);
            }
        }
    }

    @Listener
    public void onMove(MoveEvent event) {
        switch (mode.getMode()) {
            case "Vanilla":
                if(mc.thePlayer.onGround) {
                    if(started) {
                        this.setEnabled(false);
                        return;
                    }

                    if(MovementUtil.isMoving()) {
                        started = true;
                        if(motionY.getValue() == 0.4) {
                            event.setY((double) 0.42F);
                        } else {
                            event.setY(mc.thePlayer.motionY = motionY.getValue());
                        }
                    }
                }

                MovementUtil.strafe(event, speed.getValue());
                break;
            case "Self damage":
                if(stopMovement.isEnabled()) {
                    if(counter <= 3) {
                        MovementUtil.strafe(event, 0);
                    } else if(counter == 4 && mc.thePlayer.onGround && event.getY() > 0.4) {
                        if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                            MovementUtil.strafe(event, 0.6 + MovementUtil.getSpeedAmplifier() * 0.07);
                        } else {
                            MovementUtil.strafe(event, 0.6);
                        }
                    }
                }

                if(started && horizontalMove.is("Verus")) {
                    if(ticks >= waitingTicks.getValue() + 14) {
                        MovementUtil.strafe(event, 0.28);
                    } else if(ticks >= waitingTicks.getValue()) {
                        MovementUtil.strafe(event, 9);
                    }
                }
                break;
        }
    }

    @Listener
    public void onMotion(MotionEvent event) {
        switch (mode.getMode()) {
            case "Self damage":
                if(!started && counter < 3) {
                    event.setOnGround(false);
                }
                break;
        }
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        switch (mode.getMode()) {
            case "Self damage":
                if(event.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = event.getPacket();

                    if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        event.setCancelled(true);
                        velocityX = packet.getMotionX() / 8000.0D;
                        velocityY = packet.getMotionY() / 8000.0D;
                        velocityZ = packet.getMotionZ() / 8000.0D;
                    }
                }
                break;
        }
    }

    @Override
    public String getSuffix() {
        return mode.getMode();
    }

}
