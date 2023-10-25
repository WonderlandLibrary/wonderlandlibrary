package net.optifine.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;

public class EntityUtils
{
    private static final Map<Class, Integer> mapIdByClass = new HashMap<Class, Integer>();
    private static final Map<String, Integer> mapIdByLocation = new HashMap<String, Integer>();
    private static final Map<String, Integer> mapIdByName = new HashMap<String, Integer>();

    public static int getEntityIdByClass(Entity entity)
    {
        return entity == null ? -1 : getEntityIdByClass(entity.getClass());
    }

    public static int getEntityIdByClass(Class cls)
    {
        Integer integer = mapIdByClass.get(cls);
        return integer == null ? -1 : integer.intValue();
    }

    public static int getEntityIdByLocation(String locStr)
    {
        Integer integer = mapIdByLocation.get(locStr);
        return integer == null ? -1 : integer.intValue();
    }

    public static int getEntityIdByName(String name)
    {
        Integer integer = mapIdByName.get(name);
        return integer == null ? -1 : integer.intValue();
    }

    static
    {
        for (int i = 0; i < 1000; ++i)
        {
            Class oclass = EntityList.getClassFromID(i);

            if (oclass != null)
            {
                ResourceLocation resourcelocation = EntityList.getKey(oclass);

                if (resourcelocation != null)
                {
                    String s = resourcelocation.toString();
                    String s1 = EntityList.getTranslationName(resourcelocation);

                    if (s1 != null)
                    {
                        if (mapIdByClass.containsKey(oclass))
                        {
                            Config.warn("Duplicate entity class: " + oclass + ", id1: " + mapIdByClass.get(oclass) + ", id2: " + i);
                        }

                        if (mapIdByLocation.containsKey(s))
                        {
                            Config.warn("Duplicate entity location: " + s + ", id1: " + mapIdByLocation.get(s) + ", id2: " + i);
                        }

                        if (mapIdByName.containsKey(s))
                        {
                            Config.warn("Duplicate entity name: " + s1 + ", id1: " + mapIdByName.get(s1) + ", id2: " + i);
                        }

                        mapIdByClass.put(oclass, Integer.valueOf(i));
                        mapIdByLocation.put(s, Integer.valueOf(i));
                        mapIdByName.put(s1, Integer.valueOf(i));
                    }
                }
            }
        }
    }
}
