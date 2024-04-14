package de.violence.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public class ConfigList {
   public static List getConfigs() throws Exception {
      ArrayList configList = new ArrayList();
      File toCheck = new File(Minecraft.getMinecraft().mcDataDir + "/violence/Configs/");
      if(!toCheck.exists()) {
         return configList;
      } else {
         File[] var5;
         int var4 = (var5 = toCheck.listFiles()).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            File file = var5[var3];
            String fileName = file.getName();
            if(!fileName.contains("Configs")) {
               configList.add(fileName.replace(".txt", ""));
            }
         }

         return configList;
      }
   }
}
