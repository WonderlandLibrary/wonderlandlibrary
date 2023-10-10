package shadersmod.client;

import java.util.Arrays;
import java.util.List;
import net.minecraft.src.Config;
import net.minecraft.src.StrUtils;
import shadersmod.client.Shaders;

public abstract class ShaderOption {
   private String name = null;
   private String description = null;
   private String value = null;
   private String[] values = null;
   private String valueDefault = null;
   private String[] paths = null;
   private boolean enabled = true;
   private boolean visible = true;
   public static final String COLOR_GREEN = "§a";
   public static final String COLOR_RED = "§c";
   public static final String COLOR_BLUE = "§9";

   public ShaderOption(String name, String description, String value, String[] values, String valueDefault, String path) {
      this.name = name;
      this.description = description;
      this.value = value;
      this.values = values;
      this.valueDefault = valueDefault;
      if(path != null) {
         this.paths = new String[]{path};
      }

   }

   public String toString() {
      return "" + this.name + ", value: " + this.value + ", valueDefault: " + this.valueDefault + ", paths: " + Config.arrayToString((Object[])this.paths);
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public boolean setValue(String value) {
      int i = getIndex(value, this.values);
      if(i < 0) {
         return false;
      } else {
         this.value = value;
         return true;
      }
   }

   private static int getIndex(String str, String[] strs) {
      for(int i = 0; i < strs.length; ++i) {
         String s = strs[i];
         if(s.equals(str)) {
            return i;
         }
      }

      return -1;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public String getDescription() {
      return this.description;
   }

   public String[] getValues() {
      return (String[])((String[])this.values.clone());
   }

   public String getDescriptionText() {
      String s = Config.normalize(this.description);
      s = StrUtils.removePrefix(s, "//");
      s = Shaders.translate("option." + this.getName() + ".comment", s);
      return s;
   }

   public String getValueDefault() {
      return this.valueDefault;
   }

   public String getSourceLine() {
      return null;
   }

   public boolean matchesLine(String line) {
      return false;
   }

   public void resetValue() {
      this.value = this.valueDefault;
   }

   public boolean isChanged() {
      return !Config.equals(this.value, this.valueDefault);
   }

   public String getNameText() {
      return Shaders.translate("option." + this.name, this.name);
   }

   public String getValueText(String val) {
      return val;
   }

   public boolean checkUsed() {
      return false;
   }

   public void addPaths(String[] newPaths) {
      List<String> list = Arrays.asList(this.paths);

      for(int i = 0; i < newPaths.length; ++i) {
         String s = newPaths[i];
         if(!list.contains(s)) {
            this.paths = (String[])((String[])((String[])Config.addObjectToArray(this.paths, s)));
         }
      }

   }

   public boolean isUsedInLine(String line) {
      return false;
   }

   public String[] getPaths() {
      return this.paths;
   }

   public String getValueColor(String val) {
      return "";
   }

   public boolean isValidValue(String val) {
      return getIndex(val, this.values) >= 0;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void nextValue() {
      int i = getIndex(this.value, this.values);
      if(i >= 0) {
         i = (i + 1) % this.values.length;
         this.value = this.values[i];
      }

   }
}
