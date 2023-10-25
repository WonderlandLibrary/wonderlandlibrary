package com.sun.jna.win32;

import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.FromNativeContext;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;

public class W32APITypeMapper extends DefaultTypeMapper {
  public static final TypeMapper UNICODE = (TypeMapper)new W32APITypeMapper(true);
  
  public static final TypeMapper ASCII = (TypeMapper)new W32APITypeMapper(false);
  
  static Class class$com$sun$jna$WString;
  
  static Class array$Ljava$lang$String;
  
  static Class class$java$lang$Integer;
  
  protected W32APITypeMapper(boolean unicode) {
    if (unicode) {
      TypeConverter stringConverter = new TypeConverter() {
          private final W32APITypeMapper this$0;
          
          public Object toNative(Object value, ToNativeContext context) {
            if (value == null)
              return null; 
            if (value instanceof String[])
              return new StringArray((String[])value, true); 
            return new WString(value.toString());
          }
          
          public Object fromNative(Object value, FromNativeContext context) {
            if (value == null)
              return null; 
            return value.toString();
          }
          
          public Class nativeType() {
            return (W32APITypeMapper.class$com$sun$jna$WString == null) ? (W32APITypeMapper.class$com$sun$jna$WString = W32APITypeMapper.class$("com.sun.jna.WString")) : W32APITypeMapper.class$com$sun$jna$WString;
          }
        };
      addTypeConverter(String.class, stringConverter);
      addToNativeConverter((array$Ljava$lang$String == null) ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String, (ToNativeConverter)stringConverter);
    } 
    TypeConverter booleanConverter = new TypeConverter() {
        private final W32APITypeMapper this$0;
        
        public Object toNative(Object value, ToNativeContext context) {
          return new Integer(Boolean.TRUE.equals(value) ? 1 : 0);
        }
        
        public Object fromNative(Object value, FromNativeContext context) {
          return (((Integer)value).intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
        }
        
        public Class nativeType() {
          return (W32APITypeMapper.class$java$lang$Integer == null) ? (W32APITypeMapper.class$java$lang$Integer = W32APITypeMapper.class$("java.lang.Integer")) : W32APITypeMapper.class$java$lang$Integer;
        }
      };
    addTypeConverter(Boolean.class, booleanConverter);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\win32\W32APITypeMapper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */