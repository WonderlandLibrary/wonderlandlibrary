package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.core.CloudAPI;
import org.alphacentauri.core.CoreListener;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandUsers implements ICommandHandler {
   public String getName() {
      return "Users";
   }

   public boolean execute(Command cmd) {
      CloudAPI.requestUserList(CoreListener.server);
      return true;
   }

   public String[] getAliases() {
      return new String[]{"users"};
   }

   public String getDesc() {
      return "Shows who\'s online on your server with alpha centauri";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
