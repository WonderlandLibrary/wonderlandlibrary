package net.minecraft.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import koks.Koks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.lwjgl.LWJGLException;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;

public class Main {
    public static void main(String[] p_main_0_) throws LWJGLException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser optionparser = new OptionParser();
        optionparser.allowsUnrecognizedOptions();
        optionparser.accepts("demo");
        optionparser.accepts("fullscreen");
        optionparser.accepts("checkGlErrors");
        OptionSpec<String> server = optionparser.accepts("server").withRequiredArg();
        OptionSpec<Integer> port = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
        OptionSpec<File> gameDir = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        OptionSpec<File> assetsDir = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        OptionSpec<File> resourcePackDir = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        OptionSpec<String> proxyHost = optionparser.accepts("proxyHost").withRequiredArg();
        OptionSpec<Integer> proxyPort = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
        OptionSpec<String> proxyUser = optionparser.accepts("proxyUser").withRequiredArg();
        OptionSpec<String> proxyPass = optionparser.accepts("proxyPass").withRequiredArg();
        OptionSpec<String> username = optionparser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L);
        OptionSpec<String> uuid = optionparser.accepts("uuid").withRequiredArg();
        OptionSpec<String> accessToken = optionparser.accepts("accessToken").withRequiredArg().required();
        OptionSpec<String> version = optionparser.accepts("version").withRequiredArg().required();
        OptionSpec<Integer> width = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        OptionSpec<Integer> height = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        OptionSpec<String> userProperties = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> profileProperties = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> assetIndex = optionparser.accepts("assetIndex").withRequiredArg();
        OptionSpec<String> userType = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy");
        OptionSpec<String> nonOptions = optionparser.nonOptions();
        OptionSet parse = optionparser.parse(p_main_0_);
        List<String> arguments = parse.valuesOf(nonOptions);

        if (!arguments.isEmpty()) {
            System.out.println("Completely ignored arguments: " + arguments);
        }

        String proxyHostValue = parse.valueOf(proxyHost);
        Proxy proxy = Proxy.NO_PROXY;

        if (proxyHostValue != null) {
            try {
                proxy = new Proxy(Type.SOCKS, new InetSocketAddress(proxyHostValue, parse.valueOf(proxyPort)));
            } catch (Exception ignored) {
            }
        }

        final String proxyUserValue = parse.valueOf(proxyUser);
        final String proxyPassValue = parse.valueOf(proxyPass);

        if (!proxy.equals(Proxy.NO_PROXY) && isNullOrEmpty(proxyUserValue) && isNullOrEmpty(proxyPassValue)) {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUserValue, proxyPassValue.toCharArray());
                }
            });
        }

        int widthValue = parse.valueOf(width);
        int heightValue = parse.valueOf(height);
        boolean fullscreen = parse.has("fullscreen");
        boolean checkGlErrors = parse.has("checkGlErrors");
        boolean demo = parse.has("demo");
        String versionValue = parse.valueOf(version);
        Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new Serializer()).create();
        PropertyMap userPropertiesMap = gson.fromJson(parse.valueOf(userProperties), PropertyMap.class);
        PropertyMap profilePropertiesMap = gson.fromJson(parse.valueOf(profileProperties), PropertyMap.class);
        File gameDirFile = parse.valueOf(gameDir);
        File assetsDirFile = parse.has(assetsDir) ? parse.valueOf(assetsDir) : new File(gameDirFile, "assets/");
        File resourcePackDirFile = parse.has(resourcePackDir) ? parse.valueOf(resourcePackDir) : new File(gameDirFile, "resourcepacks/");
        String uuidValue = parse.has(uuid) ? uuid.value(parse) : username.value(parse);
        String assetsIndexValue = parse.has(assetIndex) ? assetIndex.value(parse) : null;
        String serverValue = parse.valueOf(server);
        Integer portValue = parse.valueOf(port);

        String name = username.value(parse);
        if (name.length() > 16 || Koks.isDeveloperMode()) {
            String environment = System.getenv("KoksUsername");
            if (environment == null) {
                System.err.println("No Username set please execute the setUsername batch script");
                System.exit(0);
            }
            if (Koks.isDeveloperMode())
                Koks.offlineMode = name.equalsIgnoreCase("offline");
            Koks.clName = environment;
        } else {
            Koks.clName = name;
        }

        Session session = new Session(Koks.clName, uuidValue, accessToken.value(parse), userType.value(parse));

        GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, userPropertiesMap, profilePropertiesMap, proxy), new GameConfiguration.DisplayInformation(widthValue, heightValue, fullscreen, checkGlErrors), new GameConfiguration.FolderInformation(gameDirFile, resourcePackDirFile, assetsDirFile, assetsIndexValue), new GameConfiguration.GameInformation(demo, versionValue), new GameConfiguration.ServerInformation(serverValue, portValue));

        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        (new Minecraft(gameconfiguration, !ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("Xverify"))).run();
    }

    private static boolean isNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
