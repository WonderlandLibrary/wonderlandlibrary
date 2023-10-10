// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.HashSet;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Set;

class VineConnectionHandler extends ConnectionHandler
{
    private static final Set<Integer> vines;
    
    static ConnectionData.ConnectorInitAction init() {
        final VineConnectionHandler connectionHandler = new VineConnectionHandler();
        final VineConnectionHandler value;
        return blockData -> {
            if (!(!blockData.getMinecraftKey().equals("minecraft:vine"))) {
                VineConnectionHandler.vines.add(blockData.getSavedBlockStateId());
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), value);
            }
        };
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        if (this.isAttachedToBlock(user, position)) {
            return blockState;
        }
        final Position upperPos = position.getRelative(BlockFace.TOP);
        final int upperBlock = this.getBlockData(user, upperPos);
        if (VineConnectionHandler.vines.contains(upperBlock) && this.isAttachedToBlock(user, upperPos)) {
            return blockState;
        }
        return 0;
    }
    
    private boolean isAttachedToBlock(final UserConnection user, final Position position) {
        return this.isAttachedToBlock(user, position, BlockFace.EAST) || this.isAttachedToBlock(user, position, BlockFace.WEST) || this.isAttachedToBlock(user, position, BlockFace.NORTH) || this.isAttachedToBlock(user, position, BlockFace.SOUTH);
    }
    
    private boolean isAttachedToBlock(final UserConnection user, final Position position, final BlockFace blockFace) {
        return ConnectionData.occludingStates.contains(this.getBlockData(user, position.getRelative(blockFace)));
    }
    
    static {
        vines = new HashSet<Integer>();
    }
}
