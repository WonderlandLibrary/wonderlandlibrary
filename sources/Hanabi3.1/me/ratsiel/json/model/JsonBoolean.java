package me.ratsiel.json.model;

import me.ratsiel.json.abstracts.JsonValue;

public class JsonBoolean extends JsonValue {
   public boolean value;

   public JsonBoolean() {
   }

   public JsonBoolean(boolean value) {
      this.value = value;
   }

   public JsonBoolean(String key, boolean value) {
      super(key);
      this.value = value;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      String space = this.createSpace();
      stringBuilder.append(space);
      if (this.getKey() != null && !this.getKey().isEmpty()) {
         stringBuilder.append("\"").append(this.getKey()).append("\"").append(" : ");
      }

      stringBuilder.append(this.isValue());
      return stringBuilder.toString();
   }

   public boolean isValue() {
      return this.value;
   }

   public void setValue(boolean value) {
      this.value = value;
   }
}
