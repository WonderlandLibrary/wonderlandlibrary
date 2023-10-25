package tech.dort.dortware.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.SliderUnit;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.MovementEvent;
import tech.dort.dortware.impl.events.PacketEvent;
import tech.dort.dortware.impl.utils.movement.ACType;
import tech.dort.dortware.impl.utils.movement.MotionUtils;

public class LongJump extends Module {

    private double movementSpeed;
    private int enabledTicks;
    private boolean didHop, done, reset, invert;
    public final EnumValue<Mode> enumValue = new EnumValue<>("Mode", this, Mode.values());
    public final BooleanValue booleanValue = new BooleanValue("Damage", this, false);
    private final BooleanValue flagCheck = new BooleanValue("Flag Check", this, true);
    private final BooleanValue autoDisable = new BooleanValue("Auto Disable", this, true);
    private final NumberValue numberValue = new NumberValue("Speed", this, 2.5, 0.1, 5, SliderUnit.BPT);

    public LongJump(ModuleData moduleData) {
        super(moduleData);
        register(enumValue, numberValue, booleanValue, flagCheck, autoDisable);
    }

    /**
     * TODO: clean this.
     *
     * @param event - event
     */
    @Subscribe
    public void onMove(MovementEvent event) {
        try {
            Mode mode = enumValue.getValue();
            double speed = numberValue.getCastedValue();
            switch (mode) {
                case MINEPLEX: {
                    mc.timer.timerSpeed = 1.0f;
                    if (!done) {
                        if (mc.thePlayer.isMovingOnGround()) {
                            mc.timer.timerSpeed = 2.2F;
                            if (reset)
                                movementSpeed = 0.8F;
                            else
                                movementSpeed += 0.6267F;
                            reset = false;
                            event.setMotionY(mc.thePlayer.motionY = 0.42F);
                            MotionUtils.setMotion(event, 0.0D);
                            if (movementSpeed >= speed) {
                                done = true;
                            }
                        } else {
                            if (mc.thePlayer.fallDistance == 0) {
                                event.setMotionY(mc.thePlayer.motionY -= 0.125F);
                            } else if (mc.thePlayer.fallDistance <= 1.4) {
                                event.setMotionY(mc.thePlayer.motionY -= 0.032F);
                            }
                            if (movementSpeed <= .8F)
                                movementSpeed = movementSpeed - .01001;
                            else if (movementSpeed < 1.5F)
                                movementSpeed *= .98F;
                            else if (movementSpeed < 3F)
                                movementSpeed *= .97F;
                            else movementSpeed *= 0.96F;
                            movementSpeed = Math.min(speed + 0.05311F, movementSpeed);
                            movementSpeed = Math.max(movementSpeed, MotionUtils.getSpeed() + 0.1623F);
                            MotionUtils.setMotion(event, invert ? -movementSpeed : movementSpeed);
                            invert = !invert;
                        }
                    } else {
                        if (mc.thePlayer.fallDistance == 0) {
                            event.setMotionY(mc.thePlayer.motionY += 0.038F);
                        } else if (mc.thePlayer.fallDistance <= 1.4) {
                            event.setMotionY(mc.thePlayer.motionY += 0.032F);
                        }
                        if (mc.thePlayer.onGround)
                            done = false;
                        if (movementSpeed <= .8F)
                            movementSpeed = movementSpeed - .01001F;
                        else if (movementSpeed < 1.5F)
                            movementSpeed *= .98;
                        else if (movementSpeed < 3.0F)
                            movementSpeed *= .97;
                        else movementSpeed *= 0.96;
                        movementSpeed = Math.min(speed + .05312, movementSpeed);
                        movementSpeed = Math.max(movementSpeed, MotionUtils.getSpeed() + 0.16279F);
                        MotionUtils.setMotion(event, movementSpeed);
                    }
                    break;
                }
                case HYPIXEL: {
                    if (!mc.thePlayer.onGround) enabledTicks++;

                    if (enabledTicks != 0 && mc.thePlayer.onGround && autoDisable.getValue()) {
                        this.toggle();
                        return;
                    }

                    if (mc.thePlayer.isMovingOnGround()) {
                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                        movementSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL) * 2.15F;
                        if (booleanValue.getValue() && mc.thePlayer.hurtTime == 0) MotionUtils.damagePlayer(true);
                        didHop = true;
                    } else {
                        if (didHop) {
                            movementSpeed = 1.25D;
                            didHop = false;
                        } else {
                            movementSpeed *= 0.98;
                        }
                    }

                    if (mc.thePlayer.isCollidedHorizontally || !mc.thePlayer.isMoving())
                        movementSpeed = (float) MotionUtils.getBaseSpeed(ACType.HYPIXEL);
                    MotionUtils.setMotion(event, Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL), movementSpeed));
                    break;
                }
                case AAC: {
                    if (enabledTicks != 0 && mc.thePlayer.onGround && autoDisable.getValue()) {
                        this.toggle();
                        return;
                    }

                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        event.setMotionY(mc.thePlayer.motionY = 0.72F);
                    } else if (mc.thePlayer.fallDistance == 0) {
                        event.setMotionY(mc.thePlayer.motionY += 0.03);
                    }
                    if (!mc.thePlayer.onGround) {
                        event.setMotionX(event.getMotionX() * 3.7);
                        event.setMotionZ(event.getMotionZ() * 3.7);
                    }
                    break;
                }
                case HYPIXEL_SAFE:
                    if (!mc.thePlayer.onGround) enabledTicks++;

                    if (enabledTicks != 0 && mc.thePlayer.onGround && autoDisable.getValue()) {
                        this.toggle();
                        return;
                    }

                    if (mc.thePlayer.isMovingOnGround()) {
                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                        movementSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL) * 2.15F;
                        if (booleanValue.getValue() && mc.thePlayer.hurtTime == 0) MotionUtils.damagePlayer(true);
                        didHop = true;
                    } else {
                        if (didHop) {
                            movementSpeed = 1;
                            didHop = false;
                        } else {
                            movementSpeed *= 0.99;
                        }
                    }

                    if (mc.thePlayer.isCollidedHorizontally || !mc.thePlayer.isMoving())
                        movementSpeed = (float) MotionUtils.getBaseSpeed(ACType.HYPIXEL);

                    MotionUtils.setMotion(event, Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL), movementSpeed));
                    break;

                case NEW_HYPIXEL:
//                    AutoHypixel autoHypixel = Client.INSTANCE.getModuleManager().get(AutoHypixel.class);
//
//                    if (autoHypixel.enabledPaper && autoHypixel.isToggled()) {
//                        return;
//                    }

                    if (!mc.thePlayer.onGround) enabledTicks++;

                    if (enabledTicks != 0 && mc.thePlayer.onGround && autoDisable.getValue()) {
                        this.toggle();
                        return;
                    }

                    if (mc.thePlayer.isMovingOnGround()) {
                        event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
                        movementSpeed = MotionUtils.getBaseSpeed(ACType.HYPIXEL) * 2.15F;
                        if (booleanValue.getValue() && mc.thePlayer.hurtTime == 0) MotionUtils.damagePlayer(true);
                        didHop = true;
                    } else {
                        if (didHop) {
                            movementSpeed = 1.3D;
                            didHop = false;
                        } else {
                            movementSpeed *= 0.5F;
                        }
                    }

                    if (mc.thePlayer.isCollidedHorizontally || !mc.thePlayer.isMoving())
                        movementSpeed = (float) MotionUtils.getBaseSpeed(ACType.HYPIXEL);

                    MotionUtils.setMotion(event, Math.max(MotionUtils.getBaseSpeed(ACType.HYPIXEL), movementSpeed));
                    break;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        done = false;
        reset = true;
        didHop = false;
        movementSpeed = 0.2F;
        enabledTicks = 0;
        super.onEnable();
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (enumValue.getValue().equals(Mode.AAC)) {
//            PacketRunnableWrapper.aacExploit.accept(event);
            return;
        }
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            if (flagCheck.getValue()) {
                this.toggle();
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public String getSuffix() {
        return " \2477" + enumValue.getValue().getDisplayName();
    }

    public enum Mode implements INameable {
        AAC("AAC"), MINEPLEX("Mineplex"), NEW_HYPIXEL("Hypixel"), HYPIXEL("Hypixel Old"), HYPIXEL_SAFE("Hypixel Old Safe");
        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}
