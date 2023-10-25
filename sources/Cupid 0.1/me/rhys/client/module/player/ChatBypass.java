package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;

public class ChatBypass extends Module {
  public ChatBypass(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onPacketRe(PacketEvent e) {
    if (e.getPacket() instanceof S02PacketChat) {
      S02PacketChat packet = (S02PacketChat)e.getPacket();
      if (packet.getChatComponent().getUnformattedText().contains("؜"))
        new ChatComponentText(packet.getChatComponent().getUnformattedText().replace("؜", "")); 
    } 
  }
  
  @EventTarget
  public void onSendPacket(PacketEvent event) {
    if (event.getPacket() instanceof C01PacketChatMessage) {
      C01PacketChatMessage packetChatMessage = (C01PacketChatMessage)event.getPacket();
      if (packetChatMessage.getMessage().startsWith("/"))
        return; 
      event.setCancelled(true);
      StringBuilder msg = new StringBuilder();
      for (char character : packetChatMessage.getMessage().toCharArray())
        msg.append(character + "؜"); 
      this.mc.getNetHandler().getNetworkManager().sendPacketNoEvent((Packet)new C01PacketChatMessage(msg.toString().replaceFirst("%", "")));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\ChatBypass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */