
package Reality.Realii.mods.modules.combat;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import Reality.Realii.utils.cheats.world.TimerUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Mouse;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.events.world.EventTick;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;

public class TickBase
extends Module {
	  
	 private  TimerUtil timeHelper;
	    private  TimerUtil timeHelper2;
	    private  ArrayList<Integer> diffs;
	    public long balanceCounter;
	    private long lastTime;
	    private WorldClient lastWorld;

	
    public TickBase(){
        super("TickBase", ModuleType.Combat);
        this.timeHelper = new TimerUtil();
        this.timeHelper2 = new TimerUtil();
        this.diffs = new ArrayList<Integer>();
        this.balanceCounter = 0L;
        this.lastWorld = null;
      
        
       
    }
    
    
  
   

    @EventHandler
    private void onPacket(EventPacketSend event) {
    	  final Packet packet = event.getPacket();
          if (packet instanceof C03PacketPlayer) {
              if (this.lastWorld != null && this.lastWorld != mc.theWorld) {
                  this.balanceCounter = 0L;
                  this.diffs.clear();
              }
              if (this.balanceCounter > 0L) {
                  --this.balanceCounter;
              }
              final long diff = System.currentTimeMillis() - this.lastTime;
              this.diffs.add((int)diff);
              this.balanceCounter += (diff - 50L) * -3L;
              this.lastTime = System.currentTimeMillis();
              if (this.balanceCounter > 150L) {}
              this.lastWorld = mc.theWorld;
          }
    	 
    	
    	
    }
    
    
    @EventHandler
    private void onRecieve(EventPacketRecieve event) {
    	 final Packet packet = event.getPacket();
         if (packet instanceof S08PacketPlayerPosLook) {
             this.balanceCounter -= 100L;
         }
    	
    }
}

