/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.JsonDeserializationContext;
import com.viaversion.viaversion.libs.gson.JsonDeserializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonNull;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.gson.JsonSerializationContext;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.internal.Streams;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.TextColorWrapper;
import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.EnumSet;
import org.jetbrains.annotations.Nullable;

final class StyleSerializer
implements JsonDeserializer<Style>,
JsonSerializer<Style> {
    private static final TextDecoration[] DECORATIONS = new TextDecoration[]{TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED};
    static final String FONT = "font";
    static final String COLOR = "color";
    static final String INSERTION = "insertion";
    static final String CLICK_EVENT = "clickEvent";
    static final String CLICK_EVENT_ACTION = "action";
    static final String CLICK_EVENT_VALUE = "value";
    static final String HOVER_EVENT = "hoverEvent";
    static final String HOVER_EVENT_ACTION = "action";
    static final String HOVER_EVENT_CONTENTS = "contents";
    @Deprecated
    static final String HOVER_EVENT_VALUE = "value";
    private final LegacyHoverEventSerializer legacyHover;
    private final boolean emitLegacyHover;

    StyleSerializer(@Nullable LegacyHoverEventSerializer legacyHover, boolean emitLegacyHover) {
        this.legacyHover = legacyHover;
        this.emitLegacyHover = emitLegacyHover;
    }

    @Override
    public Style deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return this.deserialize(object, context);
    }

    private Style deserialize(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        JsonObject hoverEvent;
        ClickEvent.Action action;
        JsonObject clickEvent;
        Style.Builder style = Style.style();
        if (json.has(FONT)) {
            style.font((Key)context.deserialize(json.get(FONT), (Type)((Object)Key.class)));
        }
        if (json.has(COLOR)) {
            TextColorWrapper color = (TextColorWrapper)context.deserialize(json.get(COLOR), (Type)((Object)TextColorWrapper.class));
            if (color.color != null) {
                style.color(color.color);
            } else if (color.decoration != null) {
                style.decoration(color.decoration, true);
            }
        }
        for (TextDecoration decoration : DECORATIONS) {
            String name = TextDecoration.NAMES.key(decoration);
            if (!json.has(name)) continue;
            style.decoration(decoration, json.get(name).getAsBoolean());
        }
        if (json.has(INSERTION)) {
            style.insertion(json.get(INSERTION).getAsString());
        }
        if (json.has(CLICK_EVENT) && (clickEvent = json.getAsJsonObject(CLICK_EVENT)) != null && (action = StyleSerializer.optionallyDeserialize(clickEvent.getAsJsonPrimitive("action"), context, ClickEvent.Action.class)) != null && action.readable()) {
            String value;
            @Nullable JsonPrimitive rawValue = clickEvent.getAsJsonPrimitive("value");
            String string = value = rawValue == null ? null : rawValue.getAsString();
            if (value != null) {
                style.clickEvent(ClickEvent.clickEvent(action, value));
            }
        }
        if (json.has(HOVER_EVENT) && (hoverEvent = json.getAsJsonObject(HOVER_EVENT)) != null && (action = StyleSerializer.optionallyDeserialize(hoverEvent.getAsJsonPrimitive("action"), context, HoverEvent.Action.class)) != null && action.readable()) {
            Object value;
            Object rawValue;
            if (hoverEvent.has(HOVER_EVENT_CONTENTS)) {
                rawValue = hoverEvent.get(HOVER_EVENT_CONTENTS);
                value = context.deserialize((JsonElement)rawValue, action.type());
            } else if (hoverEvent.has("value")) {
                rawValue = (Component)context.deserialize(hoverEvent.get("value"), (Type)((Object)Component.class));
                value = this.legacyHoverEventContents(action, (Component)rawValue, context);
            } else {
                value = null;
            }
            if (value != null) {
                style.hoverEvent(HoverEvent.hoverEvent(action, value));
            }
        }
        if (json.has(FONT)) {
            style.font((Key)context.deserialize(json.get(FONT), (Type)((Object)Key.class)));
        }
        return style.build();
    }

    private static <T> T optionallyDeserialize(JsonElement json, JsonDeserializationContext context, Class<T> type) {
        return json == null ? null : (T)context.deserialize(json, type);
    }

    private Object legacyHoverEventContents(HoverEvent.Action<?> action, Component rawValue, JsonDeserializationContext context) {
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return rawValue;
        }
        if (this.legacyHover != null) {
            try {
                if (action == HoverEvent.Action.SHOW_ENTITY) {
                    return this.legacyHover.deserializeShowEntity(rawValue, this.decoder(context));
                }
                if (action == HoverEvent.Action.SHOW_ITEM) {
                    return this.legacyHover.deserializeShowItem(rawValue);
                }
            }
            catch (IOException ex) {
                throw new JsonParseException(ex);
            }
        }
        throw new UnsupportedOperationException();
    }

    private Codec.Decoder<Component, String, JsonParseException> decoder(JsonDeserializationContext ctx) {
        return string -> {
            JsonReader reader = new JsonReader(new StringReader((String)string));
            return (Component)ctx.deserialize(Streams.parse(reader), (Type)((Object)Component.class));
        };
    }

    @Override
    public JsonElement serialize(Style src, Type typeOfSrc, JsonSerializationContext context) {
        Key font;
        HoverEvent<?> hoverEvent;
        ClickEvent clickEvent;
        String insertion;
        JsonObject json = new JsonObject();
        for (TextDecoration decoration : DECORATIONS) {
            TextDecoration.State state = src.decoration(decoration);
            if (state == TextDecoration.State.NOT_SET) continue;
            String name = TextDecoration.NAMES.key(decoration);
            assert (name != null);
            json.addProperty(name, state == TextDecoration.State.TRUE);
        }
        @Nullable TextColor color = src.color();
        if (color != null) {
            json.add(COLOR, context.serialize(color));
        }
        if ((insertion = src.insertion()) != null) {
            json.addProperty(INSERTION, insertion);
        }
        if ((clickEvent = src.clickEvent()) != null) {
            JsonObject eventJson = new JsonObject();
            eventJson.add("action", context.serialize((Object)clickEvent.action()));
            eventJson.addProperty("value", clickEvent.value());
            json.add(CLICK_EVENT, eventJson);
        }
        if ((hoverEvent = src.hoverEvent()) != null) {
            JsonObject eventJson = new JsonObject();
            eventJson.add("action", context.serialize(hoverEvent.action()));
            JsonElement modernContents = context.serialize(hoverEvent.value());
            eventJson.add(HOVER_EVENT_CONTENTS, modernContents);
            if (this.emitLegacyHover) {
                eventJson.add("value", this.serializeLegacyHoverEvent(hoverEvent, modernContents, context));
            }
            json.add(HOVER_EVENT, eventJson);
        }
        if ((font = src.font()) != null) {
            json.add(FONT, context.serialize(font));
        }
        return json;
    }

    private JsonElement serializeLegacyHoverEvent(HoverEvent<?> hoverEvent, JsonElement modernContents, JsonSerializationContext context) {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
            return modernContents;
        }
        if (this.legacyHover != null) {
            Component serialized = null;
            try {
                if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                    serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder(context));
                } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                    serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
                }
            }
            catch (IOException ex) {
                throw new JsonSyntaxException(ex);
            }
            return serialized == null ? JsonNull.INSTANCE : context.serialize(serialized);
        }
        return JsonNull.INSTANCE;
    }

    private Codec.Encoder<Component, String, RuntimeException> encoder(JsonSerializationContext ctx) {
        return component -> ctx.serialize(component).toString();
    }

    static {
        EnumSet<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
        for (TextDecoration decoration : DECORATIONS) {
            knownDecorations.remove(decoration);
        }
        if (!knownDecorations.isEmpty()) {
            throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
        }
    }
}

