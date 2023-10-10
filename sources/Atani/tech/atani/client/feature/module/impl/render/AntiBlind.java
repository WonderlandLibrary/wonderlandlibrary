package tech.atani.client.feature.module.impl.render;

import net.minecraft.potion.Potion;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

// Hooked in Entity class
@ModuleData(name = "AntiBlind", description = "Removes bad effects from the player.", category = Category.RENDER)
public class AntiBlind extends Module {

    @Listen
    public void onUpdateEvent(UpdateEvent updateEvent) {
        if (mc.thePlayer.getActivePotionEffect(Potion.blindness) != null) {
            mc.thePlayer.removePotionEffect(Potion.blindness.getId());
        }

        if (mc.thePlayer.getActivePotionEffect(Potion.confusion) != null) {
            mc.thePlayer.removePotionEffect(Potion.confusion.getId());
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}