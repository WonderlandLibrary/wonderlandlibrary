// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2;

import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.ComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl.NBTLegacyHoverEventSerializer;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonParser;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public final class ChatRewriter
{
    public static final GsonComponentSerializer HOVER_GSON_SERIALIZER;
    
    public static String legacyTextToJsonString(final String message, final boolean itemData) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(message);
        if (itemData) {
            component = ((ComponentBuilder<Component, B>)((ComponentBuilder<C, TextComponent.Builder>)((ComponentBuilder<C, TextComponent.Builder>)Component.text()).decoration(TextDecoration.ITALIC, false)).append(component)).build();
        }
        return ((ComponentSerializer<Component, O, String>)GsonComponentSerializer.gson()).serialize(component);
    }
    
    public static String legacyTextToJsonString(final String legacyText) {
        return legacyTextToJsonString(legacyText, false);
    }
    
    public static JsonElement legacyTextToJson(final String legacyText) {
        return JsonParser.parseString(legacyTextToJsonString(legacyText, false));
    }
    
    public static String jsonToLegacyText(final String value) {
        try {
            final Component component = ((ComponentSerializer<I, Component, String>)ChatRewriter.HOVER_GSON_SERIALIZER).deserialize(value);
            return LegacyComponentSerializer.legacySection().serialize(component);
        }
        catch (final Exception e) {
            Via.getPlatform().getLogger().warning("Error converting json text to legacy: " + value);
            e.printStackTrace();
            return "";
        }
    }
    
    @Deprecated
    public static void processTranslate(final JsonElement value) {
        Via.getManager().getProtocolManager().getProtocol(Protocol1_13To1_12_2.class).getComponentRewriter().processText(value);
    }
    
    static {
        HOVER_GSON_SERIALIZER = GsonComponentSerializer.builder().emitLegacyHoverEvent().legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get()).build();
    }
}
