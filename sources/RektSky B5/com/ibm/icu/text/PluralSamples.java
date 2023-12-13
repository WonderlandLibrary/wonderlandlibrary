/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.Output;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Deprecated
public class PluralSamples {
    private PluralRules pluralRules;
    private final Map<String, List<Double>> _keySamplesMap;
    @Deprecated
    public final Map<String, Boolean> _keyLimitedMap;
    private final Map<String, Set<PluralRules.FixedDecimal>> _keyFractionSamplesMap;
    private final Set<PluralRules.FixedDecimal> _fractionSamples;
    private static final int[] TENS = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000};
    private static final int LIMIT_FRACTION_SAMPLES = 3;

    @Deprecated
    public PluralSamples(PluralRules pluralRules) {
        String keyword;
        TreeSet<PluralRules.FixedDecimal> mentioned;
        HashMap<String, Set<PluralRules.FixedDecimal>> sampleFractionMap;
        int keywordsRemaining;
        HashMap<String, List<Double>> sampleMap;
        Set<String> keywords;
        block15: {
            this.pluralRules = pluralRules;
            keywords = pluralRules.getKeywords();
            int MAX_SAMPLES = 3;
            HashMap<String, Boolean> temp = new HashMap<String, Boolean>();
            for (String k2 : keywords) {
                temp.put(k2, pluralRules.isLimited(k2));
            }
            this._keyLimitedMap = temp;
            sampleMap = new HashMap<String, List<Double>>();
            keywordsRemaining = keywords.size();
            int limit = 128;
            for (int i2 = 0; keywordsRemaining > 0 && i2 < limit; ++i2) {
                keywordsRemaining = this.addSimpleSamples(pluralRules, 3, sampleMap, keywordsRemaining, (double)i2 / 2.0);
            }
            keywordsRemaining = this.addSimpleSamples(pluralRules, 3, sampleMap, keywordsRemaining, 1000000.0);
            sampleFractionMap = new HashMap<String, Set<PluralRules.FixedDecimal>>();
            mentioned = new TreeSet<PluralRules.FixedDecimal>();
            HashMap<String, Set<PluralRules.FixedDecimal>> foundKeywords = new HashMap<String, Set<PluralRules.FixedDecimal>>();
            for (PluralRules.FixedDecimal fixedDecimal : mentioned) {
                keyword = pluralRules.select(fixedDecimal);
                this.addRelation(foundKeywords, keyword, fixedDecimal);
            }
            if (foundKeywords.size() != keywords.size()) {
                for (int i3 = 1; i3 < 1000; ++i3) {
                    boolean bl = this.addIfNotPresent(i3, mentioned, foundKeywords);
                    if (!bl) {
                        continue;
                    }
                    break block15;
                }
                for (int i2 = 10; i2 < 1000; ++i2) {
                    boolean bl = this.addIfNotPresent((double)i2 / 10.0, mentioned, foundKeywords);
                    if (!bl) {
                        continue;
                    }
                    break block15;
                }
                System.out.println("Failed to find sample for each keyword: " + foundKeywords + "\n\t" + pluralRules + "\n\t" + mentioned);
            }
        }
        mentioned.add(new PluralRules.FixedDecimal(0L));
        mentioned.add(new PluralRules.FixedDecimal(1L));
        mentioned.add(new PluralRules.FixedDecimal(2L));
        mentioned.add(new PluralRules.FixedDecimal(0.1, 1));
        mentioned.add(new PluralRules.FixedDecimal(1.99, 2));
        mentioned.addAll(this.fractions(mentioned));
        for (PluralRules.FixedDecimal fixedDecimal : mentioned) {
            keyword = pluralRules.select(fixedDecimal);
            LinkedHashSet<PluralRules.FixedDecimal> list = (LinkedHashSet<PluralRules.FixedDecimal>)sampleFractionMap.get(keyword);
            if (list == null) {
                list = new LinkedHashSet<PluralRules.FixedDecimal>();
                sampleFractionMap.put(keyword, list);
            }
            list.add(fixedDecimal);
        }
        if (keywordsRemaining > 0) {
            for (String string : keywords) {
                if (!sampleMap.containsKey(string)) {
                    sampleMap.put(string, Collections.emptyList());
                }
                if (sampleFractionMap.containsKey(string)) continue;
                sampleFractionMap.put(string, Collections.emptySet());
            }
        }
        for (Map.Entry entry : sampleMap.entrySet()) {
            sampleMap.put((String)entry.getKey(), Collections.unmodifiableList((List)entry.getValue()));
        }
        for (Map.Entry entry : sampleFractionMap.entrySet()) {
            sampleFractionMap.put((String)entry.getKey(), Collections.unmodifiableSet((Set)entry.getValue()));
        }
        this._keySamplesMap = sampleMap;
        this._keyFractionSamplesMap = sampleFractionMap;
        this._fractionSamples = Collections.unmodifiableSet(mentioned);
    }

    private int addSimpleSamples(PluralRules pluralRules, int MAX_SAMPLES, Map<String, List<Double>> sampleMap, int keywordsRemaining, double val2) {
        String keyword = pluralRules.select(val2);
        boolean keyIsLimited = this._keyLimitedMap.get(keyword);
        List<Double> list = sampleMap.get(keyword);
        if (list == null) {
            list = new ArrayList<Double>(MAX_SAMPLES);
            sampleMap.put(keyword, list);
        } else if (!keyIsLimited && list.size() == MAX_SAMPLES) {
            return keywordsRemaining;
        }
        list.add(val2);
        if (!keyIsLimited && list.size() == MAX_SAMPLES) {
            --keywordsRemaining;
        }
        return keywordsRemaining;
    }

    private void addRelation(Map<String, Set<PluralRules.FixedDecimal>> foundKeywords, String keyword, PluralRules.FixedDecimal s2) {
        Set<PluralRules.FixedDecimal> set = foundKeywords.get(keyword);
        if (set == null) {
            set = new HashSet<PluralRules.FixedDecimal>();
            foundKeywords.put(keyword, set);
        }
        set.add(s2);
    }

    private boolean addIfNotPresent(double d2, Set<PluralRules.FixedDecimal> mentioned, Map<String, Set<PluralRules.FixedDecimal>> foundKeywords) {
        PluralRules.FixedDecimal numberInfo = new PluralRules.FixedDecimal(d2);
        String keyword = this.pluralRules.select(numberInfo);
        if (!foundKeywords.containsKey(keyword) || keyword.equals("other")) {
            this.addRelation(foundKeywords, keyword, numberInfo);
            mentioned.add(numberInfo);
            if (keyword.equals("other") && foundKeywords.get("other").size() > 1) {
                return true;
            }
        }
        return false;
    }

    private Set<PluralRules.FixedDecimal> fractions(Set<PluralRules.FixedDecimal> original) {
        HashSet<PluralRules.FixedDecimal> toAddTo = new HashSet<PluralRules.FixedDecimal>();
        HashSet<Integer> result = new HashSet<Integer>();
        for (PluralRules.FixedDecimal base1 : original) {
            result.add((int)base1.integerValue);
        }
        ArrayList<Integer> ints = new ArrayList<Integer>(result);
        HashSet<String> keywords = new HashSet<String>();
        for (int j2 = 0; j2 < ints.size(); ++j2) {
            Integer base = (Integer)ints.get(j2);
            String keyword = this.pluralRules.select(base.intValue());
            if (keywords.contains(keyword)) continue;
            keywords.add(keyword);
            toAddTo.add(new PluralRules.FixedDecimal(base.intValue(), 1));
            toAddTo.add(new PluralRules.FixedDecimal(base.intValue(), 2));
            Integer fract = this.getDifferentCategory(ints, keyword);
            if (fract >= TENS[2]) {
                toAddTo.add(new PluralRules.FixedDecimal(base + "." + fract));
                continue;
            }
            for (int visibleFractions = 1; visibleFractions < 3; ++visibleFractions) {
                for (int i2 = 1; i2 <= visibleFractions; ++i2) {
                    if (fract >= TENS[i2]) continue;
                    toAddTo.add(new PluralRules.FixedDecimal((double)base.intValue() + (double)fract.intValue() / (double)TENS[i2], visibleFractions));
                }
            }
        }
        return toAddTo;
    }

    private Integer getDifferentCategory(List<Integer> ints, String keyword) {
        for (int i2 = ints.size() - 1; i2 >= 0; --i2) {
            Integer other = ints.get(i2);
            String keywordOther = this.pluralRules.select(other.intValue());
            if (keywordOther.equals(keyword)) continue;
            return other;
        }
        return 37;
    }

    @Deprecated
    public PluralRules.KeywordStatus getStatus(String keyword, int offset, Set<Double> explicits, Output<Double> uniqueValue) {
        if (uniqueValue != null) {
            uniqueValue.value = null;
        }
        if (!this.pluralRules.getKeywords().contains(keyword)) {
            return PluralRules.KeywordStatus.INVALID;
        }
        Collection<Double> values = this.pluralRules.getAllKeywordValues(keyword);
        if (values == null) {
            return PluralRules.KeywordStatus.UNBOUNDED;
        }
        int originalSize = values.size();
        if (explicits == null) {
            explicits = Collections.emptySet();
        }
        if (originalSize > explicits.size()) {
            if (originalSize == 1) {
                if (uniqueValue != null) {
                    uniqueValue.value = values.iterator().next();
                }
                return PluralRules.KeywordStatus.UNIQUE;
            }
            return PluralRules.KeywordStatus.BOUNDED;
        }
        HashSet<Double> subtractedSet = new HashSet<Double>(values);
        for (Double explicit : explicits) {
            subtractedSet.remove(explicit - (double)offset);
        }
        if (subtractedSet.size() == 0) {
            return PluralRules.KeywordStatus.SUPPRESSED;
        }
        if (uniqueValue != null && subtractedSet.size() == 1) {
            uniqueValue.value = subtractedSet.iterator().next();
        }
        return originalSize == 1 ? PluralRules.KeywordStatus.UNIQUE : PluralRules.KeywordStatus.BOUNDED;
    }

    Map<String, List<Double>> getKeySamplesMap() {
        return this._keySamplesMap;
    }

    Map<String, Set<PluralRules.FixedDecimal>> getKeyFractionSamplesMap() {
        return this._keyFractionSamplesMap;
    }

    Set<PluralRules.FixedDecimal> getFractionSamples() {
        return this._fractionSamples;
    }

    Collection<Double> getAllKeywordValues(String keyword) {
        if (!this.pluralRules.getKeywords().contains(keyword)) {
            return Collections.emptyList();
        }
        Collection result = this.getKeySamplesMap().get(keyword);
        if (result.size() > 2 && !this._keyLimitedMap.get(keyword).booleanValue()) {
            return null;
        }
        return result;
    }
}

