/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.locale;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.Row;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.locale.XCldrStub;
import com.ibm.icu.impl.locale.XLikelySubtags;
import com.ibm.icu.text.LocaleDisplayNames;
import com.ibm.icu.util.LocaleMatcher;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class XLocaleDistance {
    static final boolean PRINT_OVERRIDES = false;
    public static final int ABOVE_THRESHOLD = 100;
    @Deprecated
    public static final String ANY = "\ufffd";
    static final LocaleDisplayNames english = LocaleDisplayNames.getInstance(ULocale.ENGLISH);
    static final XCldrStub.Multimap<String, String> CONTAINER_TO_CONTAINED = XLocaleDistance.xGetContainment();
    static final XCldrStub.Multimap<String, String> CONTAINER_TO_CONTAINED_FINAL;
    private static final Set<String> ALL_FINAL_REGIONS;
    private final DistanceTable languageDesired2Supported;
    private final RegionMapper regionMapper;
    private final int defaultLanguageDistance;
    private final int defaultScriptDistance;
    private final int defaultRegionDistance;
    private static final XLocaleDistance DEFAULT;

    private static String fixAny(String string) {
        return "*".equals(string) ? ANY : string;
    }

    private static List<Row.R4<String, String, Integer, Boolean>> xGetLanguageMatcherData() {
        ArrayList<Row.R4> distanceList = new ArrayList<Row.R4>();
        ICUResourceBundle suppData = LocaleMatcher.getICUSupplementalData();
        ICUResourceBundle languageMatchingNew = suppData.findTopLevel("languageMatchingNew");
        ICUResourceBundle written = (ICUResourceBundle)languageMatchingNew.get("written");
        UResourceBundleIterator iter = written.getIterator();
        while (iter.hasNext()) {
            ICUResourceBundle item = (ICUResourceBundle)iter.next();
            boolean oneway = item.getSize() > 3 && "1".equals(item.getString(3));
            distanceList.add((Row.R4)Row.of(item.getString(0), item.getString(1), Integer.parseInt(item.getString(2)), oneway).freeze());
        }
        return Collections.unmodifiableList(distanceList);
    }

    private static Set<String> xGetParadigmLocales() {
        ICUResourceBundle suppData = LocaleMatcher.getICUSupplementalData();
        ICUResourceBundle languageMatchingInfo = suppData.findTopLevel("languageMatchingInfo");
        ICUResourceBundle writtenParadigmLocales = (ICUResourceBundle)languageMatchingInfo.get("written").get("paradigmLocales");
        HashSet<String> paradigmLocales = new HashSet<String>(Arrays.asList(writtenParadigmLocales.getStringArray()));
        return Collections.unmodifiableSet(paradigmLocales);
    }

    private static Map<String, String> xGetMatchVariables() {
        ICUResourceBundle suppData = LocaleMatcher.getICUSupplementalData();
        ICUResourceBundle languageMatchingInfo = suppData.findTopLevel("languageMatchingInfo");
        ICUResourceBundle writtenMatchVariables = (ICUResourceBundle)languageMatchingInfo.get("written").get("matchVariable");
        HashMap<String, String> matchVariables = new HashMap<String, String>();
        Enumeration<String> enumer = writtenMatchVariables.getKeys();
        while (enumer.hasMoreElements()) {
            String key = enumer.nextElement();
            matchVariables.put(key, writtenMatchVariables.getString(key));
        }
        return Collections.unmodifiableMap(matchVariables);
    }

    private static XCldrStub.Multimap<String, String> xGetContainment() {
        XCldrStub.TreeMultimap<String, String> containment = XCldrStub.TreeMultimap.create();
        containment.putAll("001", "019", "002", "150", "142", "009").putAll("011", (String[])new String[]{"BF", "BJ", "CI", "CV", "GH", "GM", "GN", "GW", "LR", "ML", "MR", "NE", "NG", "SH", "SL", "SN", "TG"}).putAll("013", (String[])new String[]{"BZ", "CR", "GT", "HN", "MX", "NI", "PA", "SV"}).putAll("014", (String[])new String[]{"BI", "DJ", "ER", "ET", "KE", "KM", "MG", "MU", "MW", "MZ", "RE", "RW", "SC", "SO", "SS", "TZ", "UG", "YT", "ZM", "ZW"}).putAll("142", (String[])new String[]{"145", "143", "030", "034", "035"}).putAll("143", (String[])new String[]{"TM", "TJ", "KG", "KZ", "UZ"}).putAll("145", (String[])new String[]{"AE", "AM", "AZ", "BH", "CY", "GE", "IL", "IQ", "JO", "KW", "LB", "OM", "PS", "QA", "SA", "SY", "TR", "YE", "NT", "YD"}).putAll("015", (String[])new String[]{"DZ", "EG", "EH", "LY", "MA", "SD", "TN", "EA", "IC"}).putAll("150", (String[])new String[]{"154", "155", "151", "039"}).putAll("151", (String[])new String[]{"BG", "BY", "CZ", "HU", "MD", "PL", "RO", "RU", "SK", "UA", "SU"}).putAll("154", (String[])new String[]{"GG", "IM", "JE", "AX", "DK", "EE", "FI", "FO", "GB", "IE", "IS", "LT", "LV", "NO", "SE", "SJ"}).putAll("155", (String[])new String[]{"AT", "BE", "CH", "DE", "FR", "LI", "LU", "MC", "NL", "DD", "FX"}).putAll("017", (String[])new String[]{"AO", "CD", "CF", "CG", "CM", "GA", "GQ", "ST", "TD", "ZR"}).putAll("018", (String[])new String[]{"BW", "LS", "NA", "SZ", "ZA"}).putAll("019", (String[])new String[]{"021", "013", "029", "005", "003", "419"}).putAll("002", (String[])new String[]{"015", "011", "017", "014", "018"}).putAll("021", (String[])new String[]{"BM", "CA", "GL", "PM", "US"}).putAll("029", (String[])new String[]{"AG", "AI", "AW", "BB", "BL", "BQ", "BS", "CU", "CW", "DM", "DO", "GD", "GP", "HT", "JM", "KN", "KY", "LC", "MF", "MQ", "MS", "PR", "SX", "TC", "TT", "VC", "VG", "VI", "AN"}).putAll("003", (String[])new String[]{"021", "013", "029"}).putAll("030", (String[])new String[]{"CN", "HK", "JP", "KP", "KR", "MN", "MO", "TW"}).putAll("035", (String[])new String[]{"BN", "ID", "KH", "LA", "MM", "MY", "PH", "SG", "TH", "TL", "VN", "BU", "TP"}).putAll("039", (String[])new String[]{"AD", "AL", "BA", "ES", "GI", "GR", "HR", "IT", "ME", "MK", "MT", "RS", "PT", "SI", "SM", "VA", "XK", "CS", "YU"}).putAll("419", (String[])new String[]{"013", "029", "005"}).putAll("005", (String[])new String[]{"AR", "BO", "BR", "CL", "CO", "EC", "FK", "GF", "GY", "PE", "PY", "SR", "UY", "VE"}).putAll("053", (String[])new String[]{"AU", "NF", "NZ"}).putAll("054", (String[])new String[]{"FJ", "NC", "PG", "SB", "VU"}).putAll("057", (String[])new String[]{"FM", "GU", "KI", "MH", "MP", "NR", "PW"}).putAll("061", (String[])new String[]{"AS", "CK", "NU", "PF", "PN", "TK", "TO", "TV", "WF", "WS"}).putAll("034", (String[])new String[]{"AF", "BD", "BT", "IN", "IR", "LK", "MV", "NP", "PK"}).putAll("009", (String[])new String[]{"053", "054", "057", "061", "QO"}).putAll("QO", (String[])new String[]{"AQ", "BV", "CC", "CX", "GS", "HM", "IO", "TF", "UM", "AC", "CP", "DG", "TA"});
        XCldrStub.TreeMultimap<String, String> containmentResolved = XCldrStub.TreeMultimap.create();
        XLocaleDistance.fill("001", containment, containmentResolved);
        return XCldrStub.ImmutableMultimap.copyOf(containmentResolved);
    }

    private static Set<String> fill(String region, XCldrStub.TreeMultimap<String, String> containment, XCldrStub.Multimap<String, String> toAddTo) {
        Set contained = containment.get(region);
        if (contained == null) {
            return Collections.emptySet();
        }
        toAddTo.putAll(region, contained);
        for (String subregion : contained) {
            toAddTo.putAll(region, (Collection<String>)XLocaleDistance.fill(subregion, containment, toAddTo));
        }
        return toAddTo.get(region);
    }

    public XLocaleDistance(DistanceTable datadistancetable2, RegionMapper regionMapper) {
        this.languageDesired2Supported = datadistancetable2;
        this.regionMapper = regionMapper;
        StringDistanceNode languageNode = (StringDistanceNode)((StringDistanceTable)this.languageDesired2Supported).subtables.get(ANY).get(ANY);
        this.defaultLanguageDistance = languageNode.distance;
        StringDistanceNode scriptNode = (StringDistanceNode)((StringDistanceTable)languageNode.distanceTable).subtables.get(ANY).get(ANY);
        this.defaultScriptDistance = scriptNode.distance;
        DistanceNode regionNode = ((StringDistanceTable)scriptNode.distanceTable).subtables.get(ANY).get(ANY);
        this.defaultRegionDistance = regionNode.distance;
    }

    private static Map newMap() {
        return new TreeMap();
    }

    public int distance(ULocale desired, ULocale supported, int threshold, DistanceOption distanceOption) {
        XLikelySubtags.LSR supportedLSR = XLikelySubtags.LSR.fromMaximalized(supported);
        XLikelySubtags.LSR desiredLSR = XLikelySubtags.LSR.fromMaximalized(desired);
        return this.distanceRaw(desiredLSR, supportedLSR, threshold, distanceOption);
    }

    public int distanceRaw(XLikelySubtags.LSR desired, XLikelySubtags.LSR supported, int threshold, DistanceOption distanceOption) {
        return this.distanceRaw(desired.language, supported.language, desired.script, supported.script, desired.region, supported.region, threshold, distanceOption);
    }

    public int distanceRaw(String desiredLang, String supportedlang, String desiredScript, String supportedScript, String desiredRegion, String supportedRegion, int threshold, DistanceOption distanceOption) {
        int subdistance;
        Set<String> supportedPartitions;
        boolean scriptFirst;
        Output<DistanceTable> subtable = new Output<DistanceTable>();
        int distance = this.languageDesired2Supported.getDistance(desiredLang, supportedlang, subtable, true);
        boolean bl = scriptFirst = distanceOption == DistanceOption.SCRIPT_FIRST;
        if (scriptFirst) {
            distance >>= 2;
        }
        if (distance < 0) {
            distance = 0;
        } else if (distance >= threshold) {
            return 100;
        }
        int scriptDistance = ((DistanceTable)subtable.value).getDistance(desiredScript, supportedScript, subtable, true);
        if (scriptFirst) {
            scriptDistance >>= 1;
        }
        if ((distance += scriptDistance) >= threshold) {
            return 100;
        }
        if (desiredRegion.equals(supportedRegion)) {
            return distance;
        }
        String desiredPartition = this.regionMapper.toId(desiredRegion);
        String supportedPartition = this.regionMapper.toId(supportedRegion);
        Set<String> desiredPartitions = desiredPartition.isEmpty() ? this.regionMapper.macroToPartitions.get(desiredRegion) : null;
        Set<String> set = supportedPartitions = supportedPartition.isEmpty() ? this.regionMapper.macroToPartitions.get(supportedRegion) : null;
        if (desiredPartitions != null || supportedPartitions != null) {
            subdistance = 0;
            if (desiredPartitions == null) {
                desiredPartitions = Collections.singleton(desiredPartition);
            }
            if (supportedPartitions == null) {
                supportedPartitions = Collections.singleton(supportedPartition);
            }
            for (String desiredPartition2 : desiredPartitions) {
                for (String supportedPartition2 : supportedPartitions) {
                    int tempSubdistance = ((DistanceTable)subtable.value).getDistance(desiredPartition2, supportedPartition2, null, false);
                    if (subdistance >= tempSubdistance) continue;
                    subdistance = tempSubdistance;
                }
            }
        } else {
            subdistance = ((DistanceTable)subtable.value).getDistance(desiredPartition, supportedPartition, null, false);
        }
        return (distance += subdistance) >= threshold ? 100 : distance;
    }

    public static XLocaleDistance getDefault() {
        return DEFAULT;
    }

    private static void printMatchXml(List<String> desired, List<String> supported, Integer distance, Boolean oneway) {
    }

    private static String fixedName(List<String> match) {
        ArrayList<String> alt = new ArrayList<String>(match);
        int size = alt.size();
        assert (size >= 1 && size <= 3);
        StringBuilder result = new StringBuilder();
        if (size >= 3) {
            String region = (String)alt.get(2);
            if (region.equals("*") || region.startsWith("$")) {
                result.append(region);
            } else {
                result.append(english.regionDisplayName(region));
            }
        }
        if (size >= 2) {
            String script = (String)alt.get(1);
            if (script.equals("*")) {
                result.insert(0, script);
            } else {
                result.insert(0, english.scriptDisplayName(script));
            }
        }
        if (size >= 1) {
            String language = (String)alt.get(0);
            if (language.equals("*")) {
                result.insert(0, language);
            } else {
                result.insert(0, english.languageDisplayName(language));
            }
        }
        return XCldrStub.CollectionUtilities.join(alt, "; ");
    }

    public static void add(StringDistanceTable languageDesired2Supported, List<String> desired, List<String> supported, int percentage) {
        int size = desired.size();
        if (size != supported.size() || size < 1 || size > 3) {
            throw new IllegalArgumentException();
        }
        String desiredLang = XLocaleDistance.fixAny(desired.get(0));
        String supportedLang = XLocaleDistance.fixAny(supported.get(0));
        if (size == 1) {
            languageDesired2Supported.addSubtable(desiredLang, supportedLang, percentage);
        } else {
            String desiredScript = XLocaleDistance.fixAny(desired.get(1));
            String supportedScript = XLocaleDistance.fixAny(supported.get(1));
            if (size == 2) {
                languageDesired2Supported.addSubtables(desiredLang, supportedLang, desiredScript, supportedScript, percentage);
            } else {
                String desiredRegion = XLocaleDistance.fixAny(desired.get(2));
                String supportedRegion = XLocaleDistance.fixAny(supported.get(2));
                languageDesired2Supported.addSubtables(desiredLang, supportedLang, desiredScript, supportedScript, desiredRegion, supportedRegion, percentage);
            }
        }
    }

    public String toString() {
        return this.toString(false);
    }

    public String toString(boolean abbreviate) {
        return this.regionMapper + "\n" + this.languageDesired2Supported.toString(abbreviate);
    }

    static Set<String> getContainingMacrosFor(Collection<String> input, Set<String> output) {
        output.clear();
        for (Map.Entry<String, Set<String>> entry : CONTAINER_TO_CONTAINED.asMap().entrySet()) {
            if (!input.containsAll((Collection)entry.getValue())) continue;
            output.add(entry.getKey());
        }
        return output;
    }

    public static <K, V> XCldrStub.Multimap<K, V> invertMap(Map<V, K> map) {
        return XCldrStub.Multimaps.invertFrom(XCldrStub.Multimaps.forMap(map), XCldrStub.LinkedHashMultimap.create());
    }

    public Set<ULocale> getParadigms() {
        return this.regionMapper.paradigms;
    }

    public int getDefaultLanguageDistance() {
        return this.defaultLanguageDistance;
    }

    public int getDefaultScriptDistance() {
        return this.defaultScriptDistance;
    }

    public int getDefaultRegionDistance() {
        return this.defaultRegionDistance;
    }

    @Deprecated
    public StringDistanceTable internalGetDistanceTable() {
        return (StringDistanceTable)this.languageDesired2Supported;
    }

    public static void main(String[] args) {
        DistanceTable table = XLocaleDistance.getDefault().languageDesired2Supported;
        DistanceTable compactedTable = table.compact();
        if (!table.equals(compactedTable)) {
            throw new IllegalArgumentException("Compaction isn't equal");
        }
    }

    static {
        XCldrStub.TreeMultimap<String, String> containerToFinalContainedBuilder = XCldrStub.TreeMultimap.create();
        for (Map.Entry<String, Set<String>> entry : CONTAINER_TO_CONTAINED.asMap().entrySet()) {
            String container = entry.getKey();
            for (String contained : entry.getValue()) {
                if (CONTAINER_TO_CONTAINED.get(contained) != null) continue;
                containerToFinalContainedBuilder.put(container, contained);
            }
        }
        CONTAINER_TO_CONTAINED_FINAL = XCldrStub.ImmutableMultimap.copyOf(containerToFinalContainedBuilder);
        ALL_FINAL_REGIONS = XCldrStub.ImmutableSet.copyOf(CONTAINER_TO_CONTAINED_FINAL.get("001"));
        String[][] variableOverrides = new String[][]{{"$enUS", "AS+GU+MH+MP+PR+UM+US+VI"}, {"$cnsar", "HK+MO"}, {"$americas", "019"}, {"$maghreb", "MA+DZ+TN+LY+MR+EH"}};
        String[] paradigmRegions = new String[]{"en", "en-GB", "es", "es-419", "pt-BR", "pt-PT"};
        String[][] regionRuleOverrides = new String[][]{{"ar_*_$maghreb", "ar_*_$maghreb", "96"}, {"ar_*_$!maghreb", "ar_*_$!maghreb", "96"}, {"ar_*_*", "ar_*_*", "95"}, {"en_*_$enUS", "en_*_$enUS", "96"}, {"en_*_$!enUS", "en_*_$!enUS", "96"}, {"en_*_*", "en_*_*", "95"}, {"es_*_$americas", "es_*_$americas", "96"}, {"es_*_$!americas", "es_*_$!americas", "96"}, {"es_*_*", "es_*_*", "95"}, {"pt_*_$americas", "pt_*_$americas", "96"}, {"pt_*_$!americas", "pt_*_$!americas", "96"}, {"pt_*_*", "pt_*_*", "95"}, {"zh_Hant_$cnsar", "zh_Hant_$cnsar", "96"}, {"zh_Hant_$!cnsar", "zh_Hant_$!cnsar", "96"}, {"zh_Hant_*", "zh_Hant_*", "95"}, {"*_*_*", "*_*_*", "96"}};
        RegionMapper.Builder rmb = new RegionMapper.Builder().addParadigms(paradigmRegions);
        for (String[] variableRule : variableOverrides) {
            rmb.add(variableRule[0], variableRule[1]);
        }
        StringDistanceTable defaultDistanceTable = new StringDistanceTable();
        RegionMapper defaultRegionMapper = rmb.build();
        XCldrStub.Splitter bar = XCldrStub.Splitter.on('_');
        ArrayList[] sorted = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList()};
        for (Row.R4<String, String, Integer, Boolean> info : XLocaleDistance.xGetLanguageMatcherData()) {
            String desiredRaw = (String)info.get0();
            String string = (String)info.get1();
            List<String> desired = bar.splitToList(desiredRaw);
            List<String> supported = bar.splitToList(string);
            Boolean oneway = (Boolean)info.get3();
            int distance = desiredRaw.equals("*_*") ? 50 : (Integer)info.get2();
            int size = desired.size();
            if (size == 3) continue;
            sorted[size - 1].add(Row.of(desired, supported, distance, oneway));
        }
        for (ArrayList arrayList : sorted) {
            for (Row.R4 item2 : arrayList) {
                List desired = (List)item2.get0();
                List supported = (List)item2.get1();
                Integer distance = (Integer)item2.get2();
                Boolean oneway = (Boolean)item2.get3();
                XLocaleDistance.add(defaultDistanceTable, desired, supported, distance);
                if (oneway != Boolean.TRUE && !desired.equals(supported)) {
                    XLocaleDistance.add(defaultDistanceTable, supported, desired, distance);
                }
                XLocaleDistance.printMatchXml(desired, supported, distance, oneway);
            }
        }
        for (ArrayList arrayList : regionRuleOverrides) {
            ArrayList<String> desiredBase = new ArrayList<String>(bar.splitToList((String)((Object)arrayList[0])));
            ArrayList<String> supportedBase = new ArrayList<String>(bar.splitToList((String)((Object)arrayList[1])));
            Integer distance = 100 - Integer.parseInt((String)((Object)arrayList[2]));
            XLocaleDistance.printMatchXml(desiredBase, supportedBase, distance, false);
            Collection<String> desiredRegions = defaultRegionMapper.getIdsFromVariable((String)desiredBase.get(2));
            if (desiredRegions.isEmpty()) {
                throw new IllegalArgumentException("Bad region variable: " + (String)desiredBase.get(2));
            }
            Collection<String> supportedRegions = defaultRegionMapper.getIdsFromVariable((String)supportedBase.get(2));
            if (supportedRegions.isEmpty()) {
                throw new IllegalArgumentException("Bad region variable: " + (String)supportedBase.get(2));
            }
            for (String desiredRegion2 : desiredRegions) {
                desiredBase.set(2, desiredRegion2.toString());
                for (String supportedRegion2 : supportedRegions) {
                    supportedBase.set(2, supportedRegion2.toString());
                    XLocaleDistance.add(defaultDistanceTable, desiredBase, supportedBase, distance);
                    XLocaleDistance.add(defaultDistanceTable, supportedBase, desiredBase, distance);
                }
            }
        }
        DEFAULT = new XLocaleDistance(defaultDistanceTable.compact(), defaultRegionMapper);
    }

    static class CompactAndImmutablizer
    extends IdMakerFull<Object> {
        CompactAndImmutablizer() {
        }

        StringDistanceTable compact(StringDistanceTable item) {
            if (this.toId(item) != null) {
                return this.intern(item);
            }
            return new StringDistanceTable(this.compact(item.subtables, 0));
        }

        <K, T> Map<K, T> compact(Map<K, T> item, int level) {
            if (this.toId(item) != null) {
                return this.intern(item);
            }
            LinkedHashMap<K, Object> copy = new LinkedHashMap<K, Object>();
            for (Map.Entry<K, T> entry : item.entrySet()) {
                T value = entry.getValue();
                if (value instanceof Map) {
                    copy.put(entry.getKey(), this.compact((Map)value, level + 1));
                    continue;
                }
                copy.put(entry.getKey(), this.compact((DistanceNode)value));
            }
            return XCldrStub.ImmutableMap.copyOf(copy);
        }

        DistanceNode compact(DistanceNode item) {
            if (this.toId(item) != null) {
                return this.intern(item);
            }
            DistanceTable distanceTable = item.getDistanceTable();
            if (distanceTable == null || distanceTable.isEmpty()) {
                return new DistanceNode(item.distance);
            }
            return new StringDistanceNode(item.distance, this.compact((StringDistanceTable)((StringDistanceNode)item).distanceTable));
        }
    }

    private static class RegionSet {
        private final Set<String> tempRegions = new TreeSet<String>();
        private Operation operation = null;

        private RegionSet() {
        }

        private Set<String> parseSet(String barString) {
            int i2;
            this.operation = Operation.add;
            int last = 0;
            this.tempRegions.clear();
            block4: for (i2 = 0; i2 < barString.length(); ++i2) {
                char c2 = barString.charAt(i2);
                switch (c2) {
                    case '+': {
                        this.add(barString, last, i2);
                        last = i2 + 1;
                        this.operation = Operation.add;
                        continue block4;
                    }
                    case '-': {
                        this.add(barString, last, i2);
                        last = i2 + 1;
                        this.operation = Operation.remove;
                    }
                }
            }
            this.add(barString, last, i2);
            return this.tempRegions;
        }

        private Set<String> inverse() {
            TreeSet<String> result = new TreeSet<String>(ALL_FINAL_REGIONS);
            result.removeAll(this.tempRegions);
            return result;
        }

        private void add(String barString, int last, int i2) {
            if (i2 > last) {
                String region = barString.substring(last, i2);
                this.changeSet(this.operation, region);
            }
        }

        private void changeSet(Operation operation, String region) {
            Set<String> contained = CONTAINER_TO_CONTAINED_FINAL.get(region);
            if (contained != null && !contained.isEmpty()) {
                if (Operation.add == operation) {
                    this.tempRegions.addAll(contained);
                } else {
                    this.tempRegions.removeAll(contained);
                }
            } else if (Operation.add == operation) {
                this.tempRegions.add(region);
            } else {
                this.tempRegions.remove(region);
            }
        }

        private static enum Operation {
            add,
            remove;

        }
    }

    static class RegionMapper
    implements IdMapper<String, String> {
        final XCldrStub.Multimap<String, String> variableToPartition;
        final Map<String, String> regionToPartition;
        final XCldrStub.Multimap<String, String> macroToPartitions;
        final Set<ULocale> paradigms;

        private RegionMapper(XCldrStub.Multimap<String, String> variableToPartitionIn, Map<String, String> regionToPartitionIn, XCldrStub.Multimap<String, String> macroToPartitionsIn, Set<ULocale> paradigmsIn) {
            this.variableToPartition = XCldrStub.ImmutableMultimap.copyOf(variableToPartitionIn);
            this.regionToPartition = XCldrStub.ImmutableMap.copyOf(regionToPartitionIn);
            this.macroToPartitions = XCldrStub.ImmutableMultimap.copyOf(macroToPartitionsIn);
            this.paradigms = XCldrStub.ImmutableSet.copyOf(paradigmsIn);
        }

        @Override
        public String toId(String region) {
            String result = this.regionToPartition.get(region);
            return result == null ? "" : result;
        }

        public Collection<String> getIdsFromVariable(String variable) {
            if (variable.equals("*")) {
                return Collections.singleton("*");
            }
            Set<String> result = this.variableToPartition.get(variable);
            if (result == null || result.isEmpty()) {
                throw new IllegalArgumentException("Variable not defined: " + variable);
            }
            return result;
        }

        public Set<String> regions() {
            return this.regionToPartition.keySet();
        }

        public Set<String> variables() {
            return this.variableToPartition.keySet();
        }

        public String toString() {
            XCldrStub.TreeMultimap partitionToVariables = XCldrStub.Multimaps.invertFrom(this.variableToPartition, XCldrStub.TreeMultimap.create());
            XCldrStub.TreeMultimap<String, String> partitionToRegions = XCldrStub.TreeMultimap.create();
            for (Map.Entry<String, String> e2 : this.regionToPartition.entrySet()) {
                partitionToRegions.put(e2.getValue(), e2.getKey());
            }
            StringBuilder buffer = new StringBuilder();
            buffer.append("Partition \u27a0 Variables \u27a0 Regions (final)");
            for (Map.Entry entry : partitionToVariables.asMap().entrySet()) {
                buffer.append('\n');
                buffer.append((String)entry.getKey() + "\t" + entry.getValue() + "\t" + partitionToRegions.get((String)entry.getKey()));
            }
            buffer.append("\nMacro \u27a0 Partitions");
            for (Map.Entry<Object, Set<Object>> entry : this.macroToPartitions.asMap().entrySet()) {
                buffer.append('\n');
                buffer.append((String)entry.getKey() + "\t" + entry.getValue());
            }
            return buffer.toString();
        }

        static class Builder {
            private final XCldrStub.Multimap<String, String> regionToRawPartition = XCldrStub.TreeMultimap.create();
            private final RegionSet regionSet = new RegionSet();
            private final Set<ULocale> paradigms = new LinkedHashSet<ULocale>();

            Builder() {
            }

            void add(String variable, String barString) {
                Set tempRegions = this.regionSet.parseSet(barString);
                for (String region : tempRegions) {
                    this.regionToRawPartition.put(region, variable);
                }
                Set inverse = this.regionSet.inverse();
                String inverseVariable = "$!" + variable.substring(1);
                for (String region : inverse) {
                    this.regionToRawPartition.put(region, inverseVariable);
                }
            }

            public Builder addParadigms(String ... paradigmRegions) {
                for (String paradigm : paradigmRegions) {
                    this.paradigms.add(new ULocale(paradigm));
                }
                return this;
            }

            RegionMapper build() {
                IdMakerFull<Collection> id = new IdMakerFull<Collection>("partition");
                XCldrStub.TreeMultimap<String, String> variableToPartitions = XCldrStub.TreeMultimap.create();
                TreeMap<String, String> regionToPartition = new TreeMap<String, String>();
                XCldrStub.TreeMultimap<String, String> partitionToRegions = XCldrStub.TreeMultimap.create();
                for (Map.Entry<String, Set<String>> e2 : this.regionToRawPartition.asMap().entrySet()) {
                    String region = e2.getKey();
                    Collection rawPartition = e2.getValue();
                    String partition = String.valueOf((char)(945 + id.add(rawPartition)));
                    regionToPartition.put(region, partition);
                    partitionToRegions.put(partition, region);
                    for (String variable : rawPartition) {
                        variableToPartitions.put(variable, partition);
                    }
                }
                XCldrStub.TreeMultimap<String, String> macroToPartitions = XCldrStub.TreeMultimap.create();
                for (Map.Entry<String, Set<String>> e3 : CONTAINER_TO_CONTAINED.asMap().entrySet()) {
                    String macro = e3.getKey();
                    for (Map.Entry e2 : partitionToRegions.asMap().entrySet()) {
                        String partition = (String)e2.getKey();
                        if (Collections.disjoint((Collection)e3.getValue(), e2.getValue())) continue;
                        macroToPartitions.put(macro, partition);
                    }
                }
                return new RegionMapper(variableToPartitions, regionToPartition, macroToPartitions, this.paradigms);
            }
        }
    }

    public static enum DistanceOption {
        NORMAL,
        SCRIPT_FIRST;

    }

    static class AddSub
    implements XCldrStub.Predicate<DistanceNode> {
        private final String desiredSub;
        private final String supportedSub;
        private final CopyIfEmpty r;

        AddSub(String desiredSub, String supportedSub, StringDistanceTable distanceTableToCopy) {
            this.r = new CopyIfEmpty(distanceTableToCopy);
            this.desiredSub = desiredSub;
            this.supportedSub = supportedSub;
        }

        @Override
        public boolean test(DistanceNode node) {
            if (node == null) {
                throw new IllegalArgumentException("bad structure");
            }
            ((StringDistanceNode)node).addSubtables(this.desiredSub, this.supportedSub, this.r);
            return true;
        }
    }

    static class CopyIfEmpty
    implements XCldrStub.Predicate<DistanceNode> {
        private final StringDistanceTable toCopy;

        CopyIfEmpty(StringDistanceTable resetIfNotNull) {
            this.toCopy = resetIfNotNull;
        }

        @Override
        public boolean test(DistanceNode node) {
            StringDistanceTable subtables = (StringDistanceTable)node.getDistanceTable();
            if (subtables.subtables.isEmpty()) {
                subtables.copy(this.toCopy);
            }
            return true;
        }
    }

    @Deprecated
    public static class StringDistanceTable
    extends DistanceTable {
        final Map<String, Map<String, DistanceNode>> subtables;

        StringDistanceTable(Map<String, Map<String, DistanceNode>> tables) {
            this.subtables = tables;
        }

        StringDistanceTable() {
            this(XLocaleDistance.newMap());
        }

        @Override
        public boolean isEmpty() {
            return this.subtables.isEmpty();
        }

        public boolean equals(Object obj) {
            return this == obj || obj != null && obj.getClass() == this.getClass() && this.subtables.equals(((StringDistanceTable)obj).subtables);
        }

        public int hashCode() {
            return this.subtables.hashCode();
        }

        @Override
        public int getDistance(String desired, String supported, Output<DistanceTable> distanceTable, boolean starEquals) {
            DistanceNode value;
            boolean star = false;
            Map<String, DistanceNode> sub2 = this.subtables.get(desired);
            if (sub2 == null) {
                sub2 = this.subtables.get(XLocaleDistance.ANY);
                star = true;
            }
            if ((value = sub2.get(supported)) == null) {
                value = sub2.get(XLocaleDistance.ANY);
                if (value == null && !star && (value = (sub2 = this.subtables.get(XLocaleDistance.ANY)).get(supported)) == null) {
                    value = sub2.get(XLocaleDistance.ANY);
                }
                star = true;
            }
            if (distanceTable != null) {
                distanceTable.value = ((StringDistanceNode)value).distanceTable;
            }
            return starEquals && star && desired.equals(supported) ? 0 : value.distance;
        }

        public void copy(StringDistanceTable other) {
            for (Map.Entry<String, Map<String, DistanceNode>> e1 : other.subtables.entrySet()) {
                for (Map.Entry<String, DistanceNode> e2 : e1.getValue().entrySet()) {
                    DistanceNode value = e2.getValue();
                    DistanceNode distanceNode = this.addSubtable(e1.getKey(), e2.getKey(), value.distance);
                }
            }
        }

        DistanceNode addSubtable(String desired, String supported, int distance) {
            DistanceNode oldNode;
            Map sub2 = this.subtables.get(desired);
            if (sub2 == null) {
                sub2 = XLocaleDistance.newMap();
                this.subtables.put(desired, sub2);
            }
            if ((oldNode = sub2.get(supported)) != null) {
                return oldNode;
            }
            StringDistanceNode newNode = new StringDistanceNode(distance);
            sub2.put(supported, newNode);
            return newNode;
        }

        private DistanceNode getNode(String desired, String supported) {
            Map<String, DistanceNode> sub2 = this.subtables.get(desired);
            if (sub2 == null) {
                return null;
            }
            return sub2.get(supported);
        }

        public void addSubtables(String desired, String supported, XCldrStub.Predicate<DistanceNode> action) {
            DistanceNode node = this.getNode(desired, supported);
            if (node == null) {
                Output<DistanceTable> node2 = new Output<DistanceTable>();
                int distance = this.getDistance(desired, supported, node2, true);
                node = this.addSubtable(desired, supported, distance);
                if (node2.value != null) {
                    ((StringDistanceNode)node).copyTables((StringDistanceTable)node2.value);
                }
            }
            action.test(node);
        }

        public void addSubtables(String desiredLang, String supportedLang, String desiredScript, String supportedScript, int percentage) {
            boolean haveKeys = false;
            for (Map.Entry<String, Map<String, DistanceNode>> e1 : this.subtables.entrySet()) {
                String key1 = e1.getKey();
                boolean desiredIsKey = desiredLang.equals(key1);
                if (!desiredIsKey && !desiredLang.equals(XLocaleDistance.ANY)) continue;
                for (Map.Entry<String, DistanceNode> e2 : e1.getValue().entrySet()) {
                    String key2 = e2.getKey();
                    boolean supportedIsKey = supportedLang.equals(key2);
                    haveKeys |= desiredIsKey && supportedIsKey;
                    if (!supportedIsKey && !supportedLang.equals(XLocaleDistance.ANY)) continue;
                    DistanceNode value = e2.getValue();
                    ((StringDistanceTable)value.getDistanceTable()).addSubtable(desiredScript, supportedScript, percentage);
                }
            }
            StringDistanceTable dt = new StringDistanceTable();
            dt.addSubtable(desiredScript, supportedScript, percentage);
            CopyIfEmpty r2 = new CopyIfEmpty(dt);
            this.addSubtables(desiredLang, supportedLang, r2);
        }

        public void addSubtables(String desiredLang, String supportedLang, String desiredScript, String supportedScript, String desiredRegion, String supportedRegion, int percentage) {
            boolean haveKeys = false;
            for (Map.Entry<String, Map<String, DistanceNode>> e1 : this.subtables.entrySet()) {
                String key1 = e1.getKey();
                boolean desiredIsKey = desiredLang.equals(key1);
                if (!desiredIsKey && !desiredLang.equals(XLocaleDistance.ANY)) continue;
                for (Map.Entry<String, DistanceNode> e2 : e1.getValue().entrySet()) {
                    String key2 = e2.getKey();
                    boolean supportedIsKey = supportedLang.equals(key2);
                    haveKeys |= desiredIsKey && supportedIsKey;
                    if (!supportedIsKey && !supportedLang.equals(XLocaleDistance.ANY)) continue;
                    StringDistanceNode value = (StringDistanceNode)e2.getValue();
                    ((StringDistanceTable)value.distanceTable).addSubtables(desiredScript, supportedScript, desiredRegion, supportedRegion, percentage);
                }
            }
            StringDistanceTable dt = new StringDistanceTable();
            dt.addSubtable(desiredRegion, supportedRegion, percentage);
            AddSub r2 = new AddSub(desiredScript, supportedScript, dt);
            this.addSubtables(desiredLang, supportedLang, r2);
        }

        public String toString() {
            return this.toString(false);
        }

        @Override
        public String toString(boolean abbreviate) {
            return this.toString(abbreviate, "", new IdMakerFull<Object>("interner"), new StringBuilder()).toString();
        }

        public StringBuilder toString(boolean abbreviate, String indent, IdMakerFull<Object> intern, StringBuilder buffer) {
            Integer id;
            String indent2 = indent.isEmpty() ? "" : "\t";
            Integer n2 = id = abbreviate ? intern.getOldAndAdd(this.subtables) : null;
            if (id != null) {
                buffer.append(indent2).append('#').append(id).append('\n');
            } else {
                for (Map.Entry<String, Map<String, DistanceNode>> e1 : this.subtables.entrySet()) {
                    Map<String, DistanceNode> subsubtable = e1.getValue();
                    buffer.append(indent2).append(e1.getKey());
                    String indent3 = "\t";
                    Integer n3 = id = abbreviate ? intern.getOldAndAdd(subsubtable) : null;
                    if (id != null) {
                        buffer.append(indent3).append('#').append(id).append('\n');
                    } else {
                        for (Map.Entry<String, DistanceNode> e2 : subsubtable.entrySet()) {
                            DistanceNode value = e2.getValue();
                            buffer.append(indent3).append(e2.getKey());
                            Integer n4 = id = abbreviate ? intern.getOldAndAdd(value) : null;
                            if (id != null) {
                                buffer.append('\t').append('#').append(id).append('\n');
                            } else {
                                buffer.append('\t').append(value.distance);
                                DistanceTable distanceTable = value.getDistanceTable();
                                if (distanceTable != null) {
                                    Integer n5 = id = abbreviate ? intern.getOldAndAdd(distanceTable) : null;
                                    if (id != null) {
                                        buffer.append('\t').append('#').append(id).append('\n');
                                    } else {
                                        ((StringDistanceTable)distanceTable).toString(abbreviate, indent + "\t\t\t", intern, buffer);
                                    }
                                } else {
                                    buffer.append('\n');
                                }
                            }
                            indent3 = indent + '\t';
                        }
                    }
                    indent2 = indent;
                }
            }
            return buffer;
        }

        @Override
        public StringDistanceTable compact() {
            return new CompactAndImmutablizer().compact(this);
        }

        @Override
        public Set<String> getCloser(int threshold) {
            HashSet<String> result = new HashSet<String>();
            block0: for (Map.Entry<String, Map<String, DistanceNode>> e1 : this.subtables.entrySet()) {
                String desired = e1.getKey();
                for (Map.Entry<String, DistanceNode> e2 : e1.getValue().entrySet()) {
                    if (e2.getValue().distance >= threshold) continue;
                    result.add(desired);
                    continue block0;
                }
            }
            return result;
        }

        public Integer getInternalDistance(String a2, String b2) {
            Map<String, DistanceNode> subsub = this.subtables.get(a2);
            if (subsub == null) {
                return null;
            }
            DistanceNode dnode = subsub.get(b2);
            return dnode == null ? null : Integer.valueOf(dnode.distance);
        }

        @Override
        public DistanceNode getInternalNode(String a2, String b2) {
            Map<String, DistanceNode> subsub = this.subtables.get(a2);
            if (subsub == null) {
                return null;
            }
            return subsub.get(b2);
        }

        @Override
        public Map<String, Set<String>> getInternalMatches() {
            LinkedHashMap<String, Set<String>> result = new LinkedHashMap<String, Set<String>>();
            for (Map.Entry<String, Map<String, DistanceNode>> entry : this.subtables.entrySet()) {
                result.put(entry.getKey(), new LinkedHashSet<String>(entry.getValue().keySet()));
            }
            return result;
        }
    }

    static class StringDistanceNode
    extends DistanceNode {
        final DistanceTable distanceTable;

        public StringDistanceNode(int distance, DistanceTable distanceTable) {
            super(distance);
            this.distanceTable = distanceTable;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (obj.getClass() != this.getClass()) return false;
            StringDistanceNode other = (StringDistanceNode)obj;
            if (this.distance != other.distance) return false;
            if (!Utility.equals(this.distanceTable, other.distanceTable)) return false;
            if (!super.equals(other)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return this.distance ^ Utility.hashCode(this.distanceTable);
        }

        StringDistanceNode(int distance) {
            this(distance, new StringDistanceTable());
        }

        public void addSubtables(String desiredSub, String supportedSub, CopyIfEmpty r2) {
            ((StringDistanceTable)this.distanceTable).addSubtables(desiredSub, supportedSub, r2);
        }

        @Override
        public String toString() {
            return "distance: " + this.distance + "\n" + this.distanceTable;
        }

        public void copyTables(StringDistanceTable value) {
            if (value != null) {
                ((StringDistanceTable)this.distanceTable).copy(value);
            }
        }

        @Override
        public DistanceTable getDistanceTable() {
            return this.distanceTable;
        }
    }

    static class IdMakerFull<T>
    implements IdMapper<T, Integer> {
        private final Map<T, Integer> objectToInt = new HashMap<T, Integer>();
        private final List<T> intToObject = new ArrayList<T>();
        final String name;

        IdMakerFull(String name) {
            this.name = name;
        }

        IdMakerFull() {
            this("unnamed");
        }

        IdMakerFull(String name, T zeroValue) {
            this(name);
            this.add(zeroValue);
        }

        public Integer add(T source) {
            Integer result = this.objectToInt.get(source);
            if (result == null) {
                Integer newResult = this.intToObject.size();
                this.objectToInt.put(source, newResult);
                this.intToObject.add(source);
                return newResult;
            }
            return result;
        }

        @Override
        public Integer toId(T source) {
            return this.objectToInt.get(source);
        }

        public T fromId(int id) {
            return this.intToObject.get(id);
        }

        public T intern(T source) {
            return this.fromId(this.add(source));
        }

        public int size() {
            return this.intToObject.size();
        }

        public Integer getOldAndAdd(T source) {
            Integer result = this.objectToInt.get(source);
            if (result == null) {
                Integer newResult = this.intToObject.size();
                this.objectToInt.put(source, newResult);
                this.intToObject.add(source);
            }
            return result;
        }

        public String toString() {
            return this.size() + ": " + this.intToObject;
        }

        public boolean equals(Object obj) {
            return this == obj || obj != null && obj.getClass() == this.getClass() && this.intToObject.equals(((IdMakerFull)obj).intToObject);
        }

        public int hashCode() {
            return this.intToObject.hashCode();
        }
    }

    private static interface IdMapper<K, V> {
        public V toId(K var1);
    }

    @Deprecated
    public static class DistanceNode {
        final int distance;

        public DistanceNode(int distance) {
            this.distance = distance;
        }

        public DistanceTable getDistanceTable() {
            return null;
        }

        public boolean equals(Object obj) {
            return this == obj || obj != null && obj.getClass() == this.getClass() && this.distance == ((DistanceNode)obj).distance;
        }

        public int hashCode() {
            return this.distance;
        }

        public String toString() {
            return "\ndistance: " + this.distance;
        }
    }

    @Deprecated
    public static abstract class DistanceTable {
        abstract int getDistance(String var1, String var2, Output<DistanceTable> var3, boolean var4);

        abstract Set<String> getCloser(int var1);

        abstract String toString(boolean var1);

        public DistanceTable compact() {
            return this;
        }

        public DistanceNode getInternalNode(String any, String any2) {
            return null;
        }

        public Map<String, Set<String>> getInternalMatches() {
            return null;
        }

        public boolean isEmpty() {
            return true;
        }
    }
}

