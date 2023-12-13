package org.apache.commons.compress.archivers.zip;

public class UnicodePathExtraField extends AbstractUnicodeExtraField {
   public static final ZipShort UPATH_ID = new ZipShort(28789);

   public UnicodePathExtraField() {
   }

   public UnicodePathExtraField(String var1, byte[] var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public UnicodePathExtraField(String var1, byte[] var2) {
      super(var1, var2);
   }

   public ZipShort getHeaderId() {
      return UPATH_ID;
   }
}
