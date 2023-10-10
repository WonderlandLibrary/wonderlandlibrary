package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public abstract class AbstractCaverphone implements StringEncoder {
   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof String)) {
         throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String");
      } else {
         return this.encode((String)var1);
      }
   }

   public boolean isEncodeEqual(String var1, String var2) throws EncoderException {
      return this.encode(var1).equals(this.encode(var2));
   }
}
