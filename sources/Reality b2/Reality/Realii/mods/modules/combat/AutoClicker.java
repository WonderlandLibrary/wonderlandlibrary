
package Reality.Realii.mods.modules.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Random;

import org.lwjgl.input.Mouse;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.events.world.EventTick;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;

public class AutoClicker
extends Module {
	 public static EntityLivingBase target;
	private TimerUtil cpsTimer = new TimerUtil();
	private Option Blockhit = new Option("Blockhit", false);
    public AutoClicker(){
        super("AutoClicker", ModuleType.Combat);
        addValues(Blockhit);
    }
    
    
   

    @EventHandler
    private void onUpdate(EventTick event) {
    	 if ((boolean) Blockhit.getValue()) {
    		 Random random = new Random();
             int min13 = 1; 
            int max13 = 5; 
            //or 4
             int randomNumbercps = random.nextInt(max13 - min13 + 1) + min13;
             
          
            
             if (Mouse.isButtonDown(0)) {
           	  
           	  if (mc.thePlayer.ticksExisted % randomNumbercps == 0) {
            	 
            	 PlayerUtils.sendClick(0, true);
           	  }
             }
    	 } else {
    	 Random random = new Random();
         int min13 = 1; 
        int max13 = 4; 
        //or 4
         int randomNumbercps = random.nextInt(max13 - min13 + 1) + min13;
         
      
        
       if (Mouse.isButtonDown(0)) {
         //  if (mc.gameSettings.keyBindAttack.isKeyDown()) {
       	 // if(mc.thePlayer.ticksExisted == 0) {
       	  if (mc.thePlayer.ticksExisted % randomNumbercps == 0) {
        	 
        	 PlayerUtils.sendClick(0, true);
       	  }
         }
       
    	  
    }
    }
}

