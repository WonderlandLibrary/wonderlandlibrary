package Reality.Realii.mods.modules.movement;


import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventCollideWithBlock;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

public class LagBackChecker
extends Module {
	
    public LagBackChecker(){
        super("LagBackChecker", ModuleType.Movement);
    }

    

  

    @EventHandler
    public void onPacket(EventPacketSend e) {
     
        
    }
    }


    

