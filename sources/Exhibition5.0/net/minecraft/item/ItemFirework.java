// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import java.util.ArrayList;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.util.StatCollector;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

public class ItemFirework extends Item
{
    private static final String __OBFID = "CL_00000031";
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!worldIn.isRemote) {
            final EntityFireworkRocket var9 = new EntityFireworkRocket(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, stack);
            worldIn.spawnEntityInWorld(var9);
            if (!playerIn.capabilities.isCreativeMode) {
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
        if (stack.hasTagCompound()) {
            final NBTTagCompound var5 = stack.getTagCompound().getCompoundTag("Fireworks");
            if (var5 != null) {
                if (var5.hasKey("Flight", 99)) {
                    tooltip.add(StatCollector.translateToLocal("item.fireworks.flight") + " " + var5.getByte("Flight"));
                }
                final NBTTagList var6 = var5.getTagList("Explosions", 10);
                if (var6 != null && var6.tagCount() > 0) {
                    for (int var7 = 0; var7 < var6.tagCount(); ++var7) {
                        final NBTTagCompound var8 = var6.getCompoundTagAt(var7);
                        final ArrayList var9 = Lists.newArrayList();
                        ItemFireworkCharge.func_150902_a(var8, var9);
                        if (var9.size() > 0) {
                            for (int var10 = 1; var10 < var9.size(); ++var10) {
                                var9.set(var10, "  " + var9.get(var10));
                            }
                            tooltip.addAll(var9);
                        }
                    }
                }
            }
        }
    }
}
