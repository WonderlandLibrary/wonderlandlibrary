package net.minecraft.command;

public class NumberInvalidException extends CommandException {
   public NumberInvalidException(String var1, Object... var2) {
      super(var1, var2);
   }

   public NumberInvalidException() {
      this("commands.generic.num.invalid");
   }
}
