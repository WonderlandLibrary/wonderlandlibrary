package com.google.gson;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
  private final DateFormat enUsFormat;
  
  private final DateFormat localFormat;
  
  private final DateFormat iso8601Format;
  
  DefaultDateTypeAdapter() {
    this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
  }
  
  DefaultDateTypeAdapter(String datePattern) {
    this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
  }
  
  DefaultDateTypeAdapter(int style) {
    this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
  }
  
  public DefaultDateTypeAdapter(int dateStyle, int timeStyle) {
    this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), DateFormat.getDateTimeInstance(dateStyle, timeStyle));
  }
  
  DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
    this.enUsFormat = enUsFormat;
    this.localFormat = localFormat;
    this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    this.iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
    synchronized (this.localFormat) {
      String dateFormatAsString = this.enUsFormat.format(src);
      return new JsonPrimitive(dateFormatAsString);
    } 
  }
  
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (!(json instanceof JsonPrimitive))
      throw new JsonParseException("The date should be a string value"); 
    Date date = deserializeToDate(json);
    if (typeOfT == Date.class)
      return date; 
    if (typeOfT == Timestamp.class)
      return new Timestamp(date.getTime()); 
    if (typeOfT == Date.class)
      return new Date(date.getTime()); 
    throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
  }
  
  private Date deserializeToDate(JsonElement json) {
    synchronized (this.localFormat) {
      return this.localFormat.parse(json.getAsString());
    } 
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(DefaultDateTypeAdapter.class.getSimpleName());
    sb.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\DefaultDateTypeAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */