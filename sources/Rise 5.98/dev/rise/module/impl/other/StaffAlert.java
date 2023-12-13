package dev.rise.module.impl.other;

import com.google.gson.JsonParser;
import dev.rise.Rise;
import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.notifications.NotificationType;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.MathUtil;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.status.server.S01PacketPong;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@ModuleInfo(name = "StaffAlert", description = "Alerts you when staff ban someone else on Hypixel", category = Category.OTHER)
public class StaffAlert extends Module {

    private final NumberSetting delay = new NumberSetting("Delay (Seconds)", this, 60, 1, 120, 1);

    private final TimeUtil timer = new TimeUtil();
    private int lastTimeAmountOfBans = -1;
    public static String apikey;
    private boolean hasKey;


    @Override
    public void onUpdateAlwaysInGui() {
        this.hidden = !(PlayerUtil.isOnServer("Hypixel") || mc.isSingleplayer());
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat p = (S02PacketChat) event.getPacket();
            final String message = p.getChatComponent().getUnformattedText();
            if (message.startsWith("Your new API key is ")) {
                apikey = message.replace("Your new API key is ", "");
                Rise.addChatMessage("API Key Set!");
            }
        }
    }
    @Override
    public void onUpdate(final UpdateEvent event) {
        if(apikey == null && PlayerUtil.isOnServer("Hypixel") && mc.thePlayer.ticksExisted >= 40 && !mc.isSingleplayer() && mc.thePlayer.ticksExisted % 10 == 0){
            timer.reset();
            PacketUtil.sendPacket(new C01PacketChatMessage("/api new"));
        }
        if (!PlayerUtil.isOnServer("Hypixel") || mc.isSingleplayer()) {
            this.registerNotification(this.getModuleInfo().name() + " only works on Hypixel.");
            this.toggleModule();
            return;
        }

        if (mc.isSingleplayer()) {
            lastTimeAmountOfBans = -1;
            timer.reset();
            return;
        }

        if (timer.hasReached((long) delay.getValue() * 1000) || lastTimeAmountOfBans == -1) {
            new Thread(() -> {
                try {
                    final HttpURLConnection connection = (HttpURLConnection) new URL("https://api.hypixel.net/punishmentstats?key=" + apikey).openConnection();
                    connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.connect();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (String read; (read = reader.readLine()) != null; ) {
                        if (read.contains("true")) { //alan loves this code
                            final int staffBans = JsonParser.parseString(read).getAsJsonObject().get("staff_total").getAsInt();
                            final int diff = staffBans - lastTimeAmountOfBans;
                            final int delay = (int) Math.round(this.delay.getValue());
                            if (lastTimeAmountOfBans != -1 && diff > 0)
                                Rise.INSTANCE.getNotificationManager().registerNotification(diff + (diff == 1 ? " player has" : " players have") + " been staff banned in the past " + (delay == 1 ? "second" : delay + "s") + ".", NotificationType.WARNING);
                            lastTimeAmountOfBans = staffBans;
                        }
                    }
                } catch (final Exception e) {
                    if(apikey != null) {
                        Rise.INSTANCE.getNotificationManager().registerNotification(this.getModuleInfo().name() + " failed to get staff ban information.", NotificationType.WARNING);
                    }
                    e.printStackTrace();
                }
            }).start();
            timer.reset();
        }
    }

    @Override
    protected void onEnable() {
        lastTimeAmountOfBans = -1;
        timer.reset();
    }
}