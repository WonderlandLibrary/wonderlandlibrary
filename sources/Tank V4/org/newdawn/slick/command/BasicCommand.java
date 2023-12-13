package org.newdawn.slick.command;

public class BasicCommand implements Command {
   private String name;

   public BasicCommand(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof BasicCommand ? ((BasicCommand)var1).name.equals(this.name) : false;
   }

   public String toString() {
      return "[Command=" + this.name + "]";
   }
}
