package me.aidanmees.trivia.client.bungeehack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TextFormatting
{
  BLACK("BLACK", 0, "BLACK", '0', 0),  DARK_BLUE("DARK_BLUE", 1, "DARK_BLUE", '1', 1),  DARK_GREEN("DARK_GREEN", 2, "DARK_GREEN", '2', 2),  DARK_AQUA("DARK_AQUA", 3, "DARK_AQUA", '3', 3),  DARK_RED("DARK_RED", 4, "DARK_RED", '4', 4),  DARK_PURPLE("DARK_PURPLE", 5, "DARK_PURPLE", '5', 5),  GOLD("GOLD", 6, "GOLD", '6', 6),  GRAY("GRAY", 7, "GRAY", '7', 7),  DARK_GRAY("DARK_GRAY", 8, "DARK_GRAY", '8', 8),  BLUE("BLUE", 9, "BLUE", '9', 9),  GREEN("GREEN", 10, "GREEN", 'a', 10),  AQUA("AQUA", 11, "AQUA", 'b', 11),  RED("RED", 12, "RED", 'c', 12),  LIGHT_PURPLE("LIGHT_PURPLE", 13, "LIGHT_PURPLE", 'd', 13),  YELLOW("YELLOW", 14, "YELLOW", 'e', 14),  WHITE("WHITE", 15, "WHITE", 'f', 15),  OBFUSCATED("OBFUSCATED", 16, "OBFUSCATED", 'k', true),  BOLD("BOLD", 17, "BOLD", 'l', true),  STRIKETHROUGH("STRIKETHROUGH", 18, "STRIKETHROUGH", 'm', true),  UNDERLINE("UNDERLINE", 19, "UNDERLINE", 'n', true),  ITALIC("ITALIC", 20, "ITALIC", 'o', true),  RESET("RESET", 21, "RESET", 'r', -1);
  
  private static final Map<String, TextFormatting> NAME_MAPPING;
  private static final Pattern FORMATTING_CODE_PATTERN;
  private final String name;
  private final char formattingCode;
  private final boolean fancyStyling;
  private final String controlString;
  private final int colorIndex;
  
  static
  {
    NAME_MAPPING = Maps.newHashMap();
    FORMATTING_CODE_PATTERN = Pattern.compile("(?i)" + String.valueOf('ง') + "[0-9A-FK-OR]");
    TextFormatting[] values;
    int length = (values = values()).length;
    for (int i = 0; i < length; i++)
    {
      TextFormatting textformatting = values[i];
      NAME_MAPPING.put(lowercaseAlpha(textformatting.name), textformatting);
    }
  }
  
  private static String lowercaseAlpha(String p_175745_0_)
  {
    return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
  }
  
  private TextFormatting(String s, int n, String formattingName, char formattingCodeIn, int colorIndex)
  {
    this(s, n, formattingName, formattingCodeIn, false, colorIndex);
  }
  
  private TextFormatting(String s, int n, String formattingName, char formattingCodeIn, boolean fancyStylingIn)
  {
    this(s, n, formattingName, formattingCodeIn, fancyStylingIn, -1);
  }
  
  private TextFormatting(String s, int n, String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex)
  {
    this.name = formattingName;
    this.formattingCode = formattingCodeIn;
    this.fancyStyling = fancyStylingIn;
    this.colorIndex = colorIndex;
    this.controlString = ("ยง" + formattingCodeIn);
  }
  
  public int getColorIndex()
  {
    return this.colorIndex;
  }
  
  public boolean isFancyStyling()
  {
    return this.fancyStyling;
  }
  
  public boolean isColor()
  {
    return (!this.fancyStyling) && (this != RESET);
  }
  
  public String getFriendlyName()
  {
    return name().toLowerCase();
  }
  
  public String toString()
  {
    return this.controlString;
  }
  
  public static String getTextWithoutFormattingCodes(String text)
  {
    return text == null ? null : FORMATTING_CODE_PATTERN.matcher(text).replaceAll("");
  }
  
  public static TextFormatting getValueByName(String friendlyName)
  {
    return friendlyName == null ? null : (TextFormatting)NAME_MAPPING.get(lowercaseAlpha(friendlyName));
  }
  
  public static TextFormatting fromColorIndex(int index)
  {
    if (index < 0) {
      return RESET;
    }
    TextFormatting[] values;
    int length = (values = values()).length;
    for (int i = 0; i < length; i++)
    {
      TextFormatting textformatting = values[i];
      if (textformatting.getColorIndex() == index) {
        return textformatting;
      }
    }
    return null;
  }
  
  public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_)
  {
    List<String> list = Lists.newArrayList();
    TextFormatting[] values;
    int length = (values = values()).length;
    for (int i = 0; i < length; i++)
    {
      TextFormatting textformatting = values[i];
      if (((!textformatting.isColor()) || (p_96296_0_)) && ((!textformatting.isFancyStyling()) || (p_96296_1_))) {
        list.add(textformatting.getFriendlyName());
      }
    }
    return list;
  }
}
