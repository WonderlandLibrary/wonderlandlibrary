package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract class StateMapperBase implements IStateMapper {
   protected Map mapStateModelLocations = Maps.newLinkedHashMap();

   protected abstract ModelResourceLocation getModelResourceLocation(IBlockState var1);

   public String getPropertyString(Map var1) {
      StringBuilder var2 = new StringBuilder();
      Iterator var4 = var1.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var3 = (Entry)var4.next();
         if (var2.length() != 0) {
            var2.append(",");
         }

         IProperty var5 = (IProperty)var3.getKey();
         Comparable var6 = (Comparable)var3.getValue();
         var2.append(var5.getName());
         var2.append("=");
         var2.append(var5.getName(var6));
      }

      if (var2.length() == 0) {
         var2.append("normal");
      }

      return String.valueOf(var2);
   }

   public Map putStateModelLocations(Block var1) {
      Iterator var3 = var1.getBlockState().getValidStates().iterator();

      while(var3.hasNext()) {
         IBlockState var2 = (IBlockState)var3.next();
         this.mapStateModelLocations.put(var2, this.getModelResourceLocation(var2));
      }

      return this.mapStateModelLocations;
   }
}
