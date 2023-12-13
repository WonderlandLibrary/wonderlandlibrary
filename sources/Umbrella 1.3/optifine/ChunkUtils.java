/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.world.chunk.Chunk;
import optifine.Config;

public class ChunkUtils {
    private static Field fieldHasEntities = null;
    private static boolean fieldHasEntitiesMissing = false;

    public static boolean hasEntities(Chunk chunk) {
        if (fieldHasEntities == null) {
            if (fieldHasEntitiesMissing) {
                return true;
            }
            fieldHasEntities = ChunkUtils.findFieldHasEntities(chunk);
            if (fieldHasEntities == null) {
                fieldHasEntitiesMissing = true;
                return true;
            }
        }
        try {
            return fieldHasEntities.getBoolean(chunk);
        }
        catch (Exception var2) {
            Config.warn("Error calling Chunk.hasEntities");
            Config.warn(String.valueOf(var2.getClass().getName()) + " " + var2.getMessage());
            fieldHasEntitiesMissing = true;
            return true;
        }
    }

    private static Field findFieldHasEntities(Chunk chunk) {
        try {
            ArrayList<Field> e = new ArrayList<Field>();
            ArrayList<Object> listBoolValuesPre = new ArrayList<Object>();
            Field[] fields = Chunk.class.getDeclaredFields();
            for (int listBoolValuesFalse = 0; listBoolValuesFalse < fields.length; ++listBoolValuesFalse) {
                Field listBoolValuesTrue = fields[listBoolValuesFalse];
                if (listBoolValuesTrue.getType() != Boolean.TYPE) continue;
                listBoolValuesTrue.setAccessible(true);
                e.add(listBoolValuesTrue);
                listBoolValuesPre.add(listBoolValuesTrue.get(chunk));
            }
            chunk.setHasEntities(false);
            ArrayList<Object> var13 = new ArrayList<Object>();
            for (Field listMatchingFields : e) {
                var13.add(listMatchingFields.get(chunk));
            }
            chunk.setHasEntities(true);
            ArrayList<Object> var15 = new ArrayList<Object>();
            for (Field field : e) {
                var15.add(field.get(chunk));
            }
            ArrayList<Field> var17 = new ArrayList<Field>();
            for (int var18 = 0; var18 < e.size(); ++var18) {
                Field field1 = (Field)e.get(var18);
                Boolean valFalse = (Boolean)var13.get(var18);
                Boolean valTrue = (Boolean)var15.get(var18);
                if (valFalse.booleanValue() || !valTrue.booleanValue()) continue;
                var17.add(field1);
                Boolean valPre = (Boolean)listBoolValuesPre.get(var18);
                field1.set(chunk, valPre);
            }
            if (var17.size() == 1) {
                Field field;
                field = (Field)var17.get(0);
                return field;
            }
        }
        catch (Exception var12) {
            Config.warn(String.valueOf(var12.getClass().getName()) + " " + var12.getMessage());
        }
        Config.warn("Error finding Chunk.hasEntities");
        return null;
    }
}

