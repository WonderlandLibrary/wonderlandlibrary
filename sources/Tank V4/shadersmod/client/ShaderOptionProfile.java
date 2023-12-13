package shadersmod.client;

import java.util.ArrayList;
import optifine.Lang;

public class ShaderOptionProfile extends ShaderOption {
   private ShaderProfile[] profiles = null;
   private ShaderOption[] options = null;
   private static final String NAME_PROFILE = "<profile>";
   private static final String VALUE_CUSTOM = "<custom>";

   public ShaderOptionProfile(ShaderProfile[] var1, ShaderOption[] var2) {
      super("<profile>", "", detectProfileName(var1, var2), getProfileNames(var1), detectProfileName(var1, var2, true), (String)null);
      this.profiles = var1;
      this.options = var2;
   }

   public void nextValue() {
      super.nextValue();
      if (this.getValue().equals("<custom>")) {
         super.nextValue();
      }

      this.applyProfileOptions();
   }

   public void updateProfile() {
      ShaderProfile var1 = this.getProfile(this.getValue());
      if (var1 == null || !ShaderUtils.matchProfile(var1, this.options, false)) {
         String var2 = detectProfileName(this.profiles, this.options);
         this.setValue(var2);
      }

   }

   private void applyProfileOptions() {
      ShaderProfile var1 = this.getProfile(this.getValue());
      if (var1 != null) {
         String[] var2 = var1.getOptions();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3];
            ShaderOption var5 = this.getOption(var4);
            if (var5 != null) {
               String var6 = var1.getValue(var4);
               var5.setValue(var6);
            }
         }
      }

   }

   private ShaderOption getOption(String var1) {
      for(int var2 = 0; var2 < this.options.length; ++var2) {
         ShaderOption var3 = this.options[var2];
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private ShaderProfile getProfile(String var1) {
      for(int var2 = 0; var2 < this.profiles.length; ++var2) {
         ShaderProfile var3 = this.profiles[var2];
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public String getNameText() {
      return Lang.get("of.shaders.profile");
   }

   public String getValueText(String var1) {
      return var1.equals("<custom>") ? Lang.get("of.general.custom", "<custom>") : Shaders.translate("profile." + var1, var1);
   }

   public String getValueColor(String var1) {
      return var1.equals("<custom>") ? "§c" : "§a";
   }

   private static String detectProfileName(ShaderProfile[] var0, ShaderOption[] var1) {
      return detectProfileName(var0, var1, false);
   }

   private static String detectProfileName(ShaderProfile[] var0, ShaderOption[] var1, boolean var2) {
      ShaderProfile var3 = ShaderUtils.detectProfile(var0, var1, var2);
      return var3 == null ? "<custom>" : var3.getName();
   }

   private static String[] getProfileNames(ShaderProfile[] var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         ShaderProfile var3 = var0[var2];
         var1.add(var3.getName());
      }

      var1.add("<custom>");
      String[] var4 = (String[])var1.toArray(new String[var1.size()]);
      return var4;
   }
}
