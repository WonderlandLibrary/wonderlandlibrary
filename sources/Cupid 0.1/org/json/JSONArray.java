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
    if (x.nextClean() != '[')
      throw x.syntaxError("A JSONArray text must start with '['"); 
    char nextChar = x.nextClean();
    if (nextChar == '\000')
      throw x.syntaxError("Expected a ',' or ']'"); 
    if (nextChar != ']') {
      x.back();
      while (true) {
        if (x.nextClean() == ',') {
          x.back();
          this.myArrayList.add(JSONObject.NULL);
        } else {
          x.back();
          this.myArrayList.add(x.nextValue());
        } 
        switch (x.nextClean()) {
          case '\000':
            throw x.syntaxError("Expected a ',' or ']'");
          case ',':
            nextChar = x.nextClean();
            if (nextChar == '\000')
              throw x.syntaxError("Expected a ',' or ']'"); 
            if (nextChar == ']')
              return; 
            x.back();
            continue;
          case ']':
            return;
        } 
        break;
      } 
      throw x.syntaxError("Expected a ',' or ']'");
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
      addAll(collection, true);
    } 
  }
  
  public JSONArray(Iterable<?> iter) {
    this();
    if (iter == null)
      return; 
    addAll(iter, true);
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
    if (!array.getClass().isArray())
      throw new JSONException("JSONArray initial value should be a string or collection or array."); 
    addAll(array, true);
  }
  
  public JSONArray(int initialCapacity) throws JSONException {
    if (initialCapacity < 0)
      throw new JSONException("JSONArray initial capacity cannot be negative."); 
    this.myArrayList = new ArrayList(initialCapacity);
  }
  
  public Iterator<Object> iterator() {
    return this.myArrayList.iterator();
  }
  
  public Object get(int index) throws JSONException {
    Object object = opt(index);
    if (object == null)
      throw new JSONException("JSONArray[" + index + "] not found."); 
    return object;
  }
  
  public boolean getBoolean(int index) throws JSONException {
    Object object = get(index);
    if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
      
      .equalsIgnoreCase("false")))
      return false; 
    if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
      
      .equalsIgnoreCase("true")))
      return true; 
    throw wrongValueFormatException(index, "boolean", null);
  }
  
  public double getDouble(int index) throws JSONException {
    Object object = get(index);
    if (object instanceof Number)
      return ((Number)object).doubleValue(); 
    try {
      return Double.parseDouble(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "double", e);
    } 
  }
  
  public float getFloat(int index) throws JSONException {
    Object object = get(index);
    if (object instanceof Number)
      return ((Number)object).floatValue(); 
    try {
      return Float.parseFloat(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "float", e);
    } 
  }
  
  public Number getNumber(int index) throws JSONException {
    Object object = get(index);
    try {
      if (object instanceof Number)
        return (Number)object; 
      return JSONObject.stringToNumber(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "number", e);
    } 
  }
  
  public <E extends Enum<E>> E getEnum(Class<E> clazz, int index) throws JSONException {
    E val = optEnum(clazz, index);
    if (val == null)
      throw wrongValueFormatException(index, "enum of type " + 
          JSONObject.quote(clazz.getSimpleName()), null); 
    return val;
  }
  
  public BigDecimal getBigDecimal(int index) throws JSONException {
    Object object = get(index);
    BigDecimal val = JSONObject.objectToBigDecimal(object, null);
    if (val == null)
      throw wrongValueFormatException(index, "BigDecimal", object, null); 
    return val;
  }
  
  public BigInteger getBigInteger(int index) throws JSONException {
    Object object = get(index);
    BigInteger val = JSONObject.objectToBigInteger(object, null);
    if (val == null)
      throw wrongValueFormatException(index, "BigInteger", object, null); 
    return val;
  }
  
  public int getInt(int index) throws JSONException {
    Object object = get(index);
    if (object instanceof Number)
      return ((Number)object).intValue(); 
    try {
      return Integer.parseInt(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "int", e);
    } 
  }
  
  public JSONArray getJSONArray(int index) throws JSONException {
    Object object = get(index);
    if (object instanceof JSONArray)
      return (JSONArray)object; 
    throw wrongValueFormatException(index, "JSONArray", null);
  }
  
  public JSONObject getJSONObject(int index) throws JSONException {
    Object object = get(index);
    if (object instanceof JSONObject)
      return (JSONObject)object; 
    throw wrongValueFormatException(index, "JSONObject", null);
  }
  
  public long getLong(int index) throws JSONException {
    Object object = get(index);
    if (object instanceof Number)
      return ((Number)object).longValue(); 
    try {
      return Long.parseLong(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "long", e);
    } 
  }
  
  public String getString(int index) throws JSONException {
    Object object = get(index);
    if (object instanceof String)
      return (String)object; 
    throw wrongValueFormatException(index, "String", null);
  }
  
  public boolean isNull(int index) {
    return JSONObject.NULL.equals(opt(index));
  }
  
  public String join(String separator) throws JSONException {
    int len = length();
    if (len == 0)
      return ""; 
    StringBuilder sb = new StringBuilder(JSONObject.valueToString(this.myArrayList.get(0)));
    for (int i = 1; i < len; i++)
      sb.append(separator)
        .append(JSONObject.valueToString(this.myArrayList.get(i))); 
    return sb.toString();
  }
  
  public int length() {
    return this.myArrayList.size();
  }
  
  public void clear() {
    this.myArrayList.clear();
  }
  
  public Object opt(int index) {
    return (index < 0 || index >= length()) ? null : this.myArrayList
      .get(index);
  }
  
  public boolean optBoolean(int index) {
    return optBoolean(index, false);
  }
  
  public boolean optBoolean(int index, boolean defaultValue) {
    try {
      return getBoolean(index);
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public double optDouble(int index) {
    return optDouble(index, Double.NaN);
  }
  
  public double optDouble(int index, double defaultValue) {
    Number val = optNumber(index, null);
    if (val == null)
      return defaultValue; 
    double doubleValue = val.doubleValue();
    return doubleValue;
  }
  
  public float optFloat(int index) {
    return optFloat(index, Float.NaN);
  }
  
  public float optFloat(int index, float defaultValue) {
    Number val = optNumber(index, null);
    if (val == null)
      return defaultValue; 
    float floatValue = val.floatValue();
    return floatValue;
  }
  
  public int optInt(int index) {
    return optInt(index, 0);
  }
  
  public int optInt(int index, int defaultValue) {
    Number val = optNumber(index, null);
    if (val == null)
      return defaultValue; 
    return val.intValue();
  }
  
  public <E extends Enum<E>> E optEnum(Class<E> clazz, int index) {
    return optEnum(clazz, index, null);
  }
  
  public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue) {
    try {
      Object val = opt(index);
      if (JSONObject.NULL.equals(val))
        return defaultValue; 
      if (clazz.isAssignableFrom(val.getClass()))
        return (E)val; 
      return Enum.valueOf(clazz, val.toString());
    } catch (IllegalArgumentException e) {
      return defaultValue;
    } catch (NullPointerException e) {
      return defaultValue;
    } 
  }
  
  public BigInteger optBigInteger(int index, BigInteger defaultValue) {
    Object val = opt(index);
    return JSONObject.objectToBigInteger(val, defaultValue);
  }
  
  public BigDecimal optBigDecimal(int index, BigDecimal defaultValue) {
    Object val = opt(index);
    return JSONObject.objectToBigDecimal(val, defaultValue);
  }
  
  public JSONArray optJSONArray(int index) {
    Object o = opt(index);
    return (o instanceof JSONArray) ? (JSONArray)o : null;
  }
  
  public JSONObject optJSONObject(int index) {
    Object o = opt(index);
    return (o instanceof JSONObject) ? (JSONObject)o : null;
  }
  
  public long optLong(int index) {
    return optLong(index, 0L);
  }
  
  public long optLong(int index, long defaultValue) {
    Number val = optNumber(index, null);
    if (val == null)
      return defaultValue; 
    return val.longValue();
  }
  
  public Number optNumber(int index) {
    return optNumber(index, null);
  }
  
  public Number optNumber(int index, Number defaultValue) {
    Object val = opt(index);
    if (JSONObject.NULL.equals(val))
      return defaultValue; 
    if (val instanceof Number)
      return (Number)val; 
    if (val instanceof String)
      try {
        return JSONObject.stringToNumber((String)val);
      } catch (Exception e) {
        return defaultValue;
      }  
    return defaultValue;
  }
  
  public String optString(int index) {
    return optString(index, "");
  }
  
  public String optString(int index, String defaultValue) {
    Object object = opt(index);
    return JSONObject.NULL.equals(object) ? defaultValue : object
      .toString();
  }
  
  public JSONArray put(boolean value) {
    return put(value ? Boolean.TRUE : Boolean.FALSE);
  }
  
  public JSONArray put(Collection<?> value) {
    return put(new JSONArray(value));
  }
  
  public JSONArray put(double value) throws JSONException {
    return put(Double.valueOf(value));
  }
  
  public JSONArray put(float value) throws JSONException {
    return put(Float.valueOf(value));
  }
  
  public JSONArray put(int value) {
    return put(Integer.valueOf(value));
  }
  
  public JSONArray put(long value) {
    return put(Long.valueOf(value));
  }
  
  public JSONArray put(Map<?, ?> value) {
    return put(new JSONObject(value));
  }
  
  public JSONArray put(Object value) {
    JSONObject.testValidity(value);
    this.myArrayList.add(value);
    return this;
  }
  
  public JSONArray put(int index, boolean value) throws JSONException {
    return put(index, value ? Boolean.TRUE : Boolean.FALSE);
  }
  
  public JSONArray put(int index, Collection<?> value) throws JSONException {
    return put(index, new JSONArray(value));
  }
  
  public JSONArray put(int index, double value) throws JSONException {
    return put(index, Double.valueOf(value));
  }
  
  public JSONArray put(int index, float value) throws JSONException {
    return put(index, Float.valueOf(value));
  }
  
  public JSONArray put(int index, int value) throws JSONException {
    return put(index, Integer.valueOf(value));
  }
  
  public JSONArray put(int index, long value) throws JSONException {
    return put(index, Long.valueOf(value));
  }
  
  public JSONArray put(int index, Map<?, ?> value) throws JSONException {
    put(index, new JSONObject(value));
    return this;
  }
  
  public JSONArray put(int index, Object value) throws JSONException {
    if (index < 0)
      throw new JSONException("JSONArray[" + index + "] not found."); 
    if (index < length()) {
      JSONObject.testValidity(value);
      this.myArrayList.set(index, value);
      return this;
    } 
    if (index == length())
      return put(value); 
    this.myArrayList.ensureCapacity(index + 1);
    while (index != length())
      this.myArrayList.add(JSONObject.NULL); 
    return put(value);
  }
  
  public JSONArray putAll(Collection<?> collection) {
    addAll(collection, false);
    return this;
  }
  
  public JSONArray putAll(Iterable<?> iter) {
    addAll(iter, false);
    return this;
  }
  
  public JSONArray putAll(JSONArray array) {
    this.myArrayList.addAll(array.myArrayList);
    return this;
  }
  
  public JSONArray putAll(Object array) throws JSONException {
    addAll(array, false);
    return this;
  }
  
  public Object query(String jsonPointer) {
    return query(new JSONPointer(jsonPointer));
  }
  
  public Object query(JSONPointer jsonPointer) {
    return jsonPointer.queryFrom(this);
  }
  
  public Object optQuery(String jsonPointer) {
    return optQuery(new JSONPointer(jsonPointer));
  }
  
  public Object optQuery(JSONPointer jsonPointer) {
    try {
      return jsonPointer.queryFrom(this);
    } catch (JSONPointerException e) {
      return null;
    } 
  }
  
  public Object remove(int index) {
    return (index >= 0 && index < length()) ? this.myArrayList
      .remove(index) : null;
  }
  
  public boolean similar(Object other) {
    if (!(other instanceof JSONArray))
      return false; 
    int len = length();
    if (len != ((JSONArray)other).length())
      return false; 
    for (int i = 0; i < len; i++) {
      Object valueThis = this.myArrayList.get(i);
      Object valueOther = ((JSONArray)other).myArrayList.get(i);
      if (valueThis != valueOther) {
        if (valueThis == null)
          return false; 
        if (valueThis instanceof JSONObject) {
          if (!((JSONObject)valueThis).similar(valueOther))
            return false; 
        } else if (valueThis instanceof JSONArray) {
          if (!((JSONArray)valueThis).similar(valueOther))
            return false; 
        } else if (valueThis instanceof Number && valueOther instanceof Number) {
          if (!JSONObject.isNumberSimilar((Number)valueThis, (Number)valueOther))
            return false; 
        } else if (!valueThis.equals(valueOther)) {
          return false;
        } 
      } 
    } 
    return true;
  }
  
  public JSONObject toJSONObject(JSONArray names) throws JSONException {
    if (names == null || names.isEmpty() || isEmpty())
      return null; 
    JSONObject jo = new JSONObject(names.length());
    for (int i = 0; i < names.length(); i++)
      jo.put(names.getString(i), opt(i)); 
    return jo;
  }
  
  public String toString() {
    try {
      return toString(0);
    } catch (Exception e) {
      return null;
    } 
  }
  
  public String toString(int indentFactor) throws JSONException {
    StringWriter sw = new StringWriter();
    synchronized (sw.getBuffer()) {
      return write(sw, indentFactor, 0).toString();
    } 
  }
  
  public Writer write(Writer writer) throws JSONException {
    return write(writer, 0, 0);
  }
  
  public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
    try {
      boolean needsComma = false;
      int length = length();
      writer.write(91);
      if (length == 1) {
        try {
          JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
        } catch (Exception e) {
          throw new JSONException("Unable to write JSONArray value at index: 0", e);
        } 
      } else if (length != 0) {
        int newIndent = indent + indentFactor;
        for (int i = 0; i < length; i++) {
          if (needsComma)
            writer.write(44); 
          if (indentFactor > 0)
            writer.write(10); 
          JSONObject.indent(writer, newIndent);
          try {
            JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newIndent);
          } catch (Exception e) {
            throw new JSONException("Unable to write JSONArray value at index: " + i, e);
          } 
          needsComma = true;
        } 
        if (indentFactor > 0)
          writer.write(10); 
        JSONObject.indent(writer, indent);
      } 
      writer.write(93);
      return writer;
    } catch (IOException e) {
      throw new JSONException(e);
    } 
  }
  
  public List<Object> toList() {
    List<Object> results = new ArrayList(this.myArrayList.size());
    for (Object element : this.myArrayList) {
      if (element == null || JSONObject.NULL.equals(element)) {
        results.add(null);
        continue;
      } 
      if (element instanceof JSONArray) {
        results.add(((JSONArray)element).toList());
        continue;
      } 
      if (element instanceof JSONObject) {
        results.add(((JSONObject)element).toMap());
        continue;
      } 
      results.add(element);
    } 
    return results;
  }
  
  public boolean isEmpty() {
    return this.myArrayList.isEmpty();
  }
  
  private void addAll(Collection<?> collection, boolean wrap) {
    this.myArrayList.ensureCapacity(this.myArrayList.size() + collection.size());
    if (wrap) {
      for (Object o : collection)
        put(JSONObject.wrap(o)); 
    } else {
      for (Object o : collection)
        put(o); 
    } 
  }
  
  private void addAll(Iterable<?> iter, boolean wrap) {
    if (wrap) {
      for (Object o : iter)
        put(JSONObject.wrap(o)); 
    } else {
      for (Object o : iter)
        put(o); 
    } 
  }
  
  private void addAll(Object array, boolean wrap) throws JSONException {
    if (array.getClass().isArray()) {
      int length = Array.getLength(array);
      this.myArrayList.ensureCapacity(this.myArrayList.size() + length);
      if (wrap) {
        for (int i = 0; i < length; i++)
          put(JSONObject.wrap(Array.get(array, i))); 
      } else {
        for (int i = 0; i < length; i++)
          put(Array.get(array, i)); 
      } 
    } else if (array instanceof JSONArray) {
      this.myArrayList.addAll(((JSONArray)array).myArrayList);
    } else if (array instanceof Collection) {
      addAll((Collection)array, wrap);
    } else if (array instanceof Iterable) {
      addAll((Iterable)array, wrap);
    } else {
      throw new JSONException("JSONArray initial value should be a string or collection or array.");
    } 
  }
  
  private static JSONException wrongValueFormatException(int idx, String valueType, Throwable cause) {
    return new JSONException("JSONArray[" + idx + "] is not a " + valueType + ".", cause);
  }
  
  private static JSONException wrongValueFormatException(int idx, String valueType, Object value, Throwable cause) {
    return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (" + value + ").", cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\JSONArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */