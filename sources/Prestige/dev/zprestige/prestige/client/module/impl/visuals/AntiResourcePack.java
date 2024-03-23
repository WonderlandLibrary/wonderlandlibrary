package dev.zprestige.prestige.client.module.impl.visuals;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketReceiveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.util.impl.PacketUtil;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;

public class AntiResourcePack extends Module {

    public AntiResourcePack() {
        super("Anti Resource Pack", Category.Visual, "Prevents servers from forcing a resource pack");
    }

    @EventListener
    public void event(PacketReceiveEvent event) {
        if (event.getPacket() instanceof ResourcePackSendS2CPacket) {
            event.setCancelled();
            PacketUtil.INSTANCE.sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.ACCEPTED));
            PacketUtil.INSTANCE.sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
        }
    }
}
