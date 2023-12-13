package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;

public class IOUtils {
   private static final int EOF = -1;
   public static final char DIR_SEPARATOR_UNIX = '/';
   public static final char DIR_SEPARATOR_WINDOWS = '\\';
   public static final char DIR_SEPARATOR;
   public static final String LINE_SEPARATOR_UNIX = "\n";
   public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
   public static final String LINE_SEPARATOR;
   private static final int DEFAULT_BUFFER_SIZE = 4096;
   private static final int SKIP_BUFFER_SIZE = 2048;
   private static char[] SKIP_CHAR_BUFFER;
   private static byte[] SKIP_BYTE_BUFFER;

   public static void close(URLConnection var0) {
      if (var0 instanceof HttpURLConnection) {
         ((HttpURLConnection)var0).disconnect();
      }

   }

   public static void closeQuietly(Reader var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(Writer var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(InputStream var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(OutputStream var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(Closeable var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
      }

   }

   public static void closeQuietly(Socket var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void closeQuietly(Selector var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void closeQuietly(ServerSocket var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
         }
      }

   }

   public static InputStream toBufferedInputStream(InputStream var0) throws IOException {
      return ByteArrayOutputStream.toBufferedInputStream(var0);
   }

   public static BufferedReader toBufferedReader(Reader var0) {
      return var0 instanceof BufferedReader ? (BufferedReader)var0 : new BufferedReader(var0);
   }

   public static byte[] toByteArray(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      copy((InputStream)var0, (OutputStream)var1);
      return var1.toByteArray();
   }

   public static byte[] toByteArray(InputStream var0, long var1) throws IOException {
      if (var1 > 2147483647L) {
         throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + var1);
      } else {
         return toByteArray(var0, (int)var1);
      }
   }

   public static byte[] toByteArray(InputStream var0, int var1) throws IOException {
      if (var1 < 0) {
         throw new IllegalArgumentException("Size must be equal or greater than zero: " + var1);
      } else if (var1 == 0) {
         return new byte[0];
      } else {
         byte[] var2 = new byte[var1];

         int var3;
         int var4;
         for(var3 = 0; var3 < var1 && (var4 = var0.read(var2, var3, var1 - var3)) != -1; var3 += var4) {
         }

         if (var3 != var1) {
            throw new IOException("Unexpected readed size. current: " + var3 + ", excepted: " + var1);
         } else {
            return var2;
         }
      }
   }

   public static byte[] toByteArray(Reader var0) throws IOException {
      return toByteArray(var0, Charset.defaultCharset());
   }

   public static byte[] toByteArray(Reader var0, Charset var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      copy((Reader)var0, (OutputStream)var2, (Charset)var1);
      return var2.toByteArray();
   }

   public static byte[] toByteArray(Reader var0, String var1) throws IOException {
      return toByteArray(var0, Charsets.toCharset(var1));
   }

   /** @deprecated */
   @Deprecated
   public static byte[] toByteArray(String var0) throws IOException {
      return var0.getBytes();
   }

   public static byte[] toByteArray(URI var0) throws IOException {
      return toByteArray(var0.toURL());
   }

   public static byte[] toByteArray(URL var0) throws IOException {
      URLConnection var1 = var0.openConnection();
      byte[] var2 = toByteArray(var1);
      close(var1);
      return var2;
   }

   public static byte[] toByteArray(URLConnection var0) throws IOException {
      InputStream var1 = var0.getInputStream();
      byte[] var2 = toByteArray(var1);
      var1.close();
      return var2;
   }

   public static char[] toCharArray(InputStream var0) throws IOException {
      return toCharArray(var0, Charset.defaultCharset());
   }

   public static char[] toCharArray(InputStream var0, Charset var1) throws IOException {
      CharArrayWriter var2 = new CharArrayWriter();
      copy((InputStream)var0, (Writer)var2, (Charset)var1);
      return var2.toCharArray();
   }

   public static char[] toCharArray(InputStream var0, String var1) throws IOException {
      return toCharArray(var0, Charsets.toCharset(var1));
   }

   public static char[] toCharArray(Reader var0) throws IOException {
      CharArrayWriter var1 = new CharArrayWriter();
      copy((Reader)var0, (Writer)var1);
      return var1.toCharArray();
   }

   public static String toString(InputStream var0) throws IOException {
      return toString(var0, Charset.defaultCharset());
   }

   public static String toString(InputStream var0, Charset var1) throws IOException {
      StringBuilderWriter var2 = new StringBuilderWriter();
      copy((InputStream)var0, (Writer)var2, (Charset)var1);
      return var2.toString();
   }

   public static String toString(InputStream var0, String var1) throws IOException {
      return toString(var0, Charsets.toCharset(var1));
   }

   public static String toString(Reader var0) throws IOException {
      StringBuilderWriter var1 = new StringBuilderWriter();
      copy((Reader)var0, (Writer)var1);
      return var1.toString();
   }

   public static String toString(URI var0) throws IOException {
      return toString(var0, Charset.defaultCharset());
   }

   public static String toString(URI var0, Charset var1) throws IOException {
      return toString(var0.toURL(), Charsets.toCharset(var1));
   }

   public static String toString(URI var0, String var1) throws IOException {
      return toString(var0, Charsets.toCharset(var1));
   }

   public static String toString(URL var0) throws IOException {
      return toString(var0, Charset.defaultCharset());
   }

   public static String toString(URL var0, Charset var1) throws IOException {
      InputStream var2 = var0.openStream();
      String var3 = toString(var2, var1);
      var2.close();
      return var3;
   }

   public static String toString(URL var0, String var1) throws IOException {
      return toString(var0, Charsets.toCharset(var1));
   }

   /** @deprecated */
   @Deprecated
   public static String toString(byte[] var0) throws IOException {
      return new String(var0);
   }

   public static String toString(byte[] var0, String var1) throws IOException {
      return new String(var0, Charsets.toCharset(var1));
   }

   public static List readLines(InputStream var0) throws IOException {
      return readLines(var0, Charset.defaultCharset());
   }

   public static List readLines(InputStream var0, Charset var1) throws IOException {
      InputStreamReader var2 = new InputStreamReader(var0, Charsets.toCharset(var1));
      return readLines((Reader)var2);
   }

   public static List readLines(InputStream var0, String var1) throws IOException {
      return readLines(var0, Charsets.toCharset(var1));
   }

   public static List readLines(Reader var0) throws IOException {
      BufferedReader var1 = toBufferedReader(var0);
      ArrayList var2 = new ArrayList();

      for(String var3 = var1.readLine(); var3 != null; var3 = var1.readLine()) {
         var2.add(var3);
      }

      return var2;
   }

   public static LineIterator lineIterator(Reader var0) {
      return new LineIterator(var0);
   }

   public static LineIterator lineIterator(InputStream var0, Charset var1) throws IOException {
      return new LineIterator(new InputStreamReader(var0, Charsets.toCharset(var1)));
   }

   public static LineIterator lineIterator(InputStream var0, String var1) throws IOException {
      return lineIterator(var0, Charsets.toCharset(var1));
   }

   public static InputStream toInputStream(CharSequence var0) {
      return toInputStream(var0, Charset.defaultCharset());
   }

   public static InputStream toInputStream(CharSequence var0, Charset var1) {
      return toInputStream(var0.toString(), var1);
   }

   public static InputStream toInputStream(CharSequence var0, String var1) throws IOException {
      return toInputStream(var0, Charsets.toCharset(var1));
   }

   public static InputStream toInputStream(String var0) {
      return toInputStream(var0, Charset.defaultCharset());
   }

   public static InputStream toInputStream(String var0, Charset var1) {
      return new ByteArrayInputStream(var0.getBytes(Charsets.toCharset(var1)));
   }

   public static InputStream toInputStream(String var0, String var1) throws IOException {
      byte[] var2 = var0.getBytes(Charsets.toCharset(var1));
      return new ByteArrayInputStream(var2);
   }

   public static void write(byte[] var0, OutputStream var1) throws IOException {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   public static void write(byte[] var0, Writer var1) throws IOException {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(byte[] var0, Writer var1, Charset var2) throws IOException {
      if (var0 != null) {
         var1.write(new String(var0, Charsets.toCharset(var2)));
      }

   }

   public static void write(byte[] var0, Writer var1, String var2) throws IOException {
      write(var0, var1, Charsets.toCharset(var2));
   }

   public static void write(char[] var0, Writer var1) throws IOException {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   public static void write(char[] var0, OutputStream var1) throws IOException {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(char[] var0, OutputStream var1, Charset var2) throws IOException {
      if (var0 != null) {
         var1.write((new String(var0)).getBytes(Charsets.toCharset(var2)));
      }

   }

   public static void write(char[] var0, OutputStream var1, String var2) throws IOException {
      write(var0, var1, Charsets.toCharset(var2));
   }

   public static void write(CharSequence var0, Writer var1) throws IOException {
      if (var0 != null) {
         write(var0.toString(), var1);
      }

   }

   public static void write(CharSequence var0, OutputStream var1) throws IOException {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(CharSequence var0, OutputStream var1, Charset var2) throws IOException {
      if (var0 != null) {
         write(var0.toString(), var1, var2);
      }

   }

   public static void write(CharSequence var0, OutputStream var1, String var2) throws IOException {
      write(var0, var1, Charsets.toCharset(var2));
   }

   public static void write(String var0, Writer var1) throws IOException {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   public static void write(String var0, OutputStream var1) throws IOException {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(String var0, OutputStream var1, Charset var2) throws IOException {
      if (var0 != null) {
         var1.write(var0.getBytes(Charsets.toCharset(var2)));
      }

   }

   public static void write(String var0, OutputStream var1, String var2) throws IOException {
      write(var0, var1, Charsets.toCharset(var2));
   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer var0, Writer var1) throws IOException {
      if (var0 != null) {
         var1.write(var0.toString());
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer var0, OutputStream var1) throws IOException {
      write(var0, var1, (String)null);
   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer var0, OutputStream var1, String var2) throws IOException {
      if (var0 != null) {
         var1.write(var0.toString().getBytes(Charsets.toCharset(var2)));
      }

   }

   public static void writeLines(Collection var0, String var1, OutputStream var2) throws IOException {
      writeLines(var0, var1, var2, Charset.defaultCharset());
   }

   public static void writeLines(Collection var0, String var1, OutputStream var2, Charset var3) throws IOException {
      if (var0 != null) {
         if (var1 == null) {
            var1 = LINE_SEPARATOR;
         }

         Charset var4 = Charsets.toCharset(var3);

         for(Iterator var5 = var0.iterator(); var5.hasNext(); var2.write(var1.getBytes(var4))) {
            Object var6 = var5.next();
            if (var6 != null) {
               var2.write(var6.toString().getBytes(var4));
            }
         }

      }
   }

   public static void writeLines(Collection var0, String var1, OutputStream var2, String var3) throws IOException {
      writeLines(var0, var1, var2, Charsets.toCharset(var3));
   }

   public static void writeLines(Collection var0, String var1, Writer var2) throws IOException {
      if (var0 != null) {
         if (var1 == null) {
            var1 = LINE_SEPARATOR;
         }

         for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.write(var1)) {
            Object var4 = var3.next();
            if (var4 != null) {
               var2.write(var4.toString());
            }
         }

      }
   }

   public static int copy(InputStream var0, OutputStream var1) throws IOException {
      long var2 = copyLarge(var0, var1);
      return var2 > 2147483647L ? -1 : (int)var2;
   }

   public static long copyLarge(InputStream var0, OutputStream var1) throws IOException {
      return copyLarge(var0, var1, new byte[4096]);
   }

   public static long copyLarge(InputStream var0, OutputStream var1, byte[] var2) throws IOException {
      long var3 = 0L;

      int var6;
      for(boolean var5 = false; -1 != (var6 = var0.read(var2)); var3 += (long)var6) {
         var1.write(var2, 0, var6);
      }

      return var3;
   }

   public static long copyLarge(InputStream var0, OutputStream var1, long var2, long var4) throws IOException {
      return copyLarge(var0, var1, var2, var4, new byte[4096]);
   }

   public static long copyLarge(InputStream var0, OutputStream var1, long var2, long var4, byte[] var6) throws IOException {
      if (var2 > 0L) {
         skipFully(var0, var2);
      }

      if (var4 == 0L) {
         return 0L;
      } else {
         int var7 = var6.length;
         int var8 = var7;
         if (var4 > 0L && var4 < (long)var7) {
            var8 = (int)var4;
         }

         long var10 = 0L;

         int var9;
         while(var8 > 0 && -1 != (var9 = var0.read(var6, 0, var8))) {
            var1.write(var6, 0, var9);
            var10 += (long)var9;
            if (var4 > 0L) {
               var8 = (int)Math.min(var4 - var10, (long)var7);
            }
         }

         return var10;
      }
   }

   public static void copy(InputStream var0, Writer var1) throws IOException {
      copy(var0, var1, Charset.defaultCharset());
   }

   public static void copy(InputStream var0, Writer var1, Charset var2) throws IOException {
      InputStreamReader var3 = new InputStreamReader(var0, Charsets.toCharset(var2));
      copy((Reader)var3, (Writer)var1);
   }

   public static void copy(InputStream var0, Writer var1, String var2) throws IOException {
      copy(var0, var1, Charsets.toCharset(var2));
   }

   public static int copy(Reader var0, Writer var1) throws IOException {
      long var2 = copyLarge(var0, var1);
      return var2 > 2147483647L ? -1 : (int)var2;
   }

   public static long copyLarge(Reader var0, Writer var1) throws IOException {
      return copyLarge(var0, var1, new char[4096]);
   }

   public static long copyLarge(Reader var0, Writer var1, char[] var2) throws IOException {
      long var3 = 0L;

      int var6;
      for(boolean var5 = false; -1 != (var6 = var0.read(var2)); var3 += (long)var6) {
         var1.write(var2, 0, var6);
      }

      return var3;
   }

   public static long copyLarge(Reader var0, Writer var1, long var2, long var4) throws IOException {
      return copyLarge(var0, var1, var2, var4, new char[4096]);
   }

   public static long copyLarge(Reader var0, Writer var1, long var2, long var4, char[] var6) throws IOException {
      if (var2 > 0L) {
         skipFully(var0, var2);
      }

      if (var4 == 0L) {
         return 0L;
      } else {
         int var7 = var6.length;
         if (var4 > 0L && var4 < (long)var6.length) {
            var7 = (int)var4;
         }

         long var9 = 0L;

         int var8;
         while(var7 > 0 && -1 != (var8 = var0.read(var6, 0, var7))) {
            var1.write(var6, 0, var8);
            var9 += (long)var8;
            if (var4 > 0L) {
               var7 = (int)Math.min(var4 - var9, (long)var6.length);
            }
         }

         return var9;
      }
   }

   public static void copy(Reader var0, OutputStream var1) throws IOException {
      copy(var0, var1, Charset.defaultCharset());
   }

   public static void copy(Reader var0, OutputStream var1, Charset var2) throws IOException {
      OutputStreamWriter var3 = new OutputStreamWriter(var1, Charsets.toCharset(var2));
      copy((Reader)var0, (Writer)var3);
      var3.flush();
   }

   public static void copy(Reader var0, OutputStream var1, String var2) throws IOException {
      copy(var0, var1, Charsets.toCharset(var2));
   }

   public static boolean contentEquals(InputStream var0, InputStream var1) throws IOException {
      if (!(var0 instanceof BufferedInputStream)) {
         var0 = new BufferedInputStream((InputStream)var0);
      }

      if (!(var1 instanceof BufferedInputStream)) {
         var1 = new BufferedInputStream((InputStream)var1);
      }

      int var3;
      for(int var2 = ((InputStream)var0).read(); -1 != var2; var2 = ((InputStream)var0).read()) {
         var3 = ((InputStream)var1).read();
         if (var2 != var3) {
            return false;
         }
      }

      var3 = ((InputStream)var1).read();
      return var3 == -1;
   }

   public static boolean contentEquals(Reader var0, Reader var1) throws IOException {
      BufferedReader var4 = toBufferedReader(var0);
      BufferedReader var5 = toBufferedReader(var1);

      int var3;
      for(int var2 = var4.read(); -1 != var2; var2 = var4.read()) {
         var3 = var5.read();
         if (var2 != var3) {
            return false;
         }
      }

      var3 = var5.read();
      return var3 == -1;
   }

   public static boolean contentEqualsIgnoreEOL(Reader var0, Reader var1) throws IOException {
      BufferedReader var2 = toBufferedReader(var0);
      BufferedReader var3 = toBufferedReader(var1);
      String var4 = var2.readLine();

      String var5;
      for(var5 = var3.readLine(); var4 != null && var5 != null && var4.equals(var5); var5 = var3.readLine()) {
         var4 = var2.readLine();
      }

      return var4 == null ? var5 == null : var4.equals(var5);
   }

   public static long skip(InputStream var0, long var1) throws IOException {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + var1);
      } else {
         if (SKIP_BYTE_BUFFER == null) {
            SKIP_BYTE_BUFFER = new byte[2048];
         }

         long var3;
         long var5;
         for(var3 = var1; var3 > 0L; var3 -= var5) {
            var5 = (long)var0.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(var3, 2048L));
            if (var5 < 0L) {
               break;
            }
         }

         return var1 - var3;
      }
   }

   public static long skip(Reader var0, long var1) throws IOException {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + var1);
      } else {
         if (SKIP_CHAR_BUFFER == null) {
            SKIP_CHAR_BUFFER = new char[2048];
         }

         long var3;
         long var5;
         for(var3 = var1; var3 > 0L; var3 -= var5) {
            var5 = (long)var0.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(var3, 2048L));
            if (var5 < 0L) {
               break;
            }
         }

         return var1 - var3;
      }
   }

   public static void skipFully(InputStream var0, long var1) throws IOException {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Bytes to skip must not be negative: " + var1);
      } else {
         long var3 = skip(var0, var1);
         if (var3 != var1) {
            throw new EOFException("Bytes to skip: " + var1 + " actual: " + var3);
         }
      }
   }

   public static void skipFully(Reader var0, long var1) throws IOException {
      long var3 = skip(var0, var1);
      if (var3 != var1) {
         throw new EOFException("Chars to skip: " + var1 + " actual: " + var3);
      }
   }

   public static int read(Reader var0, char[] var1, int var2, int var3) throws IOException {
      if (var3 < 0) {
         throw new IllegalArgumentException("Length must not be negative: " + var3);
      } else {
         int var4;
         int var6;
         for(var4 = var3; var4 > 0; var4 -= var6) {
            int var5 = var3 - var4;
            var6 = var0.read(var1, var2 + var5, var4);
            if (-1 == var6) {
               break;
            }
         }

         return var3 - var4;
      }
   }

   public static int read(Reader var0, char[] var1) throws IOException {
      return read((Reader)var0, (char[])var1, 0, var1.length);
   }

   public static int read(InputStream var0, byte[] var1, int var2, int var3) throws IOException {
      if (var3 < 0) {
         throw new IllegalArgumentException("Length must not be negative: " + var3);
      } else {
         int var4;
         int var6;
         for(var4 = var3; var4 > 0; var4 -= var6) {
            int var5 = var3 - var4;
            var6 = var0.read(var1, var2 + var5, var4);
            if (-1 == var6) {
               break;
            }
         }

         return var3 - var4;
      }
   }

   public static int read(InputStream var0, byte[] var1) throws IOException {
      return read((InputStream)var0, (byte[])var1, 0, var1.length);
   }

   public static void readFully(Reader var0, char[] var1, int var2, int var3) throws IOException {
      int var4 = read(var0, var1, var2, var3);
      if (var4 != var3) {
         throw new EOFException("Length to read: " + var3 + " actual: " + var4);
      }
   }

   public static void readFully(Reader var0, char[] var1) throws IOException {
      readFully((Reader)var0, (char[])var1, 0, var1.length);
   }

   public static void readFully(InputStream var0, byte[] var1, int var2, int var3) throws IOException {
      int var4 = read(var0, var1, var2, var3);
      if (var4 != var3) {
         throw new EOFException("Length to read: " + var3 + " actual: " + var4);
      }
   }

   public static void readFully(InputStream var0, byte[] var1) throws IOException {
      readFully((InputStream)var0, (byte[])var1, 0, var1.length);
   }

   static {
      DIR_SEPARATOR = File.separatorChar;
      StringBuilderWriter var0 = new StringBuilderWriter(4);
      PrintWriter var1 = new PrintWriter(var0);
      var1.println();
      LINE_SEPARATOR = var0.toString();
      var1.close();
   }
}
