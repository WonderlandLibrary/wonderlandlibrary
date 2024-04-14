package de.violence.save;

import de.violence.save.valueString;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;

public class EasySaveGame {
   public static String version = "v0.4";
   String fileName;
   public valueString valueString;

   private void handleException(Exception ex) {
      System.out.println("Critical Error: Exception in EasySaveGame detected! by fetching this String: " + this.getContent().getString());
      ex.printStackTrace();
   }

   public EasySaveGame(String _fileName) {
      this.fileName = _fileName;

      try {
         this.loadValueString();
      } catch (Exception var3) {
         this.handleException(var3);
      }

   }

   public void save() {
      byte[] bytesEncoded = this.valueString.string.getBytes();
      String s = new String(bytesEncoded);

      try {
         FileWriter fw = new FileWriter(new File(Minecraft.getMinecraft().mcDataDir + "/violence/" + this.fileName));
         BufferedWriter e = new BufferedWriter(fw);
         e.write(s);
         e.close();
      } catch (IOException var5) {
         this.handleException(var5);
      }

   }

   public void setBoolean(String name, Boolean val) {
      try {
         this.valueString.setValue(name, val.booleanValue());
      } catch (Exception var4) {
         this.handleException(var4);
      }

   }

   public void setDouble(String name, Double val) {
      try {
         this.valueString.setValue(name, val.doubleValue());
      } catch (Exception var4) {
         this.handleException(var4);
      }

   }

   public void setString(String name, String val) {
      try {
         if(val.toLowerCase().contains(";") || val.toLowerCase().contains("%")) {
            throw new Exception("Config String can not contain \';\' or \'%\'.");
         }

         this.valueString.setValue(name, val);
      } catch (Exception var4) {
         this.handleException(var4);
      }

   }

   public void setInteger(String name, Integer val) {
      try {
         this.valueString.setValue(name, val.intValue());
      } catch (Exception var4) {
         this.handleException(var4);
      }

   }

   public void setArrayList(String name, ArrayList val) {
      try {
         Iterator var4 = val.iterator();

         String e;
         do {
            if(!var4.hasNext()) {
               this.valueString.setValue(name, val);
               return;
            }

            e = (String)var4.next();
         } while(!e.contains(";") && !e.contains("%") && !e.contains(":"));

         throw new Exception("Config ArrayList<String> can not contain \';\'  \'%\' or \':\'.");
      } catch (Exception var5) {
         this.handleException(var5);
      }
   }

   public Boolean getBoolean(String name) {
      try {
         return this.valueString.getBooleanValue(name);
      } catch (Exception var3) {
         this.handleException(var3);
         return null;
      }
   }

   public Double getDouble(String name) {
      try {
         return this.valueString.getDoubleValue(name);
      } catch (Exception var3) {
         this.handleException(var3);
         return null;
      }
   }

   public String getString(String name) {
      try {
         return this.valueString.getStringValue(name);
      } catch (Exception var3) {
         this.handleException(var3);
         return null;
      }
   }

   public Integer getInteger(String name) {
      try {
         return this.valueString.getIntegerValue(name);
      } catch (Exception var3) {
         this.handleException(var3);
         return null;
      }
   }

   public ArrayList getArrayList(String name) {
      try {
         return this.valueString.getArrayListValue(name);
      } catch (Exception var3) {
         this.handleException(var3);
         return null;
      }
   }

   public Boolean containsValue(String name) {
      try {
         if(this.valueString.containsValue(name)) {
            return Boolean.valueOf(true);
         }
      } catch (Exception var3) {
         this.handleException(var3);
         return null;
      }

      return Boolean.valueOf(false);
   }

   public valueString getContent() {
      return this.valueString;
   }

   public void removeValue(String name) {
      try {
         this.valueString.removeValue(name);
      } catch (Exception var3) {
         this.handleException(var3);
      }

   }

   public void reset() {
      this.valueString.values.clear();
      this.valueString.string = "";
   }

   public void setValueString(String s) {
      this.valueString.setValueString(s);
   }

   public String getValueString() {
      return this.valueString.getString();
   }

   private void loadValueString() throws Exception {
      try {
         FileReader fr = new FileReader(new File(Minecraft.getMinecraft().mcDataDir + "/violence/" + this.fileName));
         BufferedReader e = new BufferedReader(fr);
         String valueDecoded = e.readLine();
         if(valueDecoded == null) {
            this.valueString = new valueString();
            e.close();
            return;
         }

         String string = new String(valueDecoded);
         this.valueString = new valueString(string);
         e.close();
      } catch (FileNotFoundException var5) {
         this.generateFile();
      }

   }

   private void generateFile() throws Exception {
      (new File(Minecraft.getMinecraft().mcDataDir + "/violence/" + this.fileName)).getParentFile().mkdirs();
      FileWriter fw = new FileWriter(new File(Minecraft.getMinecraft().mcDataDir + "/violence/" + this.fileName));
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write("");
      bw.close();
      this.valueString = new valueString();
   }
}
