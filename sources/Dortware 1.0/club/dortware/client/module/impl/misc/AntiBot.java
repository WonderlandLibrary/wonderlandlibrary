package club.dortware.client.module.impl.misc;

import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.player.EntityPlayer;

@ModuleData(name = "Anti Bot", category = ModuleCategory.MISC)
public class AntiBot extends Module {

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
            if (!Float.isNaN(entityPlayer.getHealth())) {
                entityPlayer.setValid(false);
            }
        }
    }

    @Override
    public void onDisable() {
        for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
            entityPlayer.setValid(true);
        }
    }
}
