package wtf.evolution.module.impl.Player;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.text.TextFormatting;
import wtf.evolution.Main;
import wtf.evolution.editor.Drag;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPlayer;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.helpers.render.Translate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.notifications.NotificationType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ModuleInfo(name = "StaffAlert", type = Category.Player)
public class StaffAlert extends Module {

    public ArrayList<NetworkPlayerInfo> staff = new ArrayList<>();

    public Translate translate = new Translate(0,0);
    public Drag drag = Main.createDrag(this, "staff", 20, 60);

    @Override
    public void onDisable() {
        super.onDisable();
        staff.clear();
    }
    @EventTarget
    public void onPower(EventPlayer eventPlayer) {
        if (eventPlayer.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER && check(eventPlayer.getPlayerData().getDisplayName().getUnformattedText().toLowerCase())) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (check(mc.player.connection.getPlayerInfo(eventPlayer.getPlayerData().getProfile().getId()).getDisplayName().getUnformattedText().toLowerCase()))
                    Main.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " joined!", NotificationType.INFO);
            }).start();
        }

        if (eventPlayer.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
            for (NetworkPlayerInfo info : staff) {
                if (info.getGameProfile().getId().equals(eventPlayer.getPlayerData().getProfile().getId())) {
                    if (mc.player.connection.getPlayerInfo(eventPlayer.getPlayerData().getProfile().getId()).getGameProfile().getName() == null) {
                        staff.remove(info);
                        Main.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " leaved!", NotificationType.INFO);
                    }
                    else {
                        Main.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " spectator!", NotificationType.INFO);
                    }
                    break;
                }
            }
        }

    }


    public boolean check(String name) {
        return name.contains("helper") || name.contains("moder") || name.contains("admin") || name.contains("owner") || name.contains("curator") || name.contains("хелпер") || name.contains("модер") || name.contains("админ") || name.contains("куратор");
    }
}
