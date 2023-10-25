package wtf.evolution;

import net.minecraft.client.Minecraft;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.salam4ik.bot.bot.BotStarter;
import wtf.evolution.altmanager.AltManager;
import wtf.evolution.altmanager.Session;
import wtf.evolution.bot.ProxyS;

import wtf.evolution.click.Screen;
import wtf.evolution.command.CommandManager;
import wtf.evolution.editor.Drag;
import wtf.evolution.editor.DragManager;
import wtf.evolution.helpers.ChangeLog;
import wtf.evolution.helpers.FriendManager;
import wtf.evolution.helpers.file.ClickGuiSave;
import wtf.evolution.helpers.render.TPS;
import wtf.evolution.module.Manager;
import wtf.evolution.module.Module;
import wtf.evolution.module.impl.Render.FeatureList;

import wtf.evolution.notifications.NotificationManager;
import wtf.evolution.settings.config.ConfigManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import static wtf.evolution.altmanager.AltManager.file;
import static wtf.evolution.altmanager.AltManager.sessions;
import static wtf.evolution.protect.Protection.genKey;
import static wtf.evolution.protect.Protection.getHwid;

public class Main {
    public static Manager m;
    public static Screen s;

    public static boolean protectedd = false;


    public static Robot imageRobot;
    public static ProxyS proxy = new ProxyS();
    public static HashMap<String, String> nickHash = new HashMap<>();
    public static String apiKey = "f601a7d599e39775a05da33304c958a8"; //"12704f09fe65b4b24bc3e80bf6ba682b";
    public static NotificationManager notify;
    public static ConfigManager c;
    public static AltManager alt;
    public static FriendManager f;
    public static double balance;
    public static CommandManager d = new CommandManager();

    public static String username;
    public static ChangeLog changeLog = new ChangeLog();
    public static int uid;
    public static String till;



    public void load() {

        new Discord().start();
        proxy.start();
        username = net.minecraft.client.main.Main.protection.nickname;
        uid = net.minecraft.client.main.Main.protection.uid;
        till = net.minecraft.client.main.Main.protection.till;
        String hwid = getHwid();
        Document a = null;
        try {
            a = Jsoup.connect("http://89.107.10.34:7777?hwid=" + hwid).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!a.text().equals(genKey(hwid))) {
            System.exit(0);
        }
        alt = new AltManager();
        try {
            imageRobot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        notify = new NotificationManager();
        m = new Manager();
        s = new Screen();
        c = new ConfigManager();
        DragManager.loadDragData();
        c.loadConfig("default");
        FeatureList.modules.addAll(Main.m.m);
        new TPS();
        f = new FriendManager();
        ClickGuiSave.load();
        BotStarter.init();
    }

    public void unload() {
        c.saveConfig("default");
        DragManager.saveDragData();
        try {
            if (file.exists()) file.delete();
            try (FileWriter fr = new FileWriter(file)) {
                for (Session s : sessions) {
                    fr.write(s.nick + "\n");
                }
            }

        } catch (Exception ex) {

        }
    }

    public static BufferedImage getScreenImage() {
        return imageRobot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    public static Drag createDrag(Module module, String name, float x, float y) {
        DragManager.draggables.put(name, new Drag(module, name, x, y));
        return DragManager.draggables.get(name);
    }


    public static void keyEvent(int key) {
        for (Module m : m.m) {
            if (m.bind == key) {
                m.toggle();
            }
        }
    }
}
