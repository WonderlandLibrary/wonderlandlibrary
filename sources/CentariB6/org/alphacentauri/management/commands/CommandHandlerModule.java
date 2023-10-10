package org.alphacentauri.management.commands;

import java.util.ArrayList;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.modules.Module;

public class CommandHandlerModule implements ICommandHandler {
   public final Module module;

   public CommandHandlerModule(Module module) {
      this.module = module;
   }

   public String getName() {
      return this.module.getName();
   }

   public boolean execute(Command cmd) {
      return this.module.onCommand(cmd);
   }

   public String[] getAliases() {
      return this.module.getAliases();
   }

   public String getDesc() {
      return this.module.getDescription();
   }

   public ArrayList autocomplete(Command cmd) {
      return this.module.autocomplete(cmd);
   }
}
