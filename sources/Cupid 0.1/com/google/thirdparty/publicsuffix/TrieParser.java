package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;

@GwtCompatible
class TrieParser {
  private static final Joiner PREFIX_JOINER = Joiner.on("");
  
  static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded) {
    ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
    int encodedLen = encoded.length();
    int idx = 0;
    while (idx < encodedLen)
      idx += doParseTrieToBuilder(Lists.newLinkedList(), encoded.subSequence(idx, encodedLen), builder); 
    return builder.build();
  }
  
  private static int doParseTrieToBuilder(List<CharSequence> stack, CharSequence encoded, ImmutableMap.Builder<String, PublicSuffixType> builder) {
    int encodedLen = encoded.length();
    int idx = 0;
    char c = Character.MIN_VALUE;
    for (; idx < encodedLen; idx++) {
      c = encoded.charAt(idx);
      if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',')
        break; 
    } 
    stack.add(0, reverse(encoded.subSequence(0, idx)));
    if (c == '!' || c == '?' || c == ':' || c == ',') {
      String domain = PREFIX_JOINER.join(stack);
      if (domain.length() > 0)
        builder.put(domain, PublicSuffixType.fromCode(c)); 
    } 
    idx++;
    if (c != '?' && c != ',')
      while (idx < encodedLen) {
        idx += doParseTrieToBuilder(stack, encoded.subSequence(idx, encodedLen), builder);
        if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
          idx++;
          break;
        } 
      }  
    stack.remove(0);
    return idx;
  }
  
  private static CharSequence reverse(CharSequence s) {
    int length = s.length();
    if (length <= 1)
      return s; 
    char[] buffer = new char[length];
    buffer[0] = s.charAt(length - 1);
    for (int i = 1; i < length; i++) {
      buffer[i] = s.charAt(length - 1 - i);
      if (Character.isSurrogatePair(buffer[i], buffer[i - 1]))
        swap(buffer, i - 1, i); 
    } 
    return new String(buffer);
  }
  
  private static void swap(char[] buffer, int f, int s) {
    char tmp = buffer[f];
    buffer[f] = buffer[s];
    buffer[s] = tmp;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\thirdparty\publicsuffix\TrieParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */