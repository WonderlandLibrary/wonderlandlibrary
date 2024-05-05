package me.aidanmees.trivia.module;

import java.lang.reflect.Method;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.BlockPlaceEvent;
import me.aidanmees.trivia.client.events.BoundingBoxEvent;
import me.aidanmees.trivia.client.events.EntityHitEvent;
import me.aidanmees.trivia.client.events.EntityInteractEvent;
import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.main.trivia.ErrorState;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.module.state.ModuleState;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;

public class Module {

	protected static Minecraft mc = Minecraft.getMinecraft();
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
	public boolean visible = true;

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

	public void onToggle() {
		toggleTime = 0;
		if (this.toggled) {
			try {
				onEnable();
			} catch (Exception e) {
				trivia.onError(e, ErrorState.onEnable, this);
			}
		} else {
			try {
				onDisable();
			} catch (Exception e) {
				trivia.onError(e, ErrorState.onDisable, this);
			}

		}
	}
	public void onDestroyEntities(S13PacketDestroyEntities packet) {}
	
	public S18PacketEntityTeleport onEntityTeleport(S18PacketEntityTeleport packet)
	  {
	    return packet;
	  }
	  
	  public boolean onRelativeMove(S14PacketEntity packet)
	  {
	    return false;
	  }

	public void onLeftClick() {

	}

	public void onRightClick() {

	}

	public void onEnable() {
		if (trivia.loaded) {
			if(this.category != Category.HIDDEN && ClientSettings.notificationModulesEnable 
					&& trivia.getModuleByName("Notifications").isToggled()) {
				trivia.getNotificationManager().addNotification(new Notification(Level.INFO, this.getName() + " enabled!"));
			}
			trivia.getModuleGroupMananger().disableGroupModsForModule(this);
			
		}
	}

	public void onDisable() {
		if (trivia.loaded) {
			if(this.category != Category.HIDDEN && ClientSettings.notificationModulesDisable 
					&& trivia.getModuleByName("Notifications").isToggled()) {
				trivia.getNotificationManager().addNotification(new Notification(Level.INFO, this.getName() + " disabled!"));
			}
		}
	}

	public void onUpdate() {
		toggleTime++;
	}
	public void onTick() {
		
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

	public void onChatMessageRecieved(S02PacketChat packetIn) {

	}

	public void onPreMotion(PreMotionEvent event) {

	}

	public void onPostMotion() {

	}

	public void onBasicUpdates() {

	}

	public void onPacketRecieved(AbstractPacket packetIn) {

	}

	public void onPacketSent(AbstractPacket packet) {

	}
	

	public void sendPacketFinal(AbstractPacket packet) {
		mc.getNetHandler().getNetworkManager().sendPacketFinal(packet);
	}

	public static void sendPacket(AbstractPacket packet) {
		mc.getNetHandler().getNetworkManager().sendPacket(packet);
	}

	public void onClientLoad() {

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
		if(trivia.loaded) {
			trivia.getNotificationManager().addNotification(new Notification(Level.INFO, 
					this.getName() + " was bound to key: " + Keyboard.getKeyName(keyCode)));
		}
	}

	public void setToggled(boolean toggled, boolean update) {
		this.toggled = toggled;
		if(toggled) {
			
		}
		if (update) {
			try {
				onToggle();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			if (toggled) {
				// Atlas.toggledList.add(this);
			} else {
				// Atlas.toggledList.remove(this);
			}
		}
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setMode(String string) {
		for (String mode : getModes()) {
			if (string.equals(mode)) {
				this.currentMode = mode;
				return;
			}
		}
		System.out.println("Mode: §6" + string + "§7 does not exist switching to default mode...");
		if (this.getModes().length == 0) {
			this.currentMode = "default";
		} else {
			this.currentMode = this.getModes()[0];
		}
		if(trivia.loaded) {
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
		return trivia.getModuleByName(s.getSimpleName());
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
		this.toggled = state.getToggled();
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

	private static final class MethodData {

	    private final Object source;

	    private final Method target;

	    private final byte priority;

	    /**
	     * Sets the values of the data.
	     *
	     * @param source
	     *         The source Object of the data. Used by the VM to
	     *         determine to which object it should send the call to.
	     * @param target
	     *         The targeted Method to which the Event should be send to.
	     * @param priority
	     *         The priority of this Method. Used by the registry to sort
	     *         the data on.
	     */
	    public MethodData(Object source, Method target, byte priority) {
	        this.source = source;
	        this.target = target;
	        this.priority = priority;
	    }

	    /**
	     * Gets the source Object of the data.
	     *
	     * @return Source Object of the targeted Method.
	     */
	    public Object getSource() {
	        return source;
	    }

	    /**
	     * Gets the targeted Method.
	     *
	     * @return The Method that is listening to certain Event calls.
	     */
	    public Method getTarget() {
	        return target;
	    }

	    /**
	     * Gets the priority value of the targeted Method.
	     *
	     * @return The priority value of the targeted Method.
	     */
	    public byte getPriority() {
	        return priority;
	    }

	}




}