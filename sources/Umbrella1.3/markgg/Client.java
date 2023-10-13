/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package markgg;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import markgg.alts.AltManager;
import markgg.command.CommandManager;
import markgg.events.Event;
import markgg.events.listeners.EventChat;
import markgg.events.listeners.EventKey;
import markgg.extensions.DiscordRP;
import markgg.modules.Module;
import markgg.modules.combat.AntiBot;
import markgg.modules.combat.KillAura;
import markgg.modules.combat.TargetStrafe;
import markgg.modules.combat.Velocity;
import markgg.modules.ghost.AimAssist;
import markgg.modules.ghost.AntiAFK;
import markgg.modules.ghost.AutoClicker;
import markgg.modules.ghost.NoHitDelay;
import markgg.modules.ghost.Panic;
import markgg.modules.ghost.WTap;
import markgg.modules.movement.AirJump;
import markgg.modules.movement.FastLadder;
import markgg.modules.movement.Fly;
import markgg.modules.movement.InvMove;
import markgg.modules.movement.NoBob;
import markgg.modules.movement.NoWeb;
import markgg.modules.movement.Scaffold;
import markgg.modules.movement.Sneak;
import markgg.modules.movement.Speed;
import markgg.modules.movement.Sprint;
import markgg.modules.movement.Step;
import markgg.modules.player.AntiFire;
import markgg.modules.player.AntiVoid;
import markgg.modules.player.AutoRespawn;
import markgg.modules.player.Breaker;
import markgg.modules.player.ChestStealer;
import markgg.modules.player.Disabler;
import markgg.modules.player.FastBreak;
import markgg.modules.player.FastPlace;
import markgg.modules.player.NoFall;
import markgg.modules.player.NoSlow;
import markgg.modules.player.Regen;
import markgg.modules.player.Spammer;
import markgg.modules.player.Timer;
import markgg.modules.render.ClickGUI;
import markgg.modules.render.FullBright;
import markgg.modules.render.HUD2;
import markgg.modules.render.NoGuiClose;
import markgg.modules.render.Rotations;
import markgg.modules.render.XRay;
import markgg.ui.HUD;
import markgg.utilities.font.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

public class Client {
    public static String name = "Umbrella";
    public static String version = "1.3";
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList();
    public static HUD hud = new HUD();
    public static CommandManager commandManager = new CommandManager();
    public static CustomFontRenderer customFont;
    public static CustomFontRenderer customFontBig;
    public static CustomFontRenderer customFontHuge;
    public static AltManager altManager;
    public static DiscordRP discordRP;
    public String[] addedalts;

    static {
        discordRP = new DiscordRP();
    }

    public static void startup() {
        altManager = new AltManager();
        discordRP.start();
        Display.setTitle((String)(String.valueOf(name) + " " + version + " | Made by MarkGG#8181"));
        modules.add(new AntiBot());
        modules.add(new KillAura());
        modules.add(new TargetStrafe());
        modules.add(new Velocity());
        modules.add(new AimAssist());
        modules.add(new AntiAFK());
        modules.add(new AutoClicker());
        modules.add(new NoHitDelay());
        modules.add(new Panic());
        modules.add(new WTap());
        modules.add(new AirJump());
        modules.add(new Fly());
        modules.add(new NoWeb());
        modules.add(new FastLadder());
        modules.add(new InvMove());
        modules.add(new NoBob());
        modules.add(new Scaffold());
        modules.add(new Sneak());
        modules.add(new Speed());
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new AntiFire());
        modules.add(new Breaker());
        modules.add(new FastBreak());
        modules.add(new NoSlow());
        modules.add(new Timer());
        modules.add(new AntiVoid());
        modules.add(new AutoRespawn());
        modules.add(new ChestStealer());
        modules.add(new Disabler());
        modules.add(new FastPlace());
        modules.add(new NoFall());
        modules.add(new Spammer());
        modules.add(new Regen());
        modules.add(new ClickGUI());
        modules.add(new FullBright());
        modules.add(new HUD2());
        modules.add(new NoGuiClose());
        modules.add(new Rotations());
        modules.add(new XRay());
        Minecraft.getMinecraft().gameSettings.guiScale = 2;
        customFont = new CustomFontRenderer(new Font("Arial", 0, 18), true, true);
        customFontBig = new CustomFontRenderer(new Font("Arial", 0, 24), true, true);
        customFontHuge = new CustomFontRenderer(new Font("Arial", 0, 48), true, true);
    }

    public static DiscordRP getDiscordRP() {
        return discordRP;
    }

    public static void onEvent(Event e) {
        if (e instanceof EventChat) {
            commandManager.handleChat((EventChat)e);
        }
        for (Module m : modules) {
            if (!m.toggled) continue;
            m.onEvent(e);
        }
    }

    public static void keyPress(int key) {
        Client.onEvent(new EventKey(key));
        for (Module m : modules) {
            if (m.getKey() != key) continue;
            m.toggle();
        }
    }

    public static List<Module> getModulesByCategory(Module.Category c) {
        ArrayList<Module> modules = new ArrayList<Module>();
        for (Module m : Client.modules) {
            if (m.category != c) continue;
            modules.add(m);
        }
        return modules;
    }

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.name.equals(name)) continue;
            return m;
        }
        return null;
    }

    public static boolean isModuleToggled(String name) {
        for (Module m : modules) {
            if (m.name != name || !m.toggled) continue;
            return true;
        }
        return false;
    }

    public static void addChatMessage(String message) {
        message = "\u00a7d" + name + "\u00a77 - " + message;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void addClearChatMessage() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(""));
    }
}

