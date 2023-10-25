package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSit extends EntityAIMoveToBlock {
  private final EntityOcelot field_151493_a;
  
  public EntityAIOcelotSit(EntityOcelot p_i45315_1_, double p_i45315_2_) {
    super((EntityCreature)p_i45315_1_, p_i45315_2_, 8);
    this.field_151493_a = p_i45315_1_;
  }
  
  public boolean shouldExecute() {
    return (this.field_151493_a.isTamed() && !this.field_151493_a.isSitting() && super.shouldExecute());
  }
  
  public boolean continueExecuting() {
    return super.continueExecuting();
  }
  
  public void startExecuting() {
    super.startExecuting();
    this.field_151493_a.getAISit().setSitting(false);
  }
  
  public void resetTask() {
    super.resetTask();
    this.field_151493_a.setSitting(false);
  }
  
  public void updateTask() {
    super.updateTask();
    this.field_151493_a.getAISit().setSitting(false);
    if (!getIsAboveDestination()) {
      this.field_151493_a.setSitting(false);
    } else if (!this.field_151493_a.isSitting()) {
      this.field_151493_a.setSitting(true);
    } 
  }
  
  protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
    if (!worldIn.isAirBlock(pos.up()))
      return false; 
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    if (block == Blocks.chest) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof TileEntityChest && ((TileEntityChest)tileentity).numPlayersUsing < 1)
        return true; 
    } else {
      if (block == Blocks.lit_furnace)
        return true; 
      if (block == Blocks.bed && iblockstate.getValue((IProperty)BlockBed.PART) != BlockBed.EnumPartType.HEAD)
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIOcelotSit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */