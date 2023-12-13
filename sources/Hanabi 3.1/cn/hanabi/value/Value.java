package cn.hanabi.value;

import cn.hanabi.Hanabi;
import cn.hanabi.modules.Mod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Value {
   public static final List list = new ArrayList();
   public final int RADIUS = 4;
   private final Object defaultValue;
   private final String name;
   public Object value;
   public boolean isValueBoolean = false;
   public boolean isValueInteger = false;
   public boolean isValueFloat = false;
   public boolean isValueDouble = false;
   public boolean isValueMode;
   public boolean isValueLong = false;
   public boolean isValueByte = false;
   public ArrayList mode;
   public double sliderX;
   public boolean set = false;
   public boolean isSettingMode;
   public boolean openMods;
   public double maxSliderSize;
   public float currentRadius = 4.0F;
   public boolean disabled;
   private Object valueMin;
   private Object valueMax;
   private double step;
   private int current;
   private String modeTitle;

   public Value(String classname, String modeTitle, int current) {
      this.defaultValue = this.value;
      this.isValueMode = true;
      this.step = 0.1;
      this.mode = new ArrayList();
      this.current = current;
      this.name = classname + "_Mode";
      this.modeTitle = modeTitle;
      list.add(this);
   }

   public Value(Class classname, String modeTitle, int current) {
      this.defaultValue = this.value;
      this.isValueMode = true;
      this.step = 0.1;
      this.mode = new ArrayList();
      this.current = current;
      this.name = classname.getSimpleName() + "_Mode";
      this.modeTitle = modeTitle;
      list.add(this);
   }

   public Value(Class classname, String mode, Object defaultValue, Object valueMin, Object valueMax) {
      this.defaultValue = this.value;
      this.name = classname.getSimpleName() + "_" + mode;
      this.value = defaultValue;
      this.valueMin = valueMin;
      this.valueMax = valueMax;
      this.step = 0.1;
      if (this.value instanceof Double) {
         this.isValueDouble = true;
      }

      list.add(this);
   }

   public Value(String name, String mode, Object defaultValue, Object valueMin, Object valueMax) {
      this.defaultValue = this.value;
      this.name = name + "_" + mode;
      this.value = defaultValue;
      this.valueMin = valueMin;
      this.valueMax = valueMax;
      this.step = 0.1;
      if (this.value instanceof Double) {
         this.isValueDouble = true;
      }

      list.add(this);
   }

   public Value(String name, String mode, Object value, Object valueMin, Object valueMax, double steps) {
      this.defaultValue = value;
      this.name = name + "_" + mode;
      this.value = value;
      this.valueMin = valueMin;
      this.valueMax = valueMax;
      this.step = steps;
      if (value instanceof Double) {
         this.isValueDouble = true;
      }

      list.add(this);
   }

   public Value(Class classname, String mode, Object value, Object valueMin, Object valueMax, double steps) {
      this.defaultValue = value;
      this.name = classname.getSimpleName() + "_" + mode;
      this.value = value;
      this.valueMin = valueMin;
      this.valueMax = valueMax;
      this.step = steps;
      if (value instanceof Double) {
         this.isValueDouble = true;
      }

      list.add(this);
   }

   public Value(Class classname, String mode, Object value) {
      this.defaultValue = value;
      this.name = classname.getSimpleName() + "_" + mode;
      this.value = value;
      if (value instanceof Boolean) {
         this.isValueBoolean = true;
      } else if (value instanceof Integer) {
         this.isValueInteger = true;
      } else if (value instanceof Float) {
         this.isValueFloat = true;
      } else if (value instanceof Long) {
         this.isValueLong = true;
      } else if (value instanceof Byte) {
         this.isValueByte = true;
      }

      list.add(this);
   }

   public Value(String name, String mode, Object value) {
      this.defaultValue = value;
      this.name = name + "_" + mode;
      this.value = value;
      if (value instanceof Boolean) {
         this.isValueBoolean = true;
      } else if (value instanceof Integer) {
         this.isValueInteger = true;
      } else if (value instanceof Float) {
         this.isValueFloat = true;
      } else if (value instanceof Long) {
         this.isValueLong = true;
      } else if (value instanceof Byte) {
         this.isValueByte = true;
      }

      list.add(this);
   }

   public Value(String name, String name2, String nam3, Object value, Object value2, Object value3) {
      this.defaultValue = value;
      this.name = name;
      this.value = value;
      if (value instanceof Boolean) {
         this.isValueBoolean = true;
      } else if (value instanceof Integer) {
         this.isValueInteger = true;
      } else if (value instanceof Float) {
         this.isValueFloat = true;
      } else if (value instanceof Double) {
         this.isValueDouble = true;
      } else if (value instanceof Long) {
         this.isValueLong = true;
      } else if (value instanceof Byte) {
         this.isValueByte = true;
      }

      list.add(this);
   }

   public static Value getBooleanValueByName(String name) {
      Iterator var1 = list.iterator();

      Value value;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         value = (Value)var1.next();
      } while(!value.isValueBoolean || !value.getValueName().equalsIgnoreCase(name));

      return value;
   }

   public static Value getDoubleValueByName(String name) {
      Iterator var1 = list.iterator();

      Value value;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         value = (Value)var1.next();
      } while(!value.isValueDouble || !value.getValueName().equalsIgnoreCase(name));

      return value;
   }

   public static Value getModeValue(String valueName, String title) {
      Iterator var2 = list.iterator();

      Value value;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         value = (Value)var2.next();
      } while(!value.isValueMode || !value.getValueName().equalsIgnoreCase(valueName) || !value.getModeTitle().equalsIgnoreCase(title));

      return value;
   }

   public static List getValue(Mod currentModule) {
      ArrayList list = new ArrayList();
      Iterator var2 = Value.list.iterator();

      while(var2.hasNext()) {
         Value value = (Value)var2.next();
         if (value.name.split("_")[0].equalsIgnoreCase(currentModule.getName())) {
            list.add(value);
         }
      }

      return list;
   }

   public void addValue(String valueName) {
      this.mode.add(valueName);
   }

   public Value LoadValue(String[] Value) {
      this.mode.addAll(Arrays.asList(Value));
      return this;
   }

   public int getCurrentMode() {
      if (this.current > this.mode.size() - 1) {
         Hanabi.INSTANCE.println("Value is to big! Set to 0. (" + this.mode.size() + ")");
         this.current = 0;
      }

      return this.current;
   }

   public void setCurrentMode(int current) {
      if (current > this.mode.size() - 1) {
         Hanabi.INSTANCE.println("Value is to big! Set to 0. (" + this.mode.size() + ")");
         this.current = 0;
      } else {
         this.current = current;
      }
   }

   public ArrayList listModes() {
      return this.mode;
   }

   public String getModeTitle() {
      return this.modeTitle;
   }

   public String getModeAt(int index) {
      return (String)this.mode.get(index);
   }

   public String getModeAt(String modeName) {
      Iterator var2 = this.mode.iterator();

      String s;
      do {
         if (!var2.hasNext()) {
            return "NULL";
         }

         s = (String)var2.next();
      } while(!s.equalsIgnoreCase(modeName));

      return s;
   }

   public int getModeInt(String modeName) {
      for(int i = 0; i < this.mode.size(); ++i) {
         if (((String)this.mode.get(i)).equalsIgnoreCase(modeName)) {
            return i;
         }
      }

      return 0;
   }

   public boolean isCurrentMode(String modeName) {
      return this.getModeAt(this.getCurrentMode()).equalsIgnoreCase(modeName);
   }

   public boolean getValueName(String modeName) {
      return this.getModeAt(this.getCurrentMode()).equalsIgnoreCase(modeName);
   }

   public String getAllModes() {
      StringBuilder all = new StringBuilder();
      Iterator var2 = this.mode.iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         all.append(s);
      }

      return all.toString();
   }

   public final String getValueName() {
      return this.name;
   }

   public String getDisplayTitle() {
      return this.isValueMode ? this.getModeTitle() : this.getValueName().split("_")[1];
   }

   public final Object getValueMin() {
      return this.value instanceof Double ? this.valueMin : null;
   }

   public final double getSteps() {
      return this.step;
   }

   public final Object getValueMax() {
      return this.value instanceof Double ? this.valueMax : null;
   }

   public final Object getDefaultValue() {
      return this.defaultValue;
   }

   public final Object getValueState() {
      return this.value;
   }

   public final void setValueState(Object value) {
      this.value = value;
   }

   public final Object getValue() {
      return this.value;
   }

   public String[] getModes() {
      return (String[])this.mode.toArray(new String[0]);
   }

   public String getName() {
      return this.name.split("_")[1];
   }
}
