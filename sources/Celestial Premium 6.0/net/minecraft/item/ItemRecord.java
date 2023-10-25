/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemRecord
extends Item {
    private static final Map<SoundEvent, ItemRecord> RECORDS = Maps.newHashMap();
    private final SoundEvent sound;
    private final String displayName;

    protected ItemRecord(String p_i46742_1_, SoundEvent soundIn) {
        this.displayName = "item.record." + p_i46742_1_ + ".desc";
        this.sound = soundIn;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);
        RECORDS.put(this.sound, this);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        if (iblockstate.getBlock() == Blocks.JUKEBOX && !iblockstate.getValue(BlockJukebox.HAS_RECORD).booleanValue()) {
            if (!playerIn.isRemote) {
                ItemStack itemstack = stack.getHeldItem(pos);
                ((BlockJukebox)Blocks.JUKEBOX).insertRecord(playerIn, worldIn, iblockstate, itemstack);
                playerIn.playEvent(null, 1010, worldIn, Item.getIdFromItem(this));
                itemstack.func_190918_g(1);
                stack.addStat(StatList.RECORD_PLAYED);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(this.getRecordNameLocal());
    }

    public String getRecordNameLocal() {
        return I18n.translateToLocal(this.displayName);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Nullable
    public static ItemRecord getBySound(SoundEvent soundIn) {
        return RECORDS.get(soundIn);
    }

    public SoundEvent getSound() {
        return this.sound;
    }
}

