package org.apache.commons.compress.archivers.zip;

public class UnicodeCommentExtraField extends AbstractUnicodeExtraField {
   public static final ZipShort UCOM_ID = new ZipShort(25461);

   public UnicodeCommentExtraField() {
   }

   public UnicodeCommentExtraField(String var1, byte[] var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public UnicodeCommentExtraField(String var1, byte[] var2) {
      super(var1, var2);
   }

   public ZipShort getHeaderId() {
      return UCOM_ID;
   }
}
