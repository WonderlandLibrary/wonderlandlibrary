package team.massacre.api.command;

import java.util.Arrays;
import java.util.Iterator;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.manager.Manager;
import team.massacre.impl.command.ConfigCommand;
import team.massacre.impl.command.HelpCommand;
import team.massacre.impl.event.EventSendMessage;
import team.massacre.utils.Logger;

public final class CommandManager extends Manager<Command> {
   private static final String PREFIX = ".";
   private static final String HELP_MESSAGE = "Try '.help'";

   public CommandManager() {
      super(Arrays.asList(new HelpCommand(), new ConfigCommand()));
      Massacre.INSTANCE.getEventManager().register(this);
   }

   @Handler
   public void a(EventSendMessage event) {
      String message;
      if ((message = event.getMessage()).startsWith(".")) {
         event.setCancelled(true);
         String removedPrefix = message.substring(1);
         String[] arguments = removedPrefix.split(" ");
         if (!removedPrefix.isEmpty() && arguments.length > 0) {
            Iterator var5 = this.getElements().iterator();

            while(var5.hasNext()) {
               Command command = (Command)var5.next();
               String[] var7 = command.getAliases();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  String alias = var7[var9];
                  if (alias.equalsIgnoreCase(arguments[0])) {
                     try {
                        if (!command.testArgsCount(arguments.length)) {
                           throw new CommandExecutionException(command.getUsage());
                        }

                        command.execute(arguments);
                     } catch (CommandExecutionException var12) {
                        Logger.print("Invalid command syntax. Hint: " + var12.getMessage());
                     }

                     return;
                  }
               }
            }

            Logger.print(String.format("'%s' is not a command. %s", arguments[0], "Try '.help'"));
         } else {
            Logger.print("No arguments were supplied. Try '.help'");
         }
      }

   }
}
