package org.alphacentauri.commands;

import java.util.ArrayList;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.WorldClient;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandUnban implements ICommandHandler {
   public String getName() {
      return "Unban";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length != 1) {
         AC.addChat(this.getName(), "Usage: unban <password>");
         return true;
      } else {
         String arg = args[0];
         EntityPlayerSP player = AC.getMC().getPlayer();
         AC.addChat(this.getName(), "Note: Only works on Everlag!");
         player.sendChatMessage("/login " + arg);
         player.sendChatMessage("/unregister " + arg);
         AC.getMC().session = AC.getAlts().getRandomCracked().login();
         AC.getMC().theWorld.sendQuittingDisconnectingPacket();
         AC.getMC().loadWorld((WorldClient)null);
         AC.getMC().displayGuiScreen(new GuiConnecting(new GuiMainMenu(), AC.getMC(), AC.getMC().getLastServerData()));
         return true;
      }
   }

   public String[] getAliases() {
      return new String[]{"unban"};
   }

   public String getDesc() {
      return "Only works on Everlag";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
