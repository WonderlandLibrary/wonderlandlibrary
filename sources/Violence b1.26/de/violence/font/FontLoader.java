package de.violence.font;

import de.violence.font.SlickFont;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class FontLoader {
   public static File fontsFile = new File("Violence/fonts");
   public static List instances = new ArrayList();

   public static SlickFont getFont(String fontName, int scale, boolean bold) {
      File font = new File(fontsFile, fontName);
      if(!font.exists()) {
         download(font, "http://skidclient.de/client/Fonts/" + fontName);
      }

      Iterator var5 = instances.iterator();

      SlickFont c;
      do {
         if(!var5.hasNext()) {
            c = new SlickFont(font.getAbsolutePath(), scale, bold);
            instances.add(c);
            return c;
         }

         c = (SlickFont)var5.next();
      } while(c.scale != scale || !c.name.equalsIgnoreCase(font.getAbsolutePath()));

      return c;
   }

   private static void download(File input, String url) {
      input.getParentFile().mkdirs();

      try {
         FileUtils.copyURLToFile(new URL(url), input);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private static void download(File input, String url, boolean failed) {
      if(!input.exists()) {
         try {
            FileUtils.copyURLToFile(new URL(url), input);
            failed = false;
         } catch (Throwable var4) {
            failed = true;
         }

      }
   }
}
