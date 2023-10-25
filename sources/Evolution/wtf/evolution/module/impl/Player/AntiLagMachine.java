package wtf.evolution.module.impl.Player;

import net.minecraft.world.EnumSkyBlock;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventLight;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "AntiLagMachine", type = Category.Player)
public class AntiLagMachine extends Module {

    @EventTarget
    public void onWorldLight(EventLight event) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.cancel();
            }
    }

}
