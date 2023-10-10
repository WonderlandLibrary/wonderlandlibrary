package org.newdawn.slick;

import java.io.IOException;
import java.util.HashMap;
import javax.jnlp.ServiceManager;
import org.newdawn.slick.muffin.FileMuffin;
import org.newdawn.slick.muffin.Muffin;
import org.newdawn.slick.muffin.WebstartMuffin;
import org.newdawn.slick.util.Log;

public class SavedState {
   private String fileName;
   private Muffin muffin;
   private HashMap numericData = new HashMap();
   private HashMap stringData = new HashMap();

   public SavedState(String var1) throws SlickException {
      this.fileName = var1;
      if (this.isWebstartAvailable()) {
         this.muffin = new WebstartMuffin();
      } else {
         this.muffin = new FileMuffin();
      }

      try {
         this.load();
      } catch (IOException var3) {
         throw new SlickException("Failed to load state on startup", var3);
      }
   }

   public double getNumber(String var1) {
      return this.getNumber(var1, 0.0D);
   }

   public double getNumber(String var1, double var2) {
      Double var4 = (Double)this.numericData.get(var1);
      return var4 == null ? var2 : var4;
   }

   public void setNumber(String var1, double var2) {
      this.numericData.put(var1, new Double(var2));
   }

   public String getString(String var1) {
      return this.getString(var1, (String)null);
   }

   public String getString(String var1, String var2) {
      String var3 = (String)this.stringData.get(var1);
      return var3 == null ? var2 : var3;
   }

   public void setString(String var1, String var2) {
      this.stringData.put(var1, var2);
   }

   public void save() throws IOException {
      this.muffin.saveFile(this.numericData, this.fileName + "_Number");
      this.muffin.saveFile(this.stringData, this.fileName + "_String");
   }

   public void load() throws IOException {
      this.numericData = this.muffin.loadFile(this.fileName + "_Number");
      this.stringData = this.muffin.loadFile(this.fileName + "_String");
   }

   public void clear() {
      this.numericData.clear();
      this.stringData.clear();
   }

   private boolean isWebstartAvailable() {
      try {
         Class.forName("javax.jnlp.ServiceManager");
         ServiceManager.lookup("javax.jnlp.PersistenceService");
         Log.info("Webstart detected using Muffins");
         return true;
      } catch (Exception var2) {
         Log.info("Using Local File System");
         return false;
      }
   }
}
