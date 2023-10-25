package wtf.evolution.module.impl.Render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "FullBright", type = Category.Render)
public class FullBright extends Module {

    @EventTarget
    public void onUpdate(EventMotion e) {
        mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 20 * (780 + 37)));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null)
            mc.player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
    }


}
