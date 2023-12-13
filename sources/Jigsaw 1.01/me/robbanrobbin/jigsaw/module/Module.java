package me.robbanrobbin.jigsaw.module;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.events.BlockPlaceEvent;
import me.robbanrobbin.jigsaw.client.events.BoundingBoxEvent;
import me.robbanrobbin.jigsaw.client.events.EntityHitEvent;
import me.robbanrobbin.jigsaw.client.events.EntityInteractEvent;
import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.events.PreFrameBufferEvent;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.main.Jigsaw.ErrorState;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.module.state.ModuleState;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.Component;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ComponentWindow;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModuleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;

public class Module {

	public static Minecraft mc = Minecraft.getMinecraft();
	protected static Random rand = new Random();

	private String name;
	private int keyCode;
	private int defaultKeyCode;
	private boolean toggled;
	private Category category;
	protected String currentMode;
	public String description = "";
	public boolean settingsDisplayed = false;
	public int toggleTime = 0;
	
	private boolean hasSettings = true;

	public Module(String name, int defaultKeyCode, Category cat) {
		this.name = name;
		this.defaultKeyCode = defaultKeyCode;
		this.keyCode = defaultKeyCode;
		this.toggled = false;
		this.category = cat;
		if (this.getModes().length == 0) {
			this.currentMode = "default";
		} else {
			this.currentMode = this.getModes()[0];
		}
	}

	public Module(String name, int defaultKeyCode, Category cat, String description) {
		this.name = name;
		this.defaultKeyCode = defaultKeyCode;
		this.keyCode = defaultKeyCode;
		this.toggled = false;
		this.category = cat;
		if (this.getModes().length == 0) {
			this.currentMode = "default";
		} else {
			this.currentMode = this.getModes()[0];
		}
		this.description = description;
	}

	public void toggle() {
		this.setToggled(!this.toggled, true);
	}

	public Category getCategory() {
		return this.category;
	}

	public void onToggle(boolean update) {
		if(toggled) {
			Jigsaw.getUIManager().addModuleToList(this);
		}
		else {
			Jigsaw.getUIManager().removeModuleFromList(this);
		}
		if(!update) {
			return;
		}
		if(Jigsaw.loaded && Jigsaw.getSearchBar() != null) {
			Jigsaw.getSearchBar().resetTyped();
		}
		toggleTime = 0;
		this.onToggle();
		if (this.toggled) {
			try {
				onEnable();
			} catch (Exception e) {
				Jigsaw.onError(e, ErrorState.onEnable, this);
			}
		} else {
			try {
				onDisable();
			} catch (Exception e) {
				Jigsaw.onError(e, ErrorState.onDisable, this);
			}

		}
	}
	
	public void onToggle() {
		
	}

	public void onLeftClick() {

	}

	public void onRightClick() {

	}

	public void onEnable() {
		if (Jigsaw.loaded) {
			if(this.category != Category.HIDDEN && ClientSettings.notificationModulesEnable 
					&& Jigsaw.getModuleByName("Notifications").isToggled()) {
				Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, this.getName() + " enabled!"));
			}
			Jigsaw.getCommandManager().addResult("§j" + this.getName() + "§7 was §renabled!");
			Jigsaw.getModuleGroupMananger().disableGroupModsForModule(this);
		}
	}

	public void onDisable() {
		if (Jigsaw.loaded) {
			if(this.category != Category.HIDDEN && ClientSettings.notificationModulesDisable 
					&& Jigsaw.getModuleByName("Notifications").isToggled()) {
				Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, this.getName() + " disabled!"));
			}
			Jigsaw.getCommandManager().addResult("§j" + this.getName() + "§7 was §rdisabled!");
		}
	}

	public void onUpdate() {
		toggleTime++;
	}
	
	public void onUpdate(UpdateEvent event) {
		
	}

	public void onLateUpdate() {

	}

	public void onRender() {

	}

	public void onDeath() {

	}

	public void onGui() {

	}

	protected void onModeChanged(String modeBefore, String newMode) {
		
	}

	public void onChatMessageRecieved(SPacketChat packetIn) {

	}

	public void onPreMotion(PreMotionEvent event) {

	}

	public void onPostMotion() {

	}

	public void onBasicUpdates() {

	}

	public void onPacketRecieved(PacketEvent event) {

	}

	public void onPacketSent(PacketEvent event) {

	}

	public static void sendPacketFinal(Packet packet) {
		mc.getConnection().getNetworkManager().sendPacketFinal(packet);
	}

	public static void sendPacket(Packet packet) {
		mc.getConnection().getNetworkManager().sendPacket(packet);
	}

	public void onClientLoad() {
		mc = Minecraft.getMinecraft();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKeyboardKey() {
		return this.keyCode;
	}

	public int getDefaultKeyboardKey() {
		return this.defaultKeyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
		if(Jigsaw.loaded) {
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, 
					this.getName() + " was bound to key: " + Keyboard.getKeyName(keyCode)));
		}
	}

	public void setToggled(boolean toggled, boolean update) {
		if(this.toggled == toggled && update) {
			return;
		}
		this.toggled = toggled;
		try {
			onToggle(update);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setMode(String string) {
		for (String mode : getModes()) {
			if (string.equals(mode)) {
				this.currentMode = mode;
				Jigsaw.getCommandManager().addResult("§j" + this.getName() + "§7 was set to mode '§r" + this.currentMode + "§r'");
				return;
			}
		}
		System.out.println("Mode: §6" + string + "§7 does not exist switching to default mode...");
		if (this.getModes().length == 0) {
			this.currentMode = "default";
		} else {
			this.currentMode = this.getModes()[0];
		}
		if(Jigsaw.loaded) {
			this.onModeChanged(this.currentMode, string);
		}
		
	}

	public String getCurrentMode() {
		return this.currentMode;
	}

	@Deprecated
	public void setAddonText(String string) {
	}

	public String getAddonText() {
		if (this.currentMode.equals("default")) {
			return null;
		} else {
			return this.currentMode;
		}
	}

	public String[] getModes() {
		return new String[] { "default" };
	}

	public static Module getInstance(Class s) {
		return Jigsaw.getModuleByName(s.getSimpleName());
	}

	public void onEntityHit(EntityHitEvent entityHitEvent) {

	}

	public int getLengthForSort() {
		if (this.getModes().length == 1) {
			return this.getName().length();
		} else {
			return (this.getName() + this.getAddonText()).length() + 3;
		}
	}

	public void setState(ModuleState state) {
		this.setToggled(state.getToggled(), false);
		this.setMode(state.getMode());
		this.keyCode = state.getKeyBind();
	}

	public ModuleState createState() {
		return new ModuleState(this);
	}

	public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {

	}

	public boolean dontToggleOnLoadModules() {
		return false;
	}

	public boolean getEnableAtStartup() {
		return false;
	}

	public void onEntityInteract(EntityInteractEvent event) {

	}

	public boolean mode(String mode) {
		return this.currentMode.equals(mode);
	}

	public ModSetting[] getModSettings() {
		hasSettings = false;
		return null;
	}

	public void onBoundingBox(BoundingBoxEvent event) {

	}

	public boolean isCheckbox() {
		return false;
	}

	public void onLivingUpdate() {
		
	}

	public void onPreLateUpdate() {
		
	}
	
	public boolean hasCustomSettings() {
		return hasSettings;
	}

	public void onLoadWorld() {
		
	}
	
	public void preFrameBuffer(PreFrameBufferEvent event) {
		
	}
	
	public void postFrameBuffer() {
		
	}

	public void onResize(int width, int height) {
		
	}
	
	public ModuleButton getModuleButton() {
		
		for(ComponentWindow window : Jigsaw.getClickGuiManager().windows) {
			for(Component child : window.getChildren()) {
				if(child instanceof ModuleButton) {
					ModuleButton childModuleButton = (ModuleButton) child;
					if(childModuleButton.getTitle().equals(this.getName())) {
						return childModuleButton;
					}
				}
			}
		}
		
		return null;
		
	}

	public boolean isMode(String mode) {
		return this.currentMode.equals(mode);
	}
	
	public void setModeSilent(String mode) {
		this.currentMode = mode;
	}
	
	public CommandOption[] getCommandOptions() {
		return null;
	}
	
}
