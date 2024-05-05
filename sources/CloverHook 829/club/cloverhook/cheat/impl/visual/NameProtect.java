package club.cloverhook.cheat.impl.visual;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.ProcessPacketEvent;
import club.cloverhook.event.minecraft.RenderNametagEvent;
import club.cloverhook.event.minecraft.SendPacketEvent;
import club.cloverhook.utils.property.impl.BooleanProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class NameProtect extends Cheat {
    public NameProtect() {
        super("Streaming", "Makes mods and admins (such as hapis) have a hard time finding your name on stream", CheatCategory.VISUAL);
    }

    int delaytime;

    public void onEnable() {

    }

    public void onDisable() {

    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
        delaytime++;
    }

    @Collect
    public void onSendPacket(SendPacketEvent sendPacketEvent) {

    }

    @Collect
    public void onProcessPacket(ProcessPacketEvent processPacketEvent) {
        if (processPacketEvent.getPacket() instanceof S02PacketChat && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("minemen")) {
            S02PacketChat packet = (S02PacketChat) processPacketEvent.getPacket();
            System.out.println(packet.getChatComponent().getUnformattedText());
            if (packet.getChatComponent().getUnformattedText().contains(mc.session.getUsername())) {
                processPacketEvent.setCancelled(true);
            }
            if (packet.getChatComponent().getUnformattedText().contains("Winner") && packet.getChatComponent().getUnformattedText().contains(mc.session.getUsername())) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "___________________________________________"));
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Canceled packets containing round info" + EnumChatFormatting.YELLOW + " - you won btw!"));
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "___________________________________________"));
            }
        }
    }

    @Collect
    public void onRenderNametag(RenderNametagEvent renderNametagEvent) {
        renderNametagEvent.setCancelled(true);
    }

}