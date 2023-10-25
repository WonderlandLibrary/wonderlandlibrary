package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

public class RuleBasedNumberFormat extends NumberFormat {
  static final long serialVersionUID = -7664252765575395068L;
  
  public static final int SPELLOUT = 1;
  
  public static final int ORDINAL = 2;
  
  public static final int DURATION = 3;
  
  public static final int NUMBERING_SYSTEM = 4;
  
  private transient NFRuleSet[] ruleSets = null;
  
  private transient String[] ruleSetDescriptions = null;
  
  private transient NFRuleSet defaultRuleSet = null;
  
  private ULocale locale = null;
  
  private transient RbnfLenientScannerProvider scannerProvider = null;
  
  private transient boolean lookedForScanner;
  
  private transient DecimalFormatSymbols decimalFormatSymbols = null;
  
  private transient DecimalFormat decimalFormat = null;
  
  private boolean lenientParse = false;
  
  private transient String lenientParseRules;
  
  private transient String postProcessRules;
  
  private transient RBNFPostProcessor postProcessor;
  
  private Map<String, String[]> ruleSetDisplayNames;
  
  private String[] publicRuleSetNames;
  
  private static final boolean DEBUG = ICUDebug.enabled("rbnf");
  
  public RuleBasedNumberFormat(String description) {
    this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
    init(description, (String[][])null);
  }
  
  public RuleBasedNumberFormat(String description, String[][] localizations) {
    this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
    init(description, localizations);
  }
  
  public RuleBasedNumberFormat(String description, Locale locale) {
    this(description, ULocale.forLocale(locale));
  }
  
  public RuleBasedNumberFormat(String description, ULocale locale) {
    this.locale = locale;
    init(description, (String[][])null);
  }
  
  public RuleBasedNumberFormat(String description, String[][] localizations, ULocale locale) {
    this.locale = locale;
    init(description, localizations);
  }
  
  public RuleBasedNumberFormat(Locale locale, int format) {
    this(ULocale.forLocale(locale), format);
  }
  
  public RuleBasedNumberFormat(ULocale locale, int format) {
    this.locale = locale;
    ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/rbnf", locale);
    ULocale uloc = bundle.getULocale();
    setLocale(uloc, uloc);
    String description = "";
    String[][] localizations = (String[][])null;
    try {
      description = bundle.getString(rulenames[format - 1]);
    } catch (MissingResourceException e) {
      try {
        ICUResourceBundle rules = bundle.getWithFallback("RBNFRules/" + rulenames[format - 1]);
        UResourceBundleIterator it = rules.getIterator();
        while (it.hasNext())
          description = description.concat(it.nextString()); 
      } catch (MissingResourceException e1) {}
    } 
    try {
      UResourceBundle locb = bundle.get(locnames[format - 1]);
      localizations = new String[locb.getSize()][];
      for (int i = 0; i < localizations.length; i++)
        localizations[i] = locb.get(i).getStringArray(); 
    } catch (MissingResourceException e) {}
    init(description, localizations);
  }
  
  private static final String[] rulenames = new String[] { "SpelloutRules", "OrdinalRules", "DurationRules", "NumberingSystemRules" };
  
  private static final String[] locnames = new String[] { "SpelloutLocalizations", "OrdinalLocalizations", "DurationLocalizations", "NumberingSystemLocalizations" };
  
  public RuleBasedNumberFormat(int format) {
    this(ULocale.getDefault(ULocale.Category.FORMAT), format);
  }
  
  public Object clone() {
    return super.clone();
  }
  
  public boolean equals(Object that) {
    if (!(that instanceof RuleBasedNumberFormat))
      return false; 
    RuleBasedNumberFormat that2 = (RuleBasedNumberFormat)that;
    if (!this.locale.equals(that2.locale) || this.lenientParse != that2.lenientParse)
      return false; 
    if (this.ruleSets.length != that2.ruleSets.length)
      return false; 
    for (int i = 0; i < this.ruleSets.length; i++) {
      if (!this.ruleSets[i].equals(that2.ruleSets[i]))
        return false; 
    } 
    return true;
  }
  
  public int hashCode() {
    return super.hashCode();
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < this.ruleSets.length; i++)
      result.append(this.ruleSets[i].toString()); 
    return result.toString();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeUTF(toString());
    out.writeObject(this.locale);
  }
  
  private void readObject(ObjectInputStream in) throws IOException {
    ULocale uLocale;
    String description = in.readUTF();
    try {
      uLocale = (ULocale)in.readObject();
    } catch (Exception e) {
      uLocale = ULocale.getDefault(ULocale.Category.FORMAT);
    } 
    RuleBasedNumberFormat temp = new RuleBasedNumberFormat(description, uLocale);
    this.ruleSets = temp.ruleSets;
    this.defaultRuleSet = temp.defaultRuleSet;
    this.publicRuleSetNames = temp.publicRuleSetNames;
    this.decimalFormatSymbols = temp.decimalFormatSymbols;
    this.decimalFormat = temp.decimalFormat;
    this.locale = temp.locale;
  }
  
  public String[] getRuleSetNames() {
    return (String[])this.publicRuleSetNames.clone();
  }
  
  public ULocale[] getRuleSetDisplayNameLocales() {
    if (this.ruleSetDisplayNames != null) {
      Set<String> s = this.ruleSetDisplayNames.keySet();
      String[] locales = s.<String>toArray(new String[s.size()]);
      Arrays.sort(locales, String.CASE_INSENSITIVE_ORDER);
      ULocale[] result = new ULocale[locales.length];
      for (int i = 0; i < locales.length; i++)
        result[i] = new ULocale(locales[i]); 
      return result;
    } 
    return null;
  }
  
  private String[] getNameListForLocale(ULocale loc) {
    if (loc != null && this.ruleSetDisplayNames != null) {
      String[] localeNames = { loc.getBaseName(), ULocale.getDefault(ULocale.Category.DISPLAY).getBaseName() };
      for (int i = 0; i < localeNames.length; i++) {
        String lname = localeNames[i];
        while (lname.length() > 0) {
          String[] names = this.ruleSetDisplayNames.get(lname);
          if (names != null)
            return names; 
          lname = ULocale.getFallback(lname);
        } 
      } 
    } 
    return null;
  }
  
  public String[] getRuleSetDisplayNames(ULocale loc) {
    String[] names = getNameListForLocale(loc);
    if (names != null)
      return (String[])names.clone(); 
    names = getRuleSetNames();
    for (int i = 0; i < names.length; i++)
      names[i] = names[i].substring(1); 
    return names;
  }
  
  public String[] getRuleSetDisplayNames() {
    return getRuleSetDisplayNames(ULocale.getDefault(ULocale.Category.DISPLAY));
  }
  
  public String getRuleSetDisplayName(String ruleSetName, ULocale loc) {
    String[] rsnames = this.publicRuleSetNames;
    for (int ix = 0; ix < rsnames.length; ix++) {
      if (rsnames[ix].equals(ruleSetName)) {
        String[] names = getNameListForLocale(loc);
        if (names != null)
          return names[ix]; 
        return rsnames[ix].substring(1);
      } 
    } 
    throw new IllegalArgumentException("unrecognized rule set name: " + ruleSetName);
  }
  
  public String getRuleSetDisplayName(String ruleSetName) {
    return getRuleSetDisplayName(ruleSetName, ULocale.getDefault(ULocale.Category.DISPLAY));
  }
  
  public String format(double number, String ruleSet) throws IllegalArgumentException {
    if (ruleSet.startsWith("%%"))
      throw new IllegalArgumentException("Can't use internal rule set"); 
    return format(number, findRuleSet(ruleSet));
  }
  
  public String format(long number, String ruleSet) throws IllegalArgumentException {
    if (ruleSet.startsWith("%%"))
      throw new IllegalArgumentException("Can't use internal rule set"); 
    return format(number, findRuleSet(ruleSet));
  }
  
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition ignore) {
    toAppendTo.append(format(number, this.defaultRuleSet));
    return toAppendTo;
  }
  
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition ignore) {
    toAppendTo.append(format(number, this.defaultRuleSet));
    return toAppendTo;
  }
  
  public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos) {
    return format(new BigDecimal(number), toAppendTo, pos);
  }
  
  public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
    return format(new BigDecimal(number), toAppendTo, pos);
  }
  
  public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
    return format(number.doubleValue(), toAppendTo, pos);
  }
  
  public Number parse(String text, ParsePosition parsePosition) {
    String workingText = text.substring(parsePosition.getIndex());
    ParsePosition workingPos = new ParsePosition(0);
    Number tempResult = null;
    Number result = Long.valueOf(0L);
    ParsePosition highWaterMark = new ParsePosition(workingPos.getIndex());
    for (int i = this.ruleSets.length - 1; i >= 0; i--) {
      if (this.ruleSets[i].isPublic() && this.ruleSets[i].isParseable()) {
        tempResult = this.ruleSets[i].parse(workingText, workingPos, Double.MAX_VALUE);
        if (workingPos.getIndex() > highWaterMark.getIndex()) {
          result = tempResult;
          highWaterMark.setIndex(workingPos.getIndex());
        } 
        if (highWaterMark.getIndex() == workingText.length())
          break; 
        workingPos.setIndex(0);
      } 
    } 
    parsePosition.setIndex(parsePosition.getIndex() + highWaterMark.getIndex());
    return result;
  }
  
  public void setLenientParseMode(boolean enabled) {
    this.lenientParse = enabled;
  }
  
  public boolean lenientParseEnabled() {
    return this.lenientParse;
  }
  
  public void setLenientScannerProvider(RbnfLenientScannerProvider scannerProvider) {
    this.scannerProvider = scannerProvider;
  }
  
  public RbnfLenientScannerProvider getLenientScannerProvider() {
    if (this.scannerProvider == null && this.lenientParse && !this.lookedForScanner)
      try {
        this.lookedForScanner = true;
        Class<?> cls = Class.forName("com.ibm.icu.text.RbnfScannerProviderImpl");
        RbnfLenientScannerProvider provider = (RbnfLenientScannerProvider)cls.newInstance();
        setLenientScannerProvider(provider);
      } catch (Exception e) {} 
    return this.scannerProvider;
  }
  
  public void setDefaultRuleSet(String ruleSetName) {
    if (ruleSetName == null) {
      if (this.publicRuleSetNames.length > 0) {
        this.defaultRuleSet = findRuleSet(this.publicRuleSetNames[0]);
      } else {
        this.defaultRuleSet = null;
        int n = this.ruleSets.length;
        while (--n >= 0) {
          String currentName = this.ruleSets[n].getName();
          if (currentName.equals("%spellout-numbering") || currentName.equals("%digits-ordinal") || currentName.equals("%duration")) {
            this.defaultRuleSet = this.ruleSets[n];
            return;
          } 
        } 
        n = this.ruleSets.length;
        while (--n >= 0) {
          if (this.ruleSets[n].isPublic()) {
            this.defaultRuleSet = this.ruleSets[n];
            break;
          } 
        } 
      } 
    } else {
      if (ruleSetName.startsWith("%%"))
        throw new IllegalArgumentException("cannot use private rule set: " + ruleSetName); 
      this.defaultRuleSet = findRuleSet(ruleSetName);
    } 
  }
  
  public String getDefaultRuleSetName() {
    if (this.defaultRuleSet != null && this.defaultRuleSet.isPublic())
      return this.defaultRuleSet.getName(); 
    return "";
  }
  
  public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) {
    if (newSymbols != null) {
      this.decimalFormatSymbols = (DecimalFormatSymbols)newSymbols.clone();
      if (this.decimalFormat != null)
        this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols); 
      for (int i = 0; i < this.ruleSets.length; i++)
        this.ruleSets[i].parseRules(this.ruleSetDescriptions[i], this); 
    } 
  }
  
  NFRuleSet getDefaultRuleSet() {
    return this.defaultRuleSet;
  }
  
  RbnfLenientScanner getLenientScanner() {
    if (this.lenientParse) {
      RbnfLenientScannerProvider provider = getLenientScannerProvider();
      if (provider != null)
        return provider.get(this.locale, this.lenientParseRules); 
    } 
    return null;
  }
  
  DecimalFormatSymbols getDecimalFormatSymbols() {
    if (this.decimalFormatSymbols == null)
      this.decimalFormatSymbols = new DecimalFormatSymbols(this.locale); 
    return this.decimalFormatSymbols;
  }
  
  DecimalFormat getDecimalFormat() {
    if (this.decimalFormat == null) {
      this.decimalFormat = (DecimalFormat)NumberFormat.getInstance(this.locale);
      if (this.decimalFormatSymbols != null)
        this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols); 
    } 
    return this.decimalFormat;
  }
  
  private String extractSpecial(StringBuilder description, String specialName) {
    String result = null;
    int lp = description.indexOf(specialName);
    if (lp != -1)
      if (lp == 0 || description.charAt(lp - 1) == ';') {
        int lpEnd = description.indexOf(";%", lp);
        if (lpEnd == -1)
          lpEnd = description.length() - 1; 
        int lpStart = lp + specialName.length();
        while (lpStart < lpEnd && PatternProps.isWhiteSpace(description.charAt(lpStart)))
          lpStart++; 
        result = description.substring(lpStart, lpEnd);
        description.delete(lp, lpEnd + 1);
      }  
    return result;
  }
  
  private void init(String description, String[][] localizations) {
    initLocalizations(localizations);
    StringBuilder descBuf = stripWhitespace(description);
    this.lenientParseRules = extractSpecial(descBuf, "%%lenient-parse:");
    this.postProcessRules = extractSpecial(descBuf, "%%post-process:");
    int numRuleSets = 0;
    int p;
    for (p = descBuf.indexOf(";%"); p != -1; p = descBuf.indexOf(";%", p)) {
      numRuleSets++;
      p++;
    } 
    numRuleSets++;
    this.ruleSets = new NFRuleSet[numRuleSets];
    this.ruleSetDescriptions = new String[numRuleSets];
    int curRuleSet = 0;
    int start = 0;
    int j;
    for (j = descBuf.indexOf(";%"); j != -1; j = descBuf.indexOf(";%", start)) {
      this.ruleSetDescriptions[curRuleSet] = descBuf.substring(start, j + 1);
      this.ruleSets[curRuleSet] = new NFRuleSet(this.ruleSetDescriptions, curRuleSet);
      curRuleSet++;
      start = j + 1;
    } 
    this.ruleSetDescriptions[curRuleSet] = descBuf.substring(start);
    this.ruleSets[curRuleSet] = new NFRuleSet(this.ruleSetDescriptions, curRuleSet);
    boolean defaultNameFound = false;
    int n = this.ruleSets.length;
    this.defaultRuleSet = this.ruleSets[this.ruleSets.length - 1];
    while (--n >= 0) {
      String currentName = this.ruleSets[n].getName();
      if (currentName.equals("%spellout-numbering") || currentName.equals("%digits-ordinal") || currentName.equals("%duration")) {
        this.defaultRuleSet = this.ruleSets[n];
        defaultNameFound = true;
        break;
      } 
    } 
    if (!defaultNameFound)
      for (int i1 = this.ruleSets.length - 1; i1 >= 0; i1--) {
        if (!this.ruleSets[i1].getName().startsWith("%%")) {
          this.defaultRuleSet = this.ruleSets[i1];
          break;
        } 
      }  
    for (int i = 0; i < this.ruleSets.length; i++)
      this.ruleSets[i].parseRules(this.ruleSetDescriptions[i], this); 
    int publicRuleSetCount = 0;
    for (int k = 0; k < this.ruleSets.length; k++) {
      if (!this.ruleSets[k].getName().startsWith("%%"))
        publicRuleSetCount++; 
    } 
    String[] publicRuleSetTemp = new String[publicRuleSetCount];
    publicRuleSetCount = 0;
    int m;
    for (m = this.ruleSets.length - 1; m >= 0; m--) {
      if (!this.ruleSets[m].getName().startsWith("%%"))
        publicRuleSetTemp[publicRuleSetCount++] = this.ruleSets[m].getName(); 
    } 
    if (this.publicRuleSetNames != null) {
      for (m = 0; m < this.publicRuleSetNames.length; m++) {
        String name = this.publicRuleSetNames[m];
        int i1 = 0;
        while (true) {
          if (i1 < publicRuleSetTemp.length) {
            if (name.equals(publicRuleSetTemp[i1]))
              break; 
            i1++;
            continue;
          } 
          throw new IllegalArgumentException("did not find public rule set: " + name);
        } 
      } 
      this.defaultRuleSet = findRuleSet(this.publicRuleSetNames[0]);
    } else {
      this.publicRuleSetNames = publicRuleSetTemp;
    } 
  }
  
  private void initLocalizations(String[][] localizations) {
    if (localizations != null) {
      this.publicRuleSetNames = (String[])localizations[0].clone();
      Map<String, String[]> m = (Map)new HashMap<String, String>();
      for (int i = 1; i < localizations.length; i++) {
        String[] data = localizations[i];
        String loc = data[0];
        String[] names = new String[data.length - 1];
        if (names.length != this.publicRuleSetNames.length)
          throw new IllegalArgumentException("public name length: " + this.publicRuleSetNames.length + " != localized names[" + i + "] length: " + names.length); 
        System.arraycopy(data, 1, names, 0, names.length);
        m.put(loc, names);
      } 
      if (!m.isEmpty())
        this.ruleSetDisplayNames = m; 
    } 
  }
  
  private StringBuilder stripWhitespace(String description) {
    StringBuilder result = new StringBuilder();
    int start = 0;
    while (start != -1 && start < description.length()) {
      while (start < description.length() && PatternProps.isWhiteSpace(description.charAt(start)))
        start++; 
      if (start < description.length() && description.charAt(start) == ';') {
        start++;
        continue;
      } 
      int p = description.indexOf(';', start);
      if (p == -1) {
        result.append(description.substring(start));
        start = -1;
        continue;
      } 
      if (p < description.length()) {
        result.append(description.substring(start, p + 1));
        start = p + 1;
        continue;
      } 
      start = -1;
    } 
    return result;
  }
  
  private String format(double number, NFRuleSet ruleSet) {
    StringBuffer result = new StringBuffer();
    ruleSet.format(number, result, 0);
    postProcess(result, ruleSet);
    return result.toString();
  }
  
  private String format(long number, NFRuleSet ruleSet) {
    StringBuffer result = new StringBuffer();
    ruleSet.format(number, result, 0);
    postProcess(result, ruleSet);
    return result.toString();
  }
  
  private void postProcess(StringBuffer result, NFRuleSet ruleSet) {
    if (this.postProcessRules != null) {
      if (this.postProcessor == null) {
        int ix = this.postProcessRules.indexOf(";");
        if (ix == -1)
          ix = this.postProcessRules.length(); 
        String ppClassName = this.postProcessRules.substring(0, ix).trim();
        try {
          Class<?> cls = Class.forName(ppClassName);
          this.postProcessor = (RBNFPostProcessor)cls.newInstance();
          this.postProcessor.init(this, this.postProcessRules);
        } catch (Exception e) {
          if (DEBUG)
            System.out.println("could not locate " + ppClassName + ", error " + e.getClass().getName() + ", " + e.getMessage()); 
          this.postProcessor = null;
          this.postProcessRules = null;
          return;
        } 
      } 
      this.postProcessor.process(result, ruleSet);
    } 
  }
  
  NFRuleSet findRuleSet(String name) throws IllegalArgumentException {
    for (int i = 0; i < this.ruleSets.length; i++) {
      if (this.ruleSets[i].getName().equals(name))
        return this.ruleSets[i]; 
    } 
    throw new IllegalArgumentException("No rule set named " + name);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RuleBasedNumberFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */