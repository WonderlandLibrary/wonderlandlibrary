package io.github.nevalackin.client.module.misc.player;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.packet.SendPacketEvent;
import io.github.nevalackin.client.event.world.LoadWorldEvent;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class PingSpoof extends Module {

    private long timeOfFlag;

    private final DoubleProperty pingProperty = new DoubleProperty("Ping", 1000L, 0L, 1000L, 1L);

    private final ScheduledExecutorService packetSender = Executors.newSingleThreadScheduledExecutor();

    public PingSpoof() {
        super("Ping Spoof", Category.MISC, Category.SubCategory.MISC_PLAYER);

        this.setSuffix(() -> String.format("+%sms", this.pingProperty.getDisplayString()));

        this.register(this.pingProperty);
    }

    @EventLink
    private final Listener<LoadWorldEvent> onLoadWorld = event -> {
        this.timeOfFlag = 0;
    };

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S00PacketKeepAlive) {
            final S00PacketKeepAlive keepAlive = (S00PacketKeepAlive) packet;
            this.schedule(new C00PacketKeepAlive(keepAlive.func_149134_c()));
            event.setCancelled();
        } else if (packet instanceof S08PacketPlayerPosLook) {
            this.timeOfFlag = System.currentTimeMillis();
        }
    };

    @EventLink
    private final Listener<SendPacketEvent> onSendPacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C0FPacketConfirmTransaction) {
            final C0FPacketConfirmTransaction transactionPacket = (C0FPacketConfirmTransaction) packet;
            if (transactionPacket.getUid() < 0) {
                event.setCancelled();
                this.schedule(packet);
            }
        } else if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook && System.currentTimeMillis() - this.timeOfFlag < this.pingProperty.getValue().longValue()) {
            final C03PacketPlayer.C06PacketPlayerPosLook c06 = (C03PacketPlayer.C06PacketPlayerPosLook) packet;
            event.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                c06.getPositionX(), c06.getPositionY(), c06.getPositionZ(), c06.isOnGround()));
        }
    };

    private void schedule(final Packet<?> packet) {
        this.packetSender.schedule(() -> {
            if (this.mc.thePlayer != null) {
                this.mc.thePlayer.sendQueue.sendPacketDirect(packet);
            }
        }, this.pingProperty.getValue().longValue(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void onEnable() {
        this.timeOfFlag = 0;
    }

    @Override
    public void onDisable() {

    }
}
