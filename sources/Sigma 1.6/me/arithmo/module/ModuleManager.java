/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module;

import java.util.ArrayList;

import me.arithmo.management.AbstractManager;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.impl.combat.AimAssist;
import me.arithmo.module.impl.combat.AntiVelocity;
import me.arithmo.module.impl.combat.AutoArmor;
import me.arithmo.module.impl.combat.AutoPot;
import me.arithmo.module.impl.combat.AutoSoup;
import me.arithmo.module.impl.combat.AutoSword;
import me.arithmo.module.impl.combat.BowAimbot;
import me.arithmo.module.impl.combat.Criticals;
import me.arithmo.module.impl.combat.Killaura;
import me.arithmo.module.impl.combat.Triggerbot;
import me.arithmo.module.impl.hud.ArmorStatus;
import me.arithmo.module.impl.hud.Enabled;
import me.arithmo.module.impl.hud.TabGUI;
import me.arithmo.module.impl.movement.AntiFreeze;
import me.arithmo.module.impl.movement.Bhop;
import me.arithmo.module.impl.movement.Catch;
import me.arithmo.module.impl.movement.DepthStrider;
import me.arithmo.module.impl.movement.Fly;
import me.arithmo.module.impl.movement.Jesus;
import me.arithmo.module.impl.movement.KeepSprint;
import me.arithmo.module.impl.movement.LongJump;
import me.arithmo.module.impl.movement.NoSlowdown;
import me.arithmo.module.impl.movement.Sprint;
import me.arithmo.module.impl.movement.Step;
import me.arithmo.module.impl.other.AntiBot;
import me.arithmo.module.impl.other.AutoSay;
import me.arithmo.module.impl.other.AutoTPA;
import me.arithmo.module.impl.other.BedFucker;
import me.arithmo.module.impl.other.Blink;
import me.arithmo.module.impl.other.ChatCommands;
import me.arithmo.module.impl.other.ChestStealer;
import me.arithmo.module.impl.other.DeathClip;
import me.arithmo.module.impl.other.FriendAlert;
import me.arithmo.module.impl.other.Murder;
import me.arithmo.module.impl.other.PingSpoof;
import me.arithmo.module.impl.other.StreamerMode;
import me.arithmo.module.impl.other.Timer;
import me.arithmo.module.impl.player.AirJump;
import me.arithmo.module.impl.player.AutoRespawn;
import me.arithmo.module.impl.player.AutoTool;
import me.arithmo.module.impl.player.Effects;
import me.arithmo.module.impl.player.FastPlace;
import me.arithmo.module.impl.player.FastUse;
import me.arithmo.module.impl.player.InventoryWalk;
import me.arithmo.module.impl.player.ItemSpoof;
import me.arithmo.module.impl.player.Janitor;
import me.arithmo.module.impl.player.NoFall;
import me.arithmo.module.impl.player.NoRotate;
import me.arithmo.module.impl.player.Scaffold;
import me.arithmo.module.impl.player.SpeedMine;
import me.arithmo.module.impl.player.Teleport;
import me.arithmo.module.impl.render.Brightness;
import me.arithmo.module.impl.render.ChestESP;
import me.arithmo.module.impl.render.Crosshair;
import me.arithmo.module.impl.render.DONOTFUCKINGDIEYOURETARD;
import me.arithmo.module.impl.render.Freecam;
import me.arithmo.module.impl.render.Lines;
import me.arithmo.module.impl.render.Nametags;
import me.arithmo.module.impl.render.Outline;
import me.arithmo.module.impl.render.Waypoints;

public class ModuleManager<E extends Module>
extends AbstractManager<Module> {
	private boolean setup;
	public static ArrayList<Module> mods = new ArrayList();

    public ModuleManager(Class<Module> clazz) {
        super(clazz, 0);
    }

    @Override
    public void setup() {
        this.add(new Catch(new ModuleData(ModuleData.Type.Movement, "Catch", "Prevents you from getting crippled.")));
        this.add(new Janitor(new ModuleData(ModuleData.Type.Player, "Cleaner", "Cleans your inventory from useless items.")));
        this.add(new Timer(new ModuleData(ModuleData.Type.Other, "Timer", "Modifies game speed. Can be used to 'bow fly'.")));
        this.add(new Step(new ModuleData(ModuleData.Type.Movement, "Step", "Allows you to step up faster/more blocks.")));
        this.add(new Teleport(new ModuleData(ModuleData.Type.Player, "ClickBlink", "Teleports you to the selected block.")));
        this.add(new Outline(new ModuleData(ModuleData.Type.Visuals, "Glow", "Makes players glow.")));
        this.add(new PingSpoof(new ModuleData(ModuleData.Type.Other, "PingSpoof", "Chokes KeepAlive packets to fake high ping.")));
        this.add(new Bhop(new ModuleData(ModuleData.Type.Movement, "Speed", "Valk: Gotta go Zooom, ZOooooom!")));
        this.add(new Criticals(new ModuleData(ModuleData.Type.Combat, "Criticals", "Forces critical attack each hit.")));
        this.add(new SpeedMine(new ModuleData(ModuleData.Type.Player, "SpeedMine", "Mine blocks faster")));
        this.add(new Brightness(new ModuleData(ModuleData.Type.Visuals, "Brightness", "Applies permanent night vision")));
        this.add(new BowAimbot(new ModuleData(ModuleData.Type.Combat, "BowAimbot", "Aims at players & predicts movement.")));
        this.add(new FriendAlert(new ModuleData(ModuleData.Type.Other, "FriendAlert", "Event specific alerts for friends. (Joining/Dying)")));
        this.add(new ItemSpoof(new ModuleData(ModuleData.Type.Player, "ItemSpoof", "Spoofs your item with the top left inv item.")));
        this.add(new Lines(new ModuleData(ModuleData.Type.Visuals, "Lines", "Draws lines at entities.")));
//        this.add(new Tracers(new ModuleData(ModuleData.Type.Visuals, "Tracers", "Draws lines at spceific entities, Mobs/Players.")));
        this.add(new DeathClip(new ModuleData(ModuleData.Type.Other, "DeathClip", "Teleports you on death.")));
        this.add(new Triggerbot(new ModuleData(ModuleData.Type.Combat, "Triggerbot", "Clicks when hovering over a player.")));
        this.add(new AimAssist(new ModuleData(ModuleData.Type.Combat, "AimAssist", "Helps you aim better.")));
        this.add(new DONOTFUCKINGDIEYOURETARD(new ModuleData(ModuleData.Type.Visuals, "Health", "Shows your health in the middle of the screen.")));
        this.add(new AutoTool(new ModuleData(ModuleData.Type.Player, "AutoTool", "Switches to best tool.")));
        this.add(new Scaffold(new ModuleData(ModuleData.Type.Movement, "Scaffold", "Silently places blocks below your feets.")));
        //this.add(new AntiVanish(new ModuleData(ModuleData.Type.Other, "AntiVanish", "Alerts you of vanished staff members.")));
        this.add(new NoFall(new ModuleData(ModuleData.Type.Player, "NoFall", "Take no fall damage.")));
        this.add(new Freecam(new ModuleData(ModuleData.Type.Visuals, "Freecam", "Allows you to view around in noclip.")));
        this.add(new LongJump(new ModuleData(ModuleData.Type.Movement, "LongJump", "Jump, but longly.")));
        this.add(new AntiBot(new ModuleData(ModuleData.Type.Combat, "AntiBot", "Ignores/Removes bots.")));
        this.add(new DepthStrider(new ModuleData(ModuleData.Type.Movement, "DepthStrider", "Swim faster in water.")));
        this.add(new Jesus(new ModuleData(ModuleData.Type.Movement, "Jesus", "Walk on water.")));
        this.add(new FastUse(new ModuleData(ModuleData.Type.Player, "FastUse", "Consume items faster.")));
        this.add(new ChestStealer(new ModuleData(ModuleData.Type.Player, "ChestStealer", "Steal items from chests.")));
        this.add(new Fly(new ModuleData(ModuleData.Type.Movement, "Fly", "Become a bird.")));
        this.add(new AutoSoup(new ModuleData(ModuleData.Type.Combat, "AutoSoup", "Consumes soups to heal for you.")));
        this.add(new ChestESP(new ModuleData(ModuleData.Type.Visuals, "ChestESP", "Draws a box around chests.")));
        this.add(new Sprint(new ModuleData(ModuleData.Type.Movement, "Sprint", "Automatically sprints for you.")));
        this.add(new AntiVelocity(new ModuleData(ModuleData.Type.Combat, "AntiVelocity", "Reduce/Remove velocity.")));
        this.add(new FastPlace(new ModuleData(ModuleData.Type.Player, "FastPlace", "Place blocks, but fast.")));
        this.add(new ChatCommands(new ModuleData(ModuleData.Type.Other, "Commands", "Enables '.' commands in Chat.")));
        this.add(new Enabled(new ModuleData(ModuleData.Type.Visuals, "Overlay", "Your hud/In Game GUI. Ex, ArrayList.")));
        this.add(new Nametags(new ModuleData(ModuleData.Type.Visuals, "Nametags", "Lets you see players nametags thru walls.")));
        this.add(new ArmorStatus(new ModuleData(ModuleData.Type.Visuals, "ArmorHUD", "Shows you your armor stats.")));
        this.add(new AutoArmor(new ModuleData(ModuleData.Type.Player, "AutoArmor", "Automatically equips best armor.")));
        this.add(new InventoryWalk(new ModuleData(ModuleData.Type.Player, "Inventory+", "Walk in inventory + carry extra items.")));
        this.add(new Crosshair(new ModuleData(ModuleData.Type.Visuals, "Crosshair", "Draws a custom crosshair, gets bigger when you move.")));
        this.add(new Killaura(new ModuleData(ModuleData.Type.Combat, "KillAura", "Attacks entities for you.")));
        this.add(new NoRotate(new ModuleData(ModuleData.Type.Player, "NoRotate", "Prevents the server from forcing head rotations.")));
        this.add(new NoSlowdown(new ModuleData(ModuleData.Type.Movement, "NoSlowdown", "Movement isn't reduced when using an item.")));
        this.add(new AutoPot(new ModuleData(ModuleData.Type.Combat, "AutoPot", "Throws potions to heal for you.")));
//        this.add(new ESP2D(new ModuleData(ModuleData.Type.Visuals, "2DESP", "Outlined box ESP that is rendered in the 2D view.")));
        //this.add(new Radar(new ModuleData(ModuleData.Type.Visuals, "Radar", "Shows you all the players around you.")));
        this.add(new AutoSay(new ModuleData(ModuleData.Type.Other, "AutoSay", "Says what ever you set the string to for you.")));
        this.add(new AutoRespawn(new ModuleData(ModuleData.Type.Player, "Respawn", "Respawns you after you've died.")));
        //this.add(new Chams(new ModuleData(ModuleData.Type.Visuals, "Chams", "Doesn't work.")));
        this.add(new TabGUI(new ModuleData(ModuleData.Type.Visuals, "TabGUI", "Shows the TabGUI on the side of the screen.")));
        this.add(new BedFucker(new ModuleData(ModuleData.Type.MiniGames, "BedNuker", "Breaks beds around you, might get you banned on Hypixel.")));
        this.add(new AutoTPA(new ModuleData(ModuleData.Type.Other, "AutoAccepts", "Auto accepts invites/requests for you.")));
        this.add(new Effects(new ModuleData(ModuleData.Type.Player, "Zoot", "Removes harmful potion effects & fire.")));
        this.add(new AutoSword(new ModuleData(ModuleData.Type.Combat, "AutoSword", "Automatically equips best sword.")));
        this.add(new KeepSprint(new ModuleData(ModuleData.Type.Movement, "KeepSprint", "Prevents server from setting sprint.")));
        this.add(new AntiFreeze(new ModuleData(ModuleData.Type.Movement, "AntiFreeze", "Sends less packets to unfreeze, might kick you.")));
        this.add(new AirJump(new ModuleData(ModuleData.Type.Movement, "AirJump", "Allows you to jump in Mid-Air, Works on Latest NCP with Damage. Can also be used as a Fly.")));
        this.add(new Murder(new ModuleData(ModuleData.Type.MiniGames, "Murder", "Renders the murderer in murder mystery.")));
        this.add(new StreamerMode(new ModuleData(ModuleData.Type.Other, "Streaming", "Protection for streaming.")));
        this.add(new Blink(new ModuleData(ModuleData.Type.Movement, "Blink", "Holds packets and sends them on disable.")));
        this.add(new Waypoints(new ModuleData(ModuleData.Type.Visuals, "Waypoints", "Renders waypoints, server specific.")));
        if (!((Module)this.get(TabGUI.class)).isEnabled()) {
            ((Module)this.get(TabGUI.class)).toggle();
        }
        this.setup = true;
        Module.loadStatus();
        Module.loadSettings();
        if (((Module)this.get(Blink.class)).isEnabled()) {
            ((Module)this.get(Blink.class)).toggle();
        }
        if (((Module)this.get(Freecam.class)).isEnabled()) {
            ((Module)this.get(Freecam.class)).toggle();
        }
        if (!((Module)this.get(ChatCommands.class)).isEnabled()) {
            ((Module)this.get(ChatCommands.class)).toggle();
        }
        if (!((Module)this.get(Enabled.class)).isEnabled()) {
            ((Module)this.get(Enabled.class)).toggle();
        }
    }
    
    public static <T extends Module> Module findMod(Class<T> clazz)
    {
      for (Module mod : mods) {
        if (mod.getClass() == clazz) {
          return (Module)clazz.cast(mod);
        }
      }
      return null;
    }
    

    public boolean isSetup() {
        return this.setup;
    }

    public boolean isEnabled(Class<? extends Module> clazz) {
        Module module = (Module)this.get(clazz);
        return module != null && module.isEnabled();
    }

    public Module get(String name) {
        for (Module module : (Module[])this.getArray()) {
            if (!module.getName().toLowerCase().equals(name.toLowerCase())) continue;
            return module;
        }
        return null;
    }
}

