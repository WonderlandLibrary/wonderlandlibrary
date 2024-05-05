package best.azura.client.impl;

import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.clickgui.azura.ClickGUI;
import best.azura.client.impl.command.CommandManager;
import best.azura.client.impl.config.Config;
import best.azura.client.impl.config.ConfigManager;
import best.azura.client.impl.events.*;
import best.azura.client.impl.friends.FriendManager;
import best.azura.irc.impl.IRCConnector;
import best.azura.client.impl.module.ModuleManager;
import best.azura.client.impl.module.impl.other.FPSBooster;
import best.azura.client.impl.rpc.DiscordRPCImpl;
import best.azura.client.impl.ui.customhud.ElementManager;
import best.azura.client.impl.ui.font.FontRenderer;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.account.AccountManager;
import best.azura.client.impl.ui.notification.NotificationManager;
import best.azura.client.impl.value.ValueManager;
import best.azura.client.util.crypt.Crypter;
import best.azura.client.util.crypt.RSAUtil;
import best.azura.client.util.other.BalanceUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.other.FileUtil;
import best.azura.client.util.other.GLTask;
import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.core.EventPriority;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.scripting.ScriptManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;
import wtf.kinggen.KingGen;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings({"BusyWait", "unused"})
public enum Client {

	INSTANCE;

	public static final boolean BETA = false, RELEASE_SET = false;

	public static final String NAME = "Azura", VERSION = "X", RELEASE = "06042022", PREFIX = "§7[§9A§7]§f ", COLOR = "§9";
	public static String releaseBuild = "Development";
	public String clientUserName, sessionToken;
	public Key aesKey;
	private Config clientConfig;
	private Logger logger;
	private ModuleManager moduleManager;
	private ValueManager valueManager;
	private ConfigManager configManager;
	private CommandManager commandManager;
	private ElementManager elementManager;
	private FriendManager friendManager;
	private KingGen kingAltsGenerator;
	private FontRenderer fontRenderer;
	private Thread shutdownThread, fastTickThread;
	private AccountManager accountManager;
	private ClickGUI clickGUI;
	private ClickGUI newGUI;
	private NotificationManager notificationManager;
	private MicrosoftAuthenticator microsoftAuthenticator;
	private ScriptManager scriptManager;
	private IRCConnector ircConnector;
	private ArrayList<GLTask> glTasks;
	private EventBus eventBus;
	private KeyPair keyPair;
	private final DiscordRPCImpl discordRPCImpl = new DiscordRPCImpl();
	public long startTime;

	/**
	 * Method is called on reload
	 * Method called on startup of the Client
	 **/
	public void load() {
		startTime = System.currentTimeMillis();
		logger = LogManager.getLogger();
		try {
			keyPair = RSAUtil.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "ERROR - JRx02", 5000, Type.ERROR));
			System.err.println("ERROR - JRx02");
			System.exit(-1);
			return;
		}
		if (BETA) releaseBuild = "Beta Build";
		if (RELEASE_SET) releaseBuild = "Release";

		try {
			ViaMCP.getInstance().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		eventBus = new EventBus();
		valueManager = new ValueManager();
		fontRenderer = new FontRenderer(new Font("Arial", Font.PLAIN, 20));
		configManager = new ConfigManager();
		moduleManager = new ModuleManager();
		scriptManager = new ScriptManager();
		moduleManager.postLoad();
		accountManager = new AccountManager();
		commandManager = new CommandManager();
		friendManager = new FriendManager();
		notificationManager = new NotificationManager();
		microsoftAuthenticator = new MicrosoftAuthenticator();
		clientConfig = new Config("client", "Azura Client", getCurrentDate(), true, true, true, true);
		elementManager = new ElementManager();
		glTasks = new ArrayList<>();
		clientConfig.setLoadBinds(true);
		clientConfig.setLoadVisuals(true);
		clientConfig.setSaveBinds(true);
		clientConfig.setSaveVisuals(true);
		clientConfig.load(configManager.getClientDirectory());
		if (!getConfigManager().getDataDirectory().exists()) {
			//noinspection ResultOfMethodCallIgnored
			getConfigManager().getDataDirectory().mkdir();
		}

		File settingsFile = new File(Client.INSTANCE.getConfigManager().getClientDirectory() + "/data", "settings.json");

		if (settingsFile.exists()) {
			JsonObject jsonObject;

			jsonObject = new JsonParser().parse(FileUtil.getContentFromFileAsString(settingsFile)).getAsJsonObject();

			if (jsonObject.has("kingalts")) {
				kingAltsGenerator = new KingGen(jsonObject.get("kingalts").getAsString());
			} else {
				kingAltsGenerator = new KingGen("");
			}
		} else {
			kingAltsGenerator = new KingGen("");
		}

		eventBus.register(this);
		eventBus.register(commandManager);
		Runtime.getRuntime().addShutdownHook(shutdownThread = new Thread(() -> unload(false)));
		loadClickGUI();
		discordRPCImpl.startup();
		discordRPCImpl.createNewPresence();

		(fastTickThread = new Thread(() -> {
			while (fastTickThread != null && !fastTickThread.isInterrupted()) {
				try {
					Thread.sleep(15L);
					if (Minecraft.getMinecraft().currentScreen != null)
						Minecraft.getMinecraft().currentScreen.onTick();
					eventBus.call(new EventFastTick());
				} catch (Exception ignored) {
				}
			}
		})).start();
		Fonts.INSTANCE.reload();
		ircConnector = new IRCConnector("irc.azura.best", 4739);
		Client.INSTANCE.getEventBus().register(BalanceUtil.INSTANCE);
	}

	public void loadClickGUI() {
		clickGUI = new ClickGUI();
		newGUI = new ClickGUI();
	}

	/**
	 * Method called once reload command is used
	 * Method is called once the Client is exited
	 */
	public void unload(boolean reload) {
		if (!reload) {
			// Variable for the URL.
			URL sessionURL = null;

			try {
				// Parse the String into a URL.
				sessionURL = new URL("https://api.azura.best/session/stop");
			} catch (Exception ignore) {
			}

			// Variable for the actual connection.
			HttpsURLConnection urlConnection;

			try {
				// Create a URL Connection with the URL.
				assert sessionURL != null;
				urlConnection = (HttpsURLConnection) sessionURL.openConnection();

				// Set RequestInfo.
				urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
				urlConnection.addRequestProperty("Accept", "application/json");
				urlConnection.addRequestProperty("Session-Token", Crypter.encode(sessionToken));


			} catch (Exception ignore) {
			}

		}

		eventBus.unregister(this);
		eventBus.unregister(commandManager);
		if (shutdownThread != null) {
			try {
				Runtime.getRuntime().removeShutdownHook(shutdownThread);
			} catch (Exception ignored) {
			}
			shutdownThread = null;
		}
		if (fastTickThread != null) {
			fastTickThread.interrupt();
			fastTickThread = null;
		}
		clientConfig.save(configManager.getClientDirectory());
		elementManager.save();
		accountManager.saveAccounts();
	}

	/**
	 * All the getters
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	public IRCConnector getIrcConnector() {
		return ircConnector;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}

	public ValueManager getValueManager() {
		return valueManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public Config getClientConfig() {
		return clientConfig;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public ElementManager getElementManager() {
		return elementManager;
	}

	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public MicrosoftAuthenticator getMicrosoftAuthenticator() {
		return microsoftAuthenticator;
	}

	public void setKingAltsGenerator(KingGen kingAltsGenerator) {
		this.kingAltsGenerator = kingAltsGenerator;
	}

	public KingGen getKingAltsGenerator() {
		return kingAltsGenerator;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public ClickGUI getClickGUI() {
		return clickGUI;
	}

	public ClickGUI getNewGUI() {
		return newGUI;
	}

	public FriendManager getFriendManager() {
		return friendManager;
	}

	public Logger getLogger() {
		return logger;
	}

	public ScriptManager getScriptManager() {
		return scriptManager;
	}

	public void setIrcConnector(IRCConnector ircConnector) {
		this.ircConnector = ircConnector;
	}

	@EventHandler
	public final Listener<EventKey> eventKeyListener = e -> moduleManager.getRegistered().stream().filter(m -> m.getKeyBind() == e.key).forEach(m -> m.setEnabled(!m.isEnabled()));

	@EventHandler
	public final Listener<EventSentPacket> eventSendPacketListener = event -> {
		if (event.getPacket() instanceof C01PacketChatMessage) {
			C01PacketChatMessage c01 = event.getPacket();
			if (c01.getMessage().startsWith("${jndi")) {
				notificationManager.addToQueue(new Notification("Don't you even try...", c01.getMessage(), 5000, Type.INFO));
				
				event.setCancelled(true);
				DelayUtil delayUtil = new DelayUtil();
				File file = new File(this.getConfigManager().getClientDirectory().getAbsolutePath() + "/bruh.txt");
				try {
					FileWriter fileWriter = new FileWriter(this.getConfigManager().getClientDirectory().getAbsolutePath() + "/bruh.txt");
					fileWriter.write("Don't even try to use this exploit " + System.getProperty("user.name"));
					fileWriter.close();
				} catch (IOException ignore) {}

				if (delayUtil.hasReached(2000)) {
					Desktop desktop = Desktop.getDesktop();
					delayUtil.reset();
					if (file.exists()) {
						try {
							desktop.open(file);
							Display.destroy();
						} catch (IOException ignore) {}
					}
				}
			}
		}
	};

	@EventHandler
	public final Listener<EventMotion> eventMotionListener = eventMotion -> {
		if (eventMotion.isPre()) {
			DiscordRPCImpl.updateNewPresence(
					"Gaming Minecraft",
					"Playing on " + (Minecraft.getMinecraft().getCurrentServerData().serverIP == null ? "SinglePlayer"
							: Minecraft.getMinecraft().getCurrentServerData().serverIP)
			);
		}
	};

	//Exploit fix
	@EventHandler
	public final Listener<EventReceivedPacket> eventReceivedPacketListener = event -> {
		if (event.getPacket() instanceof C01PacketChatMessage) {
			C01PacketChatMessage c01 = event.getPacket();
			if (c01.getMessage().startsWith("${jndi") || c01.getMessage().contains("jndi")) {
				notificationManager.addToQueue(new Notification("Some one tried using the Log4j exploit", c01.getMessage(), 5000, Type.INFO));
				event.setCancelled(true);
			}
		}
	};

	private int before = 0;
	@SuppressWarnings("unused")
	@EventHandler
	public final Listener<EventRender2D> eventRender = event -> {
		if (FPSBooster.idleFPS.getObject() && moduleManager.getModule(FPSBooster.class).isEnabled()) {
			if (Display.isActive() && before != -1) {
				Minecraft.getMinecraft().gameSettings.limitFramerate = before;
				before = -1;
			} else if (!Display.isActive()) {
				if (before == -1)
					before = Minecraft.getMinecraft().gameSettings.limitFramerate;
				Minecraft.getMinecraft().gameSettings.limitFramerate = 15;
			}
		}
		try {
			glTasks.forEach(GLTask::run);
			glTasks.clear();
		} catch (Exception ignored) {
			glTasks.clear();
		}
	};

	public ArrayList<GLTask> getGlTasks() {
		return glTasks;
	}

	public String getUsername() {
		return clientUserName;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	public String getVersion() {
		return "1.3.0";
		//major.minor.patch
	}
}
