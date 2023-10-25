package net.minecraft.command;

public class WrongUsageException extends SyntaxErrorException {
  public WrongUsageException(String message, Object... replacements) {
    super(message, replacements);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\WrongUsageException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */