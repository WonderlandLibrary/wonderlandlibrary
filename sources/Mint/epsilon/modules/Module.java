package epsilon.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.settings.Setting;
import epsilon.settings.setting.KeybindSetting;
import epsilon.settings.setting.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class Module {
	
	public String name;
	public String displayName = "";
	public String displayInfo = "";
	public String info;
	public String description;
	public boolean hovered;
	public KeybindSetting keyCode = new KeybindSetting(0);
	public Category category;
	public static Minecraft mc = Minecraft.getMinecraft();
	public boolean expanded;
	public boolean settingsExpanded;
	public int index;
	public transient double arrayListAnimation = 0;
	private boolean cancelled;
	private String lastGetModuleName;
	private Module lastGetModule;
	public boolean toggled;
	public List<Setting> settings = new ArrayList<Setting>();
	public float sizeInGui;
	

	public Module(String name, int key, Category c, String description){
		this.name = name;
		this.description = description;
		keyCode.code = key;
		this.category = c;
		this.addSettings(keyCode);
		toggled = false;
		setup();
		
	}
	
	public void sendPacket(Packet p, boolean silent) {
		if(silent)
			mc.getNetHandler().sendPacketNoEvent(p);
		else
			mc.getNetHandler().addSilentToSendQueue(p);
	}
	
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void addSettings(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
		this.settings.sort(Comparator.comparingInt(s -> s == keyCode ? 1 : 0));
	}
	
	public void setDisplay(String display) {
		this.displayInfo = display;
	}
	
	public void onEnable(){}
	public void onDisable(){}
	public void onRender(){}
	public void setUp(){}
	public void setup(){}
	public void onUpdate(){}
	
	public boolean isEnabled(){
		return toggled;
	}
	
	public boolean isToggled() {
		return toggled;
	}
	
	public int getKey(){
		return keyCode.code;
	}
	
	public void onEvent(Event e){
		
	}
	
	public List<Setting> getSettings() {
		return settings;
	}
	
	
	public void toggle(){
		toggled = !toggled;
		if(toggled){
			onEnable();
		}else{
			onDisable();
		}
	}
	
	
	public Category getCategory() {
		return category;
	}
	
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public static int getCategoryAsInt(Category category) {
		if(category == Category.COMBAT) 
			return 0;
		
		else if(category == Category.MOVEMENT) 
			return 1;
		
		else if(category == Category.PLAYER) 
			return 2;
		
		else if(category == Category.EXPLOIT) 
			return 3;
		
		
		else if(category == Category.RENDER) 
			return 4;
		
		else if (category == Category.DEV)
			return 5;
		
		return 0;
		
	
	}
	
	
	
	public enum Category {
		COMBAT("Combat"),
		MOVEMENT("Movement"),
		PLAYER("Player"),
		EXPLOIT("Exploit"),
		RENDER("Render"), 
		DEV("Dev");				;
		
		public String name;
		public int moduleIndex;
		public boolean canExpand;
		
		Category(String name){
			this.name = name;
		}
		
	}



	public void toggleExpansion() {
		settingsExpanded = !settingsExpanded;
	}
	
	public void setExpanded(boolean aBool) {
		settingsExpanded = aBool;
	}
	
	public boolean getExpanded() {
		return settingsExpanded;
	}

	public void onPacketSend(Packet p) {
		
	}
	
	public void onPacketRecieve(Packet p) {
		
	}

	
}
