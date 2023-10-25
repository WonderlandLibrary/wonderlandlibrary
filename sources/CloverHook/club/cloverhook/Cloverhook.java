package club.cloverhook;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import com.thealtening.AltService;
import com.thealtening.utilities.SSLVerification;

import club.cloverhook.account.AccountLoginService;
import club.cloverhook.account.AccountManager;
import club.cloverhook.account.AccountsFile;
import club.cloverhook.cheat.CheatManager;
import club.cloverhook.configuration.ConfigurationManager;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.ExitGameEvent;
import club.cloverhook.event.minecraft.StartGameEvent;
import club.cloverhook.gui.hud.Hud;
import club.cloverhook.notification.mgmt.NotificationManager;
import club.cloverhook.ui.main.Screen;
import club.cloverhook.utils.font.Fonts;
import me.hippo.systems.lwjeb.EventBus;
import me.hippo.systems.lwjeb.annotation.Collect;

/**
 * @author antja03
 */
public class Cloverhook {
    public static final Cloverhook instance = new Cloverhook();
    public static final File clientDir = new File(System.getProperty("user.home") + File.separator + "cloverhook");
    public static final Logger logger = LogManager.getLogger();

    public static final String client_name = "Cloverhook";
    public static final String client_build = "829";
    public static final String user_status = "DEV";

    public static EventBus eventBus;
    public CheatManager cheatManager;
    public ConfigurationManager configurationManager;
    public Screen userInterface;
    public Hud hud;
    public NotificationManager notificationManager;
    public AccountManager accountManager;
    public AccountLoginService accountLoginService;
    public AltService altService;

    public Cloverhook() {
    	eventBus = new EventBus().field().method().build();
        eventBus.register(this);
    }

    @Collect
    public void onStartGame(StartGameEvent event) {
        if (event.getStage().equals(Stage.PRE)) {
            if (!clientDir.exists()) {
                logger.info("Creating cloverhook... " + (clientDir.mkdirs() ? "Done" : "Failed"));
            }

            cheatManager = new CheatManager();
            configurationManager = new ConfigurationManager();
            notificationManager = new NotificationManager();
            accountManager = new AccountManager();
            accountLoginService = new AccountLoginService();
            new SSLVerification().verify();
            altService = new AltService();

            Runtime.getRuntime().addShutdownHook(new Thread(this::onExitGame));
        } else {
            Fonts.createFonts();

            cheatManager.registerCheats();
            configurationManager.loadConfigurationFiles(false);
            AccountsFile.load();

            userInterface = new Screen();
            hud = new Hud();

            Display.setTitle(client_name + " | Build " + client_build);
        }
    }

    private void onExitGame() {
        configurationManager.saveConfigurationFiles();
        eventBus.publish(new ExitGameEvent());
    }

	public Object getMember() {
		return null;
	}

}
