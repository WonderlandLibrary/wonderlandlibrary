package de.violence.module.modules.PLAYER;

import de.violence.module.Module;
import de.violence.module.ui.Category;

public class Spammer extends Module {
   public Spammer() {
      super("Spammer", Category.PLAYER);
   }

   public void onEnable() {
      (new Thread(new Runnable() {
         public void run() {
            try {
               while(true) {
                  if(Spammer.this.isToggled()) {
                     Thread.sleep(8000L);
                     Spammer.this.mc.thePlayer.sendChatMessage(".info");
                  }
               }
            } catch (Exception var2) {
               ;
            }
         }
      })).start();
      super.onEnable();
   }
}
