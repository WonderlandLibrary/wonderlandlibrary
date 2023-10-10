// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8;

import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.CommandBlockStorage;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.InventoryTracker;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.platform.providers.Provider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CompressionProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MainHandProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.WorldPackets;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.PlayerPackets;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.EntityPackets;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.SpawnPackets;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.HandItemProvider;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_9To1_8 extends AbstractProtocol<ClientboundPackets1_8, ClientboundPackets1_9, ServerboundPackets1_8, ServerboundPackets1_9>
{
    public static final ValueTransformer<String, JsonElement> FIX_JSON;
    private final EntityRewriter metadataRewriter;
    
    public Protocol1_9To1_8() {
        super(ClientboundPackets1_8.class, ClientboundPackets1_9.class, ServerboundPackets1_8.class, ServerboundPackets1_9.class);
        this.metadataRewriter = new MetadataRewriter1_9To1_8(this);
    }
    
    public static JsonElement fixJson(String line) {
        if (line == null || line.equalsIgnoreCase("null")) {
            line = "{\"text\":\"\"}";
        }
        else {
            if ((!line.startsWith("\"") || !line.endsWith("\"")) && (!line.startsWith("{") || !line.endsWith("}"))) {
                return constructJson(line);
            }
            if (line.startsWith("\"") && line.endsWith("\"")) {
                line = "{\"text\":" + line + "}";
            }
        }
        try {
            return GsonUtil.getGson().fromJson(line, JsonObject.class);
        }
        catch (Exception e) {
            if (Via.getConfig().isForceJsonTransform()) {
                return constructJson(line);
            }
            Via.getPlatform().getLogger().warning("Invalid JSON String: \"" + line + "\" Please report this issue to the ViaVersion Github: " + e.getMessage());
            return GsonUtil.getGson().fromJson("{\"text\":\"\"}", JsonObject.class);
        }
    }
    
    private static JsonElement constructJson(final String text) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);
        return jsonObject;
    }
    
    public static Item getHandItem(final UserConnection info) {
        return Via.getManager().getProviders().get(HandItemProvider.class).getHandItem(info);
    }
    
    public static boolean isSword(final int id) {
        return id == 267 || id == 268 || id == 272 || id == 276 || id == 283;
    }
    
    @Override
    protected void registerPackets() {
        this.metadataRewriter.register();
        this.registerClientbound(State.LOGIN, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    if (!wrapper.isReadable(Type.COMPONENT, 0)) {
                        wrapper.write(Type.COMPONENT, Protocol1_9To1_8.fixJson(wrapper.read(Type.STRING)));
                    }
                });
            }
        });
        SpawnPackets.register(this);
        InventoryPackets.register(this);
        EntityPackets.register(this);
        PlayerPackets.register(this);
        WorldPackets.register(this);
    }
    
    @Override
    public void register(final ViaProviders providers) {
        providers.register(HandItemProvider.class, new HandItemProvider());
        providers.register(CommandBlockProvider.class, new CommandBlockProvider());
        providers.register(EntityIdProvider.class, new EntityIdProvider());
        providers.register(BossBarProvider.class, new BossBarProvider());
        providers.register(MainHandProvider.class, new MainHandProvider());
        providers.register(CompressionProvider.class, new CompressionProvider());
        providers.require(MovementTransmitterProvider.class);
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.addEntityTracker(this.getClass(), new EntityTracker1_9(userConnection));
        userConnection.put(new ClientChunks(userConnection));
        userConnection.put(new MovementTracker());
        userConnection.put(new InventoryTracker());
        userConnection.put(new CommandBlockStorage());
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.metadataRewriter;
    }
    
    static {
        FIX_JSON = new ValueTransformer<String, JsonElement>() {
            @Override
            public JsonElement transform(final PacketWrapper wrapper, final String line) {
                return Protocol1_9To1_8.fixJson(line);
            }
        };
    }
}
