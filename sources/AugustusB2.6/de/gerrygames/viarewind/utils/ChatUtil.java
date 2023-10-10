// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.ComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.logging.Level;
import de.gerrygames.viarewind.ViaRewind;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.util.regex.Pattern;

public class ChatUtil
{
    private static final Pattern UNUSED_COLOR_PATTERN;
    
    public static String jsonToLegacy(final String json) {
        if (json == null || json.equals("null") || json.isEmpty()) {
            return "";
        }
        try {
            String legacy;
            for (legacy = LegacyComponentSerializer.legacySection().serialize(((ComponentSerializer<I, Component, String>)ChatRewriter.HOVER_GSON_SERIALIZER).deserialize(json)); legacy.startsWith("§f"); legacy = legacy.substring(2)) {}
            return legacy;
        }
        catch (Exception ex) {
            ViaRewind.getPlatform().getLogger().log(Level.WARNING, "Could not convert component to legacy text: " + json, ex);
            return "";
        }
    }
    
    public static String jsonToLegacy(final JsonElement component) {
        if (component.isJsonNull() || (component.isJsonArray() && component.getAsJsonArray().isEmpty()) || (component.isJsonObject() && component.getAsJsonObject().size() == 0)) {
            return "";
        }
        if (component.isJsonPrimitive()) {
            return component.getAsString();
        }
        return jsonToLegacy(component.toString());
    }
    
    public static String legacyToJson(final String legacy) {
        if (legacy == null) {
            return "";
        }
        return ((ComponentSerializer<TextComponent, O, String>)GsonComponentSerializer.gson()).serialize(LegacyComponentSerializer.legacySection().deserialize(legacy));
    }
    
    public static String removeUnusedColor(String legacy, char last) {
        if (legacy == null) {
            return null;
        }
        legacy = ChatUtil.UNUSED_COLOR_PATTERN.matcher(legacy).replaceAll("$1$2");
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < legacy.length(); ++i) {
            char current = legacy.charAt(i);
            if (current != '§' || i == legacy.length() - 1) {
                builder.append(current);
            }
            else {
                current = legacy.charAt(++i);
                if (current != last) {
                    builder.append('§').append(current);
                    last = current;
                }
            }
        }
        return builder.toString();
    }
    
    static {
        UNUSED_COLOR_PATTERN = Pattern.compile("(?>(?>§[0-fk-or])*(§r|\\Z))|(?>(?>§[0-f])*(§[0-f]))");
    }
}
