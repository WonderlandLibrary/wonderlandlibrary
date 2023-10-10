// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.data.MappingData;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import java.util.Collection;
import java.util.EnumMap;
import com.viaversion.viaversion.api.minecraft.TagData;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.Protocol;

public class TagRewriter
{
    private static final int[] EMPTY_ARRAY;
    private final Protocol protocol;
    private final Map<RegistryType, List<TagData>> newTags;
    
    public TagRewriter(final Protocol protocol) {
        this.newTags = new EnumMap<RegistryType, List<TagData>>(RegistryType.class);
        this.protocol = protocol;
    }
    
    public void loadFromMappingData() {
        for (final RegistryType type : RegistryType.getValues()) {
            final List<TagData> tags = this.protocol.getMappingData().getTags(type);
            if (tags != null) {
                this.getOrComputeNewTags(type).addAll(tags);
            }
        }
    }
    
    public void addEmptyTag(final RegistryType tagType, final String tagId) {
        this.getOrComputeNewTags(tagType).add(new TagData(tagId, TagRewriter.EMPTY_ARRAY));
    }
    
    public void addEmptyTags(final RegistryType tagType, final String... tagIds) {
        final List<TagData> tagList = this.getOrComputeNewTags(tagType);
        for (final String id : tagIds) {
            tagList.add(new TagData(id, TagRewriter.EMPTY_ARRAY));
        }
    }
    
    public void addEntityTag(final String tagId, final EntityType... entities) {
        final int[] ids = new int[entities.length];
        for (int i = 0; i < entities.length; ++i) {
            ids[i] = entities[i].getId();
        }
        this.addTagRaw(RegistryType.ENTITY, tagId, ids);
    }
    
    public void addTag(final RegistryType tagType, final String tagId, final int... unmappedIds) {
        final List<TagData> newTags = this.getOrComputeNewTags(tagType);
        final IdRewriteFunction rewriteFunction = this.getRewriter(tagType);
        for (int i = 0; i < unmappedIds.length; ++i) {
            final int oldId = unmappedIds[i];
            unmappedIds[i] = rewriteFunction.rewrite(oldId);
        }
        newTags.add(new TagData(tagId, unmappedIds));
    }
    
    public void addTagRaw(final RegistryType tagType, final String tagId, final int... ids) {
        this.getOrComputeNewTags(tagType).add(new TagData(tagId, ids));
    }
    
    public void register(final ClientboundPacketType packetType, final RegistryType readUntilType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(TagRewriter.this.getHandler(readUntilType));
            }
        });
    }
    
    public void registerGeneric(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(TagRewriter.this.getGenericHandler());
            }
        });
    }
    
    public PacketHandler getHandler(final RegistryType readUntilType) {
        final RegistryType[] array;
        final int length;
        int i = 0;
        RegistryType type;
        return wrapper -> {
            RegistryType.getValues();
            length = array.length;
            while (i < length) {
                type = array[i];
                this.handle(wrapper, this.getRewriter(type), this.getNewTags(type));
                if (type == readUntilType) {
                    break;
                }
                else {
                    ++i;
                }
            }
        };
    }
    
    public PacketHandler getGenericHandler() {
        int length;
        int i;
        String registryKey;
        RegistryType type;
        return wrapper -> {
            for (length = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < length; ++i) {
                registryKey = wrapper.passthrough(Type.STRING);
                if (registryKey.startsWith("minecraft:")) {
                    registryKey = registryKey.substring(10);
                }
                type = RegistryType.getByKey(registryKey);
                if (type != null) {
                    this.handle(wrapper, this.getRewriter(type), this.getNewTags(type));
                }
                else {
                    this.handle(wrapper, null, null);
                }
            }
        };
    }
    
    public void handle(final PacketWrapper wrapper, final IdRewriteFunction rewriteFunction, final List<TagData> newTags) throws Exception {
        final int tagsSize = wrapper.read((Type<Integer>)Type.VAR_INT);
        wrapper.write(Type.VAR_INT, (newTags != null) ? (tagsSize + newTags.size()) : tagsSize);
        for (int i = 0; i < tagsSize; ++i) {
            wrapper.passthrough(Type.STRING);
            final int[] ids = wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
            if (rewriteFunction != null) {
                final IntList idList = new IntArrayList(ids.length);
                for (final int id : ids) {
                    final int mappedId = rewriteFunction.rewrite(id);
                    if (mappedId != -1) {
                        idList.add(mappedId);
                    }
                }
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, idList.toArray(TagRewriter.EMPTY_ARRAY));
            }
            else {
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, ids);
            }
        }
        if (newTags != null) {
            for (final TagData tag : newTags) {
                wrapper.write(Type.STRING, tag.identifier());
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, tag.entries());
            }
        }
    }
    
    public List<TagData> getNewTags(final RegistryType tagType) {
        return this.newTags.get(tagType);
    }
    
    public List<TagData> getOrComputeNewTags(final RegistryType tagType) {
        return this.newTags.computeIfAbsent(tagType, type -> new ArrayList());
    }
    
    public IdRewriteFunction getRewriter(final RegistryType tagType) {
        final MappingData mappingData = this.protocol.getMappingData();
        switch (tagType) {
            case BLOCK: {
                IdRewriteFunction idRewriteFunction;
                if (mappingData != null && mappingData.getBlockMappings() != null) {
                    final MappingData obj = mappingData;
                    Objects.requireNonNull(obj);
                    idRewriteFunction = obj::getNewBlockId;
                }
                else {
                    idRewriteFunction = null;
                }
                return idRewriteFunction;
            }
            case ITEM: {
                IdRewriteFunction idRewriteFunction2;
                if (mappingData != null && mappingData.getItemMappings() != null) {
                    final MappingData obj2 = mappingData;
                    Objects.requireNonNull(obj2);
                    idRewriteFunction2 = obj2::getNewItemId;
                }
                else {
                    idRewriteFunction2 = null;
                }
                return idRewriteFunction2;
            }
            case ENTITY: {
                return (this.protocol.getEntityRewriter() != null) ? (id -> this.protocol.getEntityRewriter().newEntityId(id)) : null;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        EMPTY_ARRAY = new int[0];
    }
}
