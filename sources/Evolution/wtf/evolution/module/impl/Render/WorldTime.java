package wtf.evolution.module.impl.Render;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "WorldTime", type = Category.Render)
public class WorldTime extends Module {

    public SliderSetting time = new SliderSetting("Time", 1000, 0, 24000, 100).call(this);

    @EventTarget
    public void onUpdate(EventUpdate e) {
        setSuffix(String.valueOf(time.get()));
        mc.world.setWorldTime((long) time.get());
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof SPacketTimeUpdate) {
            e.cancel();;
        }
    }

}
