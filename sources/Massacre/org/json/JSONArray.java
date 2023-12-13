package org.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONArray implements Iterable<Object> {
   private final ArrayList<Object> myArrayList;

   public JSONArray() {
      this.myArrayList = new ArrayList();
   }

   public JSONArray(JSONTokener x) throws JSONException {
      this();
      if (x.nextClean() != '[') {
         throw x.syntaxError("A JSONArray text must start with '['");
      } else {
         char nextChar = x.nextClean();
         if (nextChar == 0) {
            throw x.syntaxError("Expected a ',' or ']'");
         } else if (nextChar != ']') {
            x.back();

            while(true) {
               if (x.nextClean() == ',') {
                  x.back();
                  this.myArrayList.add(JSONObject.NULL);
               } else {
                  x.back();
                  this.myArrayList.add(x.nextValue());
               }

               switch(x.nextClean()) {
               case '\u0000':
                  throw x.syntaxError("Expected a ',' or ']'");
               case ',':
                  nextChar = x.nextClean();
                  if (nextChar == 0) {
                     throw x.syntaxError("Expected a ',' or ']'");
                  }

                  if (nextChar == ']') {
                     return;
                  }

                  x.back();
                  break;
               case ']':
                  return;
               default:
                  throw x.syntaxError("Expected a ',' or ']'");
               }
            }
         }
      }
   }

   public JSONArray(String source) throws JSONException {
      this(new JSONTokener(source));
   }

   public JSONArray(Collection<?> collection) {
      if (collection == null) {
         this.myArrayList = new ArrayList();
      } else {
         this.myArrayList = new ArrayList(collection.size());
         this.addAll(collection, true);
      }

   }

   public JSONArray(Iterable<?> iter) {
      this();
      if (iter != null) {
         this.addAll(iter, true);
      }
   }

   public JSONArray(JSONArray array) {
      if (array == null) {
         this.myArrayList = new ArrayList();
      } else {
         this.myArrayList = new ArrayList(array.myArrayList);
      }

   }

   public JSONArray(Object array) throws JSONException {
      this();
      if (!array.getClass().isArray()) {
         throw new JSONException("JSONArray initial value should be a string or collection or array.");
      } else {
         this.addAll(array, true);
      }
   }

   public JSONArray(int initialCapacity) throws JSONException {
      if (initialCapacity < 0) {
         throw new JSONException("JSONArray initial capacity cannot be negative.");
      } else {
         this.myArrayList = new ArrayList(initialCapacity);
      }
   }

   public Iterator<Object> iterator() {
      return this.myArrayList.iterator();
   }

   public Object get(int index) throws JSONException {
      Object object = this.opt(index);
      if (object == null) {
         throw new JSONException("JSONArray[" + index + "] not found.");
      } else {
         return object;
      }
   }

   public boolean getBoolean(int index) throws JSONException {
      Object object = this.get(index);
      if (!object.equals(Boolean.FALSE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("false"))) {
         if (!object.equals(Boolean.TRUE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("true"))) {
            throw wrongValueFormatException(index, "boolean", (Throwable)null);
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public double getDouble(int index) throws JSONException {
      Object object = this.get(index);
      if (object instanceof Number) {
         return ((Number)object).doubleValue();
      } else {
         try {
            return Double.parseDouble(object.toString());
         } catch (Exception var4) {
            throw wrongValueFormatException(index, "double", var4);
         }
      }
   }

   public float getFloat(int index) throws JSONException {
      Object object = this.get(index);
      if (object instanceof Number) {
         return (Float)object;
      } else {
         try {
            return Float.parseFloat(object.toString());
         } catch (Exception var4) {
            throw wrongValueFormatException(index, "float", var4);
         }
      }
   }

   public Number getNumber(int index) throws JSONException {
      Object object = this.get(index);

      try {
         return object instanceof Number ? (Number)object : JSONObject.stringToNumber(object.toString());
      } catch (Exception var4) {
         throw wrongValueFormatException(index, "number", var4);
      }
   }

   public <E extends Enum<E>> E getEnum(Class<E> clazz, int index) throws JSONException {
      E val = this.optEnum(clazz, index);
      if (val == null) {
         throw wrongValueFormatException(index, "enum of type " + JSONObject.quote(clazz.getSimpleName()), (Throwable)null);
      } else {
         return val;
      }
   }

   public BigDecimal getBigDecimal(int index) throws JSONException {
      Object object = this.get(index);
      BigDecimal val = JSONObject.objectToBigDecimal(object, (BigDecimal)null);
      if (val == null) {
         throw wrongValueFormatException(index, "BigDecimal", object, (Throwable)null);
      } else {
         return val;
      }
   }

   public BigInteger getBigInteger(int index) throws JSONException {
      Object object = this.get(index);
      BigInteger val = JSONObject.objectToBigInteger(object, (BigInteger)null);
      if (val == null) {
         throw wrongValueFormatException(index, "BigInteger", object, (Throwable)null);
      } else {
         return val;
      }
   }

   public int getInt(int index) throws JSONException {
      Object object = this.get(index);
      if (object instanceof Number) {
         return ((Number)object).intValue();
      } else {
         try {
            return Integer.parseInt(object.toString());
         } catch (Exception var4) {
            throw wrongValueFormatException(index, "int", var4);
         }
      }
   }

   public JSONArray getJSONArray(int index) throws JSONException {
      Object object = this.get(index);
      if (object instanceof JSONArray) {
         return (JSONArray)object;
      } else {
         throw wrongValueFormatException(index, "JSONArray", (Throwable)null);
      }
   }

   public JSONObject getJSONObject(int index) throws JSONException {
      Object object = this.get(index);
      if (object instanceof JSONObject) {
         return (JSONObject)object;
      } else {
         throw wrongValueFormatException(index, "JSONObject", (Throwable)null);
      }
   }

   public long getLong(int index) throws JSONException {
      Object object = this.get(index);
      if (object instanceof Number) {
         return ((Number)object).longValue();
      } else {
         try {
            return Long.parseLong(object.toString());
         } catch (Exception var4) {
            throw wrongValueFormatException(index, "long", var4);
         }
      }
   }

   public String getString(int index) throws JSONException {
      Object object = this.get(index);
      if (object instanceof String) {
         return (String)object;
      } else {
         throw wrongValueFormatException(index, "String", (Throwable)null);
      }
   }

   public boolean isNull(int index) {
      return JSONObject.NULL.equals(this.opt(index));
   }

   public String join(String separator) throws JSONException {
      int len = this.length();
      if (len == 0) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder(JSONObject.valueToString(this.myArrayList.get(0)));

         for(int i = 1; i < len; ++i) {
            sb.append(separator).append(JSONObject.valueToString(this.myArrayList.get(i)));
         }

         return sb.toString();
      }
   }

   public int length() {
      return this.myArrayList.size();
   }

   public Object opt(int index) {
      return index >= 0 && index < this.length() ? this.myArrayList.get(index) : null;
   }

   public boolean optBoolean(int index) {
      return this.optBoolean(index, false);
   }

   public boolean optBoolean(int index, boolean defaultValue) {
      try {
         return this.getBoolean(index);
      } catch (Exception var4) {
         return defaultValue;
      }
   }

   public double optDouble(int index) {
      return this.optDouble(index, Double.NaN);
   }

   public double optDouble(int index, double defaultValue) {
      Number val = this.optNumber(index, (Number)null);
      if (val == null) {
         return defaultValue;
      } else {
         double doubleValue = val.doubleValue();
         return doubleValue;
      }
   }

   public float optFloat(int index) {
      return this.optFloat(index, Float.NaN);
   }

   public float optFloat(int index, float defaultValue) {
      Number val = this.optNumber(index, (Number)null);
      if (val == null) {
         return defaultValue;
      } else {
         float floatValue = val.floatValue();
         return floatValue;
      }
   }

   public int optInt(int index) {
      return this.optInt(index, 0);
   }

   public int optInt(int index, int defaultValue) {
      Number val = this.optNumber(index, (Number)null);
      return val == null ? defaultValue : val.intValue();
   }

   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index) {
      return this.optEnum(clazz, index, (Enum)null);
   }

   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue) {
      try {
         Object val = this.opt(index);
         if (JSONObject.NULL.equals(val)) {
            return defaultValue;
         } else if (clazz.isAssignableFrom(val.getClass())) {
            E myE = (Enum)val;
            return myE;
         } else {
            return Enum.valueOf(clazz, val.toString());
         }
      } catch (IllegalArgumentException var6) {
         return defaultValue;
      } catch (NullPointerException var7) {
         return defaultValue;
      }
   }

   public BigInteger optBigInteger(int index, BigInteger defaultValue) {
      Object val = this.opt(index);
      return JSONObject.objectToBigInteger(val, defaultValue);
   }

   public BigDecimal optBigDecimal(int index, BigDecimal defaultValue) {
      Object val = this.opt(index);
      return JSONObject.objectToBigDecimal(val, defaultValue);
   }

   public JSONArray optJSONArray(int index) {
      Object o = this.opt(index);
      return o instanceof JSONArray ? (JSONArray)o : null;
   }

   public JSONObject optJSONObject(int index) {
      Object o = this.opt(index);
      return o instanceof JSONObject ? (JSONObject)o : null;
   }

   public long optLong(int index) {
      return this.optLong(index, 0L);
   }

   public long optLong(int index, long defaultValue) {
      Number val = this.optNumber(index, (Number)null);
      return val == null ? defaultValue : val.longValue();
   }

   public Number optNumber(int index) {
      return this.optNumber(index, (Number)null);
   }

   public Number optNumber(int index, Number defaultValue) {
      Object val = this.opt(index);
      if (JSONObject.NULL.equals(val)) {
         return defaultValue;
      } else if (val instanceof Number) {
         return (Number)val;
      } else if (val instanceof String) {
         try {
            return JSONObject.stringToNumber((String)val);
         } catch (Exception var5) {
            return defaultValue;
         }
      } else {
         return defaultValue;
      }
   }

   public String optString(int index) {
      return this.optString(index, "");
   }

   public String optString(int index, String defaultValue) {
      Object object = this.opt(index);
      return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
   }

   public JSONArray put(boolean value) {
      return this.put((Object)(value ? Boolean.TRUE : Boolean.FALSE));
   }

   public JSONArray put(Collection<?> value) {
      return this.put((Object)(new JSONArray(value)));
   }

   public JSONArray put(double value) throws JSONException {
      return this.put((Object)value);
   }

   public JSONArray put(float value) throws JSONException {
      return this.put((Object)value);
   }

   public JSONArray put(int value) {
      return this.put((Object)value);
   }

   public JSONArray put(long value) {
      return this.put((Object)value);
   }

   public JSONArray put(Map<?, ?> value) {
      return this.put((Object)(new JSONObject(value)));
   }

   public JSONArray put(Object value) {
      JSONObject.testValidity(value);
      this.myArrayList.add(value);
      return this;
   }

   public JSONArray put(int index, boolean value) throws JSONException {
      return this.put(index, (Object)(value ? Boolean.TRUE : Boolean.FALSE));
   }

   public JSONArray put(int index, Collection<?> value) throws JSONException {
      return this.put(index, (Object)(new JSONArray(value)));
   }

   public JSONArray put(int index, double value) throws JSONException {
      return this.put(index, (Object)value);
   }

   public JSONArray put(int index, float value) throws JSONException {
      return this.put(index, (Object)value);
   }

   public JSONArray put(int index, int value) throws JSONException {
      return this.put(index, (Object)value);
   }

   public JSONArray put(int index, long value) throws JSONException {
      return this.put(index, (Object)value);
   }

   public JSONArray put(int index, Map<?, ?> value) throws JSONException {
      this.put(index, (Object)(new JSONObject(value)));
      return this;
   }

   public JSONArray put(int index, Object value) throws JSONException {
      if (index < 0) {
         throw new JSONException("JSONArray[" + index + "] not found.");
      } else if (index < this.length()) {
         JSONObject.testValidity(value);
         this.myArrayList.set(index, value);
         return this;
      } else if (index == this.length()) {
         return this.put(value);
      } else {
         this.myArrayList.ensureCapacity(index + 1);

         while(index != this.length()) {
            this.myArrayList.add(JSONObject.NULL);
         }

         return this.put(value);
      }
   }

   public JSONArray putAll(Collection<?> collection) {
      this.addAll(collection, false);
      return this;
   }

   public JSONArray putAll(Iterable<?> iter) {
      this.addAll(iter, false);
      return this;
   }

   public JSONArray putAll(JSONArray array) {
      this.myArrayList.addAll(array.myArrayList);
      return this;
   }

   public JSONArray putAll(Object array) throws JSONException {
      this.addAll(array, false);
      return this;
   }

   public Object query(String jsonPointer) {
      return this.query(new JSONPointer(jsonPointer));
   }

   public Object query(JSONPointer jsonPointer) {
      return jsonPointer.queryFrom(this);
   }

   public Object optQuery(String jsonPointer) {
      return this.optQuery(new JSONPointer(jsonPointer));
   }

   public Object optQuery(JSONPointer jsonPointer) {
      try {
         return jsonPointer.queryFrom(this);
      } catch (JSONPointerException var3) {
         return null;
      }
   }

   public Object remove(int index) {
      return index >= 0 && index < this.length() ? this.myArrayList.remove(index) : null;
   }

   public boolean similar(Object other) {
      if (!(other instanceof JSONArray)) {
         return false;
      } else {
         int len = this.length();
         if (len != ((JSONArray)other).length()) {
            return false;
         } else {
            for(int i = 0; i < len; ++i) {
               Object valueThis = this.myArrayList.get(i);
               Object valueOther = ((JSONArray)other).myArrayList.get(i);
               if (valueThis != valueOther) {
                  if (valueThis == null) {
                     return false;
                  }

                  if (valueThis instanceof JSONObject) {
                     if (!((JSONObject)valueThis).similar(valueOther)) {
                        return false;
                     }
                  } else if (valueThis instanceof JSONArray) {
                     if (!((JSONArray)valueThis).similar(valueOther)) {
                        return false;
                     }
                  } else if (!valueThis.equals(valueOther)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public JSONObject toJSONObject(JSONArray names) throws JSONException {
      if (names != null && !names.isEmpty() && !this.isEmpty()) {
         JSONObject jo = new JSONObject(names.length());

         for(int i = 0; i < names.length(); ++i) {
            jo.put(names.getString(i), this.opt(i));
         }

         return jo;
      } else {
         return null;
      }
   }

   public String toString() {
      try {
         return this.toString(0);
      } catch (Exception var2) {
         return null;
      }
   }

   public String toString(int indentFactor) throws JSONException {
      StringWriter sw = new StringWriter();
      synchronized(sw.getBuffer()) {
         return this.write(sw, indentFactor, 0).toString();
      }
   }

   public Writer write(Writer writer) throws JSONException {
      return this.write(writer, 0, 0);
   }

   public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
      try {
         boolean needsComma = false;
         int length = this.length();
         writer.write(91);
         if (length == 1) {
            try {
               JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
            } catch (Exception var10) {
               throw new JSONException("Unable to write JSONArray value at index: 0", var10);
            }
         } else if (length != 0) {
            int newIndent = indent + indentFactor;

            for(int i = 0; i < length; ++i) {
               if (needsComma) {
                  writer.write(44);
               }

               if (indentFactor > 0) {
                  writer.write(10);
               }

               JSONObject.indent(writer, newIndent);

               try {
                  JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newIndent);
               } catch (Exception var9) {
                  throw new JSONException("Unable to write JSONArray value at index: " + i, var9);
               }

               needsComma = true;
            }

            if (indentFactor > 0) {
               writer.write(10);
            }

            JSONObject.indent(writer, indent);
         }

         writer.write(93);
         return writer;
      } catch (IOException var11) {
         throw new JSONException(var11);
      }
   }

   public List<Object> toList() {
      List<Object> results = new ArrayList(this.myArrayList.size());
      Iterator var2 = this.myArrayList.iterator();

      while(true) {
         while(var2.hasNext()) {
            Object element = var2.next();
            if (element != null && !JSONObject.NULL.equals(element)) {
               if (element instanceof JSONArray) {
                  results.add(((JSONArray)element).toList());
               } else if (element instanceof JSONObject) {
                  results.add(((JSONObject)element).toMap());
               } else {
                  results.add(element);
               }
            } else {
               results.add((Object)null);
            }
         }

         return results;
      }
   }

   public boolean isEmpty() {
      return this.myArrayList.isEmpty();
   }

   private void addAll(Collection<?> collection, boolean wrap) {
      this.myArrayList.ensureCapacity(this.myArrayList.size() + collection.size());
      Iterator var3;
      Object o;
      if (wrap) {
         var3 = collection.iterator();

         while(var3.hasNext()) {
            o = var3.next();
            this.put(JSONObject.wrap(o));
         }
      } else {
         var3 = collection.iterator();

         while(var3.hasNext()) {
            o = var3.next();
            this.put(o);
         }
      }

   }

   private void addAll(Iterable<?> iter, boolean wrap) {
      Iterator var3;
      Object o;
      if (wrap) {
         var3 = iter.iterator();

         while(var3.hasNext()) {
            o = var3.next();
            this.put(JSONObject.wrap(o));
         }
      } else {
         var3 = iter.iterator();

         while(var3.hasNext()) {
            o = var3.next();
            this.put(o);
         }
      }

   }

   private void addAll(Object array, boolean wrap) throws JSONException {
      if (array.getClass().isArray()) {
         int length = Array.getLength(array);
         this.myArrayList.ensureCapacity(this.myArrayList.size() + length);
         int i;
         if (wrap) {
            for(i = 0; i < length; ++i) {
               this.put(JSONObject.wrap(Array.get(array, i)));
            }
         } else {
            for(i = 0; i < length; ++i) {
               this.put(Array.get(array, i));
            }
         }
      } else if (array instanceof JSONArray) {
         this.myArrayList.addAll(((JSONArray)array).myArrayList);
      } else if (array instanceof Collection) {
         this.addAll((Collection)array, wrap);
      } else {
         if (!(array instanceof Iterable)) {
            throw new JSONException("JSONArray initial value should be a string or collection or array.");
         }

         this.addAll((Iterable)array, wrap);
      }

   }

   private static JSONException wrongValueFormatException(int idx, String valueType, Throwable cause) {
      return new JSONException("JSONArray[" + idx + "] is not a " + valueType + ".", cause);
   }

   private static JSONException wrongValueFormatException(int idx, String valueType, Object value, Throwable cause) {
      return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (" + value + ").", cause);
   }
}
