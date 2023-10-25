/* 3eLeHyy#0089 */

package org.dreamcore.client.feature.impl.misc;

import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.server.SPacketKeepAlive;
import org.apache.commons.lang3.RandomUtils;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;

public class Secret extends Feature {
    public long StopDecompilingClientFaggot = 0L;
    public BooleanSetting matrixDestruction = new BooleanSetting("Matrix Destruction", false, () -> true);

    public Secret() {
        super("Secret", "Test", Type.Misc);
        addSettings(matrixDestruction);
    }

    public void xyesos(double e) {
        for (int i = 0; i < i + 1; i++)
            xyesos(e * Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)), Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)))));
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {

    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        mc.player.connection.sendPacket(new CPacketKeepAlive(0));
        if (mc.player.ticksExisted % 3 == 0) {
            mc.player.connection.sendPacket(new CPacketInput());
        }
        if (matrixDestruction.getBoolValue()) {
            if (mc.player.ticksExisted % 6 == 0) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        this.StopDecompilingClientFaggot = RandomUtils.nextLong(0L, 10L);
        if (event.getPacket() instanceof SPacketKeepAlive)
            try {
                Thread.sleep(50L * this.StopDecompilingClientFaggot);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C00Handshake) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketClientSettings) {
            event.setCancelled(true);
        }
    }
}
