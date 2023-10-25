package me.rhys.base.module.setting.impl.number;

import java.lang.reflect.Field;
import me.rhys.base.module.setting.Setting;
import me.rhys.base.module.setting.manifest.Clamp;
import net.minecraft.client.Minecraft;

public abstract class NumberSetting<T> extends Setting {
  public NumberSetting(Object object, Field field) {
    super(object, field);
    if (!field.isAnnotationPresent((Class)Clamp.class))
      Minecraft.logger.error(field.getName() + " does not have a clamp annotation but its a number setting"); 
  }
  
  public T get() {
    try {
      return (T)this.field.get(this.object);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void set(T value) {
    try {
      this.field.set(this.object, value);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } 
  }
  
  public double getMin() {
    return ((Clamp)this.field.<Clamp>getAnnotation(Clamp.class)).min();
  }
  
  public double getMax() {
    return ((Clamp)this.field.<Clamp>getAnnotation(Clamp.class)).max();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\setting\impl\number\NumberSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */