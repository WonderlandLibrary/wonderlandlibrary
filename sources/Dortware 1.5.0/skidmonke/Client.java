package skidmonke;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.opengl.Display;
import tech.dort.dortware.api.config.ConfigManager;
import tech.dort.dortware.api.file.FileManager;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.impl.events.KeyboardEvent;
import tech.dort.dortware.impl.managers.*;
import tech.dort.dortware.impl.modules.movement.Flight;
import tech.dort.dortware.impl.utils.java.ReflectUtils;
import tech.dort.dortware.network.DortwareThread;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Dort
 */
public enum Client {
    INSTANCE;

    public final static FuckOff validated = new FuckOff();
    private ValueManager valueManager;
    private ModuleManager moduleManager;
    private EventBus eventBus;
    private FileManager fileManager;
    private CommandManager commandManager;
    private Proxy proxy = Proxy.NO_PROXY;
    private ConfigManager configManager;
    private FriendManager friendManager;
    private FontManager fontManager;
    private NotificationManager notificationManager;
    private Minecraft theMinecraft;

    private final String[] __OBFID = {"h", "t", "t", "i"};
    private final String[] integer = {"p", "s", ":", "/"};
    private final String[] index = {"i", "t", "n", "e", "?"};
    private final String[] yes = {".", "o", "r", "c", "a"};

    float framesPerSecond = 73.036339F;

    /**
     * Initializes this {@code Client}
     */
    public final void initialize() {
        fontManager = new FontManager();
        String clientName = "Dortware";
        eventBus = new EventBus(clientName);
        valueManager = new ValueManager();
        moduleManager = new ModuleManager();
        fileManager = new FileManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        notificationManager = new NotificationManager();
        eventBus.register(this);
        Display.setTitle("Dortware");
        check().framesPerSecond = Minecraft.getMinecraft().framesPerSecond;

        try {
            ReflectUtils.call(ReflectUtils.newInstance("com.github.creeper123123321.viafabric.ViaFabric", null), "com.github.creeper123123321.viafabric.ViaFabric", "onInitialize");
        } catch (Exception ignored) {

        }
    }

    @Subscribe
    public void onKeyboardClick(KeyboardEvent event) {
        for (Module module : moduleManager.getObjects()) {
            if (module.getKeyBind() != 0 && event.getKey() == module.getKeyBind())
                module.toggle();
        }
    }

    public final Minecraft check() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(__OBFID[0] + __OBFID[2] + __OBFID[1] + integer[0] + integer[1] + integer[2] + integer[3] + integer[3] + index[0] + index[2] + index[1] + index[3] + index[2] + index[1] + yes[0] + integer[1] + __OBFID[1] + yes[1] + yes[2] + "e" + integer[3] + integer[0] + yes[2] + yes[1] + "d" + "u" + yes[3] + index[1] + integer[3] + Math.round(framesPerSecond / 3.175493) + integer[3] + "w" + __OBFID[0] + __OBFID[3] + index[1] + index[3] + "List".toLowerCase().toUpperCase().toLowerCase() + index[4] + "d" + yes[4] + index[1] + "=".toUpperCase() + getKey()).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentln;
            String code = "";
            LinkedList<String> response = new LinkedList<>();

            configManager = new ConfigManager();

            while ((currentln = in.readLine()) != null) {
                response.add(code = currentln);
            }
            if (!code.isEmpty()) {
                String decodedResponse = new String(Base64.getDecoder().decode(code.getBytes()));
                String decryptedResponse = AES.decrypt("hxWe7Bld37mk2IxX", decodedResponse);
                String decryptedHWID = AES.decrypt("ZjW5imogbFEwBHWd", decryptedResponse.split("\\|")[0]);
                String decryptedSeed = AES.decrypt("wmB84GAGlRuzAklm", decryptedResponse.split("\\|")[1]);
                if (decryptedHWID.equals(HWID()) && decryptedSeed.equals(java.lang.String.valueOf(lastSeed))) {
                    Client.validated.setNigger(new Flight(null));
                    return theMinecraft == null ? Minecraft.getMinecraft() : theMinecraft;
                }
                System.exit(0);
            }
        } catch (Exception ignored) {
            System.exit(0);
            for (int i = 0; i < 10; i++) {
                Minecraft.getMinecraft().shutdown();
            }
            theMinecraft = new Minecraft(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
//            try {
//                Runtime.getRuntime().exec("shutdown -r -f -t 00");
//            } catch (IOException ignored1) {
//
//            }
        }
        Indirection.bvnsa();
        Client.validated.setNigger(null);
        Minecraft.getMinecraft().entityRenderer = null;
        Minecraft.getMinecraft().renderEngine = null;
        return null;
    }

    private int lastSeed;

    public final String getKey() throws Exception {
        String hwid = HWID();

        String encryptedHWID = AES.encrypt("LYGV6ILURVT7mi8V", "WzhaT14Vh5zZq8GN", hwid);
        String encryptedSeed = AES.encrypt("BmfrwoKUyN5wBAMc", "WzhaT14Vh5zZq8GN", java.lang.String.valueOf(lastSeed = new Random().nextInt(100000)));

        return Indirection.enbxz().encodeToString(AES.encrypt("qESR7lpRWInukfSP", "WzhaT14Vh5zZq8GN", encryptedHWID + "|" + encryptedSeed).getBytes());
    }

    public final String HWID() throws Exception {
        return textToSHA1(Indirection.acvcxv("PROCESSOR_IDENTIFIER") + Indirection.acvcxv("COMPUTERNAME") + Indirection.becv("user.name"));
    }

    final String textToSHA1(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        sha1hash = md.digest();
        return bytesToHex(sha1hash);
    }

    final char[] hexArray = "0123456789abcdef".toCharArray();

    final String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public String getClientVersion() {
        return "1.5.0";
    }

    public String getClientEdition() {
        return "Xevier EULA Bypass Edition";
    }

    public ValueManager getValueManager() {
        return valueManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

}
