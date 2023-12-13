package shadersmod.client;

import optifine.Config;

public class PropertyDefaultFastFancyOff extends Property {
   public static final String[] PROPERTY_VALUES = new String[]{"default", "fast", "fancy", "off"};
   public static final String[] USER_VALUES = new String[]{"Default", "Fast", "Fancy", "OFF"};

   public PropertyDefaultFastFancyOff(String var1, String var2, int var3) {
      super(var1, PROPERTY_VALUES, var2, USER_VALUES, var3);
   }

   public boolean isDefault() {
      return this.getValue() == 0;
   }

   public boolean isFast() {
      return this.getValue() == 1;
   }

   public boolean isFancy() {
      return this.getValue() == 2;
   }

   public boolean isOff() {
      return this.getValue() == 3;
   }

   public boolean setPropertyValue(String var1) {
      if (Config.equals(var1, "none")) {
         var1 = "off";
      }

      return super.setPropertyValue(var1);
   }
}
