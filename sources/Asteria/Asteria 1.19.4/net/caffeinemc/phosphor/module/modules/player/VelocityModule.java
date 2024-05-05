package net.caffeinemc.phosphor.module.modules.player;

import net.caffeinemc.phosphor.api.event.events.PacketEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.mixin.EntityVelocityUpdateS2CPacketAccessor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class VelocityModule extends Module {
    public final NumberSetting velocityH = new NumberSetting("Horizontal", this, 1, 0, 1, 0.01);
    public final NumberSetting velocityV = new NumberSetting("Vertical", this, 1, 0, 1, 0.01);

    public VelocityModule() {
        super("Velocity", "Changes player's velocity", Category.PLAYER);
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof EntityVelocityUpdateS2CPacket packet && packet.getId() == mc.player.getId()) {
            double velX = (packet.getVelocityX() / 8000d - mc.player.getVelocity().x) * velocityH.getValue();
            double velY = (packet.getVelocityY() / 8000d - mc.player.getVelocity().y) * velocityV.getValue();
            double velZ = (packet.getVelocityZ() / 8000d - mc.player.getVelocity().z) * velocityH.getValue();
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setVelocityX((int) (velX * 8000 + mc.player.getVelocity().x * 8000));
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setVelocityY((int) (velY * 8000 + mc.player.getVelocity().y * 8000));
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setVelocityZ((int) (velZ * 8000 + mc.player.getVelocity().z * 8000));
        }
    }
}
