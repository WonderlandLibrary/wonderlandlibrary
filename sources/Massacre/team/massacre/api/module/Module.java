package team.massacre.api.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import team.massacre.Massacre;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.api.property.impl.MultiSelectEnumProperty;

public class Module {
   public Minecraft mc = Minecraft.getMinecraft();
   public boolean state;
   public boolean expanded;
   public boolean hasSuffix;
   public String name;
   public String displayName;
   public String suffix;
   public int key;
   public int index;
   public Category category;
   private List<Property<?>> values = new ArrayList();

   public Module(String name, int key, Category category) {
      this.name = name;
      this.key = key;
      this.category = category;
   }

   protected void addValues(Property<?>... value) {
      this.values.addAll(Arrays.asList(value));
   }

   public List<Property<?>> getValues() {
      return this.values;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.hasSuffix ? this.name + "§7 " + this.getSuffix() : this.name;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public boolean getState() {
      return this.state;
   }

   public void setState(boolean state) {
      if (this.state != state) {
         this.state = state;
         if (state) {
            this.onEnable();
         } else {
            this.onDisable();
         }
      }

   }

   public String getSuffix() {
      return this.suffix;
   }

   public void setSuffix(String suffix) {
      this.hasSuffix = true;
      this.suffix = "§8§7" + StringUtils.upperSnakeCaseToPascal(suffix) + "§8";
   }

   public JsonObject save() {
      JsonObject object = new JsonObject();
      object.addProperty("toggled", this.getState());
      object.addProperty("key", this.getKey());
      List<Property<?>> properties = this.getValues();
      if (!properties.isEmpty()) {
         JsonObject propertiesObject = new JsonObject();
         Iterator var4 = properties.iterator();

         while(true) {
            while(var4.hasNext()) {
               Property<?> property = (Property)var4.next();
               if (property instanceof DoubleProperty) {
                  propertiesObject.addProperty(property.getLabel(), (Number)((DoubleProperty)property).getValue());
               } else if (property instanceof EnumProperty) {
                  EnumProperty<?> enumProperty = (EnumProperty)property;
                  propertiesObject.add(property.getLabel(), new JsonPrimitive(((Enum)enumProperty.getValue()).ordinal()));
               } else if (!(property instanceof MultiSelectEnumProperty)) {
                  if (property.getType() == Boolean.class) {
                     propertiesObject.addProperty(property.getLabel(), (Boolean)property.getValue());
                  } else if (property.getType() == Integer.class) {
                     propertiesObject.addProperty(property.getLabel(), Integer.toHexString((Integer)property.getValue()));
                  } else if (property.getType() == String.class) {
                     propertiesObject.addProperty(property.getLabel(), (String)property.getValue());
                  }
               } else {
                  MultiSelectEnumProperty<?> multiSelect = (MultiSelectEnumProperty)property;
                  JsonArray array = new JsonArray();
                  Enum[] var8 = multiSelect.getValues();
                  int var9 = var8.length;

                  for(int var10 = 0; var10 < var9; ++var10) {
                     Enum<?> e = var8[var10];
                     array.add(new JsonPrimitive(e.ordinal()));
                  }

                  propertiesObject.add(property.getLabel(), array);
               }
            }

            object.add("properties", propertiesObject);
            break;
         }
      }

      return object;
   }

   public void load(JsonObject object) {
      if (object.has("toggled")) {
         this.setState(object.get("toggled").getAsBoolean());
      }

      if (object.has("key")) {
         this.setKey(object.get("key").getAsInt());
      }

      if (object.has("properties") && !this.getValues().isEmpty()) {
         JsonObject propertiesObject = object.getAsJsonObject("properties");
         Iterator var3 = this.getValues().iterator();

         while(var3.hasNext()) {
            Property<?> property = (Property)var3.next();
            if (propertiesObject.has(property.getLabel())) {
               JsonElement propertyElement = propertiesObject.get(property.getLabel());
               if (property instanceof DoubleProperty) {
                  DoubleProperty doubleProperty = (DoubleProperty)property;
                  doubleProperty.setValue(propertyElement.getAsDouble());
               } else if (property instanceof EnumProperty) {
                  EnumProperty<?> enumProperty = (EnumProperty)property;
                  if (propertyElement.isJsonPrimitive()) {
                     JsonPrimitive primitive = propertyElement.getAsJsonPrimitive();
                     if (primitive.isNumber()) {
                        enumProperty.setValue(primitive.getAsInt());
                     }
                  }
               } else if (!(property instanceof MultiSelectEnumProperty)) {
                  if (property.getValue() instanceof Boolean) {
                     property.setValue(propertyElement.getAsBoolean());
                  } else if (property.getValue() instanceof Integer) {
                     property.setValue((int)Long.parseLong(propertyElement.getAsString(), 16));
                  } else if (property.getValue() instanceof String) {
                     property.setValue(propertyElement.getAsString());
                  }
               }
            }
         }
      }

   }

   public Property<?> getPropertyByName(String propertyName) {
      Iterator var2 = this.getValues().iterator();

      Property property;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         property = (Property)var2.next();
      } while(!propertyName.equalsIgnoreCase(property.getLabelNoSpaces()));

      return property;
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public Category getType() {
      return this.category;
   }

   public void toggle() {
      this.state = !this.state;
      if (this.state) {
         this.onEnable();
      } else {
         this.onDisable();
      }

   }

   public void onUpdate() {
   }

   public void onEnable() {
      this.hasSuffix = false;
      Massacre.INSTANCE.getEventManager().register(this);
   }

   public void onDisable() {
      Massacre.INSTANCE.getEventManager().unregister(this);
      this.mc.timer.timerSpeed = 1.0F;
   }
}
