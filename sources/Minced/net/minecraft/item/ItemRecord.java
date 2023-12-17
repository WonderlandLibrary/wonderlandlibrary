// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import com.google.common.collect.Maps;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.stats.StatList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.SoundEvent;
import java.util.Map;

public class ItemRecord extends Item
{
    private static final Map<SoundEvent, ItemRecord> RECORDS;
    private final SoundEvent sound;
    private final String displayName;
    
    protected ItemRecord(final String recordName, final SoundEvent soundIn) {
        this.displayName = "item.record." + recordName + ".desc";
        this.sound = soundIn;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);
        ItemRecord.RECORDS.put(this.sound, this);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == Blocks.JUKEBOX && !iblockstate.getValue((IProperty<Boolean>)BlockJukebox.HAS_RECORD)) {
            if (!worldIn.isRemote) {
                final ItemStack itemstack = player.getHeldItem(hand);
                ((BlockJukebox)Blocks.JUKEBOX).insertRecord(worldIn, pos, iblockstate, itemstack);
                worldIn.playEvent(null, 1010, pos, Item.getIdFromItem(this));
                itemstack.shrink(1);
                player.addStat(StatList.RECORD_PLAYED);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(this.getRecordNameLocal());
    }
    
    public String getRecordNameLocal() {
        return I18n.translateToLocal(this.displayName);
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.RARE;
    }
    
    @Nullable
    public static ItemRecord getBySound(final SoundEvent soundIn) {
        return ItemRecord.RECORDS.get(soundIn);
    }
    
    public SoundEvent getSound() {
        return this.sound;
    }
    
    static {
        RECORDS = Maps.newHashMap();
    }
}
