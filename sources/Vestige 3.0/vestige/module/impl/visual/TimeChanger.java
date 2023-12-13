package vestige.module.impl.visual;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import vestige.event.Listener;
import vestige.event.impl.PacketReceiveEvent;
import vestige.event.impl.RenderEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.DoubleSetting;

public class TimeChanger extends Module {

    private final DoubleSetting customTime = new DoubleSetting("Custom time", 18000, 0, 24000, 500);

    public TimeChanger() {
        super("Time Changer", Category.VISUAL);
        this.addSettings(customTime);
    }

    @Listener
    public void onRender(RenderEvent event) {
        mc.theWorld.setWorldTime((long) customTime.getValue());
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

}
