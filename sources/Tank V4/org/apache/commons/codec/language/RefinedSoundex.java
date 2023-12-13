package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class RefinedSoundex implements StringEncoder {
   public static final String US_ENGLISH_MAPPING_STRING = "01360240043788015936020505";
   private static final char[] US_ENGLISH_MAPPING = "01360240043788015936020505".toCharArray();
   private final char[] soundexMapping;
   public static final RefinedSoundex US_ENGLISH = new RefinedSoundex();

   public RefinedSoundex() {
      this.soundexMapping = US_ENGLISH_MAPPING;
   }

   public RefinedSoundex(char[] var1) {
      this.soundexMapping = new char[var1.length];
      System.arraycopy(var1, 0, this.soundexMapping, 0, var1.length);
   }

   public RefinedSoundex(String var1) {
      this.soundexMapping = var1.toCharArray();
   }

   public int difference(String var1, String var2) throws EncoderException {
      return SoundexUtils.difference(this, var1, var2);
   }

   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof String)) {
         throw new EncoderException("Parameter supplied to RefinedSoundex encode is not of type java.lang.String");
      } else {
         return this.soundex((String)var1);
      }
   }

   public String encode(String var1) {
      return this.soundex(var1);
   }

   char getMappingCode(char var1) {
      return !Character.isLetter(var1) ? '\u0000' : this.soundexMapping[Character.toUpperCase(var1) - 65];
   }

   public String soundex(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = SoundexUtils.clean(var1);
         if (var1.length() == 0) {
            return var1;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append(var1.charAt(0));
            char var3 = '*';

            for(int var5 = 0; var5 < var1.length(); ++var5) {
               char var4 = this.getMappingCode(var1.charAt(var5));
               if (var4 != var3) {
                  if (var4 != 0) {
                     var2.append(var4);
                  }

                  var3 = var4;
               }
            }

            return var2.toString();
         }
      }
   }
}
