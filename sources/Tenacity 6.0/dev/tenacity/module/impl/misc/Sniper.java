// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.event.impl.game.WorldEvent;
import java.util.Iterator;
import dev.tenacity.utils.misc.Multithreading;
import java.util.concurrent.TimeUnit;
import dev.tenacity.utils.player.ChatUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.server.ServerUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.Module;

public class Sniper extends Module
{
    public final StringSetting username;
    private final ModeSetting gameType;
    private final ModeSetting skywarsMode;
    private final ModeSetting skywarsType;
    private final ModeSetting bedwarsMode;
    private final NumberSetting joinDelay;
    private final TimerUtil timer;
    boolean reset;
    
    public Sniper() {
        super("Sniper", "Sniper", Category.MISC, "Joins new games until you join a game with the specified username in it.");
        this.username = new StringSetting("Target");
        this.gameType = new ModeSetting("Game Type", "Skywars", new String[] { "Skywars", "Bedwars" });
        this.skywarsMode = new ModeSetting("Skywars Mode", "Solo", new String[] { "Solo", "Doubles" });
        this.skywarsType = new ModeSetting("Skywars Type", "Normal", new String[] { "Normal", "Insane", "Ranked" });
        this.bedwarsMode = new ModeSetting("Bedwars Mode", "Solo", new String[] { "Solo", "Doubles", "Triples", "Quads" });
        this.joinDelay = new NumberSetting("Join Delay", 3.0, 10.0, 2.0, 0.5);
        this.timer = new TimerUtil();
        this.reset = false;
        this.skywarsMode.addParent(this.gameType, modeSetting -> modeSetting.is("Skywars"));
        this.skywarsType.addParent(this.gameType, modeSetting -> modeSetting.is("Skywars"));
        this.bedwarsMode.addParent(this.gameType, modeSetting -> modeSetting.is("Bedwars"));
        this.addSettings(this.username, this.joinDelay, this.gameType, this.skywarsMode, this.skywarsType, this.bedwarsMode);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (event.isPre()) {
            if (!ServerUtils.isGeniuneHypixel() || Sniper.mc.isSingleplayer()) {
                NotificationManager.post(NotificationType.WARNING, "Error", "This module only works on Hypixel servers.");
                this.toggleSilent();
                return;
            }
            for (final NetworkPlayerInfo netPlayer : Sniper.mc.thePlayer.sendQueue.getPlayerInfoMap()) {
                if (netPlayer.getGameProfile().getName() == null) {
                    continue;
                }
                final String name = netPlayer.getGameProfile().getName();
                if (name.equalsIgnoreCase(this.username.getString())) {
                    NotificationManager.post(NotificationType.SUCCESS, "Success", "Found target!");
                    this.toggle();
                    return;
                }
            }
            if (this.reset) {
                Multithreading.schedule(() -> ChatUtil.send(this.getJoinCommand()), this.joinDelay.getValue().longValue(), TimeUnit.SECONDS);
                this.reset = false;
            }
        }
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        if (event instanceof WorldEvent.Load) {
            this.reset = true;
        }
    }
    
    private String getJoinCommand() {
        final String mode = this.gameType.getMode();
        int n = -1;
        switch (mode.hashCode()) {
            case -467898612: {
                if (mode.equals("Skywars")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 1433239148: {
                if (mode.equals("Bedwars")) {
                    n = 1;
                    break;
                }
                break;
            }
        }
        Label_0432: {
            switch (n) {
                case 0: {
                    final String mode2 = this.skywarsMode.getMode();
                    switch (mode2) {
                        case "Solo": {
                            return "/play solo_" + this.skywarsType.getMode().toLowerCase();
                        }
                        case "Doubles": {
                            return "/play teams_" + this.skywarsType.getMode().toLowerCase();
                        }
                        case "Ranked": {
                            return "/play ranked_normal";
                        }
                        default: {
                            break Label_0432;
                        }
                    }
                    break;
                }
                case 1: {
                    final String mode3 = this.bedwarsMode.getMode();
                    switch (mode3) {
                        case "Solo": {
                            return "/play bedwars_eight_one";
                        }
                        case "Doubles": {
                            return "/play bedwars_eight_two";
                        }
                        case "Triples": {
                            return "/play bedwars_four_three";
                        }
                        case "Quads": {
                            return "/play bedwars_four_four";
                        }
                        default: {
                            break Label_0432;
                        }
                    }
                    break;
                }
            }
        }
        return "/l";
    }
}
