package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.LocaleData;
import com.ibm.icu.util.ULocale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class AlphabeticIndex<V> implements Iterable<AlphabeticIndex.Bucket<V>> {
  private static final String BASE = "﷐";
  
  private static final char CGJ = '͏';
  
  private static final Comparator<String> binaryCmp = new UTF16.StringComparator(true, false, 0);
  
  private final RuleBasedCollator collatorOriginal;
  
  private final RuleBasedCollator collatorPrimaryOnly;
  
  private RuleBasedCollator collatorExternal;
  
  private final Comparator<Record<V>> recordComparator = new Comparator<Record<V>>() {
      public int compare(AlphabeticIndex.Record<V> o1, AlphabeticIndex.Record<V> o2) {
        return AlphabeticIndex.this.collatorOriginal.compare(o1.name, o2.name);
      }
    };
  
  private final List<String> firstCharsInScripts;
  
  private final UnicodeSet initialLabels = new UnicodeSet();
  
  private List<Record<V>> inputList;
  
  private BucketList<V> buckets;
  
  private String overflowLabel = "…";
  
  private String underflowLabel = "…";
  
  private String inflowLabel = "…";
  
  private int maxLabelCount;
  
  public static final class ImmutableIndex<V> implements Iterable<Bucket<V>> {
    private final AlphabeticIndex.BucketList<V> buckets;
    
    private final Collator collatorPrimaryOnly;
    
    private ImmutableIndex(AlphabeticIndex.BucketList<V> bucketList, Collator collatorPrimaryOnly) {
      this.buckets = bucketList;
      this.collatorPrimaryOnly = collatorPrimaryOnly;
    }
    
    public int getBucketCount() {
      return this.buckets.getBucketCount();
    }
    
    public int getBucketIndex(CharSequence name) {
      return this.buckets.getBucketIndex(name, this.collatorPrimaryOnly);
    }
    
    public AlphabeticIndex.Bucket<V> getBucket(int index) {
      if (0 <= index && index < this.buckets.getBucketCount())
        return this.buckets.immutableVisibleList.get(index); 
      return null;
    }
    
    public Iterator<AlphabeticIndex.Bucket<V>> iterator() {
      return this.buckets.iterator();
    }
  }
  
  public AlphabeticIndex(ULocale locale) {
    this(locale, null);
  }
  
  public AlphabeticIndex(Locale locale) {
    this(ULocale.forLocale(locale), null);
  }
  
  public AlphabeticIndex(RuleBasedCollator collator) {
    this(null, collator);
  }
  
  public AlphabeticIndex<V> addLabels(UnicodeSet additions) {
    this.initialLabels.addAll(additions);
    this.buckets = null;
    return this;
  }
  
  public AlphabeticIndex<V> addLabels(ULocale... additions) {
    for (ULocale addition : additions)
      addIndexExemplars(addition); 
    this.buckets = null;
    return this;
  }
  
  public AlphabeticIndex<V> addLabels(Locale... additions) {
    for (Locale addition : additions)
      addIndexExemplars(ULocale.forLocale(addition)); 
    this.buckets = null;
    return this;
  }
  
  public AlphabeticIndex<V> setOverflowLabel(String overflowLabel) {
    this.overflowLabel = overflowLabel;
    this.buckets = null;
    return this;
  }
  
  public String getUnderflowLabel() {
    return this.underflowLabel;
  }
  
  public AlphabeticIndex<V> setUnderflowLabel(String underflowLabel) {
    this.underflowLabel = underflowLabel;
    this.buckets = null;
    return this;
  }
  
  public String getOverflowLabel() {
    return this.overflowLabel;
  }
  
  public AlphabeticIndex<V> setInflowLabel(String inflowLabel) {
    this.inflowLabel = inflowLabel;
    this.buckets = null;
    return this;
  }
  
  public String getInflowLabel() {
    return this.inflowLabel;
  }
  
  public int getMaxLabelCount() {
    return this.maxLabelCount;
  }
  
  public AlphabeticIndex<V> setMaxLabelCount(int maxLabelCount) {
    this.maxLabelCount = maxLabelCount;
    this.buckets = null;
    return this;
  }
  
  private List<String> initLabels() {
    Normalizer2 nfkdNormalizer = Normalizer2.getNFKDInstance();
    List<String> indexCharacters = new ArrayList<String>();
    String firstScriptBoundary = this.firstCharsInScripts.get(0);
    String overflowBoundary = this.firstCharsInScripts.get(this.firstCharsInScripts.size() - 1);
    for (String item : this.initialLabels) {
      boolean checkDistinct;
      if (!UTF16.hasMoreCodePointsThan(item, 1)) {
        checkDistinct = false;
      } else if (item.charAt(item.length() - 1) == '*' && item.charAt(item.length() - 2) != '*') {
        item = item.substring(0, item.length() - 1);
        checkDistinct = false;
      } else {
        checkDistinct = true;
      } 
      if (this.collatorPrimaryOnly.compare(item, firstScriptBoundary) < 0)
        continue; 
      if (this.collatorPrimaryOnly.compare(item, overflowBoundary) >= 0)
        continue; 
      if (checkDistinct && this.collatorPrimaryOnly.compare(item, separated(item)) == 0)
        continue; 
      int insertionPoint = Collections.binarySearch(indexCharacters, item, this.collatorPrimaryOnly);
      if (insertionPoint < 0) {
        indexCharacters.add(insertionPoint ^ 0xFFFFFFFF, item);
        continue;
      } 
      String itemAlreadyIn = indexCharacters.get(insertionPoint);
      if (isOneLabelBetterThanOther(nfkdNormalizer, item, itemAlreadyIn))
        indexCharacters.set(insertionPoint, item); 
    } 
    int size = indexCharacters.size() - 1;
    if (size > this.maxLabelCount) {
      int count = 0;
      int old = -1;
      for (Iterator<String> it = indexCharacters.iterator(); it.hasNext(); ) {
        count++;
        it.next();
        int bump = count * this.maxLabelCount / size;
        if (bump == old) {
          it.remove();
          continue;
        } 
        old = bump;
      } 
    } 
    return indexCharacters;
  }
  
  private static String fixLabel(String current) {
    if (!current.startsWith("﷐"))
      return current; 
    int rest = current.charAt("﷐".length());
    if (10240 < rest && rest <= 10495)
      return (rest - 10240) + "劃"; 
    return current.substring("﷐".length());
  }
  
  private void addIndexExemplars(ULocale locale) {
    String language = locale.getLanguage();
    if (language.equals("zh") || language.equals("ja") || language.equals("ko"))
      if (addChineseIndexCharacters())
        return;  
    UnicodeSet exemplars = LocaleData.getExemplarSet(locale, 0, 2);
    if (exemplars != null) {
      this.initialLabels.addAll(exemplars);
      return;
    } 
    exemplars = LocaleData.getExemplarSet(locale, 0, 0);
    exemplars = exemplars.cloneAsThawed();
    if (exemplars.containsSome(97, 122) || exemplars.size() == 0)
      exemplars.addAll(97, 122); 
    if (exemplars.containsSome(44032, 55203))
      exemplars.remove(44032, 55203).add(44032).add(45208).add(45796).add(46972).add(47560).add(48148).add(49324).add(50500).add(51088).add(52264).add(52852).add(53440).add(54028).add(54616); 
    if (exemplars.containsSome(4608, 4991)) {
      UnicodeSet ethiopic = new UnicodeSet("[[:Block=Ethiopic:]&[:Script=Ethiopic:]]");
      UnicodeSetIterator it = new UnicodeSetIterator(ethiopic);
      while (it.next() && it.codepoint != UnicodeSetIterator.IS_STRING) {
        if ((it.codepoint & 0x7) != 0)
          exemplars.remove(it.codepoint); 
      } 
    } 
    for (String item : exemplars)
      this.initialLabels.add(UCharacter.toUpperCase(locale, item)); 
  }
  
  private boolean addChineseIndexCharacters() {
    UnicodeSet contractions = new UnicodeSet();
    try {
      this.collatorPrimaryOnly.getContractionsAndExpansions(contractions, null, false);
    } catch (Exception e) {
      return false;
    } 
    String firstHanBoundary = null;
    boolean hasPinyin = false;
    for (String s : contractions) {
      if (s.startsWith("﷐")) {
        this.initialLabels.add(s);
        if (firstHanBoundary == null || this.collatorPrimaryOnly.compare(s, firstHanBoundary) < 0)
          firstHanBoundary = s; 
        char c = s.charAt(s.length() - 1);
        if ('A' <= c && c <= 'Z')
          hasPinyin = true; 
      } 
    } 
    if (hasPinyin)
      this.initialLabels.add(65, 90); 
    if (firstHanBoundary != null) {
      int hanIndex = Collections.binarySearch(this.firstCharsInScripts, "一", this.collatorPrimaryOnly);
      if (hanIndex >= 0)
        this.firstCharsInScripts.set(hanIndex, firstHanBoundary); 
      return true;
    } 
    return false;
  }
  
  private String separated(String item) {
    StringBuilder result = new StringBuilder();
    char last = item.charAt(0);
    result.append(last);
    for (int i = 1; i < item.length(); i++) {
      char ch = item.charAt(i);
      if (!UCharacter.isHighSurrogate(last) || !UCharacter.isLowSurrogate(ch))
        result.append('͏'); 
      result.append(ch);
      last = ch;
    } 
    return result.toString();
  }
  
  public ImmutableIndex<V> buildImmutableIndex() {
    BucketList<V> immutableBucketList;
    if (this.inputList != null && !this.inputList.isEmpty()) {
      immutableBucketList = createBucketList();
    } else {
      if (this.buckets == null)
        this.buckets = createBucketList(); 
      immutableBucketList = this.buckets;
    } 
    return new ImmutableIndex<V>(immutableBucketList, this.collatorPrimaryOnly);
  }
  
  public List<String> getBucketLabels() {
    initBuckets();
    ArrayList<String> result = new ArrayList<String>();
    for (Bucket<V> bucket : this.buckets)
      result.add(bucket.getLabel()); 
    return result;
  }
  
  public RuleBasedCollator getCollator() {
    if (this.collatorExternal == null)
      try {
        this.collatorExternal = (RuleBasedCollator)this.collatorOriginal.clone();
      } catch (Exception e) {
        throw new IllegalStateException("Collator cannot be cloned", e);
      }  
    return this.collatorExternal;
  }
  
  public AlphabeticIndex<V> addRecord(CharSequence name, V data) {
    this.buckets = null;
    if (this.inputList == null)
      this.inputList = new ArrayList<Record<V>>(); 
    this.inputList.add(new Record<V>(name, data));
    return this;
  }
  
  public int getBucketIndex(CharSequence name) {
    initBuckets();
    return this.buckets.getBucketIndex(name, this.collatorPrimaryOnly);
  }
  
  public AlphabeticIndex<V> clearRecords() {
    if (this.inputList != null && !this.inputList.isEmpty()) {
      this.inputList.clear();
      this.buckets = null;
    } 
    return this;
  }
  
  public int getBucketCount() {
    initBuckets();
    return this.buckets.getBucketCount();
  }
  
  public int getRecordCount() {
    return (this.inputList != null) ? this.inputList.size() : 0;
  }
  
  public Iterator<Bucket<V>> iterator() {
    initBuckets();
    return this.buckets.iterator();
  }
  
  private void initBuckets() {
    Bucket<V> nextBucket;
    String upperBoundary;
    if (this.buckets != null)
      return; 
    this.buckets = createBucketList();
    if (this.inputList == null || this.inputList.isEmpty())
      return; 
    Collections.sort(this.inputList, this.recordComparator);
    Iterator<Bucket<V>> bucketIterator = this.buckets.fullIterator();
    Bucket<V> currentBucket = bucketIterator.next();
    if (bucketIterator.hasNext()) {
      nextBucket = bucketIterator.next();
      upperBoundary = nextBucket.lowerBoundary;
    } else {
      nextBucket = null;
      upperBoundary = null;
    } 
    for (Record<V> r : this.inputList) {
      while (upperBoundary != null && this.collatorPrimaryOnly.compare(r.name, upperBoundary) >= 0) {
        currentBucket = nextBucket;
        if (bucketIterator.hasNext()) {
          nextBucket = bucketIterator.next();
          upperBoundary = nextBucket.lowerBoundary;
          continue;
        } 
        upperBoundary = null;
      } 
      Bucket<V> bucket = currentBucket;
      if (bucket.displayBucket != null)
        bucket = bucket.displayBucket; 
      if (bucket.records == null)
        bucket.records = new ArrayList(); 
      bucket.records.add(r);
    } 
  }
  
  private AlphabeticIndex(ULocale locale, RuleBasedCollator collator) {
    this.maxLabelCount = 99;
    this.collatorOriginal = (collator != null) ? collator : (RuleBasedCollator)Collator.getInstance(locale);
    try {
      this.collatorPrimaryOnly = (RuleBasedCollator)this.collatorOriginal.clone();
    } catch (Exception e) {
      throw new IllegalStateException("Collator cannot be cloned", e);
    } 
    this.collatorPrimaryOnly.setStrength(0);
    this.collatorPrimaryOnly.freeze();
    this.firstCharsInScripts = new ArrayList<String>(HACK_FIRST_CHARS_IN_SCRIPTS);
    Collections.sort(this.firstCharsInScripts, this.collatorPrimaryOnly);
    if (this.collatorPrimaryOnly.compare("一", "ᄒ") <= 0 && this.collatorPrimaryOnly.compare("ᄀ", "一") <= 0) {
      int hanIndex = Collections.binarySearch(this.firstCharsInScripts, "一", this.collatorPrimaryOnly);
      if (hanIndex >= 0)
        this.firstCharsInScripts.remove(hanIndex); 
    } 
    while (true) {
      if (this.firstCharsInScripts.isEmpty())
        throw new IllegalArgumentException("AlphabeticIndex requires some non-ignorable script boundary strings"); 
      if (this.collatorPrimaryOnly.compare(this.firstCharsInScripts.get(0), "") == 0) {
        this.firstCharsInScripts.remove(0);
        continue;
      } 
      break;
    } 
    if (locale != null)
      addIndexExemplars(locale); 
  }
  
  private static boolean isOneLabelBetterThanOther(Normalizer2 nfkdNormalizer, String one, String other) {
    String n1 = nfkdNormalizer.normalize(one);
    String n2 = nfkdNormalizer.normalize(other);
    int result = n1.codePointCount(0, n1.length()) - n2.codePointCount(0, n2.length());
    if (result != 0)
      return (result < 0); 
    result = binaryCmp.compare(n1, n2);
    if (result != 0)
      return (result < 0); 
    return (binaryCmp.compare(one, other) < 0);
  }
  
  public static class Record<V> {
    private final CharSequence name;
    
    private final V data;
    
    private Record(CharSequence name, V data) {
      this.name = name;
      this.data = data;
    }
    
    public CharSequence getName() {
      return this.name;
    }
    
    public V getData() {
      return this.data;
    }
    
    public String toString() {
      return this.name + "=" + this.data;
    }
  }
  
  public static class Bucket<V> implements Iterable<Record<V>> {
    private final String label;
    
    private final String lowerBoundary;
    
    private final LabelType labelType;
    
    private Bucket<V> displayBucket;
    
    private int displayIndex;
    
    private List<AlphabeticIndex.Record<V>> records;
    
    public enum LabelType {
      NORMAL, UNDERFLOW, INFLOW, OVERFLOW;
    }
    
    private Bucket(String label, String lowerBoundary, LabelType labelType) {
      this.label = label;
      this.lowerBoundary = lowerBoundary;
      this.labelType = labelType;
    }
    
    public String getLabel() {
      return this.label;
    }
    
    public LabelType getLabelType() {
      return this.labelType;
    }
    
    public int size() {
      return (this.records == null) ? 0 : this.records.size();
    }
    
    public Iterator<AlphabeticIndex.Record<V>> iterator() {
      if (this.records == null)
        return Collections.<AlphabeticIndex.Record<V>>emptyList().iterator(); 
      return this.records.iterator();
    }
    
    public String toString() {
      return "{labelType=" + this.labelType + ", " + "lowerBoundary=" + this.lowerBoundary + ", " + "label=" + this.label + "}";
    }
  }
  
  public enum LabelType {
    NORMAL, UNDERFLOW, INFLOW, OVERFLOW;
  }
  
  private BucketList<V> createBucketList() {
    int variableTop;
    List<String> indexCharacters = initLabels();
    CollationElementIterator cei = this.collatorPrimaryOnly.getCollationElementIterator("");
    if (this.collatorPrimaryOnly.isAlternateHandlingShifted()) {
      variableTop = CollationElementIterator.primaryOrder(this.collatorPrimaryOnly.getVariableTop());
    } else {
      variableTop = 0;
    } 
    boolean hasInvisibleBuckets = false;
    Bucket[] arrayOfBucket1 = new Bucket[26];
    Bucket[] arrayOfBucket2 = new Bucket[26];
    boolean hasPinyin = false;
    ArrayList<Bucket<V>> bucketList = new ArrayList<Bucket<V>>();
    bucketList.add(new Bucket<V>(getUnderflowLabel(), "", Bucket.LabelType.UNDERFLOW));
    int scriptIndex = -1;
    String scriptUpperBoundary = "";
    label86: for (String current : indexCharacters) {
      if (this.collatorPrimaryOnly.compare(current, scriptUpperBoundary) >= 0) {
        String inflowBoundary = scriptUpperBoundary;
        boolean skippedScript = false;
        while (true) {
          scriptUpperBoundary = this.firstCharsInScripts.get(++scriptIndex);
          if (this.collatorPrimaryOnly.compare(current, scriptUpperBoundary) < 0)
            break; 
          skippedScript = true;
        } 
        if (skippedScript && bucketList.size() > 1)
          bucketList.add(new Bucket<V>(getInflowLabel(), inflowBoundary, Bucket.LabelType.INFLOW)); 
      } 
      Bucket<V> bucket = new Bucket<V>(fixLabel(current), current, Bucket.LabelType.NORMAL);
      bucketList.add(bucket);
      char c;
      if (current.length() == 1 && 'A' <= (c = current.charAt(0)) && c <= 'Z') {
        arrayOfBucket1[c - 65] = bucket;
      } else if (current.length() == "﷐".length() + 1 && current.startsWith("﷐") && 'A' <= (c = current.charAt("﷐".length())) && c <= 'Z') {
        arrayOfBucket2[c - 65] = bucket;
        hasPinyin = true;
      } 
      if (!current.startsWith("﷐") && hasMultiplePrimaryWeights(cei, variableTop, current) && !current.endsWith("￿"))
        for (int j = bucketList.size() - 2;; j--) {
          Bucket<V> singleBucket = bucketList.get(j);
          if (singleBucket.labelType != Bucket.LabelType.NORMAL)
            continue label86; 
          if (singleBucket.displayBucket == null && !hasMultiplePrimaryWeights(cei, variableTop, singleBucket.lowerBoundary)) {
            bucket = new Bucket<V>("", current + "￿", Bucket.LabelType.NORMAL);
            bucket.displayBucket = singleBucket;
            bucketList.add(bucket);
            hasInvisibleBuckets = true;
            continue label86;
          } 
        }  
    } 
    if (bucketList.size() == 1)
      return new BucketList<V>(bucketList, bucketList); 
    bucketList.add(new Bucket<V>(getOverflowLabel(), scriptUpperBoundary, Bucket.LabelType.OVERFLOW));
    if (hasPinyin) {
      Bucket<V> asciiBucket = null;
      for (int j = 0; j < 26; j++) {
        if (arrayOfBucket1[j] != null)
          asciiBucket = arrayOfBucket1[j]; 
        if (arrayOfBucket2[j] != null && asciiBucket != null) {
          (arrayOfBucket2[j]).displayBucket = asciiBucket;
          hasInvisibleBuckets = true;
        } 
      } 
    } 
    if (!hasInvisibleBuckets)
      return new BucketList<V>(bucketList, bucketList); 
    int i = bucketList.size() - 1;
    Bucket<V> nextBucket = bucketList.get(i);
    while (--i > 0) {
      Bucket<V> bucket = bucketList.get(i);
      if (bucket.displayBucket != null)
        continue; 
      if (bucket.labelType == Bucket.LabelType.INFLOW && 
        nextBucket.labelType != Bucket.LabelType.NORMAL) {
        bucket.displayBucket = nextBucket;
        continue;
      } 
      nextBucket = bucket;
    } 
    ArrayList<Bucket<V>> publicBucketList = new ArrayList<Bucket<V>>();
    for (Bucket<V> bucket : bucketList) {
      if (bucket.displayBucket == null)
        publicBucketList.add(bucket); 
    } 
    return new BucketList<V>(bucketList, publicBucketList);
  }
  
  private static class BucketList<V> implements Iterable<Bucket<V>> {
    private final ArrayList<AlphabeticIndex.Bucket<V>> bucketList;
    
    private final List<AlphabeticIndex.Bucket<V>> immutableVisibleList;
    
    private BucketList(ArrayList<AlphabeticIndex.Bucket<V>> bucketList, ArrayList<AlphabeticIndex.Bucket<V>> publicBucketList) {
      this.bucketList = bucketList;
      int displayIndex = 0;
      for (AlphabeticIndex.Bucket<V> bucket : publicBucketList)
        bucket.displayIndex = displayIndex++; 
      this.immutableVisibleList = Collections.unmodifiableList(publicBucketList);
    }
    
    private int getBucketCount() {
      return this.immutableVisibleList.size();
    }
    
    private int getBucketIndex(CharSequence name, Collator collatorPrimaryOnly) {
      int start = 0;
      int limit = this.bucketList.size();
      while (start + 1 < limit) {
        int i = (start + limit) / 2;
        AlphabeticIndex.Bucket<V> bucket1 = this.bucketList.get(i);
        int nameVsBucket = collatorPrimaryOnly.compare(name, bucket1.lowerBoundary);
        if (nameVsBucket < 0) {
          limit = i;
          continue;
        } 
        start = i;
      } 
      AlphabeticIndex.Bucket<V> bucket = this.bucketList.get(start);
      if (bucket.displayBucket != null)
        bucket = bucket.displayBucket; 
      return bucket.displayIndex;
    }
    
    private Iterator<AlphabeticIndex.Bucket<V>> fullIterator() {
      return this.bucketList.iterator();
    }
    
    public Iterator<AlphabeticIndex.Bucket<V>> iterator() {
      return this.immutableVisibleList.iterator();
    }
  }
  
  private static boolean hasMultiplePrimaryWeights(CollationElementIterator cei, int variableTop, String s) {
    cei.setText(s);
    boolean seenPrimary = false;
    while (true) {
      int ce32 = cei.next();
      if (ce32 == -1)
        break; 
      int p = CollationElementIterator.primaryOrder(ce32);
      if (p > variableTop && (ce32 & 0xC0) != 192) {
        if (seenPrimary)
          return true; 
        seenPrimary = true;
      } 
    } 
    return false;
  }
  
  private static final List<String> HACK_FIRST_CHARS_IN_SCRIPTS = Arrays.asList(new String[] { 
        "A", "α", "ⲁ", "а", "ⰰ", "ა", "ա", "א", "𐤀", "ࠀ", 
        "ء", "ܐ", "ࡀ", "ހ", "ߊ", "ⴰ", "ሀ", "ॐ", "অ", "ੴ", 
        "ૐ", "ଅ", "ௐ", "అ", "ಅ", "അ", "අ", "ꫲ", "ꠀ", "ꢂ", 
        "𑂃", UCharacter.toString(70084), UCharacter.toString(71296), "ᮃ", "𑀅", "𐨀", "ก", "ໞ", "ꪀ", "ཀ", 
        "ᰀ", "ꡀ", "ᤀ", "ᜀ", "ᜠ", "ᝀ", "ᝠ", "ᨀ", "ᯀ", "ꤰ", 
        "ꤊ", "က", UCharacter.toString(69891), "ក", "ᥐ", "ᦀ", "ᨠ", "ꨀ", "ᬅ", "ꦄ", 
        "ᢀ", "ᱚ", "Ꭰ", "ᐁ", "ᚁ", "ᚠ", "𐰀", "ꔀ", "ꚠ", "ᄀ", 
        "ぁ", "ァ", "ㄅ", "ꀀ", "ꓸ", UCharacter.toString(93952), "𐊀", "𐊠", "𐤠", "𐌀", 
        "𐌰", "𐐨", "𐑐", "𐒀", UCharacter.toString(69840), "𐀀", "𐠀", "𐩠", "𐬀", "𐡀", 
        "𐭀", "𐭠", "𐎀", "𐎠", "𒀀", "𓀀", UCharacter.toString(68000), UCharacter.toString(67968), "一", "￿" });
  
  public static Collection<String> getFirstCharactersInScripts() {
    return HACK_FIRST_CHARS_IN_SCRIPTS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\AlphabeticIndex.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */