/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.world.chunk.Chunk;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorClass;
import optifine.ReflectorField;

public class ChunkUtils {
    private static ReflectorField fieldHasEntities = null;

    public static boolean hasEntities(Chunk p_hasEntities_0_) {
        if (fieldHasEntities == null) {
            fieldHasEntities = ChunkUtils.findFieldHasEntities(p_hasEntities_0_);
        }
        if (!fieldHasEntities.exists()) {
            return true;
        }
        Boolean obool = (Boolean)Reflector.getFieldValue(p_hasEntities_0_, fieldHasEntities);
        return obool == null ? true : obool;
    }

    /*
     * WARNING - void declaration
     */
    private static ReflectorField findFieldHasEntities(Chunk p_findFieldHasEntities_0_) {
        try {
            void var7_11;
            ArrayList<Object> list = new ArrayList<Object>();
            ArrayList<Object> list1 = new ArrayList<Object>();
            Field[] afield = Chunk.class.getDeclaredFields();
            for (int i = 0; i < afield.length; ++i) {
                Field field = afield[i];
                if (field.getType() != Boolean.TYPE) continue;
                field.setAccessible(true);
                list.add(field);
                list1.add(field.get(p_findFieldHasEntities_0_));
            }
            p_findFieldHasEntities_0_.setHasEntities(false);
            ArrayList<Object> list2 = new ArrayList<Object>();
            for (Object field1 : list) {
                list2.add(((Field)field1).get(p_findFieldHasEntities_0_));
            }
            p_findFieldHasEntities_0_.setHasEntities(true);
            ArrayList<Object> list3 = new ArrayList<Object>();
            for (Object e : list) {
                list3.add(((Field)e).get(p_findFieldHasEntities_0_));
            }
            ArrayList<Field> list4 = new ArrayList<Field>();
            boolean bl = false;
            while (++var7_11 < list.size()) {
                Field field3 = (Field)list.get((int)var7_11);
                Boolean obool = (Boolean)list2.get((int)var7_11);
                Boolean obool1 = (Boolean)list3.get((int)var7_11);
                if (obool.booleanValue() || !obool1.booleanValue()) continue;
                list4.add(field3);
                Boolean obool2 = (Boolean)list1.get((int)var7_11);
                field3.set(p_findFieldHasEntities_0_, obool2);
            }
            if (list4.size() == 1) {
                Field field = (Field)list4.get(0);
                return new ReflectorField(field);
            }
        }
        catch (Exception exception) {
            Config.warn(exception.getClass().getName() + " " + exception.getMessage());
        }
        Config.warn("Error finding Chunk.hasEntities");
        return new ReflectorField(new ReflectorClass(Chunk.class), "hasEntities");
    }
}

