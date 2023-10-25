package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

final class Serialization {
  static int readCount(ObjectInputStream stream) throws IOException {
    return stream.readInt();
  }
  
  static <K, V> void writeMap(Map<K, V> map, ObjectOutputStream stream) throws IOException {
    stream.writeInt(map.size());
    for (Map.Entry<K, V> entry : map.entrySet()) {
      stream.writeObject(entry.getKey());
      stream.writeObject(entry.getValue());
    } 
  }
  
  static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream) throws IOException, ClassNotFoundException {
    int size = stream.readInt();
    populateMap(map, stream, size);
  }
  
  static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream, int size) throws IOException, ClassNotFoundException {
    for (int i = 0; i < size; i++) {
      K key = (K)stream.readObject();
      V value = (V)stream.readObject();
      map.put(key, value);
    } 
  }
  
  static <E> void writeMultiset(Multiset<E> multiset, ObjectOutputStream stream) throws IOException {
    int entryCount = multiset.entrySet().size();
    stream.writeInt(entryCount);
    for (Multiset.Entry<E> entry : multiset.entrySet()) {
      stream.writeObject(entry.getElement());
      stream.writeInt(entry.getCount());
    } 
  }
  
  static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream) throws IOException, ClassNotFoundException {
    int distinctElements = stream.readInt();
    populateMultiset(multiset, stream, distinctElements);
  }
  
  static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream, int distinctElements) throws IOException, ClassNotFoundException {
    for (int i = 0; i < distinctElements; i++) {
      E element = (E)stream.readObject();
      int count = stream.readInt();
      multiset.add(element, count);
    } 
  }
  
  static <K, V> void writeMultimap(Multimap<K, V> multimap, ObjectOutputStream stream) throws IOException {
    stream.writeInt(multimap.asMap().size());
    for (Map.Entry<K, Collection<V>> entry : (Iterable<Map.Entry<K, Collection<V>>>)multimap.asMap().entrySet()) {
      stream.writeObject(entry.getKey());
      stream.writeInt(((Collection)entry.getValue()).size());
      for (V value : entry.getValue())
        stream.writeObject(value); 
    } 
  }
  
  static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream) throws IOException, ClassNotFoundException {
    int distinctKeys = stream.readInt();
    populateMultimap(multimap, stream, distinctKeys);
  }
  
  static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream, int distinctKeys) throws IOException, ClassNotFoundException {
    for (int i = 0; i < distinctKeys; i++) {
      K key = (K)stream.readObject();
      Collection<V> values = multimap.get(key);
      int valueCount = stream.readInt();
      for (int j = 0; j < valueCount; j++) {
        V value = (V)stream.readObject();
        values.add(value);
      } 
    } 
  }
  
  static <T> FieldSetter<T> getFieldSetter(Class<T> clazz, String fieldName) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      return new FieldSetter<T>(field);
    } catch (NoSuchFieldException e) {
      throw new AssertionError(e);
    } 
  }
  
  static final class FieldSetter<T> {
    private final Field field;
    
    private FieldSetter(Field field) {
      this.field = field;
      field.setAccessible(true);
    }
    
    void set(T instance, Object value) {
      try {
        this.field.set(instance, value);
      } catch (IllegalAccessException impossible) {
        throw new AssertionError(impossible);
      } 
    }
    
    void set(T instance, int value) {
      try {
        this.field.set(instance, Integer.valueOf(value));
      } catch (IllegalAccessException impossible) {
        throw new AssertionError(impossible);
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Serialization.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */