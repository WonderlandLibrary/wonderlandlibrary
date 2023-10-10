package org.newdawn.slick.muffin;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.newdawn.slick.util.Log;

public class FileMuffin implements Muffin {
   public void saveFile(HashMap var1, String var2) throws IOException {
      String var3 = System.getProperty("user.home");
      File var4 = new File(var3);
      var4 = new File(var4, ".java");
      if (!var4.exists()) {
         var4.mkdir();
      }

      var4 = new File(var4, var2);
      FileOutputStream var5 = new FileOutputStream(var4);
      ObjectOutputStream var6 = new ObjectOutputStream(var5);
      var6.writeObject(var1);
      var6.close();
   }

   public HashMap loadFile(String var1) throws IOException {
      HashMap var2 = new HashMap();
      String var3 = System.getProperty("user.home");
      File var4 = new File(var3);
      var4 = new File(var4, ".java");
      var4 = new File(var4, var1);
      if (var4.exists()) {
         try {
            FileInputStream var5 = new FileInputStream(var4);
            ObjectInputStream var6 = new ObjectInputStream(var5);
            var2 = (HashMap)var6.readObject();
            var6.close();
         } catch (EOFException var7) {
         } catch (ClassNotFoundException var8) {
            Log.error((Throwable)var8);
            throw new IOException("Failed to pull state from store - class not found");
         }
      }

      return var2;
   }
}
