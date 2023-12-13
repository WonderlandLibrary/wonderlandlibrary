// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.collect.Lists;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import java.util.List;

public class BlockNote extends BlockContainer
{
    private static final List field_176434_a;
    private static final String __OBFID = "CL_00000278";
    
    public BlockNote() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final boolean var5 = worldIn.isBlockPowered(pos);
        final TileEntity var6 = worldIn.getTileEntity(pos);
        if (var6 instanceof TileEntityNote) {
            final TileEntityNote var7 = (TileEntityNote)var6;
            if (var7.previousRedstoneState != var5) {
                if (var5) {
                    var7.func_175108_a(worldIn, pos);
                }
                var7.previousRedstoneState = var5;
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity var9 = worldIn.getTileEntity(pos);
        if (var9 instanceof TileEntityNote) {
            final TileEntityNote var10 = (TileEntityNote)var9;
            var10.changePitch();
            var10.func_175108_a(worldIn, pos);
        }
        return true;
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            final TileEntity var4 = worldIn.getTileEntity(pos);
            if (var4 instanceof TileEntityNote) {
                ((TileEntityNote)var4).func_175108_a(worldIn, pos);
            }
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityNote();
    }
    
    private String func_176433_b(int p_176433_1_) {
        if (p_176433_1_ < 0 || p_176433_1_ >= BlockNote.field_176434_a.size()) {
            p_176433_1_ = 0;
        }
        return BlockNote.field_176434_a.get(p_176433_1_);
    }
    
    @Override
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        final float var6 = (float)Math.pow(2.0, (eventParam - 12) / 12.0);
        worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "note." + this.func_176433_b(eventID), 3.0f, var6);
        worldIn.spawnParticle(EnumParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, eventParam / 24.0, 0.0, 0.0, new int[0]);
        return true;
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    static {
        field_176434_a = Lists.newArrayList((Object[])new String[] { "harp", "bd", "snare", "hat", "bassattack" });
    }
}
