// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import java.util.Set;
import java.util.EnumSet;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class StyleSerializer extends TypeAdapter<Style>
{
    private static final TextDecoration[] DECORATIONS;
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
    private final Gson gson;
    
    static TypeAdapter<Style> create(@Nullable final LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover, final Gson gson) {
        return new StyleSerializer(legacyHover, emitLegacyHover, gson).nullSafe();
    }
    
    private StyleSerializer(@Nullable final LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover, final Gson gson) {
        this.legacyHover = legacyHover;
        this.emitLegacyHover = emitLegacyHover;
        this.gson = gson;
    }
    
    @Override
    public Style read(final JsonReader in) throws IOException {
        in.beginObject();
        final Style.Builder style = Style.style();
        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals("font")) {
                style.font(this.gson.fromJson(in, SerializerFactory.KEY_TYPE));
            }
            else if (fieldName.equals("color")) {
                final TextColorWrapper color = this.gson.fromJson(in, SerializerFactory.COLOR_WRAPPER_TYPE);
                if (color.color != null) {
                    style.color(color.color);
                }
                else {
                    if (color.decoration == null) {
                        continue;
                    }
                    style.decoration(color.decoration, TextDecoration.State.TRUE);
                }
            }
            else if (TextDecoration.NAMES.keys().contains(fieldName)) {
                style.decoration(TextDecoration.NAMES.value(fieldName), this.readBoolean(in));
            }
            else if (fieldName.equals("insertion")) {
                style.insertion(in.nextString());
            }
            else if (fieldName.equals("clickEvent")) {
                in.beginObject();
                ClickEvent.Action action = null;
                String value = null;
                while (in.hasNext()) {
                    final String clickEventField = in.nextName();
                    if (clickEventField.equals("action")) {
                        action = this.gson.fromJson(in, SerializerFactory.CLICK_ACTION_TYPE);
                    }
                    else if (clickEventField.equals("value")) {
                        value = ((in.peek() == JsonToken.NULL) ? null : in.nextString());
                    }
                    else {
                        in.skipValue();
                    }
                }
                if (action != null && action.readable() && value != null) {
                    style.clickEvent(ClickEvent.clickEvent(action, value));
                }
                in.endObject();
            }
            else if (fieldName.equals("hoverEvent")) {
                final JsonObject hoverEventObject = this.gson.fromJson(in, JsonObject.class);
                if (hoverEventObject == null) {
                    continue;
                }
                final JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive("action");
                if (serializedAction == null) {
                    continue;
                }
                final HoverEvent.Action<Object> action2 = this.gson.fromJson(serializedAction, (Class<HoverEvent.Action<Object>>)SerializerFactory.HOVER_ACTION_TYPE);
                if (!action2.readable()) {
                    continue;
                }
                Object value2;
                if (hoverEventObject.has("contents")) {
                    final JsonElement rawValue = hoverEventObject.get("contents");
                    final Class<?> actionType = action2.type();
                    if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                        value2 = this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
                    }
                    else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                        value2 = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ITEM_TYPE);
                    }
                    else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                        value2 = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ENTITY_TYPE);
                    }
                    else {
                        value2 = null;
                    }
                }
                else if (hoverEventObject.has("value")) {
                    final Component rawValue2 = this.gson.fromJson(hoverEventObject.get("value"), SerializerFactory.COMPONENT_TYPE);
                    value2 = this.legacyHoverEventContents(action2, rawValue2);
                }
                else {
                    value2 = null;
                }
                if (value2 == null) {
                    continue;
                }
                style.hoverEvent(HoverEvent.hoverEvent(action2, value2));
            }
            else {
                in.skipValue();
            }
        }
        in.endObject();
        return style.build();
    }
    
    private boolean readBoolean(final JsonReader in) throws IOException {
        final JsonToken peek = in.peek();
        if (peek == JsonToken.BOOLEAN) {
            return in.nextBoolean();
        }
        if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
            return Boolean.parseBoolean(in.nextString());
        }
        throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
    }
    
    private Object legacyHoverEventContents(final HoverEvent.Action<?> action, final Component rawValue) {
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return rawValue;
        }
        if (this.legacyHover != null) {
            try {
                if (action == HoverEvent.Action.SHOW_ENTITY) {
                    return this.legacyHover.deserializeShowEntity(rawValue, this.decoder());
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
    
    private Codec.Decoder<Component, String, JsonParseException> decoder() {
        return (Codec.Decoder<Component, String, JsonParseException>)(string -> this.gson.fromJson(string, SerializerFactory.COMPONENT_TYPE));
    }
    
    private Codec.Encoder<Component, String, JsonParseException> encoder() {
        return (Codec.Encoder<Component, String, JsonParseException>)(component -> this.gson.toJson(component, SerializerFactory.COMPONENT_TYPE));
    }
    
    @Override
    public void write(final JsonWriter out, final Style value) throws IOException {
        out.beginObject();
        for (int i = 0, length = StyleSerializer.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = StyleSerializer.DECORATIONS[i];
            final TextDecoration.State state = value.decoration(decoration);
            if (state != TextDecoration.State.NOT_SET) {
                final String name = TextDecoration.NAMES.key(decoration);
                assert name != null;
                out.name(name);
                out.value(state == TextDecoration.State.TRUE);
            }
        }
        final TextColor color = value.color();
        if (color != null) {
            out.name("color");
            this.gson.toJson(color, SerializerFactory.COLOR_TYPE, out);
        }
        final String insertion = value.insertion();
        if (insertion != null) {
            out.name("insertion");
            out.value(insertion);
        }
        final ClickEvent clickEvent = value.clickEvent();
        if (clickEvent != null) {
            out.name("clickEvent");
            out.beginObject();
            out.name("action");
            this.gson.toJson(clickEvent.action(), SerializerFactory.CLICK_ACTION_TYPE, out);
            out.name("value");
            out.value(clickEvent.value());
            out.endObject();
        }
        final HoverEvent<?> hoverEvent = value.hoverEvent();
        if (hoverEvent != null) {
            out.name("hoverEvent");
            out.beginObject();
            out.name("action");
            final HoverEvent.Action<?> action = hoverEvent.action();
            this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);
            out.name("contents");
            if (action == HoverEvent.Action.SHOW_ITEM) {
                this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE, out);
            }
            else if (action == HoverEvent.Action.SHOW_ENTITY) {
                this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE, out);
            }
            else {
                if (action != HoverEvent.Action.SHOW_TEXT) {
                    throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
                }
                this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
            }
            if (this.emitLegacyHover) {
                out.name("value");
                this.serializeLegacyHoverEvent(hoverEvent, out);
            }
            out.endObject();
        }
        final Key font = value.font();
        if (font != null) {
            out.name("font");
            this.gson.toJson(font, SerializerFactory.KEY_TYPE, out);
        }
        out.endObject();
    }
    
    private void serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonWriter out) throws IOException {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
            this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
        }
        else if (this.legacyHover != null) {
            Component serialized = null;
            try {
                if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                    serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder());
                }
                else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                    serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
                }
            }
            catch (IOException ex) {
                throw new JsonSyntaxException(ex);
            }
            if (serialized != null) {
                this.gson.toJson(serialized, SerializerFactory.COMPONENT_TYPE, out);
            }
            else {
                out.nullValue();
            }
        }
        else {
            out.nullValue();
        }
    }
    
    static {
        DECORATIONS = new TextDecoration[] { TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED };
        final Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
        for (final TextDecoration decoration : StyleSerializer.DECORATIONS) {
            knownDecorations.remove(decoration);
        }
        if (!knownDecorations.isEmpty()) {
            throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
        }
    }
}
