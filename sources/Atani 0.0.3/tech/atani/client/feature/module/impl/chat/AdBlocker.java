package tech.atani.client.feature.module.impl.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;

import java.util.regex.Pattern;

@ModuleData(name = "AdBlocker", description = "Doesn't show spam and advertisement", category = Category.CHAT)
public class AdBlocker extends Module {

    private String chatCommonAdvertisements = "(?!.+: )(/?(((party join|join party)|p join|(guild join)|(join guild)|g join) \\w{1,16})|/?(party me|visit me|duel me|my ah|my smp)|(twitch.tv)|(youtube.com|youtu.be)|(/(visit|ah) \\w{1,16}|(visit /\\w{1,16})|(/gift)|(gilde)|(lowballing|lowbaling|lowvaling|lowvaluing|lowballer)))";
    private String chatRankBegging = "(?!.+: )([^\\[](vip|mvp|mpv|vpi)|(please|pls|plz|rank ?up|rank ?upgrade)|(buy|upgrade|gift|give) (rank|me)|(gifting|gifters)|( beg |begging|beggers))";
    private String chatCleanerBedwarsPartyAdvertisement = "(?!.+: )(([1-8]/[1-8]|[1-8]v[1-8]|[2-8]s)|(any|rbw|ranked))";

    private Pattern chatCommonAdvertisementsRegex, chatRankBeggingRegex, chatCleanerBedwarsPartyAdvertisementRegex;

    private static final String[] CZ = new String[] {
            "stream na", "streamuju", "kanal", "kanál", "odběr", "odber"
    };

    private static final String[] NORMAL = new String[] {
            "sub", "subscribe", "follow me", "youtu.be", "youtube.com", "twitch.tv", "store.blockdrop.org", "HelpBot -> me"
    };

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if(chatCommonAdvertisementsRegex == null || chatRankBeggingRegex == null) {
            chatCommonAdvertisementsRegex = Pattern.compile(chatCommonAdvertisements, Pattern.CASE_INSENSITIVE);
            chatRankBeggingRegex = Pattern.compile(chatRankBegging, Pattern.CASE_INSENSITIVE);
            chatCleanerBedwarsPartyAdvertisementRegex = Pattern.compile(chatCleanerBedwarsPartyAdvertisement, Pattern.CASE_INSENSITIVE);
        }

        if(packetEvent.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat) packetEvent.getPacket();
            String message = EnumChatFormatting.getTextWithoutFormattingCodes(s02PacketChat.getChatComponent().getUnformattedText().toLowerCase());
            hytilsSkid: {
                if ((message.startsWith("-") && message.endsWith("-")) || (message.startsWith("▬") && message.endsWith("▬")) || (message.startsWith("≡") && message.endsWith("≡")) || (!message.contains(": ")) || (message.contains(Minecraft.getMinecraft().getSession().getUsername().toLowerCase()))) break hytilsSkid;
                if (chatCommonAdvertisementsRegex.matcher(message).find(0) || chatRankBeggingRegex.matcher(message).find(0) || chatCleanerBedwarsPartyAdvertisementRegex.matcher(message).find(0)) {
                    packetEvent.setCancelled(true);
                }
            }
            for(String string : CZ) {
                if(message.contains(string))
                    packetEvent.setCancelled(true);
            }
            for(String string : NORMAL) {
                if(message.contains(string))
                    packetEvent.setCancelled(true);
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
