package Velo.api.Event;

import org.lwjgl.input.Keyboard;

import Velo.api.Main.DiscordRP;
import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
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
import net.arikia.dev.drpc.DiscordRPC;

public class EventManager {
	
	public static void onKeyPress(int keybind) {
		for(Module m : ModuleManager.modules) {
			if(m.key.code == keybind) {
				m.toggle();
			}
		}
	}
	
	public static void onPreMotionUpdate(EventPreMotion event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onPreMotionUpdate(event);
		}
	}
	
	public static void onModelUpdate(EventUpdateModel event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onModelUpdate(event);
		}
	}
	
	
	public static void onRenderNametag(EventRenderNameTag event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onRenderNametag(event);
		}
	}

	
	public static void onTick(EventTick event) {

		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onTick(event);
		}
	}
	
	
	
	
	
	public static void onPostMotionUpdate(EventPostMotion event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onPostMotionUpdate(event);
		}
	}
	
	public static void onStrafe(EventStrafe event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onStrafe(event);
		}
	}
	
	public static void onEventReceivePacket(EventReceivePacket event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onEventReceivePacket(event);
		}
	}
	
	public static void onEventGetReach(EventGetBlockReach event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onEventGetReach(event);
		}
	}
	
	public static void onUpdate(EventUpdate event) {
	
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onUpdate(event);
		}
	}
	
	public static void onMovementUpdate(EventMovement event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onMovementUpdate(event);
		}
	}
	
	public static void onRender3DUpdate(EventRender3D event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onRender3DUpdate(event);
		}
	}
	
	public static void onEventSendPacket(EventSendPacket event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onEventSendPacket(event);
		}
	}
	
	public static void onEventScoreboard(EventScoreboard event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onEventScoreboard(event);
		}
	}
	
	public static void onEventChatMessage(EventChat event) {
		if(event instanceof EventChat) {
			Main.commandManager.onChat(event);
		}
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onEventChatMessage(event);
		}
	}
	
	public static void onRenderUpdate(EventRender event) {

		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onRenderUpdate(event);
		}
	}
	public static void onKeypress(EventKey event) {
		for(Module m : ModuleManager.modules) {
			if(!m.isToggled)
				continue;
			
			m.onKeypress(event);
		}
	}
}
