// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemHangingEntity extends Item
{
    private final Class hangingEntityClass;
    private static final String __OBFID = "CL_00000038";
    
    public ItemHangingEntity(final Class p_i45342_1_) {
        this.hangingEntityClass = p_i45342_1_;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        }
        if (side == EnumFacing.UP) {
            return false;
        }
        final BlockPos var9 = pos.offset(side);
        if (!playerIn.func_175151_a(var9, side, stack)) {
            return false;
        }
        final EntityHanging var10 = this.func_179233_a(worldIn, var9, side);
        if (var10 != null && var10.onValidSurface()) {
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(var10);
            }
            --stack.stackSize;
        }
        return true;
    }
    
    private EntityHanging func_179233_a(final World worldIn, final BlockPos p_179233_2_, final EnumFacing p_179233_3_) {
        return (this.hangingEntityClass == EntityPainting.class) ? new EntityPainting(worldIn, p_179233_2_, p_179233_3_) : ((this.hangingEntityClass == EntityItemFrame.class) ? new EntityItemFrame(worldIn, p_179233_2_, p_179233_3_) : null);
    }
}
