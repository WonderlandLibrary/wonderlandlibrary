package shadersmod.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optifine.StrUtils;

public class ShaderOptionVariableConst extends ShaderOptionVariable {
   private String type = null;
   private static final Pattern PATTERN_CONST = Pattern.compile("^\\s*const\\s*(float|int)\\s*([A-Za-z0-9_]+)\\s*=\\s*(-?[0-9\\.]+f?F?)\\s*;\\s*(//.*)?$");

   public ShaderOptionVariableConst(String var1, String var2, String var3, String var4, String[] var5, String var6) {
      super(var1, var3, var4, var5, var6);
      this.type = var2;
   }

   public String getSourceLine() {
      return "const " + this.type + " " + this.getName() + " = " + this.getValue() + "; // Shader option " + this.getValue();
   }

   public boolean matchesLine(String var1) {
      Matcher var2 = PATTERN_CONST.matcher(var1);
      if (!var2.matches()) {
         return false;
      } else {
         String var3 = var2.group(2);
         return var3.matches(this.getName());
      }
   }

   public static ShaderOption parseOption(String var0, String var1) {
      Matcher var2 = PATTERN_CONST.matcher(var0);
      if (!var2.matches()) {
         return null;
      } else {
         String var3 = var2.group(1);
         String var4 = var2.group(2);
         String var5 = var2.group(3);
         String var6 = var2.group(4);
         String var7 = StrUtils.getSegment(var6, "[", "]");
         if (var7 != null && var7.length() > 0) {
            var6 = var6.replace(var7, "").trim();
         }

         String[] var8 = parseValues(var5, var7);
         if (var4 != null && var4.length() > 0) {
            var1 = StrUtils.removePrefix(var1, "/shaders/");
            ShaderOptionVariableConst var9 = new ShaderOptionVariableConst(var4, var3, var6, var5, var8, var1);
            return var9;
         } else {
            return null;
         }
      }
   }
}
