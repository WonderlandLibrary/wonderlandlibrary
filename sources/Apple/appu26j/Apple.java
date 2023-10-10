package appu26j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import appu26j.gui.LoadingScreen;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.Subscribe;

import appu26j.config.Config;
import appu26j.events.EventBus;
import appu26j.events.mc.EventKey;
import appu26j.gui.DragGUI;
import appu26j.gui.MusicPlayerGUI;
import appu26j.gui.ProfilesGUI;
import appu26j.gui.quickplay.QuickPlayGUI;
import appu26j.gui.screenshots.ScreenshotViewer;
import appu26j.interfaces.MinecraftInterface;
import appu26j.mods.ModsManager;
import appu26j.settings.SettingsManager;

public enum Apple implements MinecraftInterface
{
	CLIENT;

	public static final File DEFAULT_DIRECTORY = new File(System.getProperty("user.home"), "appleclient"), CONFIG = new File(DEFAULT_DIRECTORY, "config.json"), ACCOUNT = new File(DEFAULT_DIRECTORY, "account.txt");
    public volatile ArrayList<String> usersPlayingAppleClient = new ArrayList<>(), specialPeople = new ArrayList<>();
	public static final String VERSION = "2.67", TITLE = "Apple Client " + VERSION;
	private AppleClientVersionChecker appleClientVersionChecker;
    private long time = System.currentTimeMillis();
    private ScreenshotViewer screenshotViewer;
	private SettingsManager settingsManager;
    private MusicPlayerGUI musicPlayerGUI;
    private QuickPlayGUI quickPlayGUI;
    private ProfilesGUI profilesGUI;
	private ModsManager modsManager;
    private Websockets websockets;
	private EventBus eventBus;
	private DragGUI dragGUI;
	private Config config;

	static {
		if (!DEFAULT_DIRECTORY.exists()) {
			DEFAULT_DIRECTORY.mkdirs();
		}
	}

	/**
	 * Initializes the core of Apple Client
	 */
	public void initialize() {
		LoadingScreen.setProgress("Loading core");
		LoadingScreen.setCurrentStep(6);
		LoadingScreen.drawSplash(Minecraft.getMinecraft().getTextureManager());
		this.appleClientVersionChecker = new AppleClientVersionChecker();
		this.settingsManager = new SettingsManager().initialize();
        this.eventBus = new EventBus("Apple Client Event Bus");
		LoadingScreen.setProgress("Loading mods");
		LoadingScreen.setCurrentStep(7);
		LoadingScreen.drawSplash(Minecraft.getMinecraft().getTextureManager());
		this.modsManager = new ModsManager().initialize();
        this.screenshotViewer = new ScreenshotViewer();
        this.musicPlayerGUI = new MusicPlayerGUI();
        this.quickPlayGUI = new QuickPlayGUI();
        this.profilesGUI = new ProfilesGUI();
		this.dragGUI = new DragGUI();
		LoadingScreen.setProgress("Metadata");
		LoadingScreen.setCurrentStep(8);
		LoadingScreen.drawSplash(Minecraft.getMinecraft().getTextureManager());
		this.config = new Config();
		File musicFolder = new File("music");

		if (!musicFolder.exists()) {
			// Creates the folder
			musicFolder.mkdirs();
		}

		LoadingScreen.setProgress("Checking for updates");
		LoadingScreen.setCurrentStep(9);
		LoadingScreen.drawSplash(Minecraft.getMinecraft().getTextureManager());
		this.appleClientVersionChecker.run();
        this.websockets = new Websockets();
		LoadingScreen.setProgress("Connection to server");
		LoadingScreen.setCurrentStep(10);
		LoadingScreen.drawSplash(Minecraft.getMinecraft().getTextureManager());
		this.eventBus.register(this);
        this.requestSpecialPeople();
		LoadingScreen.setProgress("Finalizing");
		LoadingScreen.setCurrentStep(11);
		LoadingScreen.drawSplash(Minecraft.getMinecraft().getTextureManager());
        (new ServerThread()).start();
		this.websockets.start();
	}

	@Subscribe
	public void onKey(EventKey e) {
		if (e.getKey() == Keyboard.KEY_RSHIFT) {
			this.mc.displayGuiScreen(this.dragGUI.displayClickGUIWhenExiting());
		}

		if (e.getKey() == Keyboard.KEY_M) {
			this.mc.displayGuiScreen(this.musicPlayerGUI);
		}

		if (e.getKey() == Keyboard.KEY_H) {
			this.mc.displayGuiScreen(this.screenshotViewer);
		}

		if (e.getKey() == Keyboard.KEY_P)
        {
            this.mc.displayGuiScreen(this.profilesGUI);
        }
	}

	public AppleClientVersionChecker getVersionChecker() {
		return this.appleClientVersionChecker;
	}

	public SettingsManager getSettingsManager() {
		return this.settingsManager;
	}

	public ModsManager getModsManager() {
		return this.modsManager;
	}

	public EventBus getEventBus() {
		return this.eventBus;
	}

	public DragGUI getDragGUI() {
		return this.dragGUI;
	}

	public Config getConfig() {
		return this.config;
	}

	/**
	 * Unregister events
	 */
	public void shutdown() {
		this.eventBus.unregister(this);
	}

	/**
	 * Gets people with capes
	 * @return an arraylist with their names
	 */
	public ArrayList<String> getSpecialPeople() {
		return this.specialPeople;
	}

	/**
	 * Gets the current people using Apple Client
	 * @return an arraylist with their names
	 */
	public ArrayList<String> getPeopleUsingAppleClient()
    {
		return this.usersPlayingAppleClient;
    }

	/**
	 * Lets the server know you're playing on Apple Client
	 */
	public void sendAddUUIDRequestToServer() {
		new Thread(() ->
		{
			HttpURLConnection httpURLConnection = null;

			try {
				httpURLConnection = (HttpURLConnection) new URL("http://18.135.102.232:8080/adduuid/?uuid=" + this.mc.getSession().getPlayerID()).openConnection();
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.connect();
				httpURLConnection.getInputStream();
			} catch (Exception e) {
				;
			} finally {
				if (httpURLConnection != null) {
					httpURLConnection.disconnect();
				}
			}
		}).start();
	}

	/**
	 * Requests the people with capes
	 */
	public void requestSpecialPeople() {
        new Thread(() ->
        {
            HttpURLConnection httpURLConnection = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            
            try
            {
                httpURLConnection = (HttpURLConnection) new URL("http://18.135.102.232:8080/specialpeople").openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                
                while ((line = bufferedReader.readLine()) != null)
                {
                    this.specialPeople.add(line);
                }
            }
            
            catch (Exception exception)
            {
                ;
            }
            
            finally
            {
                if (bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    }
                    
                    catch (Exception exception)
                    {
                        ;
                    }
                }
                
                if (inputStreamReader != null)
                {
                    try
                    {
                        inputStreamReader.close();
                    }
                    
                    catch (Exception exception)
                    {
                        ;
                    }
                }
                
                if (httpURLConnection != null)
                {
                    httpURLConnection.disconnect();
                }
                
                File capes = new File(System.getProperty("java.io.tmpdir"), "capes");
                
                if (!capes.exists())
                {
                    capes.mkdirs();
                }
                
                for (String specialPerson : this.specialPeople)
                {
                    File cape = new File(capes, specialPerson + ".png");
                    
                    if (!cape.exists() || cape.length() == 0)
                    {
                        try
                        {
                            cape.createNewFile();
                            FileUtils.copyURLToFile(new URL("http://18.135.102.232:8080/capes/?cape=" + specialPerson), cape);
                        }
                        
                        catch (Exception e)
                        {
                            ;
                        }
                    }
                }
            }
        }).start();
	}

	public QuickPlayGUI getQuickPlayGUI() {
		return this.quickPlayGUI;
	}

	public Websockets getWebsockets() {
		return this.websockets;
	}
}
