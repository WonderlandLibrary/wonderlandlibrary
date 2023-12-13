package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class EventBlockRenderSide implements Event {
   public final BlockPos pos;
   private final IBlockState state;
   private final IBlockAccess world;
   private final EnumFacing side;
   public double maxX;
   public double maxY;
   public double maxZ;
   public double minX;
   public double minY;
   public double minZ;
   private boolean toRender;

   public EventBlockRenderSide(IBlockAccess world, BlockPos pos, EnumFacing side, double maxX, double minX, double maxY, double minY, double maxZ, double minZ) {
      this.state = Minecraft.getMinecraft().theWorld.getBlockState(pos);
      this.world = world;
      this.pos = pos;
      this.side = side;
      this.maxX = maxX;
      this.maxY = maxY;
      this.maxZ = maxZ;
      this.minX = minX;
      this.minY = minY;
      this.minZ = minZ;
   }

   public IBlockState getState() {
      return this.state;
   }

   public IBlockAccess getWorld() {
      return this.world;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public EnumFacing getSide() {
      return this.side;
   }

   public boolean isToRender() {
      return this.toRender;
   }

   public void setToRender(boolean toRender) {
      this.toRender = toRender;
   }
}
