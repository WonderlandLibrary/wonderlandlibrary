package team.massacre.impl.module.miscellaneous;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventPacket;
import team.massacre.utils.PacketUtil;
import team.massacre.utils.PlayerUtils;
import team.massacre.utils.TimerUtil;

public class AutoPlay extends Module {
   private TimerUtil timer = new TimerUtil();
   private EnumProperty<AutoPlay.Mode> mode;
   private DoubleProperty delay;

   public AutoPlay() {
      super("AutoPlay", 0, Category.PLAYER);
      this.mode = new EnumProperty("AutoPlay Mode", AutoPlay.Mode.Solo);
      this.delay = new DoubleProperty("Join Delay", 1300.0D, 100.0D, 5000.0D, 10.0D, Representation.MILLISECONDS);
      this.addValues(new Property[]{this.mode, this.delay});
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      this.timer.reset();
   }

   @Handler
   public void onReceivePacket(EventPacket eventPacketReceive) {
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         if (eventPacketReceive.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat)eventPacketReceive.getPacket();
            if (!s02PacketChat.getChatComponent().getUnformattedText().isEmpty()) {
               String message = s02PacketChat.getChatComponent().getUnformattedText();
               if (message.contains("You won! Want to play again?") || message.contains("You died! Want to play again?") && PlayerUtils.onHypixel()) {
                  Thread thread = new Thread() {
                     public void run() {
                        try {
                           Thread.sleep(((Double)AutoPlay.this.delay.getValue()).longValue());
                        } catch (Exception var2) {
                           var2.printStackTrace();
                        }

                        switch((AutoPlay.Mode)AutoPlay.this.mode.getValue()) {
                        case Solo:
                           PacketUtil.sendPacketNoEvent(new C01PacketChatMessage("/play solo_insane"));
                           break;
                        case Teams:
                           PacketUtil.sendPacketNoEvent(new C01PacketChatMessage("/play teams_insane"));
                        }

                     }
                  };
                  thread.start();
               }
            }
         }

      }
   }

   private static enum Mode {
      Solo,
      Teams;
   }
}
