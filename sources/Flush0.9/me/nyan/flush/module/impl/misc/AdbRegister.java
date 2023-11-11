package me.nyan.flush.module.impl.misc;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventConnection;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventTick;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import static java.nio.file.StandardOpenOption.*;

public class AdbRegister extends Module {
    private String password;
    private boolean connected;
    private String lastMessage = null;

    private boolean disconnect;
    private int count = 0;

    private final File file = new File(Flush.getClientPath(), "accounts.txt");

    private final String adbPath = "C:\\Users\\crico\\Documents\\Portable Software\\platform-tools\\adb.exe";
    private final String wordsPath = "C:\\Users\\crico\\Documents\\Portable Software\\platform-tools\\words.txt";

    private final ArrayList<String> words = new ArrayList<>();
    private final Random random = new Random();

    private ServerData lastServer;

    public AdbRegister() {
        super("ADBRegister", Category.MISC);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(wordsPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        count = 0;
        lastServer = null;
        connected = false;

        if (!mc.isIntegratedServerRunning()) {
            lastServer = new ServerData(mc.getCurrentServerData().serverName, mc.getCurrentServerData().serverIP, false);
        }

        execADB("shell", "settings", "put", "global", "transition_animation_scale", "0");
        Flush.LOGGER.info("Disabled android animations");

        execADB("shell", "svc", "power", "stayon", "true");
        Flush.LOGGER.info("Forced screen to stay on");

        execADB("shell", "am", "start", "-a", "android.settings.AIRPLANE_MODE_SETTINGS");
        Flush.LOGGER.info("Opened airplane mode settings");

        execADB("shell", "input", "keyevent", "KEYCODE_WAKEUP");
        Flush.LOGGER.info("Woke device up");
    }

    @Override
    public void onDisable() {
        execADB("shell", "settings", "put", "global", "transition_animation_scale", "1");
        Flush.LOGGER.info("Restoring android animations");

        execADB("shell", "svc", "power", "stayon", "false");
    }

    @SubscribeEvent
    public void onConnection(EventConnection e) {
        lastServer = new ServerData(e.getIp(), e.getIp() + ':' + e.getPort(), false);
        connected = false;
        lastMessage = null;
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (mc.isIntegratedServerRunning() || connected) {
            return;
        }

        String serverIP = mc.getCurrentServerData().serverIP.toLowerCase();
        boolean funcraft = serverIP.contains("funcraft");

        if (lastMessage != null && !lastMessage.isEmpty()) {
            password = RandomStringUtils.randomAlphanumeric(8);
            mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/register " + password +
                    (funcraft ? "" : " " + password)));
            connected = true;
        }
    }

    private void execADB(String... args) {
        String[] fullArgs = new String[args.length + 1];
        fullArgs[0] = adbPath;
        int i = 1;
        for (String arg : args) {
            fullArgs[i] = arg;
            i++;
        }
        try {
            new ProcessBuilder(fullArgs)
                    .inheritIO()
                    .start()
                    .waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void changeIP() {
        execADB("shell", "input", "touchscreen", "tap", "975", "358");
        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {
        }
        execADB("shell", "input", "touchscreen", "tap", "975", "358");
        Flush.LOGGER.info("Changed IP");
    }

    private void saveToTxt(String username, String password) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
                CREATE, WRITE, APPEND)) {
            writer.write(username + ':' + password + System.lineSeparator());
            Flush.LOGGER.info("Registered " + username + ':' + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        if (mc.theWorld == null) {
            disconnect = false;
            return;
        }
        mc.theWorld.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
        disconnect = false;
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S40PacketDisconnect) {
            S40PacketDisconnect packet = e.getPacket();
            String text = packet.getReason().getUnformattedText().toLowerCase();
            if (text.contains("code d'erreur: 5")) {
                changeIP();
                mc.addScheduledTask(this::reconnect);
            }
        }

        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = e.getPacket();
            String text = packet.getChatComponent().getUnformattedText().toLowerCase();
            if (packet.getType() != 2) {
                if (text.contains("login")) {
                    if (!disconnect) {
                        saveToTxt(mc.thePlayer.getName(), password);
                    }
                    
                    disconnect = true;
                    count++;

                    if (count == 3) {
                        changeIP();
                        count = 0;
                    }
                }

                if (text.contains("register")) {
                    lastMessage = EnumChatFormatting.getTextWithoutFormattingCodes(packet.getChatComponent().getUnformattedText());
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(EventTick e) {
        if (e.isPre()) {
            return;
        }

        if (disconnect) {
            disconnect();
        }

        if (mc.currentScreen instanceof GuiDisconnected) {
            toggle();
        }

        if (lastServer != null && mc.currentScreen instanceof GuiMultiplayer) {
            try {
                Process process = new ProcessBuilder(adbPath, "shell", "dumpsys", "telephony.registry")
                        .start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int index = line.indexOf("mDataConnectionState");
                        if (index != -1) {
                            char c = line.charAt(index + "mDataConnectionState=".length());
                            if (c == '0') {
                                return;
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            reconnect();
        }
    }

    private void reconnect() {
        if (lastServer != null) {
            String username = generateUsername();
            mc.setSession(new Session(username, "", "", "mojang"));
            Flush.LOGGER.info("Connected to " + username);

            ServerData data = new ServerData(lastServer.serverName, lastServer.serverIP, false);
            mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, data));
        }
    }

    private String generateUsername() {
        String username;
        do {
            username = StringUtils.capitalize(words.get(random.nextInt(words.size())))
                    + StringUtils.capitalize(words.get(random.nextInt(words.size())))
                    + random.nextInt(100);
        } while (username.length() > 16);
        return username;
    }
}
