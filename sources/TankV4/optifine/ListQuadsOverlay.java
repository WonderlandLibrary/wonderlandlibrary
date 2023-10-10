package optifine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.init.Blocks;

public class ListQuadsOverlay {
   private List listQuads = new ArrayList();
   private List listBlockStates = new ArrayList();
   private List listQuadsSingle = Arrays.asList();

   public void addQuad(BakedQuad var1, IBlockState var2) {
      this.listQuads.add(var1);
      this.listBlockStates.add(var2);
   }

   public int size() {
      return this.listQuads.size();
   }

   public BakedQuad getQuad(int var1) {
      return (BakedQuad)this.listQuads.get(var1);
   }

   public IBlockState getBlockState(int var1) {
      return var1 >= 0 && var1 < this.listBlockStates.size() ? (IBlockState)this.listBlockStates.get(var1) : Blocks.air.getDefaultState();
   }

   public List getListQuadsSingle(BakedQuad var1) {
      this.listQuadsSingle.set(0, var1);
      return this.listQuadsSingle;
   }

   public void clear() {
      this.listQuads.clear();
      this.listBlockStates.clear();
   }
}
