package cn.hanabi.injection.mixins;

import cn.hanabi.events.BBSetEvent;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.ghost.Reach;
import com.darkmagician6.eventapi.EventManager;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Block.class})
public abstract class MixinBlock {
   @Shadow
   @Final
   protected BlockState blockState;
   @Shadow
   protected double minX;
   @Shadow
   protected double minY;
   @Shadow
   protected double minZ;
   @Shadow
   protected double maxX;
   @Shadow
   protected double maxY;
   @Shadow
   protected double maxZ;
   @Final
   @Shadow
   protected Material blockMaterial;
   Minecraft mc = Minecraft.getMinecraft();
   int blockID = 0;
   private Block BLOCK;

   @Shadow
   public abstract void setBlockBounds(float var1, float var2, float var3, float var4, float var5, float var6);

   @Shadow
   public abstract boolean isFullCube();

   @Shadow
   public abstract boolean isBlockNormalCube();

   @Shadow
   public abstract AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3);

   @Overwrite
   public boolean isCollidable() {
      Reach reach = (Reach)ModManager.getModule(Reach.class);
      return !reach.isEnabled() || !(Boolean)Reach.throughWall.getValue();
   }

   @Overwrite
   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
      AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);
      BBSetEvent blockBBEvent = new BBSetEvent(this.blockState.getBlock(), pos, axisalignedbb);
      EventManager.call(blockBBEvent);
      axisalignedbb = blockBBEvent.getBoundingBox();
      if (axisalignedbb != null && mask.intersectsWith(axisalignedbb)) {
         list.add(axisalignedbb);
      }

   }
}
