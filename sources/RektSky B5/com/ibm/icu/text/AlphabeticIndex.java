/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Collator;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.LocaleData;
import com.ibm.icu.util.ULocale;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class AlphabeticIndex<V>
implements Iterable<Bucket<V>> {
    private static final String BASE = "\ufdd0";
    private static final char CGJ = '\u034f';
    private static final Comparator<String> binaryCmp = new UTF16.StringComparator(true, false, 0);
    private final RuleBasedCollator collatorOriginal;
    private final RuleBasedCollator collatorPrimaryOnly;
    private RuleBasedCollator collatorExternal;
    private final Comparator<Record<V>> recordComparator = new Comparator<Record<V>>(){

        @Override
        public int compare(Record<V> o1, Record<V> o2) {
            return AlphabeticIndex.this.collatorOriginal.compare(o1.name, o2.name);
        }
    };
    private final List<String> firstCharsInScripts;
    private final UnicodeSet initialLabels = new UnicodeSet();
    private List<Record<V>> inputList;
    private BucketList<V> buckets;
    private String overflowLabel = "\u2026";
    private String underflowLabel = "\u2026";
    private String inflowLabel = "\u2026";
    private int maxLabelCount = 99;
    private static final int GC_LU_MASK = 2;
    private static final int GC_LL_MASK = 4;
    private static final int GC_LT_MASK = 8;
    private static final int GC_LM_MASK = 16;
    private static final int GC_LO_MASK = 32;
    private static final int GC_L_MASK = 62;
    private static final int GC_CN_MASK = 1;

    public AlphabeticIndex(ULocale locale) {
        this(locale, null);
    }

    public AlphabeticIndex(Locale locale) {
        this(ULocale.forLocale(locale), null);
    }

    public AlphabeticIndex(RuleBasedCollator collator) {
        this(null, collator);
    }

    private AlphabeticIndex(ULocale locale, RuleBasedCollator collator) {
        this.collatorOriginal = collator != null ? collator : (RuleBasedCollator)Collator.getInstance(locale);
        try {
            this.collatorPrimaryOnly = this.collatorOriginal.cloneAsThawed();
        }
        catch (Exception e2) {
            throw new IllegalStateException("Collator cannot be cloned", e2);
        }
        this.collatorPrimaryOnly.setStrength(0);
        this.collatorPrimaryOnly.freeze();
        this.firstCharsInScripts = this.getFirstCharactersInScripts();
        Collections.sort(this.firstCharsInScripts, this.collatorPrimaryOnly);
        while (true) {
            if (this.firstCharsInScripts.isEmpty()) {
                throw new IllegalArgumentException("AlphabeticIndex requires some non-ignorable script boundary strings");
            }
            if (this.collatorPrimaryOnly.compare(this.firstCharsInScripts.get(0), "") != 0) break;
            this.firstCharsInScripts.remove(0);
        }
        if (!this.addChineseIndexCharacters() && locale != null) {
            this.addIndexExemplars(locale);
        }
    }

    public AlphabeticIndex<V> addLabels(UnicodeSet additions) {
        this.initialLabels.addAll(additions);
        this.buckets = null;
        return this;
    }

    public AlphabeticIndex<V> addLabels(ULocale ... additions) {
        for (ULocale addition : additions) {
            this.addIndexExemplars(addition);
        }
        this.buckets = null;
        return this;
    }

    public AlphabeticIndex<V> addLabels(Locale ... additions) {
        for (Locale addition : additions) {
            this.addIndexExemplars(ULocale.forLocale(addition));
        }
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
        ArrayList<String> indexCharacters = new ArrayList<String>();
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
            if (this.collatorPrimaryOnly.compare(item, firstScriptBoundary) < 0 || this.collatorPrimaryOnly.compare(item, overflowBoundary) >= 0 || checkDistinct && this.collatorPrimaryOnly.compare(item, this.separated(item)) == 0) continue;
            int insertionPoint = Collections.binarySearch(indexCharacters, item, this.collatorPrimaryOnly);
            if (insertionPoint < 0) {
                indexCharacters.add(~insertionPoint, item);
                continue;
            }
            String itemAlreadyIn = (String)indexCharacters.get(insertionPoint);
            if (!AlphabeticIndex.isOneLabelBetterThanOther(nfkdNormalizer, item, itemAlreadyIn)) continue;
            indexCharacters.set(insertionPoint, item);
        }
        int size = indexCharacters.size() - 1;
        if (size > this.maxLabelCount) {
            int count = 0;
            int old = -1;
            Iterator it = indexCharacters.iterator();
            while (it.hasNext()) {
                it.next();
                int bump = ++count * this.maxLabelCount / size;
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
        if (!current.startsWith(BASE)) {
            return current;
        }
        char rest = current.charAt(BASE.length());
        if ('\u2800' < rest && rest <= '\u28ff') {
            return rest - 10240 + "\u5283";
        }
        return current.substring(BASE.length());
    }

    private void addIndexExemplars(ULocale locale) {
        UnicodeSet exemplars = LocaleData.getExemplarSet(locale, 0, 2);
        if (exemplars != null && !exemplars.isEmpty()) {
            this.initialLabels.addAll(exemplars);
            return;
        }
        exemplars = LocaleData.getExemplarSet(locale, 0, 0);
        if ((exemplars = exemplars.cloneAsThawed()).containsSome(97, 122) || exemplars.isEmpty()) {
            exemplars.addAll(97, 122);
        }
        if (exemplars.containsSome(44032, 55203)) {
            exemplars.remove(44032, 55203).add(44032).add(45208).add(45796).add(46972).add(47560).add(48148).add(49324).add(50500).add(51088).add(52264).add(52852).add(53440).add(54028).add(54616);
        }
        if (exemplars.containsSome(4608, 4991)) {
            UnicodeSet ethiopic = new UnicodeSet("[\u1200\u1208\u1210\u1218\u1220\u1228\u1230\u1238\u1240\u1248\u1250\u1258\u1260\u1268\u1270\u1278\u1280\u1288\u1290\u1298\u12a0\u12a8\u12b0\u12b8\u12c0\u12c8\u12d0\u12d8\u12e0\u12e8\u12f0\u12f8\u1300\u1308\u1310\u1318\u1320\u1328\u1330\u1338\u1340\u1348\u1350\u1358]");
            ethiopic.retainAll(exemplars);
            exemplars.remove(4608, 4991).addAll(ethiopic);
        }
        for (String item : exemplars) {
            this.initialLabels.add(UCharacter.toUpperCase(locale, item));
        }
    }

    private boolean addChineseIndexCharacters() {
        UnicodeSet contractions = new UnicodeSet();
        try {
            this.collatorPrimaryOnly.internalAddContractions(BASE.charAt(0), contractions);
        }
        catch (Exception e2) {
            return false;
        }
        if (contractions.isEmpty()) {
            return false;
        }
        this.initialLabels.addAll(contractions);
        for (String s2 : contractions) {
            assert (s2.startsWith(BASE));
            char c2 = s2.charAt(s2.length() - 1);
            if ('A' > c2 || c2 > 'Z') continue;
            this.initialLabels.add(65, 90);
            break;
        }
        return true;
    }

    private String separated(String item) {
        StringBuilder result = new StringBuilder();
        char last = item.charAt(0);
        result.append(last);
        for (int i2 = 1; i2 < item.length(); ++i2) {
            char ch = item.charAt(i2);
            if (!UCharacter.isHighSurrogate(last) || !UCharacter.isLowSurrogate(ch)) {
                result.append('\u034f');
            }
            result.append(ch);
            last = ch;
        }
        return result.toString();
    }

    public ImmutableIndex<V> buildImmutableIndex() {
        BucketList<V> immutableBucketList;
        if (this.inputList != null && !this.inputList.isEmpty()) {
            immutableBucketList = this.createBucketList();
        } else {
            if (this.buckets == null) {
                this.buckets = this.createBucketList();
            }
            immutableBucketList = this.buckets;
        }
        return new ImmutableIndex(immutableBucketList, this.collatorPrimaryOnly);
    }

    public List<String> getBucketLabels() {
        this.initBuckets();
        ArrayList<String> result = new ArrayList<String>();
        for (Bucket<V> bucket : this.buckets) {
            result.add(bucket.getLabel());
        }
        return result;
    }

    public RuleBasedCollator getCollator() {
        if (this.collatorExternal == null) {
            try {
                this.collatorExternal = (RuleBasedCollator)this.collatorOriginal.clone();
            }
            catch (Exception e2) {
                throw new IllegalStateException("Collator cannot be cloned", e2);
            }
        }
        return this.collatorExternal;
    }

    public AlphabeticIndex<V> addRecord(CharSequence name, V data) {
        this.buckets = null;
        if (this.inputList == null) {
            this.inputList = new ArrayList<Record<V>>();
        }
        this.inputList.add(new Record(name, data));
        return this;
    }

    public int getBucketIndex(CharSequence name) {
        this.initBuckets();
        return ((BucketList)this.buckets).getBucketIndex(name, this.collatorPrimaryOnly);
    }

    public AlphabeticIndex<V> clearRecords() {
        if (this.inputList != null && !this.inputList.isEmpty()) {
            this.inputList.clear();
            this.buckets = null;
        }
        return this;
    }

    public int getBucketCount() {
        this.initBuckets();
        return ((BucketList)this.buckets).getBucketCount();
    }

    public int getRecordCount() {
        return this.inputList != null ? this.inputList.size() : 0;
    }

    @Override
    public Iterator<Bucket<V>> iterator() {
        this.initBuckets();
        return this.buckets.iterator();
    }

    private void initBuckets() {
        String upperBoundary;
        Bucket nextBucket;
        if (this.buckets != null) {
            return;
        }
        this.buckets = this.createBucketList();
        if (this.inputList == null || this.inputList.isEmpty()) {
            return;
        }
        Collections.sort(this.inputList, this.recordComparator);
        Iterator bucketIterator = ((BucketList)this.buckets).fullIterator();
        Bucket currentBucket = (Bucket)bucketIterator.next();
        if (bucketIterator.hasNext()) {
            nextBucket = (Bucket)bucketIterator.next();
            upperBoundary = nextBucket.lowerBoundary;
        } else {
            nextBucket = null;
            upperBoundary = null;
        }
        for (Record<V> r2 : this.inputList) {
            while (upperBoundary != null && this.collatorPrimaryOnly.compare(((Record)r2).name, (Object)upperBoundary) >= 0) {
                currentBucket = nextBucket;
                if (bucketIterator.hasNext()) {
                    nextBucket = (Bucket)bucketIterator.next();
                    upperBoundary = nextBucket.lowerBoundary;
                    continue;
                }
                upperBoundary = null;
            }
            Bucket bucket = currentBucket;
            if (bucket.displayBucket != null) {
                bucket = bucket.displayBucket;
            }
            if (bucket.records == null) {
                bucket.records = new ArrayList();
            }
            bucket.records.add(r2);
        }
    }

    private static boolean isOneLabelBetterThanOther(Normalizer2 nfkdNormalizer, String one, String other) {
        String n1 = nfkdNormalizer.normalize(one);
        String n2 = nfkdNormalizer.normalize(other);
        int result = n1.codePointCount(0, n1.length()) - n2.codePointCount(0, n2.length());
        if (result != 0) {
            return result < 0;
        }
        result = binaryCmp.compare(n1, n2);
        if (result != 0) {
            return result < 0;
        }
        return binaryCmp.compare(one, other) < 0;
    }

    private BucketList<V> createBucketList() {
        Bucket bucket;
        List<String> indexCharacters = this.initLabels();
        long variableTop = this.collatorPrimaryOnly.isAlternateHandlingShifted() ? (long)this.collatorPrimaryOnly.getVariableTop() & 0xFFFFFFFFL : 0L;
        boolean hasInvisibleBuckets = false;
        Bucket[] asciiBuckets = new Bucket[26];
        Bucket[] pinyinBuckets = new Bucket[26];
        boolean hasPinyin = false;
        ArrayList bucketList = new ArrayList();
        bucketList.add(new Bucket(this.getUnderflowLabel(), "", Bucket.LabelType.UNDERFLOW));
        int scriptIndex = -1;
        String scriptUpperBoundary = "";
        block0: for (String current : indexCharacters) {
            Bucket singleBucket;
            char c2;
            char c22;
            if (this.collatorPrimaryOnly.compare(current, scriptUpperBoundary) >= 0) {
                String inflowBoundary = scriptUpperBoundary;
                boolean skippedScript = false;
                while (this.collatorPrimaryOnly.compare(current, scriptUpperBoundary = this.firstCharsInScripts.get(++scriptIndex)) >= 0) {
                    skippedScript = true;
                }
                if (skippedScript && bucketList.size() > 1) {
                    bucketList.add(new Bucket(this.getInflowLabel(), inflowBoundary, Bucket.LabelType.INFLOW));
                }
            }
            bucket = new Bucket(AlphabeticIndex.fixLabel(current), current, Bucket.LabelType.NORMAL);
            bucketList.add(bucket);
            if (current.length() == 1 && 'A' <= (c22 = current.charAt(0)) && c22 <= 'Z') {
                asciiBuckets[c22 - 65] = bucket;
            } else if (current.length() == BASE.length() + 1 && current.startsWith(BASE) && 'A' <= (c2 = current.charAt(BASE.length())) && c2 <= 'Z') {
                pinyinBuckets[c2 - 65] = bucket;
                hasPinyin = true;
            }
            if (current.startsWith(BASE) || !AlphabeticIndex.hasMultiplePrimaryWeights(this.collatorPrimaryOnly, variableTop, current) || current.endsWith("\uffff")) continue;
            int n2 = bucketList.size() - 2;
            while ((singleBucket = (Bucket)bucketList.get(n2)).labelType == Bucket.LabelType.NORMAL) {
                if (singleBucket.displayBucket == null && !AlphabeticIndex.hasMultiplePrimaryWeights(this.collatorPrimaryOnly, variableTop, singleBucket.lowerBoundary)) {
                    bucket = new Bucket("", current + "\uffff", Bucket.LabelType.NORMAL);
                    bucket.displayBucket = singleBucket;
                    bucketList.add(bucket);
                    hasInvisibleBuckets = true;
                    continue block0;
                }
                --n2;
            }
        }
        if (bucketList.size() == 1) {
            return new BucketList(bucketList, bucketList);
        }
        bucketList.add(new Bucket(this.getOverflowLabel(), scriptUpperBoundary, Bucket.LabelType.OVERFLOW));
        if (hasPinyin) {
            Bucket asciiBucket = null;
            for (int i3 = 0; i3 < 26; ++i3) {
                if (asciiBuckets[i3] != null) {
                    asciiBucket = asciiBuckets[i3];
                }
                if (pinyinBuckets[i3] == null || asciiBucket == null) continue;
                pinyinBuckets[i3].displayBucket = asciiBucket;
                hasInvisibleBuckets = true;
            }
        }
        if (!hasInvisibleBuckets) {
            return new BucketList(bucketList, bucketList);
        }
        int i4 = bucketList.size() - 1;
        Bucket nextBucket = (Bucket)bucketList.get(i4);
        while (--i4 > 0) {
            bucket = (Bucket)bucketList.get(i4);
            if (bucket.displayBucket != null) continue;
            if (bucket.labelType == Bucket.LabelType.INFLOW && nextBucket.labelType != Bucket.LabelType.NORMAL) {
                bucket.displayBucket = nextBucket;
                continue;
            }
            nextBucket = bucket;
        }
        ArrayList<Bucket> publicBucketList = new ArrayList<Bucket>();
        for (Bucket bucket2 : bucketList) {
            if (bucket2.displayBucket != null) continue;
            publicBucketList.add(bucket2);
        }
        return new BucketList(bucketList, publicBucketList);
    }

    private static boolean hasMultiplePrimaryWeights(RuleBasedCollator coll, long variableTop, String s2) {
        long[] ces = coll.internalGetCEs(s2);
        boolean seenPrimary = false;
        for (int i2 = 0; i2 < ces.length; ++i2) {
            long ce = ces[i2];
            long p2 = ce >>> 32;
            if (p2 <= variableTop) continue;
            if (seenPrimary) {
                return true;
            }
            seenPrimary = true;
        }
        return false;
    }

    @Deprecated
    public List<String> getFirstCharactersInScripts() {
        ArrayList<String> dest = new ArrayList<String>(200);
        UnicodeSet set = new UnicodeSet();
        this.collatorPrimaryOnly.internalAddContractions(64977, set);
        if (set.isEmpty()) {
            throw new UnsupportedOperationException("AlphabeticIndex requires script-first-primary contractions");
        }
        for (String boundary : set) {
            int gcMask = 1 << UCharacter.getType(boundary.codePointAt(1));
            if ((gcMask & 0x3F) == 0) continue;
            dest.add(boundary);
        }
        return dest;
    }

    private static class BucketList<V>
    implements Iterable<Bucket<V>> {
        private final ArrayList<Bucket<V>> bucketList;
        private final List<Bucket<V>> immutableVisibleList;

        private BucketList(ArrayList<Bucket<V>> bucketList, ArrayList<Bucket<V>> publicBucketList) {
            this.bucketList = bucketList;
            int displayIndex = 0;
            for (Bucket<V> bucket : publicBucketList) {
                ((Bucket)bucket).displayIndex = displayIndex++;
            }
            this.immutableVisibleList = Collections.unmodifiableList(publicBucketList);
        }

        private int getBucketCount() {
            return this.immutableVisibleList.size();
        }

        private int getBucketIndex(CharSequence name, Collator collatorPrimaryOnly) {
            int start = 0;
            int limit = this.bucketList.size();
            while (start + 1 < limit) {
                int i2 = (start + limit) / 2;
                Bucket<V> bucket = this.bucketList.get(i2);
                int nameVsBucket = collatorPrimaryOnly.compare(name, (Object)((Bucket)bucket).lowerBoundary);
                if (nameVsBucket < 0) {
                    limit = i2;
                    continue;
                }
                start = i2;
            }
            Bucket bucket = this.bucketList.get(start);
            if (bucket.displayBucket != null) {
                bucket = bucket.displayBucket;
            }
            return bucket.displayIndex;
        }

        private Iterator<Bucket<V>> fullIterator() {
            return this.bucketList.iterator();
        }

        @Override
        public Iterator<Bucket<V>> iterator() {
            return this.immutableVisibleList.iterator();
        }
    }

    public static class Bucket<V>
    implements Iterable<Record<V>> {
        private final String label;
        private final String lowerBoundary;
        private final LabelType labelType;
        private Bucket<V> displayBucket;
        private int displayIndex;
        private List<Record<V>> records;

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
            return this.records == null ? 0 : this.records.size();
        }

        @Override
        public Iterator<Record<V>> iterator() {
            if (this.records == null) {
                return Collections.emptyList().iterator();
            }
            return this.records.iterator();
        }

        public String toString() {
            return "{labelType=" + (Object)((Object)this.labelType) + ", lowerBoundary=" + this.lowerBoundary + ", label=" + this.label + "}";
        }

        public static enum LabelType {
            NORMAL,
            UNDERFLOW,
            INFLOW,
            OVERFLOW;

        }
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

    public static final class ImmutableIndex<V>
    implements Iterable<Bucket<V>> {
        private final BucketList<V> buckets;
        private final Collator collatorPrimaryOnly;

        private ImmutableIndex(BucketList<V> bucketList, Collator collatorPrimaryOnly) {
            this.buckets = bucketList;
            this.collatorPrimaryOnly = collatorPrimaryOnly;
        }

        public int getBucketCount() {
            return ((BucketList)this.buckets).getBucketCount();
        }

        public int getBucketIndex(CharSequence name) {
            return ((BucketList)this.buckets).getBucketIndex(name, this.collatorPrimaryOnly);
        }

        public Bucket<V> getBucket(int index) {
            if (0 <= index && index < ((BucketList)this.buckets).getBucketCount()) {
                return (Bucket)((BucketList)this.buckets).immutableVisibleList.get(index);
            }
            return null;
        }

        @Override
        public Iterator<Bucket<V>> iterator() {
            return this.buckets.iterator();
        }
    }
}

