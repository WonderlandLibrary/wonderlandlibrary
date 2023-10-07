package viaversion.viaversion.protocols.protocol1_9to1_8;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.platform.providers.ViaProviders;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.remapper.ValueTransformer;
import viaversion.viaversion.api.rewriters.MetadataRewriter;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.packets.State;
import viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import viaversion.viaversion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
import viaversion.viaversion.protocols.protocol1_9to1_8.packets.EntityPackets;
import viaversion.viaversion.protocols.protocol1_9to1_8.packets.InventoryPackets;
import viaversion.viaversion.protocols.protocol1_9to1_8.packets.PlayerPackets;
import viaversion.viaversion.protocols.protocol1_9to1_8.packets.SpawnPackets;
import viaversion.viaversion.protocols.protocol1_9to1_8.packets.WorldPackets;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.HandItemProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.MainHandProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.CommandBlockStorage;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.InventoryTracker;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.PlaceBlockTracker;
import viaversion.viaversion.util.GsonUtil;

import java.util.List;

public class Protocol1_9To1_8 extends Protocol<ClientboundPackets1_8, ClientboundPackets1_9, ServerboundPackets1_8, ServerboundPackets1_9> {
    public static final ValueTransformer<String, JsonElement> FIX_JSON = new ValueTransformer<String, JsonElement>(Type.COMPONENT) {
        @Override
        public JsonElement transform(PacketWrapper wrapper, String line) {
            return fixJson(line);
        }
    };

    public Protocol1_9To1_8() {
        super(ClientboundPackets1_8.class, ClientboundPackets1_9.class, ServerboundPackets1_8.class, ServerboundPackets1_9.class);
    }

    public static JsonElement fixJson(String line) {
        if (line == null || line.equalsIgnoreCase("null")) {
            line = "{\"text\":\"\"}";
        } else {
            if ((!line.startsWith("\"") || !line.endsWith("\"")) && (!line.startsWith("{") || !line.endsWith("}"))) {
                return constructJson(line);
            }
            if (line.startsWith("\"") && line.endsWith("\"")) {
                line = "{\"text\":" + line + "}";
            }
        }
        try {
            return GsonUtil.getGson().fromJson(line, JsonObject.class);
        } catch (Exception e) {
            if (Via.getConfig().isForceJsonTransform()) {
                return constructJson(line);
            } else {
                Via.getPlatform().getLogger().warning("Invalid JSON String: \"" + line + "\" Please report this issue to the ViaVersion Github: " + e.getMessage());
                return GsonUtil.getGson().fromJson("{\"text\":\"\"}", JsonObject.class);
            }
        }
    }

    private static JsonElement constructJson(String text) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);
        return jsonObject;
    }

    public static Item getHandItem(final UserConnection info) {
        return Via.getManager().getProviders().get(HandItemProvider.class).getHandItem(info);
    }

    public static boolean isSword(int id) {
        if (id == 267) return true; // Iron
        if (id == 268) return true; // Wood
        if (id == 272) return true; // Stone
        if (id == 276) return true; // Diamond
        if (id == 283) return true; // Gold

        return false;
    }

    @Override
    protected void registerPackets() {
        MetadataRewriter metadataRewriter = new MetadataRewriter1_9To1_8(this);

        // Disconnect workaround (JSON!)
        registerOutgoing(State.LOGIN, 0x00, 0x00, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    if (wrapper.isReadable(Type.COMPONENT, 0)) {
                        // Already written as json somewhere else
                        return;
                    }

                    wrapper.write(Type.COMPONENT, fixJson(wrapper.read(Type.STRING)));
                });
            }
        });

        // Other Handlers
        SpawnPackets.register(this);
        InventoryPackets.register(this);
        EntityPackets.register(this);
        PlayerPackets.register(this);
        WorldPackets.register(this);
    }

    @Override
    protected void register(ViaProviders providers) {
        providers.register(HandItemProvider.class, new HandItemProvider());
        providers.register(BulkChunkTranslatorProvider.class, new BulkChunkTranslatorProvider());
        providers.register(CommandBlockProvider.class, new CommandBlockProvider());
        providers.register(EntityIdProvider.class, new EntityIdProvider());
        providers.register(BossBarProvider.class, new BossBarProvider());
        providers.register(MainHandProvider.class, new MainHandProvider());
        providers.require(MovementTransmitterProvider.class);
    }

    @Override
    public boolean isFiltered(Class packetClass) {
        return Via.getManager().getProviders().get(BulkChunkTranslatorProvider.class).isFiltered(packetClass);

    }

    @Override
    protected void filterPacket(UserConnection info, Object packet, List output) throws Exception {
        output.addAll(info.get(ClientChunks.class).transformMapChunkBulk(packet));
    }

    @Override
    public void init(UserConnection userConnection) {
        // Entity tracker
        userConnection.put(new EntityTracker1_9(userConnection));
        // Chunk tracker
        userConnection.put(new ClientChunks(userConnection));
        // Movement tracker
        userConnection.put(new MovementTracker(userConnection));
        // Inventory tracker
        userConnection.put(new InventoryTracker(userConnection));
        // Place block tracker
        userConnection.put(new PlaceBlockTracker(userConnection));
        // CommandBlock storage
        userConnection.put(new CommandBlockStorage(userConnection));
    }
}
