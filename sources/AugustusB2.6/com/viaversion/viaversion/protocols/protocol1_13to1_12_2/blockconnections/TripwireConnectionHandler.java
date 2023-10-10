// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Locale;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import java.util.Map;

public class TripwireConnectionHandler extends ConnectionHandler
{
    private static final Map<Integer, TripwireData> tripwireDataMap;
    private static final Map<Byte, Integer> connectedBlocks;
    private static final Map<Integer, BlockFace> tripwireHooks;
    
    static ConnectionData.ConnectorInitAction init() {
        final TripwireConnectionHandler connectionHandler = new TripwireConnectionHandler();
        TripwireData tripwireData;
        final TripwireConnectionHandler value;
        return blockData -> {
            if (blockData.getMinecraftKey().equals("minecraft:tripwire_hook")) {
                TripwireConnectionHandler.tripwireHooks.put(blockData.getSavedBlockStateId(), BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)));
            }
            else if (blockData.getMinecraftKey().equals("minecraft:tripwire")) {
                tripwireData = new TripwireData(blockData.getValue("attached").equals("true"), blockData.getValue("disarmed").equals("true"), blockData.getValue("powered").equals("true"));
                TripwireConnectionHandler.tripwireDataMap.put(blockData.getSavedBlockStateId(), tripwireData);
                TripwireConnectionHandler.connectedBlocks.put(getStates(blockData), blockData.getSavedBlockStateId());
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), value);
            }
        };
    }
    
    private static byte getStates(final WrappedBlockData blockData) {
        byte b = 0;
        if (blockData.getValue("attached").equals("true")) {
            b |= 0x1;
        }
        if (blockData.getValue("disarmed").equals("true")) {
            b |= 0x2;
        }
        if (blockData.getValue("powered").equals("true")) {
            b |= 0x4;
        }
        if (blockData.getValue("east").equals("true")) {
            b |= 0x8;
        }
        if (blockData.getValue("north").equals("true")) {
            b |= 0x10;
        }
        if (blockData.getValue("south").equals("true")) {
            b |= 0x20;
        }
        if (blockData.getValue("west").equals("true")) {
            b |= 0x40;
        }
        return b;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final TripwireData tripwireData = TripwireConnectionHandler.tripwireDataMap.get(blockState);
        if (tripwireData == null) {
            return blockState;
        }
        byte b = 0;
        if (tripwireData.isAttached()) {
            b |= 0x1;
        }
        if (tripwireData.isDisarmed()) {
            b |= 0x2;
        }
        if (tripwireData.isPowered()) {
            b |= 0x4;
        }
        final int east = this.getBlockData(user, position.getRelative(BlockFace.EAST));
        final int north = this.getBlockData(user, position.getRelative(BlockFace.NORTH));
        final int south = this.getBlockData(user, position.getRelative(BlockFace.SOUTH));
        final int west = this.getBlockData(user, position.getRelative(BlockFace.WEST));
        if (TripwireConnectionHandler.tripwireDataMap.containsKey(east) || TripwireConnectionHandler.tripwireHooks.get(east) == BlockFace.WEST) {
            b |= 0x8;
        }
        if (TripwireConnectionHandler.tripwireDataMap.containsKey(north) || TripwireConnectionHandler.tripwireHooks.get(north) == BlockFace.SOUTH) {
            b |= 0x10;
        }
        if (TripwireConnectionHandler.tripwireDataMap.containsKey(south) || TripwireConnectionHandler.tripwireHooks.get(south) == BlockFace.NORTH) {
            b |= 0x20;
        }
        if (TripwireConnectionHandler.tripwireDataMap.containsKey(west) || TripwireConnectionHandler.tripwireHooks.get(west) == BlockFace.EAST) {
            b |= 0x40;
        }
        final Integer newBlockState = TripwireConnectionHandler.connectedBlocks.get(b);
        return (newBlockState == null) ? blockState : newBlockState;
    }
    
    static {
        tripwireDataMap = new HashMap<Integer, TripwireData>();
        connectedBlocks = new HashMap<Byte, Integer>();
        tripwireHooks = new HashMap<Integer, BlockFace>();
    }
    
    private static final class TripwireData
    {
        private final boolean attached;
        private final boolean disarmed;
        private final boolean powered;
        
        private TripwireData(final boolean attached, final boolean disarmed, final boolean powered) {
            this.attached = attached;
            this.disarmed = disarmed;
            this.powered = powered;
        }
        
        public boolean isAttached() {
            return this.attached;
        }
        
        public boolean isDisarmed() {
            return this.disarmed;
        }
        
        public boolean isPowered() {
            return this.powered;
        }
    }
}
