package net.minecraft.command;

public class CommandException extends Exception {
   private final Object[] errorObjects;

   public CommandException(String var1, Object... var2) {
      super(var1);
      this.errorObjects = var2;
   }

   public Object[] getErrorObjects() {
      return this.errorObjects;
   }
}
