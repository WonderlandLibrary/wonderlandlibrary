package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.dependency.ModeDependency;
import best.azura.client.util.other.DelayUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;

@ModuleInfo(name = "Auto Play", category = Category.OTHER, description = "Play automatically on Hypixel")
public class AutoPlay extends Module {
    private DelayUtil autoPlayDelay = null;
    private final ModeValue gameType = new ModeValue("Game", "Game type", "Skywars", "Skywars", "Bedwars");
    private final ModeValue gameMode = new ModeValue("Game Mode", "Select What gamemode", new ModeDependency(gameType, "Skywars"), "Normal", "Normal", "Insane", "Ranked");
    private final ModeValue teamSize = new ModeValue("Team Size", "Select the team size", "Solo", "Solo", "Doubles", "3v3v3v3", "4v4v4v4");
    private final BooleanValue autoDetect = new BooleanValue("Auto Detect", "Automatically detect the gamemode", false);
    private static String currentGameMode = "NONE";
    @EventHandler
    public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {

        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat s = e.getPacket();
            String a = EnumChatFormatting.getTextWithoutFormattingCodes(s.getChatComponent().getUnformattedText());
            if (a.contains("You died!") || a.contains("1st Killer") || a.contains("You have been eliminated!")) {
                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Auto Play","Joining next game in 1 second",1000, Type.SUCCESS));
                autoPlayDelay = new DelayUtil();
                autoPlayDelay.reset();

            }
        }
    };

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = eventUpdate -> {
        if (autoDetect.getObject() && mc.thePlayer.ticksExisted < 20) {
            Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
            ScoreObjective scoreobjective = null;
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
            if (scoreplayerteam != null) {
                int j1 = scoreplayerteam.getChatFormat().getColorIndex();
                if (j1 >= 0) scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + j1);
            }
            scoreobjective = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
            if (scoreobjective != null) {
                scoreboard = scoreobjective.getScoreboard();
                Collection<Score> collection = scoreboard.getSortedScores(scoreobjective);
                for (Score score : collection) {
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(scoreobjective.getDisplayName()).contains("BED WARS"))
                        currentGameMode = "Bedwars";
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(scoreobjective.getDisplayName()).contains("THE HYPIXEL PIT"))
                        currentGameMode = "Pit";
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(scoreobjective.getDisplayName()).contains("SKYWARS"))
                        currentGameMode = "Skywars";
                    scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                    String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(scoreobjective.getDisplayName()).contains("BED WARS")) {
                        gameType.setObject("Bedwars");
                        if (EnumChatFormatting.getTextWithoutFormattingCodes(s).startsWith("Mode: ")) {
                            String mode = EnumChatFormatting.getTextWithoutFormattingCodes(s).replace("Mode: ", "");
                            if (mode.contains("Solo")) teamSize.setObject("Solo");
                            if (mode.contains("Doubles")) teamSize.setObject("Doubles");
                            if (mode.contains("3v3v3v3")) teamSize.setObject("3v3v3v3");
                            if (mode.contains("4v4v4v4")) teamSize.setObject("4v4v4v4");
                            break;
                        }
                    }
                    if (EnumChatFormatting.getTextWithoutFormattingCodes(scoreobjective.getDisplayName()).contains("SKYWARS")) {

                        gameType.setObject("Skywars");
                        String s1 = EnumChatFormatting.getTextWithoutFormattingCodes(s);
                        if (s1.startsWith("Players: ")) {

                            if (s1.contains("/24")) teamSize.setObject("Doubles");
                            if (s1.contains("/12")) teamSize.setObject("Solo");
                            if (s1.contains("/4")) gameMode.setObject("Ranked");
                            else gameMode.setObject("Insane");
                            String s2 = s1.replaceAll("[^a-zA-Z0-9\\-/.]", "");
                            if ((s2.contains("/10") && s2.endsWith("§a0")) || s2.contains("/100")) {
                                teamSize.setObject("Solo");

                            }
                            break;
                        }
                    }
                }
            }

        }
    };

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if (e.isUpdate()) {
            if (autoPlayDelay != null && autoPlayDelay.hasReached(1000)) {
                if (gameType.getObject().equalsIgnoreCase("Skywars")) {
                    if (gameMode.getObject().equalsIgnoreCase("Normal")) {
                        if (teamSize.getObject().equalsIgnoreCase("Solo"))
                            mc.thePlayer.sendChatMessage("/play solo_normal");
                        else mc.thePlayer.sendChatMessage("/play teams_normal");
                    } else if (gameMode.getObject().equalsIgnoreCase("Ranked")) {
                        mc.thePlayer.sendChatMessage("/play ranked_normal");
                    } else {
                        if (teamSize.getObject().equalsIgnoreCase("Solo"))
                            mc.thePlayer.sendChatMessage("/play solo_insane");
                        else mc.thePlayer.sendChatMessage("/play teams_insane");
                    }
                } else if (gameType.getObject().equalsIgnoreCase("Bedwars")) {
                    if (teamSize.getObject().equalsIgnoreCase("Solo"))
                        mc.thePlayer.sendChatMessage("/play bedwars_eight_one");
                    if (teamSize.getObject().equalsIgnoreCase("Doubles"))
                        mc.thePlayer.sendChatMessage("/play bedwars_eight_two");
                    if (teamSize.getObject().equalsIgnoreCase("3v3v3v3"))
                        mc.thePlayer.sendChatMessage("/play bedwars_four_three");
                    if (teamSize.getObject().equalsIgnoreCase("4v4v4v4"))
                        mc.thePlayer.sendChatMessage("/play bedwars_four_four");
                }
                autoPlayDelay.reset();
                autoPlayDelay = null;
            }
        }
    };


    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    public static String getCurrentGameMode() {
        return currentGameMode;
    }
}
