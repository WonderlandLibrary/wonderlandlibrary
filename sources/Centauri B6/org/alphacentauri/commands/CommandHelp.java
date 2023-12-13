package org.alphacentauri.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.CommandHandlerModule;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.TypeUtils;

public class CommandHelp implements ICommandHandler {
   private final int commandsPerPage = 10;

   public String getName() {
      return "Help";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         this.showPage(1);
      } else if(args.length == 1) {
         if(TypeUtils.isInteger(args[0])) {
            int i = Integer.parseInt(args[0]);
            if(i < 1) {
               i = 1;
            }

            if(i > this.getMaxPages()) {
               i = this.getMaxPages();
            }

            this.showPage(i);
         } else {
            ICommandHandler handler = AC.getCommandManager().getCommand(args[0]);
            if(handler != null) {
               AC.addChat(this.getName(), "Help:");
               if(handler instanceof CommandHandlerModule) {
                  Module module = ((CommandHandlerModule)handler).module;
                  AC.addChat(this.getName(), AC.getConfig().getPrefix() + module.getAliases()[0] + " [property] [value] - " + handler.getDesc());
                  AC.addChat(this.getName(), module.getName() + " is currently bypassing " + module.getBypass().name());
                  AC.addChat(this.getName(), "Properties of " + module.getName() + ":");

                  for(Property property : AC.getPropertyManager().ofModule(module)) {
                     if(property.isVisible()) {
                        AC.addChat(this.getName(), property.getName() + ": Currently: " + property.value + " Default: " + property.getDefaultValue() + " Type: " + property.value.getClass().getSimpleName());
                     }
                  }
               } else {
                  AC.addChat(this.getName(), AC.getConfig().getPrefix() + handler.getAliases()[0] + " - " + handler.getDesc());
               }
            } else {
               AC.addChat(this.getName(), "Unknown Command!");
            }
         }
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"help"};
   }

   public String getDesc() {
      return "Shows some help";
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         ret.addAll((Collection)this.getAllModuleAliases().stream().filter((string) -> {
            return string.startsWith(args[0]);
         }).collect(Collectors.toCollection(ArrayList::<init>)));
      }

      return ret;
   }

   private List getAllModuleAliases() {
      List<String> aliases = new ArrayList();

      for(Module module : AC.getModuleManager().all()) {
         Collections.addAll(aliases, module.getAliases());
      }

      return aliases;
   }

   private void showPage(int page) {
      AC.addChat(this.getName(), "§6Available Commands [Page " + page + "/" + this.getMaxPages() + "]:");
      int i = 0;

      for(ICommandHandler cmd : AC.getCommandManager().all()) {
         if(cmd.getAliases().length > 0) {
            if(i / 10 + 1 == page) {
               AC.addChat(this.getName(), AC.getConfig().getPrefix() + cmd.getAliases()[0] + " - " + cmd.getDesc());
            }

            ++i;
         }
      }

   }

   private int getMaxPages() {
      int i = 0;

      for(ICommandHandler cmd : AC.getCommandManager().all()) {
         if(cmd.getAliases().length > 0) {
            ++i;
         }
      }

      return i / 10 + 1;
   }
}
