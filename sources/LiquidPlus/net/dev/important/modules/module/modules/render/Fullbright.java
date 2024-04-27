/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 */
package net.dev.important.modules.module.modules.render;

import net.dev.important.Client;
import net.dev.important.event.ClientShutdownEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.render.XRay;
import net.dev.important.value.ListValue;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@Info(name="Fullbright", description="Brightens up the world around you.", category=Category.RENDER, cnName="\u591c\u89c6")
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
        if (this.getState() || Client.moduleManager.getModule(XRay.class).getState()) {
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

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

