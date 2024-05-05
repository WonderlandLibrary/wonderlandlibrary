/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.ClientShutdownEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name="Fullbright", description="Brightens up the world around you.", category=ModuleCategory.RENDER)
public class Fullbright
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Gamma", "NightVision"}, "Gamma");
    private float prevGamma = -1.0f;

    @Override
    public void onEnable() {
        this.prevGamma = Fullbright.mc.field_71474_y.field_74333_Y;
    }

    @Override
    public void onDisable() {
        if (this.prevGamma == -1.0f) {
            return;
        }
        Fullbright.mc.field_71474_y.field_74333_Y = this.prevGamma;
        this.prevGamma = -1.0f;
        if (Fullbright.mc.field_71439_g != null) {
            Fullbright.mc.field_71439_g.func_70618_n(Potion.field_76439_r.field_76415_H);
        }
    }

    @EventTarget(ignoreCondition=true)
    public void onUpdate(UpdateEvent event) {
        if (this.getState() || LiquidBounce.moduleManager.getModule(XRay.class).getState()) {
            switch (((String)this.modeValue.get()).toLowerCase()) {
                case "gamma": {
                    if (!(Fullbright.mc.field_71474_y.field_74333_Y <= 100.0f)) break;
                    Fullbright.mc.field_71474_y.field_74333_Y += 1.0f;
                    break;
                }
                case "nightvision": {
                    Fullbright.mc.field_71439_g.func_70690_d(new PotionEffect(Potion.field_76439_r.field_76415_H, 1337, 1));
                }
            }
        } else if (this.prevGamma != -1.0f) {
            Fullbright.mc.field_71474_y.field_74333_Y = this.prevGamma;
            this.prevGamma = -1.0f;
        }
    }

    @EventTarget(ignoreCondition=true)
    public void onShutdown(ClientShutdownEvent event) {
        this.onDisable();
    }
}

