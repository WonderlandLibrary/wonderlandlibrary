// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.inventory.IInventory;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;

public class BlockDropper extends BlockDispenser
{
    private final IBehaviorDispenseItem field_149947_P;
    private static final String __OBFID = "CL_00000233";
    
    public BlockDropper() {
        this.field_149947_P = new BehaviorDefaultDispenseItem();
    }
    
    @Override
    protected IBehaviorDispenseItem func_149940_a(final ItemStack p_149940_1_) {
        return this.field_149947_P;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityDropper();
    }
    
    @Override
    protected void func_176439_d(final World worldIn, final BlockPos p_176439_2_) {
        final BlockSourceImpl var3 = new BlockSourceImpl(worldIn, p_176439_2_);
        final TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();
        if (var4 != null) {
            final int var5 = var4.func_146017_i();
            if (var5 < 0) {
                worldIn.playAuxSFX(1001, p_176439_2_, 0);
            }
            else {
                final ItemStack var6 = var4.getStackInSlot(var5);
                if (var6 != null) {
                    final EnumFacing var7 = (EnumFacing)worldIn.getBlockState(p_176439_2_).getValue(BlockDispenser.FACING);
                    final BlockPos var8 = p_176439_2_.offset(var7);
                    final IInventory var9 = TileEntityHopper.func_145893_b(worldIn, var8.getX(), var8.getY(), var8.getZ());
                    ItemStack var10;
                    if (var9 == null) {
                        var10 = this.field_149947_P.dispense(var3, var6);
                        if (var10 != null && var10.stackSize == 0) {
                            var10 = null;
                        }
                    }
                    else {
                        var10 = TileEntityHopper.func_174918_a(var9, var6.copy().splitStack(1), var7.getOpposite());
                        if (var10 == null) {
                            final ItemStack copy;
                            var10 = (copy = var6.copy());
                            if (--copy.stackSize == 0) {
                                var10 = null;
                            }
                        }
                        else {
                            var10 = var6.copy();
                        }
                    }
                    var4.setInventorySlotContents(var5, var10);
                }
            }
        }
    }
}
