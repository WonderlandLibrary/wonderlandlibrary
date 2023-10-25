package org.dreamcore.client.ui.components.altmanager.althening.api;

import java.lang.reflect.Field;

public class AltHelper {
    private String className;
    private Class<?> clazz;

    public AltHelper(final String v1) {
        try {
            this.clazz = Class.forName(v1);
        } catch (ClassNotFoundException v2) {
            v2.printStackTrace();
        }
    }

    public void setStaticField(final String a2, final Object v1) throws NoSuchFieldException, IllegalAccessException {
        final Field v2 = this.clazz.getDeclaredField(a2);
        v2.setAccessible(true);
        final Field v3 = Field.class.getDeclaredField("modifiers");
        v3.setAccessible(true);
        v3.setInt(v2, v2.getModifiers() & 0xFFFFFFEF);
        v2.set(null, v1);
    }
}