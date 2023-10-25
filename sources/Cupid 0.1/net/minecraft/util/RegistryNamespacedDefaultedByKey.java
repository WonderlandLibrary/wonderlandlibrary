package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V> {
  private final K defaultValueKey;
  
  private V defaultValue;
  
  public RegistryNamespacedDefaultedByKey(K p_i46017_1_) {
    this.defaultValueKey = p_i46017_1_;
  }
  
  public void register(int id, K p_177775_2_, V p_177775_3_) {
    if (this.defaultValueKey.equals(p_177775_2_))
      this.defaultValue = p_177775_3_; 
    super.register(id, p_177775_2_, p_177775_3_);
  }
  
  public void validateKey() {
    Validate.notNull(this.defaultValueKey);
  }
  
  public V getObject(K name) {
    V v = super.getObject(name);
    return (v == null) ? this.defaultValue : v;
  }
  
  public V getObjectById(int id) {
    V v = super.getObjectById(id);
    return (v == null) ? this.defaultValue : v;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\RegistryNamespacedDefaultedByKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */