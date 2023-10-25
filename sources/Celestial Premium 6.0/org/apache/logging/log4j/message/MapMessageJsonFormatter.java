/*
 * Decompiled with CFR 0.150.
 */
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

    public static final int MAX_DEPTH = MapMessageJsonFormatter.readMaxDepth();
    private static final char DQUOTE = '\"';
    private static final char RBRACE = ']';
    private static final char LBRACE = '[';
    private static final char COMMA = ',';
    private static final char RCURLY = '}';
    private static final char LCURLY = '{';
    private static final char COLON = ':';

    private static int readMaxDepth() {
        int maxDepth = PropertiesUtil.getProperties().getIntegerProperty("log4j2.mapMessage.jsonFormatter.maxDepth", 8);
        if (maxDepth < 0) {
            throw new IllegalArgumentException("was expecting a positive maxDepth, found: " + maxDepth);
        }
        return maxDepth;
    }

    static void format(StringBuilder sb, Object object) {
        MapMessageJsonFormatter.format(sb, object, 0);
    }

    private static void format(StringBuilder sb, Object object, int depth) {
        if (depth >= MAX_DEPTH) {
            throw new IllegalArgumentException("maxDepth has been exceeded");
        }
        if (object == null) {
            sb.append("null");
        } else if (object instanceof IndexedStringMap) {
            IndexedStringMap map = (IndexedStringMap)object;
            MapMessageJsonFormatter.formatIndexedStringMap(sb, map, depth);
        } else if (object instanceof Map) {
            Map map = (Map)object;
            MapMessageJsonFormatter.formatMap(sb, map, depth);
        } else if (object instanceof List) {
            List list = (List)object;
            MapMessageJsonFormatter.formatList(sb, list, depth);
        } else if (object instanceof Collection) {
            Collection collection = (Collection)object;
            MapMessageJsonFormatter.formatCollection(sb, collection, depth);
        } else if (object instanceof Number) {
            Number number = (Number)object;
            MapMessageJsonFormatter.formatNumber(sb, number);
        } else if (object instanceof Boolean) {
            boolean booleanValue = (Boolean)object;
            MapMessageJsonFormatter.formatBoolean(sb, booleanValue);
        } else if (object instanceof StringBuilderFormattable) {
            StringBuilderFormattable formattable = (StringBuilderFormattable)object;
            MapMessageJsonFormatter.formatFormattable(sb, formattable);
        } else if (object instanceof char[]) {
            char[] charValues = (char[])object;
            MapMessageJsonFormatter.formatCharArray(sb, charValues);
        } else if (object instanceof boolean[]) {
            boolean[] booleanValues = (boolean[])object;
            MapMessageJsonFormatter.formatBooleanArray(sb, booleanValues);
        } else if (object instanceof byte[]) {
            byte[] byteValues = (byte[])object;
            MapMessageJsonFormatter.formatByteArray(sb, byteValues);
        } else if (object instanceof short[]) {
            short[] shortValues = (short[])object;
            MapMessageJsonFormatter.formatShortArray(sb, shortValues);
        } else if (object instanceof int[]) {
            int[] intValues = (int[])object;
            MapMessageJsonFormatter.formatIntArray(sb, intValues);
        } else if (object instanceof long[]) {
            long[] longValues = (long[])object;
            MapMessageJsonFormatter.formatLongArray(sb, longValues);
        } else if (object instanceof float[]) {
            float[] floatValues = (float[])object;
            MapMessageJsonFormatter.formatFloatArray(sb, floatValues);
        } else if (object instanceof double[]) {
            double[] doubleValues = (double[])object;
            MapMessageJsonFormatter.formatDoubleArray(sb, doubleValues);
        } else if (object instanceof Object[]) {
            Object[] objectValues = (Object[])object;
            MapMessageJsonFormatter.formatObjectArray(sb, objectValues, depth);
        } else {
            MapMessageJsonFormatter.formatString(sb, object);
        }
    }

    private static void formatIndexedStringMap(StringBuilder sb, IndexedStringMap map, int depth) {
        sb.append('{');
        int nextDepth = depth + 1;
        for (int entryIndex = 0; entryIndex < map.size(); ++entryIndex) {
            String key = map.getKeyAt(entryIndex);
            Object value = map.getValueAt(entryIndex);
            if (entryIndex > 0) {
                sb.append(',');
            }
            sb.append('\"');
            int keyStartIndex = sb.length();
            sb.append(key);
            StringBuilders.escapeJson(sb, keyStartIndex);
            sb.append('\"').append(':');
            MapMessageJsonFormatter.format(sb, value, nextDepth);
        }
        sb.append('}');
    }

    private static void formatMap(StringBuilder sb, Map<Object, Object> map, int depth) {
        sb.append('{');
        int nextDepth = depth + 1;
        boolean[] firstEntry = new boolean[]{true};
        map.forEach((key, value) -> {
            if (key == null) {
                throw new IllegalArgumentException("null keys are not allowed");
            }
            if (firstEntry[0]) {
                firstEntry[0] = false;
            } else {
                sb.append(',');
            }
            sb.append('\"');
            String keyString = String.valueOf(key);
            int keyStartIndex = sb.length();
            sb.append(keyString);
            StringBuilders.escapeJson(sb, keyStartIndex);
            sb.append('\"').append(':');
            MapMessageJsonFormatter.format(sb, value, nextDepth);
        });
        sb.append('}');
    }

    private static void formatList(StringBuilder sb, List<Object> items, int depth) {
        sb.append('[');
        int nextDepth = depth + 1;
        for (int itemIndex = 0; itemIndex < items.size(); ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            Object item = items.get(itemIndex);
            MapMessageJsonFormatter.format(sb, item, nextDepth);
        }
        sb.append(']');
    }

    private static void formatCollection(StringBuilder sb, Collection<Object> items, int depth) {
        sb.append('[');
        int nextDepth = depth + 1;
        boolean[] firstItem = new boolean[]{true};
        items.forEach(item -> {
            if (firstItem[0]) {
                firstItem[0] = false;
            } else {
                sb.append(',');
            }
            MapMessageJsonFormatter.format(sb, item, nextDepth);
        });
        sb.append(']');
    }

    private static void formatNumber(StringBuilder sb, Number number) {
        if (number instanceof BigDecimal) {
            BigDecimal decimalNumber = (BigDecimal)number;
            sb.append(decimalNumber.toString());
        } else if (number instanceof Double) {
            double doubleNumber = (Double)number;
            sb.append(doubleNumber);
        } else if (number instanceof Float) {
            float floatNumber = ((Float)number).floatValue();
            sb.append(floatNumber);
        } else if (number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long) {
            long longNumber = number.longValue();
            sb.append(longNumber);
        } else {
            double doubleValue;
            long longNumber = number.longValue();
            if (Double.compare(longNumber, doubleValue = number.doubleValue()) == 0) {
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
        sb.append('\"');
        int startIndex = sb.length();
        formattable.formatTo(sb);
        StringBuilders.escapeJson(sb, startIndex);
        sb.append('\"');
    }

    private static void formatCharArray(StringBuilder sb, char[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            char item = items[itemIndex];
            sb.append('\"');
            int startIndex = sb.length();
            sb.append(item);
            StringBuilders.escapeJson(sb, startIndex);
            sb.append('\"');
        }
        sb.append(']');
    }

    private static void formatBooleanArray(StringBuilder sb, boolean[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            boolean item = items[itemIndex];
            sb.append(item);
        }
        sb.append(']');
    }

    private static void formatByteArray(StringBuilder sb, byte[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            byte item = items[itemIndex];
            sb.append(item);
        }
        sb.append(']');
    }

    private static void formatShortArray(StringBuilder sb, short[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            short item = items[itemIndex];
            sb.append(item);
        }
        sb.append(']');
    }

    private static void formatIntArray(StringBuilder sb, int[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            int item = items[itemIndex];
            sb.append(item);
        }
        sb.append(']');
    }

    private static void formatLongArray(StringBuilder sb, long[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            long item = items[itemIndex];
            sb.append(item);
        }
        sb.append(']');
    }

    private static void formatFloatArray(StringBuilder sb, float[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            float item = items[itemIndex];
            sb.append(item);
        }
        sb.append(']');
    }

    private static void formatDoubleArray(StringBuilder sb, double[] items) {
        sb.append('[');
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            double item = items[itemIndex];
            sb.append(item);
        }
        sb.append(']');
    }

    private static void formatObjectArray(StringBuilder sb, Object[] items, int depth) {
        sb.append('[');
        int nextDepth = depth + 1;
        for (int itemIndex = 0; itemIndex < items.length; ++itemIndex) {
            if (itemIndex > 0) {
                sb.append(',');
            }
            Object item = items[itemIndex];
            MapMessageJsonFormatter.format(sb, item, nextDepth);
        }
        sb.append(']');
    }

    private static void formatString(StringBuilder sb, Object value) {
        sb.append('\"');
        int startIndex = sb.length();
        String valueString = String.valueOf(value);
        sb.append(valueString);
        StringBuilders.escapeJson(sb, startIndex);
        sb.append('\"');
    }
}

