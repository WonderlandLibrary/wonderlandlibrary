/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese;

import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.people.EnemyManager;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.properties.PropertyManager;
import cow.milkgod.cheese.ui.altmanager.AltManager;
import cow.milkgod.cheese.ui.tabgui.TabGui;
import cow.milkgod.cheese.utils.CheckKey;
import cow.milkgod.cheese.utils.Utils;
import cow.milkgod.cheese.utils.Wrapper;
import java.io.PrintStream;
import org.lwjgl.opengl.Display;

public class Cheese {
    public static String Client_Name = "Tomato";
    public static double Client_Version = 0.2;
    public static Cheese Cheese = new Cheese();
    public static CommandManager commandManager;
    public static ModuleManager moduleManager;
    public static EnemyManager enemyManager;
    public static FriendManager friendManager;
    public static String prefix;
    public static Utils utils;
    private static Wrapper wrap;
    private static CheckKey ck;
    public static PropertyManager propertyManager;
    public static AltManager altManager;
    private static FileManager.CustomFile customFile;
    public static FileManager fileManager;
    public static TabGui tabGui;

    static {
        prefix = "-";
        Cheese = new Cheese();
    }

    public Cheese() {
        utils = new Utils();
        wrap = new Wrapper();
        ck = new CheckKey();
    }

    public static void startClient() {
        fileManager = new FileManager();
        moduleManager = new ModuleManager();
        enemyManager = new EnemyManager();
        friendManager = new FriendManager();
        commandManager = new CommandManager();
        propertyManager = new PropertyManager();
        tabGui = new TabGui();
        fileManager.loadFiles();
        Display.setTitle(String.valueOf(String.valueOf(Cheese.getClient_Name())) + " " + Cheese.getClient_Version());
        System.out.println("Loaded Tomato!");
    }
    public static String getClient_Name() {
        return Client_Name;
    }

    public static Cheese getInstance() {
        return Cheese;
    }

    public static void setClient_Name(String client_Name) {
        Client_Name = client_Name;
    }

    public static double getClient_Version() {
        return Client_Version;
    }

    public static void setClient_Version(double client_Version) {
        Client_Version = client_Version;
    }
}

