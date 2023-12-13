package me.robbanrobbin.jigsaw.client.main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.select.Elements;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.alts.AltManager;
import me.robbanrobbin.jigsaw.client.bypasses.PresetManager;
import me.robbanrobbin.jigsaw.client.chat.ChatMananger;
import me.robbanrobbin.jigsaw.client.commands.Command;
import me.robbanrobbin.jigsaw.client.commands.CommandManager;
import me.robbanrobbin.jigsaw.client.events.BlockPlaceEvent;
import me.robbanrobbin.jigsaw.client.events.BoundingBoxEvent;
import me.robbanrobbin.jigsaw.client.events.EntityHitEvent;
import me.robbanrobbin.jigsaw.client.events.EntityInteractEvent;
import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.gui.tab.TabGui;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.GhostMode;
import me.robbanrobbin.jigsaw.client.modules.KillAura;
import me.robbanrobbin.jigsaw.client.presets.PresetMananger;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.LoadTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.client.waypoints.WaypointManager;
import me.robbanrobbin.jigsaw.console.ConsoleManager;
import me.robbanrobbin.jigsaw.cracker.CrackManager;
import me.robbanrobbin.jigsaw.files.FileMananger;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.framebuffer.FramebufferHandler;
import me.robbanrobbin.jigsaw.friends.FriendsMananger;
import me.robbanrobbin.jigsaw.gui.GuiJigsawWelcome;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.gui.NotificationManager;
import me.robbanrobbin.jigsaw.gui.UIManager;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ClickGuiManager;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ComponentTextField;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ComponentWindow;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.DisplayClickGui;
import me.robbanrobbin.jigsaw.module.Module;
import me.robbanrobbin.jigsaw.module.group.ModuleGroup;
import me.robbanrobbin.jigsaw.module.group.ModuleGroupMananger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class Jigsaw {

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
	private static String clientName = "Jigsaw";
	private static String clientVersion = "1.12.2 - v2.0";
	public static final String fakeClientVersion = "1.12.2-OptiFine_HD_U_C6";
	public static String serverVersion = null;
	private static String clientAuthor = "Robofån";
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ClickGuiManager clickGuiManager;
	private static UIManager uiManager;
	private static FileMananger fileMananger;
	private static PresetMananger presetMananger;
	private static FriendsMananger friendsMananger;
	private static ChatMananger chatMananger;
	private static ModuleGroupMananger moduleGroupMananger;
	private static CommandManager commandManager;
	private static CrackManager crackManager;
	private static PresetManager presetManager;
	private static NotificationManager notificationManager;
	private static AltManager altManager;
	private static FramebufferHandler framebufferHandler;
	private static WaypointManager waypointManager;
	private static ConsoleManager consoleManager;
	private static TabGui tabGui;
	public static boolean ghostMode = false;
	private static CopyOnWriteArrayList<Module> modules;
	public static boolean loaded = false;
	public static boolean performanceBoost = true;
	public static boolean debugMode = false;
	// public static ArrayList<Module> toggledList = new ArrayList<Module>();
	private static Robot clickRealRobot;
	public static ArrayList<String> mouseNames = new ArrayList<String>();
	public static ArrayList<String> nameTagNames = new ArrayList<String>();
	public static HashMap<String, String> devTagNames = new HashMap<String, String>();
	public static String[] proxy = null;
	public static boolean welcomed = false;
	public static boolean firstStart = false;
	public static boolean disableSprint = false;

	public static final ResourceLocation blurShader = new ResourceLocation("jigsaw/shader/guiblur.json");
	public static final ResourceLocation jigsawTexture512 = new ResourceLocation("jigsaw/jigsaw512.png");
	public static final ResourceLocation jigsawTexture1024x512 = new ResourceLocation("jigsaw/jigsaw1024x512.png");

	public static final ResourceLocation menuImage = new ResourceLocation("jigsaw/mainmenu.png");

	private static WaitTimer tpsTimer = new WaitTimer();
	public static double lastTps = 20.0;

	public static final ArrayList<ResourceLocation> gifLocations = new ArrayList<ResourceLocation>();

	public static final WaitTimer saveTimer = new WaitTimer();
	public static File jarFile = null;
	public static boolean gifMenu;

	private static Random rand = new Random();

	public static ArrayList<Packet> packetQueue = new ArrayList<Packet>();

	public static boolean lessLagPoolThing;

	/**
	 * 
	 * Disables the GUI, the UI, and all Chat Messages
	 * 
	 */
	public static void ghostMode(boolean b) {
		getModuleByName("ClickGUI").setToggled(!b, true);
		getUIManager().setEnabled(!b);
		ghostMode = b;
	}

	public static void Register() {

		// try {
		// PrintWriter writer = new PrintWriter(new
		// File(Minecraft.class.getResource("users").getPath()));
		// writer.println(System.getProperty("user.name") + " | " +
		// mc.session.getUsername() + " - " + mc.session.getSessionType() + " : ");
		// writer.flush();
		// writer.close();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }

		try {
			jarFile = new File(Jigsaw.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		// if(rand.nextInt(1001) == 50) {
		// Jigsaw.gifMenu = true;
		// }

		for (int i = 1; i <= 330; i++) {
			gifLocations.add(new ResourceLocation("jigsaw/gif/frame_" + String.valueOf(i) + "_delay-0.04s.png"));
			// gifLocations.add(new ResourceLocation("jigsaw/gifvideo/a (" +
			// String.valueOf(i) + ").jpg"));
		}
		LoadTools.doAntiLeak();

		modules = new CopyOnWriteArrayList<Module>();
		fileMananger = new FileMananger();
		try {
			fileMananger.load();
		} catch (IOException e) {
			System.out.println("File error!");
			e.printStackTrace();
		}

		Fonts.loadFonts();

		notificationManager = new NotificationManager();
		getFileMananger().loadSettings();
		if (ClientSettings.lockGuiScale || Minecraft.getMinecraft().gameSettings.guiScale == 0) {
			Minecraft.getMinecraft().gameSettings.guiScale = 2;
		}
		modules = LoadTools.addModules();
		appendModule(new GhostMode());
		uiManager = new UIManager();

		presetManager = new PresetManager();

		clickGuiManager = new ClickGuiManager();
		clickGuiManager.setup();

		friendsMananger = new FriendsMananger();
		chatMananger = new ChatMananger();
		commandManager = new CommandManager();
		altManager = new AltManager();

		consoleManager = new ConsoleManager();

		tabGui = new TabGui();

		framebufferHandler = new FramebufferHandler();

		// Default module groups!
		moduleGroupMananger = new ModuleGroupMananger();
//		moduleGroupMananger.addGroup(new ModuleGroup(new Module[] {
//		Jigsaw.getModuleByName("KillAura"),
//		Jigsaw.getModuleByName("TriggerBot"), Jigsaw.getModuleByName("MobArena"),
//		Jigsaw.getModuleByName("TpAura"), Jigsaw.getModuleByName("GhostAura"),
//		Jigsaw.getModuleByName("Aimbot"), Jigsaw.getModuleByName("OP-Fightbot") },
//		"AuraGroup"));
		
		moduleGroupMananger.addGroup(new ModuleGroup(new Module[] {
				Jigsaw.getModuleByName("KillAura"),
				Jigsaw.getModuleByName("Aimbot"),
				Jigsaw.getModuleByName("TriggerBot")
		}, "AuraGroup"));

		moduleGroupMananger.addGroup(new ModuleGroup(
				new Module[] { Jigsaw.getModuleByName("Flight"), Jigsaw.getModuleByName("AirJump") }, "AirGroup"));

		waypointManager = new WaypointManager();

		try {
			clickRealRobot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		getFileMananger().loadModules();
		for (Module m : getModules()) {
			m.onClientLoad();
		}
		if(Jigsaw.getModuleByName("GhostMode").isToggled()) {
			Jigsaw.getModuleByName("GhostMode").setToggled(false, true);
		}

		getFileMananger().loadGUI(clickGuiManager.windows);
		getFileMananger().loadFriends();
		getFileMananger().loadAlts();

		if (Jigsaw.firstStart) {
			System.out.println("Jigsaw first start!");
		}

		ClientSettings.hackerDetectAutoNotify = false;
		ClientSettings.hackerDetectMoreInfo = false;

		if (!OpenGlHelper.areShadersSupported()) {
			ClientSettings.enableBlur = false;
		}

		loaded = true;
		
//		LoadTools.alertInfoFetcher.run();
		LoadTools.doAntiLeak();

		getClickGuiManager().updateWindowManager();

	}

	public static void saveSettings() {
		getFileMananger().saveModules();
		// TODO fix this shit
		getFileMananger().saveGUI(Jigsaw.clickGuiManager.windows);
		getFileMananger().saveFriends();
		getFileMananger().saveSettings();
		getFileMananger().saveAlts();
	}

	public static void onMCClose() {
		if (Jigsaw.loaded) {
			saveSettings();
		}
	}

	public static void setToggledAllModules(boolean enabled) {
		for (Module m : Jigsaw.getModules()) {
			m.setToggled(enabled, true);
		}
	}

	public static int getModuleCount() {
		return modules.size();
	}

	public static void appendModule(Module module) {
		// TODO Module autotoggle
		modules.add(module);
		if ((module.getCategory() == Category.TARGET) && module.getName() != "Friends") {
			module.setToggled(true, true);
		}
	}

	public static final Category[] defaultCategories = new Category[] { Category.AI, Category.COMBAT, Category.MOVEMENT,
			Category.MISC, Category.RENDER, Category.FUN, Category.MINIGAMES, Category.PLAYER, Category.EXPLOITS,
			Category.WORLD };

	public static final Category[] defaultCategoriesWithTarget = new Category[] { Category.AI, Category.COMBAT,
			Category.MOVEMENT, Category.MISC, Category.RENDER, Category.FUN, Category.MINIGAMES, Category.PLAYER,
			Category.EXPLOITS, Category.WORLD, Category.TARGET };

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
	 * Gets a Jigsaw module by its name
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
	 * @deprecated Use getModulesByCategories() instead!
	 * 
	 * @param modules
	 * @param categories
	 *            Modules must be this category
	 * @param r
	 *            ReturnType. The modules name, keyboard, or the module
	 * @return An ArrayList of the filtered modules
	 */
	@Deprecated
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

	public static ArrayList<Module> getModulesByCategories(ArrayList<Module> modules, Category[] categories) {
		ArrayList<Module> filtered = new ArrayList<Module>();
		for (Module m : modules) {
			for (Category c : categories) {
				if (m.getCategory() == c) {
					filtered.add(m);
				}
			}
		}
		return filtered;
	}

	public static void disableModules(ArrayList<Module> modules) {
		for (Module m : modules) {
			if (m.isToggled()) {
				m.setToggled(false, true);
			}
		}
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

	public static UIManager getUIManager() {
		return uiManager;
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

	public static CrackManager getCrackManager() {
		return crackManager;
	}

	public static void setCrackManager(CrackManager crackManager) {
		Jigsaw.crackManager = crackManager;
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

		onRender("rendering"), onUpdate("updating"), onEnable("enabling"), onDisable("disabling"), onMessage(
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
																								"updating (living)"), onResize(
																										"onresize");

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
			try {
				module.onEntityHit(entityHitEvent);
			} catch (Exception e) {
				onError(e, ErrorState.onEntityHit, module);
			}
		}
	}

	public static void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
		for (Module module : getToggledModules()) {
			try {
				module.onBlockPlace(blockPlaceEvent);
			} catch (Exception e) {
				onError(e, ErrorState.onBlockPlace, module);
			}
		}
	}

	public static void onLeftClick() {
		for (Module module : getToggledModules()) {
			try {
				module.onLeftClick();
			} catch (Exception e) {
				onError(e, ErrorState.onLeftClick, module);
			}
		}
	}

	public static void onRightClick() {
		for (Module module : getToggledModules()) {
			try {
				module.onRightClick();
			} catch (Exception e) {
				onError(e, ErrorState.onRightClick, module);
			}
		}
	}

	public static void onChatMessageRecived(SPacketChat packetIn) {
		for (Module module : getToggledModules()) {
			try {
				module.onChatMessageRecieved(packetIn);
			} catch (Exception e) {
				onError(e, ErrorState.onMessage, module);
			}
		}
	}

	public static void onKeyPressed(int keyCode) {
		if (getTabGui() != null) {
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
		mc = Minecraft.getMinecraft();
		for (Module mod : Jigsaw.getModules()) {
			mod.mc = mc;
		}
		if (getTabGui() != null) {
			getTabGui().update();
		}
		if (mc.currentScreen instanceof GuiDownloadTerrain) {
			Utils.blackList.clear();
		}

		if (Jigsaw.firstStart && mc.player != null && mc.player.ticksExisted > 10) {
			mc.displayGuiScreen(new GuiJigsawWelcome(null));
			Jigsaw.firstStart = false;
		}

		Utils.updateLastGroundLocation();
		if (getCrackManager() != null) {
			getCrackManager().onUpdate();
		}
		getUIManager().update();
		getFramebufferHandler().update();
		if (!(mc.currentScreen instanceof DisplayClickGui)) {
			for (ComponentWindow window : Jigsaw.getClickGuiManager().windows) {
				if (window.isPinned()) {
					window.update();
				}
			}
		}
		if (!Jigsaw.welcomed) {
			if (Jigsaw.devVersion) {
				Jigsaw.getNotificationManager().addNotification(
						new Notification(Level.INFO, "You are using a dev version: " + Jigsaw.getClientVersion()
								+ " - Please report any bugs or crashes to me on the Discord server!"));
			} else {
				Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO,
						"Press left ctrl to open the click gui! Press U to access the console!"));
			}
			Jigsaw.welcomed = true;
		}
		lessLagPoolThing = Jigsaw.getModuleByName("LessLag").isToggled();
		getNotificationManager().update();
		for (Module module : getToggledModules()) {
			try {
				module.onUpdate(event);
				module.onUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onUpdate, module);
			}
		}
		if (saveTimer.hasTimeElapsed(100000, true)) {
			saveSettings();
		}
	}

	public static void onGui(boolean renderModules) {
		if (mc == null) {
			return;
		}
		if (getCrackManager() != null) {
			getCrackManager().onGui();
		}
		// if(getTabGui() != null && !ghostMode && !mc.gameSettings.showDebugInfo) {
		// getTabGui().render();
		// }
		getNotificationManager().draw();
		if (getUIManager() != null) {
			try {
				Jigsaw.getUIManager().render(renderModules);
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
			try {
				module.onPreLateUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLateUpdate, module);
			}

		}
	}

	public static void OnLateUpdate() {

		if (mc.gameSettings.ofFastRender) {
			mc.gameSettings.setOptionValue(GameSettings.Options.FAST_RENDER, 1);
		}

		onPreLateUpdate();
		for (Module module : getToggledModules()) {
			try {
				module.onLateUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLateUpdate, module);
			}
		}
		for (Packet packet : packetQueue) {
			mc.myNetworkManager.sendPacketFinal(packet);
		}
		packetQueue.clear();
	}

	public static void onPostMotion() {
		for (Module module : getToggledModules()) {
			try {
				module.onPostMotion();
			} catch (Exception e) {
				onError(e, ErrorState.onPostMotion, module);
			}

		}
	}

	public static void onPreMotion(PreMotionEvent event) {
		for (Module module : getToggledModules()) {
			try {
				module.onPreMotion(event);
			} catch (Exception e) {
				onError(e, ErrorState.onPreMotion, module);
			}

		}
	}

	public static void onBasicUpdates() {
		for (Module module : getToggledModules()) {
			try {
				module.onBasicUpdates();
			} catch (Exception e) {
				onError(e, ErrorState.onBasicUpdates, module);
			}

		}
	}

	public static void OnRender() {
		if (!ghostMode) {
			for (Module module : getToggledModules()) {
				try {
					module.onRender();
				} catch (Exception e) {
					onError(e, ErrorState.onRender, module);
				}
			}
		}
	}

	private static ArrayList<Long> times = new ArrayList<Long>();

	public static void onPacketRecieved(PacketEvent event) {
		Packet modPacket = event.getPacket();
		if (modPacket instanceof SPacketTimeUpdate) {
			times.add(Math.max(0, tpsTimer.getTime()));
			long timesAdded = 0;
			if (times.size() > 5) {
				times.remove(0);
			}
			for (long l : times) {
				timesAdded += l;
			}
			long roundedTps = timesAdded / times.size();
			lastTps = (20.0 / roundedTps) * 1000.0;
			lastTps = Math.min(lastTps, 20.0);
			tpsTimer.reset();
		}
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null) {
				continue;
			}
			try {
				module.onPacketRecieved(event);
			} catch (Exception e) {
				onError(e, ErrorState.onPacketRecieved, module);
			}
		}
	}

	public static void onPacketSent(PacketEvent event) {
		for (Module module : getToggledModules()) {
			try {
				module.onPacketSent(event);
			} catch (Exception e) {
				onError(e, ErrorState.onPacketSent, module);
			}
		}
	}

	public static void onEntityInteract(EntityInteractEvent event) {
		for (Module module : getToggledModules()) {
			try {
				module.onEntityInteract(event);
			} catch (Exception e) {
				onError(e, ErrorState.onEntityInteract, module);
			}
		}
	}

	public static final String header = "§8[§j!§8] §7";
	public static final String headerNoBrackets = "§j§lJig§r§8saw §7";

	public static void chatMessage(float message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(double message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(String message) {
		if (!ghostMode) {
			mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(header + message));
		}
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
		if (!ghostMode) {
			mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(header + message));
		}
	}

	public static void chatMessage(TextComponentString e) {
		if (!ghostMode) {
			mc.ingameGUI.getChatGUI().printChatMessage(e);
		}
	}

	public static void click() {
		mc.clickMouse();
	}

	public static boolean doDisablePacketSwitch() {
		return KillAura.getShouldChangePackets() /*
													 * || BowAimbot.getShouldChangePackets() || DJ.getIsPlaying() ||
													 * TpAura.doBlock() || RodAura.getShouldChangePackets() ||
													 * AutoPotion.isPotting()
													 */;
	}

	public static void clickReal() {
		clickRealRobot.mousePress(InputEvent.BUTTON1_MASK);
		clickRealRobot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public static void sendChatMessage(String message) {
		mc.getConnection().getNetworkManager().sendPacket(new CPacketChatMessage(message));
	}

	public static void sendChatMessageFinal(String message) {
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketChatMessage(message));
	}

	public static String playerName() {
		return Minecraft.getMinecraft().player.getName();
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
		return Jigsaw.getClickGuiManager().searchComponent.getTyped();
	}

	public static ComponentTextField getSearchBar() {
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if (Jigsaw.getClickGuiManager() == null) {
			return null;
		}
		return Jigsaw.getClickGuiManager().searchComponent;
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

	public static void onResize(int width, int height) {
		Jigsaw.getClickGuiManager().updateLayoutForAllComponents();
		for (Module module : getModules()) {
			try {
				module.onResize(width, height);
			} catch (Exception e) {
				onError(e, ErrorState.onResize, module);
			}
		}
	}

	public static PresetManager getPresetManager() {
		return presetManager;
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

	public static FramebufferHandler getFramebufferHandler() {
		return framebufferHandler;
	}

	public static WaypointManager getWaypointManager() {
		return waypointManager;
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

	public static void queuePacketToLateUpdate(Packet packet) {
		packetQueue.add(packet);
	}

	public static void onLoadWorld() {
		for (Module module : getToggledModules()) {
			try {
				module.onLoadWorld();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isFrameBufferRendering() {
		return getFramebufferHandler().rendering;
	}

	public static ConsoleManager getConsoleManager() {
		return consoleManager;
	}

}
