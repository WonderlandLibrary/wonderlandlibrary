package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.Cartesian;
import net.minecraft.util.MapPopulator;

public class BlockState {
   private final ImmutableList properties;
   private final ImmutableList validStates;
   private static final Function GET_NAME_FUNC = new Function() {
      public Object apply(Object var1) {
         return this.apply((IProperty)var1);
      }

      public String apply(IProperty var1) {
         return var1 == null ? "<NULL>" : var1.getName();
      }
   };
   private static final Joiner COMMA_JOINER = Joiner.on(", ");
   private final Block block;

   public String toString() {
      return Objects.toStringHelper((Object)this).add("block", Block.blockRegistry.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties, GET_NAME_FUNC)).toString();
   }

   public ImmutableList getValidStates() {
      return this.validStates;
   }

   public Block getBlock() {
      return this.block;
   }

   private List getAllowedValues() {
      ArrayList var1 = Lists.newArrayList();

      for(int var2 = 0; var2 < this.properties.size(); ++var2) {
         var1.add(((IProperty)this.properties.get(var2)).getAllowedValues());
      }

      return var1;
   }

   public Collection getProperties() {
      return this.properties;
   }

   public IBlockState getBaseState() {
      return (IBlockState)this.validStates.get(0);
   }

   public BlockState(Block var1, IProperty... var2) {
      this.block = var1;
      Arrays.sort(var2, new Comparator(this) {
         final BlockState this$0;

         public int compare(IProperty var1, IProperty var2) {
            return var1.getName().compareTo(var2.getName());
         }

         {
            this.this$0 = var1;
         }

         public int compare(Object var1, Object var2) {
            return this.compare((IProperty)var1, (IProperty)var2);
         }
      });
      this.properties = ImmutableList.copyOf((Object[])var2);
      LinkedHashMap var3 = Maps.newLinkedHashMap();
      ArrayList var4 = Lists.newArrayList();
      Iterator var6 = Cartesian.cartesianProduct(this.getAllowedValues()).iterator();

      while(var6.hasNext()) {
         List var5 = (List)var6.next();
         Map var7 = MapPopulator.createMap(this.properties, var5);
         BlockState.StateImplementation var8 = new BlockState.StateImplementation(var1, ImmutableMap.copyOf(var7), (BlockState.StateImplementation)null);
         var3.put(var7, var8);
         var4.add(var8);
      }

      var6 = var4.iterator();

      while(var6.hasNext()) {
         BlockState.StateImplementation var10 = (BlockState.StateImplementation)var6.next();
         var10.buildPropertyValueTable(var3);
      }

      this.validStates = ImmutableList.copyOf((Collection)var4);
   }

   static class StateImplementation extends BlockStateBase {
      private ImmutableTable propertyValueTable;
      private final ImmutableMap properties;
      private final Block block;

      StateImplementation(Block var1, ImmutableMap var2, BlockState.StateImplementation var3) {
         this(var1, var2);
      }

      public Collection getPropertyNames() {
         return Collections.unmodifiableCollection(this.properties.keySet());
      }

      public IBlockState withProperty(IProperty var1, Comparable var2) {
         if (!this.properties.containsKey(var1)) {
            throw new IllegalArgumentException("Cannot set property " + var1 + " as it does not exist in " + this.block.getBlockState());
         } else if (!var1.getAllowedValues().contains(var2)) {
            throw new IllegalArgumentException("Cannot set property " + var1 + " to " + var2 + " on block " + Block.blockRegistry.getNameForObject(this.block) + ", it is not an allowed value");
         } else {
            return (IBlockState)(this.properties.get(var1) == var2 ? this : (IBlockState)this.propertyValueTable.get(var1, var2));
         }
      }

      private Map getPropertiesWithValue(IProperty var1, Comparable var2) {
         HashMap var3 = Maps.newHashMap(this.properties);
         var3.put(var1, var2);
         return var3;
      }

      public boolean equals(Object var1) {
         return this == var1;
      }

      public int hashCode() {
         return this.properties.hashCode();
      }

      public ImmutableMap getProperties() {
         return this.properties;
      }

      private StateImplementation(Block var1, ImmutableMap var2) {
         this.block = var1;
         this.properties = var2;
      }

      public Block getBlock() {
         return this.block;
      }

      public Comparable getValue(IProperty var1) {
         if (!this.properties.containsKey(var1)) {
            throw new IllegalArgumentException("Cannot get property " + var1 + " as it does not exist in " + this.block.getBlockState());
         } else {
            return (Comparable)var1.getValueClass().cast(this.properties.get(var1));
         }
      }

      public void buildPropertyValueTable(Map var1) {
         if (this.propertyValueTable != null) {
            throw new IllegalStateException();
         } else {
            HashBasedTable var2 = HashBasedTable.create();
            Iterator var4 = this.properties.keySet().iterator();

            while(var4.hasNext()) {
               IProperty var3 = (IProperty)var4.next();
               Iterator var6 = var3.getAllowedValues().iterator();

               while(var6.hasNext()) {
                  Comparable var5 = (Comparable)var6.next();
                  if (var5 != this.properties.get(var3)) {
                     var2.put(var3, var5, (IBlockState)var1.get(this.getPropertiesWithValue(var3, var5)));
                  }
               }
            }

            this.propertyValueTable = ImmutableTable.copyOf(var2);
         }
      }
   }
}
