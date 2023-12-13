package org.alphacentauri.modules;

import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.RandomString;
import org.alphacentauri.management.util.Timer;

public class ModuleSpammer extends Module implements EventListener {
   public Timer timer = new Timer();
   public int currentDelay;
   public Property message = new Property(this, "Message", "alpha-centauri dot tk");
   public Property delay = new Property(this, "Delay", Integer.valueOf(5000));
   public Property random = new Property(this, "Randomization", Integer.valueOf(1000));
   public Property antiSpamBypass = new Property(this, "AntiSpamBypass", Boolean.valueOf(true));
   public Property antiSpamBypassAmount = new Property(this, "AntiSpamBypassAmount", Integer.valueOf(6));

   public ModuleSpammer() {
      super("Spammer", "Spams the chat", new String[]{"spammer"}, Module.Category.Misc, 15353129);
   }

   public void setEnabledSilent(boolean enabled) {
      this.timer.reset();
      this.currentDelay = ((Integer)this.delay.value).intValue() + (((Integer)this.random.value).intValue() > 0?AC.getRandom().nextInt(((Integer)this.random.value).intValue()):0);
      super.setEnabledSilent(enabled);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick && this.timer.hasMSPassed((long)this.currentDelay)) {
         this.timer.reset();
         this.currentDelay = ((Integer)this.delay.value).intValue() + (((Integer)this.random.value).intValue() > 0?AC.getRandom().nextInt(((Integer)this.random.value).intValue()):0);
         String send = (String)this.message.value;
         if(((Boolean)this.antiSpamBypass.value).booleanValue() && ((Integer)this.antiSpamBypassAmount.value).intValue() > 0) {
            send = send + " | " + (new RandomString(((Integer)this.antiSpamBypassAmount.value).intValue())).nextNoNumberString();
         }

         AC.getMC().getPlayer().sendChatMessage(send);
      }

   }
}
