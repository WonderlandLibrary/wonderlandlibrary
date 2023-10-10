package shadersmod.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optifine.Config;
import optifine.Lang;
import optifine.StrUtils;

public class ShaderOptionSwitch extends ShaderOption {
   private static final Pattern PATTERN_DEFINE = Pattern.compile("^\\s*(//)?\\s*#define\\s+([A-Za-z0-9_]+)\\s*(//.*)?$");
   private static final Pattern PATTERN_IFDEF = Pattern.compile("^\\s*#if(n)?def\\s+([A-Za-z0-9_]+)(\\s*)?$");

   public ShaderOptionSwitch(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, new String[]{"true", "false"}, var3, var4);
   }

   public String getSourceLine() {
      return isTrue(this.getValue()) ? "#define " + this.getName() + " // Shader option ON" : "//#define " + this.getName() + " // Shader option OFF";
   }

   public String getValueText(String var1) {
      return isTrue(var1) ? Lang.getOn() : Lang.getOff();
   }

   public String getValueColor(String var1) {
      return isTrue(var1) ? "§a" : "§c";
   }

   public static ShaderOption parseOption(String var0, String var1) {
      Matcher var2 = PATTERN_DEFINE.matcher(var0);
      if (!var2.matches()) {
         return null;
      } else {
         String var3 = var2.group(1);
         String var4 = var2.group(2);
         String var5 = var2.group(3);
         if (var4 != null && var4.length() > 0) {
            boolean var6 = Config.equals(var3, "//");
            boolean var7 = !var6;
            var1 = StrUtils.removePrefix(var1, "/shaders/");
            ShaderOptionSwitch var8 = new ShaderOptionSwitch(var4, var5, String.valueOf(var7), var1);
            return var8;
         } else {
            return null;
         }
      }
   }

   public boolean matchesLine(String var1) {
      Matcher var2 = PATTERN_DEFINE.matcher(var1);
      if (!var2.matches()) {
         return false;
      } else {
         String var3 = var2.group(2);
         return var3.matches(this.getName());
      }
   }

   public boolean checkUsed() {
      return true;
   }

   public boolean isUsedInLine(String var1) {
      Matcher var2 = PATTERN_IFDEF.matcher(var1);
      if (var2.matches()) {
         String var3 = var2.group(2);
         if (var3.equals(this.getName())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isTrue(String var0) {
      return Boolean.valueOf(var0);
   }
}
