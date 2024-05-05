package team.massacre.api.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import team.massacre.Massacre;
import team.massacre.api.module.Module;
import team.massacre.api.ui.csgo.SkeetUI;

public final class Config implements Serializable {
   private final String name;
   private final File file;

   public Config(String name) {
      this.name = name;
      this.file = new File(ConfigManager.CONFIGS_DIR, name + ".json");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException var3) {
         }
      }

   }

   public File getFile() {
      return this.file;
   }

   public String getName() {
      return this.name;
   }

   public JsonObject save() {
      JsonObject jsonObject = new JsonObject();
      JsonObject modulesObject = new JsonObject();
      Iterator var3 = Massacre.INSTANCE.getModuleManager().getModules().iterator();

      while(var3.hasNext()) {
         Module module = (Module)var3.next();
         modulesObject.add(module.getName(), module.save());
      }

      jsonObject.add("gui_color", new JsonPrimitive(Integer.toHexString(SkeetUI.getColor())));
      jsonObject.add("modules", modulesObject);
      return jsonObject;
   }

   public void load(JsonObject object) {
      if (object.has("modules")) {
         JsonObject modulesObject = object.getAsJsonObject("modules");
         Iterator var3 = Massacre.INSTANCE.getModuleManager().getModules().iterator();

         while(var3.hasNext()) {
            Module module = (Module)var3.next();
            if (modulesObject.has(module.getName())) {
               module.load(modulesObject.getAsJsonObject(module.getName()));
            }
         }
      }

      if (object.has("gui_color")) {
         JsonPrimitive guiColor = object.getAsJsonPrimitive("gui_color");
         SkeetUI.setColor((int)Long.parseLong(guiColor.getAsString(), 16));
      }

   }
}
