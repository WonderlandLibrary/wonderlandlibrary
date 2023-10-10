// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.EnglishEnums;
import java.util.Map;

@AsynchronouslyFormattable
public class StructuredDataMessage extends MapMessage<StructuredDataMessage, String>
{
    private static final long serialVersionUID = 1703221292892071920L;
    private static final int MAX_LENGTH = 32;
    private static final int HASHVAL = 31;
    private StructuredDataId id;
    private String message;
    private String type;
    private final int maxLength;
    
    public StructuredDataMessage(final String id, final String msg, final String type) {
        this(id, msg, type, 32);
    }
    
    public StructuredDataMessage(final String id, final String msg, final String type, final int maxLength) {
        this.id = new StructuredDataId(id, null, null, maxLength);
        this.message = msg;
        this.type = type;
        this.maxLength = maxLength;
    }
    
    public StructuredDataMessage(final String id, final String msg, final String type, final Map<String, String> data) {
        this(id, msg, type, data, 32);
    }
    
    public StructuredDataMessage(final String id, final String msg, final String type, final Map<String, String> data, final int maxLength) {
        super(data);
        this.id = new StructuredDataId(id, null, null, maxLength);
        this.message = msg;
        this.type = type;
        this.maxLength = maxLength;
    }
    
    public StructuredDataMessage(final StructuredDataId id, final String msg, final String type) {
        this(id, msg, type, 32);
    }
    
    public StructuredDataMessage(final StructuredDataId id, final String msg, final String type, final int maxLength) {
        this.id = id;
        this.message = msg;
        this.type = type;
        this.maxLength = maxLength;
    }
    
    public StructuredDataMessage(final StructuredDataId id, final String msg, final String type, final Map<String, String> data) {
        this(id, msg, type, data, 32);
    }
    
    public StructuredDataMessage(final StructuredDataId id, final String msg, final String type, final Map<String, String> data, final int maxLength) {
        super(data);
        this.id = id;
        this.message = msg;
        this.type = type;
        this.maxLength = maxLength;
    }
    
    private StructuredDataMessage(final StructuredDataMessage msg, final Map<String, String> map) {
        super(map);
        this.id = msg.id;
        this.message = msg.message;
        this.type = msg.type;
        this.maxLength = 32;
    }
    
    protected StructuredDataMessage() {
        this.maxLength = 32;
    }
    
    @Override
    public String[] getFormats() {
        final String[] formats = new String[Format.values().length];
        int i = 0;
        for (final Format format : Format.values()) {
            formats[i++] = format.name();
        }
        return formats;
    }
    
    public StructuredDataId getId() {
        return this.id;
    }
    
    protected void setId(final String id) {
        this.id = new StructuredDataId(id, null, null);
    }
    
    protected void setId(final StructuredDataId id) {
        this.id = id;
    }
    
    public String getType() {
        return this.type;
    }
    
    protected void setType(final String type) {
        if (type.length() > 32) {
            throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + type);
        }
        this.type = type;
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        this.asString(Format.FULL, null, buffer);
    }
    
    @Override
    public void formatTo(final String[] formats, final StringBuilder buffer) {
        this.asString(this.getFormat(formats), null, buffer);
    }
    
    @Override
    public String getFormat() {
        return this.message;
    }
    
    protected void setMessageFormat(final String msg) {
        this.message = msg;
    }
    
    @Override
    public String asString() {
        return this.asString(Format.FULL, null);
    }
    
    @Override
    public String asString(final String format) {
        try {
            return this.asString(EnglishEnums.valueOf(Format.class, format), null);
        }
        catch (IllegalArgumentException ex) {
            return this.asString();
        }
    }
    
    public final String asString(final Format format, final StructuredDataId structuredDataId) {
        final StringBuilder sb = new StringBuilder();
        this.asString(format, structuredDataId, sb);
        return sb.toString();
    }
    
    public final void asString(final Format format, final StructuredDataId structuredDataId, final StringBuilder sb) {
        final boolean full = Format.FULL.equals(format);
        if (full) {
            final String myType = this.getType();
            if (myType == null) {
                return;
            }
            sb.append(this.getType()).append(' ');
        }
        StructuredDataId sdId = this.getId();
        if (sdId != null) {
            sdId = sdId.makeId(structuredDataId);
        }
        else {
            sdId = structuredDataId;
        }
        if (sdId == null || sdId.getName() == null) {
            return;
        }
        if (Format.XML.equals(format)) {
            this.asXml(sdId, sb);
            return;
        }
        sb.append('[');
        StringBuilders.appendValue(sb, sdId);
        sb.append(' ');
        this.appendMap(sb);
        sb.append(']');
        if (full) {
            final String msg = this.getFormat();
            if (msg != null) {
                sb.append(' ').append(msg);
            }
        }
    }
    
    private void asXml(final StructuredDataId structuredDataId, final StringBuilder sb) {
        sb.append("<StructuredData>\n");
        sb.append("<type>").append(this.type).append("</type>\n");
        sb.append("<id>").append(structuredDataId).append("</id>\n");
        super.asXml(sb);
        sb.append("\n</StructuredData>\n");
    }
    
    @Override
    public String getFormattedMessage() {
        return this.asString(Format.FULL, null);
    }
    
    @Override
    public String getFormattedMessage(final String[] formats) {
        return this.asString(this.getFormat(formats), null);
    }
    
    private Format getFormat(final String[] formats) {
        if (formats != null && formats.length > 0) {
            for (int i = 0; i < formats.length; ++i) {
                final String format = formats[i];
                if (Format.XML.name().equalsIgnoreCase(format)) {
                    return Format.XML;
                }
                if (Format.FULL.name().equalsIgnoreCase(format)) {
                    return Format.FULL;
                }
            }
            return null;
        }
        return Format.FULL;
    }
    
    @Override
    public String toString() {
        return this.asString(null, null);
    }
    
    @Override
    public StructuredDataMessage newInstance(final Map<String, String> map) {
        return new StructuredDataMessage(this, map);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StructuredDataMessage that = (StructuredDataMessage)o;
        if (!super.equals(o)) {
            return false;
        }
        Label_0072: {
            if (this.type != null) {
                if (this.type.equals(that.type)) {
                    break Label_0072;
                }
            }
            else if (that.type == null) {
                break Label_0072;
            }
            return false;
        }
        Label_0105: {
            if (this.id != null) {
                if (this.id.equals(that.id)) {
                    break Label_0105;
                }
            }
            else if (that.id == null) {
                break Label_0105;
            }
            return false;
        }
        if (this.message != null) {
            if (this.message.equals(that.message)) {
                return true;
            }
        }
        else if (that.message == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
        result = 31 * result + ((this.id != null) ? this.id.hashCode() : 0);
        result = 31 * result + ((this.message != null) ? this.message.hashCode() : 0);
        return result;
    }
    
    @Override
    protected void validate(final String key, final boolean value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final byte value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final char value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final double value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final float value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final int value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final long value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final Object value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final short value) {
        this.validateKey(key);
    }
    
    @Override
    protected void validate(final String key, final String value) {
        this.validateKey(key);
    }
    
    protected void validateKey(final String key) {
        if (this.maxLength > 0 && key.length() > this.maxLength) {
            throw new IllegalArgumentException("Structured data keys are limited to " + this.maxLength + " characters. key: " + key);
        }
        for (int i = 0; i < key.length(); ++i) {
            final char c = key.charAt(i);
            if (c < '!' || c > '~' || c == '=' || c == ']' || c == '\"') {
                throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \"");
            }
        }
    }
    
    public enum Format
    {
        XML, 
        FULL;
    }
}
