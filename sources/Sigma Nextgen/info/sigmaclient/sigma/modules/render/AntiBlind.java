package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.potion.Effect;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class AntiBlind extends Module {
    public AntiBlind() {
        super("AntiBlind", Category.Render, "Now i can see you.");
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()){
            if(mc.player.isPotionActive(Effect.get(15))){
                mc.player.removePotionEffect(Effect.get(15));
            }
        }
        super.onUpdateEvent(event);
    }
}
