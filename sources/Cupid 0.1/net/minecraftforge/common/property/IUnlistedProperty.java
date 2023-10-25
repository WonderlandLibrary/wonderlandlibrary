package net.minecraftforge.common.property;

public interface IUnlistedProperty<V> {
  String getName();
  
  boolean isValid(V paramV);
  
  Class<V> getType();
  
  String valueToString(V paramV);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraftforge\common\property\IUnlistedProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */