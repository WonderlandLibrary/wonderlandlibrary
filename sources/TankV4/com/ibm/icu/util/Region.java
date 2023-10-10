package com.ibm.icu.util;

import com.ibm.icu.impl.ICUResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Region implements Comparable {
   public static final int UNDEFINED_NUMERIC_CODE = -1;
   private String id;
   private int code;
   private Region.RegionType type;
   private Region containingRegion = null;
   private Set containedRegions = new TreeSet();
   private List preferredValues = null;
   private static boolean regionDataIsLoaded = false;
   private static Map regionIDMap = null;
   private static Map numericCodeMap = null;
   private static Map regionAliases = null;
   private static ArrayList regions = null;
   private static ArrayList availableRegions = null;
   private static final String UNKNOWN_REGION_ID = "ZZ";
   private static final String OUTLYING_OCEANIA_REGION_ID = "QO";
   private static final String WORLD_ID = "001";

   private Region() {
   }

   private static synchronized void loadRegionData() {
      if (!regionDataIsLoaded) {
         regionAliases = new HashMap();
         regionIDMap = new HashMap();
         numericCodeMap = new HashMap();
         availableRegions = new ArrayList(Region.RegionType.values().length);
         UResourceBundle var0 = null;
         UResourceBundle var1 = null;
         UResourceBundle var2 = null;
         UResourceBundle var3 = null;
         UResourceBundle var4 = null;
         UResourceBundle var5 = null;
         UResourceBundle var6 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metadata", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
         var0 = var6.get("regionCodes");
         var1 = var6.get("territoryAlias");
         UResourceBundle var7 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
         var2 = var7.get("codeMappings");
         var4 = var7.get("territoryContainment");
         var3 = var4.get("001");
         var5 = var4.get("grouping");
         String[] var8 = var3.getStringArray();
         List var9 = Arrays.asList(var8);
         String[] var10 = var5.getStringArray();
         List var11 = Arrays.asList(var10);
         int var12 = var0.getSize();
         regions = new ArrayList(var12);

         int var13;
         String var15;
         for(var13 = 0; var13 < var12; ++var13) {
            Region var14 = new Region();
            var15 = var0.getString(var13);
            var14.id = var15;
            var14.type = Region.RegionType.TERRITORY;
            regionIDMap.put(var15, var14);
            if (var15.matches("[0-9]{3}")) {
               var14.code = Integer.valueOf(var15);
               numericCodeMap.put(var14.code, var14);
               var14.type = Region.RegionType.SUBCONTINENT;
            } else {
               var14.code = -1;
            }

            regions.add(var14);
         }

         String var16;
         Region var17;
         UResourceBundle var21;
         for(var13 = 0; var13 < var1.getSize(); ++var13) {
            var21 = var1.get(var13);
            var15 = var21.getKey();
            var16 = var21.getString();
            if (regionIDMap.containsKey(var16) && !regionIDMap.containsKey(var15)) {
               regionAliases.put(var15, regionIDMap.get(var16));
            } else {
               if (regionIDMap.containsKey(var15)) {
                  var17 = (Region)regionIDMap.get(var15);
               } else {
                  var17 = new Region();
                  var17.id = var15;
                  regionIDMap.put(var15, var17);
                  if (var15.matches("[0-9]{3}")) {
                     var17.code = Integer.valueOf(var15);
                     numericCodeMap.put(var17.code, var17);
                  } else {
                     var17.code = -1;
                  }

                  regions.add(var17);
               }

               var17.type = Region.RegionType.DEPRECATED;
               List var18 = Arrays.asList(var16.split(" "));
               var17.preferredValues = new ArrayList();
               Iterator var19 = var18.iterator();

               while(var19.hasNext()) {
                  String var20 = (String)var19.next();
                  if (regionIDMap.containsKey(var20)) {
                     var17.preferredValues.add(regionIDMap.get(var20));
                  }
               }
            }
         }

         for(var13 = 0; var13 < var2.getSize(); ++var13) {
            var21 = var2.get(var13);
            if (var21.getType() == 8) {
               String[] var24 = var21.getStringArray();
               var16 = var24[0];
               Integer var29 = Integer.valueOf(var24[1]);
               String var30 = var24[2];
               if (regionIDMap.containsKey(var16)) {
                  Region var32 = (Region)regionIDMap.get(var16);
                  var32.code = var29;
                  numericCodeMap.put(var32.code, var32);
                  regionAliases.put(var30, var32);
               }
            }
         }

         Region var22;
         if (regionIDMap.containsKey("001")) {
            var22 = (Region)regionIDMap.get("001");
            var22.type = Region.RegionType.WORLD;
         }

         if (regionIDMap.containsKey("ZZ")) {
            var22 = (Region)regionIDMap.get("ZZ");
            var22.type = Region.RegionType.UNKNOWN;
         }

         Iterator var23 = var9.iterator();

         while(var23.hasNext()) {
            var15 = (String)var23.next();
            if (regionIDMap.containsKey(var15)) {
               var22 = (Region)regionIDMap.get(var15);
               var22.type = Region.RegionType.CONTINENT;
            }
         }

         var23 = var11.iterator();

         while(var23.hasNext()) {
            var15 = (String)var23.next();
            if (regionIDMap.containsKey(var15)) {
               var22 = (Region)regionIDMap.get(var15);
               var22.type = Region.RegionType.GROUPING;
            }
         }

         if (regionIDMap.containsKey("QO")) {
            var22 = (Region)regionIDMap.get("QO");
            var22.type = Region.RegionType.SUBCONTINENT;
         }

         int var25;
         for(var25 = 0; var25 < var4.getSize(); ++var25) {
            UResourceBundle var26 = var4.get(var25);
            var16 = var26.getKey();
            var17 = (Region)regionIDMap.get(var16);

            for(int var31 = 0; var31 < var26.getSize(); ++var31) {
               String var33 = var26.getString(var31);
               Region var34 = (Region)regionIDMap.get(var33);
               if (var17 != null && var34 != null) {
                  var17.containedRegions.add(var34);
                  if (var17.getType() != Region.RegionType.GROUPING) {
                     var34.containingRegion = var17;
                  }
               }
            }
         }

         for(var25 = 0; var25 < Region.RegionType.values().length; ++var25) {
            availableRegions.add(new TreeSet());
         }

         var23 = regions.iterator();

         while(var23.hasNext()) {
            Region var27 = (Region)var23.next();
            Set var28 = (Set)availableRegions.get(var27.type.ordinal());
            var28.add(var27);
            availableRegions.set(var27.type.ordinal(), var28);
         }

         regionDataIsLoaded = true;
      }
   }

   public static Region getInstance(String var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         loadRegionData();
         Region var1 = (Region)regionIDMap.get(var0);
         if (var1 == null) {
            var1 = (Region)regionAliases.get(var0);
         }

         if (var1 == null) {
            throw new IllegalArgumentException("Unknown region id: " + var0);
         } else {
            if (var1.type == Region.RegionType.DEPRECATED && var1.preferredValues.size() == 1) {
               var1 = (Region)var1.preferredValues.get(0);
            }

            return var1;
         }
      }
   }

   public static Region getInstance(int var0) {
      loadRegionData();
      Region var1 = (Region)numericCodeMap.get(var0);
      if (var1 == null) {
         String var2 = "";
         if (var0 < 10) {
            var2 = "00";
         } else if (var0 < 100) {
            var2 = "0";
         }

         String var3 = var2 + Integer.toString(var0);
         var1 = (Region)regionAliases.get(var3);
      }

      if (var1 == null) {
         throw new IllegalArgumentException("Unknown region code: " + var0);
      } else {
         if (var1.type == Region.RegionType.DEPRECATED && var1.preferredValues.size() == 1) {
            var1 = (Region)var1.preferredValues.get(0);
         }

         return var1;
      }
   }

   public static Set getAvailable(Region.RegionType var0) {
      loadRegionData();
      return Collections.unmodifiableSet((Set)availableRegions.get(var0.ordinal()));
   }

   public Region getContainingRegion() {
      loadRegionData();
      return this.containingRegion;
   }

   public Region getContainingRegion(Region.RegionType var1) {
      loadRegionData();
      if (this.containingRegion == null) {
         return null;
      } else {
         return this.containingRegion.type.equals(var1) ? this.containingRegion : this.containingRegion.getContainingRegion(var1);
      }
   }

   public Set getContainedRegions() {
      loadRegionData();
      return Collections.unmodifiableSet(this.containedRegions);
   }

   public Set getContainedRegions(Region.RegionType var1) {
      loadRegionData();
      TreeSet var2 = new TreeSet();
      Set var3 = this.getContainedRegions();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Region var5 = (Region)var4.next();
         if (var5.getType() == var1) {
            var2.add(var5);
         } else {
            var2.addAll(var5.getContainedRegions(var1));
         }
      }

      return Collections.unmodifiableSet(var2);
   }

   public List getPreferredValues() {
      loadRegionData();
      return this.type == Region.RegionType.DEPRECATED ? Collections.unmodifiableList(this.preferredValues) : null;
   }

   public String toString() {
      return this.id;
   }

   public int getNumericCode() {
      return this.code;
   }

   public Region.RegionType getType() {
      return this.type;
   }

   public int compareTo(Region var1) {
      return this.id.compareTo(var1.id);
   }

   public int compareTo(Object var1) {
      return this.compareTo((Region)var1);
   }

   public static enum RegionType {
      UNKNOWN,
      TERRITORY,
      WORLD,
      CONTINENT,
      SUBCONTINENT,
      GROUPING,
      DEPRECATED;

      private static final Region.RegionType[] $VALUES = new Region.RegionType[]{UNKNOWN, TERRITORY, WORLD, CONTINENT, SUBCONTINENT, GROUPING, DEPRECATED};
   }
}
