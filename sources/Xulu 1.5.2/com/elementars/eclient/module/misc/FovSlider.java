// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class FovSlider extends Module
{
    private final Value<Integer> FOV;
    private final Value<String> mode;
    private float fov;
    
    public FovSlider() {
        super("FovSlider", "Better FOV slider", 0, Category.MISC, true);
        this.FOV = this.register(new Value<Integer>("FOV", this, 110, 90, 200));
        this.mode = this.register(new Value<String>("Mode", this, "Fov Changer", new String[] { "Fov Changer", "Hand Changer" }));
    }
    
    @SubscribeEvent
    public void fovOn(final EntityViewRenderEvent.FOVModifier e) {
        if (this.mode.getValue().equalsIgnoreCase("Hand Changer")) {
            e.setFOV((float)this.FOV.getValue());
        }
    }
    
    @Override
    public void onEnable() {
        FovSlider.EVENT_BUS.register((Object)this);
        this.fov = FovSlider.mc.gameSettings.fovSetting;
    }
    
    @Override
    public void onDisable() {
        FovSlider.EVENT_BUS.unregister((Object)this);
        FovSlider.mc.gameSettings.fovSetting = this.fov;
    }
    
    @Override
    public void onUpdate() {
        if (!this.isToggled() || FovSlider.mc.world == null) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("Fov Changer")) {
            FovSlider.mc.gameSettings.fovSetting = this.FOV.getValue();
        }
    }
}
