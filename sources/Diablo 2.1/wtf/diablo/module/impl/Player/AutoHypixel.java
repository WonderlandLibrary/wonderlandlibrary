package wtf.diablo.module.impl.Player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import wtf.diablo.Diablo;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.gui.notifications.Notification;
import wtf.diablo.gui.notifications.NotificationType;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.packet.PacketUtil;

@Getter
@Setter
public class AutoHypixel extends Module {
//    public ModeSetting mode = new ModeSetting("Game", "Skywars", "Bedwars");
    private NumberSetting delay = new NumberSetting("Delay (ms)", 100, 1, 10, 2000);

    public AutoHypixel() {
        super("AutoHypixel", "Automatically re-queue on hypixel", Category.PLAYER, ServerType.All);
        addSettings(delay);
    }

    @Subscribe
    public void onPacket(PacketEvent e) {
        try {
            if (e.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) e.getPacket();
                if(packet.getChatComponent().getUnformattedText().contains("You died!") || packet.getChatComponent().getUnformattedText().contains("You won!")) {
                    Notification n = new Notification("AutoHypixel", "Sending you to a new game in " +  delay.getValue() / 1000 + "s", delay.getValue(), NotificationType.INFORMATION);
                    Diablo.getInstance().addNotification(new Notification("AutoHypixel", "Sending you to a new game in " +  delay.getValue() / 1000 + "s", delay.getValue(), NotificationType.INFORMATION));
                    startThread(delay.getValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void startThread(double delay){
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long) delay);
                    PacketUtil.sendPacket(new C01PacketChatMessage("/play solo_insane"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

/*
enum Gamemode {;
    SKYWARS_SOLO_NORMAL("/play solo_normal");

    private String command;

    Gamemode(String cmd) {
        this.command = cmd;
    }
}

 */


