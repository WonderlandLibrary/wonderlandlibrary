package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautototem;

import me.earth.earthhack.impl.core.mixins.minecraft.gui.IContainer;
import me.earth.earthhack.impl.core.mixins.minecraft.network.server.ISPacketSetSlot;
import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.SPacketSetSlot;

/**
 * Handles the SPacketSetSlots sent by PingBypass AutoTotem.
 * Sets mc.player.openContainers transactionId accordingly as
 * well has the mouse slot.
 */
public class SetSlotListener extends ModuleListener<ServerAutoTotem, PacketEvent.Receive<SPacketSetSlot>>
{
    protected SetSlotListener(ServerAutoTotem module)
    {
        super(module, PacketEvent.Receive.class, SPacketSetSlot.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketSetSlot> event)
    {
        if (PingBypass.getInstance().isEnabled() && mc.player != null)
        {
            SPacketSetSlot packet = event.getPacket();
            if (packet.getSlot() == -1337)
            {
                ((IContainer) mc.player.openContainer).setTransactionID((short) packet.getWindowId()); //hmm TODO: maybe better id to increment?
                ((ISPacketSetSlot) packet).setWindowId(-1); //make NetHandlerPlayClient set mouse slot.
            }
            else if (packet.getWindowId() == -128)
            {
                event.setCancelled(true);
                mc.addScheduledTask(() ->
                {
                    Slot slot = mc.player.inventoryContainer.inventorySlots.get(packet.getSlot());
                    slot.putStack(packet.getStack());
                });
            }
        }
    }

}
