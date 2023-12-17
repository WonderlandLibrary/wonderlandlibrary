// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;

public class ItemHangingEntity extends Item
{
    private final Class<? extends EntityHanging> hangingEntityClass;
    
    public ItemHangingEntity(final Class<? extends EntityHanging> entityClass) {
        this.hangingEntityClass = entityClass;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final ItemStack itemstack = player.getHeldItem(hand);
        final BlockPos blockpos = pos.offset(facing);
        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(blockpos, facing, itemstack)) {
            final EntityHanging entityhanging = this.createEntity(worldIn, blockpos, facing);
            if (entityhanging != null && entityhanging.onValidSurface()) {
                if (!worldIn.isRemote) {
                    entityhanging.playPlaceSound();
                    worldIn.spawnEntity(entityhanging);
                }
                itemstack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
    
    @Nullable
    private EntityHanging createEntity(final World worldIn, final BlockPos pos, final EnumFacing clickedSide) {
        if (this.hangingEntityClass == EntityPainting.class) {
            return new EntityPainting(worldIn, pos, clickedSide);
        }
        return (this.hangingEntityClass == EntityItemFrame.class) ? new EntityItemFrame(worldIn, pos, clickedSide) : null;
    }
}
