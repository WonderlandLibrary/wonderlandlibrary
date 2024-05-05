package team.massacre.impl.event;

import java.util.Iterator;
import team.massacre.Massacre;
import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;
import team.massacre.api.module.Module;

public class EventKeyPress extends Cancellable implements Event {
   public int key;

   public EventKeyPress(int key) {
      this.key = key;
      Iterator var2 = Massacre.INSTANCE.getModuleManager().getModules().iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();
         if (module.getKey() == key) {
            module.toggle();
         }
      }

   }

   public int getKey() {
      return this.key;
   }
}
