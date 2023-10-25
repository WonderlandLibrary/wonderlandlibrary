package me.rhys.base.module.setting;

import java.lang.reflect.Field;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.Minecraft;

public class Setting {
  protected final Object object;
  
  protected final Field field;
  
  public Object getObject() {
    return this.object;
  }
  
  public Field getField() {
    return this.field;
  }
  
  public Setting(Object object, Field field) {
    this.object = object;
    this.field = field;
    if (!this.field.isAnnotationPresent((Class)Name.class))
      Minecraft.logger.error(field.getName() + " does not have a name annotation"); 
  }
  
  public String getName() {
    return ((Name)this.field.<Name>getAnnotation(Name.class)).value();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\setting\Setting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */