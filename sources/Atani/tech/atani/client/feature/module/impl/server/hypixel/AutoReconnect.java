package tech.atani.client.feature.module.impl.server.hypixel;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.interfaces.Methods;

@ModuleData(name = "AutoReconnect", identifier = "mc.hypixel.net AutoReconnect", description = "Automatically reconnects to games.", category = Category.SERVER, supportedIPs = {"mc.hypixel.net"})
public class AutoReconnect extends Module {

    @Listen
    public void onPacket(PacketEvent event) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        if(event.getPacket() instanceof S02PacketChat) {
            String unformatted = EnumChatFormatting.getTextWithoutFormattingCodes(((S02PacketChat)event.getPacket()).getChatComponent().getUnformattedText()), text = unformatted.replace(" ", "");
            if(unformatted.contains("Flying or related")) {
                sendPacketUnlogged(new C01PacketChatMessage("/back"));
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
