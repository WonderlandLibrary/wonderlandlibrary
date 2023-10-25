/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk1_8;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.sounds.Effect;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.sounds.SoundEffect;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.types.Chunk1_9to1_8Type;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

public class WorldPackets {
    public static void register(Protocol protocol) {
        protocol.registerClientbound(ClientboundPackets1_8.UPDATE_SIGN, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.EFFECT, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int id = wrapper.get(Type.INT, 0);
                        id = Effect.getNewId(id);
                        wrapper.set(Type.INT, 0, id);
                    }
                });
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int id = wrapper.get(Type.INT, 0);
                        if (id == 2002) {
                            int data = wrapper.get(Type.INT, 1);
                            int newData = ItemRewriter.getNewEffectID(data);
                            wrapper.set(Type.INT, 1, newData);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.NAMED_SOUND, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        String name = wrapper.get(Type.STRING, 0);
                        SoundEffect effect = SoundEffect.getByName(name);
                        int catid = 0;
                        String newname = name;
                        if (effect != null) {
                            catid = effect.getCategory().getId();
                            newname = effect.getNewName();
                        }
                        wrapper.set(Type.STRING, 0, newname);
                        wrapper.write(Type.VAR_INT, catid);
                        if (effect != null && effect.isBreaksound()) {
                            EntityTracker1_9 tracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                            int x = wrapper.passthrough(Type.INT);
                            int y = wrapper.passthrough(Type.INT);
                            int z = wrapper.passthrough(Type.INT);
                            if (tracker.interactedBlockRecently((int)Math.floor((double)x / 8.0), (int)Math.floor((double)y / 8.0), (int)Math.floor((double)z / 8.0))) {
                                wrapper.cancel();
                            }
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.CHUNK_DATA, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        ClientChunks clientChunks = wrapper.user().get(ClientChunks.class);
                        Chunk1_9to1_8Type type = new Chunk1_9to1_8Type(clientChunks);
                        Chunk1_8 chunk = (Chunk1_8)wrapper.read(type);
                        if (chunk.isUnloadPacket()) {
                            wrapper.setPacketType(ClientboundPackets1_9.UNLOAD_CHUNK);
                            wrapper.write(Type.INT, chunk.getX());
                            wrapper.write(Type.INT, chunk.getZ());
                            CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                            provider.unloadChunk(wrapper.user(), chunk.getX(), chunk.getZ());
                        } else {
                            wrapper.write(type, chunk);
                        }
                        wrapper.read(Type.REMAINING_BYTES);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.MAP_BULK_CHUNK, null, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.cancel();
                    boolean skyLight = wrapper.read(Type.BOOLEAN);
                    int count = wrapper.read(Type.VAR_INT);
                    ChunkBulkSection[] chunks = new ChunkBulkSection[count];
                    for (int i = 0; i < count; ++i) {
                        chunks[i] = new ChunkBulkSection(wrapper, skyLight);
                    }
                    ClientChunks clientChunks = wrapper.user().get(ClientChunks.class);
                    for (ChunkBulkSection chunk : chunks) {
                        CustomByteType customByteType = new CustomByteType(chunk.getLength());
                        chunk.setData(wrapper.read(customByteType));
                        clientChunks.getBulkChunks().add(ClientChunks.toLong(chunk.getX(), chunk.getZ()));
                        ByteBuf buffer = null;
                        try {
                            buffer = wrapper.user().getChannel().alloc().buffer();
                            Type.INT.write(buffer, chunk.getX());
                            Type.INT.write(buffer, chunk.getZ());
                            Type.BOOLEAN.write(buffer, true);
                            Type.UNSIGNED_SHORT.write(buffer, chunk.getBitMask());
                            Type.VAR_INT.writePrimitive(buffer, chunk.getLength());
                            customByteType.write(buffer, chunk.getData());
                            PacketWrapper chunkPacket = PacketWrapper.create(ClientboundPackets1_8.CHUNK_DATA, buffer, wrapper.user());
                            chunkPacket.send(Protocol1_9To1_8.class, false);
                        }
                        finally {
                            if (buffer != null) {
                                buffer.release();
                            }
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.BLOCK_ENTITY_DATA, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        CompoundTag tag;
                        short action = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (action == 1 && (tag = wrapper.get(Type.NBT, 0)) != null) {
                            if (tag.contains("EntityId")) {
                                String entity = (String)((Tag)tag.get("EntityId")).getValue();
                                CompoundTag spawn = new CompoundTag();
                                spawn.put("id", new StringTag(entity));
                                tag.put("SpawnData", spawn);
                            } else {
                                CompoundTag spawn = new CompoundTag();
                                spawn.put("id", new StringTag("AreaEffectCloud"));
                                tag.put("SpawnData", spawn);
                            }
                        }
                        if (action == 2) {
                            CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                            provider.addOrUpdateBlock(wrapper.user(), wrapper.get(Type.POSITION, 0), wrapper.get(Type.NBT, 0));
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.BLOCK_CHANGE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.UPDATE_SIGN, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.PLAYER_DIGGING, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.map(Type.POSITION);
                this.map(Type.BYTE);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        short status = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (status == 6) {
                            wrapper.cancel();
                        }
                    }
                });
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        EntityTracker1_9 entityTracker;
                        short status = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if ((status == 5 || status == 4 || status == 3) && (entityTracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class)).isBlocking()) {
                            entityTracker.setBlocking(false);
                            if (!Via.getConfig().isShowShieldWhenSwordInHand()) {
                                entityTracker.setSecondHand(null);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.USE_ITEM, null, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int hand = wrapper.read(Type.VAR_INT);
                        wrapper.clearInputBuffer();
                        wrapper.setId(8);
                        wrapper.write(Type.POSITION, new Position(-1, -1, -1));
                        wrapper.write(Type.UNSIGNED_BYTE, (short)255);
                        Item item = Protocol1_9To1_8.getHandItem(wrapper.user());
                        if (Via.getConfig().isShieldBlocking()) {
                            boolean isSword;
                            EntityTracker1_9 tracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                            boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand();
                            boolean bl = showShieldWhenSwordInHand ? tracker.hasSwordInHand() : (isSword = item != null && Protocol1_9To1_8.isSword(item.identifier()));
                            if (isSword) {
                                boolean blockUsingMainHand;
                                if (hand == 0 && !tracker.isBlocking()) {
                                    tracker.setBlocking(true);
                                    if (!showShieldWhenSwordInHand && tracker.getItemInSecondHand() == null) {
                                        DataItem shield = new DataItem(442, 1, 0, null);
                                        tracker.setSecondHand(shield);
                                    }
                                }
                                boolean bl2 = blockUsingMainHand = Via.getConfig().isNoDelayShieldBlocking() && !showShieldWhenSwordInHand;
                                if (blockUsingMainHand && hand == 1 || !blockUsingMainHand && hand == 0) {
                                    wrapper.cancel();
                                }
                            } else {
                                if (!showShieldWhenSwordInHand) {
                                    tracker.setSecondHand(null);
                                }
                                tracker.setBlocking(false);
                            }
                        }
                        wrapper.write(Type.ITEM, item);
                        wrapper.write(Type.UNSIGNED_BYTE, (short)0);
                        wrapper.write(Type.UNSIGNED_BYTE, (short)0);
                        wrapper.write(Type.UNSIGNED_BYTE, (short)0);
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.PLAYER_BLOCK_PLACEMENT, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map((Type)Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int hand = wrapper.read(Type.VAR_INT);
                        if (hand != 0) {
                            wrapper.cancel();
                        }
                    }
                });
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        Item item = Protocol1_9To1_8.getHandItem(wrapper.user());
                        wrapper.write(Type.ITEM, item);
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        short face = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (face == 255) {
                            return;
                        }
                        Position p = wrapper.get(Type.POSITION, 0);
                        int x = p.getX();
                        int y = p.getY();
                        int z = p.getZ();
                        switch (face) {
                            case 0: {
                                --y;
                                break;
                            }
                            case 1: {
                                ++y;
                                break;
                            }
                            case 2: {
                                --z;
                                break;
                            }
                            case 3: {
                                ++z;
                                break;
                            }
                            case 4: {
                                --x;
                                break;
                            }
                            case 5: {
                                ++x;
                            }
                        }
                        EntityTracker1_9 tracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addBlockInteraction(new Position(x, y, z));
                    }
                });
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                        Position pos = wrapper.get(Type.POSITION, 0);
                        Optional<CompoundTag> tag = provider.get(wrapper.user(), pos);
                        if (tag.isPresent()) {
                            PacketWrapper updateBlockEntity = PacketWrapper.create(ClientboundPackets1_9.BLOCK_ENTITY_DATA, null, wrapper.user());
                            updateBlockEntity.write(Type.POSITION, pos);
                            updateBlockEntity.write(Type.UNSIGNED_BYTE, (short)2);
                            updateBlockEntity.write(Type.NBT, tag.get());
                            updateBlockEntity.scheduleSend(Protocol1_9To1_8.class);
                        }
                    }
                });
            }
        });
    }

    public static final class ChunkBulkSection {
        private final int x;
        private final int z;
        private final int bitMask;
        private final int length;
        private byte[] data;

        public ChunkBulkSection(PacketWrapper wrapper, boolean skylight) throws Exception {
            this.x = wrapper.read(Type.INT);
            this.z = wrapper.read(Type.INT);
            this.bitMask = wrapper.read(Type.UNSIGNED_SHORT);
            int bitCount = Integer.bitCount(this.bitMask);
            this.length = bitCount * 10240 + (skylight ? bitCount * 2048 : 0) + 256;
        }

        public int getX() {
            return this.x;
        }

        public int getZ() {
            return this.z;
        }

        public int getBitMask() {
            return this.bitMask;
        }

        public int getLength() {
            return this.length;
        }

        public byte @Nullable [] getData() {
            return this.data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }
}

