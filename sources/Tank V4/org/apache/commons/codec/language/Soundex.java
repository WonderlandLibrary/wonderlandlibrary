package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Soundex implements StringEncoder {
   public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
   private static final char[] US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();
   public static final Soundex US_ENGLISH = new Soundex();
   /** @deprecated */
   @Deprecated
   private int maxLength = 4;
   private final char[] soundexMapping;

   public Soundex() {
      this.soundexMapping = US_ENGLISH_MAPPING;
   }

   public Soundex(char[] var1) {
      this.soundexMapping = new char[var1.length];
      System.arraycopy(var1, 0, this.soundexMapping, 0, var1.length);
   }

   public Soundex(String var1) {
      this.soundexMapping = var1.toCharArray();
   }

   public int difference(String var1, String var2) throws EncoderException {
      return SoundexUtils.difference(this, var1, var2);
   }

   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof String)) {
         throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
      } else {
         return this.soundex((String)var1);
      }
   }

   public String encode(String var1) {
      return this.soundex(var1);
   }

   private char getMappingCode(String var1, int var2) {
      char var3 = this.map(var1.charAt(var2));
      if (var2 > 1 && var3 != '0') {
         char var4 = var1.charAt(var2 - 1);
         if ('H' == var4 || 'W' == var4) {
            char var5 = var1.charAt(var2 - 2);
            char var6 = this.map(var5);
            if (var6 == var3 || 'H' == var5 || 'W' == var5) {
               return '\u0000';
            }
         }
      }

      return var3;
   }

   /** @deprecated */
   @Deprecated
   public int getMaxLength() {
      return this.maxLength;
   }

   private char[] getSoundexMapping() {
      return this.soundexMapping;
   }

   private char map(char var1) {
      int var2 = var1 - 65;
      if (var2 >= 0 && var2 < this.getSoundexMapping().length) {
         return this.getSoundexMapping()[var2];
      } else {
         throw new IllegalArgumentException("The character is not mapped: " + var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public void setMaxLength(int var1) {
      this.maxLength = var1;
   }

   public String soundex(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = SoundexUtils.clean(var1);
         if (var1.length() == 0) {
            return var1;
         } else {
            char[] var2 = new char[]{'0', '0', '0', '0'};
            int var5 = 1;
            int var6 = 1;
            var2[0] = var1.charAt(0);
            char var3 = this.getMappingCode(var1, 0);

            while(var5 < var1.length() && var6 < var2.length) {
               char var4 = this.getMappingCode(var1, var5++);
               if (var4 != 0) {
                  if (var4 != '0' && var4 != var3) {
                     var2[var6++] = var4;
                  }

                  var3 = var4;
               }
            }

            return new String(var2);
         }
      }
   }
}
