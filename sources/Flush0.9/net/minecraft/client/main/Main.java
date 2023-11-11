package net.minecraft.client.main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;
import me.nyan.flush.Flush;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.Security;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Security.setProperty("crypto.policy", "unlimited");
        FlatDarkLaf.setup();

        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.accepts("demo");
        parser.accepts("fullscreen");
        parser.accepts("checkGlErrors");
        OptionSpec<String> server = parser.accepts("server").withRequiredArg();
        OptionSpec<Integer> port = parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
        OptionSpec<File> gameDir = parser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        OptionSpec<File> assetsDir = parser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        OptionSpec<File> resourcePackDir = parser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        OptionSpec<String> proxyHost = parser.accepts("proxyHost").withRequiredArg();
        OptionSpec<Integer> proxyPort = parser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
        OptionSpec<String> proxyUser = parser.accepts("proxyUser").withRequiredArg();
        OptionSpec<String> proxyPass = parser.accepts("proxyPass").withRequiredArg();
        OptionSpec<String> username = parser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L);
        OptionSpec<String> uuid = parser.accepts("uuid").withRequiredArg();
        OptionSpec<String> accessToken = parser.accepts("accessToken").withRequiredArg().required();
        OptionSpec<String> version = parser.accepts("version").withRequiredArg().required();
        OptionSpec<Integer> widthOption = parser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        OptionSpec<Integer> heightOption = parser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        OptionSpec<String> userProperties = parser.accepts("userProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> profileProperties = parser.accepts("profileProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> assetIndex = parser.accepts("assetIndex").withRequiredArg();
        OptionSpec<String> userType = parser.accepts("userType").withRequiredArg().defaultsTo("legacy");
        OptionSpec<String> login = parser.accepts("login").withRequiredArg().ofType(String.class);

        OptionSpec<String> nonOptions = parser.nonOptions();
        OptionSet optionset = parser.parse(args);
        List<String> list = optionset.valuesOf(nonOptions);

        if (!list.isEmpty()) {
            System.out.println("Completely ignored arguments: " + list);
        }

        String s = optionset.valueOf(proxyHost);
        Proxy proxy = Proxy.NO_PROXY;

        if (s != null) {
            try {
                proxy = new Proxy(Type.SOCKS, new InetSocketAddress(s, optionset.valueOf(proxyPort)));
            } catch (Exception ignored) {

            }
        }

        final String user = optionset.valueOf(proxyUser);
        final String pass = optionset.valueOf(proxyPass);

        if (!proxy.equals(Proxy.NO_PROXY) && isNullOrEmpty(user) && isNullOrEmpty(pass)) {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass.toCharArray());
                }
            });
        }

        int width = optionset.valueOf(widthOption);
        int height = optionset.valueOf(heightOption);
        boolean fullscreen = optionset.has("fullscreen");
        boolean checkGlErrors = optionset.has("checkGlErrors");
        boolean demo = optionset.has("demo");
        String version1 = optionset.valueOf(version);
        Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new Serializer()).create();
        PropertyMap propertymap = gson.fromJson(optionset.valueOf(userProperties), PropertyMap.class);
        PropertyMap propertymap1 = gson.fromJson(optionset.valueOf(profileProperties), PropertyMap.class);
        File gameDir1 = optionset.valueOf(gameDir);
        File assetsDir1 = optionset.has(assetsDir) ? optionset.valueOf(assetsDir) : new File(gameDir1, "assets/");
        File resourcePackDir1 = optionset.has(resourcePackDir) ? optionset.valueOf(resourcePackDir) : new File(gameDir1, "resourcepacks/");
        String uuid1 = optionset.has(uuid) ? uuid.value(optionset) : username.value(optionset);
        String assetsIndex1 = optionset.has(assetIndex) ? assetIndex.value(optionset) : null;
        String serverIp = optionset.valueOf(server);
        Integer serverPort = optionset.valueOf(port);
        Session session = new Session(username.value(optionset), uuid1, accessToken.value(optionset), userType.value(optionset));

        if (optionset.has(login)) {
            Flush.defaultUsername = optionset.valueOf(login);
        }
        GameConfiguration gameconfiguration = new GameConfiguration(
                new GameConfiguration.UserInformation(session, propertymap, propertymap1, proxy),
                new GameConfiguration.DisplayInformation(width, height, fullscreen, checkGlErrors),
                new GameConfiguration.FolderInformation(gameDir1, resourcePackDir1, assetsDir1, assetsIndex1),
                new GameConfiguration.GameInformation(demo, version1),
                new GameConfiguration.ServerInformation(serverIp, serverPort)
        );
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });

        Thread.currentThread().setName("Client thread");
        (new Minecraft(gameconfiguration)).run();
    }

    private static boolean isNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}