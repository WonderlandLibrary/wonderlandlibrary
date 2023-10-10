// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter;

import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonParser;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;

public class ComponentRewriter
{
    protected final Protocol protocol;
    
    public ComponentRewriter(final Protocol protocol) {
        this.protocol = protocol;
    }
    
    public ComponentRewriter() {
        this.protocol = null;
    }
    
    public void registerComponentPacket(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> ComponentRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    @Deprecated
    public void registerChatMessage(final ClientboundPacketType packetType) {
        this.registerComponentPacket(packetType);
    }
    
    public void registerBossBar(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                final int action;
                this.handler(wrapper -> {
                    action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (action == 0 || action == 3) {
                        ComponentRewriter.this.processText(wrapper.passthrough(Type.COMPONENT));
                    }
                });
            }
        });
    }
    
    public void registerCombatEvent(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    if (wrapper.passthrough((Type<Integer>)Type.VAR_INT) == 2) {
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        ComponentRewriter.this.processText(wrapper.passthrough(Type.COMPONENT));
                    }
                });
            }
        });
    }
    
    public void registerTitle(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int action;
                this.handler(wrapper -> {
                    action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    if (action >= 0 && action <= 2) {
                        ComponentRewriter.this.processText(wrapper.passthrough(Type.COMPONENT));
                    }
                });
            }
        });
    }
    
    public JsonElement processText(final String value) {
        try {
            final JsonElement root = JsonParser.parseString(value);
            this.processText(root);
            return root;
        }
        catch (JsonSyntaxException e) {
            if (Via.getManager().isDebug()) {
                Via.getPlatform().getLogger().severe("Error when trying to parse json: " + value);
                throw e;
            }
            return new JsonPrimitive(value);
        }
    }
    
    public void processText(final JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return;
        }
        if (element.isJsonArray()) {
            this.processAsArray(element);
            return;
        }
        if (element.isJsonPrimitive()) {
            this.handleText(element.getAsJsonPrimitive());
            return;
        }
        final JsonObject object = element.getAsJsonObject();
        final JsonPrimitive text = object.getAsJsonPrimitive("text");
        if (text != null) {
            this.handleText(text);
        }
        final JsonElement translate = object.get("translate");
        if (translate != null) {
            this.handleTranslate(object, translate.getAsString());
            final JsonElement with = object.get("with");
            if (with != null) {
                this.processAsArray(with);
            }
        }
        final JsonElement extra = object.get("extra");
        if (extra != null) {
            this.processAsArray(extra);
        }
        final JsonObject hoverEvent = object.getAsJsonObject("hoverEvent");
        if (hoverEvent != null) {
            this.handleHoverEvent(hoverEvent);
        }
    }
    
    protected void handleText(final JsonPrimitive text) {
    }
    
    protected void handleTranslate(final JsonObject object, final String translate) {
    }
    
    protected void handleHoverEvent(final JsonObject hoverEvent) {
        final String action = hoverEvent.getAsJsonPrimitive("action").getAsString();
        if (action.equals("show_text")) {
            final JsonElement value = hoverEvent.get("value");
            this.processText((value != null) ? value : hoverEvent.get("contents"));
        }
        else if (action.equals("show_entity")) {
            final JsonObject contents = hoverEvent.getAsJsonObject("contents");
            if (contents != null) {
                this.processText(contents.get("name"));
            }
        }
    }
    
    private void processAsArray(final JsonElement element) {
        for (final JsonElement jsonElement : element.getAsJsonArray()) {
            this.processText(jsonElement);
        }
    }
    
    public <T extends Protocol> T getProtocol() {
        return (T)this.protocol;
    }
}
