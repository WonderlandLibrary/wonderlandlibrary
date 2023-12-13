package org.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JSONPointer {
   private static final String ENCODING = "utf-8";
   private final List<String> refTokens;

   public static JSONPointer.Builder builder() {
      return new JSONPointer.Builder();
   }

   public JSONPointer(String pointer) {
      if (pointer == null) {
         throw new NullPointerException("pointer cannot be null");
      } else if (!pointer.isEmpty() && !pointer.equals("#")) {
         String refs;
         if (pointer.startsWith("#/")) {
            refs = pointer.substring(2);

            try {
               refs = URLDecoder.decode(refs, "utf-8");
            } catch (UnsupportedEncodingException var6) {
               throw new RuntimeException(var6);
            }
         } else {
            if (!pointer.startsWith("/")) {
               throw new IllegalArgumentException("a JSON pointer should start with '/' or '#/'");
            }

            refs = pointer.substring(1);
         }

         this.refTokens = new ArrayList();
         int slashIdx = -1;
         boolean var4 = false;

         do {
            int prevSlashIdx = slashIdx + 1;
            slashIdx = refs.indexOf(47, prevSlashIdx);
            if (prevSlashIdx != slashIdx && prevSlashIdx != refs.length()) {
               String token;
               if (slashIdx >= 0) {
                  token = refs.substring(prevSlashIdx, slashIdx);
                  this.refTokens.add(unescape(token));
               } else {
                  token = refs.substring(prevSlashIdx);
                  this.refTokens.add(unescape(token));
               }
            } else {
               this.refTokens.add("");
            }
         } while(slashIdx >= 0);

      } else {
         this.refTokens = Collections.emptyList();
      }
   }

   public JSONPointer(List<String> refTokens) {
      this.refTokens = new ArrayList(refTokens);
   }

   private static String unescape(String token) {
      return token.replace("~1", "/").replace("~0", "~").replace("\\\"", "\"").replace("\\\\", "\\");
   }

   public Object queryFrom(Object document) throws JSONPointerException {
      if (this.refTokens.isEmpty()) {
         return document;
      } else {
         Object current = document;
         Iterator var3 = this.refTokens.iterator();

         while(var3.hasNext()) {
            String token = (String)var3.next();
            if (current instanceof JSONObject) {
               current = ((JSONObject)current).opt(unescape(token));
            } else {
               if (!(current instanceof JSONArray)) {
                  throw new JSONPointerException(String.format("value [%s] is not an array or object therefore its key %s cannot be resolved", current, token));
               }

               current = readByIndexToken(current, token);
            }
         }

         return current;
      }
   }

   private static Object readByIndexToken(Object current, String indexToken) throws JSONPointerException {
      try {
         int index = Integer.parseInt(indexToken);
         JSONArray currentArr = (JSONArray)current;
         if (index >= currentArr.length()) {
            throw new JSONPointerException(String.format("index %s is out of bounds - the array has %d elements", indexToken, currentArr.length()));
         } else {
            try {
               return currentArr.get(index);
            } catch (JSONException var5) {
               throw new JSONPointerException("Error reading value at index position " + index, var5);
            }
         }
      } catch (NumberFormatException var6) {
         throw new JSONPointerException(String.format("%s is not an array index", indexToken), var6);
      }
   }

   public String toString() {
      StringBuilder rval = new StringBuilder("");
      Iterator var2 = this.refTokens.iterator();

      while(var2.hasNext()) {
         String token = (String)var2.next();
         rval.append('/').append(escape(token));
      }

      return rval.toString();
   }

   private static String escape(String token) {
      return token.replace("~", "~0").replace("/", "~1").replace("\\", "\\\\").replace("\"", "\\\"");
   }

   public String toURIFragment() {
      try {
         StringBuilder rval = new StringBuilder("#");
         Iterator var2 = this.refTokens.iterator();

         while(var2.hasNext()) {
            String token = (String)var2.next();
            rval.append('/').append(URLEncoder.encode(token, "utf-8"));
         }

         return rval.toString();
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }
   }

   public static class Builder {
      private final List<String> refTokens = new ArrayList();

      public JSONPointer build() {
         return new JSONPointer(this.refTokens);
      }

      public JSONPointer.Builder append(String token) {
         if (token == null) {
            throw new NullPointerException("token cannot be null");
         } else {
            this.refTokens.add(token);
            return this;
         }
      }

      public JSONPointer.Builder append(int arrayIndex) {
         this.refTokens.add(String.valueOf(arrayIndex));
         return this;
      }
   }
}
