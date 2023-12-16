// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.src.Config;
import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.optifine.reflect.Reflector;
import net.minecraft.world.chunk.Chunk;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorClass;

public class ChunkUtils
{
    private static ReflectorClass chunkClass;
    private static ReflectorField fieldHasEntities;
    private static ReflectorField fieldPrecipitationHeightMap;
    
    static {
        ChunkUtils.chunkClass = new ReflectorClass(Chunk.class);
        ChunkUtils.fieldHasEntities = findFieldHasEntities();
        ChunkUtils.fieldPrecipitationHeightMap = new ReflectorField(ChunkUtils.chunkClass, int[].class, 0);
    }
    
    public static boolean hasEntities(final Chunk chunk) {
        return Reflector.getFieldValueBoolean(chunk, ChunkUtils.fieldHasEntities, true);
    }
    
    public static int getPrecipitationHeight(final Chunk chunk, final BlockPos pos) {
        final int[] aint = (int[])Reflector.getFieldValue(chunk, ChunkUtils.fieldPrecipitationHeightMap);
        if (aint == null || aint.length != 256) {
            return -1;
        }
        final int i = pos.getX() & 0xF;
        final int j = pos.getZ() & 0xF;
        final int k = i | j << 4;
        final int l = aint[k];
        if (l >= 0) {
            return l;
        }
        final BlockPos blockpos = chunk.getPrecipitationHeight(pos);
        return blockpos.getY();
    }
    
    private static ReflectorField findFieldHasEntities() {
        try {
            final Chunk chunk = new Chunk(null, 0, 0);
            final List list = new ArrayList();
            final List list2 = new ArrayList();
            final Field[] afield = Chunk.class.getDeclaredFields();
            for (int i = 0; i < afield.length; ++i) {
                final Field field = afield[i];
                if (field.getType() == Boolean.TYPE) {
                    field.setAccessible(true);
                    list.add(field);
                    list2.add(field.get(chunk));
                }
            }
            chunk.setHasEntities(false);
            final List list3 = new ArrayList();
            for (final Object o : list) {
                final Field field2 = (Field)o;
                list3.add(field2.get(chunk));
            }
            chunk.setHasEntities(true);
            final List list4 = new ArrayList();
            for (final Object o2 : list) {
                final Field field3 = (Field)o2;
                list4.add(field3.get(chunk));
            }
            final List list5 = new ArrayList();
            for (int j = 0; j < list.size(); ++j) {
                final Field field4 = list.get(j);
                final Boolean obool = list3.get(j);
                final Boolean obool2 = list4.get(j);
                if (!obool && obool2) {
                    list5.add(field4);
                    final Boolean obool3 = list2.get(j);
                    field4.set(chunk, obool3);
                }
            }
            if (list5.size() == 1) {
                final Field field5 = list5.get(0);
                return new ReflectorField(field5);
            }
        }
        catch (final Exception exception) {
            Config.warn(String.valueOf(exception.getClass().getName()) + " " + exception.getMessage());
        }
        Config.warn("Error finding Chunk.hasEntities");
        return new ReflectorField(new ReflectorClass(Chunk.class), "hasEntities");
    }
}
