package shadersmod.client;

import optifine.MatchBlock;

public class BlockAlias {
   private int blockId;
   private MatchBlock[] matchBlocks;

   public BlockAlias(int var1, MatchBlock[] var2) {
      this.blockId = var1;
      this.matchBlocks = var2;
   }

   public int getBlockId() {
      return this.blockId;
   }

   public boolean matches(int var1, int var2) {
      for(int var3 = 0; var3 < this.matchBlocks.length; ++var3) {
         MatchBlock var4 = this.matchBlocks[var3];
         if (var4.matches(var1, var2)) {
            return true;
         }
      }

      return false;
   }

   public int[] getMatchBlockIds() {
      int[] var1 = new int[this.matchBlocks.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = this.matchBlocks[var2].getBlockId();
      }

      return var1;
   }
}
