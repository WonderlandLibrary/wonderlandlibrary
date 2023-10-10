package org.alphacentauri.management.managers;

import java.util.ArrayList;
import java.util.List;
import org.alphacentauri.AC;
import org.alphacentauri.commands.CommandBind;
import org.alphacentauri.commands.CommandBypass;
import org.alphacentauri.commands.CommandChatClear;
import org.alphacentauri.commands.CommandClickGui;
import org.alphacentauri.commands.CommandCrash;
import org.alphacentauri.commands.CommandDebug;
import org.alphacentauri.commands.CommandDebug2;
import org.alphacentauri.commands.CommandDesign;
import org.alphacentauri.commands.CommandExport;
import org.alphacentauri.commands.CommandFixInv;
import org.alphacentauri.commands.CommandFriend;
import org.alphacentauri.commands.CommandGive;
import org.alphacentauri.commands.CommandHClip;
import org.alphacentauri.commands.CommandHelp;
import org.alphacentauri.commands.CommandIRC;
import org.alphacentauri.commands.CommandPrefix;
import org.alphacentauri.commands.CommandSay;
import org.alphacentauri.commands.CommandScript;
import org.alphacentauri.commands.CommandShowOverlay;
import org.alphacentauri.commands.CommandTabGui;
import org.alphacentauri.commands.CommandToggle;
import org.alphacentauri.commands.CommandUnban;
import org.alphacentauri.commands.CommandUsers;
import org.alphacentauri.commands.CommandVClip;
import org.alphacentauri.commands.CommandVClipAAC;
import org.alphacentauri.commands.CommandWatermark;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.CommandHandlerModule;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.modules.Module;

public class CommandManager {
   private ArrayList commandHandlers = new ArrayList();

   public CommandManager() {
      this.registerCommandHandlers();
   }

   public ArrayList all() {
      return this.commandHandlers;
   }

   public void execCommand(String input) {
      Command command = this.parseCommand(input);
      ICommandHandler handler = this.getCommand(command.getCommand());
      if(handler != null) {
         boolean success = handler.execute(command);
         if(!success) {
            AC.addChat("Commands", "Command execution failed!");
         }
      } else {
         AC.addChat("Commands", "Command not found!");
      }

   }

   public List autocomplete(String input) {
      Command command = this.parseCommandForAutoComplete(input);
      ArrayList<String> ret = new ArrayList();
      if(command.getCommand().isEmpty()) {
         ret.add(AC.getConfig().getPrefix() + "help");
         return ret;
      } else {
         ICommandHandler cmdHandler = this.getCommand(command.getCommand());
         if(cmdHandler != null) {
            return cmdHandler.autocomplete(command);
         } else {
            for(ICommandHandler commandHandler : this.commandHandlers) {
               for(String alias : commandHandler.getAliases()) {
                  if(alias.startsWith(command.getCommand())) {
                     ret.add(AC.getConfig().getPrefix() + alias);
                     break;
                  }
               }
            }

            return ret;
         }
      }
   }

   private void registerCommand(ICommandHandler handler) {
      this.commandHandlers.add(handler);
   }

   private void registerCommandHandlers() {
      for(Module module : AC.getModuleManager().all()) {
         this.registerCommand(new CommandHandlerModule(module));
      }

      this.registerCommand(new CommandBypass());
      this.registerCommand(new CommandShowOverlay());
      this.registerCommand(new CommandBind());
      this.registerCommand(new CommandVClip());
      this.registerCommand(new CommandHelp());
      this.registerCommand(new CommandUnban());
      this.registerCommand(new CommandPrefix());
      this.registerCommand(new CommandTabGui());
      this.registerCommand(new CommandChatClear());
      this.registerCommand(new CommandSay());
      this.registerCommand(new CommandHClip());
      this.registerCommand(new CommandCrash());
      this.registerCommand(new CommandScript());
      this.registerCommand(new CommandFriend());
      this.registerCommand(new CommandGive());
      this.registerCommand(new CommandWatermark());
      this.registerCommand(new CommandClickGui());
      this.registerCommand(new CommandFixInv());
      this.registerCommand(new CommandVClipAAC());
      this.registerCommand(new CommandUsers());
      this.registerCommand(new CommandExport());
      this.registerCommand(new CommandToggle());
      this.registerCommand(new CommandIRC());
      this.registerCommand(new CommandDesign());
      if(AC.isDebug()) {
         this.registerCommand(new CommandDebug());
         this.registerCommand(new CommandDebug2());
      }

   }

   private Command parseCommandForAutoComplete(String input) {
      return Command.parseAutoComplete(input);
   }

   public ICommandHandler getCommand(String cmd) {
      for(ICommandHandler handler : this.commandHandlers) {
         for(String alias : handler.getAliases()) {
            if(alias.equalsIgnoreCase(cmd)) {
               return handler;
            }
         }
      }

      return null;
   }

   private Command parseCommand(String input) {
      return Command.parse(input);
   }
}
