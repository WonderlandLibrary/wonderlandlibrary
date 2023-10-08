package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.other.OpenGUIEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.math.RandomUtil;
import dev.rise.util.misc.FileUtil;
import lombok.Data;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ModuleInfo(name = "AutoAuthme", description = "Automatically authenticates you when the server is using the Authme plugin", category = Category.OTHER)
public class AutoAuthme extends Module {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(1);

    private static final List<AutoAuthmeLogin> logins = new ArrayList<>();

    private boolean loggedIn, loggingIn;

    public static void loadLogins() {
        final String loginContent = FileUtil.loadFile("authmelogins.txt");
        if (loginContent == null) return;

        for (final String loginLine : loginContent.split("\r\n")) {
            final String[] loginData = loginLine.split(":");
            if (loginLine.length() < 3) continue;
            logins.add(new AutoAuthmeLogin(loginData[0], loginData[1], loginData[2]));
        }
    }

    public static void saveLogins() {
        final StringBuilder loginContent = new StringBuilder();
        for (final AutoAuthmeLogin login : logins) {
            loginContent.append("\r\n").append(login.getServerIp()).append(":").append(login.getUsername()).append(":").append(login.getPassword());
        }
        FileUtil.saveFile("authmelogins.txt", true, loginContent.toString());
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        loggedIn = loggingIn = false;
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (!loggedIn && !loggingIn) {
            if (event.getPacket() instanceof C01PacketChatMessage) {
                final C01PacketChatMessage packet = (C01PacketChatMessage) event.getPacket();
                final String message = packet.getMessage();

                if (message.equalsIgnoreCase("/register")) {
                    register();
                    event.setCancelled(true);
                } else if (message.equalsIgnoreCase("/login")) {
                    login();
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (!loggedIn && !loggingIn) {
            if (event.getPacket() instanceof S02PacketChat) {
                final S02PacketChat packet = (S02PacketChat) event.getPacket();

                // Get the message with color and formatting codes removed
                final String message = packet.getChatComponent().getUnformattedText();

                if (message.toLowerCase().contains("/register")) {
                    register();
                } else if (message.toLowerCase().contains("/login")) {
                    login();
                }
            }
        }
    }

    @Override
    public void onOpenGUI(final OpenGUIEvent event) {
        final GuiScreen gui = event.getNewScreen();
        if (mc.getCurrentServerData() == null || gui == null
                || gui instanceof GuiConnecting || gui instanceof GuiMultiplayer
                || gui instanceof GuiDisconnected || gui instanceof GuiMainMenu) {
            loggedIn = loggingIn = false;
        }
    }

    private void register() {
        loggingIn = true;
        final String serverIp = mc.getCurrentServerData().serverIP;
        final String username = mc.thePlayer.getName();
        final String password = RandomUtil.randomStringOfSize(8, RandomUtil.QWERTY_CHARSET);

        Rise.addChatMessage(
                "AutoAuthme is automatically registering and logging in for you! Hover for login info!",
                "Server IP: " + serverIp + "\nUsername: " + username + "\nPassword: " + password
        );

        mc.thePlayer.sendChatMessage("/register " + password + " " + password);
        SCHEDULED_EXECUTOR.schedule(() -> {
            mc.thePlayer.sendChatMessage("/login " + password);
            loggedIn = true;
        }, 1L, TimeUnit.SECONDS);

        logins.add(new AutoAuthmeLogin(serverIp, username, password));
    }

    private void login() {
        loggingIn = true;

        final String serverIp = mc.getCurrentServerData().serverIP;
        final String username = mc.thePlayer.getName();

        for (final AutoAuthmeLogin login : logins) {
            if (login.getServerIp().equalsIgnoreCase(serverIp) && login.getUsername().equalsIgnoreCase(username)) {
                final String password = login.getPassword();

                mc.thePlayer.sendChatMessage("/login " + password);

                Rise.addChatMessage(
                        "AutoAuthme is automatically logging you in! Hover for login info!",
                        "Server IP: " + serverIp + "\nUsername: " + username + "\nPassword: " + password
                );

                loggedIn = true;
                return;
            }
        }
    }
}

@Data
class AutoAuthmeLogin {
    private final String serverIp, username, password;
}
