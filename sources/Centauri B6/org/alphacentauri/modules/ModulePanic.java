package org.alphacentauri.modules;

import java.util.Collection;
import java.util.function.Consumer;
import org.alphacentauri.AC;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModulePanic extends Module {
   private Property moduleList = new Property(this, "EnabledModules", "", false);

   public ModulePanic() {
      super("Panic", "Only use in case of emergency", new String[]{"panic"}, Module.Category.Misc, 14963936);
   }

   public void setEnabledSilent(boolean enabled) {
      if(enabled) {
         StringBuilder sb = new StringBuilder();

         for(Module module : AC.getModuleManager().allEnabled()) {
            sb.append(module.getName()).append(';');
         }

         if(sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            this.moduleList.value = sb.toString();
            AC.getModuleManager().allEnabled().forEach(Module::disable);
         }
      } else if(((String)this.moduleList.value).length() > 0) {
         Collection<Module> modules = AC.getModuleManager().all();

         for(String s : ((String)this.moduleList.value).split(";")) {
            for(Module module : modules) {
               if(module.getName().equals(s)) {
                  try {
                     module.enable();
                  } catch (Exception var10) {
                     var10.printStackTrace();
                  }
                  break;
               }
            }
         }
      }

      super.setEnabledSilent(enabled);
   }
}
