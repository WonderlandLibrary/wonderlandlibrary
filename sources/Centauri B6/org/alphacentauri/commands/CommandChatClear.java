package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandChatClear implements ICommandHandler {
   public String getName() {
      return "ClearChat";
   }

   public boolean execute(Command cmd) {
      AC.getMC().ingameGUI.getChatGUI().clearChatMessages();
      return true;
   }

   public String[] getAliases() {
      return new String[]{"clearchat", "clear", "cc"};
   }

   public String getDesc() {
      return "Clears the chat";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
