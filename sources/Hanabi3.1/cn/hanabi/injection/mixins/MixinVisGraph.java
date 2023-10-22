package cn.hanabi.injection.mixins;

import java.util.BitSet;
import java.util.Set;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({VisGraph.class})
public abstract class MixinVisGraph {
   @Final
   @Shadow
   private static int[] field_178613_e;
   @Final
   @Shadow
   private BitSet field_178612_d;
   @Shadow
   private int field_178611_f;

   private static int getIndex(BlockPos pos) {
      return getIndex(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
   }

   private static int getIndex(int x, int y, int z) {
      return x | y << 8 | z << 4;
   }

   @Shadow
   protected abstract Set func_178604_a(int var1);
}
