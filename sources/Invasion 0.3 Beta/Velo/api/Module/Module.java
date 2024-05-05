package Velo.api.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Velo.api.Event.Event;
import Velo.api.Event.EventManager;
import Velo.api.Main.Main;
import Velo.api.Module.Config.Cfg;
import Velo.api.Util.Other.MathUtils;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.Util.fontRenderer.Utils.Tff;
import Velo.api.setting.Setting;
import Velo.impl.Event.EventChat;
import Velo.impl.Event.EventGetBlockReach;
import Velo.impl.Event.EventKey;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventRender;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventRenderNameTag;
import Velo.impl.Event.EventScoreboard;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventStrafe;
import Velo.impl.Event.EventTick;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Event.EventUpdateModel;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.KeybindSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

public class Module{
	
    @Expose
    @SerializedName("name")
	public String name;
    
	public String displayName;
	
    @Expose
    @SerializedName("toggled")
	public boolean isToggled;
    
	public boolean isOutOfArraylist;
	public float arrayAnimation2;
	public float arrayAnimation;
	public boolean tabGuiExpanded;
	public int mIndex;
	public float transition;
	private Fonts font1;
	public int blue1 = 0;
	public int green1 = 0;
	public int red1 = 0;
	public boolean clickguisettingsexpanded = false;
	public float transition2;
	public KeybindSetting key = new KeybindSetting(0);
	public BooleanSetting hidden = new BooleanSetting("Hidden", false);
	public Category category;
	public boolean ClickGuiExpanded = false;
	public List modulesEnabled = new ArrayList<>();
	public Minecraft mc = Minecraft.getMinecraft();

	
  
    public int keyCode = -1;
    
    @Expose
    @SerializedName("keyCode")
    public int keyCodee = key.code;
    
    @Expose
    @SerializedName("settings")
    public Cfg[] cfgSettings;
	
    
	public float optionAnim = 0;// present
	public float optionAnimNow = 0;// present
	
	
	public List<Setting> settings = new ArrayList<Setting>();
	
	public Module(String modulename, String displayName, int keybind, Category category) {
		this.name = modulename;
		this.displayName = displayName;
		this.key.code = keybind;
		this.category = category;
		this.blue1 = MathUtils.randomInt(1, 175);
		this.green1 = MathUtils.randomInt(1, 175);
		this.red1 = MathUtils.randomInt(1, 175);
		this.loadSettings(key, hidden);
	}
	
	
	

	public void setEnabled(boolean enabled) {
		this.isToggled = enabled;

	}
	public void setToggled(boolean enabled) {
		this.isToggled = enabled;
	}
	
	public void loadSettings(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
		this.settings.sort(Comparator.comparingInt(s -> s == key ? 1 : 0));
	}
	
	
	  public List<Setting> getSettings() {
	        return settings;
	    }
	
	public static int count = 0;
	public void toggle() {
		this.isToggled = !this.isToggled;
		if(isToggled) {
			this.isOutOfArraylist = true;
			this.arrayAnimation2 = 1;
			
			this.arrayAnimation = font1.Hud.getStringWidth(displayName);
			this.transition = 0;
			this.transition2 = 1;
		
	
			count++;
			
			this.onEnable();
		} else {	
		
				count--;
			
			this.onDisable();
		}
	}
	
	public float getTransition() {
		return transition;
	}

    public void setTransition(final int transition) {
        this.transition = transition;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isEnabled() {
		return isToggled;
	}
	public boolean isToggled() {
		return isToggled;
	}
	
	public int getKeybind() {
		return key.code;
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onPreMotionUpdate(EventPreMotion event) {
		
	}
	
	
	public void onEventGetReach(EventGetBlockReach event) {
		
	}
	
	public void onTick(EventTick event) {
		
	}
	
	
	public void onRender3DUpdate(EventRender3D event) {
		
	}
	
	public void onUpdate(EventUpdate event) {
		
	}
	
public void onRenderNametag(EventRenderNameTag event) {
		
	}
	
	public void onRenderUpdate(EventRender event) {
		
	}
	
	public void onEventReceivePacket(EventReceivePacket event) {
		
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		
	}
	
	public void onEventChatMessage(EventChat event) {
		
	}
	
	public void onPostMotionUpdate(EventPostMotion event) {
		
	}
	
	public void onStrafe(EventStrafe event) {
		
	}
	
	public void onEventScoreboard(EventScoreboard event) {
		
	}
	
	public void onMovementUpdate(EventMovement event) {
	
		
	}
	
	public void onKeypress(EventKey event) {
		
	}
	
	public enum Category {
		COMBAT("Combat"),
		MOVEMENT("Movement"),
		PLAYER("Player"),
		EXPLOITS("Exploits"),
		OTHER("Other"),
		VISUALS("Visuals");
		
		public String name;
		public int mIndex;
		public boolean isExpanded;
		public boolean settingsExpand;
		public float x, y;
		public int maxLength = 0;
		public float expandStuffxd;
		
		
		Category(String name) {
			this.name = name;
		}
	}

	public void onModelUpdate(EventUpdateModel event) {
	
		
	}

}
