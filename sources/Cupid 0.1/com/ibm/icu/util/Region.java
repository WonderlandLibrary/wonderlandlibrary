package com.ibm.icu.util;

import com.ibm.icu.impl.ICUResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Region implements Comparable<Region> {
  public static final int UNDEFINED_NUMERIC_CODE = -1;
  
  private String id;
  
  private int code;
  
  private RegionType type;
  
  public enum RegionType {
    UNKNOWN, TERRITORY, WORLD, CONTINENT, SUBCONTINENT, GROUPING, DEPRECATED;
  }
  
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
  
  private static synchronized void loadRegionData() {
    if (regionDataIsLoaded)
      return; 
    regionAliases = new HashMap<String, Region>();
    regionIDMap = new HashMap<String, Region>();
    numericCodeMap = new HashMap<Integer, Region>();
    availableRegions = new ArrayList<Set<Region>>((RegionType.values()).length);
    UResourceBundle regionCodes = null;
    UResourceBundle territoryAlias = null;
    UResourceBundle codeMappings = null;
    UResourceBundle worldContainment = null;
    UResourceBundle territoryContainment = null;
    UResourceBundle groupingContainment = null;
    UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metadata", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    regionCodes = rb.get("regionCodes");
    territoryAlias = rb.get("territoryAlias");
    UResourceBundle rb2 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    codeMappings = rb2.get("codeMappings");
    territoryContainment = rb2.get("territoryContainment");
    worldContainment = territoryContainment.get("001");
    groupingContainment = territoryContainment.get("grouping");
    String[] continentsArr = worldContainment.getStringArray();
    List<String> continents = Arrays.asList(continentsArr);
    String[] groupingArr = groupingContainment.getStringArray();
    List<String> groupings = Arrays.asList(groupingArr);
    int regionCodeSize = regionCodes.getSize();
    regions = new ArrayList<Region>(regionCodeSize);
    int i;
    for (i = 0; i < regionCodeSize; i++) {
      Region r = new Region();
      String id = regionCodes.getString(i);
      r.id = id;
      r.type = RegionType.TERRITORY;
      regionIDMap.put(id, r);
      if (id.matches("[0-9]{3}")) {
        r.code = Integer.valueOf(id).intValue();
        numericCodeMap.put(Integer.valueOf(r.code), r);
        r.type = RegionType.SUBCONTINENT;
      } else {
        r.code = -1;
      } 
      regions.add(r);
    } 
    for (i = 0; i < territoryAlias.getSize(); i++) {
      UResourceBundle res = territoryAlias.get(i);
      String aliasFrom = res.getKey();
      String aliasTo = res.getString();
      if (regionIDMap.containsKey(aliasTo) && !regionIDMap.containsKey(aliasFrom)) {
        regionAliases.put(aliasFrom, regionIDMap.get(aliasTo));
      } else {
        Region r;
        if (regionIDMap.containsKey(aliasFrom)) {
          r = regionIDMap.get(aliasFrom);
        } else {
          r = new Region();
          r.id = aliasFrom;
          regionIDMap.put(aliasFrom, r);
          if (aliasFrom.matches("[0-9]{3}")) {
            r.code = Integer.valueOf(aliasFrom).intValue();
            numericCodeMap.put(Integer.valueOf(r.code), r);
          } else {
            r.code = -1;
          } 
          regions.add(r);
        } 
        r.type = RegionType.DEPRECATED;
        List<String> aliasToRegionStrings = Arrays.asList(aliasTo.split(" "));
        r.preferredValues = new ArrayList<Region>();
        for (String s : aliasToRegionStrings) {
          if (regionIDMap.containsKey(s))
            r.preferredValues.add(regionIDMap.get(s)); 
        } 
      } 
    } 
    for (i = 0; i < codeMappings.getSize(); i++) {
      UResourceBundle mapping = codeMappings.get(i);
      if (mapping.getType() == 8) {
        String[] codeMappingStrings = mapping.getStringArray();
        String codeMappingID = codeMappingStrings[0];
        Integer codeMappingNumber = Integer.valueOf(codeMappingStrings[1]);
        String codeMapping3Letter = codeMappingStrings[2];
        if (regionIDMap.containsKey(codeMappingID)) {
          Region r = regionIDMap.get(codeMappingID);
          r.code = codeMappingNumber.intValue();
          numericCodeMap.put(Integer.valueOf(r.code), r);
          regionAliases.put(codeMapping3Letter, r);
        } 
      } 
    } 
    if (regionIDMap.containsKey("001")) {
      Region r = regionIDMap.get("001");
      r.type = RegionType.WORLD;
    } 
    if (regionIDMap.containsKey("ZZ")) {
      Region r = regionIDMap.get("ZZ");
      r.type = RegionType.UNKNOWN;
    } 
    for (String continent : continents) {
      if (regionIDMap.containsKey(continent)) {
        Region r = regionIDMap.get(continent);
        r.type = RegionType.CONTINENT;
      } 
    } 
    for (String grouping : groupings) {
      if (regionIDMap.containsKey(grouping)) {
        Region r = regionIDMap.get(grouping);
        r.type = RegionType.GROUPING;
      } 
    } 
    if (regionIDMap.containsKey("QO")) {
      Region r = regionIDMap.get("QO");
      r.type = RegionType.SUBCONTINENT;
    } 
    int j;
    for (j = 0; j < territoryContainment.getSize(); j++) {
      UResourceBundle mapping = territoryContainment.get(j);
      String parent = mapping.getKey();
      Region parentRegion = regionIDMap.get(parent);
      for (int k = 0; k < mapping.getSize(); k++) {
        String child = mapping.getString(k);
        Region childRegion = regionIDMap.get(child);
        if (parentRegion != null && childRegion != null) {
          parentRegion.containedRegions.add(childRegion);
          if (parentRegion.getType() != RegionType.GROUPING)
            childRegion.containingRegion = parentRegion; 
        } 
      } 
    } 
    for (j = 0; j < (RegionType.values()).length; j++)
      availableRegions.add(new TreeSet<Region>()); 
    for (Region ar : regions) {
      Set<Region> currentSet = availableRegions.get(ar.type.ordinal());
      currentSet.add(ar);
      availableRegions.set(ar.type.ordinal(), currentSet);
    } 
    regionDataIsLoaded = true;
  }
  
  public static Region getInstance(String id) {
    if (id == null)
      throw new NullPointerException(); 
    loadRegionData();
    Region r = regionIDMap.get(id);
    if (r == null)
      r = regionAliases.get(id); 
    if (r == null)
      throw new IllegalArgumentException("Unknown region id: " + id); 
    if (r.type == RegionType.DEPRECATED && r.preferredValues.size() == 1)
      r = r.preferredValues.get(0); 
    return r;
  }
  
  public static Region getInstance(int code) {
    loadRegionData();
    Region r = numericCodeMap.get(Integer.valueOf(code));
    if (r == null) {
      String pad = "";
      if (code < 10) {
        pad = "00";
      } else if (code < 100) {
        pad = "0";
      } 
      String id = pad + Integer.toString(code);
      r = regionAliases.get(id);
    } 
    if (r == null)
      throw new IllegalArgumentException("Unknown region code: " + code); 
    if (r.type == RegionType.DEPRECATED && r.preferredValues.size() == 1)
      r = r.preferredValues.get(0); 
    return r;
  }
  
  public static Set<Region> getAvailable(RegionType type) {
    loadRegionData();
    return Collections.unmodifiableSet(availableRegions.get(type.ordinal()));
  }
  
  public Region getContainingRegion() {
    loadRegionData();
    return this.containingRegion;
  }
  
  public Region getContainingRegion(RegionType type) {
    loadRegionData();
    if (this.containingRegion == null)
      return null; 
    if (this.containingRegion.type.equals(type))
      return this.containingRegion; 
    return this.containingRegion.getContainingRegion(type);
  }
  
  public Set<Region> getContainedRegions() {
    loadRegionData();
    return Collections.unmodifiableSet(this.containedRegions);
  }
  
  public Set<Region> getContainedRegions(RegionType type) {
    loadRegionData();
    Set<Region> result = new TreeSet<Region>();
    Set<Region> cr = getContainedRegions();
    for (Region r : cr) {
      if (r.getType() == type) {
        result.add(r);
        continue;
      } 
      result.addAll(r.getContainedRegions(type));
    } 
    return Collections.unmodifiableSet(result);
  }
  
  public List<Region> getPreferredValues() {
    loadRegionData();
    if (this.type == RegionType.DEPRECATED)
      return Collections.unmodifiableList(this.preferredValues); 
    return null;
  }
  
  public boolean contains(Region other) {
    loadRegionData();
    if (this.containedRegions.contains(other))
      return true; 
    for (Region cr : this.containedRegions) {
      if (cr.contains(other))
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
  
  public int compareTo(Region other) {
    return this.id.compareTo(other.id);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\Region.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */