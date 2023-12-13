package org.json;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

public class XML {
   public static final Character AMP = '&';
   public static final Character APOS = '\'';
   public static final Character BANG = '!';
   public static final Character EQ = '=';
   public static final Character GT = '>';
   public static final Character LT = '<';
   public static final Character QUEST = '?';
   public static final Character QUOT = '"';
   public static final Character SLASH = '/';
   public static final String NULL_ATTR = "xsi:nil";
   public static final String TYPE_ATTR = "xsi:type";

   private static Iterable<Integer> codePointIterator(final String string) {
      return new Iterable<Integer>() {
         public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
               private int nextIndex = 0;
               private int length = string.length();

               public boolean hasNext() {
                  return this.nextIndex < this.length;
               }

               public Integer next() {
                  int result = string.codePointAt(this.nextIndex);
                  this.nextIndex += Character.charCount(result);
                  return result;
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   public static String escape(String string) {
      StringBuilder sb = new StringBuilder(string.length());
      Iterator var2 = codePointIterator(string).iterator();

      while(var2.hasNext()) {
         int cp = (Integer)var2.next();
         switch(cp) {
         case 34:
            sb.append("&quot;");
            break;
         case 38:
            sb.append("&amp;");
            break;
         case 39:
            sb.append("&apos;");
            break;
         case 60:
            sb.append("&lt;");
            break;
         case 62:
            sb.append("&gt;");
            break;
         default:
            if (mustEscape(cp)) {
               sb.append("&#x");
               sb.append(Integer.toHexString(cp));
               sb.append(';');
            } else {
               sb.appendCodePoint(cp);
            }
         }
      }

      return sb.toString();
   }

   private static boolean mustEscape(int cp) {
      return Character.isISOControl(cp) && cp != 9 && cp != 10 && cp != 13 || (cp < 32 || cp > 55295) && (cp < 57344 || cp > 65533) && (cp < 65536 || cp > 1114111);
   }

   public static String unescape(String string) {
      StringBuilder sb = new StringBuilder(string.length());
      int i = 0;

      for(int length = string.length(); i < length; ++i) {
         char c = string.charAt(i);
         if (c == '&') {
            int semic = string.indexOf(59, i);
            if (semic > i) {
               String entity = string.substring(i + 1, semic);
               sb.append(XMLTokener.unescapeEntity(entity));
               i += entity.length() + 1;
            } else {
               sb.append(c);
            }
         } else {
            sb.append(c);
         }
      }

      return sb.toString();
   }

   public static void noSpace(String string) throws JSONException {
      int length = string.length();
      if (length == 0) {
         throw new JSONException("Empty string.");
      } else {
         for(int i = 0; i < length; ++i) {
            if (Character.isWhitespace(string.charAt(i))) {
               throw new JSONException("'" + string + "' contains a space character.");
            }
         }

      }
   }

   private static boolean parse(XMLTokener x, JSONObject context, String name, XMLParserConfiguration config) throws JSONException {
      JSONObject jsonObject = null;
      Object token = x.nextToken();
      String string;
      if (token == BANG) {
         char c = x.next();
         if (c == '-') {
            if (x.next() == '-') {
               x.skipPast("-->");
               return false;
            }

            x.back();
         } else if (c == '[') {
            token = x.nextToken();
            if ("CDATA".equals(token) && x.next() == '[') {
               string = x.nextCDATA();
               if (string.length() > 0) {
                  context.accumulate(config.getcDataTagName(), string);
               }

               return false;
            }

            throw x.syntaxError("Expected 'CDATA['");
         }

         int i = 1;

         do {
            token = x.nextMeta();
            if (token == null) {
               throw x.syntaxError("Missing '>' after '<!'.");
            }

            if (token == LT) {
               ++i;
            } else if (token == GT) {
               --i;
            }
         } while(i > 0);

         return false;
      } else if (token == QUEST) {
         x.skipPast("?>");
         return false;
      } else if (token == SLASH) {
         token = x.nextToken();
         if (name == null) {
            throw x.syntaxError("Mismatched close tag " + token);
         } else if (!token.equals(name)) {
            throw x.syntaxError("Mismatched " + name + " and " + token);
         } else if (x.nextToken() != GT) {
            throw x.syntaxError("Misshaped close tag");
         } else {
            return true;
         }
      } else if (token instanceof Character) {
         throw x.syntaxError("Misshaped tag");
      } else {
         String tagName = (String)token;
         token = null;
         jsonObject = new JSONObject();
         boolean nilAttributeFound = false;
         XMLXsiTypeConverter xmlXsiTypeConverter = null;

         while(true) {
            while(true) {
               if (token == null) {
                  token = x.nextToken();
               }

               if (!(token instanceof String)) {
                  if (token == SLASH) {
                     if (x.nextToken() != GT) {
                        throw x.syntaxError("Misshaped tag");
                     }

                     if (nilAttributeFound) {
                        context.accumulate(tagName, JSONObject.NULL);
                     } else if (jsonObject.length() > 0) {
                        context.accumulate(tagName, jsonObject);
                     } else {
                        context.accumulate(tagName, "");
                     }

                     return false;
                  }

                  if (token != GT) {
                     throw x.syntaxError("Misshaped tag");
                  }

                  while(true) {
                     token = x.nextContent();
                     if (token == null) {
                        if (tagName != null) {
                           throw x.syntaxError("Unclosed tag " + tagName);
                        }

                        return false;
                     }

                     if (token instanceof String) {
                        string = (String)token;
                        if (string.length() > 0) {
                           if (xmlXsiTypeConverter != null) {
                              jsonObject.accumulate(config.getcDataTagName(), stringToValue(string, xmlXsiTypeConverter));
                           } else {
                              jsonObject.accumulate(config.getcDataTagName(), config.isKeepStrings() ? string : stringToValue(string));
                           }
                        }
                     } else if (token == LT && parse(x, jsonObject, tagName, config)) {
                        if (jsonObject.length() == 0) {
                           context.accumulate(tagName, "");
                        } else if (jsonObject.length() == 1 && jsonObject.opt(config.getcDataTagName()) != null) {
                           context.accumulate(tagName, jsonObject.opt(config.getcDataTagName()));
                        } else {
                           context.accumulate(tagName, jsonObject);
                        }

                        return false;
                     }
                  }
               }

               string = (String)token;
               token = x.nextToken();
               if (token == EQ) {
                  token = x.nextToken();
                  if (!(token instanceof String)) {
                     throw x.syntaxError("Missing value");
                  }

                  if (config.isConvertNilAttributeToNull() && "xsi:nil".equals(string) && Boolean.parseBoolean((String)token)) {
                     nilAttributeFound = true;
                  } else if (config.getXsiTypeMap() != null && !config.getXsiTypeMap().isEmpty() && "xsi:type".equals(string)) {
                     xmlXsiTypeConverter = (XMLXsiTypeConverter)config.getXsiTypeMap().get(token);
                  } else if (!nilAttributeFound) {
                     jsonObject.accumulate(string, config.isKeepStrings() ? (String)token : stringToValue((String)token));
                  }

                  token = null;
               } else {
                  jsonObject.accumulate(string, "");
               }
            }
         }
      }
   }

   public static Object stringToValue(String string, XMLXsiTypeConverter<?> typeConverter) {
      return typeConverter != null ? typeConverter.convert(string) : stringToValue(string);
   }

   public static Object stringToValue(String string) {
      if ("".equals(string)) {
         return string;
      } else if ("true".equalsIgnoreCase(string)) {
         return Boolean.TRUE;
      } else if ("false".equalsIgnoreCase(string)) {
         return Boolean.FALSE;
      } else if ("null".equalsIgnoreCase(string)) {
         return JSONObject.NULL;
      } else {
         char initial = string.charAt(0);
         if (initial >= '0' && initial <= '9' || initial == '-') {
            try {
               return stringToNumber(string);
            } catch (Exception var3) {
            }
         }

         return string;
      }
   }

   private static Number stringToNumber(String val) throws NumberFormatException {
      char initial = val.charAt(0);
      if ((initial < '0' || initial > '9') && initial != '-') {
         throw new NumberFormatException("val [" + val + "] is not a valid number.");
      } else if (isDecimalNotation(val)) {
         try {
            BigDecimal bd = new BigDecimal(val);
            return (Number)(initial == '-' && BigDecimal.ZERO.compareTo(bd) == 0 ? -0.0D : bd);
         } catch (NumberFormatException var5) {
            try {
               Double d = Double.valueOf(val);
               if (!d.isNaN() && !d.isInfinite()) {
                  return d;
               } else {
                  throw new NumberFormatException("val [" + val + "] is not a valid number.");
               }
            } catch (NumberFormatException var4) {
               throw new NumberFormatException("val [" + val + "] is not a valid number.");
            }
         }
      } else {
         char at1;
         if (initial == '0' && val.length() > 1) {
            at1 = val.charAt(1);
            if (at1 >= '0' && at1 <= '9') {
               throw new NumberFormatException("val [" + val + "] is not a valid number.");
            }
         } else if (initial == '-' && val.length() > 2) {
            at1 = val.charAt(1);
            char at2 = val.charAt(2);
            if (at1 == '0' && at2 >= '0' && at2 <= '9') {
               throw new NumberFormatException("val [" + val + "] is not a valid number.");
            }
         }

         BigInteger bi = new BigInteger(val);
         if (bi.bitLength() <= 31) {
            return bi.intValue();
         } else {
            return (Number)(bi.bitLength() <= 63 ? bi.longValue() : bi);
         }
      }
   }

   private static boolean isDecimalNotation(String val) {
      return val.indexOf(46) > -1 || val.indexOf(101) > -1 || val.indexOf(69) > -1 || "-0".equals(val);
   }

   public static JSONObject toJSONObject(String string) throws JSONException {
      return toJSONObject(string, XMLParserConfiguration.ORIGINAL);
   }

   public static JSONObject toJSONObject(Reader reader) throws JSONException {
      return toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
   }

   public static JSONObject toJSONObject(Reader reader, boolean keepStrings) throws JSONException {
      return keepStrings ? toJSONObject(reader, XMLParserConfiguration.KEEP_STRINGS) : toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
   }

   public static JSONObject toJSONObject(Reader reader, XMLParserConfiguration config) throws JSONException {
      JSONObject jo = new JSONObject();
      XMLTokener x = new XMLTokener(reader);

      while(x.more()) {
         x.skipPast("<");
         if (x.more()) {
            parse(x, jo, (String)null, config);
         }
      }

      return jo;
   }

   public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
      return toJSONObject((Reader)(new StringReader(string)), keepStrings);
   }

   public static JSONObject toJSONObject(String string, XMLParserConfiguration config) throws JSONException {
      return toJSONObject((Reader)(new StringReader(string)), config);
   }

   public static String toString(Object object) throws JSONException {
      return toString(object, (String)null, XMLParserConfiguration.ORIGINAL);
   }

   public static String toString(Object object, String tagName) {
      return toString(object, tagName, XMLParserConfiguration.ORIGINAL);
   }

   public static String toString(Object object, String tagName, XMLParserConfiguration config) throws JSONException {
      StringBuilder sb = new StringBuilder();
      JSONArray ja;
      Object value;
      if (object instanceof JSONObject) {
         if (tagName != null) {
            sb.append('<');
            sb.append(tagName);
            sb.append('>');
         }

         JSONObject jo = (JSONObject)object;
         Iterator var13 = jo.keySet().iterator();

         while(true) {
            while(true) {
               while(var13.hasNext()) {
                  String key = (String)var13.next();
                  value = jo.opt(key);
                  if (value == null) {
                     value = "";
                  } else if (value.getClass().isArray()) {
                     value = new JSONArray(value);
                  }

                  int jaLength;
                  int i;
                  Object val;
                  if (key.equals(config.getcDataTagName())) {
                     if (value instanceof JSONArray) {
                        ja = (JSONArray)value;
                        jaLength = ja.length();

                        for(i = 0; i < jaLength; ++i) {
                           if (i > 0) {
                              sb.append('\n');
                           }

                           val = ja.opt(i);
                           sb.append(escape(val.toString()));
                        }
                     } else {
                        sb.append(escape(value.toString()));
                     }
                  } else if (value instanceof JSONArray) {
                     ja = (JSONArray)value;
                     jaLength = ja.length();

                     for(i = 0; i < jaLength; ++i) {
                        val = ja.opt(i);
                        if (val instanceof JSONArray) {
                           sb.append('<');
                           sb.append(key);
                           sb.append('>');
                           sb.append(toString(val, (String)null, config));
                           sb.append("</");
                           sb.append(key);
                           sb.append('>');
                        } else {
                           sb.append(toString(val, key, config));
                        }
                     }
                  } else if ("".equals(value)) {
                     sb.append('<');
                     sb.append(key);
                     sb.append("/>");
                  } else {
                     sb.append(toString(value, key, config));
                  }
               }

               if (tagName != null) {
                  sb.append("</");
                  sb.append(tagName);
                  sb.append('>');
               }

               return sb.toString();
            }
         }
      } else if (object != null && (object instanceof JSONArray || object.getClass().isArray())) {
         if (object.getClass().isArray()) {
            ja = new JSONArray(object);
         } else {
            ja = (JSONArray)object;
         }

         int jaLength = ja.length();

         for(int i = 0; i < jaLength; ++i) {
            value = ja.opt(i);
            sb.append(toString(value, tagName == null ? "array" : tagName, config));
         }

         return sb.toString();
      } else {
         String string = object == null ? "null" : escape(object.toString());
         return tagName == null ? "\"" + string + "\"" : (string.length() == 0 ? "<" + tagName + "/>" : "<" + tagName + ">" + string + "</" + tagName + ">");
      }
   }
}
