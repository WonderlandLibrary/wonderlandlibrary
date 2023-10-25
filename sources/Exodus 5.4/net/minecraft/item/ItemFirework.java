/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
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
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        NBTTagCompound nBTTagCompound;
        if (itemStack.hasTagCompound() && (nBTTagCompound = itemStack.getTagCompound().getCompoundTag("Fireworks")) != null) {
            NBTTagList nBTTagList;
            if (nBTTagCompound.hasKey("Flight", 99)) {
                list.add(String.valueOf(StatCollector.translateToLocal("item.fireworks.flight")) + " " + nBTTagCompound.getByte("Flight"));
            }
            if ((nBTTagList = nBTTagCompound.getTagList("Explosions", 10)) != null && nBTTagList.tagCount() > 0) {
                int n = 0;
                while (n < nBTTagList.tagCount()) {
                    NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
                    ArrayList arrayList = Lists.newArrayList();
                    ItemFireworkCharge.addExplosionInfo(nBTTagCompound2, arrayList);
                    if (arrayList.size() > 0) {
                        int n2 = 1;
                        while (n2 < arrayList.size()) {
                            arrayList.set(n2, "  " + (String)arrayList.get(n2));
                            ++n2;
                        }
                        list.addAll(arrayList);
                    }
                    ++n;
                }
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (!world.isRemote) {
            EntityFireworkRocket entityFireworkRocket = new EntityFireworkRocket(world, (float)blockPos.getX() + f, (float)blockPos.getY() + f2, (float)blockPos.getZ() + f3, itemStack);
            world.spawnEntityInWorld(entityFireworkRocket);
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            return true;
        }
        return false;
    }
}

