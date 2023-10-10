// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import java.util.Iterator;
import com.viaversion.viaversion.libs.kyori.adventure.text.KeybindComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.SelectorComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ScoreComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import java.util.Map;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponent;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.StorageNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.EntityNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import java.util.List;
import java.util.Collections;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.kyori.adventure.text.BuildableComponent;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.Gson;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class ComponentSerializerImpl extends TypeAdapter<Component>
{
    static final String TEXT = "text";
    static final String TRANSLATE = "translate";
    static final String TRANSLATE_WITH = "with";
    static final String SCORE = "score";
    static final String SCORE_NAME = "name";
    static final String SCORE_OBJECTIVE = "objective";
    static final String SCORE_VALUE = "value";
    static final String SELECTOR = "selector";
    static final String KEYBIND = "keybind";
    static final String EXTRA = "extra";
    static final String NBT = "nbt";
    static final String NBT_INTERPRET = "interpret";
    static final String NBT_BLOCK = "block";
    static final String NBT_ENTITY = "entity";
    static final String NBT_STORAGE = "storage";
    static final String SEPARATOR = "separator";
    static final Type COMPONENT_LIST_TYPE;
    private final Gson gson;
    
    static TypeAdapter<Component> create(final Gson gson) {
        return new ComponentSerializerImpl(gson).nullSafe();
    }
    
    private ComponentSerializerImpl(final Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public BuildableComponent<?, ?> read(final JsonReader in) throws IOException {
        final JsonToken token = in.peek();
        if (token == JsonToken.STRING || token == JsonToken.NUMBER || token == JsonToken.BOOLEAN) {
            return Component.text(readString(in));
        }
        if (token == JsonToken.BEGIN_ARRAY) {
            ComponentBuilder<?, ?> parent = null;
            in.beginArray();
            while (in.hasNext()) {
                final BuildableComponent<?, ?> child = this.read(in);
                if (parent == null) {
                    parent = (ComponentBuilder<?, ?>)child.toBuilder();
                }
                else {
                    parent.append(child);
                }
            }
            if (parent == null) {
                throw notSureHowToDeserialize(in.getPath());
            }
            in.endArray();
            return (BuildableComponent<?, ?>)parent.build();
        }
        else {
            if (token != JsonToken.BEGIN_OBJECT) {
                throw notSureHowToDeserialize(in.getPath());
            }
            final JsonObject style = new JsonObject();
            List<Component> extra = Collections.emptyList();
            String text = null;
            String translate = null;
            List<Component> translateWith = null;
            String scoreName = null;
            String scoreObjective = null;
            String scoreValue = null;
            String selector = null;
            String keybind = null;
            String nbt = null;
            boolean nbtInterpret = false;
            BlockNBTComponent.Pos nbtBlock = null;
            String nbtEntity = null;
            Key nbtStorage = null;
            Component separator = null;
            in.beginObject();
            while (in.hasNext()) {
                final String fieldName = in.nextName();
                if (fieldName.equals("text")) {
                    text = readString(in);
                }
                else if (fieldName.equals("translate")) {
                    translate = in.nextString();
                }
                else if (fieldName.equals("with")) {
                    translateWith = this.gson.fromJson(in, ComponentSerializerImpl.COMPONENT_LIST_TYPE);
                }
                else if (fieldName.equals("score")) {
                    in.beginObject();
                    while (in.hasNext()) {
                        final String scoreFieldName = in.nextName();
                        if (scoreFieldName.equals("name")) {
                            scoreName = in.nextString();
                        }
                        else if (scoreFieldName.equals("objective")) {
                            scoreObjective = in.nextString();
                        }
                        else if (scoreFieldName.equals("value")) {
                            scoreValue = in.nextString();
                        }
                        else {
                            in.skipValue();
                        }
                    }
                    if (scoreName == null || scoreObjective == null) {
                        throw new JsonParseException("A score component requires a name and objective");
                    }
                    in.endObject();
                }
                else if (fieldName.equals("selector")) {
                    selector = in.nextString();
                }
                else if (fieldName.equals("keybind")) {
                    keybind = in.nextString();
                }
                else if (fieldName.equals("nbt")) {
                    nbt = in.nextString();
                }
                else if (fieldName.equals("interpret")) {
                    nbtInterpret = in.nextBoolean();
                }
                else if (fieldName.equals("block")) {
                    nbtBlock = this.gson.fromJson(in, SerializerFactory.BLOCK_NBT_POS_TYPE);
                }
                else if (fieldName.equals("entity")) {
                    nbtEntity = in.nextString();
                }
                else if (fieldName.equals("storage")) {
                    nbtStorage = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
                }
                else if (fieldName.equals("extra")) {
                    extra = this.gson.fromJson(in, ComponentSerializerImpl.COMPONENT_LIST_TYPE);
                }
                else if (fieldName.equals("separator")) {
                    separator = this.read(in);
                }
                else {
                    style.add(fieldName, this.gson.fromJson(in, JsonElement.class));
                }
            }
            ComponentBuilder<?, ?> builder;
            if (text != null) {
                builder = Component.text().content(text);
            }
            else if (translate != null) {
                if (translateWith != null) {
                    builder = Component.translatable().key(translate).args(translateWith);
                }
                else {
                    builder = Component.translatable().key(translate);
                }
            }
            else if (scoreName != null && scoreObjective != null) {
                if (scoreValue == null) {
                    builder = Component.score().name(scoreName).objective(scoreObjective);
                }
                else {
                    builder = Component.score().name(scoreName).objective(scoreObjective).value(scoreValue);
                }
            }
            else if (selector != null) {
                builder = Component.selector().pattern(selector).separator(separator);
            }
            else if (keybind != null) {
                builder = Component.keybind().keybind(keybind);
            }
            else {
                if (nbt == null) {
                    throw notSureHowToDeserialize(in.getPath());
                }
                if (nbtBlock != null) {
                    builder = nbt(Component.blockNBT(), nbt, nbtInterpret, separator).pos(nbtBlock);
                }
                else if (nbtEntity != null) {
                    builder = nbt(Component.entityNBT(), nbt, nbtInterpret, separator).selector(nbtEntity);
                }
                else {
                    if (nbtStorage == null) {
                        throw notSureHowToDeserialize(in.getPath());
                    }
                    builder = nbt(Component.storageNBT(), nbt, nbtInterpret, separator).storage(nbtStorage);
                }
            }
            ((ComponentBuilder<BuildableComponent, ComponentBuilder>)builder.style(this.gson.fromJson(style, SerializerFactory.STYLE_TYPE))).append(extra);
            in.endObject();
            return (BuildableComponent<?, ?>)builder.build();
        }
    }
    
    private static String readString(final JsonReader in) throws IOException {
        final JsonToken peek = in.peek();
        if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
            return in.nextString();
        }
        if (peek == JsonToken.BOOLEAN) {
            return String.valueOf(in.nextBoolean());
        }
        throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a string");
    }
    
    private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final String nbt, final boolean interpret, @Nullable final Component separator) {
        return ((NBTComponentBuilder<C, B>)((NBTComponentBuilder<C, NBTComponentBuilder<C, B>>)builder).nbtPath(nbt).interpret(interpret)).separator(separator);
    }
    
    @Override
    public void write(final JsonWriter out, final Component value) throws IOException {
        out.beginObject();
        if (value.hasStyling()) {
            final JsonElement style = this.gson.toJsonTree(value.style(), SerializerFactory.STYLE_TYPE);
            if (style.isJsonObject()) {
                for (final Map.Entry<String, JsonElement> entry : style.getAsJsonObject().entrySet()) {
                    out.name(entry.getKey());
                    this.gson.toJson(entry.getValue(), out);
                }
            }
        }
        if (!value.children().isEmpty()) {
            out.name("extra");
            this.gson.toJson(value.children(), ComponentSerializerImpl.COMPONENT_LIST_TYPE, out);
        }
        if (value instanceof TextComponent) {
            out.name("text");
            out.value(((TextComponent)value).content());
        }
        else if (value instanceof TranslatableComponent) {
            final TranslatableComponent translatable = (TranslatableComponent)value;
            out.name("translate");
            out.value(translatable.key());
            if (!translatable.args().isEmpty()) {
                out.name("with");
                this.gson.toJson(translatable.args(), ComponentSerializerImpl.COMPONENT_LIST_TYPE, out);
            }
        }
        else if (value instanceof ScoreComponent) {
            final ScoreComponent score = (ScoreComponent)value;
            out.name("score");
            out.beginObject();
            out.name("name");
            out.value(score.name());
            out.name("objective");
            out.value(score.objective());
            if (score.value() != null) {
                out.name("value");
                out.value(score.value());
            }
            out.endObject();
        }
        else if (value instanceof SelectorComponent) {
            final SelectorComponent selector = (SelectorComponent)value;
            out.name("selector");
            out.value(selector.pattern());
            this.serializeSeparator(out, selector.separator());
        }
        else if (value instanceof KeybindComponent) {
            out.name("keybind");
            out.value(((KeybindComponent)value).keybind());
        }
        else {
            if (!(value instanceof NBTComponent)) {
                throw notSureHowToSerialize(value);
            }
            final NBTComponent<?, ?> nbt = (NBTComponent<?, ?>)value;
            out.name("nbt");
            out.value(nbt.nbtPath());
            out.name("interpret");
            out.value(nbt.interpret());
            this.serializeSeparator(out, nbt.separator());
            if (value instanceof BlockNBTComponent) {
                out.name("block");
                this.gson.toJson(((BlockNBTComponent)value).pos(), SerializerFactory.BLOCK_NBT_POS_TYPE, out);
            }
            else if (value instanceof EntityNBTComponent) {
                out.name("entity");
                out.value(((EntityNBTComponent)value).selector());
            }
            else {
                if (!(value instanceof StorageNBTComponent)) {
                    throw notSureHowToSerialize(value);
                }
                out.name("storage");
                this.gson.toJson(((StorageNBTComponent)value).storage(), SerializerFactory.KEY_TYPE, out);
            }
        }
        out.endObject();
    }
    
    private void serializeSeparator(final JsonWriter out, @Nullable final Component separator) throws IOException {
        if (separator != null) {
            out.name("separator");
            this.write(out, separator);
        }
    }
    
    static JsonParseException notSureHowToDeserialize(final Object element) {
        return new JsonParseException("Don't know how to turn " + element + " into a Component");
    }
    
    private static IllegalArgumentException notSureHowToSerialize(final Component component) {
        return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
    }
    
    static {
        COMPONENT_LIST_TYPE = new TypeToken<List<Component>>() {}.getType();
    }
}
