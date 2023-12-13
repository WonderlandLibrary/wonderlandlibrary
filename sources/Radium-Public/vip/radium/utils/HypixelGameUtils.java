package vip.radium.utils;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.network.play.server.S02PacketChat;
import vip.radium.RadiumClient;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.event.impl.world.ScoreboardHeaderChangeEvent;
import vip.radium.event.impl.world.ScoreboardModeChangeEvent;

public final class HypixelGameUtils {

    private static final HypixelGameUtils INSTANCE = new HypixelGameUtils();

    private static GameMode gameMode;

    // Defaults to prevent crash
    private static SkyWarsModePrefix skyWarsPrefix = SkyWarsModePrefix.SOLO;
    private static SkyWarsModeSuffix skyWarsSuffix = SkyWarsModeSuffix.INSANE;
    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        if (isSkyWars() && event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packetChat = (S02PacketChat) event.getPacket();
            final String chatMessage = packetChat.getChatComponent().getUnformattedText();
            if (chatMessage.equals("Teaming is not allowed on Solo mode!"))
                skyWarsPrefix = SkyWarsModePrefix.SOLO;
            else if (chatMessage.equals("Cross Teaming / Teaming with other teams is not allowed!"))
                skyWarsPrefix = SkyWarsModePrefix.TEAMS;
        }
    };

    @EventLink
    private final Listener<ScoreboardModeChangeEvent> onModeChange = event -> {
        for (SkyWarsModeSuffix suffix : SkyWarsModeSuffix.values()) {
            if (event.getMode().contains(suffix.name())) {
                skyWarsSuffix = suffix;
                return;
            }
        }
    };

    @EventLink
    private final Listener<ScoreboardHeaderChangeEvent> onHeaderChange = event -> {
        for (GameMode mode : GameMode.values()) {
            if (mode.getText().equals(event.getHeader())) {
                gameMode = mode;
                return;
            }
        }
    };

    private HypixelGameUtils() {
        RadiumClient.getInstance().getEventBus().subscribe(this);
    }

    public static GameMode getGameMode() {
        return ServerUtils.isOnHypixel() ? gameMode : GameMode.INVALID;
    }

    /**
     * TODO: XD This is the worst code in this client I promise, however to do it with
     * matching Strings to enum names would be slow so this will have to stay for now
     */
    public static SkyWarsMode getSkyWarsMode() {
        if (ServerUtils.isOnHypixel() && isSkyWars()) {
            switch (skyWarsPrefix) {
                case MEGA:
                    switch (skyWarsSuffix) {
                        default:
                            return SkyWarsMode.INVALID;
                        case NORMAL:
                            return SkyWarsMode.MEGA_NORMAL;
                        case DOUBLES:
                            return SkyWarsMode.MEGA_DOUBLES;
                    }
                case SOLO:
                    switch (skyWarsSuffix) {
                        case INSANE:
                            return SkyWarsMode.SOLO_INSANE;
                        case NORMAL:
                            return SkyWarsMode.SOLO_NORMAL;
                        default:
                            return SkyWarsMode.INVALID;
                    }
                default:
                    switch (skyWarsSuffix) {
                        case INSANE:
                            return SkyWarsMode.TEAMS_INSANE;
                        case NORMAL:
                            return SkyWarsMode.TEAMS_NORMAL;
                        default:
                            return SkyWarsMode.INVALID;
                    }
            }
        } else {
            return SkyWarsMode.INVALID;
        }
    }

    public static boolean hasGameStarted() {
        return isSkyWars() && getModeText().length() > 0;
    }

    private static String getModeText() {
        return GuiIngame.modeText;
    }

    public static boolean isSkyWars() {
        return gameMode == GameMode.SKYWARS;
    }

    public enum SkyWarsModePrefix {
        TEAMS,
        SOLO,
        MEGA
    }

    public enum SkyWarsModeSuffix {
        INSANE,
        NORMAL,
        DOUBLES
    }

    public enum SkyWarsMode {
        INVALID,
        SOLO_NORMAL,
        SOLO_INSANE,
        TEAMS_NORMAL,
        TEAMS_INSANE,
        RANKED_NORMAL,
        MEGA_NORMAL,
        MEGA_DOUBLES
    }

    public enum GameMode {
        INVALID(""),
        LOBBY("HYPIXEL"),
        BEDWARS("BEDWARS"),
        SKYWARS("SKYWARS"),
        BLITZ_SG("BLITZ SG"),
        PIT("THE HYPIXEL PIT"),
        UHC("HYPIXEL UHC"),
        DUELS("DUELS");

        private final String text;

        GameMode(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
