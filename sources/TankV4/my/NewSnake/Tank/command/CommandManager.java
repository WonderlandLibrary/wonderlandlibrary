package my.NewSnake.Tank.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import my.NewSnake.Tank.command.commands.OptionCommand;
import my.NewSnake.Tank.command.commands.UnknownCommand;
import org.reflections.Reflections;

public class CommandManager {
   public static OptionCommand optionCommand = new OptionCommand();
   public static final UnknownCommand COMMAND_UNKNOWN = new UnknownCommand();
   public static List commandList = new ArrayList();

   public static void start() {
      try {
         Reflections var0 = new Reflections(new Object[]{"my.NewSnake.Tank.command.commands", new Scanner[0]});
         Set var1 = var0.getSubTypesOf(Command.class);
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Class var2 = (Class)var3.next();
            Command var4 = (Command)var2.newInstance();
            if (var2.isAnnotationPresent(Com.class)) {
               Com var5 = (Com)var2.getAnnotation(Com.class);
               var4.setNames(var5.names());
               commandList.add(var4);
            }
         }

         commandList.add(optionCommand);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static Command getCommandFromMessage(String var0) {
      Iterator var2 = commandList.iterator();

      while(var2.hasNext()) {
         Command var1 = (Command)var2.next();
         if (var1.getNames() == null) {
            return new UnknownCommand();
         }

         String[] var3;
         int var4 = (var3 = var1.getNames()).length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var0.split(" ")[0].equalsIgnoreCase(var6)) {
               return var1;
            }
         }
      }

      return COMMAND_UNKNOWN;
   }
}
