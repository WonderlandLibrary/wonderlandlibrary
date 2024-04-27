/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.util;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import java.util.ArrayList;
import java.util.Comparator;

public class EntityTypeUtil {
    public static EntityType[] toOrderedArray(EntityType[] values2) {
        ArrayList<EntityType> types = new ArrayList<EntityType>();
        for (EntityType type : values2) {
            if (type.getId() == -1) continue;
            types.add(type);
        }
        types.sort(Comparator.comparingInt(EntityType::getId));
        return types.toArray(new EntityType[0]);
    }

    public static EntityType getTypeFromId(EntityType[] values2, int typeId, EntityType fallback) {
        EntityType type;
        if (typeId < 0 || typeId >= values2.length || (type = values2[typeId]) == null) {
            Via.getPlatform().getLogger().severe("Could not find " + fallback.getClass().getSimpleName() + " type id " + typeId);
            return fallback;
        }
        return type;
    }
}

