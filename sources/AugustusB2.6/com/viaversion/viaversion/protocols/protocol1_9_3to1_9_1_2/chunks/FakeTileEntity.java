// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.chunks;

import java.util.Arrays;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import java.util.Iterator;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class FakeTileEntity
{
    private static final Int2ObjectMap<CompoundTag> tileEntities;
    
    private static void register(final int material, final String name) {
        final CompoundTag comp = new CompoundTag();
        comp.put(name, new StringTag());
        FakeTileEntity.tileEntities.put(material, comp);
    }
    
    private static void register(final List<Integer> materials, final String name) {
        for (final int id : materials) {
            register(id, name);
        }
    }
    
    public static boolean hasBlock(final int block) {
        return FakeTileEntity.tileEntities.containsKey(block);
    }
    
    public static CompoundTag getFromBlock(final int x, final int y, final int z, final int block) {
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
        register(Arrays.asList(61, 62), "Furnace");
        register(Arrays.asList(54, 146), "Chest");
        register(130, "EnderChest");
        register(84, "RecordPlayer");
        register(23, "Trap");
        register(158, "Dropper");
        register(Arrays.asList(63, 68), "Sign");
        register(52, "MobSpawner");
        register(25, "Music");
        register(Arrays.asList(33, 34, 29, 36), "Piston");
        register(117, "Cauldron");
        register(116, "EnchantTable");
        register(Arrays.asList(119, 120), "Airportal");
        register(138, "Beacon");
        register(144, "Skull");
        register(Arrays.asList(178, 151), "DLDetector");
        register(154, "Hopper");
        register(Arrays.asList(149, 150), "Comparator");
        register(140, "FlowerPot");
        register(Arrays.asList(176, 177), "Banner");
        register(209, "EndGateway");
        register(137, "Control");
    }
}
