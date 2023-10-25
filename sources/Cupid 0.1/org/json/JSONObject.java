package org.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

public class JSONObject {
  private static final class Null {
    private Null() {}
    
    protected final Object clone() {
      return this;
    }
    
    public boolean equals(Object object) {
      return (object == null || object == this);
    }
    
    public int hashCode() {
      return 0;
    }
    
    public String toString() {
      return "null";
    }
  }
  
  static final Pattern NUMBER_PATTERN = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");
  
  private final Map<String, Object> map;
  
  public static final Object NULL = new Null();
  
  public JSONObject() {
    this.map = new HashMap<String, Object>();
  }
  
  public JSONObject(JSONObject jo, String... names) {
    this(names.length);
    for (int i = 0; i < names.length; i++) {
      try {
        putOnce(names[i], jo.opt(names[i]));
      } catch (Exception exception) {}
    } 
  }
  
  public JSONObject(JSONTokener x) throws JSONException {
    this();
    if (x.nextClean() != '{')
      throw x.syntaxError("A JSONObject text must begin with '{'"); 
    while (true) {
      char c = x.nextClean();
      switch (c) {
        case '\000':
          throw x.syntaxError("A JSONObject text must end with '}'");
        case '}':
          return;
      } 
      x.back();
      String key = x.nextValue().toString();
      c = x.nextClean();
      if (c != ':')
        throw x.syntaxError("Expected a ':' after a key"); 
      if (key != null) {
        if (opt(key) != null)
          throw x.syntaxError("Duplicate key \"" + key + "\""); 
        Object value = x.nextValue();
        if (value != null)
          put(key, value); 
      } 
      switch (x.nextClean()) {
        case ',':
        case ';':
          if (x.nextClean() == '}')
            return; 
          x.back();
          continue;
        case '}':
          return;
      } 
      break;
    } 
    throw x.syntaxError("Expected a ',' or '}'");
  }
  
  public JSONObject(Map<?, ?> m) {
    if (m == null) {
      this.map = new HashMap<String, Object>();
    } else {
      this.map = new HashMap<String, Object>(m.size());
      for (Map.Entry<?, ?> e : m.entrySet()) {
        if (e.getKey() == null)
          throw new NullPointerException("Null key."); 
        Object value = e.getValue();
        if (value != null)
          this.map.put(String.valueOf(e.getKey()), wrap(value)); 
      } 
    } 
  }
  
  public JSONObject(Object bean) {
    this();
    populateMap(bean);
  }
  
  private JSONObject(Object bean, Set<Object> objectsRecord) {
    this();
    populateMap(bean, objectsRecord);
  }
  
  public JSONObject(Object object, String... names) {
    this(names.length);
    Class<?> c = object.getClass();
    for (int i = 0; i < names.length; i++) {
      String name = names[i];
      try {
        putOpt(name, c.getField(name).get(object));
      } catch (Exception exception) {}
    } 
  }
  
  public JSONObject(String source) throws JSONException {
    this(new JSONTokener(source));
  }
  
  public JSONObject(String baseName, Locale locale) throws JSONException {
    this();
    ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, 
        Thread.currentThread().getContextClassLoader());
    Enumeration<String> keys = bundle.getKeys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      if (key != null) {
        String[] path = ((String)key).split("\\.");
        int last = path.length - 1;
        JSONObject target = this;
        for (int i = 0; i < last; i++) {
          String segment = path[i];
          JSONObject nextTarget = target.optJSONObject(segment);
          if (nextTarget == null) {
            nextTarget = new JSONObject();
            target.put(segment, nextTarget);
          } 
          target = nextTarget;
        } 
        target.put(path[last], bundle.getString((String)key));
      } 
    } 
  }
  
  protected JSONObject(int initialCapacity) {
    this.map = new HashMap<String, Object>(initialCapacity);
  }
  
  public JSONObject accumulate(String key, Object value) throws JSONException {
    testValidity(value);
    Object object = opt(key);
    if (object == null) {
      put(key, (value instanceof JSONArray) ? (new JSONArray())
          .put(value) : value);
    } else if (object instanceof JSONArray) {
      ((JSONArray)object).put(value);
    } else {
      put(key, (new JSONArray()).put(object).put(value));
    } 
    return this;
  }
  
  public JSONObject append(String key, Object value) throws JSONException {
    testValidity(value);
    Object object = opt(key);
    if (object == null) {
      put(key, (new JSONArray()).put(value));
    } else if (object instanceof JSONArray) {
      put(key, ((JSONArray)object).put(value));
    } else {
      throw wrongValueFormatException(key, "JSONArray", null, null);
    } 
    return this;
  }
  
  public static String doubleToString(double d) {
    if (Double.isInfinite(d) || Double.isNaN(d))
      return "null"; 
    String string = Double.toString(d);
    if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
      .indexOf('E') < 0) {
      while (string.endsWith("0"))
        string = string.substring(0, string.length() - 1); 
      if (string.endsWith("."))
        string = string.substring(0, string.length() - 1); 
    } 
    return string;
  }
  
  public Object get(String key) throws JSONException {
    if (key == null)
      throw new JSONException("Null key."); 
    Object object = opt(key);
    if (object == null)
      throw new JSONException("JSONObject[" + quote(key) + "] not found."); 
    return object;
  }
  
  public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) throws JSONException {
    E val = optEnum(clazz, key);
    if (val == null)
      throw wrongValueFormatException(key, "enum of type " + quote(clazz.getSimpleName()), null); 
    return val;
  }
  
  public boolean getBoolean(String key) throws JSONException {
    Object object = get(key);
    if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
      
      .equalsIgnoreCase("false")))
      return false; 
    if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
      
      .equalsIgnoreCase("true")))
      return true; 
    throw wrongValueFormatException(key, "Boolean", null);
  }
  
  public BigInteger getBigInteger(String key) throws JSONException {
    Object object = get(key);
    BigInteger ret = objectToBigInteger(object, null);
    if (ret != null)
      return ret; 
    throw wrongValueFormatException(key, "BigInteger", object, null);
  }
  
  public BigDecimal getBigDecimal(String key) throws JSONException {
    Object object = get(key);
    BigDecimal ret = objectToBigDecimal(object, null);
    if (ret != null)
      return ret; 
    throw wrongValueFormatException(key, "BigDecimal", object, null);
  }
  
  public double getDouble(String key) throws JSONException {
    Object object = get(key);
    if (object instanceof Number)
      return ((Number)object).doubleValue(); 
    try {
      return Double.parseDouble(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(key, "double", e);
    } 
  }
  
  public float getFloat(String key) throws JSONException {
    Object object = get(key);
    if (object instanceof Number)
      return ((Number)object).floatValue(); 
    try {
      return Float.parseFloat(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(key, "float", e);
    } 
  }
  
  public Number getNumber(String key) throws JSONException {
    Object object = get(key);
    try {
      if (object instanceof Number)
        return (Number)object; 
      return stringToNumber(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(key, "number", e);
    } 
  }
  
  public int getInt(String key) throws JSONException {
    Object object = get(key);
    if (object instanceof Number)
      return ((Number)object).intValue(); 
    try {
      return Integer.parseInt(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(key, "int", e);
    } 
  }
  
  public JSONArray getJSONArray(String key) throws JSONException {
    Object object = get(key);
    if (object instanceof JSONArray)
      return (JSONArray)object; 
    throw wrongValueFormatException(key, "JSONArray", null);
  }
  
  public JSONObject getJSONObject(String key) throws JSONException {
    Object object = get(key);
    if (object instanceof JSONObject)
      return (JSONObject)object; 
    throw wrongValueFormatException(key, "JSONObject", null);
  }
  
  public long getLong(String key) throws JSONException {
    Object object = get(key);
    if (object instanceof Number)
      return ((Number)object).longValue(); 
    try {
      return Long.parseLong(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(key, "long", e);
    } 
  }
  
  public static String[] getNames(JSONObject jo) {
    if (jo.isEmpty())
      return null; 
    return jo.keySet().<String>toArray(new String[jo.length()]);
  }
  
  public static String[] getNames(Object object) {
    if (object == null)
      return null; 
    Class<?> klass = object.getClass();
    Field[] fields = klass.getFields();
    int length = fields.length;
    if (length == 0)
      return null; 
    String[] names = new String[length];
    for (int i = 0; i < length; i++)
      names[i] = fields[i].getName(); 
    return names;
  }
  
  public String getString(String key) throws JSONException {
    Object object = get(key);
    if (object instanceof String)
      return (String)object; 
    throw wrongValueFormatException(key, "string", null);
  }
  
  public boolean has(String key) {
    return this.map.containsKey(key);
  }
  
  public JSONObject increment(String key) throws JSONException {
    Object value = opt(key);
    if (value == null) {
      put(key, 1);
    } else if (value instanceof Integer) {
      put(key, ((Integer)value).intValue() + 1);
    } else if (value instanceof Long) {
      put(key, ((Long)value).longValue() + 1L);
    } else if (value instanceof BigInteger) {
      put(key, ((BigInteger)value).add(BigInteger.ONE));
    } else if (value instanceof Float) {
      put(key, ((Float)value).floatValue() + 1.0F);
    } else if (value instanceof Double) {
      put(key, ((Double)value).doubleValue() + 1.0D);
    } else if (value instanceof BigDecimal) {
      put(key, ((BigDecimal)value).add(BigDecimal.ONE));
    } else {
      throw new JSONException("Unable to increment [" + quote(key) + "].");
    } 
    return this;
  }
  
  public boolean isNull(String key) {
    return NULL.equals(opt(key));
  }
  
  public Iterator<String> keys() {
    return keySet().iterator();
  }
  
  public Set<String> keySet() {
    return this.map.keySet();
  }
  
  protected Set<Map.Entry<String, Object>> entrySet() {
    return this.map.entrySet();
  }
  
  public int length() {
    return this.map.size();
  }
  
  public void clear() {
    this.map.clear();
  }
  
  public boolean isEmpty() {
    return this.map.isEmpty();
  }
  
  public JSONArray names() {
    if (this.map.isEmpty())
      return null; 
    return new JSONArray(this.map.keySet());
  }
  
  public static String numberToString(Number number) throws JSONException {
    if (number == null)
      throw new JSONException("Null pointer"); 
    testValidity(number);
    String string = number.toString();
    if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
      .indexOf('E') < 0) {
      while (string.endsWith("0"))
        string = string.substring(0, string.length() - 1); 
      if (string.endsWith("."))
        string = string.substring(0, string.length() - 1); 
    } 
    return string;
  }
  
  public Object opt(String key) {
    return (key == null) ? null : this.map.get(key);
  }
  
  public <E extends Enum<E>> E optEnum(Class<E> clazz, String key) {
    return optEnum(clazz, key, null);
  }
  
  public <E extends Enum<E>> E optEnum(Class<E> clazz, String key, E defaultValue) {
    try {
      Object val = opt(key);
      if (NULL.equals(val))
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
  
  public boolean optBoolean(String key) {
    return optBoolean(key, false);
  }
  
  public boolean optBoolean(String key, boolean defaultValue) {
    Object val = opt(key);
    if (NULL.equals(val))
      return defaultValue; 
    if (val instanceof Boolean)
      return ((Boolean)val).booleanValue(); 
    try {
      return getBoolean(key);
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public BigDecimal optBigDecimal(String key, BigDecimal defaultValue) {
    Object val = opt(key);
    return objectToBigDecimal(val, defaultValue);
  }
  
  static BigDecimal objectToBigDecimal(Object val, BigDecimal defaultValue) {
    return objectToBigDecimal(val, defaultValue, true);
  }
  
  static BigDecimal objectToBigDecimal(Object val, BigDecimal defaultValue, boolean exact) {
    if (NULL.equals(val))
      return defaultValue; 
    if (val instanceof BigDecimal)
      return (BigDecimal)val; 
    if (val instanceof BigInteger)
      return new BigDecimal((BigInteger)val); 
    if (val instanceof Double || val instanceof Float) {
      if (!numberIsFinite((Number)val))
        return defaultValue; 
      if (exact)
        return new BigDecimal(((Number)val).doubleValue()); 
      return new BigDecimal(val.toString());
    } 
    if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte)
      return new BigDecimal(((Number)val).longValue()); 
    try {
      return new BigDecimal(val.toString());
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public BigInteger optBigInteger(String key, BigInteger defaultValue) {
    Object val = opt(key);
    return objectToBigInteger(val, defaultValue);
  }
  
  static BigInteger objectToBigInteger(Object val, BigInteger defaultValue) {
    if (NULL.equals(val))
      return defaultValue; 
    if (val instanceof BigInteger)
      return (BigInteger)val; 
    if (val instanceof BigDecimal)
      return ((BigDecimal)val).toBigInteger(); 
    if (val instanceof Double || val instanceof Float) {
      if (!numberIsFinite((Number)val))
        return defaultValue; 
      return (new BigDecimal(((Number)val).doubleValue())).toBigInteger();
    } 
    if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte)
      return BigInteger.valueOf(((Number)val).longValue()); 
    try {
      String valStr = val.toString();
      if (isDecimalNotation(valStr))
        return (new BigDecimal(valStr)).toBigInteger(); 
      return new BigInteger(valStr);
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public double optDouble(String key) {
    return optDouble(key, Double.NaN);
  }
  
  public double optDouble(String key, double defaultValue) {
    Number val = optNumber(key);
    if (val == null)
      return defaultValue; 
    double doubleValue = val.doubleValue();
    return doubleValue;
  }
  
  public float optFloat(String key) {
    return optFloat(key, Float.NaN);
  }
  
  public float optFloat(String key, float defaultValue) {
    Number val = optNumber(key);
    if (val == null)
      return defaultValue; 
    float floatValue = val.floatValue();
    return floatValue;
  }
  
  public int optInt(String key) {
    return optInt(key, 0);
  }
  
  public int optInt(String key, int defaultValue) {
    Number val = optNumber(key, null);
    if (val == null)
      return defaultValue; 
    return val.intValue();
  }
  
  public JSONArray optJSONArray(String key) {
    Object o = opt(key);
    return (o instanceof JSONArray) ? (JSONArray)o : null;
  }
  
  public JSONObject optJSONObject(String key) {
    return optJSONObject(key, null);
  }
  
  public JSONObject optJSONObject(String key, JSONObject defaultValue) {
    Object object = opt(key);
    return (object instanceof JSONObject) ? (JSONObject)object : defaultValue;
  }
  
  public long optLong(String key) {
    return optLong(key, 0L);
  }
  
  public long optLong(String key, long defaultValue) {
    Number val = optNumber(key, null);
    if (val == null)
      return defaultValue; 
    return val.longValue();
  }
  
  public Number optNumber(String key) {
    return optNumber(key, null);
  }
  
  public Number optNumber(String key, Number defaultValue) {
    Object val = opt(key);
    if (NULL.equals(val))
      return defaultValue; 
    if (val instanceof Number)
      return (Number)val; 
    try {
      return stringToNumber(val.toString());
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public String optString(String key) {
    return optString(key, "");
  }
  
  public String optString(String key, String defaultValue) {
    Object object = opt(key);
    return NULL.equals(object) ? defaultValue : object.toString();
  }
  
  private void populateMap(Object bean) {
    populateMap(bean, Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>()));
  }
  
  private void populateMap(Object bean, Set<Object> objectsRecord) {
    Class<?> klass = bean.getClass();
    boolean includeSuperClass = (klass.getClassLoader() != null);
    Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
    for (Method method : methods) {
      int modifiers = method.getModifiers();
      if (Modifier.isPublic(modifiers) && 
        !Modifier.isStatic(modifiers) && (method
        .getParameterTypes()).length == 0 && 
        !method.isBridge() && method
        .getReturnType() != void.class && 
        isValidMethodName(method.getName())) {
        String key = getKeyNameFromMethod(method);
        if (key != null && !key.isEmpty())
          try {
            Object result = method.invoke(bean, new Object[0]);
            if (result != null) {
              if (objectsRecord.contains(result))
                throw recursivelyDefinedObjectException(key); 
              objectsRecord.add(result);
              this.map.put(key, wrap(result, objectsRecord));
              objectsRecord.remove(result);
              if (result instanceof Closeable)
                try {
                  ((Closeable)result).close();
                } catch (IOException iOException) {} 
            } 
          } catch (IllegalAccessException illegalAccessException) {
          
          } catch (IllegalArgumentException illegalArgumentException) {
          
          } catch (InvocationTargetException invocationTargetException) {} 
      } 
    } 
  }
  
  private static boolean isValidMethodName(String name) {
    return (!"getClass".equals(name) && !"getDeclaringClass".equals(name));
  }
  
  private static String getKeyNameFromMethod(Method method) {
    String key;
    int ignoreDepth = getAnnotationDepth(method, (Class)JSONPropertyIgnore.class);
    if (ignoreDepth > 0) {
      int forcedNameDepth = getAnnotationDepth(method, (Class)JSONPropertyName.class);
      if (forcedNameDepth < 0 || ignoreDepth <= forcedNameDepth)
        return null; 
    } 
    JSONPropertyName annotation = getAnnotation(method, JSONPropertyName.class);
    if (annotation != null && annotation.value() != null && !annotation.value().isEmpty())
      return annotation.value(); 
    String name = method.getName();
    if (name.startsWith("get") && name.length() > 3) {
      key = name.substring(3);
    } else if (name.startsWith("is") && name.length() > 2) {
      key = name.substring(2);
    } else {
      return null;
    } 
    if (key.length() == 0 || Character.isLowerCase(key.charAt(0)))
      return null; 
    if (key.length() == 1) {
      key = key.toLowerCase(Locale.ROOT);
    } else if (!Character.isUpperCase(key.charAt(1))) {
      key = key.substring(0, 1).toLowerCase(Locale.ROOT) + key.substring(1);
    } 
    return key;
  }
  
  private static <A extends Annotation> A getAnnotation(Method m, Class<A> annotationClass) {
    if (m == null || annotationClass == null)
      return null; 
    if (m.isAnnotationPresent(annotationClass))
      return m.getAnnotation(annotationClass); 
    Class<?> c = m.getDeclaringClass();
    if (c.getSuperclass() == null)
      return null; 
    for (Class<?> i : c.getInterfaces()) {
      try {
        Method im = i.getMethod(m.getName(), m.getParameterTypes());
        return getAnnotation(im, annotationClass);
      } catch (SecurityException ex) {
      
      } catch (NoSuchMethodException ex) {}
    } 
    try {
      return getAnnotation(c
          .getSuperclass().getMethod(m.getName(), m.getParameterTypes()), annotationClass);
    } catch (SecurityException ex) {
      return null;
    } catch (NoSuchMethodException ex) {
      return null;
    } 
  }
  
  private static int getAnnotationDepth(Method m, Class<? extends Annotation> annotationClass) {
    if (m == null || annotationClass == null)
      return -1; 
    if (m.isAnnotationPresent(annotationClass))
      return 1; 
    Class<?> c = m.getDeclaringClass();
    if (c.getSuperclass() == null)
      return -1; 
    for (Class<?> i : c.getInterfaces()) {
      try {
        Method im = i.getMethod(m.getName(), m.getParameterTypes());
        int d = getAnnotationDepth(im, annotationClass);
        if (d > 0)
          return d + 1; 
      } catch (SecurityException ex) {
      
      } catch (NoSuchMethodException ex) {}
    } 
    try {
      int d = getAnnotationDepth(c
          .getSuperclass().getMethod(m.getName(), m.getParameterTypes()), annotationClass);
      if (d > 0)
        return d + 1; 
      return -1;
    } catch (SecurityException ex) {
      return -1;
    } catch (NoSuchMethodException ex) {
      return -1;
    } 
  }
  
  public JSONObject put(String key, boolean value) throws JSONException {
    return put(key, value ? Boolean.TRUE : Boolean.FALSE);
  }
  
  public JSONObject put(String key, Collection<?> value) throws JSONException {
    return put(key, new JSONArray(value));
  }
  
  public JSONObject put(String key, double value) throws JSONException {
    return put(key, Double.valueOf(value));
  }
  
  public JSONObject put(String key, float value) throws JSONException {
    return put(key, Float.valueOf(value));
  }
  
  public JSONObject put(String key, int value) throws JSONException {
    return put(key, Integer.valueOf(value));
  }
  
  public JSONObject put(String key, long value) throws JSONException {
    return put(key, Long.valueOf(value));
  }
  
  public JSONObject put(String key, Map<?, ?> value) throws JSONException {
    return put(key, new JSONObject(value));
  }
  
  public JSONObject put(String key, Object value) throws JSONException {
    if (key == null)
      throw new NullPointerException("Null key."); 
    if (value != null) {
      testValidity(value);
      this.map.put(key, value);
    } else {
      remove(key);
    } 
    return this;
  }
  
  public JSONObject putOnce(String key, Object value) throws JSONException {
    if (key != null && value != null) {
      if (opt(key) != null)
        throw new JSONException("Duplicate key \"" + key + "\""); 
      return put(key, value);
    } 
    return this;
  }
  
  public JSONObject putOpt(String key, Object value) throws JSONException {
    if (key != null && value != null)
      return put(key, value); 
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
  
  public static String quote(String string) {
    StringWriter sw = new StringWriter();
    synchronized (sw.getBuffer()) {
      return quote(string, sw).toString();
    } 
  }
  
  public static Writer quote(String string, Writer w) throws IOException {
    if (string == null || string.isEmpty()) {
      w.write("\"\"");
      return w;
    } 
    char c = Character.MIN_VALUE;
    int len = string.length();
    w.write(34);
    for (int i = 0; i < len; i++) {
      char b = c;
      c = string.charAt(i);
      switch (c) {
        case '"':
        case '\\':
          w.write(92);
          w.write(c);
          break;
        case '/':
          if (b == '<')
            w.write(92); 
          w.write(c);
          break;
        case '\b':
          w.write("\\b");
          break;
        case '\t':
          w.write("\\t");
          break;
        case '\n':
          w.write("\\n");
          break;
        case '\f':
          w.write("\\f");
          break;
        case '\r':
          w.write("\\r");
          break;
        default:
          if (c < ' ' || (c >= '' && c < ' ') || (c >= ' ' && c < '℀')) {
            w.write("\\u");
            String hhhh = Integer.toHexString(c);
            w.write("0000", 0, 4 - hhhh.length());
            w.write(hhhh);
            break;
          } 
          w.write(c);
          break;
      } 
    } 
    w.write(34);
    return w;
  }
  
  public Object remove(String key) {
    return this.map.remove(key);
  }
  
  public boolean similar(Object other) {
    try {
      if (!(other instanceof JSONObject))
        return false; 
      if (!keySet().equals(((JSONObject)other).keySet()))
        return false; 
      for (Map.Entry<String, ?> entry : entrySet()) {
        String name = entry.getKey();
        Object valueThis = entry.getValue();
        Object valueOther = ((JSONObject)other).get(name);
        if (valueThis == valueOther)
          continue; 
        if (valueThis == null)
          return false; 
        if (valueThis instanceof JSONObject) {
          if (!((JSONObject)valueThis).similar(valueOther))
            return false; 
          continue;
        } 
        if (valueThis instanceof JSONArray) {
          if (!((JSONArray)valueThis).similar(valueOther))
            return false; 
          continue;
        } 
        if (valueThis instanceof Number && valueOther instanceof Number) {
          if (!isNumberSimilar((Number)valueThis, (Number)valueOther))
            return false; 
          continue;
        } 
        if (!valueThis.equals(valueOther))
          return false; 
      } 
      return true;
    } catch (Throwable exception) {
      return false;
    } 
  }
  
  static boolean isNumberSimilar(Number l, Number r) {
    if (!numberIsFinite(l) || !numberIsFinite(r))
      return false; 
    if (l.getClass().equals(r.getClass()) && l instanceof Comparable) {
      int compareTo = ((Comparable<Number>)l).compareTo(r);
      return (compareTo == 0);
    } 
    BigDecimal lBigDecimal = objectToBigDecimal(l, null, false);
    BigDecimal rBigDecimal = objectToBigDecimal(r, null, false);
    if (lBigDecimal == null || rBigDecimal == null)
      return false; 
    return (lBigDecimal.compareTo(rBigDecimal) == 0);
  }
  
  private static boolean numberIsFinite(Number n) {
    if (n instanceof Double && (((Double)n).isInfinite() || ((Double)n).isNaN()))
      return false; 
    if (n instanceof Float && (((Float)n).isInfinite() || ((Float)n).isNaN()))
      return false; 
    return true;
  }
  
  protected static boolean isDecimalNotation(String val) {
    return (val.indexOf('.') > -1 || val.indexOf('e') > -1 || val
      .indexOf('E') > -1 || "-0".equals(val));
  }
  
  protected static Number stringToNumber(String val) throws NumberFormatException {
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
  
  public static Object stringToValue(String string) {
    if ("".equals(string))
      return string; 
    if ("true".equalsIgnoreCase(string))
      return Boolean.TRUE; 
    if ("false".equalsIgnoreCase(string))
      return Boolean.FALSE; 
    if ("null".equalsIgnoreCase(string))
      return NULL; 
    char initial = string.charAt(0);
    if ((initial >= '0' && initial <= '9') || initial == '-')
      try {
        return stringToNumber(string);
      } catch (Exception exception) {} 
    return string;
  }
  
  public static void testValidity(Object o) throws JSONException {
    if (o instanceof Number && !numberIsFinite((Number)o))
      throw new JSONException("JSON does not allow non-finite numbers."); 
  }
  
  public JSONArray toJSONArray(JSONArray names) throws JSONException {
    if (names == null || names.isEmpty())
      return null; 
    JSONArray ja = new JSONArray();
    for (int i = 0; i < names.length(); i++)
      ja.put(opt(names.getString(i))); 
    return ja;
  }
  
  public String toString() {
    try {
      return toString(0);
    } catch (Exception e) {
      return null;
    } 
  }
  
  public String toString(int indentFactor) throws JSONException {
    StringWriter w = new StringWriter();
    synchronized (w.getBuffer()) {
      return write(w, indentFactor, 0).toString();
    } 
  }
  
  public static String valueToString(Object value) throws JSONException {
    return JSONWriter.valueToString(value);
  }
  
  public static Object wrap(Object object) {
    return wrap(object, null);
  }
  
  private static Object wrap(Object object, Set<Object> objectsRecord) {
    try {
      if (NULL.equals(object))
        return NULL; 
      if (object instanceof JSONObject || object instanceof JSONArray || NULL
        .equals(object) || object instanceof JSONString || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String || object instanceof BigInteger || object instanceof BigDecimal || object instanceof Enum)
        return object; 
      if (object instanceof Collection) {
        Collection<?> coll = (Collection)object;
        return new JSONArray(coll);
      } 
      if (object.getClass().isArray())
        return new JSONArray(object); 
      if (object instanceof Map) {
        Map<?, ?> map = (Map<?, ?>)object;
        return new JSONObject(map);
      } 
      Package objectPackage = object.getClass().getPackage();
      String objectPackageName = (objectPackage != null) ? objectPackage.getName() : "";
      if (objectPackageName.startsWith("java.") || objectPackageName
        .startsWith("javax.") || object
        .getClass().getClassLoader() == null)
        return object.toString(); 
      if (objectsRecord != null)
        return new JSONObject(object, objectsRecord); 
      return new JSONObject(object);
    } catch (JSONException exception) {
      throw exception;
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public Writer write(Writer writer) throws JSONException {
    return write(writer, 0, 0);
  }
  
  static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
    if (value == null || value.equals(null)) {
      writer.write("null");
    } else if (value instanceof JSONString) {
      Object o;
      try {
        o = ((JSONString)value).toJSONString();
      } catch (Exception e) {
        throw new JSONException(e);
      } 
      writer.write((o != null) ? o.toString() : quote(value.toString()));
    } else if (value instanceof Number) {
      String numberAsString = numberToString((Number)value);
      if (NUMBER_PATTERN.matcher(numberAsString).matches()) {
        writer.write(numberAsString);
      } else {
        quote(numberAsString, writer);
      } 
    } else if (value instanceof Boolean) {
      writer.write(value.toString());
    } else if (value instanceof Enum) {
      writer.write(quote(((Enum)value).name()));
    } else if (value instanceof JSONObject) {
      ((JSONObject)value).write(writer, indentFactor, indent);
    } else if (value instanceof JSONArray) {
      ((JSONArray)value).write(writer, indentFactor, indent);
    } else if (value instanceof Map) {
      Map<?, ?> map = (Map<?, ?>)value;
      (new JSONObject(map)).write(writer, indentFactor, indent);
    } else if (value instanceof Collection) {
      Collection<?> coll = (Collection)value;
      (new JSONArray(coll)).write(writer, indentFactor, indent);
    } else if (value.getClass().isArray()) {
      (new JSONArray(value)).write(writer, indentFactor, indent);
    } else {
      quote(value.toString(), writer);
    } 
    return writer;
  }
  
  static final void indent(Writer writer, int indent) throws IOException {
    for (int i = 0; i < indent; i++)
      writer.write(32); 
  }
  
  public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
    try {
      boolean needsComma = false;
      int length = length();
      writer.write(123);
      if (length == 1) {
        Map.Entry<String, ?> entry = entrySet().iterator().next();
        String key = entry.getKey();
        writer.write(quote(key));
        writer.write(58);
        if (indentFactor > 0)
          writer.write(32); 
        try {
          writeValue(writer, entry.getValue(), indentFactor, indent);
        } catch (Exception e) {
          throw new JSONException("Unable to write JSONObject value for key: " + key, e);
        } 
      } else if (length != 0) {
        int newIndent = indent + indentFactor;
        for (Map.Entry<String, ?> entry : entrySet()) {
          if (needsComma)
            writer.write(44); 
          if (indentFactor > 0)
            writer.write(10); 
          indent(writer, newIndent);
          String key = entry.getKey();
          writer.write(quote(key));
          writer.write(58);
          if (indentFactor > 0)
            writer.write(32); 
          try {
            writeValue(writer, entry.getValue(), indentFactor, newIndent);
          } catch (Exception e) {
            throw new JSONException("Unable to write JSONObject value for key: " + key, e);
          } 
          needsComma = true;
        } 
        if (indentFactor > 0)
          writer.write(10); 
        indent(writer, indent);
      } 
      writer.write(125);
      return writer;
    } catch (IOException exception) {
      throw new JSONException(exception);
    } 
  }
  
  public Map<String, Object> toMap() {
    Map<String, Object> results = new HashMap<String, Object>();
    for (Map.Entry<String, Object> entry : entrySet()) {
      Object value;
      if (entry.getValue() == null || NULL.equals(entry.getValue())) {
        value = null;
      } else if (entry.getValue() instanceof JSONObject) {
        value = ((JSONObject)entry.getValue()).toMap();
      } else if (entry.getValue() instanceof JSONArray) {
        value = ((JSONArray)entry.getValue()).toList();
      } else {
        value = entry.getValue();
      } 
      results.put(entry.getKey(), value);
    } 
    return results;
  }
  
  private static JSONException wrongValueFormatException(String key, String valueType, Throwable cause) {
    return new JSONException("JSONObject[" + 
        quote(key) + "] is not a " + valueType + ".", cause);
  }
  
  private static JSONException wrongValueFormatException(String key, String valueType, Object value, Throwable cause) {
    return new JSONException("JSONObject[" + 
        quote(key) + "] is not a " + valueType + " (" + value + ").", cause);
  }
  
  private static JSONException recursivelyDefinedObjectException(String key) {
    return new JSONException("JavaBean object contains recursively defined member variable of key " + 
        quote(key));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\JSONObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */