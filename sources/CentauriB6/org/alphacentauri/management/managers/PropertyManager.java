package org.alphacentauri.management.managers;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.alphacentauri.AC;
import org.alphacentauri.management.exceptions.SkidException;
import org.alphacentauri.management.io.ConfigFile;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class PropertyManager {
   private ArrayList properties = new ArrayList();
   private ConfigFile config;

   public void add(Property property) {
      this.properties.add(property);
   }

   private void loadData() {
      for(Property property : this.properties) {
         String key = property.getModule().getName() + "_" + property.getName();
         if(this.config.contains(key)) {
            try {
               if(property.value instanceof String) {
                  property.value = this.config.get(key);
               } else if(property.value instanceof Integer) {
                  property.value = Integer.valueOf(this.config.getInt(key));
               } else if(property.value instanceof Boolean) {
                  property.value = Boolean.valueOf(this.config.getBoolean(key));
               } else if(property.value instanceof Float) {
                  property.value = Float.valueOf(this.config.getFloat(key));
               } else if(property.value instanceof Double) {
                  property.value = Double.valueOf(this.config.getDouble(key));
               } else if(property.value instanceof Character) {
                  property.value = Character.valueOf(this.config.get(key).toCharArray()[0]);
               } else if(property.value instanceof Long) {
                  property.value = Long.valueOf(this.config.getLong(key));
               } else if(property.value instanceof Short) {
                  property.value = Short.valueOf(this.config.getShort(key));
               } else if(property.value instanceof UUID) {
                  property.value = this.config.getUUID(key);
               } else if(property.value instanceof Enum) {
                  property.value = Enum.valueOf(((Enum)property.value).getDeclaringClass(), this.config.get(key));
               } else if(property.value instanceof Iterable) {
                  try {
                     throw new SkidException("Add Iterable loading in PropertyManager");
                  } catch (SkidException var5) {
                     var5.printStackTrace();
                  }
               }
            } catch (Exception var6) {
               property.value = property.getDefaultValue();
            }
         } else {
            property.value = property.getDefaultValue();
         }
      }

   }

   public String serialize(Object value) {
      if(value instanceof String) {
         return (String)value;
      } else if(!(value instanceof Integer) && !(value instanceof Float) && !(value instanceof Double) && !(value instanceof Character) && !(value instanceof Long) && !(value instanceof Short) && !(value instanceof Boolean)) {
         if(!(value instanceof Iterable)) {
            return value instanceof UUID?value.toString():(value instanceof Enum?((Enum)value).name():"");
         } else {
            StringBuilder sb = new StringBuilder();

            for(Object o : (Iterable)value) {
               sb.append(this.serialize(o)).append(';');
            }

            sb.deleteCharAt(sb.length() - 2);
            return sb.toString();
         }
      } else {
         return String.valueOf(value);
      }
   }

   public void loadConfig(String file) {
      AC.getLogger().info("Loading Config " + file);
      if(this.config != null) {
         for(Property property : this.properties) {
            this.config.set(property.getModule().getName() + "_" + property.getName(), this.serialize(property.value));
         }

         this.config.save();
      }

      this.config = new ConfigFile(file);
      this.loadData();
   }

   public Property[] ofModule(Module module) {
      return (Property[])((ArrayList)this.properties.stream().filter((property) -> {
         return property.getModule().equals(module);
      }).collect(Collectors.toCollection(ArrayList::<init>))).toArray(new Property[0]);
   }
}
