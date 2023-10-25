package org.json;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

public class XML {
  public static final Character AMP = Character.valueOf('&');
  
  public static final Character APOS = Character.valueOf('\'');
  
  public static final Character BANG = Character.valueOf('!');
  
  public static final Character EQ = Character.valueOf('=');
  
  public static final Character GT = Character.valueOf('>');
  
  public static final Character LT = Character.valueOf('<');
  
  public static final Character QUEST = Character.valueOf('?');
  
  public static final Character QUOT = Character.valueOf('"');
  
  public static final Character SLASH = Character.valueOf('/');
  
  public static final String NULL_ATTR = "xsi:nil";
  
  public static final String TYPE_ATTR = "xsi:type";
  
  private static Iterable<Integer> codePointIterator(final String string) {
    return new Iterable<Integer>() {
        public Iterator<Integer> iterator() {
          return new Iterator<Integer>() {
              private int nextIndex = 0;
              
              private int length = string.length();
              
              public boolean hasNext() {
                return (this.nextIndex < this.length);
              }
              
              public Integer next() {
                int result = string.codePointAt(this.nextIndex);
                this.nextIndex += Character.charCount(result);
                return Integer.valueOf(result);
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
    for (Iterator<Integer> iterator = codePointIterator(string).iterator(); iterator.hasNext(); ) {
      int cp = ((Integer)iterator.next()).intValue();
      switch (cp) {
        case 38:
          sb.append("&amp;");
          continue;
        case 60:
          sb.append("&lt;");
          continue;
        case 62:
          sb.append("&gt;");
          continue;
        case 34:
          sb.append("&quot;");
          continue;
        case 39:
          sb.append("&apos;");
          continue;
      } 
      if (mustEscape(cp)) {
        sb.append("&#x");
        sb.append(Integer.toHexString(cp));
        sb.append(';');
        continue;
      } 
      sb.appendCodePoint(cp);
    } 
    return sb.toString();
  }
  
  private static boolean mustEscape(int cp) {
    return ((Character.isISOControl(cp) && cp != 9 && cp != 10 && cp != 13) || ((cp < 32 || cp > 55295) && (cp < 57344 || cp > 65533) && (cp < 65536 || cp > 1114111)));
  }
  
  public static String unescape(String string) {
    StringBuilder sb = new StringBuilder(string.length());
    for (int i = 0, length = string.length(); i < length; i++) {
      char c = string.charAt(i);
      if (c == '&') {
        int semic = string.indexOf(';', i);
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
    if (length == 0)
      throw new JSONException("Empty string."); 
    for (int i = 0; i < length; i++) {
      if (Character.isWhitespace(string.charAt(i)))
        throw new JSONException("'" + string + "' contains a space character."); 
    } 
  }
  
  private static boolean parse(XMLTokener x, JSONObject context, String name, XMLParserConfiguration config) throws JSONException {
    JSONObject jsonObject = null;
    Object token = x.nextToken();
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
        if ("CDATA".equals(token) && 
          x.next() == '[') {
          String string = x.nextCDATA();
          if (string.length() > 0)
            context.accumulate(config.getcDataTagName(), string); 
          return false;
        } 
        throw x.syntaxError("Expected 'CDATA['");
      } 
      int i = 1;
      while (true) {
        token = x.nextMeta();
        if (token == null)
          throw x.syntaxError("Missing '>' after '<!'."); 
        if (token == LT) {
          i++;
        } else if (token == GT) {
          i--;
        } 
        if (i <= 0)
          return false; 
      } 
    } 
    if (token == QUEST) {
      x.skipPast("?>");
      return false;
    } 
    if (token == SLASH) {
      token = x.nextToken();
      if (name == null)
        throw x.syntaxError("Mismatched close tag " + token); 
      if (!token.equals(name))
        throw x.syntaxError("Mismatched " + name + " and " + token); 
      if (x.nextToken() != GT)
        throw x.syntaxError("Misshaped close tag"); 
      return true;
    } 
    if (token instanceof Character)
      throw x.syntaxError("Misshaped tag"); 
    String tagName = (String)token;
    token = null;
    jsonObject = new JSONObject();
    boolean nilAttributeFound = false;
    XMLXsiTypeConverter<?> xmlXsiTypeConverter = null;
    while (true) {
      if (token == null)
        token = x.nextToken(); 
      if (token instanceof String) {
        String string = (String)token;
        token = x.nextToken();
        if (token == EQ) {
          token = x.nextToken();
          if (!(token instanceof String))
            throw x.syntaxError("Missing value"); 
          if (config.isConvertNilAttributeToNull() && "xsi:nil"
            .equals(string) && 
            Boolean.parseBoolean((String)token)) {
            nilAttributeFound = true;
          } else if (config.getXsiTypeMap() != null && !config.getXsiTypeMap().isEmpty() && "xsi:type"
            .equals(string)) {
            xmlXsiTypeConverter = config.getXsiTypeMap().get(token);
          } else if (!nilAttributeFound) {
            jsonObject.accumulate(string, 
                config.isKeepStrings() ? token : 
                
                stringToValue((String)token));
          } 
          token = null;
          continue;
        } 
        jsonObject.accumulate(string, "");
        continue;
      } 
      break;
    } 
    if (token == SLASH) {
      if (x.nextToken() != GT)
        throw x.syntaxError("Misshaped tag"); 
      if (config.getForceList().contains(tagName)) {
        if (nilAttributeFound) {
          context.append(tagName, JSONObject.NULL);
        } else if (jsonObject.length() > 0) {
          context.append(tagName, jsonObject);
        } else {
          context.put(tagName, new JSONArray());
        } 
      } else if (nilAttributeFound) {
        context.accumulate(tagName, JSONObject.NULL);
      } else if (jsonObject.length() > 0) {
        context.accumulate(tagName, jsonObject);
      } else {
        context.accumulate(tagName, "");
      } 
      return false;
    } 
    if (token == GT)
      while (true) {
        token = x.nextContent();
        if (token == null) {
          if (tagName != null)
            throw x.syntaxError("Unclosed tag " + tagName); 
          return false;
        } 
        if (token instanceof String) {
          String string = (String)token;
          if (string.length() > 0) {
            if (xmlXsiTypeConverter != null) {
              jsonObject.accumulate(config.getcDataTagName(), 
                  stringToValue(string, xmlXsiTypeConverter));
              continue;
            } 
            jsonObject.accumulate(config.getcDataTagName(), 
                config.isKeepStrings() ? string : stringToValue(string));
          } 
          continue;
        } 
        if (token == LT)
          if (parse(x, jsonObject, tagName, config)) {
            if (config.getForceList().contains(tagName)) {
              if (jsonObject.length() == 0) {
                context.put(tagName, new JSONArray());
              } else if (jsonObject.length() == 1 && jsonObject
                .opt(config.getcDataTagName()) != null) {
                context.append(tagName, jsonObject.opt(config.getcDataTagName()));
              } else {
                context.append(tagName, jsonObject);
              } 
            } else if (jsonObject.length() == 0) {
              context.accumulate(tagName, "");
            } else if (jsonObject.length() == 1 && jsonObject
              .opt(config.getcDataTagName()) != null) {
              context.accumulate(tagName, jsonObject.opt(config.getcDataTagName()));
            } else {
              context.accumulate(tagName, jsonObject);
            } 
            return false;
          }  
      }  
    throw x.syntaxError("Misshaped tag");
  }
  
  public static Object stringToValue(String string, XMLXsiTypeConverter<?> typeConverter) {
    if (typeConverter != null)
      return typeConverter.convert(string); 
    return stringToValue(string);
  }
  
  public static Object stringToValue(String string) {
    if ("".equals(string))
      return string; 
    if ("true".equalsIgnoreCase(string))
      return Boolean.TRUE; 
    if ("false".equalsIgnoreCase(string))
      return Boolean.FALSE; 
    if ("null".equalsIgnoreCase(string))
      return JSONObject.NULL; 
    char initial = string.charAt(0);
    if ((initial >= '0' && initial <= '9') || initial == '-')
      try {
        return stringToNumber(string);
      } catch (Exception exception) {} 
    return string;
  }
  
  private static Number stringToNumber(String val) throws NumberFormatException {
    char initial = val.charAt(0);
    if ((initial >= '0' && initial <= '9') || initial == '-') {
      if (isDecimalNotation(val))
        try {
          BigDecimal bd = new BigDecimal(val);
          if (initial == '-' && BigDecimal.ZERO.compareTo(bd) == 0)
            return Double.valueOf(-0.0D); 
          return bd;
        } catch (NumberFormatException retryAsDouble) {
          try {
            Double d = Double.valueOf(val);
            if (d.isNaN() || d.isInfinite())
              throw new NumberFormatException("val [" + val + "] is not a valid number."); 
            return d;
          } catch (NumberFormatException ignore) {
            throw new NumberFormatException("val [" + val + "] is not a valid number.");
          } 
        }  
      if (initial == '0' && val.length() > 1) {
        char at1 = val.charAt(1);
        if (at1 >= '0' && at1 <= '9')
          throw new NumberFormatException("val [" + val + "] is not a valid number."); 
      } else if (initial == '-' && val.length() > 2) {
        char at1 = val.charAt(1);
        char at2 = val.charAt(2);
        if (at1 == '0' && at2 >= '0' && at2 <= '9')
          throw new NumberFormatException("val [" + val + "] is not a valid number."); 
      } 
      BigInteger bi = new BigInteger(val);
      if (bi.bitLength() <= 31)
        return Integer.valueOf(bi.intValue()); 
      if (bi.bitLength() <= 63)
        return Long.valueOf(bi.longValue()); 
      return bi;
    } 
    throw new NumberFormatException("val [" + val + "] is not a valid number.");
  }
  
  private static boolean isDecimalNotation(String val) {
    return (val.indexOf('.') > -1 || val.indexOf('e') > -1 || val
      .indexOf('E') > -1 || "-0".equals(val));
  }
  
  public static JSONObject toJSONObject(String string) throws JSONException {
    return toJSONObject(string, XMLParserConfiguration.ORIGINAL);
  }
  
  public static JSONObject toJSONObject(Reader reader) throws JSONException {
    return toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
  }
  
  public static JSONObject toJSONObject(Reader reader, boolean keepStrings) throws JSONException {
    if (keepStrings)
      return toJSONObject(reader, XMLParserConfiguration.KEEP_STRINGS); 
    return toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
  }
  
  public static JSONObject toJSONObject(Reader reader, XMLParserConfiguration config) throws JSONException {
    JSONObject jo = new JSONObject();
    XMLTokener x = new XMLTokener(reader);
    while (x.more()) {
      x.skipPast("<");
      if (x.more())
        parse(x, jo, null, config); 
    } 
    return jo;
  }
  
  public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
    return toJSONObject(new StringReader(string), keepStrings);
  }
  
  public static JSONObject toJSONObject(String string, XMLParserConfiguration config) throws JSONException {
    return toJSONObject(new StringReader(string), config);
  }
  
  public static String toString(Object object) throws JSONException {
    return toString(object, null, XMLParserConfiguration.ORIGINAL);
  }
  
  public static String toString(Object object, String tagName) {
    return toString(object, tagName, XMLParserConfiguration.ORIGINAL);
  }
  
  public static String toString(Object object, String tagName, XMLParserConfiguration config) throws JSONException {
    StringBuilder sb = new StringBuilder();
    if (object instanceof JSONObject) {
      if (tagName != null) {
        sb.append('<');
        sb.append(tagName);
        sb.append('>');
      } 
      JSONObject jo = (JSONObject)object;
      for (String key : jo.keySet()) {
        Object value = jo.opt(key);
        if (value == null) {
          value = "";
        } else if (value.getClass().isArray()) {
          value = new JSONArray(value);
        } 
        if (key.equals(config.getcDataTagName())) {
          if (value instanceof JSONArray) {
            JSONArray ja = (JSONArray)value;
            int jaLength = ja.length();
            for (int i = 0; i < jaLength; i++) {
              if (i > 0)
                sb.append('\n'); 
              Object val = ja.opt(i);
              sb.append(escape(val.toString()));
            } 
            continue;
          } 
          sb.append(escape(value.toString()));
          continue;
        } 
        if (value instanceof JSONArray) {
          JSONArray ja = (JSONArray)value;
          int jaLength = ja.length();
          for (int i = 0; i < jaLength; i++) {
            Object val = ja.opt(i);
            if (val instanceof JSONArray) {
              sb.append('<');
              sb.append(key);
              sb.append('>');
              sb.append(toString(val, null, config));
              sb.append("</");
              sb.append(key);
              sb.append('>');
            } else {
              sb.append(toString(val, key, config));
            } 
          } 
          continue;
        } 
        if ("".equals(value)) {
          sb.append('<');
          sb.append(key);
          sb.append("/>");
          continue;
        } 
        sb.append(toString(value, key, config));
      } 
      if (tagName != null) {
        sb.append("</");
        sb.append(tagName);
        sb.append('>');
      } 
      return sb.toString();
    } 
    if (object != null && (object instanceof JSONArray || object.getClass().isArray())) {
      JSONArray ja;
      if (object.getClass().isArray()) {
        ja = new JSONArray(object);
      } else {
        ja = (JSONArray)object;
      } 
      int jaLength = ja.length();
      for (int i = 0; i < jaLength; i++) {
        Object val = ja.opt(i);
        sb.append(toString(val, (tagName == null) ? "array" : tagName, config));
      } 
      return sb.toString();
    } 
    String string = (object == null) ? "null" : escape(object.toString());
    return (tagName == null) ? ("\"" + string + "\"") : (
      (string.length() == 0) ? ("<" + tagName + "/>") : ("<" + tagName + ">" + string + "</" + tagName + ">"));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\XML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */