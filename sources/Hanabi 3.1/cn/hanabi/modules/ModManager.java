package cn.hanabi.modules;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.events.EventKey;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.modules.modules.combat.AutoHead;
import cn.hanabi.modules.modules.combat.AutoHeal;
import cn.hanabi.modules.modules.combat.AutoSword;
import cn.hanabi.modules.modules.combat.Criticals;
import cn.hanabi.modules.modules.combat.KeepSprint;
import cn.hanabi.modules.modules.combat.KillAura;
import cn.hanabi.modules.modules.combat.Regen;
import cn.hanabi.modules.modules.combat.TPAura;
import cn.hanabi.modules.modules.combat.TargetStrafe;
import cn.hanabi.modules.modules.combat.Velocity;
import cn.hanabi.modules.modules.ghost.AutoClicker;
import cn.hanabi.modules.modules.ghost.AutoMLG;
import cn.hanabi.modules.modules.ghost.AutoPlace;
import cn.hanabi.modules.modules.ghost.AutoRod;
import cn.hanabi.modules.modules.ghost.DoubleClicker;
import cn.hanabi.modules.modules.ghost.Eagle;
import cn.hanabi.modules.modules.ghost.FastPlace;
import cn.hanabi.modules.modules.ghost.Hitbox;
import cn.hanabi.modules.modules.ghost.LegitVelocity;
import cn.hanabi.modules.modules.ghost.Reach;
import cn.hanabi.modules.modules.ghost.Refill;
import cn.hanabi.modules.modules.ghost.SafeWalk;
import cn.hanabi.modules.modules.ghost.SmoothAim;
import cn.hanabi.modules.modules.movement.HighJump;
import cn.hanabi.modules.modules.movement.IceSpeed;
import cn.hanabi.modules.modules.movement.NoJumpDelay;
import cn.hanabi.modules.modules.movement.NoSlow;
import cn.hanabi.modules.modules.movement.Sprint;
import cn.hanabi.modules.modules.movement.Step;
import cn.hanabi.modules.modules.movement.Strafe;
import cn.hanabi.modules.modules.movement.WaterSpeed;
import cn.hanabi.modules.modules.movement.Fly.Fly;
import cn.hanabi.modules.modules.movement.LongJump.LongJump;
import cn.hanabi.modules.modules.movement.Speed.Speed;
import cn.hanabi.modules.modules.player.AimBot;
import cn.hanabi.modules.modules.player.AutoArmor;
import cn.hanabi.modules.modules.player.AutoGG;
import cn.hanabi.modules.modules.player.AutoPlay;
import cn.hanabi.modules.modules.player.AutoTools;
import cn.hanabi.modules.modules.player.Blink;
import cn.hanabi.modules.modules.player.ChatBypass;
import cn.hanabi.modules.modules.player.ChestStealer;
import cn.hanabi.modules.modules.player.Debug;
import cn.hanabi.modules.modules.player.FastUse;
import cn.hanabi.modules.modules.player.InvCleaner;
import cn.hanabi.modules.modules.player.InvMove;
import cn.hanabi.modules.modules.player.LessPacket;
import cn.hanabi.modules.modules.player.NoFall;
import cn.hanabi.modules.modules.player.Nuker;
import cn.hanabi.modules.modules.player.Patcher;
import cn.hanabi.modules.modules.player.Spammer;
import cn.hanabi.modules.modules.player.TeleportBedFucker;
import cn.hanabi.modules.modules.render.AntiEffects;
import cn.hanabi.modules.modules.render.ArrowEsp;
import cn.hanabi.modules.modules.render.BedESP;
import cn.hanabi.modules.modules.render.CaveFinder;
import cn.hanabi.modules.modules.render.Chams;
import cn.hanabi.modules.modules.render.ChestESP;
import cn.hanabi.modules.modules.render.ClickGUIModule;
import cn.hanabi.modules.modules.render.ESP;
import cn.hanabi.modules.modules.render.EveryThingBlock;
import cn.hanabi.modules.modules.render.Fullbright;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.modules.modules.render.HitAnimation;
import cn.hanabi.modules.modules.render.HudWindow;
import cn.hanabi.modules.modules.render.MusicPlayer;
import cn.hanabi.modules.modules.render.NameProtect;
import cn.hanabi.modules.modules.render.Nametags;
import cn.hanabi.modules.modules.render.NoFov;
import cn.hanabi.modules.modules.render.NoHurtCam;
import cn.hanabi.modules.modules.render.OreTarget;
import cn.hanabi.modules.modules.render.PaletteGui;
import cn.hanabi.modules.modules.render.Projectiles;
import cn.hanabi.modules.modules.render.RacistHat;
import cn.hanabi.modules.modules.render.TabGUI;
import cn.hanabi.modules.modules.render.Thermal;
import cn.hanabi.modules.modules.render.ViewClip;
import cn.hanabi.modules.modules.render.Waypoints;
import cn.hanabi.modules.modules.render.WorldColor;
import cn.hanabi.modules.modules.render.WorldTime;
import cn.hanabi.modules.modules.render.WorldWeather;
import cn.hanabi.modules.modules.world.AntiAtlas;
import cn.hanabi.modules.modules.world.AntiBot;
import cn.hanabi.modules.modules.world.AntiFall;
import cn.hanabi.modules.modules.world.AntiSpammer;
import cn.hanabi.modules.modules.world.AutoL;
import cn.hanabi.modules.modules.world.HideAndSeek;
import cn.hanabi.modules.modules.world.Jesus;
import cn.hanabi.modules.modules.world.MurderMystery;
import cn.hanabi.modules.modules.world.Phase;
import cn.hanabi.modules.modules.world.Scaffold;
import cn.hanabi.modules.modules.world.SpeedMine;
import cn.hanabi.modules.modules.world.Spoofer;
import cn.hanabi.modules.modules.world.Teams;
import cn.hanabi.modules.modules.world.Timer;
import cn.hanabi.modules.modules.world.UhcHelper;
import cn.hanabi.modules.modules.world.VClip;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@ObfuscationClass
public class ModManager {
   public static @NotNull List modules = new ArrayList();
   public static boolean needsort = true;
   public static ArrayList sortedModList = new ArrayList();

   public ModManager() {
      EventManager.register(this);
   }

   public static @NotNull List getModules() {
      List temp = new ArrayList();
      Iterator var1 = modules.iterator();

      while(var1.hasNext()) {
         Mod mod = (Mod)var1.next();
         if (mod.isHidden()) {
            temp.add(mod);
         }
      }

      return temp;
   }

   public static @NotNull List getModules(Category category) {
      ArrayList mods = new ArrayList();
      Iterator var2 = getModList().iterator();

      while(var2.hasNext()) {
         Mod mod = (Mod)var2.next();
         if (mod.getCategory() == category) {
            mods.add(mod);
         }
      }

      return mods;
   }

   public static ArrayList getEnabledModListHUD() {
      HFontRenderer font = HUD.hudMode.isCurrentMode("Classic") ? Hanabi.INSTANCE.fontManager.raleway17 : Hanabi.INSTANCE.fontManager.usans18;
      ArrayList enabledModList = new ArrayList(getModules());
      enabledModList.sort((o1, o2) -> {
         return (int)((o2.getDisplayName() != null ? (float)(font.getStringWidth(o2.getDisplayName()) + 3 + font.getStringWidth(o2.getName())) : o2.namewidth) - (o1.getDisplayName() != null ? (float)(font.getStringWidth(o1.getDisplayName()) + 3 + font.getStringWidth(o1.getName())) : o1.namewidth));
      });
      return enabledModList;
   }

   public static Mod getModule(@NotNull String name) {
      try {
         return getModule(name, false);
      } catch (Exception var2) {
         return new Mod("NMSL", Category.COMBAT) {
         };
      }
   }

   public static Mod getModule(Class clazz) {
      return (Mod)modules.stream().filter((mod) -> {
         return mod.getClass() == clazz;
      }).findFirst().orElse((Object)null);
   }

   public static Mod getModule(@NotNull String name, boolean caseSensitive) {
      return (Mod)modules.stream().filter((mod) -> {
         return !caseSensitive && name.equalsIgnoreCase(mod.getName()) || name.equals(mod.getName());
      }).findFirst().orElse((Object)null);
   }

   public static List getModList() {
      List returnmodules = new ArrayList();
      Iterator var1 = modules.iterator();

      while(var1.hasNext()) {
         Mod mod = (Mod)var1.next();
         if (mod.isHidden()) {
            returnmodules.add(mod);
         }
      }

      return returnmodules;
   }

   public static ArrayList getEnabledModList() {
      ArrayList enabledModList = new ArrayList();
      Iterator var1 = getModules().iterator();

      while(var1.hasNext()) {
         Mod m = (Mod)var1.next();
         if (m.isEnabled()) {
            enabledModList.add(m);
         }
      }

      return enabledModList;
   }

   public void addModules() {
      this.addModule(new Hitbox());
      this.addModule(new KeepSprint());
      this.addModule(new Velocity());
      this.addModule(new KillAura());
      this.addModule(new Criticals());
      this.addModule(new AutoSword());
      this.addModule(new Reach());
      this.addModule(new TargetStrafe());
      this.addModule(new AutoHeal());
      this.addModule(new AutoHead());
      this.addModule(new Regen());
      this.addModule(new TPAura());
      this.addModule(new Sprint());
      this.addModule(new Speed());
      this.addModule(new Fly());
      this.addModule(new Strafe());
      this.addModule(new LongJump());
      this.addModule(new NoSlow());
      this.addModule(new Step());
      this.addModule(new HighJump());
      this.addModule(new NoJumpDelay());
      this.addModule(new IceSpeed());
      this.addModule(new WaterSpeed());
      this.addModule(new AutoArmor());
      this.addModule(new InvCleaner());
      this.addModule(new InvMove());
      this.addModule(new NoFall());
      this.addModule(new ChatBypass());
      this.addModule(new LessPacket());
      this.addModule(new ChestStealer());
      this.addModule(new Nuker());
      this.addModule(new FastUse());
      this.addModule(new Blink());
      this.addModule(new FastPlace());
      this.addModule(new AutoTools());
      this.addModule(new AutoGG());
      this.addModule(new Patcher());
      this.addModule(new AutoPlay());
      this.addModule(new Mod("NoCommand", Category.PLAYER) {
      });
      this.addModule(new TeleportBedFucker());
      this.addModule(new Spammer());
      this.addModule(new AimBot());
      this.addModule(new ClickGUIModule());
      this.addModule(new PaletteGui());
      this.addModule(new HudWindow());
      this.addModule(new RacistHat());
      this.addModule(new Nametags());
      this.addModule(new Fullbright());
      this.addModule(new ESP());
      this.addModule(new Projectiles());
      this.addModule(new BedESP());
      this.addModule(new ChestESP());
      this.addModule(new HitAnimation());
      this.addModule(new CaveFinder());
      this.addModule(new Chams());
      this.addModule(new OreTarget());
      this.addModule(new Waypoints());
      this.addModule(new MusicPlayer());
      this.addModule(new ArrowEsp());
      this.addModule(new NoFov());
      this.addModule(new NameProtect());
      this.addModule(new TabGUI());
      this.addModule(new NoHurtCam());
      this.addModule(new ViewClip());
      this.addModule(new EveryThingBlock());
      this.addModule(new Thermal());
      this.addModule(new WorldColor());
      this.addModule(new WorldTime());
      this.addModule(new WorldWeather());
      this.addModule(new AntiEffects());
      this.addModule(new AntiBot());
      this.addModule(new Teams());
      this.addModule(new AntiFall());
      this.addModule(new AutoL());
      this.addModule(new HideAndSeek());
      this.addModule(new Eagle());
      this.addModule(new HUD());
      this.addModule(new SpeedMine());
      this.addModule(new UhcHelper());
      this.addModule(new AntiSpammer());
      this.addModule(new Jesus());
      this.addModule(new Scaffold());
      this.addModule(new Debug());
      this.addModule(new Phase());
      this.addModule(new Timer());
      this.addModule(new Spoofer());
      this.addModule(new MurderMystery());
      this.addModule(new VClip());
      this.addModule(new AntiAtlas());
      this.addModule(new AutoClicker());
      this.addModule(new LegitVelocity());
      this.addModule(new SmoothAim());
      this.addModule(new AutoMLG());
      this.addModule(new DoubleClicker());
      this.addModule(new AutoRod());
      this.addModule(new Refill());
      this.addModule(new SafeWalk());
      this.addModule(new AutoPlace());
      modules.sort((mod, mod1) -> {
         int char0 = mod.getName().charAt(0);
         int char1 = mod1.getName().charAt(0);
         return -Integer.compare(char1, char0);
      });
   }

   public void addModule(@NotNull Mod module) {
      modules.add(module);
   }

   @EventTarget
   private void onKey(@NotNull EventKey event) {
      Iterator var2 = modules.iterator();

      while(var2.hasNext()) {
         Mod module = (Mod)var2.next();
         if (module.getKeybind() == event.getKey()) {
            module.setState(!module.getState());
         }
      }

   }
}
