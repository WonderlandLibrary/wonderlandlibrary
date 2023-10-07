package viaversion.viarewind.protocol.protocol1_8to1_9.packets;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import viaversion.viarewind.ViaRewind;
import viaversion.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import viaversion.viarewind.protocol.protocol1_8to1_9.items.ReplacementRegistry1_8to1_9;
import viaversion.viarewind.protocol.protocol1_8to1_9.sound.Effect;
import viaversion.viarewind.protocol.protocol1_8to1_9.sound.SoundRemapper;
import viaversion.viarewind.protocol.protocol1_8to1_9.types.Chunk1_8Type;
import viaversion.viarewind.storage.BlockState;
import viaversion.viarewind.utils.PacketUtil;
import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.minecraft.BlockChangeRecord;
import viaversion.viaversion.api.minecraft.Environment;
import viaversion.viaversion.api.minecraft.Position;
import viaversion.viaversion.api.minecraft.chunks.Chunk;
import viaversion.viaversion.api.minecraft.chunks.Chunk1_8;
import viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.packets.State;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.types.Chunk1_9_1_2Type;

public class WorldPackets {

    public static void register(Protocol protocol) {
        /*  OUTGOING  */

        //Block Break Animation
        protocol.registerOutgoing(State.PLAY, 0x08, 0x25);

        //Update Block Entity
        protocol.registerOutgoing(State.PLAY, 0x09, 0x35, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.POSITION);
                map(Type.UNSIGNED_BYTE);
                map(Type.NBT);
                handler(packetWrapper -> {
                    CompoundTag tag = packetWrapper.get(Type.NBT, 0);
                    if (tag != null && tag.contains("SpawnData")) {
                        String entity = (String) ((CompoundTag) tag.get("SpawnData")).get("id").getValue();
                        tag.remove("SpawnData");
                        tag.put(new StringTag("entityId", entity));
                    }
                });
            }
        });

        //Block Action
        protocol.registerOutgoing(State.PLAY, 0x0A, 0x24, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.POSITION);
                map(Type.UNSIGNED_BYTE);
                map(Type.UNSIGNED_BYTE);
                map(Type.VAR_INT);
                handler(packetWrapper -> {
                    int block = packetWrapper.get(Type.VAR_INT, 0);
                    if (block >= 219 && block <= 234) {
                        packetWrapper.set(Type.VAR_INT, 0, block = 130);
                    }
                });
            }
        });

        //Block Change
        protocol.registerOutgoing(State.PLAY, 0x0B, 0x23, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.POSITION);
                map(Type.VAR_INT);
                handler(packetWrapper -> {
                    int combined = packetWrapper.get(Type.VAR_INT, 0);
                    BlockState state = BlockState.rawToState(combined);
                    state = ReplacementRegistry1_8to1_9.replace(state);
                    packetWrapper.set(Type.VAR_INT, 0, BlockState.stateToRaw(state));
                });
            }
        });

        //Server Difficulty
        protocol.registerOutgoing(State.PLAY, 0x0D, 0x41);

        //Multi Block Change
        protocol.registerOutgoing(State.PLAY, 0x10, 0x22, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT);
                map(Type.INT);
                map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                handler(packetWrapper -> {
                    for (BlockChangeRecord record : packetWrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                        BlockState state = BlockState.rawToState(record.getBlockId());
                        state = ReplacementRegistry1_8to1_9.replace(state);
                        record.setBlockId(BlockState.stateToRaw(state));
                    }
                });
            }
        });

        //Named Sound Effect
        protocol.registerOutgoing(State.PLAY, 0x19, 0x29, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.STRING);
                handler(packetWrapper -> {
                    String name = packetWrapper.get(Type.STRING, 0);
                    name = SoundRemapper.getOldName(name);
                    if (name == null) {
                        packetWrapper.cancel();
                    } else {
                        packetWrapper.set(Type.STRING, 0, name);
                    }
                });
                handler(packetWrapper -> packetWrapper.read(Type.VAR_INT));
                map(Type.INT);
                map(Type.INT);
                map(Type.INT);
                map(Type.FLOAT);
                map(Type.UNSIGNED_BYTE);
            }
        });

        //Explosion
        protocol.registerOutgoing(State.PLAY, 0x1C, 0x27, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.FLOAT);
                map(Type.FLOAT);
                map(Type.FLOAT);
                map(Type.FLOAT);
                handler(packetWrapper -> {
                    int count = packetWrapper.read(Type.INT);
                    packetWrapper.write(Type.INT, count);
                    for (int i = 0; i < count; i++) {
                        packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                        packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                        packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                    }
                });
                map(Type.FLOAT);
                map(Type.FLOAT);
                map(Type.FLOAT);
            }
        });

        //Unload Chunk
        protocol.registerOutgoing(State.PLAY, 0x1D, 0x21, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(packetWrapper -> {
                    int chunkX = packetWrapper.read(Type.INT);
                    int chunkZ = packetWrapper.read(Type.INT);
                    ClientWorld world = packetWrapper.user().get(ClientWorld.class);
                    packetWrapper.write(new Chunk1_8Type(world), new Chunk1_8(chunkX, chunkZ));
                });
            }
        });

        //Chunk Data
        protocol.registerOutgoing(State.PLAY, 0x20, 0x21, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(packetWrapper -> {
                    ClientWorld world = packetWrapper.user().get(ClientWorld.class);

                    Chunk chunk = packetWrapper.read(new Chunk1_9_1_2Type(world));

                    for (ChunkSection section : chunk.getSections()) {
                        if (section == null) continue;
                        for (int i = 0; i < section.getPaletteSize(); i++) {
                            int block = section.getPaletteEntry(i);
                            BlockState state = BlockState.rawToState(block);
                            state = ReplacementRegistry1_8to1_9.replace(state);
                            section.setPaletteEntry(i, BlockState.stateToRaw(state));
                        }
                    }

                    if (chunk.isFullChunk() && chunk
                            .getBitmask() == 0) {  //This would be an unload packet for 1.8 clients. Just set one air section
                        boolean skylight = world.getEnvironment() == Environment.NORMAL;
                        ChunkSection[] sections = new ChunkSection[16];
                        ChunkSection section = new ChunkSection();
                        sections[0] = section;
                        section.addPaletteEntry(0);
                        if (skylight) section.setSkyLight(new byte[2048]);
                        chunk = new Chunk1_8(chunk.getX(), chunk.getZ(), true, 1, sections, chunk.getBiomeData(),
                                chunk.getBlockEntities());
                    }

                    packetWrapper.write(new Chunk1_8Type(world), chunk);

                    UserConnection user = packetWrapper.user();
                    chunk.getBlockEntities().forEach(nbt -> {
                        if (!nbt.contains("x") || !nbt.contains("y") || !nbt.contains("z") || !nbt.contains("id")) return;
                        Position position = new Position((int) nbt.get("x").getValue(), (short) (int) nbt.get("y").getValue(),
                                (int) nbt.get("z").getValue());
                        String id = (String) nbt.get("id").getValue();

                        short action;
                        switch (id) {
                            case "minecraft:mob_spawner":
                                action = 1;
                                break;
                            case "minecraft:command_block":
                                action = 2;
                                break;
                            case "minecraft:beacon":
                                action = 3;
                                break;
                            case "minecraft:skull":
                                action = 4;
                                break;
                            case "minecraft:flower_pot":
                                action = 5;
                                break;
                            case "minecraft:banner":
                                action = 6;
                                break;
                            default:
                                return;
                        }

                        PacketWrapper updateTileEntity = new PacketWrapper(0x09, null, user);
                        updateTileEntity.write(Type.POSITION, position);
                        updateTileEntity.write(Type.UNSIGNED_BYTE, action);
                        updateTileEntity.write(Type.NBT, nbt);

                        PacketUtil.sendPacket(updateTileEntity, Protocol1_8TO1_9.class, false, false);
                    });
                });
            }
        });

        //Effect
        protocol.registerOutgoing(State.PLAY, 0x21, 0x28, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT);
                map(Type.POSITION);
                map(Type.INT);
                map(Type.BOOLEAN);
                handler(packetWrapper -> {
                    int id = packetWrapper.get(Type.INT, 0);
                    id = Effect.getOldId(id);
                    if (id == -1) {
                        packetWrapper.cancel();
                        return;
                    }
                    packetWrapper.set(Type.INT, 0, id);
                    if (id == 2001) {
                        BlockState state = BlockState.rawToState(packetWrapper.get(Type.INT, 1));
                        state = ReplacementRegistry1_8to1_9.replace(state);
                        packetWrapper.set(Type.INT, 1, BlockState.stateToRaw(state));
                    }
                });
            }
        });

        //Particle
        protocol.registerOutgoing(State.PLAY, 0x22, 0x2A, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT);
                handler(packetWrapper -> {
                    int type = packetWrapper.get(Type.INT, 0);
                    if (type > 41 && !ViaRewind.getConfig().isReplaceParticles()) {
                        packetWrapper.cancel();
                        return;
                    }
                    if (type == 42) { // Dragon Breath
                        packetWrapper.set(Type.INT, 0, 24); // Portal
                    } else if (type == 43) { // End Rod
                        packetWrapper.set(Type.INT, 0, 3); // Firework Spark
                    } else if (type == 44) { // Damage Indicator
                        packetWrapper.set(Type.INT, 0, 34); // Heart
                    } else if (type == 45) { // Sweep Attack
                        packetWrapper.set(Type.INT, 0, 1); // Large Explosion
                    }
                });
            }
        });

        //Map
        protocol.registerOutgoing(State.PLAY, 0x24, 0x34, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.VAR_INT);
                map(Type.BYTE);
                handler(packetWrapper -> packetWrapper.read(Type.BOOLEAN));
            }
        });

        //Combat Event
        protocol.registerOutgoing(State.PLAY, 0x2C, 0x42);

        //World Border
        protocol.registerOutgoing(State.PLAY, 0x35, 0x44);

        //Update Time
        protocol.registerOutgoing(State.PLAY, 0x44, 0x03);

        //Update Sign
        protocol.registerOutgoing(State.PLAY, 0x46, 0x33);

        //Sound Effects
        protocol.registerOutgoing(State.PLAY, 0x47, 0x29, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(packetWrapper -> {
                    int soundId = packetWrapper.read(Type.VAR_INT);
                    String sound = SoundRemapper.oldNameFromId(soundId);
                    if (sound == null) {
                        packetWrapper.cancel();
                    } else {
                        packetWrapper.write(Type.STRING, sound);
                    }
                });
                handler(packetWrapper -> packetWrapper.read(Type.VAR_INT));
                map(Type.INT);
                map(Type.INT);
                map(Type.INT);
                map(Type.FLOAT);
                map(Type.UNSIGNED_BYTE);
            }
        });
    }
}
