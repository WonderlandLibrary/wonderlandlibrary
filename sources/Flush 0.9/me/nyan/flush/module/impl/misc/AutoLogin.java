package me.nyan.flush.module.impl.misc;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventConnection;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventTick;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;

public class AutoLogin extends Module {
    private final String password = "ABCdef123";
    private boolean connected;
    private String lastMessage = null;

    public AutoLogin() {
        super("AutoLogin", Category.MISC);
    }

    @SubscribeEvent
    public void onConnection(EventConnection e) {
        connected = false;
        lastMessage = null;
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (mc.isIntegratedServerRunning() || connected) {
            return;
        }
        String serverIP = mc.getCurrentServerData().serverIP.toLowerCase();

        if (serverIP.contains("redesky")) {
            if (mc.currentScreen instanceof GuiChest) {
                GuiChest chest = (GuiChest) mc.currentScreen;
                String chestname = EnumChatFormatting.getTextWithoutFormattingCodes(chest.lowerChestInventory.getName()).toLowerCase();

                if (chestname.equalsIgnoreCase("clique no bloco verde")) {
                    for (Slot slot : chest.inventorySlots.inventorySlots) {
                        int slotNumber = slot.slotNumber;
                        ItemStack stack = chest.lowerChestInventory.getStackInSlot(slotNumber);
                        if (stack == null || !stack.hasDisplayName() || !EnumChatFormatting.getTextWithoutFormattingCodes(stack.getDisplayName()).
                                equalsIgnoreCase("clique aqui")) {
                            continue;
                        }

                        mc.playerController.windowClick(chest.inventorySlots.windowId, slotNumber, 0, 1, mc.thePlayer);
                        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/register " + password + " " + password));
                        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/login " + password));
                        connected = true;
                        break;
                    }
                }
            }
        } else if (serverIP.contains("rinaorc")) {
            if (lastMessage != null && !lastMessage.isEmpty()) {
                StringBuilder code = new StringBuilder();
                for (int i = lastMessage.length() - 1 - 1; i > lastMessage.length() - 1 - 1 - 4; i--) {
                    if (Character.isDigit(lastMessage.charAt(i))) {
                        code.insert(0, lastMessage.charAt(i));
                    }
                }

                if (code.length() == 4) {
                    if (lastMessage.toLowerCase().contains("register")) {
                        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/register " + password + " " + code));
                    } else if (lastMessage.toLowerCase().contains("login")) {
                        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/login " + password + " " + code));
                    }
                    connected = true;
                }
            }
        } else {
            if (lastMessage != null && !lastMessage.isEmpty()) {
                if (lastMessage.toLowerCase().contains("register")) {
                    mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/register " + password +
                            (serverIP.contains("funcraft") ? "" : " " + password)));
                    if (serverIP.contains("funcraft")) {
                        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/login " + password));
                    }
                } else if (lastMessage.toLowerCase().contains("login")) {
                    mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/login " + password));
                }
                connected = true;
            }
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = e.getPacket();
            if (packet.getType() != 2 && packet.getChatComponent().getUnformattedText().contains("login") ||
                    packet.getChatComponent().getUnformattedText().contains("register")) {
                lastMessage = EnumChatFormatting.getTextWithoutFormattingCodes(packet.getChatComponent().getUnformattedText());
            }
        }
    }
}
