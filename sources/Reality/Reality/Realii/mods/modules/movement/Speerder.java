package Reality.Realii.mods.modules.movement;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S2BPacketChangeGameState;

public class Speerder extends Module{

	public Speerder(){
		super("Speerder", ModuleType.Movement);
	}

    @Override
    public void onEnable() {
        super.onEnable();

    }

    @Override
    public void onDisable() {
        super.onDisable();
    	
    }
    

    @EventHandler
    private void onPacketSend(EventPacketSend e) {
    	   if (e.getPacket() instanceof S07PacketRespawn || e.getPacket() instanceof S2BPacketChangeGameState || e.getPacket() instanceof C0FPacketConfirmTransaction) {
    		   e.setCancelled(true);
    	   }
    }
    
    


   

    

    
   
    
    

}
