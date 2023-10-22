package com.sun.glass.ui.win;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

class HTMLCodec extends InputStream {
   public static final String ENCODING = "UTF-8";
   public static final String VERSION = "Version:";
   public static final String START_HTML = "StartHTML:";
   public static final String END_HTML = "EndHTML:";
   public static final String START_FRAGMENT = "StartFragment:";
   public static final String END_FRAGMENT = "EndFragment:";
   public static final String START_SELECTION = "StartSelection:";
   public static final String END_SELECTION = "EndSelection:";
   public static final String START_FRAGMENT_CMT = "<!--StartFragment-->";
   public static final String END_FRAGMENT_CMT = "<!--EndFragment-->";
   public static final String SOURCE_URL = "SourceURL:";
   public static final String DEF_SOURCE_URL = "about:blank";
   public static final String EOLN = "\r\n";
   private static final String VERSION_NUM = "1.0";
   private static final int PADDED_WIDTH = 10;
   private final BufferedInputStream bufferedStream;
   private boolean descriptionParsed = false;
   private boolean closed = false;
   public static final int BYTE_BUFFER_LEN = 8192;
   public static final int CHAR_BUFFER_LEN = 2730;
   private static final String FAILURE_MSG = "Unable to parse HTML description: ";
   private static final String INVALID_MSG = " invalid";
   private long iHTMLStart;
   private long iHTMLEnd;
   private long iFragStart;
   private long iFragEnd;
   private long iSelStart;
   private long iSelEnd;
   private String stBaseURL;
   private String stVersion;
   private long iStartOffset;
   private long iEndOffset;
   private long iReadCount;
   private EHTMLReadMode readMode;

   private static String toPaddedString(int var0, int var1) {
      String var2 = "" + var0;
      int var3 = var2.length();
      if (var0 >= 0 && var3 < var1) {
         char[] var4 = new char[var1 - var3];
         Arrays.fill(var4, '0');
         StringBuffer var5 = new StringBuffer(var1);
         var5.append(var4);
         var5.append(var2);
         var2 = var5.toString();
      }

      return var2;
   }

   public static byte[] convertToHTMLFormat(byte[] var0) {
      String var1 = "";
      String var2 = "";
      String var3 = new String(var0);
      String var4 = var3.toUpperCase();
      if (-1 == var4.indexOf("<HTML")) {
         var1 = "<HTML>";
         var2 = "</HTML>";
         if (-1 == var4.indexOf("<BODY")) {
            var1 = var1 + "<BODY>";
            var2 = "</BODY>" + var2;
         }
      }

      var1 = var1 + "<!--StartFragment-->";
      var2 = "<!--EndFragment-->" + var2;
      var3 = "about:blank";
      int var13 = "Version:".length() + "1.0".length() + "\r\n".length() + "StartHTML:".length() + 10 + "\r\n".length() + "EndHTML:".length() + 10 + "\r\n".length() + "StartFragment:".length() + 10 + "\r\n".length() + "EndFragment:".length() + 10 + "\r\n".length() + "SourceURL:".length() + var3.length() + "\r\n".length();
      int var5 = var13 + var1.length();
      int var6 = var5 + var0.length - 1;
      int var7 = var6 + var2.length();
      StringBuilder var8 = new StringBuilder(var5 + "<!--StartFragment-->".length());
      var8.append("Version:");
      var8.append("1.0");
      var8.append("\r\n");
      var8.append("StartHTML:");
      var8.append(toPaddedString(var13, 10));
      var8.append("\r\n");
      var8.append("EndHTML:");
      var8.append(toPaddedString(var7, 10));
      var8.append("\r\n");
      var8.append("StartFragment:");
      var8.append(toPaddedString(var5, 10));
      var8.append("\r\n");
      var8.append("EndFragment:");
      var8.append(toPaddedString(var6, 10));
      var8.append("\r\n");
      var8.append("SourceURL:");
      var8.append(var3);
      var8.append("\r\n");
      var8.append(var1);
      Object var9 = null;
      Object var10 = null;

      byte[] var14;
      byte[] var15;
      try {
         var14 = var8.toString().getBytes("UTF-8");
         var15 = var2.getBytes("UTF-8");
      } catch (UnsupportedEncodingException var12) {
         return null;
      }

      byte[] var11 = new byte[var14.length + var0.length + var15.length];
      System.arraycopy(var14, 0, var11, 0, var14.length);
      System.arraycopy(var0, 0, var11, var14.length, var0.length - 1);
      System.arraycopy(var15, 0, var11, var14.length + var0.length - 1, var15.length);
      var11[var11.length - 1] = 0;
      return var11;
   }

   public HTMLCodec(InputStream var1, EHTMLReadMode var2) throws IOException {
      this.bufferedStream = new BufferedInputStream(var1, 8192);
      this.readMode = var2;
   }

   public synchronized String getBaseURL() throws IOException {
      if (!this.descriptionParsed) {
         this.parseDescription();
      }

      return this.stBaseURL;
   }

   public synchronized String getVersion() throws IOException {
      if (!this.descriptionParsed) {
         this.parseDescription();
      }

      return this.stVersion;
   }

   private void parseDescription() throws IOException {
      this.stBaseURL = null;
      this.stVersion = null;
      this.iHTMLEnd = this.iHTMLStart = this.iFragEnd = this.iFragStart = this.iSelEnd = this.iSelStart = -1L;
      this.bufferedStream.mark(8192);
      String[] var1 = new String[]{"Version:", "StartHTML:", "EndHTML:", "StartFragment:", "EndFragment:", "StartSelection:", "EndSelection:", "SourceURL:"};
      BufferedReader var2 = new BufferedReader(new InputStreamReader(this.bufferedStream, "UTF-8"), 2730);
      long var3 = 0L;
      long var5 = (long)"\r\n".length();
      int var7 = var1.length;
      boolean var8 = true;

      int var9;
      label88:
      for(var9 = 0; var9 < var7; ++var9) {
         String var10 = var2.readLine();
         if (null == var10) {
            break;
         }

         while(var9 < var7) {
            if (var10.startsWith(var1[var9])) {
               var3 += (long)var10.length() + var5;
               String var11 = var10.substring(var1[var9].length()).trim();
               if (null != var11) {
                  try {
                     switch (var9) {
                        case 0:
                           this.stVersion = var11;
                           continue label88;
                        case 1:
                           this.iHTMLStart = (long)Integer.parseInt(var11);
                           continue label88;
                        case 2:
                           this.iHTMLEnd = (long)Integer.parseInt(var11);
                           continue label88;
                        case 3:
                           this.iFragStart = (long)Integer.parseInt(var11);
                           continue label88;
                        case 4:
                           this.iFragEnd = (long)Integer.parseInt(var11);
                           continue label88;
                        case 5:
                           this.iSelStart = (long)Integer.parseInt(var11);
                           continue label88;
                        case 6:
                           this.iSelEnd = (long)Integer.parseInt(var11);
                           continue label88;
                        case 7:
                           this.stBaseURL = var11;
                     }
                  } catch (NumberFormatException var13) {
                     throw new IOException("Unable to parse HTML description: " + var1[var9] + " value " + var13 + " invalid");
                  }
               }
               break;
            }

            ++var9;
         }
      }

      if (-1L == this.iHTMLStart) {
         this.iHTMLStart = var3;
      }

      if (-1L == this.iFragStart) {
         this.iFragStart = this.iHTMLStart;
      }

      if (-1L == this.iFragEnd) {
         this.iFragEnd = this.iHTMLEnd;
      }

      if (-1L == this.iSelStart) {
         this.iSelStart = this.iFragStart;
      }

      if (-1L == this.iSelEnd) {
         this.iSelEnd = this.iFragEnd;
      }

      switch (this.readMode) {
         case HTML_READ_ALL:
            this.iStartOffset = this.iHTMLStart;
            this.iEndOffset = this.iHTMLEnd;
            break;
         case HTML_READ_FRAGMENT:
            this.iStartOffset = this.iFragStart;
            this.iEndOffset = this.iFragEnd;
            break;
         case HTML_READ_SELECTION:
         default:
            this.iStartOffset = this.iSelStart;
            this.iEndOffset = this.iSelEnd;
      }

      this.bufferedStream.reset();
      if (-1L == this.iStartOffset) {
         throw new IOException("Unable to parse HTML description: invalid HTML format.");
      } else {
         for(var9 = 0; (long)var9 < this.iStartOffset; var9 = (int)((long)var9 + this.bufferedStream.skip(this.iStartOffset - (long)var9))) {
         }

         this.iReadCount = (long)var9;
         if (this.iStartOffset != this.iReadCount) {
            throw new IOException("Unable to parse HTML description: Byte stream ends in description.");
         } else {
            this.descriptionParsed = true;
         }
      }
   }

   public synchronized int read() throws IOException {
      if (this.closed) {
         throw new IOException("Stream closed");
      } else {
         if (!this.descriptionParsed) {
            this.parseDescription();
         }

         if (-1L != this.iEndOffset && this.iReadCount >= this.iEndOffset) {
            return -1;
         } else {
            int var1 = this.bufferedStream.read();
            if (var1 == -1) {
               return -1;
            } else {
               ++this.iReadCount;
               return var1;
            }
         }
      }
   }

   public synchronized void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.bufferedStream.close();
      }

   }
}
