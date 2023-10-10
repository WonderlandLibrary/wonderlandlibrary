package joptsimple.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

public class InetAddressConverter implements ValueConverter {
   public InetAddress convert(String var1) {
      try {
         return InetAddress.getByName(var1);
      } catch (UnknownHostException var3) {
         throw new ValueConversionException("Cannot convert value [" + var1 + " into an InetAddress", var3);
      }
   }

   public Class valueType() {
      return InetAddress.class;
   }

   public String valuePattern() {
      return null;
   }

   public Object convert(String var1) {
      return this.convert(var1);
   }
}
