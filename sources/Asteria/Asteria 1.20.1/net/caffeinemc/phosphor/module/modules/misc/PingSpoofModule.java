package net.caffeinemc.phosphor.module.modules.misc;

import net.caffeinemc.phosphor.api.event.events.PacketEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.AnyNumberSetting;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.s2c.play.KeepAliveS2CPacket;

public class PingSpoofModule extends Module {
    public final AnyNumberSetting lagDelay = new AnyNumberSetting("Lag Delay", this, 200, false);

    public PingSpoofModule() {
        super("PingSpoof", "Spoofing player's ping", Category.MISC);
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof KeepAliveS2CPacket packet) {
            new Thread(() -> {
                try {
                    Thread.sleep(lagDelay.getIValue());
                    mc.getNetworkHandler().getConnection().send(new KeepAliveC2SPacket(packet.getId()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            event.cancel();
        }
    }
}
