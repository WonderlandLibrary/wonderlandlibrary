package joptsimple.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

public class InetAddressConverter implements ValueConverter<InetAddress> {
  public InetAddress convert(String value) {
    try {
      return InetAddress.getByName(value);
    } catch (UnknownHostException e) {
      throw new ValueConversionException("Cannot convert value [" + value + " into an InetAddress", e);
    } 
  }
  
  public Class<InetAddress> valueType() {
    return InetAddress.class;
  }
  
  public String valuePattern() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimpl\\util\InetAddressConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */