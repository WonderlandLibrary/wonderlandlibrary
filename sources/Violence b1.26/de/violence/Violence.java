package de.violence;

import de.violence.command.CommandManager;
import de.violence.config.ConfigList;
import de.violence.irc.PacketManager;
import de.violence.irc.SocketConnector;
import de.violence.module.Module;
import de.violence.module.modules.COMBAT.Criticals;
import de.violence.module.modules.COMBAT.Killaura;
import de.violence.module.modules.COMBAT.Triggerbot;
import de.violence.module.modules.MOVEMENT.AirJump;
import de.violence.module.modules.MOVEMENT.Fly;
import de.violence.module.modules.MOVEMENT.Glide;
import de.violence.module.modules.MOVEMENT.Hypixel;
import de.violence.module.modules.MOVEMENT.IceSpeed;
import de.violence.module.modules.MOVEMENT.InventoryWalk;
import de.violence.module.modules.MOVEMENT.Jesus;
import de.violence.module.modules.MOVEMENT.LongJump;
import de.violence.module.modules.MOVEMENT.NoSlowDown;
import de.violence.module.modules.MOVEMENT.NoWeb;
import de.violence.module.modules.MOVEMENT.ReverseStep;
import de.violence.module.modules.MOVEMENT.SafeWalk;
import de.violence.module.modules.MOVEMENT.Speed;
import de.violence.module.modules.MOVEMENT.Sprint;
import de.violence.module.modules.MOVEMENT.StairsSpeed;
import de.violence.module.modules.MOVEMENT.Step;
import de.violence.module.modules.MOVEMENT.Teleport;
import de.violence.module.modules.MOVEMENT.TerrainSpeed;
import de.violence.module.modules.MOVEMENT.WallSpeed;
import de.violence.module.modules.MOVEMENT.WaterSpeed;
import de.violence.module.modules.OTHER.AntiBots;
import de.violence.module.modules.OTHER.AutoRespawn;
import de.violence.module.modules.OTHER.ClientFriends;
import de.violence.module.modules.OTHER.Hitbox;
import de.violence.module.modules.OTHER.MidClickFriends;
import de.violence.module.modules.OTHER.Teams;
import de.violence.module.modules.PLAYER.AntiCactus;
import de.violence.module.modules.PLAYER.AutoArmor;
import de.violence.module.modules.PLAYER.AutoSoup;
import de.violence.module.modules.PLAYER.BowAimbot;
import de.violence.module.modules.PLAYER.ChestStealer;
import de.violence.module.modules.PLAYER.FastBow;
import de.violence.module.modules.PLAYER.FastEat;
import de.violence.module.modules.PLAYER.InvCleaner;
import de.violence.module.modules.PLAYER.InventoryTweaks;
import de.violence.module.modules.PLAYER.NoFall;
import de.violence.module.modules.PLAYER.NoRightClickDelay;
import de.violence.module.modules.PLAYER.Spammer;
import de.violence.module.modules.PLAYER.TntBlocker;
import de.violence.module.modules.PLAYER.Velocity;
import de.violence.module.modules.RENDER.BlockESP;
import de.violence.module.modules.RENDER.Chams;
import de.violence.module.modules.RENDER.ESP;
import de.violence.module.modules.RENDER.FovChanger;
import de.violence.module.modules.RENDER.Fullbright;
import de.violence.module.modules.RENDER.GUI;
import de.violence.module.modules.RENDER.ItemESP;
import de.violence.module.modules.RENDER.NameProtect;
import de.violence.module.modules.RENDER.NameTags;
import de.violence.module.modules.RENDER.NoBob;
import de.violence.module.modules.RENDER.NoFov;
import de.violence.module.modules.RENDER.NoHurtTime;
import de.violence.module.modules.RENDER.NoInvisibles;
import de.violence.module.modules.RENDER.NoScoreboard;
import de.violence.module.modules.RENDER.ProjectileESP;
import de.violence.module.modules.RENDER.StorageESP;
import de.violence.module.modules.SETTINGS.Hud;
import de.violence.module.modules.SETTINGS.IRC;
import de.violence.module.modules.SETTINGS.Model;
import de.violence.module.modules.SETTINGS.TabGuiMod;
import de.violence.module.modules.WORLD.ChestAura;
import de.violence.module.modules.WORLD.Fucker;
import de.violence.module.modules.WORLD.Scaffold;
import de.violence.module.modules.WORLD.Tower;
import de.violence.module.ui.Category;
import de.violence.packet.PacketHandler;
import de.violence.save.manager.FileManager;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Violence {
   public static String NAME = "violence";
   public static String VERSION = "b1.26";
   static SocketConnector socketConnector;
   static Violence violence;
   static PacketManager packetManager;
   static PacketHandler packetHandler;

   public Violence() {
      violence = this;
   }

   public static SocketConnector getSocketConnector() {
      return socketConnector;
   }

   public static PacketManager getPacketManager() {
      return packetManager;
   }

   public static PacketHandler getPacketHandler() {
      return packetHandler;
   }

   public void sendChat(String s) {
      Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§3" + NAME + "§7] §f" + s));
   }

   public void onStart() {
      this.loadModules();
      new CommandManager();
      packetHandler = new PacketHandler();
      this.setUUID();
      packetManager = new PacketManager();
      socketConnector = new SocketConnector();
      socketConnector.onLoad();
   }

   private void setUUID() {
      if(!FileManager.gui.containsValue("uuid").booleanValue()) {
         FileManager.gui.setString("uuid", UUID.randomUUID().toString());
         FileManager.gui.save();
      }

   }

   public void loadModules() {
      new Sprint();
      new GUI();
      new Hud();
      new Killaura();
      new Criticals();
      new NoInvisibles();
      new ChestStealer();
      new IceSpeed();
      new ClientFriends();
      new Teams();
      new Model();
      new AutoArmor();
      new InventoryWalk();
      new NoSlowDown();
      new Jesus();
      new StairsSpeed();
      new NoWeb();
      new Scaffold();
      new ESP();
      new NoFall();
      new Speed();
      new BowAimbot();
      new InvCleaner();
      new AntiBots();
      new Fly();
      new Fucker();
      new Triggerbot();
      new Velocity();
      new TabGuiMod();
      new WaterSpeed();
      new NameProtect();
      new FovChanger();
      new NameTags();
      new NoRightClickDelay();
      new IRC();
      new SafeWalk();
      new TerrainSpeed();
      new Step();
      new StorageESP();
      new ItemESP();
      new BlockESP();
      new Tower();
      new MidClickFriends();
      new Chams();
      new Hypixel();
      new NoHurtTime();
      new NoFov();
      new AutoRespawn();
      new ChestAura();
      new NoScoreboard();
      new NoBob();
      new ProjectileESP();
      new InventoryTweaks();
      new Spammer();
      new Fullbright();
      new ReverseStep();
      new Glide();
      new AirJump();
      new FastBow();
      new FastEat();
      new LongJump();
      new AntiCactus();
      new WallSpeed();
      new AutoSoup();
      new TntBlocker();
      new Teleport();
      new Hitbox();

      try {
         new ConfigList();
         Iterator var2 = ConfigList.getConfigs().iterator();

         while(var2.hasNext()) {
            String configs = (String)var2.next();
            new Module(configs, Category.CONFIGS);
         }
      } catch (Exception var3) {
         ;
      }

   }

   public static Violence getViolence() {
      return violence;
   }
}
