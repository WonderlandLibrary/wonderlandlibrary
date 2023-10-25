package org.apache.logging.log4j.message;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.util.IndexedStringMap;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.StringBuilders;

enum MapMessageJsonFormatter {
  public static final int MAX_DEPTH;
  
  private static final char DQUOTE = '"';
  
  private static final char RBRACE = ']';
  
  private static final char LBRACE = '[';
  
  private static final char COMMA = ',';
  
  private static final char RCURLY = '}';
  
  private static final char LCURLY = '{';
  
  private static final char COLON = ':';
  
  static {
    MAX_DEPTH = readMaxDepth();
  }
  
  private static int readMaxDepth() {
    int maxDepth = PropertiesUtil.getProperties().getIntegerProperty("log4j2.mapMessage.jsonFormatter.maxDepth", 8);
    if (maxDepth < 0)
      throw new IllegalArgumentException("was expecting a positive maxDepth, found: " + maxDepth); 
    return maxDepth;
  }
  
  static void format(StringBuilder sb, Object object) {
    format(sb, object, 0);
  }
  
  private static void format(StringBuilder sb, Object object, int depth) {
    if (depth >= MAX_DEPTH)
      throw new IllegalArgumentException("maxDepth has been exceeded"); 
    if (object == null) {
      sb.append("null");
    } else if (object instanceof IndexedStringMap) {
      IndexedStringMap map = (IndexedStringMap)object;
      formatIndexedStringMap(sb, map, depth);
    } else if (object instanceof Map) {
      Map<Object, Object> map = (Map<Object, Object>)object;
      formatMap(sb, map, depth);
    } else if (object instanceof List) {
      List<Object> list = (List<Object>)object;
      formatList(sb, list, depth);
    } else if (object instanceof Collection) {
      Collection<Object> collection = (Collection<Object>)object;
      formatCollection(sb, collection, depth);
    } else if (object instanceof Number) {
      Number number = (Number)object;
      formatNumber(sb, number);
    } else if (object instanceof Boolean) {
      boolean booleanValue = ((Boolean)object).booleanValue();
      formatBoolean(sb, booleanValue);
    } else if (object instanceof StringBuilderFormattable) {
      StringBuilderFormattable formattable = (StringBuilderFormattable)object;
      formatFormattable(sb, formattable);
    } else if (object instanceof char[]) {
      char[] charValues = (char[])object;
      formatCharArray(sb, charValues);
    } else if (object instanceof boolean[]) {
      boolean[] booleanValues = (boolean[])object;
      formatBooleanArray(sb, booleanValues);
    } else if (object instanceof byte[]) {
      byte[] byteValues = (byte[])object;
      formatByteArray(sb, byteValues);
    } else if (object instanceof short[]) {
      short[] shortValues = (short[])object;
      formatShortArray(sb, shortValues);
    } else if (object instanceof int[]) {
      int[] intValues = (int[])object;
      formatIntArray(sb, intValues);
    } else if (object instanceof long[]) {
      long[] longValues = (long[])object;
      formatLongArray(sb, longValues);
    } else if (object instanceof float[]) {
      float[] floatValues = (float[])object;
      formatFloatArray(sb, floatValues);
    } else if (object instanceof double[]) {
      double[] doubleValues = (double[])object;
      formatDoubleArray(sb, doubleValues);
    } else if (object instanceof Object[]) {
      Object[] objectValues = (Object[])object;
      formatObjectArray(sb, objectValues, depth);
    } else {
      formatString(sb, object);
    } 
  }
  
  private static void formatIndexedStringMap(StringBuilder sb, IndexedStringMap map, int depth) {
    sb.append('{');
    int nextDepth = depth + 1;
    for (int entryIndex = 0; entryIndex < map.size(); entryIndex++) {
      String key = map.getKeyAt(entryIndex);
      Object value = map.getValueAt(entryIndex);
      if (entryIndex > 0)
        sb.append(','); 
      sb.append('"');
      int keyStartIndex = sb.length();
      sb.append(key);
      StringBuilders.escapeJson(sb, keyStartIndex);
      sb.append('"').append(':');
      format(sb, value, nextDepth);
    } 
    sb.append('}');
  }
  
  private static void formatMap(StringBuilder sb, Map<Object, Object> map, int depth) {
    sb.append('{');
    int nextDepth = depth + 1;
    boolean[] firstEntry = { true };
    map.forEach((key, value) -> {
          if (key == null)
            throw new IllegalArgumentException("null keys are not allowed"); 
          if (firstEntry[0]) {
            firstEntry[0] = false;
          } else {
            sb.append(',');
          } 
          sb.append('"');
          String keyString = String.valueOf(key);
          int keyStartIndex = sb.length();
          sb.append(keyString);
          StringBuilders.escapeJson(sb, keyStartIndex);
          sb.append('"').append(':');
          format(sb, value, nextDepth);
        });
    sb.append('}');
  }
  
  private static void formatList(StringBuilder sb, List<Object> items, int depth) {
    sb.append('[');
    int nextDepth = depth + 1;
    for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      Object item = items.get(itemIndex);
      format(sb, item, nextDepth);
    } 
    sb.append(']');
  }
  
  private static void formatCollection(StringBuilder sb, Collection<Object> items, int depth) {
    sb.append('[');
    int nextDepth = depth + 1;
    boolean[] firstItem = { true };
    items.forEach(item -> {
          if (firstItem[0]) {
            firstItem[0] = false;
          } else {
            sb.append(',');
          } 
          format(sb, item, nextDepth);
        });
    sb.append(']');
  }
  
  private static void formatNumber(StringBuilder sb, Number number) {
    if (number instanceof BigDecimal) {
      BigDecimal decimalNumber = (BigDecimal)number;
      sb.append(decimalNumber.toString());
    } else if (number instanceof Double) {
      double doubleNumber = ((Double)number).doubleValue();
      sb.append(doubleNumber);
    } else if (number instanceof Float) {
      float floatNumber = ((Float)number).floatValue();
      sb.append(floatNumber);
    } else if (number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long) {
      long longNumber = number.longValue();
      sb.append(longNumber);
    } else {
      long longNumber = number.longValue();
      double doubleValue = number.doubleValue();
      if (Double.compare(longNumber, doubleValue) == 0) {
        sb.append(longNumber);
      } else {
        sb.append(doubleValue);
      } 
    } 
  }
  
  private static void formatBoolean(StringBuilder sb, boolean booleanValue) {
    sb.append(booleanValue);
  }
  
  private static void formatFormattable(StringBuilder sb, StringBuilderFormattable formattable) {
    sb.append('"');
    int startIndex = sb.length();
    formattable.formatTo(sb);
    StringBuilders.escapeJson(sb, startIndex);
    sb.append('"');
  }
  
  private static void formatCharArray(StringBuilder sb, char[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      char item = items[itemIndex];
      sb.append('"');
      int startIndex = sb.length();
      sb.append(item);
      StringBuilders.escapeJson(sb, startIndex);
      sb.append('"');
    } 
    sb.append(']');
  }
  
  private static void formatBooleanArray(StringBuilder sb, boolean[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      boolean item = items[itemIndex];
      sb.append(item);
    } 
    sb.append(']');
  }
  
  private static void formatByteArray(StringBuilder sb, byte[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      byte item = items[itemIndex];
      sb.append(item);
    } 
    sb.append(']');
  }
  
  private static void formatShortArray(StringBuilder sb, short[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      short item = items[itemIndex];
      sb.append(item);
    } 
    sb.append(']');
  }
  
  private static void formatIntArray(StringBuilder sb, int[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      int item = items[itemIndex];
      sb.append(item);
    } 
    sb.append(']');
  }
  
  private static void formatLongArray(StringBuilder sb, long[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      long item = items[itemIndex];
      sb.append(item);
    } 
    sb.append(']');
  }
  
  private static void formatFloatArray(StringBuilder sb, float[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      float item = items[itemIndex];
      sb.append(item);
    } 
    sb.append(']');
  }
  
  private static void formatDoubleArray(StringBuilder sb, double[] items) {
    sb.append('[');
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      double item = items[itemIndex];
      sb.append(item);
    } 
    sb.append(']');
  }
  
  private static void formatObjectArray(StringBuilder sb, Object[] items, int depth) {
    sb.append('[');
    int nextDepth = depth + 1;
    for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
      if (itemIndex > 0)
        sb.append(','); 
      Object item = items[itemIndex];
      format(sb, item, nextDepth);
    } 
    sb.append(']');
  }
  
  private static void formatString(StringBuilder sb, Object value) {
    sb.append('"');
    int startIndex = sb.length();
    String valueString = String.valueOf(value);
    sb.append(valueString);
    StringBuilders.escapeJson(sb, startIndex);
    sb.append('"');
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\MapMessageJsonFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */