package com.ibm.icu.text;

import com.ibm.icu.lang.CharSequences;
import java.util.HashSet;
import java.util.Set;

class SourceTargetUtility {
  final Transform<String, String> transform;
  
  final UnicodeSet sourceCache;
  
  final Set<String> sourceStrings;
  
  static final UnicodeSet NON_STARTERS = (new UnicodeSet("[:^ccc=0:]")).freeze();
  
  static Normalizer2 NFC = Normalizer2.getNFCInstance();
  
  public SourceTargetUtility(Transform<String, String> transform) {
    this(transform, null);
  }
  
  public SourceTargetUtility(Transform<String, String> transform, Normalizer2 normalizer) {
    this.transform = transform;
    if (normalizer != null) {
      this.sourceCache = new UnicodeSet("[:^ccc=0:]");
    } else {
      this.sourceCache = new UnicodeSet();
    } 
    this.sourceStrings = new HashSet<String>();
    for (int i = 0; i <= 1114111; i++) {
      String s = transform.transform(UTF16.valueOf(i));
      boolean added = false;
      if (!CharSequences.equals(i, s)) {
        this.sourceCache.add(i);
        added = true;
      } 
      if (normalizer != null) {
        String d = NFC.getDecomposition(i);
        if (d != null) {
          s = transform.transform(d);
          if (!d.equals(s))
            this.sourceStrings.add(d); 
          if (!added)
            if (!normalizer.isInert(i))
              this.sourceCache.add(i);  
        } 
      } 
    } 
    this.sourceCache.freeze();
  }
  
  public void addSourceTargetSet(Transliterator transliterator, UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    UnicodeSet myFilter = transliterator.getFilterAsUnicodeSet(inputFilter);
    UnicodeSet affectedCharacters = (new UnicodeSet(this.sourceCache)).retainAll(myFilter);
    sourceSet.addAll(affectedCharacters);
    for (String s : affectedCharacters)
      targetSet.addAll(this.transform.transform(s)); 
    for (String s : this.sourceStrings) {
      if (myFilter.containsAll(s)) {
        String t = this.transform.transform(s);
        if (!s.equals(t)) {
          targetSet.addAll(t);
          sourceSet.addAll(s);
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\SourceTargetUtility.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */