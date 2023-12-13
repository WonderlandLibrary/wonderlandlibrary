/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionHandler;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.WrappedBlockData;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TripwireConnectionHandler
extends ConnectionHandler {
    private static final Map<Integer, TripwireData> tripwireDataMap = new HashMap<Integer, TripwireData>();
    private static final Map<Byte, Integer> connectedBlocks = new HashMap<Byte, Integer>();
    private static final Map<Integer, BlockFace> tripwireHooks = new HashMap<Integer, BlockFace>();

    static ConnectionData.ConnectorInitAction init() {
        TripwireConnectionHandler connectionHandler = new TripwireConnectionHandler();
        return blockData -> {
            if (blockData.getMinecraftKey().equals("minecraft:tripwire_hook")) {
                tripwireHooks.put(blockData.getSavedBlockStateId(), BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)));
            } else if (blockData.getMinecraftKey().equals("minecraft:tripwire")) {
                TripwireData tripwireData = new TripwireData(blockData.getValue("attached").equals("true"), blockData.getValue("disarmed").equals("true"), blockData.getValue("powered").equals("true"));
                tripwireDataMap.put(blockData.getSavedBlockStateId(), tripwireData);
                connectedBlocks.put(TripwireConnectionHandler.getStates(blockData), blockData.getSavedBlockStateId());
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), (ConnectionHandler)connectionHandler);
            }
        };
    }

    private static byte getStates(WrappedBlockData blockData) {
        byte b2 = 0;
        if (blockData.getValue("attached").equals("true")) {
            b2 = (byte)(b2 | 1);
        }
        if (blockData.getValue("disarmed").equals("true")) {
            b2 = (byte)(b2 | 2);
        }
        if (blockData.getValue("powered").equals("true")) {
            b2 = (byte)(b2 | 4);
        }
        if (blockData.getValue("east").equals("true")) {
            b2 = (byte)(b2 | 8);
        }
        if (blockData.getValue("north").equals("true")) {
            b2 = (byte)(b2 | 0x10);
        }
        if (blockData.getValue("south").equals("true")) {
            b2 = (byte)(b2 | 0x20);
        }
        if (blockData.getValue("west").equals("true")) {
            b2 = (byte)(b2 | 0x40);
        }
        return b2;
    }

    @Override
    public int connect(UserConnection user, Position position, int blockState) {
        Integer newBlockState;
        TripwireData tripwireData = tripwireDataMap.get(blockState);
        if (tripwireData == null) {
            return blockState;
        }
        byte b2 = 0;
        if (tripwireData.isAttached()) {
            b2 = (byte)(b2 | 1);
        }
        if (tripwireData.isDisarmed()) {
            b2 = (byte)(b2 | 2);
        }
        if (tripwireData.isPowered()) {
            b2 = (byte)(b2 | 4);
        }
        int east = this.getBlockData(user, position.getRelative(BlockFace.EAST));
        int north = this.getBlockData(user, position.getRelative(BlockFace.NORTH));
        int south = this.getBlockData(user, position.getRelative(BlockFace.SOUTH));
        int west = this.getBlockData(user, position.getRelative(BlockFace.WEST));
        if (tripwireDataMap.containsKey(east) || tripwireHooks.get(east) == BlockFace.WEST) {
            b2 = (byte)(b2 | 8);
        }
        if (tripwireDataMap.containsKey(north) || tripwireHooks.get(north) == BlockFace.SOUTH) {
            b2 = (byte)(b2 | 0x10);
        }
        if (tripwireDataMap.containsKey(south) || tripwireHooks.get(south) == BlockFace.NORTH) {
            b2 = (byte)(b2 | 0x20);
        }
        if (tripwireDataMap.containsKey(west) || tripwireHooks.get(west) == BlockFace.EAST) {
            b2 = (byte)(b2 | 0x40);
        }
        return (newBlockState = connectedBlocks.get(b2)) == null ? blockState : newBlockState;
    }

    private static final class TripwireData {
        private final boolean attached;
        private final boolean disarmed;
        private final boolean powered;

        private TripwireData(boolean attached, boolean disarmed, boolean powered) {
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

