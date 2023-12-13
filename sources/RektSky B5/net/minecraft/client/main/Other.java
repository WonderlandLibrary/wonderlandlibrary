/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.lang.reflect.Type;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.LaunchConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.util.Session;
import tk.rektsky.utils.RektSkyUtil;

public class Other {
    public static String[] arguments;

    public static void start(LaunchConfiguration config) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        String proxyHost = config.getProxyHost();
        final String proxyUser = config.getProxyUser();
        final String proxyPassword = config.getProxyPassword();
        Proxy proxy = Proxy.NO_PROXY;
        if (proxyHost != null) {
            try {
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, (int)config.getProxyPort()));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (!proxy.equals(Proxy.NO_PROXY) && Other.isNullOrEmpty(proxyUser) && Other.isNullOrEmpty(proxyPassword)) {
            Authenticator.setDefault(new Authenticator(){

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
            });
        }
        int width = config.getWidth();
        int height = config.getHeight();
        boolean fullscreen = config.isFullscreen();
        boolean checkGlErrors = config.isCheckGlErrors();
        boolean demo = config.isDemo();
        String version = config.getVersion();
        Gson gson = new GsonBuilder().registerTypeAdapter((Type)((Object)PropertyMap.class), new PropertyMap.Serializer()).create();
        PropertyMap userProperties = config.getUserProperties();
        PropertyMap profileProperties = config.getProfileProperties();
        File gameDir = config.getGameDir();
        File assetsDir = config.getAssetsDir();
        File resourcePackDir = config.getResourcePackDir();
        String username = config.getUsername();
        String uuid = config.getUuid() == null ? username : config.getUuid();
        String assetIndex = config.getAssetIndex();
        String server = config.getServer();
        Integer port = config.getPort();
        Session session = new Session(username, uuid, config.getAccessToken(), config.getUserType());
        GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, userProperties, profileProperties, proxy), new GameConfiguration.DisplayInformation(width, height, fullscreen, checkGlErrors), new GameConfiguration.FolderInformation(gameDir, resourcePackDir, assetsDir, assetIndex), new GameConfiguration.GameInformation(demo, version), new GameConfiguration.ServerInformation(server, port));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread"){

            @Override
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        new Minecraft(gameconfiguration).run();
    }

    public static void start(String[] args) {
        OptionParser optionparser = new OptionParser();
        optionparser.allowsUnrecognizedOptions();
        optionparser.accepts("demo");
        optionparser.accepts("fullscreen");
        optionparser.accepts("checkGlErrors");
        ArgumentAcceptingOptionSpec<String> serverOption = optionparser.accepts("server").withRequiredArg();
        ArgumentAcceptingOptionSpec<Integer> portOption = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565, (Integer[])new Integer[0]);
        ArgumentAcceptingOptionSpec<File> gameDirOption = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), (File[])new File[0]);
        ArgumentAcceptingOptionSpec<File> assetsDirOption = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec<String> assetIndexOption = optionparser.accepts("assetIndex").withRequiredArg();
        ArgumentAcceptingOptionSpec<File> resourcePackDirOption = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec<String> proxyHostOption = optionparser.accepts("proxyHost").withRequiredArg();
        ArgumentAcceptingOptionSpec<Integer> proxyPortOption = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", (String[])new String[0]).ofType(Integer.class);
        ArgumentAcceptingOptionSpec<String> proxyUserOption = optionparser.accepts("proxyUser").withRequiredArg();
        ArgumentAcceptingOptionSpec<String> proxyPasswordOption = optionparser.accepts("proxyPass").withRequiredArg();
        ArgumentAcceptingOptionSpec<String> versionOption = optionparser.accepts("version").withRequiredArg().defaultsTo("RELEASE", (String[])new String[0]);
        ArgumentAcceptingOptionSpec<Integer> widthOption = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854, (Integer[])new Integer[0]);
        ArgumentAcceptingOptionSpec<Integer> heightOption = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480, (Integer[])new Integer[0]);
        ArgumentAcceptingOptionSpec<String> usernameOption = optionparser.accepts("username").withRequiredArg().defaultsTo(RektSkyUtil.genRandomString(13), (String[])new String[0]);
        ArgumentAcceptingOptionSpec<String> uuidOption = optionparser.accepts("uuid").withRequiredArg();
        ArgumentAcceptingOptionSpec<String> accessTokenOption = optionparser.accepts("accessToken").withRequiredArg().required();
        ArgumentAcceptingOptionSpec<String> userPropertiesOption = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}", (String[])new String[0]);
        ArgumentAcceptingOptionSpec<String> profilePropertiesOption = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}", (String[])new String[0]);
        ArgumentAcceptingOptionSpec<String> userTypeOption = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy", (String[])new String[0]);
        NonOptionArgumentSpec<String> nonOptions = optionparser.nonOptions();
        OptionSet optionset = optionparser.parse(args);
        List<String> list = optionset.valuesOf(nonOptions);
        if (!list.isEmpty()) {
            System.out.println("Completely ignored arguments: " + list);
        }
        System.setProperty("java.net.preferIPv4Stack", "true");
        String proxyHost = optionset.valueOf(proxyHostOption);
        final String proxyUser = optionset.valueOf(proxyUserOption);
        final String proxyPassword = optionset.valueOf(proxyPasswordOption);
        Proxy proxy = Proxy.NO_PROXY;
        if (proxyHost != null) {
            try {
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, (int)((Integer)proxyPortOption.value(optionset))));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (!proxy.equals(Proxy.NO_PROXY) && Other.isNullOrEmpty(proxyUser) && Other.isNullOrEmpty(proxyPassword)) {
            Authenticator.setDefault(new Authenticator(){

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
            });
        }
        int width = optionset.valueOf(widthOption);
        int height = optionset.valueOf(heightOption);
        boolean fullscreen = optionset.has("fullscreen");
        boolean checkGlErrors = optionset.has("checkGlErrors");
        boolean demo = optionset.has("demo");
        String version = optionset.valueOf(versionOption);
        Gson gson = new GsonBuilder().registerTypeAdapter((Type)((Object)PropertyMap.class), new PropertyMap.Serializer()).create();
        PropertyMap userProperties = gson.fromJson(optionset.valueOf(userPropertiesOption), PropertyMap.class);
        PropertyMap profileProperties = gson.fromJson(optionset.valueOf(profilePropertiesOption), PropertyMap.class);
        File gameDir = optionset.valueOf(gameDirOption);
        File assetsDir = optionset.has(assetsDirOption) ? optionset.valueOf(assetsDirOption) : new File(gameDir, "assets/");
        File resourcePackDir = optionset.has(resourcePackDirOption) ? optionset.valueOf(resourcePackDirOption) : new File(gameDir, "resourcepacks/");
        String username = "";
        try {
            username = (String)usernameOption.value(optionset);
        }
        catch (Exception ex) {
            List l2 = usernameOption.values(optionset);
            username = (String)l2.get(1);
        }
        username = "Reeker" + RektSkyUtil.genRandomString(10);
        String uuid = optionset.has(uuidOption) ? (String)uuidOption.value(optionset) : username;
        String assetIndex = optionset.has(assetIndexOption) ? (String)assetIndexOption.value(optionset) : null;
        String server = optionset.valueOf(serverOption);
        Integer port = optionset.valueOf(portOption);
        Session session = new Session(username, uuid, (String)accessTokenOption.value(optionset), (String)userTypeOption.value(optionset));
        GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, userProperties, profileProperties, proxy), new GameConfiguration.DisplayInformation(width, height, fullscreen, checkGlErrors), new GameConfiguration.FolderInformation(gameDir, resourcePackDir, assetsDir, assetIndex), new GameConfiguration.GameInformation(demo, version), new GameConfiguration.ServerInformation(server, port));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread"){

            @Override
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        new Minecraft(gameconfiguration).run();
    }

    public static void main(String[] args) {
        Other.start(args);
    }

    private static boolean isNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}

