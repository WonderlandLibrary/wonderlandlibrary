// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Map;
import net.minecraft.util.datafix.IFixableData;

public class TileEntityId implements IFixableData
{
    private static final Map<String, String> OLD_TO_NEW_ID_MAP;
    
    @Override
    public int getFixVersion() {
        return 704;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        final String s = TileEntityId.OLD_TO_NEW_ID_MAP.get(compound.getString("id"));
        if (s != null) {
            compound.setString("id", s);
        }
        return compound;
    }
    
    static {
        (OLD_TO_NEW_ID_MAP = Maps.newHashMap()).put("Airportal", "minecraft:end_portal");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Banner", "minecraft:banner");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Beacon", "minecraft:beacon");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Cauldron", "minecraft:brewing_stand");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Chest", "minecraft:chest");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Comparator", "minecraft:comparator");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Control", "minecraft:command_block");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("DLDetector", "minecraft:daylight_detector");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Dropper", "minecraft:dropper");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("EnchantTable", "minecraft:enchanting_table");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("EndGateway", "minecraft:end_gateway");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("EnderChest", "minecraft:ender_chest");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("FlowerPot", "minecraft:flower_pot");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Furnace", "minecraft:furnace");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Hopper", "minecraft:hopper");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("MobSpawner", "minecraft:mob_spawner");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Music", "minecraft:noteblock");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Piston", "minecraft:piston");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("RecordPlayer", "minecraft:jukebox");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Sign", "minecraft:sign");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Skull", "minecraft:skull");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Structure", "minecraft:structure_block");
        TileEntityId.OLD_TO_NEW_ID_MAP.put("Trap", "minecraft:dispenser");
    }
}
