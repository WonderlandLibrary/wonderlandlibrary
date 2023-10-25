package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;

class CLObjectRegistry<T extends CLObjectChild> {
  private FastLongMap<T> registry;
  
  final boolean isEmpty() {
    return (this.registry == null || this.registry.isEmpty());
  }
  
  final T getObject(long id) {
    return (this.registry == null) ? null : this.registry.get(id);
  }
  
  final boolean hasObject(long id) {
    return (this.registry != null && this.registry.containsKey(id));
  }
  
  final Iterable<FastLongMap.Entry<T>> getAll() {
    return this.registry;
  }
  
  void registerObject(T object) {
    FastLongMap<T> map = getMap();
    Long key = Long.valueOf(object.getPointer());
    if (LWJGLUtil.DEBUG && map.containsKey(key.longValue()))
      throw new IllegalStateException("Duplicate object found: " + object.getClass() + " - " + key); 
    getMap().put(object.getPointer(), object);
  }
  
  void unregisterObject(T object) {
    getMap().remove(object.getPointerUnsafe());
  }
  
  private FastLongMap<T> getMap() {
    if (this.registry == null)
      this.registry = new FastLongMap<T>(); 
    return this.registry;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLObjectRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */