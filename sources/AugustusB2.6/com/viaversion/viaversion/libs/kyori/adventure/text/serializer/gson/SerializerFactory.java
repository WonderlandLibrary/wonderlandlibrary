// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.TypeAdapter;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;

final class SerializerFactory implements TypeAdapterFactory
{
    static final Class<Key> KEY_TYPE;
    static final Class<Component> COMPONENT_TYPE;
    static final Class<Style> STYLE_TYPE;
    static final Class<ClickEvent.Action> CLICK_ACTION_TYPE;
    static final Class<HoverEvent.Action> HOVER_ACTION_TYPE;
    static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE;
    static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE;
    static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE;
    static final Class<TextColor> COLOR_TYPE;
    static final Class<TextDecoration> TEXT_DECORATION_TYPE;
    static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE;
    private final boolean downsampleColors;
    private final LegacyHoverEventSerializer legacyHoverSerializer;
    private final boolean emitLegacyHover;
    
    SerializerFactory(final boolean downsampleColors, @Nullable final LegacyHoverEventSerializer legacyHoverSerializer, final boolean emitLegacyHover) {
        this.downsampleColors = downsampleColors;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.emitLegacyHover = emitLegacyHover;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> rawType = type.getRawType();
        if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ComponentSerializerImpl.create(gson);
        }
        if (SerializerFactory.KEY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)KeySerializer.INSTANCE;
        }
        if (SerializerFactory.STYLE_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)StyleSerializer.create(this.legacyHoverSerializer, this.emitLegacyHover, gson);
        }
        if (SerializerFactory.CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ClickEventActionSerializer.INSTANCE;
        }
        if (SerializerFactory.HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)HoverEventActionSerializer.INSTANCE;
        }
        if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ShowItemSerializer.create(gson);
        }
        if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ShowEntitySerializer.create(gson);
        }
        if (SerializerFactory.COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)TextColorWrapper.Serializer.INSTANCE;
        }
        if (SerializerFactory.COLOR_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)(this.downsampleColors ? TextColorSerializer.DOWNSAMPLE_COLOR : TextColorSerializer.INSTANCE);
        }
        if (SerializerFactory.TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)TextDecorationSerializer.INSTANCE;
        }
        if (SerializerFactory.BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)BlockNBTComponentPosSerializer.INSTANCE;
        }
        return null;
    }
    
    static {
        KEY_TYPE = Key.class;
        COMPONENT_TYPE = Component.class;
        STYLE_TYPE = Style.class;
        CLICK_ACTION_TYPE = ClickEvent.Action.class;
        HOVER_ACTION_TYPE = HoverEvent.Action.class;
        SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
        SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
        COLOR_WRAPPER_TYPE = TextColorWrapper.class;
        COLOR_TYPE = TextColor.class;
        TEXT_DECORATION_TYPE = TextDecoration.class;
        BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
    }
}
