package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;

public class BlockStateMapper {
   private Set setBuiltInBlocks = Sets.newIdentityHashSet();
   private Map blockStateMap = Maps.newIdentityHashMap();

   public void registerBlockStateMapper(Block var1, IStateMapper var2) {
      this.blockStateMap.put(var1, var2);
   }

   public Map putAllStateModelLocations() {
      IdentityHashMap var1 = Maps.newIdentityHashMap();
      Iterator var3 = Block.blockRegistry.iterator();

      while(var3.hasNext()) {
         Block var2 = (Block)var3.next();
         if (!this.setBuiltInBlocks.contains(var2)) {
            var1.putAll(((IStateMapper)Objects.firstNonNull((IStateMapper)this.blockStateMap.get(var2), new DefaultStateMapper())).putStateModelLocations(var2));
         }
      }

      return var1;
   }

   public void registerBuiltInBlocks(Block... var1) {
      Collections.addAll(this.setBuiltInBlocks, var1);
   }
}
