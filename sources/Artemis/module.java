package artemis;

import java.util.Iterator;

import artemis.events.Event;
import artemis.logger.Logger;
import artemis.notification.Notification;
import artemis.notification.NotificationManager;
import artemis.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S02PacketChat;

public class module {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected String name, hudname;
	private String discription;
	private int key, id;
	
	public int size = 0;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private boolean toggled;
	private Category category;
	private boolean isEnabled;
	public boolean visible = true;
	
	public module(String nm, String hudnm, String discription, int k, int id, Category c){
		name = nm;
		hudname = hudnm;
		this.discription = discription;
		key = k;
		this.id = id;
		category = c;
		toggled = false;
		setup();
	}
	
	public String getDiscription() {
		return discription;
	}
	public static module getModule(String name) {
		for (module m : Artemis.getModules()) {
			System.out.println("getModule works!!!");
			if (m.getName() == name) {
				System.out.println("getModule works even more!!!");
				System.out.println(m);
				return m;
			}
		}
		return null;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public void toggle(){
		
		toggled = !toggled;
		if(toggled){
			onEnable();
			size = 0;
		}else{
			onDisable();
			size = 0;
		}
		if (Artemis.instance.saveLoad != null) {
			Artemis.instance.saveLoad.save();
		}
	}
	
	public void onEnable(){
		 Logger.instance.log(hudname + " was enabled", Logger.LogType.NORMAL);
		NotificationManager.show(new Notification(NotificationType.ENABLE, hudname, hudname + " was enabled", 2));
	}
	public void onDisable(){
		 Logger.instance.log(hudname + " was disabled", Logger.LogType.NORMAL);
		NotificationManager.show(new Notification(NotificationType.DISABLE, hudname, hudname + " was disabled", 2));
	}
	public void onUpdate(){}
	public void onAttack(){}
	public void onRender(){}
	public void onEvent(Event e){
		
	}


	public String getName() {
		return this.name;
	}

	public String getHudname() {
		return hudname;
	}

	public void setHudname(String hudname) {
		this.hudname = hudname;
	}

	public void setName(String name) {
		
		this.name = name;
	}
	
	public Category getCategory(){
		return category;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
		if (Artemis.instance.saveLoad != null) {
			Artemis.instance.saveLoad.save();
		}
	}

	public boolean isToggled() {
		return toggled;
	}

	
	/*
	 * 
    public void setToggled(boolean toggledTo) {
        if(toggledTo) {
            if(getState()) {
                return;
            }
            toggle();
            return;
        } else {
            if(getState()) {
                toggle();
            }
            return;
        }
    }
    public void setToggled(boolean toggled) {
		this.toggled = toggled;
		
		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
		if (Artemis.instance.saveLoad != null) {
			Artemis.instance.saveLoad.save();
		}
	}
	 */
	
	public void setToggled(boolean toggledTo) {
        if(toggledTo) {
            if(isToggled()) {
                return;
            }
            toggle();
            return;
        } else {
            if(isToggled()) {
                toggle();
            }
            return;
        }
    }

	public final boolean isCategory(Category s) {
		if (s == category)
			return true;
		return false;
	}
	public boolean getState() {
		return isToggled();
	}\
	
	public void setState(boolean state) {
		this.onEnable();
		if (state) {
			this.onEnable();
			this.isEnabled = true;
		} else {
			this.onDisable();
			this.isEnabled = false;
		}
	}
	
	public boolean onSendChatMessage(String s){
		return true;
	}
	
	public boolean onRecieveChatMessage(S02PacketChat packet){
		return true;
	}

	public void setup() {
		
		
	}

	public String getMode() {
		// TODO Auto-generated method stub
		return "Vanilla";
	}

	public String getOther() {
		// TODO Auto-generated method stub
		return "N/A";
	}
	
	
	

}
