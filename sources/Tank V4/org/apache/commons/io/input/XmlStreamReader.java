package org.apache.commons.io.input;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;

public class XmlStreamReader extends Reader {
   private static final int BUFFER_SIZE = 4096;
   private static final String UTF_8 = "UTF-8";
   private static final String US_ASCII = "US-ASCII";
   private static final String UTF_16BE = "UTF-16BE";
   private static final String UTF_16LE = "UTF-16LE";
   private static final String UTF_32BE = "UTF-32BE";
   private static final String UTF_32LE = "UTF-32LE";
   private static final String UTF_16 = "UTF-16";
   private static final String UTF_32 = "UTF-32";
   private static final String EBCDIC = "CP1047";
   private static final ByteOrderMark[] BOMS;
   private static final ByteOrderMark[] XML_GUESS_BYTES;
   private final Reader reader;
   private final String encoding;
   private final String defaultEncoding;
   private static final Pattern CHARSET_PATTERN;
   public static final Pattern ENCODING_PATTERN;
   private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
   private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
   private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
   private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
   private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";

   public String getDefaultEncoding() {
      return this.defaultEncoding;
   }

   public XmlStreamReader(File var1) throws IOException {
      this((InputStream)(new FileInputStream(var1)));
   }

   public XmlStreamReader(InputStream var1) throws IOException {
      this(var1, true);
   }

   public XmlStreamReader(InputStream var1, boolean var2) throws IOException {
      this(var1, var2, (String)null);
   }

   public XmlStreamReader(InputStream var1, boolean var2, String var3) throws IOException {
      this.defaultEncoding = var3;
      BOMInputStream var4 = new BOMInputStream(new BufferedInputStream(var1, 4096), false, BOMS);
      BOMInputStream var5 = new BOMInputStream(var4, true, XML_GUESS_BYTES);
      this.encoding = this.doRawStream(var4, var5, var2);
      this.reader = new InputStreamReader(var5, this.encoding);
   }

   public XmlStreamReader(URL var1) throws IOException {
      this((URLConnection)var1.openConnection(), (String)null);
   }

   public XmlStreamReader(URLConnection var1, String var2) throws IOException {
      this.defaultEncoding = var2;
      boolean var3 = true;
      String var4 = var1.getContentType();
      InputStream var5 = var1.getInputStream();
      BOMInputStream var6 = new BOMInputStream(new BufferedInputStream(var5, 4096), false, BOMS);
      BOMInputStream var7 = new BOMInputStream(var6, true, XML_GUESS_BYTES);
      if (!(var1 instanceof HttpURLConnection) && var4 == null) {
         this.encoding = this.doRawStream(var6, var7, var3);
      } else {
         this.encoding = this.doHttpStream(var6, var7, var4, var3);
      }

      this.reader = new InputStreamReader(var7, this.encoding);
   }

   public XmlStreamReader(InputStream var1, String var2) throws IOException {
      this(var1, var2, true);
   }

   public XmlStreamReader(InputStream var1, String var2, boolean var3, String var4) throws IOException {
      this.defaultEncoding = var4;
      BOMInputStream var5 = new BOMInputStream(new BufferedInputStream(var1, 4096), false, BOMS);
      BOMInputStream var6 = new BOMInputStream(var5, true, XML_GUESS_BYTES);
      this.encoding = this.doHttpStream(var5, var6, var2, var3);
      this.reader = new InputStreamReader(var6, this.encoding);
   }

   public XmlStreamReader(InputStream var1, String var2, boolean var3) throws IOException {
      this(var1, var2, var3, (String)null);
   }

   public String getEncoding() {
      return this.encoding;
   }

   public int read(char[] var1, int var2, int var3) throws IOException {
      return this.reader.read(var1, var2, var3);
   }

   public void close() throws IOException {
      this.reader.close();
   }

   private String doRawStream(BOMInputStream var1, BOMInputStream var2, boolean var3) throws IOException {
      String var4 = var1.getBOMCharsetName();
      String var5 = var2.getBOMCharsetName();
      String var6 = getXmlProlog(var2, var5);

      try {
         return this.calculateRawEncoding(var4, var5, var6);
      } catch (XmlStreamReaderException var8) {
         if (var3) {
            return this.doLenientDetection((String)null, var8);
         } else {
            throw var8;
         }
      }
   }

   private String doHttpStream(BOMInputStream var1, BOMInputStream var2, String var3, boolean var4) throws IOException {
      String var5 = var1.getBOMCharsetName();
      String var6 = var2.getBOMCharsetName();
      String var7 = getXmlProlog(var2, var6);

      try {
         return this.calculateHttpEncoding(var3, var5, var6, var7, var4);
      } catch (XmlStreamReaderException var9) {
         if (var4) {
            return this.doLenientDetection(var3, var9);
         } else {
            throw var9;
         }
      }
   }

   private String doLenientDetection(String var1, XmlStreamReaderException var2) throws IOException {
      if (var1 != null && var1.startsWith("text/html")) {
         var1 = var1.substring(9);
         var1 = "text/xml" + var1;

         try {
            return this.calculateHttpEncoding(var1, var2.getBomEncoding(), var2.getXmlGuessEncoding(), var2.getXmlEncoding(), true);
         } catch (XmlStreamReaderException var4) {
            var2 = var4;
         }
      }

      String var3 = var2.getXmlEncoding();
      if (var3 == null) {
         var3 = var2.getContentTypeEncoding();
      }

      if (var3 == null) {
         var3 = this.defaultEncoding == null ? "UTF-8" : this.defaultEncoding;
      }

      return var3;
   }

   String calculateRawEncoding(String var1, String var2, String var3) throws IOException {
      if (var1 != null) {
         String var4;
         if (var1.equals("UTF-8")) {
            if (var2 != null && !var2.equals("UTF-8")) {
               var4 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", var1, var2, var3);
               throw new XmlStreamReaderException(var4, var1, var2, var3);
            } else if (var3 != null && !var3.equals("UTF-8")) {
               var4 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", var1, var2, var3);
               throw new XmlStreamReaderException(var4, var1, var2, var3);
            } else {
               return var1;
            }
         } else if (!var1.equals("UTF-16BE") && !var1.equals("UTF-16LE")) {
            if (!var1.equals("UTF-32BE") && !var1.equals("UTF-32LE")) {
               var4 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", var1, var2, var3);
               throw new XmlStreamReaderException(var4, var1, var2, var3);
            } else if (var2 != null && !var2.equals(var1)) {
               var4 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", var1, var2, var3);
               throw new XmlStreamReaderException(var4, var1, var2, var3);
            } else if (var3 != null && !var3.equals("UTF-32") && !var3.equals(var1)) {
               var4 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", var1, var2, var3);
               throw new XmlStreamReaderException(var4, var1, var2, var3);
            } else {
               return var1;
            }
         } else if (var2 != null && !var2.equals(var1)) {
            var4 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", var1, var2, var3);
            throw new XmlStreamReaderException(var4, var1, var2, var3);
         } else if (var3 != null && !var3.equals("UTF-16") && !var3.equals(var1)) {
            var4 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", var1, var2, var3);
            throw new XmlStreamReaderException(var4, var1, var2, var3);
         } else {
            return var1;
         }
      } else if (var2 != null && var3 != null) {
         return !var3.equals("UTF-16") || !var2.equals("UTF-16BE") && !var2.equals("UTF-16LE") ? var3 : var2;
      } else {
         return this.defaultEncoding == null ? "UTF-8" : this.defaultEncoding;
      }
   }

   String calculateHttpEncoding(String var1, String var2, String var3, String var4, boolean var5) throws IOException {
      if (var5 && var4 != null) {
         return var4;
      } else {
         String var6 = getContentTypeMime(var1);
         String var7 = getContentTypeEncoding(var1);
         boolean var8 = isAppXml(var6);
         boolean var9 = isTextXml(var6);
         String var10;
         if (!var8 && !var9) {
            var10 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", var6, var7, var2, var3, var4);
            throw new XmlStreamReaderException(var10, var6, var7, var2, var3, var4);
         } else if (var7 == null) {
            if (var8) {
               return this.calculateRawEncoding(var2, var3, var4);
            } else {
               return this.defaultEncoding == null ? "US-ASCII" : this.defaultEncoding;
            }
         } else if (!var7.equals("UTF-16BE") && !var7.equals("UTF-16LE")) {
            if (var7.equals("UTF-16")) {
               if (var2 != null && var2.startsWith("UTF-16")) {
                  return var2;
               } else {
                  var10 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", var6, var7, var2, var3, var4);
                  throw new XmlStreamReaderException(var10, var6, var7, var2, var3, var4);
               }
            } else if (!var7.equals("UTF-32BE") && !var7.equals("UTF-32LE")) {
               if (var7.equals("UTF-32")) {
                  if (var2 != null && var2.startsWith("UTF-32")) {
                     return var2;
                  } else {
                     var10 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", var6, var7, var2, var3, var4);
                     throw new XmlStreamReaderException(var10, var6, var7, var2, var3, var4);
                  }
               } else {
                  return var7;
               }
            } else if (var2 != null) {
               var10 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", var6, var7, var2, var3, var4);
               throw new XmlStreamReaderException(var10, var6, var7, var2, var3, var4);
            } else {
               return var7;
            }
         } else if (var2 != null) {
            var10 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", var6, var7, var2, var3, var4);
            throw new XmlStreamReaderException(var10, var6, var7, var2, var3, var4);
         } else {
            return var7;
         }
      }
   }

   static String getContentTypeMime(String var0) {
      String var1 = null;
      if (var0 != null) {
         int var2 = var0.indexOf(";");
         if (var2 >= 0) {
            var1 = var0.substring(0, var2);
         } else {
            var1 = var0;
         }

         var1 = var1.trim();
      }

      return var1;
   }

   static String getContentTypeEncoding(String var0) {
      String var1 = null;
      if (var0 != null) {
         int var2 = var0.indexOf(";");
         if (var2 > -1) {
            String var3 = var0.substring(var2 + 1);
            Matcher var4 = CHARSET_PATTERN.matcher(var3);
            var1 = var4.find() ? var4.group(1) : null;
            var1 = var1 != null ? var1.toUpperCase(Locale.US) : null;
         }
      }

      return var1;
   }

   private static String getXmlProlog(InputStream var0, String var1) throws IOException {
      String var2 = null;
      if (var1 != null) {
         byte[] var3 = new byte[4096];
         var0.mark(4096);
         int var4 = 0;
         int var5 = 4096;
         int var6 = var0.read(var3, var4, var5);
         int var7 = -1;

         String var8;
         for(var8 = null; var6 != -1 && var7 == -1 && var4 < 4096; var7 = var8.indexOf(62)) {
            var4 += var6;
            var5 -= var6;
            var6 = var0.read(var3, var4, var5);
            var8 = new String(var3, 0, var4, var1);
         }

         if (var7 == -1) {
            if (var6 == -1) {
               throw new IOException("Unexpected end of XML stream");
            }

            throw new IOException("XML prolog or ROOT element not found on first " + var4 + " bytes");
         }

         if (var4 > 0) {
            var0.reset();
            BufferedReader var10 = new BufferedReader(new StringReader(var8.substring(0, var7 + 1)));
            StringBuffer var11 = new StringBuffer();

            for(String var12 = var10.readLine(); var12 != null; var12 = var10.readLine()) {
               var11.append(var12);
            }

            Matcher var13 = ENCODING_PATTERN.matcher(var11);
            if (var13.find()) {
               var2 = var13.group(1).toUpperCase();
               var2 = var2.substring(1, var2.length() - 1);
            }
         }
      }

      return var2;
   }

   static boolean isAppXml(String var0) {
      return var0 != null && (var0.equals("application/xml") || var0.equals("application/xml-dtd") || var0.equals("application/xml-external-parsed-entity") || var0.startsWith("application/") && var0.endsWith("+xml"));
   }

   static boolean isTextXml(String var0) {
      return var0 != null && (var0.equals("text/xml") || var0.equals("text/xml-external-parsed-entity") || var0.startsWith("text/") && var0.endsWith("+xml"));
   }

   static {
      BOMS = new ByteOrderMark[]{ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE};
      XML_GUESS_BYTES = new ByteOrderMark[]{new ByteOrderMark("UTF-8", new int[]{60, 63, 120, 109}), new ByteOrderMark("UTF-16BE", new int[]{0, 60, 0, 63}), new ByteOrderMark("UTF-16LE", new int[]{60, 0, 63, 0}), new ByteOrderMark("UTF-32BE", new int[]{0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109}), new ByteOrderMark("UTF-32LE", new int[]{60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0}), new ByteOrderMark("CP1047", new int[]{76, 111, 167, 148})};
      CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
      ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
   }
}
