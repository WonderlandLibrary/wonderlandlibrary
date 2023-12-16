// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.world.chunk.Chunk;
import java.lang.reflect.Field;

public class ChunkUtils
{
    private static Field fieldHasEntities;
    private static boolean fieldHasEntitiesMissing;
    
    static {
        ChunkUtils.fieldHasEntities = null;
        ChunkUtils.fieldHasEntitiesMissing = false;
    }
    
    public static boolean hasEntities(final Chunk chunk) {
        if (ChunkUtils.fieldHasEntities == null) {
            if (ChunkUtils.fieldHasEntitiesMissing) {
                return true;
            }
            ChunkUtils.fieldHasEntities = findFieldHasEntities(chunk);
            if (ChunkUtils.fieldHasEntities == null) {
                return ChunkUtils.fieldHasEntitiesMissing = true;
            }
        }
        try {
            return ChunkUtils.fieldHasEntities.getBoolean(chunk);
        }
        catch (Exception var2) {
            Config.warn("Error calling Chunk.hasEntities");
            Config.warn(String.valueOf(var2.getClass().getName()) + " " + var2.getMessage());
            return ChunkUtils.fieldHasEntitiesMissing = true;
        }
    }
    
    private static Field findFieldHasEntities(final Chunk chunk) {
        try {
            final ArrayList e = new ArrayList();
            final ArrayList listBoolValuesPre = new ArrayList();
            final Field[] fields = Chunk.class.getDeclaredFields();
            for (int listBoolValuesFalse = 0; listBoolValuesFalse < fields.length; ++listBoolValuesFalse) {
                final Field listBoolValuesTrue = fields[listBoolValuesFalse];
                if (listBoolValuesTrue.getType() == Boolean.TYPE) {
                    listBoolValuesTrue.setAccessible(true);
                    e.add(listBoolValuesTrue);
                    listBoolValuesPre.add(listBoolValuesTrue.get(chunk));
                }
            }
            chunk.setHasEntities(false);
            final ArrayList var13 = new ArrayList();
            for (final Field listMatchingFields : e) {
                var13.add(listMatchingFields.get(chunk));
            }
            chunk.setHasEntities(true);
            final ArrayList var15 = new ArrayList();
            for (final Field field : e) {
                var15.add(field.get(chunk));
            }
            final ArrayList var17 = new ArrayList();
            for (int var18 = 0; var18 < e.size(); ++var18) {
                final Field field2 = e.get(var18);
                final Boolean valFalse = var13.get(var18);
                final Boolean valTrue = var15.get(var18);
                if (!valFalse && valTrue) {
                    var17.add(field2);
                    final Boolean valPre = listBoolValuesPre.get(var18);
                    field2.set(chunk, valPre);
                }
            }
            if (var17.size() == 1) {
                final Field field = var17.get(0);
                return field;
            }
        }
        catch (Exception var19) {
            Config.warn(String.valueOf(var19.getClass().getName()) + " " + var19.getMessage());
        }
        Config.warn("Error finding Chunk.hasEntities");
        return null;
    }
}
