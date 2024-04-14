package de.violence.save;

import java.util.ArrayList;

public class StringToValue {
   String name;
   int pointer;
   String string;
   boolean calculated;
   Double vald;
   Integer vali;
   String vals;
   Boolean valb;
   ArrayList vala;

   public StringToValue(String _string) throws Exception {
      this.string = _string;
      boolean foundName = false;
      String name = "";
      this.pointer = 1;

      while(!foundName) {
         name = name + this.string.charAt(this.pointer);
         ++this.pointer;
         if(this.string.charAt(this.pointer) == "%".charAt(0)) {
            foundName = true;
         }
      }

      ++this.pointer;
      this.calculated = false;
      this.name = name;
   }

   private void extractValue() throws Exception {
      if(!this.calculated) {
         this.calculated = true;
         String value = "";
         boolean foundVal = false;
         if(this.pointer + 1 < this.string.length()) {
            while(!foundVal) {
               value = value + this.string.charAt(this.pointer);
               ++this.pointer;
               if(this.string.charAt(this.pointer) == 37) {
                  foundVal = true;
               }
            }
         }

         switch(this.string.charAt(0)) {
         case 'a':
            this.vala = new ArrayList();
            int vp = 0;
            if(value.length() > 1) {
               while(vp < value.length()) {
                  boolean foundString = false;
                  String s = "";

                  while(!foundString) {
                     s = s + value.charAt(vp);
                     ++vp;
                     if(value.charAt(vp) == 58) {
                        this.vala.add(s);
                        foundString = true;
                        ++vp;
                     }
                  }
               }
            }
            break;
         case 'b':
            if(value.charAt(0) == "1".charAt(0)) {
               this.valb = Boolean.valueOf(true);
            } else {
               this.valb = Boolean.valueOf(false);
            }
            break;
         case 'd':
            this.vald = Double.valueOf(value);
            break;
         case 'i':
            this.vali = Integer.valueOf(value);
            break;
         case 's':
            this.vals = value;
            break;
         default:
            throw new Exception("Can\'t find the Type-Sign.");
         }

      }
   }

   public Double getDouble() throws Exception {
      this.extractValue();
      if(this.vald == null) {
         throw new Exception("The requested value " + this.getName() + " is no Double.");
      } else {
         return this.vald;
      }
   }

   public Integer getInt() throws Exception {
      this.extractValue();
      if(this.vali == null) {
         throw new Exception("The requested value " + this.getName() + " is no Integer.");
      } else {
         return this.vali;
      }
   }

   public String getString() throws Exception {
      this.extractValue();
      if(this.vals == null) {
         throw new Exception("The requested value " + this.getName() + " is no String.");
      } else {
         return this.vals;
      }
   }

   public Boolean getBoolean() throws Exception {
      this.extractValue();
      if(this.valb == null) {
         throw new Exception("The requested value " + this.getName() + " is no Boolean.");
      } else {
         return this.valb;
      }
   }

   public ArrayList getArray() throws Exception {
      this.extractValue();
      if(this.vala == null) {
         throw new Exception("The requested value " + this.getName() + " is no ArrayList<String>.");
      } else {
         return this.vala;
      }
   }

   public String getName() {
      return this.name;
   }
}
