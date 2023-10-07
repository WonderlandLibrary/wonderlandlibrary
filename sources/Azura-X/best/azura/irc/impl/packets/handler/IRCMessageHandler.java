package best.azura.irc.impl.packets.handler;

import best.azura.client.api.module.Module;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.command.impl.BanCommand;
import best.azura.client.impl.command.impl.GlatzCommand;
import best.azura.client.impl.module.impl.other.IRCModule;
import best.azura.client.impl.module.impl.render.Emotes;
import best.azura.client.util.crypt.HWIDUtil;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.EmoteRequest;
import best.azura.client.util.other.ServerUtil;
import best.azura.irc.core.channels.IChannel;
import best.azura.irc.core.entities.Message;
import best.azura.irc.core.entities.User;
import best.azura.irc.core.packet.Packet;
import best.azura.irc.impl.IRCConnector;
import best.azura.irc.impl.packets.client.C0LoginRequestPacket;
import best.azura.irc.impl.packets.client.C2NameChangePacket;
import best.azura.irc.impl.packets.server.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;

import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class for the Message-Handler used to
 * handle Message Input from the IRC Connection.
 */
public class IRCMessageHandler {

    // Instance of the IRC-Connector.
    private final IRCConnector ircConnector;

    // Thread to handle Incoming Messages.
    private Thread messageHandlerThread;

    /**
     * Constructor for the IRC-MessageHandler.
     *
     * @param ircConnector an instance of the IRC-Connector.
     */
    public IRCMessageHandler(IRCConnector ircConnector) {
        this.ircConnector = ircConnector;
    }

    final String[] reasons = {
            "RCED by Novoline quickmaths",
            "Ratted by Autumn client",
            "Ratted by Smoke X Crack",
            "Use Novoline Free crack",
            "Bozoware by ShidderHvH",
            "Ratted by Shidder HvH",
            "Shouldn't use SolaWare :)",
            "Ratted by Error RCE",
            "Error (Chan)",
            "You crashed ????!!? HOW",
            "okay dude",
            "Balls HEHEHEHA",
            "shutdown -r"};

    /**
     * Start the MessageHandler-Thread.
     */
    public void startHandler() {
        messageHandlerThread = new Thread(() -> {

            // Check if there is a connection.
            if (getIrcConnector().getClientSocket().isClosed()) return;

            // Repeat the progress while the conditions are met.
            while (!getIrcConnector().getClientSocket().isClosed()) {

                // Create a variable for the Scanner.
                Scanner inputScanner = null;

                // Try to initialize it.
                try {
                    inputScanner = new Scanner(getIrcConnector().getClientSocket().getInputStream());
                } catch (Exception ignore) {
                }

                // Check if the Scanner is null.
                if (inputScanner == null) return;

                // Repeat the progress while the conditions are met.
                while (inputScanner.hasNextLine()) {

                    String content = inputScanner.nextLine();

                    try {
                        JsonElement jsonElement = new JsonParser().parse(content);

                        if (jsonElement.getAsJsonObject().has("id") && jsonElement.getAsJsonObject().has("content")) {

                            Packet packet = ircConnector.getPacketManager().getPacket(jsonElement.getAsJsonObject());

                            try {
                                if (packet instanceof S7HandshakeResponse) {
                                    S7HandshakeResponse s7HandshakeResponse = (S7HandshakeResponse) packet;

                                    if (s7HandshakeResponse.isSuccess()) {
                                        ircConnector.getIrcData().setAESKey(s7HandshakeResponse.getAESKey());

                                        // Send a Login Packet.
                                        C0LoginRequestPacket c0LoginRequestPacket = new C0LoginRequestPacket();

                                        JsonObject jsonObject = new JsonObject();

                                        jsonObject.addProperty("username", ircConnector.username);
                                        jsonObject.addProperty("hwid", HWIDUtil.getHwid());
                                        jsonObject.addProperty("password", ircConnector.password);
                                        jsonObject.addProperty("product", 1);

                                        c0LoginRequestPacket.setContent(jsonObject);

                                        ircConnector.sendPacket(c0LoginRequestPacket);
                                    } else {
                                        Minecraft.getMinecraft().shutdown();
                                    }
                                } else if (packet instanceof S0LoginResponsePacket) {
                                    S0LoginResponsePacket s0LoginResponsePacket = (S0LoginResponsePacket) packet;

                                    if (s0LoginResponsePacket.isSuccess()) {

                                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Azura Chat!"
                                                , "Login successful!", 5000, Type.SUCCESS));

                                        for (int i = 0; i < s0LoginResponsePacket.getUserList().size(); i++) {
                                            JsonElement jsonElement1 = s0LoginResponsePacket.getUserList().get(i);

                                            if (jsonElement1.isJsonObject()) {
                                                JsonObject jsonObject = jsonElement1.getAsJsonObject();

                                                User user = new User(jsonObject.get("name").getAsString(),
                                                        jsonObject.get("ingame").getAsString(),
                                                        getIrcConnector().getIrcData().getRankById(Integer.parseInt(jsonObject.get("rank").getAsString())));

                                                if (!getIrcConnector().getIrcCache().getIrcUserHashMap().containsKey(jsonObject.get("name").getAsString())) {
                                                    getIrcConnector().getIrcCache().getIrcUserHashMap().put(jsonObject.get("name").getAsString(), user);
                                                } else {
                                                    getIrcConnector().getIrcCache().getIrcUserHashMap().get(jsonObject.get("name").getAsString()).setMinecraftName(jsonObject.get("name").getAsString());
                                                }
                                            }
                                        }

                                        for (int i = 0; i < s0LoginResponsePacket.getChannelList().size(); i++) {
                                            JsonElement jsonElement1 = s0LoginResponsePacket.getChannelList().get(i);

                                            if (jsonElement1.isJsonObject()) {
                                                JsonObject jsonObject = jsonElement1.getAsJsonObject();

                                                IChannel channel = new IChannel() {
                                                    @Override
                                                    public int getId() {
                                                        return jsonObject.get("id").getAsInt();
                                                    }

                                                    @Override
                                                    public String getName() {
                                                        return jsonObject.get("name").getAsString();
                                                    }

                                                    @Override
                                                    public String getDescription() {
                                                        return jsonObject.get("description").getAsString();
                                                    }

                                                    @Override
                                                    public boolean isLocked() {
                                                        return jsonObject.get("locked").getAsBoolean();
                                                    }
                                                };

                                                if (ircConnector.getIrcData().channelManager.getChannelById(jsonObject.get("id").getAsInt()) ==
                                                        null) {
                                                    ircConnector.getIrcData().channelManager.addChannel(jsonObject.get("id").getAsInt(), channel);
                                                }
                                            }
                                        }

                                        // Send Username Packet.
                                        if (Minecraft.getMinecraft().getSession() != null) {
                                            C2NameChangePacket c2NameChangePacket = new C2NameChangePacket(null);
                                            JsonObject jsonObject1 = new JsonObject();

                                            jsonObject1.addProperty("name", Minecraft.getMinecraft().getSession().getUsername());

                                            c2NameChangePacket.setContent(jsonObject1);
                                            ircConnector.sendPacket(c2NameChangePacket);
                                        }

                                        // Start Keep-Alive Handler.
                                        ircConnector.getIrcKeepAliveHandler().startHandler();
                                    } else {
                                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Azura " +
                                                "Chat!",
                                                "Login failed!", 5000, Type.ERROR));
                                        Minecraft.getMinecraft().shutdown();
                                    }

                                } else if (packet instanceof S1ChatSendPacket) {
                                    Message message = ((S1ChatSendPacket) packet).getMessage();

                                    if (message == null) return;

                                    if (!Client.INSTANCE.getModuleManager().getModuleByClass(IRCModule.class).isEnabled()) {
                                        return;
                                    }

                                    if (message.isConsole()) {
                                        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageIRC(new ChatComponentText(
                                                        ChatFormatting.PREFIX_CODE + "7[" + ChatFormatting.PREFIX_CODE + "9A" +
                                                                "-Chat" +
                                                                ChatFormatting.PREFIX_CODE + "7]" + ChatFormatting.PREFIX_CODE + "r " + ChatFormatting.PREFIX_CODE + "cSystem" + ChatFormatting.PREFIX_CODE + "8: " + ChatFormatting.PREFIX_CODE + "7" + message.getMessageContent().replace('&', ChatFormatting.PREFIX_CODE)),
                                                message.getChannelId());
                                    } else {
                                        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageIRC(new ChatComponentText(
                                                        ChatFormatting.PREFIX_CODE + "7[" + ChatFormatting.PREFIX_CODE + "9A" +
                                                                "-Chat" +
                                                                ChatFormatting.PREFIX_CODE + "7]" + ChatFormatting.PREFIX_CODE + "r " +
                                                                message.getFormattedMessage().replace('&', ChatFormatting.PREFIX_CODE)),
                                                message.getChannelId());
                                    }
                                } else if (packet instanceof S2NameChangePacket) {

                                    S2NameChangePacket nameChangePacket = (S2NameChangePacket) packet;

                                    if (!nameChangePacket.getContent().has("ingame")) return;

                                    User user = new User(nameChangePacket.getContent().get("name").getAsString(),
                                            nameChangePacket.getContent().get("ingame").getAsString(),
                                            getIrcConnector().getIrcData().getRankById(Integer.parseInt(nameChangePacket.getContent().get("rank").getAsString())));

                                    Client.INSTANCE.getLogger().info("Updating User " + user.getUsername() + " with new Name " + user.getMinecraftName() + "!");

                                    if (!getIrcConnector().getIrcCache().getIrcUserHashMap().containsKey(user.getUsername())) {
                                        getIrcConnector().getIrcCache().getIrcUserHashMap().put(user.getUsername(), user);
                                    } else {
                                        getIrcConnector().getIrcCache().getIrcUserHashMap().get(user.getUsername()).setMinecraftName(user.getMinecraftName());
                                    }
                                } else if (packet instanceof S3FunnyPacket) {
                                    S3FunnyPacket funnyPacket = (S3FunnyPacket) packet;
                                    if (IRCModule.funnyPacket.getObject()) {
                                        switch (funnyPacket.getContent().get("funny").getAsString().toLowerCase()) {
                                            case "glatze":
                                                GlatzCommand glatzCommand =
                                                        (GlatzCommand) Client.INSTANCE.getCommandManager().getCommands().stream().filter(iCommand -> iCommand instanceof GlatzCommand).findFirst().get();
                                                if (!Client.INSTANCE.getEventBus().isRegistered(glatzCommand))
                                                    Client.INSTANCE.getEventBus().register(glatzCommand);
                                                break;
                                            case "confused":
                                                if (Minecraft.getMinecraft().theWorld != null &&
                                                        !Minecraft.getMinecraft().isSingleplayer()) {
                                                    for (NetworkPlayerInfo networkPlayerInfo :
                                                            Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                                                        Emotes.addToQueue(new EmoteRequest(networkPlayerInfo.getGameProfile().getName(), "creepy"), false);
                                                    }
                                                }
                                                break;
                                            case "teleport":
                                                if (Minecraft.getMinecraft().theWorld != null &&
                                                        !Minecraft.getMinecraft().isSingleplayer())
                                                    Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX,
                                                            Minecraft.getMinecraft().thePlayer.posY + Math.random() *
                                                                    0.02F,
                                                            Minecraft.getMinecraft().thePlayer.posZ);
                                                break;
                                            case "ban":
                                                BanCommand banCommand =
                                                        (BanCommand) Client.INSTANCE.getCommandManager().getCommands().stream().filter(iCommand -> iCommand instanceof BanCommand).findFirst().get();
                                                banCommand.handleCommand(new String[0]);
                                                break;
                                            case "disable-modules":
                                                for (Module module :
                                                        Client.INSTANCE.getModuleManager().getRegistered())
                                                    module.setEnabled(false);
                                                break;
                                            case "exit":
                                                Client.INSTANCE.getLogger().info("[RAT] " + reasons[MathUtil.getRandom_int(0,
                                                        reasons.length - 1)]);
                                                System.exit(0);
                                                break;
                                            case "fake-rce":
                                                try {
                                                    Runtime.getRuntime().exec("calc");
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                JOptionPane.showMessageDialog(null, "Hello from Minecraft RCE Exploit," +
                                                        " we have installed a bitcoin " +
                                                        "miner on your PC ;)");
                                                break;
                                            case "demo":
                                                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenDemo());
                                                break;
                                            default:
                                                if (funnyPacket.getContent().get("funny").getAsString().toLowerCase().startsWith("toggle-module")) {
                                                    Module module =
                                                            Client.INSTANCE.getModuleManager().getModule(funnyPacket.getContent().get("funny").getAsString().substring("toggle-module".length()));
                                                    if (module != null) {
                                                        module.setEnabled(!module.isEnabled());
                                                    }
                                                    break;
                                                }
                                        }
                                    }
                                } else if (packet instanceof S4BanNotifierPacket) {
                                    S4BanNotifierPacket banNotifierPacket = (S4BanNotifierPacket) packet;

                                    if (IRCModule.banNotifier.getObject()) {
                                        if (banNotifierPacket.getContent().has("server")) {
                                            if (banNotifierPacket.getContent().get("server").getAsString().equals(ServerUtil.lastIP)) {
                                                if (!Client.INSTANCE.getModuleManager().getModuleByClass(IRCModule.class).isEnabled()) {
                                                    return;
                                                }

                                                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                                                        ChatFormatting.PREFIX_CODE + "7[" + ChatFormatting.PREFIX_CODE +
                                                                "9A-" + ChatFormatting.PREFIX_CODE + "cNotifier" +
                                                                ChatFormatting.PREFIX_CODE + "7]" + ChatFormatting.PREFIX_CODE +
                                                                "r The User " +
                                                                getIrcConnector().getIrcCache().getIrcUserHashMap().get(banNotifierPacket.getContent().get("name").getAsString()).getUsername() +
                                                                " has been banned on your current Server!"));
                                            }
                                        }
                                    }
                                } else if (packet instanceof S5EmotePacket) {
                                    S5EmotePacket emotePacket = (S5EmotePacket) packet;
                                    if (IRCModule.emotes.getObject()) {
                                        if (emotePacket.getContent().has("server") &&
                                                emotePacket.getContent().has("emote") &&
                                                emotePacket.getContent().has("name")) {
                                            if (emotePacket.getContent().get("server").getAsString().equals(ServerUtil.lastIP)) {
                                                if (!Client.INSTANCE.getModuleManager().getModuleByClass(IRCModule.class).isEnabled()) {
                                                    return;
                                                }

                                                Emotes.addToQueue(new EmoteRequest(getIrcConnector().getIrcCache().getIrcUserHashMap().get(emotePacket.getContent().get("name").getAsString()).getMinecraftName(), emotePacket.getContent().get("emote").getAsString()), false);
                                            }
                                        }
                                    }
                                } else if (packet instanceof S6BanPacket) {
                                    System.exit(-1);
                                }
                            } catch (Exception exception) {
                                Client.INSTANCE.getLogger().error(content, exception);
                            }
                        } else {
                            Client.INSTANCE.getLogger().error(content);
                            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Azura Chat!", "There" +
                                    " was " +
                                    "an unnatural Packet from the IRC please report this!", 5000, Type.WARNING));
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        // Start actual Thread.
        messageHandlerThread.start();
    }

    /**
     * Stop the Handler.
     */
    public void stopHandler() {

        // Interrupt the Thread.
        if (getMessageHandlerThread() != null) getMessageHandlerThread().interrupt();
    }

    /**
     * Retrieve the Instance of the IRC Connector.
     *
     * @return instance of the IRC-Connector.
     */
    public IRCConnector getIrcConnector() {
        return ircConnector;
    }

    /**
     * Retrieve the Thread of the MessageHandler.
     *
     * @return the MessageHandler Thread.
     */
    public Thread getMessageHandlerThread() {
        return messageHandlerThread;
    }
}
