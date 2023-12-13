// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;
    private static final String __OBFID = "CL_00000240";
    
    public BlockFalling() {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    public BlockFalling(final Material materialIn) {
        super(materialIn);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            this.checkFallable(worldIn, pos);
        }
    }
    
    private void checkFallable(final World worldIn, final BlockPos pos) {
        if (canFallInto(worldIn, pos.offsetDown()) && pos.getY() >= 0) {
            final byte var3 = 32;
            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-var3, -var3, -var3), pos.add(var3, var3, var3))) {
                if (!worldIn.isRemote) {
                    final EntityFallingBlock var4 = new EntityFallingBlock(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos));
                    this.onStartFalling(var4);
                    worldIn.spawnEntityInWorld(var4);
                }
            }
            else {
                worldIn.setBlockToAir(pos);
                BlockPos var5;
                for (var5 = pos.offsetDown(); canFallInto(worldIn, var5) && var5.getY() > 0; var5 = var5.offsetDown()) {}
                if (var5.getY() > 0) {
                    worldIn.setBlockState(var5.offsetUp(), this.getDefaultState());
                }
            }
        }
    }
    
    protected void onStartFalling(final EntityFallingBlock fallingEntity) {
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 2;
    }
    
    public static boolean canFallInto(final World worldIn, final BlockPos pos) {
        final Block var2 = worldIn.getBlockState(pos).getBlock();
        final Material var3 = var2.blockMaterial;
        return var2 == Blocks.fire || var3 == Material.air || var3 == Material.water || var3 == Material.lava;
    }
    
    public void onEndFalling(final World worldIn, final BlockPos pos) {
    }
}
