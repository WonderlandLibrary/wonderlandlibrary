/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package me.arithmo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.ssl.internal.ssl.Provider;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import me.arithmo.gui.altmanager.FileManager;
import me.arithmo.gui.click.ClickGui;
import me.arithmo.gui.screen.impl.mainmenu.ClientMainMenu;
import me.arithmo.management.ColorManager;
import me.arithmo.management.FontManager;
import me.arithmo.management.command.CommandManager;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.management.notifications.Notifications;
import me.arithmo.management.waypoints.WaypointManager;
import me.arithmo.module.Module;
import me.arithmo.module.ModuleManager;
import me.arithmo.util.render.NharFont;
import me.arithmo.util.render.TTFFontRenderer;
import me.arithmo.util.security.SSLUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class Client {
    public static Client instance;
    public static final String author = "Arithmo";
    public static final String version = "1.60";
    private static final int VERSION_CHECK = 34646;
    public static final String clientName = "Sigma";
    public static ColorManager cm;
    private final ModuleManager moduleManager;
    private static FileManager fileManager;
    private static ClickGui clickGui;
    public static CommandManager commandManager;
    private File dataDirectory;
    private GuiScreen mainMenu = new ClientMainMenu();
    private boolean isHidden;
    public static NharFont f;
    public static NharFont fs;
    public static NharFont fss;
    public static NharFont header;
    public static NharFont subHeader;
    public static FontManager fm;
    public static WaypointManager wm;
    public static TTFFontRenderer testFont;
    public static boolean outdated;

    public static FileManager getFileManager() {
        return fileManager;
    }

    public Client() {
        commandManager = new CommandManager();
        instance = this;
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        this.moduleManager = new ModuleManager(Module.class);
        FriendManager.start();
    }

    public static ClickGui getClickGui() {
        return clickGui;
    }

    public void setup() {
        commandManager.setup();
        this.dataDirectory = new File("Sigma");
        this.moduleManager.setup();
        fileManager = new FileManager();
        fileManager.loadFiles();
        clickGui = new ClickGui();
        f = new NharFont("SF UI Display Bold", 10.0f);
        fs = new NharFont("SF UI Display Thin", 10.0f);
        fss = new NharFont("SF UI Display Regular", 12.0f);
        header = new NharFont("SF UI Display Semibold", 10.0f);
        subHeader = new NharFont("SF UI Display Light", 10.0f);
//        try {
//            String inputLine;
//            SSLUtilities.trustAllHostnames();
//            SSLUtilities.trustAllHttpsCertificates();
//            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
//            Security.addProvider(new Provider());
//            HostnameVerifier allHostsValid = (hostname, session) -> true;
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
//            URL url = new URL("https://www.sigmaclient.info/update-check/6");
//            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
//            connection.setRequestProperty("User-Agent", "ArthimoWareTM-Agent");
//            connection.connect();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder str = new StringBuilder();
//            while ((inputLine = reader.readLine()) != null) {
//                str.append(inputLine);
//            }
//            Gson gson = new Gson();
//            JsonObject array = (JsonObject)gson.fromJson(str.toString(), JsonObject.class);
//            if (array.get("Version").getAsInt() > 6) {
//                Notifications.getManager().post("Outdated Version", "Please update the client.", 150000, Notifications.Type.WARNING);
//                Notifications.getManager().post("Client Warning", "Client will close in 5 minutes!", 10000, Notifications.Type.WARNING);
//                System.out.println("Outdated...");
//                outdated = true;
//            }
//            reader.close();
//        }
//        catch (Exception e) {
//            throw new RuntimeException();
//        }
    }

    public static ModuleManager<Module> getModuleManager() {
        return Client.instance.moduleManager;
    }

    public static File getDataDir() {
        return Client.instance.dataDirectory;
    }

    public static boolean isHidden() {
        return Client.instance.isHidden;
    }

    public static void setHidden(boolean hidden) {
        Client.instance.isHidden = hidden;
        Client.instance.mainMenu = hidden ? new GuiMainMenu() : new ClientMainMenu();
    }

    public int getVERSION_CHECK() {
        return 6;
    }

    static {
        cm = new ColorManager();
        fm = new FontManager();
        wm = new WaypointManager();
    }
}

