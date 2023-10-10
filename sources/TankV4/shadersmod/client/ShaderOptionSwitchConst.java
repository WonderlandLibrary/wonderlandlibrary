package shadersmod.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optifine.StrUtils;

public class ShaderOptionSwitchConst extends ShaderOptionSwitch {
   private static final Pattern PATTERN_CONST = Pattern.compile("^\\s*const\\s*bool\\s*([A-Za-z0-9_]+)\\s*=\\s*(true|false)\\s*;\\s*(//.*)?$");

   public ShaderOptionSwitchConst(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public String getSourceLine() {
      return "const bool " + this.getName() + " = " + this.getValue() + "; // Shader option " + this.getValue();
   }

   public static ShaderOption parseOption(String var0, String var1) {
      Matcher var2 = PATTERN_CONST.matcher(var0);
      if (!var2.matches()) {
         return null;
      } else {
         String var3 = var2.group(1);
         String var4 = var2.group(2);
         String var5 = var2.group(3);
         if (var3 != null && var3.length() > 0) {
            var1 = StrUtils.removePrefix(var1, "/shaders/");
            ShaderOptionSwitchConst var6 = new ShaderOptionSwitchConst(var3, var5, var4, var1);
            var6.setVisible(false);
            return var6;
         } else {
            return null;
         }
      }
   }

   public boolean matchesLine(String var1) {
      Matcher var2 = PATTERN_CONST.matcher(var1);
      if (!var2.matches()) {
         return false;
      } else {
         String var3 = var2.group(1);
         return var3.matches(this.getName());
      }
   }

   public boolean checkUsed() {
      return false;
   }
}
