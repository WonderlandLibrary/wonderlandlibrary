package net.minecraft.block;

import net.minecraft.util.BlockPos;

public class BlockEventData {
   private BlockPos position;
   private int eventID;
   private Block blockType;
   private int eventParameter;

   public int getEventParameter() {
      return this.eventParameter;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BlockEventData)) {
         return false;
      } else {
         BlockEventData var2 = (BlockEventData)var1;
         return this.position.equals(var2.position) && this.eventID == var2.eventID && this.eventParameter == var2.eventParameter && this.blockType == var2.blockType;
      }
   }

   public int getEventID() {
      return this.eventID;
   }

   public Block getBlock() {
      return this.blockType;
   }

   public String toString() {
      return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public BlockEventData(BlockPos var1, Block var2, int var3, int var4) {
      this.position = var1;
      this.eventID = var3;
      this.eventParameter = var4;
      this.blockType = var2;
   }
}
