// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class FireConnectionHandler extends ConnectionHandler
{
    private static final String[] WOOD_TYPES;
    private static final Map<Byte, Integer> connectedBlocks;
    private static final Set<Integer> flammableBlocks;
    
    private static void addWoodTypes(final Set<String> set, final String suffix) {
        for (final String woodType : FireConnectionHandler.WOOD_TYPES) {
            set.add("minecraft:" + woodType + suffix);
        }
    }
    
    static ConnectionData.ConnectorInitAction init() {
        final Set<String> flammabeIds = new HashSet<String>();
        flammabeIds.add("minecraft:tnt");
        flammabeIds.add("minecraft:vine");
        flammabeIds.add("minecraft:bookshelf");
        flammabeIds.add("minecraft:hay_block");
        flammabeIds.add("minecraft:deadbush");
        addWoodTypes(flammabeIds, "_slab");
        addWoodTypes(flammabeIds, "_log");
        addWoodTypes(flammabeIds, "_planks");
        addWoodTypes(flammabeIds, "_leaves");
        addWoodTypes(flammabeIds, "_fence");
        addWoodTypes(flammabeIds, "_fence_gate");
        addWoodTypes(flammabeIds, "_stairs");
        final FireConnectionHandler connectionHandler = new FireConnectionHandler();
        final String key;
        final Set set;
        int id;
        final FireConnectionHandler value;
        return blockData -> {
            key = blockData.getMinecraftKey();
            if (key.contains("_wool") || key.contains("_carpet") || set.contains(key)) {
                FireConnectionHandler.flammableBlocks.add(blockData.getSavedBlockStateId());
            }
            else if (key.equals("minecraft:fire")) {
                id = blockData.getSavedBlockStateId();
                FireConnectionHandler.connectedBlocks.put(getStates(blockData), id);
                ConnectionData.connectionHandlerMap.put(id, value);
            }
        };
    }
    
    private static byte getStates(final WrappedBlockData blockData) {
        byte states = 0;
        if (blockData.getValue("east").equals("true")) {
            states |= 0x1;
        }
        if (blockData.getValue("north").equals("true")) {
            states |= 0x2;
        }
        if (blockData.getValue("south").equals("true")) {
            states |= 0x4;
        }
        if (blockData.getValue("up").equals("true")) {
            states |= 0x8;
        }
        if (blockData.getValue("west").equals("true")) {
            states |= 0x10;
        }
        return states;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        byte states = 0;
        if (FireConnectionHandler.flammableBlocks.contains(this.getBlockData(user, position.getRelative(BlockFace.EAST)))) {
            states |= 0x1;
        }
        if (FireConnectionHandler.flammableBlocks.contains(this.getBlockData(user, position.getRelative(BlockFace.NORTH)))) {
            states |= 0x2;
        }
        if (FireConnectionHandler.flammableBlocks.contains(this.getBlockData(user, position.getRelative(BlockFace.SOUTH)))) {
            states |= 0x4;
        }
        if (FireConnectionHandler.flammableBlocks.contains(this.getBlockData(user, position.getRelative(BlockFace.TOP)))) {
            states |= 0x8;
        }
        if (FireConnectionHandler.flammableBlocks.contains(this.getBlockData(user, position.getRelative(BlockFace.WEST)))) {
            states |= 0x10;
        }
        return FireConnectionHandler.connectedBlocks.get(states);
    }
    
    static {
        WOOD_TYPES = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "dark_oak" };
        connectedBlocks = new HashMap<Byte, Integer>();
        flammableBlocks = new HashSet<Integer>();
    }
}
