package vestige.impl.module.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import vestige.api.event.Listener;
import vestige.api.event.impl.PacketReceiveEvent;
import vestige.api.event.impl.RenderEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.NumberSetting;

@ModuleInfo(name = "Time Changer", category = Category.RENDER)
public class TimeChanger extends Module {
	
	private final NumberSetting customTime = new NumberSetting("Custom time", this, 18000, 0, 24000, 500, true);
	
	public TimeChanger() {
		this.registerSettings(customTime);
	}
	
	@Listener
    public void onRender(RenderEvent e) {
        mc.theWorld.setWorldTime((long) customTime.getCurrentValue());
    }

    @Listener
    public void onReceive(PacketReceiveEvent e) {
        if(e.getPacket() instanceof S03PacketTimeUpdate) {
            e.setCancelled(true);
        }
    }
	
}