// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import com.viaversion.viaversion.api.Via;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;

public final class EntityTypeUtil
{
    private static final EntityType[] EMPTY_ARRAY;
    
    public static EntityType[] toOrderedArray(final EntityType[] values) {
        final List<EntityType> types = new ArrayList<EntityType>();
        for (final EntityType type : values) {
            if (type.getId() != -1) {
                types.add(type);
            }
        }
        types.sort(Comparator.comparingInt(EntityType::getId));
        return types.toArray(EntityTypeUtil.EMPTY_ARRAY);
    }
    
    public static <T extends EntityType> void initialize(final T[] values, final EntityType[] typesToFill, final Protocol<?, ?, ?, ?> protocol, final EntityIdSetter<T> idSetter) {
        for (final T type : values) {
            if (!type.isAbstractType()) {
                final int id = protocol.getMappingData().getEntityMappings().mappedId(type.identifier());
                Preconditions.checkArgument(id != -1, "Entity type %s has no id", type.identifier());
                idSetter.setId(type, id);
                typesToFill[id] = type;
            }
        }
    }
    
    public static EntityType[] createSizedArray(final EntityType[] values) {
        int count = 0;
        for (final EntityType type : values) {
            if (!type.isAbstractType()) {
                ++count;
            }
        }
        return new EntityType[count];
    }
    
    public static EntityType getTypeFromId(final EntityType[] values, final int typeId, final EntityType fallback) {
        final EntityType type;
        if (typeId < 0 || typeId >= values.length || (type = values[typeId]) == null) {
            Via.getPlatform().getLogger().severe("Could not find " + fallback.getClass().getSimpleName() + " type id " + typeId);
            return fallback;
        }
        return type;
    }
    
    static {
        EMPTY_ARRAY = new EntityType[0];
    }
    
    @FunctionalInterface
    public interface EntityIdSetter<T extends EntityType>
    {
        void setId(final T p0, final int p1);
    }
}
