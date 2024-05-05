package ru.smertnix.celestial.feature.impl.misc;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPlayerState;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;

public class StaffStats extends Feature
{
    private boolean isJoined;
    private final Map<String, Integer> cachedPersonals = Maps.newHashMap();
    private final TimerHelper timerHelper = new TimerHelper();

    public StaffStats()
    {
        super("StaffStats", "Стафф Статистик", FeatureCategory.Util);
    }

    @EventTarget
    public void onEvent(EventPlayerState event) {
    	System.out.println("test");
        SPacketPlayerListItem.Action action = event.getAction();
        SPacketPlayerListItem.AddPlayerData data = event.getData();
        if (data != null && data.getDisplayName() != null) {
            data.getDisplayName().getFormattedText();
            if (data.getProfile().getName() != null) {
                String displayName = data.getDisplayName().getFormattedText();
                boolean havePerm = havePermission(data.getProfile(), displayName);
                if (havePerm) {
                    if (action == SPacketPlayerListItem.Action.ADD_PLAYER) {
                        cachedPersonals.put(data.getDisplayName().getFormattedText(), 0);
                    } else if (action == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                        cachedPersonals.remove(data.getDisplayName().getFormattedText());
                    }
                }
            }
        }
    }

    private List<String> getTabPlayers() {
        return GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(mc.player.connection.getPlayerInfoMap()).stream().map(playerInfo -> playerInfo.getDisplayName().getFormattedText()).collect(Collectors.toList());
    }

    @EventTarget
    private void onRender2D(EventRender2D e) {

        if (timerHelper.hasReached(1000)) {
            cachedPersonals.forEach((s, t) -> {
                if (getTabPlayers().contains(s))
                    cachedPersonals.replace(s, t+1);
            });
            timerHelper.reset();
        }

        int maxTextWidth = 0;
        maxTextWidth += mc.rubik_16.getStringWidth(cachedPersonals.keySet().stream().sorted(Comparator.comparingDouble(text -> -mc.rubik_16.getStringWidth(text))).findFirst().get());
        maxTextWidth += mc.rubik_16.getStringWidth(formatNonZero(cachedPersonals.values().stream().sorted(Comparator.comparingDouble(time -> -mc.rubik_16.getStringWidth(formatNonZero(time-10)))).findFirst().get()));

        double w = maxTextWidth+12;
        double h = 15+(cachedPersonals.values().stream().filter(v -> v > 10).collect(Collectors.toList()).size()*9);
        RenderUtils.drawRect(0, 0, w, h + 2,  new Color(255,255,255).getRGB());
        for (int i = 0; i < w-2; i++)
        mc.rubik_16.drawString("Staff Statistics", 152, 5, -1);
        int staffY = 15;
        for (String name : cachedPersonals.keySet()) {
            int time = cachedPersonals.get(name);
            if (time < 10) continue;
            mc.rubik_16.drawString(name, 3, staffY, -1);
            mc.rubik_16.drawString("§l"+formatNonZero(time-10),(float) (w-mc.rubik_16.getStringWidth(formatNonZero(time-1))-1), staffY, -1);
            staffY += 9;
        }
    }

    private String formatNonZero(long seconds) {
        int hours = (int) (seconds / 3600);
        int minutes = (int) (seconds % 3600 / 60);
        int sec = (int) (seconds % 60);

        StringBuilder result = new StringBuilder();
        if (hours > 0) result.append(hours+"h").append(" ");
        if (minutes > 0) result.append(minutes+"m").append(" ");
        if (sec > 0) result.append(sec+"s").append(" ");

        return result.toString();
    }

    private boolean havePermission(GameProfile gameProfile, String displayName) {
        StringBuilder builder = new StringBuilder();
        char[] buffer = displayName.toCharArray();
        for (int i = 0; i < buffer.length; i++) {
            char c = buffer[i];
            if (c == '§') {
                i++;
            } else {
                builder.append(c);
            }
        }
        return havePermissionFixed(builder.toString().toLowerCase().replace(gameProfile.getName().toLowerCase(), ""));
    }

    private boolean havePermissionFixed(String displayName) {
        return displayName.contains("helper") || displayName.contains("хелпер") || displayName.contains("модер")
                || displayName.contains("moder") || displayName.contains("куратор") || displayName.contains("админ")
                || displayName.contains("admin");
    }

    void log(String msg) {
//        ChatHelper.addChatMessage(msg);
//        NotificationManager.publicity("StaffAlert", msg, 5, NotificationType.INFO);
    }

}