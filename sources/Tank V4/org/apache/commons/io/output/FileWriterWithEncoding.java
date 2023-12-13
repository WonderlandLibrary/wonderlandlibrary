package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileWriterWithEncoding extends Writer {
   private final Writer out;

   public FileWriterWithEncoding(String var1, String var2) throws IOException {
      this(new File(var1), var2, false);
   }

   public FileWriterWithEncoding(String var1, String var2, boolean var3) throws IOException {
      this(new File(var1), var2, var3);
   }

   public FileWriterWithEncoding(String var1, Charset var2) throws IOException {
      this(new File(var1), var2, false);
   }

   public FileWriterWithEncoding(String var1, Charset var2, boolean var3) throws IOException {
      this(new File(var1), var2, var3);
   }

   public FileWriterWithEncoding(String var1, CharsetEncoder var2) throws IOException {
      this(new File(var1), var2, false);
   }

   public FileWriterWithEncoding(String var1, CharsetEncoder var2, boolean var3) throws IOException {
      this(new File(var1), var2, var3);
   }

   public FileWriterWithEncoding(File var1, String var2) throws IOException {
      this(var1, var2, false);
   }

   public FileWriterWithEncoding(File var1, String var2, boolean var3) throws IOException {
      this.out = initWriter(var1, var2, var3);
   }

   public FileWriterWithEncoding(File var1, Charset var2) throws IOException {
      this(var1, var2, false);
   }

   public FileWriterWithEncoding(File var1, Charset var2, boolean var3) throws IOException {
      this.out = initWriter(var1, var2, var3);
   }

   public FileWriterWithEncoding(File var1, CharsetEncoder var2) throws IOException {
      this(var1, var2, false);
   }

   public FileWriterWithEncoding(File var1, CharsetEncoder var2, boolean var3) throws IOException {
      this.out = initWriter(var1, var2, var3);
   }

   private static Writer initWriter(File var0, Object var1, boolean var2) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("File is missing");
      } else if (var1 == null) {
         throw new NullPointerException("Encoding is missing");
      } else {
         boolean var3 = var0.exists();
         FileOutputStream var4 = null;
         OutputStreamWriter var5 = null;

         try {
            var4 = new FileOutputStream(var0, var2);
            if (var1 instanceof Charset) {
               var5 = new OutputStreamWriter(var4, (Charset)var1);
            } else if (var1 instanceof CharsetEncoder) {
               var5 = new OutputStreamWriter(var4, (CharsetEncoder)var1);
            } else {
               var5 = new OutputStreamWriter(var4, (String)var1);
            }

            return var5;
         } catch (IOException var7) {
            IOUtils.closeQuietly((Writer)var5);
            IOUtils.closeQuietly((OutputStream)var4);
            if (!var3) {
               FileUtils.deleteQuietly(var0);
            }

            throw var7;
         } catch (RuntimeException var8) {
            IOUtils.closeQuietly((Writer)var5);
            IOUtils.closeQuietly((OutputStream)var4);
            if (!var3) {
               FileUtils.deleteQuietly(var0);
            }

            throw var8;
         }
      }
   }

   public void write(int var1) throws IOException {
      this.out.write(var1);
   }

   public void write(char[] var1) throws IOException {
      this.out.write(var1);
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
   }

   public void write(String var1) throws IOException {
      this.out.write(var1);
   }

   public void write(String var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void close() throws IOException {
      this.out.close();
   }
}
