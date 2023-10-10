// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion;

import com.viaversion.viaversion.libs.opennbt.conversion.builtin.LongArrayTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.IntArrayTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.CompoundTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.ListTagConverter;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.StringTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.ByteArrayTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.DoubleTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.FloatTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.LongTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.IntTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.ShortTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.conversion.builtin.ByteTagConverter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Map;

public class ConverterRegistry
{
    private static final Map<Class<? extends Tag>, TagConverter<? extends Tag, ?>> tagToConverter;
    private static final Map<Class<?>, TagConverter<? extends Tag, ?>> typeToConverter;
    
    public static <T extends Tag, V> void register(final Class<T> tag, final Class<V> type, final TagConverter<T, V> converter) throws ConverterRegisterException {
        if (ConverterRegistry.tagToConverter.containsKey(tag)) {
            throw new ConverterRegisterException("Type conversion to tag " + tag.getName() + " is already registered.");
        }
        if (ConverterRegistry.typeToConverter.containsKey(type)) {
            throw new ConverterRegisterException("Tag conversion to type " + type.getName() + " is already registered.");
        }
        ConverterRegistry.tagToConverter.put(tag, converter);
        ConverterRegistry.typeToConverter.put(type, converter);
    }
    
    public static <T extends Tag, V> void unregister(final Class<T> tag, final Class<V> type) {
        ConverterRegistry.tagToConverter.remove(tag);
        ConverterRegistry.typeToConverter.remove(type);
    }
    
    public static <T extends Tag, V> V convertToValue(final T tag) throws ConversionException {
        if (tag == null || tag.getValue() == null) {
            return null;
        }
        if (!ConverterRegistry.tagToConverter.containsKey(tag.getClass())) {
            throw new ConversionException("Tag type " + tag.getClass().getName() + " has no converter.");
        }
        final TagConverter<T, ?> converter = (TagConverter<T, ?>)ConverterRegistry.tagToConverter.get(tag.getClass());
        return (V)converter.convert(tag);
    }
    
    public static <V, T extends Tag> T convertToTag(final V value) throws ConversionException {
        if (value == null) {
            return null;
        }
        TagConverter<T, V> converter = (TagConverter<T, V>)ConverterRegistry.typeToConverter.get(value.getClass());
        if (converter == null) {
            for (final Class<?> clazz : getAllClasses(value.getClass())) {
                if (ConverterRegistry.typeToConverter.containsKey(clazz)) {
                    try {
                        converter = (TagConverter<T, V>)ConverterRegistry.typeToConverter.get(clazz);
                        break;
                    }
                    catch (ClassCastException ex) {}
                }
            }
        }
        if (converter == null) {
            throw new ConversionException("Value type " + value.getClass().getName() + " has no converter.");
        }
        return converter.convert(value);
    }
    
    private static Set<Class<?>> getAllClasses(final Class<?> clazz) {
        final Set<Class<?>> ret = new LinkedHashSet<Class<?>>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            ret.add(c);
            ret.addAll(getAllSuperInterfaces(c));
        }
        if (ret.contains(Serializable.class)) {
            ret.remove(Serializable.class);
            ret.add(Serializable.class);
        }
        return ret;
    }
    
    private static Set<Class<?>> getAllSuperInterfaces(final Class<?> clazz) {
        final Set<Class<?>> ret = new HashSet<Class<?>>();
        for (final Class<?> c : clazz.getInterfaces()) {
            ret.add(c);
            ret.addAll(getAllSuperInterfaces(c));
        }
        return ret;
    }
    
    static {
        tagToConverter = new HashMap<Class<? extends Tag>, TagConverter<? extends Tag, ?>>();
        typeToConverter = new HashMap<Class<?>, TagConverter<? extends Tag, ?>>();
        register(ByteTag.class, Byte.class, new ByteTagConverter());
        register(ShortTag.class, Short.class, new ShortTagConverter());
        register(IntTag.class, Integer.class, new IntTagConverter());
        register(LongTag.class, Long.class, new LongTagConverter());
        register(FloatTag.class, Float.class, new FloatTagConverter());
        register(DoubleTag.class, Double.class, new DoubleTagConverter());
        register(ByteArrayTag.class, byte[].class, new ByteArrayTagConverter());
        register(StringTag.class, String.class, new StringTagConverter());
        register(ListTag.class, List.class, new ListTagConverter());
        register(CompoundTag.class, Map.class, new CompoundTagConverter());
        register(IntArrayTag.class, int[].class, new IntArrayTagConverter());
        register(LongArrayTag.class, long[].class, new LongArrayTagConverter());
    }
}
