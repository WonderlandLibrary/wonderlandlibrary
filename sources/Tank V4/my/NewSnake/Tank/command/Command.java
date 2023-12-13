package my.NewSnake.Tank.command;

public class Command {
   private String[] names;

   public String[] getNames() {
      return this.names;
   }

   public String getHelp() {
      return null;
   }

   public void runCommand(String[] var1) {
   }

   public void setNames(String[] var1) {
      this.names = var1;
   }
}
