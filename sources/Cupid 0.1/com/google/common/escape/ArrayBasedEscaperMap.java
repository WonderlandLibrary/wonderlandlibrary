package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

@Beta
@GwtCompatible
public final class ArrayBasedEscaperMap {
  private final char[][] replacementArray;
  
  public static ArrayBasedEscaperMap create(Map<Character, String> replacements) {
    return new ArrayBasedEscaperMap(createReplacementArray(replacements));
  }
  
  private ArrayBasedEscaperMap(char[][] replacementArray) {
    this.replacementArray = replacementArray;
  }
  
  char[][] getReplacementArray() {
    return this.replacementArray;
  }
  
  @VisibleForTesting
  static char[][] createReplacementArray(Map<Character, String> map) {
    Preconditions.checkNotNull(map);
    if (map.isEmpty())
      return EMPTY_REPLACEMENT_ARRAY; 
    char max = ((Character)Collections.<Character>max(map.keySet())).charValue();
    char[][] replacements = new char[max + 1][];
    for (Iterator<Character> i$ = map.keySet().iterator(); i$.hasNext(); ) {
      char c = ((Character)i$.next()).charValue();
      replacements[c] = ((String)map.get(Character.valueOf(c))).toCharArray();
    } 
    return replacements;
  }
  
  private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\escape\ArrayBasedEscaperMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */