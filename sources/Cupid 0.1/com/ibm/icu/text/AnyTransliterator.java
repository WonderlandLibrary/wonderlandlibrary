package com.ibm.icu.text;

import com.ibm.icu.lang.UScript;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

class AnyTransliterator extends Transliterator {
  static final char TARGET_SEP = '-';
  
  static final char VARIANT_SEP = '/';
  
  static final String ANY = "Any";
  
  static final String NULL_ID = "Null";
  
  static final String LATIN_PIVOT = "-Latin;Latin-";
  
  private Map<Integer, Transliterator> cache;
  
  private String target;
  
  private int targetScript;
  
  private Transliterator widthFix = Transliterator.getInstance("[[:dt=Nar:][:dt=Wide:]] nfkd");
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean isIncremental) {
    int allStart = pos.start;
    int allLimit = pos.limit;
    ScriptRunIterator it = new ScriptRunIterator(text, pos.contextStart, pos.contextLimit);
    while (it.next()) {
      if (it.limit <= allStart)
        continue; 
      Transliterator t = getTransliterator(it.scriptCode);
      if (t == null) {
        pos.start = it.limit;
        continue;
      } 
      boolean incremental = (isIncremental && it.limit >= allLimit);
      pos.start = Math.max(allStart, it.start);
      pos.limit = Math.min(allLimit, it.limit);
      int limit = pos.limit;
      t.filteredTransliterate(text, pos, incremental);
      int delta = pos.limit - limit;
      allLimit += delta;
      it.adjustLimit(delta);
      if (it.limit >= allLimit)
        break; 
    } 
    pos.limit = allLimit;
  }
  
  private AnyTransliterator(String id, String theTarget, String theVariant, int theTargetScript) {
    super(id, null);
    this.targetScript = theTargetScript;
    this.cache = new HashMap<Integer, Transliterator>();
    this.target = theTarget;
    if (theVariant.length() > 0)
      this.target = theTarget + '/' + theVariant; 
  }
  
  public AnyTransliterator(String id, UnicodeFilter filter, String target2, int targetScript2, Transliterator widthFix2, Map<Integer, Transliterator> cache2) {
    super(id, filter);
    this.targetScript = targetScript2;
    this.cache = cache2;
    this.target = target2;
  }
  
  private Transliterator getTransliterator(int source) {
    if (source == this.targetScript || source == -1) {
      if (isWide(this.targetScript))
        return null; 
      return this.widthFix;
    } 
    Integer key = Integer.valueOf(source);
    Transliterator t = this.cache.get(key);
    if (t == null) {
      String sourceName = UScript.getName(source);
      String id = sourceName + '-' + this.target;
      try {
        t = Transliterator.getInstance(id, 0);
      } catch (RuntimeException e) {}
      if (t == null) {
        id = sourceName + "-Latin;Latin-" + this.target;
        try {
          t = Transliterator.getInstance(id, 0);
        } catch (RuntimeException e) {}
      } 
      if (t != null) {
        if (!isWide(this.targetScript)) {
          List<Transliterator> v = new ArrayList<Transliterator>();
          v.add(this.widthFix);
          v.add(t);
          t = new CompoundTransliterator(v);
        } 
        this.cache.put(key, t);
      } else if (!isWide(this.targetScript)) {
        return this.widthFix;
      } 
    } 
    return t;
  }
  
  private boolean isWide(int script) {
    return (script == 5 || script == 17 || script == 18 || script == 20 || script == 22);
  }
  
  static void register() {
    HashMap<String, Set<String>> seen = new HashMap<String, Set<String>>();
    for (Enumeration<String> s = Transliterator.getAvailableSources(); s.hasMoreElements(); ) {
      String source = s.nextElement();
      if (source.equalsIgnoreCase("Any"))
        continue; 
      Enumeration<String> t = Transliterator.getAvailableTargets(source);
      while (t.hasMoreElements()) {
        String target = t.nextElement();
        int targetScript = scriptNameToCode(target);
        if (targetScript == -1)
          continue; 
        Set<String> seenVariants = seen.get(target);
        if (seenVariants == null)
          seen.put(target, seenVariants = new HashSet<String>()); 
        Enumeration<String> v = Transliterator.getAvailableVariants(source, target);
        while (v.hasMoreElements()) {
          String variant = v.nextElement();
          if (seenVariants.contains(variant))
            continue; 
          seenVariants.add(variant);
          String id = TransliteratorIDParser.STVtoID("Any", target, variant);
          AnyTransliterator trans = new AnyTransliterator(id, target, variant, targetScript);
          Transliterator.registerInstance(trans);
          Transliterator.registerSpecialInverse(target, "Null", false);
        } 
      } 
    } 
  }
  
  private static int scriptNameToCode(String name) {
    try {
      int[] codes = UScript.getCode(name);
      return (codes != null) ? codes[0] : -1;
    } catch (MissingResourceException e) {
      return -1;
    } 
  }
  
  private static class ScriptRunIterator {
    private Replaceable text;
    
    private int textStart;
    
    private int textLimit;
    
    public int scriptCode;
    
    public int start;
    
    public int limit;
    
    public ScriptRunIterator(Replaceable text, int start, int limit) {
      this.text = text;
      this.textStart = start;
      this.textLimit = limit;
      this.limit = start;
    }
    
    public boolean next() {
      this.scriptCode = -1;
      this.start = this.limit;
      if (this.start == this.textLimit)
        return false; 
      while (this.start > this.textStart) {
        int ch = this.text.char32At(this.start - 1);
        int s = UScript.getScript(ch);
        if (s == 0 || s == 1)
          this.start--; 
      } 
      while (this.limit < this.textLimit) {
        int ch = this.text.char32At(this.limit);
        int s = UScript.getScript(ch);
        if (s != 0 && s != 1)
          if (this.scriptCode == -1) {
            this.scriptCode = s;
          } else if (s != this.scriptCode) {
            break;
          }  
        this.limit++;
      } 
      return true;
    }
    
    public void adjustLimit(int delta) {
      this.limit += delta;
      this.textLimit += delta;
    }
  }
  
  public Transliterator safeClone() {
    UnicodeFilter filter = getFilter();
    if (filter != null && filter instanceof UnicodeSet)
      filter = new UnicodeSet((UnicodeSet)filter); 
    return new AnyTransliterator(getID(), filter, this.target, this.targetScript, this.widthFix, this.cache);
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
    sourceSet.addAll(myFilter);
    if (myFilter.size() != 0)
      targetSet.addAll(0, 1114111); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\AnyTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */