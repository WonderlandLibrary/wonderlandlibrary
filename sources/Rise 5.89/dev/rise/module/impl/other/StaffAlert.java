package dev.rise.module.impl.other;

import com.google.gson.JsonParser;
import dev.rise.Rise;
import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.hidden.Disabler;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.notifications.NotificationType;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PlayerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@ModuleInfo(name = "StaffAlert", description = "Alerts you when staff ban someone else on Hypixel", category = Category.OTHER)
public class StaffAlert extends Module {

    private final NumberSetting delay = new NumberSetting("Delay (Seconds)", this, 60, 1, 120, 1);

    private final TimeUtil timer = new TimeUtil();
    private int lastTimeAmountOfBans = -1;

    @Override
    public void onUpdateAlwaysInGui() {
        this.hidden = !(PlayerUtil.isOnServer("Hypixel") || mc.isSingleplayer());
    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        if (!PlayerUtil.isOnServer("Hypixel") && !mc.isSingleplayer()) {
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
                    final HttpURLConnection connection = (HttpURLConnection) new URL("https://api.hypixel.net/punishmentstats?key=").openConnection();
                    connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.connect();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (String read; (read = reader.readLine()) != null; ) {
                        if (read.startsWith("{\"success\":true")) {
                            final int staffBans = JsonParser.parseString(read).getAsJsonObject().get("staff_total").getAsInt();
                            final int diff = staffBans - lastTimeAmountOfBans;
                            final int delay = (int) Math.round(this.delay.getValue());
                            if (lastTimeAmountOfBans != -1 && diff > 0)
                                Rise.INSTANCE.getNotificationManager().registerNotification(diff + (diff == 1 ? " player has" : " players have") + " been staff banned in the past " + (delay == 1 ? "second" : delay + "s") + ".", NotificationType.WARNING);
                            lastTimeAmountOfBans = staffBans;
                        }
                    }
                } catch (final Exception e) {
                    Rise.INSTANCE.getNotificationManager().registerNotification(this.getModuleInfo().name() + " failed to get staff ban information.", NotificationType.WARNING);
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