package org.apache.logging.log4j.util;

import java.util.Locale;

public final class EnglishEnums {
  public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
    return valueOf(enumType, name, null);
  }
  
  public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, T defaultValue) {
    return (name == null) ? defaultValue : Enum.<T>valueOf(enumType, name.toUpperCase(Locale.ENGLISH));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\EnglishEnums.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */