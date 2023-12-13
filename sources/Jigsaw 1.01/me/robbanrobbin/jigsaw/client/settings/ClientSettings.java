package me.robbanrobbin.jigsaw.client.settings;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;

public class ClientSettings {
	
	public static boolean hackerDetectAutoNotify = false;
	public static boolean hackerDetectMoreInfo = false;
	public static int TpAuraAPS = 6;
	public static double TpAurarange = 200;
	public static int TpAuramaxTargets = 50;
	public static double AutoClickerhitPercent = 0.8;
	public static int AutoClickermin = 1;
	public static int AutoClickermax = 10;
	public static double ExtendedReachrange = 6f;
	public static boolean flightkick = false;
	public static boolean Flightsmooth = false;
	public static double FlightdefaultSpeed = 1;
	public static double KillauraRange = 4.3;
	public static boolean KillauraHeadsnap = false;
	public static int KillauraAPS = 12;
	public static int MultiUseamount = 100;
	public static float Nametagssize = 5;
	public static int Nukerradius = 4;
	public static double Phasedistance = 1.5D;
	public static double Regenspeed = 200;
	public static double VanillaspeedFactor = 20;
	public static double Spinspeed = 30;
	public static double stepHeight = 1f;
	public static double TPrange = 1.5;
	public static double Timerspeed = 2;
	public static boolean chestStealerAura = false;
	public static boolean playerESP = true;
	public static boolean mobsESP;
	public static boolean animalESP;
	public static boolean blockHuntESP;
	public static boolean storageESP;
	public static boolean blockHuntTracers;
	public static boolean playerTracers = true;
	public static boolean mobsTracers;
	public static boolean animalTracers;
	public static boolean storageTracers;
	public static boolean mainMenuParticles = false;
	public static int clickGuiFontSize = 13;
	public static boolean clickGuiTint = true;
	public static boolean lockGuiScale = false;
	public static boolean espFade = true;
	public static double KBHorizontal = 0.0;
	public static double KBVertical = 0.0;
	public static int bgImage = 0;
	public static boolean notificationModulesEnable = true;
	public static boolean notificationModulesDisable = false;
	public static boolean notificationModuleError = true;
	public static double savePosHeight = 3.0;
	public static boolean glideDmg = false;
	public static boolean onGroundSpoofFlight = false;
	public static boolean chestStealDelay = true;
	public static double autoMineBlockLimit = 13;
	public static boolean areaMineFastBreak = false;
	public static boolean flightCubecraftKillAnticheat = true;
	public static boolean bigWaterMark = false;
	public static boolean tabGui = true;
	public static boolean smoothAim = false;
	public static double smoothAimSpeed = 2.5;
	public static int pathfinderMaxComputations = 2460;
	public static boolean ESPaccuracyColor = false;
	public static boolean betterBobbing = false;
	public static boolean swingAnimation = true;
	public static double NewTpAuraRange = 300;
	public static double NewTpAuraAPS = 2;
	public static double longJumpTimerSpeed = 0.7;
	public static double killauraHitRatio = 1.0;
	public static double Blockrange = 8.0;
	public static boolean autoSprintStopWhenHungry = true;
	public static double bunnyHopMaxSpeed = 0.05;
	public static float killauraSmoothRotsMaxRotation = 180;
	public static int killauraAttackDelay = 0;
	public static boolean hypixelFlightOffsetOnEnable = false;
	public static boolean sprintCrits = true;
	public static boolean useCooldown = true;
	public static boolean killauraUseRaytrace = false;
	public static int blurBufferFPS = 30;
	public static boolean enableBlur = true;
	public static boolean disableKillauraInInventory = false;
	public static boolean disableKillauraOnDeath = false;
	public static boolean showTargetsInSeperateWindow = true;
	public static boolean glassMode = false;
	public static double minimapTerrainSmoothness = 0.65;
	public static int triggerbotAPS = 10;
	public static double triggerbotHitRatio = 1.0;
	public static double triggerbotRange = 3.0;
	public static double elytraHackNCPSpeed = 1.0d;
	
	public static boolean fakeHackersSneak = false;
	public static boolean fakeHackersKillaura = true;
	public static boolean fakeHackersHeadless = false;
	public static boolean fakeHackersSpin = false;
	public static int fakeHackersKillauraSpeed = 10;

	public static boolean autoMineChangeFacingRandomly = true;
	public static boolean autoMineUseXRay = false;
	public static boolean autoMineEmeralds = true;
	public static boolean autoMineDiamonds = true;
	public static boolean autoMineGold = true;
	public static boolean autoMineLapisLazuli = true;
	public static boolean autoMineIron = true;
	public static boolean autoMineRedstone = true;
	public static boolean autoMineCoal = false;
	
	public static float guiColorForegroundR = 0.9f;
	public static float guiColorForegroundG = 0.2f;
	public static float guiColorForegroundB = 0.2f;

	public static void saveSettings() {
		JsonObject json = new JsonObject();
		for (Field field : ClientSettings.class.getFields()) {
			try {
				String toAdd = null;
				if (field.getType().getSimpleName().equals("boolean")) {
					boolean a = field.getBoolean(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
				if (field.getType().getSimpleName().equals("double")) {
					double a = field.getDouble(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
				if (field.getType().getSimpleName().equals("int")) {
					int a = field.getInt(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
				if (field.getType().getSimpleName().equals("float")) {
					float a = field.getFloat(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(Jigsaw.getFileMananger().settingsDir);
			fw.write(json.toString().replaceAll("\\s", ""));
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadSettings() {
		JsonElement obj = null;
		try {
			FileReader reader = new FileReader(Jigsaw.getFileMananger().settingsDir);
			obj = new JsonParser().parse(reader);
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (obj.isJsonNull()) {
			System.out.println("Json null");
			return;
		}
		if(obj.isJsonPrimitive()) {
			Jigsaw.getFileMananger().jigsawDir.delete();
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.ERROR, "All Jigsaw settings were reset to prevent crash."
					+ " If this message still pops up, please delete the 'Jigsaw' configuration folder in .minecraft!"));
			return;
		}
		JsonObject json = (JsonObject) obj;
		for (Field field : ClientSettings.class.getFields()) {
			try {
				if (field.getType().getSimpleName().equals("boolean")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsBoolean());
				}
				if (field.getType().getSimpleName().equals("double")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsDouble());
				}
				if (field.getType().getSimpleName().equals("int")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsInt());
				}
				if (field.getType().getSimpleName().equals("float")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsFloat());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public static Color getForeGroundGuiColor() {
		return new Color(guiColorForegroundR, guiColorForegroundG, guiColorForegroundB, 0.91f);
	}
	
	public static Color getForeGroundGuiColor(float alpha) {
		return new Color(guiColorForegroundR, guiColorForegroundG, guiColorForegroundB, alpha);
	}
	
	public static Color getSearchGuiColor() {
		return new Color(0.8f, 0.1f, 0.1f, 0.91f);
	}
	
	public static Color getBackGroundGuiColor() {
		if(!enableBlur) {
			return new Color(0.07f, 0.07f, 0.07f, 0.9f);
		}
		else {
			if(glassMode) {
				return new Color(0.05f, 0.05f, 0.05f, 0.3f);
			}
			else {
				return new Color(0.05f, 0.05f, 0.05f, 0.6f);
			}
		}
	}
	
	public static Color getBackGroundGuiColor(float alpha) {
		if(!enableBlur) {
			return new Color(0.07f, 0.07f, 0.07f, alpha);
		}
		else {
			if(glassMode) {
				return new Color(0.05f, 0.05f, 0.05f, alpha);
			}
			else {
				return new Color(0.05f, 0.05f, 0.05f, alpha);
			}
		}
	}

}
