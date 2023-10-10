package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class StateMap extends StateMapperBase {
   private final List ignored;
   private final String suffix;
   private final IProperty name;

   protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
      LinkedHashMap var2 = Maps.newLinkedHashMap(var1.getProperties());
      String var3;
      if (this.name == null) {
         var3 = ((ResourceLocation)Block.blockRegistry.getNameForObject(var1.getBlock())).toString();
      } else {
         var3 = this.name.getName((Comparable)var2.remove(this.name));
      }

      if (this.suffix != null) {
         var3 = var3 + this.suffix;
      }

      Iterator var5 = this.ignored.iterator();

      while(var5.hasNext()) {
         IProperty var4 = (IProperty)var5.next();
         var2.remove(var4);
      }

      return new ModelResourceLocation(var3, this.getPropertyString(var2));
   }

   StateMap(IProperty var1, String var2, List var3, StateMap var4) {
      this(var1, var2, var3);
   }

   private StateMap(IProperty var1, String var2, List var3) {
      this.name = var1;
      this.suffix = var2;
      this.ignored = var3;
   }

   public static class Builder {
      private final List ignored = Lists.newArrayList();
      private IProperty name;
      private String suffix;

      public StateMap build() {
         return new StateMap(this.name, this.suffix, this.ignored, (StateMap)null);
      }

      public StateMap.Builder ignore(IProperty... var1) {
         Collections.addAll(this.ignored, var1);
         return this;
      }

      public StateMap.Builder withName(IProperty var1) {
         this.name = var1;
         return this;
      }

      public StateMap.Builder withSuffix(String var1) {
         this.suffix = var1;
         return this;
      }
   }
}
