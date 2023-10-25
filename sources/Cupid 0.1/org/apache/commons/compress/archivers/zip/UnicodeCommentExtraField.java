package org.apache.commons.compress.archivers.zip;

public class UnicodeCommentExtraField extends AbstractUnicodeExtraField {
  public static final ZipShort UCOM_ID = new ZipShort(25461);
  
  public UnicodeCommentExtraField() {}
  
  public UnicodeCommentExtraField(String text, byte[] bytes, int off, int len) {
    super(text, bytes, off, len);
  }
  
  public UnicodeCommentExtraField(String comment, byte[] bytes) {
    super(comment, bytes);
  }
  
  public ZipShort getHeaderId() {
    return UCOM_ID;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\UnicodeCommentExtraField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */