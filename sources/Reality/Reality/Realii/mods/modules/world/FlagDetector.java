package Reality.Realii.mods.modules.world;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;

public class FlagDetector extends Reality.Realii.mods.Module {



    public FlagDetector() {
        super("FlagDetector", ModuleType.World);

    }
    public int FlagCount = 0;



    @EventHandler
    public void onPacket(EventPacketRecieve e) {
        if(e.getPacket() instanceof S08PacketPlayerPosLook) {
            FlagCount++;
            Helper.sendMessage("Flag " + FlagCount);
        }


    }
    
}

 
    

