package tech.atani.client.feature.module.impl.render;

import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

@ModuleData(name = "FullBright", description = "Increases brightness to the maximum", category = Category.RENDER)
public class FullBright extends Module {
    private float oldGamma;

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        mc.gameSettings.gammaSetting = 100F;
    }

    @Override
    public void onEnable() {
        this.oldGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = this.oldGamma;
    }

}