// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import java.util.HashSet;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import java.util.Set;

public class RedstoneConnectionHandler extends ConnectionHandler
{
    private static final Set<Integer> redstone;
    private static final Int2IntMap connectedBlockStates;
    private static final Int2IntMap powerMappings;
    
    static ConnectionData.ConnectorInitAction init() {
        final RedstoneConnectionHandler connectionHandler = new RedstoneConnectionHandler();
        final String redstoneKey = "minecraft:redstone_wire";
        final RedstoneConnectionHandler value;
        return blockData -> {
            if (!(!"minecraft:redstone_wire".equals(blockData.getMinecraftKey()))) {
                RedstoneConnectionHandler.redstone.add(blockData.getSavedBlockStateId());
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), value);
                RedstoneConnectionHandler.connectedBlockStates.put(getStates(blockData), blockData.getSavedBlockStateId());
                RedstoneConnectionHandler.powerMappings.put(blockData.getSavedBlockStateId(), Integer.parseInt(blockData.getValue("power")));
            }
        };
    }
    
    private static short getStates(final WrappedBlockData data) {
        short b = 0;
        b |= (short)getState(data.getValue("east"));
        b |= (short)(getState(data.getValue("north")) << 2);
        b |= (short)(getState(data.getValue("south")) << 4);
        b |= (short)(getState(data.getValue("west")) << 6);
        b |= (short)(Integer.parseInt(data.getValue("power")) << 8);
        return b;
    }
    
    private static int getState(final String value) {
        switch (value) {
            case "none": {
                return 0;
            }
            case "side": {
                return 1;
            }
            case "up": {
                return 2;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        short b = 0;
        b |= (short)this.connects(user, position, BlockFace.EAST);
        b |= (short)(this.connects(user, position, BlockFace.NORTH) << 2);
        b |= (short)(this.connects(user, position, BlockFace.SOUTH) << 4);
        b |= (short)(this.connects(user, position, BlockFace.WEST) << 6);
        b |= (short)(RedstoneConnectionHandler.powerMappings.get(blockState) << 8);
        return RedstoneConnectionHandler.connectedBlockStates.getOrDefault(b, blockState);
    }
    
    private int connects(final UserConnection user, final Position position, final BlockFace side) {
        final Position relative = position.getRelative(side);
        final int blockState = this.getBlockData(user, relative);
        if (this.connects(side, blockState)) {
            return 1;
        }
        final int up = this.getBlockData(user, relative.getRelative(BlockFace.TOP));
        if (RedstoneConnectionHandler.redstone.contains(up) && !ConnectionData.occludingStates.contains(this.getBlockData(user, position.getRelative(BlockFace.TOP)))) {
            return 2;
        }
        final int down = this.getBlockData(user, relative.getRelative(BlockFace.BOTTOM));
        if (RedstoneConnectionHandler.redstone.contains(down) && !ConnectionData.occludingStates.contains(this.getBlockData(user, relative))) {
            return 1;
        }
        return 0;
    }
    
    private boolean connects(final BlockFace side, final int blockState) {
        final BlockData blockData = ConnectionData.blockConnectionData.get(blockState);
        return blockData != null && blockData.connectsTo("redstoneConnections", side.opposite(), false);
    }
    
    static {
        redstone = new HashSet<Integer>();
        connectedBlockStates = new Int2IntOpenHashMap(1296);
        powerMappings = new Int2IntOpenHashMap(1296);
    }
}
