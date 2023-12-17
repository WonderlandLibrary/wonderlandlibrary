// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

public class ItemFirework extends Item
{
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (!worldIn.isRemote) {
            final ItemStack itemstack = player.getHeldItem(hand);
            final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, itemstack);
            worldIn.spawnEntity(entityfireworkrocket);
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        if (playerIn.isElytraFlying()) {
            final ItemStack itemstack = playerIn.getHeldItem(handIn);
            if (!worldIn.isRemote) {
                final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, itemstack, playerIn);
                worldIn.spawnEntity(entityfireworkrocket);
                if (!playerIn.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("Fireworks");
        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("Flight", 99)) {
                tooltip.add(I18n.translateToLocal("item.fireworks.flight") + " " + nbttagcompound.getByte("Flight"));
            }
            final NBTTagList nbttaglist = nbttagcompound.getTagList("Explosions", 10);
            if (!nbttaglist.isEmpty()) {
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                    final List<String> list = (List<String>)Lists.newArrayList();
                    ItemFireworkCharge.addExplosionInfo(nbttagcompound2, list);
                    if (!list.isEmpty()) {
                        for (int j = 1; j < list.size(); ++j) {
                            list.set(j, "  " + list.get(j));
                        }
                        tooltip.addAll(list);
                    }
                }
            }
        }
    }
}
