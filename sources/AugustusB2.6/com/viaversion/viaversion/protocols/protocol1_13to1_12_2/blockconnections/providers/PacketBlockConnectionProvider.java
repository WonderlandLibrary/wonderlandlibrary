// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockConnectionStorage;
import com.viaversion.viaversion.api.connection.UserConnection;

public class PacketBlockConnectionProvider extends BlockConnectionProvider
{
    @Override
    public void storeBlock(final UserConnection connection, final int x, final int y, final int z, final int blockState) {
        connection.get(BlockConnectionStorage.class).store(x, y, z, blockState);
    }
    
    @Override
    public void removeBlock(final UserConnection connection, final int x, final int y, final int z) {
        connection.get(BlockConnectionStorage.class).remove(x, y, z);
    }
    
    @Override
    public int getBlockData(final UserConnection connection, final int x, final int y, final int z) {
        return connection.get(BlockConnectionStorage.class).get(x, y, z);
    }
    
    @Override
    public void clearStorage(final UserConnection connection) {
        connection.get(BlockConnectionStorage.class).clear();
    }
    
    @Override
    public void unloadChunk(final UserConnection connection, final int x, final int z) {
        connection.get(BlockConnectionStorage.class).unloadChunk(x, z);
    }
    
    @Override
    public boolean storesBlocks() {
        return true;
    }
}
