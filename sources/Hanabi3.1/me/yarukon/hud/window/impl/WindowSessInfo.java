package me.yarukon.hud.window.impl;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventPacket;
import cn.hanabi.modules.modules.combat.KillAura;
import cn.hanabi.utils.PacketHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.yarukon.hud.window.HudWindow;
import me.yarukon.hud.window.HudWindowManager;
import net.minecraft.network.play.server.S45PacketTitle;

@ObfuscationClass
public class WindowSessInfo extends HudWindow {
   public static int total = 0;
   public static int win = 0;

   public WindowSessInfo() {
      super("SessionInfo", 5.0F, 25.0F, 125.0F, 80.0F, "Session info", "", 12.0F, 0.0F, 1.0F);
      EventManager.register(this);
   }

   public void draw() {
      super.draw();
      long durationInMillis = System.currentTimeMillis() - HudWindowManager.startTime;
      String time;
      if (this.mc.isSingleplayer()) {
         time = "localhost";
      } else {
         long second = durationInMillis / 1000L % 60L;
         long minute = durationInMillis / 60000L % 60L;
         long hour = durationInMillis / 3600000L % 24L;
         time = String.format("%02dh %02dm %02ds", hour, minute, second);
      }

      double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX;
      double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ;
      double lastDist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
      String bps = String.format("%.2f bps", lastDist * 20.0 * (double)Wrapper.getTimer().timerSpeed);
      double tps = (double)Math.round(PacketHelper.tps * 10.0) / 10.0;
      Hanabi.INSTANCE.fontManager.sessionInfoIcon24.drawString("B", this.x + 5.0F, this.y + 16.0F + 2.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.usans16.drawString("Play time: " + time, this.x + 20.0F, this.y + 18.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.sessionInfoIcon24.drawString("C", this.x + 5.0F, this.y + 32.0F + 2.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.usans16.drawString("Move speed: " + bps, this.x + 20.0F, this.y + 33.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.sessionInfoIcon24.drawString("D", this.x + 4.0F, this.y + 46.0F + 2.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.usans16.drawString("Win / Total: " + win + " / " + total, this.x + 20.0F, this.y + 48.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.sessionInfoIcon24.drawString("E", this.x + 4.0F, this.y + 60.0F + 2.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.usans16.drawString("TPS: " + tps, this.x + 20.0F, this.y + 63.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.sessionInfoIcon20.drawString("F", this.x + 5.0F, this.y + 76.0F + 2.0F, this.textColor);
      Hanabi.INSTANCE.fontManager.usans16.drawString("Kills: " + KillAura.killCount, this.x + 20.0F, this.y + 77.0F, this.textColor);
   }

   @EventTarget
   public void onPacket(EventPacket evt) {
      if (evt.getPacket() instanceof S45PacketTitle) {
         S45PacketTitle packet = (S45PacketTitle)evt.getPacket();
         String title = packet.getMessage().getFormattedText();
         if (title.startsWith("§6§l") && title.endsWith("§r") || title.startsWith("§c§lYOU") && title.endsWith("§r") || title.startsWith("§c§lGame") && title.endsWith("§r") || title.startsWith("§c§lWITH") && title.endsWith("§r") || title.startsWith("§c§lYARR") && title.endsWith("§r")) {
            ++total;
         }

         if (title.startsWith("§6§l") && title.endsWith("§r")) {
            ++win;
         }
      }

   }
}
