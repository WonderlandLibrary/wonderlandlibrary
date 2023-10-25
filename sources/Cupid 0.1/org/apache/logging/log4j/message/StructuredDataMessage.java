package org.apache.logging.log4j.message;

import java.util.Map;
import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.util.StringBuilders;

@AsynchronouslyFormattable
public class StructuredDataMessage extends MapMessage<StructuredDataMessage, String> {
  private static final long serialVersionUID = 1703221292892071920L;
  
  private static final int MAX_LENGTH = 32;
  
  private static final int HASHVAL = 31;
  
  private StructuredDataId id;
  
  private String message;
  
  private String type;
  
  private final int maxLength;
  
  public enum Format {
    XML, FULL;
  }
  
  public StructuredDataMessage(String id, String msg, String type) {
    this(id, msg, type, 32);
  }
  
  public StructuredDataMessage(String id, String msg, String type, int maxLength) {
    this.id = new StructuredDataId(id, null, null, maxLength);
    this.message = msg;
    this.type = type;
    this.maxLength = maxLength;
  }
  
  public StructuredDataMessage(String id, String msg, String type, Map<String, String> data) {
    this(id, msg, type, data, 32);
  }
  
  public StructuredDataMessage(String id, String msg, String type, Map<String, String> data, int maxLength) {
    super(data);
    this.id = new StructuredDataId(id, null, null, maxLength);
    this.message = msg;
    this.type = type;
    this.maxLength = maxLength;
  }
  
  public StructuredDataMessage(StructuredDataId id, String msg, String type) {
    this(id, msg, type, 32);
  }
  
  public StructuredDataMessage(StructuredDataId id, String msg, String type, int maxLength) {
    this.id = id;
    this.message = msg;
    this.type = type;
    this.maxLength = maxLength;
  }
  
  public StructuredDataMessage(StructuredDataId id, String msg, String type, Map<String, String> data) {
    this(id, msg, type, data, 32);
  }
  
  public StructuredDataMessage(StructuredDataId id, String msg, String type, Map<String, String> data, int maxLength) {
    super(data);
    this.id = id;
    this.message = msg;
    this.type = type;
    this.maxLength = maxLength;
  }
  
  private StructuredDataMessage(StructuredDataMessage msg, Map<String, String> map) {
    super(map);
    this.id = msg.id;
    this.message = msg.message;
    this.type = msg.type;
    this.maxLength = 32;
  }
  
  protected StructuredDataMessage() {
    this.maxLength = 32;
  }
  
  public String[] getFormats() {
    String[] formats = new String[(Format.values()).length];
    int i = 0;
    for (Format format : Format.values())
      formats[i++] = format.name(); 
    return formats;
  }
  
  public StructuredDataId getId() {
    return this.id;
  }
  
  protected void setId(String id) {
    this.id = new StructuredDataId(id, null, null);
  }
  
  protected void setId(StructuredDataId id) {
    this.id = id;
  }
  
  public String getType() {
    return this.type;
  }
  
  protected void setType(String type) {
    if (type.length() > 32)
      throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + type); 
    this.type = type;
  }
  
  public void formatTo(StringBuilder buffer) {
    asString(Format.FULL, null, buffer);
  }
  
  public void formatTo(String[] formats, StringBuilder buffer) {
    asString(getFormat(formats), null, buffer);
  }
  
  public String getFormat() {
    return this.message;
  }
  
  protected void setMessageFormat(String msg) {
    this.message = msg;
  }
  
  public String asString() {
    return asString(Format.FULL, null);
  }
  
  public String asString(String format) {
    try {
      return asString((Format)EnglishEnums.valueOf(Format.class, format), null);
    } catch (IllegalArgumentException ex) {
      return asString();
    } 
  }
  
  public final String asString(Format format, StructuredDataId structuredDataId) {
    StringBuilder sb = new StringBuilder();
    asString(format, structuredDataId, sb);
    return sb.toString();
  }
  
  public final void asString(Format format, StructuredDataId structuredDataId, StringBuilder sb) {
    boolean full = Format.FULL.equals(format);
    if (full) {
      String myType = getType();
      if (myType == null)
        return; 
      sb.append(getType()).append(' ');
    } 
    StructuredDataId sdId = getId();
    if (sdId != null) {
      sdId = sdId.makeId(structuredDataId);
    } else {
      sdId = structuredDataId;
    } 
    if (sdId == null || sdId.getName() == null)
      return; 
    if (Format.XML.equals(format)) {
      asXml(sdId, sb);
      return;
    } 
    sb.append('[');
    StringBuilders.appendValue(sb, sdId);
    sb.append(' ');
    appendMap(sb);
    sb.append(']');
    if (full) {
      String msg = getFormat();
      if (msg != null)
        sb.append(' ').append(msg); 
    } 
  }
  
  private void asXml(StructuredDataId structuredDataId, StringBuilder sb) {
    sb.append("<StructuredData>\n");
    sb.append("<type>").append(this.type).append("</type>\n");
    sb.append("<id>").append(structuredDataId).append("</id>\n");
    asXml(sb);
    sb.append("\n</StructuredData>\n");
  }
  
  public String getFormattedMessage() {
    return asString(Format.FULL, (StructuredDataId)null);
  }
  
  public String getFormattedMessage(String[] formats) {
    return asString(getFormat(formats), (StructuredDataId)null);
  }
  
  private Format getFormat(String[] formats) {
    if (formats != null && formats.length > 0) {
      for (int i = 0; i < formats.length; i++) {
        String format = formats[i];
        if (Format.XML.name().equalsIgnoreCase(format))
          return Format.XML; 
        if (Format.FULL.name().equalsIgnoreCase(format))
          return Format.FULL; 
      } 
      return null;
    } 
    return Format.FULL;
  }
  
  public String toString() {
    return asString((Format)null, (StructuredDataId)null);
  }
  
  public StructuredDataMessage newInstance(Map<String, String> map) {
    return new StructuredDataMessage(this, map);
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    StructuredDataMessage that = (StructuredDataMessage)o;
    if (!super.equals(o))
      return false; 
    if ((this.type != null) ? !this.type.equals(that.type) : (that.type != null))
      return false; 
    if ((this.id != null) ? !this.id.equals(that.id) : (that.id != null))
      return false; 
    if ((this.message != null) ? !this.message.equals(that.message) : (that.message != null))
      return false; 
    return true;
  }
  
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
    result = 31 * result + ((this.id != null) ? this.id.hashCode() : 0);
    result = 31 * result + ((this.message != null) ? this.message.hashCode() : 0);
    return result;
  }
  
  protected void validate(String key, boolean value) {
    validateKey(key);
  }
  
  protected void validate(String key, byte value) {
    validateKey(key);
  }
  
  protected void validate(String key, char value) {
    validateKey(key);
  }
  
  protected void validate(String key, double value) {
    validateKey(key);
  }
  
  protected void validate(String key, float value) {
    validateKey(key);
  }
  
  protected void validate(String key, int value) {
    validateKey(key);
  }
  
  protected void validate(String key, long value) {
    validateKey(key);
  }
  
  protected void validate(String key, Object value) {
    validateKey(key);
  }
  
  protected void validate(String key, short value) {
    validateKey(key);
  }
  
  protected void validate(String key, String value) {
    validateKey(key);
  }
  
  protected void validateKey(String key) {
    if (this.maxLength > 0 && key.length() > this.maxLength)
      throw new IllegalArgumentException("Structured data keys are limited to " + this.maxLength + " characters. key: " + key); 
    for (int i = 0; i < key.length(); i++) {
      char c = key.charAt(i);
      if (c < '!' || c > '~' || c == '=' || c == ']' || c == '"')
        throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \""); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\StructuredDataMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */