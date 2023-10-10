// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.HashSet;
import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Locale;
import java.util.Set;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import java.util.Map;

class ChestConnectionHandler extends ConnectionHandler
{
    private static final Map<Integer, BlockFace> chestFacings;
    private static final Map<Byte, Integer> connectedStates;
    private static final Set<Integer> trappedChests;
    
    static ConnectionData.ConnectorInitAction init() {
        final ChestConnectionHandler connectionHandler = new ChestConnectionHandler();
        final ChestConnectionHandler value;
        return blockData -> {
            if (blockData.getMinecraftKey().equals("minecraft:chest") || blockData.getMinecraftKey().equals("minecraft:trapped_chest")) {
                if (!blockData.getValue("waterlogged").equals("true")) {
                    ChestConnectionHandler.chestFacings.put(blockData.getSavedBlockStateId(), BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)));
                    if (blockData.getMinecraftKey().equalsIgnoreCase("minecraft:trapped_chest")) {
                        ChestConnectionHandler.trappedChests.add(blockData.getSavedBlockStateId());
                    }
                    ChestConnectionHandler.connectedStates.put(getStates(blockData), blockData.getSavedBlockStateId());
                    ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), value);
                }
            }
        };
    }
    
    private static Byte getStates(final WrappedBlockData blockData) {
        byte states = 0;
        final String type = blockData.getValue("type");
        if (type.equals("left")) {
            states |= 0x1;
        }
        if (type.equals("right")) {
            states |= 0x2;
        }
        states |= (byte)(BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)).ordinal() << 2);
        if (blockData.getMinecraftKey().equals("minecraft:trapped_chest")) {
            states |= 0x10;
        }
        return states;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final BlockFace facing = ChestConnectionHandler.chestFacings.get(blockState);
        byte states = 0;
        states |= (byte)(facing.ordinal() << 2);
        final boolean trapped = ChestConnectionHandler.trappedChests.contains(blockState);
        if (trapped) {
            states |= 0x10;
        }
        int relative;
        if (ChestConnectionHandler.chestFacings.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.NORTH))) && trapped == ChestConnectionHandler.trappedChests.contains(relative)) {
            states |= (byte)((facing == BlockFace.WEST) ? 1 : 2);
        }
        else if (ChestConnectionHandler.chestFacings.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.SOUTH))) && trapped == ChestConnectionHandler.trappedChests.contains(relative)) {
            states |= (byte)((facing == BlockFace.EAST) ? 1 : 2);
        }
        else if (ChestConnectionHandler.chestFacings.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.WEST))) && trapped == ChestConnectionHandler.trappedChests.contains(relative)) {
            states |= (byte)((facing == BlockFace.NORTH) ? 2 : 1);
        }
        else if (ChestConnectionHandler.chestFacings.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.EAST))) && trapped == ChestConnectionHandler.trappedChests.contains(relative)) {
            states |= (byte)((facing == BlockFace.SOUTH) ? 2 : 1);
        }
        final Integer newBlockState = ChestConnectionHandler.connectedStates.get(states);
        return (newBlockState == null) ? blockState : newBlockState;
    }
    
    static {
        chestFacings = new HashMap<Integer, BlockFace>();
        connectedStates = new HashMap<Byte, Integer>();
        trappedChests = new HashSet<Integer>();
    }
}
