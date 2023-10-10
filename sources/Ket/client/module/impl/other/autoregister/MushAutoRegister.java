package client.module.impl.other.autoregister;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.event.impl.other.UpdateEvent;
import client.module.impl.other.AutoRegister;
import client.util.TimeUtil;
import client.value.Mode;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.Sys;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MushAutoRegister extends Mode<AutoRegister> {

    public MushAutoRegister(final String name, final AutoRegister parent) {
        super(name, parent);
    }

    private final TimeUtil kek = new TimeUtil();
    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (mc.isSingleplayer()) return;
        if (mc.currentScreen instanceof GuiChest) {
            final GuiChest chest = (GuiChest) mc.currentScreen;
            for (int i = 0; i < chest.getInventoryRows() * 9; i++) {
                final Slot slot = chest.inventorySlots.getSlot(i);
                if (slot.getHasStack() && slot.getStack().hasTagCompound() && slot.getStack().getTagCompound().getTag("SkullOwner") != null && !slot.getStack().getTagCompound().getTag("SkullOwner").toString().contains("Properties:{textures:[0:{Signature:\"JPWjQdKHO7E2eULma/B/LTkEUK5A4W56txhthM+lH+lpcgubUCTQrLuXkg2A3ppMM4fwbDw9BJzp5DztMTl0jgD85Bw4kGQzqtIdrbh/ipNAgGn9RVEm+hao/T9i2EYSBMqNvK4UQjeQsImkuTxJwMKK5FO/yGZLtwFFhZnZMFSwm7qEI+Lerw3gQZ7NIn3u9wmv7eBPnd3oeuB+Lad+Fl5jNiWclqSqVYCb6VmA4M88ZnBnGZWM6FsWdZ+IdyLGiZcwx8DeRU3+PPHL2ujvncaeHe1p8Blauxy1jo/8Z/egPzsw5pVoSJbz+FNFQgm7+r7Q+s2pmhJ8lBb3apJfuI1ZIzZIrYKXg+DtQjIp2xD+wqGeplL9U9+OeD6kW8uTT943AlJ6xm1ljSOs6iPlhpvkmm+1q2TmtVuJxAguc2EIKsVtxjYQp7rfoxmlaKVL+UjvjI8M+JN2dQ3sAG/GLgB7MwDmjsa1rn9W4QG7hYqfG97f5sai6xHOFieEWPUwcIv3jz5FWUoeIvQkf0k+6vLC3L2bxTWdOZjK1xI5OALZ/yDkH8whwIeF1RBzSgn0+4OZSu+r+igw1SiBgKhFLq3DaFBglx62AObNZy8Gd9GrtC44gqG9zZTKJ3wAU3fdQKU13fVQ7dF2bY18jmm0jiDZxm5ACLuLA2bQt4LPb/I=\",Value:\"ewogICJ0aW1lc3RhbXAiIDogMTYwMjcxOTI0NDUyMywKICAicHJvZmlsZUlkIiA6ICJhZDJjYzAyMjUxNWU0YjNiYjY4ZWU2YTlkZjEwYzFiOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTd2FydGgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWMwNTk3MDUyZTQwZWEzMDEzYmQzMTQ2N2JlZTYzZTE2NjViOTcwOWE3YWJkMDdhODBhYWM2N2E1ZjBjYWYwYyIKICAgIH0KICB9Cn0=\"}]}")) mc.thePlayer.sendQueue.addToSendQueue(new C0EPacketClickWindow(chest.inventorySlots.windowId, i, 0, 0, slot.getStack(), chest.inventorySlots.getNextTransactionID(mc.thePlayer.inventory)));
            }
        }
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) packet;
            final String message = wrapper.getChatComponent().getUnformattedText(), password = "baller123";
            if (message.equals("Crie uma conta utilizando /register <senha> <senha>")) mc.thePlayer.sendChatMessage("/register " + password + " " + password);
            if (message.equals("Faça o login utilizando /login <senha>")) mc.thePlayer.sendChatMessage("/login " + password);
            if (message.equals("Você se registrou com sucesso!") || message.equals("Você se autenticou com sucesso!")) {
                if (getParent().getMushGen().getValue()) {
                    try {
                        final BufferedWriter bw = new BufferedWriter(new FileWriter("alts.txt", true));
                        bw.append(mc.getSession().getUsername()).append("\n");
                        bw.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    mc.theWorld.sendQuittingDisconnectingPacket();
                }
            }
        }
    };
}
