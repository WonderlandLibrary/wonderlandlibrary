package com.sun.webkit.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DirectoryURLConnection extends URLConnection {
   private static final String[] patStrings = new String[]{"([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*((?:\\d\\d:\\d\\d)|(?:\\d{4}))\\s*(\\p{Print}*)", "(\\d{2}/\\d{2}/\\d{4})\\s*(\\d{2}:\\d{2}[ap])\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)", "(\\d{2}-\\d{2}-\\d{2})\\s*(\\d{2}:\\d{2}[AP]M)\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)"};
   private static final int[][] patternGroups = new int[][]{{7, 4, 5, 6, 1}, {4, 3, 1, 2, 0}, {4, 3, 1, 2, 0}};
   private static final Pattern[] patterns;
   private static final Pattern linkp = Pattern.compile("(\\p{Print}+) \\-\\> (\\p{Print}+)$");
   private static final String styleSheet = "<style type=\"text/css\" media=\"screen\">TABLE { border: 0;}TR.header { background: #FFFFFF; color: black; font-weight: bold; text-align: center;}TR.odd { background: #E0E0E0;}TR.even { background: #C0C0C0;}TD.file { text-align: left;}TD.fsize { text-align: right; padding-right: 1em;}TD.dir { text-align: center; color: green; padding-right: 1em;}TD.link { text-align: center; color: red; padding-right: 1em;}TD.date { text-align: justify;}</style>";
   private final URLConnection inner;
   private final boolean sure;
   private String dirUrl = null;
   private boolean toHTML = true;
   private final boolean ftp;
   private InputStream ins = null;

   DirectoryURLConnection(URLConnection var1, boolean var2) {
      super(var1.getURL());
      this.dirUrl = var1.getURL().toExternalForm();
      this.inner = var1;
      this.sure = !var2;
      this.ftp = true;
   }

   DirectoryURLConnection(URLConnection var1) {
      super(var1.getURL());
      this.dirUrl = var1.getURL().toExternalForm();
      this.ftp = false;
      this.sure = true;
      this.inner = var1;
   }

   public void connect() throws IOException {
      this.inner.connect();
   }

   public InputStream getInputStream() throws IOException {
      if (this.ins == null) {
         if (this.ftp) {
            this.ins = new DirectoryInputStream(this.inner.getInputStream(), !this.sure);
         } else {
            this.ins = new DirectoryInputStream(this.inner.getInputStream(), false);
         }
      }

      return this.ins;
   }

   public String getContentType() {
      try {
         if (!this.sure) {
            this.getInputStream();
         }
      } catch (IOException var2) {
      }

      return this.toHTML ? "text/html" : this.inner.getContentType();
   }

   public String getContentEncoding() {
      return this.inner.getContentEncoding();
   }

   public int getContentLength() {
      return this.inner.getContentLength();
   }

   public Map getHeaderFields() {
      return this.inner.getHeaderFields();
   }

   public String getHeaderField(String var1) {
      return this.inner.getHeaderField(var1);
   }

   static {
      patterns = new Pattern[patStrings.length];

      for(int var0 = 0; var0 < patStrings.length; ++var0) {
         patterns[var0] = Pattern.compile(patStrings[var0]);
      }

   }

   private final class DirectoryInputStream extends PushbackInputStream {
      private final byte[] buffer;
      private boolean endOfStream;
      private ByteArrayOutputStream bytesOut;
      private PrintStream out;
      private ByteArrayInputStream bytesIn;
      private final StringBuffer tmpString;
      private int lineCount;

      private DirectoryInputStream(InputStream var2, boolean var3) {
         super(var2, 512);
         this.endOfStream = false;
         this.bytesOut = new ByteArrayOutputStream();
         this.out = new PrintStream(this.bytesOut);
         this.bytesIn = null;
         this.tmpString = new StringBuffer();
         this.lineCount = 0;
         this.buffer = new byte[512];
         int var7;
         if (var3) {
            StringBuffer var4 = new StringBuffer();
            int var5 = 0;

            try {
               var5 = super.read(this.buffer, 0, this.buffer.length);
            } catch (IOException var15) {
            }

            if (var5 <= 0) {
               DirectoryURLConnection.this.toHTML = false;
            } else {
               for(var7 = 0; var7 < var5; ++var7) {
                  var4.append((char)this.buffer[var7]);
               }

               String var18 = var4.toString();
               DirectoryURLConnection.this.toHTML = false;
               Pattern[] var8 = DirectoryURLConnection.patterns;
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  Pattern var11 = var8[var10];
                  Matcher var12 = var11.matcher(var18);
                  if (var12.find()) {
                     DirectoryURLConnection.this.toHTML = true;
                     break;
                  }
               }

               try {
                  super.unread(this.buffer, 0, var5);
               } catch (IOException var14) {
               }
            }
         }

         if (DirectoryURLConnection.this.toHTML) {
            String var16 = null;
            URL var6 = null;
            if (!DirectoryURLConnection.this.dirUrl.endsWith("/")) {
               DirectoryURLConnection.this.dirUrl = DirectoryURLConnection.this.dirUrl + "/";
            }

            try {
               var6 = URLs.newURL(DirectoryURLConnection.this.dirUrl);
            } catch (Exception var13) {
            }

            String var17 = var6.getPath();
            if (var17 != null && !var17.isEmpty()) {
               var7 = var17.lastIndexOf("/", var17.length() - 2);
               if (var7 >= 0) {
                  int var19 = var17.length() - var7 - 1;
                  var7 = DirectoryURLConnection.this.dirUrl.indexOf(var17);
                  var16 = DirectoryURLConnection.this.dirUrl.substring(0, var7 + var17.length() - var19) + DirectoryURLConnection.this.dirUrl.substring(var7 + var17.length());
               }
            }

            this.out.print("<html><head><title>index of ");
            this.out.print(DirectoryURLConnection.this.dirUrl);
            this.out.print("</title>");
            this.out.print("<style type=\"text/css\" media=\"screen\">TABLE { border: 0;}TR.header { background: #FFFFFF; color: black; font-weight: bold; text-align: center;}TR.odd { background: #E0E0E0;}TR.even { background: #C0C0C0;}TD.file { text-align: left;}TD.fsize { text-align: right; padding-right: 1em;}TD.dir { text-align: center; color: green; padding-right: 1em;}TD.link { text-align: center; color: red; padding-right: 1em;}TD.date { text-align: justify;}</style>");
            this.out.print("</head><body><h1>Index of ");
            this.out.print(DirectoryURLConnection.this.dirUrl);
            this.out.print("</h1><hr></hr>");
            this.out.print("<TABLE width=\"95%\" cellpadding=\"5\" cellspacing=\"5\">");
            this.out.print("<TR class=\"header\"><TD>File</TD><TD>Size</TD><TD>Last Modified</TD></TR>");
            if (var16 != null) {
               ++this.lineCount;
               this.out.print("<TR class=\"odd\"><TD colspan=3 class=\"file\"><a href=\"");
               this.out.print(var16);
               this.out.print("\">Up to parent directory</a></TD></TR>");
            }

            this.out.close();
            this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
            this.out = null;
            this.bytesOut = null;
         }

      }

      private void parseFile(String var1) {
         this.tmpString.append(var1);

         int var2;
         while((var2 = this.tmpString.indexOf("\n")) >= 0) {
            String var3 = this.tmpString.substring(0, var2);
            this.tmpString.delete(0, var2 + 1);
            String var4 = var3;
            String var5 = null;
            String var6 = null;
            boolean var7 = false;
            boolean var8 = false;
            URL var9 = null;
            if (var3 != null) {
               ++this.lineCount;

               try {
                  var9 = URLs.newURL(DirectoryURLConnection.this.dirUrl + URLEncoder.encode(var4, "UTF-8"));
                  URLConnection var10 = var9.openConnection();
                  var10.connect();
                  var6 = var10.getHeaderField("last-modified");
                  var5 = var10.getHeaderField("content-length");
                  if (var5 == null) {
                     var7 = true;
                  }

                  var10.getInputStream().close();
               } catch (IOException var11) {
                  var8 = true;
               }

               if (this.bytesOut == null) {
                  this.bytesOut = new ByteArrayOutputStream();
                  this.out = new PrintStream(this.bytesOut);
               }

               this.out.print("<TR class=\"" + (this.lineCount % 2 == 0 ? "even" : "odd") + "\"><TD class=\"file\">");
               if (var8) {
                  this.out.print(var3);
               } else {
                  this.out.print("<a href=\"");
                  this.out.print(var9.toExternalForm());
                  this.out.print("\">");
                  this.out.print(var3);
                  this.out.print("</a>");
               }

               if (var7) {
                  this.out.print("</TD><TD class=\"dir\">&lt;Directory&gt;</TD>");
               } else {
                  this.out.print("</TD><TD class=\"fsize\">" + (var5 == null ? " " : var5) + "</TD>");
               }

               this.out.print("<TD class=\"date\">" + (var6 == null ? " " : var6) + "</TD></TR>");
            }
         }

         if (this.bytesOut != null) {
            this.out.close();
            this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
            this.out = null;
            this.bytesOut = null;
         }

      }

      private void parseFTP(String var1) {
         this.tmpString.append(var1);

         int var2;
         while((var2 = this.tmpString.indexOf("\n")) >= 0) {
            String var3 = this.tmpString.substring(0, var2);
            this.tmpString.delete(0, var2 + 1);
            String var4 = null;
            String var5 = null;
            String var6 = null;
            String var7 = null;
            boolean var8 = false;
            Matcher var9 = null;

            for(int var10 = 0; var10 < DirectoryURLConnection.patterns.length; ++var10) {
               var9 = DirectoryURLConnection.patterns[var10].matcher(var3);
               if (var9.find()) {
                  var4 = var9.group(DirectoryURLConnection.patternGroups[var10][0]);
                  var6 = var9.group(DirectoryURLConnection.patternGroups[var10][1]);
                  var7 = var9.group(DirectoryURLConnection.patternGroups[var10][2]);
                  if (DirectoryURLConnection.patternGroups[var10][3] > 0) {
                     var7 = var7 + " " + var9.group(DirectoryURLConnection.patternGroups[var10][3]);
                  }

                  if (DirectoryURLConnection.patternGroups[var10][4] > 0) {
                     String var11 = var9.group(DirectoryURLConnection.patternGroups[var10][4]);
                     var8 = var11.startsWith("d");
                  }

                  if ("<DIR>".equals(var6)) {
                     var8 = true;
                     var6 = null;
                  }
               }
            }

            if (var4 != null) {
               var9 = DirectoryURLConnection.linkp.matcher(var4);
               if (var9.find()) {
                  var4 = var9.group(1);
                  var5 = var9.group(2);
               }

               if (this.bytesOut == null) {
                  this.bytesOut = new ByteArrayOutputStream();
                  this.out = new PrintStream(this.bytesOut);
               }

               ++this.lineCount;
               this.out.print("<TR class=\"" + (this.lineCount % 2 == 0 ? "even" : "odd") + "\"><TD class=\"file\"><a href=\"");

               try {
                  this.out.print(DirectoryURLConnection.this.dirUrl + URLEncoder.encode(var4, "UTF-8"));
               } catch (UnsupportedEncodingException var12) {
               }

               if (var8) {
                  this.out.print("/");
               }

               this.out.print("\">");
               this.out.print(var4);
               this.out.print("</a>");
               if (var5 != null) {
                  this.out.print(" &rarr; " + var5 + "</TD><TD class=\"link\">&lt;Link&gt;</TD>");
               } else if (var8) {
                  this.out.print("</TD><TD class=\"dir\">&lt;Directory&gt;</TD>");
               } else {
                  this.out.print("</TD><TD class=\"fsize\">" + var6 + "</TD>");
               }

               this.out.print("<TD class=\"date\">" + var7 + "</TD></TR>");
            }
         }

         if (this.bytesOut != null) {
            this.out.close();
            this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
            this.out = null;
            this.bytesOut = null;
         }

      }

      private void endOfList() {
         if (DirectoryURLConnection.this.ftp) {
            this.parseFTP("\n");
         } else {
            this.parseFile("\n");
         }

         if (this.bytesOut == null) {
            this.bytesOut = new ByteArrayOutputStream();
            this.out = new PrintStream(this.bytesOut);
         }

         this.out.print("</TABLE><br><hr></hr></body></html>");
         this.out.close();
         this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
         this.out = null;
         this.bytesOut = null;
      }

      public int read(byte[] var1) throws IOException {
         return this.read(var1, 0, var1.length);
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         boolean var4 = false;
         if (!DirectoryURLConnection.this.toHTML) {
            return super.read(var1, var2, var3);
         } else {
            int var5;
            if (this.bytesIn != null) {
               var5 = this.bytesIn.read(var1, var2, var3);
               if (var5 != -1) {
                  return var5;
               }

               this.bytesIn.close();
               this.bytesIn = null;
               if (this.endOfStream) {
                  return -1;
               }
            }

            if (!this.endOfStream) {
               var5 = super.read(this.buffer, 0, this.buffer.length);
               if (var5 == -1) {
                  this.endOfStream = true;
                  this.endOfList();
                  return this.read(var1, var2, var3);
               }

               if (DirectoryURLConnection.this.ftp) {
                  this.parseFTP(new String(this.buffer, 0, var5));
               } else {
                  this.parseFile(new String(this.buffer, 0, var5));
               }

               if (this.bytesIn != null) {
                  return this.read(var1, var2, var3);
               }
            }

            return 0;
         }
      }

      // $FF: synthetic method
      DirectoryInputStream(InputStream var2, boolean var3, Object var4) {
         this(var2, var3);
      }
   }
}
