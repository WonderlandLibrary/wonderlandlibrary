package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(
   name = "Policies",
   category = "Core",
   printObject = true
)
public final class CompositeTriggeringPolicy implements TriggeringPolicy {
   private final TriggeringPolicy[] policies;

   private CompositeTriggeringPolicy(TriggeringPolicy... var1) {
      this.policies = var1;
   }

   public void initialize(RollingFileManager var1) {
      TriggeringPolicy[] var2 = this.policies;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TriggeringPolicy var5 = var2[var4];
         var5.initialize(var1);
      }

   }

   public boolean isTriggeringEvent(LogEvent var1) {
      TriggeringPolicy[] var2 = this.policies;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TriggeringPolicy var5 = var2[var4];
         if (var5.isTriggeringEvent(var1)) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("CompositeTriggeringPolicy{");
      boolean var2 = true;
      TriggeringPolicy[] var3 = this.policies;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         TriggeringPolicy var6 = var3[var5];
         if (!var2) {
            var1.append(", ");
         }

         var1.append(var6.toString());
         var2 = false;
      }

      var1.append("}");
      return var1.toString();
   }

   @PluginFactory
   public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") TriggeringPolicy... var0) {
      return new CompositeTriggeringPolicy(var0);
   }
}
