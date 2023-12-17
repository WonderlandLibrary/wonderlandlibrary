// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.walkers;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.datafix.IDataWalker;

public class BlockEntityTag implements IDataWalker
{
    private static final Logger LOGGER;
    private static final Map<String, String> NEW_TO_OLD_ID_MAP;
    private static final Map<String, String> ITEM_ID_TO_BLOCK_ENTITY_ID;
    
    @Nullable
    private static String getBlockEntityID(final int blockID, final String p_188267_1_) {
        return (blockID < 515) ? BlockEntityTag.NEW_TO_OLD_ID_MAP.get(new ResourceLocation(p_188267_1_).toString()) : BlockEntityTag.ITEM_ID_TO_BLOCK_ENTITY_ID.get(new ResourceLocation(p_188267_1_).toString());
    }
    
    @Override
    public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
        if (!compound.hasKey("tag", 10)) {
            return compound;
        }
        final NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
        if (nbttagcompound.hasKey("BlockEntityTag", 10)) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("BlockEntityTag");
            final String s = compound.getString("id");
            final String s2 = getBlockEntityID(versionIn, s);
            boolean flag;
            if (s2 == null) {
                BlockEntityTag.LOGGER.warn("Unable to resolve BlockEntity for ItemInstance: {}", (Object)s);
                flag = false;
            }
            else {
                flag = !nbttagcompound2.hasKey("id");
                nbttagcompound2.setString("id", s2);
            }
            fixer.process(FixTypes.BLOCK_ENTITY, nbttagcompound2, versionIn);
            if (flag) {
                nbttagcompound2.removeTag("id");
            }
        }
        return compound;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        NEW_TO_OLD_ID_MAP = Maps.newHashMap();
        ITEM_ID_TO_BLOCK_ENTITY_ID = Maps.newHashMap();
        Map<String, String> map = BlockEntityTag.NEW_TO_OLD_ID_MAP;
        map.put("minecraft:furnace", "Furnace");
        map.put("minecraft:lit_furnace", "Furnace");
        map.put("minecraft:chest", "Chest");
        map.put("minecraft:trapped_chest", "Chest");
        map.put("minecraft:ender_chest", "EnderChest");
        map.put("minecraft:jukebox", "RecordPlayer");
        map.put("minecraft:dispenser", "Trap");
        map.put("minecraft:dropper", "Dropper");
        map.put("minecraft:sign", "Sign");
        map.put("minecraft:mob_spawner", "MobSpawner");
        map.put("minecraft:noteblock", "Music");
        map.put("minecraft:brewing_stand", "Cauldron");
        map.put("minecraft:enhanting_table", "EnchantTable");
        map.put("minecraft:command_block", "CommandBlock");
        map.put("minecraft:beacon", "Beacon");
        map.put("minecraft:skull", "Skull");
        map.put("minecraft:daylight_detector", "DLDetector");
        map.put("minecraft:hopper", "Hopper");
        map.put("minecraft:banner", "Banner");
        map.put("minecraft:flower_pot", "FlowerPot");
        map.put("minecraft:repeating_command_block", "CommandBlock");
        map.put("minecraft:chain_command_block", "CommandBlock");
        map.put("minecraft:standing_sign", "Sign");
        map.put("minecraft:wall_sign", "Sign");
        map.put("minecraft:piston_head", "Piston");
        map.put("minecraft:daylight_detector_inverted", "DLDetector");
        map.put("minecraft:unpowered_comparator", "Comparator");
        map.put("minecraft:powered_comparator", "Comparator");
        map.put("minecraft:wall_banner", "Banner");
        map.put("minecraft:standing_banner", "Banner");
        map.put("minecraft:structure_block", "Structure");
        map.put("minecraft:end_portal", "Airportal");
        map.put("minecraft:end_gateway", "EndGateway");
        map.put("minecraft:shield", "Shield");
        map = BlockEntityTag.ITEM_ID_TO_BLOCK_ENTITY_ID;
        map.put("minecraft:furnace", "minecraft:furnace");
        map.put("minecraft:lit_furnace", "minecraft:furnace");
        map.put("minecraft:chest", "minecraft:chest");
        map.put("minecraft:trapped_chest", "minecraft:chest");
        map.put("minecraft:ender_chest", "minecraft:enderchest");
        map.put("minecraft:jukebox", "minecraft:jukebox");
        map.put("minecraft:dispenser", "minecraft:dispenser");
        map.put("minecraft:dropper", "minecraft:dropper");
        map.put("minecraft:sign", "minecraft:sign");
        map.put("minecraft:mob_spawner", "minecraft:mob_spawner");
        map.put("minecraft:noteblock", "minecraft:noteblock");
        map.put("minecraft:brewing_stand", "minecraft:brewing_stand");
        map.put("minecraft:enhanting_table", "minecraft:enchanting_table");
        map.put("minecraft:command_block", "minecraft:command_block");
        map.put("minecraft:beacon", "minecraft:beacon");
        map.put("minecraft:skull", "minecraft:skull");
        map.put("minecraft:daylight_detector", "minecraft:daylight_detector");
        map.put("minecraft:hopper", "minecraft:hopper");
        map.put("minecraft:banner", "minecraft:banner");
        map.put("minecraft:flower_pot", "minecraft:flower_pot");
        map.put("minecraft:repeating_command_block", "minecraft:command_block");
        map.put("minecraft:chain_command_block", "minecraft:command_block");
        map.put("minecraft:shulker_box", "minecraft:shulker_box");
        map.put("minecraft:white_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:orange_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:magenta_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:light_blue_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:yellow_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:lime_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:pink_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:gray_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:silver_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:cyan_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:purple_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:blue_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:brown_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:green_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:red_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:black_shulker_box", "minecraft:shulker_box");
        map.put("minecraft:bed", "minecraft:bed");
        map.put("minecraft:standing_sign", "minecraft:sign");
        map.put("minecraft:wall_sign", "minecraft:sign");
        map.put("minecraft:piston_head", "minecraft:piston");
        map.put("minecraft:daylight_detector_inverted", "minecraft:daylight_detector");
        map.put("minecraft:unpowered_comparator", "minecraft:comparator");
        map.put("minecraft:powered_comparator", "minecraft:comparator");
        map.put("minecraft:wall_banner", "minecraft:banner");
        map.put("minecraft:standing_banner", "minecraft:banner");
        map.put("minecraft:structure_block", "minecraft:structure_block");
        map.put("minecraft:end_portal", "minecraft:end_portal");
        map.put("minecraft:end_gateway", "minecraft:end_gateway");
        map.put("minecraft:shield", "minecraft:shield");
    }
}
