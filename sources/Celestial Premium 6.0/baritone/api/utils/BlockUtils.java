/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class BlockUtils {
    private static transient Map<String, Block> resourceCache = new HashMap<String, Block>();

    public static String blockToString(Block block) {
        ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
        String name = loc.getPath();
        if (!loc.getNamespace().equals("minecraft")) {
            name = loc.toString();
        }
        return name;
    }

    public static Block stringToBlockRequired(String name) {
        Block block = BlockUtils.stringToBlockNullable(name);
        if (block == null) {
            throw new IllegalArgumentException(String.format("Invalid block name %s", name));
        }
        return block;
    }

    public static Block stringToBlockNullable(String name) {
        Block block = resourceCache.get(name);
        if (block != null) {
            return block;
        }
        if (resourceCache.containsKey(name)) {
            return null;
        }
        block = Block.getBlockFromName(name.contains(":") ? name : "minecraft:" + name);
        HashMap<String, Block> copy = new HashMap<String, Block>(resourceCache);
        copy.put(name, block);
        resourceCache = copy;
        return block;
    }

    private BlockUtils() {
    }
}

