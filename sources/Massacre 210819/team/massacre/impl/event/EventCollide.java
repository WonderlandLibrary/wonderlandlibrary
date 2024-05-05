package team.massacre.impl.event;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public class EventCollide extends Cancellable implements Event {
   public AxisAlignedBB axisAlignedBB;
   public Block block;
   public int x;
   public int y;
   public int z;

   public EventCollide(AxisAlignedBB axisAlignedBB, Block block, int x, int y, int z) {
      this.axisAlignedBB = axisAlignedBB;
      this.block = block;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public AxisAlignedBB getAxisAlignedBB() {
      return this.axisAlignedBB;
   }

   public void setAxisAlignedBB(AxisAlignedBB axisAlignedBB) {
      this.axisAlignedBB = axisAlignedBB;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getZ() {
      return this.z;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public Block getBlock() {
      return this.block;
   }
}
