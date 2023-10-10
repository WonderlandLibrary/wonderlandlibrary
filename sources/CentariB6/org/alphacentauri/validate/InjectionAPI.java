package org.alphacentauri.validate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import tk.alphacentauri.launcher.Main;

public class InjectionAPI {
   private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

   private static Iterator list(ClassLoader CL) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      Class CL_class;
      for(CL_class = CL.getClass(); CL_class != ClassLoader.class; CL_class = CL_class.getSuperclass()) {
         ;
      }

      Field ClassLoader_classes_field = CL_class.getDeclaredField("classes");
      ClassLoader_classes_field.setAccessible(true);
      Vector classes = (Vector)ClassLoader_classes_field.get(CL);
      return classes.iterator();
   }

   private static void checkCMDArgs() {
      RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();

      for(String argument : runtimeMxBean.getInputArguments()) {
         if(argument.toLowerCase().contains("javaagent")) {
            Main.b.send(999 + (new Random()).nextInt(13337), new byte[0]);
            System.out.println("Try harder c: : " + argument);
            System.exit(-1);
            return;
         }
      }

   }

   public static String bytesToHex(byte[] bytes) {
      char[] hexChars = new char[bytes.length * 2];

      for(int j = 0; j < bytes.length; ++j) {
         int v = bytes[j] & 255;
         hexChars[j * 2] = hexArray[v >>> 4];
         hexChars[j * 2 + 1] = hexArray[v & 15];
      }

      return new String(hexChars);
   }

   public static void inject() {
      CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
      if(codeSource != null && codeSource.getLocation() != null) {
         checkClasses();
         checkCMDArgs();

         try {
            File jar = new File(URLDecoder.decode(codeSource.getLocation().getPath(), "UTF-8"));
            FileInputStream fin = new FileInputStream(jar);
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            for(int c = fin.read(buffer); c != -1; c = fin.read(buffer)) {
               bout.write(buffer, 0, c);
            }

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String digest = bytesToHex(md5.digest(bout.toByteArray()));
            String expected = "0909F242D8A9692A487AA1116AF62CBF";
            String saltStr = "cqxtkrvignmyyxmvupprkgofjingzhhvqdvvfvluzzmhzqjxnjwjycwebkkwlxvm";
            if(digest.equalsIgnoreCase(expected)) {
               byte[] digestBytes = digest.getBytes(Charset.forName("UTF-8"));
               byte[] salt = saltStr.getBytes(Charset.forName("UTF-8"));
               byte[] salty = new byte[digestBytes.length + salt.length];
               int ind = 0;

               for(int i = 0; i < digestBytes.length; ++i) {
                  salty[ind++] = digestBytes[i];
               }

               for(int i = 0; i < salt.length; ++i) {
                  salty[ind++] = salt[i];
               }

               byte[] proof = md5.digest(salty);
               Main.b.send(137, proof);
            } else {
               System.out.println("Uhm, nope.");
               Main.b.send(999 + (new Random()).nextInt(13337), new byte[0]);
               System.exit(-1);
            }
         } catch (Exception var14) {
            System.out.println("Fuck!");
            System.exit(-1);
         }
      } else {
         Main.b.send(999 + (new Random()).nextInt(13337), new byte[0]);
         System.out.println("Debugging? Not with me faggot.");
         System.exit(-1);
      }

   }

   private static void checkClasses() {
      int i = 0;

      for(ClassLoader classLoader : Main.e.a) {
         try {
            for(Iterator classes = list(classLoader); classes.hasNext(); ++i) {
               Object next = classes.next();
            }
         } catch (IllegalAccessException | NoSuchFieldException var5) {
            var5.printStackTrace();
         }
      }

      if(i != 12) {
         Main.b.send(999 + (new Random()).nextInt(13337), new byte[0]);
         System.out.println("Hmmm... you have " + (i - 12) + " class(es) more then me ;)");
         System.exit(-1);
      }

   }
}
