package optifine;

import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;

public class NextTickHashSet extends TreeSet {
   private LongHashMap longHashMap = new LongHashMap();
   private int minX = Integer.MIN_VALUE;
   private int minZ = Integer.MIN_VALUE;
   private int maxX = Integer.MIN_VALUE;
   private int maxZ = Integer.MIN_VALUE;
   private static final int UNDEFINED = Integer.MIN_VALUE;

   public NextTickHashSet(Set var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         this.add(var2);
      }

   }

   public boolean contains(Object var1) {
      if (!(var1 instanceof NextTickListEntry)) {
         return false;
      } else {
         NextTickListEntry var2 = (NextTickListEntry)var1;
         Set var3 = this.getSubSet(var2, false);
         return var3 == null ? false : var3.contains(var2);
      }
   }

   public boolean add(Object var1) {
      if (!(var1 instanceof NextTickListEntry)) {
         return false;
      } else {
         NextTickListEntry var2 = (NextTickListEntry)var1;
         if (var2 == null) {
            return false;
         } else {
            Set var3 = this.getSubSet(var2, true);
            boolean var4 = var3.add(var2);
            boolean var5 = super.add(var1);
            if (var4 != var5) {
               throw new IllegalStateException("Added: " + var4 + ", addedParent: " + var5);
            } else {
               return var5;
            }
         }
      }
   }

   public boolean remove(Object var1) {
      if (!(var1 instanceof NextTickListEntry)) {
         return false;
      } else {
         NextTickListEntry var2 = (NextTickListEntry)var1;
         Set var3 = this.getSubSet(var2, false);
         if (var3 == null) {
            return false;
         } else {
            boolean var4 = var3.remove(var2);
            boolean var5 = super.remove(var2);
            if (var4 != var5) {
               throw new IllegalStateException("Added: " + var4 + ", addedParent: " + var5);
            } else {
               return var5;
            }
         }
      }
   }

   private Set getSubSet(NextTickListEntry var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         BlockPos var3 = var1.position;
         int var4 = var3.getX() >> 4;
         int var5 = var3.getZ() >> 4;
         return this.getSubSet(var4, var5, var2);
      }
   }

   private Set getSubSet(int var1, int var2, boolean var3) {
      long var4 = ChunkCoordIntPair.chunkXZ2Int(var1, var2);
      HashSet var6 = (HashSet)this.longHashMap.getValueByKey(var4);
      if (var6 == null && var3) {
         var6 = new HashSet();
         this.longHashMap.add(var4, var6);
      }

      return var6;
   }

   public Iterator iterator() {
      if (this.minX == Integer.MIN_VALUE) {
         return super.iterator();
      } else if (this.size() <= 0) {
         return Iterators.emptyIterator();
      } else {
         int var1 = this.minX >> 4;
         int var2 = this.minZ >> 4;
         int var3 = this.maxX >> 4;
         int var4 = this.maxZ >> 4;
         ArrayList var5 = new ArrayList();

         for(int var6 = var1; var6 <= var3; ++var6) {
            for(int var7 = var2; var7 <= var4; ++var7) {
               Set var8 = this.getSubSet(var6, var7, false);
               if (var8 != null) {
                  var5.add(var8.iterator());
               }
            }
         }

         if (var5.size() <= 0) {
            return Iterators.emptyIterator();
         } else if (var5.size() == 1) {
            return (Iterator)var5.get(0);
         } else {
            return Iterators.concat(var5.iterator());
         }
      }
   }

   public void setIteratorLimits(int var1, int var2, int var3, int var4) {
      this.minX = Math.min(var1, var3);
      this.minZ = Math.min(var2, var4);
      this.maxX = Math.max(var1, var3);
      this.maxZ = Math.max(var2, var4);
   }

   public void clearIteratorLimits() {
      this.minX = Integer.MIN_VALUE;
      this.minZ = Integer.MIN_VALUE;
      this.maxX = Integer.MIN_VALUE;
      this.maxZ = Integer.MIN_VALUE;
   }
}
