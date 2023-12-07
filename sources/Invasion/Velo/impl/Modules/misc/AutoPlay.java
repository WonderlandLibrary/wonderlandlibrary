package Velo.impl.Modules.misc;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class AutoPlay extends Module {
	
	
    private int clickState=0;
    private BooleanSetting silentValue = new BooleanSetting("Silent", true);
    		private NumberSetting delayValue = new NumberSetting("JoinDelay", 3, 0, 7, 1);
    private boolean clicking= false;
    public Timer t = new Timer();
    
	public AutoPlay() {
		super("AutoPlay", "AutoPlay", Keyboard.KEY_NONE, Category.OTHER);
	}
	
	public void onEnable() {
		  clickState=0;
		  clicking=false;
	}
	
	public void onDisable() {
		
	}
	@Override
	public void onUpdate(EventUpdate event) {
		if(clicking = true && clickState == 2) {
		clicking=false;
		        clickState=0;
		}
		super.onUpdate(event);
	}
	
@Override
public void onEventReceivePacket(EventReceivePacket e) {
    Packet packet = e.packet;
            if(clicking&&(packet instanceof C0EPacketClickWindow|| packet instanceof C07PacketPlayerDigging)){
                e.setCancelled(true);
                
            }
            
            if (packet instanceof S2FPacketSetSlot) {
            	packet = new S2FPacketSetSlot();
                ItemStack item=((S2FPacketSetSlot)packet).func_149174_e();
                int windowId=((S2FPacketSetSlot)packet).func_149175_c();
                int slot=((S2FPacketSetSlot)packet).func_149173_d();
                String itemName=item.getUnlocalizedName();
                //do check
                if(clickState==0 && windowId==0 && slot==42 && itemName.contains("paper") && item.getDisplayName().contains("Jogar novamente")){
       
                    clickState=1;
                    clicking=true;
               
if(t.elapsed((long) (delayValue.getValue()*1000L))) {
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(item));
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                            clickState=2;
                        }
                    }        
                else if(clickState==2 && windowId!=0 && slot==11 && itemName.contains("enderPearl")){
                  if(t.elapsed(500l)) {
                            clicking=false;
                            clickState=0;
                            mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(windowId, slot, 0, 0, item, (short)1919));
                        }
                    }
                }
            
            if(silentValue.isEnabled() && clickState==2 && packet instanceof S2DPacketOpenWindow){
                e.setCancelled(true);
            }
            
            
	super.onEventReceivePacket(e);
}
}
