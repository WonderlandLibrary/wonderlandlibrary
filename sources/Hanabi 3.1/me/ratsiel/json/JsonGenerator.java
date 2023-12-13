package me.ratsiel.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.ratsiel.json.abstracts.JsonHandler;
import me.ratsiel.json.abstracts.JsonValue;
import me.ratsiel.json.model.JsonArray;
import me.ratsiel.json.model.JsonBoolean;
import me.ratsiel.json.model.JsonNull;
import me.ratsiel.json.model.JsonNumber;
import me.ratsiel.json.model.JsonObject;
import me.ratsiel.json.model.JsonString;

public class JsonGenerator {
   protected final HashMap handlers = new HashMap();

   public JsonValue toJson(Object value) {
      if (value == null) {
         JsonValue jsonValue = new JsonNull();
         return jsonValue;
      } else {
         JsonHandler jsonHandler = this.getHandler(value.getClass());
         if (jsonHandler != null) {
            return jsonHandler.deserialize(value);
         } else {
            Class clazz = value.getClass();
            JsonString jsonValue;
            if (String.class.isAssignableFrom(clazz)) {
               jsonValue = new JsonString();
               ((JsonString)jsonValue).setValue((String)value);
               return jsonValue;
            } else if (Enum.class.isAssignableFrom(clazz)) {
               jsonValue = new JsonString();
               ((JsonString)jsonValue).setValue(((Enum)value).name());
               return jsonValue;
            } else if (Number.class.isAssignableFrom(clazz)) {
               JsonValue jsonValue = new JsonNumber();
               ((JsonNumber)jsonValue).setValue(value.toString());
               return jsonValue;
            } else if (Boolean.class.isAssignableFrom(clazz)) {
               JsonValue jsonValue = new JsonBoolean();
               ((JsonBoolean)jsonValue).setValue((Boolean)value);
               return jsonValue;
            } else {
               Iterator var18;
               if (List.class.isAssignableFrom(clazz)) {
                  JsonArray jsonArray = new JsonArray();
                  List list = (List)value;
                  var18 = list.iterator();

                  while(var18.hasNext()) {
                     Object listValue = var18.next();
                     jsonArray.add(this.toJson(listValue));
                  }

                  return jsonArray;
               } else {
                  JsonObject jsonObject;
                  if (Map.class.isAssignableFrom(clazz)) {
                     jsonObject = new JsonObject();
                     Map map = (Map)value;
                     var18 = map.entrySet().iterator();

                     while(true) {
                        while(var18.hasNext()) {
                           Map.Entry entry = (Map.Entry)var18.next();
                           Object key = entry.getKey();
                           Object mapValue = entry.getValue();
                           if (key == null) {
                              jsonObject.add("null", this.toJson(mapValue));
                           } else {
                              Class keyClazz = key.getClass();
                              if (!String.class.isAssignableFrom(keyClazz) && !Number.class.isAssignableFrom(keyClazz) && !Boolean.class.isAssignableFrom(keyClazz)) {
                                 if (Enum.class.isAssignableFrom(keyClazz)) {
                                    jsonObject.add(((Enum)key).name(), this.toJson(mapValue));
                                 }
                              } else {
                                 jsonObject.add(String.valueOf(key), this.toJson(mapValue));
                              }
                           }
                        }

                        return jsonObject;
                     }
                  } else {
                     jsonObject = new JsonObject();
                     Field[] var6;
                     int var7;
                     int var8;
                     Field declaredField;
                     if (this.hasSuperclass(clazz)) {
                        var6 = clazz.getSuperclass().getDeclaredFields();
                        var7 = var6.length;

                        for(var8 = 0; var8 < var7; ++var8) {
                           declaredField = var6[var8];
                           this.populateFields(value, jsonObject, declaredField);
                        }
                     }

                     var6 = clazz.getDeclaredFields();
                     var7 = var6.length;

                     for(var8 = 0; var8 < var7; ++var8) {
                        declaredField = var6[var8];
                        this.populateFields(value, jsonObject, declaredField);
                     }

                     return jsonObject;
                  }
               }
            }
         }
      }
   }

   private void populateFields(Object value, JsonObject jsonObject, Field declaredField) {
      if (declaredField.getModifiers() != 8) {
         declaredField.setAccessible(true);

         try {
            JsonValue transformedValue = this.toJson(declaredField.get(value));
            transformedValue.setKey(declaredField.getName());
            jsonObject.add(transformedValue);
         } catch (IllegalAccessException var5) {
            var5.printStackTrace();
         }

      }
   }

   public List fromJson(JsonValue jsonValue, Class listClazz, Class clazz) {
      return (List)listClazz.cast(this.fromJson((String)null, (JsonValue)jsonValue, clazz));
   }

   public Object fromJsonCast(JsonValue jsonValue, Class clazz) {
      return clazz.cast(this.fromJson((String)null, (JsonValue)jsonValue, clazz));
   }

   public Object fromJson(JsonValue jsonValue, Class clazz) {
      return this.fromJson((String)null, (JsonValue)jsonValue, clazz);
   }

   public Object fromJson(String key, JsonValue jsonValue, Class clazz) {
      JsonHandler jsonHandler = this.getHandler(clazz);
      if (jsonValue instanceof JsonString) {
         return jsonHandler != null ? jsonHandler.serialize(jsonValue) : clazz.cast(((JsonString)jsonValue).getValue());
      } else if (jsonValue instanceof JsonNumber) {
         return jsonHandler != null ? jsonHandler.serialize(jsonValue) : ((JsonNumber)jsonValue).getNumber(clazz);
      } else if (jsonValue instanceof JsonBoolean) {
         return jsonHandler != null ? jsonHandler.serialize(jsonValue) : clazz.cast(((JsonBoolean)jsonValue).isValue());
      } else if (jsonValue instanceof JsonNull) {
         return jsonHandler != null ? jsonHandler.serialize(jsonValue) : null;
      } else {
         if (jsonValue instanceof JsonObject) {
            if (key != null) {
               return this.fromJson(((JsonObject)jsonValue).get(key), clazz);
            }

            try {
               Object object = clazz.newInstance();
               if (jsonHandler != null) {
                  return jsonHandler.serialize(jsonValue);
               }

               Class objectClazz = object.getClass();
               Field[] var7;
               int var8;
               int var9;
               Field declaredField;
               if (this.hasSuperclass(objectClazz)) {
                  var7 = objectClazz.getSuperclass().getDeclaredFields();
                  var8 = var7.length;

                  for(var9 = 0; var9 < var8; ++var9) {
                     declaredField = var7[var9];
                     declaredField.setAccessible(true);
                     declaredField.set(object, this.fromJson(declaredField.getName(), jsonValue, declaredField.getType()));
                  }
               }

               var7 = objectClazz.getDeclaredFields();
               var8 = var7.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  declaredField = var7[var9];
                  declaredField.setAccessible(true);
                  declaredField.set(object, this.fromJson(declaredField.getName(), jsonValue, declaredField.getType()));
               }

               return object;
            } catch (IllegalAccessException | InstantiationException var11) {
               var11.printStackTrace();
            }
         } else if (jsonValue instanceof JsonArray) {
            List list = new ArrayList();
            ((JsonArray)jsonValue).loop((integer, listValue) -> {
               list.add(this.fromJson(listValue, clazz));
            });
            return list;
         }

         return null;
      }
   }

   public boolean hasSuperclass(Class clazz) {
      return clazz.getSuperclass() != null;
   }

   public void registerHandler(Class clazz, JsonHandler jsonHandler) {
      if (!this.isRegistered(clazz)) {
         this.handlers.put(clazz, jsonHandler);
      }

   }

   public void unregisterHandler(Class clazz) {
      if (this.isRegistered(clazz)) {
         this.handlers.remove(clazz);
      }

   }

   public JsonHandler getHandler(Class clazz) {
      return !this.isRegistered(clazz) ? null : (JsonHandler)this.handlers.get(clazz);
   }

   public boolean isRegistered(Class clazz) {
      return this.handlers.containsKey(clazz);
   }
}
