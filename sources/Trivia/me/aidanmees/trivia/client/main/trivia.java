package me.aidanmees.trivia.client.main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.AntiCrash.AntiCrash;
import me.aidanmees.trivia.client.alts.AltManager;
//import me.aidanmees.trivia.client.bot.BotManager;
//import me.aidanmees.trivia.client.bot.account.AccountManager;
//import me.aidanmees.trivia.client.bot.manager.SettingsManager;
import me.aidanmees.trivia.client.bungeehack.GuiBungeeCord;
import me.aidanmees.trivia.client.chat.ChatMananger;
import me.aidanmees.trivia.client.commands.Command;
import me.aidanmees.trivia.client.commands.CommandManager;
import me.aidanmees.trivia.client.events.BlockPlaceEvent;
import me.aidanmees.trivia.client.events.BoundingBoxEvent;
import me.aidanmees.trivia.client.events.EntityHitEvent;
import me.aidanmees.trivia.client.events.EntityInteractEvent;
import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.gui.tab.TabGui;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.modules.Combat.BowAimbot;
import me.aidanmees.trivia.client.modules.Combat.InfiniteReach;

import me.aidanmees.trivia.client.presets.PresetMananger;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.target.AuraUtils;
import me.aidanmees.trivia.client.tools.LoadTools;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.cracker.PortscanManager;
import me.aidanmees.trivia.files.FileMananger;
import me.aidanmees.trivia.friends.FriendsMananger;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;
import me.aidanmees.trivia.gui.NotificationManager;
import me.aidanmees.trivia.gui.UIRenderer;
import me.aidanmees.trivia.gui.custom.SearchBar;
import me.aidanmees.trivia.gui.custom.clickgui.ClickGuiManager;
import me.aidanmees.trivia.gui.custom.clickgui.DisplayClickGui;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.module.Module;
import me.aidanmees.trivia.module.group.ModuleGroup;
import me.aidanmees.trivia.module.group.ModuleGroupMananger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class trivia {
	

	public static me.aidanmees.trivia.client.Clickgui.settings.SettingsManager setmgr;
	
	
	public static String loggedInName = null;
	public static boolean java8 = true;
	public static boolean promtedUpdate = false;
	public static boolean promtedAlert = false;
	public static boolean upToDate = true;
	public static boolean triedConnectToUpdate = false;
	public static boolean triedConnectToAlert = false;
	public static boolean changelogFailed = false;
	public static boolean customButtons = false;
	public static Elements changeLineElmts;
	public static String motd = null;
	public static boolean devVersion = false;
	private static String clientName = "Remix";
	public static String registeredUser = null;
	private static String clientVersion = "B1";
	public static String serverVersion = null;
	private static String clientAuthor = "Aidan & Mees";
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ClickGuiManager clickGuiManager;
	private static UIRenderer uiRenderer;
	private static FileMananger fileMananger;
	private static Document doc;
	 static boolean LoggedIn = false;
	private static PresetMananger presetMananger;
	public static FriendsMananger friendsMananger;
	private static ChatMananger chatMananger;
	private static ModuleGroupMananger moduleGroupMananger;
	private static CommandManager commandManager;
	private static PortscanManager crackManager;
	
	private static NotificationManager notificationManager;
	private static AltManager altManager;
	  public static AntiCrash antiCrash = new AntiCrash();
	  public static ModSetting modsetting;
	private static TabGui tabGui;
	
	public static CopyOnWriteArrayList<Module> modules;
	public static boolean loaded = false;
	public static boolean performanceBoost = true;
	public static boolean debugMode = false;
	//public static BotManager botManager;
	// public static ArrayList<Module> toggledList = new ArrayList<Module>();
	private static Robot clickRealRobot;
	public static ArrayList<String> mouseNames = new ArrayList<String>();
	public static ArrayList<String> nameTagNames = new ArrayList<String>();
	public static HashMap<String, String> devTagNames = new HashMap<String, String>();
	public static String[] proxy = null;
	public static boolean welcomed = false;
	public static boolean firstStart = false;
	static String property;
    static String serial;

	public static final ResourceLocation triviaTexture512 = new ResourceLocation("trivia/trivia512.png");
	public static final ResourceLocation triviaTexture1024x512 = new ResourceLocation("trivia/trivia1024x512.png");
	public static final ResourceLocation triviaTexture1024 = new ResourceLocation("trivia/trivia1024.png");
	public static ResourceLocation[] images = null;
	public static final ResourceLocation triviaCookie = new ResourceLocation("trivia/cookie.png");
	public static final ResourceLocation triviaImage2 = new ResourceLocation("trivia/MainMenuImage-1Blur.png");
	public static final ResourceLocation triviaImage4 = new ResourceLocation("trivia/MainMenuImage0Blur.png");
	public static final ResourceLocation triviaImage5 = new ResourceLocation("trivia/MainMenuImage-1.png");
	public static final ResourceLocation triviaImage6 = new ResourceLocation("trivia/MainMenuImage0.jpg");
	
	
	
	
	
	public static final ResourceLocation singleleplayer = new ResourceLocation("trivia/singleplayer.png");
	public static final ResourceLocation settings = new ResourceLocation("trivia/settings.png");
	public static final ResourceLocation multiplayer = new ResourceLocation("trivia/multiplayer.png");
	public static final ResourceLocation exit = new ResourceLocation("trivia/exit.png");
	public static final ResourceLocation altmanager = new ResourceLocation("trivia/altmanager.png");

	
	private static WaitTimer tpsTimer = new WaitTimer();

   // public static AccountManager accountManager;
   // public static SettingsManager settingsManager;
    
    public static boolean protocol;
    
	public static double lastTps = 20.0;
	
	public static final ArrayList<ResourceLocation> gifLocations = new ArrayList<ResourceLocation>();
	
	public static final WaitTimer saveTimer = new WaitTimer();
	public static File jarFile = null;
	public static boolean gifMenu;
	
	
	private static Random rand = new Random();

	/**
	 * 
	 * Disables the GUI, the UI, and all Chat Messages
	 * 
	 */
	

	public static void Register() {
		if(rand.nextInt(100) == 50) {
			trivia.gifMenu = true;
		}
		try {
			jarFile = new File(trivia.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		for(int i = 0; i < 282; i++) {
			gifLocations.add(new ResourceLocation("trivia/gif/frame_" + String.valueOf(i) + "_delay-0.04s.png"));
		}
		LoadTools.doAntiLeak();
			
		images = new ResourceLocation[] { triviaCookie, triviaImage2, triviaImage5, triviaImage4 , triviaImage6};
		modules = new CopyOnWriteArrayList<Module>();
		fileMananger = new FileMananger();
		try {
			fileMananger.load();
		} catch (IOException e) {
			System.out.println("File error!");
			e.printStackTrace();
			System.exit(-1);
		}

		notificationManager = new NotificationManager();
		getFileMananger().loadSettings();
		if (ClientSettings.lockGuiScale || Minecraft.getMinecraft().gameSettings.guiScale == 0) {
			Minecraft.getMinecraft().gameSettings.guiScale = 2;
		}
		modules = LoadTools.addModules();
		
		uiRenderer = new UIRenderer();
		//botManager = new BotManager();

		clickGuiManager = new ClickGuiManager();
		clickGuiManager.setup();
		setmgr = new me.aidanmees.trivia.client.Clickgui.settings.SettingsManager();
		presetMananger = new PresetMananger();
		friendsMananger = new FriendsMananger();
		chatMananger = new ChatMananger();
	//accountManager = new AccountManager();
		//settingsManager = new SettingsManager();
		commandManager = new CommandManager();
		
		altManager = new AltManager();
		
		
		tabGui = new TabGui();

		// Default module groups!
		moduleGroupMananger = new ModuleGroupMananger();
		moduleGroupMananger.addGroup(new ModuleGroup(new Module[] { 
				trivia.getModuleByName("KillauraBETA"),trivia.getModuleByName("HvHAura"),
			
				trivia.getModuleByName("InfiniteReach"), 
				 trivia.getModuleByName("Aimbot") }, "AuraGroup"));

		moduleGroupMananger.addGroup(new ModuleGroup(
				new Module[] { trivia.getModuleByName("Regen") }, "RegenGroup"));

		moduleGroupMananger.addGroup(new ModuleGroup(
				new Module[] { trivia.getModuleByName("Flight"), trivia.getModuleByName("AirJump") }, "AirGroup"));
		try {
			clickRealRobot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		getFileMananger().loadModules();
		for (Module m : getModules()) {
			m.onClientLoad();
		}
		
		getFileMananger().loadGUI(clickGuiManager.windows);
		getFileMananger().loadFriends();

		

		if(trivia.firstStart) {
			System.out.println("trivia first start!");
		}
		
		loaded = true;
	}
	public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "-";
            }
            i++;
        }
        return s;
    }


	
	public static boolean IsWhitelisted(String string) {
		
        
		 final String url = "https://pastebin.com/PYaEEc2v";
        try {
            doc = Jsoup.connect(url).get();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        for (final Element element : doc.select("meta[property=og:title]")) {
            final String meme = element.toString();
            final String Replace = meme.replaceAll("<meta property=\"og:title\" content=\"", "");
            final String Replace2 = Replace.replaceAll(" - Pastebin.com\">", "");
            final String[] parts = Replace2.split("  ");
            for (int i = 0; i < parts.length; ++i) {
           	
                if (parts[i].equals(string)) {
                    LoggedIn = true;
                    return true;
                    
                }
            }
        }
        if (!LoggedIn) {
            return false;
        }
       return false;
	}
	
	public static void saveSettings() {
		getFileMananger().saveModules();
		//TODO fix this shit
		getFileMananger().saveGUI(trivia.clickGuiManager.windows);
		getFileMananger().saveFriends();
		getFileMananger().saveSettings();
		getFileMananger().saveAlts();
	}

	public static void onMCClose() {
		if (trivia.loaded) {
			saveSettings();
		}
	}

	public static void setToggledAllModules(boolean enabled) {
		for (Module m : trivia.getModules()) {
			m.setToggled(enabled, true);
		}
	}

	public static int getModuleCount() {
		return modules.size();
	}

	public static void appendModule(Module module) {
		// TODO Module autotoggle
		modules.add(module);
		if ( module.getName() != "Friends") {
			module.setToggled(true, true);
		}
	}

	public static final Category[] defaultCategories = new Category[] { Category.AUTOMATION, Category.COMBAT,
			Category.MOVEMENT, Category.MISC, Category.RENDER, Category.LEGIT, Category.MINIGAMES, Category.PLAYER,
			Category.EXPLOITS, Category.WORLD };

	public static ArrayList<Module> getModules() {

		ArrayList<Module> mods = new ArrayList<Module>();
		for (Module m : modules) {
			mods.add(m);
		}
		return mods;
	}

	public static ArrayList<Module> getToggledModules() {
		ArrayList<Module> a = new ArrayList<Module>();
		for (Module m : modules) {
			if (m.isToggled()) {
				a.add(m);
			}
		}
		return a;
	}

	public static ArrayList<Object> getToggledModulesObject() {
		ArrayList<Object> a = new ArrayList<Object>();
		for (Module m : modules) {
			if (m.isToggled()) {
				a.add(m);
			}
		}
		return a;
	}

	/**
	 * Gets an trivia module by its name
	 * 
	 * @param name
	 *            The name
	 * @return The module which name was specified
	 */
	public static Module getModuleByName(String name) {
		Iterator<Module> iter = modules.iterator();
		while (iter.hasNext()) {
			Module mod = iter.next();
			if (mod.getName().equalsIgnoreCase(name)) {
				return mod;
			}
		}
		System.err.println("Module: " + name + " was not found");
		return null;
	}

	public static boolean isModuleName(String name) {
		for (Module m : modules) {
			if (m.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Filters modules
	 * 
	 * @param modules
	 * @param categories
	 *            Modules must be this category
	 * @param r
	 *            ReturnType. The modules name, keyboard, or the module
	 * @return An ArrayList of the filtered modules
	 */
	public static ArrayList<Object> filterModulesByCategory(ArrayList<Module> modules, Category[] categories,
			ReturnType r) {
		ArrayList<Object> filtered = new ArrayList<Object>();
		for (Module m : modules) {
			for (Category c : categories) {
				if (m.getCategory() == c) {
					if (r == ReturnType.MODULE) {
						filtered.add(m);
					}
					if (r == ReturnType.NAME) {
						filtered.add(m.getName());
					}
					if (r == ReturnType.KEYBOARD) {
						filtered.add(m.getKeyboardKey());
					}

					break;
				}
			}
		}
		return filtered;
	}

	public static String getClientName() {
		return clientName;
	}

	public static String getClientVersion() {
		return clientVersion;
	}

	public static String getClientAuthor() {
		return clientAuthor;
	}

	public static UIRenderer getUIRenderer() {
		return uiRenderer;
	}

	public static FileMananger getFileMananger() {
		return fileMananger;
	}

	public static PresetMananger getPresetMananger() {
		return presetMananger;
	}

	public static FriendsMananger getFriendsMananger() {
		return friendsMananger;
	}

	public static ChatMananger getChatMananger() {
		return chatMananger;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static ModuleGroupMananger getModuleGroupMananger() {
		return moduleGroupMananger;
	}

	public static PortscanManager getCrackManager() {
		return crackManager;
	}

	public static void setCrackManager(PortscanManager crackManager) {
		trivia.crackManager = crackManager;
	}

	public static void onError(Exception e, ErrorState errorState, Module module) {
		try {
			if (module != null) {
				try {
					System.err.println("Module: " + module.getName() + " ecountered an exception! /n"
							+ "Disabling module without update...");
					module.setToggled(false, false);
					e.printStackTrace();
				} catch (Exception e2) {
					System.err.println("Could not disable module...");

				}

			}
			getNotificationManager().addNotification(new Notification(Level.ERROR,
					e.getMessage() + ", at line: " + e.getStackTrace()[0].getLineNumber() + ", in class: "
							+ e.getStackTrace()[0].getFileName() + ", in method: "
							+ e.getStackTrace()[0].getMethodName()));

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static enum ErrorState {

		onRender("rendering"), onTick("ticking"),  onUpdate("updating"), onEnable("enabling"), onDisable("disabling"), onMessage(
				"recieving message"), onLeftClick("recieving leftclick"), onRightClick(
						"recieving rightclick"), onConstruct("contructing"), onPacketRecieved(
								"recieving packet"), onPacketSent("sending packet"), onEntityHit(
										"hitting entity"), onLateUpdate("updating (post)"), onGui(
												"rendering gui"), onPostMotion("updating (postMotion)"), onPreMotion(
														"updating (preMotion)"), onBasicUpdates(
																"updating (basicUpdates)"), onBlockPlace(
																		"placing block"), onEntityInteract(
																				"interacting with entity"), onBoundingBox(
																						"setting boundingbox"), onLivingUpdate(
																								"updating (living)"), onDestroyEntities( "onDestroyEntities"), onRelativeMove("onRelativeMove"), onEntityTeleport("onEntityTeleport");

		String text;

		ErrorState(String displayText) {
			this.text = displayText;
		}

		public String getDisplayText() {
			return this.text;
		}

	}

	public static void onEntityHit(EntityHitEvent entityHitEvent) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onEntityHit(entityHitEvent);
			} catch (Exception e) {
				onError(e, ErrorState.onEntityHit, module);
			}
		}
	}
	
	public static void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onBlockPlace(blockPlaceEvent);
			} catch (Exception e) {
				onError(e, ErrorState.onBlockPlace, module);
			}
		}
	}

	public static void onLeftClick() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onLeftClick();
			} catch (Exception e) {
				onError(e, ErrorState.onLeftClick, module);
			}
		}
	}

	public static void onRightClick() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onRightClick();
			} catch (Exception e) {
				onError(e, ErrorState.onRightClick, module);
			}
		}
	}

	public static void onChatMessageSent(C01PacketChatMessage packet) {
		try {
			chatMananger.onMessageSent(packet);
		} catch (Exception e) {
			onError(e, ErrorState.onMessage, null);
		}
	}

	public static void onChatMessageRecived(S02PacketChat packetIn) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onChatMessageRecieved(packetIn);
			} catch (Exception e) {
				onError(e, ErrorState.onMessage, module);
			}
		}
	}
	public static void onTick() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onLeftClick();
			} catch (Exception e) {
				onError(e, ErrorState.onTick, module);
			}
		}
	}

	public static void onKeyPressed(int keyCode) {
		if(getTabGui() != null) {
			getTabGui().onKeyPressed(keyCode);
		}
		for (Module module : getModules()) {
			if (module.getKeyboardKey() == keyCode) {
				module.toggle();
			}
		}
	}

	public static void OnUpdate(UpdateEvent event) {
		Utils.spectator = false;
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if(getTabGui() != null) {
			getTabGui().update();
		}
		if (mc.currentScreen instanceof GuiDownloadTerrain) {
			Utils.blackList.clear();
		}
		Utils.updateLastGroundLocation();
		if (registeredUser == null) {
			mc.shutdown();
		}
		if (getCrackManager() != null) {
			getCrackManager().onUpdate();
		}
		if (!trivia.welcomed) {
			trivia.getNotificationManager().addNotification(new Notification(Level.INFO,
					"Press left ctrl to open the click gui. Do .commands for a list of all commands!"));
			trivia.welcomed = true;
		}
		getNotificationManager().update();
		for (Module module : getToggledModules()) {
			try {
				ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
				if (group != null
						&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
						&& AuraUtils.getDisableAura()) {
					continue;
				}
				module.onUpdate(event);
				module.onUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onUpdate, module);
			}
		}
		if(saveTimer.hasTimeElapsed(100000, true)) {
			saveSettings();
		}
	}
	
	
	
	
	
	public static void onDestroyEntities(S13PacketDestroyEntities packet) {
		 if ((packet instanceof S13PacketDestroyEntities))
	        {
	          packet = (S13PacketDestroyEntities)packet;
	          for (Module module : getToggledModules()) {
	  			try {
	  				ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
	  				if (group != null
	  						&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
	  						&& AuraUtils.getDisableAura()) {
	  					continue;
	  				}
	  				module.onDestroyEntities(packet);
	  				
	  			} catch (Exception e) {
	  				onError(e, ErrorState.onDestroyEntities, module);
	  			
	  		}
	          }
	        }
		
		
	}
	public static void onEntityTeleport(S18PacketEntityTeleport packet) {
		 if ((packet instanceof S18PacketEntityTeleport))
	        {
	          packet = (S18PacketEntityTeleport)packet;
	          for (Module module : getToggledModules()) {
	  			try {
	  				ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
	  				if (group != null
	  						&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
	  						&& AuraUtils.getDisableAura()) {
	  					continue;
	  				}
	  				module.onEntityTeleport(packet);
	  				
	  			} catch (Exception e) {
	  				onError(e, ErrorState.onEntityTeleport, module);
	  			
	  		}
	          }
	        }
		
		
	}

	public static void onGui(boolean renderModules) {
		if(mc == null) {
			return;
		}
		

		getNotificationManager().draw();
		if (getUIRenderer() != null && !mc.gameSettings.showDebugInfo) {
			try {
				trivia.getUIRenderer().render(renderModules);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		for (Module module : getToggledModules()) {
			try {
				module.onGui();
			} catch (Exception e) {
				onError(e, ErrorState.onGui, module);
			}

		}
	}
	
	public static void onPreLateUpdate() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPreLateUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLateUpdate, module);
			}

		}
	}

	public static void OnLateUpdate() {
		onPreLateUpdate();
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onLateUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLateUpdate, module);
			}

		}
	}

	public static void onPostMotion() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPostMotion();
			} catch (Exception e) {
				onError(e, ErrorState.onPostMotion, module);
			}

		}
	}

	public static void onPreMotion(PreMotionEvent event) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPreMotion(event);
			} catch (Exception e) {
				onError(e, ErrorState.onPreMotion, module);
			}

		}
	}

	public static void onBasicUpdates() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onBasicUpdates();
			} catch (Exception e) {
				onError(e, ErrorState.onBasicUpdates, module);
			}

		}
	}

	public static void OnRender() {
		
			for (Module module : getToggledModules()) {
				ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
				if (group != null
						&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
						&& AuraUtils.getDisableAura()) {
					continue;
				}
				try {
					module.onRender();
				} catch (Exception e) {
					onError(e, ErrorState.onRender, module);
				}
			}
		}
	
	
	private static ArrayList<Long> times = new ArrayList<Long>();

	


	public static void onPacketRecieved(AbstractPacket modPacket) {
		if(modPacket instanceof S03PacketTimeUpdate) {
			times.add(Math.max(1000, tpsTimer.getTime()));
			long timesAdded = 0;
			if(times.size() > 5) {
				times.remove(0);
			}
			for(long l : times) {
				timesAdded += l;
			}
			long roundedTps = timesAdded / times.size();
			lastTps = (20.0 / roundedTps) * 1000.0;
			tpsTimer.reset();
		}
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPacketRecieved(modPacket);
			} catch (Exception e) {
				onError(e, ErrorState.onPacketRecieved, module);
			}
		}
	}

	public static void onPacketSent(AbstractPacket packet) {
		
		
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPacketSent(packet);
			} catch (Exception e) {
				onError(e, ErrorState.onPacketSent, module);
			}
		}
	}
	
	public void onRelativeMove(S14PacketEntity packet) {
		 if ((packet instanceof S14PacketEntity))
	      {
	        packet = (S14PacketEntity)packet;
	        for (Module module : getToggledModules()) {
	  			try {
	  				ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
	  				if (group != null
	  						&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
	  						&& AuraUtils.getDisableAura()) {
	  					continue;
	  				}
	  				module.onRelativeMove(packet);
	  				
	  			} catch (Exception e) {
	  				onError(e, ErrorState.onRelativeMove, module);
	  			}
	        }
	      }
	      
	}

	public static void onEntityInteract(EntityInteractEvent event) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onEntityInteract(event);
			} catch (Exception e) {
				onError(e, ErrorState.onEntityInteract, module);
			}
		}
	}

	public static final String header = "§8[§2§lRC§8] §f";
	public static final String headerNoBrackets = "§2§lR§f§2emix §f";

	public static void chatMessage(float message) {
		chatMessage(String.valueOf(message));
	}
	
	

	public static void chatMessage(double message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(String message) {
		
			mc.thePlayer.addChatMessage(new ChatComponentText(header + message));
			
		
	}

	public static void chatMessage(int message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(long message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(boolean message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(String header, String message) {
		
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(header + message));
		}
	

	public static void chatMessage(ChatComponentText e) {
		
			mc.ingameGUI.getChatGUI().printChatMessage(e);
		
	}


    public static boolean handleOutPacket(Packet packet, EnumConnectionState packetState) {
        if (GuiBungeeCord.mc.isUUIDHack && packet instanceof C00Handshake) {
            if (((C00Handshake)packet).getRequestedState() == EnumConnectionState.LOGIN) {
                ((C00Handshake)packet).setIp(((C00Handshake)packet).getIp() + "\u0000" + mc.getFakeIp() + "\u0000" + UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(mc.getFakeNick()).toString().getBytes()).toString().replace("-", ""));
            }
            System.out.println(((C00Handshake)packet).getIp());
        }
        if (packetState != EnumConnectionState.PLAY) {
            return true;
        }
        
        return true;
    }




	public static void click() {
		mc.clickMouse();
	}

	public static boolean doDisablePacketSwitch() {
		return BowAimbot.getShouldChangePackets() 
				|| InfiniteReach.doBlock();
	}

	public static void clickReal() {
		clickRealRobot.mousePress(InputEvent.BUTTON1_MASK);
		clickRealRobot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public static void sendChatMessage(String message) {
		mc.getNetHandler().getNetworkManager().sendPacket(new C01PacketChatMessage(message));
	}

	public static void sendChatMessageFinal(String message) {
		mc.getNetHandler().getNetworkManager().sendPacketFinal(new C01PacketChatMessage(message));
	}

	public static String playerName() {
		return Minecraft.getMinecraft().thePlayer.getName();
	}

	public static boolean isCommand(String string) {
		for (Command cmd : getCommandManager().commands) {
			if (cmd.getName().equals(string)) {
				return true;
			}
		}
		return false;
	}

	public static Command getCommandByName(String string) {
		for (Command cmd : getCommandManager().commands) {
			if (cmd.getName().equals(string)) {
				return cmd;
			}
		}
		return null;
	}

	public static boolean getIsMouseHead(String name) {
		for (String s : mouseNames) {
			if (s.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean getIsTagName(String name) {
		for (String s : nameTagNames) {
			if (s.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static String getSearch() {
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if (mc.currentScreen instanceof DisplayClickGui) {
			return ((DisplayClickGui) mc.currentScreen).searchBar.typed;
		}
		return null;
	}

	public static SearchBar getSearchBar() {
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if (mc.currentScreen instanceof DisplayClickGui) {
			return ((DisplayClickGui) mc.currentScreen).searchBar;
		}
		return null;
	}

	public static void onBoundingBox(BoundingBoxEvent event) {
		for (Module module : getToggledModules()) {
			try {
				module.onBoundingBox(event);
			} catch (Exception e) {
				onError(e, ErrorState.onBoundingBox, module);
			}
		}
	}

	public static void onLivingUpdate() {
		for (Module module : getToggledModules()) {
			try {
				module.onLivingUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLivingUpdate, module);
			}
		}
	}



	public static NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public static TabGui getTabGui() {
		return tabGui;
	}

	public static AltManager getAltManager() {
		return altManager;
	}

	public static void onDeath() {
		for (Module module : getToggledModules()) {
			try {
				module.onDeath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ClickGuiManager getClickGuiManager() {
		return clickGuiManager;
	}

	 public static float getDistanceBetweenAngless(float angle1, float angle2) {
	        float angle = Math.abs(angle1 - angle2) % 360.0f;
	        if (angle > 180.0f) {
	            angle = 360.0f - angle;
	        }
	        return angle;
	    }
	 public static float fnsCalc4(Entity entity) {
	        Minecraft.getMinecraft();
	        double n = entity.posX - mc.thePlayer.posX;
	        Minecraft.getMinecraft();
	        double n2 = entity.posZ - mc.thePlayer.posZ;
	        Minecraft.getMinecraft();
	        Minecraft.getMinecraft();
	        return - MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)(- Math.toDegrees(Math.atan((entity.posY - 1.6 + (double)entity.getEyeHeight() - mc.thePlayer.posY) / (double)MathHelper.sqrt_double(n * n + n2 * n2)))));
	    }

	    public static float[] fnsCalc2(Entity entity) {
	        float[] arrf = new float[2];
	        Minecraft.getMinecraft();
	        arrf[0] = trivia.fnsCalc3(entity) + mc.thePlayer.rotationYaw;
	        Minecraft.getMinecraft();
	        arrf[1] = trivia.fnsCalc4(entity) + mc.thePlayer.rotationPitch;
	        return arrf;
	    }

	    public static float fnsCalc3(Entity entity) {
	        Minecraft.getMinecraft();
	        double n = entity.posX - mc.thePlayer.posX;
	        Minecraft.getMinecraft();
	        double n2 = entity.posZ - mc.thePlayer.posZ;
	        double degrees = n2 < 0.0 && n < 0.0 ? 90.0 + Math.toDegrees(Math.atan(n2 / n)) : (n2 < 0.0 && n > 0.0 ? -90.0 + Math.toDegrees(Math.atan(n2 / n)) : Math.toDegrees(- Math.atan(n / n2)));
	        Minecraft.getMinecraft();
	        return MathHelper.wrapAngleTo180_float(- mc.thePlayer.rotationYaw - (float)degrees);
	    }

	    public static float[] fnsCalc(int n6, int n5, int n3, EnumFacing enumFacing) {
	        Minecraft.getMinecraft();
	        EntitySnowball entity = new EntitySnowball(mc.theWorld);
	        entity.posX = (double)n6 + 0.5;
	        entity.posY = (double)n5 + 0.5;
	        entity.posZ = (double)n3 + 0.5;
	        EntitySnowball entitySnowball = entity;
	        entitySnowball.posX += (double)enumFacing.getDirectionVec().getX() * 0.25;
	        EntitySnowball entitySnowball2 = entity;
	        entitySnowball2.posY += (double)enumFacing.getDirectionVec().getY() * 0.25;
	        EntitySnowball entitySnowball3 = entity;
	        entitySnowball3.posZ += (double)enumFacing.getDirectionVec().getZ() * 0.25;
	        return trivia.fnsCalc2(entity);
	    }

	
	public static float[] getRotationsNeeded(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        Minecraft.getMinecraft();
        double diffX = entity.posX - mc.thePlayer.posX;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            diffY = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() * 0.9 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        } else {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        }
        Minecraft.getMinecraft();
        double diffZ = entity.posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        float[] arrf = new float[2];
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[0] = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[1] = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
        return arrf;
    


	}
}