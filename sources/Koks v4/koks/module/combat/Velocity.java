package koks.module.combat;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.manager.value.annotation.Value;
import koks.event.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.MathHelper;

import java.util.Random;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Velocity", category = Module.Category.COMBAT, description = "Reduce your knock back")
public class Velocity extends Module {

    @Value(name = "Mode", modes = {"Packet", "BackToBlock", "Jump", "Vulcan2.0.1", "Intave13KeepLow", "Intave13Reverse", "Intave13Wall", "Intave13Old", "Intave13GommeZero", "Intave13Flag", "Intave14.1.2", "AAC3.3.12", "AAC3.3.14", "AAC4", "Simple", "BAC1.0.4", "Matrix6.6.1"})
    public String mode = "Packet";

    @Value(name = "Cancel")
    boolean cancel = true;

    @Value(name = "Cancel Explosion")
    boolean cancelExplosion = false;

    @Value(name = "Vertical", minimum = 0, maximum = 100)
    int vertical = 0;

    @Value(name = "Horizontal", minimum = 0, maximum = 100)
    int horizontal = 0;

    @Value(name = "Intave14.1.2-Improve", displayName = "Improve")
    boolean improveIntave1412 = true;

    @Value(name = "BackToBlock-HurtTime", displayName = "HurtTime", minimum = 1, maximum = 10)
    int backToBlockHurtTime = 5;


    public boolean wasOnGround;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        if(name.contains("-")) {
            final String[] split = name.split("-");
            return split[0].equalsIgnoreCase(mode);
        }
        switch (name) {
            case "Vertical":
            case "Horizontal":
                return mode.equalsIgnoreCase("Packet") && !cancel;
            case "Cancel Explosion":
                return mode.equalsIgnoreCase("Packet") && cancel;
            case "Cancel":
                return mode.equalsIgnoreCase("Packet");
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info(priority = Event.Priority.HIGH)
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();

        switch (mode) {
            case "BackToBlock":
                if(event instanceof UpdateEvent) {
                    if(getHurtTime() == backToBlockHurtTime) {
                        getPlayer().motionX *= -1;
                        getPlayer().motionZ *= -1;
                        if(getPlayer().motionY < 0) {
                            getPlayer().motionY *= -1;
                        }
                    }
                }
                break;
            case "Packet":
                if (event instanceof final VelocityEvent velocityEvent) {
                    if (cancel)
                        velocityEvent.setCanceled(true);
                    else {
                        velocityEvent.setVertical(vertical);
                        velocityEvent.setHorizontal(horizontal);
                    }
                }
                if (event instanceof final PacketEvent packetEvent) {
                    if (packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                        final Packet<? extends INetHandler> packet = packetEvent.getPacket();
                        if (packet instanceof S27PacketExplosion)
                            if (cancelExplosion)
                                packetEvent.setCanceled(true);
                    }
                }
                break;
            case "Simple":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() != 0) {
                        getPlayer().motionY = 0;
                        setMotion(0);
                    }
                }
                break;
            case "Jump":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() == 10 && getPlayer().onGround) {
                        getPlayer().jump();
                    }
                }
                break;
            case "Matrix6.6.1":
                if(event instanceof UpdateEvent) {
                    if(getHurtTime() > 2) {
                        movementUtil.setSpeed(0.14);
                    }
                }
                break;
            case "BAC1.0.4":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() != 0) {
                        getPlayer().onGround = true;
                        getPlayer().motionX *= 0.2;
                        getPlayer().motionZ *= 0.2;
                    }
                }
                break;
            case "AAC3.3.12":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().hurtTime > 0) {
                        getPlayer().motionX *= 0.8;
                        getPlayer().motionZ *= 0.8;
                        getPlayer().motionY *= 1;
                    }
                }
                break;
            case "AAC3.3.14":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().hurtTime > 0) {
                        setMotion(0);
                    }
                }
                break;
            case "Intave13Flag":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() == 10 && getPlayer().onGround) {
                        sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX() + 6, getY() + 1, getZ() + 6, true));
                    }
                }
                break;
            case "Intave13Wall":
                if (event instanceof UpdateEvent) {
                    //TODO: Checking for Full BLock
                    if(mc.inGameHasFocus) {
                        final float velocity = (float) MathHelper.getRandomDoubleInRange(new Random(), 0.3045, 0.3345);
                        if (getPlayer().isCollidedHorizontally && !getPlayer().onGround && !getPlayer().isCollidedVertically && !getPlayer().isInWeb && !getPlayer().isInWater() && !getPlayer().isInLava() && getHurtTime() != 0) {
                            movementUtil.setSpeed(velocity, getYaw());
                        }
                    }
                }
                break;
            case "Intave13Old":
                if (event instanceof UpdateEvent) {
                    if (getPlayer().hurtTime != 0)
                        if (getPlayer().hurtTime == 6)
                            movementUtil.setSpeed(0.17, getYaw());
                }
            case "Intave13KeepLow":
                if (event instanceof UpdateEvent) {
                    switch (getHurtTime()) {
                        case 10:
                            if (getPlayer().onGround) {
                                wasOnGround = true;
                            }
                            break;
                        case 0:
                            wasOnGround = false;
                            break;
                        case 9:
                            if (wasOnGround) {
                                getPlayer().motionY = 0.0D;
                            }
                            break;
                    }
                }
                break;
            case "Intave13GommeZero":
                if (event instanceof VelocityEvent) {
                    if (getHurtTime() != 0) {
                        event.setCanceled(true);
                    }
                }

                if (event instanceof UpdateEvent) {
                    if (getHurtTime() != 0) {
                        getGameSettings().keyBindForward.pressed = false;
                        getGameSettings().keyBindBack.pressed = false;
                        getGameSettings().keyBindLeft.pressed = false;
                        getGameSettings().keyBindRight.pressed = false;
                    }
                }
                break;
            case "Intave13Reverse":
                if (event instanceof MoveEvent) {
                    if (getPlayer() != null) {
                        if (getHurtTime() > 0) {
                            getPlayer().setSprinting(false);
                            movementUtil.setSpeed(0.05F);
                        }
                    }
                }
                break;
            case "Intave14.1.2":
                if(event instanceof final KnockbackModifierEvent knockbackModifierEvent) {
                    knockbackModifierEvent.setFlag(true);
                    if(improveIntave1412) {
                        int i = 0;
                        i += EnchantmentHelper.getKnockbackModifier(getPlayer());
                        if(getPlayer().isSprinting())
                            i++;
                        if(getPlayer().isSwingInProgress && getPlayer().hurtTime != 0 && getPlayer().moveForward != 0 && getPlayer().moveStrafing != 0) {
                            if(getPlayer().onGround || !getPlayer().isSprinting()) {
                                if (i > 0) {
                                    getPlayer().addVelocity((-MathHelper.sin((float) (getYaw() * Math.PI / 180)) * i * 0.5), 0.1, (MathHelper.cos((float) (getYaw() * Math.PI / 180)) * i * 0.5));
                                }
                            }
                        }
                    }
                }
                break;
            case "Intave14.3.3":
                if(event instanceof UpdateEvent) {
                    if(getPlayer().hurtTime == 10) {
                        getPlayer().motionX *= -1;
                        getPlayer().motionZ *= -1;
                    } else if(getPlayer().hurtTime == 9) {
                        if(getPlayer().onGround) {
                            getPlayer().motionX *= 0.9;
                            getPlayer().motionZ *= 0.9;
                        }
                    }
                }
                break;
            case "AAC4":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() > 5) {
                        getPlayer().onGround = true;
                        stopWalk();
                    } else if(getHurtTime() != 0) {
                        resumeWalk();
                    }
                }
                break;
            case "Vulcan2.0.1":
                if (event instanceof UpdateEvent) {
                    if (getHurtTime() != 0) {
                        movementUtil.setSpeed(0.2);
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
