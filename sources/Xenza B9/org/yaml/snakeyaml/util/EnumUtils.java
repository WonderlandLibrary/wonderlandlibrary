// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.util;

public class EnumUtils
{
    public static <T extends Enum<T>> T findEnumInsensitiveCase(final Class<T> enumType, final String name) {
        for (final T constant : enumType.getEnumConstants()) {
            if (constant.name().compareToIgnoreCase(name) == 0) {
                return constant;
            }
        }
        throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + "." + name);
    }
}
