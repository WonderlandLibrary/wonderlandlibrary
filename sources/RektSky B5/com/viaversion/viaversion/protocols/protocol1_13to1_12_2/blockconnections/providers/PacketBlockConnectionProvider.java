/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockConnectionStorage;

public class PacketBlockConnectionProvider
extends BlockConnectionProvider {
    @Override
    public void storeBlock(UserConnection connection, int x2, int y2, int z2, int blockState) {
        connection.get(BlockConnectionStorage.class).store(x2, y2, z2, blockState);
    }

    @Override
    public void removeBlock(UserConnection connection, int x2, int y2, int z2) {
        connection.get(BlockConnectionStorage.class).remove(x2, y2, z2);
    }

    @Override
    public int getBlockData(UserConnection connection, int x2, int y2, int z2) {
        return connection.get(BlockConnectionStorage.class).get(x2, y2, z2);
    }

    @Override
    public void clearStorage(UserConnection connection) {
        connection.get(BlockConnectionStorage.class).clear();
    }

    @Override
    public void unloadChunk(UserConnection connection, int x2, int z2) {
        connection.get(BlockConnectionStorage.class).unloadChunk(x2, z2);
    }

    @Override
    public boolean storesBlocks() {
        return true;
    }
}

