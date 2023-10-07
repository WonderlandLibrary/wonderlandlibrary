// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSnow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemSnow extends ItemBlock
{
    private static final String __OBFID = "CL_00000068";
    
    public ItemSnow(final Block p_i45781_1_) {
        super(p_i45781_1_);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (stack.stackSize == 0) {
            return false;
        }
        if (!playerIn.func_175151_a(pos, side, stack)) {
            return false;
        }
        IBlockState var9 = worldIn.getBlockState(pos);
        Block var10 = var9.getBlock();
        if (var10 != this.block && side != EnumFacing.UP) {
            pos = pos.offset(side);
            var9 = worldIn.getBlockState(pos);
            var10 = var9.getBlock();
        }
        if (var10 == this.block) {
            final int var11 = (int)var9.getValue(BlockSnow.LAYERS_PROP);
            if (var11 <= 7) {
                final IBlockState var12 = var9.withProperty(BlockSnow.LAYERS_PROP, var11 + 1);
                if (worldIn.checkNoEntityCollision(this.block.getCollisionBoundingBox(worldIn, pos, var12)) && worldIn.setBlockState(pos, var12, 2)) {
                    worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
                    --stack.stackSize;
                    return true;
                }
            }
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
}
