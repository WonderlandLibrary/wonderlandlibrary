package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ObjectUtils;

public class ToStringBuilder implements Builder<String> {
  private static volatile ToStringStyle defaultStyle = ToStringStyle.DEFAULT_STYLE;
  
  private final StringBuffer buffer;
  
  private final Object object;
  
  private final ToStringStyle style;
  
  public static ToStringStyle getDefaultStyle() {
    return defaultStyle;
  }
  
  public static void setDefaultStyle(ToStringStyle style) {
    if (style == null)
      throw new IllegalArgumentException("The style must not be null"); 
    defaultStyle = style;
  }
  
  public static String reflectionToString(Object object) {
    return ReflectionToStringBuilder.toString(object);
  }
  
  public static String reflectionToString(Object object, ToStringStyle style) {
    return ReflectionToStringBuilder.toString(object, style);
  }
  
  public static String reflectionToString(Object object, ToStringStyle style, boolean outputTransients) {
    return ReflectionToStringBuilder.toString(object, style, outputTransients, false, null);
  }
  
  public static <T> String reflectionToString(T object, ToStringStyle style, boolean outputTransients, Class<? super T> reflectUpToClass) {
    return ReflectionToStringBuilder.toString(object, style, outputTransients, false, reflectUpToClass);
  }
  
  public ToStringBuilder(Object object) {
    this(object, null, null);
  }
  
  public ToStringBuilder(Object object, ToStringStyle style) {
    this(object, style, null);
  }
  
  public ToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer) {
    if (style == null)
      style = getDefaultStyle(); 
    if (buffer == null)
      buffer = new StringBuffer(512); 
    this.buffer = buffer;
    this.style = style;
    this.object = object;
    style.appendStart(buffer, object);
  }
  
  public ToStringBuilder append(boolean value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(boolean[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(byte value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(byte[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(char value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(char[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(double value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(double[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(float value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(float[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(int value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(int[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(long value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(long[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(Object obj) {
    this.style.append(this.buffer, (String)null, obj, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(Object[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(short value) {
    this.style.append(this.buffer, (String)null, value);
    return this;
  }
  
  public ToStringBuilder append(short[] array) {
    this.style.append(this.buffer, (String)null, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, boolean value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, boolean[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, boolean[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, byte value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, byte[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, byte[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, char value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, char[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, char[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, double value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, double[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, double[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, float value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, float[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, float[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, int value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, int[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, int[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, long value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, long[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, long[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, Object obj) {
    this.style.append(this.buffer, fieldName, obj, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, Object obj, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, obj, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, Object[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, Object[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder append(String fieldName, short value) {
    this.style.append(this.buffer, fieldName, value);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, short[] array) {
    this.style.append(this.buffer, fieldName, array, (Boolean)null);
    return this;
  }
  
  public ToStringBuilder append(String fieldName, short[] array, boolean fullDetail) {
    this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
    return this;
  }
  
  public ToStringBuilder appendAsObjectToString(Object srcObject) {
    ObjectUtils.identityToString(getStringBuffer(), srcObject);
    return this;
  }
  
  public ToStringBuilder appendSuper(String superToString) {
    if (superToString != null)
      this.style.appendSuper(this.buffer, superToString); 
    return this;
  }
  
  public ToStringBuilder appendToString(String toString) {
    if (toString != null)
      this.style.appendToString(this.buffer, toString); 
    return this;
  }
  
  public Object getObject() {
    return this.object;
  }
  
  public StringBuffer getStringBuffer() {
    return this.buffer;
  }
  
  public ToStringStyle getStyle() {
    return this.style;
  }
  
  public String toString() {
    if (getObject() == null) {
      getStringBuffer().append(getStyle().getNullText());
    } else {
      this.style.appendEnd(getStringBuffer(), getObject());
    } 
    return getStringBuffer().toString();
  }
  
  public String build() {
    return toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\builder\ToStringBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */