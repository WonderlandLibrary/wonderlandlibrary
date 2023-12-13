package org.json;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class JSONWriter {
   private static final int maxdepth = 200;
   private boolean comma = false;
   protected char mode = 'i';
   private final JSONObject[] stack = new JSONObject[200];
   private int top = 0;
   protected Appendable writer;

   public JSONWriter(Appendable w) {
      this.writer = w;
   }

   private JSONWriter append(String string) throws JSONException {
      if (string == null) {
         throw new JSONException("Null pointer");
      } else if (this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Value out of sequence.");
      } else {
         try {
            if (this.comma && this.mode == 'a') {
               this.writer.append(',');
            }

            this.writer.append(string);
         } catch (IOException var3) {
            throw new JSONException(var3);
         }

         if (this.mode == 'o') {
            this.mode = 'k';
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter array() throws JSONException {
      if (this.mode != 'i' && this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Misplaced array.");
      } else {
         this.push((JSONObject)null);
         this.append("[");
         this.comma = false;
         return this;
      }
   }

   private JSONWriter end(char m, char c) throws JSONException {
      if (this.mode != m) {
         throw new JSONException(m == 'a' ? "Misplaced endArray." : "Misplaced endObject.");
      } else {
         this.pop(m);

         try {
            this.writer.append(c);
         } catch (IOException var4) {
            throw new JSONException(var4);
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter endArray() throws JSONException {
      return this.end('a', ']');
   }

   public JSONWriter endObject() throws JSONException {
      return this.end('k', '}');
   }

   public JSONWriter key(String string) throws JSONException {
      if (string == null) {
         throw new JSONException("Null key.");
      } else if (this.mode == 'k') {
         try {
            JSONObject topObject = this.stack[this.top - 1];
            if (topObject.has(string)) {
               throw new JSONException("Duplicate key \"" + string + "\"");
            } else {
               topObject.put(string, true);
               if (this.comma) {
                  this.writer.append(',');
               }

               this.writer.append(JSONObject.quote(string));
               this.writer.append(':');
               this.comma = false;
               this.mode = 'o';
               return this;
            }
         } catch (IOException var3) {
            throw new JSONException(var3);
         }
      } else {
         throw new JSONException("Misplaced key.");
      }
   }

   public JSONWriter object() throws JSONException {
      if (this.mode == 'i') {
         this.mode = 'o';
      }

      if (this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Misplaced object.");
      } else {
         this.append("{");
         this.push(new JSONObject());
         this.comma = false;
         return this;
      }
   }

   private void pop(char c) throws JSONException {
      if (this.top <= 0) {
         throw new JSONException("Nesting error.");
      } else {
         char m = this.stack[this.top - 1] == null ? 97 : 107;
         if (m != c) {
            throw new JSONException("Nesting error.");
         } else {
            --this.top;
            this.mode = (char)(this.top == 0 ? 100 : (this.stack[this.top - 1] == null ? 97 : 107));
         }
      }
   }

   private void push(JSONObject jo) throws JSONException {
      if (this.top >= 200) {
         throw new JSONException("Nesting too deep.");
      } else {
         this.stack[this.top] = jo;
         this.mode = (char)(jo == null ? 97 : 107);
         ++this.top;
      }
   }

   public static String valueToString(Object value) throws JSONException {
      if (value != null && !value.equals((Object)null)) {
         String object;
         if (value instanceof JSONString) {
            try {
               object = ((JSONString)value).toJSONString();
            } catch (Exception var3) {
               throw new JSONException(var3);
            }

            if (object != null) {
               return object;
            } else {
               throw new JSONException("Bad value from toJSONString: " + object);
            }
         } else if (value instanceof Number) {
            object = JSONObject.numberToString((Number)value);
            return JSONObject.NUMBER_PATTERN.matcher(object).matches() ? object : JSONObject.quote(object);
         } else if (!(value instanceof Boolean) && !(value instanceof JSONObject) && !(value instanceof JSONArray)) {
            if (value instanceof Map) {
               Map<?, ?> map = (Map)value;
               return (new JSONObject(map)).toString();
            } else if (value instanceof Collection) {
               Collection<?> coll = (Collection)value;
               return (new JSONArray(coll)).toString();
            } else if (value.getClass().isArray()) {
               return (new JSONArray(value)).toString();
            } else {
               return value instanceof Enum ? JSONObject.quote(((Enum)value).name()) : JSONObject.quote(value.toString());
            }
         } else {
            return value.toString();
         }
      } else {
         return "null";
      }
   }

   public JSONWriter value(boolean b) throws JSONException {
      return this.append(b ? "true" : "false");
   }

   public JSONWriter value(double d) throws JSONException {
      return this.value(d);
   }

   public JSONWriter value(long l) throws JSONException {
      return this.append(Long.toString(l));
   }

   public JSONWriter value(Object object) throws JSONException {
      return this.append(valueToString(object));
   }
}
