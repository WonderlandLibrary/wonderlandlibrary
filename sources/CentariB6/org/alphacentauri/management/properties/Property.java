package org.alphacentauri.management.properties;

import org.alphacentauri.AC;
import org.alphacentauri.management.modules.Module;

public class Property {
   private final Module module;
   private final String name;
   public Object value;
   private final Object defaultValue;
   private boolean visible;

   public Property(Module module, String name, Object value) {
      this(module, name, value, true);
   }

   public Property(Module module, String name, Object value, boolean visible) {
      this.module = module;
      this.name = name;
      this.value = value;
      this.defaultValue = value;
      this.visible = visible;
      AC.getPropertyManager().add(this);
   }

   public String getName() {
      return this.name;
   }

   public Object getDefaultValue() {
      return this.defaultValue;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public Module getModule() {
      return this.module;
   }
}
