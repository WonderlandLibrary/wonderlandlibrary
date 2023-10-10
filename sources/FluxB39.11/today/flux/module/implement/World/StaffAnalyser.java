package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.network.play.server.S02PacketChat;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.event.RespawnEvent;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.*;

import java.net.URL;

public class StaffAnalyser extends Module {
    public static ModeValue notificationType = new ModeValue("StaffAnalyser", "Notification Type", "Notification", "Chat Message");
    public static BooleanValue hideNoBan = new BooleanValue("StaffAnalyser", "Hide 0 Ban", false);
    public static FloatValue delay = new FloatValue("StaffAnalyser", "Check Delay", 60F, 10F, 300F, 1F);
    public static String key = null;
    TimeHelper sendNewApiTimer = new TimeHelper();
    CheckThread thread;

    public int lastBanned = 0;

    public StaffAnalyser() {
        super("StaffAnalyser", Category.World, false);
        thread = new CheckThread();
        thread.start();
    }

    @EventTarget
    public void onRespawn(RespawnEvent e) {
        key = null;
    }

    @EventTarget
    public void onPre(PreUpdateEvent e) {
        if (ServerUtils.INSTANCE.isOnHypixel() && sendNewApiTimer.isDelayComplete(3000) && key == null) {
            mc.thePlayer.sendChatMessage("/api new");
            sendNewApiTimer.reset();
        }
    }

    @EventTarget
    public void onPacket(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat chatPacket = (S02PacketChat) e.getPacket();
            String chatMessage = chatPacket.getChatComponent().getUnformattedText();
            if (chatMessage.matches("Your new API key is ........-....-....-....-............")) {
                e.setCancelled(true);
                key = chatMessage.replace("Your new API key is ", "");
                ChatUtils.debug("Get API Key: " + key);
            }
        }
    }
}

class CheckThread extends Thread {
    int lastBannedCount = 0;

    @Override
    public void run() {
        while (true) {
            try {
                if (StaffAnalyser.key == null) {
                    sleep(1000);
                    continue;
                }
                sleep(StaffAnalyser.delay.getValue().intValue() * 1000L);
                String result = HttpUtil.performGetRequest(new URL("https://api.hypixel.net/watchdogStats?key=" + StaffAnalyser.key));

                Gson gson = new Gson();
                BanQuantityListJSON banQuantityListJSON = gson.fromJson(result, BanQuantityListJSON.class);
                // int watchdogLastMinute = banQuantityListJSON.getWatchdogLastMinute(); //1分钟前内狗ban的人数
                // int watchdogTotal = banQuantityListJSON.getWatchdogTotal(); //共狗ban的人数
                int staffTotal = banQuantityListJSON.getStaffTotal(); //共客服ban的人数

                if (lastBannedCount == 0) {
                    lastBannedCount = staffTotal;
                } else {
                    int banned = staffTotal - lastBannedCount;
                    lastBannedCount = staffTotal;

                    if (banned > 1) {
                        if (StaffAnalyser.notificationType.isCurrentMode("Notification"))
                            NotificationManager.show("Staff Warning", "Staff banned " + banned + " players in " + StaffAnalyser.delay.getValue().intValue() + "s.", banned > 3 ? Notification.Type.ERROR : Notification.Type.WARNING, StaffAnalyser.delay.getValue().longValue() * 1000);
                        else
                            PlayerUtils.tellPlayer("\247cStaff banned " + banned + " players in " + StaffAnalyser.delay.getValue().intValue() + "s.");
                    } else if (!StaffAnalyser.hideNoBan.getValue()) {
                        if (StaffAnalyser.notificationType.isCurrentMode("Notification"))
                            NotificationManager.show("Staff Info", "Staff didn't ban any player in " + StaffAnalyser.delay.getValue().intValue() + "s.", Notification.Type.SUCCESS, StaffAnalyser.delay.getValue().longValue() * 1000);
                        else
                            PlayerUtils.tellPlayer("\247aStaff didn't ban any player in " + StaffAnalyser.delay.getValue().intValue() + "s.");
                    }

                    ModuleManager.staffAnalyserMod.lastBanned = banned;
                    ModuleManager.staffAnalyserMod.setTag(String.valueOf(banned));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

@NoArgsConstructor
@Data
class BanQuantityListJSON {
    @SerializedName("success")
    boolean success;
    @SerializedName("watchdog_lastMinute")
    int watchdogLastMinute;
    @SerializedName("staff_rollingDaily")
    int staffRollingDaily;
    @SerializedName("watchdog_total")
    int watchdogTotal;
    @SerializedName("watchdog_rollingDaily")
    int watchdogRollingDaily;
    @SerializedName("staff_total")
    int staffTotal;
}