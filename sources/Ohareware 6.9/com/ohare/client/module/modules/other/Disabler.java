package com.ohare.client.module.modules.other;

import java.awt.Color;

import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.EnumValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S00PacketKeepAlive;

/**
 * made by Xen for OhareWare
 *
 * @since 6/10/2019
 **/
public class Disabler extends Module {
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.FAITHFUL);
    private TimerUtil timer = new TimerUtil();
    private boolean sent;
    private int i;

    public Disabler() {
        super("Disabler", Category.OTHER, new Color(0).getRGB());
        setDescription("Anticheat disabler");
        setHidden(true);
        addValues(mode);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case FAITHFUL:
                if (!event.isSending() && event.getPacket() instanceof S00PacketKeepAlive) {
                    event.setCanceled(true);
                    mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C00PacketKeepAlive(((S00PacketKeepAlive) event.getPacket()).func_149134_c() - 1));
                }
                break;
            case VELT:
                if (!event.isSending() && event.getPacket() instanceof S00PacketKeepAlive) {
                    sent = true;
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    event.setCanceled(true);
                }
                break;
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValue()) {
            case VELT:
                if (sent) {
                    final float[] offsets = new float[]{0.08f, 0.0925f, -0.025f,0.01F,-0.025f,0.05F,-0.045f};
                    if (i > offsets.length - 1) i = 0;
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offsets[i], mc.thePlayer.posZ, mc.thePlayer.onGround));
                    mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C00PacketKeepAlive(-11111));
                    i++;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onEnable() {
        sent = false;
        i = 0;
    }

    public enum Mode {
        FAITHFUL, VELT
    }
}
