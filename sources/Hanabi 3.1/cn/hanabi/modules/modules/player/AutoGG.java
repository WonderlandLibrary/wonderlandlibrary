package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.SoundFxPlayer;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ScreenShotHelper;

public class AutoGG extends Mod {
   public Value sceenshot = new Value("AutoGG", "SceenShot", true);
   public Value delay = new Value("AutoGG", "Speak Delay", 100.0, 100.0, 3000.0, 100.0);
   public boolean needSpeak = false;
   public boolean speaked = false;
   public boolean notiSent = false;
   public TimeHelper timer = new TimeHelper();
   public Notification noti;
   public String playCommand;
   public String lastTitle;

   public AutoGG() {
      super("AutoGG", Category.PLAYER);
      this.noti = new Notification("", Notification.Type.INFO);
      this.playCommand = "";
      this.lastTitle = "";
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if (mc.thePlayer != null) {
         AutoPlay autoPlay = (AutoPlay)ModManager.getModule("AutoPlay");
         boolean hypMode = true;
         if (this.needSpeak) {
            if (!this.speaked && this.timer.isDelayComplete((Double)this.delay.getValueState())) {
               this.speaked = true;
               mc.thePlayer.sendChatMessage("/ac GG");
               if ((Boolean)this.sceenshot.getValueState()) {
                  mc.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer()));
               }

               if (ModManager.getModule("AutoPlay").getState()) {
                  ClientUtil.notifications.add(this.noti);
                  (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Notification, -7.0F);
               }
            }

            if (this.speaked && this.timer.isDelayComplete((Double)autoPlay.delay.getValueState() + (Double)this.delay.getValueState())) {
               this.speaked = false;
               this.needSpeak = false;
            }
         }
      }

   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getPacket() instanceof S45PacketTitle) {
         S45PacketTitle packet = (S45PacketTitle)e.getPacket();
         String title = packet.getMessage().getFormattedText();
         if (title.startsWith("§6§l") && title.endsWith("§r") || title.startsWith("§c§lYOU") && title.endsWith("§r") || title.startsWith("§c§lGame") && title.endsWith("§r") || title.startsWith("§c§lWITH") && title.endsWith("§r") || title.startsWith("§c§lYARR") && title.endsWith("§r")) {
            this.timer.reset();
            this.needSpeak = true;
         }

         this.lastTitle = title;
      }

   }
}
