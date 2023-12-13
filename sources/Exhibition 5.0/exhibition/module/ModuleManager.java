// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module;

import java.util.jar.JarEntry;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.InputStream;
import java.util.jar.JarInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import exhibition.management.SubFolder;
import java.io.File;
import exhibition.Client;
import exhibition.module.impl.combat.AutoSword;
import exhibition.module.impl.msgo.Aimbot;
import exhibition.module.impl.player.Effects;
import exhibition.module.impl.other.AutoTPA;
import exhibition.module.impl.other.BedFucker;
import exhibition.module.impl.movement.Phase;
import exhibition.module.impl.hud.BubbleGui;
import exhibition.module.impl.hud.TabGUI;
import exhibition.module.impl.render.Chams;
import exhibition.module.impl.other.KopyKatt;
import exhibition.module.impl.player.AutoRespawn;
import exhibition.module.impl.other.AutoSay;
import exhibition.module.impl.hud.Radar;
import exhibition.module.impl.render.ESP2D;
import exhibition.module.impl.combat.AutoPot;
import exhibition.module.impl.movement.NoSlowdown;
import exhibition.module.impl.player.NoRotate;
import exhibition.module.impl.movement.Speed;
import exhibition.module.impl.combat.Killaura;
import exhibition.module.impl.render.Crosshair;
import exhibition.module.impl.player.InventoryWalk;
import exhibition.module.impl.combat.AutoArmor;
import exhibition.module.impl.hud.ArmorStatus;
import exhibition.module.impl.render.Nametags;
import exhibition.module.impl.player.AutoEat;
import exhibition.module.impl.hud.Enabled;
import exhibition.module.impl.other.ChatCommands;
import exhibition.module.impl.player.FastPlace;
import exhibition.module.impl.combat.AntiVelocity;
import exhibition.module.impl.render.Xray;
import exhibition.management.keybinding.KeyMask;
import exhibition.module.impl.movement.Sprint;
import exhibition.module.impl.render.ChestESP;
import exhibition.module.impl.combat.AutoSoup;
import exhibition.module.impl.msgo.AntiAim;
import exhibition.module.impl.movement.Fly;
import exhibition.module.impl.other.ChestStealer;
import exhibition.module.impl.player.FastUse;
import exhibition.module.impl.movement.Jesus;
import exhibition.module.impl.movement.DepthStrider;
import exhibition.module.impl.combat.AntiBot;
import exhibition.module.impl.movement.LongJump;
import exhibition.module.impl.render.Freecam;
import exhibition.module.impl.player.NoFall;
import exhibition.module.impl.other.AntiVanish;
import exhibition.module.impl.player.Scaffold;
import exhibition.module.impl.player.AutoTool;
import exhibition.module.impl.render.DONOTFUCKINGDIEYOURETARD;
import exhibition.module.impl.combat.AimAssist;
import exhibition.module.impl.combat.AutoClicker;
import exhibition.module.impl.other.DeathClip;
import exhibition.module.impl.render.Lines;
import exhibition.module.impl.player.ItemSpoof;
import exhibition.module.impl.other.FriendAlert;
import exhibition.module.impl.combat.BowAimbot;
import exhibition.module.impl.render.Brightness;
import exhibition.module.impl.player.SpeedMine;
import exhibition.module.impl.combat.Criticals;
import exhibition.module.impl.movement.Bhop;
import exhibition.module.impl.other.PingSpoof;
import exhibition.module.impl.render.Outline;
import exhibition.module.impl.render.Tags;
import exhibition.module.impl.player.Teleport;
import exhibition.module.impl.movement.Step;
import exhibition.module.impl.other.Timer;
import exhibition.module.data.ModuleData;
import exhibition.management.AbstractManager;

public class ModuleManager<E extends Module> extends AbstractManager<Module>
{
    private boolean setup;
    
    public ModuleManager(final Class<Module> clazz) {
        super(clazz, 0);
    }
    
    @Override
    public void setup() {
        this.loadLocalPlugins();
        this.add(new Timer(new ModuleData(ModuleData.Type.Other, "Timer", "Modifies game speed. Can be used to 'bow fly'.")));
        this.add(new Step(new ModuleData(ModuleData.Type.Movement, "Step", "Moves you up one block.")));
        this.add(new Teleport(new ModuleData(ModuleData.Type.Player, "ClickBlink", "Teleports you to the selected block.")));
        this.add(new Tags(new ModuleData(ModuleData.Type.Visuals, "3DTags", "Nametags but 3D.")));
        this.add(new Outline(new ModuleData(ModuleData.Type.Visuals, "Glow", "Makes players glow.")));
        this.add(new PingSpoof(new ModuleData(ModuleData.Type.Other, "PingSpoof", "Chokes KeepAlive packets to fake high ping.")));
        this.add(new Bhop(new ModuleData(ModuleData.Type.Movement, "Bhop", "Bunnyhops for speed.")));
        this.add(new Criticals(new ModuleData(ModuleData.Type.Combat, "Criticals", "Critical attack each hit.")));
        this.add(new SpeedMine(new ModuleData(ModuleData.Type.Player, "SpeedMine", "Mine blocks faster")));
        this.add(new Brightness(new ModuleData(ModuleData.Type.Visuals, "Brightness", "Applies night vision")));
        this.add(new BowAimbot(new ModuleData(ModuleData.Type.Combat, "BowAimbot", "Aims at players & predicts movement.")));
        this.add(new FriendAlert(new ModuleData(ModuleData.Type.Other, "FriendAlert", "Event specific alerts for friends. (Joining/Dying)")));
        this.add(new ItemSpoof(new ModuleData(ModuleData.Type.Player, "ItemSpoof", "Spoofs your item with the top left inv item.")));
        this.add(new Lines(new ModuleData(ModuleData.Type.Visuals, "Lines", "Draws lines at entities.")));
        this.add(new DeathClip(new ModuleData(ModuleData.Type.Other, "DeathClip", "Teleports you on death.")));
        this.add(new AutoClicker(new ModuleData(ModuleData.Type.Combat, "AutoClicker", "Clicks automatically for you.")));
        this.add(new AimAssist(new ModuleData(ModuleData.Type.Combat, "AimAssist", "Aims for you.")));
        this.add(new DONOTFUCKINGDIEYOURETARD(new ModuleData(ModuleData.Type.Visuals, "Health", "Shows your health in the middle of the screen.")));
        this.add(new AutoTool(new ModuleData(ModuleData.Type.Player, "AutoTool", "Switches to best tool.")));
        this.add(new Scaffold(new ModuleData(ModuleData.Type.Movement, "Scaffold", "Silently places blocks.")));
        this.add(new AntiVanish(new ModuleData(ModuleData.Type.Other, "AntiVanish", "Alerts you of vanished staff members.")));
        this.add(new NoFall(new ModuleData(ModuleData.Type.Player, "NoFall", "Take no fall damage.")));
        this.add(new Freecam(new ModuleData(ModuleData.Type.Visuals, "FreeCam", "Allows you to view around in noclip.")));
        this.add(new LongJump(new ModuleData(ModuleData.Type.Movement, "LongJump", "Jump, but longly.")));
        this.add(new AntiBot(new ModuleData(ModuleData.Type.Other, "AntiBot", "Ignores/Removes bots.")));
        this.add(new DepthStrider(new ModuleData(ModuleData.Type.Movement, "DepthStrider", "Swim faster in water.")));
        this.add(new Jesus(new ModuleData(ModuleData.Type.Movement, "Jesus", "Walk on water.")));
        this.add(new FastUse(new ModuleData(ModuleData.Type.Player, "FastUse", "Consume items faster.")));
        this.add(new ChestStealer(new ModuleData(ModuleData.Type.Player, "ChestStealer", "Steal items from chests.")));
        this.add(new Fly(new ModuleData(ModuleData.Type.Movement, "Fly", "Become a bird.")));
        this.add(new AntiAim(new ModuleData(ModuleData.Type.MSGO, "AntiAim", "Derp, essentially.")));
        this.add(new AutoSoup(new ModuleData(ModuleData.Type.Combat, "AutoSoup", "Consumes soups to heal for you.")));
        this.add(new ChestESP(new ModuleData(ModuleData.Type.Visuals, "ChestESP", "Draws a box around chests.")));
        this.add(new Sprint(new ModuleData(ModuleData.Type.Movement, "Sprint", "Automatically sprints for you.")));
        this.add(new Xray(new ModuleData(ModuleData.Type.Visuals, "Xray", "Sends brain waves to blocks.", 45, KeyMask.None)));
        this.add(new AntiVelocity(new ModuleData(ModuleData.Type.Combat, "AntiVelocity", "Reduce/Remove velocity.")));
        this.add(new FastPlace(new ModuleData(ModuleData.Type.Player, "FastPlace", "Place blocks, but fast.")));
        this.add(new ChatCommands(new ModuleData(ModuleData.Type.Other, "Commands", "Commands, but for chat.")));
        this.add(new Enabled(new ModuleData(ModuleData.Type.Visuals, "Enabled", "Your hud.")));
        this.add(new AutoEat(new ModuleData(ModuleData.Type.Player, "AutoEat", "Does /eat for you.")));
        this.add(new Nametags(new ModuleData(ModuleData.Type.Visuals, "2DTags", "Nametags that are rendered in the 2D view.")));
        this.add(new ArmorStatus(new ModuleData(ModuleData.Type.Visuals, "ArmorHUD", "Shows you your armor stats.")));
        this.add(new AutoArmor(new ModuleData(ModuleData.Type.Player, "AutoArmor", "Switches out current armor for best armor.")));
        this.add(new InventoryWalk(new ModuleData(ModuleData.Type.Player, "Inventory", "Walk in inventory + carry extra items.")));
        this.add(new Crosshair(new ModuleData(ModuleData.Type.Visuals, "Crosshair", "Draws a custom crosshair.")));
        this.add(new Killaura(new ModuleData(ModuleData.Type.Combat, "KillAura", "Attacks entities for you.")));
        this.add(new Speed(new ModuleData(ModuleData.Type.Movement, "OnGround", "Speeds that shall never be touched.")));
        this.add(new NoRotate(new ModuleData(ModuleData.Type.Player, "NoRotate", "Prevents the server from forcing head rotations.")));
        this.add(new NoSlowdown(new ModuleData(ModuleData.Type.Movement, "NoSlowdown", "Movement isn't reduced when using an item.")));
        this.add(new AutoPot(new ModuleData(ModuleData.Type.Combat, "AutoPot", "Throws potions to heal for you.")));
        this.add(new ESP2D(new ModuleData(ModuleData.Type.Visuals, "2DESP", "Outlined box ESP that is rendered in the 2D view.")));
        this.add(new Radar(new ModuleData(ModuleData.Type.Visuals, "Radar", "Shows you all the players around you.")));
        this.add(new AutoSay(new ModuleData(ModuleData.Type.Other, "AutoSay", "Says what ever you set the string to for you.")));
        this.add(new AutoRespawn(new ModuleData(ModuleData.Type.Player, "AutoRespawn", "Respawns you after you've died.")));
        this.add(new KopyKatt(new ModuleData(ModuleData.Type.Other, "KopyKatt", "Copies what the selected target says. Use '.cc' to select the target.")));
        this.add(new Chams(new ModuleData(ModuleData.Type.Visuals, "Chams", "Doesn't work.")));
        this.add(new TabGUI(new ModuleData(ModuleData.Type.Visuals, "TabGUI", "TabGUI.")));
        this.add(new BubbleGui(new ModuleData(ModuleData.Type.Visuals, "BubbleGUI", "Credits to DoubleParallax.")));
        this.add(new Phase(new ModuleData(ModuleData.Type.Movement, "Phase", "Clip through blocks.")));
        this.add(new BedFucker(new ModuleData(ModuleData.Type.Other, "BedFucker", "Breaks beds around you.")));
        this.add(new AutoTPA(new ModuleData(ModuleData.Type.Other, "AutoTPA", "Auto tp accepts for you.")));
        this.add(new Effects(new ModuleData(ModuleData.Type.Player, "Effects", "Removes harmful potion effects & fire.")));
        this.add(new Aimbot(new ModuleData(ModuleData.Type.MSGO, "Ragebot", "Minestrike/Cops & Crims ragebot.")));
        this.add(new AutoSword(new ModuleData(ModuleData.Type.Combat, "AutoSword", "Swords automatically woah how cool.")));
        this.setup = true;
        Module.loadStatus();
        if (!this.get(ChatCommands.class).isEnabled()) {
            this.get(ChatCommands.class).toggle();
        }
    }
    
    private void loadLocalPlugins() {
        final String basePath = Client.getDataDir().getAbsolutePath();
        final String newPath = basePath + (basePath.endsWith(File.separator) ? SubFolder.ModuleJars.getFolderName() : (File.separator + SubFolder.ModuleJars.getFolderName()));
        final File test = new File(newPath);
        if (!test.exists()) {
            test.mkdirs();
        }
        for (final File file : test.listFiles()) {
            if (file.getAbsolutePath().endsWith(".jar")) {
                try {
                    this.loadJar(file);
                    System.out.println(file.getAbsoluteFile().getName() + " has been successfully loaded!");
                }
                catch (IOException e) {
                    System.out.println("IOException thrown! -- Error loading Plugin.");
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void loadJar(final File file) throws IOException {
        final JarInputStream jis = new JarInputStream(new FileInputStream(file));
        final URLClassLoader urlLoader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
        for (JarEntry jarEntry = jis.getNextJarEntry(); jarEntry != null; jarEntry = jis.getNextJarEntry()) {
            if (!jarEntry.isDirectory()) {
                if (jarEntry.getName().endsWith(".class")) {
                    final String className = jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - ".class".length());
                    if (!className.contains("$")) {
                        try {
                            final Class<?> classs = urlLoader.loadClass(className);
                            if (Module.class.isAssignableFrom(classs)) {
                                this.add((Module)classs.newInstance());
                            }
                        }
                        catch (ReflectiveOperationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        jis.close();
        urlLoader.close();
    }
    
    public boolean isSetup() {
        return this.setup;
    }
    
    public boolean isEnabled(final Class<? extends Module> clazz) {
        final Module module = this.get(clazz);
        return module != null && module.isEnabled();
    }
    
    public Module get(final String name) {
        for (final Module module : this.getArray()) {
            if (module.getName().toLowerCase().equals(name.toLowerCase())) {
                return module;
            }
        }
        return null;
    }
}
