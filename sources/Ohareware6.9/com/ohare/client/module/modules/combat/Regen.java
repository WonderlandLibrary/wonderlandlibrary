package com.ohare.client.module.modules.combat;

import java.awt.Color;

import com.ohare.client.event.events.game.TickEvent;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.Printer;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.EnumValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;

/**
 * made by Xen for OhareWare
 *
 * @since 7/21/2019
 **/
public class Regen extends Module {
    private boolean teleport, teleported, sendback, niggay;
    private double prevx, prevy, prevz;
    private float preyaw, prepitch;
    private int moveUnder;
    private TimerUtil timerUtil = new TimerUtil();
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.Faithful);

    public Regen() {
        super("Regen", Category.COMBAT, new Color(0x4DAE99).getRGB());
        addValues(mode);
        setDescription("Regenerate health.");
    }

    @Override
    public void onEnable() {
        teleport = teleported = sendback = false;
        prevx = prevy = prevz = 0;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.getActivePotionEffect(Potion.regeneration) != null && mode.getValue() == Mode.Potion) {
            if (mc.thePlayer.onGround || mc.thePlayer.isOnLadder() || mc.thePlayer.isInLiquid() || mc.thePlayer.isOnLiquid()) {
                if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth()) {
                    for (int i = 0; i < mc.thePlayer.getMaxHealth() - mc.thePlayer.getHealth(); i++) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    }
                }
            }
        }
        if (mode.getValue() == Mode.Old) {
            if (mc.thePlayer.onGround || mc.thePlayer.isOnLadder() || mc.thePlayer.isInLiquid() || mc.thePlayer.isOnLiquid()) {
                if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth()) {
                    for (int i = 0; i < mc.thePlayer.getMaxHealth() - mc.thePlayer.getHealth(); i++) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    }
                }
            }
        }
        if (mode.getValue() == Mode.Faithful) {
            if (mc.thePlayer.getHealth() <= 6 && !teleport) {
                prevx = mc.thePlayer.posX;
                prevy = mc.thePlayer.posY;
                prevz = mc.thePlayer.posZ;
                preyaw = mc.thePlayer.rotationYaw;
                prepitch = mc.thePlayer.rotationPitch;
                teleport = true;
            }
            if (teleport && !teleported) {
                mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(0, -999, 0);
                Printer.print("Healed!");
                teleported = true;
            }
            if ((teleport && mc.thePlayer.getHealth() > 6 && !sendback) || !mc.thePlayer.isEntityAlive()) {
                teleport = false;
                teleported = false;
                if (mc.thePlayer.isEntityAlive()) sendback = true;
                timerUtil.reset();
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.isSending() && event.getPacket() instanceof C03PacketPlayer && mode.getValue() == Mode.Bypass) {
            C03PacketPlayer player = (C03PacketPlayer) event.getPacket();
            if (mc.thePlayer.onGround || mc.thePlayer.isOnLadder() || mc.thePlayer.isInLiquid() || mc.thePlayer.isOnLiquid()) {
                if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.ticksExisted % 2 == 0) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(player.isOnGround()));
                }
            }
        }
        if (mode.getValue() == Mode.Faithful) {
            if (sendback && event.isSending()) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    if (!mc.thePlayer.isMoving() && mc.thePlayer.posY == mc.thePlayer.lastTickPosY) {
                        event.setCanceled(true);
                    }
                    C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
                    mc.thePlayer.motionY = mc.thePlayer.motionZ = mc.thePlayer.motionX = 0;
                    if (event.getPacket() instanceof C03PacketPlayer) {
                        if (!niggay) {
                            packet.setX(prevx);
                            packet.setY(prevy);
                            packet.setZ(prevz);
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(prevx,prevy,prevz, true));
                            mc.thePlayer.setPosition(prevx,prevy,prevz);
                            niggay = true;
                            moveUnder = 2;
                        }
                    }
                    if (timerUtil.reach(500)) {
                        sendback = false;
                        niggay = false;
                        prevx = prevy = prevz = 0;
                        mc.thePlayer.rotationYaw = preyaw;
                        mc.thePlayer.rotationPitch = prepitch;
                        mc.renderGlobal.loadRenderers();
                        timerUtil.reset();
                    }
                }
            } else {
                if (event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                    moveUnder = 1;
                }
                if (event.getPacket() instanceof S02PacketChat) {
                    S02PacketChat packet = (S02PacketChat) event.getPacket();
                    if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mode.getValue() == Mode.Faithful) {
            if (mc.thePlayer != null && moveUnder == 1 && sendback) {
                if (mc.thePlayer.getDistanceSq(prevx, prevy, prevz) > 1) {
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(prevx, prevy, prevz, false));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, prevy, Double.NEGATIVE_INFINITY, true));
                    moveUnder = 0;
                }
            }
        }
    }

    public enum Mode {
        Bypass, Potion, Old, Faithful
    }
}
