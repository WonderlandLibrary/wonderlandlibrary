package me.rhys.client;

import me.rhys.base.command.Command;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.InitializeEvent;
import me.rhys.base.event.impl.init.CommandInitializeEvent;
import me.rhys.base.event.impl.init.FileInitializeEvent;
import me.rhys.base.event.impl.init.ModuleInitializeEvent;
import me.rhys.base.file.IFile;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.client.command.BindCommand;
import me.rhys.client.command.FriendCommand;
import me.rhys.client.command.HelpCommand;
import me.rhys.client.command.PluginsCommand;
import me.rhys.client.command.ToggleCommand;
import me.rhys.client.command.VClip;
import me.rhys.client.files.AccountsFile;
import me.rhys.client.module.combat.AutoGapple;
import me.rhys.client.module.combat.AutoSoup;
import me.rhys.client.module.combat.CrackHead;
import me.rhys.client.module.combat.FastBow;
import me.rhys.client.module.combat.PVPBot;
import me.rhys.client.module.combat.aura.Aura;
import me.rhys.client.module.combat.autorod.AutoRod;
import me.rhys.client.module.combat.criticals.Criticals;
import me.rhys.client.module.combat.velocity.Velocity;
import me.rhys.client.module.ghost.AimBot;
import me.rhys.client.module.ghost.AntiCombo;
import me.rhys.client.module.ghost.ConnectionDrop;
import me.rhys.client.module.ghost.Eagle;
import me.rhys.client.module.ghost.Reach;
import me.rhys.client.module.ghost.WTap;
import me.rhys.client.module.ghost.autoclicker.AutoClicker;
import me.rhys.client.module.ghost.beanernet.BeanerNet;
import me.rhys.client.module.ghost.ghostbot.GhostBot;
import me.rhys.client.module.movement.AutoJump;
import me.rhys.client.module.movement.CustomSpeed;
import me.rhys.client.module.movement.Sprint;
import me.rhys.client.module.movement.antivoid.AntiVoid;
import me.rhys.client.module.movement.fly.Fly;
import me.rhys.client.module.movement.highjump.HighJump;
import me.rhys.client.module.movement.noslow.NoSlow;
import me.rhys.client.module.movement.speed.Speed;
import me.rhys.client.module.movement.step.Step;
import me.rhys.client.module.player.AntiCheatFinder;
import me.rhys.client.module.player.AutoSwordSlot;
import me.rhys.client.module.player.BoatCrafter;
import me.rhys.client.module.player.ChatBypass;
import me.rhys.client.module.player.Clipper;
import me.rhys.client.module.player.Debugger;
import me.rhys.client.module.player.FPSBooster;
import me.rhys.client.module.player.FastEat;
import me.rhys.client.module.player.InvManager;
import me.rhys.client.module.player.InventoryMove;
import me.rhys.client.module.player.Log4J;
import me.rhys.client.module.player.NoRotate;
import me.rhys.client.module.player.PacketBlock;
import me.rhys.client.module.player.Spammer;
import me.rhys.client.module.player.Stealer;
import me.rhys.client.module.player.Timer;
import me.rhys.client.module.player.autoarmor.AutoArmor;
import me.rhys.client.module.player.breaker.Breaker;
import me.rhys.client.module.player.crasher.Crasher;
import me.rhys.client.module.player.disabler.Disabler;
import me.rhys.client.module.player.nofall.NoFall;
import me.rhys.client.module.player.pingspoof.PingSpoof;
import me.rhys.client.module.player.scaffold.Scaffold;
import me.rhys.client.module.render.Cape;
import me.rhys.client.module.render.Chams;
import me.rhys.client.module.render.ClickGui;
import me.rhys.client.module.render.FullBright;
import me.rhys.client.module.render.HUD;
import me.rhys.client.module.render.ItemScale;
import me.rhys.client.module.render.KeyStrokes;
import me.rhys.client.module.render.Nametags;
import me.rhys.client.module.render.NoHurtCam;
import me.rhys.client.module.render.NoParticals;
import me.rhys.client.module.render.TimeChanger;
import me.rhys.client.module.render.animations.Animations;
import me.rhys.client.ui.alt.AltUI;
import me.rhys.client.ui.click.ClickUI;

public class Manager {
  @EventTarget
  public void initialize(InitializeEvent event) {
    event.setName("Cupid");
    event.setVersion("0.1");
  }
  
  @EventTarget
  public void commandInitialize(CommandInitializeEvent event) {
    event.register(new Command[] { (Command)new HelpCommand("help", "shows you information about other commands", new String[0]), (Command)new ToggleCommand("toggle", "toggle <module>", "toggles a module", new String[] { "t" }), (Command)new BindCommand("bind", "bind <add/remove> <module> <key>", "binds a key to a module", new String[0]), (Command)new PluginsCommand("plugins", "shows you the servers plugins", new String[0]), (Command)new FriendCommand("friend", "friend add / remove <name>, list", "if you have friends..", new String[] { "f" }), (Command)new VClip("vclip", "teleport through blocks", new String[] { "vc" }) });
  }
  
  @EventTarget
  public void fileInitialize(FileInitializeEvent event) {
    event.register((IFile)new AccountsFile());
  }
  
  @EventTarget
  public void moduleInitialize(ModuleInitializeEvent event) {
    event.register(new Module[] { 
          (Module)new Aura("Aura", "Attacks players for you", Category.COMBAT, 0), (Module)new Velocity("Velocity", "Control your velocity", Category.COMBAT, 0), (Module)new AutoSoup("AutoSoup", "atgker943", Category.COMBAT, 0), (Module)new CrackHead("CrackHead", "dfgdg", Category.COMBAT, 0), (Module)new Criticals("Criticals", "Force Critical attacks", Category.COMBAT, 0), (Module)new Sprint("Sprint", "Sprints for you", Category.MOVEMENT, 0), (Module)new Fly("Fly", "Fly like a faggot", Category.MOVEMENT, 0), (Module)new AntiVoid("AntiVoid", "erkkgr", Category.MOVEMENT, 0), (Module)new Speed("Speed", "move at un-legit speeds", Category.MOVEMENT, 0), (Module)new NoSlow("NoSlow", "Removes slowdown from items / blocks", Category.MOVEMENT, 0), 
          (Module)new Step("Step", "Steps up blocks", Category.MOVEMENT, 0), (Module)new HighJump("HighJump", "dfdf", Category.MOVEMENT, 0), (Module)new AutoJump("AutoJump", "jump for u", Category.MOVEMENT, 0), (Module)new Timer("Timer", "Control game time", Category.PLAYER, 0), (Module)new FastEat("FastEat", "arehare", Category.PLAYER, 0), (Module)new Clipper("Clipper", "Ddgdg", Category.PLAYER, 0), (Module)new PacketBlock("PacketBlock", "PacketBlock", Category.PLAYER, 0), (Module)new BoatCrafter("BoatCrafter", "asasa", Category.PLAYER, 0), (Module)new PingSpoof("PingSpoof", "Spoof your connection latency", Category.PLAYER, 0), (Module)new InventoryMove("InventoryMove", "Allows you to move in your inventory", Category.PLAYER, 0), 
          (Module)new NoRotate("NoRotate", "Prevents Server from changing head position", Category.PLAYER, 0), (Module)new HUD("HUD", "HUD for the client", Category.RENDER, 0), (Module)new FullBright("FullBright", "Brighter", Category.RENDER, 0), (Module)new KeyStrokes("KeyStrokes", "KeyStrokes", Category.RENDER, 0), (Module)new ItemScale("ItemScale", "ItemScale", Category.RENDER, 0), (Module)new Cape("Cape", "capes", Category.RENDER, 0), (Module)new TimeChanger("TimeChanger", "dgdfgdf", Category.RENDER, 0), (Module)new NoParticals("NoParticals", "NoParticals", Category.RENDER, 0), (Module)new ClickGui("ClickGui", "Opens the ClickUI", Category.RENDER, 54), (Module)new FPSBooster("FPSBooster", "Crashes your game", Category.PLAYER, 0), 
          (Module)new Animations("Animations", "Change swing / block animations", Category.RENDER, 0), (Module)new Chams("Chams", "Chams?", Category.RENDER, 0), (Module)new Nametags("Nametags", "Display a player's nametag", Category.RENDER, 0), (Module)new NoHurtCam("NoHurtCam", "Hides hurtcam", Category.RENDER, 0), (Module)new NoFall("NoFall", "Prevents fall damage", Category.PLAYER, 0), (Module)new Disabler("Disabler", "Disables Anti-Cheats", Category.PLAYER, 0), (Module)new Spammer("Spammer", "dfdf", Category.PLAYER, 0), (Module)new Log4J("Log4J", "dgfdg", Category.PLAYER, 0), (Module)new AntiCheatFinder("AntiCheatFinder", "sfdf", Category.PLAYER, 0), (Module)new Crasher("Crasher", "Crash a server", Category.PLAYER, 0), 
          (Module)new ChatBypass("ChatBypass", "Bypass ur mom", Category.PLAYER, 0), (Module)new Breaker("Breaker", "Breaks shit.", Category.PLAYER, 0), (Module)new Stealer("Stealer", "Stealers thing like a crab", Category.PLAYER, 0), (Module)new AutoArmor("AutoArmor", "Thing", Category.PLAYER, 0), (Module)new InvManager("InvManager", "thing", Category.PLAYER, 0), (Module)new Debugger("Debugger", "Debugger", Category.PLAYER, 0), (Module)new FastBow("FastBow", "etawgwr", Category.COMBAT, 0), (Module)new AutoGapple("AutoGapple", "Autogap", Category.COMBAT, 0), (Module)new Reach("Reach", "Extend Reach", Category.GHOST, 0), (Module)new WTap("WTap", "dgfg", Category.GHOST, 0), 
          (Module)new GhostBot("GhostBot", "asasa", Category.GHOST, 0), (Module)new BeanerNet("BeanerNet", "Bean a net", Category.GHOST, 0), (Module)new Eagle("Eagle", "Eagle", Category.GHOST, 0), (Module)new AutoClicker("AutoClicker", "Clicks for you", Category.GHOST, 0), (Module)new AntiCombo("AntiCombo (MMC)", "Anti Combo", Category.GHOST, 0), (Module)new AutoSwordSlot("AutoSwordSlot", "dfdg", Category.GHOST, 0), (Module)new AutoRod("AutoRod", "AutoRods", Category.GHOST, 0), (Module)new AimBot("AimBot", "rhear", Category.GHOST, 0), (Module)new ConnectionDrop("ConnectionDrop", "fgf", Category.GHOST, 0), (Module)new Scaffold("Scaffold", "Places blocks for you", Category.PLAYER, 0), 
          (Module)new CustomSpeed("CustomSpeed", "Custom speed bypasses?", Category.MOVEMENT, 0), (Module)new PVPBot("PvPBot", "testing", Category.COMBAT, 0) });
  }
  
  public static final class UI {
    public static final ClickUI CLICK = new ClickUI();
    
    public static final AltUI ALT = new AltUI();
  }
  
  public static final class Data {
    public static String lastAlt;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\Manager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */