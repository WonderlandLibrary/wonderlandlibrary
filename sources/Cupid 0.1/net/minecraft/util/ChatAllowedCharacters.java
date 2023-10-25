package net.minecraft.util;

public class ChatAllowedCharacters {
  public static final char[] allowedCharactersArray = new char[] { 
      '/', '\n', '\r', '\t', Character.MIN_VALUE, '\f', '`', '?', '*', '\\', 
      '<', '>', '|', '"', ':' };
  
  public static boolean isAllowedCharacter(char character) {
    return (character != '§' && character >= ' ' && character != '');
  }
  
  public static String filterAllowedCharacters(String input) {
    StringBuilder stringbuilder = new StringBuilder();
    for (char c0 : input.toCharArray()) {
      if (isAllowedCharacter(c0))
        stringbuilder.append(c0); 
    } 
    return stringbuilder.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\ChatAllowedCharacters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */