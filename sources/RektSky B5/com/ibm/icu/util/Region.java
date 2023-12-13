/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.util.UResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Region
implements Comparable<Region> {
    private String id;
    private int code;
    private RegionType type;
    private Region containingRegion = null;
    private Set<Region> containedRegions = new TreeSet<Region>();
    private List<Region> preferredValues = null;
    private static boolean regionDataIsLoaded = false;
    private static Map<String, Region> regionIDMap = null;
    private static Map<Integer, Region> numericCodeMap = null;
    private static Map<String, Region> regionAliases = null;
    private static ArrayList<Region> regions = null;
    private static ArrayList<Set<Region>> availableRegions = null;
    private static final String UNKNOWN_REGION_ID = "ZZ";
    private static final String OUTLYING_OCEANIA_REGION_ID = "QO";
    private static final String WORLD_ID = "001";

    private Region() {
    }

    private static synchronized void loadRegionData() {
        int i2;
        int i3;
        if (regionDataIsLoaded) {
            return;
        }
        regionAliases = new HashMap<String, Region>();
        regionIDMap = new HashMap<String, Region>();
        numericCodeMap = new HashMap<Integer, Region>();
        availableRegions = new ArrayList(RegionType.values().length);
        UResourceBundle metadataAlias = null;
        UResourceBundle territoryAlias = null;
        UResourceBundle codeMappings = null;
        UResourceBundle idValidity = null;
        UResourceBundle regionList = null;
        UResourceBundle regionRegular = null;
        UResourceBundle regionMacro = null;
        UResourceBundle regionUnknown = null;
        UResourceBundle worldContainment = null;
        UResourceBundle territoryContainment = null;
        UResourceBundle groupingContainment = null;
        UResourceBundle metadata = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "metadata", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        metadataAlias = metadata.get("alias");
        territoryAlias = metadataAlias.get("territory");
        UResourceBundle supplementalData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        codeMappings = supplementalData.get("codeMappings");
        idValidity = supplementalData.get("idValidity");
        regionList = idValidity.get("region");
        regionRegular = regionList.get("regular");
        regionMacro = regionList.get("macroregion");
        regionUnknown = regionList.get("unknown");
        territoryContainment = supplementalData.get("territoryContainment");
        worldContainment = territoryContainment.get(WORLD_ID);
        groupingContainment = territoryContainment.get("grouping");
        String[] continentsArr = worldContainment.getStringArray();
        List<String> continents = Arrays.asList(continentsArr);
        String[] groupingArr = groupingContainment.getStringArray();
        List<String> groupings = Arrays.asList(groupingArr);
        ArrayList<String> regionCodes = new ArrayList<String>();
        ArrayList<String> allRegions = new ArrayList<String>();
        allRegions.addAll(Arrays.asList(regionRegular.getStringArray()));
        allRegions.addAll(Arrays.asList(regionMacro.getStringArray()));
        allRegions.add(regionUnknown.getString());
        for (String r2 : allRegions) {
            int rangeMarkerLocation = r2.indexOf("~");
            if (rangeMarkerLocation > 0) {
                StringBuilder regionName = new StringBuilder(r2);
                char endRange = regionName.charAt(rangeMarkerLocation + 1);
                regionName.setLength(rangeMarkerLocation);
                for (char lastChar = regionName.charAt(rangeMarkerLocation - 1); lastChar <= endRange; lastChar = (char)(lastChar + '\u0001')) {
                    String newRegion = regionName.toString();
                    regionCodes.add(newRegion);
                    regionName.setCharAt(rangeMarkerLocation - 1, lastChar);
                }
                continue;
            }
            regionCodes.add(r2);
        }
        regions = new ArrayList(regionCodes.size());
        for (String id : regionCodes) {
            Region r3 = new Region();
            r3.id = id;
            r3.type = RegionType.TERRITORY;
            regionIDMap.put(id, r3);
            if (id.matches("[0-9]{3}")) {
                r3.code = Integer.valueOf(id);
                numericCodeMap.put(r3.code, r3);
                r3.type = RegionType.SUBCONTINENT;
            } else {
                r3.code = -1;
            }
            regions.add(r3);
        }
        for (i3 = 0; i3 < territoryAlias.getSize(); ++i3) {
            Region r4;
            UResourceBundle res = territoryAlias.get(i3);
            String aliasFrom = res.getKey();
            String aliasTo = res.get("replacement").getString();
            if (regionIDMap.containsKey(aliasTo) && !regionIDMap.containsKey(aliasFrom)) {
                regionAliases.put(aliasFrom, regionIDMap.get(aliasTo));
                continue;
            }
            if (regionIDMap.containsKey(aliasFrom)) {
                r4 = regionIDMap.get(aliasFrom);
            } else {
                r4 = new Region();
                r4.id = aliasFrom;
                regionIDMap.put(aliasFrom, r4);
                if (aliasFrom.matches("[0-9]{3}")) {
                    r4.code = Integer.valueOf(aliasFrom);
                    numericCodeMap.put(r4.code, r4);
                } else {
                    r4.code = -1;
                }
                regions.add(r4);
            }
            r4.type = RegionType.DEPRECATED;
            List<String> aliasToRegionStrings = Arrays.asList(aliasTo.split(" "));
            r4.preferredValues = new ArrayList<Region>();
            for (String s2 : aliasToRegionStrings) {
                if (!regionIDMap.containsKey(s2)) continue;
                r4.preferredValues.add(regionIDMap.get(s2));
            }
        }
        for (i3 = 0; i3 < codeMappings.getSize(); ++i3) {
            UResourceBundle mapping = codeMappings.get(i3);
            if (mapping.getType() != 8) continue;
            String[] codeMappingStrings = mapping.getStringArray();
            String codeMappingID = codeMappingStrings[0];
            Integer codeMappingNumber = Integer.valueOf(codeMappingStrings[1]);
            String codeMapping3Letter = codeMappingStrings[2];
            if (!regionIDMap.containsKey(codeMappingID)) continue;
            Region r5 = regionIDMap.get(codeMappingID);
            r5.code = codeMappingNumber;
            numericCodeMap.put(r5.code, r5);
            regionAliases.put(codeMapping3Letter, r5);
        }
        if (regionIDMap.containsKey(WORLD_ID)) {
            Region r6 = regionIDMap.get(WORLD_ID);
            r6.type = RegionType.WORLD;
        }
        if (regionIDMap.containsKey(UNKNOWN_REGION_ID)) {
            Region r7 = regionIDMap.get(UNKNOWN_REGION_ID);
            r7.type = RegionType.UNKNOWN;
        }
        for (String continent : continents) {
            if (!regionIDMap.containsKey(continent)) continue;
            Region r8 = regionIDMap.get(continent);
            r8.type = RegionType.CONTINENT;
        }
        for (String grouping : groupings) {
            if (!regionIDMap.containsKey(grouping)) continue;
            Region r9 = regionIDMap.get(grouping);
            r9.type = RegionType.GROUPING;
        }
        if (regionIDMap.containsKey(OUTLYING_OCEANIA_REGION_ID)) {
            Region r10 = regionIDMap.get(OUTLYING_OCEANIA_REGION_ID);
            r10.type = RegionType.SUBCONTINENT;
        }
        for (i2 = 0; i2 < territoryContainment.getSize(); ++i2) {
            UResourceBundle mapping = territoryContainment.get(i2);
            String parent = mapping.getKey();
            if (parent.equals("containedGroupings") || parent.equals("deprecated")) continue;
            Region parentRegion = regionIDMap.get(parent);
            for (int j2 = 0; j2 < mapping.getSize(); ++j2) {
                String child = mapping.getString(j2);
                Region childRegion = regionIDMap.get(child);
                if (parentRegion == null || childRegion == null) continue;
                parentRegion.containedRegions.add(childRegion);
                if (parentRegion.getType() == RegionType.GROUPING) continue;
                childRegion.containingRegion = parentRegion;
            }
        }
        for (i2 = 0; i2 < RegionType.values().length; ++i2) {
            availableRegions.add(new TreeSet());
        }
        for (Region ar : regions) {
            Set<Region> currentSet = availableRegions.get(ar.type.ordinal());
            currentSet.add(ar);
            availableRegions.set(ar.type.ordinal(), currentSet);
        }
        regionDataIsLoaded = true;
    }

    public static Region getInstance(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        Region.loadRegionData();
        Region r2 = regionIDMap.get(id);
        if (r2 == null) {
            r2 = regionAliases.get(id);
        }
        if (r2 == null) {
            throw new IllegalArgumentException("Unknown region id: " + id);
        }
        if (r2.type == RegionType.DEPRECATED && r2.preferredValues.size() == 1) {
            r2 = r2.preferredValues.get(0);
        }
        return r2;
    }

    public static Region getInstance(int code) {
        Region.loadRegionData();
        Region r2 = numericCodeMap.get(code);
        if (r2 == null) {
            String pad = "";
            if (code < 10) {
                pad = "00";
            } else if (code < 100) {
                pad = "0";
            }
            String id = pad + Integer.toString(code);
            r2 = regionAliases.get(id);
        }
        if (r2 == null) {
            throw new IllegalArgumentException("Unknown region code: " + code);
        }
        if (r2.type == RegionType.DEPRECATED && r2.preferredValues.size() == 1) {
            r2 = r2.preferredValues.get(0);
        }
        return r2;
    }

    public static Set<Region> getAvailable(RegionType type) {
        Region.loadRegionData();
        return Collections.unmodifiableSet(availableRegions.get(type.ordinal()));
    }

    public Region getContainingRegion() {
        Region.loadRegionData();
        return this.containingRegion;
    }

    public Region getContainingRegion(RegionType type) {
        Region.loadRegionData();
        if (this.containingRegion == null) {
            return null;
        }
        if (this.containingRegion.type.equals((Object)type)) {
            return this.containingRegion;
        }
        return this.containingRegion.getContainingRegion(type);
    }

    public Set<Region> getContainedRegions() {
        Region.loadRegionData();
        return Collections.unmodifiableSet(this.containedRegions);
    }

    public Set<Region> getContainedRegions(RegionType type) {
        Region.loadRegionData();
        TreeSet<Region> result = new TreeSet<Region>();
        Set<Region> cr = this.getContainedRegions();
        for (Region r2 : cr) {
            if (r2.getType() == type) {
                result.add(r2);
                continue;
            }
            result.addAll(r2.getContainedRegions(type));
        }
        return Collections.unmodifiableSet(result);
    }

    public List<Region> getPreferredValues() {
        Region.loadRegionData();
        if (this.type == RegionType.DEPRECATED) {
            return Collections.unmodifiableList(this.preferredValues);
        }
        return null;
    }

    public boolean contains(Region other) {
        Region.loadRegionData();
        if (this.containedRegions.contains(other)) {
            return true;
        }
        for (Region cr : this.containedRegions) {
            if (!cr.contains(other)) continue;
            return true;
        }
        return false;
    }

    public String toString() {
        return this.id;
    }

    public int getNumericCode() {
        return this.code;
    }

    public RegionType getType() {
        return this.type;
    }

    @Override
    public int compareTo(Region other) {
        return this.id.compareTo(other.id);
    }

    public static enum RegionType {
        UNKNOWN,
        TERRITORY,
        WORLD,
        CONTINENT,
        SUBCONTINENT,
        GROUPING,
        DEPRECATED;

    }
}

