package shadersmod.client;

public class ShaderUtils {
   public static ShaderOption getShaderOption(String var0, ShaderOption[] var1) {
      if (var1 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            ShaderOption var3 = var1[var2];
            if (var3.getName().equals(var0)) {
               return var3;
            }
         }

         return null;
      }
   }

   public static ShaderProfile detectProfile(ShaderProfile[] var0, ShaderOption[] var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            ShaderProfile var4 = var0[var3];
            if (var2 == null) {
               return var4;
            }
         }

         return null;
      }
   }
}
