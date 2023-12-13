package com.enjoytheban;

import java.time.OffsetDateTime;

import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.CommandManager;
import com.enjoytheban.management.FileManager;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.modules.render.UI.TabUI;
import com.enjoytheban.ui.login.AltManager;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

import net.minecraft.util.ResourceLocation;

/**
 * The basis for this client
 * 
 * @author Purity
 */

// haha make sure to set breakthegame to true on line 379 in Minecraft and the
// one in mod manager oh and uh line 393 :) before you export :)

public class Client {

	// Setting the name, date and version number of the client
	public final String name = "ETB";
	public final double version = 0.6;
	public static boolean publicMode = false;
	
	// Discord RCP
	IPCClient client = new IPCClient(500494614311206913L);

	// Makes an instance so everything in this class can used by other classes
	public static Client instance = new Client();

	// Makes a variable for all managers
	private ModuleManager modulemanager;
	private CommandManager commandmanager;
	private AltManager altmanager;
	private FriendManager friendmanager;
	// Make a variable for tabui
	private TabUI tabui;
	//Client cape from Seige
    public static ResourceLocation CLIENT_CAPE = new ResourceLocation("ETB/cape.png");

	// Called when we start Minecraft
	public void initiate() {
		// Initiate the managers
		// cape = new ResourceLocation("assets/ETB/cape.png");
		(this.commandmanager = new CommandManager()).init();
		(this.friendmanager = new FriendManager()).init();
		(this.modulemanager = new ModuleManager()).init();
		// init tabgui
		(this.tabui = new TabUI()).init();
		// init altmanager and read the alts
		(this.altmanager = new AltManager()).init();
		this.altmanager.setupAlts();
		// init the fucking filemanagers
		FileManager.init();
		onReady(client);
	}

	// Method to return the modulemanager
	public ModuleManager getModuleManager() {
		return modulemanager;
	}

	// Method to return the commandmanager
	public CommandManager getCommandManager() {
		return commandmanager;
	}

	public AltManager getAltManager() {
		return altmanager;
	}

	// A shutdown method that saves settings when the minecraft applet is closed
	// called shutdownMinecraftApplet in Minecraft
	public void shutDown() {
		String values = "";
		// runs through modules
		for (Module m : Client.instance.getModuleManager().getModules()) {
			// runs through values
			for (Value v : m.getValues()) {
				// sets content to bullshit
				values += String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
			}
		}
		// saves settings
		FileManager.save("Values.txt", values, false);

		String enabled = "";
		// get the modules
		for (Module m : Client.instance.getModuleManager().getModules()) {
			// if the mod isnt enabled
			if (!m.isEnabled()) {
				continue;
			}
			enabled += String.format("%s%s", m.getName(), System.lineSeparator());
		}
		// save it
		FileManager.save("Enabled.txt", enabled, false);
	}
	
    public void onReady(IPCClient client) {
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setDetails(name + " " + version)
                        .setState("Minecraft 1.8")
                        .setStartTimestamp(OffsetDateTime.now())
                        .setDetails("https://www.enjoytheban.com/")
                        .setLargeImage("etb_logo", "https://www.enjoytheban.com/");
                client.sendRichPresence(builder.build());
            }
        });
        try {
            client.connect();
            System.out.println("RPC Set!");
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
    }
}