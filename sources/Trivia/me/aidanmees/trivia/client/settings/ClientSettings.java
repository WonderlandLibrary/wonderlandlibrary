package me.aidanmees.trivia.client.settings;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import net.minecraft.item.ItemStack;

public class ClientSettings {
	
	/*----------------------AUTOPOT------------------------*/
	public static double healthAutoPot = 3.0;
    public static long delayAutoPot = 1000;
    public static boolean potions = true;
    public static boolean upwards = false;
    public static boolean soup = true;
    public static boolean drop = false;
   
    
 
    
    /*----------------------AUTOPOT------------------------*/
	
	
	/*---------------------Killaura BETA--------------------*/
	
	public static boolean HypixelNofall = false;
	public static boolean TEAMMM = false;
	public static boolean players = true;
	public static boolean animals = false;
	public static boolean mobs = false;
	
	
	public static boolean invisibles = false;
	public static boolean block = true;
	public static boolean dura = false;
	public static boolean teams = false;
	public static boolean autoDisable = true;
	public static boolean GWEN = true;
	
	public static double rnd = 0.5;
	public static double random = 1.1;
	public static double apsBeta = 13.0D;
	public static double rangeBeta = 4.3D;
	public static double sps = 2.0D;
	/*---------------------Killaura BETA--------------------*/



	
	public static  boolean Disadvantage = false;
	public static  int MaxEntitys = 10;
	public static double distanceMis = 0.5D;


	public static boolean HOLOGRAM = false;
	public static boolean FireWork = false;
	public static boolean ITEMS = true;

	public static double  JitterStrength = 0.5D;




	public static double  MaxBHPS = 2.2D;




	public static double  MinBHPS = 1.8D;

    public static boolean CSGuiOutline = true;


	public static float AASpeed = 100.0f;
	
	public static boolean JitterClick = true;
	public static boolean BlockHitA = true;
	public static boolean InvFill = true;
	
	
	
	
	public static float HitBoxSize = 0.2f;
	public static double Reach = 3.3;
	public static boolean hackerDetectAutoNotify = false;
	public static boolean hackerDetectMoreInfo = false;
	public static double TpAuraAPS = 6;
	public static double TpAurarange = 200;
	public static int TpAuramaxTargets = 50;
	public static double AutoClickerhitPercent = 0.8;
	public static int AutoClickermin = 1;
	public static int AutoClickermax = 10;
	public static double ExtendedReachrange = 6f;
	public static boolean flightkick = false;
	public static boolean Flightsmooth = false;
	public static double FlightdefaultSpeed = 1;
	
	
	 
	public static boolean AAC = false;
	public static boolean skywars = false;
	public static boolean sortInventory = false;
	public static boolean itemSlots = false;
	public static boolean legitSorting = false;
	
	
	
	/*--------------TRIVIAS NEW AURA CHECKBOX----------------*/
	/*1*/public static boolean autoblockn = true;
	/*2*/public static boolean armorbreakern = false;
	/*3*/public static boolean playersn = true;
	/*4*/public static boolean animalsn = false;
	/*5*/public static boolean monstersn = false;
	/*6*/public static boolean invisiblesn = false;
	/*7*/public static boolean friendsn = false;
	/*8*/public static boolean noswingn = false;
	/*9*/public static boolean lockviewn = false;
	/*10*/public static boolean deathn = true;
	/*11*/public static boolean teamn = false;
	
	
	/*--------------TRIVIAS NEW AURA SLIDE----------------*/
	/*1*/public static double ticksn = 30;
	/*2*/public static double reachn = 4.4;
	/*3*/public static double FOVn = 360;
	/*4*/public static double APSn = 12;
	/*5*/public static double rndn = 4;
	/*6*/public static double sdelayn = 10;
	/*7*/public static double multiDelayn = 500;
	/*8*/public static double targetsn = 4;
	
	
	
	
	
	   /* 
   
   
    public Property<Sorting> sorting = new Property<>(this, "Sorting", Sorting.Crosshair);
    public Property<AuraMode> mode = new Property<>(this, "Mode", switchMode);
*/
	
	
	
	
	
	
	public static boolean PREDICT = true;
	public static boolean OVERPOT = true;
	public static boolean Regen = false;
	
	
	public static double RANGEBETA = 4.3;
	public static double DELAYBETA = 9;
	public static double PARTICALSIZE = 2;
	
	
	
	
	
	
	
	/*---------------------New dank madafaking killaura--------------------*/
	public static int attackRate = 12;
	public static double reach = 4.3;
	public static boolean criticals = false;
	public static double blockRange = 4.7;
	public static boolean antibot = true;
	public static boolean push = true;
	public static boolean PLAYERSS = true;
	public static boolean friendProtect = true;
	public static boolean MOBSS = true;
	
	
	/*   @Property(label = "Attack_rate", aliases = { "Attacks_Per_Second", "APS", "CPS", "Speed" })
    @Clamp(min = "1", max = "20")
    private int attackRate;
    @Property(label = "Reach", aliases = { "Range", "Distance" })
    @Clamp(min = "3", max = "8")
    @Increment("0.25")
    private double reach;
    @Property(label = "Comparator", aliases = { "Sort", "Sorter" })
    public EntityComparator entityComparator;
    @Property(label = "Criticals", aliases = { "Crits" })
    private boolean criticals;
    @Child("Criticals")
    @Property(label = "Critical_Mode", aliases = { "Crit_Mode" })
    private CriticalType criticalMode;
    @Property(label = "Auto_block", aliases = { "Block" })
    private boolean autoblock;
    @Child("Auto_block")
    @Property(label = "Range", aliases = { "Reach", "Distance" })
    @Clamp(min = "4", max = "10")
    @Increment("0.5")
    private double blockRange;
    @Property(label = "Anti_Bot", aliases = { "NoBot" })
    private boolean antibot;
    @Property(label = "Push", aliases = { "Wtap", "W-tap" })
    private boolean push;
    @Property(label = "Players", aliases = { "Humans" })
    private boolean players;
    @Child("Players")
    @Property(label = "Friend Protect", aliases = { "Friendly", "Friendlies" })
    private boolean friendProtect;
    @Property(label = "Mobs", aliases = { "Mob" })
    private boolean mobs;
   */ 
	
	
	
	/*---------------------New dank madafaking killaura--------------------*/
	
	
	
	
	public static boolean NOFRIENDSBETA = false;
	public static boolean AACBETA = false;
	public static boolean NOSWINGBETA = false;
	public static boolean NOSLOWBETA = true;
	public static boolean AUTOBLOCKBETA = true;
	public static boolean MAABETA = false;
	
	
	public static boolean Jump = true;
	
	
	public static double delayP = 27;
	
	
	public static double health = 4.0;
	
	
	
	
	
	
	
	
	
	public static double AimBotSpeed = 4.1;
	public static double AimBotMin = 7;
	public static double AimBotMax = 260;
	public static double AimBotRange = 4.6;
	
	public static boolean aimbot_Pitch = true;
	public static boolean ignoreTeams = false;
	public static boolean WeaponOnly = false;
	public static boolean MouseHold = true;
	
	public static double KillauraRange = 4.3;
    public static float ReachHack = 4.25f;
 
	public static boolean KillauraHeadsnap = false;
	public static boolean randomDelay = false;
	public static boolean noSwing = false;
	public static boolean autoblock = false;
	public static boolean TEAMSS = false;
	public static boolean ereach = false;
	public static boolean jumpcrit = false;
	public static int KillauraAPS = 12;
	public static float delay = 12f;
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
	public static boolean chestESP;
	public static boolean blockHuntTracers;
	public static boolean playerTracers = true;
	public static boolean mobsTracers;
	public static boolean animalTracers;
	public static boolean chestTracers;
	public static boolean mainMenuParticles = false;
	public static int clickGuiFontSize = 13;
	public static boolean clickGuiTint = true;
	public static boolean lockGuiScale = false;
	public static boolean espFade = true;
	public static int KBHorizontal = 0;
	public static int KBVertical = 0;
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
			FileWriter fw = new FileWriter(trivia.getFileMananger().settingsDir);
			fw.write(json.toString().replaceAll("\\s", ""));
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadSettings() {
		JsonElement obj = null;
		try {
			FileReader reader = new FileReader(trivia.getFileMananger().settingsDir);
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
			trivia.getFileMananger().triviaDir.delete();
			trivia.getNotificationManager().addNotification(new Notification(Level.ERROR, "All trivia settings werre reset to prevent crash."
					+ " If this message still pops up, please delete the 'trivia' folder in .minecraft!"));
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

	public static Color framebgColor1 = new Color(1f, 0.2f, 0.2f, 0.6f);
	public static Color frameHeadColor = new Color(1f, 0.3f, 0.3f, 1f);
	public static Color buttonSelectedColor = new Color(60, 60, 60, 200);
	public static Color guiBackgroundColor = new Color(50, 50, 50, 170);
	
	




	

}
