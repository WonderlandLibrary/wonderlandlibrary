/*
 * Decompiled with CFR 0.152.
 */
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.JSONPointerException;
import org.json.JSONTokener;

public class JSONArray
implements Iterable<Object> {
    private final ArrayList<Object> myArrayList;

    public JSONArray() {
        this.myArrayList = new ArrayList();
    }

    public JSONArray(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() != ']') {
            x.back();
            block4: while (true) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.myArrayList.add(JSONObject.NULL);
                } else {
                    x.back();
                    this.myArrayList.add(x.nextValue());
                }
                switch (x.nextClean()) {
                    case ',': {
                        if (x.nextClean() == ']') {
                            return;
                        }
                        x.back();
                        continue block4;
                    }
                    case ']': {
                        return;
                    }
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
            for (Object o : collection) {
                this.myArrayList.add(JSONObject.wrap(o));
            }
        }
    }

    public JSONArray(Object array) throws JSONException {
        this();
        if (array.getClass().isArray()) {
            int length = Array.getLength(array);
            this.myArrayList.ensureCapacity(length);
            for (int i = 0; i < length; ++i) {
                this.put(JSONObject.wrap(Array.get(array, i)));
            }
        } else {
            throw new JSONException("JSONArray initial value should be a string or collection or array.");
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return this.myArrayList.iterator();
    }

    public Object get(int index) throws JSONException {
        Object object = this.opt(index);
        if (object == null) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        return object;
    }

    public boolean getBoolean(int index) throws JSONException {
        Object object = this.get(index);
        if (object.equals(Boolean.FALSE) || object instanceof String && ((String)object).equalsIgnoreCase("false")) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || object instanceof String && ((String)object).equalsIgnoreCase("true")) {
            return true;
        }
        throw new JSONException("JSONArray[" + index + "] is not a boolean.");
    }

    public double getDouble(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }

    public float getFloat(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? ((Number)object).floatValue() : Float.parseFloat(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }

    public Number getNumber(int index) throws JSONException {
        Object object = this.get(index);
        try {
            if (object instanceof Number) {
                return (Number)object;
            }
            return JSONObject.stringToNumber(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }

    public <E extends Enum<E>> E getEnum(Class<E> clazz, int index) throws JSONException {
        E val = this.optEnum(clazz, index);
        if (val == null) {
            throw new JSONException("JSONArray[" + index + "] is not an enum of type " + JSONObject.quote(clazz.getSimpleName()) + ".");
        }
        return val;
    }

    public BigDecimal getBigDecimal(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return new BigDecimal(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] could not convert to BigDecimal.", e);
        }
    }

    public BigInteger getBigInteger(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return new BigInteger(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] could not convert to BigInteger.", e);
        }
    }

    public int getInt(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }

    public JSONArray getJSONArray(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
    }

    public long getLong(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }

    public String getString(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        }
        throw new JSONException("JSONArray[" + index + "] not a string.");
    }

    public boolean isNull(int index) {
        return JSONObject.NULL.equals(this.opt(index));
    }

    public String join(String separator) throws JSONException {
        int len = this.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }

    public int length() {
        return this.myArrayList.size();
    }

    public Object opt(int index) {
        return index < 0 || index >= this.length() ? null : this.myArrayList.get(index);
    }

    public boolean optBoolean(int index) {
        return this.optBoolean(index, false);
    }

    public boolean optBoolean(int index, boolean defaultValue) {
        try {
            return this.getBoolean(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(int index) {
        return this.optDouble(index, Double.NaN);
    }

    public double optDouble(int index, double defaultValue) {
        Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).doubleValue();
        }
        if (val instanceof String) {
            try {
                return Double.parseDouble((String)val);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public float optFloat(int index) {
        return this.optFloat(index, Float.NaN);
    }

    public float optFloat(int index, float defaultValue) {
        Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).floatValue();
        }
        if (val instanceof String) {
            try {
                return Float.parseFloat((String)val);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public int optInt(int index) {
        return this.optInt(index, 0);
    }

    public int optInt(int index, int defaultValue) {
        Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).intValue();
        }
        if (val instanceof String) {
            try {
                return new BigDecimal(val.toString()).intValue();
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public <E extends Enum<E>> E optEnum(Class<E> clazz, int index) {
        return this.optEnum(clazz, index, null);
    }

    public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue) {
        try {
            Object val = this.opt(index);
            if (JSONObject.NULL.equals(val)) {
                return defaultValue;
            }
            if (clazz.isAssignableFrom(val.getClass())) {
                Enum myE = (Enum)val;
                return (E)myE;
            }
            return Enum.valueOf(clazz, val.toString());
        }
        catch (IllegalArgumentException e) {
            return defaultValue;
        }
        catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public BigInteger optBigInteger(int index, BigInteger defaultValue) {
        Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof BigInteger) {
            return (BigInteger)val;
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal)val).toBigInteger();
        }
        if (val instanceof Double || val instanceof Float) {
            return new BigDecimal(((Number)val).doubleValue()).toBigInteger();
        }
        if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte) {
            return BigInteger.valueOf(((Number)val).longValue());
        }
        try {
            String valStr = val.toString();
            if (JSONObject.isDecimalNotation(valStr)) {
                return new BigDecimal(valStr).toBigInteger();
            }
            return new BigInteger(valStr);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public BigDecimal optBigDecimal(int index, BigDecimal defaultValue) {
        Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof BigDecimal) {
            return (BigDecimal)val;
        }
        if (val instanceof BigInteger) {
            return new BigDecimal((BigInteger)val);
        }
        if (val instanceof Double || val instanceof Float) {
            return new BigDecimal(((Number)val).doubleValue());
        }
        if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte) {
            return new BigDecimal(((Number)val).longValue());
        }
        try {
            return new BigDecimal(val.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
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
        Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).longValue();
        }
        if (val instanceof String) {
            try {
                return new BigDecimal(val.toString()).longValue();
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public Number optNumber(int index) {
        return this.optNumber(index, null);
    }

    public Number optNumber(int index, Number defaultValue) {
        Object val = this.opt(index);
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
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public String optString(int index) {
        return this.optString(index, "");
    }

    public String optString(int index, String defaultValue) {
        Object object = this.opt(index);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }

    public JSONArray put(boolean value) {
        this.put(value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONArray put(Collection<?> value) {
        this.put(new JSONArray(value));
        return this;
    }

    public JSONArray put(double value) throws JSONException {
        Double d = new Double(value);
        JSONObject.testValidity(d);
        this.put(d);
        return this;
    }

    public JSONArray put(int value) {
        this.put(new Integer(value));
        return this;
    }

    public JSONArray put(long value) {
        this.put(new Long(value));
        return this;
    }

    public JSONArray put(Map<?, ?> value) {
        this.put(new JSONObject(value));
        return this;
    }

    public JSONArray put(Object value) {
        this.myArrayList.add(value);
        return this;
    }

    public JSONArray put(int index, boolean value) throws JSONException {
        this.put(index, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONArray put(int index, Collection<?> value) throws JSONException {
        this.put(index, new JSONArray(value));
        return this;
    }

    public JSONArray put(int index, double value) throws JSONException {
        this.put(index, new Double(value));
        return this;
    }

    public JSONArray put(int index, int value) throws JSONException {
        this.put(index, new Integer(value));
        return this;
    }

    public JSONArray put(int index, long value) throws JSONException {
        this.put(index, new Long(value));
        return this;
    }

    public JSONArray put(int index, Map<?, ?> value) throws JSONException {
        this.put(index, new JSONObject(value));
        return this;
    }

    public JSONArray put(int index, Object value) throws JSONException {
        JSONObject.testValidity(value);
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < this.length()) {
            this.myArrayList.set(index, value);
        } else if (index == this.length()) {
            this.put(value);
        } else {
            this.myArrayList.ensureCapacity(index + 1);
            while (index != this.length()) {
                this.put(JSONObject.NULL);
            }
            this.put(value);
        }
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
        }
        catch (JSONPointerException e) {
            return null;
        }
    }

    public Object remove(int index) {
        return index >= 0 && index < this.length() ? this.myArrayList.remove(index) : null;
    }

    public boolean similar(Object other) {
        if (!(other instanceof JSONArray)) {
            return false;
        }
        int len = this.length();
        if (len != ((JSONArray)other).length()) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            Object valueOther;
            Object valueThis = this.myArrayList.get(i);
            if (valueThis == (valueOther = ((JSONArray)other).myArrayList.get(i))) {
                return true;
            }
            if (valueThis == null) {
                return false;
            }
            if (!(valueThis instanceof JSONObject ? !((JSONObject)valueThis).similar(valueOther) : (valueThis instanceof JSONArray ? !((JSONArray)valueThis).similar(valueOther) : !valueThis.equals(valueOther)))) continue;
            return false;
        }
        return true;
    }

    public JSONObject toJSONObject(JSONArray names) throws JSONException {
        if (names == null || names.length() == 0 || this.length() == 0) {
            return null;
        }
        JSONObject jo = new JSONObject(names.length());
        for (int i = 0; i < names.length(); ++i) {
            jo.put(names.getString(i), this.opt(i));
        }
        return jo;
    }

    public String toString() {
        try {
            return this.toString(0);
        }
        catch (Exception e) {
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String toString(int indentFactor) throws JSONException {
        StringWriter sw = new StringWriter();
        StringBuffer stringBuffer = sw.getBuffer();
        synchronized (stringBuffer) {
            return this.write(sw, indentFactor, 0).toString();
        }
    }

    public Writer write(Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }

    public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            boolean commanate = false;
            int length = this.length();
            writer.write(91);
            if (length == 1) {
                try {
                    JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
                }
                catch (Exception e) {
                    throw new JSONException("Unable to write JSONArray value at index: 0", e);
                }
            }
            if (length != 0) {
                int newindent = indent + indentFactor;
                for (int i = 0; i < length; ++i) {
                    if (commanate) {
                        writer.write(44);
                    }
                    if (indentFactor > 0) {
                        writer.write(10);
                    }
                    JSONObject.indent(writer, newindent);
                    try {
                        JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent);
                    }
                    catch (Exception e) {
                        throw new JSONException("Unable to write JSONArray value at index: " + i, e);
                    }
                    commanate = true;
                }
                if (indentFactor > 0) {
                    writer.write(10);
                }
                JSONObject.indent(writer, indent);
            }
            writer.write(93);
            return writer;
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public List<Object> toList() {
        ArrayList<Object> results = new ArrayList<Object>(this.myArrayList.size());
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
}

