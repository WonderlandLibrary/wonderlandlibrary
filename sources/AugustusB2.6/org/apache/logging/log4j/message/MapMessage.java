// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.EnglishEnums;
import java.util.Iterator;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.IndexedStringMap;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.MultiFormatStringBuilderFormattable;

@AsynchronouslyFormattable
@PerformanceSensitive({ "allocation" })
public class MapMessage<M extends MapMessage<M, V>, V> implements MultiFormatStringBuilderFormattable
{
    private static final long serialVersionUID = -5031471831131487120L;
    private final IndexedStringMap data;
    
    public MapMessage() {
        this.data = new SortedArrayStringMap();
    }
    
    public MapMessage(final int initialCapacity) {
        this.data = new SortedArrayStringMap(initialCapacity);
    }
    
    public MapMessage(final Map<String, V> map) {
        this.data = new SortedArrayStringMap(map);
    }
    
    @Override
    public String[] getFormats() {
        return MapFormat.names();
    }
    
    @Override
    public Object[] getParameters() {
        final Object[] result = new Object[this.data.size()];
        for (int i = 0; i < this.data.size(); ++i) {
            result[i] = this.data.getValueAt(i);
        }
        return result;
    }
    
    @Override
    public String getFormat() {
        return "";
    }
    
    public Map<String, V> getData() {
        final TreeMap<String, V> result = new TreeMap<String, V>();
        for (int i = 0; i < this.data.size(); ++i) {
            result.put(this.data.getKeyAt(i), this.data.getValueAt(i));
        }
        return Collections.unmodifiableMap((Map<? extends String, ? extends V>)result);
    }
    
    public IndexedReadOnlyStringMap getIndexedReadOnlyStringMap() {
        return this.data;
    }
    
    public void clear() {
        this.data.clear();
    }
    
    public boolean containsKey(final String key) {
        return this.data.containsKey(key);
    }
    
    public void put(final String candidateKey, final String value) {
        if (value == null) {
            throw new IllegalArgumentException("No value provided for key " + candidateKey);
        }
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
    }
    
    public void putAll(final Map<String, String> map) {
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            this.data.putValue(entry.getKey(), entry.getValue());
        }
    }
    
    public String get(final String key) {
        final Object result = this.data.getValue(key);
        return ParameterFormatter.deepToString(result);
    }
    
    public String remove(final String key) {
        final String result = this.get(key);
        this.data.remove(key);
        return result;
    }
    
    public String asString() {
        return this.format(null, new StringBuilder()).toString();
    }
    
    public String asString(final String format) {
        try {
            return this.format(EnglishEnums.valueOf(MapFormat.class, format), new StringBuilder()).toString();
        }
        catch (IllegalArgumentException ex) {
            return this.asString();
        }
    }
    
    public <CV> void forEach(final BiConsumer<String, ? super CV> action) {
        this.data.forEach((BiConsumer<String, ? super Object>)action);
    }
    
    public <CV, S> void forEach(final TriConsumer<String, ? super CV, S> action, final S state) {
        this.data.forEach((TriConsumer<String, ? super Object, S>)action, state);
    }
    
    private StringBuilder format(final MapFormat format, final StringBuilder sb) {
        if (format == null) {
            this.appendMap(sb);
        }
        else {
            switch (format) {
                case XML: {
                    this.asXml(sb);
                    break;
                }
                case JSON: {
                    this.asJson(sb);
                    break;
                }
                case JAVA: {
                    this.asJava(sb);
                    break;
                }
                case JAVA_UNQUOTED: {
                    this.asJavaUnquoted(sb);
                    break;
                }
                default: {
                    this.appendMap(sb);
                    break;
                }
            }
        }
        return sb;
    }
    
    public void asXml(final StringBuilder sb) {
        sb.append("<Map>\n");
        for (int i = 0; i < this.data.size(); ++i) {
            sb.append("  <Entry key=\"").append(this.data.getKeyAt(i)).append("\">");
            final int size = sb.length();
            ParameterFormatter.recursiveDeepToString(this.data.getValueAt(i), sb);
            StringBuilders.escapeXml(sb, size);
            sb.append("</Entry>\n");
        }
        sb.append("</Map>");
    }
    
    @Override
    public String getFormattedMessage() {
        return this.asString();
    }
    
    @Override
    public String getFormattedMessage(final String[] formats) {
        return this.format(this.getFormat(formats), new StringBuilder()).toString();
    }
    
    private MapFormat getFormat(final String[] formats) {
        if (formats == null || formats.length == 0) {
            return null;
        }
        for (int i = 0; i < formats.length; ++i) {
            final MapFormat mapFormat = MapFormat.lookupIgnoreCase(formats[i]);
            if (mapFormat != null) {
                return mapFormat;
            }
        }
        return null;
    }
    
    protected void appendMap(final StringBuilder sb) {
        for (int i = 0; i < this.data.size(); ++i) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(this.data.getKeyAt(i)).append('=').append('\"');
            ParameterFormatter.recursiveDeepToString(this.data.getValueAt(i), sb);
            sb.append('\"');
        }
    }
    
    protected void asJson(final StringBuilder sb) {
        MapMessageJsonFormatter.format(sb, this.data);
    }
    
    protected void asJavaUnquoted(final StringBuilder sb) {
        this.asJava(sb, false);
    }
    
    protected void asJava(final StringBuilder sb) {
        this.asJava(sb, true);
    }
    
    private void asJava(final StringBuilder sb, final boolean quoted) {
        sb.append('{');
        for (int i = 0; i < this.data.size(); ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.data.getKeyAt(i)).append('=');
            if (quoted) {
                sb.append('\"');
            }
            ParameterFormatter.recursiveDeepToString(this.data.getValueAt(i), sb);
            if (quoted) {
                sb.append('\"');
            }
        }
        sb.append('}');
    }
    
    public M newInstance(final Map<String, V> map) {
        return (M)new MapMessage(map);
    }
    
    @Override
    public String toString() {
        return this.asString();
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        this.format(null, buffer);
    }
    
    @Override
    public void formatTo(final String[] formats, final StringBuilder buffer) {
        this.format(this.getFormat(formats), buffer);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MapMessage<?, ?> that = (MapMessage<?, ?>)o;
        return this.data.equals(that.data);
    }
    
    @Override
    public int hashCode() {
        return this.data.hashCode();
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    protected void validate(final String key, final boolean value) {
    }
    
    protected void validate(final String key, final byte value) {
    }
    
    protected void validate(final String key, final char value) {
    }
    
    protected void validate(final String key, final double value) {
    }
    
    protected void validate(final String key, final float value) {
    }
    
    protected void validate(final String key, final int value) {
    }
    
    protected void validate(final String key, final long value) {
    }
    
    protected void validate(final String key, final Object value) {
    }
    
    protected void validate(final String key, final short value) {
    }
    
    protected void validate(final String key, final String value) {
    }
    
    protected String toKey(final String candidateKey) {
        return candidateKey;
    }
    
    public M with(final String candidateKey, final boolean value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final byte value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final char value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final double value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final float value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final int value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final long value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final Object value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final short value) {
        final String key = this.toKey(candidateKey);
        this.validate(key, value);
        this.data.putValue(key, value);
        return (M)this;
    }
    
    public M with(final String candidateKey, final String value) {
        final String key = this.toKey(candidateKey);
        this.put(key, value);
        return (M)this;
    }
    
    public enum MapFormat
    {
        XML, 
        JSON, 
        JAVA, 
        JAVA_UNQUOTED;
        
        public static MapFormat lookupIgnoreCase(final String format) {
            return MapFormat.XML.name().equalsIgnoreCase(format) ? MapFormat.XML : (MapFormat.JSON.name().equalsIgnoreCase(format) ? MapFormat.JSON : (MapFormat.JAVA.name().equalsIgnoreCase(format) ? MapFormat.JAVA : (MapFormat.JAVA_UNQUOTED.name().equalsIgnoreCase(format) ? MapFormat.JAVA_UNQUOTED : null)));
        }
        
        public static String[] names() {
            return new String[] { MapFormat.XML.name(), MapFormat.JSON.name(), MapFormat.JAVA.name(), MapFormat.JAVA_UNQUOTED.name() };
        }
    }
}
