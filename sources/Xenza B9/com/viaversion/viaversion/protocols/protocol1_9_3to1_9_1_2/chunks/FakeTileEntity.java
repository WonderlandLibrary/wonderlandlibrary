// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.chunks;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class FakeTileEntity
{
    private static final Int2ObjectMap<CompoundTag> tileEntities;
    
    private static void register(final String name, final int... ids) {
        for (final int id : ids) {
            final CompoundTag comp = new CompoundTag();
            comp.put("id", new StringTag(name));
            FakeTileEntity.tileEntities.put(id, comp);
        }
    }
    
    public static boolean isTileEntity(final int block) {
        return FakeTileEntity.tileEntities.containsKey(block);
    }
    
    public static CompoundTag createTileEntity(final int x, final int y, final int z, final int block) {
        final CompoundTag originalTag = FakeTileEntity.tileEntities.get(block);
        if (originalTag != null) {
            final CompoundTag tag = originalTag.clone();
            tag.put("x", new IntTag(x));
            tag.put("y", new IntTag(y));
            tag.put("z", new IntTag(z));
            return tag;
        }
        return null;
    }
    
    static {
        tileEntities = new Int2ObjectOpenHashMap<CompoundTag>();
        register("Furnace", 61, 62);
        register("Chest", 54, 146);
        register("EnderChest", 130);
        register("RecordPlayer", 84);
        register("Trap", 23);
        register("Dropper", 158);
        register("Sign", 63, 68);
        register("MobSpawner", 52);
        register("Music", 25);
        register("Piston", 33, 34, 29, 36);
        register("Cauldron", 117);
        register("EnchantTable", 116);
        register("Airportal", 119, 120);
        register("Beacon", 138);
        register("Skull", 144);
        register("DLDetector", 178, 151);
        register("Hopper", 154);
        register("Comparator", 149, 150);
        register("FlowerPot", 140);
        register("Banner", 176, 177);
        register("EndGateway", 209);
        register("Control", 137);
    }
}
