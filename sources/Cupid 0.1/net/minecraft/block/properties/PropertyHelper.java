package net.minecraft.block.properties;

import com.google.common.base.Objects;

public abstract class PropertyHelper<T extends Comparable<T>> implements IProperty<T> {
  private final Class<T> valueClass;
  
  private final String name;
  
  protected PropertyHelper(String name, Class<T> valueClass) {
    this.valueClass = valueClass;
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Class<T> getValueClass() {
    return this.valueClass;
  }
  
  public String toString() {
    return Objects.toStringHelper(this).add("name", this.name).add("clazz", this.valueClass).add("values", getAllowedValues()).toString();
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
      PropertyHelper propertyhelper = (PropertyHelper)p_equals_1_;
      return (this.valueClass.equals(propertyhelper.valueClass) && this.name.equals(propertyhelper.name));
    } 
    return false;
  }
  
  public int hashCode() {
    return 31 * this.valueClass.hashCode() + this.name.hashCode();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\properties\PropertyHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */