// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Collection;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class JSONObject
{
    private final Map<String, Object> map;
    public static final Object NULL;
    
    public JSONObject() {
        this.map = new HashMap<String, Object>();
    }
    
    public JSONObject(final JSONObject jo, final String[] names) {
        this();
        for (int i = 0; i < names.length; ++i) {
            try {
                this.putOnce(names[i], jo.opt(names[i]));
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject(final JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        while (true) {
            char c = x.nextClean();
            switch (c) {
                case '\0': {
                    throw x.syntaxError("A JSONObject text must end with '}'");
                }
                case '}': {}
                default: {
                    x.back();
                    final String key = x.nextValue().toString();
                    c = x.nextClean();
                    if (c != ':') {
                        throw x.syntaxError("Expected a ':' after a key");
                    }
                    this.putOnce(key, x.nextValue());
                    switch (x.nextClean()) {
                        case ',':
                        case ';': {
                            if (x.nextClean() == '}') {
                                return;
                            }
                            x.back();
                            continue;
                        }
                        case '}': {
                            return;
                        }
                        default: {
                            throw x.syntaxError("Expected a ',' or '}'");
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public JSONObject(final Map<?, ?> map) {
        this.map = new HashMap<String, Object>();
        if (map != null) {
            for (final Map.Entry<?, ?> e : map.entrySet()) {
                final Object value = e.getValue();
                if (value != null) {
                    this.map.put(String.valueOf(e.getKey()), wrap(value));
                }
            }
        }
    }
    
    public JSONObject(final Object bean) {
        this();
        this.populateMap(bean);
    }
    
    public JSONObject(final Object object, final String[] names) {
        this();
        final Class<?> c = object.getClass();
        for (int i = 0; i < names.length; ++i) {
            final String name = names[i];
            try {
                this.putOpt(name, c.getField(name).get(object));
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject(final String source) throws JSONException {
        this(new JSONTokener(source));
    }
    
    public JSONObject(final String baseName, final Locale locale) throws JSONException {
        this();
        final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());
        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            final Object key = keys.nextElement();
            if (key != null) {
                final String[] path = ((String)key).split("\\.");
                final int last = path.length - 1;
                JSONObject target = this;
                for (final String segment : path) {
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
    
    public JSONObject accumulate(final String key, final Object value) throws JSONException {
        testValidity(value);
        final Object object = this.opt(key);
        if (object == null) {
            this.put(key, (value instanceof JSONArray) ? new JSONArray().put(value) : value);
        }
        else if (object instanceof JSONArray) {
            ((JSONArray)object).put(value);
        }
        else {
            this.put(key, new JSONArray().put(object).put(value));
        }
        return this;
    }
    
    public JSONObject append(final String key, final Object value) throws JSONException {
        testValidity(value);
        final Object object = this.opt(key);
        if (object == null) {
            this.put(key, new JSONArray().put(value));
        }
        else {
            if (!(object instanceof JSONArray)) {
                throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
            }
            this.put(key, ((JSONArray)object).put(value));
        }
        return this;
    }
    
    public static String doubleToString(final double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String string = Double.toString(d);
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }
            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }
    
    public Object get(final String key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        final Object object = this.opt(key);
        if (object == null) {
            throw new JSONException("JSONObject[" + quote(key) + "] not found.");
        }
        return object;
    }
    
    public <E extends Enum<E>> E getEnum(final Class<E> clazz, final String key) throws JSONException {
        final E val = (E)this.optEnum((Class<Enum>)clazz, key);
        if (val == null) {
            throw new JSONException("JSONObject[" + quote(key) + "] is not an enum of type " + quote(clazz.getSimpleName()) + ".");
        }
        return val;
    }
    
    public boolean getBoolean(final String key) throws JSONException {
        final Object object = this.get(key);
        if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] is not a Boolean.");
    }
    
    public BigInteger getBigInteger(final String key) throws JSONException {
        final Object object = this.get(key);
        try {
            return new BigInteger(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) + "] could not be converted to BigInteger.");
        }
    }
    
    public BigDecimal getBigDecimal(final String key) throws JSONException {
        final Object object = this.get(key);
        try {
            return new BigDecimal(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) + "] could not be converted to BigDecimal.");
        }
    }
    
    public double getDouble(final String key) throws JSONException {
        final Object object = this.get(key);
        try {
            return (object instanceof Number) ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) + "] is not a number.");
        }
    }
    
    public int getInt(final String key) throws JSONException {
        final Object object = this.get(key);
        try {
            return (object instanceof Number) ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) + "] is not an int.");
        }
    }
    
    public JSONArray getJSONArray(final String key) throws JSONException {
        final Object object = this.get(key);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final String key) throws JSONException {
        final Object object = this.get(key);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONObject.");
    }
    
    public long getLong(final String key) throws JSONException {
        final Object object = this.get(key);
        try {
            return (object instanceof Number) ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) + "] is not a long.");
        }
    }
    
    public static String[] getNames(final JSONObject jo) {
        final int length = jo.length();
        if (length == 0) {
            return null;
        }
        final Iterator<String> iterator = jo.keys();
        final String[] names = new String[length];
        int i = 0;
        while (iterator.hasNext()) {
            names[i] = iterator.next();
            ++i;
        }
        return names;
    }
    
    public static String[] getNames(final Object object) {
        if (object == null) {
            return null;
        }
        final Class<?> klass = object.getClass();
        final Field[] fields = klass.getFields();
        final int length = fields.length;
        if (length == 0) {
            return null;
        }
        final String[] names = new String[length];
        for (int i = 0; i < length; ++i) {
            names[i] = fields[i].getName();
        }
        return names;
    }
    
    public String getString(final String key) throws JSONException {
        final Object object = this.get(key);
        if (object instanceof String) {
            return (String)object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] not a string.");
    }
    
    public boolean has(final String key) {
        return this.map.containsKey(key);
    }
    
    public JSONObject increment(final String key) throws JSONException {
        final Object value = this.opt(key);
        if (value == null) {
            this.put(key, 1);
        }
        else if (value instanceof BigInteger) {
            this.put(key, ((BigInteger)value).add(BigInteger.ONE));
        }
        else if (value instanceof BigDecimal) {
            this.put(key, ((BigDecimal)value).add(BigDecimal.ONE));
        }
        else if (value instanceof Integer) {
            this.put(key, (int)value + 1);
        }
        else if (value instanceof Long) {
            this.put(key, (long)value + 1L);
        }
        else if (value instanceof Double) {
            this.put(key, (double)value + 1.0);
        }
        else {
            if (!(value instanceof Float)) {
                throw new JSONException("Unable to increment [" + quote(key) + "].");
            }
            this.put(key, (float)value + 1.0f);
        }
        return this;
    }
    
    public boolean isNull(final String key) {
        return JSONObject.NULL.equals(this.opt(key));
    }
    
    public Iterator<String> keys() {
        return this.keySet().iterator();
    }
    
    public Set<String> keySet() {
        return this.map.keySet();
    }
    
    public int length() {
        return this.map.size();
    }
    
    public JSONArray names() {
        final JSONArray ja = new JSONArray();
        final Iterator<String> keys = this.keys();
        while (keys.hasNext()) {
            ja.put(keys.next());
        }
        return (ja.length() == 0) ? null : ja;
    }
    
    public static String numberToString(final Number number) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(number);
        String string = number.toString();
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }
            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }
    
    public Object opt(final String key) {
        return (key == null) ? null : this.map.get(key);
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> clazz, final String key) {
        return this.optEnum(clazz, key, (E)null);
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> clazz, final String key, final E defaultValue) {
        try {
            final Object val = this.opt(key);
            if (JSONObject.NULL.equals(val)) {
                return defaultValue;
            }
            if (clazz.isAssignableFrom(val.getClass())) {
                final E myE = (E)val;
                return myE;
            }
            return Enum.valueOf(clazz, val.toString());
        }
        catch (IllegalArgumentException e) {
            return defaultValue;
        }
        catch (NullPointerException e2) {
            return defaultValue;
        }
    }
    
    public boolean optBoolean(final String key) {
        return this.optBoolean(key, false);
    }
    
    public boolean optBoolean(final String key, final boolean defaultValue) {
        try {
            return this.getBoolean(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public double optDouble(final String key) {
        return this.optDouble(key, Double.NaN);
    }
    
    public BigInteger optBigInteger(final String key, final BigInteger defaultValue) {
        try {
            return this.getBigInteger(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public BigDecimal optBigDecimal(final String key, final BigDecimal defaultValue) {
        try {
            return this.getBigDecimal(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public double optDouble(final String key, final double defaultValue) {
        try {
            return this.getDouble(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public int optInt(final String key) {
        return this.optInt(key, 0);
    }
    
    public int optInt(final String key, final int defaultValue) {
        try {
            return this.getInt(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public JSONArray optJSONArray(final String key) {
        final Object o = this.opt(key);
        return (o instanceof JSONArray) ? ((JSONArray)o) : null;
    }
    
    public JSONObject optJSONObject(final String key) {
        final Object object = this.opt(key);
        return (object instanceof JSONObject) ? ((JSONObject)object) : null;
    }
    
    public long optLong(final String key) {
        return this.optLong(key, 0L);
    }
    
    public long optLong(final String key, final long defaultValue) {
        try {
            return this.getLong(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public String optString(final String key) {
        return this.optString(key, "");
    }
    
    public String optString(final String key, final String defaultValue) {
        final Object object = this.opt(key);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }
    
    private void populateMap(final Object bean) {
        final Class<?> klass = bean.getClass();
        final boolean includeSuperClass = klass.getClassLoader() != null;
        final Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            try {
                final Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    final String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        if ("getClass".equals(name) || "getDeclaringClass".equals(name)) {
                            key = "";
                        }
                        else {
                            key = name.substring(3);
                        }
                    }
                    else if (name.startsWith("is")) {
                        key = name.substring(2);
                    }
                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        }
                        else if (!Character.isUpperCase(key.charAt(1))) {
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        }
                        final Object result = method.invoke(bean, (Object[])null);
                        if (result != null) {
                            this.map.put(key, wrap(result));
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject put(final String key, final boolean value) throws JSONException {
        this.put(key, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONObject put(final String key, final Collection<?> value) throws JSONException {
        this.put(key, new JSONArray(value));
        return this;
    }
    
    public JSONObject put(final String key, final double value) throws JSONException {
        this.put(key, new Double(value));
        return this;
    }
    
    public JSONObject put(final String key, final int value) throws JSONException {
        this.put(key, new Integer(value));
        return this;
    }
    
    public JSONObject put(final String key, final long value) throws JSONException {
        this.put(key, new Long(value));
        return this;
    }
    
    public JSONObject put(final String key, final Map<?, ?> value) throws JSONException {
        this.put(key, new JSONObject(value));
        return this;
    }
    
    public JSONObject put(final String key, final Object value) throws JSONException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.map.put(key, value);
        }
        else {
            this.remove(key);
        }
        return this;
    }
    
    public JSONObject putOnce(final String key, final Object value) throws JSONException {
        if (key != null && value != null) {
            if (this.opt(key) != null) {
                throw new JSONException("Duplicate key \"" + key + "\"");
            }
            this.put(key, value);
        }
        return this;
    }
    
    public JSONObject putOpt(final String key, final Object value) throws JSONException {
        if (key != null && value != null) {
            this.put(key, value);
        }
        return this;
    }
    
    public Object query(final String jsonPointer) {
        return new JSONPointer(jsonPointer).queryFrom(this);
    }
    
    public Object optQuery(final String jsonPointer) {
        final JSONPointer pointer = new JSONPointer(jsonPointer);
        try {
            return pointer.queryFrom(this);
        }
        catch (JSONPointerException e) {
            return null;
        }
    }
    
    public static String quote(final String string) {
        final StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            try {
                return quote(string, sw).toString();
            }
            catch (IOException ignored) {
                return "";
            }
        }
    }
    
    public static Writer quote(final String string, final Writer w) throws IOException {
        if (string == null || string.length() == 0) {
            w.write("\"\"");
            return w;
        }
        char c = '\0';
        final int len = string.length();
        w.write(34);
        for (int i = 0; i < len; ++i) {
            final char b = c;
            c = string.charAt(i);
            switch (c) {
                case '\"':
                case '\\': {
                    w.write(92);
                    w.write(c);
                    break;
                }
                case '/': {
                    if (b == '<') {
                        w.write(92);
                    }
                    w.write(c);
                    break;
                }
                case '\b': {
                    w.write("\\b");
                    break;
                }
                case '\t': {
                    w.write("\\t");
                    break;
                }
                case '\n': {
                    w.write("\\n");
                    break;
                }
                case '\f': {
                    w.write("\\f");
                    break;
                }
                case '\r': {
                    w.write("\\r");
                    break;
                }
                default: {
                    if (c < ' ' || (c >= '\u0080' && c < ' ') || (c >= '\u2000' && c < '\u2100')) {
                        w.write("\\u");
                        final String hhhh = Integer.toHexString(c);
                        w.write("0000", 0, 4 - hhhh.length());
                        w.write(hhhh);
                        break;
                    }
                    w.write(c);
                    break;
                }
            }
        }
        w.write(34);
        return w;
    }
    
    public Object remove(final String key) {
        return this.map.remove(key);
    }
    
    public boolean similar(final Object other) {
        try {
            if (!(other instanceof JSONObject)) {
                return false;
            }
            final Set<String> set = this.keySet();
            if (!set.equals(((JSONObject)other).keySet())) {
                return false;
            }
            for (final String name : set) {
                final Object valueThis = this.get(name);
                final Object valueOther = ((JSONObject)other).get(name);
                if (valueThis instanceof JSONObject) {
                    if (!((JSONObject)valueThis).similar(valueOther)) {
                        return false;
                    }
                    continue;
                }
                else if (valueThis instanceof JSONArray) {
                    if (!((JSONArray)valueThis).similar(valueOther)) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (!valueThis.equals(valueOther)) {
                        return false;
                    }
                    continue;
                }
            }
            return true;
        }
        catch (Throwable exception) {
            return false;
        }
    }
    
    public static Object stringToValue(final String string) {
        if (string.equals("")) {
            return string;
        }
        if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("null")) {
            return JSONObject.NULL;
        }
        final char initial = string.charAt(0);
        if (initial < '0' || initial > '9') {
            if (initial != '-') {
                return string;
            }
        }
        try {
            if (string.indexOf(46) > -1 || string.indexOf(101) > -1 || string.indexOf(69) > -1 || "-0".equals(string)) {
                final Double d = Double.valueOf(string);
                if (!d.isInfinite() && !d.isNaN()) {
                    return d;
                }
            }
            else {
                final Long myLong = new Long(string);
                if (string.equals(myLong.toString())) {
                    if (myLong == myLong.intValue()) {
                        return myLong.intValue();
                    }
                    return myLong;
                }
            }
        }
        catch (Exception ex) {}
        return string;
    }
    
    public static void testValidity(final Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
            else if (o instanceof Float && (((Float)o).isInfinite() || ((Float)o).isNaN())) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
    }
    
    public JSONArray toJSONArray(final JSONArray names) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        final JSONArray ja = new JSONArray();
        for (int i = 0; i < names.length(); ++i) {
            ja.put(this.opt(names.getString(i)));
        }
        return ja;
    }
    
    @Override
    public String toString() {
        try {
            return this.toString(0);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String toString(final int indentFactor) throws JSONException {
        final StringWriter w = new StringWriter();
        synchronized (w.getBuffer()) {
            return this.write(w, indentFactor, 0).toString();
        }
    }
    
    public static String valueToString(final Object value) throws JSONException {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof JSONString) {
            Object object;
            try {
                object = ((JSONString)value).toJSONString();
            }
            catch (Exception e) {
                throw new JSONException(e);
            }
            if (object instanceof String) {
                return (String)object;
            }
            throw new JSONException("Bad value from toJSONString: " + object);
        }
        else {
            if (value instanceof Number) {
                return numberToString((Number)value);
            }
            if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
                return value.toString();
            }
            if (value instanceof Map) {
                final Map<?, ?> map = (Map<?, ?>)value;
                return new JSONObject(map).toString();
            }
            if (value instanceof Collection) {
                final Collection<?> coll = (Collection<?>)value;
                return new JSONArray(coll).toString();
            }
            if (value.getClass().isArray()) {
                return new JSONArray(value).toString();
            }
            return quote(value.toString());
        }
    }
    
    public static Object wrap(final Object object) {
        try {
            if (object == null) {
                return JSONObject.NULL;
            }
            if (object instanceof JSONObject || object instanceof JSONArray || JSONObject.NULL.equals(object) || object instanceof JSONString || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String || object instanceof BigInteger || object instanceof BigDecimal) {
                return object;
            }
            if (object instanceof Collection) {
                final Collection<?> coll = (Collection<?>)object;
                return new JSONArray(coll);
            }
            if (object.getClass().isArray()) {
                return new JSONArray(object);
            }
            if (object instanceof Map) {
                final Map<?, ?> map = (Map<?, ?>)object;
                return new JSONObject(map);
            }
            final Package objectPackage = object.getClass().getPackage();
            final String objectPackageName = (objectPackage != null) ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JSONObject(object);
        }
        catch (Exception exception) {
            return null;
        }
    }
    
    public Writer write(final Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }
    
    static final Writer writeValue(final Writer writer, final Object value, final int indentFactor, final int indent) throws JSONException, IOException {
        if (value == null || value.equals(null)) {
            writer.write("null");
        }
        else if (value instanceof JSONObject) {
            ((JSONObject)value).write(writer, indentFactor, indent);
        }
        else if (value instanceof JSONArray) {
            ((JSONArray)value).write(writer, indentFactor, indent);
        }
        else if (value instanceof Map) {
            final Map<?, ?> map = (Map<?, ?>)value;
            new JSONObject(map).write(writer, indentFactor, indent);
        }
        else if (value instanceof Collection) {
            final Collection<?> coll = (Collection<?>)value;
            new JSONArray(coll).write(writer, indentFactor, indent);
        }
        else if (value.getClass().isArray()) {
            new JSONArray(value).write(writer, indentFactor, indent);
        }
        else if (value instanceof Number) {
            writer.write(numberToString((Number)value));
        }
        else if (value instanceof Boolean) {
            writer.write(value.toString());
        }
        else if (value instanceof JSONString) {
            Object o;
            try {
                o = ((JSONString)value).toJSONString();
            }
            catch (Exception e) {
                throw new JSONException(e);
            }
            writer.write((o != null) ? o.toString() : quote(value.toString()));
        }
        else {
            quote(value.toString(), writer);
        }
        return writer;
    }
    
    static final void indent(final Writer writer, final int indent) throws IOException {
        for (int i = 0; i < indent; ++i) {
            writer.write(32);
        }
    }
    
    public Writer write(final Writer writer, final int indentFactor, final int indent) throws JSONException {
        try {
            boolean commanate = false;
            final int length = this.length();
            final Iterator<String> keys = this.keys();
            writer.write(123);
            if (length == 1) {
                final Object key = keys.next();
                writer.write(quote(key.toString()));
                writer.write(58);
                if (indentFactor > 0) {
                    writer.write(32);
                }
                writeValue(writer, this.map.get(key), indentFactor, indent);
            }
            else if (length != 0) {
                final int newindent = indent + indentFactor;
                while (keys.hasNext()) {
                    final Object key2 = keys.next();
                    if (commanate) {
                        writer.write(44);
                    }
                    if (indentFactor > 0) {
                        writer.write(10);
                    }
                    indent(writer, newindent);
                    writer.write(quote(key2.toString()));
                    writer.write(58);
                    if (indentFactor > 0) {
                        writer.write(32);
                    }
                    writeValue(writer, this.map.get(key2), indentFactor, newindent);
                    commanate = true;
                }
                if (indentFactor > 0) {
                    writer.write(10);
                }
                indent(writer, indent);
            }
            writer.write(125);
            return writer;
        }
        catch (IOException exception) {
            throw new JSONException(exception);
        }
    }
    
    public Map<String, Object> toMap() {
        final Map<String, Object> results = new HashMap<String, Object>();
        for (final Map.Entry<String, Object> entry : this.map.entrySet()) {
            Object value;
            if (entry.getValue() == null || JSONObject.NULL.equals(entry.getValue())) {
                value = null;
            }
            else if (entry.getValue() instanceof JSONObject) {
                value = entry.getValue().toMap();
            }
            else if (entry.getValue() instanceof JSONArray) {
                value = entry.getValue().toList();
            }
            else {
                value = entry.getValue();
            }
            results.put(entry.getKey(), value);
        }
        return results;
    }
    
    static {
        NULL = new Null();
    }
    
    private static final class Null
    {
        @Override
        protected final Object clone() {
            return this;
        }
        
        @Override
        public boolean equals(final Object object) {
            return object == null || object == this;
        }
        
        @Override
        public String toString() {
            return "null";
        }
    }
}
