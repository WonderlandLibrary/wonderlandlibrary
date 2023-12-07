package Velo.impl.Modules.player;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AutoGapple extends Module {
	
	private Timer t = new Timer();
	
	public static NumberSetting health = new NumberSetting("Health", 10, 4, 20, 1);
	public AutoGapple() {
		super("AutoGapple", "AutoGapple", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
    private boolean doEat(){
     
        
            if(mc.thePlayer.getHealth() <= health.getValue() ){
            	   return true;
            }
             return false;
    }
	

	
	
}
