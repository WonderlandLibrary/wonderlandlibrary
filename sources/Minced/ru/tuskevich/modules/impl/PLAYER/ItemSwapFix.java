// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "ItemSwapFix", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class ItemSwapFix extends Module
{
    @EventTarget
    public void onPacket(final EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof SPacketHeldItemChange) {
            final SPacketHeldItemChange packetHeldItemChange = (SPacketHeldItemChange)eventPacket.getPacket();
            final int heldItemHotbarIndex = packetHeldItemChange.getHeldItemHotbarIndex();
            final Minecraft mc = ItemSwapFix.mc;
            if (heldItemHotbarIndex != Minecraft.player.inventory.currentItem) {
                final Minecraft mc2 = ItemSwapFix.mc;
                final NetHandlerPlayClient connection = Minecraft.player.connection;
                final Minecraft mc3 = ItemSwapFix.mc;
                connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
                eventPacket.cancel();
            }
        }
    }
}
