package epsilon.modules.render;

import java.util.List;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventKey;
import epsilon.events.listeners.EventRenderGUI;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.Setting;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.KeybindSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.ColorUtil;
import epsilon.util.ESP.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Module{
	
	
	public ESP(){
		super("ESP", Keyboard.KEY_NONE, Category.RENDER, "Doesnt do anything yet");
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventRenderGUI) {
			if(mc.theWorld!=null) {
				for (Object ento: mc.theWorld.loadedEntityList) {
					
					if(ento instanceof EntityPlayer) {
						EntityPlayer t = (EntityPlayer) ento;
						if(!t.isDead && t!=mc.thePlayer) {
							RenderUtils.drawEntityESP(index, arrayListAnimation, sizeInGui, arrayListAnimation, arrayListAnimation, index, index, index, index, index, index, index, sizeInGui, index);
						}
					}
				}
			}	
		}
	}
}