package org.apache.commons.io.input;

import java.io.IOException;

public class XmlStreamReaderException extends IOException {
   private static final long serialVersionUID = 1L;
   private final String bomEncoding;
   private final String xmlGuessEncoding;
   private final String xmlEncoding;
   private final String contentTypeMime;
   private final String contentTypeEncoding;

   public XmlStreamReaderException(String var1, String var2, String var3, String var4) {
      this(var1, (String)null, (String)null, var2, var3, var4);
   }

   public XmlStreamReaderException(String var1, String var2, String var3, String var4, String var5, String var6) {
      super(var1);
      this.contentTypeMime = var2;
      this.contentTypeEncoding = var3;
      this.bomEncoding = var4;
      this.xmlGuessEncoding = var5;
      this.xmlEncoding = var6;
   }

   public String getBomEncoding() {
      return this.bomEncoding;
   }

   public String getXmlGuessEncoding() {
      return this.xmlGuessEncoding;
   }

   public String getXmlEncoding() {
      return this.xmlEncoding;
   }

   public String getContentTypeMime() {
      return this.contentTypeMime;
   }

   public String getContentTypeEncoding() {
      return this.contentTypeEncoding;
   }
}
