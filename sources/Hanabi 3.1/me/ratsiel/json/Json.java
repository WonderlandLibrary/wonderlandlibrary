package me.ratsiel.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import me.ratsiel.json.abstracts.JsonHandler;
import me.ratsiel.json.abstracts.JsonValue;
import me.ratsiel.json.model.JsonString;

public class Json {
   private final JsonGenerator jsonGenerator = new JsonGenerator();
   private final JsonParser jsonParser = new JsonParser();

   public Json() {
      this.registerHandler(UUID.class, new JsonHandler() {
         public UUID serialize(JsonValue jsonValue) {
            JsonString jsonString = (JsonString)jsonValue;
            return UUID.fromString(jsonString.getValue());
         }

         public JsonValue deserialize(UUID value) {
            return new JsonString(value.toString());
         }
      });
   }

   public JsonValue fromJsonString(String json) {
      return this.jsonParser.parse(json);
   }

   public JsonValue fromJsonString(String json, Class clazz) {
      return this.jsonParser.parse(json, clazz);
   }

   public JsonValue fromFile(File file, Class clazz) throws FileNotFoundException {
      return (JsonValue)clazz.cast(this.jsonParser.parse(file));
   }

   public JsonValue fromFile(File file) throws FileNotFoundException {
      return this.jsonParser.parse((InputStream)(new FileInputStream(file)));
   }

   public JsonValue parse(InputStream inputStream, Class clazz) {
      return this.jsonParser.parse(inputStream, clazz);
   }

   public JsonValue parse(InputStream inputStream) {
      return this.jsonParser.parse(inputStream);
   }

   public JsonValue toJson(Object value) {
      return this.jsonGenerator.toJson(value);
   }

   public List fromJson(JsonValue jsonValue, Class listClazz, Class clazz) {
      return this.jsonGenerator.fromJson(jsonValue, listClazz, clazz);
   }

   public Object fromJsonCast(JsonValue jsonValue, Class clazz) {
      return this.jsonGenerator.fromJsonCast(jsonValue, clazz);
   }

   public Object fromJson(JsonValue jsonValue, Class clazz) {
      return this.jsonGenerator.fromJson(jsonValue, clazz);
   }

   public void registerHandler(Class clazz, JsonHandler jsonHandler) {
      this.jsonGenerator.registerHandler(clazz, jsonHandler);
   }

   public void unregisterHandler(Class clazz) {
      this.jsonGenerator.unregisterHandler(clazz);
   }
}
