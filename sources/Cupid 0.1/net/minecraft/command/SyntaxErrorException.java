package net.minecraft.command;

public class SyntaxErrorException extends CommandException {
  public SyntaxErrorException() {
    this("commands.generic.snytax", new Object[0]);
  }
  
  public SyntaxErrorException(String message, Object... replacements) {
    super(message, replacements);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\SyntaxErrorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */