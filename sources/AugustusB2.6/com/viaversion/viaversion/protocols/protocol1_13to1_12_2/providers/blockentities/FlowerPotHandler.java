// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.blockentities;

import java.util.concurrent.ConcurrentHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.util.Pair;
import java.util.Map;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;

public class FlowerPotHandler implements BlockEntityProvider.BlockEntityHandler
{
    private static final Map<Pair<?, Byte>, Integer> flowers;
    
    public static void register(final String identifier, final byte numbericBlockId, final byte blockData, final int newId) {
        FlowerPotHandler.flowers.put(new Pair<Object, Byte>(identifier, blockData), newId);
        FlowerPotHandler.flowers.put(new Pair<Object, Byte>(numbericBlockId, blockData), newId);
    }
    
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        Object item = tag.contains("Item") ? tag.get("Item").getValue() : null;
        Object data = tag.contains("Data") ? tag.get("Data").getValue() : null;
        if (item instanceof String) {
            item = ((String)item).replace("minecraft:", "");
        }
        else if (item instanceof Number) {
            item = ((Number)item).byteValue();
        }
        else {
            item = 0;
        }
        if (data instanceof Number) {
            data = ((Number)data).byteValue();
        }
        else {
            data = 0;
        }
        Integer flower = FlowerPotHandler.flowers.get(new Pair(item, (byte)data));
        if (flower != null) {
            return flower;
        }
        flower = FlowerPotHandler.flowers.get(new Pair(item, 0));
        if (flower != null) {
            return flower;
        }
        return 5265;
    }
    
    static {
        flowers = new ConcurrentHashMap<Pair<?, Byte>, Integer>();
        register("air", (byte)0, (byte)0, 5265);
        register("sapling", (byte)6, (byte)0, 5266);
        register("sapling", (byte)6, (byte)1, 5267);
        register("sapling", (byte)6, (byte)2, 5268);
        register("sapling", (byte)6, (byte)3, 5269);
        register("sapling", (byte)6, (byte)4, 5270);
        register("sapling", (byte)6, (byte)5, 5271);
        register("tallgrass", (byte)31, (byte)2, 5272);
        register("yellow_flower", (byte)37, (byte)0, 5273);
        register("red_flower", (byte)38, (byte)0, 5274);
        register("red_flower", (byte)38, (byte)1, 5275);
        register("red_flower", (byte)38, (byte)2, 5276);
        register("red_flower", (byte)38, (byte)3, 5277);
        register("red_flower", (byte)38, (byte)4, 5278);
        register("red_flower", (byte)38, (byte)5, 5279);
        register("red_flower", (byte)38, (byte)6, 5280);
        register("red_flower", (byte)38, (byte)7, 5281);
        register("red_flower", (byte)38, (byte)8, 5282);
        register("red_mushroom", (byte)40, (byte)0, 5283);
        register("brown_mushroom", (byte)39, (byte)0, 5284);
        register("deadbush", (byte)32, (byte)0, 5285);
        register("cactus", (byte)81, (byte)0, 5286);
    }
}
