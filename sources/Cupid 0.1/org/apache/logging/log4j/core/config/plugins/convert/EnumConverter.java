package org.apache.logging.log4j.core.config.plugins.convert;

import org.apache.logging.log4j.util.EnglishEnums;

public class EnumConverter<E extends Enum<E>> implements TypeConverter<E> {
  private final Class<E> clazz;
  
  public EnumConverter(Class<E> clazz) {
    this.clazz = clazz;
  }
  
  public E convert(String s) {
    return (E)EnglishEnums.valueOf(this.clazz, s);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\convert\EnumConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */