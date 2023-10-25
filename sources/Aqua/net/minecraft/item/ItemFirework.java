package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFirework
extends Item {
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, (double)((float)pos.getX() + hitX), (double)((float)pos.getY() + hitY), (double)((float)pos.getZ() + hitZ), stack);
            worldIn.spawnEntityInWorld((Entity)entityfireworkrocket);
            if (!playerIn.capabilities.isCreativeMode) {
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        NBTTagCompound nbttagcompound;
        if (stack.hasTagCompound() && (nbttagcompound = stack.getTagCompound().getCompoundTag("Fireworks")) != null) {
            NBTTagList nbttaglist;
            if (nbttagcompound.hasKey("Flight", 99)) {
                tooltip.add((Object)(StatCollector.translateToLocal((String)"item.fireworks.flight") + " " + nbttagcompound.getByte("Flight")));
            }
            if ((nbttaglist = nbttagcompound.getTagList("Explosions", 10)) != null && nbttaglist.tagCount() > 0) {
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                    ArrayList list = Lists.newArrayList();
                    ItemFireworkCharge.addExplosionInfo((NBTTagCompound)nbttagcompound1, (List)list);
                    if (list.size() <= 0) continue;
                    for (int j = 1; j < list.size(); ++j) {
                        list.set(j, (Object)("  " + (String)list.get(j)));
                    }
                    tooltip.addAll((Collection)list);
                }
            }
        }
    }
}
