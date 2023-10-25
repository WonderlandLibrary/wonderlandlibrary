/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonDeserializationContext;
import com.viaversion.viaversion.libs.gson.JsonDeserializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonSerializationContext;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.BuildableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.EntityNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.KeybindComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.ScopedComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ScoreComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.SelectorComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.StorageNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

final class ComponentSerializerImpl
implements JsonDeserializer<Component>,
JsonSerializer<Component> {
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

    ComponentSerializerImpl() {
    }

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return this.deserialize0(json, context);
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private BuildableComponent<?, ?> deserialize0(JsonElement element, JsonDeserializationContext context) throws JsonParseException {
        Style style;
        ComponentBuilder<TextComponent, TextComponent.Builder> component;
        if (element.isJsonPrimitive()) {
            return Component.text(element.getAsString());
        }
        if (element.isJsonArray()) {
            void var3_4;
            Object var3_3 = null;
            for (JsonElement childElement : element.getAsJsonArray()) {
                BuildableComponent<?, ?> child = this.deserialize0(childElement, context);
                if (var3_4 == null) {
                    Buildable.Builder builder = child.toBuilder();
                    continue;
                }
                var3_4.append((Component)child);
            }
            if (var3_4 != null) return var3_4.build();
            throw ComponentSerializerImpl.notSureHowToDeserialize(element);
        }
        if (!element.isJsonObject()) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(element);
        }
        JsonObject jsonObject = element.getAsJsonObject();
        if (jsonObject.has(TEXT)) {
            component = Component.text().content(jsonObject.get(TEXT).getAsString());
        } else if (jsonObject.has(TRANSLATE)) {
            String key = jsonObject.get(TRANSLATE).getAsString();
            if (!jsonObject.has(TRANSLATE_WITH)) {
                component = Component.translatable().key(key);
            } else {
                JsonArray with = jsonObject.getAsJsonArray(TRANSLATE_WITH);
                ArrayList args = new ArrayList(with.size());
                int size = with.size();
                for (int i = 0; i < size; ++i) {
                    JsonElement argElement = with.get(i);
                    args.add(this.deserialize0(argElement, context));
                }
                component = Component.translatable().key(key).args(args);
            }
        } else if (jsonObject.has(SCORE)) {
            JsonObject score = jsonObject.getAsJsonObject(SCORE);
            if (!score.has(SCORE_NAME) || !score.has(SCORE_OBJECTIVE)) {
                throw new JsonParseException("A score component requires a name and objective");
            }
            ScoreComponent.Builder builder = Component.score().name(score.get(SCORE_NAME).getAsString()).objective(score.get(SCORE_OBJECTIVE).getAsString());
            component = score.has(SCORE_VALUE) ? builder.value(score.get(SCORE_VALUE).getAsString()) : builder;
        } else if (jsonObject.has(SELECTOR)) {
            @Nullable Component separator = this.deserializeSeparator(jsonObject, context);
            component = Component.selector().pattern(jsonObject.get(SELECTOR).getAsString()).separator(separator);
        } else if (jsonObject.has(KEYBIND)) {
            component = Component.keybind().keybind(jsonObject.get(KEYBIND).getAsString());
        } else {
            if (!jsonObject.has(NBT)) throw ComponentSerializerImpl.notSureHowToDeserialize(element);
            String nbt = jsonObject.get(NBT).getAsString();
            boolean interpret = jsonObject.has(NBT_INTERPRET) && jsonObject.getAsJsonPrimitive(NBT_INTERPRET).getAsBoolean();
            @Nullable Component separator = this.deserializeSeparator(jsonObject, context);
            if (jsonObject.has(NBT_BLOCK)) {
                BlockNBTComponent.Pos pos = (BlockNBTComponent.Pos)context.deserialize(jsonObject.get(NBT_BLOCK), (Type)((Object)BlockNBTComponent.Pos.class));
                component = ComponentSerializerImpl.nbt(Component.blockNBT(), nbt, interpret, separator).pos(pos);
            } else if (jsonObject.has(NBT_ENTITY)) {
                component = ComponentSerializerImpl.nbt(Component.entityNBT(), nbt, interpret, separator).selector(jsonObject.get(NBT_ENTITY).getAsString());
            } else {
                if (!jsonObject.has(NBT_STORAGE)) throw ComponentSerializerImpl.notSureHowToDeserialize(element);
                component = ComponentSerializerImpl.nbt(Component.storageNBT(), nbt, interpret, separator).storage((Key)context.deserialize(jsonObject.get(NBT_STORAGE), (Type)((Object)Key.class)));
            }
        }
        if (jsonObject.has(EXTRA)) {
            JsonArray extra = jsonObject.getAsJsonArray(EXTRA);
            int size = extra.size();
            for (int i = 0; i < size; ++i) {
                JsonElement extraElement = extra.get(i);
                component.append((Component)this.deserialize0(extraElement, context));
            }
        }
        if ((style = (Style)context.deserialize(element, (Type)((Object)Style.class))).isEmpty()) return component.build();
        component.style(style);
        return component.build();
    }

    @Nullable
    private Component deserializeSeparator(JsonObject json, JsonDeserializationContext context) {
        if (json.has(SEPARATOR)) {
            return this.deserialize0(json.get(SEPARATOR), context);
        }
        return null;
    }

    private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(B builder, String nbt, boolean interpret, @Nullable Component separator) {
        return builder.nbtPath(nbt).interpret(interpret).separator(separator);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        ScopedComponent<ScoreComponent> sc;
        List<Component> children;
        JsonElement style;
        JsonObject object = new JsonObject();
        if (src.hasStyling() && (style = context.serialize(src.style())).isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : ((JsonObject)style).entrySet()) {
                object.add(entry.getKey(), entry.getValue());
            }
        }
        if (!(children = src.children()).isEmpty()) {
            JsonArray extra = new JsonArray();
            for (Component component : children) {
                extra.add(context.serialize(component));
            }
            object.add(EXTRA, extra);
        }
        if (src instanceof TextComponent) {
            object.addProperty(TEXT, ((TextComponent)src).content());
            return object;
        } else if (src instanceof TranslatableComponent) {
            TranslatableComponent tc = (TranslatableComponent)src;
            object.addProperty(TRANSLATE, tc.key());
            if (tc.args().isEmpty()) return object;
            JsonArray jsonArray = new JsonArray();
            for (Component arg : tc.args()) {
                jsonArray.add(context.serialize(arg));
            }
            object.add(TRANSLATE_WITH, jsonArray);
            return object;
        } else if (src instanceof ScoreComponent) {
            sc = (ScoreComponent)src;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(SCORE_NAME, sc.name());
            jsonObject.addProperty(SCORE_OBJECTIVE, sc.objective());
            @Nullable String string = sc.value();
            if (string != null) {
                jsonObject.addProperty(SCORE_VALUE, string);
            }
            object.add(SCORE, jsonObject);
            return object;
        } else if (src instanceof SelectorComponent) {
            sc = (SelectorComponent)src;
            object.addProperty(SELECTOR, sc.pattern());
            this.serializeSeparator(context, object, sc.separator());
            return object;
        } else if (src instanceof KeybindComponent) {
            object.addProperty(KEYBIND, ((KeybindComponent)src).keybind());
            return object;
        } else {
            if (!(src instanceof NBTComponent)) throw ComponentSerializerImpl.notSureHowToSerialize(src);
            NBTComponent nc = (NBTComponent)src;
            object.addProperty(NBT, nc.nbtPath());
            object.addProperty(NBT_INTERPRET, nc.interpret());
            if (src instanceof BlockNBTComponent) {
                JsonElement jsonElement = context.serialize(((BlockNBTComponent)nc).pos());
                object.add(NBT_BLOCK, jsonElement);
                this.serializeSeparator(context, object, nc.separator());
                return object;
            } else if (src instanceof EntityNBTComponent) {
                object.addProperty(NBT_ENTITY, ((EntityNBTComponent)nc).selector());
                return object;
            } else {
                if (!(src instanceof StorageNBTComponent)) throw ComponentSerializerImpl.notSureHowToSerialize(src);
                object.add(NBT_STORAGE, context.serialize(((StorageNBTComponent)nc).storage()));
            }
        }
        return object;
    }

    private void serializeSeparator(JsonSerializationContext context, JsonObject json, @Nullable Component separator) {
        if (separator != null) {
            json.add(SEPARATOR, context.serialize(separator));
        }
    }

    static JsonParseException notSureHowToDeserialize(Object element) {
        return new JsonParseException("Don't know how to turn " + element + " into a Component");
    }

    private static IllegalArgumentException notSureHowToSerialize(Component component) {
        return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
    }
}

