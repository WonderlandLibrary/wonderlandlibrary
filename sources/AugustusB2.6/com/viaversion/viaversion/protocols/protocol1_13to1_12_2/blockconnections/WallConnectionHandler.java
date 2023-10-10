// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.ArrayList;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.BlockFace;

public class WallConnectionHandler extends AbstractFenceConnectionHandler
{
    private static final BlockFace[] BLOCK_FACES;
    private static final int[] OPPOSITES;
    
    static List<ConnectionData.ConnectorInitAction> init() {
        final List<ConnectionData.ConnectorInitAction> actions = new ArrayList<ConnectionData.ConnectorInitAction>(2);
        actions.add(new WallConnectionHandler("cobbleWallConnections").getInitAction("minecraft:cobblestone_wall"));
        actions.add(new WallConnectionHandler("cobbleWallConnections").getInitAction("minecraft:mossy_cobblestone_wall"));
        return actions;
    }
    
    public WallConnectionHandler(final String blockConnections) {
        super(blockConnections);
    }
    
    @Override
    protected byte getStates(final WrappedBlockData blockData) {
        byte states = super.getStates(blockData);
        if (blockData.getValue("up").equals("true")) {
            states |= 0x10;
        }
        return states;
    }
    
    @Override
    protected byte getStates(final UserConnection user, final Position position, final int blockState) {
        byte states = super.getStates(user, position, blockState);
        if (this.up(user, position)) {
            states |= 0x10;
        }
        return states;
    }
    
    public boolean up(final UserConnection user, final Position position) {
        if (this.isWall(this.getBlockData(user, position.getRelative(BlockFace.BOTTOM))) || this.isWall(this.getBlockData(user, position.getRelative(BlockFace.TOP)))) {
            return true;
        }
        final int blockFaces = this.getBlockFaces(user, position);
        if (blockFaces == 0 || blockFaces == 15) {
            return true;
        }
        for (int i = 0; i < WallConnectionHandler.BLOCK_FACES.length; ++i) {
            if ((blockFaces & 1 << i) != 0x0 && (blockFaces & 1 << WallConnectionHandler.OPPOSITES[i]) == 0x0) {
                return true;
            }
        }
        return false;
    }
    
    private int getBlockFaces(final UserConnection user, final Position position) {
        int blockFaces = 0;
        for (int i = 0; i < WallConnectionHandler.BLOCK_FACES.length; ++i) {
            if (this.isWall(this.getBlockData(user, position.getRelative(WallConnectionHandler.BLOCK_FACES[i])))) {
                blockFaces |= 1 << i;
            }
        }
        return blockFaces;
    }
    
    private boolean isWall(final int id) {
        return this.getBlockStates().contains(id);
    }
    
    static {
        BLOCK_FACES = new BlockFace[] { BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST };
        OPPOSITES = new int[] { 3, 2, 1, 0 };
    }
}
