package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {
   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
   private final int field_150068_a;

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{POWER});
   }

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(POWER);
   }

   protected BlockPressurePlateWeighted(Material var1, int var2) {
      this(var1, var2, var1.getMaterialMapColor());
   }

   public int tickRate(World var1) {
      return 10;
   }

   protected BlockPressurePlateWeighted(Material var1, int var2, MapColor var3) {
      super(var1, var3);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
      this.field_150068_a = var2;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWER, var1);
   }

   protected int getRedstoneStrength(IBlockState var1) {
      return (Integer)var1.getValue(POWER);
   }

   protected IBlockState setRedstoneStrength(IBlockState var1, int var2) {
      return var1.withProperty(POWER, var2);
   }

   protected int computeRedstoneStrength(World var1, BlockPos var2) {
      int var3 = Math.min(var1.getEntitiesWithinAABB(Entity.class, this.getSensitiveAABB(var2)).size(), this.field_150068_a);
      if (var3 > 0) {
         float var4 = (float)Math.min(this.field_150068_a, var3) / (float)this.field_150068_a;
         return MathHelper.ceiling_float_int(var4 * 15.0F);
      } else {
         return 0;
      }
   }
}
