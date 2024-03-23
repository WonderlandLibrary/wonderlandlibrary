package xyz.northclient.auth;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import xyz.northclient.NorthSingleton;
import xyz.northclient.features.ui.GuiAuth;
import xyz.northclient.features.ui.GuiMainMenuNew;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.util.animations.Animation;
import xyz.northclient.util.animations.util.Easing;
import xyz.northclient.util.animations.util.Easings;
import xyz.northclient.util.font.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class Auth extends WebSocketClient implements Runnable {

    public Gson gson = new Gson();
    public int uid = -32467324;

    public Auth() throws URISyntaxException {
        super(new URI("ws://127.0.0.1:43112"));




        //  super(new URI("ws://147.135.211.212:43112"));
        if (!Auth.firstLogin) {
            marawAutomobile = 0;
            uid = 0;
        }
    }






    public static String randomStr(int length) {
        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }


    public Thread tha;
    public void start(String username, String hwid) {
        tha = new Thread(() -> {
            try {
                this.run(username,hwid);
            }catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        });
        tha.start();
    }
    public String username;
    public static boolean firstLogin = true;
    public boolean gatoCape = false;
    public int marawAutomobile = -31243;
    public int keepAliveDelay = 0;
    public String hwid;
    private void run(String username ,String hwid) throws Exception {
        this.prevUsername = username;
        // System.out.println("Reconnecting");
        backendStatus = "Reconnecting";
        this.username = username;
        this.hwid = hwid;
        this.connect();
    }

    public HashMap<String,String> playersOnServer = new HashMap<>();
    public ArrayList<String> gatos = new ArrayList<>();

    public String sendPacket(JsonObject json) {
        String fingerprint = randomStr(32);
        json.addProperty("fingerprint",fingerprint);
        this.send(EncryptUtil.encrypt(gson.toJson(json)));
        return fingerprint;
    }

    public void sendPacketNonFingerprint(JsonObject json) {
        this.send(EncryptUtil.encrypt(gson.toJson(json)));
    }

    public String fingerprintLogin = "";
    public String backendStatus = "";
    public String prevUsername;

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        JsonObject handshake = new JsonObject();
        handshake.addProperty("id",1);
        handshake.addProperty("username",username);
        handshake.addProperty("hwid",hwid);
        // handshake.addProperty("hwid","gej");
        handshake.addProperty("beta", NorthSingleton.BETA);
        fingerprintLogin = sendPacket(handshake);
        backendStatus = "Sent auth packet";
        //  System.out.println("Sent auth packet");
    }

    @Override
    public void onMessage(String s) {
        JsonObject obj = gson.fromJson(EncryptUtil.decrypt(s), JsonObject.class);
        if(obj.get("id").getAsInt() == 2) {
            if(!obj.get("declined").getAsBoolean() && fingerprintLogin.equalsIgnoreCase(obj.get("resFingerprint").getAsString())) {
                //       System.out.println("Connected");
                backendStatus = "Connected";
                uid = obj.get("uid").getAsInt();
                marawAutomobile = obj.get("marawAutomobile").getAsInt();
                keepAliveDelay = obj.get("marawAutomobile").getAsInt();

                Runtime.getRuntime().addShutdownHook(new Thread(this::close));
                new Thread(() -> {
                    while(true) {
                        try {
                            JsonObject pac = new JsonObject();
                            pac.addProperty("id",0);
                            pac.addProperty("time",System.currentTimeMillis());
                            sendPacketNonFingerprint(pac);
                            Thread.sleep(marawAutomobile);
                        }catch (Exception e) {}
                    }
                }).start();
                if(firstLogin) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenuNew());
                    firstLogin = false;
                }

                JsonObject meta = new Gson().fromJson(obj.get("meta").getAsString(), JsonObject.class);
                this.gatoCape = meta.get("gatoCape").getAsBoolean();
            } else {
                //   System.out.println( obj.get("reason").getAsString());
                backendStatus = obj.get("reason").getAsString();
                this.close();
            }
        }

        if(obj.get("id").getAsInt() == 4) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[" + NorthSingleton.INSTANCE.getUiHook().getTheme().getChatPrefixColor() + "North" + EnumChatFormatting.GRAY + "] " + obj.get("author").getAsString() + EnumChatFormatting.DARK_GRAY + " » " + EnumChatFormatting.GRAY + obj.get("content").getAsString()));
        }

        if(obj.get("id").getAsInt() == 6) {
            JsonObject players = obj.getAsJsonObject("players");

            for(Map.Entry<String,JsonElement> el : players.entrySet()) {
                playersOnServer.put(el.getKey(),el.getValue().getAsString());
            }

            obj.getAsJsonArray("gatos").forEach((b) -> gatos.add(b.getAsString()));
        }

        if(obj.get("id").getAsInt() == 7) {
            if(obj.get("action").getAsString().equalsIgnoreCase("connect")) {
                playersOnServer.put(obj.get("minecraftName").getAsString(),obj.get("clientName").getAsString());

            } else if(obj.get("action").getAsString().equalsIgnoreCase("disconnect")) {
                playersOnServer.remove(obj.get("minecraftName").getAsString());
            }

            if(obj.get("gato").getAsBoolean()) {
                if(obj.get("action").getAsString().equalsIgnoreCase("connect")) {  gatos.add(obj.get("minecraftName").getAsString());  }
                if(obj.get("action").getAsString().equalsIgnoreCase("disconnect")) {  gatos.remove(obj.get("minecraftName").getAsString());  }
            }
        }

    }

    public void sendIRCMessage(String msg) {
        JsonObject res = new JsonObject();
        res.addProperty("id",3);
        res.addProperty("message",msg);
        sendPacketNonFingerprint(res);
    }

    public static String hwid() {
        try{
            String toEncrypt =  System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!firstLogin) {
            try {
                NorthSingleton.INSTANCE.auth = new Auth();
                NorthSingleton.INSTANCE.auth.start(prevUsername,Auth.hwid());
            }catch (Exception e) {
                System.exit(-1);
            }
        }
    }

    public void joinServer(String s) {
        playersOnServer.clear();
        gatos.clear();
        JsonObject object = new JsonObject();
        object.addProperty("id",5);
        object.addProperty("ip",s);
        object.addProperty("name",Minecraft.getMinecraft().session.getUsername());
        sendPacketNonFingerprint(object);
    }

    @Override
    public void onError(Exception e) {

    }

    public boolean first = true, reverse = false;

    public void renderAuthStatus() {
        if(!backendStatus.equalsIgnoreCase("Connected")) {
            int x = (int) ((int) (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()/2.0f) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(backendStatus)/2.0f));
            Minecraft.getMinecraft().fontRendererObj.drawGradientShadowString(backendStatus,x,20, ColorUtil.MainColor(),ColorUtil.AccentColor());
        }
    }

    public void quitServer() {
        playersOnServer.clear();
        gatos.clear();
        JsonObject object = new JsonObject();
        object.addProperty("id",5);
        object.addProperty("ip","null");
        object.addProperty("name","null");
        sendPacketNonFingerprint(object);
    }
}