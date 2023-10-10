/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.locale;

import com.ibm.icu.impl.locale.XCldrStub;
import com.ibm.icu.impl.locale.XLikelySubtags;
import com.ibm.icu.impl.locale.XLocaleDistance;
import com.ibm.icu.util.LocalePriorityList;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.ULocale;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class XLocaleMatcher {
    private static final XLikelySubtags.LSR UND = new XLikelySubtags.LSR("und", "", "");
    private static final ULocale UND_LOCALE = new ULocale("und");
    private final XLocaleDistance localeDistance;
    private final int thresholdDistance;
    private final int demotionPerAdditionalDesiredLocale;
    private final XLocaleDistance.DistanceOption distanceOption;
    private final Map<XLikelySubtags.LSR, Set<ULocale>> supportedLanguages;
    private final Set<ULocale> exactSupportedLocales;
    private final ULocale defaultLanguage;

    public static Builder builder() {
        return new Builder();
    }

    public XLocaleMatcher(String supportedLocales) {
        this(XLocaleMatcher.builder().setSupportedLocales(supportedLocales));
    }

    public XLocaleMatcher(LocalePriorityList supportedLocales) {
        this(XLocaleMatcher.builder().setSupportedLocales(supportedLocales));
    }

    public XLocaleMatcher(Set<ULocale> supportedLocales) {
        this(XLocaleMatcher.builder().setSupportedLocales(supportedLocales));
    }

    private XLocaleMatcher(Builder builder) {
        this.localeDistance = builder.localeDistance == null ? XLocaleDistance.getDefault() : builder.localeDistance;
        this.thresholdDistance = builder.thresholdDistance < 0 ? this.localeDistance.getDefaultScriptDistance() : builder.thresholdDistance;
        Set<XLikelySubtags.LSR> paradigms = this.extractLsrSet(this.localeDistance.getParadigms());
        XCldrStub.Multimap<XLikelySubtags.LSR, ULocale> temp2 = this.extractLsrMap(builder.supportedLanguagesList, paradigms);
        this.supportedLanguages = temp2.asMap();
        this.exactSupportedLocales = XCldrStub.ImmutableSet.copyOf(temp2.values());
        this.defaultLanguage = builder.defaultLanguage != null ? builder.defaultLanguage : (this.supportedLanguages.isEmpty() ? null : this.supportedLanguages.entrySet().iterator().next().getValue().iterator().next());
        this.demotionPerAdditionalDesiredLocale = builder.demotionPerAdditionalDesiredLocale < 0 ? this.localeDistance.getDefaultRegionDistance() + 1 : builder.demotionPerAdditionalDesiredLocale;
        this.distanceOption = builder.distanceOption;
    }

    private Set<XLikelySubtags.LSR> extractLsrSet(Set<ULocale> languagePriorityList) {
        LinkedHashSet<XLikelySubtags.LSR> result = new LinkedHashSet<XLikelySubtags.LSR>();
        for (ULocale item : languagePriorityList) {
            XLikelySubtags.LSR max = item.equals(UND_LOCALE) ? UND : XLikelySubtags.LSR.fromMaximalized(item);
            result.add(max);
        }
        return result;
    }

    private XCldrStub.Multimap<XLikelySubtags.LSR, ULocale> extractLsrMap(Set<ULocale> languagePriorityList, Set<XLikelySubtags.LSR> priorities) {
        XCldrStub.LinkedHashMultimap<XLikelySubtags.LSR, ULocale> builder = XCldrStub.LinkedHashMultimap.create();
        for (ULocale item : languagePriorityList) {
            XLikelySubtags.LSR max = item.equals(UND_LOCALE) ? UND : XLikelySubtags.LSR.fromMaximalized(item);
            builder.put(max, item);
        }
        if (builder.size() > 1 && priorities != null) {
            XCldrStub.LinkedHashMultimap<XLikelySubtags.LSR, ULocale> builder2 = XCldrStub.LinkedHashMultimap.create();
            boolean first = true;
            for (Map.Entry entry : builder.asMap().entrySet()) {
                XLikelySubtags.LSR key = (XLikelySubtags.LSR)entry.getKey();
                if (!first && !priorities.contains(key)) continue;
                builder2.putAll(key, entry.getValue());
                first = false;
            }
            builder2.putAll(builder);
            if (!builder2.equals(builder)) {
                throw new IllegalArgumentException();
            }
            builder = builder2;
        }
        return XCldrStub.ImmutableMultimap.copyOf(builder);
    }

    public ULocale getBestMatch(ULocale ulocale) {
        return this.getBestMatch(ulocale, (Output<ULocale>)null);
    }

    public ULocale getBestMatch(String languageList) {
        return this.getBestMatch(LocalePriorityList.add(languageList).build(), null);
    }

    public ULocale getBestMatch(ULocale ... locales) {
        return this.getBestMatch(new LinkedHashSet<ULocale>(Arrays.asList(locales)), null);
    }

    public ULocale getBestMatch(Set<ULocale> desiredLanguages) {
        return this.getBestMatch(desiredLanguages, null);
    }

    public ULocale getBestMatch(LocalePriorityList desiredLanguages) {
        return this.getBestMatch(desiredLanguages, null);
    }

    public ULocale getBestMatch(LocalePriorityList desiredLanguages, Output<ULocale> outputBestDesired) {
        return this.getBestMatch(XLocaleMatcher.asSet(desiredLanguages), outputBestDesired);
    }

    private static Set<ULocale> asSet(LocalePriorityList languageList) {
        LinkedHashSet<ULocale> temp = new LinkedHashSet<ULocale>();
        for (ULocale locale : languageList) {
            temp.add(locale);
        }
        return temp;
    }

    public ULocale getBestMatch(Set<ULocale> desiredLanguages, Output<ULocale> outputBestDesired) {
        if (desiredLanguages.size() == 1) {
            return this.getBestMatch(desiredLanguages.iterator().next(), outputBestDesired);
        }
        XCldrStub.Multimap<XLikelySubtags.LSR, ULocale> desiredLSRs = this.extractLsrMap(desiredLanguages, null);
        int bestDistance = Integer.MAX_VALUE;
        ULocale bestDesiredLocale = null;
        Collection bestSupportedLocales = null;
        int delta = 0;
        block0: for (Map.Entry<XLikelySubtags.LSR, ULocale> desiredLsrAndLocale : desiredLSRs.entries()) {
            ULocale desiredLocale = desiredLsrAndLocale.getValue();
            XLikelySubtags.LSR desiredLSR = desiredLsrAndLocale.getKey();
            if (delta < bestDistance) {
                if (this.exactSupportedLocales.contains(desiredLocale)) {
                    if (outputBestDesired != null) {
                        outputBestDesired.value = desiredLocale;
                    }
                    return desiredLocale;
                }
                Collection found = this.supportedLanguages.get(desiredLSR);
                if (found != null) {
                    if (outputBestDesired != null) {
                        outputBestDesired.value = desiredLocale;
                    }
                    return (ULocale)found.iterator().next();
                }
            }
            for (Map.Entry<XLikelySubtags.LSR, Set<ULocale>> supportedLsrAndLocale : this.supportedLanguages.entrySet()) {
                int distance = delta + this.localeDistance.distanceRaw(desiredLSR, supportedLsrAndLocale.getKey(), this.thresholdDistance, this.distanceOption);
                if (distance >= bestDistance) continue;
                bestDistance = distance;
                bestDesiredLocale = desiredLocale;
                bestSupportedLocales = supportedLsrAndLocale.getValue();
                if (distance != 0) continue;
                break block0;
            }
            delta += this.demotionPerAdditionalDesiredLocale;
        }
        if (bestDistance >= this.thresholdDistance) {
            if (outputBestDesired != null) {
                outputBestDesired.value = null;
            }
            return this.defaultLanguage;
        }
        if (outputBestDesired != null) {
            outputBestDesired.value = bestDesiredLocale;
        }
        if (bestSupportedLocales.contains(bestDesiredLocale)) {
            return bestDesiredLocale;
        }
        return (ULocale)bestSupportedLocales.iterator().next();
    }

    public ULocale getBestMatch(ULocale desiredLocale, Output<ULocale> outputBestDesired) {
        Collection found;
        XLikelySubtags.LSR desiredLSR;
        int bestDistance = Integer.MAX_VALUE;
        ULocale bestDesiredLocale = null;
        Collection bestSupportedLocales = null;
        XLikelySubtags.LSR lSR = desiredLSR = desiredLocale.equals(UND_LOCALE) ? UND : XLikelySubtags.LSR.fromMaximalized(desiredLocale);
        if (this.exactSupportedLocales.contains(desiredLocale)) {
            if (outputBestDesired != null) {
                outputBestDesired.value = desiredLocale;
            }
            return desiredLocale;
        }
        if (this.distanceOption == XLocaleDistance.DistanceOption.NORMAL && (found = (Collection)this.supportedLanguages.get(desiredLSR)) != null) {
            if (outputBestDesired != null) {
                outputBestDesired.value = desiredLocale;
            }
            return (ULocale)found.iterator().next();
        }
        for (Map.Entry<XLikelySubtags.LSR, Set<ULocale>> supportedLsrAndLocale : this.supportedLanguages.entrySet()) {
            int distance = this.localeDistance.distanceRaw(desiredLSR, supportedLsrAndLocale.getKey(), this.thresholdDistance, this.distanceOption);
            if (distance >= bestDistance) continue;
            bestDistance = distance;
            bestDesiredLocale = desiredLocale;
            bestSupportedLocales = supportedLsrAndLocale.getValue();
            if (distance != 0) continue;
            break;
        }
        if (bestDistance >= this.thresholdDistance) {
            if (outputBestDesired != null) {
                outputBestDesired.value = null;
            }
            return this.defaultLanguage;
        }
        if (outputBestDesired != null) {
            outputBestDesired.value = bestDesiredLocale;
        }
        if (bestSupportedLocales.contains(bestDesiredLocale)) {
            return bestDesiredLocale;
        }
        return (ULocale)bestSupportedLocales.iterator().next();
    }

    public static ULocale combine(ULocale bestSupported, ULocale bestDesired) {
        if (!bestSupported.equals(bestDesired) && bestDesired != null) {
            String variants;
            ULocale.Builder b2 = new ULocale.Builder().setLocale(bestSupported);
            String region = bestDesired.getCountry();
            if (!region.isEmpty()) {
                b2.setRegion(region);
            }
            if (!(variants = bestDesired.getVariant()).isEmpty()) {
                b2.setVariant(variants);
            }
            for (char extensionKey : bestDesired.getExtensionKeys()) {
                b2.setExtension(extensionKey, bestDesired.getExtension(extensionKey));
            }
            bestSupported = b2.build();
        }
        return bestSupported;
    }

    public int distance(ULocale desired, ULocale supported) {
        return this.localeDistance.distanceRaw(XLikelySubtags.LSR.fromMaximalized(desired), XLikelySubtags.LSR.fromMaximalized(supported), this.thresholdDistance, this.distanceOption);
    }

    public int distance(String desiredLanguage, String supportedLanguage) {
        return this.localeDistance.distanceRaw(XLikelySubtags.LSR.fromMaximalized(new ULocale(desiredLanguage)), XLikelySubtags.LSR.fromMaximalized(new ULocale(supportedLanguage)), this.thresholdDistance, this.distanceOption);
    }

    public String toString() {
        return this.exactSupportedLocales.toString();
    }

    public double match(ULocale desired, ULocale supported) {
        return (double)(100 - this.distance(desired, supported)) / 100.0;
    }

    @Deprecated
    public double match(ULocale desired, ULocale desiredMax, ULocale supported, ULocale supportedMax) {
        return this.match(desired, supported);
    }

    public ULocale canonicalize(ULocale ulocale) {
        return null;
    }

    public int getThresholdDistance() {
        return this.thresholdDistance;
    }

    public static class Builder {
        private Set<ULocale> supportedLanguagesList;
        private int thresholdDistance = -1;
        private int demotionPerAdditionalDesiredLocale = -1;
        private ULocale defaultLanguage;
        private XLocaleDistance localeDistance;
        private XLocaleDistance.DistanceOption distanceOption;

        public Builder setSupportedLocales(String languagePriorityList) {
            this.supportedLanguagesList = XLocaleMatcher.asSet(LocalePriorityList.add(languagePriorityList).build());
            return this;
        }

        public Builder setSupportedLocales(LocalePriorityList languagePriorityList) {
            this.supportedLanguagesList = XLocaleMatcher.asSet(languagePriorityList);
            return this;
        }

        public Builder setSupportedLocales(Set<ULocale> languagePriorityList) {
            this.supportedLanguagesList = languagePriorityList;
            return this;
        }

        public Builder setThresholdDistance(int thresholdDistance) {
            this.thresholdDistance = thresholdDistance;
            return this;
        }

        public Builder setDemotionPerAdditionalDesiredLocale(int demotionPerAdditionalDesiredLocale) {
            this.demotionPerAdditionalDesiredLocale = demotionPerAdditionalDesiredLocale;
            return this;
        }

        public Builder setLocaleDistance(XLocaleDistance localeDistance) {
            this.localeDistance = localeDistance;
            return this;
        }

        public Builder setDefaultLanguage(ULocale defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
            return this;
        }

        public Builder setDistanceOption(XLocaleDistance.DistanceOption distanceOption) {
            this.distanceOption = distanceOption;
            return this;
        }

        public XLocaleMatcher build() {
            return new XLocaleMatcher(this);
        }
    }
}

