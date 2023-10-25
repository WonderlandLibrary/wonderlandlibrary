package net.minecraft.util;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V> {
  private final V defaultObject;
  
  public RegistryDefaulted(V defaultObjectIn) {
    this.defaultObject = defaultObjectIn;
  }
  
  public V getObject(K name) {
    V v = super.getObject(name);
    return (v == null) ? this.defaultObject : v;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\RegistryDefaulted.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */