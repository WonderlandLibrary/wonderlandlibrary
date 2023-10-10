// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.lang.reflect.Modifier;
import org.apache.logging.log4j.status.StatusLogger;
import java.io.InvalidObjectException;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.io.StreamCorruptedException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.lang.reflect.Method;

public class SortedArrayStringMap implements IndexedStringMap
{
    private static final int DEFAULT_INITIAL_CAPACITY = 4;
    private static final long serialVersionUID = -5748905872274478116L;
    private static final int HASHVAL = 31;
    private static final TriConsumer<String, Object, StringMap> PUT_ALL;
    private static final String[] EMPTY;
    private static final String FROZEN = "Frozen collection cannot be modified";
    private transient String[] keys;
    private transient Object[] values;
    private transient int size;
    private static final Method setObjectInputFilter;
    private static final Method getObjectInputFilter;
    private static final Method newObjectInputFilter;
    private int threshold;
    private boolean immutable;
    private transient boolean iterating;
    
    public SortedArrayStringMap() {
        this(4);
    }
    
    public SortedArrayStringMap(final int initialCapacity) {
        this.keys = SortedArrayStringMap.EMPTY;
        this.values = SortedArrayStringMap.EMPTY;
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be at least zero but was " + initialCapacity);
        }
        this.threshold = ceilingNextPowerOfTwo((initialCapacity == 0) ? 1 : initialCapacity);
    }
    
    public SortedArrayStringMap(final ReadOnlyStringMap other) {
        this.keys = SortedArrayStringMap.EMPTY;
        this.values = SortedArrayStringMap.EMPTY;
        if (other instanceof SortedArrayStringMap) {
            this.initFrom0((SortedArrayStringMap)other);
        }
        else if (other != null) {
            this.resize(ceilingNextPowerOfTwo(other.size()));
            other.forEach(SortedArrayStringMap.PUT_ALL, this);
        }
    }
    
    public SortedArrayStringMap(final Map<String, ?> map) {
        this.keys = SortedArrayStringMap.EMPTY;
        this.values = SortedArrayStringMap.EMPTY;
        this.resize(ceilingNextPowerOfTwo(map.size()));
        for (final Map.Entry<String, ?> entry : map.entrySet()) {
            this.putValue(entry.getKey(), entry.getValue());
        }
    }
    
    private void assertNotFrozen() {
        if (this.immutable) {
            throw new UnsupportedOperationException("Frozen collection cannot be modified");
        }
    }
    
    private void assertNoConcurrentModification() {
        if (this.iterating) {
            throw new ConcurrentModificationException();
        }
    }
    
    @Override
    public void clear() {
        if (this.keys == SortedArrayStringMap.EMPTY) {
            return;
        }
        this.assertNotFrozen();
        this.assertNoConcurrentModification();
        Arrays.fill(this.keys, 0, this.size, null);
        Arrays.fill(this.values, 0, this.size, null);
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final String key) {
        return this.indexOfKey(key) >= 0;
    }
    
    @Override
    public Map<String, String> toMap() {
        final Map<String, String> result = new HashMap<String, String>(this.size());
        for (int i = 0; i < this.size(); ++i) {
            final Object value = this.getValueAt(i);
            result.put(this.getKeyAt(i), (value == null) ? null : String.valueOf(value));
        }
        return result;
    }
    
    @Override
    public void freeze() {
        this.immutable = true;
    }
    
    @Override
    public boolean isFrozen() {
        return this.immutable;
    }
    
    @Override
    public <V> V getValue(final String key) {
        final int index = this.indexOfKey(key);
        if (index < 0) {
            return null;
        }
        return (V)this.values[index];
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public int indexOfKey(final String key) {
        if (this.keys == SortedArrayStringMap.EMPTY) {
            return -1;
        }
        if (key == null) {
            return this.nullKeyIndex();
        }
        final int start = (this.size > 0 && this.keys[0] == null) ? 1 : 0;
        return Arrays.binarySearch(this.keys, start, this.size, key);
    }
    
    private int nullKeyIndex() {
        return (this.size > 0 && this.keys[0] == null) ? 0 : -1;
    }
    
    @Override
    public void putValue(final String key, final Object value) {
        this.assertNotFrozen();
        this.assertNoConcurrentModification();
        if (this.keys == SortedArrayStringMap.EMPTY) {
            this.inflateTable(this.threshold);
        }
        final int index = this.indexOfKey(key);
        if (index >= 0) {
            this.keys[index] = key;
            this.values[index] = value;
        }
        else {
            this.insertAt(~index, key, value);
        }
    }
    
    private void insertAt(final int index, final String key, final Object value) {
        this.ensureCapacity();
        System.arraycopy(this.keys, index, this.keys, index + 1, this.size - index);
        System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
        this.keys[index] = key;
        this.values[index] = value;
        ++this.size;
    }
    
    @Override
    public void putAll(final ReadOnlyStringMap source) {
        if (source == this || source == null || source.isEmpty()) {
            return;
        }
        this.assertNotFrozen();
        this.assertNoConcurrentModification();
        if (source instanceof SortedArrayStringMap) {
            if (this.size == 0) {
                this.initFrom0((SortedArrayStringMap)source);
            }
            else {
                this.merge((SortedArrayStringMap)source);
            }
        }
        else if (source != null) {
            source.forEach(SortedArrayStringMap.PUT_ALL, this);
        }
    }
    
    private void initFrom0(final SortedArrayStringMap other) {
        if (this.keys.length < other.size) {
            this.keys = new String[other.threshold];
            this.values = new Object[other.threshold];
        }
        System.arraycopy(other.keys, 0, this.keys, 0, other.size);
        System.arraycopy(other.values, 0, this.values, 0, other.size);
        this.size = other.size;
        this.threshold = other.threshold;
    }
    
    private void merge(final SortedArrayStringMap other) {
        final String[] myKeys = this.keys;
        final Object[] myVals = this.values;
        final int newSize = other.size + this.size;
        this.threshold = ceilingNextPowerOfTwo(newSize);
        if (this.keys.length < this.threshold) {
            this.keys = new String[this.threshold];
            this.values = new Object[this.threshold];
        }
        boolean overwrite = true;
        if (other.size() > this.size()) {
            System.arraycopy(myKeys, 0, this.keys, other.size, this.size);
            System.arraycopy(myVals, 0, this.values, other.size, this.size);
            System.arraycopy(other.keys, 0, this.keys, 0, other.size);
            System.arraycopy(other.values, 0, this.values, 0, other.size);
            this.size = other.size;
            overwrite = false;
        }
        else {
            System.arraycopy(myKeys, 0, this.keys, 0, this.size);
            System.arraycopy(myVals, 0, this.values, 0, this.size);
            System.arraycopy(other.keys, 0, this.keys, this.size, other.size);
            System.arraycopy(other.values, 0, this.values, this.size, other.size);
        }
        for (int i = this.size; i < newSize; ++i) {
            final int index = this.indexOfKey(this.keys[i]);
            if (index < 0) {
                this.insertAt(~index, this.keys[i], this.values[i]);
            }
            else if (overwrite) {
                this.keys[index] = this.keys[i];
                this.values[index] = this.values[i];
            }
        }
        Arrays.fill(this.keys, this.size, newSize, null);
        Arrays.fill(this.values, this.size, newSize, null);
    }
    
    private void ensureCapacity() {
        if (this.size >= this.threshold) {
            this.resize(this.threshold * 2);
        }
    }
    
    private void resize(final int newCapacity) {
        final String[] oldKeys = this.keys;
        final Object[] oldValues = this.values;
        this.keys = new String[newCapacity];
        this.values = new Object[newCapacity];
        System.arraycopy(oldKeys, 0, this.keys, 0, this.size);
        System.arraycopy(oldValues, 0, this.values, 0, this.size);
        this.threshold = newCapacity;
    }
    
    private void inflateTable(final int toSize) {
        this.threshold = toSize;
        this.keys = new String[toSize];
        this.values = new Object[toSize];
    }
    
    @Override
    public void remove(final String key) {
        if (this.keys == SortedArrayStringMap.EMPTY) {
            return;
        }
        final int index = this.indexOfKey(key);
        if (index >= 0) {
            this.assertNotFrozen();
            this.assertNoConcurrentModification();
            System.arraycopy(this.keys, index + 1, this.keys, index, this.size - 1 - index);
            System.arraycopy(this.values, index + 1, this.values, index, this.size - 1 - index);
            this.keys[this.size - 1] = null;
            this.values[this.size - 1] = null;
            --this.size;
        }
    }
    
    @Override
    public String getKeyAt(final int index) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        return this.keys[index];
    }
    
    @Override
    public <V> V getValueAt(final int index) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        return (V)this.values[index];
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public <V> void forEach(final BiConsumer<String, ? super V> action) {
        this.iterating = true;
        try {
            for (int i = 0; i < this.size; ++i) {
                action.accept(this.keys[i], (Object)this.values[i]);
            }
        }
        finally {
            this.iterating = false;
        }
    }
    
    @Override
    public <V, T> void forEach(final TriConsumer<String, ? super V, T> action, final T state) {
        this.iterating = true;
        try {
            for (int i = 0; i < this.size; ++i) {
                action.accept(this.keys[i], (Object)this.values[i], state);
            }
        }
        finally {
            this.iterating = false;
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SortedArrayStringMap)) {
            return false;
        }
        final SortedArrayStringMap other = (SortedArrayStringMap)obj;
        if (this.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); ++i) {
            if (!Objects.equals(this.keys[i], other.keys[i])) {
                return false;
            }
            if (!Objects.equals(this.values[i], other.values[i])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = 37;
        result = 31 * result + this.size;
        result = 31 * result + hashCode(this.keys, this.size);
        result = 31 * result + hashCode(this.values, this.size);
        return result;
    }
    
    private static int hashCode(final Object[] values, final int length) {
        int result = 1;
        for (int i = 0; i < length; ++i) {
            result = 31 * result + ((values[i] == null) ? 0 : values[i].hashCode());
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(256);
        sb.append('{');
        for (int i = 0; i < this.size; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.keys[i]).append('=');
            sb.append((this.values[i] == this) ? "(this map)" : this.values[i]);
        }
        sb.append('}');
        return sb.toString();
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        if (this.keys == SortedArrayStringMap.EMPTY) {
            s.writeInt(ceilingNextPowerOfTwo(this.threshold));
        }
        else {
            s.writeInt(this.keys.length);
        }
        s.writeInt(this.size);
        if (this.size > 0) {
            for (int i = 0; i < this.size; ++i) {
                s.writeObject(this.keys[i]);
                try {
                    s.writeObject(marshall(this.values[i]));
                }
                catch (Exception e) {
                    this.handleSerializationException(e, i, this.keys[i]);
                    s.writeObject(null);
                }
            }
        }
    }
    
    private static byte[] marshall(final Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try (final ObjectOutputStream oos = new ObjectOutputStream(bout)) {
            oos.writeObject(obj);
            oos.flush();
            return bout.toByteArray();
        }
    }
    
    private static Object unmarshall(final byte[] data, final ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream bin = new ByteArrayInputStream(data);
        Collection<String> allowedClasses = null;
        ObjectInputStream ois;
        if (inputStream instanceof FilteredObjectInputStream) {
            allowedClasses = ((FilteredObjectInputStream)inputStream).getAllowedClasses();
            ois = new FilteredObjectInputStream(bin, allowedClasses);
        }
        else {
            try {
                final Object obj = SortedArrayStringMap.getObjectInputFilter.invoke(inputStream, new Object[0]);
                final Object filter = SortedArrayStringMap.newObjectInputFilter.invoke(null, obj);
                ois = new ObjectInputStream(bin);
                SortedArrayStringMap.setObjectInputFilter.invoke(ois, filter);
            }
            catch (IllegalAccessException | InvocationTargetException ex3) {
                final ReflectiveOperationException ex2;
                final ReflectiveOperationException ex = ex2;
                throw new StreamCorruptedException("Unable to set ObjectInputFilter on stream");
            }
        }
        try {
            return ois.readObject();
        }
        finally {
            ois.close();
        }
    }
    
    private static int ceilingNextPowerOfTwo(final int x) {
        final int BITS_PER_INT = 32;
        return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        if (!(s instanceof FilteredObjectInputStream) && SortedArrayStringMap.setObjectInputFilter == null) {
            throw new IllegalArgumentException("readObject requires a FilteredObjectInputStream or an ObjectInputStream that accepts an ObjectInputFilter");
        }
        s.defaultReadObject();
        this.keys = SortedArrayStringMap.EMPTY;
        this.values = SortedArrayStringMap.EMPTY;
        final int capacity = s.readInt();
        if (capacity < 0) {
            throw new InvalidObjectException("Illegal capacity: " + capacity);
        }
        final int mappings = s.readInt();
        if (mappings < 0) {
            throw new InvalidObjectException("Illegal mappings count: " + mappings);
        }
        if (mappings > 0) {
            this.inflateTable(capacity);
        }
        else {
            this.threshold = capacity;
        }
        for (int i = 0; i < mappings; ++i) {
            this.keys[i] = (String)s.readObject();
            try {
                final byte[] marshalledObject = (byte[])s.readObject();
                this.values[i] = ((marshalledObject == null) ? null : unmarshall(marshalledObject, s));
            }
            catch (Exception | LinkageError ex) {
                final Throwable t;
                final Throwable error = t;
                this.handleSerializationException(error, i, this.keys[i]);
                this.values[i] = null;
            }
        }
        this.size = mappings;
    }
    
    private void handleSerializationException(final Throwable t, final int i, final String key) {
        StatusLogger.getLogger().warn("Ignoring {} for key[{}] ('{}')", String.valueOf(t), i, this.keys[i]);
    }
    
    static {
        PUT_ALL = ((key, value, contextData) -> contextData.putValue(key, value));
        EMPTY = new String[0];
        Method[] methods = ObjectInputStream.class.getMethods();
        Method setMethod = null;
        Method getMethod = null;
        for (final Method method : methods) {
            if (method.getName().equals("setObjectInputFilter")) {
                setMethod = method;
            }
            else if (method.getName().equals("getObjectInputFilter")) {
                getMethod = method;
            }
        }
        Method newMethod = null;
        try {
            if (setMethod != null) {
                final Class<?> clazz = Class.forName("org.apache.logging.log4j.util.internal.DefaultObjectInputFilter");
                final Method[] methods2;
                methods = (methods2 = clazz.getMethods());
                for (final Method method2 : methods2) {
                    if (method2.getName().equals("newInstance") && Modifier.isStatic(method2.getModifiers())) {
                        newMethod = method2;
                        break;
                    }
                }
            }
        }
        catch (ClassNotFoundException ex) {}
        newObjectInputFilter = newMethod;
        setObjectInputFilter = setMethod;
        getObjectInputFilter = getMethod;
    }
}
