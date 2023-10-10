package com.ibm.icu.impl.locale;

public class Extension {
   private char _key;
   protected String _value;

   protected Extension(char var1) {
      this._key = var1;
   }

   Extension(char var1, String var2) {
      this._key = var1;
      this._value = var2;
   }

   public char getKey() {
      return this._key;
   }

   public String getValue() {
      return this._value;
   }

   public String getID() {
      return this._key + "-" + this._value;
   }

   public String toString() {
      return this.getID();
   }
}
