package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.LocaleUtility;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.util.CaseInsensitiveString;
import com.ibm.icu.util.UResourceBundle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

class TransliteratorRegistry {
  private static final char LOCALE_SEP = '_';
  
  private static final String NO_VARIANT = "";
  
  private static final String ANY = "Any";
  
  private Map<CaseInsensitiveString, Object[]> registry;
  
  private Map<CaseInsensitiveString, Map<CaseInsensitiveString, List<CaseInsensitiveString>>> specDAG;
  
  private List<CaseInsensitiveString> availableIDs;
  
  private static final boolean DEBUG = false;
  
  static class Spec {
    private String top;
    
    private String spec;
    
    private String nextSpec;
    
    private String scriptName;
    
    private boolean isSpecLocale;
    
    private boolean isNextLocale;
    
    private ICUResourceBundle res;
    
    public Spec(String theSpec) {
      this.top = theSpec;
      this.spec = null;
      this.scriptName = null;
      try {
        int script = UScript.getCodeFromName(this.top);
        int[] s = UScript.getCode(this.top);
        if (s != null) {
          this.scriptName = UScript.getName(s[0]);
          if (this.scriptName.equalsIgnoreCase(this.top))
            this.scriptName = null; 
        } 
        this.isSpecLocale = false;
        this.res = null;
        if (script == -1) {
          Locale toploc = LocaleUtility.getLocaleFromName(this.top);
          this.res = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/translit", toploc);
          if (this.res != null && LocaleUtility.isFallbackOf(this.res.getULocale().toString(), this.top))
            this.isSpecLocale = true; 
        } 
      } catch (MissingResourceException e) {
        this.scriptName = null;
      } 
      reset();
    }
    
    public boolean hasFallback() {
      return (this.nextSpec != null);
    }
    
    public void reset() {
      if (this.spec != this.top) {
        this.spec = this.top;
        this.isSpecLocale = (this.res != null);
        setupNext();
      } 
    }
    
    private void setupNext() {
      this.isNextLocale = false;
      if (this.isSpecLocale) {
        this.nextSpec = this.spec;
        int i = this.nextSpec.lastIndexOf('_');
        if (i > 0) {
          this.nextSpec = this.spec.substring(0, i);
          this.isNextLocale = true;
        } else {
          this.nextSpec = this.scriptName;
        } 
      } else if (this.nextSpec != this.scriptName) {
        this.nextSpec = this.scriptName;
      } else {
        this.nextSpec = null;
      } 
    }
    
    public String next() {
      this.spec = this.nextSpec;
      this.isSpecLocale = this.isNextLocale;
      setupNext();
      return this.spec;
    }
    
    public String get() {
      return this.spec;
    }
    
    public boolean isLocale() {
      return this.isSpecLocale;
    }
    
    public ResourceBundle getBundle() {
      if (this.res != null && this.res.getULocale().toString().equals(this.spec))
        return (ResourceBundle)this.res; 
      return null;
    }
    
    public String getTop() {
      return this.top;
    }
  }
  
  static class ResourceEntry {
    public String resource;
    
    public String encoding;
    
    public int direction;
    
    public ResourceEntry(String n, String enc, int d) {
      this.resource = n;
      this.encoding = enc;
      this.direction = d;
    }
  }
  
  static class LocaleEntry {
    public String rule;
    
    public int direction;
    
    public LocaleEntry(String r, int d) {
      this.rule = r;
      this.direction = d;
    }
  }
  
  static class AliasEntry {
    public String alias;
    
    public AliasEntry(String a) {
      this.alias = a;
    }
  }
  
  static class CompoundRBTEntry {
    private String ID;
    
    private List<String> idBlockVector;
    
    private List<RuleBasedTransliterator.Data> dataVector;
    
    private UnicodeSet compoundFilter;
    
    public CompoundRBTEntry(String theID, List<String> theIDBlockVector, List<RuleBasedTransliterator.Data> theDataVector, UnicodeSet theCompoundFilter) {
      this.ID = theID;
      this.idBlockVector = theIDBlockVector;
      this.dataVector = theDataVector;
      this.compoundFilter = theCompoundFilter;
    }
    
    public Transliterator getInstance() {
      List<Transliterator> transliterators = new ArrayList<Transliterator>();
      int passNumber = 1;
      int limit = Math.max(this.idBlockVector.size(), this.dataVector.size());
      for (int i = 0; i < limit; i++) {
        if (i < this.idBlockVector.size()) {
          String idBlock = this.idBlockVector.get(i);
          if (idBlock.length() > 0)
            transliterators.add(Transliterator.getInstance(idBlock)); 
        } 
        if (i < this.dataVector.size()) {
          RuleBasedTransliterator.Data data = this.dataVector.get(i);
          transliterators.add(new RuleBasedTransliterator("%Pass" + passNumber++, data, null));
        } 
      } 
      Transliterator t = new CompoundTransliterator(transliterators, passNumber - 1);
      t.setID(this.ID);
      if (this.compoundFilter != null)
        t.setFilter(this.compoundFilter); 
      return t;
    }
  }
  
  public TransliteratorRegistry() {
    this.registry = Collections.synchronizedMap(new HashMap<CaseInsensitiveString, Object>());
    this.specDAG = Collections.synchronizedMap(new HashMap<CaseInsensitiveString, Map<CaseInsensitiveString, List<CaseInsensitiveString>>>());
    this.availableIDs = new ArrayList<CaseInsensitiveString>();
  }
  
  public Transliterator get(String ID, StringBuffer aliasReturn) {
    Object[] entry = find(ID);
    return (entry == null) ? null : instantiateEntry(ID, entry, aliasReturn);
  }
  
  public void put(String ID, Class<? extends Transliterator> transliteratorSubclass, boolean visible) {
    registerEntry(ID, transliteratorSubclass, visible);
  }
  
  public void put(String ID, Transliterator.Factory factory, boolean visible) {
    registerEntry(ID, factory, visible);
  }
  
  public void put(String ID, String resourceName, String encoding, int dir, boolean visible) {
    registerEntry(ID, new ResourceEntry(resourceName, encoding, dir), visible);
  }
  
  public void put(String ID, String alias, boolean visible) {
    registerEntry(ID, new AliasEntry(alias), visible);
  }
  
  public void put(String ID, Transliterator trans, boolean visible) {
    registerEntry(ID, trans, visible);
  }
  
  public void remove(String ID) {
    String[] stv = TransliteratorIDParser.IDtoSTV(ID);
    String id = TransliteratorIDParser.STVtoID(stv[0], stv[1], stv[2]);
    this.registry.remove(new CaseInsensitiveString(id));
    removeSTV(stv[0], stv[1], stv[2]);
    this.availableIDs.remove(new CaseInsensitiveString(id));
  }
  
  private static class IDEnumeration implements Enumeration<String> {
    Enumeration<CaseInsensitiveString> en;
    
    public IDEnumeration(Enumeration<CaseInsensitiveString> e) {
      this.en = e;
    }
    
    public boolean hasMoreElements() {
      return (this.en != null && this.en.hasMoreElements());
    }
    
    public String nextElement() {
      return ((CaseInsensitiveString)this.en.nextElement()).getString();
    }
  }
  
  public Enumeration<String> getAvailableIDs() {
    return new IDEnumeration(Collections.enumeration(this.availableIDs));
  }
  
  public Enumeration<String> getAvailableSources() {
    return new IDEnumeration(Collections.enumeration(this.specDAG.keySet()));
  }
  
  public Enumeration<String> getAvailableTargets(String source) {
    CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
    Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
    if (targets == null)
      return new IDEnumeration(null); 
    return new IDEnumeration(Collections.enumeration(targets.keySet()));
  }
  
  public Enumeration<String> getAvailableVariants(String source, String target) {
    CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
    CaseInsensitiveString citrg = new CaseInsensitiveString(target);
    Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
    if (targets == null)
      return new IDEnumeration(null); 
    List<CaseInsensitiveString> variants = targets.get(citrg);
    if (variants == null)
      return new IDEnumeration(null); 
    return new IDEnumeration(Collections.enumeration(variants));
  }
  
  private void registerEntry(String source, String target, String variant, Object entry, boolean visible) {
    String s = source;
    if (s.length() == 0)
      s = "Any"; 
    String ID = TransliteratorIDParser.STVtoID(source, target, variant);
    registerEntry(ID, s, target, variant, entry, visible);
  }
  
  private void registerEntry(String ID, Object entry, boolean visible) {
    String[] stv = TransliteratorIDParser.IDtoSTV(ID);
    String id = TransliteratorIDParser.STVtoID(stv[0], stv[1], stv[2]);
    registerEntry(id, stv[0], stv[1], stv[2], entry, visible);
  }
  
  private void registerEntry(String ID, String source, String target, String variant, Object entry, boolean visible) {
    Object[] arrayOfObj;
    CaseInsensitiveString ciID = new CaseInsensitiveString(ID);
    if (entry instanceof Object[]) {
      arrayOfObj = (Object[])entry;
    } else {
      arrayOfObj = new Object[] { entry };
    } 
    this.registry.put(ciID, arrayOfObj);
    if (visible) {
      registerSTV(source, target, variant);
      if (!this.availableIDs.contains(ciID))
        this.availableIDs.add(ciID); 
    } else {
      removeSTV(source, target, variant);
      this.availableIDs.remove(ciID);
    } 
  }
  
  private void registerSTV(String source, String target, String variant) {
    CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
    CaseInsensitiveString citrg = new CaseInsensitiveString(target);
    CaseInsensitiveString civar = new CaseInsensitiveString(variant);
    Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
    if (targets == null) {
      targets = Collections.synchronizedMap(new HashMap<CaseInsensitiveString, List<CaseInsensitiveString>>());
      this.specDAG.put(cisrc, targets);
    } 
    List<CaseInsensitiveString> variants = targets.get(citrg);
    if (variants == null) {
      variants = new ArrayList<CaseInsensitiveString>();
      targets.put(citrg, variants);
    } 
    if (!variants.contains(civar))
      if (variant.length() > 0) {
        variants.add(civar);
      } else {
        variants.add(0, civar);
      }  
  }
  
  private void removeSTV(String source, String target, String variant) {
    CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
    CaseInsensitiveString citrg = new CaseInsensitiveString(target);
    CaseInsensitiveString civar = new CaseInsensitiveString(variant);
    Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = this.specDAG.get(cisrc);
    if (targets == null)
      return; 
    List<CaseInsensitiveString> variants = targets.get(citrg);
    if (variants == null)
      return; 
    variants.remove(civar);
    if (variants.size() == 0) {
      targets.remove(citrg);
      if (targets.size() == 0)
        this.specDAG.remove(cisrc); 
    } 
  }
  
  private Object[] findInDynamicStore(Spec src, Spec trg, String variant) {
    String ID = TransliteratorIDParser.STVtoID(src.get(), trg.get(), variant);
    return this.registry.get(new CaseInsensitiveString(ID));
  }
  
  private Object[] findInStaticStore(Spec src, Spec trg, String variant) {
    Object[] entry = null;
    if (src.isLocale()) {
      entry = findInBundle(src, trg, variant, 0);
    } else if (trg.isLocale()) {
      entry = findInBundle(trg, src, variant, 1);
    } 
    if (entry != null)
      registerEntry(src.getTop(), trg.getTop(), variant, entry, false); 
    return entry;
  }
  
  private Object[] findInBundle(Spec specToOpen, Spec specToFind, String variant, int direction) {
    ResourceBundle res = specToOpen.getBundle();
    if (res == null)
      return null; 
    for (int pass = 0; pass < 2; pass++) {
      StringBuilder tag = new StringBuilder();
      if (pass == 0) {
        tag.append((direction == 0) ? "TransliterateTo" : "TransliterateFrom");
      } else {
        tag.append("Transliterate");
      } 
      tag.append(specToFind.get().toUpperCase(Locale.ENGLISH));
      try {
        String[] subres = res.getStringArray(tag.toString());
        int i = 0;
        if (variant.length() != 0)
          for (i = 0; i < subres.length && 
            !subres[i].equalsIgnoreCase(variant); i += 2); 
        if (i < subres.length) {
          int dir = (pass == 0) ? 0 : direction;
          return new Object[] { new LocaleEntry(subres[i + 1], dir) };
        } 
      } catch (MissingResourceException e) {}
    } 
    return null;
  }
  
  private Object[] find(String ID) {
    String[] stv = TransliteratorIDParser.IDtoSTV(ID);
    return find(stv[0], stv[1], stv[2]);
  }
  
  private Object[] find(String source, String target, String variant) {
    Spec src = new Spec(source);
    Spec trg = new Spec(target);
    Object[] entry = null;
    if (variant.length() != 0) {
      entry = findInDynamicStore(src, trg, variant);
      if (entry != null)
        return entry; 
      entry = findInStaticStore(src, trg, variant);
      if (entry != null)
        return entry; 
    } 
    while (true) {
      src.reset();
      while (true) {
        entry = findInDynamicStore(src, trg, "");
        if (entry != null)
          return entry; 
        entry = findInStaticStore(src, trg, "");
        if (entry != null)
          return entry; 
        if (!src.hasFallback())
          break; 
        src.next();
      } 
      if (!trg.hasFallback())
        break; 
      trg.next();
    } 
    return null;
  }
  
  private Transliterator instantiateEntry(String ID, Object[] entryWrapper, StringBuffer aliasReturn) {
    while (true) {
      Object entry = entryWrapper[0];
      if (entry instanceof RuleBasedTransliterator.Data) {
        RuleBasedTransliterator.Data data = (RuleBasedTransliterator.Data)entry;
        return new RuleBasedTransliterator(ID, data, null);
      } 
      if (entry instanceof Class) {
        try {
          return ((Class<Transliterator>)entry).newInstance();
        } catch (InstantiationException e) {
        
        } catch (IllegalAccessException e2) {}
        return null;
      } 
      if (entry instanceof AliasEntry) {
        aliasReturn.append(((AliasEntry)entry).alias);
        return null;
      } 
      if (entry instanceof Transliterator.Factory)
        return ((Transliterator.Factory)entry).getInstance(ID); 
      if (entry instanceof CompoundRBTEntry)
        return ((CompoundRBTEntry)entry).getInstance(); 
      if (entry instanceof AnyTransliterator) {
        AnyTransliterator temp = (AnyTransliterator)entry;
        return temp.safeClone();
      } 
      if (entry instanceof RuleBasedTransliterator) {
        RuleBasedTransliterator temp = (RuleBasedTransliterator)entry;
        return temp.safeClone();
      } 
      if (entry instanceof CompoundTransliterator) {
        CompoundTransliterator temp = (CompoundTransliterator)entry;
        return temp.safeClone();
      } 
      if (entry instanceof Transliterator)
        return (Transliterator)entry; 
      TransliteratorParser parser = new TransliteratorParser();
      try {
        ResourceEntry re = (ResourceEntry)entry;
        parser.parse(re.resource, re.direction);
      } catch (ClassCastException e) {
        LocaleEntry le = (LocaleEntry)entry;
        parser.parse(le.rule, le.direction);
      } 
      if (parser.idBlockVector.size() == 0 && parser.dataVector.size() == 0) {
        entryWrapper[0] = new AliasEntry(NullTransliterator._ID);
        continue;
      } 
      if (parser.idBlockVector.size() == 0 && parser.dataVector.size() == 1) {
        entryWrapper[0] = parser.dataVector.get(0);
        continue;
      } 
      if (parser.idBlockVector.size() == 1 && parser.dataVector.size() == 0) {
        if (parser.compoundFilter != null) {
          entryWrapper[0] = new AliasEntry(parser.compoundFilter.toPattern(false) + ";" + (String)parser.idBlockVector.get(0));
          continue;
        } 
        entryWrapper[0] = new AliasEntry(parser.idBlockVector.get(0));
        continue;
      } 
      entryWrapper[0] = new CompoundRBTEntry(ID, parser.idBlockVector, parser.dataVector, parser.compoundFilter);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\TransliteratorRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */