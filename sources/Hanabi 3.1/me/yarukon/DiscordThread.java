package me.yarukon;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.modules.modules.combat.KillAura;
import cn.hanabi.utils.TimeHelper;
import me.yarukon.hud.window.HudWindowManager;
import me.yarukon.hud.window.impl.WindowSessInfo;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

public class DiscordThread extends Thread {
   public static final String DISCORD_ID = "898551434608533555";
   public boolean isDiscordRunning = false;
   public TimeHelper timer = new TimeHelper();

   public void run() {
      this.timer.reset();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         Hanabi.INSTANCE.println("Closing Discord hook.");
         this.isDiscordRunning = false;
         DiscordRPC.discordShutdown();
      }));
      DiscordEventHandlers handlers = (new DiscordEventHandlers.Builder()).setReadyEventHandler((user) -> {
         Hanabi.INSTANCE.println("Found Discord: " + user.username + "#" + user.discriminator + ".");
         this.isDiscordRunning = true;
      }).build();
      DiscordRPC.discordInitialize("898551434608533555", handlers, true);
      DiscordRPC.discordRegister("898551434608533555", "");

      while(true) {
         while(true) {
            DiscordRPC.discordRunCallbacks();
            if (this.isDiscordRunning) {
               int killed = KillAura.killCount;
               int win = WindowSessInfo.win;
               DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder("Killed: " + killed + " Won:" + win);
               presence.setBigImage("logo", "Hanabi 3.1 [" + (Client.rank.equals("release") ? "Release" : "Beta") + "]");
               presence.setDetails("User: " + Client.username);
               if (!Minecraft.getMinecraft().isSingleplayer() && Minecraft.getMinecraft().getCurrentServerData() != null) {
                  String ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
                  ip = ip.contains(":") ? ip : ip + ":25565";
                  boolean isHypickle = ip.toLowerCase().contains("hypixel");
                  presence.setSmallImage(isHypickle ? "hypickle" : "server", isHypickle ? "mc.hypixel.net:25565" : ip);
                  presence.setStartTimestamps(HudWindowManager.startTime / 1000L);
               }

               DiscordRPC.discordUpdatePresence(presence.build());

               try {
                  sleep(1000L);
               } catch (InterruptedException var7) {
                  var7.printStackTrace();
               }
            } else if (this.timer.isDelayComplete(10000L)) {
               Hanabi.INSTANCE.println("Timeout while finding Discord process! exiting...");
               return;
            }
         }
      }
   }
}
