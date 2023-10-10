package net.minecraft.block.state;

import com.google.common.base.Predicate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockWorldState {
   private IBlockState state;
   private final BlockPos pos;
   private TileEntity tileEntity;
   private final boolean field_181628_c;
   private final World world;
   private boolean tileEntityInitialized;

   public IBlockState getBlockState() {
      if (this.state == null && (this.field_181628_c || this.world.isBlockLoaded(this.pos))) {
         this.state = this.world.getBlockState(this.pos);
      }

      return this.state;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public BlockWorldState(World var1, BlockPos var2, boolean var3) {
      this.world = var1;
      this.pos = var2;
      this.field_181628_c = var3;
   }

   public static Predicate hasState(Predicate var0) {
      return new Predicate(var0) {
         private final Predicate val$p_177510_0_;

         public boolean apply(Object var1) {
            return this.apply((BlockWorldState)var1);
         }

         {
            this.val$p_177510_0_ = var1;
         }

         public boolean apply(BlockWorldState var1) {
            return var1 != null && this.val$p_177510_0_.apply(var1.getBlockState());
         }
      };
   }

   public TileEntity getTileEntity() {
      if (this.tileEntity == null && !this.tileEntityInitialized) {
         this.tileEntity = this.world.getTileEntity(this.pos);
         this.tileEntityInitialized = true;
      }

      return this.tileEntity;
   }
}
