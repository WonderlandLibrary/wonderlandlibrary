// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.PlayerPositionStorage1_13;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets.SoundPackets1_13;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets.PlayerPacket1_13;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data.PaintingMapping;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets.EntityPackets1_13;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets.BlockItemPackets1_13;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_12_2To1_13 extends BackwardsProtocol<ClientboundPackets1_13, ClientboundPackets1_12_1, ServerboundPackets1_13, ServerboundPackets1_12_1>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityRewriter entityRewriter;
    private final BlockItemPackets1_13 blockItemPackets;
    
    public Protocol1_12_2To1_13() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_12_1.class, ServerboundPackets1_13.class, ServerboundPackets1_12_1.class);
        this.entityRewriter = new EntityPackets1_13(this);
        this.blockItemPackets = new BlockItemPackets1_13(this);
    }
    
    @Override
    protected void registerPackets() {
        this.executeAsyncAfterLoaded(Protocol1_13To1_12_2.class, () -> {
            Protocol1_12_2To1_13.MAPPINGS.load();
            PaintingMapping.init();
            Via.getManager().getProviders().register(BackwardsBlockEntityProvider.class, new BackwardsBlockEntityProvider());
            return;
        });
        final TranslatableRewriter translatableRewriter = new TranslatableRewriter(this) {
            @Override
            protected void handleTranslate(final JsonObject root, final String translate) {
                String newTranslate = this.newTranslatables.get(translate);
                if (newTranslate != null || (newTranslate = Protocol1_12_2To1_13.this.getMappingData().getTranslateMappings().get(translate)) != null) {
                    root.addProperty("translate", newTranslate);
                }
            }
        };
        translatableRewriter.registerPing();
        translatableRewriter.registerBossBar(ClientboundPackets1_13.BOSSBAR);
        translatableRewriter.registerChatMessage(ClientboundPackets1_13.CHAT_MESSAGE);
        translatableRewriter.registerLegacyOpenWindow(ClientboundPackets1_13.OPEN_WINDOW);
        translatableRewriter.registerDisconnect(ClientboundPackets1_13.DISCONNECT);
        translatableRewriter.registerCombatEvent(ClientboundPackets1_13.COMBAT_EVENT);
        translatableRewriter.registerTitle(ClientboundPackets1_13.TITLE);
        translatableRewriter.registerTabList(ClientboundPackets1_13.TAB_LIST);
        this.blockItemPackets.register();
        this.entityRewriter.register();
        new PlayerPacket1_13(this).register();
        new SoundPackets1_13(this).register();
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_13.NBT_QUERY);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_13.CRAFT_RECIPE_RESPONSE);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_13.UNLOCK_RECIPES);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_13.ADVANCEMENTS);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_13.DECLARE_RECIPES);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_13.TAGS);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_12_1>)this).cancelServerbound(ServerboundPackets1_12_1.CRAFT_RECIPE_REQUEST);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_12_1>)this).cancelServerbound(ServerboundPackets1_12_1.RECIPE_BOOK_DATA);
    }
    
    @Override
    public void init(final UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_13Types.EntityType.PLAYER));
        user.put(new BackwardsBlockStorage());
        user.put(new TabCompleteStorage());
        if (ViaBackwards.getConfig().isFix1_13FacePlayer() && !user.has(PlayerPositionStorage1_13.class)) {
            user.put(new PlayerPositionStorage1_13());
        }
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_12_2To1_13.MAPPINGS;
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_13 getItemRewriter() {
        return this.blockItemPackets;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
    }
}
