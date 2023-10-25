package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemRecord extends Item {
  private static final Map<String, ItemRecord> RECORDS = Maps.newHashMap();
  
  public final String recordName;
  
  protected ItemRecord(String name) {
    this.recordName = name;
    this.maxStackSize = 1;
    setCreativeTab(CreativeTabs.tabMisc);
    RECORDS.put("records." + name, this);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (iblockstate.getBlock() == Blocks.jukebox && !((Boolean)iblockstate.getValue((IProperty)BlockJukebox.HAS_RECORD)).booleanValue()) {
      if (worldIn.isRemote)
        return true; 
      ((BlockJukebox)Blocks.jukebox).insertRecord(worldIn, pos, iblockstate, stack);
      worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1005, pos, Item.getIdFromItem(this));
      stack.stackSize--;
      playerIn.triggerAchievement(StatList.field_181740_X);
      return true;
    } 
    return false;
  }
  
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(getRecordNameLocal());
  }
  
  public String getRecordNameLocal() {
    return StatCollector.translateToLocal("item.record." + this.recordName + ".desc");
  }
  
  public EnumRarity getRarity(ItemStack stack) {
    return EnumRarity.RARE;
  }
  
  public static ItemRecord getRecord(String name) {
    return RECORDS.get(name);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */