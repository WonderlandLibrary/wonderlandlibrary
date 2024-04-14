package de.violence.save;

import de.violence.save.Value;
import de.violence.save.valueToString;
import java.util.ArrayList;
import java.util.Iterator;

public class valueString {
   String string;
   ArrayList values = new ArrayList();

   public valueString(String input) {
      this.string = input;
      this.updateValues();
   }

   public valueString() {
      this.string = "";
   }

   public boolean containsValue(String name) throws Exception {
      Iterator var3 = this.getValues().iterator();

      while(var3.hasNext()) {
         Value v = (Value)var3.next();
         if(v.getValue().getName().equals(name)) {
            return true;
         }
      }

      return false;
   }

   public void removeValue(String name) throws Exception {
      if(this.containsValue(name)) {
         Value old = this.getValue(name);
         this.string = this.string.replace(old.valString + ";", "");
         this.updateValues();
      }

   }

   public void setValue(String name, double val) throws Exception {
      this.removeValue(name);
      this.string = this.string + (new valueToString(name, val)).getString() + ";";
      this.updateValues();
   }

   public void setValue(String name, int val) throws Exception {
      this.removeValue(name);
      this.string = this.string + (new valueToString(name, val)).getString() + ";";
      this.updateValues();
   }

   public void setValue(String name, String val) throws Exception {
      this.removeValue(name);
      this.string = this.string + (new valueToString(name, val)).getString() + ";";
      this.updateValues();
   }

   public void setValue(String name, ArrayList val) throws Exception {
      this.removeValue(name);
      this.string = this.string + (new valueToString(name, val)).getString() + ";";
      this.updateValues();
   }

   public void setValue(String name, boolean val) throws Exception {
      this.removeValue(name);
      this.string = this.string + (new valueToString(name, val)).getString() + ";";
      this.updateValues();
   }

   public String getString() {
      return this.string;
   }

   public void setValueString(String s) {
      this.string = s;
      this.updateValues();
   }

   public void updateValues() {
      this.values.clear();

      for(int pointer = 0; pointer < this.string.length(); ++pointer) {
         String stringValue;
         for(stringValue = ""; this.string.charAt(pointer) != 59; ++pointer) {
            stringValue = stringValue + this.string.charAt(pointer);
         }

         this.values.add(new Value(stringValue));
      }

   }

   public ArrayList getValues() {
      return this.values;
   }

   public Boolean getBooleanValue(String name) throws Exception {
      Value v = this.getValue(name);
      return v == null?null:v.getValue().getBoolean();
   }

   public Integer getIntegerValue(String name) throws Exception {
      Value v = this.getValue(name);
      return v == null?null:v.getValue().getInt();
   }

   public String getStringValue(String name) throws Exception {
      Value v = this.getValue(name);
      return v == null?null:v.getValue().getString();
   }

   public Double getDoubleValue(String name) throws Exception {
      Value v = this.getValue(name);
      return v == null?null:v.getValue().getDouble();
   }

   public ArrayList getArrayListValue(String name) throws Exception {
      Value v = this.getValue(name);
      return v == null?null:v.getValue().getArray();
   }

   private Value getValue(String name) throws Exception {
      Iterator var3 = this.getValues().iterator();

      while(var3.hasNext()) {
         Value v = (Value)var3.next();
         if(v.getValue().getName().equals(name)) {
            return v;
         }
      }

      return null;
   }
}
