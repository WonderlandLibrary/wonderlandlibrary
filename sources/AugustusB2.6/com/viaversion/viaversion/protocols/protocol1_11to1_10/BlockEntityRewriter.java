// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_11to1_10;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;

public class BlockEntityRewriter
{
    private static BiMap<String, String> oldToNewNames;
    
    private static void rewrite(final String oldName, final String newName) {
        BlockEntityRewriter.oldToNewNames.put(oldName, "minecraft:" + newName);
    }
    
    public static BiMap<String, String> inverse() {
        return BlockEntityRewriter.oldToNewNames.inverse();
    }
    
    public static String toNewIdentifier(final String oldId) {
        final String newName = BlockEntityRewriter.oldToNewNames.get(oldId);
        if (newName != null) {
            return newName;
        }
        return oldId;
    }
    
    static {
        BlockEntityRewriter.oldToNewNames = (BiMap<String, String>)HashBiMap.create();
        rewrite("Furnace", "furnace");
        rewrite("Chest", "chest");
        rewrite("EnderChest", "ender_chest");
        rewrite("RecordPlayer", "jukebox");
        rewrite("Trap", "dispenser");
        rewrite("Dropper", "dropper");
        rewrite("Sign", "sign");
        rewrite("MobSpawner", "mob_spawner");
        rewrite("Music", "noteblock");
        rewrite("Piston", "piston");
        rewrite("Cauldron", "brewing_stand");
        rewrite("EnchantTable", "enchanting_table");
        rewrite("Airportal", "end_portal");
        rewrite("Beacon", "beacon");
        rewrite("Skull", "skull");
        rewrite("DLDetector", "daylight_detector");
        rewrite("Hopper", "hopper");
        rewrite("Comparator", "comparator");
        rewrite("FlowerPot", "flower_pot");
        rewrite("Banner", "banner");
        rewrite("Structure", "structure_block");
        rewrite("EndGateway", "end_gateway");
        rewrite("Control", "command_block");
    }
}
