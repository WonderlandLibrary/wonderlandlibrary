package net.optifine.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.optifine.reflect.IFieldLocator;
import net.optifine.reflect.ReflectorRaw;

public class FieldLocatorActionKeyF3
implements IFieldLocator {
    public Field getField() {
        Class<Minecraft> oclass = Minecraft.class;
        Field field = this.getFieldRenderChunksMany();
        if (field == null) {
            Config.log((String)("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3 (field renderChunksMany not found)"));
            return null;
        }
        Field field1 = ReflectorRaw.getFieldAfter(Minecraft.class, (Field)field, (Class)Boolean.TYPE, (int)0);
        if (field1 == null) {
            Config.log((String)("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3"));
            return null;
        }
        return field1;
    }

    private Field getFieldRenderChunksMany() {
        Minecraft minecraft = Minecraft.getMinecraft();
        boolean flag = minecraft.renderChunksMany;
        Field[] afield = Minecraft.class.getDeclaredFields();
        minecraft.renderChunksMany = true;
        Object[] afield1 = ReflectorRaw.getFields((Object)minecraft, (Field[])afield, (Class)Boolean.TYPE, (Object)Boolean.TRUE);
        minecraft.renderChunksMany = false;
        Object[] afield2 = ReflectorRaw.getFields((Object)minecraft, (Field[])afield, (Class)Boolean.TYPE, (Object)Boolean.FALSE);
        minecraft.renderChunksMany = flag;
        HashSet set = new HashSet((Collection)Arrays.asList((Object[])afield1));
        HashSet set1 = new HashSet((Collection)Arrays.asList((Object[])afield2));
        HashSet set2 = new HashSet((Collection)set);
        set2.retainAll((Collection)set1);
        Field[] afield3 = (Field[])set2.toArray((Object[])new Field[set2.size()]);
        return afield3.length != 1 ? null : afield3[0];
    }
}
