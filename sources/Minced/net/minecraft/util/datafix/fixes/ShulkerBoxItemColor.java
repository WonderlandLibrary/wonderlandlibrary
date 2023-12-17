// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxItemColor implements IFixableData
{
    public static final String[] NAMES_BY_COLOR;
    
    @Override
    public int getFixVersion() {
        return 813;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("minecraft:shulker_box".equals(compound.getString("id")) && compound.hasKey("tag", 10)) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
            if (nbttagcompound.hasKey("BlockEntityTag", 10)) {
                final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("BlockEntityTag");
                if (nbttagcompound2.getTagList("Items", 10).isEmpty()) {
                    nbttagcompound2.removeTag("Items");
                }
                final int i = nbttagcompound2.getInteger("Color");
                nbttagcompound2.removeTag("Color");
                if (nbttagcompound2.isEmpty()) {
                    nbttagcompound.removeTag("BlockEntityTag");
                }
                if (nbttagcompound.isEmpty()) {
                    compound.removeTag("tag");
                }
                compound.setString("id", ShulkerBoxItemColor.NAMES_BY_COLOR[i % 16]);
            }
        }
        return compound;
    }
    
    static {
        NAMES_BY_COLOR = new String[] { "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box" };
    }
}
