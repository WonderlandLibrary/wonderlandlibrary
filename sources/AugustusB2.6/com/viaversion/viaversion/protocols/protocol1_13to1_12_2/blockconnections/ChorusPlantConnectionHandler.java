// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.ArrayList;
import java.util.List;

public class ChorusPlantConnectionHandler extends AbstractFenceConnectionHandler
{
    private final int endstone;
    
    static List<ConnectionData.ConnectorInitAction> init() {
        final List<ConnectionData.ConnectorInitAction> actions = new ArrayList<ConnectionData.ConnectorInitAction>(2);
        final ChorusPlantConnectionHandler handler = new ChorusPlantConnectionHandler();
        actions.add(handler.getInitAction("minecraft:chorus_plant"));
        actions.add(handler.getExtraAction());
        return actions;
    }
    
    public ChorusPlantConnectionHandler() {
        super(null);
        this.endstone = ConnectionData.getId("minecraft:end_stone");
    }
    
    public ConnectionData.ConnectorInitAction getExtraAction() {
        return blockData -> {
            if (blockData.getMinecraftKey().equals("minecraft:chorus_flower")) {
                this.getBlockStates().add(blockData.getSavedBlockStateId());
            }
        };
    }
    
    @Override
    protected byte getStates(final WrappedBlockData blockData) {
        byte states = super.getStates(blockData);
        if (blockData.getValue("up").equals("true")) {
            states |= 0x10;
        }
        if (blockData.getValue("down").equals("true")) {
            states |= 0x20;
        }
        return states;
    }
    
    @Override
    protected byte getStates(final UserConnection user, final Position position, final int blockState) {
        byte states = super.getStates(user, position, blockState);
        if (this.connects(BlockFace.TOP, this.getBlockData(user, position.getRelative(BlockFace.TOP)), false)) {
            states |= 0x10;
        }
        if (this.connects(BlockFace.BOTTOM, this.getBlockData(user, position.getRelative(BlockFace.BOTTOM)), false)) {
            states |= 0x20;
        }
        return states;
    }
    
    @Override
    protected boolean connects(final BlockFace side, final int blockState, final boolean pre1_12) {
        return this.getBlockStates().contains(blockState) || (side == BlockFace.BOTTOM && blockState == this.endstone);
    }
}
