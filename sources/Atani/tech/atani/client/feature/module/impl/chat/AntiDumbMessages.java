package tech.atani.client.feature.module.impl.chat;

import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;

@ModuleData(name = "AntiDumbMessages", description = "Doesn't show dumb spam like \"gg\", \"gl\", etc.", category = Category.CHAT)
public class AntiDumbMessages extends Module {

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if(packetEvent.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat) packetEvent.getPacket();
            String message = EnumChatFormatting.getTextWithoutFormattingCodes(s02PacketChat.getChatComponent().getUnformattedText()).toLowerCase();
            if(message.contains("gl") || message.contains("gg") || message.contains("hf")) {
                packetEvent.setCancelled(true);
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
