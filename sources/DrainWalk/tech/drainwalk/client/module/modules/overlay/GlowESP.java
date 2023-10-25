package tech.drainwalk.client.module.modules.overlay;

import net.minecraft.entity.player.EntityPlayer;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.events.EventTarget;
import tech.drainwalk.events.UpdateEvent;

public class GlowESP extends Module {
    public GlowESP() {
        super("GlowESP", Category.OVERLAY);
        addType(Type.MAIN);
    }


    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        for (EntityPlayer playerEntity : mc.world.playerEntities) {
            if (playerEntity != mc.player ) {
                playerEntity.setGlowing(true);
            }
        }
    }

    @EventTarget
    public void onDisable(){
        for (EntityPlayer playerEntity : mc.world.playerEntities){
            if (playerEntity != mc.player){
                playerEntity.setGlowing(false);
            }
        }
    }
}
