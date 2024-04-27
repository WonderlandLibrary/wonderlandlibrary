/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.chunks;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;

public class FakeTileEntity {
    private static final Int2ObjectMap<CompoundTag> tileEntities = new Int2ObjectOpenHashMap<CompoundTag>();

    private static void register(String name, int ... ids) {
        for (int id : ids) {
            CompoundTag comp = new CompoundTag();
            comp.put("id", new StringTag(name));
            tileEntities.put(id, comp);
        }
    }

    public static boolean isTileEntity(int block) {
        return tileEntities.containsKey(block);
    }

    public static CompoundTag createTileEntity(int x, int y, int z, int block) {
        CompoundTag originalTag = (CompoundTag)tileEntities.get(block);
        if (originalTag != null) {
            CompoundTag tag = originalTag.clone();
            tag.put("x", new IntTag(x));
            tag.put("y", new IntTag(y));
            tag.put("z", new IntTag(z));
            return tag;
        }
        return null;
    }

    static {
        FakeTileEntity.register("Furnace", 61, 62);
        FakeTileEntity.register("Chest", 54, 146);
        FakeTileEntity.register("EnderChest", 130);
        FakeTileEntity.register("RecordPlayer", 84);
        FakeTileEntity.register("Trap", 23);
        FakeTileEntity.register("Dropper", 158);
        FakeTileEntity.register("Sign", 63, 68);
        FakeTileEntity.register("MobSpawner", 52);
        FakeTileEntity.register("Music", 25);
        FakeTileEntity.register("Piston", 33, 34, 29, 36);
        FakeTileEntity.register("Cauldron", 117);
        FakeTileEntity.register("EnchantTable", 116);
        FakeTileEntity.register("Airportal", 119, 120);
        FakeTileEntity.register("Beacon", 138);
        FakeTileEntity.register("Skull", 144);
        FakeTileEntity.register("DLDetector", 178, 151);
        FakeTileEntity.register("Hopper", 154);
        FakeTileEntity.register("Comparator", 149, 150);
        FakeTileEntity.register("FlowerPot", 140);
        FakeTileEntity.register("Banner", 176, 177);
        FakeTileEntity.register("EndGateway", 209);
        FakeTileEntity.register("Control", 137);
    }
}

