
package Reality.Realii.mods.modules.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import Reality.Realii.utils.cheats.world.TimerUtil;
import net.minecraft.util.ResourceLocation;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;




public class Ambiance
        extends Module {
	  private Numbers<Number> time = new Numbers<>("time", 0, 0, 22999, 1);
	
	
    public Ambiance() {
        super("Ambiance", ModuleType.Render);
        addValues(time);
    }
    

    private final TimerUtil timer2 = new TimerUtil();
    private float timeIncrease;
    @EventHandler
    private void onPacketReceive(EventPacketRecieve e) {
    	 if (e.getPacket() instanceof S03PacketTimeUpdate)
             e.setCancelled(true);
    }
   

    @EventHandler
    public void onRender(EventRender3D event) {
    	 mc.theWorld.setWorldTime((long) ((time.getValue().longValue())));
    }
}

