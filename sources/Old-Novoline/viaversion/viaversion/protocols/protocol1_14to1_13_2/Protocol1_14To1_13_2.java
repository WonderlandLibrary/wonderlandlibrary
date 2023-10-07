package viaversion.viaversion.protocols.protocol1_14to1_13_2;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.remapper.PacketHandler;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.rewriters.ComponentRewriter;
import viaversion.viaversion.api.rewriters.MetadataRewriter;
import viaversion.viaversion.api.rewriters.SoundRewriter;
import viaversion.viaversion.api.rewriters.StatisticsRewriter;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.data.ComponentRewriter1_14;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.data.MappingData;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.metadata.MetadataRewriter1_14To1_13_2;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.EntityPackets;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.InventoryPackets;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.PlayerPackets;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.WorldPackets;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;

public class Protocol1_14To1_13_2 extends Protocol<ClientboundPackets1_13, ClientboundPackets1_14, ServerboundPackets1_13, ServerboundPackets1_14> {

    public static final MappingData MAPPINGS = new MappingData();

    public Protocol1_14To1_13_2() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_14.class, ServerboundPackets1_13.class, ServerboundPackets1_14.class);
    }

    @Override
    protected void registerPackets() {
        MetadataRewriter metadataRewriter = new MetadataRewriter1_14To1_13_2(this);

        InventoryPackets.register(this);
        EntityPackets.register(this);
        WorldPackets.register(this);
        PlayerPackets.register(this);

        new SoundRewriter(this).registerSound(ClientboundPackets1_13.SOUND);
        new StatisticsRewriter(this, metadataRewriter::getNewEntityId).register(ClientboundPackets1_13.STATISTICS);

        ComponentRewriter componentRewriter = new ComponentRewriter1_14(this);
        componentRewriter.registerChatMessage(ClientboundPackets1_13.CHAT_MESSAGE);

        registerOutgoing(ClientboundPackets1_13.TAGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(new PacketHandler() {
                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int blockTagsSize = wrapper.read(Type.VAR_INT);
                        wrapper.write(Type.VAR_INT, blockTagsSize + 6); // block tags
                        for (int i = 0; i < blockTagsSize; i++) {
                            wrapper.passthrough(Type.STRING);
                            int[] blockIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                            for (int j = 0; j < blockIds.length; j++) {
                                blockIds[j] = getMappingData().getNewBlockId(blockIds[j]);
                            }
                        }
                        // Minecraft crashes if we not send signs tags
                        wrapper.write(Type.STRING, "minecraft:signs");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{
                                getMappingData().getNewBlockId(150), getMappingData().getNewBlockId(155)
                        });
                        wrapper.write(Type.STRING, "minecraft:wall_signs");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{
                                getMappingData().getNewBlockId(155)
                        });
                        wrapper.write(Type.STRING, "minecraft:standing_signs");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{
                                getMappingData().getNewBlockId(150)
                        });
                        // Fences and walls tags - used for block connections
                        wrapper.write(Type.STRING, "minecraft:fences");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{189, 248, 472, 473, 474, 475});
                        wrapper.write(Type.STRING, "minecraft:walls");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{271, 272});
                        wrapper.write(Type.STRING, "minecraft:wooden_fences");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{189, 472, 473, 474, 475});
                        int itemTagsSize = wrapper.read(Type.VAR_INT);
                        wrapper.write(Type.VAR_INT, itemTagsSize + 2); // item tags
                        for (int i = 0; i < itemTagsSize; i++) {
                            wrapper.passthrough(Type.STRING);
                            int[] itemIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                            for (int j = 0; j < itemIds.length; j++) {
                                itemIds[j] = getMappingData().getNewItemId(itemIds[j]);
                            }
                        }
                        // Should fix fuel shift clicking
                        wrapper.write(Type.STRING, "minecraft:signs");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{
                                getMappingData().getNewItemId(541)
                        });
                        // Arrows tag (used by bow)
                        wrapper.write(Type.STRING, "minecraft:arrows");
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{526, 825, 826});
                        int fluidTagsSize = wrapper.passthrough(Type.VAR_INT); // fluid tags
                        for (int i = 0; i < fluidTagsSize; i++) {
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                        }
                        wrapper.write(Type.VAR_INT, 0);  // new entity tags - do we need to send this?
                    }
                });
            }
        });

        // Set Difficulty packet added in 19w11a
        cancelIncoming(ServerboundPackets1_14.SET_DIFFICULTY);
        // Lock Difficulty packet added in 19w11a
        cancelIncoming(ServerboundPackets1_14.LOCK_DIFFICULTY);
        // Unknown packet added in 19w13a
        cancelIncoming(ServerboundPackets1_14.UPDATE_JIGSAW_BLOCK);
    }

    @Override
    protected void onMappingDataLoaded() {
        WorldPackets.air = getMappingData().getBlockStateMappings().getNewId(0);
        WorldPackets.voidAir = getMappingData().getBlockStateMappings().getNewId(8591);
        WorldPackets.caveAir = getMappingData().getBlockStateMappings().getNewId(8592);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new EntityTracker1_14(userConnection));
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }

    @Override
    public MappingData getMappingData() {
        return MAPPINGS;
    }
}
