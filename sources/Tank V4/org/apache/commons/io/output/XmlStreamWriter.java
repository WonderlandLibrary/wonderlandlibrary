package org.apache.commons.io.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.input.XmlStreamReader;

public class XmlStreamWriter extends Writer {
   private static final int BUFFER_SIZE = 4096;
   private final OutputStream out;
   private final String defaultEncoding;
   private StringWriter xmlPrologWriter;
   private Writer writer;
   private String encoding;
   static final Pattern ENCODING_PATTERN;

   public XmlStreamWriter(OutputStream var1) {
      this((OutputStream)var1, (String)null);
   }

   public XmlStreamWriter(OutputStream var1, String var2) {
      this.xmlPrologWriter = new StringWriter(4096);
      this.out = var1;
      this.defaultEncoding = var2 != null ? var2 : "UTF-8";
   }

   public XmlStreamWriter(File var1) throws FileNotFoundException {
      this((File)var1, (String)null);
   }

   public XmlStreamWriter(File var1, String var2) throws FileNotFoundException {
      this((OutputStream)(new FileOutputStream(var1)), var2);
   }

   public String getEncoding() {
      return this.encoding;
   }

   public String getDefaultEncoding() {
      return this.defaultEncoding;
   }

   public void close() throws IOException {
      if (this.writer == null) {
         this.encoding = this.defaultEncoding;
         this.writer = new OutputStreamWriter(this.out, this.encoding);
         this.writer.write(this.xmlPrologWriter.toString());
      }

      this.writer.close();
   }

   public void flush() throws IOException {
      if (this.writer != null) {
         this.writer.flush();
      }

   }

   private void detectEncoding(char[] var1, int var2, int var3) throws IOException {
      int var4 = var3;
      StringBuffer var5 = this.xmlPrologWriter.getBuffer();
      if (var5.length() + var3 > 4096) {
         var4 = 4096 - var5.length();
      }

      this.xmlPrologWriter.write(var1, var2, var4);
      if (var5.length() >= 5) {
         if (var5.substring(0, 5).equals("<?xml")) {
            int var6 = var5.indexOf("?>");
            if (var6 > 0) {
               Matcher var7 = ENCODING_PATTERN.matcher(var5.substring(0, var6));
               if (var7.find()) {
                  this.encoding = var7.group(1).toUpperCase();
                  this.encoding = this.encoding.substring(1, this.encoding.length() - 1);
               } else {
                  this.encoding = this.defaultEncoding;
               }
            } else if (var5.length() >= 4096) {
               this.encoding = this.defaultEncoding;
            }
         } else {
            this.encoding = this.defaultEncoding;
         }

         if (this.encoding != null) {
            this.xmlPrologWriter = null;
            this.writer = new OutputStreamWriter(this.out, this.encoding);
            this.writer.write(var5.toString());
            if (var3 > var4) {
               this.writer.write(var1, var2 + var4, var3 - var4);
            }
         }
      }

   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      if (this.xmlPrologWriter != null) {
         this.detectEncoding(var1, var2, var3);
      } else {
         this.writer.write(var1, var2, var3);
      }

   }

   static {
      ENCODING_PATTERN = XmlStreamReader.ENCODING_PATTERN;
   }
}
