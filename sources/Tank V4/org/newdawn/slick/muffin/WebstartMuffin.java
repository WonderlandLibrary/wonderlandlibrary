package org.newdawn.slick.muffin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import org.newdawn.slick.util.Log;

public class WebstartMuffin implements Muffin {
   public void saveFile(HashMap var1, String var2) throws IOException {
      PersistenceService var3;
      URL var5;
      try {
         var3 = (PersistenceService)ServiceManager.lookup("javax.jnlp.PersistenceService");
         BasicService var4 = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
         URL var6 = var4.getCodeBase();
         var5 = new URL(var6, var2);
      } catch (Exception var15) {
         Log.error((Throwable)var15);
         throw new IOException("Failed to save state: ");
      }

      try {
         var3.delete(var5);
      } catch (Exception var14) {
         Log.info("No exisiting Muffin Found - First Save");
      }

      try {
         var3.create(var5, 1024L);
         FileContents var17 = var3.get(var5);
         DataOutputStream var7 = new DataOutputStream(var17.getOutputStream(false));
         Set var8 = var1.keySet();
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            String var10 = (String)var9.next();
            var7.writeUTF(var10);
            if (var2.endsWith("Number")) {
               double var11 = (Double)var1.get(var10);
               var7.writeDouble(var11);
            } else {
               String var16 = (String)var1.get(var10);
               var7.writeUTF(var16);
            }
         }

         var7.flush();
         var7.close();
      } catch (Exception var13) {
         Log.error((Throwable)var13);
         throw new IOException("Failed to store map of state data");
      }
   }

   public HashMap loadFile(String var1) throws IOException {
      HashMap var2 = new HashMap();

      try {
         PersistenceService var3 = (PersistenceService)ServiceManager.lookup("javax.jnlp.PersistenceService");
         BasicService var4 = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
         URL var5 = var4.getCodeBase();
         URL var6 = new URL(var5, var1);
         FileContents var7 = var3.get(var6);
         DataInputStream var8 = new DataInputStream(var7.getInputStream());
         String var9;
         if (var1.endsWith("Number")) {
            while((var9 = var8.readUTF()) != null) {
               double var15 = var8.readDouble();
               var2.put(var9, new Double(var15));
            }
         } else {
            while((var9 = var8.readUTF()) != null) {
               String var10 = var8.readUTF();
               var2.put(var9, var10);
            }
         }

         var8.close();
      } catch (EOFException var12) {
      } catch (IOException var13) {
      } catch (Exception var14) {
         Log.error((Throwable)var14);
         throw new IOException("Failed to load state from webstart muffin");
      }

      return var2;
   }
}
