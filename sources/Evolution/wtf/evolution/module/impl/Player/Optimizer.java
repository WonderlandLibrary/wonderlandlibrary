package wtf.evolution.module.impl.Player;

import net.minecraft.world.EnumSkyBlock;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventLight;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
@ModuleInfo(name = "Optimizer", type = Category.Player)
public class Optimizer extends Module {

    public BooleanSetting light = new BooleanSetting("Light", true).call(this);
    public BooleanSetting entities = new BooleanSetting("Entities", true).call(this);

    @EventTarget
    public void onWorldLight(EventLight event) {
        if (light.get()) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.cancel();
            }
            if (event.getEnumSkyBlock() == EnumSkyBlock.BLOCK) {
                event.cancel();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

    }

}
