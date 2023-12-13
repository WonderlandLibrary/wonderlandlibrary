/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.combat;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.movement.Fly;
import dev.rise.module.impl.movement.LongJump;
import dev.rise.module.impl.movement.Speed;
import dev.rise.module.manager.ModuleManager;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.MoveUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

import java.util.Objects;

@ModuleInfo(name = "Velocity", description = "Changes how and how much velocity you take", category = Category.COMBAT)
public final class Velocity extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Custom", "Custom", "Redesky", "Minemenclub", "Vulcan", "Stack", "Follow First Tick", "Tick Cancel", "Ground");

    private final BooleanSetting explosion = new BooleanSetting("Explosion", this, true);

    private final BooleanSetting onlyInAir = new BooleanSetting("Only In Air", this, false);
    private final NumberSetting horizontal = new NumberSetting("Horizontal", this, 100.0, 0.0, 100.0, 0.1);
    private final NumberSetting vertical = new NumberSetting("Vertical", this, 100.0, 0.0, 100.0, 0.1);
    private final BooleanSetting moreOptions = new BooleanSetting("More Options", this, false);
    private final NumberSetting delayHorizontal = new NumberSetting("Delayed Horizontal", this, 100.0, 0.0, 100.0, 0.1);
    private final NumberSetting delayVertical = new NumberSetting("Delayed Vertical", this, 100.0, 0.0, 100.0, 0.1);
    private final NumberSetting horizontalAttack = new NumberSetting("Horizontal Attack", this, 100.0, 0.0, 100.0, 0.1);
    private final NumberSetting verticalAttack = new NumberSetting("Vertical Attack", this, 100.0, 0.0, 100.0, 0.1);
    private final NumberSetting timer = new NumberSetting("Timer", this, 1, 0.1, 2, 0.1);
    private final NumberSetting timerTicks = new NumberSetting("Timer Ticks", this, 0, 0, 9, 0.1);

    private final NumberSetting ticksOption = new NumberSetting("Ticks", this, 3, 1, 20, 1);

    private int ticks, cancelTicks, row;
    private double motionY, motionX, motionZ;
    private boolean receivingVelocity, lastTick, countingTicks;
    private final ModuleManager moduleManager = Rise.INSTANCE.getModuleManager();

    @Override
    protected void onEnable() {
        receivingVelocity = false;
        countingTicks = false;
        lastTick = false;
        cancelTicks = 0;
        ticks = 0;
    }

    @Override
    public void onUpdateAlwaysInGui() {
        switch (mode.getMode()) {
            case "Custom":
            case "Redesky":
                horizontal.hidden = vertical.hidden = false;

                moreOptions.hidden = false;

                horizontalAttack.hidden = verticalAttack.hidden = timer.hidden = timerTicks.hidden
                        = delayHorizontal.hidden = delayVertical.hidden = !moreOptions.isEnabled();
                break;

            default:
                horizontal.hidden = vertical.hidden = horizontalAttack.hidden = verticalAttack.hidden = timer.hidden = timerTicks.hidden = delayHorizontal.hidden = delayVertical.hidden = true;
                moreOptions.hidden = true;
                break;
        }

        ticksOption.hidden = !mode.is("Tick Cancel");
    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        switch (mode.getMode()) {
            case "Tick Cancel":
                if (cancelTicks >= ticksOption.getValue() && countingTicks) {
                    if (!(this.getModule(Speed.class).isEnabled() || this.getModule(LongJump.class).isEnabled() || this.getModule(Fly.class).isEnabled()))
                        mc.thePlayer.setVelocity(0, 0, 0);
                    countingTicks = false;
                }
                break;

            case "Minemenclub":
                if (cancelTicks >= 3 && countingTicks) {
                    if (!(this.getModule(Speed.class).isEnabled() || this.getModule(LongJump.class).isEnabled() || this.getModule(Fly.class).isEnabled()))
                        mc.thePlayer.setVelocity(0, 0, 0);
                    countingTicks = false;
                }
                break;
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.thePlayer.onGround && onlyInAir.isEnabled()) return;

        final Packet<?> p = event.getPacket();

        if (p instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;

            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                switch (mode.getMode()) {
                    case "Custom": {
                        final double horizontal = this.horizontal.getValue();
                        final double vertical = this.vertical.getValue();

                        if (horizontal == 0.0 && vertical == 0.0) {
                            event.setCancelled(true);
                            return;
                        }

                        packet.motionX *= horizontal / 100.0;
                        packet.motionY *= vertical / 100.0;
                        packet.motionZ *= horizontal / 100.0;

                        event.setPacket(packet);
                        break;
                    }

                    case "Vulcan": {
                        final double horizontal = this.horizontal.getValue();
                        final double vertical = this.vertical.getValue();

                        if (horizontal == 0.0 && vertical == 0.0) {
                            event.setCancelled(true);
                            return;
                        }
                        break;
                    }

                    case "Tick Cancel": {
                        countingTicks = true;
                        cancelTicks = 0;
                        break;
                    }

                    case "Ground": {
                        receivingVelocity = true;
                        break;
                    }

                    case "Redesky": {
                        final double horizontal = this.horizontal.getValue();
                        final double vertical = this.vertical.getValue();

                        if (horizontal == 0.0 && vertical == 0.0) {
                            event.setCancelled(true);
                            return;
                        }

                        packet.motionX *= horizontal / 100.0;
                        packet.motionY *= vertical / 100.0;
                        packet.motionZ *= horizontal / 100.0;

                        //Epic halal aac5 velocity bypass
                        mc.getNetHandler().addToSendQueueWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        receivingVelocity = true;

                        event.setCancelled(true);
                        break;
                    }

                    case "Stack": {
                        row++;
                        if (MoveUtil.isMoving()) {
                            if (row <= 2)
                                event.setCancelled(true);
                            else
                                row = 0;
                        } else
                            event.setCancelled(true);
                        break;
                    }
                    case "Minemenclub": {
                        if (this.getModule(Fly.class).isEnabled() || this.getModule(Speed.class).isEnabled()) {
                            row = 3;
                        }

                        row++;
                        if (MoveUtil.isMoving()) {
                            if (row <= 2)
                                event.setCancelled(true);
                            else {
                                countingTicks = !this.getModule(Speed.class).isEnabled();
                                cancelTicks = 0;
                                row = 0;
                            }
                        } else
                            event.setCancelled(true);
                        break;
                    }

                    case "Follow First Tick": {
                        event.setCancelled(true);
                        motionY = packet.getMotionY() / 8000f;
                        motionX = packet.getMotionX() / 8000f;
                        motionZ = packet.getMotionZ() / 8000f;
                        mc.thePlayer.motionY = 0.03;
                        //mc.thePlayer.motionZ = motionZ * 0.5;
                        //mc.thePlayer.motionX = motionX * 0.5;
                        lastTick = true;
                        break;
                    }
                }
            }
        }

        if (p instanceof S27PacketExplosion) {
            if (explosion.isEnabled()) {

                final S27PacketExplosion explosion = (S27PacketExplosion) p;

                switch (mode.getMode()) {
                    case "Custom":
                        final double horizontal = this.horizontal.getValue();
                        final double vertical = this.vertical.getValue();

                        if (horizontal == 0.0 && vertical == 0.0) {
                            event.setCancelled(true);
                            return;
                        }

                        explosion.posX *= horizontal / 100.0;
                        explosion.posY *= vertical / 100.0;
                        explosion.posZ *= horizontal / 100.0;

                        event.setPacket(explosion);
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (countingTicks)
            cancelTicks++;

        ticks++;

        if (!moreOptions.isEnabled() || mc.thePlayer.onGround && onlyInAir.isEnabled()) return;

        switch (mode.getMode()) {
            case "Custom": {
                if (mc.thePlayer.hurtTime == 9) {
                    ticks = 0;
                }

                if (ticks < timerTicks.getValue()) {
                    mc.timer.timerSpeed = (float) timer.getValue();
                }

                if (ticks == (int) timerTicks.getValue() || mc.thePlayer == null || mc.thePlayer.ticksExisted < 3) {
                    mc.timer.timerSpeed = 1;
                }

                assert mc.thePlayer != null;
                if (mc.thePlayer.hurtTime == 9) {
                    motionX = mc.thePlayer.motionX;
                    motionY = mc.thePlayer.motionY;
                    motionZ = mc.thePlayer.motionZ;
                }

                if (mc.thePlayer.hurtTime == 8 && !Objects.requireNonNull(moduleManager.getModule("Fly")).isEnabled() && moreOptions.isEnabled()) {
                    mc.thePlayer.motionX *= delayHorizontal.getValue() / 100;
                    mc.thePlayer.motionY *= delayVertical.getValue() / 100;
                    mc.thePlayer.motionZ *= delayHorizontal.getValue() / 100;
                }
            }
            break;

            case "Ground": {
                if (receivingVelocity) {
                    mc.thePlayer.onGround = true;
                    receivingVelocity = false;
                }
                break;
            }

            case "Follow First Tick": {
                if (lastTick) {
                    event.setY(event.getY() + motionY);
                    event.setZ(event.getZ() + motionZ);
                    event.setX(event.getX() + motionX);
                    lastTick = false;
                }
            }
            break;

            case "Redesky": {
                if (mc.thePlayer.hurtTime == 9) {
                    ticks = 0;
                }

                if (ticks < timerTicks.getValue()) {
                    mc.timer.timerSpeed = (float) timer.getValue();
                }

                if (ticks == (int) timerTicks.getValue() || mc.thePlayer == null || mc.thePlayer.ticksExisted < 3) {
                    mc.timer.timerSpeed = 1;
                }

                assert mc.thePlayer != null;
                if (mc.thePlayer.hurtTime == 9) {
                    motionX = mc.thePlayer.motionX;
                    motionY = mc.thePlayer.motionY;
                    motionZ = mc.thePlayer.motionZ;
                }

                if (mc.thePlayer.hurtTime == 8 && !Objects.requireNonNull(moduleManager.getModule("Fly")).isEnabled() && moreOptions.isEnabled()) {
                    mc.thePlayer.motionX = motionX * (delayHorizontal.getValue() / 100);
                    mc.thePlayer.motionY = motionY * (delayVertical.getValue() / 100);
                    mc.thePlayer.motionZ = motionZ * (delayHorizontal.getValue() / 100);
                }

                if (receivingVelocity) {
                    mc.getNetHandler().addToSendQueueWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                    receivingVelocity = false;
                }
            }
            break;
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        Packet p = event.getPacket();
        switch (mode.getMode()) {
            case "Vulcan":
                if (mc.thePlayer.hurtTime > 0 && p instanceof C0FPacketConfirmTransaction) {
                    event.setCancelled(true);
                }
                break;
        }
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (!moreOptions.isEnabled() || mc.thePlayer.onGround && onlyInAir.isEnabled()) return;

        if (mode.is("Custom") || mode.is("Redesky")) {
            if (mc.thePlayer.hurtTime > 0) {
                final double horiz = horizontalAttack.getValue() / 100;
                final double vert = verticalAttack.getValue() / 100;

                mc.thePlayer.motionZ *= horiz;
                mc.thePlayer.motionX *= horiz;
                mc.thePlayer.motionY *= vert;
            }
        }
    }
}
