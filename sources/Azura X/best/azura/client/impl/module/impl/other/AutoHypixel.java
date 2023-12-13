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
import best.azura.client.impl.events.EventWorldChange;
import best.azura.client.impl.module.impl.render.Emotes;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.other.EmoteRequest;
import best.azura.client.util.other.ServerUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
@ModuleInfo(name = "Auto Hypixel", description = "Automatically manage azura on Hypixel.", category = Category.OTHER)
public class AutoHypixel extends Module {
	
	private final BooleanValue autoGG = new BooleanValue("Auto GG", "Automatically say GG after a game", true);
	private final NumberValue<Long> autoGGDelayValue = new NumberValue<>("Auto GG Delay", "Delay for automatically saying GG after a game",
			autoGG::getObject, 1000L, 250L, 0L, 5000L);
	private final BooleanValue autoQuickMaths = new BooleanValue("Auto Quick Maths", "Automatically solve quick maths (for pit)", false);
	private final BooleanValue autoQuitBan = new BooleanValue("Auto Quit On Ban", "Automatically go into the lobby when someone gets banned", true);
	private final BooleanValue autoLeaveParty = new BooleanValue("Auto Leave Party on Ban", "Automatically leave the party once a person got banned.", false);
	private final BooleanValue staffAnalyzer = new BooleanValue("Staff Analyzer", "Track the amount of staff bans", true);
	private final NumberValue<Long> staffAnalyzerDelay = new NumberValue<>("Staff Analyzer Delay", "Delay for checking staff status",
			staffAnalyzer::getObject, 60L, 1L, 1L, 60L);
	private final BooleanValue autoLanguage = new BooleanValue("Auto Language", "Automatically sets language to english", true);
	private final BooleanValue autoPlay = new BooleanValue("Auto Play", "Automatically join a new game", true);
	private final ModeValue gameType = new ModeValue("Game", "Game type", (val) -> {
		if (this.gameType.getObject().equals("Skywars")) this.teamSize.setObjects(new String[]{"Solo", "Doubles"});
		else this.teamSize.setObjects(new String[]{"Solo", "Doubles", "3v3v3v3", "4v4v4v4"});
	}, autoPlay::getObject, "Skywars", "Skywars", "Bedwars");
	private final BooleanValue checkHP = new BooleanValue("Check HP", "Prints out the HP difference (Duels)", false);
	private final ModeValue gameMode = new ModeValue("Game Mode", "Select What gamemode", () -> autoPlay.getObject() && gameType.getObject().equals("Skywars"), "Normal", "Normal", "Insane", "Ranked");
	private final ModeValue teamSize = new ModeValue("Team Size", "Select the team size", autoPlay::getObject, "Solo", "Solo", "Doubles", "3v3v3v3", "4v4v4v4");
	private final BooleanValue autoDetect = new BooleanValue("Auto Detect", "Automatically detect the gamemode", false);
	private final NumberValue<Long> autoPlayDelayValue = new NumberValue<>("Auto Play Delay", "Delay for automatically joining a game",
			autoPlay::getObject, 1000L, 250L, 0L, 5000L);
	public static boolean isCurrentlyInGame;
	private DelayUtil autoGGDelay = null, autoPlayDelay = null;
	private final DelayUtil delay = new DelayUtil(), apiDelay = new DelayUtil(), languageDelay = new DelayUtil();
	private int lastStaffBans = 0;
	private static String currentGameMode = "NONE", currentTeamSize = "NONE", currentGameType = "NONE", apiKey = "NONE", currentPitEvent = "NONE";
	public static final ArrayList<String> alreadyDiedPlayers = new ArrayList<>();
	public static int ticks = 0;
	
	@EventHandler
	public Listener<Event> eventListener = this::handle;
	
	public void handle(Event event) {
		if (!ServerUtil.isHypixel()) return;
		if (event instanceof EventWorldChange) {
			isCurrentlyInGame = false;
			alreadyDiedPlayers.clear();
		}
		if (event instanceof EventReceivedPacket) {
			EventReceivedPacket e = (EventReceivedPacket) event;
			if (e.getPacket() instanceof S02PacketLoginSuccess) {
				new Thread(() -> {
					try {
						Thread.sleep(500);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					mc.thePlayer.sendChatMessage("/api new");
				}).start();
			}
			if (e.getPacket() instanceof S02PacketChat) {
				S02PacketChat s = e.getPacket();
				String a = EnumChatFormatting.getTextWithoutFormattingCodes(s.getChatComponent().getUnformattedText()),
						b = a.replace(" ", "");
				if (b.contains("WINNER") && checkHP.getObject()) {
					Client.INSTANCE.getNotificationManager().addToQueue(new Notification("HvH", "Last health " + String.format("%.1f", mc.thePlayer.getHealth()).replace(",", ".") + " / 20.0", 2000, Type.INFO));
				}
				if (b.startsWith("1stKiller")) {
					if (autoGG.getObject()) {
						autoGGDelay = new DelayUtil();
						autoGGDelay.reset();
					}
					if (a.contains(mc.thePlayer.getName())) Emotes.addToQueue(new EmoteRequest(mc.thePlayer.getName(), "dab"), true);
				}
				if (!a.contains(":") && isCurrentlyInGame) {
					if (a.split(" ")[0].length() < 3)
						alreadyDiedPlayers.add(a.split(" ")[1]);
					else alreadyDiedPlayers.add(a.split(" ")[0]);
				}
				if (b.startsWith("Youdied!") || b.startsWith("1stKiller") || b.startsWith("Youhavebeeneliminated!")) {
					final double value = Math.round(autoPlayDelayValue.getObject() / 100.0) / 10.0;
					final String s1 = value + (value == 1 ? " second" : " seconds");
					Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Auto Play", "Joining next game in " + s1, autoPlayDelayValue.getObject(), Type.SUCCESS));
					autoPlayDelay = new DelayUtil();
					autoPlayDelay.reset();
				}
				if (autoQuitBan.getObject() && a.contains("A player has been removed from your game.")) {
					new Thread(() -> {
						Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Auto Quit On Ban",
								"Left the game due to someone being banned", 4000, Type.INFO));
						if (autoLeaveParty.getObject()) mc.thePlayer.sendChatMessage("/p leave");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}
						mc.thePlayer.sendChatMessage("/hub");
					}).start();
				}
				if (a.startsWith("QUICK MATHS! Solve: ") && mc.thePlayer != null && !a.contains(mc.thePlayer.getName()) && autoQuickMaths.getObject()) {
					try {
						String input = a.split("QUICK MATHS! Solve: ")[1].replace("x", "*");
						ScriptEngineManager mgr = new ScriptEngineManager();
						ScriptEngine engine = mgr.getEngineByName("JavaScript");
						mc.thePlayer.sendChatMessage(engine.eval(input).toString());
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
				String s0 = s.getChatComponent().getFormattedText();
				if (s0.startsWith("§aYour new API key is ")) {
					String s1 = EnumChatFormatting.getTextWithoutFormattingCodes(s0);
					apiKey = s1.split("Your new API key is ")[1];
					e.setCancelled(true);
				}
				if (EnumChatFormatting.getTextWithoutFormattingCodes(s0).startsWith("Selected language: ") && autoLanguage.getObject())
					e.setCancelled(true);
				if (a.contains("Cages opened! FIGHT!") || a.contains("Protect your bed and destroy the enemy beds.")) {
					isCurrentlyInGame = true;
				}
			}
		}
		if (event instanceof EventMotion) {
			EventMotion e = (EventMotion) event;
			if (e.isUpdate()) {
				ticks++;
				if (autoGG.getObject() && autoGGDelay != null && autoGGDelay.hasReached(autoGGDelayValue.getObject())) {
					mc.thePlayer.sendChatMessage("gg");
					autoGGDelay.reset();
					autoGGDelay = null;
				}
				if (autoPlayDelay != null && autoPlayDelay.hasReached(autoPlayDelayValue.getObject()) && autoPlay.getObject()) {
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
		}
		if (event instanceof EventUpdate) {
			if (mc.thePlayer.ticksExisted < 20) {
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
					currentGameMode = "NONE";
					currentPitEvent = "NONE";
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
							if (EnumChatFormatting.getTextWithoutFormattingCodes(s).startsWith("Mode: ")) {
								String mode = EnumChatFormatting.getTextWithoutFormattingCodes(s).replace("Mode: ", "");
								if (mode.contains("Solo")) currentTeamSize = ("Solo");
								if (mode.contains("Doubles")) currentTeamSize = ("Doubles");
								if (mode.contains("3v3v3v3")) currentTeamSize = ("3v3v3v3");
								if (mode.contains("4v4v4v4")) currentTeamSize = ("4v4v4v4");
								break;
							}
						}
						if (EnumChatFormatting.getTextWithoutFormattingCodes(scoreobjective.getDisplayName()).contains("SKYWARS")) {
							String s1 = EnumChatFormatting.getTextWithoutFormattingCodes(s);
							if (s1.startsWith("Players: ")) {
								if (s1.contains("/24")) currentTeamSize = ("Doubles");
								if (s1.contains("/12")) currentTeamSize = ("Solo");
								if (s1.contains("/4")) currentGameType = ("Ranked");
								else currentGameType = ("Insane");
								String s2 = s1.replaceAll("[^a-zA-Z0-9\\-/.]", "");
								if ((s2.contains("/10") && s2.endsWith("§a0")) || s2.contains("/100")) {
									currentTeamSize = ("Solo");
								}
								break;
							}
						}
						if (EnumChatFormatting.getTextWithoutFormattingCodes(scoreobjective.getDisplayName()).contains("THE HYPIXEL PIT")) {
							String s1 = EnumChatFormatting.getTextWithoutFormattingCodes(s);
							if (s1.startsWith("Event: ")) {
								if (s1.contains("/24")) currentTeamSize = ("Doubles");
								if (s1.contains("/12")) currentTeamSize = ("Solo");
								if (s1.contains("/4")) currentGameType = ("Ranked");
								else currentGameType = ("Insane");
								String s2 = s1.replaceAll("[^a-zA-Z0-9\\-/.]", "");
								if ((s2.contains("/10") && s2.endsWith("§a0")) || s2.contains("/100")) {
									currentTeamSize = ("Solo");
								}
								break;
							}
						}
					}
				}
				gameType.setObject(currentGameMode);
				gameMode.setObject(currentGameType);
				teamSize.setObject(currentTeamSize);
			}
			if (languageDelay.hasReached(30000) && autoLanguage.getObject()) {
				mc.thePlayer.sendChatMessage("/language english");
				languageDelay.reset();
			}
			if (delay.hasReached(staffAnalyzerDelay.getObject() * 1000) && staffAnalyzer.getObject()) {
				new Thread(() -> {
					try {
						if (apiKey.equals("NONE")) return;
						URL url = new URL("https://api.hypixel.net/punishmentstats?key=" + apiKey);
						BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
						for (String read; (read = in.readLine()) != null; ) {
							String line = StringUtils.substringAfterLast(read, "staff_total\"").replace("}", "").replace(":", "");
							if (lastStaffBans == 0) lastStaffBans = Integer.parseInt(line);
							else {
								final int bans = Integer.parseInt(line) - lastStaffBans;
								final String ending = " in the last" + (staffAnalyzerDelay.getObject() == 1 ? "" :
										" " + staffAnalyzerDelay.getObject()) + " second" +
										(staffAnalyzerDelay.getObject() == 1 ? "" : "s") + ".";
								if (bans > 0)
									Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Staff Activity", "Staff banned "
											+ bans + " player" + (bans == 1 ? "" : "s") + ending, 2000, (bans > 4 ? bans > 7 ? Type.ERROR : Type.WARNING : Type.INFO)));
								else
									Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Staff Activity",
											"Staff banned no players" + ending, 2000, Type.SUCCESS));
							}
							lastStaffBans = Integer.parseInt(line);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						mc.thePlayer.sendChatMessage("/api new");
					}
				}).start();
				delay.reset();
			}
		}
	}
	
	public static String getCurrentGameMode() {
		return currentGameMode;
	}
	
	public static String getCurrentGameType() {
		return currentGameType;
	}
	
	public static String getCurrentTeamSize() {
		return currentTeamSize;
	}
	
	public static String getApiKey() {
		return apiKey;
	}
	
	public static String getCurrentPitEvent() {
		return currentPitEvent;
	}
}