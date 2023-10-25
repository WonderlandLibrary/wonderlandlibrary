package net.optifine.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorClass;
import net.optifine.reflect.ReflectorField;

public class ChunkUtils {
    private static ReflectorClass chunkClass = new ReflectorClass(Chunk.class);
    private static ReflectorField fieldHasEntities = ChunkUtils.findFieldHasEntities();
    private static ReflectorField fieldPrecipitationHeightMap = new ReflectorField(chunkClass, int[].class, 0);

    public static boolean hasEntities(Chunk chunk) {
        return Reflector.getFieldValueBoolean((Object)chunk, (ReflectorField)fieldHasEntities, (boolean)true);
    }

    public static int getPrecipitationHeight(Chunk chunk, BlockPos pos) {
        int[] aint = (int[])Reflector.getFieldValue((Object)chunk, (ReflectorField)fieldPrecipitationHeightMap);
        if (aint != null && aint.length == 256) {
            int j;
            int i = pos.getX() & 0xF;
            int k = i | (j = pos.getZ() & 0xF) << 4;
            int l = aint[k];
            if (l >= 0) {
                return l;
            }
            BlockPos blockpos = chunk.getPrecipitationHeight(pos);
            return blockpos.getY();
        }
        return -1;
    }

    private static ReflectorField findFieldHasEntities() {
        try {
            Chunk chunk = new Chunk((World)null, 0, 0);
            ArrayList list = new ArrayList();
            ArrayList list1 = new ArrayList();
            Field[] afield = Chunk.class.getDeclaredFields();
            for (int i = 0; i < afield.length; ++i) {
                Field field = afield[i];
                if (field.getType() != Boolean.TYPE) continue;
                field.setAccessible(true);
                list.add((Object)field);
                list1.add(field.get((Object)chunk));
            }
            chunk.setHasEntities(false);
            ArrayList list2 = new ArrayList();
            for (Object o : list) {
                Field field1 = (Field)o;
                list2.add(field1.get((Object)chunk));
            }
            chunk.setHasEntities(true);
            ArrayList list3 = new ArrayList();
            for (Object o : list) {
                Field field2 = (Field)o;
                list3.add(field2.get((Object)chunk));
            }
            ArrayList list4 = new ArrayList();
            for (int j = 0; j < list.size(); ++j) {
                Field field3 = (Field)list.get(j);
                Boolean obool = (Boolean)list2.get(j);
                Boolean obool1 = (Boolean)list3.get(j);
                if (obool.booleanValue() || !obool1.booleanValue()) continue;
                list4.add((Object)field3);
                Boolean obool2 = (Boolean)list1.get(j);
                field3.set((Object)chunk, (Object)obool2);
            }
            if (list4.size() == 1) {
                Field field4 = (Field)list4.get(0);
                return new ReflectorField(field4);
            }
        }
        catch (Exception exception) {
            Config.warn((String)(exception.getClass().getName() + " " + exception.getMessage()));
        }
        Config.warn((String)"Error finding Chunk.hasEntities");
        return new ReflectorField(new ReflectorClass(Chunk.class), "hasEntities");
    }
}
