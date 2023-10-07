package viaversion.viaversion.protocols.protocol1_16_2to1_16_1;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.rewriters.MetadataRewriter;
import viaversion.viaversion.api.rewriters.RegistryType;
import viaversion.viaversion.api.rewriters.SoundRewriter;
import viaversion.viaversion.api.rewriters.StatisticsRewriter;
import viaversion.viaversion.api.rewriters.TagRewriter;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_16_2to1_16_1.data.MappingData;
import viaversion.viaversion.protocols.protocol1_16_2to1_16_1.metadata.MetadataRewriter1_16_2To1_16_1;
import viaversion.viaversion.protocols.protocol1_16_2to1_16_1.packets.EntityPackets;
import viaversion.viaversion.protocols.protocol1_16_2to1_16_1.packets.InventoryPackets;
import viaversion.viaversion.protocols.protocol1_16_2to1_16_1.packets.WorldPackets;
import viaversion.viaversion.protocols.protocol1_16_2to1_16_1.storage.EntityTracker1_16_2;
import viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;

public class Protocol1_16_2To1_16_1 extends Protocol<ClientboundPackets1_16, ClientboundPackets1_16_2, ServerboundPackets1_16, ServerboundPackets1_16_2> {

    public static final MappingData MAPPINGS = new MappingData();
    private TagRewriter tagRewriter;

    public Protocol1_16_2To1_16_1() {
        super(ClientboundPackets1_16.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16.class, ServerboundPackets1_16_2.class);
    }

    @Override
    protected void registerPackets() {
        MetadataRewriter metadataRewriter = new MetadataRewriter1_16_2To1_16_1(this);

        EntityPackets.register(this);
        WorldPackets.register(this);
        InventoryPackets.register(this);

        tagRewriter = new TagRewriter(this, metadataRewriter::getNewEntityId);
        tagRewriter.register(ClientboundPackets1_16.TAGS);

        new StatisticsRewriter(this, metadataRewriter::getNewEntityId).register(ClientboundPackets1_16.STATISTICS);

        SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_16.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16.ENTITY_SOUND);

        // Recipe book data has been split into 2 separate packets
        registerIncoming(ServerboundPackets1_16_2.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    int recipeType = wrapper.read(Type.VAR_INT);
                    boolean open = wrapper.read(Type.BOOLEAN);
                    boolean filter = wrapper.read(Type.BOOLEAN);
                    wrapper.write(Type.VAR_INT, 1); // Settings
                    wrapper.write(Type.BOOLEAN, recipeType == 0); // Crafting
                    wrapper.write(Type.BOOLEAN, filter);
                    wrapper.write(Type.BOOLEAN, recipeType == 1); // Furnace
                    wrapper.write(Type.BOOLEAN, filter);
                    wrapper.write(Type.BOOLEAN, recipeType == 2); // Blast Furnace
                    wrapper.write(Type.BOOLEAN, filter);
                    wrapper.write(Type.BOOLEAN, recipeType == 3); // Smoker
                    wrapper.write(Type.BOOLEAN, filter);
                });
            }
        });
        registerIncoming(ServerboundPackets1_16_2.SEEN_RECIPE, ServerboundPackets1_16.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    String recipe = wrapper.read(Type.STRING);
                    wrapper.write(Type.VAR_INT, 0); // Shown
                    wrapper.write(Type.STRING, recipe);
                });
            }
        });
    }

    @Override
    protected void onMappingDataLoaded() {
        tagRewriter.addTag(RegistryType.ITEM, "minecraft:stone_crafting_materials", 14, 962);
        tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:mushroom_grow_block");

        // The client now wants ALL (previous) tags to be sent, sooooo :>
        tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:soul_fire_base_blocks", "minecraft:furnace_materials", "minecraft:crimson_stems",
                "minecraft:gold_ores", "minecraft:piglin_loved", "minecraft:piglin_repellents", "minecraft:creeper_drop_music_discs",
                "minecraft:logs_that_burn", "minecraft:stone_tool_materials", "minecraft:warped_stems");
        tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:infiniburn_nether", "minecraft:crimson_stems",
                "minecraft:wither_summon_base_blocks", "minecraft:infiniburn_overworld", "minecraft:piglin_repellents",
                "minecraft:hoglin_repellents", "minecraft:prevent_mob_spawning_inside", "minecraft:wart_blocks",
                "minecraft:stone_pressure_plates", "minecraft:nylium", "minecraft:gold_ores", "minecraft:pressure_plates",
                "minecraft:logs_that_burn", "minecraft:strider_warm_blocks", "minecraft:warped_stems", "minecraft:infiniburn_end",
                "minecraft:base_stone_nether", "minecraft:base_stone_overworld");
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new EntityTracker1_16_2(userConnection));
    }

    @Override
    public MappingData getMappingData() {
        return MAPPINGS;
    }
}
