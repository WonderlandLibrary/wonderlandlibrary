// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl;

import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.nbt.BinaryTag;
import java.util.UUID;
import com.viaversion.viaversion.libs.kyori.adventure.nbt.api.BinaryTagHolder;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.io.IOException;
import com.viaversion.viaversion.libs.kyori.adventure.nbt.CompoundBinaryTag;
import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import com.viaversion.viaversion.libs.kyori.adventure.nbt.TagStringIO;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;

final class NBTLegacyHoverEventSerializerImpl implements LegacyHoverEventSerializer
{
    static final NBTLegacyHoverEventSerializerImpl INSTANCE;
    private static final TagStringIO SNBT_IO;
    private static final Codec<CompoundBinaryTag, String, IOException, IOException> SNBT_CODEC;
    static final String ITEM_TYPE = "id";
    static final String ITEM_COUNT = "Count";
    static final String ITEM_TAG = "tag";
    static final String ENTITY_NAME = "name";
    static final String ENTITY_TYPE = "type";
    static final String ENTITY_ID = "id";
    
    private NBTLegacyHoverEventSerializerImpl() {
    }
    
    @Override
    public HoverEvent.ShowItem deserializeShowItem(@NotNull final Component input) throws IOException {
        assertTextComponent(input);
        final CompoundBinaryTag contents = NBTLegacyHoverEventSerializerImpl.SNBT_CODEC.decode(((TextComponent)input).content());
        final CompoundBinaryTag tag = contents.getCompound("tag");
        return HoverEvent.ShowItem.of(Key.key(contents.getString("id")), contents.getByte("Count", (byte)1), (tag == CompoundBinaryTag.empty()) ? null : BinaryTagHolder.encode(tag, NBTLegacyHoverEventSerializerImpl.SNBT_CODEC));
    }
    
    @Override
    public HoverEvent.ShowEntity deserializeShowEntity(@NotNull final Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
        assertTextComponent(input);
        final CompoundBinaryTag contents = NBTLegacyHoverEventSerializerImpl.SNBT_CODEC.decode(((TextComponent)input).content());
        return HoverEvent.ShowEntity.of(Key.key(contents.getString("type")), UUID.fromString(contents.getString("id")), componentCodec.decode(contents.getString("name")));
    }
    
    private static void assertTextComponent(final Component component) {
        if (!(component instanceof TextComponent) || !component.children().isEmpty()) {
            throw new IllegalArgumentException("Legacy events must be single Component instances");
        }
    }
    
    @NotNull
    @Override
    public Component serializeShowItem(final HoverEvent.ShowItem input) throws IOException {
        final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder().putString("id", input.item().asString()).putByte("Count", (byte)input.count());
        final BinaryTagHolder nbt = input.nbt();
        if (nbt != null) {
            builder.put("tag", nbt.get(NBTLegacyHoverEventSerializerImpl.SNBT_CODEC));
        }
        return Component.text(NBTLegacyHoverEventSerializerImpl.SNBT_CODEC.encode(builder.build()));
    }
    
    @NotNull
    @Override
    public Component serializeShowEntity(final HoverEvent.ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
        final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder().putString("id", input.id().toString()).putString("type", input.type().asString());
        final Component name = input.name();
        if (name != null) {
            builder.putString("name", componentCodec.encode(name));
        }
        return Component.text(NBTLegacyHoverEventSerializerImpl.SNBT_CODEC.encode(builder.build()));
    }
    
    static {
        INSTANCE = new NBTLegacyHoverEventSerializerImpl();
        SNBT_IO = TagStringIO.get();
        final TagStringIO snbt_IO = NBTLegacyHoverEventSerializerImpl.SNBT_IO;
        Objects.requireNonNull(snbt_IO);
        final Codec.Decoder<CompoundBinaryTag, String, X> decoder = snbt_IO::asCompound;
        final TagStringIO snbt_IO2 = NBTLegacyHoverEventSerializerImpl.SNBT_IO;
        Objects.requireNonNull(snbt_IO2);
        SNBT_CODEC = Codec.of((Codec.Decoder<CompoundBinaryTag, String, IOException>)decoder, (Codec.Encoder<CompoundBinaryTag, String, IOException>)snbt_IO2::asString);
    }
}
