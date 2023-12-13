package my.NewSnake.Tank.module.modules.COMBAT.aura;

import java.util.Iterator;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.Tank.option.OptionManager;
import my.NewSnake.Tank.option.types.BooleanOption;
import my.NewSnake.event.events.UpdateEvent;

public class AuraMode extends BooleanOption {
   public void setValue(Boolean var1) {
      Option var2;
      Iterator var3;
      if (var1) {
         var3 = OptionManager.getOptionList().iterator();

         while(var3.hasNext()) {
            var2 = (Option)var3.next();
            if (var2.getModule().equals(this.getModule()) && var2 instanceof AuraMode) {
               ((BooleanOption)var2).setValueHard(false);
            }
         }
      } else {
         var3 = OptionManager.getOptionList().iterator();

         while(var3.hasNext()) {
            var2 = (Option)var3.next();
            if (var2.getModule().equals(this.getModule()) && var2 instanceof AuraMode && var2 != this) {
               ((BooleanOption)var2).setValueHard(true);
               break;
            }
         }
      }

      super.setValue(var1);
   }

   public boolean enable() {
      return (Boolean)this.getValue();
   }

   public AuraMode(String var1, boolean var2, Module var3) {
      super(var1, var1, var2, var3, true);
   }

   public boolean disable() {
      return (Boolean)this.getValue();
   }

   public boolean onUpdate(UpdateEvent var1) {
      return (Boolean)this.getValue();
   }
}
