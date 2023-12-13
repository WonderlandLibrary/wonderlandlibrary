package optifine;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock {
   private int blockId = -1;
   private int[] metadatas = null;

   public MatchBlock(int var1) {
      this.blockId = var1;
   }

   public MatchBlock(int var1, int var2) {
      this.blockId = var1;
      if (var2 >= 0 && var2 <= 15) {
         this.metadatas = new int[]{var2};
      }

   }

   public MatchBlock(int var1, int[] var2) {
      this.blockId = var1;
      this.metadatas = var2;
   }

   public int getBlockId() {
      return this.blockId;
   }

   public int[] getMetadatas() {
      return this.metadatas;
   }

   public boolean matches(BlockStateBase var1) {
      return var1.getBlockId() != this.blockId ? false : Matches.metadata(var1.getMetadata(), this.metadatas);
   }

   public boolean matches(int var1, int var2) {
      return var1 != this.blockId ? false : Matches.metadata(var2, this.metadatas);
   }

   public void addMetadata(int var1) {
      if (this.metadatas != null && var1 >= 0 && var1 <= 15) {
         for(int var2 = 0; var2 < this.metadatas.length; ++var2) {
            if (this.metadatas[var2] == var1) {
               return;
            }
         }

         this.metadatas = Config.addIntToArray(this.metadatas, var1);
      }

   }

   public String toString() {
      return this.blockId + ":" + Config.arrayToString(this.metadatas);
   }
}
