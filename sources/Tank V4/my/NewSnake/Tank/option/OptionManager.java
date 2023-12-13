package my.NewSnake.Tank.option;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import my.NewSnake.Tank.command.CommandManager;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.option.types.BooleanOption;
import my.NewSnake.Tank.option.types.NumberOption;
import my.NewSnake.Tank.option.types.StringOption;
import my.NewSnake.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

public final class OptionManager {
   private static CopyOnWriteArrayList optionList = new CopyOnWriteArrayList();
   private static final File OPTION_DIR = FileUtils.getConfigFile("Options");

   public static void load() {
      List var0 = FileUtils.read(OPTION_DIR);
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         String var1 = (String)var2.next();

         try {
            String[] var3 = var1.split(":");
            String var4 = var3[0];
            String var5 = var3[1];
            String var6 = var3[2];
            Option var7 = getOption(var4, var3[2]);
            if (var7 != null) {
               if (var7 instanceof NumberOption) {
                  ((NumberOption)var7).setValue(var5);
               } else if (var7 instanceof BooleanOption) {
                  ((BooleanOption)var7).setValueHard(Boolean.parseBoolean(var5));
               } else if (var7 instanceof StringOption) {
                  var7.setValue(var5);
               }
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }

   }

   public static void save() {
      ArrayList var0 = new ArrayList();
      Iterator var2 = optionList.iterator();

      while(var2.hasNext()) {
         Option var1 = (Option)var2.next();
         String var3 = var1.getId();
         String var4 = var1.getValue().toString();
         Module var5 = var1.getModule();
         var0.add(String.format("%s:%s:%s", var3, var4, var5.getId()));
      }

      FileUtils.write(OPTION_DIR, var0, true);
   }

   public static List getOptionList() {
      return optionList;
   }

   public static Option getOption(String var0, String var1) {
      Iterator var3 = optionList.iterator();

      Option var2;
      do {
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var2 = (Option)var3.next();
         } while(!var2.getId().equalsIgnoreCase(var0) && !var2.getDisplayName().equalsIgnoreCase(var0));
      } while(!var2.getModule().getId().equalsIgnoreCase(var1));

      return var2;
   }

   public static void start() {
      try {
         Iterator var1 = ModuleManager.getModules().iterator();

         while(var1.hasNext()) {
            Module var0 = (Module)var1.next();
            var0.preInitialize();
            Field[] var2;
            int var3 = (var2 = var0.getClass().getDeclaredFields()).length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Field var5 = var2[var4];
               var5.setAccessible(true);
               if (var5.isAnnotationPresent(Option.Op.class)) {
                  Option.Op var7;
                  String var8;
                  if (!var5.getType().isAssignableFrom(Float.TYPE) && !var5.getType().isAssignableFrom(Double.TYPE) && !var5.getType().isAssignableFrom(Integer.TYPE) && !var5.getType().isAssignableFrom(Long.TYPE) && !var5.getType().isAssignableFrom(Short.TYPE) && !var5.getType().isAssignableFrom(Byte.TYPE)) {
                     if (var5.getType().isAssignableFrom(Boolean.TYPE)) {
                        boolean var18;
                        try {
                           var18 = var5.getBoolean(var0);
                        } catch (IllegalArgumentException var13) {
                           var18 = false;
                        }

                        var7 = (Option.Op)var5.getAnnotation(Option.Op.class);
                        var8 = var7.name().equalsIgnoreCase("null") ? StringUtils.capitalize(var5.getName()) : StringUtils.capitalize(var7.name());
                        optionList.add(new BooleanOption(var5.getName(), StringUtils.capitalize(var8), var18, var0, false));
                     } else if (var5.getType().isAssignableFrom(String.class)) {
                        String var20;
                        try {
                           var20 = (String)var5.get(var0);
                        } catch (IllegalArgumentException var12) {
                           var20 = "";
                        }

                        var7 = (Option.Op)var5.getAnnotation(Option.Op.class);
                        var8 = var7.name().equalsIgnoreCase("null") ? StringUtils.capitalize(var5.getName()) : StringUtils.capitalize(var7.name());
                        optionList.add(new StringOption(var5.getName(), StringUtils.capitalize(var8), var20, var0));
                     }
                  } else {
                     Object var6;
                     try {
                        var6 = (Number)var5.get(var0);
                     } catch (IllegalArgumentException var11) {
                        var6 = 0;
                     }

                     var7 = (Option.Op)var5.getAnnotation(Option.Op.class);
                     var8 = var7.name().equalsIgnoreCase("null") ? StringUtils.capitalize(var5.getName()) : StringUtils.capitalize(var7.name());
                     NumberOption var9 = new NumberOption(var5.getName(), StringUtils.capitalize(var8), (Number)var6, var0);
                     var9.setMin(var7.min());
                     var9.setMax(var7.max());
                     var9.setIncrement(var7.increment());
                     optionList.add(var9);
                  }
               }
            }

            ArrayList var15 = new ArrayList();
            if (CommandManager.optionCommand.getNames() != null) {
               String[] var16;
               int var17 = (var16 = CommandManager.optionCommand.getNames()).length;

               for(int var21 = 0; var21 < var17; ++var21) {
                  String var19 = var16[var21];
                  var15.add(var19);
               }
            }

            var15.add(var0.getId());
            var15.add(var0.getDisplayName());
            CommandManager.optionCommand.setNames((String[])var15.toArray(new String[0]));
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      }

      load();
      save();
   }
}
