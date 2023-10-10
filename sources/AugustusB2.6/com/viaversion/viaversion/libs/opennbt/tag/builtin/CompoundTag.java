// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.EOFException;
import com.viaversion.viaversion.libs.opennbt.tag.TagCreateException;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.tag.TagRegistry;
import java.io.DataInput;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompoundTag extends Tag implements Iterable<Map.Entry<String, Tag>>
{
    public static final int ID = 10;
    private Map<String, Tag> value;
    
    public CompoundTag() {
        this(new LinkedHashMap<String, Tag>());
    }
    
    public CompoundTag(final Map<String, Tag> value) {
        this.value = new LinkedHashMap<String, Tag>(value);
    }
    
    public CompoundTag(final LinkedHashMap<String, Tag> value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    @Override
    public Map<String, Tag> getValue() {
        return this.value;
    }
    
    public void setValue(final Map<String, Tag> value) {
        Preconditions.checkNotNull(value);
        this.value = new LinkedHashMap<String, Tag>(value);
    }
    
    public void setValue(final LinkedHashMap<String, Tag> value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    public boolean isEmpty() {
        return this.value.isEmpty();
    }
    
    public boolean contains(final String tagName) {
        return this.value.containsKey(tagName);
    }
    
    @Nullable
    public <T extends Tag> T get(final String tagName) {
        return (T)this.value.get(tagName);
    }
    
    @Nullable
    public <T extends Tag> T put(final String tagName, final T tag) {
        return (T)this.value.put(tagName, tag);
    }
    
    @Nullable
    public <T extends Tag> T remove(final String tagName) {
        return (T)this.value.remove(tagName);
    }
    
    public Set<String> keySet() {
        return this.value.keySet();
    }
    
    public Collection<Tag> values() {
        return this.value.values();
    }
    
    public Set<Map.Entry<String, Tag>> entrySet() {
        return this.value.entrySet();
    }
    
    public int size() {
        return this.value.size();
    }
    
    public void clear() {
        this.value.clear();
    }
    
    @Override
    public Iterator<Map.Entry<String, Tag>> iterator() {
        return this.value.entrySet().iterator();
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        try {
            while (true) {
                final int id = in.readByte();
                if (id == 0) {
                    break;
                }
                final String name = in.readUTF();
                final Tag tag = TagRegistry.createInstance(id);
                tag.read(in);
                this.value.put(name, tag);
            }
        }
        catch (TagCreateException e) {
            throw new IOException("Failed to create tag.", e);
        }
        catch (EOFException e2) {
            throw new IOException("Closing tag was not found!");
        }
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        for (final Map.Entry<String, Tag> entry : this.value.entrySet()) {
            final Tag tag = entry.getValue();
            out.writeByte(tag.getTagId());
            out.writeUTF(entry.getKey());
            tag.write(out);
        }
        out.writeByte(0);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CompoundTag tags = (CompoundTag)o;
        return this.value.equals(tags.value);
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @Override
    public final CompoundTag clone() {
        final LinkedHashMap<String, Tag> newMap = new LinkedHashMap<String, Tag>();
        for (final Map.Entry<String, Tag> entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().clone());
        }
        return new CompoundTag(newMap);
    }
    
    @Override
    public int getTagId() {
        return 10;
    }
}
