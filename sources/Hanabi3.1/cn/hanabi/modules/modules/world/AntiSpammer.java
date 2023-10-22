package cn.hanabi.modules.modules.world;

import cn.hanabi.events.EventPacket;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.Levenshtein;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.network.play.server.S02PacketChat;

public class AntiSpammer extends Mod {
   public static final Levenshtein lt = new Levenshtein();
   public Value history = new Value("AntiSpammer", "History Message", 10.0, 3.0, 30.0, 1.0);
   public Value ratio = new Value("AntiSpammer", "Ratio", 0.6, 0.1, 1.0, 0.01);
   public ArrayList historyChat = new ArrayList();

   public AntiSpammer() {
      super("AntiSpammer", Category.WORLD);
   }

   @EventTarget
   public void onFuckingPacket(EventPacket motherfucker) {
      if (mc.thePlayer != null && mc.theWorld != null && motherfucker.getPacket() instanceof S02PacketChat) {
         S02PacketChat packet = (S02PacketChat)motherfucker.getPacket();
         String message = packet.getChatComponent().getFormattedText().replaceAll("§.", "").replaceAll("[.*?]", "").replaceAll("<.*?>", "");
         StringBuilder result = new StringBuilder();
         int isSpace = 0;
         char[] var6 = message.toCharArray();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            char c = var6[var8];
            if (c == ' ') {
               ++isSpace;
            }

            if (isSpace >= 2) {
               result.append(c);
            }
         }

         result = new StringBuilder(result.toString().replace(" ", ""));
         Iterator var10 = this.historyChat.iterator();

         while(var10.hasNext()) {
            String history = (String)var10.next();
            double similar = (double)lt.getSimilarityRatio(result.toString(), history);
            if (similar > (Double)this.ratio.getValueState()) {
               if (this.getDisplayName() != null) {
                  this.setDisplayName(Integer.parseInt(this.getDisplayName()) + 1 + "");
               } else {
                  this.setDisplayName("1");
               }

               motherfucker.setCancelled(true);
               break;
            }
         }

         this.historyChat.add(result.toString());
         if (this.historyChat.size() > ((Double)this.history.getValueState()).intValue()) {
            this.historyChat.remove(0);
         }
      }

   }
}
