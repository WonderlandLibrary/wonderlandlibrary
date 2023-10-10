package my.NewSnake.Tank.module;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import my.NewSnake.event.EventManager;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.KeyPressEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

public final class ModuleManager {
   private static final File MODULE_DIR = FileUtils.getConfigFile("Mods");
   public static List moduleList = new ArrayList();

   public static Module getModule(String var0) {
      Iterator var2 = moduleList.iterator();

      Module var1;
      do {
         if (!var2.hasNext()) {
            var1 = new Module();
            var1.setProperties("Null", "Null", (Module.Category)null, -1, (String)null, false);
            return var1;
         }

         var1 = (Module)var2.next();
      } while(!var1.getId().equalsIgnoreCase(var0) && !var1.getDisplayName().equalsIgnoreCase(var0));

      return var1;
   }

   public static List getModules(Module.Category var0) {
      ArrayList var1 = new ArrayList();
      Iterator var3 = moduleList.iterator();

      while(var3.hasNext()) {
         Module var2 = (Module)var3.next();
         if (var2.getCategory() == var0) {
            var1.add(var2);
         }
      }

      return var1;
   }

   public static void load() {
      try {
         List var0 = FileUtils.read(MODULE_DIR);
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            String var1 = (String)var2.next();
            String[] var3 = var1.split(":");
            String var4 = var3[0];
            String var5 = var3[1];
            String var6 = var3[2];
            String var7 = var3[3];
            String var8 = var3[4];
            Module var9 = getModule(var4);
            var9.setProperties(var4, var5, var9.getCategory(), var9.getKeybind(), var9.getSuffix(), var9.isShown());
            if (!var9.getId().equalsIgnoreCase("null")) {
               int var10 = var7.equalsIgnoreCase("null") ? -1 : Integer.parseInt(var7);
               boolean var11 = Boolean.parseBoolean(var8);
               boolean var12 = Boolean.parseBoolean(var6);
               if (var11 != var9.isEnabled()) {
                  var9.toggle();
               }

               var9.setShown(var12);
               var9.setDisplayName(var5);
               var9.setKeybind(var10);
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   public static void save() {
      ArrayList var0 = new ArrayList();
      Iterator var2 = moduleList.iterator();

      while(var2.hasNext()) {
         Module var1 = (Module)var2.next();
         String var3 = var1.getId();
         String var4 = var1.getDisplayName();
         String var5 = Boolean.toString(var1.isShown());
         String var6 = var1.getKeybind() <= 0 ? "null" : Integer.toString(var1.getKeybind());
         String var7 = Boolean.toString(var1.isEnabled());
         var0.add(String.format("%s:%s:%s:%s:%s", var3, var4, var5, var6, var7));
      }

      FileUtils.write(MODULE_DIR, var0, true);
   }

   public static List getModulesForRender() {
      List var0 = moduleList;
      var0.sort(new Comparator() {
         public int compare(Object var1, Object var2) {
            return this.compare((Module)var1, (Module)var2);
         }

         public int compare(Module var1, Module var2) {
            String var3 = String.format("%s" + (var1.getSuffix().length() > 0 ? " §7[%s]" : ""), var1.getDisplayName(), var1.getSuffix());
            String var4 = String.format("%s" + (var2.getSuffix().length() > 0 ? " §7[%s]" : ""), var2.getDisplayName(), var2.getSuffix());
            return ClientUtils.clientFont().getStringWidth(var4) - ClientUtils.clientFont().getStringWidth(var3);
         }
      });
      return var0;
   }

   public static List getModules() {
      return moduleList;
   }

   public static ArrayList getModulesInCategory(Module.Category var0) {
      ArrayList var1 = new ArrayList();
      Iterator var3 = moduleList.iterator();

      while(var3.hasNext()) {
         Module var2 = (Module)var3.next();
         if (var2.getCategory() == var0) {
            var1.add(var2);
         }
      }

      return var1;
   }

   public static void start() {
      Reflections var0 = new Reflections("my.NewSnake.Tank.module.modules", new Scanner[0]);
      Set var1 = var0.getSubTypesOf(Module.class);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Class var2 = (Class)var3.next();

         try {
            Module var4 = (Module)var2.newInstance();
            if (var2.isAnnotationPresent(Module.Mod.class)) {
               Module.Mod var5 = (Module.Mod)var2.getAnnotation(Module.Mod.class);
               var4.setProperties(var2.getSimpleName(), var5.displayName().equals("null") ? var2.getSimpleName() : var5.displayName(), Module.Category.valueOf(StringUtils.capitalize(var2.getPackage().getName().split("modules.")[1])), var5.keybind(), var5.suffix(), var5.shown());
               if (var5.enabled()) {
                  var4.enable();
               }

               moduleList.add(var4);
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      moduleList.sort(new Comparator() {
         public int compare(Object var1, Object var2) {
            return this.compare((Module)var1, (Module)var2);
         }

         public int compare(Module var1, Module var2) {
            String var3 = var1.getDisplayName();
            String var4 = var2.getDisplayName();
            return var3.compareTo(var4);
         }
      });
      load();
      save();
      EventManager.register(new ModuleManager());
   }

   @EventTarget
   private void onKeyBoard(KeyPressEvent var1) {
      Iterator var3 = moduleList.iterator();

      while(var3.hasNext()) {
         Module var2 = (Module)var3.next();
         if (var1.getKey() == var2.getKeybind()) {
            var2.toggle();
            save();
         }
      }

   }
}
