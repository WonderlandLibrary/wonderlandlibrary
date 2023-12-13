package me.ratsiel.json.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import me.ratsiel.json.abstracts.JsonValue;
import me.ratsiel.json.interfaces.IListable;

public class JsonObject extends JsonValue implements IListable {
   protected final List values = new ArrayList();

   public JsonObject() {
   }

   public JsonObject(String key) {
      super(key);
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      String space = this.createSpace();
      stringBuilder.append(space);
      if (this.getKey() != null && !this.getKey().isEmpty()) {
         stringBuilder.append("\"").append(this.getKey()).append("\"").append(" : ");
      }

      stringBuilder.append("{");
      this.loop((integer, jsonValue) -> {
         jsonValue.setIntend(this.getIntend() + 2);
         stringBuilder.append("\n");
         if (integer != this.size() - 1) {
            stringBuilder.append(jsonValue).append(",");
         } else {
            stringBuilder.append(jsonValue);
         }

      });
      stringBuilder.append("\n").append(space).append("}");
      return stringBuilder.toString();
   }

   public JsonValue get(String key) {
      return !this.has(key) ? null : (JsonValue)this.values.stream().filter((jsonValue) -> {
         return jsonValue.getKey().equalsIgnoreCase(key);
      }).findFirst().get();
   }

   public JsonValue get(String key, Class clazz) {
      return (JsonValue)clazz.cast(this.get(key));
   }

   public boolean has(String key) {
      return this.values.stream().anyMatch((jsonValue) -> {
         return jsonValue.getKey() != null && jsonValue.getKey().equalsIgnoreCase(key);
      });
   }

   public int getIndex(String key) {
      if (!this.has(key)) {
         return -1;
      } else {
         for(int index = 0; index < this.values.size(); ++index) {
            JsonValue jsonValue = (JsonValue)this.values.get(index);
            if (jsonValue.getKey().equalsIgnoreCase(key)) {
               return index;
            }
         }

         return -1;
      }
   }

   public int size() {
      return this.values.size();
   }

   public void add(String key, JsonValue jsonValue) {
      jsonValue.setKey(key);
      this.add(jsonValue);
   }

   public void add(JsonValue value) {
      if (this.has(value.getKey())) {
         int index = this.getIndex(value.getKey());
         this.add(index, value);
      } else {
         this.values.add(value);
      }

   }

   public void add(int index, JsonValue value) {
      this.values.set(index, value);
   }

   public void addString(String key, String value) {
      JsonString jsonString = new JsonString(key, value);
      this.add((JsonValue)jsonString);
   }

   public void addBoolean(String key, boolean value) {
      JsonBoolean jsonBoolean = new JsonBoolean(key, value);
      this.add((JsonValue)jsonBoolean);
   }

   public void addByte(String key, byte value) {
      JsonNumber jsonNumber = new JsonNumber(key, String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addInteger(String key, int value) {
      JsonNumber jsonNumber = new JsonNumber(key, String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addShort(String key, short value) {
      JsonNumber jsonNumber = new JsonNumber(key, String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addDouble(String key, double value) {
      JsonNumber jsonNumber = new JsonNumber(key, String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addFloat(String key, float value) {
      JsonNumber jsonNumber = new JsonNumber(key, String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addLong(String key, long value) {
      JsonNumber jsonNumber = new JsonNumber(key, String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public JsonValue get(int index, Class clazz) {
      return (JsonValue)clazz.cast(this.get(index));
   }

   public JsonValue get(int index) {
      return (JsonValue)this.values.get(index);
   }

   public void loop(Consumer consumer) {
      Iterator var2 = this.values.iterator();

      while(var2.hasNext()) {
         JsonValue value = (JsonValue)var2.next();
         consumer.accept(value);
      }

   }

   public void loop(BiConsumer consumer) {
      for(int index = 0; index < this.values.size(); ++index) {
         JsonValue jsonValue = (JsonValue)this.values.get(index);
         consumer.accept(index, jsonValue);
      }

   }
}
