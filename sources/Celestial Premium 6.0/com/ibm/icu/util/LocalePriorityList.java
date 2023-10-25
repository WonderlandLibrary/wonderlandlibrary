/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.ULocale;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class LocalePriorityList
implements Iterable<ULocale> {
    private static final double D0 = 0.0;
    private static final Double D1 = 1.0;
    private static final Pattern languageSplitter = Pattern.compile("\\s*,\\s*");
    private static final Pattern weightSplitter = Pattern.compile("\\s*(\\S*)\\s*;\\s*q\\s*=\\s*(\\S*)");
    private final Map<ULocale, Double> languagesAndWeights;
    private static Comparator<Double> myDescendingDouble = new Comparator<Double>(){

        @Override
        public int compare(Double o1, Double o2) {
            return -o1.compareTo(o2);
        }
    };

    public static Builder add(ULocale languageCode) {
        return new Builder().add(languageCode);
    }

    public static Builder add(ULocale languageCode, double weight) {
        return new Builder().add(languageCode, weight);
    }

    public static Builder add(LocalePriorityList languagePriorityList) {
        return new Builder().add(languagePriorityList);
    }

    public static Builder add(String acceptLanguageString) {
        return new Builder().add(acceptLanguageString);
    }

    public Double getWeight(ULocale language) {
        return this.languagesAndWeights.get(language);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ULocale language : this.languagesAndWeights.keySet()) {
            if (result.length() != 0) {
                result.append(", ");
            }
            result.append(language);
            double weight = this.languagesAndWeights.get(language);
            if (weight == D1) continue;
            result.append(";q=").append(weight);
        }
        return result.toString();
    }

    @Override
    public Iterator<ULocale> iterator() {
        return this.languagesAndWeights.keySet().iterator();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        try {
            LocalePriorityList that = (LocalePriorityList)o;
            return ((Object)this.languagesAndWeights).equals(that.languagesAndWeights);
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    public int hashCode() {
        return ((Object)this.languagesAndWeights).hashCode();
    }

    private LocalePriorityList(Map<ULocale, Double> languageToWeight) {
        this.languagesAndWeights = languageToWeight;
    }

    public static class Builder {
        private final Map<ULocale, Double> languageToWeight = new LinkedHashMap<ULocale, Double>();

        private Builder() {
        }

        public LocalePriorityList build() {
            return this.build(false);
        }

        public LocalePriorityList build(boolean preserveWeights) {
            TreeMap<Double, LinkedHashSet<ULocale>> doubleCheck = new TreeMap<Double, LinkedHashSet<ULocale>>(myDescendingDouble);
            for (ULocale lang : this.languageToWeight.keySet()) {
                Double weight = this.languageToWeight.get(lang);
                LinkedHashSet<ULocale> s = (LinkedHashSet<ULocale>)doubleCheck.get(weight);
                if (s == null) {
                    s = new LinkedHashSet<ULocale>();
                    doubleCheck.put(weight, s);
                }
                s.add(lang);
            }
            LinkedHashMap<ULocale, Double> temp = new LinkedHashMap<ULocale, Double>();
            for (Map.Entry langEntry : doubleCheck.entrySet()) {
                Double weight = (Double)langEntry.getKey();
                for (ULocale lang : (Set)langEntry.getValue()) {
                    temp.put(lang, preserveWeights ? weight : D1);
                }
            }
            return new LocalePriorityList(Collections.unmodifiableMap(temp));
        }

        public Builder add(LocalePriorityList languagePriorityList) {
            for (ULocale language : languagePriorityList.languagesAndWeights.keySet()) {
                this.add(language, (Double)languagePriorityList.languagesAndWeights.get(language));
            }
            return this;
        }

        public Builder add(ULocale languageCode) {
            return this.add(languageCode, D1);
        }

        public Builder add(ULocale ... languageCodes) {
            for (ULocale languageCode : languageCodes) {
                this.add(languageCode, D1);
            }
            return this;
        }

        public Builder add(ULocale languageCode, double weight) {
            if (this.languageToWeight.containsKey(languageCode)) {
                this.languageToWeight.remove(languageCode);
            }
            if (weight <= 0.0) {
                return this;
            }
            if (weight > D1) {
                weight = D1;
            }
            this.languageToWeight.put(languageCode, weight);
            return this;
        }

        public Builder add(String acceptLanguageList) {
            String[] items = languageSplitter.split(acceptLanguageList.trim());
            Matcher itemMatcher = weightSplitter.matcher("");
            for (String item : items) {
                if (itemMatcher.reset(item).matches()) {
                    ULocale language = new ULocale(itemMatcher.group(1));
                    double weight = Double.parseDouble(itemMatcher.group(2));
                    if (!(weight >= 0.0) || !(weight <= D1)) {
                        throw new IllegalArgumentException("Illegal weight, must be 0..1: " + weight);
                    }
                    this.add(language, weight);
                    continue;
                }
                if (item.length() == 0) continue;
                this.add(new ULocale(item));
            }
            return this;
        }
    }
}

