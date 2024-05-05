// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.potion.PotionEffect;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import net.minecraft.potion.Potion;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class FullBright extends Module
{
    public ModeSetting mode;
    
    public FullBright() {
        super("FullBright", 0, "Renders the world at full brightness", Category.RENDER);
        this.addSetting(this.mode = new ModeSetting("Mode", "Gamma", new String[] { "Gamma", "Potion" }));
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (this.mode.is("Gamma")) {
            FullBright.mc.gameSettings.gammaSetting = 1.0f;
        }
        if (this.mode.is("Potion") && FullBright.mc.thePlayer.isPotionActive(Potion.nightVision)) {
            FullBright.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
        }
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getSelected());
        if (e instanceof EventMotion) {
            if (this.mode.is("Gamma")) {
                if (FullBright.mc.gameSettings.gammaSetting == 1.0f || FullBright.mc.gameSettings.gammaSetting < 1.0f) {
                    FullBright.mc.gameSettings.gammaSetting = 100.0f;
                }
                if (FullBright.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                    FullBright.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
                }
            }
            if (this.mode.is("Potion")) {
                if (FullBright.mc.gameSettings.gammaSetting > 1.0f) {
                    FullBright.mc.gameSettings.gammaSetting = 1.0f;
                }
                FullBright.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
            }
        }
    }
}
