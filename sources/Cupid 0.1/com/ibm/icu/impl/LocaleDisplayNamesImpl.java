package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.text.LocaleDisplayNames;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public class LocaleDisplayNamesImpl extends LocaleDisplayNames {
  private final ULocale locale;
  
  private final LocaleDisplayNames.DialectHandling dialectHandling;
  
  private final DisplayContext capitalization;
  
  private final DataTable langData;
  
  private final DataTable regionData;
  
  private final Appender appender;
  
  private final MessageFormat format;
  
  private final MessageFormat keyTypeFormat;
  
  private static final Cache cache = new Cache();
  
  private enum CapitalizationContextUsage {
    LANGUAGE, SCRIPT, TERRITORY, VARIANT, KEY, TYPE;
  }
  
  private Map<CapitalizationContextUsage, boolean[]> capitalizationUsage = null;
  
  private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>();
  
  static {
    contextUsageTypeMap.put("languages", CapitalizationContextUsage.LANGUAGE);
    contextUsageTypeMap.put("script", CapitalizationContextUsage.SCRIPT);
    contextUsageTypeMap.put("territory", CapitalizationContextUsage.TERRITORY);
    contextUsageTypeMap.put("variant", CapitalizationContextUsage.VARIANT);
    contextUsageTypeMap.put("key", CapitalizationContextUsage.KEY);
    contextUsageTypeMap.put("type", CapitalizationContextUsage.TYPE);
  }
  
  public static LocaleDisplayNames getInstance(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) {
    synchronized (cache) {
      return cache.get(locale, dialectHandling);
    } 
  }
  
  public static LocaleDisplayNames getInstance(ULocale locale, DisplayContext... contexts) {
    synchronized (cache) {
      return cache.get(locale, contexts);
    } 
  }
  
  public LocaleDisplayNamesImpl(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) {
    this(locale, new DisplayContext[] { (dialectHandling == LocaleDisplayNames.DialectHandling.STANDARD_NAMES) ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES, DisplayContext.CAPITALIZATION_NONE });
  }
  
  public LocaleDisplayNamesImpl(ULocale locale, DisplayContext... contexts) {
    LocaleDisplayNames.DialectHandling dialectHandling = LocaleDisplayNames.DialectHandling.STANDARD_NAMES;
    DisplayContext capitalization = DisplayContext.CAPITALIZATION_NONE;
    for (DisplayContext contextItem : contexts) {
      switch (contextItem.type()) {
        case LANG:
          dialectHandling = (contextItem.value() == DisplayContext.STANDARD_NAMES.value()) ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
          break;
        case REGION:
          capitalization = contextItem;
          break;
      } 
    } 
    this.dialectHandling = dialectHandling;
    this.capitalization = capitalization;
    this.langData = LangDataTables.impl.get(locale);
    this.regionData = RegionDataTables.impl.get(locale);
    this.locale = ULocale.ROOT.equals(this.langData.getLocale()) ? this.regionData.getLocale() : this.langData.getLocale();
    String sep = this.langData.get("localeDisplayPattern", "separator");
    if ("separator".equals(sep))
      sep = ", "; 
    this.appender = new Appender(sep);
    String pattern = this.langData.get("localeDisplayPattern", "pattern");
    if ("pattern".equals(pattern))
      pattern = "{0} ({1})"; 
    this.format = new MessageFormat(pattern);
    String keyTypePattern = this.langData.get("localeDisplayPattern", "keyTypePattern");
    if ("keyTypePattern".equals(keyTypePattern))
      keyTypePattern = "{0}={1}"; 
    this.keyTypeFormat = new MessageFormat(keyTypePattern);
    if (capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU || capitalization == DisplayContext.CAPITALIZATION_FOR_STANDALONE) {
      this.capitalizationUsage = (Map)new HashMap<CapitalizationContextUsage, boolean>();
      boolean[] noTransforms = new boolean[2];
      noTransforms[0] = false;
      noTransforms[1] = false;
      CapitalizationContextUsage[] allUsages = CapitalizationContextUsage.values();
      for (CapitalizationContextUsage usage : allUsages)
        this.capitalizationUsage.put(usage, noTransforms); 
      ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
      UResourceBundle contextTransformsBundle = null;
      try {
        contextTransformsBundle = rb.getWithFallback("contextTransforms");
      } catch (MissingResourceException e) {
        contextTransformsBundle = null;
      } 
      if (contextTransformsBundle != null) {
        UResourceBundleIterator ctIterator = contextTransformsBundle.getIterator();
        while (ctIterator.hasNext()) {
          UResourceBundle contextTransformUsage = ctIterator.next();
          int[] intVector = contextTransformUsage.getIntVector();
          if (intVector.length >= 2) {
            String usageKey = contextTransformUsage.getKey();
            CapitalizationContextUsage usage = contextUsageTypeMap.get(usageKey);
            if (usage != null) {
              boolean[] transforms = new boolean[2];
              transforms[0] = (intVector[0] != 0);
              transforms[1] = (intVector[1] != 0);
              this.capitalizationUsage.put(usage, transforms);
            } 
          } 
        } 
      } 
    } 
  }
  
  public ULocale getLocale() {
    return this.locale;
  }
  
  public LocaleDisplayNames.DialectHandling getDialectHandling() {
    return this.dialectHandling;
  }
  
  public DisplayContext getContext(DisplayContext.Type type) {
    switch (type) {
      case LANG:
        result = (this.dialectHandling == LocaleDisplayNames.DialectHandling.STANDARD_NAMES) ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES;
        return result;
      case REGION:
        result = this.capitalization;
        return result;
    } 
    DisplayContext result = DisplayContext.STANDARD_NAMES;
    return result;
  }
  
  private String adjustForUsageAndContext(CapitalizationContextUsage usage, String name) {
    String result = name;
    boolean titlecase = false;
    switch (this.capitalization) {
      case LANG:
        titlecase = true;
        break;
      case REGION:
      case null:
        if (this.capitalizationUsage != null) {
          boolean[] transforms = this.capitalizationUsage.get(usage);
          titlecase = (this.capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU) ? transforms[0] : transforms[1];
        } 
        break;
    } 
    if (titlecase) {
      int stopPosLimit = 8, len = name.length();
      if (stopPosLimit > len)
        stopPosLimit = len; 
      int stopPos;
      for (stopPos = 0; stopPos < stopPosLimit; stopPos++) {
        int ch = name.codePointAt(stopPos);
        if (ch < 65 || (ch > 90 && ch < 97) || (ch > 122 && ch < 192))
          break; 
        if (ch >= 65536)
          stopPos++; 
      } 
      if (stopPos > 0 && stopPos < len) {
        String firstWord = name.substring(0, stopPos);
        String firstWordTitleCase = UCharacter.toTitleCase(this.locale, firstWord, null, 768);
        result = firstWordTitleCase.concat(name.substring(stopPos));
      } else {
        result = UCharacter.toTitleCase(this.locale, name, null, 768);
      } 
    } 
    return result;
  }
  
  public String localeDisplayName(ULocale locale) {
    return localeDisplayNameInternal(locale);
  }
  
  public String localeDisplayName(Locale locale) {
    return localeDisplayNameInternal(ULocale.forLocale(locale));
  }
  
  public String localeDisplayName(String localeId) {
    return localeDisplayNameInternal(new ULocale(localeId));
  }
  
  private String localeDisplayNameInternal(ULocale locale) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_1
    //   3: invokevirtual getLanguage : ()Ljava/lang/String;
    //   6: astore_3
    //   7: aload_1
    //   8: invokevirtual getBaseName : ()Ljava/lang/String;
    //   11: invokevirtual length : ()I
    //   14: ifne -> 20
    //   17: ldc 'root'
    //   19: astore_3
    //   20: aload_1
    //   21: invokevirtual getScript : ()Ljava/lang/String;
    //   24: astore #4
    //   26: aload_1
    //   27: invokevirtual getCountry : ()Ljava/lang/String;
    //   30: astore #5
    //   32: aload_1
    //   33: invokevirtual getVariant : ()Ljava/lang/String;
    //   36: astore #6
    //   38: aload #4
    //   40: invokevirtual length : ()I
    //   43: ifle -> 50
    //   46: iconst_1
    //   47: goto -> 51
    //   50: iconst_0
    //   51: istore #7
    //   53: aload #5
    //   55: invokevirtual length : ()I
    //   58: ifle -> 65
    //   61: iconst_1
    //   62: goto -> 66
    //   65: iconst_0
    //   66: istore #8
    //   68: aload #6
    //   70: invokevirtual length : ()I
    //   73: ifle -> 80
    //   76: iconst_1
    //   77: goto -> 81
    //   80: iconst_0
    //   81: istore #9
    //   83: aload_0
    //   84: getfield dialectHandling : Lcom/ibm/icu/text/LocaleDisplayNames$DialectHandling;
    //   87: getstatic com/ibm/icu/text/LocaleDisplayNames$DialectHandling.DIALECT_NAMES : Lcom/ibm/icu/text/LocaleDisplayNames$DialectHandling;
    //   90: if_acmpne -> 285
    //   93: iload #7
    //   95: ifeq -> 169
    //   98: iload #8
    //   100: ifeq -> 169
    //   103: new java/lang/StringBuilder
    //   106: dup
    //   107: invokespecial <init> : ()V
    //   110: aload_3
    //   111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: bipush #95
    //   116: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   119: aload #4
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: bipush #95
    //   126: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   129: aload #5
    //   131: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   134: invokevirtual toString : ()Ljava/lang/String;
    //   137: astore #10
    //   139: aload_0
    //   140: aload #10
    //   142: invokespecial localeIdName : (Ljava/lang/String;)Ljava/lang/String;
    //   145: astore #11
    //   147: aload #11
    //   149: aload #10
    //   151: invokevirtual equals : (Ljava/lang/Object;)Z
    //   154: ifne -> 169
    //   157: aload #11
    //   159: astore_2
    //   160: iconst_0
    //   161: istore #7
    //   163: iconst_0
    //   164: istore #8
    //   166: goto -> 285
    //   169: iload #7
    //   171: ifeq -> 227
    //   174: new java/lang/StringBuilder
    //   177: dup
    //   178: invokespecial <init> : ()V
    //   181: aload_3
    //   182: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: bipush #95
    //   187: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   190: aload #4
    //   192: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: invokevirtual toString : ()Ljava/lang/String;
    //   198: astore #10
    //   200: aload_0
    //   201: aload #10
    //   203: invokespecial localeIdName : (Ljava/lang/String;)Ljava/lang/String;
    //   206: astore #11
    //   208: aload #11
    //   210: aload #10
    //   212: invokevirtual equals : (Ljava/lang/Object;)Z
    //   215: ifne -> 227
    //   218: aload #11
    //   220: astore_2
    //   221: iconst_0
    //   222: istore #7
    //   224: goto -> 285
    //   227: iload #8
    //   229: ifeq -> 285
    //   232: new java/lang/StringBuilder
    //   235: dup
    //   236: invokespecial <init> : ()V
    //   239: aload_3
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: bipush #95
    //   245: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   248: aload #5
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: invokevirtual toString : ()Ljava/lang/String;
    //   256: astore #10
    //   258: aload_0
    //   259: aload #10
    //   261: invokespecial localeIdName : (Ljava/lang/String;)Ljava/lang/String;
    //   264: astore #11
    //   266: aload #11
    //   268: aload #10
    //   270: invokevirtual equals : (Ljava/lang/Object;)Z
    //   273: ifne -> 285
    //   276: aload #11
    //   278: astore_2
    //   279: iconst_0
    //   280: istore #8
    //   282: goto -> 285
    //   285: aload_2
    //   286: ifnonnull -> 295
    //   289: aload_0
    //   290: aload_3
    //   291: invokespecial localeIdName : (Ljava/lang/String;)Ljava/lang/String;
    //   294: astore_2
    //   295: new java/lang/StringBuilder
    //   298: dup
    //   299: invokespecial <init> : ()V
    //   302: astore #10
    //   304: iload #7
    //   306: ifeq -> 321
    //   309: aload #10
    //   311: aload_0
    //   312: aload #4
    //   314: invokevirtual scriptDisplayNameInContext : (Ljava/lang/String;)Ljava/lang/String;
    //   317: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   320: pop
    //   321: iload #8
    //   323: ifeq -> 342
    //   326: aload_0
    //   327: getfield appender : Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$Appender;
    //   330: aload_0
    //   331: aload #5
    //   333: invokevirtual regionDisplayName : (Ljava/lang/String;)Ljava/lang/String;
    //   336: aload #10
    //   338: invokevirtual append : (Ljava/lang/String;Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
    //   341: pop
    //   342: iload #9
    //   344: ifeq -> 363
    //   347: aload_0
    //   348: getfield appender : Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$Appender;
    //   351: aload_0
    //   352: aload #6
    //   354: invokevirtual variantDisplayName : (Ljava/lang/String;)Ljava/lang/String;
    //   357: aload #10
    //   359: invokevirtual append : (Ljava/lang/String;Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
    //   362: pop
    //   363: aload_1
    //   364: invokevirtual getKeywords : ()Ljava/util/Iterator;
    //   367: astore #11
    //   369: aload #11
    //   371: ifnull -> 520
    //   374: aload #11
    //   376: invokeinterface hasNext : ()Z
    //   381: ifeq -> 520
    //   384: aload #11
    //   386: invokeinterface next : ()Ljava/lang/Object;
    //   391: checkcast java/lang/String
    //   394: astore #12
    //   396: aload_1
    //   397: aload #12
    //   399: invokevirtual getKeywordValue : (Ljava/lang/String;)Ljava/lang/String;
    //   402: astore #13
    //   404: aload_0
    //   405: aload #12
    //   407: invokevirtual keyDisplayName : (Ljava/lang/String;)Ljava/lang/String;
    //   410: astore #14
    //   412: aload_0
    //   413: aload #12
    //   415: aload #13
    //   417: invokevirtual keyValueDisplayName : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   420: astore #15
    //   422: aload #15
    //   424: aload #13
    //   426: invokevirtual equals : (Ljava/lang/Object;)Z
    //   429: ifne -> 447
    //   432: aload_0
    //   433: getfield appender : Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$Appender;
    //   436: aload #15
    //   438: aload #10
    //   440: invokevirtual append : (Ljava/lang/String;Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
    //   443: pop
    //   444: goto -> 517
    //   447: aload #12
    //   449: aload #14
    //   451: invokevirtual equals : (Ljava/lang/Object;)Z
    //   454: ifne -> 495
    //   457: aload_0
    //   458: getfield keyTypeFormat : Lcom/ibm/icu/text/MessageFormat;
    //   461: iconst_2
    //   462: anewarray java/lang/String
    //   465: dup
    //   466: iconst_0
    //   467: aload #14
    //   469: aastore
    //   470: dup
    //   471: iconst_1
    //   472: aload #15
    //   474: aastore
    //   475: invokevirtual format : (Ljava/lang/Object;)Ljava/lang/String;
    //   478: astore #16
    //   480: aload_0
    //   481: getfield appender : Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$Appender;
    //   484: aload #16
    //   486: aload #10
    //   488: invokevirtual append : (Ljava/lang/String;Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
    //   491: pop
    //   492: goto -> 517
    //   495: aload_0
    //   496: getfield appender : Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$Appender;
    //   499: aload #14
    //   501: aload #10
    //   503: invokevirtual append : (Ljava/lang/String;Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
    //   506: ldc '='
    //   508: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   511: aload #15
    //   513: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   516: pop
    //   517: goto -> 374
    //   520: aconst_null
    //   521: astore #12
    //   523: aload #10
    //   525: invokevirtual length : ()I
    //   528: ifle -> 538
    //   531: aload #10
    //   533: invokevirtual toString : ()Ljava/lang/String;
    //   536: astore #12
    //   538: aload #12
    //   540: ifnull -> 564
    //   543: aload_0
    //   544: getfield format : Lcom/ibm/icu/text/MessageFormat;
    //   547: iconst_2
    //   548: anewarray java/lang/Object
    //   551: dup
    //   552: iconst_0
    //   553: aload_2
    //   554: aastore
    //   555: dup
    //   556: iconst_1
    //   557: aload #12
    //   559: aastore
    //   560: invokevirtual format : (Ljava/lang/Object;)Ljava/lang/String;
    //   563: astore_2
    //   564: aload_0
    //   565: getstatic com/ibm/icu/impl/LocaleDisplayNamesImpl$CapitalizationContextUsage.LANGUAGE : Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$CapitalizationContextUsage;
    //   568: aload_2
    //   569: invokespecial adjustForUsageAndContext : (Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$CapitalizationContextUsage;Ljava/lang/String;)Ljava/lang/String;
    //   572: areturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #272	-> 0
    //   #274	-> 2
    //   #279	-> 7
    //   #280	-> 17
    //   #282	-> 20
    //   #283	-> 26
    //   #284	-> 32
    //   #286	-> 38
    //   #287	-> 53
    //   #288	-> 68
    //   #291	-> 83
    //   #293	-> 93
    //   #294	-> 103
    //   #295	-> 139
    //   #296	-> 147
    //   #297	-> 157
    //   #298	-> 160
    //   #299	-> 163
    //   #300	-> 166
    //   #303	-> 169
    //   #304	-> 174
    //   #305	-> 200
    //   #306	-> 208
    //   #307	-> 218
    //   #308	-> 221
    //   #309	-> 224
    //   #312	-> 227
    //   #313	-> 232
    //   #314	-> 258
    //   #315	-> 266
    //   #316	-> 276
    //   #317	-> 279
    //   #318	-> 282
    //   #324	-> 285
    //   #325	-> 289
    //   #328	-> 295
    //   #329	-> 304
    //   #331	-> 309
    //   #333	-> 321
    //   #334	-> 326
    //   #336	-> 342
    //   #337	-> 347
    //   #340	-> 363
    //   #341	-> 369
    //   #342	-> 374
    //   #343	-> 384
    //   #344	-> 396
    //   #345	-> 404
    //   #346	-> 412
    //   #347	-> 422
    //   #348	-> 432
    //   #349	-> 447
    //   #350	-> 457
    //   #352	-> 480
    //   #353	-> 492
    //   #354	-> 495
    //   #358	-> 517
    //   #361	-> 520
    //   #362	-> 523
    //   #363	-> 531
    //   #366	-> 538
    //   #367	-> 543
    //   #370	-> 564
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   139	30	10	langScriptCountry	Ljava/lang/String;
    //   147	22	11	result	Ljava/lang/String;
    //   200	27	10	langScript	Ljava/lang/String;
    //   208	19	11	result	Ljava/lang/String;
    //   258	27	10	langCountry	Ljava/lang/String;
    //   266	19	11	result	Ljava/lang/String;
    //   480	12	16	keyValue	Ljava/lang/String;
    //   396	121	12	key	Ljava/lang/String;
    //   404	113	13	value	Ljava/lang/String;
    //   412	105	14	keyDisplayName	Ljava/lang/String;
    //   422	95	15	valueDisplayName	Ljava/lang/String;
    //   0	573	0	this	Lcom/ibm/icu/impl/LocaleDisplayNamesImpl;
    //   0	573	1	locale	Lcom/ibm/icu/util/ULocale;
    //   2	571	2	resultName	Ljava/lang/String;
    //   7	566	3	lang	Ljava/lang/String;
    //   26	547	4	script	Ljava/lang/String;
    //   32	541	5	country	Ljava/lang/String;
    //   38	535	6	variant	Ljava/lang/String;
    //   53	520	7	hasScript	Z
    //   68	505	8	hasCountry	Z
    //   83	490	9	hasVariant	Z
    //   304	269	10	buf	Ljava/lang/StringBuilder;
    //   369	204	11	keys	Ljava/util/Iterator;
    //   523	50	12	resultRemainder	Ljava/lang/String;
    // Local variable type table:
    //   start	length	slot	name	signature
    //   369	204	11	keys	Ljava/util/Iterator<Ljava/lang/String;>;
  }
  
  private String localeIdName(String localeId) {
    return this.langData.get("Languages", localeId);
  }
  
  public String languageDisplayName(String lang) {
    if (lang.equals("root") || lang.indexOf('_') != -1)
      return lang; 
    return adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, this.langData.get("Languages", lang));
  }
  
  public String scriptDisplayName(String script) {
    String str = this.langData.get("Scripts%stand-alone", script);
    if (str.equals(script))
      str = this.langData.get("Scripts", script); 
    return adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, str);
  }
  
  public String scriptDisplayNameInContext(String script) {
    return adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, this.langData.get("Scripts", script));
  }
  
  public String scriptDisplayName(int scriptCode) {
    return adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, scriptDisplayName(UScript.getShortName(scriptCode)));
  }
  
  public String regionDisplayName(String region) {
    return adjustForUsageAndContext(CapitalizationContextUsage.TERRITORY, this.regionData.get("Countries", region));
  }
  
  public String variantDisplayName(String variant) {
    return adjustForUsageAndContext(CapitalizationContextUsage.VARIANT, this.langData.get("Variants", variant));
  }
  
  public String keyDisplayName(String key) {
    return adjustForUsageAndContext(CapitalizationContextUsage.KEY, this.langData.get("Keys", key));
  }
  
  public String keyValueDisplayName(String key, String value) {
    return adjustForUsageAndContext(CapitalizationContextUsage.TYPE, this.langData.get("Types", key, value));
  }
  
  public static class DataTable {
    ULocale getLocale() {
      return ULocale.ROOT;
    }
    
    String get(String tableName, String code) {
      return get(tableName, null, code);
    }
    
    String get(String tableName, String subTableName, String code) {
      return code;
    }
  }
  
  static class ICUDataTable extends DataTable {
    private final ICUResourceBundle bundle;
    
    public ICUDataTable(String path, ULocale locale) {
      this.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
    }
    
    public ULocale getLocale() {
      return this.bundle.getULocale();
    }
    
    public String get(String tableName, String subTableName, String code) {
      return ICUResourceTableAccess.getTableString(this.bundle, tableName, subTableName, code);
    }
  }
  
  static abstract class DataTables {
    public abstract LocaleDisplayNamesImpl.DataTable get(ULocale param1ULocale);
    
    public static DataTables load(String className) {
      try {
        return (DataTables)Class.forName(className).newInstance();
      } catch (Throwable t) {
        final LocaleDisplayNamesImpl.DataTable NO_OP = new LocaleDisplayNamesImpl.DataTable();
        return new DataTables() {
            public LocaleDisplayNamesImpl.DataTable get(ULocale locale) {
              return NO_OP;
            }
          };
      } 
    }
  }
  
  static abstract class ICUDataTables extends DataTables {
    private final String path;
    
    protected ICUDataTables(String path) {
      this.path = path;
    }
    
    public LocaleDisplayNamesImpl.DataTable get(ULocale locale) {
      return new LocaleDisplayNamesImpl.ICUDataTable(this.path, locale);
    }
  }
  
  static class LangDataTables {
    static final LocaleDisplayNamesImpl.DataTables impl = LocaleDisplayNamesImpl.DataTables.load("com.ibm.icu.impl.ICULangDataTables");
  }
  
  static class RegionDataTables {
    static final LocaleDisplayNamesImpl.DataTables impl = LocaleDisplayNamesImpl.DataTables.load("com.ibm.icu.impl.ICURegionDataTables");
  }
  
  public enum DataTableType {
    LANG, REGION;
  }
  
  public static boolean haveData(DataTableType type) {
    switch (type) {
      case LANG:
        return LangDataTables.impl instanceof ICUDataTables;
      case REGION:
        return RegionDataTables.impl instanceof ICUDataTables;
    } 
    throw new IllegalArgumentException("unknown type: " + type);
  }
  
  static class Appender {
    private final String sep;
    
    Appender(String sep) {
      this.sep = sep;
    }
    
    StringBuilder append(String s, StringBuilder b) {
      if (b.length() > 0)
        b.append(this.sep); 
      b.append(s);
      return b;
    }
  }
  
  private static class Cache {
    private ULocale locale;
    
    private LocaleDisplayNames.DialectHandling dialectHandling;
    
    private DisplayContext capitalization;
    
    private LocaleDisplayNames cache;
    
    private Cache() {}
    
    public LocaleDisplayNames get(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) {
      if (dialectHandling != this.dialectHandling || DisplayContext.CAPITALIZATION_NONE != this.capitalization || !locale.equals(this.locale)) {
        this.locale = locale;
        this.dialectHandling = dialectHandling;
        this.capitalization = DisplayContext.CAPITALIZATION_NONE;
        this.cache = new LocaleDisplayNamesImpl(locale, dialectHandling);
      } 
      return this.cache;
    }
    
    public LocaleDisplayNames get(ULocale locale, DisplayContext... contexts) {
      LocaleDisplayNames.DialectHandling dialectHandlingIn = LocaleDisplayNames.DialectHandling.STANDARD_NAMES;
      DisplayContext capitalizationIn = DisplayContext.CAPITALIZATION_NONE;
      for (DisplayContext contextItem : contexts) {
        switch (contextItem.type()) {
          case LANG:
            dialectHandlingIn = (contextItem.value() == DisplayContext.STANDARD_NAMES.value()) ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
            break;
          case REGION:
            capitalizationIn = contextItem;
            break;
        } 
      } 
      if (dialectHandlingIn != this.dialectHandling || capitalizationIn != this.capitalization || !locale.equals(this.locale)) {
        this.locale = locale;
        this.dialectHandling = dialectHandlingIn;
        this.capitalization = capitalizationIn;
        this.cache = new LocaleDisplayNamesImpl(locale, contexts);
      } 
      return this.cache;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\LocaleDisplayNamesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */