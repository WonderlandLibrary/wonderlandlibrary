// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.HashMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
import java.util.Map;
import com.viaversion.viaversion.rewriter.ComponentRewriter;

public class TranslatableRewriter extends ComponentRewriter
{
    private static final Map<String, Map<String, String>> TRANSLATABLES;
    protected final Map<String, String> newTranslatables;
    
    public static void loadTranslatables() {
        final JsonObject jsonObject = VBMappingDataLoader.loadData("translation-mappings.json");
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final Map<String, String> versionMappings = new HashMap<String, String>();
            TranslatableRewriter.TRANSLATABLES.put(entry.getKey(), versionMappings);
            for (final Map.Entry<String, JsonElement> translationEntry : entry.getValue().getAsJsonObject().entrySet()) {
                versionMappings.put(translationEntry.getKey(), translationEntry.getValue().getAsString());
            }
        }
    }
    
    public TranslatableRewriter(final BackwardsProtocol protocol) {
        this(protocol, protocol.getClass().getSimpleName().split("To")[1].replace("_", "."));
    }
    
    public TranslatableRewriter(final BackwardsProtocol protocol, final String sectionIdentifier) {
        super(protocol);
        final Map<String, String> newTranslatables = TranslatableRewriter.TRANSLATABLES.get(sectionIdentifier);
        if (newTranslatables == null) {
            ViaBackwards.getPlatform().getLogger().warning("Error loading " + sectionIdentifier + " translatables!");
            this.newTranslatables = new HashMap<String, String>();
        }
        else {
            this.newTranslatables = newTranslatables;
        }
    }
    
    public void registerPing() {
        this.protocol.registerClientbound(State.LOGIN, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerDisconnect(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    @Override
    public void registerChatMessage(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerLegacyOpenWindow(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerOpenWindow(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerTabList(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT));
                    TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT));
                });
            }
        });
    }
    
    public void registerCombatKill(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    @Override
    protected void handleTranslate(final JsonObject root, final String translate) {
        final String newTranslate = this.newTranslatables.get(translate);
        if (newTranslate != null) {
            root.addProperty("translate", newTranslate);
        }
    }
    
    static {
        TRANSLATABLES = new HashMap<String, Map<String, String>>();
    }
}
