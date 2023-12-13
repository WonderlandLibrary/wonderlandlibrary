// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.HashSet;
import java.util.Set;
import com.viaversion.viaversion.util.Pair;
import java.util.Map;

public class SnowyGrassConnectionHandler extends ConnectionHandler
{
    private static final Map<Pair<Integer, Boolean>, Integer> grassBlocks;
    private static final Set<Integer> snows;
    
    static ConnectionData.ConnectorInitAction init() {
        final Set<String> snowyGrassBlocks = new HashSet<String>();
        snowyGrassBlocks.add("minecraft:grass_block");
        snowyGrassBlocks.add("minecraft:podzol");
        snowyGrassBlocks.add("minecraft:mycelium");
        final SnowyGrassConnectionHandler handler = new SnowyGrassConnectionHandler();
        return blockData -> {
            if (snowyGrassBlocks.contains(blockData.getMinecraftKey())) {
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), handler);
                blockData.set("snowy", "true");
                SnowyGrassConnectionHandler.grassBlocks.put(new Pair<Integer, Boolean>(blockData.getSavedBlockStateId(), true), blockData.getBlockStateId());
                blockData.set("snowy", "false");
                SnowyGrassConnectionHandler.grassBlocks.put(new Pair<Integer, Boolean>(blockData.getSavedBlockStateId(), false), blockData.getBlockStateId());
            }
            if (blockData.getMinecraftKey().equals("minecraft:snow") || blockData.getMinecraftKey().equals("minecraft:snow_block")) {
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), handler);
                SnowyGrassConnectionHandler.snows.add(blockData.getSavedBlockStateId());
            }
        };
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final int blockUpId = this.getBlockData(user, position.getRelative(BlockFace.TOP));
        final Integer newId = SnowyGrassConnectionHandler.grassBlocks.get(new Pair(blockState, SnowyGrassConnectionHandler.snows.contains(blockUpId)));
        if (newId != null) {
            return newId;
        }
        return blockState;
    }
    
    static {
        grassBlocks = new HashMap<Pair<Integer, Boolean>, Integer>();
        snows = new HashSet<Integer>();
    }
}
