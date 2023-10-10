package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;

final class HttpPostBodyUtil {
   public static final int chunkSize = 8096;
   public static final String CONTENT_DISPOSITION = "Content-Disposition";
   public static final String NAME = "name";
   public static final String FILENAME = "filename";
   public static final String FORM_DATA = "form-data";
   public static final String ATTACHMENT = "attachment";
   public static final String FILE = "file";
   public static final String MULTIPART_MIXED = "multipart/mixed";
   public static final Charset ISO_8859_1;
   public static final Charset US_ASCII;
   public static final String DEFAULT_BINARY_CONTENT_TYPE = "application/octet-stream";
   public static final String DEFAULT_TEXT_CONTENT_TYPE = "text/plain";

   private HttpPostBodyUtil() {
   }

   static int findNonWhitespace(String var0, int var1) {
      int var2;
      for(var2 = var1; var2 < var0.length() && Character.isWhitespace(var0.charAt(var2)); ++var2) {
      }

      return var2;
   }

   static int findWhitespace(String var0, int var1) {
      int var2;
      for(var2 = var1; var2 < var0.length() && !Character.isWhitespace(var0.charAt(var2)); ++var2) {
      }

      return var2;
   }

   static int findEndOfString(String var0) {
      int var1;
      for(var1 = var0.length(); var1 > 0 && Character.isWhitespace(var0.charAt(var1 - 1)); --var1) {
      }

      return var1;
   }

   static {
      ISO_8859_1 = CharsetUtil.ISO_8859_1;
      US_ASCII = CharsetUtil.US_ASCII;
   }

   static class SeekAheadOptimize {
      byte[] bytes;
      int readerIndex;
      int pos;
      int origPos;
      int limit;
      ByteBuf buffer;

      SeekAheadOptimize(ByteBuf var1) throws HttpPostBodyUtil.SeekAheadNoBackArrayException {
         if (!var1.hasArray()) {
            throw new HttpPostBodyUtil.SeekAheadNoBackArrayException();
         } else {
            this.buffer = var1;
            this.bytes = var1.array();
            this.readerIndex = var1.readerIndex();
            this.origPos = this.pos = var1.arrayOffset() + this.readerIndex;
            this.limit = var1.arrayOffset() + var1.writerIndex();
         }
      }

      void setReadPosition(int var1) {
         this.pos -= var1;
         this.readerIndex = this.getReadPosition(this.pos);
         this.buffer.readerIndex(this.readerIndex);
      }

      int getReadPosition(int var1) {
         return var1 - this.origPos + this.readerIndex;
      }

      void clear() {
         this.buffer = null;
         this.bytes = null;
         this.limit = 0;
         this.pos = 0;
         this.readerIndex = 0;
      }
   }

   static class SeekAheadNoBackArrayException extends Exception {
      private static final long serialVersionUID = -630418804938699495L;
   }

   public static enum TransferEncodingMechanism {
      BIT7("7bit"),
      BIT8("8bit"),
      BINARY("binary");

      private final String value;
      private static final HttpPostBodyUtil.TransferEncodingMechanism[] $VALUES = new HttpPostBodyUtil.TransferEncodingMechanism[]{BIT7, BIT8, BINARY};

      private TransferEncodingMechanism(String var3) {
         this.value = var3;
      }

      private TransferEncodingMechanism() {
         this.value = this.name();
      }

      public String value() {
         return this.value;
      }

      public String toString() {
         return this.value;
      }
   }
}
