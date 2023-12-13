/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;

@ModuleInfo(name = "Spammer", description = "Spams a given message", category = Category.OTHER)
public final class Spammer extends Module {

    public static String customMessage = "Buy Rise client from intent.store!";

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Hypixel");
    private final ModeSetting lobby = new ModeSetting("Lobby", this, "Main", "Main", "Skywars", "Bedwars", "Duels", "UHC");
    private final NumberSetting delay = new NumberSetting("Delay", this, 1000, 50, 10000, 100);
    private final ModeSetting bypass = new ModeSetting("Bypass Mode", this, "Off", "Off", "Random");
    private final NumberSetting length = new NumberSetting("Bypass Length", this, 8, 1, 16, 1);

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onUpdateAlways() {
        lobby.hidden = !mode.is("Hypixel");

        length.hidden = !bypass.is("Random");
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("loyisa.cn")) {
            Rise.addChatMessage("This server is in a Spammer blacklist due to a request after console spam.");
            this.toggleModule();
            return;
        }

        final long delay = Math.round(this.delay.getValue());

        if (timer.hasReached(delay)) {
            final String message = customMessage;
            final StringBuilder builder = new StringBuilder();

            switch (bypass.getMode()) {
                case "Off": {
                    for (int i = 0; i < message.length(); i++) {
                        builder.append(message.charAt(i));
                    }
                }
                break;

                case "Random": {
                    builder.append(message).append(" ");

                    for (int i = 0; i < length.getValue(); i++) {
                        final String alphabet = "qwertyuiopasdfghjklzxcvbnm1234567890";
                        builder.append(alphabet.charAt(randomInt(0, alphabet.length())));
                    }
                    break;
                }
            }

            mc.thePlayer.sendChatMessage(builder.toString());
            if (mode.is("Hypixel"))
                mc.thePlayer.sendChatMessage(getLobbyCommand());
            timer.reset();
        }
    }

    private String getLobbyCommand() {
        switch (lobby.getMode()) {
            case "Skywars":
                return "/l s";

            case "Bedwars":
                return "/l b";

            case "Duels":
                return "/l duel";

            case "UHC":
                return "/l uhc";
        }
        return "/l main";
    }
}
