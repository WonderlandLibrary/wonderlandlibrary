package vip.radium.module.impl.player;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import vip.radium.event.impl.packet.PacketSendEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;

@ModuleInfo(label = "Inventory Move", category = ModuleCategory.PLAYER)
public final class InventoryMove extends Module {

    private final Property<Boolean> cancelPacketProperty = new Property<>("Cancel", false);

    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent = event -> {
        if (cancelPacketProperty.getValue() &&
                (event.getPacket() instanceof C16PacketClientStatus || event.getPacket() instanceof C0DPacketCloseWindow)) {
            event.setCancelled();
        }
    };
}
