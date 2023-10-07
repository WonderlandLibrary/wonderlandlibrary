// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.opennbt.tag;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import java.util.function.Supplier;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;

public final class TagRegistry
{
    private static final int HIGHEST_ID = 12;
    private static final Class<? extends Tag>[] idToTag;
    private static final Supplier<? extends Tag>[] instanceSuppliers;
    private static final Object2IntMap<Class<? extends Tag>> tagToId;
    
    public static void register(final int id, final Class<? extends Tag> tag, final Supplier<? extends Tag> supplier) throws TagRegisterException {
        if (id < 0 || id > 12) {
            throw new TagRegisterException("Tag ID must be between 0 and 12");
        }
        if (TagRegistry.idToTag[id] != null) {
            throw new TagRegisterException("Tag ID \"" + id + "\" is already in use.");
        }
        if (TagRegistry.tagToId.containsKey(tag)) {
            throw new TagRegisterException("Tag \"" + tag.getSimpleName() + "\" is already registered.");
        }
        TagRegistry.instanceSuppliers[id] = supplier;
        TagRegistry.idToTag[id] = tag;
        TagRegistry.tagToId.put(tag, id);
    }
    
    public static void unregister(final int id) {
        TagRegistry.tagToId.removeInt(getClassFor(id));
        TagRegistry.idToTag[id] = null;
        TagRegistry.instanceSuppliers[id] = null;
    }
    
    @Nullable
    public static Class<? extends Tag> getClassFor(final int id) {
        return (id >= 0 && id < TagRegistry.idToTag.length) ? TagRegistry.idToTag[id] : null;
    }
    
    public static int getIdFor(final Class<? extends Tag> clazz) {
        return TagRegistry.tagToId.getInt(clazz);
    }
    
    public static Tag createInstance(final int id) throws TagCreateException {
        final Supplier<? extends Tag> supplier = (id > 0 && id < TagRegistry.instanceSuppliers.length) ? TagRegistry.instanceSuppliers[id] : null;
        if (supplier == null) {
            throw new TagCreateException("Could not find tag with ID \"" + id + "\".");
        }
        return (Tag)supplier.get();
    }
    
    static {
        idToTag = new Class[13];
        instanceSuppliers = new Supplier[13];
        (tagToId = new Object2IntOpenHashMap<Class<? extends Tag>>()).defaultReturnValue(-1);
        register(1, ByteTag.class, ByteTag::new);
        register(2, ShortTag.class, ShortTag::new);
        register(3, IntTag.class, IntTag::new);
        register(4, LongTag.class, LongTag::new);
        register(5, FloatTag.class, FloatTag::new);
        register(6, DoubleTag.class, DoubleTag::new);
        register(7, ByteArrayTag.class, ByteArrayTag::new);
        register(8, StringTag.class, StringTag::new);
        register(9, ListTag.class, ListTag::new);
        register(10, CompoundTag.class, CompoundTag::new);
        register(11, IntArrayTag.class, IntArrayTag::new);
        register(12, LongArrayTag.class, LongArrayTag::new);
    }
}
