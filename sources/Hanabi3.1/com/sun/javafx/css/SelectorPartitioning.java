package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SelectorPartitioning {
   private final Map idMap = new HashMap();
   private final Map typeMap = new HashMap();
   private final Map styleClassMap = new HashMap();
   private int ordinal;
   private static final int ID_BIT = 4;
   private static final int TYPE_BIT = 2;
   private static final int STYLECLASS_BIT = 1;
   private static final PartitionKey WILDCARD = new PartitionKey("*");
   private static final Comparator COMPARATOR = (var0, var1) -> {
      return var0.getOrdinal() - var1.getOrdinal();
   };

   SelectorPartitioning() {
   }

   void reset() {
      this.idMap.clear();
      this.typeMap.clear();
      this.styleClassMap.clear();
      this.ordinal = 0;
   }

   private static Partition getPartition(PartitionKey var0, Map var1) {
      Partition var2 = (Partition)var1.get(var0);
      if (var2 == null) {
         var2 = new Partition(var0);
         var1.put(var0, var2);
      }

      return var2;
   }

   void partition(Selector var1) {
      SimpleSelector var2 = null;
      if (var1 instanceof CompoundSelector) {
         List var3 = ((CompoundSelector)var1).getSelectors();
         int var4 = var3.size() - 1;
         var2 = (SimpleSelector)var3.get(var4);
      } else {
         var2 = (SimpleSelector)var1;
      }

      String var15 = var2.getId();
      boolean var16 = var15 != null && !var15.isEmpty();
      PartitionKey var5 = var16 ? new PartitionKey(var15) : null;
      String var6 = var2.getName();
      boolean var7 = var6 != null && !var6.isEmpty();
      PartitionKey var8 = var7 ? new PartitionKey(var6) : null;
      Set var9 = var2.getStyleClassSet();
      boolean var10 = var9 != null && var9.size() > 0;
      PartitionKey var11 = var10 ? new PartitionKey(var9) : null;
      int var12 = (var16 ? 4 : 0) | (var7 ? 2 : 0) | (var10 ? 1 : 0);
      Partition var13 = null;
      Slot var14 = null;
      var1.setOrdinal(this.ordinal++);
      switch (var12) {
         case 1:
         case 4:
         case 5:
         default:
            assert false;
            break;
         case 2:
         case 3:
            var13 = getPartition(var8, this.typeMap);
            if ((var12 & 1) == 1) {
               var14 = var13.partition(var11, this.styleClassMap);
               var14.addSelector(var1);
            } else {
               var13.addSelector(var1);
            }
            break;
         case 6:
         case 7:
            var13 = getPartition(var5, this.idMap);
            var14 = var13.partition(var8, this.typeMap);
            if ((var12 & 1) == 1) {
               var14 = var14.partition(var11, this.styleClassMap);
            }

            var14.addSelector(var1);
      }

   }

   List match(String var1, String var2, Set var3) {
      boolean var4 = var1 != null && !var1.isEmpty();
      PartitionKey var5 = var4 ? new PartitionKey(var1) : null;
      boolean var6 = var2 != null && !var2.isEmpty();
      PartitionKey var7 = var6 ? new PartitionKey(var2) : null;
      boolean var8 = var3 != null && var3.size() > 0;
      PartitionKey var9 = var8 ? new PartitionKey(var3) : null;
      int var10 = (var4 ? 4 : 0) | (var6 ? 2 : 0) | (var8 ? 1 : 0);
      Partition var11 = null;
      Slot var12 = null;
      ArrayList var13 = new ArrayList();

      while(true) {
         while(var10 != 0) {
            PartitionKey var14;
            Set var15;
            Iterator var16;
            Slot var17;
            Set var18;
            switch (var10) {
               case 1:
                  --var10;
                  break;
               case 2:
               case 3:
                  var14 = var7;

                  do {
                     var11 = (Partition)this.typeMap.get(var14);
                     if (var11 != null) {
                        if (var11.selectors != null) {
                           var13.addAll(var11.selectors);
                        }

                        if ((var10 & 1) == 1) {
                           var15 = (Set)var9.key;
                           var16 = var11.slots.values().iterator();

                           while(var16.hasNext()) {
                              var17 = (Slot)var16.next();
                              if (var17.selectors != null && !var17.selectors.isEmpty()) {
                                 var18 = (Set)var17.partition.key.key;
                                 if (var15.containsAll(var18)) {
                                    var13.addAll(var17.selectors);
                                 }
                              }
                           }
                        }
                     }

                     var14 = !WILDCARD.equals(var14) ? WILDCARD : null;
                  } while(var14 != null);

                  var10 -= 2;
                  break;
               case 4:
               case 5:
                  var10 -= 4;
                  break;
               case 6:
               case 7:
                  var11 = (Partition)this.idMap.get(var5);
                  if (var11 != null) {
                     if (var11.selectors != null) {
                        var13.addAll(var11.selectors);
                     }

                     var14 = var7;

                     do {
                        var12 = (Slot)var11.slots.get(var14);
                        if (var12 != null) {
                           if (var12.selectors != null) {
                              var13.addAll(var12.selectors);
                           }

                           if ((var10 & 1) == 1) {
                              var15 = (Set)var9.key;
                              var16 = var12.referents.values().iterator();

                              while(var16.hasNext()) {
                                 var17 = (Slot)var16.next();
                                 if (var17.selectors != null && !var17.selectors.isEmpty()) {
                                    var18 = (Set)var17.partition.key.key;
                                    if (var15.containsAll(var18)) {
                                       var13.addAll(var17.selectors);
                                    }
                                 }
                              }
                           }
                        }

                        var14 = !WILDCARD.equals(var14) ? WILDCARD : null;
                     } while(var14 != null);
                  }

                  var10 -= 4;
                  break;
               default:
                  assert false;
            }
         }

         Collections.sort(var13, COMPARATOR);
         return var13;
      }
   }

   private static final class Slot {
      private final Partition partition;
      private final Map referents;
      private List selectors;

      private Slot(Partition var1) {
         this.partition = var1;
         this.referents = new HashMap();
      }

      private void addSelector(Selector var1) {
         if (this.selectors == null) {
            this.selectors = new ArrayList();
         }

         this.selectors.add(var1);
      }

      private Slot partition(PartitionKey var1, Map var2) {
         Slot var3 = (Slot)this.referents.get(var1);
         if (var3 == null) {
            Partition var4 = SelectorPartitioning.getPartition(var1, var2);
            var3 = new Slot(var4);
            this.referents.put(var1, var3);
         }

         return var3;
      }

      // $FF: synthetic method
      Slot(Partition var1, Object var2) {
         this(var1);
      }
   }

   private static final class Partition {
      private final PartitionKey key;
      private final Map slots;
      private List selectors;

      private Partition(PartitionKey var1) {
         this.key = var1;
         this.slots = new HashMap();
      }

      private void addSelector(Selector var1) {
         if (this.selectors == null) {
            this.selectors = new ArrayList();
         }

         this.selectors.add(var1);
      }

      private Slot partition(PartitionKey var1, Map var2) {
         Slot var3 = (Slot)this.slots.get(var1);
         if (var3 == null) {
            Partition var4 = SelectorPartitioning.getPartition(var1, var2);
            var3 = new Slot(var4);
            this.slots.put(var1, var3);
         }

         return var3;
      }

      // $FF: synthetic method
      Partition(PartitionKey var1, Object var2) {
         this(var1);
      }
   }

   private static final class PartitionKey {
      private final Object key;

      private PartitionKey(Object var1) {
         this.key = var1;
      }

      public boolean equals(Object var1) {
         if (var1 == null) {
            return false;
         } else if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            PartitionKey var2 = (PartitionKey)var1;
            return this.key == var2.key || this.key != null && this.key.equals(var2.key);
         }
      }

      public int hashCode() {
         int var1 = 7;
         var1 = 71 * var1 + (this.key != null ? this.key.hashCode() : 0);
         return var1;
      }

      // $FF: synthetic method
      PartitionKey(Object var1, Object var2) {
         this(var1);
      }
   }
}
