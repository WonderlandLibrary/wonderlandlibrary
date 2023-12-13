package javassist.tools.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.Translator;

public class Webserver {
   private ServerSocket socket;
   private ClassPool classPool;
   protected Translator translator;
   private static final byte[] endofline = new byte[]{13, 10};
   private static final int typeHtml = 1;
   private static final int typeClass = 2;
   private static final int typeGif = 3;
   private static final int typeJpeg = 4;
   private static final int typeText = 5;
   public String debugDir;
   public String htmlfileBase;

   public static void main(String[] var0) throws IOException {
      if (var0.length == 1) {
         Webserver var1 = new Webserver(var0[0]);
         var1.run();
      } else {
         System.err.println("Usage: java javassist.tools.web.Webserver <port number>");
      }

   }

   public Webserver(String var1) throws IOException {
      this(Integer.parseInt(var1));
   }

   public Webserver(int var1) throws IOException {
      this.debugDir = null;
      this.htmlfileBase = null;
      this.socket = new ServerSocket(var1);
      this.classPool = null;
      this.translator = null;
   }

   public void setClassPool(ClassPool var1) {
      this.classPool = var1;
   }

   public void addTranslator(ClassPool var1, Translator var2) throws NotFoundException, CannotCompileException {
      this.classPool = var1;
      this.translator = var2;
      var2.start(this.classPool);
   }

   public void end() throws IOException {
      this.socket.close();
   }

   public void logging(String var1) {
      System.out.println(var1);
   }

   public void logging(String var1, String var2) {
      System.out.print(var1);
      System.out.print(" ");
      System.out.println(var2);
   }

   public void logging(String var1, String var2, String var3) {
      System.out.print(var1);
      System.out.print(" ");
      System.out.print(var2);
      System.out.print(" ");
      System.out.println(var3);
   }

   public void logging2(String var1) {
      System.out.print("    ");
      System.out.println(var1);
   }

   public void run() {
      System.err.println("ready to service...");

      while(true) {
         while(true) {
            try {
               ServiceThread var1 = new ServiceThread(this, this.socket.accept());
               var1.start();
            } catch (IOException var2) {
               this.logging(var2.toString());
            }
         }
      }
   }

   final void process(Socket var1) throws IOException {
      BufferedInputStream var2 = new BufferedInputStream(var1.getInputStream());
      String var3 = this.readLine(var2);
      this.logging(var1.getInetAddress().getHostName(), (new Date()).toString(), var3);

      while(this.skipLine(var2) > 0) {
      }

      BufferedOutputStream var4 = new BufferedOutputStream(var1.getOutputStream());

      try {
         this.doReply(var2, var4, var3);
      } catch (BadHttpRequest var6) {
         this.replyError(var4, var6);
      }

      var4.flush();
      var2.close();
      var4.close();
      var1.close();
   }

   private String readLine(InputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();

      int var3;
      while((var3 = var1.read()) >= 0 && var3 != 13) {
         var2.append((char)var3);
      }

      var1.read();
      return var2.toString();
   }

   private int skipLine(InputStream var1) throws IOException {
      int var2;
      int var3;
      for(var3 = 0; (var2 = var1.read()) >= 0 && var2 != 13; ++var3) {
      }

      var1.read();
      return var3;
   }

   public void doReply(InputStream var1, OutputStream var2, String var3) throws IOException, BadHttpRequest {
      if (!var3.startsWith("GET /")) {
         throw new BadHttpRequest();
      } else {
         String var7;
         String var6 = var7 = var3.substring(5, var3.indexOf(32, 5));
         byte var5;
         if (var6.endsWith(".class")) {
            var5 = 2;
         } else if (!var6.endsWith(".html") && !var6.endsWith(".htm")) {
            if (var6.endsWith(".gif")) {
               var5 = 3;
            } else if (var6.endsWith(".jpg")) {
               var5 = 4;
            } else {
               var5 = 5;
            }
         } else {
            var5 = 1;
         }

         int var4 = var6.length();
         if (var5 != 2 || var4 != null) {
            this.checkFilename(var6, var4);
            if (this.htmlfileBase != null) {
               var6 = this.htmlfileBase + var6;
            }

            if (File.separatorChar != '/') {
               var6 = var6.replace('/', File.separatorChar);
            }

            File var8 = new File(var6);
            if (var8.canRead()) {
               this.sendHeader(var2, var8.length(), var5);
               FileInputStream var13 = new FileInputStream(var8);
               byte[] var14 = new byte[4096];

               while(true) {
                  var4 = var13.read(var14);
                  if (var4 <= 0) {
                     var13.close();
                     return;
                  }

                  var2.write(var14, 0, var4);
               }
            } else {
               if (var5 == 2) {
                  InputStream var9 = this.getClass().getResourceAsStream("/" + var7);
                  if (var9 != null) {
                     ByteArrayOutputStream var10 = new ByteArrayOutputStream();
                     byte[] var11 = new byte[4096];

                     while(true) {
                        var4 = var9.read(var11);
                        if (var4 <= 0) {
                           byte[] var12 = var10.toByteArray();
                           this.sendHeader(var2, (long)var12.length, 2);
                           var2.write(var12);
                           var9.close();
                           return;
                        }

                        var10.write(var11, 0, var4);
                     }
                  }
               }

               throw new BadHttpRequest();
            }
         }
      }
   }

   private void checkFilename(String var1, int var2) throws BadHttpRequest {
      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = var1.charAt(var3);
         if (!Character.isJavaIdentifierPart(var4) && var4 != '.' && var4 != '/') {
            throw new BadHttpRequest();
         }
      }

      if (var1.indexOf("..") >= 0) {
         throw new BadHttpRequest();
      }
   }

   private void sendHeader(OutputStream var1, long var2, int var4) throws IOException {
      var1.write("HTTP/1.0 200 OK".getBytes());
      var1.write(endofline);
      var1.write("Content-Length: ".getBytes());
      var1.write(Long.toString(var2).getBytes());
      var1.write(endofline);
      if (var4 == 2) {
         var1.write("Content-Type: application/octet-stream".getBytes());
      } else if (var4 == 1) {
         var1.write("Content-Type: text/html".getBytes());
      } else if (var4 == 3) {
         var1.write("Content-Type: image/gif".getBytes());
      } else if (var4 == 4) {
         var1.write("Content-Type: image/jpg".getBytes());
      } else if (var4 == 5) {
         var1.write("Content-Type: text/plain".getBytes());
      }

      var1.write(endofline);
      var1.write(endofline);
   }

   private void replyError(OutputStream var1, BadHttpRequest var2) throws IOException {
      this.logging2("bad request: " + var2.toString());
      var1.write("HTTP/1.0 400 Bad Request".getBytes());
      var1.write(endofline);
      var1.write(endofline);
      var1.write("<H1>Bad Request</H1>".getBytes());
   }
}
