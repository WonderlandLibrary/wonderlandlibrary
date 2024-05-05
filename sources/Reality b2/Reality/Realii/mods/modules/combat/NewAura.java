
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

public class NewAura
extends Module {
	

	
    public NewAura(){
        super("NewAura", ModuleType.Combat);
    
      
        
       
    }
    
    
  
   

    @EventHandler
    private void onPacket(EventPacketSend event) {
    	 
    	 
    	
    	
    }
    
    
    @EventHandler
    private void onRecieve(EventPacketRecieve event) {
    	
    	
    }
}

