// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import com.google.common.collect.Maps;
import net.minecraft.util.StatCollector;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import java.util.Map;

public class ItemRecord extends Item
{
    private static final Map field_150928_b;
    public final String recordName;
    private static final String __OBFID = "CL_00000057";
    
    protected ItemRecord(final String p_i45350_1_) {
        this.recordName = p_i45350_1_;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        ItemRecord.field_150928_b.put("records." + p_i45350_1_, this);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState var9 = worldIn.getBlockState(pos);
        if (var9.getBlock() != Blocks.jukebox || (boolean)var9.getValue(BlockJukebox.HAS_RECORD)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        ((BlockJukebox)Blocks.jukebox).insertRecord(worldIn, pos, var9, stack);
        worldIn.playAuxSFXAtEntity(null, 1005, pos, Item.getIdFromItem(this));
        --stack.stackSize;
        return true;
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
        tooltip.add(this.getRecordNameLocal());
    }
    
    public String getRecordNameLocal() {
        return StatCollector.translateToLocal("item.record." + this.recordName + ".desc");
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.RARE;
    }
    
    public static ItemRecord getRecord(final String p_150926_0_) {
        return ItemRecord.field_150928_b.get(p_150926_0_);
    }
    
    static {
        field_150928_b = Maps.newHashMap();
    }
}
