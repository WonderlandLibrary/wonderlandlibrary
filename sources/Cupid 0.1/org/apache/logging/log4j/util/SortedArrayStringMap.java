package org.apache.logging.log4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.status.StatusLogger;

public class SortedArrayStringMap implements IndexedStringMap {
  private static final int DEFAULT_INITIAL_CAPACITY = 4;
  
  private static final long serialVersionUID = -5748905872274478116L;
  
  private static final int HASHVAL = 31;
  
  private static final TriConsumer<String, Object, StringMap> PUT_ALL;
  
  static {
    PUT_ALL = ((key, value, contextData) -> contextData.putValue(key, value));
  }
  
  private static final String[] EMPTY = new String[0];
  
  private static final String FROZEN = "Frozen collection cannot be modified";
  
  private transient String[] keys = EMPTY;
  
  private transient Object[] values = (Object[])EMPTY;
  
  private transient int size;
  
  private static final Method setObjectInputFilter;
  
  private static final Method getObjectInputFilter;
  
  private static final Method newObjectInputFilter;
  
  private int threshold;
  
  private boolean immutable;
  
  private transient boolean iterating;
  
  static {
    Method[] methods = ObjectInputStream.class.getMethods();
    Method setMethod = null;
    Method getMethod = null;
    for (Method method : methods) {
      if (method.getName().equals("setObjectInputFilter")) {
        setMethod = method;
      } else if (method.getName().equals("getObjectInputFilter")) {
        getMethod = method;
      } 
    } 
    Method newMethod = null;
    try {
      if (setMethod != null) {
        Class<?> clazz = Class.forName("org.apache.logging.log4j.util.internal.DefaultObjectInputFilter");
        methods = clazz.getMethods();
        for (Method method : methods) {
          if (method.getName().equals("newInstance") && Modifier.isStatic(method.getModifiers())) {
            newMethod = method;
            break;
          } 
        } 
      } 
    } catch (ClassNotFoundException classNotFoundException) {}
    newObjectInputFilter = newMethod;
    setObjectInputFilter = setMethod;
    getObjectInputFilter = getMethod;
  }
  
  public SortedArrayStringMap() {
    this(4);
  }
  
  public SortedArrayStringMap(int initialCapacity) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Initial capacity must be at least zero but was " + initialCapacity); 
    this.threshold = ceilingNextPowerOfTwo((initialCapacity == 0) ? 1 : initialCapacity);
  }
  
  public SortedArrayStringMap(ReadOnlyStringMap other) {
    if (other instanceof SortedArrayStringMap) {
      initFrom0((SortedArrayStringMap)other);
    } else if (other != null) {
      resize(ceilingNextPowerOfTwo(other.size()));
      other.forEach(PUT_ALL, this);
    } 
  }
  
  public SortedArrayStringMap(Map<String, ?> map) {
    resize(ceilingNextPowerOfTwo(map.size()));
    for (Map.Entry<String, ?> entry : map.entrySet())
      putValue(entry.getKey(), entry.getValue()); 
  }
  
  private void assertNotFrozen() {
    if (this.immutable)
      throw new UnsupportedOperationException("Frozen collection cannot be modified"); 
  }
  
  private void assertNoConcurrentModification() {
    if (this.iterating)
      throw new ConcurrentModificationException(); 
  }
  
  public void clear() {
    if (this.keys == EMPTY)
      return; 
    assertNotFrozen();
    assertNoConcurrentModification();
    Arrays.fill((Object[])this.keys, 0, this.size, (Object)null);
    Arrays.fill(this.values, 0, this.size, (Object)null);
    this.size = 0;
  }
  
  public boolean containsKey(String key) {
    return (indexOfKey(key) >= 0);
  }
  
  public Map<String, String> toMap() {
    Map<String, String> result = new HashMap<>(size());
    for (int i = 0; i < size(); i++) {
      Object value = getValueAt(i);
      result.put(getKeyAt(i), (value == null) ? null : String.valueOf(value));
    } 
    return result;
  }
  
  public void freeze() {
    this.immutable = true;
  }
  
  public boolean isFrozen() {
    return this.immutable;
  }
  
  public <V> V getValue(String key) {
    int index = indexOfKey(key);
    if (index < 0)
      return null; 
    return (V)this.values[index];
  }
  
  public boolean isEmpty() {
    return (this.size == 0);
  }
  
  public int indexOfKey(String key) {
    if (this.keys == EMPTY)
      return -1; 
    if (key == null)
      return nullKeyIndex(); 
    int start = (this.size > 0 && this.keys[0] == null) ? 1 : 0;
    return Arrays.binarySearch((Object[])this.keys, start, this.size, key);
  }
  
  private int nullKeyIndex() {
    return (this.size > 0 && this.keys[0] == null) ? 0 : -1;
  }
  
  public void putValue(String key, Object value) {
    assertNotFrozen();
    assertNoConcurrentModification();
    if (this.keys == EMPTY)
      inflateTable(this.threshold); 
    int index = indexOfKey(key);
    if (index >= 0) {
      this.keys[index] = key;
      this.values[index] = value;
    } else {
      insertAt(index ^ 0xFFFFFFFF, key, value);
    } 
  }
  
  private void insertAt(int index, String key, Object value) {
    ensureCapacity();
    System.arraycopy(this.keys, index, this.keys, index + 1, this.size - index);
    System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
    this.keys[index] = key;
    this.values[index] = value;
    this.size++;
  }
  
  public void putAll(ReadOnlyStringMap source) {
    if (source == this || source == null || source.isEmpty())
      return; 
    assertNotFrozen();
    assertNoConcurrentModification();
    if (source instanceof SortedArrayStringMap) {
      if (this.size == 0) {
        initFrom0((SortedArrayStringMap)source);
      } else {
        merge((SortedArrayStringMap)source);
      } 
    } else if (source != null) {
      source.forEach(PUT_ALL, this);
    } 
  }
  
  private void initFrom0(SortedArrayStringMap other) {
    if (this.keys.length < other.size) {
      this.keys = new String[other.threshold];
      this.values = new Object[other.threshold];
    } 
    System.arraycopy(other.keys, 0, this.keys, 0, other.size);
    System.arraycopy(other.values, 0, this.values, 0, other.size);
    this.size = other.size;
    this.threshold = other.threshold;
  }
  
  private void merge(SortedArrayStringMap other) {
    String[] myKeys = this.keys;
    Object[] myVals = this.values;
    int newSize = other.size + this.size;
    this.threshold = ceilingNextPowerOfTwo(newSize);
    if (this.keys.length < this.threshold) {
      this.keys = new String[this.threshold];
      this.values = new Object[this.threshold];
    } 
    boolean overwrite = true;
    if (other.size() > size()) {
      System.arraycopy(myKeys, 0, this.keys, other.size, this.size);
      System.arraycopy(myVals, 0, this.values, other.size, this.size);
      System.arraycopy(other.keys, 0, this.keys, 0, other.size);
      System.arraycopy(other.values, 0, this.values, 0, other.size);
      this.size = other.size;
      overwrite = false;
    } else {
      System.arraycopy(myKeys, 0, this.keys, 0, this.size);
      System.arraycopy(myVals, 0, this.values, 0, this.size);
      System.arraycopy(other.keys, 0, this.keys, this.size, other.size);
      System.arraycopy(other.values, 0, this.values, this.size, other.size);
    } 
    for (int i = this.size; i < newSize; i++) {
      int index = indexOfKey(this.keys[i]);
      if (index < 0) {
        insertAt(index ^ 0xFFFFFFFF, this.keys[i], this.values[i]);
      } else if (overwrite) {
        this.keys[index] = this.keys[i];
        this.values[index] = this.values[i];
      } 
    } 
    Arrays.fill((Object[])this.keys, this.size, newSize, (Object)null);
    Arrays.fill(this.values, this.size, newSize, (Object)null);
  }
  
  private void ensureCapacity() {
    if (this.size >= this.threshold)
      resize(this.threshold * 2); 
  }
  
  private void resize(int newCapacity) {
    String[] oldKeys = this.keys;
    Object[] oldValues = this.values;
    this.keys = new String[newCapacity];
    this.values = new Object[newCapacity];
    System.arraycopy(oldKeys, 0, this.keys, 0, this.size);
    System.arraycopy(oldValues, 0, this.values, 0, this.size);
    this.threshold = newCapacity;
  }
  
  private void inflateTable(int toSize) {
    this.threshold = toSize;
    this.keys = new String[toSize];
    this.values = new Object[toSize];
  }
  
  public void remove(String key) {
    if (this.keys == EMPTY)
      return; 
    int index = indexOfKey(key);
    if (index >= 0) {
      assertNotFrozen();
      assertNoConcurrentModification();
      System.arraycopy(this.keys, index + 1, this.keys, index, this.size - 1 - index);
      System.arraycopy(this.values, index + 1, this.values, index, this.size - 1 - index);
      this.keys[this.size - 1] = null;
      this.values[this.size - 1] = null;
      this.size--;
    } 
  }
  
  public String getKeyAt(int index) {
    if (index < 0 || index >= this.size)
      return null; 
    return this.keys[index];
  }
  
  public <V> V getValueAt(int index) {
    if (index < 0 || index >= this.size)
      return null; 
    return (V)this.values[index];
  }
  
  public int size() {
    return this.size;
  }
  
  public <V> void forEach(BiConsumer<String, ? super V> action) {
    this.iterating = true;
    try {
      for (int i = 0; i < this.size; i++)
        action.accept(this.keys[i], (V)this.values[i]); 
    } finally {
      this.iterating = false;
    } 
  }
  
  public <V, T> void forEach(TriConsumer<String, ? super V, T> action, T state) {
    this.iterating = true;
    try {
      for (int i = 0; i < this.size; i++)
        action.accept(this.keys[i], (V)this.values[i], state); 
    } finally {
      this.iterating = false;
    } 
  }
  
  public boolean equals(Object obj) {
    if (obj == this)
      return true; 
    if (!(obj instanceof SortedArrayStringMap))
      return false; 
    SortedArrayStringMap other = (SortedArrayStringMap)obj;
    if (size() != other.size())
      return false; 
    for (int i = 0; i < size(); i++) {
      if (!Objects.equals(this.keys[i], other.keys[i]))
        return false; 
      if (!Objects.equals(this.values[i], other.values[i]))
        return false; 
    } 
    return true;
  }
  
  public int hashCode() {
    int result = 37;
    result = 31 * result + this.size;
    result = 31 * result + hashCode((Object[])this.keys, this.size);
    result = 31 * result + hashCode(this.values, this.size);
    return result;
  }
  
  private static int hashCode(Object[] values, int length) {
    int result = 1;
    for (int i = 0; i < length; i++)
      result = 31 * result + ((values[i] == null) ? 0 : values[i].hashCode()); 
    return result;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder(256);
    sb.append('{');
    for (int i = 0; i < this.size; i++) {
      if (i > 0)
        sb.append(", "); 
      sb.append(this.keys[i]).append('=');
      sb.append((this.values[i] == this) ? "(this map)" : this.values[i]);
    } 
    sb.append('}');
    return sb.toString();
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
    if (this.keys == EMPTY) {
      s.writeInt(ceilingNextPowerOfTwo(this.threshold));
    } else {
      s.writeInt(this.keys.length);
    } 
    s.writeInt(this.size);
    if (this.size > 0)
      for (int i = 0; i < this.size; i++) {
        s.writeObject(this.keys[i]);
        try {
          s.writeObject(marshall(this.values[i]));
        } catch (Exception e) {
          handleSerializationException(e, i, this.keys[i]);
          s.writeObject(null);
        } 
      }  
  }
  
  private static byte[] marshall(Object obj) throws IOException {
    if (obj == null)
      return null; 
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(bout)) {
      oos.writeObject(obj);
      oos.flush();
      return bout.toByteArray();
    } 
  }
  
  private static Object unmarshall(byte[] data, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
    ObjectInputStream ois;
    ByteArrayInputStream bin = new ByteArrayInputStream(data);
    Collection<String> allowedClasses = null;
    if (inputStream instanceof FilteredObjectInputStream) {
      allowedClasses = ((FilteredObjectInputStream)inputStream).getAllowedClasses();
      ois = new FilteredObjectInputStream(bin, allowedClasses);
    } else {
      try {
        Object obj = getObjectInputFilter.invoke(inputStream, new Object[0]);
        Object filter = newObjectInputFilter.invoke(null, new Object[] { obj });
        ois = new ObjectInputStream(bin);
        setObjectInputFilter.invoke(ois, new Object[] { filter });
      } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
        throw new StreamCorruptedException("Unable to set ObjectInputFilter on stream");
      } 
    } 
    try {
      return ois.readObject();
    } finally {
      ois.close();
    } 
  }
  
  private static int ceilingNextPowerOfTwo(int x) {
    int BITS_PER_INT = 32;
    return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
  }
  
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    if (!(s instanceof FilteredObjectInputStream) && setObjectInputFilter == null)
      throw new IllegalArgumentException("readObject requires a FilteredObjectInputStream or an ObjectInputStream that accepts an ObjectInputFilter"); 
    s.defaultReadObject();
    this.keys = EMPTY;
    this.values = (Object[])EMPTY;
    int capacity = s.readInt();
    if (capacity < 0)
      throw new InvalidObjectException("Illegal capacity: " + capacity); 
    int mappings = s.readInt();
    if (mappings < 0)
      throw new InvalidObjectException("Illegal mappings count: " + mappings); 
    if (mappings > 0) {
      inflateTable(capacity);
    } else {
      this.threshold = capacity;
    } 
    for (int i = 0; i < mappings; i++) {
      this.keys[i] = (String)s.readObject();
      try {
        byte[] marshalledObject = (byte[])s.readObject();
        this.values[i] = (marshalledObject == null) ? null : unmarshall(marshalledObject, s);
      } catch (Exception|LinkageError error) {
        handleSerializationException(error, i, this.keys[i]);
        this.values[i] = null;
      } 
    } 
    this.size = mappings;
  }
  
  private void handleSerializationException(Throwable t, int i, String key) {
    StatusLogger.getLogger().warn("Ignoring {} for key[{}] ('{}')", String.valueOf(t), Integer.valueOf(i), this.keys[i]);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\SortedArrayStringMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */