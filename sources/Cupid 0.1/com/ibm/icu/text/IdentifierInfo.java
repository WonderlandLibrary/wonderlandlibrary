package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class IdentifierInfo {
  private static final UnicodeSet ASCII = (new UnicodeSet(0, 127)).freeze();
  
  private String identifier;
  
  private final BitSet requiredScripts = new BitSet();
  
  private final Set<BitSet> scriptSetSet = new HashSet<BitSet>();
  
  private final BitSet commonAmongAlternates = new BitSet();
  
  private final UnicodeSet numerics = new UnicodeSet();
  
  private final UnicodeSet identifierProfile = new UnicodeSet(0, 1114111);
  
  private IdentifierInfo clear() {
    this.requiredScripts.clear();
    this.scriptSetSet.clear();
    this.numerics.clear();
    this.commonAmongAlternates.clear();
    return this;
  }
  
  public IdentifierInfo setIdentifierProfile(UnicodeSet identifierProfile) {
    this.identifierProfile.set(identifierProfile);
    return this;
  }
  
  public UnicodeSet getIdentifierProfile() {
    return new UnicodeSet(this.identifierProfile);
  }
  
  public IdentifierInfo setIdentifier(String identifier) {
    this.identifier = identifier;
    clear();
    BitSet scriptsForCP = new BitSet();
    int i;
    for (i = 0; i < identifier.length(); i += Character.charCount(i)) {
      int cp = Character.codePointAt(identifier, i);
      if (UCharacter.getType(cp) == 9)
        this.numerics.add(cp - UCharacter.getNumericValue(cp)); 
      UScript.getScriptExtensions(cp, scriptsForCP);
      scriptsForCP.clear(0);
      scriptsForCP.clear(1);
      switch (scriptsForCP.cardinality()) {
        case 0:
          break;
        case 1:
          this.requiredScripts.or(scriptsForCP);
          break;
        default:
          if (!this.requiredScripts.intersects(scriptsForCP) && this.scriptSetSet.add(scriptsForCP))
            scriptsForCP = new BitSet(); 
          break;
      } 
    } 
    if (this.scriptSetSet.size() > 0) {
      this.commonAmongAlternates.set(0, 159);
      for (Iterator<BitSet> it = this.scriptSetSet.iterator(); it.hasNext(); ) {
        BitSet next = it.next();
        if (this.requiredScripts.intersects(next)) {
          it.remove();
          continue;
        } 
        this.commonAmongAlternates.and(next);
        for (BitSet other : this.scriptSetSet) {
          if (next != other && contains(next, other))
            it.remove(); 
        } 
      } 
    } 
    if (this.scriptSetSet.size() == 0)
      this.commonAmongAlternates.clear(); 
    return this;
  }
  
  public String getIdentifier() {
    return this.identifier;
  }
  
  public BitSet getScripts() {
    return (BitSet)this.requiredScripts.clone();
  }
  
  public Set<BitSet> getAlternates() {
    Set<BitSet> result = new HashSet<BitSet>();
    for (BitSet item : this.scriptSetSet)
      result.add((BitSet)item.clone()); 
    return result;
  }
  
  public UnicodeSet getNumerics() {
    return new UnicodeSet(this.numerics);
  }
  
  public BitSet getCommonAmongAlternates() {
    return (BitSet)this.commonAmongAlternates.clone();
  }
  
  private static final BitSet JAPANESE = set(new BitSet(), new int[] { 25, 17, 20, 22 });
  
  private static final BitSet CHINESE = set(new BitSet(), new int[] { 25, 17, 5 });
  
  private static final BitSet KOREAN = set(new BitSet(), new int[] { 25, 17, 18 });
  
  private static final BitSet CONFUSABLE_WITH_LATIN = set(new BitSet(), new int[] { 8, 14, 6 });
  
  public SpoofChecker.RestrictionLevel getRestrictionLevel() {
    if (!this.identifierProfile.containsAll(this.identifier) || getNumerics().size() > 1)
      return SpoofChecker.RestrictionLevel.UNRESTRICTIVE; 
    if (ASCII.containsAll(this.identifier))
      return SpoofChecker.RestrictionLevel.ASCII; 
    int cardinalityPlus = this.requiredScripts.cardinality() + ((this.commonAmongAlternates.cardinality() == 0) ? this.scriptSetSet.size() : 1);
    if (cardinalityPlus < 2)
      return SpoofChecker.RestrictionLevel.HIGHLY_RESTRICTIVE; 
    if (containsWithAlternates(JAPANESE, this.requiredScripts) || containsWithAlternates(CHINESE, this.requiredScripts) || containsWithAlternates(KOREAN, this.requiredScripts))
      return SpoofChecker.RestrictionLevel.HIGHLY_RESTRICTIVE; 
    if (cardinalityPlus == 2 && this.requiredScripts.get(25) && !this.requiredScripts.intersects(CONFUSABLE_WITH_LATIN))
      return SpoofChecker.RestrictionLevel.MODERATELY_RESTRICTIVE; 
    return SpoofChecker.RestrictionLevel.MINIMALLY_RESTRICTIVE;
  }
  
  public int getScriptCount() {
    int count = this.requiredScripts.cardinality() + ((this.commonAmongAlternates.cardinality() == 0) ? this.scriptSetSet.size() : 1);
    return count;
  }
  
  public String toString() {
    return this.identifier + ", " + this.identifierProfile.toPattern(false) + ", " + getRestrictionLevel() + ", " + displayScripts(this.requiredScripts) + ", " + displayAlternates(this.scriptSetSet) + ", " + this.numerics.toPattern(false);
  }
  
  private boolean containsWithAlternates(BitSet container, BitSet containee) {
    if (!contains(container, containee))
      return false; 
    for (BitSet alternatives : this.scriptSetSet) {
      if (!container.intersects(alternatives))
        return false; 
    } 
    return true;
  }
  
  public static String displayAlternates(Set<BitSet> alternates) {
    if (alternates.size() == 0)
      return ""; 
    StringBuilder result = new StringBuilder();
    Set<BitSet> sorted = new TreeSet<BitSet>(BITSET_COMPARATOR);
    sorted.addAll(alternates);
    for (BitSet item : sorted) {
      if (result.length() != 0)
        result.append("; "); 
      result.append(displayScripts(item));
    } 
    return result.toString();
  }
  
  public static final Comparator<BitSet> BITSET_COMPARATOR = new Comparator<BitSet>() {
      public int compare(BitSet arg0, BitSet arg1) {
        int diff = arg0.cardinality() - arg1.cardinality();
        if (diff != 0)
          return diff; 
        int i0 = arg0.nextSetBit(0);
        int i1 = arg1.nextSetBit(0);
        while ((diff = i0 - i1) == 0 && i0 > 0) {
          i0 = arg0.nextSetBit(i0 + 1);
          i1 = arg1.nextSetBit(i1 + 1);
        } 
        return diff;
      }
    };
  
  public static String displayScripts(BitSet scripts) {
    StringBuilder result = new StringBuilder();
    for (int i = scripts.nextSetBit(0); i >= 0; i = scripts.nextSetBit(i + 1)) {
      if (result.length() != 0)
        result.append(' '); 
      result.append(UScript.getShortName(i));
    } 
    return result.toString();
  }
  
  public static BitSet parseScripts(String scriptsString) {
    BitSet result = new BitSet();
    for (String item : scriptsString.trim().split(",?\\s+")) {
      if (item.length() != 0)
        result.set(UScript.getCodeFromName(item)); 
    } 
    return result;
  }
  
  public static Set<BitSet> parseAlternates(String scriptsSetString) {
    Set<BitSet> result = new HashSet<BitSet>();
    for (String item : scriptsSetString.trim().split("\\s*;\\s*")) {
      if (item.length() != 0)
        result.add(parseScripts(item)); 
    } 
    return result;
  }
  
  public static final boolean contains(BitSet container, BitSet containee) {
    for (int i = containee.nextSetBit(0); i >= 0; i = containee.nextSetBit(i + 1)) {
      if (!container.get(i))
        return false; 
    } 
    return true;
  }
  
  public static final BitSet set(BitSet bitset, int... values) {
    for (int value : values)
      bitset.set(value); 
    return bitset;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\IdentifierInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */