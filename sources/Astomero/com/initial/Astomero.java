package com.initial;

import com.initial.configs.*;
import com.initial.modules.impl.visual.*;
import com.initial.login.alt.*;
import net.minecraft.client.multiplayer.*;
import com.initial.discord.*;
import com.initial.commands.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.util.*;
import com.initial.modules.*;
import java.util.*;
import java.io.*;
import com.initial.scriptmanager.*;
import com.initial.events.*;
import net.minecraft.client.entity.*;
import com.initial.events.impl.*;

public class Astomero
{
    public static String getLoginUser;
    public static String client_build;
    public String name;
    public static Astomero instance;
    public Configs configManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ClickGUI clickGui;
    public AltManager altManager;
    public ServerData serverData;
    public ScriptManager scriptManager;
    public File script;
    public File nigger;
    public File dir;
    public File dataDr;
    public File scriptFolder;
    public static DiscordRP discordRP;
    public static CommandManager commandManager;
    
    public Astomero() {
        this.name = "Astomero";
    }
    
    public void start() {
        this.nigger = new File(Minecraft.getMinecraft().mcDataDir, "Astomero");
        if (!this.nigger.exists()) {
            this.nigger.mkdir();
        }
        this.dir = new File(this.nigger + "/configs");
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
        this.scriptFolder = new File(this.nigger + "/scripts");
        if (!this.scriptFolder.exists()) {
            this.scriptFolder.mkdir();
        }
        this.eventManager = new EventManager();
        this.moduleManager = new ModuleManager();
        this.configManager = new Configs();
        this.clickGui = new ClickGUI();
        this.altManager = new AltManager();
        this.scriptManager = new ScriptManager();
        Astomero.discordRP.start();
        Display.setTitle("Minecraft 1.8.8");
        EventManager.register(this);
        this.loadKeys();
    }
    
    public static void onEvent(final EventNigger e) {
        if (e instanceof EventChat) {
            Astomero.commandManager.handleChat((EventChat)e);
        }
    }
    
    public static DiscordRP getDiscordRP() {
        return Astomero.discordRP;
    }
    
    public void shutdown() {
        EventManager.unregister(this);
        this.saveKeys();
    }
    
    @EventTarget
    public void onKey(final EventKey event) {
        this.moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }
    
    public static void addDebugMessage(String message) {
        message = "§f[" + ChatFormatting.RED + "Debug§f] " + message;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
    
    public static void addChatMessage(String message) {
        message = "§f[" + ChatFormatting.RED + "Astomero§f] " + message;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
    
    public void saveKeys() {
        this.dataDr = new File(this.nigger, "keys.json");
        final ArrayList<String> toSave = new ArrayList<String>();
        for (final Module m : Astomero.instance.moduleManager.getModules()) {
            toSave.add(m.getName() + ">" + m.getKey());
        }
        try {
            final PrintWriter pw = new PrintWriter(this.dataDr);
            for (final String str : toSave) {
                pw.println(str);
            }
            pw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void loadKeys() {
        this.dataDr = new File(this.nigger, "keys.json");
        final ArrayList<String> lines = new ArrayList<String>();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(this.dataDr));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (final String s : lines) {
                final String[] args = s.split(">");
                for (final Module m : Astomero.instance.moduleManager.getModules()) {
                    if (m.getName().equalsIgnoreCase(args[0])) {
                        m.setKey(Integer.parseInt(args[1]));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @EventTarget
    public void onTick(final EventTick e) {
        final EntityPlayerSP pl = Minecraft.getMinecraft().thePlayer;
        for (final Script script : this.scriptManager.scripts) {
            script.engineManager.put("localPlayer", pl);
            script.engineManager.put("mc", Minecraft.getMinecraft());
        }
        this.callFunc("onTick", e);
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        this.callFunc("onPre", e);
    }
    
    @EventTarget
    public void onSend(final EventSendPacket e) {
        this.callFunc("onSend", e);
    }
    
    @EventTarget
    public void onGet(final EventReceivePacket e) {
        this.callFunc("onGet", e);
    }
    
    @EventTarget
    public void onDraw(final EventRender2D e) {
        this.callFunc("onDraw", e);
    }
    
    public void callFunc(final String funcName, final Event event) {
        for (final Script script : this.scriptManager.scripts) {
            if (script.scriptModule.isToggled()) {
                script.callEvent(funcName, event);
            }
        }
    }
    
    static {
        Astomero.getLoginUser = "?";
        Astomero.client_build = "210823";
        Astomero.instance = new Astomero();
        Astomero.discordRP = new DiscordRP();
        Astomero.commandManager = new CommandManager();
    }
}
