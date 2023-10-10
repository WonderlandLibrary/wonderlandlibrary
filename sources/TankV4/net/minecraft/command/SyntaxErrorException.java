package net.minecraft.command;

public class SyntaxErrorException extends CommandException {
   public SyntaxErrorException() {
      this("commands.generic.snytax");
   }

   public SyntaxErrorException(String var1, Object... var2) {
      super(var1, var2);
   }
}
