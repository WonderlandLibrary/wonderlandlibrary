package com.ibm.icu.impl;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.ReplaceableString;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeMatcher;

public class UtilityExtensions {
  public static void appendToRule(StringBuffer rule, String text, boolean isLiteral, boolean escapeUnprintable, StringBuffer quoteBuf) {
    for (int i = 0; i < text.length(); i++)
      Utility.appendToRule(rule, text.charAt(i), isLiteral, escapeUnprintable, quoteBuf); 
  }
  
  public static void appendToRule(StringBuffer rule, UnicodeMatcher matcher, boolean escapeUnprintable, StringBuffer quoteBuf) {
    if (matcher != null)
      appendToRule(rule, matcher.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf); 
  }
  
  public static String formatInput(ReplaceableString input, Transliterator.Position pos) {
    StringBuffer appendTo = new StringBuffer();
    formatInput(appendTo, input, pos);
    return Utility.escape(appendTo.toString());
  }
  
  public static StringBuffer formatInput(StringBuffer appendTo, ReplaceableString input, Transliterator.Position pos) {
    if (0 <= pos.contextStart && pos.contextStart <= pos.start && pos.start <= pos.limit && pos.limit <= pos.contextLimit && pos.contextLimit <= input.length()) {
      String b = input.substring(pos.contextStart, pos.start);
      String c = input.substring(pos.start, pos.limit);
      String d = input.substring(pos.limit, pos.contextLimit);
      appendTo.append('{').append(b).append('|').append(c).append('|').append(d).append('}');
    } else {
      appendTo.append("INVALID Position {cs=" + pos.contextStart + ", s=" + pos.start + ", l=" + pos.limit + ", cl=" + pos.contextLimit + "} on " + input);
    } 
    return appendTo;
  }
  
  public static String formatInput(Replaceable input, Transliterator.Position pos) {
    return formatInput((ReplaceableString)input, pos);
  }
  
  public static StringBuffer formatInput(StringBuffer appendTo, Replaceable input, Transliterator.Position pos) {
    return formatInput(appendTo, (ReplaceableString)input, pos);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\UtilityExtensions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */