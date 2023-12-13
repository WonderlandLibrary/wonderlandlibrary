// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata;

import java.util.Optional;
import de.gerrygames.viarewind.protocol.protocol1_8to1_7_6_10.metadata.MetaIndex1_8to1_7_6_10;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.util.Pair;
import java.util.HashMap;

public class MetaIndex1_7_6_10to1_8
{
    private static final HashMap<Pair<Entity1_10Types.EntityType, Integer>, MetaIndex1_8to1_7_6_10> metadataRewrites;
    
    private static Optional<MetaIndex1_8to1_7_6_10> getIndex(final Entity1_10Types.EntityType type, final int index) {
        final Pair pair = new Pair((X)type, (Y)index);
        if (MetaIndex1_7_6_10to1_8.metadataRewrites.containsKey(pair)) {
            return Optional.of(MetaIndex1_7_6_10to1_8.metadataRewrites.get(pair));
        }
        return Optional.empty();
    }
    
    public static MetaIndex1_8to1_7_6_10 searchIndex(final Entity1_10Types.EntityType type, final int index) {
        Entity1_10Types.EntityType currentType = type;
        do {
            final Optional<MetaIndex1_8to1_7_6_10> optMeta = getIndex(currentType, index);
            if (optMeta.isPresent()) {
                return optMeta.get();
            }
            currentType = currentType.getParent();
        } while (currentType != null);
        return null;
    }
    
    static {
        metadataRewrites = new HashMap<Pair<Entity1_10Types.EntityType, Integer>, MetaIndex1_8to1_7_6_10>();
        for (final MetaIndex1_8to1_7_6_10 index : MetaIndex1_8to1_7_6_10.values()) {
            MetaIndex1_7_6_10to1_8.metadataRewrites.put(new Pair<Entity1_10Types.EntityType, Integer>(index.getClazz(), index.getNewIndex()), index);
        }
    }
}
