// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import net.minecraft.src.Config;
import net.minecraft.entity.EntityList;
import java.util.HashMap;
import net.minecraft.entity.Entity;
import java.util.Map;

public class EntityUtils
{
    private static final Map<Class, Integer> mapIdByClass;
    private static final Map<String, Integer> mapIdByName;
    private static final Map<String, Class> mapClassByName;
    
    public static int getEntityIdByClass(final Entity entity) {
        return (entity == null) ? -1 : getEntityIdByClass(entity.getClass());
    }
    
    public static int getEntityIdByClass(final Class cls) {
        final Integer integer = EntityUtils.mapIdByClass.get(cls);
        return (integer == null) ? -1 : integer;
    }
    
    public static int getEntityIdByName(final String name) {
        final Integer integer = EntityUtils.mapIdByName.get(name);
        return (integer == null) ? -1 : integer;
    }
    
    public static Class getEntityClassByName(final String name) {
        final Class oclass = EntityUtils.mapClassByName.get(name);
        return oclass;
    }
    
    static {
        mapIdByClass = new HashMap<Class, Integer>();
        mapIdByName = new HashMap<String, Integer>();
        mapClassByName = new HashMap<String, Class>();
        for (int i = 0; i < 1000; ++i) {
            final Class oclass = EntityList.getClassFromID(i);
            if (oclass != null) {
                final String s = EntityList.getStringFromID(i);
                if (s != null) {
                    if (EntityUtils.mapIdByClass.containsKey(oclass)) {
                        Config.warn("Duplicate entity class: " + oclass + ", id1: " + EntityUtils.mapIdByClass.get(oclass) + ", id2: " + i);
                    }
                    if (EntityUtils.mapIdByName.containsKey(s)) {
                        Config.warn("Duplicate entity name: " + s + ", id1: " + EntityUtils.mapIdByName.get(s) + ", id2: " + i);
                    }
                    if (EntityUtils.mapClassByName.containsKey(s)) {
                        Config.warn("Duplicate entity name: " + s + ", class1: " + EntityUtils.mapClassByName.get(s) + ", class2: " + oclass);
                    }
                    EntityUtils.mapIdByClass.put(oclass, i);
                    EntityUtils.mapIdByName.put(s, i);
                    EntityUtils.mapClassByName.put(s, oclass);
                }
            }
        }
    }
}
