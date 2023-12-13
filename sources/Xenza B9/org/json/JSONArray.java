// 
// Decompiled by Procyon v0.6.0
// 

package org.json;

import java.lang.reflect.Array;
import java.util.List;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Map;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class JSONArray implements Iterable<Object>
{
    private final ArrayList<Object> myArrayList;
    
    public JSONArray() {
        this.myArrayList = new ArrayList<Object>();
    }
    
    public JSONArray(final JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        char nextChar = x.nextClean();
        if (nextChar == '\0') {
            throw x.syntaxError("Expected a ',' or ']'");
        }
        if (nextChar == ']') {
            return;
        }
        x.back();
        while (true) {
            if (x.nextClean() == ',') {
                x.back();
                this.myArrayList.add(JSONObject.NULL);
            }
            else {
                x.back();
                this.myArrayList.add(x.nextValue());
            }
            switch (x.nextClean()) {
                case '\0': {
                    throw x.syntaxError("Expected a ',' or ']'");
                }
                case ',': {
                    nextChar = x.nextClean();
                    if (nextChar == '\0') {
                        throw x.syntaxError("Expected a ',' or ']'");
                    }
                    if (nextChar == ']') {
                        return;
                    }
                    x.back();
                    continue;
                }
                case ']': {
                    return;
                }
                default: {
                    throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }
    
    public JSONArray(final String source) throws JSONException {
        this(new JSONTokener(source));
    }
    
    public JSONArray(final Collection<?> collection) {
        if (collection == null) {
            this.myArrayList = new ArrayList<Object>();
        }
        else {
            this.myArrayList = new ArrayList<Object>(collection.size());
            this.addAll(collection, true);
        }
    }
    
    public JSONArray(final Iterable<?> iter) {
        this();
        if (iter == null) {
            return;
        }
        this.addAll(iter, true);
    }
    
    public JSONArray(final JSONArray array) {
        if (array == null) {
            this.myArrayList = new ArrayList<Object>();
        }
        else {
            this.myArrayList = new ArrayList<Object>(array.myArrayList);
        }
    }
    
    public JSONArray(final Object array) throws JSONException {
        this();
        if (!array.getClass().isArray()) {
            throw new JSONException("JSONArray initial value should be a string or collection or array.");
        }
        this.addAll(array, true);
    }
    
    public JSONArray(final int initialCapacity) throws JSONException {
        if (initialCapacity < 0) {
            throw new JSONException("JSONArray initial capacity cannot be negative.");
        }
        this.myArrayList = new ArrayList<Object>(initialCapacity);
    }
    
    @Override
    public Iterator<Object> iterator() {
        return this.myArrayList.iterator();
    }
    
    public Object get(final int index) throws JSONException {
        final Object object = this.opt(index);
        if (object == null) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        return object;
    }
    
    public boolean getBoolean(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw wrongValueFormatException(index, "boolean", object, null);
    }
    
    public double getDouble(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof Number) {
            return ((Number)object).doubleValue();
        }
        try {
            return Double.parseDouble(object.toString());
        }
        catch (final Exception e) {
            throw wrongValueFormatException(index, "double", object, e);
        }
    }
    
    public float getFloat(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof Number) {
            return ((Number)object).floatValue();
        }
        try {
            return Float.parseFloat(object.toString());
        }
        catch (final Exception e) {
            throw wrongValueFormatException(index, "float", object, e);
        }
    }
    
    public Number getNumber(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            if (object instanceof Number) {
                return (Number)object;
            }
            return JSONObject.stringToNumber(object.toString());
        }
        catch (final Exception e) {
            throw wrongValueFormatException(index, "number", object, e);
        }
    }
    
    public <E extends Enum<E>> E getEnum(final Class<E> clazz, final int index) throws JSONException {
        final E val = (E)this.optEnum((Class<Enum>)clazz, index);
        if (val == null) {
            throw wrongValueFormatException(index, "enum of type " + JSONObject.quote(clazz.getSimpleName()), this.opt(index), null);
        }
        return val;
    }
    
    public BigDecimal getBigDecimal(final int index) throws JSONException {
        final Object object = this.get(index);
        final BigDecimal val = JSONObject.objectToBigDecimal(object, null);
        if (val == null) {
            throw wrongValueFormatException(index, "BigDecimal", object, null);
        }
        return val;
    }
    
    public BigInteger getBigInteger(final int index) throws JSONException {
        final Object object = this.get(index);
        final BigInteger val = JSONObject.objectToBigInteger(object, null);
        if (val == null) {
            throw wrongValueFormatException(index, "BigInteger", object, null);
        }
        return val;
    }
    
    public int getInt(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof Number) {
            return ((Number)object).intValue();
        }
        try {
            return Integer.parseInt(object.toString());
        }
        catch (final Exception e) {
            throw wrongValueFormatException(index, "int", object, e);
        }
    }
    
    public JSONArray getJSONArray(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw wrongValueFormatException(index, "JSONArray", object, null);
    }
    
    public JSONObject getJSONObject(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        throw wrongValueFormatException(index, "JSONObject", object, null);
    }
    
    public long getLong(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof Number) {
            return ((Number)object).longValue();
        }
        try {
            return Long.parseLong(object.toString());
        }
        catch (final Exception e) {
            throw wrongValueFormatException(index, "long", object, e);
        }
    }
    
    public String getString(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        }
        throw wrongValueFormatException(index, "String", object, null);
    }
    
    public boolean isNull(final int index) {
        return JSONObject.NULL.equals(this.opt(index));
    }
    
    public String join(final String separator) throws JSONException {
        final int len = this.length();
        if (len == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(JSONObject.valueToString(this.myArrayList.get(0)));
        for (int i = 1; i < len; ++i) {
            sb.append(separator).append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }
    
    public int length() {
        return this.myArrayList.size();
    }
    
    public void clear() {
        this.myArrayList.clear();
    }
    
    public Object opt(final int index) {
        return (index < 0 || index >= this.length()) ? null : this.myArrayList.get(index);
    }
    
    public boolean optBoolean(final int index) {
        return this.optBoolean(index, false);
    }
    
    public boolean optBoolean(final int index, final boolean defaultValue) {
        try {
            return this.getBoolean(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public double optDouble(final int index) {
        return this.optDouble(index, Double.NaN);
    }
    
    public double optDouble(final int index, final double defaultValue) {
        final Number val = this.optNumber(index, null);
        if (val == null) {
            return defaultValue;
        }
        final double doubleValue = val.doubleValue();
        return doubleValue;
    }
    
    public float optFloat(final int index) {
        return this.optFloat(index, Float.NaN);
    }
    
    public float optFloat(final int index, final float defaultValue) {
        final Number val = this.optNumber(index, null);
        if (val == null) {
            return defaultValue;
        }
        final float floatValue = val.floatValue();
        return floatValue;
    }
    
    public int optInt(final int index) {
        return this.optInt(index, 0);
    }
    
    public int optInt(final int index, final int defaultValue) {
        final Number val = this.optNumber(index, null);
        if (val == null) {
            return defaultValue;
        }
        return val.intValue();
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> clazz, final int index) {
        return this.optEnum(clazz, index, (E)null);
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> clazz, final int index, final E defaultValue) {
        try {
            final Object val = this.opt(index);
            if (JSONObject.NULL.equals(val)) {
                return defaultValue;
            }
            if (clazz.isAssignableFrom(val.getClass())) {
                final E myE = (E)val;
                return myE;
            }
            return Enum.valueOf(clazz, val.toString());
        }
        catch (final IllegalArgumentException e) {
            return defaultValue;
        }
        catch (final NullPointerException e2) {
            return defaultValue;
        }
    }
    
    public BigInteger optBigInteger(final int index, final BigInteger defaultValue) {
        final Object val = this.opt(index);
        return JSONObject.objectToBigInteger(val, defaultValue);
    }
    
    public BigDecimal optBigDecimal(final int index, final BigDecimal defaultValue) {
        final Object val = this.opt(index);
        return JSONObject.objectToBigDecimal(val, defaultValue);
    }
    
    public JSONArray optJSONArray(final int index) {
        final Object o = this.opt(index);
        return (o instanceof JSONArray) ? ((JSONArray)o) : null;
    }
    
    public JSONObject optJSONObject(final int index) {
        final Object o = this.opt(index);
        return (o instanceof JSONObject) ? ((JSONObject)o) : null;
    }
    
    public long optLong(final int index) {
        return this.optLong(index, 0L);
    }
    
    public long optLong(final int index, final long defaultValue) {
        final Number val = this.optNumber(index, null);
        if (val == null) {
            return defaultValue;
        }
        return val.longValue();
    }
    
    public Number optNumber(final int index) {
        return this.optNumber(index, null);
    }
    
    public Number optNumber(final int index, final Number defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return (Number)val;
        }
        if (val instanceof String) {
            try {
                return JSONObject.stringToNumber((String)val);
            }
            catch (final Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public String optString(final int index) {
        return this.optString(index, "");
    }
    
    public String optString(final int index, final String defaultValue) {
        final Object object = this.opt(index);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }
    
    public JSONArray put(final boolean value) {
        return this.put(value ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONArray put(final Collection<?> value) {
        return this.put(new JSONArray(value));
    }
    
    public JSONArray put(final double value) throws JSONException {
        return this.put((Object)value);
    }
    
    public JSONArray put(final float value) throws JSONException {
        return this.put((Object)value);
    }
    
    public JSONArray put(final int value) {
        return this.put((Object)value);
    }
    
    public JSONArray put(final long value) {
        return this.put((Object)value);
    }
    
    public JSONArray put(final Map<?, ?> value) {
        return this.put(new JSONObject(value));
    }
    
    public JSONArray put(final Object value) {
        JSONObject.testValidity(value);
        this.myArrayList.add(value);
        return this;
    }
    
    public JSONArray put(final int index, final boolean value) throws JSONException {
        return this.put(index, value ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONArray put(final int index, final Collection<?> value) throws JSONException {
        return this.put(index, new JSONArray(value));
    }
    
    public JSONArray put(final int index, final double value) throws JSONException {
        return this.put(index, (Object)value);
    }
    
    public JSONArray put(final int index, final float value) throws JSONException {
        return this.put(index, (Object)value);
    }
    
    public JSONArray put(final int index, final int value) throws JSONException {
        return this.put(index, (Object)value);
    }
    
    public JSONArray put(final int index, final long value) throws JSONException {
        return this.put(index, (Object)value);
    }
    
    public JSONArray put(final int index, final Map<?, ?> value) throws JSONException {
        this.put(index, new JSONObject(value));
        return this;
    }
    
    public JSONArray put(final int index, final Object value) throws JSONException {
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < this.length()) {
            JSONObject.testValidity(value);
            this.myArrayList.set(index, value);
            return this;
        }
        if (index == this.length()) {
            return this.put(value);
        }
        this.myArrayList.ensureCapacity(index + 1);
        while (index != this.length()) {
            this.myArrayList.add(JSONObject.NULL);
        }
        return this.put(value);
    }
    
    public JSONArray putAll(final Collection<?> collection) {
        this.addAll(collection, false);
        return this;
    }
    
    public JSONArray putAll(final Iterable<?> iter) {
        this.addAll(iter, false);
        return this;
    }
    
    public JSONArray putAll(final JSONArray array) {
        this.myArrayList.addAll(array.myArrayList);
        return this;
    }
    
    public JSONArray putAll(final Object array) throws JSONException {
        this.addAll(array, false);
        return this;
    }
    
    public Object query(final String jsonPointer) {
        return this.query(new JSONPointer(jsonPointer));
    }
    
    public Object query(final JSONPointer jsonPointer) {
        return jsonPointer.queryFrom(this);
    }
    
    public Object optQuery(final String jsonPointer) {
        return this.optQuery(new JSONPointer(jsonPointer));
    }
    
    public Object optQuery(final JSONPointer jsonPointer) {
        try {
            return jsonPointer.queryFrom(this);
        }
        catch (final JSONPointerException e) {
            return null;
        }
    }
    
    public Object remove(final int index) {
        return (index >= 0 && index < this.length()) ? this.myArrayList.remove(index) : null;
    }
    
    public boolean similar(final Object other) {
        if (!(other instanceof JSONArray)) {
            return false;
        }
        final int len = this.length();
        if (len != ((JSONArray)other).length()) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            final Object valueThis = this.myArrayList.get(i);
            final Object valueOther = ((JSONArray)other).myArrayList.get(i);
            if (valueThis != valueOther) {
                if (valueThis == null) {
                    return false;
                }
                if (valueThis instanceof JSONObject) {
                    if (!((JSONObject)valueThis).similar(valueOther)) {
                        return false;
                    }
                }
                else if (valueThis instanceof JSONArray) {
                    if (!((JSONArray)valueThis).similar(valueOther)) {
                        return false;
                    }
                }
                else if (valueThis instanceof Number && valueOther instanceof Number) {
                    if (!JSONObject.isNumberSimilar((Number)valueThis, (Number)valueOther)) {
                        return false;
                    }
                }
                else if (valueThis instanceof JSONString && valueOther instanceof JSONString) {
                    if (!((JSONString)valueThis).toJSONString().equals(((JSONString)valueOther).toJSONString())) {
                        return false;
                    }
                }
                else if (!valueThis.equals(valueOther)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public JSONObject toJSONObject(final JSONArray names) throws JSONException {
        if (names == null || names.isEmpty() || this.isEmpty()) {
            return null;
        }
        final JSONObject jo = new JSONObject(names.length());
        for (int i = 0; i < names.length(); ++i) {
            jo.put(names.getString(i), this.opt(i));
        }
        return jo;
    }
    
    @Override
    public String toString() {
        try {
            return this.toString(0);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public String toString(final int indentFactor) throws JSONException {
        final StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            return this.write(sw, indentFactor, 0).toString();
        }
    }
    
    public Writer write(final Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }
    
    public Writer write(final Writer writer, final int indentFactor, final int indent) throws JSONException {
        try {
            boolean needsComma = false;
            final int length = this.length();
            writer.write(91);
            Label_0176: {
                if (length == 1) {
                    try {
                        JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
                        break Label_0176;
                    }
                    catch (final Exception e) {
                        throw new JSONException("Unable to write JSONArray value at index: 0", e);
                    }
                }
                if (length != 0) {
                    final int newIndent = indent + indentFactor;
                    for (int i = 0; i < length; ++i) {
                        if (needsComma) {
                            writer.write(44);
                        }
                        if (indentFactor > 0) {
                            writer.write(10);
                        }
                        JSONObject.indent(writer, newIndent);
                        try {
                            JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newIndent);
                        }
                        catch (final Exception e2) {
                            throw new JSONException("Unable to write JSONArray value at index: " + i, e2);
                        }
                        needsComma = true;
                    }
                    if (indentFactor > 0) {
                        writer.write(10);
                    }
                    JSONObject.indent(writer, indent);
                }
            }
            writer.write(93);
            return writer;
        }
        catch (final IOException e3) {
            throw new JSONException(e3);
        }
    }
    
    public List<Object> toList() {
        final List<Object> results = new ArrayList<Object>(this.myArrayList.size());
        for (final Object element : this.myArrayList) {
            if (element == null || JSONObject.NULL.equals(element)) {
                results.add(null);
            }
            else if (element instanceof JSONArray) {
                results.add(((JSONArray)element).toList());
            }
            else if (element instanceof JSONObject) {
                results.add(((JSONObject)element).toMap());
            }
            else {
                results.add(element);
            }
        }
        return results;
    }
    
    public boolean isEmpty() {
        return this.myArrayList.isEmpty();
    }
    
    private void addAll(final Collection<?> collection, final boolean wrap) {
        this.myArrayList.ensureCapacity(this.myArrayList.size() + collection.size());
        if (wrap) {
            for (final Object o : collection) {
                this.put(JSONObject.wrap(o));
            }
        }
        else {
            for (final Object o : collection) {
                this.put(o);
            }
        }
    }
    
    private void addAll(final Iterable<?> iter, final boolean wrap) {
        if (wrap) {
            for (final Object o : iter) {
                this.put(JSONObject.wrap(o));
            }
        }
        else {
            for (final Object o : iter) {
                this.put(o);
            }
        }
    }
    
    private void addAll(final Object array, final boolean wrap) throws JSONException {
        if (array.getClass().isArray()) {
            final int length = Array.getLength(array);
            this.myArrayList.ensureCapacity(this.myArrayList.size() + length);
            if (wrap) {
                for (int i = 0; i < length; ++i) {
                    this.put(JSONObject.wrap(Array.get(array, i)));
                }
            }
            else {
                for (int i = 0; i < length; ++i) {
                    this.put(Array.get(array, i));
                }
            }
        }
        else if (array instanceof JSONArray) {
            this.myArrayList.addAll(((JSONArray)array).myArrayList);
        }
        else if (array instanceof Collection) {
            this.addAll((Collection<?>)array, wrap);
        }
        else {
            if (!(array instanceof Iterable)) {
                throw new JSONException("JSONArray initial value should be a string or collection or array.");
            }
            this.addAll((Iterable<?>)array, wrap);
        }
    }
    
    private static JSONException wrongValueFormatException(final int idx, final String valueType, final Object value, final Throwable cause) {
        if (value == null) {
            return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (null).", cause);
        }
        if (value instanceof Map || value instanceof Iterable || value instanceof JSONObject) {
            return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (" + value.getClass() + ").", cause);
        }
        return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (" + value.getClass() + " : " + value + ").", cause);
    }
}
