package me.nyan.flush.module.impl.misc;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.Timer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import org.apache.commons.lang3.RandomStringUtils;

public class AutoPlay extends Module {
    private final NumberSetting delay = new NumberSetting("Delay", this, 100, 0, 1000);
    private final BooleanSetting autoGG = new BooleanSetting("Auto GG", this, true);
    private final Timer timer = new Timer();
    private boolean shouldSend = false;
    private boolean sentMessage = false;

    public AutoPlay() {
        super("AutoPlay", Category.MISC);
    }

    @Override
    public void onEnable() {
        shouldSend = false;
        timer.reset();
        super.onEnable();
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (shouldSend && timer.hasTimeElapsed(delay.getValueInt(), false)) {
            if (mc.getCurrentServerData().serverIP.toLowerCase().contains("funcraft")) {
                if (autoGG.getValue())
                    mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("@gg"));

                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/re"));
                shouldSend = false;
            } else if (mc.getCurrentServerData().serverIP.toLowerCase().contains("redesky")) {
                if (!sentMessage) {
                    if (autoGG.getValue()) {
                        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("gg" + " >" + RandomStringUtils.randomAlphanumeric(6) + "<"));
                    }

                    int lastSlot = mc.thePlayer.inventory.currentItem;
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(lastSlot));
                    sentMessage = true;
                }

                if (mc.currentScreen instanceof GuiChest) {
                    GuiChest chest = (GuiChest) mc.currentScreen;

                    for (Slot slot : chest.inventorySlots.inventorySlots) {
                        int slotNumber = slot.slotNumber;
                        ItemStack stack = chest.lowerChestInventory.getStackInSlot(slotNumber);
                        if (stack == null)
                            continue;

                        mc.playerController.windowClick(chest.inventorySlots.windowId, slotNumber, 0, 1, mc.thePlayer);
                        mc.thePlayer.closeScreen();
                        shouldSend = false;
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S02PacketChat) {
            String text = ((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText().toLowerCase();

            if (mc.getIntegratedServer() != null || shouldSend)
                return;

            if (mc.getCurrentServerData().serverIP.toLowerCase().contains("funcraft")) {
                if ((text.contains("afficher vos statistiques") && text.contains("retourner au lobby")) || (text.contains("yeah !") &&
                        text.contains("temps de jeu") && text.contains("vous remportez"))) {
                    shouldSend = true;
                    timer.reset();
                }
            } else if (mc.getCurrentServerData().serverIP.toLowerCase().contains("redesky")) {
                if (text.contains("este mapa") || text.contains("venceu a partida") || text.contains("ganhou")) {
                    shouldSend = true;
                    sentMessage = false;
                    timer.reset();
                }
            }
        }
    }
}