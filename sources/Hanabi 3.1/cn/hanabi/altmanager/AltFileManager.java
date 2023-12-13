package cn.hanabi.altmanager;

import cn.hanabi.Hanabi;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;

public class AltFileManager {
   private static final File directory;
   public static ArrayList Files;

   public AltFileManager() {
      this.makeDirectories();
      Files.add(new Alts("alts", false, true));
   }

   public void loadFiles() {
      Iterator var1 = Files.iterator();

      while(var1.hasNext()) {
         CustomFile f = (CustomFile)var1.next();

         try {
            if (f.loadOnStart()) {
               f.loadFile();
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public void saveFiles() {
      Iterator var1 = Files.iterator();

      while(var1.hasNext()) {
         CustomFile f = (CustomFile)var1.next();

         try {
            f.saveFile();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public CustomFile getFile(Class clazz) {
      Iterator var2 = Files.iterator();

      CustomFile file;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         file = (CustomFile)var2.next();
      } while(file.getClass() != clazz);

      return file;
   }

   public void makeDirectories() {
      try {
         if (!directory.exists()) {
            if (directory.mkdir()) {
               Hanabi.INSTANCE.println("Directory is created!");
            } else {
               Hanabi.INSTANCE.println("Failed to create directory!");
            }
         }

      } catch (Exception var2) {
         throw new RuntimeException();
      }
   }

   static {
      directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + "Hanabi");
      Files = new ArrayList();
   }

   public abstract static class CustomFile {
      private final File file;
      private final String name;
      private final boolean load;

      public CustomFile(String name, boolean Module2, boolean loadOnStart) {
         this.name = name;
         this.load = loadOnStart;
         this.file = new File(AltFileManager.directory, name + ".txt");
         if (!this.file.exists()) {
            try {
               this.saveFile();
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }

      }

      public final File getFile() {
         return this.file;
      }

      private boolean loadOnStart() {
         return this.load;
      }

      public final String getName() {
         return this.name;
      }

      public abstract void loadFile() throws IOException;

      public abstract void saveFile() throws IOException;
   }
}
