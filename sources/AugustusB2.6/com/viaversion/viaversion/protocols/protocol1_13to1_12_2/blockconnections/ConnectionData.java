// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.PacketBlockConnectionProvider;
import java.util.Collection;
import java.util.Locale;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.Map;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;

public class ConnectionData
{
    private static final BlockChangeRecord1_8[] EMPTY_RECORDS;
    public static BlockConnectionProvider blockConnectionProvider;
    static Int2ObjectMap<String> idToKey;
    static Object2IntMap<String> keyToId;
    static Int2ObjectMap<ConnectionHandler> connectionHandlerMap;
    static Int2ObjectMap<BlockData> blockConnectionData;
    static IntSet occludingStates;
    
    public static void update(final UserConnection user, final Position position) {
        for (final BlockFace face : BlockFace.values()) {
            final Position pos = position.getRelative(face);
            final int blockState = ConnectionData.blockConnectionProvider.getBlockData(user, pos.x(), pos.y(), pos.z());
            final ConnectionHandler handler = ConnectionData.connectionHandlerMap.get(blockState);
            if (handler != null) {
                final int newBlockState = handler.connect(user, pos, blockState);
                final PacketWrapper blockUpdatePacket = PacketWrapper.create(ClientboundPackets1_13.BLOCK_CHANGE, null, user);
                blockUpdatePacket.write(Type.POSITION, pos);
                blockUpdatePacket.write(Type.VAR_INT, newBlockState);
                try {
                    blockUpdatePacket.send(Protocol1_13To1_12_2.class);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public static void updateChunkSectionNeighbours(final UserConnection user, final int chunkX, final int chunkZ, final int chunkSectionY) {
        for (int chunkDeltaX = -1; chunkDeltaX <= 1; ++chunkDeltaX) {
            for (int chunkDeltaZ = -1; chunkDeltaZ <= 1; ++chunkDeltaZ) {
                if (Math.abs(chunkDeltaX) + Math.abs(chunkDeltaZ) != 0) {
                    final List<BlockChangeRecord1_8> updates = new ArrayList<BlockChangeRecord1_8>();
                    if (Math.abs(chunkDeltaX) + Math.abs(chunkDeltaZ) == 2) {
                        for (int blockY = chunkSectionY * 16; blockY < chunkSectionY * 16 + 16; ++blockY) {
                            final int blockPosX = (chunkDeltaX == 1) ? 0 : 15;
                            final int blockPosZ = (chunkDeltaZ == 1) ? 0 : 15;
                            updateBlock(user, new Position((chunkX + chunkDeltaX << 4) + blockPosX, (short)blockY, (chunkZ + chunkDeltaZ << 4) + blockPosZ), updates);
                        }
                    }
                    else {
                        for (int blockY = chunkSectionY * 16; blockY < chunkSectionY * 16 + 16; ++blockY) {
                            int xStart;
                            int xEnd;
                            int zStart;
                            int zEnd;
                            if (chunkDeltaX == 1) {
                                xStart = 0;
                                xEnd = 2;
                                zStart = 0;
                                zEnd = 16;
                            }
                            else if (chunkDeltaX == -1) {
                                xStart = 14;
                                xEnd = 16;
                                zStart = 0;
                                zEnd = 16;
                            }
                            else if (chunkDeltaZ == 1) {
                                xStart = 0;
                                xEnd = 16;
                                zStart = 0;
                                zEnd = 2;
                            }
                            else {
                                xStart = 0;
                                xEnd = 16;
                                zStart = 14;
                                zEnd = 16;
                            }
                            for (int blockX = xStart; blockX < xEnd; ++blockX) {
                                for (int blockZ = zStart; blockZ < zEnd; ++blockZ) {
                                    updateBlock(user, new Position((chunkX + chunkDeltaX << 4) + blockX, (short)blockY, (chunkZ + chunkDeltaZ << 4) + blockZ), updates);
                                }
                            }
                        }
                    }
                    if (!updates.isEmpty()) {
                        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_13.MULTI_BLOCK_CHANGE, null, user);
                        wrapper.write(Type.INT, chunkX + chunkDeltaX);
                        wrapper.write(Type.INT, chunkZ + chunkDeltaZ);
                        wrapper.write(Type.BLOCK_CHANGE_RECORD_ARRAY, updates.toArray(ConnectionData.EMPTY_RECORDS));
                        try {
                            wrapper.send(Protocol1_13To1_12_2.class);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
    public static void updateBlock(final UserConnection user, final Position pos, final List<BlockChangeRecord1_8> records) {
        final int blockState = ConnectionData.blockConnectionProvider.getBlockData(user, pos.x(), pos.y(), pos.z());
        final ConnectionHandler handler = getConnectionHandler(blockState);
        if (handler == null) {
            return;
        }
        final int newBlockState = handler.connect(user, pos, blockState);
        records.add(new BlockChangeRecord1_8(pos.x() & 0xF, pos.y(), pos.z() & 0xF, newBlockState));
    }
    
    public static void updateBlockStorage(final UserConnection userConnection, final int x, final int y, final int z, final int blockState) {
        if (!needStoreBlocks()) {
            return;
        }
        if (isWelcome(blockState)) {
            ConnectionData.blockConnectionProvider.storeBlock(userConnection, x, y, z, blockState);
        }
        else {
            ConnectionData.blockConnectionProvider.removeBlock(userConnection, x, y, z);
        }
    }
    
    public static void clearBlockStorage(final UserConnection connection) {
        if (!needStoreBlocks()) {
            return;
        }
        ConnectionData.blockConnectionProvider.clearStorage(connection);
    }
    
    public static boolean needStoreBlocks() {
        return ConnectionData.blockConnectionProvider.storesBlocks();
    }
    
    public static void connectBlocks(final UserConnection user, final Chunk chunk) {
        final long xOff = chunk.getX() << 4;
        final long zOff = chunk.getZ() << 4;
        for (int i = 0; i < chunk.getSections().length; ++i) {
            final ChunkSection section = chunk.getSections()[i];
            if (section != null) {
                boolean willConnect = false;
                for (int p = 0; p < section.getPaletteSize(); ++p) {
                    final int id = section.getPaletteEntry(p);
                    if (connects(id)) {
                        willConnect = true;
                        break;
                    }
                }
                if (willConnect) {
                    final long yOff = i << 4;
                    for (int y = 0; y < 16; ++y) {
                        for (int z = 0; z < 16; ++z) {
                            for (int x = 0; x < 16; ++x) {
                                int block = section.getFlatBlock(x, y, z);
                                final ConnectionHandler handler = getConnectionHandler(block);
                                if (handler != null) {
                                    block = handler.connect(user, new Position((int)(xOff + x), (short)(yOff + y), (int)(zOff + z)), block);
                                    section.setFlatBlock(x, y, z, block);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void init() {
        if (!Via.getConfig().isServersideBlockConnections()) {
            return;
        }
        Via.getPlatform().getLogger().info("Loading block connection mappings ...");
        final JsonObject mapping1_13 = MappingDataLoader.loadData("mapping-1.13.json", true);
        final JsonObject blocks1_13 = mapping1_13.getAsJsonObject("blockstates");
        for (final Map.Entry<String, JsonElement> blockState : blocks1_13.entrySet()) {
            final int id = Integer.parseInt(blockState.getKey());
            final String key = blockState.getValue().getAsString();
            ConnectionData.idToKey.put(id, key);
            ConnectionData.keyToId.put(key, id);
        }
        ConnectionData.connectionHandlerMap = new Int2ObjectOpenHashMap<ConnectionHandler>(3650, 0.99f);
        if (!Via.getConfig().isReduceBlockStorageMemory()) {
            ConnectionData.blockConnectionData = new Int2ObjectOpenHashMap<BlockData>(1146, 0.99f);
            final JsonObject mappingBlockConnections = MappingDataLoader.loadData("blockConnections.json");
            for (final Map.Entry<String, JsonElement> entry : mappingBlockConnections.entrySet()) {
                final int id2 = ConnectionData.keyToId.get((Object)entry.getKey());
                final BlockData blockData = new BlockData();
                for (final Map.Entry<String, JsonElement> type : entry.getValue().getAsJsonObject().entrySet()) {
                    final String name = type.getKey();
                    final JsonObject object = type.getValue().getAsJsonObject();
                    final boolean[] data = new boolean[6];
                    for (final BlockFace value : BlockFace.values()) {
                        final String face = value.toString().toLowerCase(Locale.ROOT);
                        if (object.has(face)) {
                            data[value.ordinal()] = object.getAsJsonPrimitive(face).getAsBoolean();
                        }
                    }
                    blockData.put(name, data);
                }
                if (entry.getKey().contains("stairs")) {
                    blockData.put("allFalseIfStairPre1_12", new boolean[6]);
                }
                ConnectionData.blockConnectionData.put(id2, blockData);
            }
        }
        final JsonObject blockData2 = MappingDataLoader.loadData("blockData.json");
        final JsonArray occluding = blockData2.getAsJsonArray("occluding");
        for (final JsonElement jsonElement : occluding) {
            ConnectionData.occludingStates.add((int)ConnectionData.keyToId.get((Object)jsonElement.getAsString()));
        }
        final List<ConnectorInitAction> initActions = new ArrayList<ConnectorInitAction>();
        initActions.add(PumpkinConnectionHandler.init());
        initActions.addAll(BasicFenceConnectionHandler.init());
        initActions.add(NetherFenceConnectionHandler.init());
        initActions.addAll(WallConnectionHandler.init());
        initActions.add(MelonConnectionHandler.init());
        initActions.addAll(GlassConnectionHandler.init());
        initActions.add(ChestConnectionHandler.init());
        initActions.add(DoorConnectionHandler.init());
        initActions.add(RedstoneConnectionHandler.init());
        initActions.add(StairConnectionHandler.init());
        initActions.add(FlowerConnectionHandler.init());
        initActions.addAll(ChorusPlantConnectionHandler.init());
        initActions.add(TripwireConnectionHandler.init());
        initActions.add(SnowyGrassConnectionHandler.init());
        initActions.add(FireConnectionHandler.init());
        if (Via.getConfig().isVineClimbFix()) {
            initActions.add(VineConnectionHandler.init());
        }
        for (final String key2 : ConnectionData.keyToId.keySet()) {
            final WrappedBlockData wrappedBlockData = WrappedBlockData.fromString(key2);
            for (final ConnectorInitAction action : initActions) {
                action.check(wrappedBlockData);
            }
        }
        if (Via.getConfig().getBlockConnectionMethod().equalsIgnoreCase("packet")) {
            ConnectionData.blockConnectionProvider = new PacketBlockConnectionProvider();
            Via.getManager().getProviders().register(BlockConnectionProvider.class, ConnectionData.blockConnectionProvider);
        }
    }
    
    public static boolean isWelcome(final int blockState) {
        return ConnectionData.blockConnectionData.containsKey(blockState) || ConnectionData.connectionHandlerMap.containsKey(blockState);
    }
    
    public static boolean connects(final int blockState) {
        return ConnectionData.connectionHandlerMap.containsKey(blockState);
    }
    
    public static int connect(final UserConnection user, final Position position, final int blockState) {
        final ConnectionHandler handler = ConnectionData.connectionHandlerMap.get(blockState);
        return (handler != null) ? handler.connect(user, position, blockState) : blockState;
    }
    
    public static ConnectionHandler getConnectionHandler(final int blockstate) {
        return ConnectionData.connectionHandlerMap.get(blockstate);
    }
    
    public static int getId(final String key) {
        return ConnectionData.keyToId.getOrDefault(key, -1);
    }
    
    public static String getKey(final int id) {
        return ConnectionData.idToKey.get(id);
    }
    
    static {
        EMPTY_RECORDS = new BlockChangeRecord1_8[0];
        ConnectionData.idToKey = new Int2ObjectOpenHashMap<String>(8582, 0.99f);
        ConnectionData.keyToId = new Object2IntOpenHashMap<String>(8582, 0.99f);
        ConnectionData.connectionHandlerMap = new Int2ObjectOpenHashMap<ConnectionHandler>(1);
        ConnectionData.blockConnectionData = new Int2ObjectOpenHashMap<BlockData>(1);
        ConnectionData.occludingStates = new IntOpenHashSet(377, 0.99f);
    }
    
    @FunctionalInterface
    interface ConnectorInitAction
    {
        void check(final WrappedBlockData p0);
    }
}
