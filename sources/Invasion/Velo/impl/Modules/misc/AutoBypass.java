package Velo.impl.Modules.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import Velo.api.Module.Module;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class AutoBypass extends Module {
	
	
    private String skull=null;
    private String type="none";
    private List<Packet<INetHandlerPlayServer>> packets= (List<Packet<INetHandlerPlayServer>>) new ArrayList<Packet<INetHandlerPlayServer>>();
    private List clickedSlot=new ArrayList<Integer>();
    private JsonParser jsonParser= new JsonParser();
    public Timer t = new Timer();
    private List<String> brLangMap = new ArrayList<String>();
    		
	public AutoBypass() {
		super("AuthBypass", "AuthBypass", Keyboard.KEY_NONE, Category.OTHER);
	}
	
	@Override
	public void onPreMotionUpdate(EventPreMotion event) {
	       if(!packets.isEmpty() && t.elapsed(1500l)){
	            for(Packet packet : packets){
	                mc.getNetHandler().addToSendQueue(packet);
	            }
	       }
	       packets.clear();
		super.onPreMotionUpdate(event);
	}
	
	public void onEnable() {
		        skull=null;
			        type="none";
			        packets.clear();
			        clickedSlot.clear();
			    	Thread task1 = new Thread();
				
			        new Thread(new Runnable() {
						
						@Override
						public void run() {
							 try {
								JsonObject localeJson= new JsonParser().parse(IOUtils.toString(AutoBypass.class.getResourceAsStream("br_items.json"),"utf-8")).getAsJsonObject();
							} catch (JsonSyntaxException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					            brLangMap.clear();
					            
					            //    brLangMap["item.$key"] = localeJson.toString().toLowerCase();
					            
					        }
			        }).start();
							
						
					
			           
	
			        
	}
	
	public void onDisable() {
		
	}

	
    private void click(int windowId,int slot,ItemStack item){
        clickedSlot.add(slot);
        packets.add(new C0EPacketClickWindow(windowId,slot,0,0,item, (short) RandomUtils.nextInt(114,514)));
}

  
    
}
