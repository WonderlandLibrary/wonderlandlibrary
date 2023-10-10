package net.minecraft.command;

public class CommandNotFoundException extends CommandException {
   public CommandNotFoundException(String var1, Object... var2) {
      super(var1, var2);
   }

   public CommandNotFoundException() {
      this("commands.generic.notFound");
   }
}
