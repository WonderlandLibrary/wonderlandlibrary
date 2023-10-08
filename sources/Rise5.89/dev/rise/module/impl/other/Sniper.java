/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModuleInfo(name = "Sniper", description = "Snipes the given user in the given game on Hypixel", category = Category.OTHER)
public final class Sniper extends Module {

    public static String username = "Rise";

    private final ModeSetting mode = new ModeSetting("Mode", this, "Skywars", "Skywars", "Bedwars");
    private final ModeSetting skywarsMode = new ModeSetting("Skywars Mode", this, "Solo", "Solo", "Doubles");
    private final ModeSetting skywarsType = new ModeSetting("Skywars Type", this, "Normal", "Normal", "Insane", "Ranked");
    private final ModeSetting bedwarsMode = new ModeSetting("Bedwars Mode", this, "Solo", "Solo", "Doubles", "Triples", "Quads");
    private final NumberSetting delay = new NumberSetting("Delay", this, 1000, 50, 10000, 100);

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onUpdateAlwaysInGui() {
        bedwarsMode.hidden = !mode.is("Bedwars");

        skywarsMode.hidden = !(mode.is("Skywars") && !skywarsType.is("Ranked"));

        skywarsType.hidden = !mode.is("Skywars");

        this.hidden = !(PlayerUtil.isOnServer("Hypixel") || mc.isSingleplayer());
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (!PlayerUtil.isOnServer("Hypixel") && !mc.isSingleplayer()) {
            this.registerNotification(this.getModuleInfo().name() + " only works on Hypixel.");
            this.toggleModule();
            return;
        }

        if (mc.isSingleplayer())
            return;

        final long delay = Math.round(this.delay.getValue() + (Math.random() * 100));

        for (final NetworkPlayerInfo info : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
            if (info.getGameProfile().getName() == null)
                continue;

            final String name = info.getGameProfile().getName();
            if (name.equalsIgnoreCase(username)) {
                this.registerNotification("Successfully sniped " + username + ".");
                this.toggleModule();
                return;
            }
        }

        if (timer.hasReached(delay)) {
            mc.thePlayer.sendChatMessage(getJoinCommand());
            timer.reset();
        }
    }

    private String getJoinCommand() {
        switch (mode.getMode()) {
            case "Skywars":
                switch (skywarsMode.getMode()) {
                    case "Solo":
                        return "/play solo_" + skywarsType.getMode().toLowerCase();

                    case "Doubles":
                        return "/play teams_" + skywarsType.getMode().toLowerCase();

                    case "Ranked":
                        return "/play ranked_normal";
                }
                break;

            case "Bedwars":
                switch (bedwarsMode.getMode()) {
                    case "Solo":
                        return "/play bedwars_eight_one";

                    case "Doubles":
                        return "/play bedwars_eight_two";

                    case "Triples":
                        return "/play bedwars_four_three";

                    case "Quads":
                        return "/play bedwars_four_four";
                }
                break;
        }
        return "/l";
    }
}