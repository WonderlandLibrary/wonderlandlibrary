package me.ratsiel.json.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import me.ratsiel.json.abstracts.JsonValue;
import me.ratsiel.json.interfaces.IListable;

public class JsonArray extends JsonValue implements IListable {
   protected final List values = new ArrayList();

   public JsonArray() {
   }

   public JsonArray(String key) {
      super(key);
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      String space = this.createSpace();
      stringBuilder.append(space);
      if (this.getKey() != null && !this.getKey().isEmpty()) {
         stringBuilder.append("\"").append(this.getKey()).append("\"").append(" : ");
      }

      stringBuilder.append("[");
      this.loop((integer, jsonValue) -> {
         jsonValue.setIntend(this.getIntend() + 2);
         stringBuilder.append("\n");
         if (integer != this.size() - 1) {
            stringBuilder.append(jsonValue).append(",");
         } else {
            stringBuilder.append(jsonValue);
         }

      });
      stringBuilder.append("\n").append(space).append("]");
      return stringBuilder.toString();
   }

   public int size() {
      return this.values.size();
   }

   public void add(JsonValue value) {
      this.values.add(value);
   }

   public void add(int index, JsonValue value) {
      this.values.set(index, value);
   }

   public void addString(String value) {
      JsonString jsonString = new JsonString(value);
      this.add((JsonValue)jsonString);
   }

   public void addBoolean(boolean value) {
      JsonBoolean jsonBoolean = new JsonBoolean(value);
      this.add((JsonValue)jsonBoolean);
   }

   public void addByte(byte value) {
      JsonNumber jsonNumber = new JsonNumber(String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addInteger(int value) {
      JsonNumber jsonNumber = new JsonNumber(String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addShort(short value) {
      JsonNumber jsonNumber = new JsonNumber(String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addDouble(double value) {
      JsonNumber jsonNumber = new JsonNumber(String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addFloat(float value) {
      JsonNumber jsonNumber = new JsonNumber(String.valueOf(value));
      this.add((JsonValue)jsonNumber);
   }

   public void addLong(long value) {
      JsonNumber jsonNumber = new JsonNumber(String.valueOf(value));
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
