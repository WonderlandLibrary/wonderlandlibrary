package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class ListedRenderChunk extends RenderChunk {
   private final int baseDisplayList = GLAllocation.generateDisplayLists(EnumWorldBlockLayer.values().length);

   public void deleteGlResources() {
      super.deleteGlResources();
      GLAllocation.deleteDisplayLists(this.baseDisplayList, EnumWorldBlockLayer.values().length);
   }

   public int getDisplayList(EnumWorldBlockLayer var1, CompiledChunk var2) {
      return !var2.isLayerEmpty(var1) ? this.baseDisplayList + var1.ordinal() : -1;
   }

   public ListedRenderChunk(World var1, RenderGlobal var2, BlockPos var3, int var4) {
      super(var1, var2, var3, var4);
   }
}
