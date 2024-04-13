package dev.tenacity;

import dev.tenacity.command.AbstractCommand;
import dev.tenacity.command.Command;
import dev.tenacity.command.CommandRepository;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import dev.tenacity.command.impl.*;
import dev.tenacity.config.ConfigHandler;
import dev.tenacity.event.EventBus;
import dev.tenacity.event.impl.player.Blink;
import dev.tenacity.event.impl.player.SpeedMine;
import dev.tenacity.module.ModuleRepository;
import dev.tenacity.module.impl.combat.*;
import dev.tenacity.module.impl.exploit.*;
import dev.tenacity.module.impl.funny.AntiRetardModule;
import dev.tenacity.module.impl.funny.zoom;
import dev.tenacity.module.impl.misc.*;
import dev.tenacity.module.impl.movement.*;
import dev.tenacity.module.impl.player.*;
import dev.tenacity.module.impl.render.*;
import dev.tenacity.module.impl.world.AlwaysNight;
import dev.tenacity.module.impl.world.TimerModule;
import dev.tenacity.util.render.Theme;
import dev.tenacity.viamcp.impl.ViaMCP;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import javax.swing.JOptionPane;

import javax.net.ssl.HttpsURLConnection;
import javax.sound.sampled.*;
import java.awt.*;

import static dev.tenacity.command.impl.NameCommand.CN;
import static dev.tenacity.command.impl.NameCommand.CV;

public final class Tenacity {

    public final Color getClientColor() {
        return new Color(236, 133, 209);
    }

    public final Color getAlternateClientColor() {
        return new Color(28, 167, 222);
    }

    // Tenacity's Constants
    public static final Tenacity INSTANCE = new Tenacity();
    public static final String NAME = "Tenacity";
    public static final String VERSION = "6.0";
    public static final String TYPE = "Development";
    public static final String EXIT = "Tenacity Shutting down";
    public static final String AUTH = "Authenticating...";
    public static final String WELCOME = "Welcome to Tenacity!";
    public static final String CUSTOM_NAME = CN;
    public static final String CUSTOM_VERSION = CV;
    public static final String BLANK = " ";
    public static final ResourceLocation opening = new ResourceLocation("Tenacity/Sounds/opening.wav");
    public static final ResourceLocation cat = new ResourceLocation("Tenacity/cat.png");
    String hwid;

    // Tenacity's Repositories
    private final ModuleRepository moduleRepository;
    private final CommandRepository commandRepository;

    // Tenacity's Event System
    private final EventBus eventBus;

    // Tenacity's Config System
    private final ConfigHandler configHandler;


    private Tenacity() {
        eventBus = new EventBus();

        Theme.init();

        moduleRepository = ModuleRepository.builder()
                .putAll(new KillAuraModule(), new SpeedModule(), new FlightModule(),
                        new SprintModule(), new ArraylistModule(), new ClickGUIModule(),
                        new HUDModule(), new DisablerModule(), new ScaffoldModule(),
                        new BlockAnimationModule(), new VelocityModule(), new LongJumpModule(),
                        new NoSlowModule(), new NoFallModule(), new FullBrightModule(), new AntiExploit(),
                        new PostProcessing(), new AutoClicker(), new Reach(), new FastPlaceModule(),
                        new MCF(), new AntiRetardModule(), new AlwaysNight(), new AntiVoid(),
                        new TPAura(), new spiderModule(), new zoom(), new Crasher(), new TimerModule(),
                        new BedBreakerModule(), new AntiInvis(), new ResetVL(), new Regen(),
                        new AutoHypixel(), new Criticals(), new FastBow(), new Blink(),
                        new SpeedMine(), new ESP2D(), new PlayerInfoModule(),
                        new TargetInfoModule(), new RegisterModule())
                .build();

        commandRepository = CommandRepository.builder()
                .putAll(new BindCommand(), new ToggleCommand(), new FriendCommand(), new ConfigCommand(), new CommandListCommand(), new CatCommand(),
                        new BindCommandB(), new ToggleCommandT(),  new FriendCommandF(), new ConfigCommandC(), new CommandListCommandC(),
                        new DClip(), new VClipCommand(), new HClip(), new CrasherCommand(), new NameCommand())
                .build();
        configHandler = new ConfigHandler();

        try {

            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        Display.setTitle(AUTH);
        configHandler.loadDefaultConfig();

        try {
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Panel.background", Color.BLACK);
            UIManager.put("OptionPane.background", Color.BLACK);
            UIManager.put("Panel.foreground", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);

            String hwid = getSystemHWID();

            String username = JOptionPane.showInputDialog(null, "Please enter your username:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty. (Hint: Its the same as your discord display name, not case sensitive!)", "Error: Incorrect username", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            username = username.toLowerCase();

            String usernameHWID = username + "\\" + hwid;

            boolean isHWIDValid = checkHWIDAgainstServer(usernameHWID);

            if (!isHWIDValid) {
                JFrame frame = new JFrame();
                JOptionPane optionPane = new JOptionPane("Your HWID or Username did not match up with our database!\nYour HWID: " + hwid + "\nPlease copy this HWID to your clipboard and create a ticket in the discord!",
                        JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

                JButton copyButton = new JButton("Copy");
                copyButton.addActionListener(e -> {
                    StringSelection stringSelection = new StringSelection(hwid);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    JOptionPane.showMessageDialog(null, "HWID copied to clipboard.");
                });

                JButton closeButton = new JButton("Close");
                closeButton.addActionListener(e -> frame.dispose());

                optionPane.setOptions(new Object[]{copyButton, closeButton});

                JDialog dialog = new JDialog(frame, "HWID or Username Not Found", true);
                dialog.setContentPane(optionPane);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

                System.exit(0);
            } else {
                System.out.println("Username and HWID found. Proceeding...");
                WelcomeTTS();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSystemHWID() throws Exception {
        try {
            hwid = getHWID();
            if(hwid != null) {
                System.out.println("HWID found:" + BLANK + hwid + BLANK + "!");
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return hwid;
    }

    private boolean checkHWIDAgainstServer(String hwid) {
        try {
            URL url = new URL("https://rentry.co/7ybsp498/raw");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String hwidList = reader.lines().collect(Collectors.joining("\n"));
                return hwidList.contains(hwid);
            }
        } catch (IOException e) {
            System.err.println("Error accessing Tenacity database verification URL!");
            return false;
        }
    }

    public static void main(String[] args) {
        Tenacity tenacity = new Tenacity();
        tenacity.initialize();
    }


    public void WelcomeTTS() {
        WelcomeTT();
        try {
            ResourceLocation audioFile = opening;

            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(audioFile).getInputStream();
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream));
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            Thread.sleep(audioClip.getMicrosecondLength() / 800);
            audioClip.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void WelcomeTT() {
        Display.setTitle(WELCOME);
        try {
            Thread.sleep(500);
            TenacityT();
        }
        catch (Exception e) {
            System.err.println(e);
            TenacityT();
        }
    }

    public void TenacityT() {
        try {
            Thread.sleep(1000);
            Display.setTitle(NAME + BLANK + VERSION + BLANK + TYPE);
        }
        catch (Exception e) {
            System.err.println(e);
            Display.setTitle(NAME + BLANK + VERSION + BLANK + TYPE);
        }
    }

    public void terminate() {
        try {
            Display.setTitle(EXIT);
            configHandler.saveDefaultConfig();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ModuleRepository getModuleRepository() {
        return moduleRepository;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public CommandRepository getCommandRepository() {
        return commandRepository;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public static String getName() {
        return NAME;
    }
    public static Tenacity getInstance() {
        return INSTANCE;
    }
    public static String getVersion() {
        return VERSION;
    }

    public static String WELCOME() {
        return WELCOME;
    }
    public static String AUTH() {
        return AUTH;
    }
    public static String EXIT() {
        return EXIT;
    }
    public static String BLANK() {
        return BLANK;
    }

    private String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "/";
            }
            i++;
        }
        return s;
    }
}
