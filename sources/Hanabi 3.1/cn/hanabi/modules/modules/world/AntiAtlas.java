package cn.hanabi.modules.modules.world;

import cn.hanabi.events.EventChat;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class AntiAtlas extends Mod {
   public Value delay = new Value("AntiAtlas", "Delay", 3.0, 1.0, 5.0, 0.5);
   private final TimeHelper timer = new TimeHelper();
   private int index;

   public AntiAtlas() {
      super("AntiAtlas", Category.WORLD);
   }

   public void onEnable() {
      super.onEnable();
      this.index = 0;
   }

   public void onDisable() {
      super.onDisable();
      this.index = 0;
   }

   @EventTarget
   void onLoadWorld(EventWorldChange event) {
      this.index = 0;
   }

   @EventTarget
   void onUpdate(EventUpdate event) {
      if (this.timer.isDelayComplete((long)(((Double)this.delay.getValue()).intValue() + 5) * 1000L)) {
         ++this.index;
         ArrayList players = new ArrayList(mc.theWorld.playerEntities);
         players.removeIf((player) -> {
            AntiBot var10000 = (AntiBot)ModManager.getModule(AntiBot.class);
            boolean var2;
            if (!AntiBot.isBot(player)) {
               Teams var1 = (Teams)ModManager.getModule(Teams.class);
               if (!Teams.isOnSameTeam(player)) {
                  var2 = false;
                  return var2;
               }
            }

            var2 = true;
            return var2;
         });
         this.index = this.index >= players.size() ? 0 : this.index;

         try {
            if (players.get(this.index) == mc.thePlayer) {
               return;
            }

            mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("/report " + ((EntityPlayer)players.get(this.index)).getName() + " killaura"));
         } catch (Exception var4) {
            this.index = 0;
         }

         this.timer.reset();
      }

   }

   @EventTarget
   void onChat(EventChat event) {
      IChatComponent cc = event.getChatComponent();
      if (cc.getSiblings().size() == 0) {
         ChatStyle cs = cc.getChatStyle();
         if (cs.getColor() == EnumChatFormatting.GREEN && cs.getChatClickEvent() == null && cs.getChatHoverEvent() == null) {
            event.setCancelled(true);
         }
      }

   }
}
