package me.ratsiel.json.model;

import me.ratsiel.json.abstracts.JsonValue;

public class JsonString extends JsonValue {
   private String value;

   public JsonString() {
   }

   public JsonString(String key, String value) {
      super(key);
      this.value = value;
   }

   public JsonString(String value) {
      this.value = value;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      String space = this.createSpace();
      stringBuilder.append(space);
      if (this.getKey() != null && !this.getKey().isEmpty()) {
         stringBuilder.append("\"").append(this.getKey()).append("\"").append(" : ").append("\"").append(this.getValue()).append("\"");
      } else {
         stringBuilder.append("\"").append(this.value).append("\"");
      }

      return stringBuilder.toString();
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
