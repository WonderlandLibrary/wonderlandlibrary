package de.violence.save;

import java.util.ArrayList;
import java.util.Iterator;

public class valueToString {
   String type;
   String value;
   String name;

   public valueToString(String _name, double val) {
      this.type = "d";
      this.name = _name;
      this.value = String.valueOf(val);
   }

   public valueToString(String _name, int val) {
      this.type = "i";
      this.name = _name;
      this.value = String.valueOf(val);
   }

   public valueToString(String _name, String val) {
      this.type = "s";
      this.name = _name;
      this.value = val;
   }

   public valueToString(String _name, boolean val) {
      this.type = "b";
      this.name = _name;
      if(val) {
         this.value = "1";
      } else {
         this.value = "0";
      }

   }

   public valueToString(String _name, ArrayList val) {
      this.type = "a";
      this.name = _name;
      this.value = "";

      String s;
      for(Iterator var4 = val.iterator(); var4.hasNext(); this.value = this.value + s + ":") {
         s = (String)var4.next();
      }

   }

   public String getString() {
      return this.type + this.name + "%" + this.value + "%";
   }
}
