package me.rhys.client.command;

import me.rhys.base.Lite;
import me.rhys.base.command.Command;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.EnumChatFormatting;

public class PluginsCommand extends Command {
  public PluginsCommand(String label, String description, String... aliases) {
    super(label, description, aliases);
  }
  
  public boolean handle(EntityPlayerSP player, String label, String[] args) {
    Lite.EVENT_BUS.register(this);
    player.sendQueue.addToSendQueue((Packet)new C14PacketTabComplete("/"));
    return true;
  }
  
  @EventTarget
  public void onPacket(PacketEvent e) {
    if (e.getPacket() instanceof S3APacketTabComplete) {
      S3APacketTabComplete s3APacketTabComplete = (S3APacketTabComplete)e.getPacket();
      String[] commands = s3APacketTabComplete.func_149630_c();
      StringBuilder message = new StringBuilder();
      int size = 0;
      for (String command : commands) {
        String pluginName = command.split(":")[0].substring(1);
        if (!message.toString().contains(pluginName) && command.contains(":") && 
          !pluginName.equalsIgnoreCase("minecraft") && !pluginName.equalsIgnoreCase("bukkit")) {
          size++;
          if (message.length() == 0) {
            message.append(pluginName);
          } else {
            message.append(", " + EnumChatFormatting.GREEN).append(pluginName);
          } 
        } 
      } 
      if (message.length() == 0) {
        (Minecraft.getMinecraft()).thePlayer.sendMessage("No Plugins.");
      } else {
        (Minecraft.getMinecraft()).thePlayer.sendMessage(String.format("Plugins: (%s) ", new Object[] { Integer.valueOf(size) }) + EnumChatFormatting.GREEN + message.toString());
      } 
      Lite.EVENT_BUS.unRegister(this);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\command\PluginsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */