package shadersmod.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShaderProfile {
   private String name = null;
   private Map mapOptionValues = new HashMap();
   private Set disabledPrograms = new HashSet();

   public ShaderProfile(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void addOptionValue(String var1, String var2) {
      this.mapOptionValues.put(var1, var2);
   }

   public void addOptionValues(ShaderProfile var1) {
      if (var1 != null) {
         this.mapOptionValues.putAll(var1.mapOptionValues);
      }

   }

   public void applyOptionValues(ShaderOption[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         ShaderOption var3 = var1[var2];
         String var4 = var3.getName();
         String var5 = (String)this.mapOptionValues.get(var4);
         if (var5 != null) {
            var3.setValue(var5);
         }
      }

   }

   public String[] getOptions() {
      Set var1 = this.mapOptionValues.keySet();
      String[] var2 = (String[])var1.toArray(new String[var1.size()]);
      return var2;
   }

   public String getValue(String var1) {
      return (String)this.mapOptionValues.get(var1);
   }

   public void addDisabledProgram(String var1) {
      this.disabledPrograms.add(var1);
   }

   public Collection getDisabledPrograms() {
      return new HashSet(this.disabledPrograms);
   }

   public void addDisabledPrograms(Collection var1) {
      this.disabledPrograms.addAll(var1);
   }

   public boolean isProgramDisabled(String var1) {
      return this.disabledPrograms.contains(var1);
   }
}
