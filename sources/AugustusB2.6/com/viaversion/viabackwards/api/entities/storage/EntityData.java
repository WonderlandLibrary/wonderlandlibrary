// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.entities.storage;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viabackwards.ViaBackwards;
import java.util.Locale;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class EntityData
{
    private final BackwardsProtocol<?, ?, ?, ?> protocol;
    private final int id;
    private final int replacementId;
    private final String key;
    private NameVisibility nameVisibility;
    private MetaCreator defaultMeta;
    
    public EntityData(final BackwardsProtocol<?, ?, ?, ?> protocol, final EntityType type, final int replacementId) {
        this(protocol, type.name(), type.getId(), replacementId);
    }
    
    public EntityData(final BackwardsProtocol<?, ?, ?, ?> protocol, final String key, final int id, final int replacementId) {
        this.nameVisibility = NameVisibility.NONE;
        this.protocol = protocol;
        this.id = id;
        this.replacementId = replacementId;
        this.key = key.toLowerCase(Locale.ROOT);
    }
    
    public EntityData jsonName() {
        this.nameVisibility = NameVisibility.JSON;
        return this;
    }
    
    public EntityData plainName() {
        this.nameVisibility = NameVisibility.PLAIN;
        return this;
    }
    
    public EntityData spawnMetadata(final MetaCreator handler) {
        this.defaultMeta = handler;
        return this;
    }
    
    public boolean hasBaseMeta() {
        return this.defaultMeta != null;
    }
    
    public int typeId() {
        return this.id;
    }
    
    public Object mobName() {
        if (this.nameVisibility == NameVisibility.NONE) {
            return null;
        }
        String name = this.protocol.getMappingData().mappedEntityName(this.key);
        if (name == null) {
            ViaBackwards.getPlatform().getLogger().warning("Entity name for " + this.key + " not found in protocol " + this.protocol.getClass().getSimpleName());
            name = this.key;
        }
        return (this.nameVisibility == NameVisibility.JSON) ? ChatRewriter.legacyTextToJson(name) : name;
    }
    
    public int replacementId() {
        return this.replacementId;
    }
    
    public MetaCreator defaultMeta() {
        return this.defaultMeta;
    }
    
    public boolean isObjectType() {
        return false;
    }
    
    public int objectData() {
        return -1;
    }
    
    @Override
    public String toString() {
        return "EntityData{id=" + this.id + ", mobName='" + this.key + '\'' + ", replacementId=" + this.replacementId + ", defaultMeta=" + this.defaultMeta + '}';
    }
    
    private enum NameVisibility
    {
        PLAIN, 
        JSON, 
        NONE;
    }
    
    @FunctionalInterface
    public interface MetaCreator
    {
        void createMeta(final WrappedMetadata p0);
    }
}
