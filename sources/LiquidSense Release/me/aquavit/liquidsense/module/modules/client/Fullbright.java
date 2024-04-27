package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.ClientShutdownEvent;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.render.XRay;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "Fullbright", description = "Brightens up the world around you.", category = ModuleCategory.CLIENT)
public class Fullbright extends Module {

    private final ListValue modeValue = new ListValue("Mode", new String[] {"Gamma", "NightVision"}, "Gamma");

    private float prevGamma = -1;

    @Override
    public void onEnable() {
        prevGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        if(prevGamma == -1)
            return;

        mc.gameSettings.gammaSetting = prevGamma;
        prevGamma = -1;
        if(mc.thePlayer != null) mc.thePlayer.removePotionEffectClient(Potion.nightVision.id);
    }

    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (getState() || LiquidSense.moduleManager.getModule(XRay.class).getState()) {
            switch(modeValue.get().toLowerCase()) {
                case "gamma":
                    if(mc.gameSettings.gammaSetting <= 100F)
                        mc.gameSettings.gammaSetting++;
                    break;
                case "nightvision":
                    mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1337, 1));
                    break;
            }
        }else if(prevGamma != -1) {
            mc.gameSettings.gammaSetting = prevGamma;
            prevGamma = -1;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onShutdown(final ClientShutdownEvent event) {
        onDisable();
    }
}
