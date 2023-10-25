package net.minecraft.command;

public class PlayerNotFoundException extends CommandException {
  public PlayerNotFoundException() {
    this("commands.generic.player.notFound", new Object[0]);
  }
  
  public PlayerNotFoundException(String message, Object... replacements) {
    super(message, replacements);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\PlayerNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */