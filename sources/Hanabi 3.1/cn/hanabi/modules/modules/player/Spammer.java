package cn.hanabi.modules.modules.player;

import cn.hanabi.command.commands.PrefixCommand;
import cn.hanabi.command.commands.SpammerCommand;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.io.IOException;
import java.util.Random;

public class Spammer extends Mod {
   public static String text = "Hanabi Moment.";
   public static String prefix = "[Hanabi]";
   private final Value spammerdelay = new Value("Spammer", "Delay", 2000.0, 500.0, 10000.0, 100.0);
   private final Value randomstring = new Value("Spammer", "Random String", true);
   TimeHelper delay = new TimeHelper();
   Random random = new Random();
   double state = 0.0;
   private int num;

   public Spammer() {
      super("Spammer", Category.PLAYER);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      this.setDisplayName("Delay:" + this.spammerdelay.getValueState() + " Times:" + this.num);

      try {
         SpammerCommand.loadText();
         PrefixCommand.loadText();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      if (this.delay.isDelayComplete(((Double)this.spammerdelay.getValueState()).longValue())) {
         ++this.state;
         ++this.num;
         String message = prefix + text;
         if ((Boolean)this.randomstring.getValueState()) {
            message = message + " >" + (new StringRandom()).getStringRandom(6) + "<";
         }

         mc.thePlayer.sendChatMessage(message);
         this.delay.reset();
      }

   }

   public void onDisable() {
      this.state = 0.0;
      this.num = 0;
      super.onDisable();
   }

   public static class StringRandom {
      public String getStringRandom(int length) {
         StringBuilder val = new StringBuilder();
         Random random = new Random();

         for(int i = 0; i < length; ++i) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
               int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
               val.append((char)(random.nextInt(26) + temp));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
               val.append(random.nextInt(10));
            }
         }

         return val.toString();
      }
   }
}
